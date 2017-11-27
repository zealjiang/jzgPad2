package com.jcpt.jzg.padsystem.bmw.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.TaskTypeAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.model.BMWDetectionModel;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.interfaces.ISaveListener;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.jcpt.jzg.padsystem.model.DetectionModel.PHOTO_BIG_PHOTO;
import static com.jcpt.jzg.padsystem.model.DetectionModel.PHOTO_REQUEST;

/**
 * 车况检测
 */
public class BMWDetectionFragment extends BaseFragment implements INextStepListener {

    @BindView(R.id.rvTaskType)
    RecyclerView rvTaskType;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    private List<String> typeList;
    private TaskTypeAdapter typeAdapter;
    private MyAdapter adapter;
    private String taskId;


    private BMWImportantDetectionFragment BMWImportantDetectionFragment;
    private BMWDetectMainActivity activity;
    private List<CheckPositionItem> checkPositionList;
    private List<Fragment> fragments;
    private String NAMES[] = {"重点", "非重点"};

    private TaskDetailModel taskDetailModel;
    /**
     * Created by 李波 on 2016/12/5.
     * 未提交数据之前的修改标记
     */
    public static boolean detectionIsFix;


    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 对应检测项上面横向的一排 照片
     */
    private ArrayList<PictureItem> pictureItems;

    /**
     * 当前未拍照的位置
     */
    private int curNoPhotoPosition = 0;

    private LocalDetectionData wrapper;
    private BMWDetectionModel detectionModel;


    public BMWDetectionFragment() {
    }

    @Override
    protected void initData() {
        fragments = new ArrayList<>();
        BMWImportantDetectionFragment = new BMWImportantDetectionFragment();//重点
        BMWImportantDetectionFragment.setiNextStepListener(BMWDetectionFragment.this);
        fragments.add(BMWImportantDetectionFragment);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void setView() {

        activity = (BMWDetectMainActivity) getActivity();
        taskId = activity.getTaskid();

        detectionModel = new BMWDetectionModel(activity);

        wrapper = activity.getLocalDetectionData();
        if (wrapper != null && wrapper.getDefectValueList() != null) {
            ((BMWDetectMainActivity) getActivity()).getSubmitModel().setDefectValue(wrapper.getDefectValueList());
        }

        //判断这个任务是否存在检测详情数据
        taskDetailModel = ((BMWDetectMainActivity)this.getActivity()).getTaskDetailModel();
        LogUtil.e(TAG,"网络： "+" taskDetailModel: "+taskDetailModel);
        if(taskDetailModel!=null) {
            //基本照片
            List<TaskDetailModel.PicListBean> picList = taskDetailModel.getPicList();
            if (picList != null) {
                ArrayList<PictureItem> picItemList = new ArrayList<>();
                for (int i = 0; i < picList.size(); i++) {
                    PictureItem pi = new PictureItem();
                    pi.setPicId(picList.get(i).getPicID());
                    pi.setPicName(picList.get(i).getPicName());
                    pi.setPicPath(picList.get(i).getPicPath());
                    picItemList.add(pi);
                }

                wrapper.setPictureList(picItemList);
            }
        }

        loadData();

        rvTaskType.setLayoutManager(new LinearLayoutManager(PadSysApp.getAppContext()));
        typeAdapter = new TaskTypeAdapter(getContext(), typeList);
        rvTaskType.setAdapter(typeAdapter);

        typeAdapter.setOnItemClickListener(new TaskTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (typeAdapter.getCurrPosition() == position)
                    return;

                //点击切换方位时，让检测项的 RecyclerView 回到顶部  -> 李波 on 2016/12/6.
//                BMWImportantDetectionFragment.getRvOptions().smoothScrollToPosition(0);
                BMWImportantDetectionFragment.getScrollView().smoothScrollTo(0,0);
                BMWImportantDetectionFragment.getPulltorefreshview().hideHeader();
//                unBMWImportantDetectionFragment.getRvNoKeyPointOptions().smoothScrollToPosition(0);

                if (viewpager.getCurrentItem() == 1)
                    viewpager.setCurrentItem(0);
                typeAdapter.setCurrPosition(position);
                setData(position);
            }
        });

        adapter = new MyAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(viewpager);

    }

    private void loadData() {
        if (((BMWDetectMainActivity) getActivity()).getLocalDetectionData() == null) {
            return;
        }

        checkPositionList = ((BMWDetectMainActivity) getActivity()).getLocalDetectionData().getCheckPositionList();
        typeList = new ArrayList<>();
        typeList.add("基本照片");
        if (checkPositionList != null && checkPositionList.size() > 0) {
            for (int i= 0;i<checkPositionList.size();i++) {
                typeList.add(checkPositionList.get(i).getCheckPositionName());
            }
        }

        setData(1);
        if (wrapper != null) {
            loadPhoto();
        }


    }

    public void loadPhoto() {
        //读取详情返回网络图片
        if (wrapper != null) {

            //如果缓存数据的基本照片为空，提取原始方案重新构建  -> 李波 on 2017/11/20.
            if (wrapper.getPictureList()==null || wrapper.getPictureList().size()==0){

                String planId = ((BMWDetectMainActivity)getActivity()).getPlanId();

                String json = DBManager.getInstance().queryLocalPlan(planId, Constants.DATA_TYPE_PLAN);
                DetectionWrapper wrapperPlan = new Gson().fromJson(json, DetectionWrapper.class);

                if (wrapperPlan!=null)
                    wrapper.setPictureList(wrapperPlan.getPictureList());

            }

            pictureItems = (ArrayList<PictureItem>) wrapper.getPictureList();
            if (pictureItems != null && pictureItems.size() > 0) {
                for (int i = 0; i < pictureItems.size(); i++) {
                    String path = PadSysApp.picDirPath + pictureItems.get(i).getPicId() + ".jpg";
                    if (FileUtils.isFileExists(path)) {
                        pictureItems.get(i).setPicPath(path);
                    }else if(!StringUtils.isEmpty(pictureItems.get(i).getPicPath())&&!pictureItems.get(i).getPicPath().startsWith("http")){
                        pictureItems.get(i).setPicPath("");
                    }
                }
                Collections.sort(pictureItems);
            }
        }

    }


    /**
     * 跳转到下一个方位
     *
     * @param position 点击的下一个位置角标
     */
    private void toNextPostition(int position) {
        //获取重点检测项分支  -> 李波 on 2016/11/28.
        List<ImportantItem> importantList = checkPositionList.get(typeAdapter.getCurrPosition()).getImportantList();

        ImportantItem importantItem = null;

        if (importantList != null) {
            if (importantList.size() == 1) {
                String importantId = importantList.get(0).getImportantId();
                if (importantId.equals("1")) {
                    importantItem = importantList.get(0);
                }
            } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                String importantId = importantList.get(0).getImportantId();
                if (importantId.equals("1")) {
                    importantItem = importantList.get(0);
                } else {
                    importantItem = importantList.get(1);
                }
            }
        }

        if (importantItem != null) {
            //判断当前方位的重点项是否已经全部选中，如果未选中不让点击切换方位  -> 李波 on 2016/11/28.
            List<CheckItem> checkItemList = importantItem.getCheckItemList();

            int status = detectionModel.getImportantSelStatus(checkItemList);

            if (status == 0) {
                MyToast.showLong("您还有未检测项的重点项");
            } else {
                //保存用户任务数据到数据库  -> 李波 on 2016/11/29.
                detectionModel.saveUseTaskToDB();
                typeAdapter.setCurrPosition(position);
                setData(position);
            }
        }
    }


    public void showPhoto() {
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        loadPhoto();
                        if (isPhotoFull()) {
                            //打开相册
                            Intent intent = new Intent(activity, PictureZoomActivity.class);
                            intent.putExtra("pictureItems", pictureItems);
                            intent.putExtra("curPosition", 0);
                            intent.putExtra("url", "");
                            intent.putExtra("showRecapture", true);
                            intent.putExtra("isThumbnail", true);

                            intent.putExtra("taskid",((BMWDetectMainActivity)getActivity()).getTaskid());
                            startActivityForResult(intent, PHOTO_BIG_PHOTO);

                        } else {
                            //打开相机
                            userCamera(curNoPhotoPosition, Constants.CAPTURE_TYPE_MULTI, taskId, pictureItems);
                        }
                    }
                });


    }

    /**
     * 跳转自定义相机
     *
     * @param position     当前点击的图片位置
     * @param capture_type 拍摄模式，是单拍还是连拍
     * @param taskId       当前任务 Id
     * @param pictureItems 当前点击图片的图片集合
     */
    public void userCamera(int position, int capture_type, String taskId, ArrayList<PictureItem> pictureItems) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", taskId);
//        intent.putExtra("position",position);
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍
        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {
            intent.putExtra("picList", pictureItems);
            intent.putExtra("position", position);
        } else {

            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem pictureItem = new PictureItem();
            pictureItem.setPicId(pictureItems.get(position).getPicId());
            singleList.add(pictureItem);
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    //是否拍摄完成
    public boolean isPhotoFull() {
        boolean isPhotoFull = true;
        if (pictureItems != null && pictureItems.size() > 0) {
            for (int i = 0; i < pictureItems.size(); i++) {
                if (TextUtils.isEmpty(pictureItems.get(i).getPicPath())) {
                    curNoPhotoPosition = i;
                    isPhotoFull = false;
                    break;
                }
            }
        }
        return isPhotoFull;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void setData(int position) {
        if (position == 0) {
            if (isVisible) {
                // 显示相机
                showPhoto();
            }
        } else {
            CheckPositionItem directionItem = checkPositionList.get(position - 1);//获取某个需要检测的方位，比如：左前
            List<ImportantItem> importantList = directionItem.getImportantList();
            ImportantItem importantItem = null;
            ImportantItem unImportantItem = null;
            if (importantList != null) {
                if (importantList.size() == 1) {
                    String importantId = importantList.get(0).getImportantId();
                    if (importantId.equals("1")) {
                        importantItem = importantList.get(0);
                        unImportantItem = new ImportantItem();
                    } else {
                        unImportantItem = importantList.get(0);
                        importantItem = new ImportantItem();
                    }

                } else if (importantList.size() == 2) {//有重点和非重点 ImportantId : "1" -- 重点，0 -- 非重点
                    String importantId = importantList.get(0).getImportantId();
                    if (importantId.equals("1")) {
                        importantItem = importantList.get(0);
                        unImportantItem = importantList.get(1);
                    } else {
                        importantItem = importantList.get(1);
                        unImportantItem = importantList.get(0);
                    }
                }
            } else {
                importantItem = new ImportantItem();
                unImportantItem = new ImportantItem();
            }

            BMWImportantDetectionFragment.setItem(importantItem, directionItem ,unImportantItem);

        }
        /**
         * created by wujj on 2017/2/14
         * 当检测方位是最后一项时显示下一步按钮。
         */
        if (position == typeList.size() - 1) {
            BMWImportantDetectionFragment.isLastItem = true;
        } else {
            BMWImportantDetectionFragment.isLastItem = false;
        }
        BMWImportantDetectionFragment.showNextStep();

    }

    @Override
    public void nextStep(int position) {
        BMWDetectMainActivity activity = (BMWDetectMainActivity) getActivity();
        activity.remove(Integer.valueOf(3));
        int currPosition = typeAdapter.getCurrPosition();
        int nextPosition = ++currPosition;
        if (nextPosition != typeAdapter.getItemCount()) {
            typeAdapter.setCurrPosition(nextPosition);
            setData(nextPosition);
        } else {
            ((BMWDetectMainActivity) this.getActivity()).skipToFragment(3);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BMWDetectionModel.PHOTO_REQUEST:
                if (data != null) {
                    int captureType = data.getIntExtra(Constants.CAPTURE_TYPE, Constants.CAPTURE_TYPE_MULTI);
                    ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                    if (picList != null && picList.size() > 0) {
                        if (captureType == Constants.CAPTURE_TYPE_MULTI) {
                            pictureItems = picList;
                            wrapper.setPictureList(pictureItems);
                        }
                    }
                }
                //拍照返回后停留在第二个
                showSecond();
                break;
            case BMWDetectionModel.PHOTO_BIG_PHOTO:
                if (data != null) {
                    boolean recapture = data.getBooleanExtra("recapture", false);
                    //重拍位置 0214 郑有权
                    int curPicPos = data.getIntExtra("recapturePosition", 0);
                    if (recapture) {
                        FrescoCacheHelper.clearSingleCacheByUrl(pictureItems.get(curPicPos).getPicPath(), false);
//                        pictureItems.get(curPicPos).setPicPath("");
                        userCamera(curPicPos, Constants.CAPTURE_TYPE_MULTI, taskId, pictureItems);
                    } else {
                        //拍照返回后停留在第二个
                        showSecond();
                    }
                } else {
                    //拍照返回后停留在第二个
                    showSecond();
                }

                break;
            default:
                //拍照返回后停留在第二个
                showSecond();
                break;
        }

    }

    public void showSecond() {
        typeAdapter.setCurrPosition(1);
        setData(1);
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        //配置标题的方法
        @Override
        public CharSequence getPageTitle(int position) {
            return NAMES[position];
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        detectionModel.saveUseTaskToDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detectionModel.saveUseTaskToDB();
    }

    //预留的保存按钮功能  -> 李波 on 2016/12/6.
    private ISaveListener iSaveListener = new ISaveListener() {
        @Override
        public void saveData() {
//            MyToast.showLong("保存成功");
            detectionModel.saveUseTaskToDB();
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            isVisible = true;
            ((BMWDetectMainActivity) getActivity()).remove(3);
            activity.setOnListenerSaveData(iSaveListener);
            if (typeAdapter.getCurrPosition() == 0) {
                if (isPhotoFull()) {
                    showSecond();
                } else {
                    setData(0);
                }
            } else {
                setData(typeAdapter.getCurrPosition());
            }


        } else {
            isVisible = false;
            if(detectionModel!=null) {
                detectionModel.saveUseTaskToDB();
            }
        }
    }


    /**
     * Created by 李波 on 2016/12/5.
     * 判断车况检测必填项数据的完整性，确保提交
     */
    public boolean checkDetectionDataComplete(boolean isShowToast) {
        return detectionModel.checkDetectionDataComplete(activity, isShowToast);
    }

}
