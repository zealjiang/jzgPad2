package com.jcpt.jzg.padsystem.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.interfaces.ICustomCameraListener;
import com.jcpt.jzg.padsystem.model.DetectionModel;
import com.jcpt.jzg.padsystem.mvpview.IDetectionMain;
import com.jcpt.jzg.padsystem.mvpview.IRepairLogListener;
import com.jcpt.jzg.padsystem.mvpview.ISubmit;
import com.jcpt.jzg.padsystem.mvpview.IUploadListener;
import com.jcpt.jzg.padsystem.mvpview.InsUseRecordInf;
import com.jcpt.jzg.padsystem.presenter.ActiveRepairLogPresenter;
import com.jcpt.jzg.padsystem.presenter.GetConfigurePresenter;
import com.jcpt.jzg.padsystem.presenter.InsUseRecordPresenter;
import com.jcpt.jzg.padsystem.presenter.SubmitPresenter;
import com.jcpt.jzg.padsystem.presenter.UploadPresenter;
import com.jcpt.jzg.padsystem.ui.fragment.CarPhotoFragment;
import com.jcpt.jzg.padsystem.ui.fragment.CarSelectFragment;
import com.jcpt.jzg.padsystem.ui.fragment.DetectionFragment;
import com.jcpt.jzg.padsystem.ui.fragment.OtherFragment;
import com.jcpt.jzg.padsystem.ui.fragment.OtherInformationFragment;
import com.jcpt.jzg.padsystem.ui.fragment.ProcedureFragment;
import com.jcpt.jzg.padsystem.utils.BeanCloneUtil;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.CheckPriceBean;
import com.jcpt.jzg.padsystem.vo.EventProcedureModel;
import com.jcpt.jzg.padsystem.vo.InsUseRecordModel;
import com.jcpt.jzg.padsystem.vo.Repairlog;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.SubmitPartModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.TaskItem;
import com.jcpt.jzg.padsystem.vo.detection.CarPicModel;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.CustomButton;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.jcpt.jzg.padsystem.widget.NoScrollViewPager;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;


public class DetectMainActivity extends BaseActivity implements IDetectionMain, ISubmit, IUploadListener,IRepairLogListener,InsUseRecordInf {

    public static DetectMainActivity detectMainActivity;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    NoScrollViewPager pager;
    private final String ITEMS[] = {"手续信息", "车型选择", "车况检测", "车辆照片", "其他信息"};
    List<Fragment> fragmentList;
    ProcedureFragment procedureFragment;
    CarSelectFragment carSelectFragment;
    DetectionFragment detectionFragment;
    CarPhotoFragment carPhotoFragment;
    OtherFragment otherFragment;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.btnCheckRepairLog)
    CustomButton btnCheckRepairLog;
    @BindView(R.id.btnInsUseRecord)
    CustomButton btnInsUseRecord;

    private String taskId;
    private SubmitPresenter submitPresenter;//文字上传
    private UploadPresenter picUploadPresenter;//图片上传
    private DetectionWrapper wrapper;
    private TaskItem taskItem;
    private MyAdapter myAdapter;
    private boolean isModify;
    private boolean isNeedDetail;//是否需要请求详情数据
    private boolean isContinue;//是否是“继续检测”
    private ICustomCameraListener iCustomCameraListener;
    private Set<Integer> unselectable;

    private SubmitModel submitModel;
    private TaskDetailModel taskDetailModel;
    private int curIndex = 0;
    private GetConfigurePresenter presenter;

    private LocalDetectionData localDetectionData;


    //行驶证是否未见
    private boolean isShowDriverLicense = false;
    //登记证是否未见
    private boolean isShowRegistration = false;
    //是否显示的是信息核对页面标识-//created by wujj on 2017/2/14
    public static boolean isShowOtherInformationFragment = false;

    private OtherInformationFragment otherInformationFragment;
    private ResultCheckedActivity resultCheckedActivity;
    private ResponseJson<CheckPriceBean> response;
    private int ifConfirmPrice;

    //维保记录
    private ActiveRepairLogPresenter activeRepairLogPresenter;

    private SubmitPartModel submitPartModel;
    public final String TIME_TYPE = "TimeType";//操作类型 1、2、3、4、5
    public final String BEGIN_TIME = "BeginTime";//开始时间
    public final String END_TIME = "EndTime";//结束时间

    //出险记录
    private InsUseRecordPresenter insUseRecordPresenter;

    private DetectionModel detectionModel;
    private boolean isNeedSaveData = true;//在finish之后是否要调用保存数据

    /**
     * Created by 李波 on 2017/8/21.
     * 供搜索使用的检测项大集合
     */
    public  List<CheckItem> checkItemList = new ArrayList<>();
    public  List<CheckItem> uncheckItemList = new ArrayList<>();

    /**
     * Created by 李波 on 2017/8/21.
     * 供搜索使用的检测项合集数据
     */
   public ImportantItem importantItemSearch = new ImportantItem();
   public ImportantItem unImportantItemSearch = new ImportantItem();
    private int isAlertAccdient;
    private String accdientAlertMsg;




    private String planId;

    DetectionWrapper detectionWrapperTemp;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        LogUtil.e("DetectMainActivity","initViews savedInstanceState"+savedInstanceState);
        setContentView(R.layout.activity_detect_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        detectMainActivity = this;
        wrapper = PadSysApp.wrapper;
        if(wrapper==null){
            return;
        }
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        int width = UIUtils.dip2px(this,160);

        StringHelper.savePlanPhotoId(PadSysApp.wrapper);

        detectionModel = new DetectionModel(this);

        unselectable = new HashSet<>();
        taskItem = (TaskItem) getIntent().getSerializableExtra("TaskItem");
        isModify = getIntent().getBooleanExtra("isModify", false);
        isContinue = getIntent().getBooleanExtra("isContinue", false);
        isNeedDetail = getIntent().getBooleanExtra("isNeedDetail", false);
        isAlertAccdient = getIntent().getIntExtra("isAlertAccdient",0);
        accdientAlertMsg = getIntent().getStringExtra("accdientAlertMsg");

        submitModel = new SubmitModel();
        submitModel.setSpSourceId(taskItem.getSpSourceId());
        submitModel.setContinue(isContinue);
        //设置taskId
        if(taskItem != null){
            PadSysApp.taskId = taskId = taskItem.getTaskId() + "";
            planId = taskItem.getPlanId();
            submitModel.setId(Integer.valueOf(taskId));

            //如果内存的wrapper 里因某些原因造成无基本照片，那必须从数据库取出原始方案数据  -> 李波 on 2017/11/21.
            if (wrapper.getPictureList()==null || wrapper.getPictureList().size() == 0){

                boolean isExist = DBManager.getInstance().isPlanExist(planId, Constants.DATA_TYPE_PLAN);//查数据库中是否有检测方案数据
                if (isExist) {
                    String json = DBManager.getInstance().queryLocalPlan(planId, Constants.DATA_TYPE_PLAN);
                    wrapper = new Gson().fromJson(json, DetectionWrapper.class);
                }

            }

            LogUtil.e(TAG,"taskId: "+taskId);
        }else{
            //MyToast.showLong("任务id错误");
            finish();
        }

        //克隆一份初始对象赋值给 localDetectionData,保证localDetectionData里面的修改不会影响到初始方案wrapper   -> 李波 on 2016/12/7.
        if(wrapper != null){

            detectionWrapperTemp = wrapper.myclone();

            localDetectionData = new LocalDetectionData();
            localDetectionData.setCheckPositionList(detectionWrapperTemp.getCheckPositionList());
            localDetectionData.setIsEdit(detectionWrapperTemp.getIsEdit());
            localDetectionData.setPlanId(detectionWrapperTemp.getPlanId());
            localDetectionData.setPlanName(detectionWrapperTemp.getPlanName());
            localDetectionData.setPictureList(detectionWrapperTemp.getPictureList());

            if (localDetectionData.getPictureList()==null){
                List<PictureItem> pictureItemListTemp = new ArrayList<>();
                localDetectionData.setPictureList(pictureItemListTemp);
            }

        }

        ArrayList<String> deletePicIdArray = new ArrayList<String>();
        submitModel.setDeletePicId(deletePicIdArray);
        submitPresenter = new SubmitPresenter(this);
        picUploadPresenter = new UploadPresenter(this, this);
        presenter = new GetConfigurePresenter(this);
        PadSysApp.picDirPath = Constants.ROOT_DIR + File.separator + taskId + File.separator;

        clearCache();

        String DetectionJson = DBManager.getInstance().queryLocalUseTask(taskId,Constants.DATA_TYPE_USE_TASK,PadSysApp.getUser().getUserId());
        if (DetectionJson!=null){
            LocalDetectionData detectionWrapper = new Gson().fromJson(DetectionJson,LocalDetectionData.class);
            setLocalDetectionData(detectionWrapper);

            List<PictureItem> pictureItemListTemp = new ArrayList<>();

            if (detectionWrapperTemp.getPictureList()!=null) {
                List<PictureItem> pictureItemList = detectionWrapperTemp.getPictureList();
                List<PictureItem> pictureItemLocal = localDetectionData.getPictureList();
                for (int i = 0; i < pictureItemList.size(); i++) {
                    String id = pictureItemList.get(i).getPicId();
                    for (int j = 0; j < pictureItemLocal.size(); j++) {
                        String localId = pictureItemLocal.get(j).getPicId();
                        if (id.equals(localId)) {
                            pictureItemListTemp.add(pictureItemLocal.get(j));
                        }
                    }

                }
                localDetectionData.setPictureList(pictureItemListTemp);
            }

        }

        //--------------------------------------------------
        initSearchImAndUnimList();
        //--------------------------------------------------


        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) {
                        //根据任务taskId来创建文件夹，如果存在就不创建
                        processLogic();
                    } else {
                        MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        finish();
                    }
                }
            });

        } else {
            processLogic();
        }

        activeRepairLogPresenter = new ActiveRepairLogPresenter(this);
        insUseRecordPresenter = new InsUseRecordPresenter(this);



        //从本地数据库中获取此用户下的taskId
        List<DBBase> list = DBManager.getInstance().query(taskId,Constants.DATA_TYPE_SUBMIT_PART,PadSysApp.getUser().getUserId());
        LogUtil.e(TAG,"本地数据库： "+list+" list.size(): "+list.size());
        if(list!=null&&list.size()>0){
            String json = list.get(0).getJson();
            if(!TextUtils.isEmpty(json)){
                submitPartModel = new Gson().fromJson(json,SubmitPartModel.class);
                submitModel.setTaskTimeRegionList(submitPartModel.getTaskTimeRegionList());
            }
        }else{

             // 统计手续信息、车型选择、车况检测、车辆照片、其他信息的时间
             //TaskTimeRegionList 数据集合      TimeType  操作类型    BeginTime   开始时间   EndTime   结束时间
             //
            ArrayList<LinkedHashMap<String,String>> timeList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                LinkedHashMap<String,String> map = new LinkedHashMap();
                map.put(TIME_TYPE,String.valueOf(i+1));//操作类型 1、2、3、4、5
                map.put(BEGIN_TIME,"");//开始时间
                map.put(END_TIME,"");//结束时间
                timeList.add(map);
            }
            submitModel.setTaskTimeRegionList(timeList);
        }

        if (!isContinue){
            ifShowAlertAccident();
        }


    }

    private void ifShowAlertAccident() {
        if (isAlertAccdient == 1){
            showAlertAccident();
        }
    }

    private void showAlertAccident() {
        if (!TextUtils.isEmpty(accdientAlertMsg)){
            final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(this);
            View view = myUniversalDialog.getLayoutView(R.layout.dialog_check_vin);
            TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            TextView tvIknow = (TextView) view.findViewById(R.id.tvIknow);
            tvMsg.setText(accdientAlertMsg);
            myUniversalDialog.setLayoutView(view,480,260);
            myUniversalDialog.show();
            tvIknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myUniversalDialog.cancel();
                }
            });
        }
    }

    /**
     * Created by 李波 on 2017/8/21.
     * 初始化重点 和 非重点所有检测项的大合集，以供搜索使用，因搜索从本地搜。
     */
    private void initSearchImAndUnimList() {
        List<CheckPositionItem> checkPositionList   = localDetectionData.getCheckPositionList();
        if(checkPositionList==null)
            return;
        for (int i = 0; i < checkPositionList.size(); i++) {
            CheckPositionItem directionItem = checkPositionList.get(i);//获取某个需要检测的方位，比如：左前
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

            //把每一个检测项的重点集合加入到 重点搜索合集里  -> 李波 on 2017/8/21.
            List<CheckItem> imItemList = importantItem.getCheckItemList();
            if (imItemList!=null) {
                for (int j = 0; j < imItemList.size(); j++) {
                    checkItemList.add(imItemList.get(j));
                }
            }

            //把每一个检测项的非重点集合加入到 非重点搜索合集里  -> 李波 on 2017/8/21.
            List<CheckItem> unimItemList = unImportantItem.getCheckItemList();
            if (unimItemList!=null) {
                for (int j = 0; j < unimItemList.size(); j++) {
                    uncheckItemList.add(unimItemList.get(j));
                }
            }
        }


        //所有检测项只要不是缺陷状态2，全部置为正常状态1  -> 李波 on 2017/8/24.
        for (int i = 0; i < checkItemList.size(); i++) {
            if (checkItemList.get(i).getStatus() != 2)
                checkItemList.get(i).setStatus(1);
        }

        for (int j = 0; j < uncheckItemList.size(); j++) {
            if (uncheckItemList.get(j).getStatus() != 2)
                uncheckItemList.get(j).setStatus(1);
        }

        importantItemSearch.setImportantId("1");
        importantItemSearch.setImportantName("重点");
        unImportantItemSearch.setImportantId("0");
        unImportantItemSearch.setImportantName("非重点");

        importantItemSearch.setCheckItemList(checkItemList);
        unImportantItemSearch.setCheckItemList(uncheckItemList);
    }


    public View getButtonView(){
        return  btnCheckRepairLog;
    }



    /**
     * 如果是修改进来的先清空本地的缓存数据
     */
    private void clearCache() {
        //如果是修改进来的，先清空本地的缓存数据
        if (isModify||isNeedDetail) {
            clearTaskData();
        }
    }

    /**
     * 清楚任务数据
     */
    private void clearTaskData(){
        //删除本地数据库此taskId的记录
        DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(), taskId);
        //删除对应的本地图片
        FileUtils.deleteDir(PadSysApp.picDirPath);

    }

    private void processLogic() {
        FileUtils.createOrExistsDir(Constants.ROOT_DIR + File.separator + taskId);
        if (isModify||isNeedDetail) {
            if (PadSysApp.networkAvailable) {
                //获取详情数据前清空本地对应的缓存数据
                DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(),taskId);

                presenter.getDetailData(taskId);
            } else {
                MyToast.showLong("网络不可用，无法获取任务数据");
                finish();
            }
        } else {

            initFragment();
        }
    }


    @Override
    protected void setData() {
    }


    public void add(Integer... index) {
        if(unselectable==null){
            this.finish();
            return;
        }
        for (Integer i : index) {
            unselectable.add(i);
        }
    }

    public void remove(Integer index) {
        if(unselectable==null){
            return;
        }
        if (unselectable.contains(index))
        unselectable.remove(index);
    }


    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Integer index = (Integer) v.getTag();
                    boolean contains = unselectable.contains(index);
                    LogUtil.e(TAG, "curr:" + curIndex + ",clicked:" + index + ",contains=" + contains);
                    if (curIndex==2 && index==4){ //当前是车况检测的情况下 点击其他信息时需判断  -> 李波 on 2016/12/11.
                        boolean isBaseComplete = procedureFragment.checkBasePhoto(true);
                        boolean isComplete = detectionFragment.checkDetectionDataComplete(false);

                        if (isBaseComplete && isComplete){ //如果车况检测数据完整直接跳转其他信息,否则提示  -> 李波 on 2016/12/9.
                            //统计车况检测结束时间
                            recordDetectionET();

                            remove(4);
                            skipToFragment(4);
                        }else{
                            add(4);
                            if (isBaseComplete)
                                detectionModel.showDialog(DetectMainActivity.this);
                            return true;
                        }
                    }
                    if(curIndex==0 && index>0){//如果当前选中的是手续信息，点击了车型选择

                        String hintHead = "";
                        if(index>1) {
                            hintHead = "手续信息页面  ";
                        }
                        //第一步判断手续信息必填项是否填写完成
                        boolean proIsComplete = procedureFragment.checkAndSaveData(false,true,hintHead);
                        if(proIsComplete){
                            //判断车型是否有值，有值跳到车型选择，无值跳到车系选择
                            if (submitModel.getStyleID() > 0 && (submitModel.getModelID() == 0 || submitModel.getMakeID() == 0)) {
                                //如果有ID出错，则重置
                                submitModel.setMakeID(0);
                                submitModel.setModelID(0);
                                submitModel.setStyleID(0);
                            }

                            //设置车型选择tabView可点击
                            remove(1);
                            if(index == 1) {
                                //时间统计
                                recordCarSelectST();

                                if (submitModel.getStyleID() == 0 && submitModel.getModelID() == 0) {//如果没有选择品牌、车系
                                    //跳到选择车辆铭牌
                                    procedureFragment.tapCarTypeSelect(1,"");
                                    return true;
                                }else{
                                    //跳到车型选择
                                    skipToFragment(1);
                                    return true;
                                }
                            }
                        }else{
                            add(1);
                            add(2);
                            add(3);
                            add(4);
                            return true;
                        }

                        //第二步判断车型选择必填项是否填写完成
                        boolean isSaveSuc = carSelectFragment.checkAndSaveData(true,true);
                        if (isSaveSuc) {
                            //时间统计
                            recordCarSelectET();

                            //设置车况检测和车辆照片tabView可点击
                            remove(2);
                            remove(3);
                            if (index == 2 || index == 3) {
                                //统计车况检测开始时间
                                recordDetectionST();

                                //跳转到车况检测
                                skipToFragment(index);
                                return true;
                            }
                        } else {
                            //设置车况检测和车辆照片tabView不可点击
                            add(2);
                            add(3);
                            add(4);
                            return true;
                        }

                        //第三步判断车况检测和车辆照片必填项是否填写完成
                        //判断车况检测、车辆照片
                        boolean isBaseComplete = procedureFragment.checkBasePhoto(true);
                        boolean isComplete = detectionFragment.checkDetectionDataComplete(true);

                        if (isBaseComplete && isComplete) { //如果车况检测数据完整直接跳转其他信息,否则提示  -> 李波 on 2016/12/9.
                            remove(4);
                            if(index == 4) {
                                //统计车况检测结束时间
                                recordDetectionET();

                                skipToFragment(4);
                                return true;
                            }
                        }else{
                            add(4);
                            return true;
                        }

                    }
                    if(curIndex==1 && index>1) {//如果当前选中的是车型选择，点击了车况检测或车辆照片或其他信息

                        //检查车型选择必填项是否都已填完
                        boolean isSaveSuc = carSelectFragment.checkAndSaveData(true,true);
                        if (isSaveSuc) {
                            //时间统计
                            recordCarSelectET();

                            //设置车况检测和车辆照片tabView可点击
                            remove(2);
                            remove(3);
                            if (index == 2 || index == 3) {
                                //统计车况检测开始时间
                                recordDetectionST();

                                //跳转到车况检测
                                skipToFragment(index);
                                return true;
                            }
                        } else {
                            //设置tabView不可点击
                            add(2);
                            add(3);
                            add(4);
                            return true;
                        }

                        //判断车况检测、车辆照片
                        boolean isBaseComplete = procedureFragment.checkBasePhoto(true);
                        boolean isComplete = detectionFragment.checkDetectionDataComplete(true);

                        if (isBaseComplete && isComplete) { //如果车况检测数据完整直接跳转其他信息,否则提示  -> 李波 on 2016/12/9.
                            remove(4);
                            if (index == 4) {
                                //统计车况检测结束时间
                                recordDetectionET();

                                skipToFragment(4);
                                return true;
                            }
                        }else {
                            add(4);
                            return true;
                        }
                    }

                    if(curIndex == 3 && index == 4 ){
                        //如果照片完整，则进入其他信息
                        if(carPhotoFragment.isComplete() && isDetectionComplete()){
                            //统计车况检测结束时间
                            recordDetectionET();

                            remove(4);
                            skipToFragment(4);
                            return true;
                        }else {
                            skipToFragment(3);
                            add(4);
                            return true;
                        }
                    }
                    if (contains) {
                        return true;
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * 记录车型选择开始时间
     * by zealjiang
     */
    public void recordCarSelectST(){
        //判断车型选择是否填写完成
        boolean isSaveSuc = carSelectFragment.check(false);
        if (!isSaveSuc) {
            //开始时间是否为空
            LinkedHashMap<String,String> map = submitModel.getTaskTimeRegionList().get(1);
            if(StringUtils.isEmpty(map.get(BEGIN_TIME))){
                //记录车型选择开始时间
                String time = TimeUtils.getCurTimeString();
                map.put(BEGIN_TIME,time);
                //MyToast.showLong("记录车型选择开始时间: "+time);
            }
        }
    }

    /**
     * 记录车型选择结束时间
     * by zealjiang
     */
    public void recordCarSelectET(){
        //判断车型选择是否填写完成
        boolean isSaveSuc = carSelectFragment.check(false);
        if (isSaveSuc) {
            //结束时间是否为空
            LinkedHashMap<String,String> map = submitModel.getTaskTimeRegionList().get(1);
            if(StringUtils.isEmpty(map.get(END_TIME))){
                //开始时间是否为空
                if(!StringUtils.isEmpty(map.get(BEGIN_TIME))){
                    //记录车型选择结束时间
                    String time = TimeUtils.getCurTimeString();
                    map.put(END_TIME,time);
                    //MyToast.showLong("记录车型选择结束时间: "+time);
                }
            }
        }
    }

    /**
     * 记录车况检测和车辆照片结束时间
     * by zealjiang
     */
    public void recordDetectionST(){
        //判断车况检测和车辆照片是否填写完成
        boolean isBaseComplete = procedureFragment.checkBasePhoto(false);
        boolean isComplete = detectionFragment.checkDetectionDataComplete(false);

        if (isBaseComplete && isComplete) { //如果车况检测数据完整直接跳转其他信息,否则提示
        }else{
            //开始时间是否为空
            LinkedHashMap<String,String> map = submitModel.getTaskTimeRegionList().get(2);
            if(StringUtils.isEmpty(map.get(BEGIN_TIME))){
                //记录车况检测和车辆照片开始时间
                String time = TimeUtils.getCurTimeString();
                map.put(BEGIN_TIME,time);
                //MyToast.showLong("记录车况检测和车辆照片开始时间: "+time);
            }
        }
    }

    /**
     * 记录车况检测和车辆照片结束时间
     * by zealjiang
     */
    public void recordDetectionET(){
        //判断车况检测和车辆照片是否填写完成
        boolean isBaseComplete = procedureFragment.checkBasePhoto(false);
        boolean isComplete = detectionFragment.checkDetectionDataComplete(true);
        if (isBaseComplete && isComplete) {
            //结束时间是否为空
            LinkedHashMap<String,String> map = submitModel.getTaskTimeRegionList().get(2);
            if(StringUtils.isEmpty(map.get(END_TIME))){
                //开始时间是否为空
                if(!StringUtils.isEmpty(map.get(BEGIN_TIME))){
                    //记录车况检测和车辆照片结束时间
                    String time = TimeUtils.getCurTimeString();
                    map.put(END_TIME,time);
                    //MyToast.showLong("记录车况检测和车辆照片结束时间: "+time);
                }
            }

        }
    }

    /**
     * 保存页面使用时长数据
     * 判断是否是在清空taskId数据后调此方法，如果是，就不保存时长数据
     * @return
     */
    private boolean saveTimeData(){

        //第一次领取一个任务时，本地数据库没有数据，服务器也没有返回详情数据时submitPartModel为Null
        if(submitPartModel==null){
            submitPartModel = new SubmitPartModel();
        }
        if(submitModel==null){
            return false;
        }

        submitPartModel.setTaskTimeRegionList(submitModel.getTaskTimeRegionList());
        String json = new Gson().toJson(submitPartModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_SUBMIT_PART, taskId,PadSysApp.getUser().getUserId(),json);
        return true;
    }


    /**
     * 初始化fragment
     *
     */
    private void initFragment() {

        fragmentList = new ArrayList<>();
        procedureFragment = new ProcedureFragment();
        carSelectFragment = new CarSelectFragment();
        detectionFragment = new DetectionFragment();
        carPhotoFragment = new CarPhotoFragment();
        otherFragment = new OtherFragment();

        fragmentList.add(procedureFragment);
        fragmentList.add(carSelectFragment);
        fragmentList.add(detectionFragment);
        fragmentList.add(carPhotoFragment);
        fragmentList.add(otherFragment);
        myAdapter = new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(myAdapter);
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(5);
        tabs.setupWithViewPager(pager);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                curIndex = tab.getPosition();
                pager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            RelativeLayout parentView = (RelativeLayout) UIUtils.inflate(R.layout.item_tag);
            TextView tagView = (TextView) parentView.findViewById(R.id.tvTag);
            tagView.setText(ITEMS[i]);
            parentView.setTag(i);
            parentView.setOnTouchListener(listener);
            tab.setCustomView(parentView);
        }

        if (!isModify) { //非修改进来的刚开始只能点击手续信息tab      这里谁注释掉提交前一定加上
            add(1, 2, 3, 4);
        }

    }


    public String getTaskid() {
        return taskId;
    }


    /**
     * 情况一： 为了保证在所有的fragment生成后能收到详情数据，将fragment的生成延迟到获取详情之后
     * 情况二： 无详情数据，在获取到配置方案数据后，生成fragment
     */
    @Override
    public void requestDataSucceed(TaskDetailModel data) {
        taskDetailModel = data;
        //检测数据详情数据与初始化数据进行比对，把有详情的内容的数据对应设置到大对象里以便回显  -> 李波 on 2016/11/30.
        if (taskDetailModel!=null) {
            detectionModel.equalsDailDataToPadSysAppWrapper(taskDetailModel);
        }
        //保存附加照片
        List<TaskDetailModel.TaskCarPicAdditionalListBean> listPicAdd = data.getTaskCarPicAdditionalList();
        CarPicModel carPicModel = new CarPicModel();
        carPicModel.setTaskCarPicAdditionalList(BeanCloneUtil.cloneTo(listPicAdd));
        String json = new Gson().toJson(carPicModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.ADDITIONAL_PHOTO, taskId,PadSysApp.getUser().getUserId(),json);

        initFragment();
    }

    /**
     * 请求详情失败直接关闭本界面
     * @param error 错误信息
     */
    @Override
    public void showError(String error) {
        super.showError(error);
        if(mIUploadMsgLister != null){
            mIUploadMsgLister.uploadMsgFail();
        }
        finish();
    }

    //维保记录返回成功      郑有权
    @Override
    public void repairLogSucceed(Repairlog repairlog) {
        if(repairlog.getRepairLogStatus() == 0){
            MyToast.showLong("维保记录正在查询中");
        }else if(repairlog.getRepairLogStatus() == 1){

            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("url", repairlog.getRepairLogUrl());
            intent.putExtra("title", "查看维保");
            intent.putExtra("leftRMargin", 10);
            startActivity(intent);
        }else if(repairlog.getRepairLogStatus() == 2){
            MyToast.showLong("查询无结果");
        }else if(repairlog.getRepairLogStatus() == 3){
            MyToast.showLong("VIN码修改维保仅能查询1次，已超出次数！");
        }else if(repairlog.getRepairLogStatus() == 4){
            MyToast.showLong("您还未查询维保，点击车架号VIN进行查询");
        }

    }
    //维保记录返回错误      郑有权
    @Override
    public void showRepairLogError(String error) {
        MyToast.showShort(error);
    }

    //出险记录              李志江
    @Override
    public void insUseRecordSucceed(InsUseRecordModel insUseRecordModel) {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra("url", insUseRecordModel.getClaimsHistoryURL());
        intent.putExtra("title", "出险记录");
        intent.putExtra("leftRMargin", 10);
        startActivity(intent);
    }

    //出险记录返回错误
    @Override
    public void insUseRecordError(String msg) {
        MyToast.showShort(msg);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ITEMS[position];
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(isModify?"您修改的内容提交后才会生效，现在退出将不会保存您修改后的内容":"您即将退出车况检测");
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(isModify){
                    FileUtils.deleteDir(PadSysApp.picDirPath);
                }
                finish();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @OnClick({R.id.ivBack, R.id.btnCheckRepairLog,R.id.btnInsUseRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                exit();
                break;
            case R.id.btnCheckRepairLog:
               //查看维保
                showWeibao();
                break;
            case R.id.btnInsUseRecord:
                //出险记录
                String vin = submitModel.getVin();
                if(TextUtils.isEmpty(vin)){
                    MyToast.showShort("请优先输入VIN码");
                }else {
                    String taskId = submitModel.getId()+"";
                    insUseRecordPresenter.requestInsUseRecord(vin,taskId);
                }
                break;
        }
    }

    /**
     * 查看维保
     */
    private void showWeibao() {
        String vin = submitModel.getVin();
        String taskId = submitModel.getId()+"";
        String userid = PadSysApp.getUser().getUserId()+"";
        activeRepairLogPresenter.loadActiveRepairLog(vin,taskId,userid,taskItem.getTaskSourceId()+"");
    }

    /**
     * 跳到指定的栏目(0:手续信息、1:车型选择、2:车况检测、3:车辆照片、4:其他信息)
     *
     * @author zealjiang
     * @time 2016/11/23 18:38
     */
    public void skipToFragment(int fragmentIndex) {
        pager.setCurrentItem(fragmentIndex,true);
    }

    /**
     * 获取任务对象
     *
     * @author zealjiang
     * @time 2016/12/1 17:19
     */
    public TaskItem getTaskItem() {
        return taskItem;
    }


    /**
     * 获取提交数据对象
     *
     * @author zealjiang
     * @time 2016/12/1 17:21
     */
    public SubmitModel getSubmitModel() {
        return submitModel;
    }

    public void setSubmitModel(SubmitModel submitModel) {
        this.submitModel = submitModel;
    }

    /**
     * 获取任务详情对象
     *
     * @author zealjiang
     * @time 2016/12/1 17:22
     */
    public TaskDetailModel getTaskDetailModel() {
        return taskDetailModel;
    }


    public DetectionWrapper getWrapper() {
        return wrapper;
    }

    /**
     * 设置自定义相机监听
     *
     * @param iCustomCameraListener
     */
    public void setICustomCameraListener(ICustomCameraListener iCustomCameraListener) {
        this.iCustomCameraListener = iCustomCameraListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Log.i("SearchCheckActivity", "onActivityResult: Acitivity100000000");

            switch (requestCode) {
                case DetectionModel.DEFECT_ITEM:
                    iCameraListener.setPhoto();
                    break;
                case DetectionModel.DEFECT_ITEM_RECAPTURE:
                    iCameraListener.recapturePhoto(data);
                    break;
                case DetectionModel.PHOTO_REQUEST:
                    if (null != data)
                        iCustomCameraListener.setPhoto(data);
                    break;
                case DetectionModel.PHOTO_BIG_PHOTO:
                    if (data != null)
                        iCustomCameraListener.recapturePhoto(data);
                    break;
            }
        }
    }

    /**
     * created by wujj on 2017/2/15
     * 跳转评估结果检查页面之前先检查数据完整性并保存数据
     * @return
     */
    public boolean checkDataCompleteAndSave(){
        //手续信息
        boolean boo = procedureFragment.checkAndSaveData(true,true,"手续信息");
        if (!boo) {
            return false;
        }
        //车型选择
        boo = carSelectFragment.checkAndSaveData(true,true);
        if (!boo) {
            return false;
        }
        //车况检测
        boo = detectionFragment.checkDetectionDataComplete(true);
        if (!boo) {
            return false;
        }

        //其它信息
        boo = otherFragment.checkAndSaveData();
        if (!boo) {
            return false;
        }
        return true;
    }

    /**
     * 检查并提交
     * <p>
     * 手续信息界面只要必填项填完便可跳入车型选择，但在提交时，不可以空项(大多数是不可为空项，只有少数几个是必填项)都要求填写了才能提交
     * 先提交文字再提交图片压缩包
     *
     * @author zealjiang
     * @time 2016/12/4 14:28
     */
    public boolean checkAndSubmit(ResultCheckedActivity context,int ifConfirmPrice,String confirmPrice) {//confirmPrice 点击确认价格赋值为1--created by wujj on 4.21

        //提交前检查删除图片列表里的 id 和 它对应的本地图片在不在，如果本地图片在，那得将删除图片列表里面的id移除
        // 规避掉，删除图片id 加入删除列表后，又重新拍了新图片，最后都上传了 -> 李波 on 2017/9/28.
        if(submitModel != null && submitModel.getDeletePicId() != null){

            //获取图片删除列表集合  -> 李波 on 2017/9/28.
            List<String> DeletePicIdS = submitModel.getDeletePicId();

            for (int i = 0; i < DeletePicIdS.size(); i++) {
                  String pic = PadSysApp.picDirPath + DeletePicIdS.get(i)+".jpg";
                  if (FileUtils.isFileExists(pic)){
                      DeletePicIdS.remove(i);
                  }
            }
        }


        resultCheckedActivity = context;
        resultCheckedActivity.showDialog();
        //手续信息
        boolean boo = procedureFragment.checkAndSaveData(true,true,"手续信息");
        if (!boo) {
            return false;
        }
        //车型选择
        boo = carSelectFragment.checkAndSaveData(true,true);
        if (!boo) {
            return false;
        }
        //车况检测
        boo = detectionFragment.checkDetectionDataComplete(true);
        if (!boo) {
            return false;
        }

        //其它信息
        boo = otherFragment.checkAndSaveData();
        if (!boo) {
            return false;
        }

        //判断文本数据提交成功后有没有zip包需要上传,0表示没，1表示有
        if(FileUtils.isFileExists(PadSysApp.picDirPath)){
            //判断此文件夹下是否有图片
            List<File> fileList = FileUtils.listFilesInDir(new File(PadSysApp.picDirPath));
            if(fileList!=null&&fileList.size()>0){
                //有zip包
                submitModel.setHasPicZip(1);
            }else{
                //无zip包
                submitModel.setHasPicZip(0);
            }
        }else{
            //无zip包
            submitModel.setHasPicZip(0);
        }

        submitModel.setUserId(PadSysApp.getUser().getUserId());
        String json = new Gson().toJson(submitModel);

        Log.i("TAGTAGTAG", "checkAndSubmit: " + json);


        if (!TextUtils.isEmpty(json)) {
            //提交
            submitPresenter.submit(json,ifConfirmPrice,confirmPrice);
        }
        return true;
    }

    @Override
    public void submitError(String error) {
        if(resultCheckedActivity!=null) {
            resultCheckedActivity.dismissDialog();
        }
        MyToast.showShort(error);
    }

    /**
     * 所有文字信息提交返回成功
     */
    @Override
    public void requestTxtSucceed(ResponseJson<CheckPriceBean> response,int ifConfirmPrice) {
        if(resultCheckedActivity!=null) {
            resultCheckedActivity.dismissDialog();
        }

        this.ifConfirmPrice = ifConfirmPrice;
        //created by wujj 0n 4.21
        this.response = response;
        if (response.getStatus() == 100) {
            //1: 合理估值 4: 超出峰值，并且已经超出评估次数 5、6：车型库返回为空/0-则不显示预警提示，直接提交
            if (response.getMemberValue().getCode() == 5 || response.getMemberValue().getCode() == 6
                    ||response.getMemberValue().getCode() == 1 || response.getMemberValue().getCode() == 4){
                ifConfirmPrice = 1;//直接提交
            }
            if (response.getMemberValue().getCode() == 7){//事故车
                ifConfirmPrice = 1;//直接提交
            }
            if (ifConfirmPrice == 0) {//弹框
                if (resultCheckedActivity != null) {
                    resultCheckedActivity.showCheckPriceResultDialog(response);
                    return;
                }
            }
            if (mIUploadMsgLister != null) {
                mIUploadMsgLister.uploadMsgSucceed();
            }
            if (submitModel.getHasPicZip() == 1) {
                //成功后上传图片压缩包
                picUploadPresenter.initData(taskId,response.getMemberValue().getTaskStatus()+"");
                picUploadPresenter.zipUpload(resultCheckedActivity);
            } else {
                //删除本地数据库此taskId的记录
                clearTaskData();
                MyToast.showShort("提交成功");
                ifShowAlreadyConfirmMsgDialog(ifConfirmPrice);

            }
        }

    }

    /**
     * 是否显示已确认评估信息对话框-超过峰值时显示
     * created by wujj 0n 4.21
     */
    private void ifShowAlreadyConfirmMsgDialog(int ifConfirmPrice) {
        if (ifConfirmPrice == 2){
            //返回任务列表页面
            PadSysApp.isRefresh = true;
            jump(MainActivity.class, true);
            if(resultCheckedActivity!=null){
                resultCheckedActivity.finish();
            }
            return;
        }
        if (response.getMemberValue().getCode() == 3 || response.getMemberValue().getCode() == 4) { //3：超过峰值，但未超出评估次数.情况：第一次超出峰值直接点击确认按钮，第二次提交价格仍超出峰值
            final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(resultCheckedActivity);
            View view = myUniversalDialog.getLayoutView(R.layout.alreadyconfirminformationlayoutview);
            TextView tvIknow = (TextView) view.findViewById(R.id.tv_iknow);
            tvIknow.setText("知道了");
            TextView tvmessage = (TextView) view.findViewById(R.id.tvmessage);
            String messageAlert = response.getMemberValue().getMessage().getMessageAlert();
            if (!TextUtils.isEmpty(messageAlert)){
                tvmessage.setText(messageAlert);
            }
            myUniversalDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
            {
                public boolean onKey(DialogInterface dialog,
                                     int keyCode, KeyEvent event)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {
//                    dialog.dismiss();

                        //此处把dialog dismiss掉，然后把本身的activity finish掉
                        //   BarcodeActivity.this.finish();
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            });
            myUniversalDialog.setLayoutView(view);
            myUniversalDialog.show();
            tvIknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myUniversalDialog.dismiss();
                    //返回任务列表页面
                    PadSysApp.isRefresh = true;
                    jump(MainActivity.class, true);//
                    if(resultCheckedActivity!=null){
                        resultCheckedActivity.finish();
                    }
                }
            });
        } else {
            //返回任务列表页面
            PadSysApp.isRefresh = true;
            jump(MainActivity.class, true);
            if(resultCheckedActivity!=null){
                resultCheckedActivity.finish();
            }
        }
    }

    /**
     * 图片上传成功
     */
    @Override
    public void uploadSucceed() {
        //删除本地数据库此taskId的记录
        clearTaskData();
        isNeedSaveData = false;
        MyToast.showShort("提交成功");
        //created by wujj 0n 4.21
        ifShowAlreadyConfirmMsgDialog(ifConfirmPrice);
    }


    /**
     * 图片上传失败
     */
    @Override
    public void uploadFail() {
        //图片上传失败后返回到列表界面，刷新列表
//        PadSysApp.isRefresh = true;
        MyToast.showShort("提交失败");
    }


    public boolean isModify() {
        return isModify;
    }



    public LocalDetectionData getLocalDetectionData() {
        return localDetectionData;
    }

    public void setLocalDetectionData(LocalDetectionData localDetectionData) {
        this.localDetectionData = localDetectionData;
    }

    /**
     * Created by 吴佳俊 on 2016/12/12.
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText
            View v = getCurrentFocus();
            if (null!=v && isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Created by 吴佳俊 on 2016/12/12.
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * Created by 吴佳俊 on 2016/12/12.
     * 隐藏软件盘
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isShowRegistration() {
        return isShowRegistration;
    }

    public void setShowRegistration(boolean showRegistration) {
        isShowRegistration = showRegistration;
    }

    public boolean isShowDriverLicense() {
        return isShowDriverLicense;
    }

    public void setShowDriverLicense(boolean showDriverLicense) {
        isShowDriverLicense = showDriverLicense;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventProcedureModel eventProcedureModel) {
        isShowDriverLicense = eventProcedureModel.isDrivingNoneIsSelect();
        isShowRegistration = eventProcedureModel.isRegisterNoneIsSelected();
//        MyToast.showShort("行驶证有未见是否选中 :"+eventProcedureModel.isDrivingNoneIsSelect()+" 登记证未见是否选中: "+eventProcedureModel.isRegisterNoneIsSelected());
    };



    /**
     * 横竖屏切换的监听，当切换时会自动调用
     * 解决当某些情况硬件切换到了竖屏，回到界面切换回横屏时， TabLayout 布局位置拉伸问题。先隐藏 在显示 可以避免这个问题
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //切换为竖屏
        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            tabs.setVisibility(View.GONE);

        }
        //切换为横屏
        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            tabs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabs.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTimeData();
        tabs.setVisibility(View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if(isNeedSaveData){
            saveTimeData();
        }

        //清空拍照时留下的临时图片文件
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.TEMP_TAKE_PHOTO_DIR);
        FileUtils.deleteDir(mediaStorageDir);
    }

    private void applySDPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            if (PadSysApp.networkAvailable) {
                            }
                        } else {
                            MyToast.showLong("此功能需要开启SD卡读写授权，请在设置-权限管理中开启!");
                        }
                    }
                });
    }

    public boolean isDetectionComplete(){
        return detectionFragment.checkDetectionDataComplete(true);
    }

    private IUploadMsgLister mIUploadMsgLister;

    public IUploadMsgLister getmIUploadMsgLister() {
        return mIUploadMsgLister;
    }

    public void setmIUploadMsgLister(IUploadMsgLister mIUploadMsgLister) {
        this.mIUploadMsgLister = mIUploadMsgLister;
    }

    /**
     * 上传文字信息
     */
    public interface IUploadMsgLister{

        void uploadMsgSucceed();
        void uploadMsgFail();
    }

    public List<CheckItem> getCheckItemList(){
        return checkItemList;
    }

    public List<CheckItem> getUncheckItemList(){
        return uncheckItemList;
    }

    public ImportantItem getImportantItem(){
        return importantItemSearch;
    }

    public ImportantItem getUnImportantItem(){
        return unImportantItemSearch;
    }

    public String getPathRegisterMorePI() {
        return procedureFragment.getPathRegisterMorePI();
    }

    public void setPathRegisterMorePI(String path) {
        procedureFragment.setPathRegisterMorePI(path);
    }

    public String getPathRegisterMorePI2() {
        return procedureFragment.getPathRegisterMorePI2();
    }

    public void setPathRegisterMorePI2(String path) {
        procedureFragment.setPathRegisterMorePI2(path);
    }

    public String getPlanId() {
        return planId;
    }

}
