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
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.CarPhotoProdedureAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.EventProcedurePhoto;
import com.jcpt.jzg.padsystem.vo.ProcedureModel;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;



public class CarPhotoProdedureFragment extends BaseFragment {

    @BindView(R.id.rv)
    public  RecyclerView recyclerview;

    private Context context;
    private GridLayoutManager gridLayoutManager;
    public CarPhotoProdedureAdapter adapter;
    private List<String> photoPaths;
    private Map<String, String> mapsPhoto = new HashMap<>();

    //行驶证是否未见
    private boolean isShowDriverLicense = false;
    //登记证是否未见
    private boolean isShowRegistration = false;


    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItems;
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private int curPicPos = 0;
    private String taskId;

    private ArrayList<PictureItem> itemArrayList;
    private TaskDetailModel taskDetailModel;

    private boolean configDriving; //行驶证照片 81
    private boolean configRegister;//登记证照片 82
    private DetectionWrapper detectionWrapper;

    public void setShowDriverLicense(boolean showDriverLicense) {
        isShowDriverLicense = showDriverLicense;
    }

    public void setShowRegistration(boolean showRegistration) {
        isShowRegistration = showRegistration;
    }

    public CarPhotoProdedureFragment() {

        pictureItems = new ArrayList<>();
//        initDatapictureItems();
    }

    @Override
    protected void setView() {
        init();
        setProdedurePhoto();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_photo_procedure, container, false);
        ButterKnife.bind(this, view);
        context = this.getContext();
        taskId = ((DetectMainActivity) getActivity()).getTaskid();
        return view;
    }

    @Override
    protected void initData() {
        taskDetailModel = ((DetectMainActivity) this.getActivity()).getTaskDetailModel();
        detectionWrapper = ((DetectMainActivity)this.getActivity()).getWrapper();

        List<String> configList =  detectionWrapper.getProcedureList();
        for (int i = 0; i < configList.size(); i++) {
            switch (configList.get(i)) {
                case "81":
                    configDriving = true;
                    break;
                case "82":
                    configRegister = true;
                    break;
            }
        }
    }

    private void setProdedurePhoto(){
        mapsPhoto.clear();
        //加载网络详情返回的图片
        TaskDetailModel mTaskDetailModel = ((DetectMainActivity)getActivity()).getTaskDetailModel();
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
        adapter = new CarPhotoProdedureAdapter(context, pictureItems,configDriving,configRegister);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new CarPhotoProdedureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {
                curPicPos = position;

                int picId = Integer.valueOf(pictureItems.get(position).getPicId());

                if (picId == 18 || picId == 19 || picId == 20 || picId == 24){ //行驶证  -> 李波 on 2017/11/22.
                    //不可操作
                    if (isShowDriverLicense) {
                        MyToast.showLong("行驶证未见不可操作");
                    } else {
                        clickPic();
                    }
                }

                if (picId == 21 || picId == 22 || picId == 23 || picId == 28){ //登记证  -> 李波 on 2017/11/22.
                    //不可操作
                    if (isShowRegistration) {
                        MyToast.showLong("登记证未见不可操作");
                    } else {
                        clickPic();
                    }
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

                //登记证未见
                if (isShowRegistration) {
                    itemArrayList.clear();
                    itemArrayList.add(pictureItems.get(0));
                    itemArrayList.add(pictureItems.get(1));
                    itemArrayList.add(pictureItems.get(2));
                    itemArrayList.add(pictureItems.get(3));
                } else if (isShowDriverLicense) {//行驶证未见
                    switch (curPicPos) {
                        case 4:
                            curPicPos = 0;
                            break;
                        case 5:
                            curPicPos = 1;
                            break;
                        case 6:
                            curPicPos = 2;
                            break;
                        case 7:
                            curPicPos = 3;
                            break;
                    }
                    itemArrayList.clear();
                    itemArrayList.add(pictureItems.get(4));
                    itemArrayList.add(pictureItems.get(5));
                    itemArrayList.add(pictureItems.get(6));
                    if (pictureItems.size() == 8) itemArrayList.add(pictureItems.get(7));
                }


            //查看大图
            Intent intent = new Intent(context, PictureZoomActivity.class);
            intent.putExtra("url", picPath);
            intent.putExtra("showRecapture", true);
            intent.putExtra("isCarPhotoProdedureFragment", true);
            intent.putExtra("pictureItems",itemArrayList);
            intent.putExtra("curPosition",curPicPos);
            intent.putExtra("taskid",((DetectMainActivity)getActivity()).getTaskid());
            startActivityForResult(intent, PHOTO_BIG_PHOTO);
        }
    }




    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position, int capture_type) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", ((DetectMainActivity) getActivity()).getTaskid());
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
        int i = 0;
        int size =0;
        if (pictureItems != null && pictureItems.size() > 0) {

            if (configDriving && configRegister) { //行驶证 登记证都存在的情况  -> 李波 on 2017/11/22.

                size = 6; //行驶证 4张 + 登记证必拍 2张  -> 李波 on 2017/11/22.
                //如果行驶证未见
                if (isShowDriverLicense) {
                    i = 4;
                }
                //如果登记证未见
                if (isShowRegistration) {
                    size = 4;
                }

            }else if (configDriving==true && configRegister==false){ //只有行驶证的情况  -> 李波 on 2017/11/22.
                size = 4;
                //如果行驶证未见
                if (isShowDriverLicense) {
                    return isfinish;
                }
            }else if (configDriving==false && configRegister==true){ //只有登记证的情况  -> 李波 on 2017/11/22.
                size = 2; //必拍只有两张，更多登记证1,2为选拍，不在判断范围内  -> 李波 on 2017/11/22.
                //如果登记证未见
                if (isShowRegistration) {
                    return isfinish;
                }
            }

            for (; i < size; i++) {
                if (TextUtils.isEmpty(pictureItems.get(i).getPicPath())) {
                    isfinish = false;
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

        if (configDriving && configRegister) {
            pictureItems.add(new PictureItem("18", "行驶证正本正面", ""));
            pictureItems.add(new PictureItem("19", "行驶证正本背面", ""));
            pictureItems.add(new PictureItem("20", "行驶证副本正面", ""));
            pictureItems.add(new PictureItem("24", "行驶证副本背面", ""));
            pictureItems.add(new PictureItem("21", "登记证 1-2 页照片", ""));
            pictureItems.add(new PictureItem("22", "登记证 3-4 页照片", ""));
            pictureItems.add(new PictureItem("23", "更多登记证照片1", ""));

            try {//规避掉初始化时的空指针  -> 李波 on 2017/9/11.
                //判断登记照片1 是否有显示地址，有添加更多2  -> 李波 on 2017/9/11.
                String path23 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI();
                String path28 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI2();
                if (!TextUtils.isEmpty(path23) || FileUtils.isFileExists(PadSysApp.picDirPath + "23.jpg") || !TextUtils.isEmpty(path28)) {

                    pictureItems.add(new PictureItem("28", "更多登记证照片2", ""));
                }

            } catch (Exception e) {

            }
        }else if (configDriving==true && configRegister==false){
            pictureItems.add(new PictureItem("18", "行驶证正本正面", ""));
            pictureItems.add(new PictureItem("19", "行驶证正本背面", ""));
            pictureItems.add(new PictureItem("20", "行驶证副本正面", ""));
            pictureItems.add(new PictureItem("24", "行驶证副本背面", ""));
        }else if (configDriving==false && configRegister==true){
            pictureItems.add(new PictureItem("21", "登记证 1-2 页照片", ""));
            pictureItems.add(new PictureItem("22", "登记证 3-4 页照片", ""));
            pictureItems.add(new PictureItem("23", "更多登记证照片1", ""));

            try {//规避掉初始化时的空指针  -> 李波 on 2017/9/11.
                //判断登记照片1 是否有显示地址，有添加更多2  -> 李波 on 2017/9/11.
                String path23 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI();
                String path28 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI2();
                if (!TextUtils.isEmpty(path23) || FileUtils.isFileExists(PadSysApp.picDirPath + "23.jpg") || !TextUtils.isEmpty(path28)) {

                    pictureItems.add(new PictureItem("28", "更多登记证照片2", ""));
                }

            } catch (Exception e) {

            }
        }

    }

    public void updatePhoto() {

        SubmitModel submitModel = ((DetectMainActivity) this.getActivity()).getSubmitModel();

        List<String>  deleteIdList = submitModel.getDeletePicId();

        setDelPicPath(deleteIdList,"18");
        setDelPicPath(deleteIdList,"19");
        setDelPicPath(deleteIdList,"20");
        setDelPicPath(deleteIdList,"24");
        setDelPicPath(deleteIdList,"21");
        setDelPicPath(deleteIdList,"22");

//        if (ProcedureCarFragment.procedureCarFragment.getProcedureModel().getDeletePicId().contains("23")){
//            pictureItems.get(6).setPicPath("");
//        }
//
//        if (ProcedureCarFragment.procedureCarFragment.getProcedureModel().getDeletePicId().contains("28")&&pictureItems.size()==8){
//            pictureItems.get(7).setPicPath("");
//        }

        setLocalPicPath("18");
        setLocalPicPath("19");
        setLocalPicPath("20");
        setLocalPicPath("24");
        setLocalPicPath("21");
        setLocalPicPath("22");
        setLocalPicPath("23");
        setLocalPicPath("28");

        if (pictureItems.size() == curPicPos)
            curPicPos=pictureItems.size()-1;

        //刷新图片之前再次清对应图片的缓存，否则修改状态下重拍，有时会出现始终显示上一张图片的缓存  -> 李波 on 2017/1/7.
        if(!pictureItems.get(curPicPos).getPicPath().startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
        }
        adapter.setDataLists(pictureItems);
        adapter.notifyDataSetChanged();
    }

    //如果本地路径有照片就对应设置本地路径  -> 李波 on 2017/11/22.
    private void setLocalPicPath(String picId) {
        if( FileUtils.isFileExists(PadSysApp.picDirPath+picId+".jpg")){
            for (int i = 0; i < pictureItems.size(); i++) {
                if (pictureItems.get(i).getPicId().equals(picId)){
                    pictureItems.get(i).setPicPath(PadSysApp.picDirPath+picId+".jpg");
                }
            }

        }
    }


    //当删除id集合里有此图片，就将路径置为空  -> 李波 on 2017/11/22.
    private void setDelPicPath(List<String> deleteIdList,String picId) {
        if (deleteIdList.contains(picId)){
            for (int i = 0; i < pictureItems.size(); i++) {
                if (pictureItems.get(i).getPicId().equals(picId)){
                    pictureItems.get(i).setPicPath("");
                }
            }
        }
    }


    public void loadPhoto() {

        String taskId = ((DetectMainActivity) getActivity()).getTaskid();
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

                String picId = pictureItems.get(i).getPicId();

                if (picId.equals("23")) {

                    String path23SD = PadSysApp.picDirPath+"23.jpg";
                    if (FileUtils.isFileExists(path23SD)) {
                        ((DetectMainActivity) getActivity()).setPathRegisterMorePI(path23SD);
                    }

                    String path23 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI();
                    pictureItems.get(i).setPicPath(path23);
                }

                if (picId.equals("28")) {

                    String path28SD = PadSysApp.picDirPath+"28.jpg";
                    if (FileUtils.isFileExists(path28SD)) {
                        ((DetectMainActivity) getActivity()).setPathRegisterMorePI2(path28SD);
                    }

                    String path28 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI2();
                    pictureItems.get(i).setPicPath(path28);
                }

            }

            PictureItem  pictureItem23 = null;
            PictureItem  pictureItem28 = null;

            for (int i = 0; i < pictureItems.size(); i++) {
                String picId = pictureItems.get(i).getPicId();

                if (picId.equals("23")) {
                    pictureItem23 = pictureItems.get(i);
                }

                if (picId.equals("28")) {
                    pictureItem28 = pictureItems.get(i);
                }
            }

            String path23 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI();
            String path28 = ((DetectMainActivity) getActivity()).getPathRegisterMorePI2();


            if (TextUtils.isEmpty(path23) && !TextUtils.isEmpty(path28)) {

                 for (int i = 0; i < pictureItems.size(); i++) {

                     String picId = pictureItems.get(i).getPicId();

                    if (picId.equals("23")) {
                        if (FileUtils.isFileExists(PadSysApp.picDirPath + "28.jpg")) {
                            FileUtils.copyFile(PadSysApp.picDirPath + "28.jpg", PadSysApp.picDirPath + "23.jpg");
                            FileUtils.deleteFile(PadSysApp.picDirPath + "28.jpg");
                            pictureItems.get(i).setPicPath(PadSysApp.picDirPath + "23.jpg");
                            ((DetectMainActivity) getActivity()).setPathRegisterMorePI(PadSysApp.picDirPath + "23.jpg");
                        } else {
                            pictureItems.get(i).setPicPath(path28);
                            ((DetectMainActivity) getActivity()).setPathRegisterMorePI(path28);
                        }

                        ((DetectMainActivity) getActivity()).setPathRegisterMorePI2("");
                        if (pictureItem28!=null)
                            pictureItem28.setPicPath("");

                        break;
                    }
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
