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
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.BMWCarPhotoSubjoinAdapter;
import com.jcpt.jzg.padsystem.adapter.CarPhotoSubjoinAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.OnCompressListener;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.ImageCompressor;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.CarPicModel;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 附加照片
 * @author 郑有权
 * @time 2017/02/20 11:07
 */
public class BMWCarPhotoSubjoinFragment extends BaseFragment{


    @BindView(R.id.rv)
    public RecyclerView recyclerview;

    private Context context;
    private GridLayoutManager gridLayoutManager;
    private BMWCarPhotoSubjoinAdapter adapter;
    private Map<String, String> mapsPhoto = new HashMap<>();

    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItems;
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private int curPicPos = 0;
    private int curEndPicPos = 0;  //当前最后一张图片的id编号
    private String curphotoPrefix = "";  //当前拍照编号
    TaskDetailModel mTaskDetailModel;
    private ProgressDialog dialog;

    public ArrayList<PictureItem> getPictureItems() {
        return pictureItems;
    }

    public void setPictureItems(ArrayList<PictureItem> pictureItems) {
        this.pictureItems = pictureItems;
    }

    public BMWCarPhotoSubjoinFragment() {
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
        initDatapictureItems();
    }

    public void init(){

        gridLayoutManager = new GridLayoutManager(context,4);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new BMWCarPhotoSubjoinAdapter(context,pictureItems,true);
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
        pictureItems.add(new PictureItem("U_0","拍摄附加照片",""));
        //读取详情返回网络图片
        mTaskDetailModel = ((BMWDetectMainActivity) getActivity()).getTaskDetailModel();

        if(mTaskDetailModel != null){
            List<TaskDetailModel.TaskCarPicAdditionalListBean> lists =  mTaskDetailModel.getTaskCarPicAdditionalList();
            if(lists != null && lists.size()>0){
                for(int i =0 ;i<lists.size();i++){
                    mapsPhoto.put(lists.get(i).getPicName(), lists.get(i).getPath());
                }
            }
        }else{
            //从本地数据库中获取此用户下的taskId
            String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
            List<DBBase> list = DBManager.getInstance().query(taskId,Constants.ADDITIONAL_PHOTO,PadSysApp.getUser().getUserId());
            LogUtil.e(TAG,"本地数据库： "+list+" list.size(): "+list.size());
            CarPicModel carPicModel = null;
            if(list!=null&&list.size()>0){
                String json = list.get(0).getJson();
                if(!TextUtils.isEmpty(json)){
                    carPicModel = new Gson().fromJson(json,CarPicModel.class);
                }
            }
            if(carPicModel!=null){
                List<TaskDetailModel.TaskCarPicAdditionalListBean> lists = carPicModel.getTaskCarPicAdditionalList();
                if(lists != null && lists.size()>0){
                    for(int i =0 ;i<lists.size();i++){
                        mapsPhoto.put(lists.get(i).getPicName(), lists.get(i).getPath());
                    }
                }
            }
        }

    }


    public void clickPic(){
        String picPath = pictureItems.get(curPicPos).getPicPath();
        if(TextUtils.isEmpty(picPath)){
            //拍照
            userCamera("U_"+(curEndPicPos+1));
        }else{
            //去除第一个拍摄附加照片
            PictureItem pictureItem = pictureItems.get(0);
            pictureItems.remove(0);
            //查看大图
            Intent intent = new Intent(context,PictureZoomActivity.class);
            intent.putExtra("url",picPath);
            intent.putExtra("showRecapture",true);
            intent.putExtra("isDel",true);
            intent.putExtra("pictureItems",pictureItems);
            intent.putExtra("curPosition",curPicPos-1);//在PictureZoomActivity点重拍显示下面小图列表时不要显示"拍摄附加照片"
            intent.putExtra("taskid",((BMWDetectMainActivity)getActivity()).getTaskid());
            startActivityForResult(intent,PHOTO_BIG_PHOTO);

            //加回第一个展示点击用
            pictureItems.add(0,pictureItem);
        }
    }



    /**
     * 跳转相机
     */
    private void userCamera(final String photoPrefix){

        curphotoPrefix = photoPrefix;


       /* RxPermissions.getInstance(context)
                .request(Manifest.permission.CAMERA) //请求相机权限
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String photoName = photoPrefix+".jpg";
                            //请求权限
                            RxPermissions.getInstance(context).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean granted) {
                                    if(granted){
                                        FileUtils.createOrExistsDir(PadSysApp.picDirPath);
                                    }else{
                                        MyToast.showShort("需要获取SD卡读写权限来保存图片");
                                    }
                                }
                            });

                            //指定照片存储路径  -> 李波 on 2016/11/25.
                            File file = new File(PadSysApp.picDirPath,photoName);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

                            startActivityForResult(intent,PHOTO_REQUEST);
                        } else {
                            MyToast.showLong("此功能需要开启相机权限!");
                        }
                    }
                });*/


        //调用自定义相机
        Intent intent = new Intent(context,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId",((BMWDetectMainActivity)getActivity()).getTaskid());
        intent.putExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_SINGLE);//拍摄模式，是单拍还是连拍

            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem pictureItem = new PictureItem();
            pictureItem.setPicId(photoPrefix);
            pictureItem.setPicName("附加照片"+StringHelper.returnInt(curphotoPrefix));
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
                curPicPos = data.getIntExtra("recapturePosition",curPicPos) + 1;//因为在进入PictureZoomActivity之前curPicPos-1把"拍摄附加照片"去掉了，回来后，还要加上这张照片，所以curPicPos要加1
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
     * 压缩照片--缺陷项详情下的三张照片
     * @param context
     * @param imgPath      图片的文件路径
     * @param defectPicId  图片的前缀名
     */
    private void compress(Context context, String imgPath, final String defectPicId, final String taskId){
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
        pictureItems.clear();
        pictureItems.add(new PictureItem("U_0","拍摄附加照片",""));

        //读取本地拍照图片
        File dir = new File(PadSysApp.picDirPath);
        File[] files = dir.listFiles();
        if (files != null) {
            if (files.length > 0) {
                for (File file : files) {
                    if (file.getName().contains("U_")) {
                        mapsPhoto.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
            pictureItems.add(new PictureItem(entry.getKey(),"附加照片"+StringHelper.returnInt(entry.getKey()),entry.getValue()));
        }
        Collections.sort(pictureItems);

        updatePhoto();
    }

    public void updatePhoto(){
        //刷新图片之前再次清对应图片的缓存，否则修改状态下重拍，有时会出现始终显示上一张图片的缓存  -> 李波 on 2017/1/7.
        if(pictureItems !=null && pictureItems.size()>0){
//            FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(),false);
            adapter.setDataLists(pictureItems);
            adapter.notifyDataSetChanged();

            int curEndPos = StringHelper.returnInt(pictureItems.get(pictureItems.size()-1).getPicId());
            if(curEndPos >= curEndPicPos){
                curEndPicPos = curEndPos;
            }

        }
    }


    /**
     * 删除图片
     * @param position
     */

    public void delPic(final int position){
        curPicPos = position;
        //删除详情mode图片
        if(mTaskDetailModel != null){
            List<TaskDetailModel.TaskCarPicAdditionalListBean> lists =  mTaskDetailModel.getTaskCarPicAdditionalList();
            if(lists != null && lists.size()>0){
                for(int i =0 ;i<lists.size();i++){
                    if(lists.get(i).getPicName().equals(pictureItems.get(position).getPicId())){
                        lists.remove(i);
                    }
                }

            }
        }
        //删除本地图片
        String path = PadSysApp.picDirPath + pictureItems.get(position).getPicId() + ".jpg";
        if (FileUtils.isFileExists(path)) {
            FileUtils.deleteFile(path);
        }
        mapsPhoto.remove(pictureItems.get(position).getPicId());
        SubmitModel submitModel = ((BMWDetectMainActivity)this.getActivity()).getSubmitModel();
        //添加删除参数
        if(submitModel != null && submitModel.getDeletePicId() != null){
            submitModel.getDeletePicId().add(pictureItems.get(position).getPicId());
        }

        loadPhoto();
    }

}


