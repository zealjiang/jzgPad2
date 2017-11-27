package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.BMWCarPhotoSubjoinAdapter;
import com.jcpt.jzg.padsystem.adapter.CarPhotoSubjoinAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.OnCompressListener;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.ImageCompressor;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 宝马 - 其他缺陷照片
 * @author libo
 * @time 2017/11/6 11:07
 */
public class BMWCarPhotoOtherDefectFragment extends BaseFragment{


    @BindView(R.id.rv)
    public RecyclerView recyclerview;

    private Context context;
    private GridLayoutManager gridLayoutManager;
    private BMWCarPhotoSubjoinAdapter adapter;
    private Map<String, String> mapsPhoto = new HashMap<>();

    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItems;

    private ArrayList<PictureItem> pictureItemsWater;              //泡水检查
    private ArrayList<PictureItem> pictureItemsWindowLamp;         //玻璃及大灯
    private ArrayList<PictureItem> pictureItemsBodyPaint;          //车身外观缺陷
    private ArrayList<PictureItem> pictureItemsBodyAppearancePaste;//粘贴物
    private ArrayList<PictureItem> pictureItemsBodyAppearancePaint;//漆面缺陷
    private ArrayList<PictureItem> pictureItemsBodyAppearanceChrome;//镀铬部件缺陷

    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private int curPicPos = 0;
    private int curEndPicPos = 0;  //当前最后一张图片的id编号
    private String curphotoPrefix = "";  //当前拍照编号
    private ProgressDialog dialog;

    private boolean isOpenCamera;

    public ArrayList<PictureItem> getPictureItems() {
        return pictureItems;
    }

    public void setPictureItems(ArrayList<PictureItem> pictureItems) {
        this.pictureItems = pictureItems;
    }

    public BMWCarPhotoOtherDefectFragment() {
        pictureItems = new ArrayList<>();
        pictureItemsWater = new ArrayList<>();
        pictureItemsWindowLamp = new ArrayList<>();
        pictureItemsBodyPaint = new ArrayList<>();
        pictureItemsBodyAppearancePaste = new ArrayList<>();
        pictureItemsBodyAppearancePaint = new ArrayList<>();
        pictureItemsBodyAppearanceChrome = new ArrayList<>();
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
        initDatapictureItems();
    }

    public void init(){

        gridLayoutManager = new GridLayoutManager(context,4);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new BMWCarPhotoSubjoinAdapter(context,pictureItems,isOpenCamera);
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new BMWCarPhotoSubjoinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                curPicPos = position;
                clickPic();
            }
        });
    }

    //初始化图片
    public void initDatapictureItems(){
        mapsPhoto.clear();
        pictureItems.clear();
        if (isOpenCamera)
        pictureItems.add(new PictureItem("QTJC_WaterCar_"+0,"拍摄其他缺陷",""));
  }


    public void clickPic(){
        String picPath = pictureItems.get(curPicPos).getPicPath();

        if (isOpenCamera) {

            if (TextUtils.isEmpty(picPath)) {
                //拍照
                userCamera("QTJC_WaterCar_" + (curEndPicPos + 1));
            } else {
                //去除第一个拍摄附加照片
                PictureItem pictureItem = pictureItems.get(0);
                pictureItems.remove(0);
                //查看大图
                Intent intent = new Intent(context, PictureZoomActivity.class);
                intent.putExtra("url", picPath);
                intent.putExtra("showRecapture", true);
                intent.putExtra("isDel", true);
                intent.putExtra("pictureItems", pictureItems);
                intent.putExtra("curPosition", curPicPos - 1);//在PictureZoomActivity点重拍显示下面小图列表时不要显示"拍摄附加照片"
                intent.putExtra("taskid", ((BMWDetectMainActivity) getActivity()).getTaskid());
                startActivityForResult(intent, PHOTO_BIG_PHOTO);

                //加回第一个展示点击用
                pictureItems.add(0, pictureItem);
            }

        }else {
                //查看大图
                Intent intent = new Intent(context, PictureZoomActivity.class);
                intent.putExtra("url", picPath);
                intent.putExtra("showRecapture", true);
                intent.putExtra("isDel", true);
                intent.putExtra("pictureItems", pictureItems);
                intent.putExtra("curPosition", curPicPos);
                intent.putExtra("taskid", ((BMWDetectMainActivity) getActivity()).getTaskid());
                startActivityForResult(intent, PHOTO_BIG_PHOTO);
        }
    }



    /**
     * 跳转相机
     */
    private void userCamera(final String photoPrefix){

        curphotoPrefix = photoPrefix;

        //调用自定义相机
        Intent intent = new Intent(context,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId",((BMWDetectMainActivity)getActivity()).getTaskid());
        intent.putExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_SINGLE);//拍摄模式，是单拍还是连拍

            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem pictureItem = new PictureItem();
            pictureItem.setPicId(photoPrefix);
            pictureItem.setPicName(pictureItems.get(curPicPos).getPicName());
            singleList.add(pictureItem);
            intent.putExtra("picList",singleList);
            intent.putExtra("position",0);

        ((BMWDetectMainActivity)context).startActivityForResult(intent,PHOTO_REQUEST);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PHOTO_BIG_PHOTO&&resultCode== Activity.RESULT_OK){
            if(data!=null){
                //重拍
                boolean recapture = data.getBooleanExtra("recapture", false);

                if (isOpenCamera)
                    curPicPos = data.getIntExtra("recapturePosition",curPicPos) + 1;//因为在进入PictureZoomActivity之前curPicPos-1把"拍摄附加照片"去掉了，回来后，还要加上这张照片，所以curPicPos要加1
                else
                    curPicPos = data.getIntExtra("recapturePosition",curPicPos);

                if(recapture){
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(),false);
                    pictureItems.get(curPicPos).setPicPath("");
                    userCamera(pictureItems.get(curPicPos).getPicId());
                }
                //删除
                boolean isDel = data.getBooleanExtra("isDel", false);
                if(isDel){
                    delPic(curPicPos);
                }

            }
        }else if(requestCode == PHOTO_REQUEST){
            //拍照
            File file = new File(PadSysApp.picDirPath,curphotoPrefix+".jpg");
            compress(context, file.getAbsolutePath(),curphotoPrefix, ((BMWDetectMainActivity)getActivity()).getTaskid());
        }

    }

    /**
     * Created by 李波 on 2016/11/30.
     * 压缩照片
     * @param context
     * @param imgPath      图片的文件路径
     * @param defectPicId  图片的前缀名
     */
    private void compress(Context context, String imgPath, final String defectPicId, final String
            taskId){
        final String TAG = "PhotoCompressor";

        ImageCompressor.get(context)
                .setFilename("temp")
                .load(new File(imgPath))                     //传人要压缩的图片
                .putGear(ImageCompressor.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                        showDialog();
                    }
                    @Override
                    public void onSuccess(File file) {
                        LogUtil.e(TAG,"src pic:"+file.length()/1024+"kb");
                        String realPath = Constants.ROOT_DIR + File.separator + taskId+File.separator;
                        String fileName = defectPicId+".jpg";
                        String fullPath = realPath + fileName;

                        LogUtil.e(TAG,"final Path="+realPath+fileName);
                        if(FileUtils.createOrExistsDir(realPath)){
                            if(FileUtils.isFileExists(fullPath))
                                FrescoCacheHelper.clearSingleCacheByUrl(fullPath,true);
                            FileUtils.copyFile(file,new File(realPath,fileName));
                            closeDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        closeDialog();
                    }
                }).launch();
    }

    public void showDialog(){
        if(dialog==null)
            dialog = ProgressDialog.show(this.getContext(),"照片处理中","");
        else
            dialog.show();
    }

    private void closeDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
            dialog=null;
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


    /*
       加载详情显示
     */
    public void loadPhoto(){
        mapsPhoto.clear();

        pictureItemsWater.clear();
        pictureItemsWindowLamp.clear();
        pictureItemsBodyPaint.clear();
        pictureItemsBodyAppearancePaste.clear();
        pictureItemsBodyAppearancePaint.clear();
        pictureItemsBodyAppearanceChrome.clear();

        pictureItems.clear();
        if (isOpenCamera)
        pictureItems.add(new PictureItem("QTJC_WaterCar_"+0,"拍摄其他缺陷",""));

        //读取本地拍照图片
        File dir = new File(PadSysApp.picDirPath);
        File[] files = dir.listFiles();
        if (files != null) {
            if (files.length > 0) {

                for (File file : files) {
                    if (file.getName().contains("QTJC_WaterCar_")) {
                        initPhotoData(file);
                    }
                    if (file.getName().contains("QTJC_WindowLamp_")) {
                        initPhotoData(file);
                    }
                    if (file.getName().contains("QTJC_BodyPaint_")) {
                        initPhotoData(file);
                    }
                    if (file.getName().contains("QTJC_BodyAppearancePaste_")) {
                        initPhotoData(file);
                    }
                    if (file.getName().contains("QTJC_BodyAppearancePaint_")) {
                        initPhotoData(file);
                    }
                    if (file.getName().contains("QTJC_BodyAppearanceChrome_")) {
                        initPhotoData(file);
                    }
                }
            }
        }


        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            if (entry.getKey().contains("QTJC_WaterCar_")) {
                pictureItemsWater.add(new PictureItem(entry.getKey(), "火烧泡水" + Integer.valueOf(entry.getKey().substring(entry.getKey().length() - 1)), entry.getValue()));
            }
        }
        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            if (entry.getKey().contains("QTJC_WindowLamp_")) {
                pictureItemsWindowLamp.add(new PictureItem(entry.getKey(), "玻璃及大灯" + Integer.valueOf(entry.getKey().substring(entry.getKey().length() - 1)), entry.getValue()));
            }
        }
        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            if (entry.getKey().contains("QTJC_BodyPaint_")) {
                pictureItemsBodyPaint.add(new PictureItem(entry.getKey(), "车身外观缺陷" + Integer.valueOf(entry.getKey().substring(entry.getKey().length() - 1)), entry.getValue()));
            }
        }
        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            if (entry.getKey().contains("QTJC_BodyAppearancePaste_")) {
                pictureItemsBodyAppearancePaste.add(new PictureItem(entry.getKey(), "粘贴物" + Integer.valueOf(entry.getKey().substring(entry.getKey().length() - 1)), entry.getValue()));
            }
        }
        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            if (entry.getKey().contains("QTJC_BodyAppearancePaint_")) {
                pictureItemsBodyAppearancePaint.add(new PictureItem(entry.getKey(), "漆面缺陷" + Integer.valueOf(entry.getKey().substring(entry.getKey().length() - 1)), entry.getValue()));
            }
        }
        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            if (entry.getKey().contains("QTJC_BodyAppearanceChrome_")) {
                pictureItemsBodyAppearanceChrome.add(new PictureItem(entry.getKey(), "镀铬部件缺陷" + Integer.valueOf(entry.getKey().substring(entry.getKey().length() - 1)), entry.getValue()));
            }
        }

        Collections.sort(pictureItemsWater);
        Collections.sort(pictureItemsWindowLamp);
        Collections.sort(pictureItemsBodyPaint);
        Collections.sort(pictureItemsBodyAppearancePaste);
        Collections.sort(pictureItemsBodyAppearancePaint);
        Collections.sort(pictureItemsBodyAppearanceChrome);

        pictureItems.addAll(pictureItemsWater);
        pictureItems.addAll(pictureItemsWindowLamp);
        pictureItems.addAll(pictureItemsBodyPaint);
        pictureItems.addAll(pictureItemsBodyAppearancePaste);
        pictureItems.addAll(pictureItemsBodyAppearancePaint);
        pictureItems.addAll(pictureItemsBodyAppearanceChrome);

        updatePhoto();
    }


    private void initPhotoData(File file) {
        mapsPhoto.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
    }

    public void updatePhoto(){
        //刷新图片之前再次清对应图片的缓存，否则修改状态下重拍，有时会出现始终显示上一张图片的缓存  -> 李波 on 2017/1/7.

//            FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(),false);
            adapter.setDataLists(pictureItems);
            adapter.notifyDataSetChanged();

        if(pictureItems !=null && pictureItems.size()>0){
            String value = pictureItems.get(pictureItems.size()-1).getPicId();
            int curEndPos = Integer.valueOf(value.substring(value.length()-1));
            if(curEndPos >= curEndPicPos){
                curEndPicPos = curEndPos;
            }
        }else {
            curEndPicPos = 0;
        }
    }


    /**
     * 删除图片
     * @param position
     */

    public void delPic(final int position){
        curPicPos = position;
        //删除本地图片
        String path = PadSysApp.picDirPath + pictureItems.get(position).getPicId() + ".jpg";
        if (FileUtils.isFileExists(path)) {
            FileUtils.deleteFile(path);
        }
        mapsPhoto.remove(pictureItems.get(position).getPicId());

       /* SubmitModel submitModel = ((BMWDetectMainActivity)this.getActivity()).getSubmitModel();
        //添加删除参数
        if(submitModel != null && submitModel.getDeletePicId() != null){
            submitModel.getDeletePicId().add(pictureItems.get(position).getPicId());
        }*/
        loadPhoto();
    }


}


