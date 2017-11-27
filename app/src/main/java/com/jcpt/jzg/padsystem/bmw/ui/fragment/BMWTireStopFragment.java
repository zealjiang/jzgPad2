package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.MyTagStringAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.interfaces.INextStepListener;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.vo.BMWTireStopBean;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.widget.BmwTireStopItemView;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.wheel.CustomListener;
import com.jcpt.jzg.padsystem.widget.wheel.OptionsPickerView;
import com.jcpt.jzg.padsystem.widget.wheel.WheelView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by wujj on 2017/10/25.
 * 邮箱：wujj@jingzhengu.com
 * 作用：轮胎轮毂
 */

public class BMWTireStopFragment extends BaseFragment {
    @BindView(R.id.tvNext)
    CustomRippleButton tvNext;
    Unbinder unbinder;
    @BindView(R.id.tflNoTire)
    TagFlowLayout tflNoTire;
    @BindView(R.id.etLeftFront)
    EditText etLeftFront;
    @BindView(R.id.etRightFront)
    EditText etRightFront;
    @BindView(R.id.etLeftBack)
    EditText etLeftBack;
    @BindView(R.id.etRightBack)
    EditText etRightBack;
    @BindView(R.id.etTire)
    EditText etTire;
    @BindView(R.id.tvLeftFront)
    TextView tvLeftFront;
    @BindView(R.id.btnLeftFront)
    TextView btnLeftFront;
    @BindView(R.id.tvRightFront)
    TextView tvRightFront;
    @BindView(R.id.btnRightFront)
    TextView btnRightFront;
    @BindView(R.id.tvLeftBack)
    TextView tvLeftBack;
    @BindView(R.id.btnLeftBack)
    TextView btnLeftBack;
    @BindView(R.id.tvRightBack)
    TextView tvRightBack;
    @BindView(R.id.btnRightBack)
    TextView btnRightBack;
    @BindView(R.id.tyreYear)
    BmwTireStopItemView tyreYear;
    @BindView(R.id.tyreStandard)
    BmwTireStopItemView tyreStandard;
    @BindView(R.id.tyreTread)
    BmwTireStopItemView tyreTread;
    @BindView(R.id.tyrePressure)
    BmwTireStopItemView tyrePressure;
    @BindView(R.id.tyreDecorate)
    BmwTireStopItemView tyreDecorate;
    @BindView(R.id.tyreSeason)
    BmwTireStopItemView tyreSeason;
    @BindView(R.id.tyreOldAndNew)
    BmwTireStopItemView tyreOldAndNew;
    @BindView(R.id.tvTire)
    TextView tvTire;
    private INextStepListener iNextStepListener;
    private String[] val;
    private BMWTireStopBean bmwTireStopBean;
    private int tagClickPos = -1;

    private ArrayList<String> tyreBrandList = new ArrayList<>();
    private ArrayList<String> tyreDiameterList = new ArrayList<>();
    private ArrayList<String> tyreFlatList = new ArrayList<>();
    private ArrayList<String> tyreWideList = new ArrayList<>();
    private ArrayList<String> tyreYearList = new ArrayList<>();
    private OptionsPickerView pvCustomOptions1;
    private OptionsPickerView pvCustomOptions2;
    private OptionsPickerView pvCustomOptions3;
    private OptionsPickerView pvCustomOptions4;
    private String tyreBrandLeftFront = "";//品牌
    private String tyreDiameterLeftFront = "";//直径
    private String tyreFlatLeftFront = "";//扁平比
    private String tyreWideLeftFront = "";//胎宽
    private String tyreYearLeftFront = "";//年份

    private String tyreBrandRightFront = "";
    private String tyreDiameterRightFront = "";
    private String tyreFlatRightFront = "";
    private String tyreWideRightFront = "";
    private String tyreYearRightFront = "";

    private String tyreBrandLeftAfter = "";
    private String tyreDiameterLeftAfter = "";
    private String tyreFlatLeftAfter = "";
    private String tyreWideLeftAfter = "";
    private String tyreYearLeftAfter = "";

    private String tyreBrandRightAfter = "";
    private String tyreDiameterRightAfter = "";
    private String tyreFlatRightAfter = "";
    private String tyreWideRightAfter = "";
    private String tyreYearRightAfter = "";

    //左前品牌型号年份选中的位置
    private int leftFrontTyreBrandPos;
    private int leftFrontTyreDiameterPos;
    private int leftFrontTyreFlatPos;
    private int leftFrontTyreWidePos;
    private int leftFrontTyreYearPos;
    //右前品牌型号年份选中的位置
    private int rightFrontTyreBrandPos;
    private int rightFrontTyreDiameterPos;
    private int rightFrontTyreFlatPos;
    private int rightFrontTyreWidePos;
    private int rightFrontTyreYearPos;
    //左后品牌型号年份选中的位置
    private int leftAfterTyreBrandPos;
    private int leftAfterTyreDiameterPos;
    private int leftAfterTyreFlatPos;
    private int leftAfterTyreWidePos;
    private int leftAfterTyreYearPos;
    //右后品牌型号年份选中的位置
    private int rightAfterTyreBrandPos;
    private int rightAfterTyreDiameterPos;
    private int rightAfterTyreFlatPos;
    private int rightAfterTyreWidePos;
    private int rightAfterTyreYearPos;
    private SubmitModel submitModel;

    @Override
    protected void setView() {
        setTag(val, tflNoTire);
        showData();
    }

    //回显数据
    private void showData() {
        if (bmwTireStopBean != null) {
            //给品牌、年份、胎宽、扁平比、直径重新赋值上次选择的值，保证保存savedata的时候取到的是上次选中的值
            tyreBrandLeftFront = bmwTireStopBean.getTyreBrandLeftFront();
            tyreDiameterLeftFront = bmwTireStopBean.getTyreDiameterLeftFront();
            tyreFlatLeftFront = bmwTireStopBean.getTyreFlatLeftFront();
            tyreWideLeftFront = bmwTireStopBean.getTyreWideLeftFront();
            tyreYearLeftFront = bmwTireStopBean.getTyreYearLeftFront();

            tyreBrandRightFront = bmwTireStopBean.getTyreBrandRightFront();
            tyreDiameterRightFront = bmwTireStopBean.getTyreDiameterRightFront();
            tyreFlatRightFront = bmwTireStopBean.getTyreFlatRightFront();
            tyreWideRightFront = bmwTireStopBean.getTyreWideRightFront();
            tyreYearRightFront = bmwTireStopBean.getTyreYearRightFront();

            tyreBrandLeftAfter = bmwTireStopBean.getTyreBrandLeftAfter();
            tyreDiameterLeftAfter = bmwTireStopBean.getTyreDiameterLeftAfter();
            tyreFlatLeftAfter = bmwTireStopBean.getTyreFlatLeftAfter();
            tyreWideLeftAfter = bmwTireStopBean.getTyreWideLeftAfter();
            tyreYearLeftAfter = bmwTireStopBean.getTyreYearLeftAfter();

            tyreBrandRightAfter = bmwTireStopBean.getTyreBrandRightAfter();
            tyreDiameterRightAfter = bmwTireStopBean.getTyreDiameterRightAfter();
            tyreFlatRightAfter = bmwTireStopBean.getTyreFlatRightAfter();
            tyreWideRightAfter = bmwTireStopBean.getTyreWideRightAfter();
            tyreYearRightAfter = bmwTireStopBean.getTyreYearRightAfter();

            tvLeftFront.setText(bmwTireStopBean.getLTSC_TyreLeftAnterior());//左前轮胎品牌/型号/年份
            tvRightFront.setText(bmwTireStopBean.getLTSC_TyreRightFront());//右前轮胎品牌/型号/年份
            tvLeftBack.setText(bmwTireStopBean.getLTSC_TyreLeftAfter());//左后轮胎品牌/型号/年份
            tvRightBack.setText(bmwTireStopBean.getLTSC_TyreRightAfter());//右后轮胎品牌/型号/年份

            //胎纹深度
            etLeftFront.setText(bmwTireStopBean.getTyreDepthLeftFront());
            etRightFront.setText(bmwTireStopBean.getTyreDepthRightFront());
            etLeftBack.setText(bmwTireStopBean.getTyreDepthLeftAfter());
            etRightBack.setText(bmwTireStopBean.getTyreDepthRightAfter());
            etTire.setText(bmwTireStopBean.getTyreDepthSpareTire());

            //无备胎
            Set<Integer> set = new HashSet<Integer>();
            int noSpareTire = bmwTireStopBean.getNoSpareTire();
            if (noSpareTire == -1) {
                set = null;
            } else {
                set.clear();
                set.add(noSpareTire);
            }
            tflNoTire.getAdapter().setSelectedList(set);
            tflNoTire.getOnSelectListener().onSelected(set);


            int ltsc_tyreYear = bmwTireStopBean.getLTSC_TyreYear();
            switch (ltsc_tyreYear) {
                case 0:
                    tyreYear.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyreYear.getRbtnNo().setChecked(true);
                    break;
            }
            int ltsc_tyreStandard = bmwTireStopBean.getLTSC_TyreStandard();
            switch (ltsc_tyreStandard) {
                case 0:
                    tyreStandard.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyreStandard.getRbtnNo().setChecked(true);
                    break;
            }
            int ltsc_tyreTread = bmwTireStopBean.getLTSC_TyreTread();
            switch (ltsc_tyreTread) {
                case 0:
                    tyreTread.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyreTread.getRbtnNo().setChecked(true);
                    break;
            }
            int ltsc_tyrePressure = bmwTireStopBean.getLTSC_TyrePressure();
            switch (ltsc_tyrePressure) {
                case 0:
                    tyrePressure.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyrePressure.getRbtnNo().setChecked(true);
                    break;
            }
            int ltsc_tyreDecorate = bmwTireStopBean.getLTSC_TyreDecorate();
            switch (ltsc_tyreDecorate) {
                case 0:
                    tyreDecorate.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyreDecorate.getRbtnNo().setChecked(true);
                    break;
            }
            int ltsc_tyreSeason = bmwTireStopBean.getLTSC_TyreSeason();
            switch (ltsc_tyreSeason) {
                case 0:
                    tyreSeason.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyreSeason.getRbtnNo().setChecked(true);
                    break;
            }
            int ltsc_tyreOldAndNew = bmwTireStopBean.getLTSC_TyreOldAndNew();
            switch (ltsc_tyreOldAndNew) {
                case 0:
                    tyreOldAndNew.getRbtnYes().setChecked(true);
                    break;
                case 1:
                    tyreOldAndNew.getRbtnNo().setChecked(true);
                    break;
            }


        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tire_stop_bmw, container, false);
        unbinder = ButterKnife.bind(this, view);
        initListener();
        initCustomOptionPicker();
        return view;
    }
    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions1 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int option4, int options5, View v) {
                leftFrontTyreBrandPos = options1;
                leftFrontTyreDiameterPos = option4;
                leftFrontTyreFlatPos = options3;
                leftFrontTyreWidePos = option2;
                leftFrontTyreYearPos = options5;
                //返回的分别是三个级别的选中位置
                if (tyreBrandList != null && tyreBrandList.size()>0){
                    //品牌
                    tyreBrandLeftFront = tyreBrandList.get(options1);
                }
                if (tyreDiameterList != null && tyreDiameterList.size()>0){
                    //直径
                    tyreDiameterLeftFront = tyreDiameterList.get(option4);
                }
                if (tyreFlatList != null && tyreFlatList.size()>0){
                    //扁平比
                    tyreFlatLeftFront = tyreFlatList.get(options3);
                }
                if (tyreWideList != null && tyreWideList.size()>0){
                    //胎宽
                    tyreWideLeftFront = tyreWideList.get(option2);
                }
                if (tyreYearList != null && tyreYearList.size()>0){
                    //年份
                    tyreYearLeftFront = tyreYearList.get(options5);
                }
                String str = tyreBrandLeftFront + " " + tyreYearLeftFront + " " + tyreWideLeftFront + "/" + tyreFlatLeftFront + "R" + tyreDiameterLeftFront;
                tvLeftFront.setText(str);
                if (TextUtils.isEmpty(tvRightFront.getText())) {
                    tvRightFront.setText(str);

                    tyreBrandRightFront = tyreBrandLeftFront;
                    tyreDiameterRightFront = tyreDiameterLeftFront;
                    tyreFlatRightFront = tyreFlatLeftFront;
                    tyreWideRightFront = tyreWideLeftFront;
                    tyreYearRightFront = tyreYearLeftFront;
                }
                if (TextUtils.isEmpty(tvLeftBack.getText())) {
                    tvLeftBack.setText(str);

                    tyreBrandLeftAfter = tyreBrandLeftFront;
                    tyreDiameterLeftAfter = tyreDiameterLeftFront;
                    tyreFlatLeftAfter = tyreFlatLeftFront;
                    tyreWideLeftAfter = tyreWideLeftFront;
                    tyreYearLeftAfter = tyreYearLeftFront;
                }
                if (TextUtils.isEmpty(tvRightBack.getText())) {
                    tvRightBack.setText(str);

                    tyreBrandRightAfter = tyreBrandLeftFront;
                    tyreDiameterRightAfter = tyreDiameterLeftFront;
                    tyreFlatRightAfter = tyreFlatLeftFront;
                    tyreWideRightAfter = tyreWideLeftFront;
                    tyreYearRightAfter = tyreYearLeftFront;
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final WheelView wheelView1 = (WheelView)v.findViewById(R.id.options1);
                        final WheelView wheelView2 = (WheelView)v.findViewById(R.id.options2);
                        final WheelView wheelView3 = (WheelView)v.findViewById(R.id.options3);
                        final WheelView wheelView4 = (WheelView)v.findViewById(R.id.options4);
                        final WheelView wheelView5 = (WheelView)v.findViewById(R.id.options5);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //滑轮正在滚动，屏蔽“完成”按钮的点击事件，解决再次打开选择框反显不对应的问题
                                if (!wheelView1.isFlingOrDaggle()&&!wheelView2.isFlingOrDaggle()&&!wheelView3.isFlingOrDaggle()
                                        &&!wheelView4.isFlingOrDaggle()&&!wheelView5.isFlingOrDaggle()){
                                    pvCustomOptions1.returnData();
                                    pvCustomOptions1.dismiss();
                                }
                            }
                        });
                    }
                })
                .setOutSideCancelable(false)
                .setSelectOptions(leftFrontTyreBrandPos,leftFrontTyreDiameterPos,leftFrontTyreFlatPos,leftFrontTyreWidePos,leftFrontTyreYearPos)
                .isDialog(true)
                .setCyclic(true,true,true,true,true)
                .build();

        pvCustomOptions2 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int option4, int options5, View v) {
                rightFrontTyreBrandPos = options1;
                rightFrontTyreDiameterPos = option4;
                rightFrontTyreFlatPos = options3;
                rightFrontTyreWidePos = option2;
                rightFrontTyreYearPos = options5;
                //返回的分别是三个级别的选中位置
                if (tyreBrandList != null && tyreBrandList.size()>0){
                    //品牌
                    tyreBrandRightFront = tyreBrandList.get(options1);
                }
                if (tyreDiameterList != null && tyreDiameterList.size()>0){
                    //直径
                    tyreDiameterRightFront = tyreDiameterList.get(option4);
                }
                if (tyreFlatList != null && tyreFlatList.size()>0){
                    //扁平比
                    tyreFlatRightFront = tyreFlatList.get(options3);
                }
                if (tyreWideList != null && tyreWideList.size()>0){
                    //胎宽
                    tyreWideRightFront = tyreWideList.get(option2);
                }
                if (tyreYearList != null && tyreYearList.size()>0){
                    //年份
                    tyreYearRightFront = tyreYearList.get(options5);
                }
                String str = tyreBrandRightFront + " " + tyreYearRightFront + " " + tyreWideRightFront + "/" + tyreFlatRightFront + "R" + tyreDiameterRightFront;
                tvRightFront.setText(str);
                if (TextUtils.isEmpty(tvLeftFront.getText())) {
                    tvLeftFront.setText(str);

                    tyreBrandLeftFront = tyreBrandRightFront;
                    tyreDiameterLeftFront = tyreDiameterRightFront;
                    tyreFlatLeftFront = tyreFlatRightFront;
                    tyreWideLeftFront = tyreWideRightFront;
                    tyreYearLeftFront = tyreYearRightFront;
                }
                if (TextUtils.isEmpty(tvLeftBack.getText())) {
                    tvLeftBack.setText(str);

                    tyreBrandLeftAfter = tyreBrandRightFront;
                    tyreDiameterLeftAfter = tyreDiameterRightFront;
                    tyreFlatLeftAfter = tyreFlatRightFront;
                    tyreWideLeftAfter = tyreWideRightFront;
                    tyreYearLeftAfter = tyreYearRightFront;
                }
                if (TextUtils.isEmpty(tvRightBack.getText())) {
                    tvRightBack.setText(str);

                    tyreBrandRightAfter = tyreBrandRightFront;
                    tyreDiameterRightAfter = tyreDiameterRightFront;
                    tyreFlatRightAfter = tyreFlatRightFront;
                    tyreWideRightAfter = tyreWideRightFront;
                    tyreYearRightAfter = tyreYearRightFront;
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final WheelView wheelView1 = (WheelView)v.findViewById(R.id.options1);
                        final WheelView wheelView2 = (WheelView)v.findViewById(R.id.options2);
                        final WheelView wheelView3 = (WheelView)v.findViewById(R.id.options3);
                        final WheelView wheelView4 = (WheelView)v.findViewById(R.id.options4);
                        final WheelView wheelView5 = (WheelView)v.findViewById(R.id.options5);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //滑轮正在滚动，屏蔽“完成”按钮的点击事件，解决再次打开选择框反显不对应的问题
                                if (!wheelView1.isFlingOrDaggle()&&!wheelView2.isFlingOrDaggle()&&!wheelView3.isFlingOrDaggle()
                                        &&!wheelView4.isFlingOrDaggle()&&!wheelView5.isFlingOrDaggle()){
                                    pvCustomOptions2.returnData();
                                    pvCustomOptions2.dismiss();
                                }
                            }
                        });
                    }
                })
                .setOutSideCancelable(false)
                .setSelectOptions(rightFrontTyreBrandPos,rightFrontTyreDiameterPos,rightFrontTyreFlatPos,rightFrontTyreWidePos,rightFrontTyreYearPos)
                .isDialog(true)
                .setCyclic(true,true,true,true,true)
                .build();

        pvCustomOptions3 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int option4, int options5, View v) {
                leftAfterTyreBrandPos = options1;
                leftAfterTyreDiameterPos = option4;
                leftAfterTyreFlatPos = options3;
                leftAfterTyreWidePos = option2;
                leftAfterTyreYearPos = options5;
                //返回的分别是三个级别的选中位置
                if (tyreBrandList != null && tyreBrandList.size()>0){
                    //品牌
                    tyreBrandLeftAfter = tyreBrandList.get(options1);
                }
                if (tyreDiameterList != null && tyreDiameterList.size()>0){
                    //直径
                    tyreDiameterLeftAfter = tyreDiameterList.get(option4);
                }
                if (tyreFlatList != null && tyreFlatList.size()>0){
                    //扁平比
                    tyreFlatLeftAfter = tyreFlatList.get(options3);
                }
                if (tyreWideList != null && tyreWideList.size()>0){
                    //胎宽
                    tyreWideLeftAfter = tyreWideList.get(option2);
                }
                if (tyreYearList != null && tyreYearList.size()>0){
                    //年份
                    tyreYearLeftAfter = tyreYearList.get(options5);
                }
                String str = tyreBrandLeftAfter + " " + tyreYearLeftAfter + " " + tyreWideLeftAfter + "/" + tyreFlatLeftAfter + "R" + tyreDiameterLeftAfter;
                tvLeftBack.setText(str);
                if (TextUtils.isEmpty(tvRightFront.getText())) {
                    tvRightFront.setText(str);

                    tyreBrandRightFront = tyreBrandLeftAfter;
                    tyreDiameterRightFront = tyreDiameterLeftAfter;
                    tyreFlatRightFront = tyreFlatLeftAfter;
                    tyreWideRightFront= tyreWideLeftAfter;
                    tyreYearRightFront = tyreYearLeftAfter;
                }
                if (TextUtils.isEmpty(tvLeftFront.getText())) {
                    tvLeftFront.setText(str);

                    tyreBrandLeftFront = tyreBrandLeftAfter;
                    tyreDiameterLeftFront = tyreDiameterLeftAfter;
                    tyreFlatLeftFront = tyreFlatLeftAfter;
                    tyreWideLeftFront = tyreWideLeftAfter;
                    tyreYearLeftFront = tyreYearLeftAfter;
                }
                if (TextUtils.isEmpty(tvRightBack.getText())) {
                    tvRightBack.setText(str);

                    tyreBrandRightAfter = tyreBrandLeftAfter;
                    tyreDiameterRightAfter = tyreDiameterLeftAfter;
                    tyreFlatRightAfter = tyreFlatLeftAfter;
                    tyreWideRightAfter = tyreWideLeftAfter;
                    tyreYearRightAfter = tyreYearLeftAfter;
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final WheelView wheelView1 = (WheelView)v.findViewById(R.id.options1);
                        final WheelView wheelView2 = (WheelView)v.findViewById(R.id.options2);
                        final WheelView wheelView3 = (WheelView)v.findViewById(R.id.options3);
                        final WheelView wheelView4 = (WheelView)v.findViewById(R.id.options4);
                        final WheelView wheelView5 = (WheelView)v.findViewById(R.id.options5);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //滑轮正在滚动，屏蔽“完成”按钮的点击事件，解决再次打开选择框反显不对应的问题
                                if (!wheelView1.isFlingOrDaggle()&&!wheelView2.isFlingOrDaggle()&&!wheelView3.isFlingOrDaggle()
                                        &&!wheelView4.isFlingOrDaggle()&&!wheelView5.isFlingOrDaggle()){
                                    pvCustomOptions3.returnData();
                                    pvCustomOptions3.dismiss();
                                }
                            }
                        });
                    }
                })
                .setOutSideCancelable(false)
                .setSelectOptions(leftAfterTyreBrandPos,leftAfterTyreDiameterPos,leftAfterTyreFlatPos,leftAfterTyreWidePos,leftAfterTyreYearPos)
                .isDialog(true)
                .setCyclic(true,true,true,true,true)
                .build();

        pvCustomOptions4 = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int option4, int options5, View v) {
                rightAfterTyreBrandPos = options1;
                rightAfterTyreDiameterPos = option4;
                rightAfterTyreFlatPos = options3;
                rightAfterTyreWidePos = option2;
                rightAfterTyreYearPos = options5;
                //返回的分别是三个级别的选中位置
                if (tyreBrandList != null && tyreBrandList.size()>0){
                    //品牌
                    tyreBrandRightAfter = tyreBrandList.get(options1);
                }
                if (tyreDiameterList != null && tyreDiameterList.size()>0){
                    //直径
                    tyreDiameterRightAfter = tyreDiameterList.get(option4);
                }
                if (tyreFlatList != null && tyreFlatList.size()>0){
                    //扁平比
                    tyreFlatRightAfter = tyreFlatList.get(options3);
                }
                if (tyreWideList != null && tyreWideList.size()>0){
                    //胎宽
                    tyreWideRightAfter = tyreWideList.get(option2);
                }
                if (tyreYearList != null && tyreYearList.size()>0){
                    //年份
                    tyreYearRightAfter = tyreYearList.get(options5);
                }
                String str = tyreBrandRightAfter + " " + tyreYearRightAfter + " " + tyreWideRightAfter + "/" + tyreFlatRightAfter + "R" + tyreDiameterRightAfter;
                tvRightBack.setText(str);
                if (TextUtils.isEmpty(tvRightFront.getText())) {
                    tvRightFront.setText(str);

                    tyreBrandRightFront = tyreBrandRightAfter;
                    tyreDiameterRightFront = tyreDiameterRightAfter;
                    tyreFlatRightFront = tyreFlatRightAfter;
                    tyreWideRightFront = tyreWideRightAfter;
                    tyreYearRightFront = tyreYearRightAfter;
                }
                if (TextUtils.isEmpty(tvLeftBack.getText())) {
                    tvLeftBack.setText(str);

                    tyreBrandLeftAfter = tyreBrandRightAfter;
                    tyreDiameterLeftAfter = tyreDiameterRightAfter;
                    tyreFlatLeftAfter = tyreFlatRightAfter;
                    tyreWideLeftAfter = tyreWideRightAfter;
                    tyreYearLeftAfter = tyreYearRightAfter;
                }
                if (TextUtils.isEmpty(tvLeftFront.getText())) {
                    tvLeftFront.setText(str);

                    tyreBrandLeftFront = tyreBrandRightAfter;
                    tyreDiameterLeftFront = tyreDiameterRightAfter;
                    tyreFlatLeftFront = tyreFlatRightAfter;
                    tyreWideLeftFront = tyreWideRightAfter;
                    tyreYearLeftFront = tyreYearRightAfter;
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final WheelView wheelView1 = (WheelView)v.findViewById(R.id.options1);
                        final WheelView wheelView2 = (WheelView)v.findViewById(R.id.options2);
                        final WheelView wheelView3 = (WheelView)v.findViewById(R.id.options3);
                        final WheelView wheelView4 = (WheelView)v.findViewById(R.id.options4);
                        final WheelView wheelView5 = (WheelView)v.findViewById(R.id.options5);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //滑轮正在滚动，屏蔽“完成”按钮的点击事件，解决再次打开选择框反显不对应的问题
                                if (!wheelView1.isFlingOrDaggle()&&!wheelView2.isFlingOrDaggle()&&!wheelView3.isFlingOrDaggle()
                                        &&!wheelView4.isFlingOrDaggle()&&!wheelView5.isFlingOrDaggle()){
                                    pvCustomOptions4.returnData();
                                    pvCustomOptions4.dismiss();
                                }
                            }
                        });
                    }
                })
                .setOutSideCancelable(false)
                .setSelectOptions(rightAfterTyreBrandPos,rightAfterTyreDiameterPos,rightAfterTyreFlatPos,rightAfterTyreWidePos,rightAfterTyreYearPos)
                .isDialog(true)
                .setCyclic(true,true,true,true,true)
                .build();

        pvCustomOptions1.setNPicker(tyreBrandList, tyreWideList, tyreFlatList, tyreDiameterList, tyreYearList);//添加数据
        pvCustomOptions2.setNPicker(tyreBrandList, tyreWideList, tyreFlatList, tyreDiameterList, tyreYearList);//添加数据
        pvCustomOptions3.setNPicker(tyreBrandList, tyreWideList, tyreFlatList, tyreDiameterList, tyreYearList);//添加数据
        pvCustomOptions4.setNPicker(tyreBrandList, tyreWideList, tyreFlatList, tyreDiameterList, tyreYearList);//添加数据
    }

    private void initListener() {
        tflNoTire.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet!=null && selectPosSet.size() > 0) {//选中
                    etTire.setText("");
                    tvTire.setTextColor(Color.GRAY);
                    etTire.setFocusable(false);
                    etTire.setFocusableInTouchMode(false);
                    etTire.setEnabled(false);
                } else {//未选中
                    etTire.setFocusableInTouchMode(true);
                    etTire.setFocusable(true);
                    etTire.requestFocus();
                    etTire.setEnabled(true);
                    tvTire.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @Override
    protected void initData() {
        val = new String[]{"无备胎"};
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        //查询此条缓存是否存在
        boolean isExist = DBManager.getInstance().isExist(Constants.DATA_TYPE_BMW_TIRESTOP, taskId, PadSysApp.getUser().getUserId());
        DBManager.getInstance().closeDB();
        if (isExist) {
            List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_BMW_TIRESTOP, PadSysApp.getUser().getUserId());
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getDataType().equals(Constants.DATA_TYPE_BMW_TIRESTOP)) {
                        String json = list.get(i).getJson();
                        if (!TextUtils.isEmpty(json)) {
                            bmwTireStopBean = new Gson().fromJson(json, BMWTireStopBean.class);
                        }
                    }
                }
            }
        }

        DetectionWrapper wrapper = ((BMWDetectMainActivity) getActivity()).getWrapper();
        if (wrapper != null) {
            DetectionWrapper.TyreInfoListBean tyreInfoList = wrapper.getTyreInfoList();
            if (tyreInfoList != null) {
                List<DetectionWrapper.TyreInfoListBean.TyreBrandBean> tyreBrand = tyreInfoList.getTyreBrand();
                if (tyreBrand != null && tyreBrand.size() > 0) {
                    for (int i = 0; i < tyreBrand.size(); i++) {
                        tyreBrandList.add(tyreBrand.get(i).getValueName());
                    }
                }
                List<DetectionWrapper.TyreInfoListBean.TyreDiameterBean> tyreDiameter = tyreInfoList.getTyreDiameter();
                if (tyreDiameter != null && tyreDiameter.size() > 0) {
                    for (int i = 0; i < tyreDiameter.size(); i++) {
                        tyreDiameterList.add(tyreDiameter.get(i).getValueName());
                    }
                }
                List<DetectionWrapper.TyreInfoListBean.TyreFlatBean> tyreFlat = tyreInfoList.getTyreFlat();
                if (tyreFlat != null && tyreFlat.size() > 0) {
                    for (int i = 0; i < tyreFlat.size(); i++) {
                        tyreFlatList.add(tyreFlat.get(i).getValueName());
                    }
                }
                List<DetectionWrapper.TyreInfoListBean.TyreWideBean> tyreWide = tyreInfoList.getTyreWide();
                if (tyreWide != null && tyreWide.size() > 0) {
                    for (int i = 0; i < tyreWide.size(); i++) {
                        tyreWideList.add(tyreWide.get(i).getValueName());
                    }
                }
                List<DetectionWrapper.TyreInfoListBean.TyreYearBean> tyreYear = tyreInfoList.getTyreYear();
                if (tyreYear != null && tyreYear.size() > 0) {
                    for (int i = 0; i < tyreYear.size(); i++) {
                        tyreYearList.add(tyreYear.get(i).getValueName());
                    }
                }
            }
        }
        if (bmwTireStopBean != null){
            //左前品牌型号年份选中的位置
            leftFrontTyreBrandPos = bmwTireStopBean.getLeftFrontTyreBrandPos();
            leftFrontTyreDiameterPos = bmwTireStopBean.getLeftFrontTyreDiameterPos();
            leftFrontTyreFlatPos = bmwTireStopBean.getLeftFrontTyreFlatPos();
            leftFrontTyreWidePos = bmwTireStopBean.getLeftFrontTyreWidePos();
            leftFrontTyreYearPos = bmwTireStopBean.getLeftFrontTyreYearPos();
            //右前品牌型号年份选中的位置
            rightFrontTyreBrandPos = bmwTireStopBean.getRightFrontTyreBrandPos();
            rightFrontTyreDiameterPos = bmwTireStopBean.getRightFrontTyreDiameterPos();
            rightFrontTyreFlatPos = bmwTireStopBean.getRightFrontTyreFlatPos();
            rightFrontTyreWidePos = bmwTireStopBean.getRightFrontTyreWidePos();
            rightFrontTyreYearPos = bmwTireStopBean.getRightFrontTyreYearPos();
            //左后品牌型号年份选中的位置
            leftAfterTyreBrandPos = bmwTireStopBean.getLeftAfterTyreBrandPos();
            leftAfterTyreDiameterPos = bmwTireStopBean.getLeftAfterTyreDiameterPos();
            leftAfterTyreFlatPos = bmwTireStopBean.getLeftAfterTyreFlatPos();
            leftAfterTyreWidePos = bmwTireStopBean.getLeftAfterTyreWidePos();
            leftAfterTyreYearPos = bmwTireStopBean.getLeftAfterTyreYearPos();
            //右后品牌型号年份选中的位置
            rightAfterTyreBrandPos = bmwTireStopBean.getRightAfterTyreBrandPos();
            rightAfterTyreDiameterPos = bmwTireStopBean.getRightAfterTyreDiameterPos();
            rightAfterTyreFlatPos = bmwTireStopBean.getRightAfterTyreFlatPos();
            rightAfterTyreWidePos = bmwTireStopBean.getRightAfterTyreWidePos();
            rightAfterTyreYearPos = bmwTireStopBean.getRightAfterTyreYearPos();
        }
    }

    public void setOnNextStepListener(INextStepListener iNextStepListener) {
        this.iNextStepListener = iNextStepListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setTag(final String[] vals, final TagFlowLayout tagFlowLayout) {
        MyTagStringAdapter Adapter = new MyTagStringAdapter(vals, tagFlowLayout, this.context);
        tagFlowLayout.setAdapter(Adapter);
//        setTagListener(tagFlowLayout);
    }

    @OnClick({R.id.tvNext, R.id.etLeftFront, R.id.etRightFront, R.id.etLeftBack, R.id.etRightBack, R.id.etTire, R.id.btnLeftFront, R.id.btnRightFront, R.id.btnLeftBack, R.id.btnRightBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if (iNextStepListener != null) {
                    iNextStepListener.nextStep(2);
                    //解决有的小米Max2手机不隐藏软键盘的问题
                    KeyboardUtils.hideSoftInput(getActivity());
                }
                break;
            case R.id.etLeftFront:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftFront);
                break;
            case R.id.etRightFront:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightFront);
                break;
            case R.id.etLeftBack:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etLeftBack);
                break;
            case R.id.etRightBack:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etRightBack);
                break;
            case R.id.etTire:
                //弹出软键盘
                KeyboardUtils.showSoftInput(getActivity(), etTire);
                break;
            case R.id.btnLeftFront:
                pvCustomOptions1.show();
                break;
            case R.id.btnRightFront:
                pvCustomOptions2.show();
                break;
            case R.id.btnLeftBack:
                pvCustomOptions3.show();
                break;
            case R.id.btnRightBack:
                pvCustomOptions4.show();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    //保存数据
    private void saveData() {
        if (bmwTireStopBean == null) {
            bmwTireStopBean = new BMWTireStopBean();
        }
        if (submitModel == null){
            submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();
        }
        List<SubmitModel.SpCheckItemGroupListBean> spCheckItemGroupList = ((BMWDetectMainActivity) getActivity()).getSpCheckItemGroupList();
        //防止提交时重复add数据进spCheckItemGroupList中
        if (spCheckItemGroupList!=null&&spCheckItemGroupList.size()>0){
            for (int i = 0; i < spCheckItemGroupList.size(); i++){
                if (spCheckItemGroupList.get(i).getGroupId() == 2){
                    spCheckItemGroupList.remove(i);
                }
            }
        }
        SubmitModel.SpCheckItemGroupListBean spCheckItemGroupListBean = new SubmitModel.SpCheckItemGroupListBean();
        spCheckItemGroupListBean.setGroupId(2);
        List<SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean> SpCheckItemList = new ArrayList<>();
        //轮胎品牌型号年份
        bmwTireStopBean.setLTSC_TyreLeftAnterior(tvLeftFront.getText().toString());//左前
        bmwTireStopBean.setLTSC_TyreLeftAfter(tvLeftBack.getText().toString());//右前
        bmwTireStopBean.setLTSC_TyreRightFront(tvRightFront.getText().toString());//左后
        bmwTireStopBean.setLTSC_TyreRightAfter(tvRightBack.getText().toString());//右后

        //左前品牌、年份、胎宽、扁平比、直径
        bmwTireStopBean.setTyreBrandLeftFront(tyreBrandLeftFront);
        bmwTireStopBean.setTyreDiameterLeftFront(tyreDiameterLeftFront);
        bmwTireStopBean.setTyreFlatLeftFront(tyreFlatLeftFront);
        bmwTireStopBean.setTyreWideLeftFront(tyreWideLeftFront);
        bmwTireStopBean.setTyreYearLeftFront(tyreYearLeftFront);

        //右前品牌、年份、胎宽、扁平比、直径
        bmwTireStopBean.setTyreBrandRightFront(tyreBrandRightFront);
        bmwTireStopBean.setTyreDiameterRightFront(tyreDiameterRightFront);
        bmwTireStopBean.setTyreFlatRightFront(tyreFlatRightFront);
        bmwTireStopBean.setTyreWideRightFront(tyreWideRightFront);
        bmwTireStopBean.setTyreYearRightFront(tyreYearRightFront);

        //左后品牌、年份、胎宽、扁平比、直径
        bmwTireStopBean.setTyreBrandLeftAfter(tyreBrandLeftAfter);
        bmwTireStopBean.setTyreDiameterLeftAfter(tyreDiameterLeftAfter);
        bmwTireStopBean.setTyreFlatLeftAfter(tyreFlatLeftAfter);
        bmwTireStopBean.setTyreWideLeftAfter(tyreWideLeftAfter);
        bmwTireStopBean.setTyreYearLeftAfter(tyreYearLeftAfter);

        //右后品牌、年份、胎宽、扁平比、直径
        bmwTireStopBean.setTyreBrandRightAfter(tyreBrandRightAfter);
        bmwTireStopBean.setTyreDiameterRightAfter(tyreDiameterRightAfter);
        bmwTireStopBean.setTyreFlatRightAfter(tyreFlatRightAfter);
        bmwTireStopBean.setTyreWideRightAfter(tyreWideRightAfter);
        bmwTireStopBean.setTyreYearRightAfter(tyreYearRightAfter);

        //左前轮胎品牌/型号
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean1 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean1.setNameEn("LTSC_TyreLeftAnterior");
        spCheckItemListBean1.setValueStr(tyreBrandLeftFront+"/"+tyreWideLeftFront + "/" + tyreFlatLeftFront + "R" + tyreDiameterLeftFront);
        spCheckItemListBean1.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean1);

        //右前轮胎品牌/型号
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean2 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean2.setNameEn("LTSC_TyreRightFront");
        spCheckItemListBean2.setValueStr(tyreBrandRightFront+"/"+tyreWideRightFront + "/" + tyreFlatRightFront + "R" + tyreDiameterRightFront);
        spCheckItemListBean2.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean2);

        //左后轮胎品牌/型号
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean3 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean3.setNameEn("LTSC_TyreLeftAfter");
        spCheckItemListBean3.setValueStr(tyreBrandLeftAfter+"/"+tyreWideLeftAfter + "/" + tyreFlatLeftAfter + "R" + tyreDiameterLeftAfter);
        spCheckItemListBean3.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean3);

        //右后轮胎品牌/型号
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean4 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean4.setNameEn("LTSC_TyreRightAfter");
        spCheckItemListBean4.setValueStr(tyreBrandRightAfter+"/"+tyreWideRightAfter + "/" + tyreFlatRightAfter + "R" + tyreDiameterRightAfter);
        spCheckItemListBean4.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean4);

        //左前轮胎年份
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean5 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean5.setNameEn("LTSC_TyreLeftFrontYear");
        spCheckItemListBean5.setValueStr(tyreYearLeftFront);
        spCheckItemListBean5.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean5);

        //右前轮胎年份
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean6 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean6.setNameEn("LTSC_TyreRightFrontYear");
        spCheckItemListBean6.setValueStr(tyreYearRightFront);
        spCheckItemListBean6.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean6);

        //左后轮胎年份
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean7 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean7.setNameEn("LTSC_TyreLeftAfterYear");
        spCheckItemListBean7.setValueStr(tyreYearLeftAfter);
        spCheckItemListBean7.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean7);

        //右后轮胎年份
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean8 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean8.setNameEn("LTSC_TyreRightAfterYear");
        spCheckItemListBean8.setValueStr(tyreYearRightAfter);
        spCheckItemListBean8.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean8);

        //左前轮毂尺寸
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean9 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean9.setNameEn("LTSC_TyreLeftFrontSize");
        spCheckItemListBean9.setValueStr(tyreDiameterLeftFront);
        spCheckItemListBean9.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean9);

        //右前轮毂尺寸
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean10 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean10.setNameEn("LTSC_TyreRightFrontSize");
        spCheckItemListBean10.setValueStr(tyreDiameterRightFront);
        spCheckItemListBean10.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean10);

        //左后轮毂尺寸
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean11 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean11.setNameEn("LTSC_TyreLeftAfterSize");
        spCheckItemListBean11.setValueStr(tyreDiameterLeftAfter);
        spCheckItemListBean11.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean11);

        //右后轮毂尺寸
        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean12 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean12.setNameEn("LTSC_TyreRightAfterSize");
        spCheckItemListBean12.setValueStr(tyreDiameterRightAfter);
        spCheckItemListBean12.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean12);

        //左前品牌型号年份选中的位置
        bmwTireStopBean.setLeftFrontTyreBrandPos(leftFrontTyreBrandPos);
        bmwTireStopBean.setLeftFrontTyreDiameterPos(leftFrontTyreDiameterPos);
        bmwTireStopBean.setLeftFrontTyreFlatPos(leftFrontTyreFlatPos);
        bmwTireStopBean.setLeftFrontTyreWidePos(leftFrontTyreWidePos);
        bmwTireStopBean.setLeftFrontTyreYearPos(leftFrontTyreYearPos);
        //右前品牌型号年份选中的位置
        bmwTireStopBean.setRightFrontTyreBrandPos(rightFrontTyreBrandPos);
        bmwTireStopBean.setRightFrontTyreDiameterPos(rightFrontTyreDiameterPos);
        bmwTireStopBean.setRightFrontTyreFlatPos(rightFrontTyreFlatPos);
        bmwTireStopBean.setRightFrontTyreWidePos(rightFrontTyreWidePos);
        bmwTireStopBean.setRightFrontTyreYearPos(rightFrontTyreYearPos);
        //左后品牌型号年份选中的位置
        bmwTireStopBean.setLeftAfterTyreBrandPos(leftAfterTyreBrandPos);
        bmwTireStopBean.setLeftAfterTyreDiameterPos(leftAfterTyreDiameterPos);
        bmwTireStopBean.setLeftAfterTyreFlatPos(leftAfterTyreFlatPos);
        bmwTireStopBean.setLeftAfterTyreWidePos(leftAfterTyreWidePos);
        bmwTireStopBean.setLeftAfterTyreYearPos(leftAfterTyreYearPos);
        //右后品牌型号年份选中的位置
        bmwTireStopBean.setRightAfterTyreBrandPos(rightAfterTyreBrandPos);
        bmwTireStopBean.setRightAfterTyreDiameterPos(rightAfterTyreDiameterPos);
        bmwTireStopBean.setRightAfterTyreFlatPos(rightAfterTyreFlatPos);
        bmwTireStopBean.setRightAfterTyreWidePos(rightAfterTyreWidePos);
        bmwTireStopBean.setRightAfterTyreYearPos(rightAfterTyreYearPos);

        //胎纹深度
        bmwTireStopBean.setTyreDepthLeftFront(etLeftFront.getText().toString().trim());
        bmwTireStopBean.setTyreDepthRightFront(etRightFront.getText().toString().trim());
        bmwTireStopBean.setTyreDepthLeftAfter(etLeftBack.getText().toString().trim());
        bmwTireStopBean.setTyreDepthRightAfter(etRightBack.getText().toString().trim());
        bmwTireStopBean.setTyreDepthSpareTire(etTire.getText().toString().trim());//备胎

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean13 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean13.setNameEn("LTSC_TyreDepth");
        spCheckItemListBean13.setValueStr(etLeftFront.getText().toString().trim()+","+etRightFront.getText().toString().trim()+","+
                etLeftBack.getText().toString().trim()+","+etRightBack.getText().toString().trim()+(TextUtils.isEmpty(etTire.getText().toString().trim())?"":","+etTire.getText().toString().trim()));
        spCheckItemListBean13.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean13);

        //无备胎
        HashSet<Integer> selectedList = (HashSet<Integer>) tflNoTire.getSelectedList();
        Iterator<Integer> iterator = selectedList.iterator();
        if (iterator.hasNext()) {
            while (iterator.hasNext()) {
                tagClickPos = iterator.next();
            }
        }
        bmwTireStopBean.setNoSpareTire(tagClickPos);

        //轮胎（包含备胎）不超过5年
        int tyreYearPos = tyreYear.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyreYear(tyreYearPos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean14 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean14.setNameEn("LTSC_TyreYear");
        switch (tyreYearPos){
            case 0:
                spCheckItemListBean14.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean14.setValueStr("0");
                break;
            default:spCheckItemListBean14.setValueStr("-1");
        }
        spCheckItemListBean14.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean14);

        //轮胎制造商及尺寸符合车辆要求
        int tyreStandardPos = tyreStandard.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyreStandard(tyreStandardPos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean15 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean15.setNameEn("LTSC_TyreStandard");
        switch (tyreStandardPos){
            case 0:
                spCheckItemListBean15.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean15.setValueStr("0");
                break;
            default:spCheckItemListBean15.setValueStr("-1");
        }
        spCheckItemListBean15.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean15);

        //轮胎（包含备胎）胎纹深度最少3毫米
        int tyreTreadPos = tyreTread.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyreTread(tyreTreadPos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean16 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean16.setNameEn("LTSC_TyreTread");
        switch (tyreTreadPos){
            case 0:
                spCheckItemListBean16.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean16.setValueStr("0");
                break;
            default:spCheckItemListBean16.setValueStr("-1");
        }
        spCheckItemListBean16.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean16);

        //轮胎气压正常（并按照标准设定）
        int tyrePressurePos = tyrePressure.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyrePressure(tyrePressurePos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean17 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean17.setNameEn("LTSC_TyrePressure");
        switch (tyrePressurePos){
            case 0:
                spCheckItemListBean17.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean17.setValueStr("0");
                break;
            default:spCheckItemListBean17.setValueStr("-1");
        }
        spCheckItemListBean17.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean17);

        //检查轮毂及轮饰处于与车龄相适应的良好状态
        int tyreDecoratePos = tyreDecorate.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyreDecorate(tyreDecoratePos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean18 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean18.setNameEn("LTSC_TyreDecorate");
        switch (tyreDecoratePos){
            case 0:
                spCheckItemListBean18.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean18.setValueStr("0");
                break;
            default:spCheckItemListBean18.setValueStr("-1");
        }
        spCheckItemListBean18.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean18);

        //轮胎季节
        int tyreSeasonPos = tyreSeason.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyreSeason(tyreSeasonPos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean19 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean19.setNameEn("LTSC_TyreSeason");
        switch (tyreSeasonPos){
            case 0:
                spCheckItemListBean19.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean19.setValueStr("0");
                break;
            default:spCheckItemListBean19.setValueStr("-1");
        }
        spCheckItemListBean19.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean19);

        //轮胎新旧
        int tyreOldAndNewPos = tyreOldAndNew.getRbtnClickPos();
        bmwTireStopBean.setLTSC_TyreOldAndNew(tyreOldAndNewPos);

        SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean spCheckItemListBean20 = new SubmitModel.SpCheckItemGroupListBean.SpCheckItemListBean();
        spCheckItemListBean20.setNameEn("LTSC_TyreOldAndNew");
        switch (tyreOldAndNewPos){
            case 0:
                spCheckItemListBean20.setValueStr("1");
                break;
            case 1:
                spCheckItemListBean20.setValueStr("0");
                break;
            default:spCheckItemListBean20.setValueStr("-1");
        }
        spCheckItemListBean20.setIsQualified("-1");
        SpCheckItemList.add(spCheckItemListBean20);

        spCheckItemGroupListBean.setSpCheckItemList(SpCheckItemList);
        spCheckItemGroupList.add(spCheckItemGroupListBean);
        submitModel.setSpCheckItemGroupList(spCheckItemGroupList);
        ((BMWDetectMainActivity) getActivity()).setSubmitModel(submitModel);
        //保存到数据库
        String json = new Gson().toJson(bmwTireStopBean);
        String taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_BMW_TIRESTOP, taskId, PadSysApp.getUser().getUserId(), json);
    }

    //检查不可为空项是否都已填写并保存
    public boolean checkAndSaveData() {
        if (TextUtils.isEmpty(etLeftFront.getText().toString().trim())||TextUtils.isEmpty(etRightFront.getText().toString().trim())
                ||TextUtils.isEmpty(etLeftBack.getText().toString().trim())||TextUtils.isEmpty(etRightBack.getText().toString().trim())){
            MyToast.showLong("轮胎轮毂未填写完整");
            return false;
        }
        Set<Integer> selectedList = tflNoTire.getSelectedList();
        if (selectedList.size() == 0){
            if (TextUtils.isEmpty(etTire.getText())){
                MyToast.showLong("轮胎轮毂未填写完整");
                return false;
            }
        }
        if (tyreYear.getRg().getCheckedRadioButtonId() == -1||tyreStandard.getRg().getCheckedRadioButtonId() == -1||tyreTread.getRg().getCheckedRadioButtonId() == -1||
                tyrePressure.getRg().getCheckedRadioButtonId() == -1||tyreDecorate.getRg().getCheckedRadioButtonId() == -1||
                tyreSeason.getRg().getCheckedRadioButtonId() == -1||tyreOldAndNew.getRg().getCheckedRadioButtonId() == -1){
            MyToast.showLong("轮胎轮毂未填写完整");
            return false;
        }
        saveData();
        return true;
    }
}
