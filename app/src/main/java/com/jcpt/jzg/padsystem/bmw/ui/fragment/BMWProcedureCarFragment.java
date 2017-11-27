package com.jcpt.jzg.padsystem.bmw.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.MyRegionSimpleNameTagStringAdapter;
import com.jcpt.jzg.padsystem.adapter.MyTagStringAdapter;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.bmw.presenter.GetTrafficViolationPresenter;
import com.jcpt.jzg.padsystem.bmw.ui.activity.BMWDetectMainActivity;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.dialog.ShowMsgDialog;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.mvpview.IBMWTrafficViolationInf;
import com.jcpt.jzg.padsystem.mvpview.ICarInfoInterface;
import com.jcpt.jzg.padsystem.mvpview.IVInChecked;
import com.jcpt.jzg.padsystem.presenter.CarDataByVinPresenter;
import com.jcpt.jzg.padsystem.presenter.VinCheckedPresenter;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.CarVINCheck;
import com.jcpt.jzg.padsystem.utils.DateTool;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.InputUtil;
import com.jcpt.jzg.padsystem.utils.LicencePlateAddress;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.vo.BMWOrderInfBean;
import com.jcpt.jzg.padsystem.vo.CarInfoModel;
import com.jcpt.jzg.padsystem.vo.EventModel;
import com.jcpt.jzg.padsystem.vo.EventProcedureModel;
import com.jcpt.jzg.padsystem.vo.ProcedureModel;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.TrafficViolationBean;
import com.jcpt.jzg.padsystem.vo.VinCheckedModel;
import com.jcpt.jzg.padsystem.vo.detection.DetectionWrapper;
import com.jcpt.jzg.padsystem.vo.detection.PictureItem;
import com.jcpt.jzg.padsystem.widget.MyUniversalDialog;
import com.jcpt.jzg.padsystem.widget.nodoubleclick.NoDoubleOnclickListener;
import com.jcpt.jzg.padsystem.widget.tag.FlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagFlowLayout;
import com.jcpt.jzg.padsystem.widget.tag.TagView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jcpt.jzg.padsystem.R.id.tv_register_date;
import static com.jcpt.jzg.padsystem.app.PadSysApp.getUser;

/**
 * 对应的初始化对象名【ProcedureList】："ProcedureList":["81","82","87","88","89","90","91","92","93","94","96","97","98","99","100","101","102","103","105","106","107","109","110","111","112"]
 * 若ProcedureList中去除了某个id，那么该id对应的版块隐藏，同时不校验该版块的值
 * <p>
 * 89 车牌号码
 * 91 车架号VIN
 * ** 车身颜色  写死在本地，永远显示
 * 81 行驶证照片
 * 87 登记日期
 * 101 出厂日期
 * 92 发动机号
 * 277 钥匙
 * 278 车牌数量
 * 279 维保记录可查
 * 280 DMS无大事故记录
 * 281 事故记录已维修
 * 286 无未结算事故维修记录
 * 282 技术召回
 * 287 燃油表无报警
 * 274 里程表显示
 * 283 下次保养天数
 * 284 下次保养距离
 * 285 有无此附件
 * <p>
 * <p>
 * 手续照片保存名字               照片对应板块
 * 18               行驶证正本正面
 * 19               行驶证正本背面
 * 20               行驶证副本正面
 * 24               行驶证副本背面
 * <p>
 *  宝马手续信息
 */
public class BMWProcedureCarFragment extends BaseFragment implements IVInChecked, ICarInfoInterface {

    private BMWProcedureCarFragment self;

    @BindView(R.id.v_line_driving)
    View vLineDriving;

    @BindView(R.id.ll_driving)
    LinearLayout llDriving;
    //行驶证正本正面
    @BindView(R.id.iv_dri_lic_front)
    SimpleDraweeView sdvDrivingLicenceFront;
    //文字
    @BindView(R.id.tvDriLicFront)
    TextView tvDriLicFront;

    //行驶证正本背面
    @BindView(R.id.iv_dri_lic_back)
    SimpleDraweeView sdvDrivingLicenceBack;
    //文字
    @BindView(R.id.tvDriLicBack)
    TextView tvDriLicBack;

    //行驶证副本正面
    @BindView(R.id.iv_dri_lic_vice_front)
    SimpleDraweeView sdvDrivingLicenceViceFront;
    //文字
    @BindView(R.id.tvDriLicViceFront)
    TextView tvDriLicViceFront;

    //行驶证副本背面
    @BindView(R.id.iv_dri_lic_vice_back)
    SimpleDraweeView sdvDrivingLicenceViceBack;
    //文字
    @BindView(R.id.tvDriLicViceBack)
    TextView tvDriLicViceBack;

    //行驶证未见
    @BindView(R.id.cBoxXSZWJ)
    CheckBox cBoxDrivingNone;

    //无行驶证
    @BindView(R.id.cbDrivingLicNo)
    CheckBox cbDrivingLicNo;


    //车牌号码
    @BindView(R.id.v_line_plate_id)
    View vLinePlateId;
    @BindView(R.id.ll_plate_id)
    LinearLayout llPlateId;
    @BindView(R.id.tv_region_simple_name)
    TextView tvRegionSimpleName;
    @BindView(R.id.et_car_plate_no)
    EditText etPlateNo;
    @BindView(R.id.cbCarPlateId)
    CheckBox cbCarPlateId;

    //车架号VIN
    @BindView(R.id.v_line_vin)
    View vLineVin;
    @BindView(R.id.ll_vin)
    LinearLayout llVin;
    @BindView(R.id.et_vin)
    TextView etVIN;

    //车身颜色
    @BindView(R.id.v_line_car_color)
    View vLineCarColor;
    @BindView(R.id.ll_car_color)
    LinearLayout llCarColor;
    @BindView(R.id.tvCarColor)
    TextView tvCarColor;

    //登记日期
    @BindView(R.id.view_line_register_date)
    View vLineRegisterDate;
    @BindView(R.id.ll_register_date)
    LinearLayout llRegisterDate;
    @BindView(tv_register_date)
    TextView tvRegisterDate;
    @BindView(R.id.tv_register_date_name)
    TextView tvRegisterDateName;
    @BindView(R.id.cbRegisterDateNone)
    CheckBox cbRegisterDateNone;

    //出厂日期
    @BindView(R.id.view_line_factory_date)
    View vLineFactoryDate;
    @BindView(R.id.ll_factory_date)
    LinearLayout llFactoryDate;
    @BindView(R.id.tv_factory_date)
    TextView tvFactoryDate;

    //发动机号
    @BindView(R.id.v_line_engine_id)
    View vLineEngineId;
    @BindView(R.id.ll_engine_id)
    LinearLayout llEngineId;
    @BindView(R.id.et_engine_id)
    EditText etEngineId;
    @BindView(R.id.tv_engine_id)
    TextView tvEngineId;

    //钥匙
    @BindView(R.id.v_line_key)
    View vLineKey;
    @BindView(R.id.llKey)
    LinearLayout llKey;
    @BindView(R.id.rgKey)
    RadioGroup rgKey;
    @BindView(R.id.rbKeyStdYes)
    RadioButton rbKeyStdYes;
    @BindView(R.id.rbKeyStdNo)
    RadioButton rbKeyStdNo;

    //车牌数量
    @BindView(R.id.v_line_car_plate_num)
    View vLineCarPlateNum;
    @BindView(R.id.llCarPlate)
    LinearLayout llCarPlate;
    @BindView(R.id.rbCarPlateNumStdYes)
    RadioButton rbCarPlateNumStdYes;
    @BindView(R.id.rbCarPlateNumStdNo)
    RadioButton rbCarPlateNumStdNo;

    //维保记录可查
    @BindView(R.id.v_line_maint_record)
    View vLineMaintRecord;
    @BindView(R.id.llMaintRecord)
    LinearLayout llMaintRecord;
    @BindView(R.id.rgMaiRecord)
    RadioGroup rgMaiRecord;
    @BindView(R.id.rbMaiRecordYes)
    RadioButton rbMaiRecordYes;
    @BindView(R.id.rbMaiRecordNo)
    RadioButton rbMaiRecordNo;

    //DMS无大事故记录
    @BindView(R.id.v_line_dms_major_acci)
    View vLineDMSMajorAcci;
    @BindView(R.id.llDMSAcciRecord)
    LinearLayout llDMSAcciRecord;
    @BindView(R.id.rgDMSAcciRecord)
    RadioGroup rgDMSAcciRecord;
    @BindView(R.id.rbDMSAcciRecordYes)
    RadioButton rbDMSAcciRecordYes;
    @BindView(R.id.rbDMSAcciRecordNo)
    RadioButton rbDMSAcciRecordNo;

    //事故记录已维修
    @BindView(R.id.v_line_acci_maintain)
    View vLineAcciMaintain;
    @BindView(R.id.llAcciMaintain)
    LinearLayout llAcciMaintain;
    @BindView(R.id.rgAcciMaintain)
    RadioGroup rgAcciMaintain;
    @BindView(R.id.rbAcciMaintainYes)
    RadioButton rbAcciMaintainYes;
    @BindView(R.id.rbAcciMaintainNo)
    RadioButton rbAcciMaintainNo;

    //无未结算事故维修记录
    @BindView(R.id.v_line_acci_no_pay)
    View vLineAcciNoPay;
    @BindView(R.id.llAcciNoPay)
    LinearLayout llAcciNoPay;
    @BindView(R.id.rgAcciNoPay)
    RadioGroup rgAcciNoPay;
    @BindView(R.id.rbAcciNoPayYes)
    RadioButton rbAcciNoPayYes;
    @BindView(R.id.rbAcciNoPayNo)
    RadioButton rbAcciNoPayNo;

    //技术召回
    @BindView(R.id.v_line_technic_recall)
    View vLineTechnicRecall;
    @BindView(R.id.llTechnicRecall)
    LinearLayout llTechnicRecall;
    @BindView(R.id.rgTechnicRecall)
    RadioGroup rgTechnicRecall;
    @BindView(R.id.rbTechnicRecallYes)
    RadioButton rbTechnicRecallYes;
    @BindView(R.id.rbTechnicRecallNo)
    RadioButton rbTechnicRecallNo;

    //燃油表无报警
    @BindView(R.id.v_line_oil_alarm)
    View vLineOilAlarm;
    @BindView(R.id.llOilAlarm)
    LinearLayout llOilAlarm;
    @BindView(R.id.rgOilAlarm)
    RadioGroup rgOilAlarm;
    @BindView(R.id.rbOilAlarmYes)
    RadioButton rbOilAlarmYes;
    @BindView(R.id.rbOilAlarmNo)
    RadioButton rbOilAlarmNo;

    //里程表显示
    @BindView(R.id.v_line_mileage)
    View vLineMileage;
    @BindView(R.id.llMileage)
    LinearLayout llMileage;
    @BindView(R.id.etMileage)
    EditText etMileage;

    //下次保养天数
    @BindView(R.id.v_line_next_maintain_days)
    View vLineNextMaintainDays;
    @BindView(R.id.ll_next_maintain_days)
    LinearLayout llNextMaintainDays;
    @BindView(R.id.et_next_maintain_days)
    EditText etNextMaintainDays;

    //下次保养距离
    @BindView(R.id.v_line_next_maint_mileage)
    View vLineNextMaintMileage;
    @BindView(R.id.ll_next_maint_mileage)
    LinearLayout llNextMaintMileage;
    @BindView(R.id.et_next_maintain_mileage)
    EditText etNextMaintainMileage;

    //有无此附件
    @BindView(R.id.v_line_accessory)
    View vLineAccessory;
    @BindView(R.id.ll_accessory)
    LinearLayout llAccessory;


    //钥匙
    @BindView(R.id.tflKeyNum)
    TagFlowLayout tflKeyNum;
    //车牌数量
    @BindView(R.id.tflCarPlateNum)
    TagFlowLayout tflCarPlateNum;
    //有无此附件
    @BindView(R.id.tflAccessory)
    TagFlowLayout tflAccessory;


    MyTagStringAdapter tagStringAdapter;


    String[] tflKeyNumLists = {"0", "1", "2", "3", "4"};
    String[] tflCarPlateNumLists = {"0", "1", "2"};
    String[] tflAccessoryLists = {"年检标", "交强险标", "用户手册", "随车说明书", "三包凭证", "保修服务手册", "救援电话", "授权经销商联系手册"};

    private DetectionWrapper detectionWrapper;
    //下面是：配置项
    private boolean configPlateNum = false;//车牌号码 89
    private boolean configVin = false;//vin 91
    private boolean configCarColor = true;//车身颜色 方案里不配，永远显示
    private boolean configDriving = false;//行驶证照片 81
    private boolean configRegisterDate = false;//登记日期 87
    private boolean configFactoryDate = false;//出厂日期 101
    private boolean configEngineId = false;//发动机号 92
    private boolean configKey = false;//钥匙 277
    private boolean configCarPlateNum = false;//车牌数量 278
    private boolean configMaintainRecord = false;//维保记录可查 279
    private boolean configDMSAcciRecord = false;//DMS无大事故记录 280
    private boolean configAcciMaintained = false;//事故记录已维修 281
    private boolean configAcciMaintainedPay = false;//无未结算事故维修 286
    private boolean configTechnicRecall = false;//技术召回 282
    private boolean configOilAlarm = false;//燃油表无报警 287
    private boolean configMileage = false;//里程表显示 274
    private boolean configNextMaintDays = false;//下次保养天数 283
    private boolean configNextMaintMileage = false;//下次保养距离 284
    private boolean configAccessory = false;//有无此附件 285

    private SubmitModel submitModel;

    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItemsDL;//行驶证照片
    private int curPicPos = 0;//当前点击图片的位置
    private ArrayList<String> highQualityPicIdArray = new ArrayList<>();//高清图片列表
    private EventProcedureModel eventProcedureModel;

    private final int PHOTO_REQUEST_DL = 10;//行驶证拍照
    private final int PHOTO_BIG_PHOTO_DL = 11;//行驶证预览大图

    private String pathDLFront;//行驶证正本正面本地图片地址
    private String pathDLBack;//行驶证正本背面本地图片地址
    private String pathDLViceFront;//行驶证副本正面本地图片地址
    private String pathDLViceBack;//行驶证副本背面本地图片地址

    private FrescoImageLoader frescoImageLoader;

    public ProcedureModel procedureModel;
    private String taskId;
    private VinCheckedPresenter vinCheckedPresenter;
    private TaskDetailModel taskDetailModel;//修改返回的详情数据
    //通过VIN获取合格证数据（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
    private CarDataByVinPresenter carDataByVinPresenter;
    private DateTool dateTool;
    private BMWOrderInfBean bmwOrderInfBean;


    @Override
    protected void initData() {
        frescoImageLoader = FrescoImageLoader.getSingleton();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procedure_car_bmw, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        self = this;
        carDataByVinPresenter = new CarDataByVinPresenter(this);
        dateTool = new DateTool();

        if (submitModel == null) {
            submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();
        }

        bmwOrderInfBean = ((BMWDetectMainActivity) getActivity()).getBMWOrderInfBean();
        //通过行驶证识别传递过来的数据或验证VIN后传过来的
        String vin = ACache.get(this.getContext().getApplicationContext()).getAsString("vin");//VIN
        if(!StringUtils.isEmpty(vin)){
            submitModel.setVin(vin);
        }

        initListener();
        init();
        return view;
    }

    @Override
    protected void setView() {
        //请求合证证数据，用于车型选择，不做展示用（第一次进入手续信息和每次修改VIN后请求）（选择车型时调取车辆合格证中的【车辆型号】，并默认选中，不再通过VIN查询车辆型号）
        String vin = submitModel.getVin();
        if (!StringUtils.isEmpty(vin)) {
            carDataByVinPresenter.requestCarInfo(vin);
        }
    }

    /**
     * 根据服务器返回的配置项决定显示哪些栏信息
     *
     * @author zealjiang
     * @time 2016/11/23 16:02
     */
    private void configItem() {
        if (detectionWrapper == null) {
            return;
        }
        List<String> configList = detectionWrapper.getProcedureList();
        for (int i = 0; i < configList.size(); i++) {
            switch (configList.get(i)) {
                case "89"://车牌号码
                    configPlateNum = true;
                    break;
                case "91"://VIN
                    configVin = true;
                    break;
                case "81"://行驶证照片
                    configDriving = true;
                    break;
                case "87"://登记日期
                    configRegisterDate = true;
                    break;
                case "101"://出厂日期
                    configFactoryDate = true;
                    break;
                case "92"://发动机号
                    configEngineId = true;
                    break;
                case "277"://钥匙
                    configKey = true;
                    break;
                case "278"://车牌数量
                    configCarPlateNum = true;
                    break;
                case "279"://维保记录可查
                    configMaintainRecord = true;
                    break;
                case "280"://DMS无大事故记录
                    configDMSAcciRecord = true;
                    break;
                case "281"://事故记录已维修
                    configAcciMaintained = true;
                    break;
                case "286"://无未结算事故维修
                    configAcciMaintainedPay = true;
                    break;
                case "282"://技术召回
                    configTechnicRecall = true;
                    break;
                case "287"://燃油表无报警
                    configOilAlarm = true;
                    break;
                case "274"://里程表显示
                    configMileage = true;
                    break;
                case "283"://下次保养天数
                    configNextMaintDays = true;
                    break;
                case "284"://下次保养距离
                    configNextMaintMileage = true;
                    break;
                case "285"://有无此附件
                    configAccessory = true;
                    break;

            }
        }

        if (!configPlateNum) {//车牌号码
            vLinePlateId.setVisibility(View.GONE);
            llPlateId.setVisibility(View.GONE);
        }
        if (!configVin) {//VIN
            vLineVin.setVisibility(View.GONE);
            llVin.setVisibility(View.GONE);
        }
        if (!configCarColor) {//车身颜色
            vLineCarColor.setVisibility(View.GONE);
            llCarColor.setVisibility(View.GONE);
        }
        if (!configDriving) {//行驶证照片
            vLineDriving.setVisibility(View.GONE);
            llDriving.setVisibility(View.GONE);
        }
        if (!configRegisterDate) {//登记日期
            vLineRegisterDate.setVisibility(View.GONE);
            llRegisterDate.setVisibility(View.GONE);
        }
        if (!configFactoryDate) {//出厂日期
            vLineFactoryDate.setVisibility(View.GONE);
            llFactoryDate.setVisibility(View.GONE);
        }
        if (!configEngineId) {//发动机号
            vLineEngineId.setVisibility(View.GONE);
            llEngineId.setVisibility(View.GONE);
        }
        if (!configKey) {//钥匙
            vLineKey.setVisibility(View.GONE);
            llKey.setVisibility(View.GONE);
        }
        if (!configCarPlateNum) {//车牌数量
            vLineCarPlateNum.setVisibility(View.GONE);
            llCarPlate.setVisibility(View.GONE);
        }
        if (!configMaintainRecord) {//维保记录可查
            vLineMaintRecord.setVisibility(View.GONE);
            llMaintRecord.setVisibility(View.GONE);
        }
        if (!configDMSAcciRecord) {//DMS无大事故记录
            vLineDMSMajorAcci.setVisibility(View.GONE);
            llDMSAcciRecord.setVisibility(View.GONE);
        }
        if (!configAcciMaintained) {//事故记录已维修
            vLineAcciMaintain.setVisibility(View.GONE);
            llAcciMaintain.setVisibility(View.GONE);
        }
        if (!configAcciMaintainedPay) {//无未结算事故维修
            vLineAcciNoPay.setVisibility(View.GONE);
            llAcciNoPay.setVisibility(View.GONE);
        }
        if (!configTechnicRecall) {//技术召回
            vLineTechnicRecall.setVisibility(View.GONE);
            llTechnicRecall.setVisibility(View.GONE);
        }
        if (!configOilAlarm) {//燃油表无报警
            vLineOilAlarm.setVisibility(View.GONE);
            llOilAlarm.setVisibility(View.GONE);
        }
        if (!configMileage) {//里程表显示
            vLineMileage.setVisibility(View.GONE);
            llMileage.setVisibility(View.GONE);
        }
        if (!configNextMaintDays) {//下次保养天数
            vLineNextMaintainDays.setVisibility(View.GONE);
            llNextMaintainDays.setVisibility(View.GONE);
        }
        if (!configNextMaintMileage) {//下次保养距离
            vLineNextMaintMileage.setVisibility(View.GONE);
            llNextMaintMileage.setVisibility(View.GONE);
        }
        if (!configAccessory) {//有无此附件
            vLineAccessory.setVisibility(View.GONE);
            llAccessory.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        //车牌号码不能输入表情符 汉字 i I o O
        InputUtil.getInstance().inputRestrict(etPlateNo, true, true, false, false, "iIoO", null);

        //发动机号不能输入表情符 汉字
        InputUtil.getInstance().inputRestrict(etEngineId, true, true, false, false, null, null);

        //车牌未见
        cbCarPlateId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etPlateNo.setText("");
                    etPlateNo.setEnabled(false);
                } else {
                    etPlateNo.setEnabled(true);
                }
            }
        });


        //车型号vin
        etVIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //只修改VIN
                checkVINDialog();
            }
        });


        //登记日期
        tvRegisterDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //登记日期
                dateTool.selectDate(self.getContext(), tvRegisterDate, "登记日期", false);
            }
        });

        //登记日期未见
        cbRegisterDateNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tvRegisterDate.setText("");
                    tvRegisterDate.setEnabled(false);
                } else {
                    tvRegisterDate.setEnabled(true);
                }
            }
        });

        //出厂日期
        tvFactoryDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //出厂日期
                dateTool.selectYearMonth(self.getContext(), "出厂日期", tvFactoryDate, Calendar.getInstance().get(Calendar.YEAR), 1990, false);
            }
        });

        //行驶证未见
        cBoxDrivingNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cBoxDrivingNone.setTextColor(getResources().getColor(R.color.white));
                    cbDrivingLicNo.setEnabled(false);
                    cbDrivingLicNo.setBackgroundResource(R.drawable.btn_bg_unable);
                    cbDrivingLicNo.setTextColor(getResources().getColor(R.color.global_gray_2));

                    //判断是否存在行驶证照片
                    boolean isEmpty = isDrivingLPicEmpty();
                    if (isEmpty && isChecked) {
                        drivingLicenceChecked(1);
                    } else {
                        ShowMsgDialog.showMaterialDialog2Btn(context, "是否要删除行驶证照片？", "", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //重新设置回未点击状态
                                cBoxDrivingNone.setChecked(false);
                                return;
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drivingLicenceChecked(1);
                            }
                        });
                    }

                } else {
                    cBoxDrivingNone.setTextColor(getResources().getColor(R.color.black));
                    cbDrivingLicNo.setEnabled(true);
                    cbDrivingLicNo.setBackgroundResource(R.drawable.btn_bg_selector);
                    cbDrivingLicNo.setTextColor(getResources().getColor(R.color.btn_text_selector));

                    //行驶证未见没选中
                    sdvDrivingLicenceFront.setClickable(true);
                    sdvDrivingLicenceBack.setClickable(true);
                    sdvDrivingLicenceViceFront.setClickable(true);
                    sdvDrivingLicenceViceBack.setClickable(true);

                    eventProcedureModel.setDrivingNoneIsSelect(false);
                    EventBus.getDefault().post(eventProcedureModel);

                    //四张行驶证照片的文字颜色置黑
                    tvDriLicFront.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicBack.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicViceFront.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicViceBack.setTextColor(getResources().getColor(R.color.black));

                }

            }
        });

        //无行驶证
        cbDrivingLicNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbDrivingLicNo.setTextColor(getResources().getColor(R.color.white));
                    cBoxDrivingNone.setEnabled(false);
                    cBoxDrivingNone.setBackgroundResource(R.drawable.btn_bg_unable);
                    cBoxDrivingNone.setTextColor(getResources().getColor(R.color.global_gray_2));

                    //判断是否存在行驶证照片
                    boolean isEmpty = isDrivingLPicEmpty();
                    if (isEmpty && b) {
                        drivingLicenceChecked(2);
                    } else {
                        ShowMsgDialog.showMaterialDialog2Btn(context, "是否要删除行驶证照片？", "", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //重新设置回未点击状态
                                cbDrivingLicNo.setChecked(false);
                                return;
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drivingLicenceChecked(2);
                            }
                        });
                    }
                }else{
                    cbDrivingLicNo.setTextColor(getResources().getColor(R.color.black));
                    cBoxDrivingNone.setEnabled(true);
                    cBoxDrivingNone.setBackgroundResource(R.drawable.btn_bg_selector);
                    cBoxDrivingNone.setTextColor(getResources().getColor(R.color.btn_text_selector));


                    //无行驶证没选中
                    sdvDrivingLicenceFront.setClickable(true);
                    sdvDrivingLicenceBack.setClickable(true);
                    sdvDrivingLicenceViceFront.setClickable(true);
                    sdvDrivingLicenceViceBack.setClickable(true);

                    eventProcedureModel.setDrivingNoneIsSelect(false);
                    EventBus.getDefault().post(eventProcedureModel);

                    //四张行驶证照片的文字颜色置黑
                    tvDriLicFront.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicBack.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicViceFront.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicViceBack.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        etMileage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String sMileage = etMileage.getText().toString();
                    if(!StringUtils.isEmpty(sMileage)&&Integer.valueOf(sMileage)<100){
                        MyToast.showShort("里程不能小于100");
                    }
                }
            }
        });
        //里程表显示  不能小于100
        etMileage.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            //String changedText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().startsWith("0")) {
                    etMileage.removeTextChangedListener(this);
                    etMileage.setText(beforeText);
                    etMileage.setSelection(etMileage.getText().length());
                    etMileage.addTextChangedListener(this);
                }
            }
        });

        //下次保养天数  不允许超过360天
        etNextMaintainDays.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if (before == 0 && count > 0) {
                    changedText = s.subSequence(start, start + count).toString();
                } else {
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (changedText.equals("")) {
                    return;
                }
                //LogUtil.e("afterTextChanged changedText",changedText);

                if (Integer.valueOf(s.toString()) > 360) {
                    etNextMaintainDays.removeTextChangedListener(this);
                    etNextMaintainDays.setText(beforeText);
                    etNextMaintainDays.setSelection(etNextMaintainDays.getText().length());
                    MyToast.showShort("不允许超过360天");
                    etNextMaintainDays.addTextChangedListener(this);
                }
                if (s.toString().startsWith("0") && s.toString().length() > 1) {
                    etNextMaintainDays.removeTextChangedListener(this);
                    etNextMaintainDays.setText(beforeText);
                    etNextMaintainDays.setSelection(etNextMaintainDays.getText().length());
                    etNextMaintainDays.addTextChangedListener(this);
                }
            }
        });
    }

    /**
     * 初始经连拍照片
     *
     * @author zealjiang
     * @time 2016/11/29 16:00
     */
    private void initPicData() {
        //初始化照片
        pictureItemsDL = new ArrayList<>();
        pictureItemsDL.add(new PictureItem("18", "行驶证正本正面", ""));
        pictureItemsDL.add(new PictureItem("19", "行驶证正本背面", ""));
        pictureItemsDL.add(new PictureItem("20", "行驶证副本正面", ""));
        pictureItemsDL.add(new PictureItem("24", "行驶证副本背面", ""));

        pathDLFront = PadSysApp.picDirPath + "18.jpg";//行驶证正本正面本地图片地址
        pathDLBack = PadSysApp.picDirPath + "19.jpg";//行驶证正本背面本地图片地址
        pathDLViceFront = PadSysApp.picDirPath + "20.jpg";//行驶证副本正面本地图片地址
        pathDLViceBack = PadSysApp.picDirPath + "24.jpg";//行驶证副本背面本地图片地址

        //行驶证正本正面
        if (FileUtils.isFileExists(pathDLFront)) {
            pictureItemsDL.get(0).setPicPath(pathDLFront);
        }
        //行驶证正本背面
        if (FileUtils.isFileExists(pathDLBack)) {
            pictureItemsDL.get(1).setPicPath(pathDLBack);
        }
        //行驶证副本正面
        if (FileUtils.isFileExists(pathDLViceFront)) {
            pictureItemsDL.get(2).setPicPath(pathDLViceFront);
        }
        //行驶证副本背面
        if (FileUtils.isFileExists(pathDLViceBack)) {
            pictureItemsDL.get(3).setPicPath(pathDLViceBack);
        }
    }

    private void init() {

        initPageData();
        initLocalData();
        initNetData();
        setPageData();
    }

    private void initPageData(){
        taskId = ((BMWDetectMainActivity) getActivity()).getTaskid();
        detectionWrapper = ((BMWDetectMainActivity) this.getActivity()).getWrapper();
        eventProcedureModel = new EventProcedureModel();
        submitModel = ((BMWDetectMainActivity) getActivity()).getSubmitModel();

        configItem();
        //初始化图片
        initPicData();

        setTag(tflKeyNumLists, tflKeyNum);
        setTag(tflCarPlateNumLists, tflCarPlateNum);
        setTag(tflAccessoryLists, tflAccessory);


        //车牌号简称
        if (TextUtils.isEmpty(getUser().getCityShortName())) {
            tvRegionSimpleName.setText("苏");
        } else {
            //根据登录的信息设置城市简称
            String cshortName = PadSysApp.getUser().getCityShortName();
            tvRegionSimpleName.setText(cshortName);
        }

        //车牌号
        etPlateNo.setTransformationMethod(new InputLowerToUpper());
        //车架号VIN
        etVIN.setTransformationMethod(new InputLowerToUpper());
        etVIN.setText(submitModel.getVin());
        //发动机号
        etEngineId.setTransformationMethod(new InputLowerToUpper());

        //宝马下单时给出
        if(bmwOrderInfBean != null){
            //车牌号码
            if(!StringUtils.isEmpty(bmwOrderInfBean.getCarLicense())){
                tvRegionSimpleName.setText(bmwOrderInfBean.getCarLicense().substring(0,1));
                etPlateNo.setText(bmwOrderInfBean.getCarLicense().substring(1));
            }

            //VIN (以确认过的VIN为主，这里不在覆盖数据)

            //登记日期
            if(!StringUtils.isEmpty(bmwOrderInfBean.getRecordDate())){
                tvRegisterDate.setText(dateTool.dateSplit(bmwOrderInfBean.getRecordDate()));
            }

            //发动机号
            if(!StringUtils.isEmpty(bmwOrderInfBean.getEngineNum())){
                etEngineId.setText(bmwOrderInfBean.getEngineNum());
            }

            //车身颜色
            if(!StringUtils.isEmpty(bmwOrderInfBean.getOtherColor())){
                tvCarColor.setText(bmwOrderInfBean.getOtherColor());
            }
        }
    }

    /**
     * 获取本地数据
     */
    private void initLocalData(){
        //如果本地数据库中有本次继续检测的数据就赋值显示
        //从本地数据库中获取此用户下的taskId的数据
        List<DBBase> list = DBManager.getInstance().query(taskId, Constants.DATA_TYPE_PROCEDURE, getUser().getUserId());
        LogUtil.e(TAG, "本地数据库： " + list + " list.size(): " + list.size());
        if (list != null && list.size() > 0) {
            String json = list.get(0).getJson();
            if (!TextUtils.isEmpty(json)) {
                procedureModel = new Gson().fromJson(json, ProcedureModel.class);
            }
        }
        LogUtil.e(TAG, "procedureModel :" + procedureModel);
        //从本地数据库初始化值
        if (procedureModel != null) {
            if (submitModel == null) {
                if (this.getActivity() == null) {
                    //关闭BMWDetectMainActivity
                    AppManager.getAppManager().finishActivity(BMWDetectMainActivity.class);
                }
                return;
            }
            //车牌号码
            String carLicense = procedureModel.getCarLicense();
            submitModel.setCarLicense(carLicense);

            //车架号VIN
            submitModel.setVin(procedureModel.getVin());

            //车身颜色
            submitModel.setOtherColor(procedureModel.getOtherColor());

            //行驶证
            int id = procedureModel.getDrivingLicenseProperty();
            submitModel.setDrivingLicenseProperty(id);

            //登记日期
            submitModel.setRecordDate(dateTool.dateSplit(procedureModel.getRecordDate()));

            //出厂日期
            submitModel.setProductionTime(dateTool.dateSplit(procedureModel.getProductionTime(),7));


            //发动机号
            submitModel.setEngineNum(procedureModel.getEngineNum());

            //钥匙
            submitModel.setCarKeyDes(procedureModel.getCarKeyDes());

            //车牌数量
            submitModel.setPlateCount(procedureModel.getPlateCount());

            //维保记录可查
            submitModel.setRepairRecord(procedureModel.getRepairRecord());

            //DMS无大事故记录
            submitModel.setAccidentRecord(procedureModel.getAccidentRecord());

            //事故已记录已维修
            submitModel.setAccidentRepair(procedureModel.getAccidentRepair());

            //无未结算事故维修记录
            submitModel.setSettleRecord(procedureModel.getSettleRecord());

            //技术召回
            submitModel.setIsRecall(procedureModel.getIsRecall());

            //燃油表无报警
            submitModel.setPetrolAlarm(procedureModel.getPetrolAlarm());

            //里程表显示
            submitModel.setMileage(procedureModel.getMileage());

            //下次保养天数
            submitModel.setNextRepairDays(procedureModel.getNextRepairDays());

            //下次保养距离
            submitModel.setNextRepairMileage(procedureModel.getNextRepairMileage());

            //有无此附件
            submitModel.setStdInspection(procedureModel.getStdInspection());
            submitModel.setCoyInsurance(procedureModel.getCoyInsurance());
            submitModel.setUserManual(procedureModel.getUserManual());
            submitModel.setBordbuch(procedureModel.getBordbuch());
            submitModel.setThreeGuarantees(procedureModel.getThreeGuarantees());
            submitModel.setGuaranteeHandbook(procedureModel.getGuaranteeHandbook());
            submitModel.setRescueCall(procedureModel.getRescueCall());
            submitModel.setEnvironmental(procedureModel.getEnvironmental());
            submitModel.setContactHandbook(procedureModel.getContactHandbook());

        }
    }

    /**
     * 获取网络数据
     */
    private void initNetData(){
        //判断这个任务是否存在检测详情数据
        taskDetailModel = ((BMWDetectMainActivity) this.getActivity()).getTaskDetailModel();
        LogUtil.e(TAG, "网络： " + " taskDetailModel: " + taskDetailModel);
        if (taskDetailModel != null) {

        }
    }

    /**
     * 本地数据或网络数据后为页面view赋值显示
     */
    private void setPageData() {
        if(procedureModel == null){
            return;
        }
        //车牌号码
        String carLicense = procedureModel.getCarLicense();
        if (!TextUtils.isEmpty(carLicense)) {
            tvRegionSimpleName.setText(carLicense.substring(0, 1));
            etPlateNo.setText(carLicense.substring(1));
        }
        if (procedureModel.isCarPlateNumNone()) {
            cbCarPlateId.setChecked(true);
        }

        //车架号VIN
        etVIN.setText(procedureModel.getVin());

        //车身颜色
        tvCarColor.setText(procedureModel.getOtherColor());

        //行驶证
        int id = procedureModel.getDrivingLicenseProperty();
        if (id == 2) {
            //行驶证未见
            cBoxDrivingNone.setChecked(true);
        }else if(id == 3){
            //无行驶证
            cbDrivingLicNo.setChecked(true);
        }

        //登记日期
        tvRegisterDate.setText(dateTool.dateSplit(procedureModel.getRecordDate()));
        if (procedureModel.isRegisterDateNone()) {
            cbRegisterDateNone.setChecked(true);
        }

        //出厂日期
        tvFactoryDate.setText(dateTool.dateSplit(procedureModel.getProductionTime(),7));

        //发动机号
        etEngineId.setText(procedureModel.getEngineNum());

        Set<Integer> set = new HashSet<Integer>();
        //钥匙
        set.clear();
        if (!StringUtils.isEmpty(procedureModel.getCarKeyDes())) {
            String[] sArray = procedureModel.getCarKeyDes().split(",");
            if (sArray.length > 0) {
                if (!StringUtils.isEmpty(sArray[0])) {
                    set.add(Integer.valueOf(sArray[0]));
                    tflKeyNum.getAdapter().setSelectedList(set);
                }
                if (sArray.length == 2) {
                    if (!StringUtils.isEmpty(sArray[1])) {
                        int value = Integer.valueOf(sArray[1]);
                        if (value == 0) {
                            rbKeyStdNo.setChecked(true);
                        } else if (value == 1) {
                            rbKeyStdYes.setChecked(true);
                        }
                    }
                }
            }

        }

        //车牌数量
        set.clear();
        if (!StringUtils.isEmpty(procedureModel.getPlateCount())) {
            String[] sArray = procedureModel.getPlateCount().split(",");
            if (sArray.length > 0) {
                if (!StringUtils.isEmpty(sArray[0])) {
                    set.add(Integer.valueOf(sArray[0]));
                    tflCarPlateNum.getAdapter().setSelectedList(set);
                }
                if (sArray.length == 2) {
                    if (!StringUtils.isEmpty(sArray[1])) {
                        int value = Integer.valueOf(sArray[1]);
                        if (value == 0) {
                            rbCarPlateNumStdNo.setChecked(true);
                        } else if (value == 1) {
                            rbCarPlateNumStdYes.setChecked(true);
                        }
                    }
                }
            }
        }

        //维保记录可查
        int value = procedureModel.getRepairRecord();
        if (value == 0) {
            rbMaiRecordNo.setChecked(true);
        } else if (value == 1) {
            rbMaiRecordYes.setChecked(true);
        }

        //DMS无大事故记录
        value = procedureModel.getAccidentRecord();
        if (value == 0) {
            rbDMSAcciRecordNo.setChecked(true);
        } else if (value == 1) {
            rbDMSAcciRecordYes.setChecked(true);
        }

        //事故已记录已维修
        value = procedureModel.getAccidentRepair();
        if (value == 0) {
            rbAcciMaintainNo.setChecked(true);
        } else if (value == 1) {
            rbAcciMaintainYes.setChecked(true);
        }

        //无未结算事故维修记录
        value = procedureModel.getSettleRecord();
        if (value == 0) {
            rbAcciNoPayNo.setChecked(true);
        } else if (value == 1) {
            rbAcciNoPayYes.setChecked(true);
        }

        //技术召回
        value = procedureModel.getIsRecall();
        if (value == 0) {
            rbTechnicRecallNo.setChecked(true);
        } else if (value == 1) {
            rbTechnicRecallYes.setChecked(true);
        }

        //燃油表无报警
        value = procedureModel.getPetrolAlarm();
        if (value == 0) {
            rbOilAlarmNo.setChecked(true);
        } else if (value == 1) {
            rbOilAlarmYes.setChecked(true);
        }

        //里程表显示
        if (procedureModel.getMileage() != -1) {
            etMileage.setText(procedureModel.getMileage() + "");
        }

        //下次保养天数
        if (procedureModel.getNextRepairDays() != -1) {
            etNextMaintainDays.setText(procedureModel.getNextRepairDays() + "");
        }

        //下次保养距离
        if (procedureModel.getNextRepairMileage() != -1) {
            etNextMaintainMileage.setText(procedureModel.getNextRepairMileage() + "");
        }

        //有无此附件
        set.clear();
        if (procedureModel.getStdInspection() == 1) {//年检标
            set.add(0);
        }
        if (procedureModel.getCoyInsurance() == 1) {//交强险标
            set.add(1);
        }
        if (procedureModel.getUserManual() == 1) {//用户手册
            set.add(2);
        }
        if (procedureModel.getBordbuch() == 1) {//随车说明书
            set.add(3);
        }
        if (procedureModel.getThreeGuarantees() == 1) {//三包凭证
            set.add(4);
        }
        if (procedureModel.getGuaranteeHandbook() == 1) {//保修服务手册
            set.add(5);
        }
        if (procedureModel.getRescueCall() == 1) {//救援电话
            set.add(6);
        }
        if (procedureModel.getContactHandbook() == 1) {//保修服务手册
            set.add(7);
        }
        tflAccessory.getAdapter().setSelectedList(set);

        //如果当前任务目录下存在手续信息照片，显示这些照片
        refreshDLPic();
    }


    @OnClick({R.id.tv_region_simple_name, R.id.iv_dri_lic_front, R.id.iv_dri_lic_back, R.id.iv_dri_lic_vice_front, R.id.iv_dri_lic_vice_back})
    void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.tv_region_simple_name:
                //车辆号牌首字母
                ArrayList<String> listData = LicencePlateAddress.getInstance().getSimpleName();
                final String[] sArray = listData.toArray(new String[listData.size()]);
                int checkedId = 0;
                String simpleName = tvRegionSimpleName.getText().toString().trim();
                if (!TextUtils.isEmpty(simpleName)) {
                    for (int i = 0; i < sArray.length; i++) {
                        if (sArray[i].equals(simpleName)) {
                            checkedId = i;
                            break;
                        }
                    }
                }

                final MyUniversalDialog dialog = new MyUniversalDialog(context);
                View layoutView = dialog.getLayoutView(R.layout.region_simple_name);
                final TagFlowLayout tflRegionSimpleName = (TagFlowLayout) layoutView.findViewById(R.id.tflRegionSimpleName);
                setRegionSimpleNameTag(sArray, tflRegionSimpleName);
                TagView tagView = (TagView) tflRegionSimpleName.getChildAt(checkedId);
                tagView.setChecked(true);
                dialog.setLayoutGravity(layoutView, MyUniversalDialog.DialogGravity.CENTER);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                tflRegionSimpleName.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(TagView view, int position, FlowLayout parent) {
                        dialog.dismiss();
                        TagView tagView = (TagView) tflRegionSimpleName.getChildAt(position);
                        TextView tv = (TextView) tagView.getTagView();
                        tvRegionSimpleName.setText(tv.getText().toString());
                        return false;
                    }
                });
                break;
            case R.id.iv_dri_lic_front://行驶证正本正面
                curPicPos = 0;
                takePic(pictureItemsDL, PHOTO_REQUEST_DL, PHOTO_BIG_PHOTO_DL);
                break;
            case R.id.iv_dri_lic_back://行驶证正本背面
                curPicPos = 1;
                takePic(pictureItemsDL, PHOTO_REQUEST_DL, PHOTO_BIG_PHOTO_DL);
                break;
            case R.id.iv_dri_lic_vice_front://行驶证副本正面
                curPicPos = 2;
                takePic(pictureItemsDL, PHOTO_REQUEST_DL, PHOTO_BIG_PHOTO_DL);
                break;
            case R.id.iv_dri_lic_vice_back://行驶证副本背面
                curPicPos = 3;
                takePic(pictureItemsDL, PHOTO_REQUEST_DL, PHOTO_BIG_PHOTO_DL);
                break;
        }
    }

    public void setRegionSimpleNameTag(final String[] vals, final TagFlowLayout tagFlowLayout) {
        MyRegionSimpleNameTagStringAdapter simpleNameAdapter = new MyRegionSimpleNameTagStringAdapter(vals, tagFlowLayout, getActivity());
        tagFlowLayout.setAdapter(simpleNameAdapter);
    }

    /**
     * 点击VIN 时弹出
     * 确认是否有VIN；VIN是否正确；实现修改 vin ，修改成功并用新 vin 重新获取合格证数据
     */
    public void checkVINDialog() {
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(context);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_check_vin_layout);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        final EditText editVin = (EditText) view.findViewById(R.id.editVin);
        editVin.setText(etVIN.getText().toString());
        editVin.setTransformationMethod(new InputLowerToUpper());
        editVin.setSelection(editVin.getText().length());
        TextView tvDrivingLicense = (TextView) view.findViewById(R.id.tvDrivingLicense);
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tvDrivingLicense.setVisibility(View.GONE);
        TextView btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        TextView btnSubmit = (TextView) view.findViewById(R.id.btnSubmit);
        btnSubmit.setText("确认");
        myUniversalDialog.setLayoutView(view, 480, 260);
        boolean hasVIN = true;
        if (TextUtils.isEmpty(etVIN.getText().toString())) {
            hasVIN = false;
            //弹出软键盘
            KeyboardUtils.showSoftInput(context, editVin);
        }
        if (hasVIN) {
            tvTitle.setText("请确认车架号VIN码是否正确？");
        } else {
            tvTitle.setText("请输入车架号VIN码");
        }
        myUniversalDialog.show();
        myUniversalDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 校验VIN
                 */
                String vin = editVin.getText().toString();
                boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
                if (isValid) {
                    //判断是否与上次VIN相同，相同则直接关闭对话框
                    String oldVin = etVIN.getText().toString();
                    if (vin.equals(oldVin)) {
                        myUniversalDialog.cancel();
                    } else {
                        if (vinCheckedPresenter == null) {
                            vinCheckedPresenter = new VinCheckedPresenter(BMWProcedureCarFragment.this);
                        }
                        //请求服务器修改 vin
                        vinCheckedPresenter.getVinCheckedResult(getUser().getUserId() + "", vin, taskId, myUniversalDialog);
                    }
                } else {
                    MyToast.showLong("请输入正确VIN");
                }
            }
        });

    }

    private void showAlertAccident(String accdientAlertMsg) {
        if (!TextUtils.isEmpty(accdientAlertMsg)) {
            final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(context);
            View view = myUniversalDialog.getLayoutView(R.layout.dialog_check_vin);
            TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            TextView tvIknow = (TextView) view.findViewById(R.id.tvIknow);
            tvMsg.setText(accdientAlertMsg);
            myUniversalDialog.setLayoutView(view, 480, 260);
            myUniversalDialog.show();
            tvIknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myUniversalDialog.cancel();
                }
            });
        }
    }

    public void setTag(ArrayList<String> list, final TagFlowLayout tagFlowLayout) {
        tagStringAdapter = new MyTagStringAdapter(list, tagFlowLayout, getActivity());
        tagFlowLayout.setAdapter(tagStringAdapter);
        setTagListener(tagFlowLayout);
    }

    public void setTag(final String[] vals, final TagFlowLayout tagFlowLayout) {
        tagStringAdapter = new MyTagStringAdapter(vals, tagFlowLayout, getActivity());
        tagFlowLayout.setAdapter(tagStringAdapter);
        setTagListener(tagFlowLayout);
    }

    private void setTagListener(final TagFlowLayout tagFlowLayout) {

        TagFlowLayout.OnTagClickListener onTagClickListener = new TagFlowLayout.OnTagClickListener() {

            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {
                //MyToast.showShort(((TextView)view.getTagView()).getText().toString()+"  "+view.isChecked());

                return false;
            }
        };
        tagFlowLayout.setOnTagClickListener(onTagClickListener);

        TagFlowLayout.OnSelectListener tagOnSelectListener = new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (tagFlowLayout == tflKeyNum) {//钥匙
                    if (selectPosSet.size() > 0) {

                    }
                }
            }
        };

        tagFlowLayout.setOnSelectListener(tagOnSelectListener);
    }


    /**
     * 拍照
     *
     * @param picList            连续拍照的数组数据
     * @param requestCodeCamera  拍照请求码
     * @param requestCodePreview 查看大图请求码
     * @author zealjiang
     * @time 2016/11/29 16:34
     */
    private void takePic(ArrayList<PictureItem> picList, int requestCodeCamera, int requestCodePreview) {

        String picPath = picList.get(curPicPos).getPicPath();
        if (TextUtils.isEmpty(picPath)) {
            userCamera(curPicPos, Constants.CAPTURE_TYPE_MULTI, picList, requestCodeCamera);
        } else {

            Intent intent = new Intent(context, PictureZoomActivity.class);
            intent.putExtra("url", picPath);
            intent.putExtra("showRecapture", true);
            intent.putExtra("pictureItems", picList);
            intent.putExtra("curPosition", curPicPos);
            intent.putExtra("taskid", ((BMWDetectMainActivity) getActivity()).getTaskid());
            intent.putStringArrayListExtra("highQualityPicIdArray", highQualityPicIdArray);
            startActivityForResult(intent, requestCodePreview);
        }
    }

    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position, int capture_type, ArrayList<PictureItem> listPic, int requestCode) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", taskId);
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍
        //高质量图片的ID
        intent.putStringArrayListExtra("highQualityPicIdArray", highQualityPicIdArray);
        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {
            intent.putExtra("picList", listPic);
            intent.putExtra("position", position);
        } else {
            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem newPic = new PictureItem();
            newPic.setPicId(listPic.get(position).getPicId());
            newPic.setPicName(listPic.get(position).getPicName());
            singleList.add(newPic);
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == PHOTO_REQUEST_DL) {//行驶证照片连续拍照
            if (null != data) {
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE, Constants.CAPTURE_TYPE_MULTI);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if (picList != null && picList.size() > 0) {
                    if (captureType == Constants.CAPTURE_TYPE_SINGLE) {
                        if (TextUtils.isEmpty(picList.get(0).getPicPath())) {
                            return;
                        }
                        pictureItemsDL.get(curPicPos).setPicPath(picList.get(0).getPicPath());
                    } else {
                        pictureItemsDL.clear();
                        pictureItemsDL.addAll(picList);
                    }
                    //刷新显示照片
                    refreshDLPic();
                }
            }
        }
    }

    /**
     * 刷新行驶证图片
     *
     * @author zealjiang
     * @time 2016/11/29 18:11
     */
    public void refreshDLPic() {
        //行驶证正本正面
        //如果车辆照片拍了，本地对应应该有相应的照片，处理这种情况
        if (FileUtils.isFileExists(PadSysApp.picDirPath + "18.jpg")) {
            pictureItemsDL.get(0).setPicPath(PadSysApp.picDirPath + "18.jpg");
        }
        pathDLFront = pictureItemsDL.get(0).getPicPath();
        picShow(pathDLFront, sdvDrivingLicenceFront);
        //行驶证正本背面
        if (FileUtils.isFileExists(PadSysApp.picDirPath + "19.jpg")) {
            pictureItemsDL.get(1).setPicPath(PadSysApp.picDirPath + "19.jpg");
        }
        pathDLBack = pictureItemsDL.get(1).getPicPath();
        picShow(pathDLBack, sdvDrivingLicenceBack);
        //行驶证副本正面
        if (FileUtils.isFileExists(PadSysApp.picDirPath + "20.jpg")) {
            pictureItemsDL.get(2).setPicPath(PadSysApp.picDirPath + "20.jpg");
        }
        pathDLViceFront = pictureItemsDL.get(2).getPicPath();
        picShow(pathDLViceFront, sdvDrivingLicenceViceFront);
        //行驶证副本背面
        if (FileUtils.isFileExists(PadSysApp.picDirPath + "24.jpg")) {
            pictureItemsDL.get(3).setPicPath(PadSysApp.picDirPath + "24.jpg");
        }
        pathDLViceBack = pictureItemsDL.get(3).getPicPath();
        picShow(pathDLViceBack, sdvDrivingLicenceViceBack);
    }

    /**
     * 显示图片
     *
     * @param filePath
     * @param simpleDraweeView
     */
    private void picShow(String filePath, SimpleDraweeView simpleDraweeView) {
        if (!filePath.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(filePath, false);
        }
        if (!TextUtils.isEmpty(filePath)) {
            if (filePath.startsWith("http") || FileUtils.isFileExists(filePath)) {
                frescoImageLoader.displayImage(simpleDraweeView, filePath);
            } else {
                simpleDraweeView.setImageResource(R.drawable.xiangji);
            }
        } else {
            simpleDraweeView.setImageResource(R.drawable.xiangji);
        }
    }

    /**
     * 行驶证照片是否为空，空返回true,不为空返回false
     *
     * @return
     */
    private boolean isDrivingLPicEmpty() {
        for (int i = 0; i < pictureItemsDL.size(); i++) {
            if (!TextUtils.isEmpty(pictureItemsDL.get(i).getPicPath())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 行驶证未见选中或无行驶证选中
     * 1:未见   2：无
     */
    private void drivingLicenceChecked(int drivinglicNo) {
        //删除照片
        ArrayList<String> deleteArray = new ArrayList<>();
        for (int i = 0; i < pictureItemsDL.size(); i++) {
            deleteArray.add(pictureItemsDL.get(i).getPicPath());
        }
        FrescoCacheHelper.clearMultiCaches(deleteArray, true);
        for (int i = 0; i < pictureItemsDL.size(); i++) {
            pictureItemsDL.get(i).setPicPath("");
        }
        //刷新行驶证照片
        refreshDLPic();

        //如果是修改进来的同时要删除掉详情里的行驶证照片
        deleteDrivngDetailPhoto();


        //设置为不会编辑
        sdvDrivingLicenceFront.setClickable(false);
        sdvDrivingLicenceBack.setClickable(false);
        sdvDrivingLicenceViceFront.setClickable(false);
        sdvDrivingLicenceViceBack.setClickable(false);


        //四张行驶证照片的文字颜色置灰
        tvDriLicFront.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvDriLicBack.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvDriLicViceFront.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvDriLicViceBack.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));

        if(drivinglicNo == 1){
            eventProcedureModel.setDrivingNoneIsSelect(true);
            eventProcedureModel.setDrivingLicNoIsSelect(false);
            EventBus.getDefault().post(eventProcedureModel);
        }else if(drivinglicNo == 2){
            eventProcedureModel.setDrivingLicNoIsSelect(true);
            eventProcedureModel.setDrivingNoneIsSelect(false);
            EventBus.getDefault().post(eventProcedureModel);
        }

    }

    /**
     * 删除详情里的行驶证图片
     *
     * @author zealjiang
     * @time 2016/12/20 14:09
     */
    private void deleteDrivngDetailPhoto() {
        if (taskDetailModel != null) {
            //图片
            List<TaskDetailModel.ProcedurePicListBean> procedurePicList = taskDetailModel.getProcedurePicList();
            if (procedurePicList != null) {
                for (int i = 0; i < procedurePicList.size(); i++) {
                    TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                    String picId = procedurePicBean.getPicID();
                    if (!TextUtils.isEmpty(picId)) {
                        if ("18".equals(picId)) {//行驶证正本正面
                            procedurePicList.remove(i);
                            i--;
                        } else if ("19".equals(picId)) {//行驶证正本背面
                            procedurePicList.remove(i);
                            i--;
                        } else if ("20".equals(picId)) {//行驶证副本正面
                            procedurePicList.remove(i);
                            i--;
                        } else if ("24".equals(picId)) {//行驶证副本背面
                            procedurePicList.remove(i);
                            i--;
                        }
                    }
                }

                taskDetailModel.setProcedurePicList(procedurePicList);
            }
        }
    }


    /**
     * 合格证数据 -- 设置品牌型号
     * 每次修改VIN时，再次查询合格证数据
     * 如果没取到合格证数据不清空旧数据
     */
    private void setCertificationData(CarInfoModel carInfoModel) {
        //通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
        if (null != carInfoModel) {
            //出厂日期
            //tvProductionDate.setText(dateSplit(StringUtils.null2Length0(carInfoModel.getProductionTime()), 7));

            //品牌型号
            String brandType = StringUtils.null2Length0(carInfoModel.getRecordBrand());//品牌型号（由品牌和型号组成，中间用逗号分开，服务器通过vin查询返回）
            submitModel.setBrandType(brandType);
            //将品牌型号保存到本地
            if (procedureModel == null) {
                procedureModel = new ProcedureModel();
            }
            procedureModel.setBrandType(brandType);
        }
    }


    /**
     * Created by 李波 on 2016/12/19.
     * 从车况检测点击其他信息界面时的 基本照片的状态判断
     *
     * @param isShowHint 没填写完是否显示提示信息，true显示，false不显示
     */
    public boolean checkBasePhoto(boolean isShowHint) {
        //如果行驶证可见，行驶证照片不可为空

        //行驶证  行驶证照片属性（ 0 有（默认）， 1 行驶证有瑕疵， 2 行驶证未见 ,3 无行驶证），单选
        if (configDriving) {
            //行驶证未见
            boolean isDrivingNone = cBoxDrivingNone.isChecked();
            //无行驶证
            boolean isDrivingLicNo = cbDrivingLicNo.isChecked();


            //如果行驶证可见且有行驶证，行驶证照片不可为空
            if (!isDrivingNone && !isDrivingLicNo) {

                //行驶证正本正面
                if (TextUtils.isEmpty(pictureItemsDL.get(0).getPicPath())) {
                    if (isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }

                //行驶证正本背面
                if (TextUtils.isEmpty(pictureItemsDL.get(1).getPicPath())) {
                    if (isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }
                //行驶证副本正面
                if (TextUtils.isEmpty(pictureItemsDL.get(2).getPicPath())) {
                    if (isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }
                //行驶证副本背面
                if (TextUtils.isEmpty(pictureItemsDL.get(3).getPicPath())) {
                    if (isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }
            }

        }
        return true;
    }


    /**
     * 检查并保存数据
     *
     * @param isCheck     提交时不可为空的项是否都已经填写是否检查
     * @param isNextCheck 必填项是否检查
     */
    public boolean checkAndSaveData(boolean isCheck, boolean isNextCheck, String hintHead) {
        hintHead = StringUtils.null2Length0(hintHead);
        //第一次领取一个任务时，本地数据库没有数据，服务器也没有返回详情数据时procedureModel为Null
        if (procedureModel == null) {
            procedureModel = new ProcedureModel();
        }
        if (this.getActivity() == null) {
            //关闭BMWDetectMainActivity
            AppManager.getAppManager().finishActivity(BMWDetectMainActivity.class);
            return false;
        }
        SubmitModel submitModel = ((BMWDetectMainActivity) this.getActivity()).getSubmitModel();
        if (submitModel == null) {
            return false;
        }


        //车牌号码
        if (configPlateNum) {
            String regionSimpleName = tvRegionSimpleName.getText().toString();
            String plateNo = etPlateNo.getText().toString();
            plateNo = regionSimpleName + plateNo;
            if (StringUtils.isEmpty(plateNo) || plateNo.length() == 1) {
                submitModel.setCarLicense("");//没填车牌号，提交到服务器前要把省份也去掉
            } else {
                submitModel.setCarLicense(plateNo.toUpperCase());
                procedureModel.setCarLicense(plateNo.toUpperCase());
            }

            if (isCheck && etPlateNo.isEnabled()) {
                if ((TextUtils.isEmpty(regionSimpleName) || (plateNo.trim().length() < 7))) {//如果没有填写车牌并且也没选择无车牌,车牌号为7/8位
                    MyToast.showShort(hintHead + "请正确填写车牌号码");
                    return false;
                }
            }

            if (cbCarPlateId.isChecked()) {
                //车牌有无（ 0 无车牌、 1 有车牌、 2 未悬挂 、3 与实车不符、 4 车牌未见）
                submitModel.setCarLicenseEx(4);
                procedureModel.setCarLicenseEx(4);

                //车牌有无（0无，1有），单选
                submitModel.setCarLicenseHave(0);
                procedureModel.setCarLicenseHave(0);

                procedureModel.setCarPlateNumNone(true);
            } else {
                //车牌有无（ 0 无车牌、 1 有车牌、 2 未悬挂 、3 与实车不符）
                submitModel.setCarLicenseEx(1);
                procedureModel.setCarLicenseEx(1);

                //车牌有无（0无，1有），单选
                submitModel.setCarLicenseHave(1);
                procedureModel.setCarLicenseHave(1);

                procedureModel.setCarPlateNumNone(false);
            }

        } else {
            submitModel.setCarLicense("");
            procedureModel.setCarLicense("");

            //车牌有无（ 0 无车牌、 1 有车牌、 2 未悬挂 、3 与实车不符）
            submitModel.setCarLicenseEx(1);
            procedureModel.setCarLicenseEx(1);

            //车牌有无（0无，1有），单选
            submitModel.setCarLicenseHave(1);
            procedureModel.setCarLicenseHave(1);
        }

        //车架号VIN
        if (configVin) {
            String vin = etVIN.getText().toString();
            if (isCheck || isNextCheck) {
                if (TextUtils.isEmpty(vin)) {
                    MyToast.showShort(hintHead + "请填写车架号VIN");
                    return false;
                }
            }
            boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
            if (!isValid && isNextCheck) {
                MyToast.showShort("根据VIN校验规则，可能存在填写错误，请仔细核对!");
                return false;
            }
            if (!isValid && isCheck) {
                MyToast.showShort("根据VIN校验规则，可能存在填写错误，请仔细核对!");
                return false;
            }
            submitModel.setVin(vin.toUpperCase());
            procedureModel.setVin(vin.toUpperCase());
        } else {
            submitModel.setVin("");
            procedureModel.setVin("");
        }

        //车身颜色
        if (configCarColor) {
            String carColor = tvCarColor.getText().toString();

            submitModel.setOtherColor(carColor);
            procedureModel.setOtherColor(carColor);
        }

        //行驶证  行驶证照片属性（ 0 有（默认）， 1 行驶证有瑕疵， 2 行驶证未见 ,3 无行驶证），单选
        if (configDriving) {
            //行驶证未见
            boolean isDrivingNone = cBoxDrivingNone.isChecked();
            int driving = 0;
            if (isDrivingNone) {
                driving = 2;
            }

            //无行驶证
            boolean isDrivingLicNo = cbDrivingLicNo.isChecked();
            if(isDrivingLicNo){
                driving = 3;
            }

            //如果行驶证可见，行驶证照片不可为空
            if (!isDrivingNone && !isDrivingLicNo && isCheck) {
                //行驶证正本正面
                if (TextUtils.isEmpty(pictureItemsDL.get(0).getPicPath())) {
                    MyToast.showShort(hintHead + "请拍摄行驶证正本正面");
                    return false;
                }

                //行驶证正本背面
                if (TextUtils.isEmpty(pictureItemsDL.get(1).getPicPath())) {
                    MyToast.showShort(hintHead + "请拍摄行驶证正本背面");
                    return false;
                }
                //行驶证副本正面
                if (TextUtils.isEmpty(pictureItemsDL.get(2).getPicPath())) {
                    MyToast.showShort(hintHead + "请拍摄行驶证副本正面");
                    return false;
                }
                //行驶证副本背面
                if (TextUtils.isEmpty(pictureItemsDL.get(3).getPicPath())) {
                    MyToast.showShort(hintHead + "请拍摄行驶证副本背面");
                    return false;
                }
            }

            submitModel.setDrivingLicenseProperty(driving);
            procedureModel.setDrivingLicenseProperty(driving);

            //判断是否上传删除的照片ID
            if (driving == 2 || driving == 3) {
                //判断详情中行驶证是否可见
                if (taskDetailModel != null) {
                    int drivingLicense = taskDetailModel.getBasic().getDrivingLicenseProperty();
                    if (drivingLicense != 2 && drivingLicense != 3) {
                        ArrayList<String> deleteArray = (ArrayList) submitModel.getDeletePicId();
                        if (deleteArray == null) {
                            deleteArray = new ArrayList<String>();
                        }
                        deleteArray.add("18");
                        deleteArray.add("19");
                        deleteArray.add("20");
                        deleteArray.add("24");
                        submitModel.setDeletePicId(deleteArray);
                    }
                }
            }


        } else {
            submitModel.setDrivingLicenseProperty(0);
            procedureModel.setDrivingLicenseProperty(0);
        }


        //登记日期
        if (configRegisterDate) {
            String registerDate = tvRegisterDate.getText().toString();
            if (isCheck && !cbRegisterDateNone.isChecked()) {
                if (TextUtils.isEmpty(registerDate)) {
                    MyToast.showShort(hintHead + "请填写登记日期");
                    return false;
                }
            }
            submitModel.setRecordDate(registerDate);
            procedureModel.setRecordDate(registerDate);

            if (cbRegisterDateNone.isChecked()) {
                procedureModel.setRegisterDateNone(true);
            } else {
                procedureModel.setRegisterDateNone(false);
            }
        } else {
            submitModel.setRecordDate("");
            procedureModel.setRecordDate("");
        }


        //出厂日期
        if (configFactoryDate) {
            String factoryDate = tvFactoryDate.getText().toString();
            if (isCheck) {
                if (TextUtils.isEmpty(factoryDate)) {
                    MyToast.showShort(hintHead + "请填写出厂日期");
                    return false;
                }
            }
            submitModel.setProductionTime(factoryDate);
            procedureModel.setProductionTime(factoryDate);
        } else {
            submitModel.setProductionTime("");
            procedureModel.setProductionTime("");
        }


        //发动机号
        if (configEngineId) {
            String engineId = etEngineId.getText().toString();
            if (isCheck) {
                if (TextUtils.isEmpty(engineId)) {
                    MyToast.showShort(hintHead + "请填写发动机号");
                    return false;
                }
            }
            submitModel.setEngineNum(engineId.toUpperCase());
            procedureModel.setEngineNum(engineId.toUpperCase());
        } else {
            submitModel.setEngineNum("");
            procedureModel.setEngineNum("");
        }


        Set<Integer> set;
        //钥匙   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configKey) {
            int keyNum;
            set = tflKeyNum.getSelectedList();
            if (set.size() == 0) {//一个也没选中
                keyNum = -1;
            } else {
                keyNum = set.iterator().next();
            }

            int isStd = -1;
            if (rbKeyStdYes.isChecked()) {
                isStd = 1;
            }
            if (rbKeyStdNo.isChecked()) {
                isStd = 0;
            }

            submitModel.setCarKeyDes(keyNum + "," + isStd);
            procedureModel.setCarKeyDes(keyNum + "," + isStd);

            if (isCheck) {
                if (keyNum == -1) {
                    MyToast.showShort(hintHead + "请填写钥匙数量");
                    return false;
                }else if(isStd == -1){
                    MyToast.showShort(hintHead + "请填写钥匙是否达标");
                    return false;
                }
            }
        } else {
            submitModel.setCarKeyDes("-1,-1");
            procedureModel.setCarKeyDes("-1,-1");
        }

        //车牌数量   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configCarPlateNum) {
            int plateNum;
            set = tflCarPlateNum.getSelectedList();
            if (set.size() == 0) {//一个也没选中
                plateNum = -1;
            } else {
                plateNum = set.iterator().next();
            }

            int isStd = -1;
            if (rbCarPlateNumStdYes.isChecked()) {
                isStd = 1;
            }
            if (rbCarPlateNumStdNo.isChecked()) {
                isStd = 0;
            }

            submitModel.setPlateCount(plateNum + "," + isStd);
            procedureModel.setPlateCount(plateNum + "," + isStd);

            if (isCheck) {
                if (plateNum == -1) {
                    MyToast.showShort(hintHead + "请填写车牌数量");
                    return false;
                }else if(isStd == -1){
                    MyToast.showShort(hintHead + "请填写车牌数量是否达标");
                    return false;
                }
            }
        } else {
            submitModel.setPlateCount("-1,-1");
            procedureModel.setPlateCount("-1,-1");
        }


        //维保记录可查   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configMaintainRecord) {

            int isYes = -1;
            if (rbMaiRecordYes.isChecked()) {
                isYes = 1;
            }
            if (rbMaiRecordNo.isChecked()) {
                isYes = 0;
            }

            submitModel.setRepairRecord(isYes);
            procedureModel.setRepairRecord(isYes);

            if (isCheck) {
                if (isYes == -1) {
                    MyToast.showShort(hintHead + "请选择维保记录可查");
                    return false;
                }
            }
        } else {
            submitModel.setRepairRecord(-1);
            procedureModel.setRepairRecord(-1);
        }

        //DMS无大事故记录   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configDMSAcciRecord) {

            int isYes = -1;
            if (rbDMSAcciRecordYes.isChecked()) {
                isYes = 1;
            }
            if (rbDMSAcciRecordNo.isChecked()) {
                isYes = 0;
            }

            submitModel.setAccidentRecord(isYes);
            procedureModel.setAccidentRecord(isYes);

            if (isCheck) {
                if (isYes == -1) {
                    MyToast.showShort(hintHead + "请选择DMS无大事故记录");
                    return false;
                }
            }
        } else {
            submitModel.setAccidentRecord(-1);
            procedureModel.setAccidentRecord(-1);
        }


        //事故已记录已维修   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configAcciMaintained) {

            int isYes = -1;
            if (rbAcciMaintainYes.isChecked()) {
                isYes = 1;
            }
            if (rbAcciMaintainNo.isChecked()) {
                isYes = 0;
            }

            submitModel.setAccidentRepair(isYes);
            procedureModel.setAccidentRepair(isYes);

            if (isCheck) {
                if (isYes == -1) {
                    MyToast.showShort(hintHead + "请选择事故已记录已维修");
                    return false;
                }
            }
        } else {
            submitModel.setAccidentRepair(-1);
            procedureModel.setAccidentRepair(-1);
        }


        //无未结算事故维修记录   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configAcciMaintainedPay) {
            int isYes = -1;
            if (rbAcciNoPayYes.isChecked()) {
                isYes = 1;
            }
            if (rbAcciNoPayNo.isChecked()) {
                isYes = 0;
            }

            submitModel.setSettleRecord(isYes);
            procedureModel.setSettleRecord(isYes);

            if (isCheck) {
                if (isYes == -1) {
                    MyToast.showShort(hintHead + "请选择无未结算事故维修记录");
                    return false;
                }
            }
        } else {
            submitModel.setSettleRecord(-1);
            procedureModel.setSettleRecord(-1);
        }


        //技术召回   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configTechnicRecall) {
            int isYes = -1;
            if (rbTechnicRecallYes.isChecked()) {
                isYes = 1;
            }
            if (rbTechnicRecallNo.isChecked()) {
                isYes = 0;
            }

            submitModel.setIsRecall(isYes);
            procedureModel.setIsRecall(isYes);

            if (isCheck) {
                if (isYes == -1) {
                    MyToast.showShort(hintHead + "请选择技术召回");
                    return false;
                }
            }
        } else {
            submitModel.setIsRecall(-1);
            procedureModel.setIsRecall(-1);
        }


        //燃油表无报警   是/合格/达标  这种值是1，否/不合格/不达标值是0
        if (configOilAlarm) {
            int isYes = -1;
            if (rbOilAlarmYes.isChecked()) {
                isYes = 1;
            }
            if (rbOilAlarmNo.isChecked()) {
                isYes = 0;
            }

            submitModel.setPetrolAlarm(isYes);
            procedureModel.setPetrolAlarm(isYes);

            if (isCheck) {
                if (isYes == -1) {
                    MyToast.showShort(hintHead + "请选择燃油表无报警");
                    return false;
                }
            }
        } else {
            submitModel.setPetrolAlarm(-1);
            procedureModel.setPetrolAlarm(-1);
        }


        //里程表显示
        if (configMileage) {
            int mileage = -1;
            String smileage = etMileage.getText().toString();
            if (!StringUtils.isEmpty(smileage)) {
                mileage = Integer.valueOf(smileage);
            }
            submitModel.setMileage(mileage);
            procedureModel.setMileage(mileage);
            if (isCheck) {
                if (StringUtils.isEmpty(smileage)) {
                    MyToast.showShort(hintHead + "请填写里程表显示");
                    return false;
                }else if(mileage < 100){
                    MyToast.showShort(hintHead + "里程表显示不能小于100");
                }
            }
        } else {
            submitModel.setMileage(-1);
            procedureModel.setMileage(-1);
        }


        //下次保养天数
        if (configNextMaintDays) {
            int days = -1;
            String sDays = etNextMaintainDays.getText().toString();
            if (!StringUtils.isEmpty(sDays)) {
                days = Integer.valueOf(sDays);
            }
            submitModel.setNextRepairDays(days);
            procedureModel.setNextRepairDays(days);

            if (isCheck) {
                if (StringUtils.isEmpty(sDays)) {
                    MyToast.showShort(hintHead + "请填写下次保养天数");
                    return false;
                }
            }
        } else {
            submitModel.setNextRepairDays(-1);
            procedureModel.setNextRepairDays(-1);
        }


        //下次保养距离
        if (configNextMaintMileage) {
            int mileage = -1;
            String smileage = etNextMaintainMileage.getText().toString();
            if (!StringUtils.isEmpty(smileage)) {
                mileage = Integer.valueOf(smileage);
            }
            submitModel.setNextRepairMileage(mileage);
            procedureModel.setNextRepairMileage(mileage);

            if (isCheck) {
                if (StringUtils.isEmpty(smileage)) {
                    MyToast.showShort(hintHead + "请填写下次保养距离");
                    return false;
                }
            }
        } else {
            submitModel.setNextRepairMileage(-1);
            procedureModel.setNextRepairMileage(-1);
        }


        //有无此附件   是/合格/达标  这种值是1，否/不合格/不达标值是0  多选
        if (configAccessory) {
            set = tflAccessory.getSelectedList();
            if (set.size() == 0) {//一个也没选中
            } else {
                //年检标
                submitModel.setStdInspection(0);
                procedureModel.setStdInspection(0);
                //交强险标
                submitModel.setCoyInsurance(0);
                procedureModel.setCoyInsurance(0);
                //用户手册
                submitModel.setUserManual(0);
                procedureModel.setUserManual(0);
                //随车说明书
                submitModel.setBordbuch(0);
                procedureModel.setBordbuch(0);
                //三包凭证
                submitModel.setThreeGuarantees(0);
                procedureModel.setThreeGuarantees(0);
                //保修服务手册
                submitModel.setGuaranteeHandbook(0);
                procedureModel.setGuaranteeHandbook(0);
                //救援电话
                submitModel.setRescueCall(0);
                procedureModel.setRescueCall(0);
                //授权经销商联系手册
                submitModel.setContactHandbook(0);
                procedureModel.setContactHandbook(0);

                //得到选中的标签对应的ID
                int count = tflAccessory.getAdapter().getCount();
                for (int i = 0; i < count; i++) {
                    if (set.contains(i)) {
                        String tagName = (String) tflAccessory.getAdapter().getItem(i);
                        if ("年检标".equals(tagName)) {
                            submitModel.setStdInspection(1);
                            procedureModel.setStdInspection(1);
                        } else if ("交强险标".equals(tagName)) {
                            submitModel.setCoyInsurance(1);
                            procedureModel.setCoyInsurance(1);
                        } else if ("用户手册".equals(tagName)) {
                            submitModel.setUserManual(1);
                            procedureModel.setUserManual(1);
                        } else if ("随车说明书".equals(tagName)) {
                            submitModel.setBordbuch(1);
                            procedureModel.setBordbuch(1);
                        } else if ("三包凭证".equals(tagName)) {
                            submitModel.setThreeGuarantees(1);
                            procedureModel.setThreeGuarantees(1);
                        } else if ("保修服务手册".equals(tagName)) {
                            submitModel.setGuaranteeHandbook(1);
                            procedureModel.setGuaranteeHandbook(1);
                        } else if ("救援电话".equals(tagName)) {
                            submitModel.setRescueCall(1);
                            procedureModel.setRescueCall(1);
                        } else if ("授权经销商联系手册".equals(tagName)) {
                            submitModel.setContactHandbook(1);
                            procedureModel.setContactHandbook(1);
                        }
                    }
                }

            }
        }

        String json = new Gson().toJson(procedureModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_PROCEDURE, taskId, getUser().getUserId(), json);

        return true;
    }

    /**
     * 修改VIN 后重新设置，并根据新的vin 重新获取合格证的数据
     * @param vin
     */
    public void onVinClick(String vin) {
        String oldVin = etVIN.getText().toString();
        etVIN.setText(vin);
        submitModel.setVin(vin);
        if (!vin.equals(oldVin)) {
            carDataByVinPresenter.requestCarInfo(vin); //获取合格证数据
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventModel eventModel) {
        if(eventModel.getName().equals("productDate")){
            tvFactoryDate.setText(eventModel.getValue());
        }
    }

    /**
     *  修改 vin 成功
     */
    @Override
    public void requestVinCheckedSucceed(ResponseJson<VinCheckedModel> response, MyUniversalDialog myUniversalDialog, String vin) {
        onVinClick(vin);

        myUniversalDialog.cancel();

        VinCheckedModel memberValue = response.getMemberValue();
        int isAlertAccdient = memberValue.getIsAlertAccdient();
        String accdientAlertMsg = memberValue.getAccdientAlertMsg();
        if (isAlertAccdient == 1) { //判断是否是大事故车，是就提醒下
            showAlertAccident(accdientAlertMsg);
        }
    }

    @Override
    public void requestVinCheckedFailed(String message, MyUniversalDialog myUniversalDialog) {
        View view = myUniversalDialog.getView();
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.VISIBLE);
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }

    @Override
    public void succeedCarInfo(CarInfoModel carInfoModel) {

        //设置合格证数据
        setCertificationData(carInfoModel);
    }


    @Override
    public void onResume() {
        super.onResume();
        //刷新照片
        refreshDLPic();

        //如果手动从文件夹中删除了图片,将删除的图片的path置空
        if (pictureItemsDL != null && pictureItemsDL.size() > 0) {
            for (int i = 0; i < pictureItemsDL.size(); i++) {
                if (!FileUtils.isFileExists(pictureItemsDL.get(i).getPicPath())&&!pictureItemsDL.get(i).getPicPath().startsWith("http")) {
                    pictureItemsDL.get(i).setPicPath("");
                }
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e("ProcedureCarFragment", "onPause");
        checkAndSaveData(false, false, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("ProcedureCarFragment", "onDestroy" + this);
        EventBus.getDefault().unregister(this);
        checkAndSaveData(false, false, "");
    }

}
