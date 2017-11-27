package com.jcpt.jzg.padsystem.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.ResultCheckedAdapter;
import com.jcpt.jzg.padsystem.adapter.ResultCheckedTagStringAdapter;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.mvpview.IGetTopWarning;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.PreventDoubleClickUtil;
import com.jcpt.jzg.padsystem.vo.CheckPriceBean;
import com.jcpt.jzg.padsystem.vo.LocalCarConfigModel;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TopWarningValueBean;
import com.jcpt.jzg.padsystem.vo.detection.CheckItem;
import com.jcpt.jzg.padsystem.vo.detection.CheckPositionItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectDetailItem;
import com.jcpt.jzg.padsystem.vo.detection.DefectType;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.ImportantItem;
import com.jcpt.jzg.padsystem.vo.detection.LocalDetectionData;
import com.jcpt.jzg.padsystem.vo.detection.StarScoreItem;
import com.jcpt.jzg.padsystem.widget.CustomRippleButton;
import com.jcpt.jzg.padsystem.widget.DividerGridItemDecoration;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jcpt.jzg.padsystem.app.PadSysApp.wrapper;

/**
 * created by wujj on 2017/2/14
 * 评估结果检查页面
 */
public class ResultCheckedActivity extends BaseActivity implements DetectMainActivity.IUploadMsgLister,IGetTopWarning {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btnSubmit)
    CustomRippleButton btnSubmit;
    @BindView(R.id.tvAppearanceCheck)
    TextView tvAppearanceCheck;
    @BindView(R.id.ll_AppearanceCheck)
    LinearLayout llAppearanceCheck;
    @BindView(R.id.tvEngineCheck)
    TextView tvEngineCheck;
    @BindView(R.id.ll_EngineCheck)
    LinearLayout llEngineCheck;
    @BindView(R.id.tvDecorateCheck)
    TextView tvDecorateCheck;
    @BindView(R.id.ll_DecorateCheck)
    LinearLayout llDecorateCheck;
    @BindView(R.id.tvElectricalCheck)
    TextView tvElectricalCheck;
    @BindView(R.id.ll_ElectricalCheck)
    LinearLayout llElectricalCheck;
    @BindView(R.id.tvFire)
    TextView tvFire;
    @BindView(R.id.tvWater)
    TextView tvWater;
    @BindView(R.id.tvMarketOwnership)
    TextView tvMarketOwnership;
    @BindView(R.id.tvMarketAcceptance)
    TextView tvMarketAcceptance;
    @BindView(R.id.tvMarketHedgeRatio)
    TextView tvMarketHedgeRatio;
    @BindView(R.id.tvOtherInfo)
    TextView tvOtherInfo;
    @BindView(R.id.tvVinOtherInfoChecked)
    TextView tvVinOtherInfoChecked;
    @BindView(R.id.tvAssessmentPrace)
    TextView tvAssessmentPrace;
    @BindView(R.id.tvSalePrice)
    TextView tvSalePrice;
    @BindView(R.id.activity_result_checked)
    LinearLayout activityResultChecked;
    @BindView(R.id.tvAccidentCheck)
    TextView tvAccidentCheck;
    @BindView(R.id.ll_AccidentCheck)
    LinearLayout llAccidentCheck;
    @BindView(R.id.tvChassisCheck)
    TextView tvChassisCheck;
    @BindView(R.id.ll_ChassisCheck)
    LinearLayout llChassisCheck;
    @BindView(R.id.tvNameplateProperty)
    TextView tvNameplateProperty;
    @BindView(R.id.ll_NameplateProperty)
    LinearLayout llNameplateProperty;
    @BindView(R.id.ll_Vin)
    LinearLayout llVin;
    @BindView(R.id.tvTyre)
    TextView tvTyre;
    @BindView(R.id.ll_Tyre)
    LinearLayout llTyre;
    @BindView(R.id.tvDrivingLicenseCheckEx)
    TextView tvDrivingLicenseCheckEx;
    @BindView(R.id.ll_DrivingLicenseCheckEx)
    LinearLayout llDrivingLicenseCheckEx;
    @BindView(R.id.view_line_otherInfo)
    View view_line_otherInfo;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tflAccident)
    TagFlowLayout tflAccident;
    @BindView(R.id.llAccidentQuexianxiang)
    LinearLayout llAccidentQuexianxiang;
    @BindView(R.id.ll_AccidentCheckQuexianxiang)
    LinearLayout llAccidentCheckQuexianxiang;
    @BindView(R.id.flAppearanceCheck)
    FrameLayout flAppearanceCheck;
    @BindView(R.id.flAccidentCheck)
    FrameLayout flAccidentCheck;
    @BindView(R.id.flEngineCheck)
    FrameLayout flEngineCheck;
    @BindView(R.id.flDecorateCheck)
    FrameLayout flDecorateCheck;
    @BindView(R.id.flChassisCheck)
    FrameLayout flChassisCheck;
    @BindView(R.id.flElectricalCheck)
    FrameLayout flElectricalCheck;
    @BindView(R.id.tflAppearanceCheck)
    TagFlowLayout tflAppearanceCheck;
    @BindView(R.id.tflAccidentCheck)
    TagFlowLayout tflAccidentCheck;
    @BindView(R.id.tflEngineCheck)
    TagFlowLayout tflEngineCheck;
    @BindView(R.id.tflDecorateCheck)
    TagFlowLayout tflDecorateCheck;
    @BindView(R.id.tflChassisCheck)
    TagFlowLayout tflChassisCheck;
    @BindView(R.id.tflElectricalCheck)
    TagFlowLayout tflElectricalCheck;
    @BindView(R.id.tflFire)
    TagFlowLayout tflFire;
    @BindView(R.id.tflwater)
    TagFlowLayout tflwater;
    @BindView(R.id.llAppearanceCheckBg)
    LinearLayout llAppearanceCheckBg;
    @BindView(R.id.llAccidentCheckBg)
    LinearLayout llAccidentCheckBg;
    @BindView(R.id.llEngineCheckBg)
    LinearLayout llEngineCheckBg;
    @BindView(R.id.llDecorateCheckBg)
    LinearLayout llDecorateCheckBg;
    @BindView(R.id.llChassisCheckBg)
    LinearLayout llChassisCheckBg;
    @BindView(R.id.llElectricalCheckBg)
    LinearLayout llElectricalCheckBg;
    @BindView(R.id.llFireBg)
    LinearLayout llFireBg;
    @BindView(R.id.llWaterBg)
    LinearLayout llWaterBg;
    @BindView(R.id.llFire)
    LinearLayout llFire;
    @BindView(R.id.llWater)
    LinearLayout llWater;
    private SubmitModel submitModel;
    private View appearanceStarChecked1;
    private View appearanceStarChecked2;
    private View appearanceStarChecked3;
    private View appearanceStarChecked4;
    private View appearanceStarChecked5;
    private View accidentStarChecked1;
    private View accidentStarChecked2;
    private View accidentStarChecked3;
    private View accidentStarChecked4;
    private View accidentStarChecked5;
    private View engineStarChecked1;
    private View engineStarChecked2;
    private View engineStarChecked3;
    private View engineStarChecked4;
    private View engineStarChecked5;
    private View decorateCheckStarChecked1;
    private View decorateCheckStarChecked2;
    private View decorateCheckStarChecked3;
    private View decorateCheckStarChecked4;
    private View decorateCheckStarChecked5;
    private View chassisCheckStarChecked1;
    private View chassisCheckStarChecked2;
    private View chassisCheckStarChecked3;
    private View chassisCheckStarChecked4;
    private View chassisCheckStarChecked5;
    private View electricalCheckStarChecked1;
    private View electricalCheckStarChecked2;
    private View electricalCheckStarChecked3;
    private View electricalCheckStarChecked4;
    private View electricalCheckStarChecked5;
    private String carFullName;
    //下面是：配置项
    private boolean configDriving = false;//行驶证照片 81
    private boolean configRegister = false;//登记证照片 82
    private boolean configOwener = false;//机动车所有人 83
    private boolean configCardType = false;//证件类型 84
    private boolean configCardId = false;//车主证件号 85
    private boolean configRegisterSame = false;//登记证与车主证件号 86
    private boolean configRegisterDate = false;//登记日期 87
    private boolean configRegion = false;//初登地区 88
    private boolean configPlateNum = false;//车牌号码 89
    private boolean configCarType = false;//品牌型号 90
    private boolean configVin = false;//vin 91
    private boolean configEngineId = false;//发动机号 92
    private boolean configFuelType = false;//燃料种类 93
    private boolean configLoadNum = false;//载客数量 94
    private boolean configObtainWay = false;//获得方式 95
    private boolean configCarColor = false;//车身颜色 96
    private boolean configIsMadeInChina = false;//进口国产 97
    private boolean configDisplacement = false;//排量 98
    private boolean configTireType = false;//轮胎规格 99
    private boolean configUseNature = false;//使用性质 100
    private boolean configProductDate = false;//出厂日期 101
    private boolean configTransferNum = false;//过户次数 102
    private boolean configLastTransferDate = false;//最后过户日期 103
    private boolean configOldUser = false;//曾使用方 104
    private boolean configCurUser = false;//现使用方 105
    private boolean configInsuranceBill = false;//交强险保单 106
    private boolean configInsuranceExpDate = false;//交强险到期日 107
    private boolean configInsuranceAddress = false;//交强险所在地 108
    private boolean configCarBill = false;//原车发票 109
    private boolean configOtherBill = false;//其他票证 110
    private boolean configRegisterAdditionInfo = false;//登记证附加信息 111
    private boolean configAnnualDate = false;//年检有效期 112
    private boolean configPlateRegion = false;//上牌地区 276
    private boolean configExtraKey= false;//备用钥匙 277
    private boolean configEmissionStandard= false;//排放标准 297
    private List<String> tflAccidentList = new ArrayList<>();
    private List<String> tflAppearanceCheckList = new ArrayList<>();//属于外观检查缺陷项集合
    private List<String> tflAccidentCheckList = new ArrayList<>();//属于事故检查缺陷项集合
    private List<String> tflEngineCheckList = new ArrayList<>();//属于机舱检查缺陷项集合
    private List<String> tflDecorateCheckList = new ArrayList<>();//属于内饰检查缺陷项集合
    private List<String> tflChassisCheckList = new ArrayList<>();//属于底盘检查缺陷项集合
    private List<String> tflElectricalCheckList = new ArrayList<>();//属于电气检查缺陷项集合
    private List<String> tflFireList = new ArrayList<>();//属于火烧迹象缺陷项集合
    private List<String> tflWaterList = new ArrayList<>();//属于泡水迹象检查缺陷项集合
    private String[] tflAccidentAllDefect;
    private String[] tflAppearanceDefectList;
    private String[] tflAccidentDefectList;
    private String[] tflEngineDefectList;
    private String[] tflDecorateDefectList;
    private String[] tflChassisDefectList;
    private String[] tflElectricalDefectList;
    private String[] tflFireDefectList;
    private String[] tflWaterDefectList;
    private String taskId;
    private String tvCertificateEx = "";


    DetectMainActivity detectMainActivity;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_result_checked);
        ButterKnife.bind(this);
        detectMainActivity = DetectMainActivity.detectMainActivity;
        detectMainActivity.setmIUploadMsgLister(this);
        initView();
        setView();
    }

    private void setView() {
        //根据服务器返回的配置项决定隐藏哪些检测评级项
        List<String> reportStarSelect = wrapper.getReportStarSelect();
        if (reportStarSelect != null) {
            int reportStarSelectSize = reportStarSelect.size();
            for (int i = 0; i < reportStarSelectSize; i++) {
                String R0X = reportStarSelect.get(i);
                //外观检查
                if (R0X.equals("R01")) {
                    llAppearanceCheck.setVisibility(View.GONE);
                }
                //事故检查
                if (R0X.equals("R03")) {
                    llAccidentCheck.setVisibility(View.GONE);
                }
                //机舱检查
                if (R0X.equals("R08")) {
                    llEngineCheck.setVisibility(View.GONE);
                }
                //内饰检查
                if (R0X.equals("R02")) {
                    llDecorateCheck.setVisibility(View.GONE);
                }
                //底盘检查
                if (R0X.equals("R10")) {
                    llChassisCheck.setVisibility(View.GONE);
                }
                //电气检查
                if (R0X.equals("R06")) {
                    llElectricalCheck.setVisibility(View.GONE);
                }
                //火烧检查
                if (R0X.equals("R04")) {
                    llFire.setVisibility(View.GONE);
                }
                //泡水检查
                if (R0X.equals("R05")) {
                    llWater.setVisibility(View.GONE);
                }
            }
        }
        if (wrapper.getIsAuto() == 1) {//自动
            //如果是自动则显示所有检查
            llAppearanceCheck.setVisibility(View.VISIBLE);
            llAccidentCheck.setVisibility(View.VISIBLE);
            llEngineCheck.setVisibility(View.VISIBLE);
            llDecorateCheck.setVisibility(View.VISIBLE);
            llChassisCheck.setVisibility(View.VISIBLE);
            llElectricalCheck.setVisibility(View.VISIBLE);
            llFire.setVisibility(View.VISIBLE);
            llWater.setVisibility(View.VISIBLE);
            llAccidentCheckQuexianxiang.setVisibility(View.GONE);//隐藏缺陷项
            if (llAppearanceCheck.getVisibility() == View.VISIBLE) {
                flAppearanceCheck.setVisibility(View.GONE);
                tvAppearanceCheck.setVisibility(View.GONE);
                tflAppearanceCheck.setVisibility(View.VISIBLE);
            }
            if (llAccidentCheck.getVisibility() == View.VISIBLE) {
                flAccidentCheck.setVisibility(View.GONE);
                tvAccidentCheck.setVisibility(View.GONE);
                tflAccidentCheck.setVisibility(View.VISIBLE);
            }
            if (llEngineCheck.getVisibility() == View.VISIBLE) {
                flEngineCheck.setVisibility(View.GONE);
                tvEngineCheck.setVisibility(View.GONE);
                tflEngineCheck.setVisibility(View.VISIBLE);
            }
            if (llDecorateCheck.getVisibility() == View.VISIBLE) {
                flDecorateCheck.setVisibility(View.GONE);
                tvDecorateCheck.setVisibility(View.GONE);
                tflDecorateCheck.setVisibility(View.VISIBLE);
            }
            if (llChassisCheck.getVisibility() == View.VISIBLE) {
                flChassisCheck.setVisibility(View.GONE);
                tvChassisCheck.setVisibility(View.GONE);
                tflChassisCheck.setVisibility(View.VISIBLE);
            }
            if (llElectricalCheck.getVisibility() == View.VISIBLE) {
                flElectricalCheck.setVisibility(View.GONE);
                tvElectricalCheck.setVisibility(View.GONE);
                tflElectricalCheck.setVisibility(View.VISIBLE);
            }
            tvFire.setVisibility(View.GONE);
            tflFire.setVisibility(View.VISIBLE);
            tvWater.setVisibility(View.GONE);
            tflwater.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        //外观检查星级
        appearanceStarChecked1 = findViewById(R.id.appearanceStarChecked1);
        appearanceStarChecked2 = findViewById(R.id.appearanceStarChecked2);
        appearanceStarChecked3 = findViewById(R.id.appearanceStarChecked3);
        appearanceStarChecked4 = findViewById(R.id.appearanceStarChecked4);
        appearanceStarChecked5 = findViewById(R.id.appearanceStarChecked5);
        //事故检查星级
        accidentStarChecked1 = findViewById(R.id.accidentStarChecked1);
        accidentStarChecked2 = findViewById(R.id.accidentStarChecked2);
        accidentStarChecked3 = findViewById(R.id.accidentStarChecked3);
        accidentStarChecked4 = findViewById(R.id.accidentStarChecked4);
        accidentStarChecked5 = findViewById(R.id.accidentStarChecked5);
        //机舱检查星级
        engineStarChecked1 = findViewById(R.id.engineStarChecked1);
        engineStarChecked2 = findViewById(R.id.engineStarChecked2);
        engineStarChecked3 = findViewById(R.id.engineStarChecked3);
        engineStarChecked4 = findViewById(R.id.engineStarChecked4);
        engineStarChecked5 = findViewById(R.id.engineStarChecked5);
        //内饰检查星级
        decorateCheckStarChecked1 = findViewById(R.id.decorateCheckStarChecked1);
        decorateCheckStarChecked2 = findViewById(R.id.decorateCheckStarChecked2);
        decorateCheckStarChecked3 = findViewById(R.id.decorateCheckStarChecked3);
        decorateCheckStarChecked4 = findViewById(R.id.decorateCheckStarChecked4);
        decorateCheckStarChecked5 = findViewById(R.id.decorateCheckStarChecked5);
        //底盘检查星级
        chassisCheckStarChecked1 = findViewById(R.id.chassisCheckStarChecked1);
        chassisCheckStarChecked2 = findViewById(R.id.chassisCheckStarChecked2);
        chassisCheckStarChecked3 = findViewById(R.id.chassisCheckStarChecked3);
        chassisCheckStarChecked4 = findViewById(R.id.chassisCheckStarChecked4);
        chassisCheckStarChecked5 = findViewById(R.id.chassisCheckStarChecked5);
        //电气检查星级
        electricalCheckStarChecked1 = findViewById(R.id.electricalCheckStarChecked1);
        electricalCheckStarChecked2 = findViewById(R.id.electricalCheckStarChecked2);
        electricalCheckStarChecked3 = findViewById(R.id.electricalCheckStarChecked3);
        electricalCheckStarChecked4 = findViewById(R.id.electricalCheckStarChecked4);
        electricalCheckStarChecked5 = findViewById(R.id.electricalCheckStarChecked5);
    }

    public void setTag(final String[] vals, final TagFlowLayout tagFlowLayout) {
        ResultCheckedTagStringAdapter resultCheckedTagStringAdapter = new ResultCheckedTagStringAdapter(vals, tagFlowLayout, this);
        tagFlowLayout.setAdapter(resultCheckedTagStringAdapter);
    }

    @Override
    protected void setData() {
        submitModel = detectMainActivity.getSubmitModel();
        if (wrapper.getIsAuto() == 0) {//手动
            //缺陷项--列出车况检测所有缺陷项
            LocalDetectionData data = detectMainActivity.getLocalDetectionData();
            if (data != null) {
                List<CheckPositionItem> checkPositionList = data.getCheckPositionList();//所有检测方位的集合
                if (checkPositionList != null) {
                    int checkPositionListLen = checkPositionList.size();
                    if (checkPositionListLen > 0) {
                        for (int i = 0; i < checkPositionListLen; i++) {//遍历每个检测方位
                            List<ImportantItem> importantList = checkPositionList.get(i).getImportantList();//每一个检测方位下的重点与非重点项
                            if (importantList != null) {
                                int importantListLen = importantList.size();
                                if (importantListLen > 0) {
                                    for (int q = 0; q < importantListLen; q++) {
                                        ImportantItem importantItem = importantList.get(q);//重点或非重点
                                        if (importantItem != null) {
                                            String importantId = importantItem.getImportantId();
                                            List<CheckItem> checkItemList = importantItem.getCheckItemList();//一个方位下的所有检测项
                                            if (checkItemList != null) {
                                                int checkItemListLen = checkItemList.size();
                                                switch (importantId) {
                                                    case "0"://非重点
                                                        if (checkItemListLen > 0) {
                                                            for (int w = 0; w < checkItemListLen; w++) {
                                                                List<DefectType> defectTypeList = checkItemList.get(w).getDefectTypeList();//一个检测项下的所有缺陷值
                                                                if (defectTypeList != null) {
                                                                    int defectTypeListLen = defectTypeList.size();
                                                                    for (int e = 0; e < defectTypeListLen; e++) {//遍历缺陷值
                                                                        DefectType defectType = defectTypeList.get(e);
                                                                        List<DefectDetailItem> defectDetailList2 = defectType.getDefectDetailList();
                                                                        for (int i1 = 0; i1 < defectDetailList2.size(); i1++) {
                                                                            int status = defectDetailList2.get(i1).getStatus();
                                                                            if (status == 1) {//选中
                                                                                tflAccidentList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        break;
                                                    case "1"://重点
                                                        if (checkItemListLen > 0) {
                                                            for (int w = 0; w < checkItemListLen; w++) {
                                                                List<DefectType> defectTypeList = checkItemList.get(w).getDefectTypeList();//一个检测项下的所有缺陷值
                                                                if (defectTypeList != null) {
                                                                    int defectTypeListLen = defectTypeList.size();
                                                                    for (int e = 0; e < defectTypeListLen; e++) {//遍历缺陷值
                                                                        DefectType defectType = defectTypeList.get(e);
                                                                        List<DefectDetailItem> defectDetailList2 = defectType.getDefectDetailList();
                                                                        for (int i1 = 0; i1 < defectDetailList2.size(); i1++) {
                                                                            int status = defectDetailList2.get(i1).getStatus();
                                                                            if (status == 1) {//选中
                                                                                tflAccidentList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        break;
                                                }
                                                if (tflAccidentList != null && tflAccidentList.size() > 0) {
                                                    llAccidentQuexianxiang.setVisibility(View.GONE);
                                                    tflAccident.setVisibility(View.VISIBLE);
                                                    tflAccidentAllDefect = new String[tflAccidentList.size()];
                                                    for (int d = 0; d < tflAccidentList.size(); d++) {
                                                        String s = tflAccidentList.get(d);
                                                        tflAccidentAllDefect[d] = s;
                                                    }
                                                    setTag(tflAccidentAllDefect, tflAccident);
                                                } else {
                                                    tflAccident.setVisibility(View.GONE);
                                                    llAccidentQuexianxiang.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (submitModel != null) {
                //外观检查--星星数量代表意义：0默认无选择、1全车外观很差，存在较多修复痕迹、2全车外观整体较差，存在多处复痕迹、3全车外观存在少量缺陷，存在少量修复痕迹、
                // 4全车外观仅存在少量瑕疵，细微修复痕迹、5全车外观仅存在细微瑕疵
                List<StarScoreItem> scoreProjectDesList = wrapper.getScoreProjectDesList();
                if (llAppearanceCheck.getVisibility() == View.VISIBLE) {
                    int appearanceCheck = submitModel.getAppearanceCheck();
                    switch (appearanceCheck) {
                        case 0:
                            break;
                        case 1:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R01") && starScoreItem.getFinalScoreIntervalId() == 1){
                                        tvAppearanceCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAppearanceCheck.setText("车辆外观存在多处损伤，多处修复痕迹");
                            }
                            appearanceStarChecked1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R01") && starScoreItem.getFinalScoreIntervalId() == 2){
                                        tvAppearanceCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAppearanceCheck.setText("车辆外观存在少量损伤，多处修复痕迹");
                            }
                            appearanceStarChecked2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R01") && starScoreItem.getFinalScoreIntervalId() == 3){
                                        tvAppearanceCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAppearanceCheck.setText("车辆外观存在少量损伤，少量修复痕迹");
                            }
                            appearanceStarChecked3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R01") && starScoreItem.getFinalScoreIntervalId() == 4){
                                        tvAppearanceCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAppearanceCheck.setText("车辆外观无损伤，存在少量修复痕迹");
                            }
                            appearanceStarChecked4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R01") && starScoreItem.getFinalScoreIntervalId() == 5){
                                        tvAppearanceCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAppearanceCheck.setText("车辆外观无损伤，无修复痕迹");
                            }
                            appearanceStarChecked5.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
                //事故检查
                if (llAccidentCheck.getVisibility() == View.VISIBLE) {
                    int accidentCheck = submitModel.getAccidentCheck();
                    switch (accidentCheck) {
                        case 0:
                            break;
                        case 1:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R03") && starScoreItem.getFinalScoreIntervalId() == 1){
                                        tvAccidentCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAccidentCheck.setText("车辆因碰撞事故，导致多处结构件严重损伤或有结构件存在切割更换修复痕迹");
                            }
                            accidentStarChecked1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R03") && starScoreItem.getFinalScoreIntervalId() == 2){
                                        tvAccidentCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAccidentCheck.setText("车辆因碰撞事故，导致多处外围结构件，部分重要结构件损伤或修复痕迹");
                            }
                            accidentStarChecked2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R03") && starScoreItem.getFinalScoreIntervalId() == 3){
                                        tvAccidentCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAccidentCheck.setText("车辆因轻微碰撞事故，导致部分外围结构件存在损伤或修复痕迹");
                            }
                            accidentStarChecked3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R03") && starScoreItem.getFinalScoreIntervalId() == 4){
                                        tvAccidentCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAccidentCheck.setText("车辆因轻微碰撞事故，导致前防撞梁、可拆卸前纵梁吸能盒及底大边存在轻微损伤或修复痕迹");
                            }
                            accidentStarChecked4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R03") && starScoreItem.getFinalScoreIntervalId() == 5){
                                        tvAccidentCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvAccidentCheck.setText("全车结构件无损伤，无修复痕迹");
                            }
                            accidentStarChecked5.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
                //机舱检查
                if (llEngineCheck.getVisibility() == View.VISIBLE) {
                    int engineCheck = submitModel.getEngineCheck();
                    switch (engineCheck) {
                        case 0:
                            break;
                        case 1:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R08") && starScoreItem.getFinalScoreIntervalId() == 1){
                                        tvEngineCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvEngineCheck.setText("发动机怠速工况存在缺陷，机舱严重油污，机舱部件存在严重缺陷");
                            }
                            engineStarChecked1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R08") && starScoreItem.getFinalScoreIntervalId() == 2){
                                        tvEngineCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvEngineCheck.setText("发动机怠速工况较差，机舱部件存在较多缺陷");
                            }
                            engineStarChecked2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R08") && starScoreItem.getFinalScoreIntervalId() == 3){
                                        tvEngineCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvEngineCheck.setText("发动机怠速工况一般，机舱部件存在少量缺陷");
                            }
                            engineStarChecked3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R08") && starScoreItem.getFinalScoreIntervalId() == 4){
                                        tvEngineCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvEngineCheck.setText("发动机怠速工况正常，机舱部件少量老化");
                            }
                            engineStarChecked4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R08") && starScoreItem.getFinalScoreIntervalId() == 5){
                                        tvEngineCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvEngineCheck.setText("发动机怠速工况正常，舱内部件无缺失");
                            }
                            engineStarChecked5.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
                //内饰检查
                if (llDecorateCheck.getVisibility() == View.VISIBLE) {
                    int decorateCheck = submitModel.getDecorateCheck();
                    switch (decorateCheck) {
                        case 0:
                            break;
                        case 1:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R02") && starScoreItem.getFinalScoreIntervalId() == 1){
                                        tvDecorateCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvDecorateCheck.setText("内饰存在多处破损、多处脏污");
                            }
                            decorateCheckStarChecked1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R02") && starScoreItem.getFinalScoreIntervalId() == 2){
                                        tvDecorateCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvDecorateCheck.setText("内饰存在少量破损、多处脏污");
                            }
                            decorateCheckStarChecked2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R02") && starScoreItem.getFinalScoreIntervalId() == 3){
                                        tvDecorateCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvDecorateCheck.setText("内饰存在少量破损、少量脏污");
                            }
                            decorateCheckStarChecked3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R02") && starScoreItem.getFinalScoreIntervalId() == 4){
                                        tvDecorateCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvDecorateCheck.setText("内饰存在轻微破损、少量脏污");
                            }
                            decorateCheckStarChecked4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R02") && starScoreItem.getFinalScoreIntervalId() == 5){
                                        tvDecorateCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvDecorateCheck.setText("内饰整洁，无破损、无脏污");
                            }
                            decorateCheckStarChecked5.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
                //底盘检查
                if (llChassisCheck.getVisibility() == View.VISIBLE) {
                    int chassisCheck = submitModel.getChassisCheck();
                    switch (chassisCheck) {
                        case 0:
                            break;
                        case 1:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R10") && starScoreItem.getFinalScoreIntervalId() == 1){
                                        tvChassisCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvChassisCheck.setText("底盘部件存在多处损伤、多处修复痕迹");
                            }
                            chassisCheckStarChecked1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R10") && starScoreItem.getFinalScoreIntervalId() == 2){
                                        tvChassisCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvChassisCheck.setText("底盘部件存在少量损伤、多处修复痕迹");
                            }
                            chassisCheckStarChecked2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R10") && starScoreItem.getFinalScoreIntervalId() == 3){
                                        tvChassisCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvChassisCheck.setText("底盘部件存在少量损伤、少量修复痕迹");
                            }
                            chassisCheckStarChecked3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R10") && starScoreItem.getFinalScoreIntervalId() == 4){
                                        tvChassisCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvChassisCheck.setText("底盘部件无损伤、存在少量修复痕迹");
                            }
                            chassisCheckStarChecked4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R10") && starScoreItem.getFinalScoreIntervalId() == 5){
                                        tvChassisCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvChassisCheck.setText("底盘部件无损伤、无修复");
                            }
                            chassisCheckStarChecked5.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
                //电气检查
                if (llElectricalCheck.getVisibility() == View.VISIBLE) {
                    int electricalCheck = submitModel.getElectricalCheck();
                    switch (electricalCheck) {
                        case 0:
                            break;
                        case 1:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R06") && starScoreItem.getFinalScoreIntervalId() == 1){
                                        tvElectricalCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvElectricalCheck.setText("全车电气部件外观部件有缺失，有功能故障");
                            }
                            electricalCheckStarChecked1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R06") && starScoreItem.getFinalScoreIntervalId() == 2){
                                        tvElectricalCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvElectricalCheck.setText("全车电气部件外观有损伤，有功能故障");
                            }
                            electricalCheckStarChecked2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R06") && starScoreItem.getFinalScoreIntervalId() == 3){
                                        tvElectricalCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvElectricalCheck.setText("全车电气部件外观正常磨损，有功能故障");
                            }
                            electricalCheckStarChecked3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R06") && starScoreItem.getFinalScoreIntervalId() == 4){
                                        tvElectricalCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvElectricalCheck.setText("全车电气部件外观正常磨损，无功能故障");
                            }
                            electricalCheckStarChecked4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            if (scoreProjectDesList != null && scoreProjectDesList.size() > 0){
                                for (StarScoreItem starScoreItem : scoreProjectDesList) {
                                    if (starScoreItem.getReportProjectId().equals("R06") && starScoreItem.getFinalScoreIntervalId() == 5){
                                        tvElectricalCheck.setText(starScoreItem.getResultDes());
                                    }
                                }
                            }else {
                                tvElectricalCheck.setText("全车电气部件外观无损伤，无功能故障");
                            }
                            electricalCheckStarChecked5.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                }
                //火烧迹象
                int burningMark = submitModel.getBurningMark();
                switch (burningMark) {
                    case 0:
                        tvFire.setText("未见火烧迹象");
                        break;
                    case 1:
                        tvFire.setText("存在火烧迹象");
                        break;
                    case 2:
                        tvFire.setText("经综合判断为火烧车");
                        break;
                    default:
                }
                //泡水迹象
                int waterMark = submitModel.getWaterMark();
                switch (waterMark) {
                    case 0:
                        tvWater.setText("未见泡水迹象");
                        break;
                    case 1:
                        tvWater.setText("存在泡水迹象");
                        break;
                    case 2:
                        tvWater.setText("经综合判断为泡水车");
                        break;
                    default:
                }
            }
        } else if (wrapper.getIsAuto() == 1){//自动
                /*
     * R01	外观检查
     * R02	内饰检查
     * R03	事故检查
     * R04	火烧检查
     * R05	泡水检查
     * R06	电气检查
     * R07	手续信息
     * R08	机舱检查
     * R09	机械检查
     * R10	底盘检查
     * R11	参数配置
     * */
            //缺陷项--列出车况检测所有缺陷项
            LocalDetectionData data1 = detectMainActivity.getLocalDetectionData();
            if (data1 != null) {
                List<CheckPositionItem> checkPositionList = data1.getCheckPositionList();//所有检测方位的集合
                if (checkPositionList != null) {
                    int checkPositionListLen = checkPositionList.size();
                    if (checkPositionListLen > 0) {
                        for (int i = 0; i < checkPositionListLen; i++) {//遍历每个检测方位
                            List<ImportantItem> importantList = checkPositionList.get(i).getImportantList();//每一个检测方位下的重点与非重点项
                            if (importantList != null) {
                                int importantListLen = importantList.size();
                                if (importantListLen > 0) {
                                    for (int q = 0; q < importantListLen; q++) {
                                        ImportantItem importantItem = importantList.get(q);//重点或非重点
                                        if (importantItem != null) {
                                            String importantId = importantItem.getImportantId();
                                            List<CheckItem> checkItemList = importantItem.getCheckItemList();//一个方位下的所有检测项
                                            if (checkItemList != null) {
                                                int checkItemListLen = checkItemList.size();
                                                switch (importantId) {
                                                    case "0"://非重点
                                                        if (checkItemListLen > 0) {
                                                            for (int w = 0; w < checkItemListLen; w++) {
                                                                List<DefectType> defectTypeList = checkItemList.get(w).getDefectTypeList();//一个检测项下的所有缺陷值
                                                                if (defectTypeList != null) {
                                                                    int defectTypeListLen = defectTypeList.size();
                                                                    for (int e = 0; e < defectTypeListLen; e++) {//遍历缺陷值
                                                                        DefectType defectType = defectTypeList.get(e);
                                                                        List<DefectDetailItem> defectDetailList2 = defectType.getDefectDetailList();
                                                                        for (int i1 = 0; i1 < defectDetailList2.size(); i1++) {
                                                                            int status = defectDetailList2.get(i1).getStatus();
                                                                            if (status == 1) {//选中
                                                                                switch (defectDetailList2.get(i1).getReportId()) {
                                                                                    case "R01"://外观检查
                                                                                        tflAppearanceCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R03"://事故检查
                                                                                        tflAccidentCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R08"://机舱检查
                                                                                        tflEngineCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R02"://内饰检查
                                                                                        tflDecorateCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R10"://底盘检查
                                                                                        tflChassisCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R06"://电气检查
                                                                                        tflElectricalCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R04"://火烧迹象
                                                                                        tflFireList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R05"://泡水迹象
                                                                                        tflWaterList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    default:
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        break;
                                                    case "1"://重点
                                                        if (checkItemListLen > 0) {
                                                            for (int w = 0; w < checkItemListLen; w++) {
                                                                List<DefectType> defectTypeList = checkItemList.get(w).getDefectTypeList();//一个检测项下的所有缺陷值
                                                                if (defectTypeList != null) {
                                                                    int defectTypeListLen = defectTypeList.size();
                                                                    for (int e = 0; e < defectTypeListLen; e++) {//遍历缺陷值
                                                                        DefectType defectType = defectTypeList.get(e);
                                                                        List<DefectDetailItem> defectDetailList2 = defectType.getDefectDetailList();
                                                                        for (int i1 = 0; i1 < defectDetailList2.size(); i1++) {
                                                                            int status = defectDetailList2.get(i1).getStatus();
                                                                            String reportId = defectDetailList2.get(i1).getReportId();
                                                                            if (status == 1) {//选中
                                                                                switch (defectDetailList2.get(i1).getReportId()) {
                                                                                    case "R01"://外观检查
                                                                                        tflAppearanceCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R03"://事故检查
                                                                                        tflAccidentCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R08"://机舱检查
                                                                                        tflEngineCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R02"://内饰检查
                                                                                        tflDecorateCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R10"://底盘检查
                                                                                        tflChassisCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R06"://电气检查
                                                                                        tflElectricalCheckList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R04"://火烧迹象
                                                                                        tflFireList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    case "R05"://泡水迹象
                                                                                        tflWaterList.add(checkItemList.get(w).getCheckName() + defectDetailList2.get(i1).getDefectName());
                                                                                        break;
                                                                                    default:
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //外观检查
                if (tflAppearanceCheckList != null && tflAppearanceCheckList.size() > 0) {
                    llAppearanceCheckBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflAppearanceCheck.setVisibility(View.VISIBLE);
                    tflAppearanceDefectList = new String[tflAppearanceCheckList.size()];
                    for (int d = 0; d < tflAppearanceCheckList.size(); d++) {
                        String s = tflAppearanceCheckList.get(d);
                        tflAppearanceDefectList[d] = s;
                    }
                    setTag(tflAppearanceDefectList, tflAppearanceCheck);
                } else {
                    tflAppearanceCheck.setVisibility(View.GONE);
                    tvAppearanceCheck.setVisibility(View.VISIBLE);
                    tvAppearanceCheck.setText("外观检查未选择缺陷");
                }
                //事故检查
                if (tflAccidentCheckList != null && tflAccidentCheckList.size() > 0) {
                    llAccidentCheckBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflAccidentCheck.setVisibility(View.VISIBLE);
                    tflAccidentDefectList = new String[tflAccidentCheckList.size()];
                    for (int d = 0; d < tflAccidentCheckList.size(); d++) {
                        String s = tflAccidentCheckList.get(d);
                        tflAccidentDefectList[d] = s;
                    }
                    setTag(tflAccidentDefectList, tflAccidentCheck);
                } else {
                    tflAccidentCheck.setVisibility(View.GONE);
                    tvAccidentCheck.setVisibility(View.VISIBLE);
                    tvAccidentCheck.setText("事故检查未选择缺陷");
                }
                //机舱检查
                if (tflEngineCheckList != null && tflEngineCheckList.size() > 0) {
                    llEngineCheckBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflEngineCheck.setVisibility(View.VISIBLE);
                    tflEngineDefectList = new String[tflEngineCheckList.size()];
                    for (int d = 0; d < tflEngineCheckList.size(); d++) {
                        String s = tflEngineCheckList.get(d);
                        tflEngineDefectList[d] = s;
                    }
                    setTag(tflEngineDefectList, tflEngineCheck);
                } else {
                    tflEngineCheck.setVisibility(View.GONE);
                    tvEngineCheck.setVisibility(View.VISIBLE);
                    tvEngineCheck.setText("机舱检查未选择缺陷");
                }
                //内饰检查
                if (tflDecorateCheckList != null && tflDecorateCheckList.size() > 0) {
                    llDecorateCheckBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflDecorateCheck.setVisibility(View.VISIBLE);
                    tflDecorateDefectList = new String[tflDecorateCheckList.size()];
                    for (int d = 0; d < tflDecorateCheckList.size(); d++) {
                        String s = tflDecorateCheckList.get(d);
                        tflDecorateDefectList[d] = s;
                    }
                    setTag(tflDecorateDefectList, tflDecorateCheck);
                } else {
                    tflDecorateCheck.setVisibility(View.GONE);
                    tvDecorateCheck.setVisibility(View.VISIBLE);
                    tvDecorateCheck.setText("内饰检查未选择缺陷");
                }
                //底盘检查
                if (tflChassisCheckList != null && tflChassisCheckList.size() > 0) {
                    llChassisCheckBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflChassisCheck.setVisibility(View.VISIBLE);
                    tflChassisDefectList = new String[tflChassisCheckList.size()];
                    for (int d = 0; d < tflChassisCheckList.size(); d++) {
                        String s = tflChassisCheckList.get(d);
                        tflChassisDefectList[d] = s;
                    }
                    setTag(tflChassisDefectList, tflChassisCheck);
                } else {
                    tflChassisCheck.setVisibility(View.GONE);
                    tvChassisCheck.setVisibility(View.VISIBLE);
                    tvChassisCheck.setText("底盘检查未选择缺陷");
                }
                //电气检查
                if (tflElectricalCheckList != null && tflElectricalCheckList.size() > 0) {
                    llElectricalCheckBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflElectricalCheck.setVisibility(View.VISIBLE);
                    tflElectricalDefectList = new String[tflElectricalCheckList.size()];
                    for (int d = 0; d < tflElectricalCheckList.size(); d++) {
                        String s = tflElectricalCheckList.get(d);
                        tflElectricalDefectList[d] = s;
                    }
                    setTag(tflElectricalDefectList, tflElectricalCheck);
                } else {
                    tflElectricalCheck.setVisibility(View.GONE);
                    tvElectricalCheck.setVisibility(View.VISIBLE);
                    tvElectricalCheck.setText("电气检查未选择缺陷");
                }
                //火烧迹象
                if (tflFireList != null && tflFireList.size() > 0) {
                    llFireBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflFire.setVisibility(View.VISIBLE);
                    tflFireDefectList = new String[tflFireList.size()];
                    for (int d = 0; d < tflFireList.size(); d++) {
                        String s = tflFireList.get(d);
                        tflFireDefectList[d] = s;
                    }
                    setTag(tflFireDefectList, tflFire);
                } else {
                    tflFire.setVisibility(View.GONE);
                    tvFire.setVisibility(View.VISIBLE);
                    tvFire.setText("火烧迹象未选择缺陷");
                }
                //泡水迹象
                if (tflWaterList != null && tflWaterList.size() > 0) {
                    llWaterBg.setBackgroundColor(Color.WHITE);//背景置为白色
                    tflwater.setVisibility(View.VISIBLE);
                    tflWaterDefectList = new String[tflWaterList.size()];
                    for (int d = 0; d < tflWaterList.size(); d++) {
                        String s = tflWaterList.get(d);
                        tflWaterDefectList[d] = s;
                    }
                    setTag(tflWaterDefectList, tflwater);
                } else {
                    tflwater.setVisibility(View.GONE);
                    tvWater.setVisibility(View.VISIBLE);
                    tvWater.setText("泡水迹象未选择缺陷");
                }
            }
        }
        if (submitModel != null) {

            //市场保有量
            int marketOwnership = submitModel.getMarketOwnership();
            switch (marketOwnership) {
                case 1:
                    tvMarketOwnership.setText("较高");
                    break;
                case 2:
                    tvMarketOwnership.setText("一般");
                    break;
                case 3:
                    tvMarketOwnership.setText("较低");
                    break;
                default:
            }
            //市场认可度
            int marketAcceptance = submitModel.getMarketAcceptance();
            switch (marketAcceptance) {
                case 1:
                    tvMarketAcceptance.setText("较高");
                    break;
                case 2:
                    tvMarketAcceptance.setText("一般");
                    break;
                case 3:
                    tvMarketAcceptance.setText("较低");
                    break;
                default:
            }
            //市场保值率
            int marketHedgeRatio = submitModel.getMarketHedgeRatio();
            switch (marketHedgeRatio) {
                case 1:
                    tvMarketHedgeRatio.setText("较高");
                    break;
                case 2:
                    tvMarketHedgeRatio.setText("一般");
                    break;
                case 3:
                    tvMarketHedgeRatio.setText("较低");
                    break;
                default:
            }
            //其他信息
            int gaugeLamp = submitModel.getGaugeLamp();
            switch (gaugeLamp){
                case 0:
                    break;
                case 1:
                    tvCertificateEx += "仪表故障灯常亮;";
                    break;
                default:

            }
            String certificateEx = submitModel.getCertificateEx();
//            String tvCertificateEx = "";
            if (!TextUtils.isEmpty(certificateEx)) {
                String[] certificateExArr = certificateEx.split(",");
                for (int i = 0; i < certificateExArr.length; i++) {
                    String s = certificateExArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        switch (index) {
                            case 1:
                                tvCertificateEx = tvCertificateEx + "正在抵押;";
                                break;
                            case 2:
                                tvCertificateEx = tvCertificateEx + "发动机号变更;";
                                break;
                            case 3:
                                tvCertificateEx = tvCertificateEx + "重打车架号;";
                                break;
                            case 4:
                                tvCertificateEx = tvCertificateEx + "登记证补领;";
                                break;
                            case 5:
                                tvCertificateEx = tvCertificateEx + "颜色变更;";
                                break;
                            default:
                        }
                    }
                }
            }
            //登记证附加信息其他说明
            String certificateExDes = submitModel.getCertificateExDes();
            if (!TextUtils.isEmpty(certificateExDes)) {
                tvCertificateEx = tvCertificateEx + certificateExDes + ";";
            }
            //其他信息
            String assessmentDes = submitModel.getAssessmentDes();
            if (!TextUtils.isEmpty(assessmentDes)) {
                tvCertificateEx = tvCertificateEx + assessmentDes;
            }
            if (gaugeLamp == 0 && TextUtils.isEmpty(certificateEx) && TextUtils.isEmpty(certificateExDes) && TextUtils.isEmpty(assessmentDes)) {
                tvOtherInfo.setVisibility(View.GONE);
                view_line_otherInfo.setVisibility(View.VISIBLE);
            } else {
                tvOtherInfo.setText(tvCertificateEx);
            }
            //车身铭牌--1 破损、 2 实车铭牌未见、 3 铭牌出厂日期与登记证 yyyy 年 MM 月 dd 日不一致）
            String nameplateProperty = submitModel.getNameplateProperty();
            if (!TextUtils.isEmpty(nameplateProperty)) {
                llNameplateProperty.setVisibility(View.VISIBLE);
                String[] nameArr = nameplateProperty.split(",");
                for (int i = 0; i < nameArr.length; i++) {
                    String s = nameArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        switch (index) {
                            case 1:
                                tvNameplateProperty.append("破损;");
                                break;
                            case 2:
                                tvNameplateProperty.append("实车铭牌未见;");
                                break;
                            case 3:
                                String productionTime = submitModel.getProductionTime();
                                tvNameplateProperty.append("铭牌出厂日期与登记证" + productionTime + "不一致;");
                                break;
                            default:
                        }
                    }
                }
            } else {
                llNameplateProperty.setVisibility(View.GONE);
            }
            //车架号VIN--1实车车架号未见、2可以看清无法拓印、3锈蚀、4与登记证不一致、5打磨修复痕迹、6打刻较浅、7异物遮挡无法拍摄
            String vinProperty = submitModel.getVinProperty();
            if (!TextUtils.isEmpty(vinProperty)) {
                llVin.setVisibility(View.VISIBLE);
                String[] vinArr = vinProperty.split(",");
                for (int i = 0; i < vinArr.length; i++) {
                    String s = vinArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        switch (index) {
                            case 1:
                                tvVinOtherInfoChecked.append("实车车架号未见;");
                                break;
                            case 2:
                                tvVinOtherInfoChecked.append("可以看清无法拓印;");
                                break;
                            case 3:
                                tvVinOtherInfoChecked.append("锈蚀;");
                                break;
                            case 4:
                                tvVinOtherInfoChecked.append("与登记证不一致;");
                                break;
                            case 5:
                                tvVinOtherInfoChecked.append("打磨修复痕迹;");
                                break;
                            case 6:
                                tvVinOtherInfoChecked.append("打刻较浅;");
                                break;
                            case 7:
                                tvVinOtherInfoChecked.append("异物遮挡无法拍摄;");
                                break;
                            default:
                        }
                    }
                }
            } else {
                llVin.setVisibility(View.GONE);
            }
            //轮胎规格--1实车与登记证不一致，2同轴花纹不一致
            String tyreProperty = submitModel.getTyreProperty();
            if (!TextUtils.isEmpty(tyreProperty)) {
                llTyre.setVisibility(View.VISIBLE);
                String[] tyreArr = tyreProperty.split(",");
                for (int i = 0; i < tyreArr.length; i++) {
                    String s = tyreArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        switch (index) {
                            case 1:
                                tvTyre.append("实车与登记证不一致;");
                                break;
                            case 2:
                                tvTyre.append("同轴花纹不一致;");
                                break;
                            default:
                        }
                    }
                }
            } else {
                llTyre.setVisibility(View.GONE);
            }
            //行驶证车辆照片--1颜色不一致（贴纸）、2天窗不一致、3轮毂不一致、4车身覆盖件不一致、5前照灯总成不一致、6中网不一致
            String drivingLicenseCheckEx = submitModel.getDrivingLicenseCheckEx();
            if (!TextUtils.isEmpty(drivingLicenseCheckEx)) {
                llDrivingLicenseCheckEx.setVisibility(View.VISIBLE);
                String[] drivingLicenseArr = drivingLicenseCheckEx.split(",");
                for (int i = 0; i < drivingLicenseArr.length; i++) {
                    String s = drivingLicenseArr[i];
                    if (!TextUtils.isEmpty(s)) {
                        int index = Integer.parseInt(s);
                        switch (index) {
                            case 1:
                                tvDrivingLicenseCheckEx.append("颜色不一致(贴纸);");
                                break;
                            case 2:
                                tvDrivingLicenseCheckEx.append("天窗不一致;");
                                break;
                            case 3:
                                tvDrivingLicenseCheckEx.append("轮毂不一致;");
                                break;
                            case 4:
                                tvDrivingLicenseCheckEx.append("车身覆盖件不一致;");
                                break;
                            case 5:
                                tvDrivingLicenseCheckEx.append("前照灯总成不一致;");
                                break;
                            case 6:
                                tvDrivingLicenseCheckEx.append("中网不一致;");
                                break;
                            default:
                        }
                    }
                }
            } else {
                llDrivingLicenseCheckEx.setVisibility(View.GONE);
            }
            //车商收车价
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            float assessmentPrace = submitModel.getAssessmentPrace();
            if (String.valueOf(assessmentPrace).startsWith("0")) {
                String priceBuy = decimalFormat.format(assessmentPrace);
                tvAssessmentPrace.setText(0 + priceBuy);
            } else {
                String priceBuy = decimalFormat.format(assessmentPrace);
                tvAssessmentPrace.setText(priceBuy);
            }
            //车商售车价
            float salePrice = submitModel.getSalePrice();
            if (String.valueOf(salePrice).startsWith("0")) {
                String priceSale = decimalFormat.format(salePrice);
                tvSalePrice.setText(0 + priceSale);
            } else {
                String priceSale = decimalFormat.format(salePrice);
                tvSalePrice.setText(priceSale);
            }
            ArrayList<String> list = new ArrayList<>();
            DetectionWrapper detectionWrapper = detectMainActivity.getWrapper();
            List<String> configList = detectionWrapper.getProcedureList();
            for (int i = 0; i < configList.size(); i++) {
                switch (configList.get(i)) {
                    case "81":
                        configDriving = true;
                        break;
                    case "82":
                        configRegister = true;
                        break;
                    case "83":
                        configOwener = true;
                        break;
                    case "84":
                        configCardType = true;
                        break;
                    case "85":
                        configCardId = true;
                        break;
                    case "86":
                        configRegisterSame = true;
                        break;
                    case "87":
                        configRegisterDate = true;
                        break;
                    case "88":
                        configRegion = true;
                        break;
                    case "89":
                        configPlateNum = true;
                        break;
                    case "90":
                        configCarType = true;
                        break;
                    case "91":
                        configVin = true;
                        break;
                    case "92":
                        configEngineId = true;
                        break;
                    case "93":
                        configFuelType = true;
                        break;
                    case "94":
                        configLoadNum = true;
                        break;
                    case "95":
                        configObtainWay = true;
                        break;
                    case "96":
                        configCarColor = true;
                        break;
                    case "97":
                        configIsMadeInChina = true;
                        break;
                    case "98":
                        configDisplacement = true;
                        break;
                    case "99":
                        configTireType = true;
                        break;
                    case "100":
                        configUseNature = true;
                        break;
                    case "101":
                        configProductDate = true;
                        break;
                    case "102":
                        configTransferNum = true;
                        break;
                    case "103":
                        configLastTransferDate = true;
                        break;
                    case "104":
                        configOldUser = true;
                        break;
                    case "105":
                        configCurUser = true;
                        break;
                    case "106":
                        configInsuranceBill = true;
                        break;
                    case "107":
                        configInsuranceExpDate = true;
                        break;
                    case "108":
                        configInsuranceAddress = true;
                        break;
                    case "109":
                        configCarBill = true;
                        break;
                    case "110":
                        configOtherBill = true;
                        break;
                    case "111":
                        configRegisterAdditionInfo = true;
                        break;
                    case "112":
                        configAnnualDate = true;
                        break;
                    case "276":
                        configPlateRegion = true;
                        break;
                    case "277":
                        configExtraKey = true;
                        break;
                    case "297":
                        configEmissionStandard = true;
                        break;
                }
            }
            //车辆款型
            taskId = detectMainActivity.getTaskid();
            //查询此条缓存是否存在
            boolean exist = DBManager.getInstance().isExist(Constants.DATA_TYPE_CAR_TYPE, taskId, PadSysApp.getUser().getUserId());
            DBManager.getInstance().closeDB();
            if (exist) {//缓存存在
                String json = "";
                //根据taskId、userId、Constants.DATA_CAR从本地数据库中获取缓存数据
                List<DBBase> queryList = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_CAR_TYPE, PadSysApp.getUser().getUserId());
                if (queryList != null) {
                    int size = queryList.size();
                    for (int i = 0; i < size; i++) {
                        if (queryList.get(i).getDataType().equals(Constants.DATA_TYPE_CAR_TYPE)) {
                            json = queryList.get(i).getJson();
                            if (!TextUtils.isEmpty(json)) {
                                LocalCarConfigModel localCarConfigModel = new Gson().fromJson(json, LocalCarConfigModel.class);
                                //车辆款型
                                String styleFullName = localCarConfigModel.getStyleFullName();
                                if (!TextUtils.isEmpty(styleFullName)){
                                    list.add("车辆款型:" + styleFullName);
                                }else {
                                    list.add("车辆款型:" + " -");
                                }
                                //厂商指导价
                                String nowMsrp = localCarConfigModel.getNowMsrp();
                                if (!TextUtils.isEmpty(nowMsrp)) {
                                        list.add("厂商指导价:" + nowMsrp + "万元");
                                } else {
                                        list.add("厂商指导价:" + " -");
                                }
//                                String fullName1 = localCarConfigModel.getFullName();
//                                if (!TextUtils.isEmpty(fullName1)) {
//                                    String fullName = fullName1.trim();
//                                    //车辆款型
//                                    char[] c = fullName.toCharArray();
//                                    int tp = 0;//记录汉字
//                                    boolean flag = false;
//                                    int b = 0;
//                                    for (int index = c.length - 1; index >= 0; index--) {
//                                        if ((int) c[index] > 10000) {//汉字
//                                            tp += 2;
//                                            if (tp == 4) {
//                                                tp = 0;
//                                                flag = true;
//                                            }
//                                            if (flag) {
//                                                if (tp == 2) {
//                                                    b = index + 1;
//                                                    index = -1;//停止循环
//                                                }
//                                            }
//
//                                        } else {//字母
//                                            if (c[index] >= 'a' && c[index] <= 'z' || c[index] >= 'A' && c[index] <= 'Z') {//字母
//                                                b = index + 1;
//                                                index = -1;//停止循环
//                                            }
//                                        }
//                                    }
//                                    styleName = fullName.substring(0, b);
//                                    if (!TextUtils.isEmpty(styleName)) {
//                                        list.add("车辆款型:" + styleName);
//                                    } else {
//                                        list.add("车辆款型:" + " -");
//                                    }
//                                    //厂商指导价
//                                    int tp1 = 0;//记录汉字
//                                    boolean flag1 = false;
//                                    int a = 0;
//                                    int b1 = 0;
//                                    for (int index = c.length - 1; index >= 0; index--) {
//                                        if ((int) c[index] > 10000) {//汉字
//                                            tp1 += 2;
//                                            if (tp1 == 4) {
//                                                a = index;
//                                                tp1 = 0;
//                                                flag1 = true;
//                                            }
//                                            if (flag1) {
//                                                if (tp1 == 2) {
//                                                    b1 = index + 1;
//                                                    index = -1;//停止循环
//                                                }
//                                            }
//                                        } else {//字母
//                                            if (c[index] >= 'a' && c[index] <= 'z' || c[index] >= 'A' && c[index] <= 'Z') {//字母
//                                                b1 = index + 1;
//                                                index = -1;//停止循环
//                                            }
//                                        }
//                                    }
//                                    nowMsrp = fullName.substring(b1, a);
//                                    if (!TextUtils.isEmpty(nowMsrp)) {
//                                        list.add("厂商指导价:" + nowMsrp + "万元");
//                                    } else {
//                                        list.add("厂商指导价:" + " -");
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
            //表显里程
            String mileage = submitModel.getMileage() + "";
            //公里数与实车不符 0-不符 1-相符
            int mileageSame = submitModel.getMileageSame();
            if (!TextUtils.isEmpty(mileage)) {
                switch (mileageSame) {
                    case 0:
                        list.add("表显里程:" + mileage + "公里" + "(公里数与实车不符)");
                        break;
                    case 1:
                        list.add("表显里程:" + mileage + "公里");
                        break;
                    default:
                }
            } else {
                list.add("表显里程:" + " -");
            }
            //实车颜色--1白、2灰、3红、4蓝、5绿、6紫、7粉、8黄、9黑、10棕、11橙、12银、13金、14青、15多彩、16双色、99其他
            int carColorActual = submitModel.getCarColorActual();
            switch (carColorActual) {
                case 1:
                    list.add("实车颜色:白");
                    break;
                case 2:
                    list.add("实车颜色:灰");
                    break;
                case 3:
                    list.add("实车颜色:红");
                    break;
                case 4:
                    list.add("实车颜色:蓝");
                    break;
                case 5:
                    list.add("实车颜色:绿");
                    break;
                case 6:
                    list.add("实车颜色:紫");
                    break;
                case 7:
                    list.add("实车颜色:粉");
                    break;
                case 8:
                    list.add("实车颜色:黄");
                    break;
                case 9:
                    list.add("实车颜色:黑");
                    break;
                case 10:
                    list.add("实车颜色:棕");
                    break;
                case 11:
                    list.add("实车颜色:橙");
                    break;
                case 12:
                    list.add("实车颜色:银");
                    break;
                case 13:
                    list.add("实车颜色:金");
                    break;
                case 14:
                    list.add("实车颜色:青");
                    break;
                case 15:
                    list.add("实车颜色:多彩");
                    break;
                case 16:
                    list.add("实车颜色:双色");
                    break;
                case 99:
                    String otherColor = submitModel.getOtherColor();
                    list.add("实车颜色:" + otherColor);
                    break;
                default:
                    list.add("实车颜色:" + " -");
            }
            //机动车所有人
            if (configOwener) {
                String likeMan = submitModel.getCarOwner();
                if (!TextUtils.isEmpty(likeMan)) {
                    list.add("机动车所有人:" + likeMan);
                } else {
                    list.add("机动车所有人:" + " -");
                }
            }
            if (configCardType) {
                //证件类型--0默认，1身份证，2军官证，3护照，4组织机构代码证，5车主证件未见
                int cardType = submitModel.getCardType();
                switch (cardType) {
                    case 0:
                        list.add("证件类型:" + " -");
                        break;
                    case 1:
                        list.add("证件类型:身份证");
                        break;
                    case 2:
                        list.add("证件类型:军官证");
                        break;
                    case 3:
                        list.add("证件类型:护照");
                        break;
                    case 4:
                        list.add("证件类型:组织机构代码证");
                        break;
                    case 5:
                        list.add("证件类型:车主证件未见");
                        break;
                }
            }

            //车主证件号
            if (configCardId) {
                String cardNum = submitModel.getCardNum();
                if (!TextUtils.isEmpty(cardNum)) {
                    if (configRegisterSame){
                        int isCardSame = submitModel.getIsCardSame();
                        switch (isCardSame) {
                            case 0:
                                list.add("车主证件号:" + cardNum + "(与车主证件不一致)");
                                break;
                            case 1:
                                list.add("车主证件号:" + cardNum);
                                break;
                            default:
                                list.add("车主证件号:" + cardNum);
                        }
                    }else {
                        list.add("车主证件号:" + cardNum);
                    }
                } else {
                    list.add("车主证件号:" + " -");
                }
            }
            //登记日期
            if (configRegisterDate) {
                String recordDate = submitModel.getRecordDate();
                if (!TextUtils.isEmpty(recordDate)) {
                    list.add("登记日期:" + recordDate);
                } else {
                    list.add("登记日期:" + " -");
                }
            }
            //初登地区
            if (configRegion) {
                String provName = submitModel.getProvName();
                String cityName = submitModel.getCityName();

                if (!TextUtils.isEmpty(provName) && !TextUtils.isEmpty(cityName)) {
                    if (provName.equals(cityName)) {
                        list.add("初登地区:" + provName);
                    } else {
                        list.add("初登地区:" + provName + cityName);
                    }
                }
            }
            //上牌地区
            if (configPlateRegion) {
                String onCardProvName = submitModel.getOnCardProvName();
                String onCardCityName = submitModel.getOnCardCityName();
                if (!TextUtils.isEmpty(onCardProvName) && !TextUtils.isEmpty(onCardCityName)) {
                    if (onCardProvName.equals(onCardCityName)) {
                        list.add("上牌地区:" + onCardProvName);
                    } else {
                        list.add("上牌地区:" + onCardProvName + onCardCityName);
                    }
                } else {
                    list.add("上牌地区:" + " -");
                }
            }
            //车牌号码
            if (configPlateNum) {
                int carLicenseEx = submitModel.getCarLicenseEx();//车牌有无--0未落户、1有车牌、2未悬挂、3与实车不符
                switch (carLicenseEx) {
                    case 0:
                        list.add("车牌号码:未落户");
                        break;
                    case 1:
                        String carLicense = submitModel.getCarLicense();
                        if (!TextUtils.isEmpty(carLicense)) {
                            list.add("车牌号码:" + carLicense);
                        } else {
                            list.add("车牌号码:" + " -");
                        }
                        break;
                    case 2:
                        String carLicense1 = submitModel.getCarLicense();
                        if (!TextUtils.isEmpty(carLicense1)) {
                            if (carLicense1.length() > 1){
                                list.add("车牌号码:" + carLicense1 + "(未悬挂)");
                            }else {
                                list.add("车牌号码:未悬挂");
                            }
                        } else {
                            list.add("车牌号码:" + " -");
                        }
                        break;
                    case 3:
                        String carLicense2 = submitModel.getCarLicense();
                        if (!TextUtils.isEmpty(carLicense2)) {
                            list.add("车牌号码:" + carLicense2 + "(与实车不符)");
                        } else {
                            list.add("车牌号码:" + " -");
                        }
                        break;
                    default:
                        list.add("车牌号码:" + " -");
                }
            }
            //品牌型号
            if (configCarType) {
                String recordBrand = submitModel.getRecordBrand();
                if (!TextUtils.isEmpty(recordBrand)) {
                    list.add("品牌型号:" + recordBrand);
                } else {
                    list.add("品牌型号:" + " -");
                }
            }
            //车架号VIN
            if (configVin) {
                String vin = submitModel.getVin();
                if (!TextUtils.isEmpty(vin)) {
                    list.add("车架号VIN:" + vin);
                } else {
                    list.add("车架号VIN:" + " -");
                }
            }
            //发动机号
            if (configEngineId) {
                String engineNum = submitModel.getEngineNum();
                if (!TextUtils.isEmpty(engineNum)) {
                    list.add("发动机号:" + engineNum);
                } else {
                    list.add("发动机号:" + " -");
                }
            }
            //燃料种类--1汽油、2柴油、3混动、4天然气、5纯电动
            if (configFuelType) {
                int fuelType = submitModel.getFuelType();
                switch (fuelType) {
                    case 1:
                        list.add("燃料种类:汽油");
                        break;
                    case 2:
                        list.add("燃料种类:柴油");
                        break;
                    case 3:
                        list.add("燃料种类:混动");
                        break;
                    case 4:
                        list.add("燃料种类:天然气");
                        break;
                    case 5:
                        list.add("燃料种类:纯电动");
                        break;
                    default:
                        list.add("燃料种类:" + " -");
                }
            }
            //核定载客数--2、4、5、6、7、8、9、9以上
            if (configLoadNum) {
                int seating = submitModel.getSeating();
                switch (seating) {
                    case 2:
                        list.add("核定载客数:2");
                        break;
                    case 4:
                        list.add("核定载客数:4");
                        break;
                    case 5:
                        list.add("核定载客数:5");
                        break;
                    case 6:
                        list.add("核定载客数:6");
                        break;
                    case 7:
                        list.add("核定载客数:7");
                        break;
                    case 8:
                        list.add("核定载客数:8");
                        break;
                    case 9:
                        list.add("核定载客数:9");
                        break;
                    case 99:
                        list.add("核定载客数:9以上");
                        break;
                    default:
                        list.add("核定载客数:" + " -");
                }
            }
            //获得方式--1购买、2仲裁裁判、3继承、4赠与、5协议抵偿债务、6中奖、7资产重组、8资产整体买卖、9调拨、10境外自带、11法院调解/裁定/判决
            if (configObtainWay) {
                int carGetWay = submitModel.getCarGetWay();
                switch (carGetWay) {
                    case 1:
                        list.add("获得方式:购买");
                        break;
                    case 2:
                        list.add("获得方式:仲裁裁判");
                        break;
                    case 3:
                        list.add("获得方式:继承");
                        break;
                    case 4:
                        list.add("获得方式:赠与");
                        break;
                    case 5:
                        list.add("获得方式:协议抵偿债务");
                        break;
                    case 6:
                        list.add("获得方式:中奖");
                        break;
                    case 7:
                        list.add("获得方式:资产重组");
                        break;
                    case 8:
                        list.add("获得方式:资产整体买卖");
                        break;
                    case 9:
                        list.add("获得方式:调拨");
                        break;
                    case 10:
                        list.add("获得方式:境外自带");
                        break;
                    case 11:
                        list.add("获得方式:法院调解/裁定/判决");
                        break;
                    default:
                        list.add("获得方式:" + " -");
                }
            }
            //登记证车身颜色--1白、2灰、3红、4粉、5黄、6蓝、7绿、8紫、9棕、10黑、99其它
            if (configCarColor) {
                int color = submitModel.getColor();
                switch (color) {
                    case 1:
                        list.add("登记证车身颜色:白");
                        break;
                    case 2:
                        list.add("登记证车身颜色:灰");
                        break;
                    case 3:
                        list.add("登记证车身颜色:红");
                        break;
                    case 4:
                        list.add("登记证车身颜色:粉");
                        break;
                    case 5:
                        list.add("登记证车身颜色:黄");
                        break;
                    case 6:
                        list.add("登记证车身颜色:蓝");
                        break;
                    case 7:
                        list.add("登记证车身颜色:绿");
                        break;
                    case 8:
                        list.add("登记证车身颜色:紫");
                        break;
                    case 9:
                        list.add("登记证车身颜色:棕");
                        break;
                    case 10:
                        list.add("登记证车身颜色:黑");
                        break;
                    case 11:
                        list.add("登记证车身颜色:双色");
                        break;
                    case 99:
                        String carColorDes = submitModel.getCarColorDes();
                        list.add("登记证车身颜色:" + carColorDes);
                        break;
                    default:
                        list.add("登记证车身颜色:" + " -");
                }
            }
            //轮胎规格
            if (configTireType) {
                String tyre = submitModel.getTyre();
                if (!TextUtils.isEmpty(tyre)) {
                    list.add("轮胎规格:" + tyre);
                } else {
                    list.add("轮胎规格:" + " -");
                }
            }
            //使用性质--1 营运、 2 非营运、 3 营转非、 4 出租营转非、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运
            if (configUseNature) {
                int service = submitModel.getService();
                switch (service) {
                    case 1:
                        list.add("使用性质:营运");
                        break;
                    case 2:
                        list.add("使用性质:非营运");
                        break;
                    case 3:
                        list.add("使用性质:营转非");
                        break;
                    case 4:
                        list.add("使用性质:出租营转非");
                        break;
                    case 5:
                        list.add("使用性质:租赁");
                        break;
                    case 6:
                        list.add("使用性质:警用");
                        break;
                    case 7:
                        list.add("使用性质:消防");
                        break;
                    case 8:
                        list.add("使用性质:救护");
                        break;
                    case 9:
                        list.add("使用性质:工程抢险");
                        break;
                    case 10:
                        list.add("使用性质:货运");
                        break;
                    case 11:
                        list.add("使用性质:公路客运");
                        break;
                    case 12:
                        list.add("使用性质:公交客运");
                        break;
                    case 13:
                        list.add("使用性质:出租客运");
                        break;
                    case 14:
                        list.add("使用性质:旅游客运");
                        break;
                    default:
                        list.add("使用性质:" + " -");
                }
            }
            //出厂日期
            if (configProductDate) {
                String productionTime = submitModel.getProductionTime();
                if (!TextUtils.isEmpty(productionTime)) {
                    list.add("出厂日期:" + productionTime);
                } else {
                    list.add("出厂日期:" + " -");
                }
            }
            //过户次数
            if (configTransferNum) {
                String transferCount = submitModel.getTransferCount() + "";
                //如果登记证未见，则显示-          郑有权 4.9
                if(detectMainActivity.isShowRegistration()){
                    list.add("过户次数:" + " -");
                }else{
                    if (!TextUtils.isEmpty(transferCount)) {
                        list.add("过户次数:" + transferCount);
                    } else {
                        list.add("过户次数:" + " -");
                    }
                }

            }
            //最后过户日期
            if (configLastTransferDate) {
                String lastTransferDate = submitModel.getLastTransferDate();
                if (!TextUtils.isEmpty(lastTransferDate)) {
                    list.add("最后过户日期:" + lastTransferDate);
                } else {
                    list.add("最后过户日期:" + " -");
                }
            }
            //曾使用方--1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录
            if (configOldUser){
                int oldUseOwner = submitModel.getOldUseOwner();
                switch (oldUseOwner) {
                    case 1:
                        list.add("曾使用方:仅个人记录");
                        break;
                    case 2:
                        list.add("曾使用方:有单位记录");
                        break;
                    case 3:
                        list.add("曾使用方:有出租车记录");
                        break;
                    case 4:
                        list.add("曾使用方:有汽车租赁公司记录");
                        break;
                    case 5:
                        list.add("曾使用方:有汽车销售(服务)公司记录");
                        break;
                    default:
                        list.add("曾使用方:" + " -");
                }
            }
            //现使用方--1仅个人记录、2有单位记录、3有出租车记录、4有汽车租赁公司记录、5有汽车销售（服务）公司记录
            if (configCurUser) {
                int nowUseOwner = submitModel.getNowUseOwner();
                switch (nowUseOwner) {
                    case 1:
                        list.add("现使用方:仅个人记录");
                        break;
                    case 2:
                        list.add("现使用方:有单位记录");
                        break;
                    case 3:
                        list.add("现使用方:有出租车记录");
                        break;
                    case 4:
                        list.add("现使用方:有汽车租赁公司记录");
                        break;
                    case 5:
                        list.add("现使用方:有汽车销售(服务)公司记录");
                        break;
                    default:
                        list.add("现使用方:" + " -");
                }
            }
            //备用钥匙   (（0--0；1--1；2--2及以上；-1--空） ) ，单选
            if (configExtraKey){
                int spareKey = submitModel.getSpareKey();
                switch (spareKey){
                    case 0:
                        list.add("钥匙:0");
                        break;
                    case 1:
                        list.add("钥匙:1");
                        break;
                    case 2:
                        list.add("钥匙:2及以上");
                        break;
                    default:
                        list.add("钥匙:" + " -");
                }
            }

            //排放标准 (国二及以下：1，国三：2，国四：3，国五：4，无法判断：5 )，单选
            if (configEmissionStandard){
                int effluentStd = submitModel.getEffluentStd();
                switch (effluentStd){
                    case 1:
                        list.add("排放标准:国二及以下");
                        break;
                    case 2:
                        list.add("排放标准:国三");
                        break;
                    case 3:
                        list.add("排放标准:国四");
                        break;
                    case 4:
                        list.add("排放标准:国五");
                        break;
                    case 5:
                        list.add("排放标准:无法判断");
                        break;
                    default:
                        list.add("排放标准:" + " -");
                }
            }
            //交强险保单--1正常、2未见、3被保险人与车主不一致
            if (configInsuranceBill) {
                int trafficInsuranceHave = submitModel.getTrafficInsuranceHave();
                switch (trafficInsuranceHave) {
                    case 1:
                        list.add("交强险保单:正常");
                        break;
                    case 2:
                        list.add("交强险保单:未见");
                        break;
                    case 3:
                        list.add("交强险保单:被保险人与车主不一致");
                        break;
                    default:
                        list.add("交强险保单:" + " -");
                }
            }
            //交强险到期日
            if (configInsuranceExpDate) {
                String insurance = submitModel.getInsurance();
                if (!TextUtils.isEmpty(insurance)) {
                    list.add("交强险到期日:" + insurance);
                } else {
                    list.add("交强险到期日:" + " -");
                }
            }
            //交强险所在地
            if (configInsuranceAddress){
                String insuranceProvName = submitModel.getInsuranceProvName();
                String insuranceCityName = submitModel.getInsuranceCityName();
                if (!TextUtils.isEmpty(insuranceProvName) && !TextUtils.isEmpty(insuranceCityName)){
                    if (insuranceProvName.equals(insuranceCityName)){
                        list.add("交强险所在地:" + insuranceCityName);
                    }else {
                        list.add("交强险所在地:" + insuranceProvName + insuranceCityName);
                    }
                }else {
                    list.add("交强险所在地:" + " -");
                }
            }
            //原车发票
            if (configCarBill) {
                int carInvoiceHave = submitModel.getCarInvoiceHave();//1无工商章、2未见，3有
                switch (carInvoiceHave) {
                    case 1://无工商章
                        String carInvoiceMoney = submitModel.getCarInvoiceMoney() + "";//原车发票金额
                        String carInvoiceDate = submitModel.getCarInvoiceDate();//开票日期
                        if (!TextUtils.isEmpty(carInvoiceMoney)) {
                            list.add("原车发票金额:" + carInvoiceMoney + "元" + "(无工商章)");
                        } else {
                            list.add("原车发票金额:" + " -");
                        }
                        if (!TextUtils.isEmpty(carInvoiceDate)) {
                            list.add("原车发票开票日期:" + carInvoiceDate);
                        } else {
                            list.add("原车发票开票日期:" + " -");
                        }
                        break;
                    case 2://未见
                        list.add("原车发票:未见");
                        break;
                    case 3://有
                        String carInvoiceMoney2 = submitModel.getCarInvoiceMoney() + "";//原车发票金额
                        String carInvoiceDate2 = submitModel.getCarInvoiceDate();//开票日期
                        if (!TextUtils.isEmpty(carInvoiceMoney2)) {
                            list.add("原车发票金额:" + carInvoiceMoney2 + "元");
                        } else {
                            list.add("原车发票金额:" + " -");
                        }
                        if (!TextUtils.isEmpty(carInvoiceDate2)) {
                            list.add("原车发票开票日期:" + carInvoiceDate2);
                        } else {
                            list.add("原车发票开票日期:" + " -");
                        }
                        break;
                    default:
                        list.add("原车发票金额:" + " -");
                        list.add("原车发票开票日期:" + " -");
                }
            }
            //其他票证--1过户票、2备用钥匙、3进口关单、4购置税完税证明（征税）、5购置税完税证明（免税）
            if (configOtherBill) {
                String carInvoiceOther = submitModel.getCarInvoiceOther();
                String tvCarInvoiceOther = "";
                if (!TextUtils.isEmpty(carInvoiceOther)) {
                    String[] carInvoiceOtherArr = carInvoiceOther.split(",");
                    for (int i = 0; i < carInvoiceOtherArr.length; i++) {
                        String s = carInvoiceOtherArr[i];
                        if (!TextUtils.isEmpty(s)) {
                            int index = Integer.parseInt(s);
                            switch (index) {
                                case 1:
                                    tvCarInvoiceOther = tvCarInvoiceOther + "过户票;";
                                    break;
                                case 2:
                                    tvCarInvoiceOther = tvCarInvoiceOther + "钥匙;";
                                    break;
                                case 3:
                                    tvCarInvoiceOther = tvCarInvoiceOther + "进口关单;";
                                    break;
                                case 4:
                                    tvCarInvoiceOther = tvCarInvoiceOther + "购置税完税证明(征税);";
                                    break;
                                case 5:
                                    tvCarInvoiceOther = tvCarInvoiceOther + "购置税完税证明(免税);";
                                    break;
                                default:
                            }
                        }
                    }
                    list.add("其他票证:" + tvCarInvoiceOther.toString());
                } else {
                    list.add("其他票证:" + " -");
                }
            }
            //登记证附加信息--1正在抵押、2发动机号变更、3重打车架号、4登记证补领、5颜色变更
//            if (configRegisterAdditionInfo) {
//                String certificateEx = submitModel.getCertificateEx();
//                String tvCertificateEx = "";
//                if (!TextUtils.isEmpty(certificateEx)) {
//                    String[] certificateExArr = certificateEx.split(",");
//                    for (int i = 0; i < certificateExArr.length; i++) {
//                        String s = certificateExArr[i];
//                        if (!TextUtils.isEmpty(s)) {
//                            int index = Integer.parseInt(s);
//                            switch (index) {
//                                case 1:
//                                    tvCertificateEx = tvCertificateEx + "正在抵押;";
//                                    break;
//                                case 2:
//                                    tvCertificateEx = tvCertificateEx + "发动机号变更;";
//                                    break;
//                                case 3:
//                                    tvCertificateEx = tvCertificateEx + "重打车架号;";
//                                    break;
//                                case 4:
//                                    tvCertificateEx = tvCertificateEx + "登记证补领;";
//                                    break;
//                                case 5:
//                                    tvCertificateEx = tvCertificateEx + "颜色变更;";
//                                    break;
//                                default:
//                            }
//                        }
//                    }
//                    list.add("登记证附加信息:" + tvCertificateEx);
//                } else {
//                    list.add("登记证附加信息:" + " -");
//                }
//
//                //登记证附加信息其他说明
//                String certificateExDes = submitModel.getCertificateExDes();
//                if (!TextUtils.isEmpty(certificateExDes)) {
//                    list.add("登记证附加信息其他说明:" + certificateExDes);
//                } else {
//                    list.add("登记证附加信息其他说明:" + " -");
//                }
//            }
            //年检有效期
            if (configAnnualDate) {
                if (submitModel.isInspectionIsConfirmLocal() == true) {//选中无法判断
                    list.add("年检有效期:无法判断");
                } else {
                    String inspection = submitModel.getInspection();
                    if (!TextUtils.isEmpty(inspection)) {
                        list.add("年检有效期:" + inspection);
                    } else {
                        list.add("年检有效期:" + " -");
                    }
                }
            }

            //查看配置
            //判断是否要显示查看配置

                LinkedHashMap<String, String> configureSelectedMap = submitModel.getConfigureSelectedMap();
                //MyToast.showShort("configureSelectedMap size: "+configureSelectedMap.size());
                Iterator iter = configureSelectedMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    list.add(key + ":" + val);
                }
//
//            if(wrapper.getConfigureStatus()==1) {
//                LinkedHashMap<String, String> configureSelectedMap = submitModel.getConfigureSelectedMap();
//                Iterator<Map.Entry<String, String>> entries = configureSelectedMap.entrySet().iterator();
//                while (entries.hasNext()) {
//                    Map.Entry<String, String> entry = entries.next();
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    list.add(key + ":" + value);
//                }
//            }

            ResultCheckedAdapter adapter = new ResultCheckedAdapter(this, list);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
            recyclerView.setAdapter(adapter);
            //--------------------------------测试用----------------------------------
//            TopWarningPresenter topWarningPresenter = new TopWarningPresenter(this);
//            topWarningPresenter.getTopWraningValue(submitModel.getId()+"",submitModel.getStyleID()+"",submitModel.getRecordDate(),submitModel.getMileage()+"",submitModel.getRegisterCityID()+"",submitModel.getSalePrice()+"");
        }
    }

    @OnClick({R.id.iv_back, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();//关闭当前页面
                break;
            case R.id.btnSubmit://提交审核
                if (PadSysApp.networkAvailable) {
                    if (PreventDoubleClickUtil.noDoubleClick()) {
                        //收车价
                        String assessmentPrace = submitModel.getAssessmentPrace() + "";
                        //售车价
                        String salePrice = submitModel.getSalePrice() + "";
                        if (assessmentPrace.equals("0.01")||salePrice.equals("0.02")){//大事故车
                            detectMainActivity.checkAndSubmit(ResultCheckedActivity.this,2,"0");
                        }else {
                        detectMainActivity.checkAndSubmit(ResultCheckedActivity.this,0,"0");
                        }
                    }
                } else {
                    MyToast.showShort("没有网络");
                }
                break;
        }
    }

    @Override
    public void uploadMsgSucceed() {
        dismissDialog();
    }

    @Override
    public void uploadMsgFail() {
        finish();
        dismissDialog();
    }

    public void showCheckPriceResultDialog(ResponseJson<CheckPriceBean> response){
            //1 合理估值
            if (response.getMemberValue().getCode() == 1){
                //直接提交数据
//                showDialog();
//                detectMainActivity.checkAndSubmit(ResultCheckedActivity.this,1,"0");
            }else if (response.getMemberValue().getCode() == 2){//超过预警值
                final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(this);
                View view = myUniversalDialog.getLayoutView(R.layout.checkpricelayoutview);
                TextView tvmessage = (TextView) view.findViewById(R.id.tvmessage);
                TextView tvonlyonetime = (TextView) view.findViewById(R.id.tv_only_one_time);//只有一次修改机会
                TextView tvproblemprice = (TextView) view.findViewById(R.id.tvproblemprice);//问题价格
                TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
                TextView tvright = (TextView) view.findViewById(R.id.tvright);
                String messageContent = response.getMemberValue().getMessage().getMessageContent();
                if (!TextUtils.isEmpty(messageContent)){
                    tvmessage.setText(messageContent);
                }
                String messageTimes = response.getMemberValue().getMessage().getMessageTimes();
                if (!TextUtils.isEmpty(messageTimes)){
                    tvonlyonetime.setText(messageTimes);
                }
                String messagePrice = response.getMemberValue().getMessage().getMessagePrice();
                if (!TextUtils.isEmpty(messagePrice)){
                    tvproblemprice.setText(messagePrice);
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
                //修改价格
                tvleft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myUniversalDialog.dismiss();
                        //关闭页面
                        finish();
                    }
                });
                //确认价格
                tvright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myUniversalDialog.dismiss();
                        //提交数据到服务器
                        detectMainActivity.checkAndSubmit(ResultCheckedActivity.this,1,"1");
                    }
                });
            }else if (response.getMemberValue().getCode() == 3){//超过峰值，但未超出评估次数
                final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(this);
                View view = myUniversalDialog.getLayoutView(R.layout.checkpricelayoutview);
                TextView tvmessage = (TextView) view.findViewById(R.id.tvmessage);
                TextView tvonlyonetime = (TextView) view.findViewById(R.id.tv_only_one_time);//只有一次修改机会
                TextView tvproblemprice = (TextView) view.findViewById(R.id.tvproblemprice);
                TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
                TextView tvright = (TextView) view.findViewById(R.id.tvright);
                String messageContent = response.getMemberValue().getMessage().getMessageContent();
                if (!TextUtils.isEmpty(messageContent)){
                    tvmessage.setText(messageContent);
                }
                String messageTimes = response.getMemberValue().getMessage().getMessageTimes();
                if (!TextUtils.isEmpty(messageTimes)){
                    tvonlyonetime.setText(messageTimes);
                }
                String messagePrice = response.getMemberValue().getMessage().getMessagePrice();
                if (!TextUtils.isEmpty(messagePrice)){
                    tvproblemprice.setText(messagePrice);
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
                //修改价格
                tvleft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myUniversalDialog.dismiss();
                        //关闭页面
                        finish();
                    }
                });
                //确认价格
                tvright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myUniversalDialog.dismiss();
                        //提交数据到服务器
                        detectMainActivity.checkAndSubmit(ResultCheckedActivity.this,1,"1");//由登记日期未见引起
                    }
                });

            }else if (response.getMemberValue().getCode() == 4){//4: 超出峰值，并且已经超出评估次数

            }
    }

    @Override
    public void requestTopWarningValueSucceed(ResponseJson<TopWarningValueBean> response) {
        String message = "收车价最大峰值:"+response.getMemberValue().getAssessPrice().getMaxPeakValue()+"\n"
                +"收车价最小峰值:"+response.getMemberValue().getAssessPrice().getMinPeakValue()+"\n"
                +"收车价最大预警值:"+response.getMemberValue().getAssessPrice().getMaxWarningValue()+"\n"
                +"收车价最小预警值:"+response.getMemberValue().getAssessPrice().getMinWarningValue()+"\n"
                +"售车价最大峰值："+response.getMemberValue().getSalePrice().getMaxPeakValue()+"\n"
                +"售车价最小峰值："+response.getMemberValue().getSalePrice().getMinPeakValue()+"\n"
                +"售车价最大预警值："+response.getMemberValue().getSalePrice().getMaxWarningValue()+"\n"
                +"售车价最小预警值："+response.getMemberValue().getSalePrice().getMinWarningValue();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                    }
//                })
                .show();
    }

    @Override
    public void showError(String error) {
        super.showError(error);
//        MyToast.showLong("请检查网络...");
    }
}
