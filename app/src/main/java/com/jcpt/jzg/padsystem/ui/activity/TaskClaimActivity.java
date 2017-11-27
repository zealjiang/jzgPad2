package com.jcpt.jzg.padsystem.ui.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.TagAdapter;
import com.jcpt.jzg.padsystem.adapter.TaskClaimAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.bmw.presenter.GetBMWOrderInfPresenter;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWRecheckActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.mvpview.IBMWOrderInf;
import com.jcpt.jzg.padsystem.mvpview.IDetectionConfiguration;
import com.jcpt.jzg.padsystem.mvpview.ITaskClaimListener;
import com.jcpt.jzg.padsystem.mvpview.IVInChecked;
import com.jcpt.jzg.padsystem.presenter.DetectionConfigurationPresenter;
import com.jcpt.jzg.padsystem.presenter.TaskClaimPresenter;
import com.jcpt.jzg.padsystem.presenter.VinCheckedPresenter;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.CarVINCheck;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MD5Utils;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.NetWorkTool;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.DrivingLicenceModel;
import com.jcpt.jzg.padsystem.vo.TaskClaim;
import com.jcpt.jzg.padsystem.vo.TaskClaimList;
import com.jcpt.jzg.padsystem.vo.TaskItem;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.jcpt.jzg.padsystem.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.blankj.utilcode.utils.StringUtils.null2Length0;
import static com.jcpt.jzg.padsystem.app.PadSysApp.getUser;
import static com.jcpt.jzg.padsystem.app.PadSysApp.wrapper;
import static com.jcpt.jzg.padsystem.global.Constants.ScreenWidth;

/**
 * 任务认领
 */
public class TaskClaimActivity extends BaseActivity implements ITaskClaimListener, IDetectionConfiguration,IVInChecked,IBMWOrderInf {

    @BindView(R.id.refresh)
    MaterialRefreshLayout materialRefreshLayout;
    @BindView(R.id.rvTaskClaimList)
    RecyclerView rvTaskClaimList;

    @BindView(R.id.rvTag)
    RecyclerView rvTag;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.hsv)
    HorizontalScrollView hsv;
    private List<TaskItem> taskClaimList;
    private TaskClaimAdapter taskClaimAdapter;

    boolean isLoadingMore;
    boolean isUpdate = false;

    LinearLayoutManager layoutManager;

    TaskClaimPresenter taskClaimPresenter;

    private String requestTaskid;
    private String requestVIN;
    private String requestLastItemId = "0";

    private DetectionConfigurationPresenter configurationPresenter;
    private TaskItem currItem;
    private boolean isNeedDetail = false;//是否要请求详情数据（复检的单子可能有之前提交过的数据
    private int sourceId = 0;//标签ID（服务器返回）
    private int pageIndex = 1;//用于分页加载
    private final int PageNum = 20;//服务器每页返回20条数据
    private boolean isLoadAll = false;//是否加载完全部数据
    private boolean isClickSearch;//是否点击的查询按钮

    private LinearLayoutManager linearLayoutManager;
    private TagAdapter tagAdapter;
    private List<TaskClaimList.SourceListBean> listTagData;
    private List<TaskClaimList.SourceListBean> listTagDataInit;//针对动态添加RadioButton方式
    private Subscription subscription;

    //行驶证识别
    private final int PHOTO_REQUEST_DL = 10;//行驶证识别 拍照
    private final int PHOTO_BIG_PHOTO_DL = 11;//行驶证识别 查看大图
    private final int REQUEST_CODE_CAR_LICENCE = 12;//行驶证识别
    private ArrayList<PictureItem> pictureItemsDL = new ArrayList<>();
    private MyUniversalDialog myUniversalDialog;
    private VinCheckedPresenter vinCheckedPresenter;
    private TextView tvMsg;
    private int isAlertAccdient;
    private String accdientAlertMsg;
    private String vinCheckedFailMsg;
    private boolean ifClickContinueClaim;//是否点击的继续认领

    //宝马
    private boolean isBMWOrder = false;//是否是宝马单子 是：1,否：0
    private GetBMWOrderInfPresenter getBMWOrderInfPresenter;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_claim);
        ButterKnife.bind(this);

        configurationPresenter = new DetectionConfigurationPresenter(this);
        getBMWOrderInfPresenter = new GetBMWOrderInfPresenter(this);

        listTagData = new ArrayList<>();
        listTagDataInit = new ArrayList<>();
        //Tag  recycler 实现方式
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTag.setLayoutManager(linearLayoutManager);
        rvTag.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.DIP_8)));
        tagAdapter = new TagAdapter(this, listTagData);
        rvTag.setAdapter(tagAdapter);
        tagAdapter.setOnItemClickListener(new TagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                tagAdapter.setSelectedPosition(pos);
                tagAdapter.notifyDataSetChanged();
                sourceId = listTagData.get(pos).getId();
                loadDataInit();
            }
        });

        //清空行驶证识别要传递到手续信息的数据
        ACache.get(PadSysApp.getAppContext()).put("vin", "");
        ACache.get(PadSysApp.getAppContext()).put("plateNo", "");
    }

    @Override
    protected void setData() {
        taskClaimList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        rvTaskClaimList.setLayoutManager(layoutManager);
        taskClaimAdapter = new TaskClaimAdapter(TaskClaimActivity.this, taskClaimList);
        rvTaskClaimList.setAdapter(taskClaimAdapter);
        taskClaimAdapter.setmListener(new TaskClaimAdapter.IMyViewHolderClicks() {
            @Override
            public void onFollowStatusChange(TaskItem taskClaim) {
                requestTaskid = taskClaim.getTaskId() + "";
                requestVIN = taskClaim.getVinCode();
                currItem = taskClaim;
                if(currItem.getSpSourceId() == 1){
                    //宝马
                    isBMWOrder = true;
                }else{
                    isBMWOrder = false;
                }
                if (currItem.getOldTaskId() != 0) {//复检数据有详细数据
                    isNeedDetail = true;
                }

                showConfirmTaskClaim();
            }
        });

        taskClaimPresenter = new TaskClaimPresenter(this);
        materialRefreshLayout.setMaterialRefreshListener(
                new MaterialRefreshListener() {
                    @Override
                    public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                        isLoadAll = false;
                        isClickSearch = false;
                        isUpdate = true;
                        requestLastItemId = "0";
                        pageIndex = 1;
                        taskClaimPresenter.loadTaskClaimList(isClickSearch);
                    }

                    @Override
                    public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                });
    }

    private void refreshRadioButton(List<TaskClaimList.SourceListBean> sourceList) {
        //判断本次获取的标签数量是位置与上次是否相同，相同只更新标签内容，不同则重新生成标签
        if (listTagDataInit.size() == 0) {
            listTagDataInit.addAll(sourceList);
        } else {
            if (sourceList.size() != listTagDataInit.size()) {
                listTagDataInit.clear();
                listTagDataInit.addAll(sourceList);
            }
            int n = 0;
            for (int i = 0; i < listTagDataInit.size(); i++) {
                if (!listTagDataInit.get(i).getName().equals(sourceList.get(i).getName())) {
                    listTagDataInit.clear();
                    listTagDataInit.addAll(sourceList);
                    break;
                }
                n++;
            }
            if (n == listTagDataInit.size()) {
                //只更新内容
                for (int i = 0; i < listTagData.size(); i++) {
                    View child = radioGroup.getChildAt(i);
                    if (child instanceof RadioButton) {
                        final RadioButton radioButton = (RadioButton) child;
                        if (listTagData.get(i).getTaskCount() > 99999) {
                            radioButton.setText(listTagData.get(i).getName() + "\n99999+");
                        } else {
                            radioButton.setText(listTagData.get(i).getName() + "\n" + listTagData.get(i).getTaskCount());
                        }
                    }
                }
                return;
            }
        }
        //重新添加RadioButton
        radioGroup.removeAllViews();
        addRadioButton(sourceList);
        radioGroup.check(0);
    }

    private void addRadioButton(final List<TaskClaimList.SourceListBean> listTagData) {
        for (int i = 0; i < listTagData.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_tag_task_radiobutton, radioGroup, false);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.btnTag);
            radioButton.setId(i);
            if (i > 0) {
                int margin = getResources().getDimensionPixelSize(R.dimen.DIP_8);
                RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
                layoutParams.setMargins(margin, 0, 0, 0);
            }
            if (listTagData.get(i).getTaskCount() > 99999) {
                radioButton.setText(listTagData.get(i).getName() + "\n99999+");
            } else {
                radioButton.setText(listTagData.get(i).getName() + "\n" + listTagData.get(i).getTaskCount());
            }
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //针对动态添加的radioGroup
                    sourceId = listTagData.get(v.getId()).getId();
                    loadDataInit();
                }
            });
            radioGroup.addView(radioButton);
        }

//        resetRGroupWidth();

    }

    private void resetRGroupWidth(){
        int width;
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        radioGroup.measure(w,h);
        width = radioGroup.getMeasuredWidth();
        //int max = ScreenWidth > ScreenHeight ? ScreenWidth : ScreenHeight;
        //注意：横竖屏切换会导致ScreenWidth < ScreenHeight,因为是横屏显示，所以要用最大值做为宽度
        int maxWidth = (int) (ScreenWidth * 0.6f);
        if (width > maxWidth) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) hsv.getLayoutParams();
            layoutParams.width = maxWidth;
            hsv.setLayoutParams(layoutParams);
        }
    }

    @OnClick({R.id.rLayoutTaskClaimBack,R.id.tvSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rLayoutTaskClaimBack:
                jump(MainActivity.class, true);
                break;
            case R.id.tvSearch:
                jump(TaskClaimSearchActivity.class, false);
                break;
        }
    }

    private void loadDataInit() {
        //先取消上次的订阅 防止出现由于网慢在发出请求A标签数据此时用户又点击了B标签出现重复请求的情况
        if (subscription!=null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        if (ifClickContinueClaim){
            showDialog();
        }
        isLoadAll = false;
        isClickSearch = false;
        isUpdate = true;
        requestLastItemId = "0";
        pageIndex = 1;
        subscription = taskClaimPresenter.loadTaskClaimList(isClickSearch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataInit();
    }

    //上拉加载更多
    public void loadPage() {

        if (isLoadingMore || isLoadAll) {
            materialRefreshLayout.finishRefresh();
            materialRefreshLayout.finishRefreshLoadMore();
            if(isLoadAll){
                if(taskClaimList.size()==0){
                    MyToast.showShort("暂无数据");
                }else {
                    MyToast.showShort("加载完所有数据");
                }
            }
            return;
        }
        isClickSearch = false;
        isUpdate = false;
        isLoadingMore = true;
        if (taskClaimList != null && taskClaimList.size() > 0) {
            requestLastItemId = taskClaimList.get(taskClaimList.size() - 1).getTaskId() + "";
            subscription = taskClaimPresenter.loadTaskClaimList(isClickSearch);
        }else{
            materialRefreshLayout.finishRefresh();
            materialRefreshLayout.finishRefreshLoadMore();
        }
    }


    /**
     * 确认认领任务
     */
    public void showConfirmTaskClaim() {
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(TaskClaimActivity.this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_layout);
        TextView tvmessage = (TextView) view.findViewById(R.id.tvmessage);
        tvmessage.setText("您确认认领此任务吗？");
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        tvleft.setText("取消");
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        tvright.setText("确认认领");
        myUniversalDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
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
                if (NetWorkTool.isConnect()) {
                    taskClaimPresenter.loadTaskClaim();
                }

            }
        });

    }

    /**
     * 任务认领成功
     */
    public void showTaskClaimSuccend() {
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(TaskClaimActivity.this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_layout);
        TextView tvmessage = (TextView) view.findViewById(R.id.tvmessage);
        tvmessage.setText("任务认领成功");
        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        tvleft.setText("继续认领");
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        tvright.setText("开始检测");
        //屏蔽返回键
        myUniversalDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog,
                                 int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        myUniversalDialog.setLayoutView(view);
        myUniversalDialog.show();
        tvleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifClickContinueClaim = true;
                myUniversalDialog.cancel();
                isClickSearch = false;
                //继续认领。刷列表
                requestLastItemId = "0";
                pageIndex = 1;
                taskClaimList.clear();
                taskClaimPresenter.loadTaskClaimList(isClickSearch);
            }
        });
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifClickContinueClaim = false;
                myUniversalDialog.cancel();
                if (TextUtils.isEmpty(requestVIN)) {
                    checkVINDialog(false,null);
                } else {
                    checkVINDialog(true,requestVIN);
                }
            }
        });

    }

    /**
     * 任务认领失败，已被认领
     */
    public void showTaskClaimFail() {
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(TaskClaimActivity.this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_fail_layout);
        myUniversalDialog.setLayoutView(view);
        myUniversalDialog.show();
        myUniversalDialog.setCanceledOnTouchOutside(false);
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        isClickSearch = false;
                        myUniversalDialog.cancel();
                        //继续认领。刷列表
                        requestLastItemId = "0";
                        pageIndex = 1;
                        taskClaimList.clear();
                        taskClaimPresenter.loadTaskClaimList(isClickSearch);
                    }
                });


    }

    @Override
    public void showError(String error) {
        super.showError(error);
        materialRefreshLayout.finishRefresh();
        materialRefreshLayout.finishRefreshLoadMore();
        dismissDialog();
        if (isUpdate) {
            taskClaimList.clear();
        }

        if (taskClaimAdapter != null) {
            taskClaimAdapter.notifyDataSetChanged();
        }
        isUpdate = false;
        isLoadingMore = false;
    }

    /**
     * 确认是否有VIN；VIN是否正确；
     * @param hasVIN
     * @param strVIN
     */
    public void checkVINDialog(boolean hasVIN, String strVIN) {
        myUniversalDialog = new MyUniversalDialog(this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_check_vin_layout);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        final EditText editVin = (EditText) view.findViewById(R.id.editVin);
        tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        editVin.setTransformationMethod(new InputLowerToUpper());
        TextView tvDrivingLicense = (TextView) view.findViewById(R.id.tvDrivingLicense);
        if(isBMWOrder){
            tvDrivingLicense.setVisibility(View.GONE);
        }else{
            tvDrivingLicense.setVisibility(View.VISIBLE);
        }
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
        myUniversalDialog.setLayoutView(view,480,260);
        myUniversalDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                vinCheckedFailMsg = "";
                isClickSearch = false;
                //刷新列表
                requestLastItemId = "0";
                pageIndex = 1;
                taskClaimList.clear();
                taskClaimPresenter.loadTaskClaimList(isClickSearch);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 先校验VIN，正确，则跳转到手续页
                 */
                String vin = editVin.getText().toString();
                boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
                if(isValid){
                    if (vinCheckedPresenter == null){
                        vinCheckedPresenter = new VinCheckedPresenter(TaskClaimActivity.this);
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
            editVin.setSelection(strVIN.length());//将光标移至文字末尾   -> 李波 on 2017/9/27.
        } else {
            tvTitle.setText("请输入车架号VIN码");
        }
    }

    /**
     * 更新标签数量
     *
     * @param taskClaim
     */
    private void refreshTagNum(TaskClaimList taskClaim) {
        if (pageIndex == 1) {
            //更新tag标签数量
            List<TaskClaimList.SourceListBean> sourceList = taskClaim.getSourceList();
            if (sourceList != null && sourceList.size() > 0) {
                if (sourceList.get(0).getTaskCount() == -1) return;
                //recyclerView 实现方式
                listTagData.clear();
                listTagData.addAll(sourceList);
                tagAdapter.notifyDataSetChanged();
                //RadioGroup动态添加的方式
                refreshRadioButton(sourceList);
            }
        }
    }

    @Override
    public void taskClaimListsucceed(TaskClaimList taskClaim, boolean isClickSearch) {
        materialRefreshLayout.finishRefresh();
        materialRefreshLayout.finishRefreshLoadMore();

        if (ifClickContinueClaim){
            dismissDialog();
        }
        isLoadingMore = false;
        if (taskClaim == null) return;
        if (!isLoadingMore) {
            //只有第一次更新才刷新数量
            refreshTagNum(taskClaim);
        }

        //如果是下拉刷新则清空
        if (isUpdate) {
            taskClaimList.clear();
        }
        isUpdate = false;
        if(sourceId!=taskClaim.getSourceID()){//如果不是当前标签的数据就丢弃
            taskClaimAdapter.notifyDataSetChanged();
            return;
        }
        List<TaskItem> taskClaimLists = taskClaim.getTaskList();
        taskClaimList.addAll(taskClaimLists);
        taskClaimAdapter.notifyDataSetChanged();

        if (taskClaimLists != null && taskClaimLists.size() == 0 && taskClaimList.size() == 0) {
            MyToast.showShort("暂无数据");
            isLoadAll = true;
        } else {
            if (taskClaimLists.size() < PageNum) {
                isLoadAll = true;
            } else {
                pageIndex += 1;
            }
        }
    }

    @Override
    public void taskClaimsucceed(TaskClaim TaskClaim) {
        //任务认领成功
        PadSysApp.isRefresh = true;
        showTaskClaimSuccend();
    }

    @Override
    public void taskBackucceed(String TaskBack) {

    }

    @Override
    public void taskClaimFail(TaskClaim TaskClaim) {
        //任务认领失败，已被认领
        showTaskClaimFail();
    }

    @Override
    public Map<String, String> getTaskClaimListParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", "0");
        params.put("userId", PadSysApp.getUser().getUserId() + "");
        params.put("lastItemId", requestLastItemId);
        params.put("orderno", "");
        params.put("keyWord", "");
        params.put("sourceId", sourceId + "");
        params.put("PageIndex", pageIndex + "");
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public Map<String, String> getTaskClaimParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", PadSysApp.getUser().getUserId() + "");
        params.put("taskid", requestTaskid);
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public Map<String, String> getTaskBackParams() {
        return null;
    }


    private void checkDetectionPlan(TaskItem item) {
        configurationPresenter.getConfigureByTaskId(String.valueOf(item.getTaskId()));
    }

    /**
     * @param task     本次相关的任务对象
     * @param isModify true表示需要请求检测数据详情，false表示不需要请求检测数据详情
     * @author zealjiang
     * @time 2016/11/28 16:28
     */
    public void jumpDetectMain(TaskItem task, boolean isModify, boolean isNeedDetail) {
        if (isBMWOrder){
            if(currItem.getIsRecheck() == 2){//复检
                Intent intent = new Intent(this, BMWRecheckActivity.class);
                intent.putExtra("TaskItem", task);
                intent.putExtra("isModify", isModify);
                intent.putExtra("isNeedDetail", isNeedDetail);
                intent.putExtra("isAlertAccdient", isAlertAccdient);
                intent.putExtra("accdientAlertMsg", accdientAlertMsg);
                //再次返回刷新我的任务列表
                PadSysApp.isRefresh = true;
                startActivity(intent);
            }else {
                //如果是宝马初检  进入手续信息前获取宝马传过来的信息
                if(!isNeedDetail){
                    getBMWOrderInfPresenter.getBMWBackInfo(task.getTaskId()+"");
                }else{
                    Intent intent = new Intent(this, BMWDetectMainActivity.class);
                    intent.putExtra("TaskItem", task);
                    intent.putExtra("isModify", isModify);
                    intent.putExtra("isNeedDetail", isNeedDetail);
                    intent.putExtra("isAlertAccdient", isAlertAccdient);
                    intent.putExtra("accdientAlertMsg", accdientAlertMsg);
                    //再次返回刷新我的任务列表
                    PadSysApp.isRefresh = true;
                    startActivity(intent);
                }
            }
        }else {
            Intent intent = new Intent(TaskClaimActivity.this, DetectMainActivity.class);
            intent.putExtra("TaskItem", task);
            intent.putExtra("isModify", isModify);
            intent.putExtra("isNeedDetail", isNeedDetail);
            intent.putExtra("isAlertAccdient",isAlertAccdient);
            intent.putExtra("accdientAlertMsg",accdientAlertMsg);
            //再次返回刷新我的任务列表
            PadSysApp.isRefresh = true;
            startActivity(intent);
        }
    }


    @Override
    public void requestSucceed(DetectionWrapper data) {
        boolean isExist = DBManager.getInstance().isPlanExist(data.getPlanId(), Constants.DATA_TYPE_PLAN);
        if (!isExist) {
            wrapper = data;
            String json = new Gson().toJson(data);
            DBManager.getInstance().insertPlan(data.getPlanId(), json, Constants.DATA_TYPE_PLAN);//保存此方案

            //打印出此方案
            LogUtil.e(TAG, "网络： " + json);
        } else {
            String json = DBManager.getInstance().queryLocalPlan(data.getPlanId(), Constants.DATA_TYPE_PLAN);
            DetectionWrapper wrapper = new Gson().fromJson(json, DetectionWrapper.class);
            if (wrapper != null) {
                PadSysApp.wrapper = wrapper;
            }
            //打印出此方案
            LogUtil.e(TAG, "数据库： " + json);
        }

        jumpDetectMain(currItem, false, isNeedDetail);
    }

    @Override
    public void startCheckSucceed() {
        checkDetectionPlan(currItem);
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
        Intent intent = new Intent(this,CameraActivity.class);
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
                        Intent intent = new Intent(this, DrivingLicenceIdentifyActivity.class);
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

                //请求开始检测接口
                configurationPresenter.startDetection(currItem.getTaskId() + "");
                break;
        }
    }

    @Override
    public void requestVinCheckedSucceed(ResponseJson<VinCheckedModel> response, MyUniversalDialog myUniversalDialog,String vin) {
        myUniversalDialog.cancel();
        ACache.get(PadSysApp.getAppContext()).put("vin", vin);
        VinCheckedModel memberValue = response.getMemberValue();
        isAlertAccdient = memberValue.getIsAlertAccdient();
        accdientAlertMsg = memberValue.getAccdientAlertMsg();
        //请求开始检测接口
        configurationPresenter.startDetection(currItem.getTaskId() + "");
    }

    @Override
    public void requestVinCheckedFailed(String message, MyUniversalDialog myUniversalDialog) {
        vinCheckedFailMsg = message;
        mHandler.sendEmptyMessage(0);
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
    public void requestDataSucceed(BMWOrderInfBean data) {
        Intent intent = new Intent(this, BMWDetectMainActivity.class);
        intent.putExtra("BMWOrderInfBean", data);
        intent.putExtra("TaskItem", currItem);
        intent.putExtra("isModify", false);
        intent.putExtra("isNeedDetail", isNeedDetail);
        intent.putExtra("isAlertAccdient", isAlertAccdient);
        intent.putExtra("accdientAlertMsg", accdientAlertMsg);
        //再次返回刷新我的任务列表
        PadSysApp.isRefresh = true;
        startActivity(intent);
    }
}
