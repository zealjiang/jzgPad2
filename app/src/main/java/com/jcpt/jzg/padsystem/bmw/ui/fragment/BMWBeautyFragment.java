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
import com.jcpt.jzg.padsystem.adapter.BMWBeautyAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWRecheckActivity;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.BMWBeautyModel;
import com.jcpt.jzg.padsystem.vo.BMWSubmitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BMWBeautyFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private BMWBeautyAdapter adapter;
    private String taskId;
    private BMWSubmitModel bmwSubmitModel;
    private BMWBeautyModel bmwBeautyModel;
    private List<BMWBeautyModel.PartBean> listBwmBeauty;
    private BMWRecheckActivity activity;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //2
        View view = inflater.inflate(R.layout.fragment_bmw_beauty, container, false);
        ButterKnife.bind(this, view);

        activity = (BMWRecheckActivity) this.getActivity();
        listBwmBeauty = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BMWBeautyAdapter(this.getContext(),listBwmBeauty);
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
        initShowData();
    }
    @Override
    protected void initData() {
        //1
    }

    @OnClick({R.id.tvSubmit})
    void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                activity.checkAndSubmit(true,true);
                break;
        }
    }

    /**
     * 构建页面数据
     */
    private void buildShowData(){
        String[] snameChArray = {"车身整洁","车身底部整洁，包括轮罩周边区域","轮毂整洁","车窗玻璃整洁","反光镜整洁","车内外观总体上整洁",
                "完成内饰清洗","车门合页整洁","驾驶座舱整洁","车顶内衬整洁","车内无异味","车门饰板整洁","车门内衬整洁","车内窗玻璃整洁",
                "脚踏板胶垫整洁（必要时更换）","发动机室整洁"};
        String[] snameEnArray = {"CLMR_BodyNeat","CLMR_BodyBottomNeat","CLMR_WheelNeat","CLMR_WindowNeat","CLMR_ReflectorNeat",
                "CLMR_CarAppearanceNeat","CLMR_InteriorNeat", "CLMR_HingeNeat","CLMR_CockpitNeat","CLMR_RoofTrimNeat","CLMR_SmellNeat",
                "CLMR_DoorTrimPanelNeat","CLMR_DoorLiningNeat","CLMR_InsideWindowNeat","CLMR_RubberPadNeat", "CLMR_EngineRoomNeat"};
        bmwBeautyModel = new BMWBeautyModel();
        ArrayList<BMWBeautyModel.PartBean> beautyList = new ArrayList();
        for (int i = 0; i < snameChArray.length; i++) {
            BMWBeautyModel.PartBean partBean = new BMWBeautyModel.PartBean();
            partBean.setId(i+1);
            partBean.setNameEn(snameEnArray[i]);
            partBean.setNameCh(snameChArray[i]);
            partBean.setValueStr(1);//默认选"是"
            beautyList.add(partBean);
        }
        bmwBeautyModel.setPart(beautyList);
    }

    private void initShowData() {

        if(taskId==null||bmwSubmitModel==null){
            return;
        }

        //从本地数据库中获取此用户下的taskId的美容项数据，如果数据为空则请求服务器
        List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_BMW_BEAUTY, PadSysApp.getUser().getUserId());
        if(list!=null&&list.size()>0){
            String json = list.get(0).getJson();
            if(!TextUtils.isEmpty(json)){
                bmwBeautyModel = new Gson().fromJson(json,BMWBeautyModel.class);
                showData();
            }
        }
        //显示默认数据
        if(bmwBeautyModel == null) {
            buildShowData();
            showData();
        }
    }

    /**
     * 显示数据
     */
    private void showData(){
        if(bmwBeautyModel!=null){
            listBwmBeauty.clear();
            listBwmBeauty.addAll(bmwBeautyModel.getPart());
            bmwBeautyModel.setPart(listBwmBeauty);
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

        if(listBwmBeauty==null){
            return false;
        }
        //获取选中的项
        if(isCheck || isNextCheck){
            for (int i = 0; i < listBwmBeauty.size(); i++) {
                if(listBwmBeauty.get(i).getValueStr() == -1){
                    MyToast.showShort(listBwmBeauty.get(i).getNameCh()+"还没检查，请完成");
                    return false;
                }
            }
        }

        bmwSubmitModel.setSpCheckItemList(listBwmBeauty);
        bmwBeautyModel.setPart(listBwmBeauty);

        String json = new Gson().toJson(bmwBeautyModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_BMW_BEAUTY, taskId,PadSysApp.getUser().getUserId(),json);

        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
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
