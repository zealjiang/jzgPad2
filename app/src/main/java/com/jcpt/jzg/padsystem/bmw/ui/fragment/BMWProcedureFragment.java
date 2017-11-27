package com.jcpt.jzg.padsystem.bmw.ui.fragment;

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
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.dialog.GBCodeConfirmDialog;
import com.jcpt.jzg.padsystem.mvpview.IRepairLogListener;
import com.jcpt.jzg.padsystem.ui.activity.PlateTypeActivity;
import com.jcpt.jzg.padsystem.ui.fragment.ProcedureCarFragment;
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
public class BMWProcedureFragment extends BaseFragment implements IRepairLogListener{

    @BindView(R.id.rvOtherType)
    RecyclerView rvOtherType;
    @BindView(R.id.tvNext)
    CustomRippleButton crbNext;

    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private final int REQUEST_CODE_PLATE_TYPE = 10;//请求铭牌码

    OtherTypeAdapter otherTypeAdapter;
    BMWProcedureCarFragment bmwProcedureCarFragment;//宝马手续信息
    List<String> otherTypeLists = new ArrayList<String>();

    @Override
    protected void setView() {
        init();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procedure, container, false);
        ButterKnife.bind(this, view);
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
            if(bmwProcedureCarFragment!=null){
                //刷新图片
                bmwProcedureCarFragment.refreshDLPic();
            }

        }else{
            //保存
            if(bmwProcedureCarFragment!=null){
                bmwProcedureCarFragment.checkAndSaveData(false,false,"");
            }
        }
    }

    private void init(){

        bmwProcedureCarFragment = new BMWProcedureCarFragment();
        addFragmentToStack(bmwProcedureCarFragment);


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
                    addFragmentToStack(bmwProcedureCarFragment);
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
        BMWDetectMainActivity activity = (BMWDetectMainActivity) getActivity();
        boolean checkResult = bmwProcedureCarFragment.checkAndSaveData(false,true,hintHead);
        if(checkResult){
            if(index==1) {
                //时间统计
                activity.recordCarSelectST();

                //判断车型是否有值，有值跳到车型选择，无值跳到车系选择
                SubmitModel submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();

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
                    ((BMWDetectMainActivity) this.getActivity()).skipToFragment(1);
                } else if (submitModel.getStyleID() == 0 && submitModel.getModelID() == 0) {//如果没有选择品牌、车系
                    //跳到选择车辆铭牌
                    Intent intent = new Intent(context,PlateTypeActivity.class);
                    intent.putExtra("vin",submitModel.getVin());
                    intent.putExtra("productDate",submitModel.getProductionTime());
                    intent.putExtra("brandType",submitModel.getBrandType());//品牌和型号
                    BMWProcedureFragment.this.startActivityForResult(intent,REQUEST_CODE_PLATE_TYPE);
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
        return bmwProcedureCarFragment.checkAndSaveData(isCheck,isNextCheck,hintHead);
    }

    /**
     * 点击车况检测时-- 判断手续照片完整性
     */
    public boolean checkBasePhoto(boolean isShowHint){
        return bmwProcedureCarFragment.checkBasePhoto(isShowHint);
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

                SubmitModel submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();
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
                BMWDetectMainActivity activity = (BMWDetectMainActivity) getActivity();
                activity.remove(1);
                //跳到车型选择
                ((BMWDetectMainActivity) this.getActivity()).skipToFragment(1);
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

    @Override
    public void repairLogSucceed(Repairlog repairlog) {

    }

    @Override
    public void showRepairLogError(String error) {

    }
}
