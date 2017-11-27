package com.jcpt.jzg.padsystem.bmw.ui.fragment;

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
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarPhotoProdedureAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.EventProcedurePhoto;
import com.jcpt.jzg.padsystem.vo.ProcedureModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BMWCarPhotoProdedureFragment extends BaseFragment {

    @BindView(R.id.rv)
    public  RecyclerView recyclerview;

    private Context context;
    private GridLayoutManager gridLayoutManager;
    public CarPhotoProdedureAdapter adapter;
    private List<String> photoPaths;
    private Map<String, String> mapsPhoto = new HashMap<>();

    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItems;
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private int curPicPos = 0;
    private String taskId;

    private ArrayList<PictureItem> itemArrayList;
    private TaskDetailModel taskDetailModel;

    //行驶证是否未见
    private boolean isShowDriverLicense = false;

    //无行驶证  -> 李波 on 2017/11/24.
    private boolean drivingLicNoIsSelect = false;

    public BMWCarPhotoProdedureFragment() {

        pictureItems = new ArrayList<>();
        initDatapictureItems();
    }

    public void setShowDriverLicense(boolean showDriverLicense) {
        isShowDriverLicense = showDriverLicense;
    }

    public void setDrivingLicNoIsSelect(boolean drivingLicNoIsSelect) {
        this.drivingLicNoIsSelect = drivingLicNoIsSelect;
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
        taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        setProdedurePhoto();
        return view;
    }

    @Override
    protected void initData() {
        taskDetailModel = ((BMWDetectMainActivity) this.getActivity()).getTaskDetailModel();
    }

    private void setProdedurePhoto(){
        mapsPhoto.clear();
        //加载网络详情返回的图片
        TaskDetailModel mTaskDetailModel = ((BMWDetectMainActivity)getActivity()).getTaskDetailModel();
        if(mTaskDetailModel != null){
            List<TaskDetailModel.ProcedurePicListBean> getProcedurePicList = mTaskDetailModel.getProcedurePicList();
            if(getProcedurePicList != null){
                for(int y = 0;y<getProcedurePicList.size();y++){
                    mapsPhoto.put(getProcedurePicList.get(y).getPicID(),getProcedurePicList.get(y).getPicPath());
                }
            }
        }else {
            //从本地数据库中获取此用户下的taskId
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_PROCEDURE, PadSysApp.getUser().getUserId());
            LogUtil.e(TAG, "本地数据库： " + list + " list.size(): " + list.size());
            if (list != null && list.size() > 0) {
                String json = list.get(0).getJson();
                if (!TextUtils.isEmpty(json)) {
                    ProcedureModel procedureModel = new Gson().fromJson(json, ProcedureModel.class);
                    if(procedureModel!=null){
                        List<TaskDetailModel.ProcedurePicListBean> procedurePicList = procedureModel.getProcedurePicList();
                        if(procedurePicList!=null){
                            for (int i = 0; i < procedurePicList.size(); i++) {
                                mapsPhoto.put(procedurePicList.get(i).getPicID(),procedurePicList.get(i).getPicPath());
                            }
                        }
                    }
                }
            }
        }


        initDatapictureItems();

        for(int i = 0;i<pictureItems.size();i++){
            if(mapsPhoto.get(pictureItems.get(i).getPicId()) != null){
                pictureItems.get(i).setPicPath(mapsPhoto.get(pictureItems.get(i).getPicId()));
            }
            String path = PadSysApp.picDirPath+pictureItems.get(i).getPicId()+".jpg";
            if(FileUtils.isFileExists(path)){
                pictureItems.get(i).setPicPath(path);
            }
        }
    }

    public void init() {
        gridLayoutManager = new GridLayoutManager(context, 4);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new CarPhotoProdedureAdapter(context, pictureItems,false,false);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new CarPhotoProdedureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {
                curPicPos = position;

                if (isShowDriverLicense) {
                    MyToast.showLong("行驶证未见不可操作");
                } else if (drivingLicNoIsSelect){
                    MyToast.showLong("无行驶证不可操作");
                } else{
                    clickPic();
                }
                }

        });


    }


    public void clickPic() {
        String picPath = pictureItems.get(curPicPos).getPicPath();

        if (TextUtils.isEmpty(picPath)) {
            //拍照
            userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE);
        } else {
            if (itemArrayList == null){
                itemArrayList = new ArrayList<>();
            }
            itemArrayList.clear();
            itemArrayList.addAll(pictureItems);

            //查看大图
            Intent intent = new Intent(context, PictureZoomActivity.class);
            intent.putExtra("url", picPath);
            intent.putExtra("showRecapture", true);
            intent.putExtra("isCarPhotoProdedureFragment", true);
            intent.putExtra("pictureItems",itemArrayList);
            intent.putExtra("curPosition",curPicPos);
            intent.putExtra("taskid",((BMWDetectMainActivity)getActivity()).getTaskid());
            startActivityForResult(intent, PHOTO_BIG_PHOTO);
        }
    }




    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position, int capture_type) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", ((BMWDetectMainActivity) getActivity()).getTaskid());
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍
        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {
            intent.putExtra("picList", pictureItems);
            intent.putExtra("position", position);
        } else {
            ArrayList<PictureItem> singleList = new ArrayList<>();
            singleList.add(pictureItems.get(position));
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    /**
     * 照片是否上传完全
     *
     * @return
     */
    public boolean isFinish() {

        boolean isfinish = true;

        if (!isShowDriverLicense&&!drivingLicNoIsSelect) {
            if (pictureItems != null && pictureItems.size() > 0) {
                for (int i = 0; i < pictureItems.size(); i++) {
                    if (TextUtils.isEmpty(pictureItems.get(i).getPicPath())) {
                        isfinish = false;
                    }
                }
            }
        }
        return isfinish;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_BIG_PHOTO && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                boolean recapture = data.getBooleanExtra("recapture", false);
                boolean isDel = data.getBooleanExtra("isDel", false);
                //重拍位置 0214 郑有权
                curPicPos = data.getIntExtra("recapturePosition",curPicPos);
                if (recapture) {
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
                    pictureItems.get(curPicPos).setPicPath("");
                    userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE);
                }

            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            loadPhoto();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPhoto();
    }

    public void initDatapictureItems() {
        pictureItems.clear();
        pictureItems.add(new PictureItem("18", "行驶证正本正面", ""));
        pictureItems.add(new PictureItem("19", "行驶证正本背面", ""));
        pictureItems.add(new PictureItem("20", "行驶证副本正面", ""));
        pictureItems.add(new PictureItem("24", "行驶证副本背面", ""));
    }

    public void updatePhoto() {

        if (pictureItems.size() == curPicPos)
            curPicPos=pictureItems.size()-1;

        //刷新图片之前再次清对应图片的缓存，否则修改状态下重拍，有时会出现始终显示上一张图片的缓存  -> 李波 on 2017/1/7.
        if(!pictureItems.get(curPicPos).getPicPath().startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
        }
        adapter.setDataLists(pictureItems);
        adapter.notifyDataSetChanged();
    }


    public void loadPhoto() {

        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        //从本地数据库中获取此用户下的taskId
        List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_PROCEDURE, PadSysApp.getUser().getUserId());
        LogUtil.e(TAG, "本地数据库： " + list + " list.size(): " + list.size());
        ProcedureModel procedureModel = null;
        if (list != null && list.size() > 0) {
            String json = list.get(0).getJson();
            if (!TextUtils.isEmpty(json)) {
                procedureModel = new Gson().fromJson(json, ProcedureModel.class);
            }
        }
        if (procedureModel != null) {
            List<TaskDetailModel.ProcedurePicListBean> picList = procedureModel.getProcedurePicList();//获取网络图片
            mapsPhoto.clear();
            if (picList != null) {
                for (int i = 0; i < picList.size(); i++) {
                    mapsPhoto.put(picList.get(i).getPicID(), picList.get(i).getPicPath());
                }
            }
            initDatapictureItems();
            for (int i = 0; i < pictureItems.size(); i++) {
                if (mapsPhoto.get(pictureItems.get(i).getPicId()) != null) {
                    pictureItems.get(i).setPicPath(mapsPhoto.get(pictureItems.get(i).getPicId()));
                }
                String path = PadSysApp.picDirPath + pictureItems.get(i).getPicId() + ".jpg";
                if (FileUtils.isFileExists(path)) {
                    pictureItems.get(i).setPicPath(path);
                }

            }

            updatePhoto();

            //发通知给手续信息更新照片 by zealjiang 2016-12-27
            EventProcedurePhoto eventProcedurePhoto = new EventProcedurePhoto();
            eventProcedurePhoto.setHasRefreshed(true);
            EventBus.getDefault().post(eventProcedurePhoto);
        }
    }
}
