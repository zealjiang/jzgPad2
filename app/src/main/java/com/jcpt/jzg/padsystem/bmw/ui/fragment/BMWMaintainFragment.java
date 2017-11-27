package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.BMWMaintainAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.presenter.GetRepairPresenter;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWRecheckActivity;
import com.jcpt.jzg.padsystem.mvpview.IBMWRepairListInf;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.BMWMaintainModel;
import com.jcpt.jzg.padsystem.vo.BMWReCheckBean;
import com.jcpt.jzg.padsystem.vo.BMWSubmitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 宝马复检待维修项
 */
public class BMWMaintainFragment extends BaseFragment implements IBMWRepairListInf {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private BMWMaintainAdapter adapter;
    private String taskId;
    private BMWSubmitModel bmwSubmitModel;
    private BMWMaintainModel bmwMaintainModel;
    private List<BMWReCheckBean.BMWRepairBean> listBwmOutfit;
    private GetRepairPresenter getRepairListPresenter;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //2
        View view = inflater.inflate(R.layout.fragment_bmw_maintain, container, false);
        ButterKnife.bind(this, view);

        getRepairListPresenter = new GetRepairPresenter(this);
        listBwmOutfit = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BMWMaintainAdapter(this.getContext(),listBwmOutfit);
        recyclerView.setAdapter(adapter);


        if (bmwSubmitModel == null) {
            bmwSubmitModel = ((BMWRecheckActivity) getActivity()).getBmwSubmitModel();
        }
        taskId = ((BMWRecheckActivity) getActivity()).getMTaskId();

        return view;
    }


    @Override
    protected void setView() {
        //3
        getPlanAndData();
    }
    @Override
    protected void initData() {
        //1
    }

    @OnClick({R.id.tvNext})
    void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                boolean isOk = checkAndSaveData(true,true,"待维修项");
                if(!isOk){
                    return;
                }
                //跳到美容
                ((BMWRecheckActivity) getActivity()).toFragment(1);
                break;
        }
    }

    private void getPlanAndData() {

        if(taskId==null||bmwSubmitModel==null){
            return;
        }

        //从本地数据库中获取此用户下的taskId的待维修项数据，如果数据为空则请求服务器
        List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_BMW_MAINTAIN, PadSysApp.getUser().getUserId());
        if(list!=null&&list.size()>0){
            String json = list.get(0).getJson();
            if(!TextUtils.isEmpty(json)){
                bmwMaintainModel = new Gson().fromJson(json,BMWMaintainModel.class);
                showData();
            }
        }
        //从服务器请求数据
        if(bmwMaintainModel == null) {
            taskId = ((BMWRecheckActivity)getActivity()).getMTaskId();
            getRepairListPresenter.getBMWBackInfo(taskId);
        }
    }

    /**
     * 显示数据
     */
    private void showData(){
        if(bmwMaintainModel!=null){
            bmwSubmitModel.setReportAddr(bmwMaintainModel.getReportAddr());
            listBwmOutfit.clear();
            listBwmOutfit.addAll(bmwMaintainModel.getOutfit());
            bmwMaintainModel.setOutfit(listBwmOutfit);
            if(adapter!=null) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 检查并保存数据
     * @param isCheck 提交时不可为空的项是否都已经填写是否检查
     * @param isNextCheck 必填项是否检查
     */
    public boolean checkAndSaveData(boolean isCheck,boolean isNextCheck,String hintHead){
        if(adapter==null){//此Fragment还没初始化
            return true;
        }

        if(listBwmOutfit==null){
            return false;
        }
        //获取选中的项
        if(isCheck || isNextCheck){
            for (int i = 0; i < listBwmOutfit.size(); i++) {
                if(listBwmOutfit.get(i).getRepairResult() == -1){
                    MyToast.showShort(listBwmOutfit.get(i).getRepairDes()+"还没检查，请完成");
                    return false;
                }
            }
        }

        bmwSubmitModel.setSpRepairList(listBwmOutfit);
        bmwMaintainModel.setOutfit(listBwmOutfit);

        String json = new Gson().toJson(bmwMaintainModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_BMW_MAINTAIN, taskId,PadSysApp.getUser().getUserId(),json);

        return true;
    }

    @Override
    public void requestDataSucceed(BMWReCheckBean data) {

        if(data == null){
            return;
        }
        bmwMaintainModel = new BMWMaintainModel();
        bmwSubmitModel.setReportAddr(data.getCJReportUrl());
        bmwMaintainModel.setReportAddr(data.getCJReportUrl());
        List<BMWReCheckBean.BMWRepairBean> dataList = data.getRepairList();
        if(data == null || dataList.size() == 0){
            return;
        }

        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setRepairResult(-1);
        }
        bmwMaintainModel.setOutfit(dataList);
        showData();
    }

    @Override
    public void onPause() {
        super.onPause();
        checkAndSaveData(false,false,"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkAndSaveData(false,false,"");
    }

}
