package com.jcpt.jzg.padsystem.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarPhotoBaseAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 基本照片
 * @author zealjiang
 * @time 2016/11/15 11:07
 */
public class CarPhotoBaseFragment extends BaseFragment{


    @BindView(R.id.rv)
    public  RecyclerView recyclerview;

    private Context context;
    private GridLayoutManager gridLayoutManager;
    private CarPhotoBaseAdapter adapter;

    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItems;
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private int curPicPos = 0;

    LocalDetectionData mDetectionWrapper;


    public CarPhotoBaseFragment() {
        pictureItems = new ArrayList<>();
    }

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_photo_procedure, container, false);
        ButterKnife.bind(this, view);
        context = this.getContext();
        return view;
    }

    @Override
    protected void initData() {

    }

    //设置基本照片
    private void setBasePhoto(){
        for(int i = 0;i<pictureItems.size();i++){
            String path = PadSysApp.picDirPath+pictureItems.get(i).getPicId()+".jpg";
            if(FileUtils.isFileExists(path)){
                pictureItems.get(i).setPicPath(path);
            }
        }

        //重写设置mDetectionWrapper中PictureList
        if(pictureItems!=null&&pictureItems.size()>0){
            if (mDetectionWrapper != null) {
                List<PictureItem> getPictureList = mDetectionWrapper.getPictureList();
                if(getPictureList != null){
                    for (int k = 0; k < getPictureList.size(); k++) {
                        PictureItem pictureItem= getPictureList.get(k);
                        for (int i = 0; i < pictureItems.size(); i++) {
                            if(pictureItems.get(i).getPicId().equals(pictureItem.getPicId())){
                                pictureItem.setPicPath(pictureItems.get(i).getPicPath());
                                break;
                            }
                        }
                    }
                }
            }
        }

    }


    private void initDatapictureItems(){
        pictureItems.clear();
        mDetectionWrapper = ((DetectMainActivity)getActivity()).getLocalDetectionData();
        //读取详情返回网络图片
        if (mDetectionWrapper != null) {
            List<PictureItem> getPictureList = mDetectionWrapper.getPictureList();
            if(getPictureList != null){
                for (int k = 0; k < getPictureList.size(); k++) {
                    PictureItem pictureItem= getPictureList.get(k);
                    pictureItems.add(new PictureItem(pictureItem.getPicId(),pictureItem.getPicName(),pictureItem.getPicPath(),pictureItem.getPicOrder()));
                }
            }
        }

        Collections.sort(pictureItems);
    }

    public void init(){
        initDatapictureItems();
        setBasePhoto();

        gridLayoutManager = new GridLayoutManager(PadSysApp.getAppContext(),4);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new CarPhotoBaseAdapter(PadSysApp.getAppContext(),pictureItems);
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new CarPhotoBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {
                curPicPos = position;
                clickPic();
            }
        });
    }



    public void clickPic(){
        String picPath = pictureItems.get(curPicPos).getPicPath();

        if(TextUtils.isEmpty(picPath)){
            //拍照
            userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE);
        }else{
            //查看大图
            Intent intent = new Intent(context,PictureZoomActivity.class);

            intent.putExtra("pictureItems",pictureItems);
            intent.putExtra("curPosition",curPicPos);
            intent.putExtra("url",picPath);
            intent.putExtra("showRecapture",true);
            intent.putExtra("taskid",((DetectMainActivity)getActivity()).getTaskid());

            startActivityForResult(intent,PHOTO_BIG_PHOTO);
        }
    }



    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position,int capture_type){
        Intent intent = new Intent(context,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId",((DetectMainActivity)getActivity()).getTaskid());
        intent.putExtra(Constants.CAPTURE_TYPE,capture_type);//拍摄模式，是单拍还是连拍

        ArrayList<PictureItem> singleList = new ArrayList<>();
        singleList.add(pictureItems.get(position));
        intent.putExtra("picList",singleList);
        intent.putExtra("position",0);

        startActivityForResult(intent,PHOTO_REQUEST);
    }

    /**
     * 照片是否上传完全
     * @return
     */
    public boolean isFinish(){
        boolean isfinish = true;
        for(int i =0;i<pictureItems.size();i++){
            if(TextUtils.isEmpty(pictureItems.get(i).getPicPath())){
                isfinish = false;
            }
        }
        return  isfinish;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            if (requestCode == PHOTO_BIG_PHOTO) {
                if (data != null) {
                    boolean recapture = data.getBooleanExtra("recapture", false);
                    //重拍位置 0214 郑有权
                    curPicPos = data.getIntExtra("recapturePosition", curPicPos);
                    if (recapture) {
                        if (!pictureItems.get(curPicPos).getPicPath().startsWith("http")) {
                            FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
                        }
                        pictureItems.get(curPicPos).setPicPath("");
                        userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE);
                    }
                }
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            loadPhoto();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPhoto();

    }



    public void updatePhoto(){
        //刷新图片之前再次清对应图片的缓存，否则修改状态下重拍，有时会出现始终显示上一张图片的缓存  -> 李波 on 2017/1/7.
        if(pictureItems !=null && pictureItems.size()>0){
            String path = pictureItems.get(curPicPos).getPicPath();
            if(!StringUtils.isEmpty(path)&&!path.startsWith("http")) {
                FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
            }

            adapter.setDataLists(pictureItems);
            adapter.notifyDataSetChanged();
        }

    }


    public void loadPhoto(){

        setBasePhoto();
        updatePhoto();
    }


}
