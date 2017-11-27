package com.jcpt.jzg.padsystem.bmw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.OtherTypeAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.bmw.presenter.SubmitReCheckPresenter;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.mvpview.IBMWSubmitRecheckInf;
import com.jcpt.jzg.padsystem.mvpview.IRepairLogListener;
import com.jcpt.jzg.padsystem.mvpview.ISucceedInf;
import com.jcpt.jzg.padsystem.mvpview.InsUseRecordInf;
import com.jcpt.jzg.padsystem.presenter.ActiveRepairLogPresenter;
import com.jcpt.jzg.padsystem.presenter.InsUseRecordPresenter;
import com.jcpt.jzg.padsystem.ui.activity.WebviewActivity;
import com.jcpt.jzg.padsystem.bmw.ui.fragment.BMWBeautyFragment;
import com.jcpt.jzg.padsystem.bmw.ui.fragment.BMWMaintainFragment;
import com.jcpt.jzg.padsystem.ui.fragment.WebViewFragment;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.BMWSubmitModel;
import com.jcpt.jzg.padsystem.vo.InsUseRecordModel;
import com.jcpt.jzg.padsystem.vo.Repairlog;
import com.jcpt.jzg.padsystem.vo.TaskItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * 宝马复检
 */
public class BMWRecheckActivity extends BaseActivity implements IRepairLogListener,InsUseRecordInf,IBMWSubmitRecheckInf {
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.btnMaintenance)
    Button btnMaintenance;//维保
    @BindView(R.id.btnInsUseRecord)
    Button btnInsUseRecord;//出险


    private OtherTypeAdapter otherTypeAdapter;
    private BMWMaintainFragment bmwMaintainFragment;
    private BMWBeautyFragment bmwBeautyFragment;
    private WebViewFragment webViewFragment;

    private SparseArray<Fragment> fragmentSparseArray;
    private BMWSubmitModel bmwSubmitModel;
    private String taskId;
    private String vin;
    private TaskItem taskItem;

    //维保记录
    private ActiveRepairLogPresenter activeRepairLogPresenter;
    //出险记录
    private InsUseRecordPresenter insUseRecordPresenter;
    //复检信息提交
    private SubmitReCheckPresenter submitReCheckPresenter;

    //左侧选项标签
    List<String> tagLists = new ArrayList<String>();
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bmw_recheck);
        ButterKnife.bind(this);

        activeRepairLogPresenter = new ActiveRepairLogPresenter(this);
        insUseRecordPresenter = new InsUseRecordPresenter(this);
        submitReCheckPresenter = new SubmitReCheckPresenter(this);

        taskItem = (TaskItem) getIntent().getSerializableExtra("TaskItem");
        bmwSubmitModel = new BMWSubmitModel();
        taskId = taskItem.getTaskId()+"";

        //通过行驶证识别传递过来的数据或验证VIN后传过来的
        vin = ACache.get(this.getApplicationContext()).getAsString("vin");//VIN
        // clearData();
    }

    @Override
    protected void setData() {

        fragmentSparseArray = new SparseArray<>();
        init();

    }

    public void init() {
        bmwMaintainFragment = new BMWMaintainFragment();
        bmwBeautyFragment = new BMWBeautyFragment();
        webViewFragment = new WebViewFragment();

        //添加到list中
        fragmentSparseArray.put(0, bmwMaintainFragment);
        tagLists.add("待维修项");
        fragmentSparseArray.put(1, bmwBeautyFragment);
        tagLists.add("美容");
        fragmentSparseArray.put(2, webViewFragment);
        tagLists.add("初检报告");
        addFragmentToStack(bmwMaintainFragment);
        addFragmentToStack(bmwBeautyFragment);
        addFragmentToStack(webViewFragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(bmwBeautyFragment);
        fragmentTransaction.hide(webViewFragment);
        fragmentTransaction.show(bmwMaintainFragment);
        fragmentTransaction.commitAllowingStateLoss();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        otherTypeAdapter = new OtherTypeAdapter(this, tagLists);
        recyclerView.setAdapter(otherTypeAdapter);

        otherTypeAdapter.setOnItemClickListener(new OtherTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (otherTypeAdapter.getCurrPosition() == position)
                    return;
                otherTypeAdapter.setCurrPosition(position);
                if (position == 0) {
                    switchFragment(0);
                } else if (position == 1) {
                    switchFragment(1);
                } else if (position == 2) {
                    switchFragment(2);
                    webViewFragment.setUrl(bmwSubmitModel.getReportAddr());
                }
            }
        });

    }


    /**
     * 此种显示和隐藏fragment的方式要比replace 重新初始化fragment生命周期要好，但内存占用上会大
     *
     * @param fragment
     */
    private void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.flContent, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void switchFragment(int fragmentId) {
        Fragment curFragment = fragmentSparseArray.get(fragmentId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (curFragment.isAdded()) {
        } else {
            fragmentTransaction.add(R.id.flContent, curFragment);
        }

        fragmentTransaction.show(curFragment);
        for (int i = 0; i < fragmentSparseArray.size(); i++) {
            if (fragmentId == i) {
                continue;
            }
            fragmentTransaction.hide(fragmentSparseArray.get(i));
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void toFragment(int fragmentId){
        switchFragment(fragmentId);
        otherTypeAdapter.setCurrPosition(fragmentId);
    }

    public boolean checkAndSubmit(boolean isCheck, boolean isNextCheck) {

        //待维修项
        boolean booCar = bmwMaintainFragment.checkAndSaveData(true,true,"待维修项");
        if (!booCar) {
            return false;
        }
        //美容
        boolean booConfig = bmwBeautyFragment.checkAndSaveData(true,true,"美容");
        if (!booConfig) {
            return false;
        }

        bmwSubmitModel.setTaskId(Integer.valueOf(taskId));
        bmwSubmitModel.setUserId(PadSysApp.getUser().getUserId());
        String json = new Gson().toJson(bmwSubmitModel);

        Log.i("TAGTAGTAG", "checkAndSubmit: " + json);


        if (!TextUtils.isEmpty(json)) {
            //提交
            submitReCheckPresenter.saveCosmetology(taskId,json);
        }
        return true;
    }

    public BMWSubmitModel getBmwSubmitModel() {
        return bmwSubmitModel;
    }

    public String getMTaskId() {
        return taskId + "";
    }

    /**
     * 提交成功后清楚本地数据
     */
    private void clearData(){
        DBManager.getInstance().deleteAfterSubmit(PadSysApp.getUser().getUserId(),taskId);
    }

    @OnClick({R.id.rLayoutTaskClaimBack,R.id.btnMaintenance,R.id.btnInsUseRecord,R.id.btnTrafficPeccancy})
    void onClick(View view){
        switch (view.getId()){
            case R.id.rLayoutTaskClaimBack://返回
                finish();
                break;
            case R.id.btnMaintenance://查看维保
                if(StringUtils.isEmpty(vin)){
                    return;
                }
                String userid = PadSysApp.getUser().getUserId()+"";
                activeRepairLogPresenter.loadActiveRepairLog(vin,taskId,userid,taskItem.getTaskSourceId()+"");
                break;
            case R.id.btnInsUseRecord://出险记录
                if(StringUtils.isEmpty(vin)){
                    return;
                }
                insUseRecordPresenter.requestInsUseRecord(vin,taskId);
                break;
            case R.id.btnTrafficPeccancy://违章记录
                break;

        }
    }

    //维保记录返回成功
    @Override
    public void repairLogSucceed(Repairlog repairlog) {
        if (repairlog.getRepairLogStatus() == 0) {
            MyToast.showLong("维保记录正在查询中");
        } else if (repairlog.getRepairLogStatus() == 1) {
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("url", repairlog.getRepairLogUrl());
            intent.putExtra("title", "查看维保");
            startActivity(intent);
        } else if (repairlog.getRepairLogStatus() == 2) {
            MyToast.showLong("查询无结果");
        } else if (repairlog.getRepairLogStatus() == 3) {
            MyToast.showLong("VIN码修改维保仅能查询1次，已超出次数！");
        } else if (repairlog.getRepairLogStatus() == 4) {
            MyToast.showLong("您还未查询维保，点击车架号VIN进行查询");
        }
    }

    //维保记录返回失败
    @Override
    public void showRepairLogError(String error) {
        MyToast.showShort(error);
    }

    //出险记录返回成功
    @Override
    public void insUseRecordSucceed(InsUseRecordModel insUseRecordModel) {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra("url", insUseRecordModel.getClaimsHistoryURL());
        intent.putExtra("title", "出险记录");
        startActivity(intent);
    }

    //出险记录返回错误
    @Override
    public void insUseRecordError(String msg) {
        MyToast.showShort(msg);
    }

    //复检信息提交
    @Override
    public void requestDataSucceed(String data) {
        MyToast.showShort("提交成功");
        finish();
        PadSysApp.isRefresh = true;
    }
}
