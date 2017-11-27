package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.blankj.utilcode.utils.FileUtils;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.PhotoDefectExpandableAdapter;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean.PartsPositionListBean
        .DefectTypeListBean;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectType;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jcpt.jzg.padsystem.app.PadSysApp.picDirPath;

/**
 * 缺陷照片
 *
 * @author zealjiang
 * @time 2016/11/14 16:25
 */
public class BMWCarPhotoDefectFragment extends BaseFragment implements PhotoDefectExpandableAdapter.IchooseFlawPhotoLister {

    @BindView(R.id.eListView)
    public  ExpandableListView expandableListView;
    /**
     * 每个分组的名字的集合
     */
    private List<String> groupList;
    /**
     * 每个分组下的每个子项的 GridView 数据集合
     */
    private List<PictureItem> itemGridList;
    /**
     * 包含各分组的List
     */
    private List<List<PictureItem>> itemList;
    private Map<String, String> maps = new HashMap<>();
    private Map<String, String> mapsPhoto = new HashMap<>();


    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItems;
    private final int PHOTO_REQUEST = 10;
    private final int PHOTO_BIG_PHOTO = 11;
    private int curPicPos = 0;


    LocalDetectionData mDetectionWrapper;

    PhotoDefectExpandableAdapter photoDefectExpandableAdapter;

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_photo_defect, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {


    }

    public void init() {
        mDetectionWrapper = ((BMWDetectMainActivity)getActivity()).getLocalDetectionData();

        itemList = new ArrayList<>();
        //分组
        groupList = new ArrayList<>();
        for (int i = 0; i < mDetectionWrapper.getCheckPositionList().size(); i++) {
            groupList.add(mDetectionWrapper.getCheckPositionList().get(i).getCheckPositionName());
            maps.put(mDetectionWrapper.getCheckPositionList().get(i).getCheckPositionName(),
                    mDetectionWrapper.getCheckPositionList().get(i).getCheckPositionId());
            List<PictureItem> itemGridList = new ArrayList<>();
            itemList.add(itemGridList);
        }

        photoDefectExpandableAdapter = new PhotoDefectExpandableAdapter(this.getContext(), groupList, itemList);
        expandableListView.setAdapter(photoDefectExpandableAdapter);
        // 隐藏分组指示器
        expandableListView.setGroupIndicator(null);
        // 默认展开第一组
        expandableListView.expandGroup(0);
        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        photoDefectExpandableAdapter.setmIchooseFlawPhotoLister(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadPhoto("");
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            loadPhoto("");
        }
    }

    /**
     * 点击图片
     * @param itemPositon
     * @param tagPositon
     * @param mPictureItem
     */
    @Override
    public void show(int itemPositon, int tagPositon, PictureItem mPictureItem) {
        pictureItems = (ArrayList) itemList.get(itemPositon);
        curPicPos = tagPositon;
        //查看大图
        Intent intent = new Intent(context, PictureZoomActivity.class);
        intent.putExtra("url", mPictureItem.getPicPath());
        intent.putExtra("showRecapture", true);
        intent.putExtra("isDel", true);
        intent.putExtra("pictureItems",pictureItems);
        intent.putExtra("curPosition",curPicPos);
        intent.putExtra("taskid",((BMWDetectMainActivity)getActivity()).getTaskid());
        startActivityForResult(intent, PHOTO_BIG_PHOTO);
//        userCamera(tagPositon,Constants.CAPTURE_TYPE_SINGLE);

    }

    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position, int capture_type) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", ((BMWDetectMainActivity)getActivity()).getTaskid());
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_BIG_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                boolean recapture = data.getBooleanExtra("recapture", false);
                //重拍位置 0214 郑有权
                curPicPos = data.getIntExtra("recapturePosition",curPicPos);
                if (recapture) {
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
                    pictureItems.get(curPicPos).setPicPath("");
                    userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE);
                }
                boolean isDel = data.getBooleanExtra("isDel", false);
                if(isDel){

                    //删除本地图片
                    String path = picDirPath + pictureItems.get(curPicPos).getPicId() + ".jpg";
                    FrescoCacheHelper.clearSingleCacheByUrl(path, true);
                    if (FileUtils.isFileExists(path)) {
                        FileUtils.deleteFile(path);
                    }
                    //如果删除的是复检的网络图片
                    FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), true);

                    SubmitModel submitModel = ((BMWDetectMainActivity)this.getActivity()).getSubmitModel();
                    //添加删除参数
                    if(submitModel != null && submitModel.getDeletePicId() != null){
                        if(!submitModel.getDeletePicId().contains(pictureItems.get(curPicPos).getPicId())){
                            submitModel.getDeletePicId().add(pictureItems.get(curPicPos).getPicId());
                        }

                    }

                    loadPhoto(pictureItems.get(curPicPos).getPicId());
                }

            }
        }

    }


    /**
     * picId 是要删除的网络图片地址，可以为空字符串
     * @param picId
     */
    public void loadPhoto(String picId){

        mDetectionWrapper = ((BMWDetectMainActivity)getActivity()).getLocalDetectionData();
        mapsPhoto.clear();
        //读取详情返回网络图片
        List<CheckPositionItem> getCheckPositionList = mDetectionWrapper.getCheckPositionList();
        if (getCheckPositionList != null) {
            for (int j = 0; j < getCheckPositionList.size(); j++) {
                if (getCheckPositionList.get(j) != null) {
                    List<ImportantItem> getImportantList = getCheckPositionList.get(j).getImportantList();
                    if (getImportantList != null) {
                        for (int k = 0; k < getImportantList.size(); k++) {
                            if (getImportantList.get(k) != null) {
                                List<CheckItem> getCheckItemList = getImportantList.get(k).getCheckItemList();
                                if (getCheckItemList != null) {
                                    for (int l = 0; l < getCheckItemList.size(); l++) {
                                        if (getCheckItemList.get(l) != null) {
                                            List<DefectType> getDefectTypeList = getCheckItemList.get(l).getDefectTypeList();
                                            if (getDefectTypeList != null) {
                                                for (int m = 0; m < getDefectTypeList.size(); m++) {
                                                    if (getDefectTypeList.get(m) != null) {
                                                        List<DefectDetailItem> getDefectDetailList = getDefectTypeList.get(m).getDefectDetailList();
                                                        if (getDefectDetailList != null) {
                                                            for (int n = 0; n < getDefectDetailList.size(); n++) {
                                                                if (getDefectDetailList.get(n) != null) {
                                                                    List<DefectTypeListBean.ImgListBean> getPicDefectHttpUrlList = getDefectDetailList.get(n).getPicDefectHttpUrlList();
                                                                    if (getPicDefectHttpUrlList != null) {
                                                                        for (int p = 0; p < getPicDefectHttpUrlList.size(); p++) {
                                                                            if (getPicDefectHttpUrlList.get(p) != null) {
                                                                                if(!TextUtils.isEmpty(getPicDefectHttpUrlList.get(p).getUriID())){
                                                                                    if(getPicDefectHttpUrlList.get(p).getUriID().equals(picId)){
                                                                                        getPicDefectHttpUrlList.remove(p);
                                                                                    }else{
                                                                                        mapsPhoto.put(getPicDefectHttpUrlList.get(p).getUriID(), getPicDefectHttpUrlList.get(p).getPicPath());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //读取本地拍照图片
        File dir = new File(picDirPath);
        File[] files = dir.listFiles();
        if (files != null) {
            if (files.length > 0) {
                for (File file : files) {
                    if (file.getName().contains("_")) {
                        mapsPhoto.put(StringHelper.subFile(file.getName()), file.getAbsolutePath());
                        FrescoCacheHelper.clearSingleCacheByUrl(file.getAbsolutePath(), false);
                    }
                }
            }
        }
        itemList.clear();
        for (int i = 0; i < groupList.size(); i++) {
            List<PictureItem> itemGridList = new ArrayList<>();
            for (Map.Entry<String, String> entry : mapsPhoto.entrySet()) {
                if (entry.getKey().contains(maps.get(groupList.get(i)))) {
                    PictureItem photoModel = new PictureItem(entry.getKey(), StringHelper.cutPhotoName(entry.getKey()), entry.getValue());
                    itemGridList.add(photoModel);
                }
            }
            itemList.add(itemGridList);
        }
        photoDefectExpandableAdapter.setItemList(itemList);
        photoDefectExpandableAdapter.notifyDataSetChanged();

    }


}
