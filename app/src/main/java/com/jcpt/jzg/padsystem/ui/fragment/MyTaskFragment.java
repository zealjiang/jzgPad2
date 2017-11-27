package com.jcpt.jzg.padsystem.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.MyTagStringAdapter;
import com.jcpt.jzg.padsystem.adapter.MyTaskAdapter;
import com.jcpt.jzg.padsystem.adapter.TaskTypeAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.mvpview.IBMWOrderInf;
import com.jcpt.jzg.padsystem.mvpview.IDetectionConfiguration;
import com.jcpt.jzg.padsystem.mvpview.ITaskClaimListener;
import com.jcpt.jzg.padsystem.mvpview.ITaskStatus;
import com.jcpt.jzg.padsystem.mvpview.IUploadListener;
import com.jcpt.jzg.padsystem.mvpview.IVInChecked;
import com.jcpt.jzg.padsystem.presenter.DetectionConfigurationPresenter;
import com.jcpt.jzg.padsystem.bmw.presenter.GetBMWOrderInfPresenter;
import com.jcpt.jzg.padsystem.presenter.TaskClaimPresenter;
import com.jcpt.jzg.padsystem.presenter.UploadPresenter;
import com.jcpt.jzg.padsystem.presenter.VinCheckedPresenter;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWRecheckActivity;
import com.jcpt.jzg.padsystem.ui.activity.BackReasonWebViewActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.DrivingLicenceIdentifyActivity;
import com.jcpt.jzg.padsystem.ui.activity.WebviewActivity;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.CarVINCheck;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.utils.ShowDialogTool;
import com.jcpt.jzg.padsystem.utils.StringHelper;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.DrivingLicenceModel;
import com.jcpt.jzg.padsystem.vo.TaskClaim;
import com.jcpt.jzg.padsystem.vo.TaskClaimList;
import com.jcpt.jzg.padsystem.vo.TaskItem;
import com.jcpt.jzg.padsystem.vo.User;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.ClearableEditText;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.jcpt.jzg.padsystem.widget.WrapContentLinearLayoutManager;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.utils.StringUtils.null2Length0;
import static com.jcpt.jzg.padsystem.app.PadSysApp.getUser;
import static com.jcpt.jzg.padsystem.app.PadSysApp.wrapper;

/**
 * 复检：有详情数据的，点击"开始检测",先请求详情数据，再将详情数据带到检测流程中去；在没有提交的情况下退出当前检测，下次再进入为继续检测，这时不再请求详情数据。
 */
public class MyTaskFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ITaskClaimListener, IUploadListener,
        IDetectionConfiguration,IVInChecked,ITaskStatus,IBMWOrderInf {

    private final String TAG = "MyTaskFragment";
    @BindView(R.id.rvTaskType)
    RecyclerView rvTaskType;
    @BindView(R.id.rvTaskList)
    RecyclerView rvTaskList;
    @BindView(R.id.swipeLayout)
    MaterialRefreshLayout swipeLayout;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.etSearch)
    ClearableEditText etSearch;
    @BindView(R.id.btnSearch)
    CustomRippleButton btnSearch;

    private List<TaskItem> taskList;
    private List<String> typeList;
    private TaskTypeAdapter typeAdapter;
    private MyTaskAdapter taskAdapter;

    TaskClaimPresenter taskClaimPresenter;
    WrapContentLinearLayoutManager layoutManager;
    private DetectionConfigurationPresenter configurationPresenter;

    boolean isLoadingMore = false;
    boolean isUpdate = false;

    private String requestStatus = "-1";
    private String requestTaskid;
    private String requestLastItemId = "0";
    private int pageIndex = 1;//用于分页加载
    private final int PageNum = 20;//服务器每页返回20条数据
    private boolean isLoadAll = false;//是否加载完全部数据
    public String requestBackReason = "";
    private TaskItem currItem;
    private int currItemPosition = 0;
    private boolean isModify = false;//是否是修改的单子
    private boolean isNeedDetail = false;//是否要请求详情数据（复检的单子可能有之前提交过的数据
    private boolean isContinue = false;//是否是“继续检测”
    private boolean isClickSearch = false;//是否点击的查询按钮


    //0 未认领的任务 -1全部 1未提交 2待特批  4驳回的任务(被驳回) 6签收  30审核中
    private String[] typeString = {"全部", "未提交", "已提交", "已签收"};
    private String[] typeStringSuper = {"修改"};
    private String[] typeStringStatus = {"-1", "1", "2", "6"};

    UploadPresenter uploadPresenter;
    private ShowDialogTool showDialogTool;
    private ArrayList<String> returnReason;
    private EditText edBack;
    private int smallestScreenWidthDp;
    private Context context;

    //行驶证识别
    private final int PHOTO_REQUEST_DL = 10;//行驶证识别 拍照
    private final int PHOTO_BIG_PHOTO_DL = 11;//行驶证识别 查看大图
    private final int REQUEST_CODE_CAR_LICENCE = 12;//行驶证识别
    private ArrayList<PictureItem> pictureItemsDL = new ArrayList<>();
    private MyUniversalDialog myUniversalDialog;

    private final int REQUEST_CODE = 100;
    private boolean isBackSucceed;
    private int isSuperAppraiser;//1：是超级评估师  0：非超级评估师
    private boolean isSuper = false;//是否是超级评估师“修改”搜索出的列表
    private String keyWord = "";//搜索关键字
    private VinCheckedPresenter vinCheckedPresenter;
    private int isAlertAccdient;
    private String accdientAlertMsg;
    private String vinCheckedFailMsg;
    private TextView tvMsg;
    //宝马
    private boolean isBMWOrder = false;//是否是宝马单子 是：1,否：0
    private GetBMWOrderInfPresenter getBMWOrderInfPresenter;

    public MyTaskFragment() {
        isSuperAppraiser = PadSysApp.getUser().getIsSuperAppraiser();
        if(isSuperAppraiser==1){
            taskList = new ArrayList<>();
            typeList = Arrays.asList(typeStringSuper);
        }else{
            taskList = new ArrayList<>();
            typeList = Arrays.asList(typeString);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
        if (PadSysApp.isRefresh&&isSuperAppraiser!=1) {
            keyWord = "";
            showDialog();
            clearRequest();
        }else if(PadSysApp.isRefresh&&isSuperAppraiser==1){
            keyWord = etSearch.getText().toString().trim();
            isClickSearch = true;
            if (!StringUtils.isEmpty(keyWord)) {
                //查询
                showDialog();
                requestStatus = typeStringStatus[0];
                clearRequest();
            }
        } else {
            if (taskList != null && taskList.size() > 0) {
                taskAdapter.notifyItemChanged(currItemPosition);
            }
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showDialogTool = new ShowDialogTool();
        View view = inflater.inflate(R.layout.fragment_my_task, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        //清空行驶证识别要传递到手续信息的数据
        ACache.get(PadSysApp.getAppContext()).put("vin", "");
        ACache.get(PadSysApp.getAppContext()).put("plateNo", "");

        swipeLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //判断是不是修改
                if(isSuperAppraiser==1){
                    if(!StringUtils.isEmpty(keyWord)){
                        clearRequest();
                    }else{
                        materialRefreshLayout.finishRefresh();
                    }
                }else {
                    clearRequest();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (!isLoadingMore && !isLoadAll) {
                    //判断是不是修改
                    if(isSuperAppraiser==1){
                        if(!StringUtils.isEmpty(keyWord)){
                            loadPage();//这里多线程也要手动控制isLoadingMore
                        }else{
                            materialRefreshLayout.finishRefreshLoadMore();
                        }
                    }else {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                } else {
                    // refresh complete
                    materialRefreshLayout.finishRefresh();
                   // load more refresh complete
                    materialRefreshLayout.finishRefreshLoadMore();
                    MyToast.showLong("加载完所有数据");
                }
            }
        });
        taskClaimPresenter = new TaskClaimPresenter(this);
        uploadPresenter = new UploadPresenter(getActivity(), this);
        configurationPresenter = new DetectionConfigurationPresenter(this);
        getBMWOrderInfPresenter = new GetBMWOrderInfPresenter(this);
        PadSysApp.isRefresh = true;
        User user = PadSysApp.getUser();
        if (user == null) {
            user = (User) ACache.get(PadSysApp.getAppContext()).getAsObject(Constants.KEY_ACACHE_USER);
        }
        returnReason = user.getReturnReason();
        Configuration config = getResources().getConfiguration();
        smallestScreenWidthDp = config.smallestScreenWidthDp;


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWord = etSearch.getText().toString().trim();
                isClickSearch = true;
                if (!StringUtils.isEmpty(keyWord)) {
                    //查询
                    showDialog();
                    requestStatus = typeStringStatus[0];
                    clearRequest();
                } else {
                    MyToast.showShort("请输入查询关键词");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvTaskType.setLayoutManager(new LinearLayoutManager(getActivity()));
        typeAdapter = new TaskTypeAdapter(getContext(),typeList);
        rvTaskType.setAdapter(typeAdapter);

        if(isSuperAppraiser==1){
            typeAdapter.setCurrPosition(typeList.size()-1);
            llSearch.setVisibility(View.VISIBLE);
            isSuper = true;
        }

        typeAdapter.setOnItemClickListener(new TaskTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (typeAdapter.getCurrPosition() == position)
                    return;
                if (NetWorkTool.isConnect()) {
                    if (RecyclerView.SCROLL_STATE_IDLE == rvTaskList.getScrollState() && isUpdate == false && isLoadingMore == false) {
                        typeAdapter.setCurrPosition(position);
                        if (isSuperAppraiser == 1) {
                            llSearch.setVisibility(View.VISIBLE);
                            isSuper = true;
                            keyWord = etSearch.getText().toString().trim();
                            if (StringUtils.isEmpty(keyWord)) {
                                taskList.clear();
                            } else {
                                showDialog();
                                requestStatus = typeStringStatus[0];
                                clearRequest();
                            }

                        } else {
                            llSearch.setVisibility(View.GONE);
                            isSuper = false;
                            showDialog();
                            requestStatus = typeStringStatus[position];
                            keyWord = "";
                            clearRequest();
                        }
                    }
                }
            }
        });

        layoutManager = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTaskList.setLayoutManager(layoutManager);
        taskAdapter = new MyTaskAdapter(getContext(),taskList,this);
        rvTaskList.setAdapter(taskAdapter);


        rvTaskList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isUpdate) {
                    return true;
                } else {
                    return false;
                }

            }
        });

        taskAdapter.setOnClickListener(new MyTaskAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, int pos) {
                if (taskList != null && taskList.size() > 0) {
                    currItemPosition = position;
                    currItem = taskList.get(position);
                    if(currItem.getSpSourceId() == 1){
                        //宝马
                        isBMWOrder = true;
                    }else{
                        isBMWOrder = false;
                    }
                    if(isBMWOrder){
                        if (pos == Constants.BACK) {
                            //退回
                            showBack(currItem);
                        } else if (pos == Constants.CONTINUEDETECTION) {
                            //继续检测
                            goToCheckDetectionPlan();
                        } else if (pos == Constants.CHECKREPORT) {
                            //查看报告
                            checkReport(currItem);
                        } else if (pos == Constants.MODIFICATION) {
                            //修改
                            isModify = true;
                            isContinue = false;
                            isNeedDetail = false;
                            if (currItem.getOldTaskId() != 0) {//复检数据有详细数据
                                isNeedDetail = true;
                            }
                            checkDetectionPlan(currItem, isModify, isNeedDetail, isContinue);
                        } else if (pos == Constants.CHECKREASON) {
                            //查看原因
                            showReason(currItem);
                        } else if (pos == Constants.UPLOAD) {
                            //继续上传
                            uploadPresenter.initData(currItem.getTaskId() + "", "");
                            uploadPresenter.zipUpload(getActivity());

                        } else if (pos == Constants.START) {
                            isNeedDetail = false;
                            isContinue = false;
                            //开始检测
                            if (currItem.getOldTaskId() != 0) {//复检数据有详细数据
                                isNeedDetail = true;
                            }

                            //开始检测删除以前旧图片
                            FileUtils.deleteDir(Constants.ROOT_DIR + File.separator + currItem.getTaskId() + File.separator);
                            requestTaskid = currItem.getTaskId() + "";

                            /**
                             * 1.下单时已输入VIN的订单，此处显示确认VIN弹窗；
                             * 2.下单时未输入VIN的订单，此处显示输入VIN弹窗.
                             */
                            if (TextUtils.isEmpty(currItem.getVinCode())) {
                                checkVINDialog(false, null);
                            } else {
                                checkVINDialog(true, currItem.getVinCode());
                            }

                        } else if (pos == Constants.LONG) {
                            //保存方案为本地文件
                            System.out.println("当前任务方案id==" + currItem.getPlanId());
                            StringHelper.SDPlan();
                        }
                    }else {
                        if (pos == Constants.BACK) {
                            //退回
                            showBack(currItem);
                        } else if (pos == Constants.CONTINUEDETECTION) {
                            //继续检测
                            goToCheckDetectionPlan();
                        } else if (pos == Constants.CHECKREPORT) {
                            //查看报告
                            checkReport(currItem);
                        } else if (pos == Constants.MODIFICATION) {
                            //修改
                            isModify = true;
                            isContinue = false;
                            isNeedDetail = false;
                            if (currItem.getOldTaskId() != 0) {//复检数据有详细数据
                                isNeedDetail = true;
                            }
                            checkDetectionPlan(currItem, isModify, isNeedDetail, isContinue);
                        } else if (pos == Constants.CHECKREASON) {
                            //查看原因
                            showReason(currItem);
                        } else if (pos == Constants.UPLOAD) {
                            //继续上传
                            uploadPresenter.initData(currItem.getTaskId() + "", "");
                            uploadPresenter.zipUpload(getActivity());

                        } else if (pos == Constants.START) {
                            isNeedDetail = false;
                            isContinue = false;
                            //开始检测
                            if (currItem.getOldTaskId() != 0) {//复检数据有详细数据
                                isNeedDetail = true;
                            }

                            //开始检测删除以前旧图片
                            FileUtils.deleteDir(Constants.ROOT_DIR + File.separator + currItem.getTaskId() + File.separator);
                            requestTaskid = currItem.getTaskId() + "";

                            /**
                             * 1.下单时已输入VIN的订单，此处显示确认VIN弹窗；
                             * 2.下单时未输入VIN的订单，此处显示输入VIN弹窗.
                             */
                            if (TextUtils.isEmpty(currItem.getVinCode())) {
                                checkVINDialog(false, null);
                            } else {
                                checkVINDialog(true, currItem.getVinCode());
                            }

                        } else if (pos == Constants.LONG) {
                            //保存方案为本地文件
                            System.out.println("当前任务方案id==" + currItem.getPlanId());
                            StringHelper.SDPlan();
                        }
                    }
                }
            }

            @Override
            public void onTtemPhoto(int position) {
                currItem = taskList.get(position);
                currItemPosition = position;
                checkReport(currItem);
            }
        });

    }

    /**
     * 确认是否有VIN；VIN是否正确；
     * @param hasVIN
     * @param strVIN
     */
    public void checkVINDialog(boolean hasVIN, String strVIN) {
        myUniversalDialog = new MyUniversalDialog(getActivity());
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_check_vin_layout);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        final EditText editVin = (EditText) view.findViewById(R.id.editVin);
        editVin.setTransformationMethod(new InputLowerToUpper());
        TextView tvDrivingLicense = (TextView) view.findViewById(R.id.tvDrivingLicense);
        if(isBMWOrder){
            tvDrivingLicense.setVisibility(View.GONE);
        }else{
            tvDrivingLicense.setVisibility(View.VISIBLE);
        }
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
        myUniversalDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        myUniversalDialog.setLayoutView(view,480,260);
        myUniversalDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                vinCheckedFailMsg = "";
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void onClick(View v) {
                /**
                 * 先校验VIN，正确，则跳转到手续页
                 */
                String vin = editVin.getText().toString();
                boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
                if(isValid){
                    if (vinCheckedPresenter == null){
                        vinCheckedPresenter = new VinCheckedPresenter(MyTaskFragment.this);
                    }
                    vinCheckedPresenter.getVinCheckedResult(getUser().getUserId()+"", vin,currItem.getTaskId()+"",myUniversalDialog);
                }else{
                    MyToast.showLong("请输入正确VIN");
                }
            }
        });
        tvDrivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 行驶证识别
                carLicenceRecognize();
            }
        });
        if (hasVIN) {
            tvTitle.setText("请确认车架号VIN码是否正确？");
            editVin.setText(strVIN);
            editVin.setSelection(strVIN.length());//将光标移至文字末尾   -> 李波 on 2017/9/12.
        } else {
            tvTitle.setText("请输入车架号VIN码");
        }
    }

    private void checkDetectionPlan(TaskItem item, boolean isModify, boolean isNeedDetail, boolean isContinue) {
        LogUtil.e(TAG, "taskId :" + item.getTaskId() + "   planId :" + item.getPlanId());
        if (wrapper != null && wrapper.getPlanId().equals(item.getPlanId())) {//内存中存在对应planid的检测方案，则直接用
            //打印出此方案
            String json = new Gson().toJson(wrapper);
            LogUtil.e(TAG, "内存： " + json);

            //如果内存方案里无基本照片集合，那就重新获取方案  -> 李波 on 2017/11/21.
            if (wrapper.getPictureList() == null || wrapper.getPictureList().size() == 0) {
                DBManager.getInstance().deletePlan(wrapper.getPlanId(), Constants.DATA_TYPE_PLAN);
                configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
            } else {
                jumpDetectMain(item, isModify, isNeedDetail, isContinue);
            }

        } else {
            //如果是0方案，表示未配置方案，从网络读取
            if ("0".equals(item.getPlanId())) {
                LogUtil.e(TAG, "0方案");
                configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
                if (DBManager.getInstance().isPlanExist("0", Constants.DATA_TYPE_PLAN)) {
                    DBManager.getInstance().deletePlan("0", Constants.DATA_TYPE_PLAN);
                }
            } else {
                boolean isExist = DBManager.getInstance().isPlanExist(item.getPlanId(), Constants.DATA_TYPE_PLAN);//查数据库中是否有检测方案数据
                if (!isExist) {//数据库中不存在，则从网络获取
                    configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
                } else {

                    String json = DBManager.getInstance().queryLocalPlan(item.getPlanId(), Constants.DATA_TYPE_PLAN);
                    //打印出此方案
                    LogUtil.e(TAG, "数据库： " + json);
                    if (TextUtils.isEmpty(json)) {
                        DBManager.getInstance().deleteUselessData();
                        configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
                        return;
                    }
                    DetectionWrapper wrapper = new Gson().fromJson(json, DetectionWrapper.class);
                    if (wrapper != null) {
                        List<CheckPositionItem> checkPositionList  = wrapper.getCheckPositionList();
                        if(checkPositionList == null ||checkPositionList.size() == 0){//没有检测项，重新获取方案
                            DBManager.getInstance().deletePlan(wrapper.getPlanId(), Constants.DATA_TYPE_PLAN);
                            configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
                        } else if (wrapper.getPictureList() == null || wrapper.getPictureList().size() == 0) {// 没有基本照片重新获取方案
                            DBManager.getInstance().deletePlan(wrapper.getPlanId(), Constants.DATA_TYPE_PLAN);
                            configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
                        } else {
                            PadSysApp.wrapper = wrapper;
                            jumpDetectMain(item,isModify, isNeedDetail, isContinue);
                        }
                    }else{
                        DBManager.getInstance().deleteUselessData();
                        configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
                        return;
                    }
                }
            }
        }
    }


    /**
     * 显示退回编辑框
     *
     * @param task
     */
    public void showBack(final TaskItem task) {

        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(getActivity());
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_back_layout);
        TextView tvPhingguNo = (TextView) view.findViewById(R.id.tvPhingguNo);
        tvPhingguNo.setText("评估编号：" + task.getOrderNo());
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        TagFlowLayout tflReturnReason = (TagFlowLayout) view.findViewById(R.id.tflReturnReason);
        edBack = (EditText) view.findViewById(R.id.edBack);
        setTag(returnReason, tflReturnReason);
        showDialogTool.inputRestrict(edBack, true, false, false, false);
        myUniversalDialog.setLayoutView(view);
        WindowManager.LayoutParams dialogLayoutParams = myUniversalDialog.getWindow().getAttributes();
//        //兼顾Pad
        if (smallestScreenWidthDp == 600) {
            int padHorizontalwidth = Constants.ScreenWidth > Constants.ScreenHeight ? Constants.ScreenWidth : Constants.ScreenHeight;
            int padHorizontalheight = Constants.ScreenWidth > Constants.ScreenHeight ? Constants.ScreenHeight : Constants.ScreenWidth;
            dialogLayoutParams.width = padHorizontalwidth * 2 / 3;
            dialogLayoutParams.height = padHorizontalheight * 2 / 3;
        }
        myUniversalDialog.getWindow().setAttributes(dialogLayoutParams);
        myUniversalDialog.show();
        tvleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                requestBackReason = "";
            }
        });
        //屏蔽换行键
        edBack.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etReturnReason = edBack.getText().toString();
                if (!TextUtils.isEmpty(etReturnReason)) {
                    if (etReturnReason.length() < 3) {
                        MyToast.showShort("您输入的退回原因小于3位");
                        return;
                    }
                    if (TextUtils.isEmpty(requestBackReason)) {
                        requestBackReason = etReturnReason;
                    } else {
                        requestBackReason = requestBackReason + "," + etReturnReason;
                    }
                    myUniversalDialog.cancel();
                    requestTaskid = task.getTaskId() + "";
                    taskClaimPresenter.backTask();
                    requestBackReason = "";
                } else {
                    if (TextUtils.isEmpty(requestBackReason)) {
                        MyToast.showShort("请选择退回原因");
                    } else {
                        myUniversalDialog.cancel();
                        requestTaskid = task.getTaskId() + "";
                        taskClaimPresenter.backTask();
                        requestBackReason = "";
                    }
                }
            }
        });
    }

    public void setTag(ArrayList<String> vals, TagFlowLayout tagFlowLayout) {
        MyTagStringAdapter adapter = new MyTagStringAdapter(vals, tagFlowLayout, getActivity());
        tagFlowLayout.setAdapter(adapter);
        setTagListener(tagFlowLayout);
    }

    private void setTagListener(final TagFlowLayout tagFlowLayout) {
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {
                TagView tagView = (TagView) tagFlowLayout.getChildAt(position);
                TextView tv = (TextView) tagView.getTagView();
                if (tagView.isChecked()) {
                    requestBackReason = tv.getText().toString();
                } else {
                    requestBackReason = "";
                }
                return false;
            }
        });
    }

    /**
     * 显示驳回原因
     *
     * @param task
     */
    public void showReason(final TaskItem task) {

        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(getActivity());
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_reason_layout);
        TextView tvPhingguNo = (TextView) view.findViewById(R.id.tvPhingguNo);
        tvPhingguNo.setText("评估编号：" + task.getOrderNo());
        TextView tvFushen = (TextView) view.findViewById(R.id.tvFushen);
        tvFushen.setText("复核评估师：" + task.getReCheckUserName());
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        final TextView tvReason = (TextView) view.findViewById(R.id.tvReason);
        final TextView tvCarFullName = (TextView) view.findViewById(R.id.tvCarFullName);
        tvReason.setText(task.getBackReason());
        tvCarFullName.setText(task.getCarName());
        myUniversalDialog.setLayoutView(view);
        myUniversalDialog.show();
        tvleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
            }
        });
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                isModify = true;
                isContinue = false;
                checkDetectionPlan(currItem, isModify, isNeedDetail, isContinue);
            }
        });

    }

    //下拉刷新
    @Override
    public void onRefresh() {
//        clearRequest();
    }

    //上拉加载更多
    public void loadPage() {

        isLoadingMore = true;
        if (taskList.size() > 0) {
            requestLastItemId = taskList.get(taskList.size() - 1).getTaskId() + "";
        }
        taskClaimPresenter.loadTaskClaimList(isClickSearch);

    }

    @Override
    public void taskClaimListsucceed(TaskClaimList taskClaim, boolean isClickSearch) {
        dismissDialog();
        if (taskClaim == null) return;
        try {
            if (isUpdate) {
                taskList.clear();
            }
            List<TaskItem> taskClaimLists = taskClaim.getTaskList();
            taskList.addAll(taskClaimLists);
            taskAdapter.setDatas(taskList);

            //超级用户修改列表进入可修改
            taskAdapter.setIsSuperModify(isSuper);

            taskAdapter.notifyDataSetChanged();

            // refresh complete
            swipeLayout.finishRefresh();
            swipeLayout.finishRefreshLoadMore();
            isUpdate = false;
            isLoadingMore = false;
            if (PadSysApp.isRefresh) {
                PadSysApp.isRefresh = false;
            }

            if (taskClaimLists != null && taskClaimLists.size() == 0 && taskList.size() == 0) {
                MyToast.showShort("暂无数据");
            } else {
                if (taskClaimLists.size() < PageNum) {
                    //MyToast.showShort("加载完所有数据");
                    isLoadAll = true;
                } else {
                    pageIndex += 1;
                }
            }

        } catch (Exception e) {
            System.out.println("刷新列表出错");
        }
    }

    @Override
    public void taskClaimsucceed(TaskClaim TaskClaim) {

    }

    @Override
    public void taskBackucceed(String memberValueUrl) {
        MyToast.showLong("退回成功");
        requestBackReason = "";
        //清除数据
        clearRequest();
        //清除数据
        //删除本地数据库此taskId的记录
        DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(), requestTaskid);
        uploadPresenter.clearUploadFile(requestTaskid);
        //删除本地数据库此taskId的记录
        DBManager.getInstance().deleteAfterSubmit(getUser().getUserId(), requestTaskid);
        uploadPresenter.clearUploadFile(requestTaskid);
        if (!TextUtils.isEmpty(memberValueUrl)){
            Intent intent = new Intent(getContext(),BackReasonWebViewActivity.class);
            intent.putExtra("url",memberValueUrl);
            intent.putExtra("title","退回原因调研");
            startActivity(intent);
        }

    }



    @Override
    public void taskClaimFail(TaskClaim TaskClaim) {

    }


    public void setRefreshParams(){
        requestLastItemId = "0";
        keyWord = "";
        pageIndex = 1;
    }

    @Override
    public Map<String, String> getTaskClaimListParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", requestStatus);
        params.put("userId", getUser().getUserId() + "");
        params.put("lastItemId", requestLastItemId);
        params.put("keyWord", keyWord);
        params.put("sourceId", "0");//表示全部
        params.put("PageIndex", pageIndex + "");
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public Map<String, String> getTaskClaimParams() {
        return null;
    }

    @Override
    public Map<String, String> getTaskBackParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskid", requestTaskid);
        params.put("userId", getUser().getUserId() + "");
        params.put("reason", requestBackReason);
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public void showError(String error) {
        //退回失败 requestBackReason = "";
        requestBackReason = "";
        isUpdate = false;
        isLoadingMore = false;
        PadSysApp.isRefresh = false;
        swipeLayout.finishRefresh();
        swipeLayout.finishRefreshLoadMore();
        if(!StringUtils.isEmpty(error)){
            MyToast.showShort(error);
        }

        dismissDialog();
        if (error.equals("任务状态错误")){
            //清除数据
            //删除本地数据库此taskId的记录
            DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(), requestTaskid);
            uploadPresenter.clearUploadFile(requestTaskid);
        }
    }

    @Override
    public void showDialog() {
        showDialogTool.showLoadingDialog(getActivity());
    }

    @Override
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }


    /**
     * @param isModify     true表示需要请求检测数据详情，false表示不需要请求检测数据详情
     * @param isNeedDetail true表示需要请求检测数据详情，false表示不需要请求检测数据详情(只针对开始检测，如果isModify为true，则此参数忽略)
     * @author zealjiang
     * @time 2016/11/28 16:28
     */
    public void jumpDetectMain(TaskItem taskItem,boolean isModify, boolean isNeedDetail, boolean isContinue) {
        if(isBMWOrder){
            if(currItem.getIsRecheck() == 2){ //复检
                Intent intent = new Intent(getActivity(), BMWRecheckActivity.class);
                intent.putExtra("TaskItem", taskItem);
                intent.putExtra("isModify", isModify);
                intent.putExtra("isContinue", isContinue);
                intent.putExtra("isNeedDetail", isNeedDetail);
                intent.putExtra("isAlertAccdient", isAlertAccdient);
                intent.putExtra("accdientAlertMsg", accdientAlertMsg);
                startActivity(intent);
            }else{
                //如果是宝马初检  进入手续信息前获取宝马传过来的信息
                if(!isNeedDetail && !isContinue){
                    getBMWOrderInfPresenter.getBMWBackInfo(taskItem.getTaskId()+"");
                }else{
                    Intent intent = new Intent(getActivity(), BMWDetectMainActivity.class);
                    intent.putExtra("TaskItem", taskItem);
                    intent.putExtra("isModify", isModify);
                    intent.putExtra("isContinue", isContinue);
                    intent.putExtra("isNeedDetail", isNeedDetail);
                    intent.putExtra("isAlertAccdient", isAlertAccdient);
                    intent.putExtra("accdientAlertMsg", accdientAlertMsg);
                    startActivity(intent);
                }
            }
        }else {
            Intent intent = new Intent(getActivity(), DetectMainActivity.class);
            intent.putExtra("TaskItem", taskItem);
            intent.putExtra("isModify", isModify);
            intent.putExtra("isContinue", isContinue);
            intent.putExtra("isNeedDetail", isNeedDetail);
            intent.putExtra("isAlertAccdient", isAlertAccdient);
            intent.putExtra("accdientAlertMsg", accdientAlertMsg);
            startActivity(intent);
        }
    }


    @Override
    public void uploadSucceed() {
        //上传成功
        MyToast.showLong("上传成功");
        //刷新列表
        clearRequest();

    }

    @Override
    public void uploadFail() {
        MyToast.showLong("上传失败");
    }

    @Override
    public void requestSucceed(DetectionWrapper data) {
        wrapper = data;

        List<CheckPositionItem> checkPositionList  = wrapper.getCheckPositionList();
        if(checkPositionList == null ||checkPositionList.size() == 0){//没有检测项，重新获取方案
            MyToast.showShort("此方案无检测项");
            return;
        }

        String json = new Gson().toJson(data);
        DBManager.getInstance().insertPlan(data.getPlanId(), json, Constants.DATA_TYPE_PLAN);//保存此方案
        jumpDetectMain(currItem, isModify, isNeedDetail, isContinue);
        //打印出此方案
        LogUtil.e(TAG, "网络： " + json);
    }


    //开始检测返回成功
    @Override
    public void startCheckSucceed() {
        isModify = false;
        isContinue = false;
        checkDetectionPlan(currItem, isModify, isNeedDetail, isContinue);
    }


    /**
     * 刷新列表
     */
    public void clearRequest() {
        isLoadAll = false;
        if (!isLoadingMore) {
            isUpdate = true;
            requestLastItemId = "0";
            pageIndex = 1;
            taskClaimPresenter.loadTaskClaimList(isClickSearch);
            if (taskList != null && taskList.size() > 0) {
                rvTaskList.scrollToPosition(0);
            }
        }
    }

    /**
     * 查看报告
     *
     * @param mCurrItem
     */
    public void checkReport(TaskItem mCurrItem) {

        Intent intent = new Intent(getActivity(), WebviewActivity.class);
        intent.putExtra("url", mCurrItem.getReportLink());
        intent.putExtra("title", "查看报告");
        if(!isBMWOrder){
            intent.putExtra("leftRMargin", 10);
        }
        startActivity(intent);
        LogUtil.e("url", mCurrItem.getReportLink());
    }

    /**
     * 行驶证识别
     * TODO 暂时不考虑复检带照片
     */
    private void carLicenceRecognize(){
        pictureItemsDL.clear();
        pictureItemsDL.add(new PictureItem("18","行驶证正本正面",""));

        takePic(pictureItemsDL,PHOTO_REQUEST_DL,PHOTO_BIG_PHOTO_DL);
    }

    private void takePic(ArrayList<PictureItem> picList, int requestCodeCamera, int requestCodePreview){
            userCamera(0,Constants.CAPTURE_TYPE_SINGLE,picList,requestCodeCamera);
    }

    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position,int capture_type,ArrayList<PictureItem> listPic,int requestCode){
        Intent intent = new Intent(context,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId",requestTaskid);
        intent.putExtra(Constants.CAPTURE_TYPE,capture_type);//拍摄模式，是单拍

        ArrayList<PictureItem> singleList = new ArrayList<>();
        PictureItem newPic = new PictureItem();
        newPic.setPicId(listPic.get(position).getPicId());
        newPic.setPicName(listPic.get(position).getPicName());
        singleList.add(newPic);
        intent.putExtra("picList",singleList);
        intent.putExtra("position",0);

        startActivityForResult(intent,requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK || null == data)
            return;
        switch (requestCode){
            case PHOTO_REQUEST_DL://行驶证识别 拍照
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_SINGLE);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if(picList!=null && picList.size()>0){
                    if(captureType==Constants.CAPTURE_TYPE_SINGLE){
                        String path = picList.get(0).getPicPath();
                        if(TextUtils.isEmpty(path)){
                            return;
                        }
                        pictureItemsDL.get(0).setPicPath(path);

                        //行驶证识别
                        //判断是否有行驶证正本正面图片
                        Intent intent = new Intent(context, DrivingLicenceIdentifyActivity.class);
                        intent.putExtra("path", path);
                        intent.putExtra("isShowVIN", true);//是否显示VIN
                        intent.putExtra("taskId",currItem.getTaskId()+"");
                        startActivityForResult(intent, REQUEST_CODE_CAR_LICENCE);
                    }
                }
                break;
            case REQUEST_CODE_CAR_LICENCE://行驶证识别信息
                DrivingLicenceModel drivingLicenceModel = (DrivingLicenceModel)data.getSerializableExtra("DrivingLicenceModel");
                MyToast.showShort("成功返回行驶证识别信息");
                //车牌号码
                String plateNo = null2Length0(drivingLicenceModel.getPlateNo());
                //vin
                String vin = null2Length0(drivingLicenceModel.getVIN());

                //目的是将数据传递到手续信息
                ACache.get(PadSysApp.getAppContext()).put("vin", vin);
                ACache.get(PadSysApp.getAppContext()).put("plateNo", plateNo);

                //vin check
                accdientAlertMsg = data.getStringExtra("accdientAlertMsg");
                isAlertAccdient = data.getIntExtra("isAlertAccdient",0);

                //跳到手续信息
                myUniversalDialog.cancel();
                isModify = false;
                isContinue = false;

                //请求开始检测接口
                configurationPresenter.startDetection(currItem.getTaskId() + "");
            break;
            case REQUEST_CODE:
                isBackSucceed = data.getBooleanExtra("isBackSucceed", false);
                //退回成功,刷新列表
                if (isBackSucceed){
//                MyToast.showLong("退回成功");
                    //清除数据
                    clearRequest();
                    //删除本地数据库此taskId的记录
                    DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(), requestTaskid);
                    uploadPresenter.clearUploadFile(requestTaskid);
                }
                break;
        }
    }

    @Override
    public void requestVinCheckedSucceed(ResponseJson<VinCheckedModel> response,MyUniversalDialog myUniversalDialog,String vin) {
        ACache.get(PadSysApp.getAppContext()).put("vin", vin);
        VinCheckedModel memberValue = response.getMemberValue();
        isAlertAccdient = memberValue.getIsAlertAccdient();
        accdientAlertMsg = memberValue.getAccdientAlertMsg();
        myUniversalDialog.cancel();
        //请求开始检测接口
        configurationPresenter.startDetection(currItem.getTaskId() + "");

    }

    @Override
    public void requestVinCheckedFailed(String message,MyUniversalDialog myUniversalDialog) {
        vinCheckedFailMsg = message;
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 获取当前列表状态
     * @return
     */
    public String getRequestStatus(){
        return requestStatus;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                tvMsg.setText(vinCheckedFailMsg);
            }else {
                myUniversalDialog.cancel();
            }
        }
    };

    @Override
    public void requestTaskStatusSucceed(ResponseJson<Integer> response) {
        if (response.getMemberValue() == 1){
            goToCheckDetectionPlan();
        }else {
            MyToast.showShort(response.getMsg());
        }
    }

    public void goToCheckDetectionPlan() {
        isModify = false;
        isNeedDetail = false;
        isContinue = true;
        checkDetectionPlan(currItem, isModify, isNeedDetail, isContinue);
    }

    @Override
    public void requestTaskStatusFailed(String Msg) {
        MyToast.showShort(Msg);
            keyWord = "";
            showDialog();
            clearRequest();
    }

    @Override
    public void requestDataSucceed(BMWOrderInfBean data) {
        //进入手续信息前获取宝马传过来的信息
        Intent intent = new Intent(getActivity(), BMWDetectMainActivity.class);
        intent.putExtra("BMWOrderInfBean", data);
        intent.putExtra("TaskItem", currItem);
        intent.putExtra("isModify", isModify);
        intent.putExtra("isContinue", isContinue);
        intent.putExtra("isNeedDetail", isNeedDetail);
        intent.putExtra("isAlertAccdient", isAlertAccdient);
        intent.putExtra("accdientAlertMsg", accdientAlertMsg);
        startActivity(intent);
    }
}
