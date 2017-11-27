package com.jcpt.jzg.padsystem.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.OtherTypeAdapter;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.fragment.BMWProcedureCarFragment;
import com.jcpt.jzg.padsystem.dialog.GBCodeConfirmDialog;
import com.jcpt.jzg.padsystem.mvpview.IRepairLogListener;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PlateTypeActivity;
import com.jcpt.jzg.padsystem.utils.ChineseUtil;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.Repairlog;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 手续信息
 */
public class ProcedureFragment extends BaseFragment implements IRepairLogListener{

    @BindView(R.id.rvOtherType)
    RecyclerView rvOtherType;
    @BindView(R.id.tvNext)
    CustomRippleButton crbNext;

    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private final int REQUEST_CODE_PLATE_TYPE = 10;//请求铭牌码

    OtherTypeAdapter otherTypeAdapter;
    ProcedureCarFragment procedureCarFragment;
    List<String> otherTypeLists = new ArrayList<String>();

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procedure, container, false);
        ButterKnife.bind(this, view);
        isShowRepair(true);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //注意：初始化时isVisibleToUser==false,此方法在onCreate()之前执行
        if (isVisibleToUser) {
            if(procedureCarFragment!=null){
                //刷新图片
                procedureCarFragment.refreshDLPic();
                procedureCarFragment.refreshDLRegisterPic();
            }
            isShowRepair(true);

        }else{
            //保存
            if(procedureCarFragment!=null){
                procedureCarFragment.checkAndsaveData(false,false,"");
                procedureCarFragment.sendMessage();
            }
            isShowRepair(true);
        }
    }

    private void init(){

        procedureCarFragment = new ProcedureCarFragment();
        addFragmentToStack(procedureCarFragment);



        otherTypeLists.add("车辆手续");
        rvOtherType.setLayoutManager(new LinearLayoutManager(context));
        otherTypeAdapter = new OtherTypeAdapter(context,otherTypeLists);
        rvOtherType.setAdapter(otherTypeAdapter);

        otherTypeAdapter.setOnItemClickListener(new OtherTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(otherTypeAdapter.getCurrPosition()==position)
                    return;
                otherTypeAdapter.setCurrPosition(position);
                MyToast.showLong(otherTypeLists.get(position));
                if(position == 0){
                    addFragmentToStack(procedureCarFragment);
                }
            }
        });


        crbNext.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                tapCarTypeSelect(1,"");

            }
        });

    }


    private void addFragmentToStack(Fragment fragment) {
        manager = getChildFragmentManager();
        fragmentTransaction =   manager.beginTransaction();
        fragmentTransaction.replace(R.id.flProcedure, fragment);
        fragmentTransaction.commit();
    }


    /**
     * 点击了车型选择或下一步
     * @param index 当前点击的哪个tab 手续信息为0 以此后推
     * @return 返回false表示手续信息必填项已填完，但点击的是车况检测或车辆照片或其他信息
     * @author zealjiang
     * @time 2016/12/14 10:39
     */
    public boolean tapCarTypeSelect(int index,String hintHead){
        DetectMainActivity activity = (DetectMainActivity) getActivity();
        boolean checkResult = procedureCarFragment.checkAndsaveData(false,true,hintHead);
        if(checkResult){
            if(index==1) {
                //时间统计
                activity.recordCarSelectST();

                //判断车型是否有值，有值跳到车型选择，无值跳到车系选择
                SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();

                if (submitModel.getStyleID() > 0 && (submitModel.getModelID() == 0 || submitModel.getMakeID() == 0)) {
                    //如果有ID出错，则重置
                    submitModel.setMakeID(0);
                    submitModel.setModelID(0);
                    submitModel.setStyleID(0);
                }

                if (submitModel.getModelID() > 0) {//如果选择了品牌、车系
                    //设置车型选择tabView可点击
                    activity.remove(1);
                    //跳到车型选择
                    ((DetectMainActivity) this.getActivity()).skipToFragment(1);
                } else if (submitModel.getStyleID() == 0 && submitModel.getModelID() == 0) {//如果没有选择品牌、车系
                    //跳到选择车辆铭牌
                    Intent intent = new Intent(context,PlateTypeActivity.class);
                    intent.putExtra("vin",submitModel.getVin());
                    intent.putExtra("productDate",submitModel.getProductionTime());
                    intent.putExtra("brandType",submitModel.getBrandType());//品牌和型号
                    ProcedureFragment.this.startActivityForResult(intent,REQUEST_CODE_PLATE_TYPE);
                } else {
                    MyToast.showShort("品牌、车系、车型ID有错");
                }
            }
            return true;
        }else{
            return false;
        }
    }


    /**
     * 数据保存
     * @author zealjiang
     * @time 2016/12/2 17:26
     */
    public boolean checkAndSaveData(boolean isCheck,boolean isNextCheck,String hintHead){
        return procedureCarFragment.checkAndsaveData(isCheck,isNextCheck,hintHead);
    }

    /**
     * Created by 李波 on 2016/12/19.
     * 点击车况检测时-- 判断手续照片完整性
     */
    public boolean checkBasePhoto(boolean isShowHint){
        return procedureCarFragment.checkBasePhoto(isShowHint);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case REQUEST_CODE_PLATE_TYPE:
                if(data==null){
                    return;
                }
                String vin = data.getStringExtra("vin");
                String plateType = data.getStringExtra("plateType");
                String productDate = data.getStringExtra("productDate");
                boolean isInputPlateType = data.getBooleanExtra("isInputPlateType",false);

                SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
                submitModel.setVin(vin);
                //保存所选择的车型信息
                if(isInputPlateType) {
                    submitModel.setNameplate(plateType);
                }else{
                    submitModel.setNameplate("");
                }
                submitModel.setNameplate(plateType);
                submitModel.setProductionTime(productDate);

                //清空车型
                submitModel.setMakeID(0);
                submitModel.setModelID(0);
                submitModel.setStyleID(0);

                //跳到车型
                //设置车型选择tabView可点击
                DetectMainActivity activity = (DetectMainActivity) getActivity();
                activity.remove(1);
                //跳到车型选择
                ((DetectMainActivity) this.getActivity()).skipToFragment(1);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e(TAG,"ProcedureF onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e(TAG,"ProcedureF onDestroy");
    }



    public void isShowRepair(Boolean isShow){

            if(((DetectMainActivity) getActivity()) != null && ((DetectMainActivity) getActivity()).getButtonView() != null){
                if(isShow){
                    ((DetectMainActivity) getActivity()).getButtonView().setVisibility(View.VISIBLE);
                }else{
                    ((DetectMainActivity) getActivity()).getButtonView().setVisibility(View.GONE);
                }
            }

    }


    @Override
    public void repairLogSucceed(Repairlog repairlog) {

    }

    @Override
    public void showRepairLogError(String error) {

    }


    public String getPathRegisterMorePI() {
        return procedureCarFragment.getPathRegisterMorePI();
    }

    public void setPathRegisterMorePI(String path) {
        procedureCarFragment.setPathRegisterMorePI(path);
    }

    public String getPathRegisterMorePI2() {
        return procedureCarFragment.getPathRegisterMorePI2();
    }

    public void setPathRegisterMorePI2(String path) {
        procedureCarFragment.setPathRegisterMorePI2(path);
    }
}
