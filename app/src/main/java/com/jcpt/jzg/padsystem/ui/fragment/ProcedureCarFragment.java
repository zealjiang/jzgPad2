package com.jcpt.jzg.padsystem.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jcpt.jzg.padsystem.R;
import com.jcpt.jzg.padsystem.adapter.MyRegionSimpleNameTagStringAdapter;
import com.jcpt.jzg.padsystem.adapter.MyTagStringAdapter;
import com.jcpt.jzg.padsystem.app.AppManager;
import com.jcpt.jzg.padsystem.app.PadSysApp;
import com.jcpt.jzg.padsystem.base.BaseFragment;
import com.jcpt.jzg.padsystem.camera.CameraActivity;
import com.jcpt.jzg.padsystem.db.DBBase;
import com.jcpt.jzg.padsystem.db.DBManager;
import com.jcpt.jzg.padsystem.dialog.DayPickerDialog;
import com.jcpt.jzg.padsystem.dialog.ShowMsgDialog;
import com.jcpt.jzg.padsystem.global.Constants;
import com.jcpt.jzg.padsystem.http.ResponseJson;
import com.jcpt.jzg.padsystem.interfaces.AfterTextChanged;
import com.jcpt.jzg.padsystem.mvpview.ICarInfoInterface;
import com.jcpt.jzg.padsystem.mvpview.IProvinceCityUnique;
import com.jcpt.jzg.padsystem.mvpview.IRepairLogListener;
import com.jcpt.jzg.padsystem.mvpview.IVInChecked;
import com.jcpt.jzg.padsystem.presenter.ActiveRepairLogPresenter;
import com.jcpt.jzg.padsystem.presenter.CarDataByVinPresenter;
import com.jcpt.jzg.padsystem.presenter.ProvinceCityUniquePresenter;
import com.jcpt.jzg.padsystem.presenter.VinCheckedPresenter;
import com.jcpt.jzg.padsystem.ui.activity.CarTypeSelectActivity;
import com.jcpt.jzg.padsystem.ui.activity.DetectMainActivity;
import com.jcpt.jzg.padsystem.ui.activity.DrivingLicenceIdentifyActivity;
import com.jcpt.jzg.padsystem.ui.activity.PictureZoomActivity;
import com.jcpt.jzg.padsystem.ui.activity.ProvinceCitySelectActivity;
import com.jcpt.jzg.padsystem.utils.ACache;
import com.jcpt.jzg.padsystem.utils.CarVINCheck;
import com.jcpt.jzg.padsystem.utils.FrescoCacheHelper;
import com.jcpt.jzg.padsystem.utils.FrescoImageLoader;
import com.jcpt.jzg.padsystem.utils.InputLowerToUpper;
import com.jcpt.jzg.padsystem.utils.InputUtil;
import com.jcpt.jzg.padsystem.utils.LicencePlateAddress;
import com.jcpt.jzg.padsystem.utils.LogUtil;
import com.jcpt.jzg.padsystem.utils.MyToast;
import com.jcpt.jzg.padsystem.utils.UIUtils;
import com.jcpt.jzg.padsystem.view.QNumberPicker;
import com.jcpt.jzg.padsystem.vo.CarInfoModel;
import com.jcpt.jzg.padsystem.vo.DrivingLicenceModel;
import com.jcpt.jzg.padsystem.vo.EventModel;
import com.jcpt.jzg.padsystem.vo.EventProcedureModel;
import com.jcpt.jzg.padsystem.vo.EventProcedurePhoto;
import com.jcpt.jzg.padsystem.vo.LocalCarConfigModel;
import com.jcpt.jzg.padsystem.vo.LocalProvinceCityModel;
import com.jcpt.jzg.padsystem.vo.ProcedureModel;
import com.jcpt.jzg.padsystem.vo.ProvinceCityUniqueModel;
import com.jcpt.jzg.padsystem.vo.Repairlog;
import com.jcpt.jzg.padsystem.vo.SubmitModel;
import com.jcpt.jzg.padsystem.vo.TaskDetailModel;
import com.jcpt.jzg.padsystem.vo.TaskItem;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.blankj.utilcode.utils.StringUtils.null2Length0;
import static com.jcpt.jzg.padsystem.R.id.tv_register_date;
import static com.jcpt.jzg.padsystem.app.PadSysApp.getUser;

/**
 * 对应的初始化对象名【ProcedureList】："ProcedureList":["81","82","87","88","89","90","91","92","93","94","96","97","98","99","100","101","102","103","105","106","107","109","110","111","112"]
 * 若ProcedureList中去除了某个id，那么该id对应的版块隐藏，同时不校验该版块的值
 * <p>
 * 81 行驶证照片
 * 82 登记证照片
 * 83 机动车所有人
 * 84 证件类型
 * 85 车主证件号
 * 86 登记证与车主证件号
 * 87 登记日期
 * 88 初登地区
 * 89 车牌号码
 * 90 车辆型号
 * 91 车架号VIN
 * 92 发动机号
 * 93 燃料种类
 * 94 核定载客数
 * 95 获得方式
 * 96 车身颜色
 * 97 进口国产
 * 98 排量
 * 99 轮胎规格
 * 100 使用性质
 * 101 出厂日期
 * 102 过户次数
 * 103 最后过户日期
 * 104 曾使用方
 * 105 现使用方
 * 106 交强险保单
 * 107 交强险到期日
 * 108 交强险所在地
 * 109 原车发票
 * 110 其它票证
 * 111 登记证附加信息
 * 112 年检有效期
 * 276 上牌地区
 * 277 备用钥匙
 * 297 排放标准
 * <p>
 * 手续照片保存名字               照片对应板块
 * 16               铭牌
 * 17               车架 VIN
 * 18               行驶证正本正面
 * 19               行驶证正本背面
 * 20               行驶证副本正面
 * 21               登记证 1-2 页照片
 * 22               登记证 3-4 页照片
 * 23               更多登记证照片1
 * 28               更多登记证照片2
 * 24               行驶证副本背面
 *  
 * 必填项为
 * zealjiang
 *  车辆手续
 */
public class ProcedureCarFragment extends BaseFragment implements IProvinceCityUnique, IRepairLogListener, ICarInfoInterface, IVInChecked{

    private ProcedureCarFragment self;

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

    //识别
    @BindView(R.id.tv_identification)
    TextView tvIdentification;

    //行驶证有瑕疵
    @BindView(R.id.cBoxXSZYXC)
    CheckBox cBoxDrivingLicenceFlaw;

    //行驶证未见
    @BindView(R.id.cBoxXSZWJ)
    CheckBox cBoxDrivingNone;

    @BindView(R.id.v_line_register)
    View vLineRegister;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;
    //登记证1-2页照片
    @BindView(R.id.sdv_register_1)
    SimpleDraweeView sdvRegister1;
    //文字
    @BindView(R.id.tvRegister1)
    TextView tvRegister1;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    //登记证3-4页照片
    @BindView(R.id.sdv_register_3)
    SimpleDraweeView sdvRegister3;
    @BindView(R.id.tvRegister3)
    TextView tvRegister3;

    //更多登记证照片1
    @BindView(R.id.sdv_register_more)
    SimpleDraweeView sdvRegisterMore;
    @BindView(R.id.tvRegisterMore)
    TextView tvRegisterMore;

    //更多登记证照片2
    @BindView(R.id.llRegisterMore2)
    LinearLayout llRegisterMore2;
    @BindView(R.id.sdv_register_more2)
    SimpleDraweeView sdvRegisterMore2;
    @BindView(R.id.tvRegisterMore2)
    TextView tvRegisterMore2;

    //登记证有瑕疵
    @BindView(R.id.cBoxDJZYXC)
    CheckBox cBoxRegisterFlaw;
    //登记证未见
    @BindView(R.id.cBoxDJZWJ)
    CheckBox cBoxRegisterNone;

    //机动车所有人
    @BindView(R.id.ll_vehicle_owener)
    LinearLayout llVehicleOwener;
    @BindView(R.id.view_line_vehicle_owener)
    View vLineVehicleOwener;
    @BindView(R.id.et_vehicle_owener)
    EditText etVehicleOwener;
    @BindView(R.id.tv_vehicle_owener)
    TextView tvVehicleOwener;

    //证件类型
    @BindView(R.id.ll_card)
    LinearLayout llCard;
    @BindView(R.id.v_line_card)
    View vLineCard;
    @BindView(R.id.tv_card)
    TextView tvCard;


    //车主证件号
    @BindView(R.id.ll_owner_id)
    LinearLayout llOwnerId;
    @BindView(R.id.v_line_owner_id)
    View vLineOwnerId;
    @BindView(R.id.et_owner_id)
    EditText etOwnerId;
    @BindView(R.id.tv_owner_id)
    TextView tvOwnerId;

    //登记日期
    @BindView(R.id.view_line_register_date)
    View vLineRegisterDate;
    @BindView(R.id.ll_register_date)
    LinearLayout llRegisterDate;
    @BindView(tv_register_date)
    TextView tvRegisterDate;
    @BindView(R.id.tv_register_date_name)
    TextView tvRegisterDateName;

    //初登地区
    @BindView(R.id.view_line_register_region)
    View vLineRegisterRegion;
    @BindView(R.id.ll_register_region)
    LinearLayout llRegisterRegion;
    @BindView(R.id.tv_register_region)
    TextView tvRegisterRegion;
    @BindView(R.id.tv_register_region_name)
    TextView tvRegisterRegionName;

    //上牌地区
    @BindView(R.id.view_line_plate_region)
    View vLinePlateRegion;
    @BindView(R.id.ll_plate_region)
    LinearLayout llPlateRegion;
    @BindView(R.id.tv_plate_region)
    TextView tvPlateRegion;

    //车牌号码
    @BindView(R.id.v_line_plate_id)
    View vLinePlateId;
    @BindView(R.id.ll_plate_id)
    LinearLayout llPlateId;
    @BindView(R.id.tv_region_simple_name)
    TextView tvRegionSimpleName;
    @BindView(R.id.et_car_plate_no)
    EditText etPlateNo;

    //车辆型号
    @BindView(R.id.v_line_car_type)
    View vLineCarType;
    @BindView(R.id.ll_car_type)
    LinearLayout llCarType;
    @BindView(R.id.et_car_type)
    EditText etCarType;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.tv_car_color)
    TextView tvCarColor;

    //发动机号
    @BindView(R.id.v_line_engine_id)
    View vLineEngineId;
    @BindView(R.id.ll_engine_id)
    LinearLayout llEngineId;
    @BindView(R.id.et_engine_id)
    EditText etEngineId;
    @BindView(R.id.tv_engine_id)
    TextView tvEngineId;

    //车架号VIN
    @BindView(R.id.v_line_vin)
    View vLineVin;
    @BindView(R.id.ll_vin)
    LinearLayout llVin;
    @BindView(R.id.et_vin)
    TextView etVIN;

    //燃料种类
    @BindView(R.id.v_line_fuel_type)
    View vLineFuelType;
    @BindView(R.id.ll_fuel_type)
    LinearLayout llFuelType;
    @BindView(R.id.tv_fuel_type)
    TextView tvFuelType;

    //核定载客数量
    @BindView(R.id.v_line_load_num)
    View vLineLoadNum;
    @BindView(R.id.ll_load_num)
    LinearLayout llLoadNum;

    //获得方式
    @BindView(R.id.v_line_obtain_way)
    View vLineObtainWay;
    @BindView(R.id.ll_obtain_way)
    LinearLayout llObtainWay;
    @BindView(R.id.tv_obtain_way)
    TextView tvObtainWay;


    //车身颜色
    @BindView(R.id.v_line_car_color)
    View vLineCarColor;
    @BindView(R.id.ll_car_color)
    LinearLayout llCarColor;
    @BindView(R.id.et_car_color)
    EditText etCarColor;

    //进口国产
    @BindView(R.id.v_line_is_made_in_china)
    View vLineIsMadeInChina;
    @BindView(R.id.ll_is_made_in_china)
    LinearLayout llIsMadeInChina;

    //排量
    @BindView(R.id.v_line_displacement)
    View vLineDisplacement;
    @BindView(R.id.ll_displacement)
    LinearLayout llDisplacement;
    @BindView(R.id.et_displacement)
    EditText etDisplacement;

    //轮胎规格
    @BindView(R.id.v_line_tire_model)
    View vLineTireModel;
    @BindView(R.id.ll_tire_model)
    LinearLayout llTireModel;
    @BindView(R.id.et_tire_model)
    EditText etTireModel;
    @BindView(R.id.tv_tire_model)
    TextView tvTireModel;

    //使用性质
    @BindView(R.id.v_line_use_nature)
    View vLineUseNature;
    @BindView(R.id.ll_use_nature)
    LinearLayout llUseNature;
    @BindView(R.id.tv_use_nature)
    TextView tvUseNature;

    //出厂日期
    @BindView(R.id.v_line_product_date)
    View vLineProductDate;
    @BindView(R.id.ll_product_date)
    LinearLayout llProductDate;
    @BindView(R.id.tv_production_date)
    TextView tvProductionDate;

    //过户次数
    @BindView(R.id.v_line_transfer_num)
    View vLineTransferNum;
    @BindView(R.id.ll_transfer_num)
    LinearLayout llTransferNum;
    @BindView(R.id.et_transfer_num)
    EditText etTransferNum;
    @BindView(R.id.tv_transfer_num)
    TextView tvTransferNum;

    //最后过户日期
    @BindView(R.id.v_line_last_transfer_date)
    View vLineLastTransferDate;
    @BindView(R.id.ll_last_transfer_date)
    LinearLayout llLastTransferDate;
    @BindView(R.id.tv_last_transfer_date)
    TextView tvLastTransferDate;
    @BindView(R.id.tv_last_transfer_date_name)
    TextView tvLastTransferDateName;


    //曾使用方
    @BindView(R.id.v_line_old_user)
    View vLineOldUser;
    @BindView(R.id.ll_old_user)
    LinearLayout llOldUser;
    @BindView(R.id.tv_old_user)
    TextView tvOldUser;

    //现使用方
    @BindView(R.id.v_line_cur_user)
    View vLineCurUser;
    @BindView(R.id.ll_cur_user)
    LinearLayout llCurUser;
    @BindView(R.id.tv_cur_user)
    TextView tvCurUser;

    //钥匙
    @BindView(R.id.v_line_extra_key)
    View vLineExtraKey;
    @BindView(R.id.ll_extra_key)
    LinearLayout llExtraKey;

    //排放标准
    @BindView(R.id.v_line_emission_standard)
    View vLineEmissionStandard;
    @BindView(R.id.ll_emission_standard)
    LinearLayout llEmissionStandard;
    @BindView(R.id.llEmissionStdRef)
    LinearLayout llEmissionStdRef;
    @BindView(R.id.tvEmissionStandard)
    TextView tvEmissionStandard;

    //交强险保险单
    @BindView(R.id.v_line_insurance_bill)
    View vLineInsuranceBill;
    @BindView(R.id.ll_insurance_bill)
    LinearLayout llInsuranceBill;

    //交强险到期日
    @BindView(R.id.v_line_insurance_exp_date)
    View vLineInsuranceExpDate;
    @BindView(R.id.tv_insurance_exp_date)
    TextView tvInsuranceExpDate;
    @BindView(R.id.ll_insurance_exp_date)
    LinearLayout llInsuranceExpDate;
    @BindView(R.id.tv_compulsory_insurance_expire_date)
    TextView tvComInsuranceExDate;

    //交强险所在地
    @BindView(R.id.v_line_insurance_address)
    View vLineInsuranceAddress;
    @BindView(R.id.tv_insurance_address)
    TextView tvInsuranceAddress;
    @BindView(R.id.ll_insurance_address)
    LinearLayout llInsuranceAddress;
    @BindView(R.id.tv_compulsory_insurance_address)
    TextView tvComInsuranceAddress;

    //原车发票
    @BindView(R.id.v_line_original_bill)
    View vLineOriginalBill;
    @BindView(R.id.tv_original_bill)
    TextView tvOriginalBill;
    @BindView(R.id.ll_original_bill)
    LinearLayout llOriginalBill;
    @BindView(R.id.et_bill_money)
    EditText etBillMoney;
    @BindView(R.id.tv_bill_date)
    TextView tvBillDate;

    //其它票证
    @BindView(R.id.v_line_other_bill)
    View vLineOtherBill;
    @BindView(R.id.ll_other_bill)
    LinearLayout llOtherBill;

    //登记证附加信息
    @BindView(R.id.v_line_register_addition_info)
    View vLineRegisterAdditionInfo;
    @BindView(R.id.ll_register_addition_info)
    LinearLayout llRegisterAdditionInfo;
    @BindView(R.id.et_register_addition_info)
    EditText etRegisterAdditionInfo;
    @BindView(R.id.tv_register_addition_info)
    TextView tvRegisterAdditionInfo;

    //年检有效期
    @BindView(R.id.v_line_annual_date)
    View vLineAnnualDate;
    @BindView(R.id.ll_annual_date)
    LinearLayout llAnnualDate;
    @BindView(R.id.tv_annual_inspection)
    TextView tvAnnualInspection;

    //证件类型
    @BindView(R.id.tflCardType)
    TagFlowLayout tflCardType;
    //车主证件号
    @BindView(R.id.tflCheZhuZhengJian)
    TagFlowLayout tflCheZhuZhengJian;
    //车牌号码
    @BindView(R.id.tflChepaihaoma)
    TagFlowLayout tflChepaihaoma;
    //燃料种类
    @BindView(R.id.tflFuelType)
    TagFlowLayout tflFuelType;
    //核定载客量
    @BindView(R.id.tflBusload)
    TagFlowLayout tflBusload;
    @BindView(R.id.tvBusLoad)
    TextView tvBusLoad;
    //获得方式
    @BindView(R.id.tflHuodeFangshi)
    TagFlowLayout tflHuodeFangshi;
    //车身颜色
    @BindView(R.id.tflCarColor)
    TagFlowLayout tflCarColor;
    //进口国产
    @BindView(R.id.tflJinkouGuochan)
    TagFlowLayout tflJinkouGuochan;
    //排量
    @BindView(R.id.tflPailiang)
    TagFlowLayout tflPailiang;
    //使用性质
    @BindView(R.id.tflShiyongxingzhi)
    TagFlowLayout tflShiyongxingzhi;
    //现使用方
    @BindView(R.id.tflCengshiyongfang)
    TagFlowLayout tflCengshiyongfang;
    //现使用方
    @BindView(R.id.tflXianshiyongfang)
    TagFlowLayout tflXianshiyongfang;
    //备用钥匙
    @BindView(R.id.tflExtraKey)
    TagFlowLayout tflExtraKey;
    //排放标准
    @BindView(R.id.tflEmissionStandard)
    TagFlowLayout tflEmissionStandard;
    //交强险保单
    @BindView(R.id.tflJiaoqiangxianbaodan)
    TagFlowLayout tflJiaoqiangxianbaodan;
    //原车发票
    @BindView(R.id.tflYuanchefapiao)
    TagFlowLayout tflYuanchefapiao;
    //其它票证
    @BindView(R.id.tflQitapiaozheng)
    TagFlowLayout tflQitapiaozheng;
    //登记证附加信息
    @BindView(R.id.tflDJZFujiaxinxi)
    TagFlowLayout tflDJZFujiaxinxi;

    MyTagStringAdapter displacementAdapter;


    String [] tflCardTypeLists = {"身份证","军官证","护照","组织机构代码证","车主证件未见"};
    String [] tflCheZhuZhengJianLists = {"与车主证件不一致"};
    String [] tflChepaihaomaLists = {"未落户","未悬挂","与实车不符"};
    String [] tflFuelTypeLists = {"汽油","柴油","混动","天然气","纯电动"};
    String [] tflBusloadLists = {"2","4","5","6","7","8","9","9以上"};
    String [] tflHuodeFangshiLists = {"购买","仲裁裁判","继承","赠与","协议抵偿债务","中奖",
            "资产重组","资产整体买卖","调拨","境外自带","法院调解、裁定、判决"};
    String [] tflCarColorLists = {"白","灰","红","粉","黄","蓝", "绿","紫","棕","黑","双色","其他"};
    String [] tflJinkouGuochanLists = {"国产","进口"};
    String [] tflPailiangLists = {"T"};
    String [] tflShiyongxingzhiLists = {"非营运","营转非","出租营转非","营运","租赁","警用","消防",
            "救护","工程抢险","货运","公路客运","公交客运","出租客运","旅游客运"};
    String [] tflCengshiyongfangLists = {"仅个人记录","有单位记录","有出租车记录","有汽车租赁公司记录","有汽车销售（服务）公司记录"};
    String [] tflXianshiyongfangLists = {"仅个人记录","有单位记录","有出租车记录","有汽车租赁公司记录","有汽车销售（服务）公司记录"};
    String [] tflExtraKeyLists = {"0","1","2及以上"};
    String [] tflEmissionStandardLists = {"国二及以下","国三","国四","国五","无法判断"};
    String [] tflJiaoqiangxianbaodanLists = {"正常","未见","被保险人与车主不一致"};
    String [] tflYuanchefapiaoLists = {"无工商章","未见"};
    String [] tflQitapiaozhengLists = {"过户票","进口关单","购置税完税证明（征税）","购置税完税证明（免税）"};
    ArrayList<String> tflQitapiaozhengList = new ArrayList<>();
    String [] tflDJZFujiaxinxiLists = {"正在抵押","发动机号变更","重打车架号","登记证补领","颜色变更"};


    private final int REQUEST_CODE_CAR_LICENSE = 100;//行驶证识别
    private final int REQUEST_CODE_CAR_TYPE_SELECT = 200;

    private DatePickerDialog dialog;
    private Calendar calendar;

    private DayPickerDialog dayPickerDialog;

    private final int REQUEST_PROVINCE_CITY_CODE = 101;
    private final int REQUEST_PROVINCE_CITY_CODE_PLATE = 102;//上牌地区
    private final int REQUEST_PROVINCE_CITY_CODE_INSURANCE_ADDRESS = 103;//交强险所在地
    private EventProcedureModel eventProcedureModel;
    private DetectionWrapper detectionWrapper;
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
    private boolean configCarType = false;//车辆型号 90
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
    private boolean configAnnualDate = false;//年检有效 112
    private boolean configPlateRegion= false;//上牌地区 276
    private boolean configExtraKey= false;//备用钥匙 277
    private boolean configEmissionStandard= false;//排放标准 297

    private SubmitModel submitModel;

    //连续拍照图片列表
    private ArrayList<PictureItem> pictureItemsDL;//行驶证照片
    private ArrayList<PictureItem> pictureItemsDLRegister;//登记证照片
    private ArrayList<PictureItem> pictureItemsDLRegisterMore;//登记证照片 更多照片
    private PictureItem registerMorePI;//登记证更多照片1
    private PictureItem registerMorePI2;//登记证更多照片2
    private final int PHOTO_REQUEST_DL = 10;
    private final int PHOTO_BIG_PHOTO_DL = 11;
    private final int PHOTO_REQUEST_DLREG = 12;
    private final int PHOTO_BIG_PHOTO_DLREG = 13;
    private final int PHOTO_RL_MORE_SINGLE = 14;//更多登记证照片1拍摄请求码
    private final int PHOTO_RL_MORE_SINGLE2 = 16;//更多登记证照片2拍摄请求码
    private final int PHOTO_RL_MORE_BIG = 15;//更多登记证照片1查看大图请求码
    private final int PHOTO_RL_MORE_BIG2 = 17;//更多登记证照片2查看大图请求码
    private int curPicPos = 0;
    private String pathDLFront;//行驶证正本正面本地图片地址
    private String pathDLBack;//行驶证正本背面本地图片地址
    private String pathDLViceFront;//行驶证副本正面本地图片地址
    private String pathDLViceBack;//行驶证副本背面本地图片地址
    private String pathDLRegister1;//登记证1-2页照片本地图片地址
    private String pathDLRegister3;//登记证3-4页照片本地图片地址
    private String pathDLRegisterM;//更多登记证照片1本地图片地址
    private String pathDLRegisterM2;//更多登记证照片2本地图片地址
    private String taskId;
    public  ProcedureModel procedureModel;
    private TaskDetailModel taskDetailModel;//修改返回的详情数据
    private TagFlowLayout.OnSelectListener tagOnSelectListener;

    private ArrayList<String> highQualityPicIdArray = new ArrayList<>();

    private Handler handler = new Handler();

    private ProvinceCityUniquePresenter provinceCityUniquePresenter;
    //维保记录
    private ActiveRepairLogPresenter activeRepairLogPresenter;
    //通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
    private CarDataByVinPresenter carDataByVinPresenter;
    private FrescoImageLoader frescoImageLoader;


    private boolean isJustGetCertificateData = false;//无详情，进入手续信息，行驶证、登记证均可见，带入合格证数据，且均可编辑；有详情时，不带入合格证数据，可编辑
    private CarInfoModel carInfoModel;//合格证数据
    private boolean isSearchPlateRegion = true;//是否根据车牌号码查询上牌地区
    private VinCheckedPresenter vinCheckedPresenter;
    private String vinCheckedFailMsg;


    private boolean isDel;
    private boolean isDel2;
    private int cardType;
    private TaskItem taskItem;

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            String string = etDisplacement.getText().toString();
            if(!TextUtils.isEmpty(string)){
                if(!string.endsWith(".")){


                    int fuelType = -1;
                    if(configFuelType&&llFuelType.getVisibility()!=View.GONE) {
                        Set<Integer> set = tflFuelType.getSelectedList();
                        if (set.size() == 0) {//一个也没选中
                            fuelType = -1;
                        } else {
                            fuelType = set.iterator().next() + 1;
                        }
                    }
                    if(fuelType==5){//如果燃料种类没有选中纯电动，不检测排量是否为0
                        etDisplacement.setText("0");
                    }else{
                        if(Float.valueOf(string)==0){
                            MyToast.showShort("排量不可为0");
                            etDisplacement.setText("");
                        }
                    }

                }
            }
        }
    };


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("ProcedureCarFragment","initViews");
        View view = inflater.inflate(R.layout.fragment_procedure_car, container, false);
/*        UIUtils uiUtils = new UIUtils();
        uiUtils.setupUI(view);*/
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        if (submitModel == null) {
            submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
        }

        provinceCityUniquePresenter = new ProvinceCityUniquePresenter(this);
        activeRepairLogPresenter = new ActiveRepairLogPresenter(this);
        carDataByVinPresenter = new CarDataByVinPresenter(this);

        //通过行驶证识别传递过来的数据或列表验证VIN后传过来
        String vin = ACache.get(this.getContext().getApplicationContext()).getAsString("vin");//VIN
        String plateNo = ACache.get(this.getContext().getApplicationContext()).getAsString("plateNo");//车牌号码
        if(!StringUtils.isEmpty(vin)){
            submitModel.setVin(vin);
            submitModel.setCarLicense(plateNo);
        }

        taskItem = ((DetectMainActivity)getActivity()).getTaskItem();
        initListener();//Created by wujj on 2016/12/12.
        self = this;
        highQualityPicIdArray.add("21");
        highQualityPicIdArray.add("22");
        //highQualityPicIdArray.add("18");//行驶证正本正面，因为要识别个人认为要高清

        return view;
    }

    @Override
    protected void setView() {
        init();


        //无详情，进入手续信息，行驶证、登记证均可见，带入合格证数据，且均可编辑；有详情时，不带入合格证数据，可编辑；
        //合格证数据（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
        String vin = submitModel.getVin();
        if(!StringUtils.isEmpty(vin)){
            //如果是继续检测只请求合格证数据不覆盖
            if(submitModel.isContinue()){
                isJustGetCertificateData = true;
            }
            carDataByVinPresenter.requestCarInfo(vin);
        }
    }

    @Override
    protected void initData() {
        frescoImageLoader = FrescoImageLoader.getSingleton();
    }

    /**
     * Created by wujj on 2016/12/12.
     * 在点击下一项的时候，让其下一个EditText先获取焦点才能跳到下一项（因为点击别处的时候，已经把下一项的焦点置为false了）
     */
    private void initListener() {

        //出厂日期
        tvProductionDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //出厂日期
                selectYearMonth("出厂日期",tvProductionDate,Calendar.getInstance().get(Calendar.YEAR),1990,false);
            }
        });

        //登记日期
        tvRegisterDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //登记日期
                selectDate(tvRegisterDate,"登记日期",false);
            }
        });

        //最后过户日期
        tvLastTransferDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                selectDate(tvLastTransferDate,"最后过户日期",false);
            }
        });

        //交强险到期日
        tvComInsuranceExDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //交强险到期日
                selectDate(tvComInsuranceExDate,"交强险到期日",true);
            }
        });


        //开票日期
        tvBillDate.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //开票日期
/*              //系统日历
                calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(self.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        boolean flag = DateTimeUtils.laterThanNow(year, monthOfYear, dayOfMonth);
                        if (!flag) {
                            String date = year + "-" + (monthOfYear + 1)+ "-"+dayOfMonth;;
                            dialog.dismiss();
                            tvBillDate.setText(date);
                        } else {
                            MyToast.showLong(self.getContext().getString(R.string.selected_date_cannot_after_than_today));
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                isModified = true;*/

                selectDate(tvBillDate,"开票日期",false);
            }
        });

        //年检有效期
        tvAnnualInspection.setOnClickListener(new NoDoubleOnclickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                selectDate(tvAnnualInspection,"年检有效期",true);
            }
        });


        //车型号vin
        etVIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //维保
                checkVINDialog();
            }
        });


        //其他说明
        etRegisterAdditionInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    //隐藏软键盘--Created by wujj on 2016/12/12.
                    UIUtils.hideInputMethodManager(context);
                    //失去焦点--Created by wujj on 2016/12/12.
                    etRegisterAdditionInfo.setFocusable(false);
                    etRegisterAdditionInfo.clearFocus();
                    etRegisterAdditionInfo.setFocusableInTouchMode(false);
                }
                return false;
            }
        });
        //排量--Created by wujj on 2016/12/16.
        etDisplacement.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    //隐藏软键盘
                    UIUtils.hideInputMethodManager(context);
                    //失去焦点
                    etDisplacement.setFocusable(false);
                    etDisplacement.clearFocus();
                    etDisplacement.setFocusableInTouchMode(false);
                }
                return false;
            }
        });

        etDisplacement.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean hasFocus = etDisplacement.hasFocus();
                //第一次进来不检测排量是否为空
                if(!hasFocus){
                    return;
                }
                int fuelType = -1;
                if(configFuelType&&llFuelType.getVisibility()!=View.GONE) {
                    Set<Integer> set = tflFuelType.getSelectedList();
                    if (set.size() == 0) {//一个也没选中
                        fuelType = -1;
                    } else {
                        fuelType = set.iterator().next() + 1;
                    }
                }
                if(fuelType!=5){//如果燃料种类没有选中纯电动，不检测排量是否为0
                    LogUtil.e(TAG,"fuelType: "+fuelType);
                    if(delayRun!=null){
                        //每次editText有变化的时候，则移除上次发出的延迟线程
                        handler.removeCallbacks(delayRun);
                    }

                    //延迟800ms，如果不再输入字符，则执行该线程的run方法
                    handler.postDelayed(delayRun, 1000);
                }
            }

        });

        etVIN.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                carTypeSelectCanTab(s.toString(),tvProductionDate.getText().toString());
            }
        });

        //不能输入表情符
        InputUtil.getInstance().inputRestrict(etCarType,true,false,false,false,null,null);

        //不能输入表情符 英文 数字
        InputUtil.getInstance().inputRestrict(etCarColor,true,false,true,true,null,null);

        //车牌号码不能输入表情符 汉字 i I o O
        InputUtil.getInstance().inputRestrict(etPlateNo, true, true, false, false, "iIoO", new AfterTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                //MyToast.showShort(s.toString());
                if(!isSearchPlateRegion){
                    isSearchPlateRegion = true;
                    return;
                }
                String shortName = tvRegionSimpleName.getText().toString();
                if(!StringUtils.isEmpty(s.toString())&&s.toString().length()>=6&&llPlateRegion.getVisibility()!=View.GONE){
                    provinceCityUniquePresenter.getProvinceCityUnique(shortName+s.toString());
                }
            }
        });

        //登记证附加信息  不能输入表情
        InputUtil.getInstance().inputRestrict(etRegisterAdditionInfo,true,false,false,false,null,null);

        //轮胎规格  不能输入表情 汉字
        InputUtil.getInstance().inputRestrict(etTireModel,true,true,false,false,null,null);

        //机动车所有人不能输入表情
        InputUtil.getInstance().inputRestrict(etVehicleOwener,true,false,false,false,null,null);

        //发动机号不能输入表情符 汉字
        InputUtil.getInstance().inputRestrict(etEngineId,true,true,false,false,null,null);
    }


    /**
     * 车牌号码
     */
    private void carPlateNum(){
        //行驶证未见
        boolean isDrivingNone = cBoxDrivingNone.isChecked();
        //登记证未见
        boolean isRegisterNone = cBoxRegisterNone.isChecked();
        //车牌号码
        Set<Integer> selectPosSet = tflChepaihaoma.getSelectedList();

        //当车牌号码为不可输入状态时，上牌地区置为不可选择状态（选择【未落户】或登记证和行驶证都未见，同时选择了【未悬挂】），并清空已选择地区；

        tvRegionSimpleName.setEnabled(true);
        etPlateNo.setEnabled(true);

        //上牌地区
        tvPlateRegion.setEnabled(true);
        if(selectPosSet.size()>0){
            int selected = selectPosSet.iterator().next();
            //当评估师选择【未落户】时，车牌号码不可输入，并清空已输入信息；
            if ("未落户".equals(tflChepaihaomaLists[selected])) {
                tvRegionSimpleName.setEnabled(false);
                etPlateNo.setText("");
                etPlateNo.setEnabled(false);

                //上牌地区
                tvPlateRegion.setText("");
                tvPlateRegion.setEnabled(false);

                submitModel.setOnCardProvID(-1);
                submitModel.setOnCardProvName("");
                submitModel.setOnCardCityID(-1);
                submitModel.setOnCardCityName("");

                if(procedureModel!=null) {
                    procedureModel.setOnCardProvID(-1);
                    procedureModel.setOnCardProvName("");
                    procedureModel.setOnCardCityID(-1);
                    procedureModel.setOnCardCityName("");
                }
            }
        }

        if(isDrivingNone&&isRegisterNone){
            //当登记证和行驶证都未见时，【与实车不符】置为不可选择状态
            setTagViewEnabled(false,2,tflChepaihaoma);

            //当登记证和行驶证都未见，同时选择了【未悬挂】，车牌号置为不可输入状态；，并清空已输入信息；
            if(selectPosSet.size()>0) {
                int selected = selectPosSet.iterator().next();
                if ("未悬挂".equals(tflChepaihaomaLists[selected])) {
                    //车牌号码不允许输入
                    tvRegionSimpleName.setEnabled(false);
                    etPlateNo.setEnabled(false);
                    etPlateNo.setText("");

                    //上牌地区
                    tvPlateRegion.setText("");
                    tvPlateRegion.setEnabled(false);

                    submitModel.setOnCardProvID(-1);
                    submitModel.setOnCardProvName("");
                    submitModel.setOnCardCityID(-1);
                    submitModel.setOnCardCityName("");

                    if(procedureModel!=null) {
                        procedureModel.setOnCardProvID(-1);
                        procedureModel.setOnCardProvName("");
                        procedureModel.setOnCardCityID(-1);
                        procedureModel.setOnCardCityName("");
                    }
                }
            }
        }else{
            //当登记证和行驶证都未见时，【与实车不符】置为不可选择状态
            setTagViewEnabled(true,2,tflChepaihaoma);
        }

    }

    /**
     * 新旧VIN对比，不一样就查维保，查合格证数据
     * @param vin
     */
    public void onVinClick(String vin) {
        String oldVin = etVIN.getText().toString();
        etVIN.setText(vin);
        submitModel.setVin(vin);
        if(!vin.equals(oldVin)){
            //每次修改VIN时，取合格证数据，覆盖旧数据，且可修改；没取到合格证数据不清空旧数数据
            //修改VIN确认之后再次查询合格证数据
            //通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
            isJustGetCertificateData = false;
            carDataByVinPresenter.requestCarInfo(vin);

            //查询维保
            activeRepairLogPresenter.getCarInfoController(vin,taskId,getUser().getUserId()+"",taskItem.getTaskSourceId()+"");
        }
    }

    @Override
    public void repairLogSucceed(Repairlog repairlog) {

    }

    @Override
    public void showRepairLogError(String error) {
    }

    @Override
    public void succeedCarInfo(CarInfoModel carInfoModel) {
        this.carInfoModel = carInfoModel;

        //注意：无详情，进入手续信息，行驶证、登记证均可见，带入合格证数据，且均可编辑；有详情时，不带入合格证数据，可编辑
        if(!isJustGetCertificateData){
            //设置显示返回的合格证数据
            setCertificationData(carInfoModel);
            isJustGetCertificateData = true;
        }else{
            if(carInfoModel!=null) {
                //显示排放标准参考值
                if (StringUtils.isEmpty(carInfoModel.getEffluentStd())) {
                    tvEmissionStandard.setText("无参考数据");
                } else {
                    tvEmissionStandard.setText("参考：" + carInfoModel.getEffluentStd());
                }
            }
        }
    }

    /**
     * 设置合格证数据
     * 每次修改VIN时，再次查询合格证数据，并将查询结果替换之前的数据(取到覆盖，取不到覆盖)，且可修改；
     * 如果没取到合格证数据不清空旧数据
     */
    private void setCertificationData(CarInfoModel carInfoModel){
        //通过VIN获取（品牌型号、发动机号、燃料种类、轮胎规格、额定载客、出厂日期）
        if(null!=carInfoModel){
            //出厂日期
            tvProductionDate.setText(dateSplit(StringUtils.null2Length0(carInfoModel.getProductionTime()), 7));

            //行驶证未见和登记证未见同时选中，登记日期、品牌型号、发动机号、使用性质、燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；
            //选中登记证未见（行驶证可见），燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；不会再次获取合格证数据
            Set<Integer> set = new HashSet<Integer>();

            if(cBoxRegisterNone.isChecked()){
                //燃料种类
                set.clear();
                tflFuelType.getAdapter().setSelectedList(set);
                tflFuelType.getOnSelectListener().onSelected(set);
                setTagViewState(tflFuelType,false);

                //轮胎规格
                etTireModel.setText("");

                //核定载客量
                tflBusload.getAdapter().setSelectedList(set);
                setTagViewState(tflBusload,false);

                if(cBoxDrivingNone.isChecked()){
                    //品牌型号
                    etCarType.setText("");

                    //发动机号
                    etEngineId.setText("");
                }
            }

            if(!cBoxDrivingNone.isChecked()){

                //品牌型号
                String brandType = StringUtils.null2Length0(carInfoModel.getRecordBrand());//品牌型号（由品牌和型号组成，中间用逗号分开，服务器通过vin查询返回）
                submitModel.setBrandType(brandType);
                //将品牌型号保存到本地
                if (procedureModel == null) {
                    procedureModel = new ProcedureModel();
                }
                procedureModel.setBrandType(brandType);

                etCarType.setText(brandType.replace(",", ""));//去掉品牌和型号中间的逗号分隔符
                //发动机号
                etEngineId.setText(StringUtils.null2Length0(carInfoModel.getEngineNum()));

                if(!cBoxRegisterNone.isChecked()){
                    //燃料种类
                    String fuel = StringUtils.null2Length0(carInfoModel.getFuelType());
                    int pos = -1;
                    for (int i = 0; i < tflFuelTypeLists.length; i++) {
                        if (tflFuelTypeLists[i].equals(fuel)) {
                            pos = i;
                            break;
                        }
                    }
                    if (pos > -1) {
                        set.clear();
                        set.add(pos);
                        tflFuelType.getAdapter().setSelectedList(set);
                        tflFuelType.getOnSelectListener().onSelected(set);
                        setTagViewState(tflFuelType,true);
                    }

                    //轮胎规格
                    etTireModel.setText(StringUtils.null2Length0(carInfoModel.getTyre()));

                    //额定载客
                    int busload = carInfoModel.getSeating();
                    //核定载客量   核定载客（ 2-9,99 代表“ 9 人以上”），单选
                    set.clear();
                    if (busload == 2) {
                        set.add(0);
                    } else if (busload > 3 && busload <= 9) {
                        set.add(busload - 3);
                    } else if (busload == 99) {
                        set.add(7);
                    }
                    tflBusload.getAdapter().setSelectedList(set);
                    setTagViewState(tflBusload,true);
                }
            }

            //排放标准参考值
            if(StringUtils.isEmpty(carInfoModel.getEffluentStd())){
                tvEmissionStandard.setText("无参考数据");
            }else{
                tvEmissionStandard.setText("参考："+carInfoModel.getEffluentStd());
            }

        }
    }


    /**
     * 初始化页面显示
     */
    private void init(){
        initPageData();
        getPageData();
        setPageData();
    }


    private void initPageData(){

        taskId = ((DetectMainActivity) getActivity()).getTaskid();
        //初始化图片
        initPicData();

        eventProcedureModel = new EventProcedureModel();
        detectionWrapper = ((DetectMainActivity)this.getActivity()).getWrapper();

        configItem();
        //其它票据
        for (int i = 0; i < tflQitapiaozhengLists.length; i++) {
            tflQitapiaozhengList.add(tflQitapiaozhengLists[i]);
        }

        setTag(tflCardTypeLists, tflCardType);
        setTag(tflCheZhuZhengJianLists, tflCheZhuZhengJian);
        setTag(tflChepaihaomaLists, tflChepaihaoma);
        setTagSelect(tflFuelTypeLists, tflFuelType);
        setTagSelect(tflBusloadLists, tflBusload);
        setTag(tflHuodeFangshiLists, tflHuodeFangshi);
        setTag(tflCarColorLists, tflCarColor);
        setTag(tflJinkouGuochanLists, tflJinkouGuochan);
        setTag(tflPailiangLists, tflPailiang);
        setTag(tflShiyongxingzhiLists, tflShiyongxingzhi);
        setTag(tflCengshiyongfangLists, tflCengshiyongfang);
        setTag(tflXianshiyongfangLists, tflXianshiyongfang);
        setTag(tflExtraKeyLists, tflExtraKey);
        setTag(tflEmissionStandardLists, tflEmissionStandard);
        setTag(tflJiaoqiangxianbaodanLists, tflJiaoqiangxianbaodan);
        setTag(tflYuanchefapiaoLists, tflYuanchefapiao);
        setTag(tflQitapiaozhengList, tflQitapiaozheng);
        setTag(tflDJZFujiaxinxiLists, tflDJZFujiaxinxi);

        RxTextView.textChangeEvents(etDisplacement)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>(){
                    @Override
                    public void call(TextViewTextChangeEvent event) {
                        EditText view = (EditText) event.view();
                        String price = event.text().toString();
                        if (!TextUtils.isEmpty(price)) {
                            if(price.startsWith(".")){
                                view.setText("");
                                return;
                            }
                            if(price.length()>1 && !price.contains(".")&&Double.valueOf(price)>=10){
                                String s1 = price.substring(0,1)+".";
                                view.setText(s1);
                                view.setSelection(s1.length());
                                return;
                            }

                        }
                    }
                });


        //行驶证未见
        cBoxDrivingNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //判断是否存在行驶证照片
                    boolean isEmpty = isDrivingLPicEmpty();
                    if(isEmpty&&isChecked){
                        drivingLicenceChecked();
                    }else {
                        ShowMsgDialog.showMaterialDialog2Btn(context, "是否要删除行驶证照片？", "", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //重新设置回未点击状态
                                cBoxDrivingNone.setChecked(false);
                                tvIdentification.setEnabled(true);
                                return;
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                drivingLicenceChecked();
                            }
                        });
                    }

                }else{
                    //行驶证未见没选中
                    sdvDrivingLicenceFront.setClickable(true);
                    sdvDrivingLicenceBack.setClickable(true);
                    sdvDrivingLicenceViceFront.setClickable(true);
                    sdvDrivingLicenceViceBack.setClickable(true);
                    cBoxDrivingLicenceFlaw.setClickable(true);

                    //年检有效期 选择行驶证未见时，此字段置为不可选择状态，并清空已选择信息；
                    tvAnnualInspection.setEnabled(true);
                    tvIdentification.setEnabled(true);

                    eventProcedureModel.setDrivingNoneIsSelect(false);
                    EventBus.getDefault().post(eventProcedureModel);

                    //四张行驶证照片的文字颜色置黑
                    tvDriLicFront.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicBack.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicViceFront.setTextColor(getResources().getColor(R.color.black));
                    tvDriLicViceBack.setTextColor(getResources().getColor(R.color.black));
                    //行驶证有瑕疵文字颜色置黑
                    ColorStateList csl =(ColorStateList)getResources().getColorStateList(R.color.btn_text_selector);
                    cBoxDrivingLicenceFlaw.setTextColor(csl);
                    cBoxDrivingLicenceFlaw.setBackgroundResource(R.drawable.btn_bg_selector);

                    //车牌号码
                    carPlateNum();

                    //行驶证未见和登记证未见同时选中，登记日期、品牌型号、发动机号、使用性质、燃料种类、轮胎规格、核定载客数，
                    //不可输入并清空已输入信息，提交时可以为空；

                    //登记日期
                    tvRegisterDate.setEnabled(true);
                    //品牌型号
                    etCarType.setEnabled(true);
                    //发动机号
                    etEngineId.setEnabled(true);
                    //使用性质
                    setTagViewState(tflShiyongxingzhi,true);

                    setDisableByDrivingIsNone(false);

                }

            }
        });

        //登记证未见
        cBoxRegisterNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //判断是否存在行驶证照片
                    boolean isEmpty = isRegisterLPicEmpty();
                    if(isEmpty){
                        registerLicenceChecked();
                    }else {
                        ShowMsgDialog.showMaterialDialog2Btn(context, "是否要删除登记证照片？", "", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //重新设置回未点击状态
                                cBoxRegisterNone.setChecked(false);
                                return;
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                registerLicenceChecked();
                            }
                        });
                    }

                }else{
                    sdvRegister1.setClickable(true);
                    sdvRegister3.setClickable(true);
                    sdvRegisterMore.setClickable(true);
                    cBoxRegisterFlaw.setClickable(true);


                    //显示证件类型一栏
                    if(configCardType) {
                        tflCardType.setEnabled(true);
                        setTagViewState(tflCardType,true);
                    }
                    //显示车主证件号一栏
                    if(configCardId) {
                        etOwnerId.setEnabled(true);
                        setTagViewState(tflCheZhuZhengJian,true);
                    }
                    //显示燃料种类
                    if(configFuelType) {
                        setTagViewState(tflFuelType,true);
                    }

                    //显示初登地区
                    if (configRegion) {
                        tvRegisterRegion.setEnabled(true);
                    }

                    //显示获得方式
                    if(configObtainWay) {
                        setTagViewState(tflHuodeFangshi,true);
                    }
                    //显示进口国产
                    if(configIsMadeInChina) {

                    }
                    //显示轮胎规格
                    if(configTireType) {
                        etTireModel.setEnabled(true);
                    }
                    //显示过户次数
                    if(configTransferNum) {
                        etTransferNum.setEnabled(true);
                    }

                    //最后过户日期  当过户次数输入0时，最后过户日期为不可选择状态；登记证选择未见时，此项置为不可选择状态，并清空已输入信息；
                    if(!TextUtils.isEmpty(etTransferNum.getText().toString())) {
                        if (Integer.valueOf(etTransferNum.getText().toString()) > 0) {
                            //显示最后过户日期
                            if (configLastTransferDate) {
                                tvLastTransferDate.setEnabled(true);
                            }
                        }
                    }

                    //显示登记证附加信息
                    if(configRegisterAdditionInfo) {
                        etRegisterAdditionInfo.setEnabled(true);
                        setTagViewState(tflDJZFujiaxinxi,true);
                    }

                    //显示车身颜色
                    if(configCarColor) {
                        setTagViewState(tflCarColor,true);
                    }


                    //显示机动车所有人一栏  放到最后判断是为了改变与其相关的项的显示与隐藏
                    if(configOwener) {
                        etVehicleOwener.setEnabled(true);
                    }

                    //显示登记日期
                    if(configRegisterDate) {
                        tvRegisterDate.setEnabled(true);
                    }
                    //显示品牌型号
                    if(configCarType) {
                        etCarType.setEnabled(true);
                    }
                    //显示发动机号
                    if(configEngineId) {
                        etEngineId.setEnabled(true);
                    }
                    //核定载客
                    setTagViewState(tflBusload,true);

                    //使用性质
                    setTagViewState(tflShiyongxingzhi,true);

                    //曾使用方
                    setTagViewState(tflCengshiyongfang,true);

                    //现使用方
                    setTagViewState(tflXianshiyongfang,true);

                    cBoxRegisterNone.setFocusable(true);
                    cBoxRegisterNone.setFocusableInTouchMode(true);
                    cBoxRegisterNone.requestFocus();
                    cBoxRegisterNone.findFocus();
                    cBoxRegisterNone.setFocusable(false);

                    eventProcedureModel.setRegisterNoneIsSelected(false);
                    EventBus.getDefault().post(eventProcedureModel);

                    //四张登记证照片的文字颜色置黑
                    tvRegister1.setTextColor(getResources().getColor(R.color.black));
                    tvRegister3.setTextColor(getResources().getColor(R.color.black));
                    tvRegisterMore.setTextColor(getResources().getColor(R.color.black));
                    //登记证有瑕疵文字颜色置灰
                    ColorStateList csl =(ColorStateList)getResources().getColorStateList(R.color.btn_text_selector);
                    cBoxRegisterFlaw.setTextColor(csl);
                    cBoxRegisterFlaw.setBackgroundResource(R.drawable.btn_bg_selector);

                    //车牌号码
                    carPlateNum();

                    setDisableByRejesterIsNone(false);
                }

            }
        });

        //车牌号简称
        String plateNo = "";
        if(submitModel!=null) {
            plateNo = submitModel.getCarLicense();
        }
        if(TextUtils.isEmpty(getUser().getCityShortName())&&StringUtils.isEmpty(plateNo)){
            tvRegionSimpleName.setText("苏");
        }else{
            //根据登录的信息设置城市简称
            String cshortName = PadSysApp.getUser().getCityShortName();
            tvRegionSimpleName.setText(cshortName);

            if(!StringUtils.isEmpty(plateNo)) {
                ArrayList<String> listData = LicencePlateAddress.getInstance().getSimpleName();
                String shortName = plateNo.substring(0, 1);
                if (!TextUtils.isEmpty(shortName)) {
                    if (listData.contains(shortName)) {
                        tvRegionSimpleName.setText(plateNo.substring(0, 1));
                    }
                }
                isSearchPlateRegion = false;
                etPlateNo.setText(plateNo.substring(1));
            }
        }

        //车牌号
        etPlateNo.setTransformationMethod(new InputLowerToUpper());
        //车辆型号
        etCarType.setTransformationMethod(new InputLowerToUpper());
        //车架号VIN
        etVIN.setTransformationMethod(new InputLowerToUpper());
        if(submitModel!=null) {
            String vin = submitModel.getVin();
            etVIN.setText(vin);
        }

        if (submitModel == null) {
            submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
            if(submitModel==null){
                getActivity().finish();
                return;
            }
        }

        String vin = submitModel.getVin();
        if (!submitModel.isContinue()&&!StringUtils.isEmpty(vin)) {
            //查询维保
            activeRepairLogPresenter.getCarInfoController(vin, taskId, getUser().getUserId() + "",taskItem.getTaskSourceId()+"");
        }

        //车主证件号
        etOwnerId.setTransformationMethod(new InputLowerToUpper());

        //发动机号
        etEngineId.setTransformationMethod(new InputLowerToUpper());
        //轮胎规格
        etTireModel.setTransformationMethod(new InputLowerToUpper());

        //过户次数 默认值为-1
        etTransferNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stn = s.toString();
                if(stn.startsWith("0")&&stn.length()>1){
                    etTransferNum.removeTextChangedListener(this);
                    etTransferNum.setText("0");
                    etTransferNum.setSelection(etTransferNum.length());
                    etTransferNum.addTextChangedListener(this);
                }


                //原车发票  过户次数大于0时，原车发票置为不可输入（选择）状态，并清空已输入（选择）信息；
                //原车发票	选择未见后，发票金额和开票日期为不可输入状态；

                Set<Integer> selectPosSet = tflYuanchefapiao.getSelectedList();
                if(selectPosSet.size()>0){
                    int selected = selectPosSet.iterator().next();
                    if (!"未见".equals(tflYuanchefapiaoLists[selected])) {
                        etBillMoney.setEnabled(true);
                        tvBillDate.setEnabled(true);
                        setTagViewState(tflYuanchefapiao,true);
                        setDisableTv(tvOriginalBill,true);
                    }
                }else{
                    etBillMoney.setEnabled(true);
                    tvBillDate.setEnabled(true);
                    setTagViewState(tflYuanchefapiao,true);
                    setDisableTv(tvOriginalBill,true);
                }



                if(!TextUtils.isEmpty(stn)) {
                    if (Integer.valueOf(stn) > 0) {

                        if(configCarBill) {
                            //原车发票
                            etBillMoney.setText("");
                            etBillMoney.setEnabled(false);
                            tvBillDate.setText("");
                            tvBillDate.setEnabled(false);
                            HashSet<Integer> set = null;
                            tflYuanchefapiao.getAdapter().setSelectedList(set);
                            setTagViewState(tflYuanchefapiao,false);
                            setDisableTv(tvOriginalBill,false);
                        }
                    }
                }

                //最后过户日期   当过户次数输入0时，最后过户日期为不可选择状态；并清空已输入（选择）信息；登记证选择未见时，此项置为不可选择状态，并清空已输入信息；
                tvLastTransferDate.setEnabled(true);

                if(!TextUtils.isEmpty(s.toString())) {
                    if (Integer.valueOf(s.toString()) == 0) {

                        tvLastTransferDate.setEnabled(false);
                        tvLastTransferDate.setText("");
                    }
                }
                //登记证选择未见时，此项置为不可选择状态，并清空已输入信息；
                if(configLastTransferDate&&cBoxRegisterNone.isChecked()) {
                    tvLastTransferDate.setEnabled(false);
                    tvLastTransferDate.setText("");
                }

                //过户票  当过户次数为0时，过户票置为不可选择状态；
                setTagViewEnabled(true,0,tflQitapiaozheng);
                if(!TextUtils.isEmpty(s.toString())) {
                    if(Integer.valueOf(s.toString())==0){
                        setTagViewEnabled(false,0,tflQitapiaozheng);
                    }
                }

            }
        });

    }

    /**
     * 获取数据
     */
    private void getPageData(){
        //获取本地数据
        initLocalData();

        //获取网络数据
        initNetData();
    }

    /**
     * 本地数据或网络数据后为页面view赋值显示
     */
    private void setPageData(){
        if(procedureModel == null){
            return;
        }

        //机动车所有人
        etVehicleOwener.setText(procedureModel.getCarOwner());

        //证件类型  证件类型（ 0 默认， 1 身份证， 2 军官证， 3 护照， 4 组织机构代码证， 5 车主证件未见），单选
        //证件类型选择“车主证件未见”，车主证件号清空并不可选
        int cardType = procedureModel.getCardType();
        Set<Integer> set = new HashSet<Integer>();
        if(cardType>0) {
            set.add(cardType - 1);
            tflCardType.getAdapter().setSelectedList(set);
            tflCardType.afterPerformClick(set);
        }
        if(cardType==5) {
            set.clear();
            etOwnerId.setText("");
            etOwnerId.setEnabled(false);
            tflCheZhuZhengJian.getAdapter().setSelectedList(set);
            tflCheZhuZhengJian.afterPerformClick(set);
            setTagViewState(tflCheZhuZhengJian, false);
            tvOwnerId.setTextColor(getResources().getColor(R.color.global_gray_7));
        }

        //车主证件号
        etOwnerId.setText(procedureModel.getCardNum());

        //登记证与车主证件号  与车主证件号是否一致（服务器： 0 不一致， 1 一致  ,默认是一致），单选
        int isCardSame = procedureModel.getIsCardSame();
        set.clear();
        if(isCardSame==0){
            set.add(0);
            tflCheZhuZhengJian.getAdapter().setSelectedList(set);
            tflCheZhuZhengJian.afterPerformClick(set);
        }


        //登记日期
        tvRegisterDate.setText(dateSplit(procedureModel.getRecordDate()));

        //初登地区  需要名字
        if(null2Length0(procedureModel.getProvName()).equals(null2Length0(procedureModel.getCityName()))){
            tvRegisterRegion.setText(null2Length0(procedureModel.getProvName()));
        }else{
            tvRegisterRegion.setText(null2Length0(procedureModel.getProvName())+ null2Length0(procedureModel.getCityName()));
        }


        //上牌地区  需要名字
        isSearchPlateRegion = false;
        if(null2Length0(procedureModel.getOnCardProvName()).equals(null2Length0(procedureModel.getOnCardCityName()))){
            tvPlateRegion.setText(null2Length0(procedureModel.getOnCardProvName()));
        }else{
            tvPlateRegion.setText(null2Length0(procedureModel.getOnCardProvName())+ null2Length0(procedureModel.getOnCardCityName()));
        }


        //交强险所在地
        if(null2Length0(procedureModel.getInsuranceProvName()).equals(null2Length0(procedureModel.getInsuranceCityName()))){
            tvComInsuranceAddress.setText(null2Length0(procedureModel.getInsuranceProvName()));
        }else{
            tvComInsuranceAddress.setText(null2Length0(procedureModel.getInsuranceProvName())+ null2Length0(procedureModel.getInsuranceCityName()));
        }


        //车牌号码
        String carLicense = procedureModel.getCarLicense();
        if(!TextUtils.isEmpty(carLicense)){
            tvRegionSimpleName.setText(carLicense.substring(0,1));
            etPlateNo.setText(carLicense.substring(1));

            //根据车牌号码查询上牌地区
            if(StringUtils.isEmpty(tvPlateRegion.getText().toString())){
                isSearchPlateRegion = true;
                String tshortName = tvRegionSimpleName.getText().toString();
                String tplateNo = etPlateNo.getText().toString();
                if(StringUtils.isEmpty(tplateNo)) {//etPlateNo值为空会报查询参数错误
                    tplateNo = "A";
                }
                if(!StringUtils.isEmpty(tshortName)){
                    provinceCityUniquePresenter.getProvinceCityUnique(tshortName + tplateNo);
                }
            }
        }

        //有无车牌  车牌有无（ 0 无车牌、 1 有车牌、 2 未悬挂 、3 与实车不符），单选
        int carLicenseEx = procedureModel.getCarLicenseEx();
        set.clear();
        if(carLicenseEx==0) {
            set.add(0);
        }else if(carLicenseEx==2){
            set.add(1);
        }else if(carLicenseEx==3){
            set.add(2);
        }
        tflChepaihaoma.getAdapter().setSelectedList(set);
        tflChepaihaoma.getOnSelectListener().onSelected(set);


        //品牌型号
        etCarType.setText(procedureModel.getRecordBrand());


        //车架号VIN
        etVIN.setText(procedureModel.getVin());


        //发动机号
        etEngineId.setText(procedureModel.getEngineNum());


        //燃料种类  燃料种类： 1 汽油、 2 柴油、 3 混动、 4 天然气、 5 纯电动
        int fuelType = procedureModel.getFuelType();

        set.clear();
        if(fuelType>0) {
            set.add(fuelType - 1);
            tflFuelType.getAdapter().setSelectedList(set);
            tflFuelType.getOnSelectListener().onSelected(set);
        }
        if(cBoxRegisterNone.isChecked()){
            setTagViewState(tflFuelType,false);
        }


        //核定载客量   核定载客（ 2-9,99 代表“ 9 人以上”），单选
        int busload = procedureModel.getSeating();

        set.clear();
        if(busload==2){
            set.add(0);
        }else if(busload>3&&busload<=9){
            set.add(busload-3);
        }else if(busload==99){
            set.add(7);
        }
        tflBusload.getAdapter().setSelectedList(set);
        tflBusload.afterPerformClick(set);
        if(cBoxRegisterNone.isChecked()){
            setTagViewState(tflBusload,false);
        }


        //获得方式    车辆获得方式（ 1 购买、 2 仲裁裁判、 3 继承、 4 赠与、 5 协议抵偿债务、 6 中奖、 7 资产重组、 8 资产整体买卖、 9 调拨、 10 境外自带、 11 法院调解 / 裁定 / 判决），单选
        int obtainWay = procedureModel.getCarGetWay();
        set.clear();
        if(obtainWay>0) {
            set.add(obtainWay - 1);
            tflHuodeFangshi.getAdapter().setSelectedList(set);
            tflHuodeFangshi.afterPerformClick(set);
        }


        //车身颜色   车身颜色 (1 白、 2 灰、 3 红、 4 粉、 5 黄、 6 蓝、 7 绿、 8 紫、 9 棕、  10 黑、11、双色 99 其它 ) ，单选
        int carColor = procedureModel.getColor();//created by wujj on 2016/12/21
        set.clear();
        if(carColor==99){
            set.add(11);
            tflCarColor.getAdapter().setSelectedList(set);
            tflCarColor.getOnSelectListener().onSelected(set);
            etCarColor.setVisibility(View.VISIBLE);
            etCarColor.setText(procedureModel.getCarColorDes());
        }else if(carColor>0) {
            set.add(carColor - 1);
            tflCarColor.getAdapter().setSelectedList(set);
            tflCarColor.afterPerformClick(set);
        }


        //进口国产  进口国产 (0 国产、 1 进口 ) 去掉了

        //排量
        String exhaust = StringUtils.null2Length0(procedureModel.getExhaust());
        if(exhaust.toUpperCase().contains("L")||exhaust.toUpperCase().contains("T")){
            if(exhaust.length()==1){//如果是"L",或"T"
                //如果是燃料种类是纯电动，显示0.0
                if(fuelType==5){
                    etDisplacement.setText("0.0");
                }else{
                    etDisplacement.setText("");
                }

            }else{
                etDisplacement.setText(exhaust.substring(0,exhaust.length()-1));
            }
        }else{
            etDisplacement.setText(exhaust);
        }
        //是否有涡轮增压（ 0 无， 1 有），单选
        int turbo = procedureModel.getTurboChargingHave();
        set.clear();
        if(turbo==1) {
            set.add(0);
            tflPailiang.getAdapter().setSelectedList(set);
            tflPailiang.afterPerformClick(set);
        }

        //轮胎规格
        etTireModel.setText(procedureModel.getTyre());

        //排放标准 (国二及以下：1，国三：2，国四：3，国五：4，无法判断：5 )，单选
        int effluentStd = procedureModel.getEffluentStd();
        set.clear();
        if(effluentStd>0) {
            set.add(effluentStd - 1);
            tflEmissionStandard.getAdapter().setSelectedList(set);
        }
        //排放标准参考
        if(!StringUtils.isEmpty(procedureModel.getEffluentStdRef())){
            tvEmissionStandard.setText(procedureModel.getEffluentStdRef());
        }

        //使用性质  显示使用性质（ 1 非营运、 2 营转非、 3 出租营转非、 4 营运、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
        // 提交顺序 （ 1 营运、 2 非营运、 3 营转非、 4 出租营转非、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
        int useNature = procedureModel.getService();
        set.clear();
        if(useNature>0) {
            if(useNature==1){
                set.add(3);
            }else if(useNature==2){
                set.add(0);
            }else if(useNature==3){
                set.add(1);
            }else if(useNature==4){
                set.add(2);
            }else{
                set.add(useNature - 1);
            }

            tflShiyongxingzhi.getAdapter().setSelectedList(set);
            tflShiyongxingzhi.afterPerformClick(set);
        }
        //出厂日期
        tvProductionDate.setText(dateSplit(procedureModel.getProductionTime(),7));

        //过户次数
        int transferCount = procedureModel.getTransferCount();
        if(transferCount<0){
            etTransferNum.setText("");
        }else{
            etTransferNum.setText(transferCount+"");
        }

        //最后过户日期
        tvLastTransferDate.setText(dateSplit(procedureModel.getLastTransferDate()));


        //曾使用方   (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
        int oldUser = procedureModel.getOldUseOwner();
        set.clear();
        if(oldUser>0) {
            set.add(oldUser - 1);
            tflCengshiyongfang.getAdapter().setSelectedList(set);
            tflCengshiyongfang.afterPerformClick(set);
        }

        //现使用方   现使用方 (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
        int curUser = procedureModel.getNowUseOwner();
        set.clear();
        if(curUser>0) {
            set.add(curUser - 1);
            tflXianshiyongfang.getAdapter().setSelectedList(set);
            tflXianshiyongfang.afterPerformClick(set);
        }

        //备用钥匙   （0--0；1--1；2--2及以上；-1--空） ，单选
        int spareKey = procedureModel.getSpareKey();
        if(spareKey>-1) {
            set.clear();
            set.add(spareKey);
            tflExtraKey.getAdapter().setSelectedList(set);
            tflExtraKey.afterPerformClick(set);
        }

        //交强险保单   交强险保单 (1 正常、 2 未见、 3 被保险人与车主不一致 ) ，单选
        int insuranceBill = procedureModel.getTrafficInsuranceHave();
        if(insuranceBill>0) {
            set.clear();
            set.add(insuranceBill - 1);
            tflJiaoqiangxianbaodan.getAdapter().setSelectedList(set);
            tflJiaoqiangxianbaodan.afterPerformClick(set);
        }

        //交强险到期日
        tvComInsuranceExDate.setText(dateSplit(procedureModel.getInsurance()));

        //原车发票 过户次数大于0时，原车发票置为不可输入（选择）状态，并清空已输入（选择）信息；
        if(procedureModel.getCarInvoiceMoney()>=0){
            etBillMoney.setText(procedureModel.getCarInvoiceMoney()+"");
        }else{
            etBillMoney.setText("");
        }
        if(procedureModel.getTransferCount()>0){
            etBillMoney.setText("");
        }


        //开票日期
        tvBillDate.setText(dateSplit(procedureModel.getCarInvoiceDate()));

        //工商章   原车发票 (1 无工商章、 2 未见， 3 有 ) ，单选
        int industrialStamp = procedureModel.getCarInvoiceHave();
        if(industrialStamp>0&&industrialStamp<3) {
            set.clear();
            set.add(industrialStamp - 1);
            tflYuanchefapiao.getAdapter().setSelectedList(set);
            tflYuanchefapiao.getOnSelectListener().onSelected(set);
        }else if(industrialStamp == 3){
            set.clear();
            tflYuanchefapiao.getAdapter().setSelectedList(set);
            tflYuanchefapiao.getOnSelectListener().onSelected(set);
        }

        //原车发票 未见
        TagView tagView = (TagView) tflYuanchefapiao.getChildAt(1);
        if (tagView.isChecked()) {
            changeTagViewEnabled(1, 1, tagView, tflYuanchefapiao);
        }


        //其它票证  其它票证 (1 过户票、 2 备用钥匙、 3 进口关单、 4 购置税完税证明（征税）、 5 购置税完税证明（免税） ) ，复选（英文逗号分隔）
        //当过户次数为0时，过户票置为不可选择状态；
        String otherBill = procedureModel.getCarInvoiceOther();
        set.clear();
        if(!TextUtils.isEmpty(otherBill)){
            //得到当前的所有显示选项
            int count = tflQitapiaozheng.getAdapter().getCount();
            HashMap<Integer,String> billMap = new HashMap<>();
            for (int i = 0; i < count; i++) {
                billMap.put(i,(String)tflQitapiaozheng.getAdapter().getItem(i));
            }

            String[] bills = otherBill.split(",");
            for (int i = 0; i < bills.length; i++) {
                if(bills[i].equals("1")){
                    for (Map.Entry<Integer, String> entry : billMap.entrySet()) {
                        if(entry.getValue().equals("过户票")){
                            set.add(entry.getKey());
                            break;
                        }
                    }
                }else if(bills[i].equals("2")){
                    for (Map.Entry<Integer, String> entry : billMap.entrySet()) {
                        if(entry.getValue().equals("备用钥匙")){
                            set.add(entry.getKey());
                            break;
                        }
                    }
                }else if(bills[i].equals("3")){
                    for (Map.Entry<Integer, String> entry : billMap.entrySet()) {
                        if(entry.getValue().equals("进口关单")){
                            set.add(entry.getKey());
                            break;
                        }
                    }
                }else if(bills[i].equals("4")){
                    for (Map.Entry<Integer, String> entry : billMap.entrySet()) {
                        if(entry.getValue().equals("购置税完税证明（征税）")){
                            set.add(entry.getKey());
                            break;
                        }
                    }
                }else if(bills[i].equals("5")){
                    for (Map.Entry<Integer, String> entry : billMap.entrySet()) {
                        if(entry.getValue().equals("购置税完税证明（免税）")){
                            set.add(entry.getKey());
                            break;
                        }
                    }
                }
            }

            tflQitapiaozheng.getAdapter().setSelectedList(set);
            tflQitapiaozheng.getOnSelectListener().onSelected(set);

            //过户票  当过户次数为0时，过户票置为不可选择状态；
            if(!TextUtils.isEmpty(etTransferNum.getText().toString())) {
                if(Integer.valueOf(etTransferNum.getText().toString())==0){
                    setTagViewEnabled(false,0,tflQitapiaozheng);
                }
            }
        }

        //登记证附加信息  此项允许为空   登记证附加信息（ 1 正在抵押、 2 发动机号变更、 3 重打车架号、 4 登记证补领、 5 颜色变更），复选（英文逗号分隔）
        String registerAdditionInfo = procedureModel.getCertificateEx();
        set.clear();
        if(!TextUtils.isEmpty(registerAdditionInfo)){
            String[] additionInfos = registerAdditionInfo.split(",");
            for (int i = 0; i < additionInfos.length; i++) {
                set.add(Integer.valueOf(additionInfos[i])-1);
            }
            tflDJZFujiaxinxi.getAdapter().setSelectedList(set);
            tflDJZFujiaxinxi.afterPerformClick(set);
        }

        //登记证附加信息 说明
        etRegisterAdditionInfo.setText(procedureModel.getCertificateExDes());

        //年检有效期
        tvAnnualInspection.setText(dateSplit(procedureModel.getInspection()));

        //如果手续信息必填项不为空，可进入车型选择
        carTypeSelectCanTab(submitModel.getVin(),submitModel.getProductionTime());


        //行驶证
        int id = procedureModel.getDrivingLicenseProperty();
        if(id==1){
            //行驶证有瑕疵
            cBoxDrivingLicenceFlaw.setChecked(true);
        }else if(id==2){
            //行驶证未见
            cBoxDrivingNone.setChecked(true);
        }

        //登记证
        id = procedureModel.getRegistLicenseProperty();
        if(id==1){
            //登记证有瑕疵
            cBoxRegisterFlaw.setChecked(true);
        }else if(id==2){
            //登记证未见
            cBoxRegisterNone.setChecked(true);
        }

        if(procedureModel.getDrivingLicenseProperty()==2){
            tvIdentification.setEnabled(false);
        }

        //刷新照片
        refreshDLPic();
        refreshDLRegisterPic();

    }


    /**
     * 初始化本地数据
     */
    private void initLocalData(){

        //从本地数据库中获取此用户下的taskId
        List<DBBase> list = DBManager.getInstance().query(taskId,Constants.DATA_TYPE_PROCEDURE, getUser().getUserId());
        LogUtil.e(TAG,"本地数据库： "+list+" list.size(): "+list.size());
        if(list!=null&&list.size()>0){
            String json = list.get(0).getJson();
            if(!TextUtils.isEmpty(json)){
                procedureModel = new Gson().fromJson(json,ProcedureModel.class);
            }
        }

        //合格证数据
        if(procedureModel!=null){
            if(procedureModel.isHasCertificateData()){
                //如果有合格证数据，还原出合格证数据
                if(carInfoModel == null){

                    carInfoModel = new CarInfoModel();
                    //品牌型号
                    carInfoModel.setRecordBrand(procedureModel.getBrandType());
                    //发动机号
                    carInfoModel.setEngineNum(procedureModel.getEngineNum());
                    //轮胎规格
                    carInfoModel.setTyre(procedureModel.getTyre());
                    //出厂日期
                    carInfoModel.setProductionTime(procedureModel.getProductionTime());
                    //燃料种类
                    if(procedureModel.getFuelType()> -1){
                        carInfoModel.setFuelType(tflFuelTypeLists[procedureModel.getFuelType()-1]);
                    }
                    //额定载客
                    carInfoModel.setSeating(procedureModel.getSeating());
                    //排放标准参考值
                    carInfoModel.setEffluentStd(procedureModel.getEffluentStdRef());
                }
            }
        }

        //从本地数据库初始化值
        if(procedureModel!=null){
            if(submitModel==null){
                if(this.getActivity()==null){
                    //关闭DetectMainActivity
                    AppManager.getAppManager().finishActivity(DetectMainActivity.class);
                }
                return;
            }

            //如果存在网络图片地址，则显示网络图片
            List<TaskDetailModel.ProcedurePicListBean> procedurePicList = procedureModel.getProcedurePicList();
            if(procedurePicList!=null){
                for (int i = 0; i < procedurePicList.size(); i++) {
                    TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                    String picId = procedurePicBean.getPicID();
                    if(!TextUtils.isEmpty(picId)){
                        if("18".equals(picId)){//行驶证正本正面
                            pathDLFront = procedurePicBean.getPicPath();
                            pictureItemsDL.get(0).setPicPath(pathDLFront);
                        }else if("19".equals(picId)){//行驶证正本背面
                            pathDLBack = procedurePicBean.getPicPath();
                            pictureItemsDL.get(1).setPicPath(pathDLBack);
                        }else if("20".equals(picId)){//行驶证副本正面
                            pathDLViceFront = procedurePicBean.getPicPath();
                            pictureItemsDL.get(2).setPicPath(pathDLViceFront);
                        }else if("21".equals(picId)){//登记证 1-2 页照片
                            pathDLRegister1 = procedurePicBean.getPicPath();
                            pictureItemsDLRegister.get(0).setPicPath(pathDLRegister1);
                        }else if("22".equals(picId)){//登记证 3-4 页照片
                            pathDLRegister3 = procedurePicBean.getPicPath();
                            pictureItemsDLRegister.get(1).setPicPath(pathDLRegister3);
                        }else if("23".equals(picId)){//更多登记证照片1
                            if(!procedureModel.getDeletePicId().contains("23")){
                                pathDLRegisterM = procedurePicBean.getPicPath();
                                registerMorePI.setPicPath(pathDLRegisterM);
                            }
                            if(procedureModel.getSubmitdelPicId().contains("23")){
                                List<String> deletePic = submitModel.getDeletePicId();
                                if(!deletePic.contains("23")){
                                    deletePic.add("23");
                                }
                            }
                        }else if("28".equals(picId)){//更多登记证照片2
                            if(!procedureModel.getDeletePicId().contains("28")){
                                pathDLRegisterM2 = procedurePicBean.getPicPath();
                                registerMorePI2.setPicPath(pathDLRegisterM2);
                            }
                            if(procedureModel.getSubmitdelPicId().contains("28")){
                                List<String> deletePic = submitModel.getDeletePicId();
                                if(!deletePic.contains("28")){
                                    deletePic.add("28");
                                }
                            }
                        }else if("24".equals(picId)){//行驶证副本背面
                            pathDLViceBack = procedurePicBean.getPicPath();
                            pictureItemsDL.get(3).setPicPath(pathDLViceBack);
                        }
                    }
                }
            }


            //机动车所有人
            submitModel.setCarOwner(procedureModel.getCarOwner());

            //证件类型  证件类型（ 0 默认， 1 身份证， 2 军官证， 3 护照， 4 组织机构代码证， 5 车主证件未见），单选
            //证件类型选择“车主证件未见”，车主证件号清空并不可选
            int cardType = procedureModel.getCardType();
            submitModel.setCardType(cardType);


            //车主证件号
            submitModel.setCardNum(procedureModel.getCardNum());

            //登记证与车主证件号  与车主证件号是否一致（服务器： 0 不一致， 1 一致  ,默认是一致），单选
            int isCardSame = procedureModel.getIsCardSame();
            submitModel.setIsCardSame(isCardSame);

            //登记日期
            submitModel.setRecordDate(dateSplit(procedureModel.getRecordDate()));

            //初登地区  需要名字
            submitModel.setRegisterprovID(procedureModel.getRegisterprovID());
            submitModel.setProvName(procedureModel.getProvName());
            submitModel.setRegisterCityID(procedureModel.getRegisterCityID());
            submitModel.setCityName(procedureModel.getCityName());

            //上牌地区  需要名字
            submitModel.setOnCardProvID(procedureModel.getOnCardProvID());
            submitModel.setOnCardProvName(procedureModel.getOnCardProvName());
            submitModel.setOnCardCityID(procedureModel.getOnCardCityID());
            submitModel.setOnCardCityName(procedureModel.getOnCardCityName());

            //交强险所在地
            submitModel.setInsuranceProvID(procedureModel.getInsuranceProvID());
            submitModel.setInsuranceProvName(procedureModel.getInsuranceProvName());
            submitModel.setInsuranceCityID(procedureModel.getInsuranceCityID());
            submitModel.setInsuranceCityName(procedureModel.getInsuranceCityName());

            //车牌号码
            String carLicense = procedureModel.getCarLicense();
            submitModel.setCarLicense(carLicense);


            //有无车牌  车牌有无（ 0 无车牌、 1 有车牌、 2 未悬挂 、3 与实车不符），单选
            int carLicenseEx = procedureModel.getCarLicenseEx();
            submitModel.setCarLicenseEx(carLicenseEx);

            //品牌型号
            submitModel.setRecordBrand(procedureModel.getRecordBrand());
            submitModel.setBrandType(procedureModel.getBrandType());


            //车架号VIN
            submitModel.setVin(procedureModel.getVin());

            //发动机号
            submitModel.setEngineNum(procedureModel.getEngineNum());

            //燃料种类  燃料种类： 1 汽油、 2 柴油、 3 混动、 4 天然气、 5 纯电动
            int fuelType = procedureModel.getFuelType();
            submitModel.setFuelType(fuelType);


            //核定载客量   核定载客（ 2-9,99 代表“ 9 人以上”），单选
            int busload = procedureModel.getSeating();
            submitModel.setSeating(busload);


            //获得方式    车辆获得方式（ 1 购买、 2 仲裁裁判、 3 继承、 4 赠与、 5 协议抵偿债务、 6 中奖、 7 资产重组、 8 资产整体买卖、 9 调拨、 10 境外自带、 11 法院调解 / 裁定 / 判决），单选
            int obtainWay = procedureModel.getCarGetWay();
            submitModel.setCarGetWay(obtainWay);

            //车身颜色   车身颜色 (1 白、 2 灰、 3 红、 4 粉、 5 黄、 6 蓝、 7 绿、 8 紫、 9 棕、  10 黑、11、双色 99 其它 ) ，单选
            int carColor = procedureModel.getColor();//created by wujj on 2016/12/21
            submitModel.setColor(carColor);//created by wujj on 2016/12/21
            submitModel.setCarColorDes(procedureModel.getCarColorDes());


            //排量
            String exhaust = StringUtils.null2Length0(procedureModel.getExhaust());
            submitModel.setExhaust(exhaust);
            //是否有涡轮增压（ 0 无， 1 有），单选
            int turbo = procedureModel.getTurboChargingHave();
            submitModel.setTurboChargingHave(turbo);

            //轮胎规格
            submitModel.setTyre(procedureModel.getTyre());

            //排放标准 (国二及以下：1，国三：2，国四：3，国五：4，无法判断：5 )，单选
            int effluentStd = procedureModel.getEffluentStd();
            submitModel.setEffluentStd(effluentStd);


            //使用性质  显示使用性质（ 1 非营运、 2 营转非、 3 出租营转非、 4 营运、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
            // 提交顺序 （ 1 营运、 2 非营运、 3 营转非、 4 出租营转非、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
            int useNature = procedureModel.getService();
            submitModel.setService(useNature);

            //出厂日期
            submitModel.setProductionTime(dateSplit(procedureModel.getProductionTime(),7));

            //过户次数
            int transferCount = procedureModel.getTransferCount();
            submitModel.setTransferCount(transferCount);

            //最后过户日期
            submitModel.setLastTransferDate(dateSplit(procedureModel.getLastTransferDate()));

            //曾使用方   (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
            int oldUser = procedureModel.getOldUseOwner();
            submitModel.setOldUseOwner(oldUser);


            //现使用方   现使用方 (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
            int curUser = procedureModel.getNowUseOwner();
            submitModel.setNowUseOwner(curUser);

            //备用钥匙   （0--0；1--1；2--2及以上；-1--空） ，单选
            int spareKey = procedureModel.getSpareKey();
            submitModel.setSpareKey(spareKey);


            //交强险保单   交强险保单 (1 正常、 2 未见、 3 被保险人与车主不一致 ) ，单选
            int insuranceBill = procedureModel.getTrafficInsuranceHave();
            submitModel.setTrafficInsuranceHave(insuranceBill);


            //交强险到期日
            submitModel.setInsurance(dateSplit(procedureModel.getInsurance()));

            //原车发票 过户次数大于0时，原车发票置为不可输入（选择）状态，并清空已输入（选择）信息；
            submitModel.setCarInvoiceMoney(procedureModel.getCarInvoiceMoney());
            //开票日期
            submitModel.setCarInvoiceDate(dateSplit(procedureModel.getCarInvoiceDate()));
            //工商章   原车发票 (1 无工商章、 2 未见， 3 有 ) ，单选
            int industrialStamp = procedureModel.getCarInvoiceHave();
            submitModel.setCarInvoiceHave(industrialStamp);


            //其它票证  其它票证 (1 过户票、 2 备用钥匙、 3 进口关单、 4 购置税完税证明（征税）、 5 购置税完税证明（免税） ) ，复选（英文逗号分隔）
            //当过户次数为0时，过户票置为不可选择状态；
            String otherBill = procedureModel.getCarInvoiceOther();
            submitModel.setCarInvoiceOther(otherBill);




            //登记证附加信息  此项允许为空   登记证附加信息（ 1 正在抵押、 2 发动机号变更、 3 重打车架号、 4 登记证补领、 5 颜色变更），复选（英文逗号分隔）
            String registerAdditionInfo = procedureModel.getCertificateEx();
            submitModel.setCertificateEx(registerAdditionInfo);

            //登记证附加信息 说明
            submitModel.setCertificateExDes(procedureModel.getCertificateExDes());

            //年检有效期
            submitModel.setInspection(dateSplit(procedureModel.getInspection()));

            //车辆铭牌
            submitModel.setNameplate(procedureModel.getNameplate());


             //行驶证
             int id = procedureModel.getDrivingLicenseProperty();
             submitModel.setDrivingLicenseProperty(id);

             //登记证
             id = procedureModel.getRegistLicenseProperty();
             submitModel.setRegistLicenseProperty(id);
        }

    }

    /**
     * 用网络数据给页面赋值
     */
    private void initNetData() {

        //判断这个任务是否存在检测详情数据
        taskDetailModel = ((DetectMainActivity)this.getActivity()).getTaskDetailModel();
        LogUtil.e(TAG,"网络： "+" taskDetailModel: "+taskDetailModel);
        if(taskDetailModel!=null){
            //无详情，进入手续信息，行驶证、登记证均可见，带入合格证数据，且均可编辑；有详情时，不带入合格证数据，可编辑；
            isJustGetCertificateData = true;

            //图片
            List<TaskDetailModel.ProcedurePicListBean> procedurePicList =  taskDetailModel.getProcedurePicList();
            if(procedurePicList!=null){
                for (int i = 0; i < procedurePicList.size(); i++) {
                    TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                    String picId = procedurePicBean.getPicID();
                    if(!TextUtils.isEmpty(picId)){
                        if("18".equals(picId)){//行驶证正本正面
                            pathDLFront = procedurePicBean.getPicPath();
                            pictureItemsDL.get(0).setPicPath(pathDLFront);
                        }else if("19".equals(picId)){//行驶证正本背面
                            pathDLBack = procedurePicBean.getPicPath();
                            pictureItemsDL.get(1).setPicPath(pathDLBack);
                        }else if("20".equals(picId)){//行驶证副本正面
                            pathDLViceFront = procedurePicBean.getPicPath();
                            pictureItemsDL.get(2).setPicPath(pathDLViceFront);
                        }else if("21".equals(picId)){//登记证 1-2 页照片
                            pathDLRegister1 = procedurePicBean.getPicPath();
                            pictureItemsDLRegister.get(0).setPicPath(pathDLRegister1);
                        }else if("22".equals(picId)){//登记证 3-4 页照片
                            pathDLRegister3 = procedurePicBean.getPicPath();
                            pictureItemsDLRegister.get(1).setPicPath(pathDLRegister3);
                        }else if("23".equals(picId)){//更多登记证照片1
                            pathDLRegisterM = procedurePicBean.getPicPath();
                            registerMorePI.setPicPath(pathDLRegisterM);
                        }else if ("28".equals(picId)){//更多登记证照片2
                            pathDLRegisterM2 = procedurePicBean.getPicPath();
                            registerMorePI2.setPicPath(pathDLRegisterM2);
                        }else if("24".equals(picId)){//行驶证副本背面
                            pathDLViceBack = procedurePicBean.getPicPath();
                            pictureItemsDL.get(3).setPicPath(pathDLViceBack);
                        }
                    }
                }
            }

            TaskDetailModel.BasicBean basicBean =  taskDetailModel.getBasic();
            if(basicBean!=null){
                //如果任务是修改状态，本地数据库又没有保存手续信息的数据
                if(procedureModel==null){
                    procedureModel = new ProcedureModel();
                }
                //重新为submitModel赋上检测详情数据的值
                //行驶证
                submitModel.setDrivingLicenseProperty(basicBean.getDrivingLicenseProperty());
                procedureModel.setDrivingLicenseProperty(basicBean.getDrivingLicenseProperty());

                //登记证
                submitModel.setRegistLicenseProperty(basicBean.getRegistLicenseProperty());
                procedureModel.setRegistLicenseProperty(basicBean.getRegistLicenseProperty());

                //机动车所有人
                submitModel.setCarOwner(basicBean.getCarOwner());
                procedureModel.setCarOwner(basicBean.getCarOwner());
                //证件类型  证件类型（ 0 默认， 1 身份证， 2 军官证， 3 护照， 4 组织机构代码证， 5 车主证件未见），单选
                submitModel.setCardType(basicBean.getCardType());
                procedureModel.setCardType(basicBean.getCardType());
                //车主证件号
                submitModel.setCardNum(basicBean.getCardNum());
                procedureModel.setCardNum(basicBean.getCardNum());
                //登记证与车主证件号  登记证与车主证件号是否一致（ 0 否， 1 是），单选
                submitModel.setIsCardSame(basicBean.getIsCardSame());
                procedureModel.setIsCardSame(basicBean.getIsCardSame());
                //登记日期
                String recordDate = basicBean.getRecordDate();
                if(!TextUtils.isEmpty(recordDate)&&recordDate.startsWith("1900-01-01")){
                    recordDate = "";
                }
                submitModel.setRecordDate(recordDate);
                procedureModel.setRecordDate(recordDate);
                //初登地区  需要名字
                submitModel.setProvName(basicBean.getProvinceName());
                procedureModel.setProvName(basicBean.getProvinceName());
                submitModel.setRegisterprovID(basicBean.getRegisterProvID());
                procedureModel.setRegisterprovID(basicBean.getRegisterProvID());
                submitModel.setCityName(basicBean.getCityName());
                procedureModel.setCityName(basicBean.getCityName());
                submitModel.setRegisterCityID(basicBean.getRegisterCityID());
                procedureModel.setRegisterCityID(basicBean.getRegisterCityID());

                //上牌地区  需要名字
                submitModel.setOnCardProvName(basicBean.getOnCardProvName());
                procedureModel.setOnCardProvName(basicBean.getOnCardProvName());
                submitModel.setOnCardProvID(basicBean.getOnCardProvID());
                procedureModel.setOnCardProvID(basicBean.getOnCardProvID());
                submitModel.setOnCardCityName(basicBean.getOnCardCityName());
                procedureModel.setOnCardCityName(basicBean.getOnCardCityName());
                submitModel.setOnCardCityID(basicBean.getOnCardCityID());
                procedureModel.setOnCardCityID(basicBean.getOnCardCityID());

                //交强险所在地
                submitModel.setInsuranceProvName(basicBean.getInsuranceProvName());
                procedureModel.setInsuranceProvName(basicBean.getInsuranceProvName());
                submitModel.setInsuranceProvID(basicBean.getInsuranceProvID());
                procedureModel.setInsuranceProvID(basicBean.getInsuranceProvID());
                submitModel.setInsuranceCityName(basicBean.getInsuranceCityName());
                procedureModel.setInsuranceCityName(basicBean.getInsuranceCityName());
                submitModel.setInsuranceCityID(basicBean.getInsuranceCityID());
                procedureModel.setInsuranceCityID(basicBean.getInsuranceCityID());

                //车牌号码
                submitModel.setCarLicense(basicBean.getCarLicense());
                procedureModel.setCarLicense(basicBean.getCarLicense());

                //有无车牌  车牌有无（0无车牌、1有车牌、2未悬挂），单选
                submitModel.setCarLicenseEx(basicBean.getCarLicenseEx());
                procedureModel.setCarLicenseEx(basicBean.getCarLicenseEx());
                //车辆型号
                submitModel.setRecordBrand(basicBean.getRecordBrand());
                procedureModel.setRecordBrand(basicBean.getRecordBrand());
                //车架号VIN
                submitModel.setVin(basicBean.getVin());
                procedureModel.setVin(basicBean.getVin());
                //发动机号
                submitModel.setEngineNum(basicBean.getEngineNum());
                procedureModel.setEngineNum(basicBean.getEngineNum());
                //燃料种类  燃料种类： 1 汽油、 2 柴油、 3 混动、 4 天然气、 5 纯电动
                submitModel.setFuelType(basicBean.getFuelType());
                procedureModel.setFuelType(basicBean.getFuelType());
                //核定载客量   核定载客（ 2-9,99 代表“ 9 人以上”），单选
                submitModel.setSeating(basicBean.getSeating());
                procedureModel.setSeating(basicBean.getSeating());
                //获得方式    车辆获得方式（ 1 购买、 2 仲裁裁判、 3 继承、 4 赠与、 5 协议抵偿债务、 6 中奖、 7 资产重组、 8 资产整体买卖、 9 调拨、 10 境外自带、 11 法院调解 / 裁定 / 判决），单选
                submitModel.setCarGetWay(basicBean.getCarGetWay());
                procedureModel.setCarGetWay(basicBean.getCarGetWay());
                //车身颜色   车身颜色 (1 白、 2 灰、 3 红、 4 粉、 5 黄、 6 蓝、 7 绿、 8 紫、 9 棕、 10 黑、11、双色 99 其它  ) ，单选
                submitModel.setColor(basicBean.getColor());//created by wujj on 2016/12/21
                procedureModel.setColor(basicBean.getColor());//created by wujj on 2016/12/21
                //车身颜色其它
                submitModel.setCarColorDes(basicBean.getCarColorDes());
                procedureModel.setCarColorDes(basicBean.getCarColorDes());

                //是否有涡轮增压（ 0 无， 1 有），单选  改为Exhaust是否含有T
                if(basicBean.getExhaust().toUpperCase().contains("T")){
                    submitModel.setTurboChargingHave(1);
                    procedureModel.setTurboChargingHave(1);
                }else{
                    submitModel.setTurboChargingHave(0);
                    procedureModel.setTurboChargingHave(0);
                }
                //轮胎规格
                submitModel.setTyre(basicBean.getTyre());
                procedureModel.setTyre(basicBean.getTyre());
                //使用性质  显示使用性质（ 1 非营运、 2 营转非、 3 出租营转非、 4 营运、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
                // 提交顺序 （ 1 营运、 2 非营运、 3 营转非、 4 出租营转非、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
                submitModel.setService(basicBean.getService());
                procedureModel.setService(basicBean.getService());
                //出厂日期
                String productionTime = basicBean.getProductionTime();
                if(!TextUtils.isEmpty(productionTime)&&productionTime.startsWith("1900-01-01")){
                    productionTime = "";
                }
                if(!TextUtils.isEmpty(productionTime)&&productionTime.length()>=10) {
                    submitModel.setProductionTime(productionTime.substring(0, 10));
                    procedureModel.setProductionTime(productionTime.substring(0, 10));
                }
                //过户次数
                submitModel.setTransferCount(basicBean.getTransferCount());
                procedureModel.setTransferCount(basicBean.getTransferCount());
                //最后过户日期
                String lastTransferDate = basicBean.getLastTransferDate();
                if(!TextUtils.isEmpty(lastTransferDate)&&lastTransferDate.startsWith("1900-01-01")){
                    lastTransferDate = "";
                }
                submitModel.setLastTransferDate(lastTransferDate);
                procedureModel.setLastTransferDate(lastTransferDate);
                //曾使用方   曾使用方 (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
                submitModel.setOldUseOwner(basicBean.getOldUseOwner());
                procedureModel.setOldUseOwner(basicBean.getOldUseOwner());
                //现使用方   现使用方 (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
                submitModel.setNowUseOwner(basicBean.getNowUseOwner());
                procedureModel.setNowUseOwner(basicBean.getNowUseOwner());

                //备用钥匙   (0--0；1--1；2--2及以上；-1--空) ，单选
                submitModel.setSpareKey(basicBean.getSpareKey());
                procedureModel.setSpareKey(basicBean.getSpareKey());

                //排放标准 (国二及以下：1，国三：2，国四：3，国五：4，无法判断：5 )，单选
                submitModel.setEffluentStd(basicBean.getEffluentStd());
                procedureModel.setEffluentStd(basicBean.getEffluentStd());

                //交强险保单   交强险保单 (1 正常、 2 未见、 3 被保险人与车主不一致 ) ，单选
                submitModel.setTrafficInsuranceHave(basicBean.getTrafficInsuranceHave());
                procedureModel.setTrafficInsuranceHave(basicBean.getTrafficInsuranceHave());
                //交强险到期日
                String insuranceDate = basicBean.getInsurance();
                if(!TextUtils.isEmpty(insuranceDate)&&insuranceDate.startsWith("1900-01-01")){
                    insuranceDate = "";
                }
                submitModel.setInsurance(insuranceDate);
                procedureModel.setInsurance(insuranceDate);
                //原车发票
                submitModel.setCarInvoiceMoney(basicBean.getCarInvoiceMoney());
                procedureModel.setCarInvoiceMoney(basicBean.getCarInvoiceMoney());
                //开票日期
                String carInvoiceDate = basicBean.getCarInvoiceDate();
                if(!TextUtils.isEmpty(carInvoiceDate)&&carInvoiceDate.startsWith("1900-01-01")){
                    carInvoiceDate = "";
                }
                submitModel.setCarInvoiceDate(carInvoiceDate);
                procedureModel.setCarInvoiceDate(carInvoiceDate);
                //工商章   原车发票 (1 无工商章、 2 未见， 3 有 ) ，单选
                submitModel.setCarInvoiceHave(basicBean.getCarInvoiceHave());
                procedureModel.setCarInvoiceHave(basicBean.getCarInvoiceHave());
                //其它票证  其它票证 (1 过户票、 2 备用钥匙、 3 进口关单、 4 购置税完税证明（征税）、 5 购置税完税证明（免税） ) ，复选（英文逗号分隔）
                submitModel.setCarInvoiceOther(basicBean.getCarInvoiceOther());
                procedureModel.setCarInvoiceOther(basicBean.getCarInvoiceOther());
                //登记证附加信息  此项允许为空   登记证附加信息（ 1 正在抵押、 2 发动机号变更、 3 重打车架号、 4 登记证补领、 5 颜色变更），复选（英文逗号分隔）
                submitModel.setCertificateEx(basicBean.getCertificateEx());
                procedureModel.setCertificateEx(basicBean.getCertificateEx());
                //登记证附加信息 说明
                submitModel.setCertificateExDes(basicBean.getCertificateExDes());
                procedureModel.setCertificateExDes(basicBean.getCertificateExDes());
                //年检有效期
                String inspection = basicBean.getInspection();
                if(!TextUtils.isEmpty(inspection)&&inspection.startsWith("1900-01-01")){
                    inspection = "";
                }
                submitModel.setInspection(inspection);
                procedureModel.setInspection(inspection);
                if(TextUtils.isEmpty(inspection)){
                    procedureModel.setInspectionSelected(true);
                }else{
                    procedureModel.setInspectionSelected(false);
                }

            }

        }

    }

    /**
     * 判断VIN和出厂日期，如果不为空，车型选择可点击
     *
     * @param etVin
     */
    private void carTypeSelectCanTab(String etVin,String productDate){
        etVin = StringUtils.null2Length0(etVin);
        productDate = StringUtils.null2Length0(productDate);
        //判断VIN和出厂日期，如果不为空，车型选择可点击
        if(etVin.length()==0){
            ((DetectMainActivity)ProcedureCarFragment.this.getActivity()).add(1);
            ((DetectMainActivity)ProcedureCarFragment.this.getActivity()).add(2);
            ((DetectMainActivity)ProcedureCarFragment.this.getActivity()).add(3);
            ((DetectMainActivity)ProcedureCarFragment.this.getActivity()).add(4);
        }else{

        }
    }

    /**
     * 初始经连拍照片
     *
     * @author zealjiang
     * @time 2016/11/29 16:00
     */
    private void initPicData(){

        //初始化照片
        pictureItemsDL = new ArrayList<>();
        pictureItemsDL.add(new PictureItem("18","行驶证正本正面",""));
        pictureItemsDL.add(new PictureItem("19","行驶证正本背面",""));
        pictureItemsDL.add(new PictureItem("20","行驶证副本正面",""));
        pictureItemsDL.add(new PictureItem("24","行驶证副本背面",""));

        pictureItemsDLRegister = new ArrayList<>();
        pictureItemsDLRegister.add(new PictureItem("21","登记证 1-2 页照片",""));
        pictureItemsDLRegister.add(new PictureItem("22","登记证 3-4 页照片",""));


        registerMorePI = new PictureItem("23","更多登记证照片1","");
        registerMorePI2 = new PictureItem("28","更多登记证照片2","");

        pictureItemsDLRegisterMore = new ArrayList<>();
        pictureItemsDLRegisterMore.add(registerMorePI);
        pictureItemsDLRegisterMore.add(registerMorePI2);

        pathDLFront = PadSysApp.picDirPath+"18.jpg";//行驶证正本正面本地图片地址
        pathDLBack = PadSysApp.picDirPath+"19.jpg";//行驶证正本背面本地图片地址
        pathDLViceFront = PadSysApp.picDirPath+"20.jpg";//行驶证副本正面本地图片地址
        pathDLViceBack = PadSysApp.picDirPath+"24.jpg";//行驶证副本背面本地图片地址

        pathDLRegister1 = PadSysApp.picDirPath+"21.jpg";//登记证1-2页照片本地图片地址
        pathDLRegister3 = PadSysApp.picDirPath+"22.jpg";//登记证3-4页照片本地图片地址
        pathDLRegisterM = PadSysApp.picDirPath+"23.jpg";//更多登记证照片本地图片地址
        pathDLRegisterM2 = PadSysApp.picDirPath+"28.jpg";//更多登记证照片本地图片地址

        //行驶证正本正面
        if(FileUtils.isFileExists(pathDLFront)) {
            pictureItemsDL.get(0).setPicPath(pathDLFront);
        }
        //行驶证正本背面
        if(FileUtils.isFileExists(pathDLBack)) {
            pictureItemsDL.get(1).setPicPath(pathDLBack);
        }
        //行驶证副本正面
        if(FileUtils.isFileExists(pathDLViceFront)) {
            pictureItemsDL.get(2).setPicPath(pathDLViceFront);
        }
        //行驶证副本背面
        if(FileUtils.isFileExists(pathDLViceBack)) {
            pictureItemsDL.get(3).setPicPath(pathDLViceBack);
        }
        //登记证1-2页照片
        if(FileUtils.isFileExists(pathDLRegister1)) {
            pictureItemsDLRegister.get(0).setPicPath(pathDLRegister1);
        }
        //登记证3-4页照片
        if(FileUtils.isFileExists(pathDLRegister3)) {
            pictureItemsDLRegister.get(1).setPicPath(pathDLRegister3);
        }
        //更多登记证照片1
        if(FileUtils.isFileExists(pathDLRegisterM)) {
            registerMorePI.setPicPath(pathDLRegisterM);
            pictureItemsDLRegisterMore.get(0).setPicPath(pathDLRegisterM);
        }
        //更多登记证照片2
        if(FileUtils.isFileExists(pathDLRegisterM2)) {
            registerMorePI2.setPicPath(pathDLRegisterM2);
            pictureItemsDLRegisterMore.get(1).setPicPath(pathDLRegisterM2);
        }

    }

    /**
     * 跳转自定义相机统一入口
     */
    private void userCamera(int position,int capture_type,ArrayList<PictureItem> listPic,int requestCode){
        Intent intent = new Intent(context,CameraActivity.class);
        intent.putExtra("showGallery",true);//是否显示从相册选取的图标
        intent.putExtra("taskId",taskId);
        intent.putExtra(Constants.CAPTURE_TYPE,capture_type);//拍摄模式，是单拍还是连拍
        //高质量图片的ID
        intent.putStringArrayListExtra("highQualityPicIdArray",highQualityPicIdArray);
        if(capture_type==Constants.CAPTURE_TYPE_MULTI){
            intent.putExtra("picList",listPic);
            intent.putExtra("position",position);
        }else{
            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem newPic = new PictureItem();
            newPic.setPicId(listPic.get(position).getPicId());
            newPic.setPicName(listPic.get(position).getPicName());
            singleList.add(newPic);
            intent.putExtra("picList",singleList);
            intent.putExtra("position",0);
        }
        startActivityForResult(intent,requestCode);
    }

    /**
     * 单拍 预览或拍照
     *
     * @param requestCodeCamera  拍照请求码
     * @param requestCodePreview 查看大图请求码
     * @author zealjiang
     * @time 2016/12/15 18:12
     */
    private void takeSingleCamera(int requestCodeCamera,int requestCodePreview,PictureItem pictureItem){
        String picPath = pictureItem.getPicPath();
        if(TextUtils.isEmpty(picPath)){
            Intent intent = new Intent(context,CameraActivity.class);
            intent.putExtra("showGallery",true);//是否显示从相册选取的图标
            intent.putExtra("taskId",taskId);
            intent.putExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_SINGLE);//拍摄模式，单拍
            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem newPic = new PictureItem();
            newPic.setPicId(pictureItem.getPicId());
            newPic.setPicName(pictureItem.getPicName());
            singleList.add(newPic);
            intent.putExtra("picList",singleList);
            intent.putExtra("position",0);
            startActivityForResult(intent,requestCodeCamera);
        }else{

            Intent intent = new Intent(context,PictureZoomActivity.class);
            intent.putExtra("url",picPath);
            intent.putExtra("showRecapture",true);
            intent.putExtra("pictureItems",pictureItemsDLRegisterMore);
            intent.putExtra("curPosition",curPicPos);
            intent.putExtra("isDel",true);
            intent.putExtra("taskid",((DetectMainActivity)getActivity()).getTaskid());
            startActivityForResult(intent,requestCodePreview);
        }
    }


    /**
     * 拍照
     * @author zealjiang
     * @time 2016/11/29 16:34
     * @param picList 连续拍照的数组数据
     * @param requestCodeCamera  拍照请求码
     * @param requestCodePreview 查看大图请求码
     */
    private void takePic(ArrayList<PictureItem> picList,int requestCodeCamera,int requestCodePreview){

        String picPath = picList.get(curPicPos).getPicPath();
        if(TextUtils.isEmpty(picPath)){
            userCamera(curPicPos,Constants.CAPTURE_TYPE_MULTI,picList,requestCodeCamera);
        }else{

            Intent intent = new Intent(context,PictureZoomActivity.class);
            intent.putExtra("url",picPath);
            intent.putExtra("showRecapture",true);
            intent.putExtra("pictureItems",picList);
            intent.putExtra("curPosition",curPicPos);
            intent.putExtra("taskid",((DetectMainActivity)getActivity()).getTaskid());
            intent.putStringArrayListExtra("highQualityPicIdArray",highQualityPicIdArray);
            startActivityForResult(intent,requestCodePreview);
        }
    }

    /**
     * 刷新行驶证图片
     *
     * @author zealjiang
     * @time 2016/11/29 18:11
     */
    public void refreshDLPic(){
        //行驶证正本正面
        //如果车辆照片拍了，本地对应应该有相应的照片，处理这种情况
        if(FileUtils.isFileExists(PadSysApp.picDirPath+"18.jpg")){
            pictureItemsDL.get(0).setPicPath(PadSysApp.picDirPath+"18.jpg");
        }
        pathDLFront = pictureItemsDL.get(0).getPicPath();
        if(!pathDLFront.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLFront, false);
        }
        if(!TextUtils.isEmpty(pathDLFront)) {
            if(pathDLFront.startsWith("http")){
                frescoImageLoader.displayImage(sdvDrivingLicenceFront,pathDLFront);
            }else if(FileUtils.isFileExists(pathDLFront)){
                frescoImageLoader.displayImage(sdvDrivingLicenceFront,pathDLFront);
            }else{
                sdvDrivingLicenceFront.setImageResource(R.drawable.xiangji);
            }
        }else{
            sdvDrivingLicenceFront.setImageResource(R.drawable.xiangji);
        }
        //行驶证正本背面
        if(FileUtils.isFileExists(PadSysApp.picDirPath+"19.jpg")){
            pictureItemsDL.get(1).setPicPath(PadSysApp.picDirPath+"19.jpg");
        }
        pathDLBack = pictureItemsDL.get(1).getPicPath();
        if(!pathDLBack.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLBack, false);
        }
        if(!TextUtils.isEmpty(pathDLBack)) {
            if(pathDLBack.startsWith("http")){
                frescoImageLoader.displayImage(sdvDrivingLicenceBack,pathDLBack);
            }else if(FileUtils.isFileExists(pathDLBack)){
                frescoImageLoader.displayImage(sdvDrivingLicenceBack,pathDLBack);
            }else{
                sdvDrivingLicenceBack.setImageResource(R.drawable.xiangji);
            }
        }else{
            sdvDrivingLicenceBack.setImageResource(R.drawable.xiangji);
        }
        //行驶证副本正面
        if(FileUtils.isFileExists(PadSysApp.picDirPath+"20.jpg")){
            pictureItemsDL.get(2).setPicPath(PadSysApp.picDirPath+"20.jpg");
        }
        pathDLViceFront = pictureItemsDL.get(2).getPicPath();
        if(!pathDLViceFront.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLViceFront, false);
        }
        if(!TextUtils.isEmpty(pathDLViceFront)) {
            if(pathDLViceFront.startsWith("http")||FileUtils.isFileExists(pathDLViceFront)){
                frescoImageLoader.displayImage(sdvDrivingLicenceViceFront,pathDLViceFront);
            }else{
                sdvDrivingLicenceViceFront.setImageResource(R.drawable.xiangji);
            }
        }else{
            sdvDrivingLicenceViceFront.setImageResource(R.drawable.xiangji);
        }
        //行驶证副本背面
        if(FileUtils.isFileExists(PadSysApp.picDirPath+"24.jpg")){
            pictureItemsDL.get(3).setPicPath(PadSysApp.picDirPath+"24.jpg");
        }
        pathDLViceBack = pictureItemsDL.get(3).getPicPath();
        if(!pathDLViceBack.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLViceBack, false);
        }
        if(!TextUtils.isEmpty(pathDLViceBack)) {
            if(pathDLViceBack.startsWith("http")||FileUtils.isFileExists(pathDLViceBack)) {
                frescoImageLoader.displayImage(sdvDrivingLicenceViceBack, pathDLViceBack);
            }else{
                sdvDrivingLicenceViceBack.setImageResource(R.drawable.xiangji);
            }
        }else{
            sdvDrivingLicenceViceBack.setImageResource(R.drawable.xiangji);
        }

    }

    /**
     * 刷新登记证图片
     * @author zealjiang
     * @time 2016/11/29 18:11
     */
    public synchronized void  refreshDLRegisterPic(){
        //登记证1-2页照片
        //如果车辆照片拍了，本地对应应该有相应的照片，处理这种情况
        if(FileUtils.isFileExists(PadSysApp.picDirPath+"21.jpg")){
            pictureItemsDLRegister.get(0).setPicPath(PadSysApp.picDirPath+"21.jpg");
        }
        pathDLRegister1 = pictureItemsDLRegister.get(0).getPicPath();
        if(!pathDLRegister1.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLRegister1, false);
        }
        if(!TextUtils.isEmpty(pathDLRegister1)) {
            if(pathDLRegister1.startsWith("http")||FileUtils.isFileExists(pathDLRegister1)) {
                frescoImageLoader.displayImage(sdvRegister1, pathDLRegister1);
            }else{
                sdvRegister1.setImageResource(R.drawable.xiangji);
            }
        }else{
            sdvRegister1.setImageResource(R.drawable.xiangji);
        }
        //登记证3-4页照片
        if(FileUtils.isFileExists(PadSysApp.picDirPath+"22.jpg")){
            pictureItemsDLRegister.get(1).setPicPath(PadSysApp.picDirPath+"22.jpg");
        }
        pathDLRegister3 = pictureItemsDLRegister.get(1).getPicPath();
        if(!pathDLRegister3.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLRegister3, false);
        }
        if(!TextUtils.isEmpty(pathDLRegister3)) {
            if(pathDLRegister3.startsWith("http")||FileUtils.isFileExists(pathDLRegister3)) {
                frescoImageLoader.displayImage(sdvRegister3, pathDLRegister3);
            }else{
                sdvRegister3.setImageResource(R.drawable.xiangji);
            }
        }else{
            sdvRegister3.setImageResource(R.drawable.xiangji);
        }


        //更多登记证照片1、2
        if (FileUtils.isFileExists(PadSysApp.picDirPath + "23.jpg")) {
            registerMorePI.setPicPath(PadSysApp.picDirPath + "23.jpg");
        }
        pathDLRegisterM = registerMorePI.getPicPath();
        if (!pathDLRegisterM.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLRegisterM, false);
        }


        if (FileUtils.isFileExists(PadSysApp.picDirPath + "28.jpg")) {
            registerMorePI2.setPicPath(PadSysApp.picDirPath + "28.jpg");
        }
        pathDLRegisterM2 = registerMorePI2.getPicPath();
        if (!pathDLRegisterM2.startsWith("http")) {
            FrescoCacheHelper.clearSingleCacheByUrl(pathDLRegisterM2, false);
        }

        if (TextUtils.isEmpty(pathDLRegisterM) && !TextUtils.isEmpty(pathDLRegisterM2)) {

            if (FileUtils.isFileExists(PadSysApp.picDirPath + "28.jpg")) {
                FileUtils.copyFile(PadSysApp.picDirPath + "28.jpg", PadSysApp.picDirPath +"23.jpg");
                FileUtils.deleteFile(PadSysApp.picDirPath + "28.jpg");
                registerMorePI.setPicPath(PadSysApp.picDirPath + "23.jpg");
            } else {
                registerMorePI.setPicPath(pathDLRegisterM2);
            }

            if(procedureModel.getDeletePicId().contains("23")){
                procedureModel.getDeletePicId().remove("23");
            }

            if(!procedureModel.getDeletePicId().contains("28")){
                procedureModel.getDeletePicId().add("28");

                if(!submitModel.getDeletePicId().contains("28")) {
                    submitModel.getDeletePicId().add("28");
                }
            }

            registerMorePI2.setPicPath("");


            //如果存在网络图片地址，则显示网络图片
            List<TaskDetailModel.ProcedurePicListBean> procedurePicList = procedureModel.getProcedurePicList();
            if(procedurePicList!=null) {
                for (int i = 0; i < procedurePicList.size(); i++) {
                    TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                    String picId = procedurePicBean.getPicID();
                    if (!TextUtils.isEmpty(picId)) {
                        if("23".equals(picId)) {//更多登记证照片1
                            procedurePicBean.setPicPath(registerMorePI.getPicPath());
                        }else if("28".equals(picId)){
                            procedurePicBean.setPicPath("");
                        }
                    }
                }
            }
        }

        pathDLRegisterM = registerMorePI.getPicPath();
        pathDLRegisterM2 = registerMorePI2.getPicPath();

        if (!TextUtils.isEmpty(pathDLRegisterM)&&TextUtils.isEmpty(pathDLRegisterM2)){
            frescoImageLoader.displayImage(sdvRegisterMore, pathDLRegisterM);
            llRegisterMore2.setVisibility(View.VISIBLE);
            sdvRegisterMore2.setImageResource(R.drawable.jiahaopic);
        }else if(!TextUtils.isEmpty(pathDLRegisterM)&&!TextUtils.isEmpty(pathDLRegisterM2)){
            frescoImageLoader.displayImage(sdvRegisterMore, pathDLRegisterM);
            llRegisterMore2.setVisibility(View.VISIBLE);
            frescoImageLoader.displayImage(sdvRegisterMore2, pathDLRegisterM2);
        }else if(TextUtils.isEmpty(pathDLRegisterM)&&TextUtils.isEmpty(pathDLRegisterM2)){
            sdvRegisterMore.setImageResource(R.drawable.jiahaopic);
            llRegisterMore2.setVisibility(View.GONE);
        }


    }

    /**
     * 根据服务器返回的配置项决定显示哪些栏信息
     *
     * @author zealjiang
     * @time 2016/11/23 16:02
     */
    private void configItem(){
        if(detectionWrapper==null){
            return;
        }
        List<String> configList =  detectionWrapper.getProcedureList();

        //TODO 测试代码模拟 登记证 和 行驶证 未见的情况
//        if(configList.contains("81"))
//            configList.remove("81");
//        if(configList.contains("82"))
//            configList.remove("82");

        for (int i = 0; i < configList.size(); i++) {

            switch (configList.get(i)){
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

        if(!configDriving){
            llDriving.setVisibility(View.GONE);
        }
        if(!configRegister){
            vLineRegister.setVisibility(View.GONE);
            llRegister.setVisibility(View.GONE);
        }
        if(!configOwener){
            vLineVehicleOwener.setVisibility(View.GONE);
            llVehicleOwener.setVisibility(View.GONE);
        }
        if(!configCardType){
            vLineCard.setVisibility(View.GONE);
            llCard.setVisibility(View.GONE);
        }
        if(!configCardId){
            vLineOwnerId.setVisibility(View.GONE);
            llOwnerId.setVisibility(View.GONE);
        }
        if(!configRegisterSame){
/*            vLineRegisterId.setVisibility(View.GONE);
            llRegisterId.setVisibility(View.GONE);*/
        }
        if(!configRegisterDate){
            vLineRegisterDate.setVisibility(View.GONE);
            llRegisterDate.setVisibility(View.GONE);
        }
        if(!configRegion){
            vLineRegisterRegion.setVisibility(View.GONE);
            llRegisterRegion.setVisibility(View.GONE);
        }
        if(!configPlateRegion){//上牌地区
            vLinePlateRegion.setVisibility(View.GONE);
            llPlateRegion.setVisibility(View.GONE);
        }
        if(!configPlateNum){
            vLinePlateId.setVisibility(View.GONE);
            llPlateId.setVisibility(View.GONE);
        }
        if(!configCarType){
            vLineCarType.setVisibility(View.GONE);
            llCarType.setVisibility(View.GONE);
        }
        if(!configVin){
            vLineVin.setVisibility(View.GONE);
            llVin.setVisibility(View.GONE);
        }
        if(!configEngineId){
            vLineEngineId.setVisibility(View.GONE);
            llEngineId.setVisibility(View.GONE);
        }
        if(!configFuelType){
            vLineFuelType.setVisibility(View.GONE);
            llFuelType.setVisibility(View.GONE);
        }
        if(!configLoadNum){
            vLineLoadNum.setVisibility(View.GONE);
            llLoadNum.setVisibility(View.GONE);
        }
        if(!configObtainWay){
            vLineObtainWay.setVisibility(View.GONE);
            llObtainWay.setVisibility(View.GONE);
        }
        if(!configCarColor){
            vLineCarColor.setVisibility(View.GONE);
            llCarColor.setVisibility(View.GONE);
        }
        if(!configIsMadeInChina){
            vLineIsMadeInChina.setVisibility(View.GONE);
            llIsMadeInChina.setVisibility(View.GONE);
        }
        if(!configDisplacement){
            vLineDisplacement.setVisibility(View.GONE);
            llDisplacement.setVisibility(View.GONE);
        }
        if(!configTireType){
            vLineTireModel.setVisibility(View.GONE);
            llTireModel.setVisibility(View.GONE);
        }
        if(!configUseNature){
            vLineUseNature.setVisibility(View.GONE);
            llUseNature.setVisibility(View.GONE);
        }
        if(!configProductDate){
            vLineProductDate.setVisibility(View.GONE);
            llProductDate.setVisibility(View.GONE);
        }
        if(!configTransferNum){
            vLineTransferNum.setVisibility(View.GONE);
            llTransferNum.setVisibility(View.GONE);
        }
        if(!configLastTransferDate){
            vLineLastTransferDate.setVisibility(View.GONE);
            llLastTransferDate.setVisibility(View.GONE);
        }
        if(!configOldUser){
            vLineOldUser.setVisibility(View.GONE);
            llOldUser.setVisibility(View.GONE);
        }
        if(!configCurUser){
            vLineCurUser.setVisibility(View.GONE);
            llCurUser.setVisibility(View.GONE);
        }
        if(!configInsuranceBill){
            vLineInsuranceBill.setVisibility(View.GONE);
            llInsuranceBill.setVisibility(View.GONE);
        }
        if(!configInsuranceExpDate){
            vLineInsuranceExpDate.setVisibility(View.GONE);
            llInsuranceExpDate.setVisibility(View.GONE);
        }
        if(!configInsuranceAddress){
            vLineInsuranceAddress.setVisibility(View.GONE);
            llInsuranceAddress.setVisibility(View.GONE);
        }
        if(!configCarBill){
            vLineOriginalBill.setVisibility(View.GONE);
            llOriginalBill.setVisibility(View.GONE);
        }
        if(!configOtherBill){
            vLineOtherBill.setVisibility(View.GONE);
            llOtherBill.setVisibility(View.GONE);
        }
        if(!configRegisterAdditionInfo){
            vLineRegisterAdditionInfo.setVisibility(View.GONE);
            llRegisterAdditionInfo.setVisibility(View.GONE);
        }
        if(!configAnnualDate){
            vLineAnnualDate.setVisibility(View.GONE);
            llAnnualDate.setVisibility(View.GONE);
        }
        if(!configExtraKey){
            vLineExtraKey.setVisibility(View.GONE);
            llExtraKey.setVisibility(View.GONE);
        }
        if(!configEmissionStandard){//排放标准
            vLineEmissionStandard.setVisibility(View.GONE);
            llEmissionStandard.setVisibility(View.GONE);
        }
    }

    /**
     * 删除详情里的行驶证图片
     *
     * @author zealjiang
     * @time 2016/12/20 14:09
     */
    private void deleteDrivngDetailPhoto(){
        if(taskDetailModel!=null) {
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
     * 删除详情里的登记证图片
     *
     * @author zealjiang
     * @time 2016/12/20 14:09
     */
    private void deleteRegisterDetailPhoto(){
        if(taskDetailModel!=null) {
            //图片
            List<TaskDetailModel.ProcedurePicListBean> procedurePicList = taskDetailModel.getProcedurePicList();
            if (procedurePicList != null) {
                for (int i = 0; i < procedurePicList.size(); i++) {
                    TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                    String picId = procedurePicBean.getPicID();
                    if (!TextUtils.isEmpty(picId)) {
                        if ("21".equals(picId)) {//登记证 1-2 页照片
                            procedurePicList.remove(i);
                            i--;
                        } else if ("22".equals(picId)) {//登记证 3-4 页照片
                            procedurePicList.remove(i);
                            i--;
                        } else if ("23".equals(picId)) {//更多登记证照片
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
     * 行驶证未见选中
     */
    private void drivingLicenceChecked(){
        if(procedureModel == null){
            procedureModel = new ProcedureModel();
        }

        //删除照片
        ArrayList<String> deleteArray = new ArrayList<>();
        for (int i = 0; i < pictureItemsDL.size(); i++) {
            deleteArray.add(pictureItemsDL.get(i).getPicPath());

            if(!procedureModel.getDeletePicId().contains(pictureItemsDL.get(i).getPicId())){
                procedureModel.getDeletePicId().add(pictureItemsDL.get(i).getPicId());
            }

            if(submitModel != null && submitModel.getDeletePicId() != null){
                if(!submitModel.getDeletePicId().contains(pictureItemsDL.get(i).getPicId())){
                    submitModel.getDeletePicId().add(pictureItemsDL.get(i).getPicId());
                }
            }
        }
        FrescoCacheHelper.clearMultiCaches(deleteArray,true);
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
        cBoxDrivingLicenceFlaw.setChecked(false);
        cBoxDrivingLicenceFlaw.setClickable(false);

        HashSet<Integer> set = new HashSet();
        //年检有效期 选择行驶证未见时，此字段置为不可选择状态，并清空已选择信息；
        tvAnnualInspection.setEnabled(false);
        tvAnnualInspection.setText("");

        eventProcedureModel.setDrivingNoneIsSelect(true);
        EventBus.getDefault().post(eventProcedureModel);

        //识别
        tvIdentification.setEnabled(false);

        //四张行驶证照片的文字颜色置灰
        tvDriLicFront.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvDriLicBack.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvDriLicViceFront.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvDriLicViceBack.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        //行驶证有瑕疵文字颜色置灰
        cBoxDrivingLicenceFlaw.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        cBoxDrivingLicenceFlaw.setBackgroundResource(R.drawable.btn_bg_unable);

        //车牌号码
        carPlateNum();

        //行驶证未见和登记证未见同时选中，登记日期、品牌型号、发动机号、使用性质、燃料种类、轮胎规格、核定载客数，
        //不可输入并清空已输入信息，提交时可以为空；
        if(cBoxRegisterNone.isChecked()){
            set = null;
            //隐藏登记日期
            tvRegisterDate.setText("");
            tvRegisterDate.setEnabled(false);

            //品牌型号  合格证数据
            etCarType.setText("");
            etCarType.setEnabled(false);

            //发动机号 合格证数据
            etEngineId.setText("");
            etEngineId.setEnabled(false);

            //使用性质
            tflShiyongxingzhi.getAdapter().setSelectedList(set);
            setTagViewState(tflShiyongxingzhi,false);

            //燃料种类
            tflFuelType.getAdapter().setSelectedList(set);
            setTagViewState(tflFuelType,false);

            //隐藏轮胎规格
            etTireModel.setText("");
            etTireModel.setEnabled(false);

            //额定载客
            tflBusload.getAdapter().setSelectedList(set);
            setTagViewState(tflBusload,false);

            //选中登记证未见（行驶证可见），燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；
            setDisableByRejesterIsNone(true);

            setDisableByDrivingIsNone(true);
        }

    }

    /**
     * 登记证未见选中
     */
    private void registerLicenceChecked(){


        //选中登记证未见（行驶证可见），燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；
        setDisableByRejesterIsNone(true);


        etDisplacement.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);//create by wujj on 2016/12/16
        //删除照片
        ArrayList<String> deleteArray = new ArrayList<>();
        for (int i = 0; i < pictureItemsDLRegister.size(); i++) {
            deleteArray.add(pictureItemsDLRegister.get(i).getPicPath());

            if(!procedureModel.getDeletePicId().contains(pictureItemsDLRegister.get(i).getPicId())){
                procedureModel.getDeletePicId().add(pictureItemsDLRegister.get(i).getPicId());
            }

            if(submitModel != null && submitModel.getDeletePicId() != null){
                if(!submitModel.getDeletePicId().contains(pictureItemsDLRegister.get(i).getPicId())){
                    submitModel.getDeletePicId().add(pictureItemsDLRegister.get(i).getPicId());
                }
            }

        }
        FrescoCacheHelper.clearMultiCaches(deleteArray, true);
        for (int i = 0; i < pictureItemsDLRegister.size(); i++) {
            pictureItemsDLRegister.get(i).setPicPath("");
        }

        //删除登记证更多照片1
        FrescoCacheHelper.clearSingleCacheByUrl(registerMorePI.getPicPath(),true);
        registerMorePI.setPicPath("");
        //删除登记证更多照片2
        FrescoCacheHelper.clearSingleCacheByUrl(registerMorePI2.getPicPath(),true);
        registerMorePI2.setPicPath("");

        //刷新登记证照片
        refreshDLRegisterPic();


        //如果是修改进来的同时要删除掉详情里的登记证照片
        deleteRegisterDetailPhoto();

        sdvRegister1.setClickable(false);
        sdvRegister3.setClickable(false);
        sdvRegisterMore.setClickable(false);
        cBoxRegisterFlaw.setChecked(false);
        cBoxRegisterFlaw.setClickable(false);


        //隐藏机动车所有人一栏   并清空
        etVehicleOwener.setText("");
        etVehicleOwener.setEnabled(false);
        //隐藏证件类型一栏
        Set<Integer> set = null;
        tflCardType.getAdapter().setSelectedList(set);
        //tflCardType.setEnabled(false);
        setTagViewState(tflCardType,false);
        //隐藏车主证件号一栏
        etOwnerId.setText("");
        etOwnerId.setEnabled(false);
        tflCheZhuZhengJian.getAdapter().setSelectedList(set);
        setTagViewState(tflCheZhuZhengJian,false);

        //燃料种类
        tflFuelType.getAdapter().setSelectedList(set);
        setTagViewState(tflFuelType,false);


        //隐藏初登地区
        tvRegisterRegion.setText("");
        tvRegisterRegion.setEnabled(false);

        if(submitModel==null){
            SubmitModel submitModel = ((DetectMainActivity)this.getActivity()).getSubmitModel();
            if(submitModel==null){
                //如果还获取不到就退出DetectMainActivity
                this.getActivity().finish();
                return;
            }
        }
        submitModel.setRegisterprovID(-1);
        submitModel.setProvName("");
        submitModel.setRegisterCityID(-1);
        submitModel.setCityName("");

        if(procedureModel!=null) {
            procedureModel.setRegisterprovID(-1);
            procedureModel.setProvName("");
            procedureModel.setRegisterCityID(-1);
            procedureModel.setCityName("");
        }

        //上牌地区
        carPlateNum();


        //隐藏获得方式
        tflHuodeFangshi.getAdapter().setSelectedList(set);
        setTagViewState(tflHuodeFangshi,false);

        //隐藏进口国产
        tflJinkouGuochan.getAdapter().setSelectedList(set);
        setTagViewState(tflJinkouGuochan,false);

        //隐藏轮胎规格
        etTireModel.setText("");
        etTireModel.setEnabled(false);

        //隐藏过户次数
        etTransferNum.setEnabled(false);
        etTransferNum.setText("");


        //隐藏最后过户日期
        tvLastTransferDate.setEnabled(false);
        tvLastTransferDate.setText("");

        //隐藏登记证附加信息
        etRegisterAdditionInfo.setText("");
        etRegisterAdditionInfo.setEnabled(false);
        tflDJZFujiaxinxi.getAdapter().setSelectedList(set);
        setTagViewState(tflDJZFujiaxinxi,false);

        //隐藏车身颜色
        tflCarColor.getAdapter().setSelectedList(set);
        setTagViewState(tflCarColor,false);
        //车身其他颜色
        etCarColor.setText("");
        etCarColor.setVisibility(View.GONE);

        if(cBoxRegisterNone.isChecked()&&cBoxDrivingNone.isChecked()){
            //登记日期
            tvRegisterDate.setText("");
            tvRegisterDate.setEnabled(false);

            //品牌型号
            etCarType.setText("");
            etCarType.setEnabled(false);

            //发动机号
            etEngineId.setText("");
            etEngineId.setEnabled(false);

            //使用性质
            tflShiyongxingzhi.getAdapter().setSelectedList(set);
            setTagViewState(tflShiyongxingzhi,false);
        }

        //额定载客
        tflBusload.getAdapter().setSelectedList(set);
        setTagViewState(tflBusload,false);

        //曾使用方
        tflCengshiyongfang.getAdapter().setSelectedList(set);
        setTagViewState(tflCengshiyongfang,false);
        //现使用方
        tflXianshiyongfang.getAdapter().setSelectedList(set);
        setTagViewState(tflXianshiyongfang,false);


        eventProcedureModel.setRegisterNoneIsSelected(true);
        EventBus.getDefault().post(eventProcedureModel);

        //四张登记证照片的文字颜色置灰
        tvRegister1.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvRegister3.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        tvRegisterMore.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        //登记证有瑕疵文字颜色置灰
        cBoxRegisterFlaw.setTextColor(getResources().getColor(R.color.common_gray_dark_middle));
        cBoxRegisterFlaw.setBackgroundResource(R.drawable.btn_bg_unable);

        //车牌号码
        carPlateNum();

    }

    /**
     * 行驶证照片是否为空，空返回true,不为空返回false
     *
     * @return
     */
    private boolean isDrivingLPicEmpty(){
        for (int i = 0; i < pictureItemsDL.size(); i++) {
            if(!TextUtils.isEmpty(pictureItemsDL.get(i).getPicPath())){
                return false;
            }
        }
        return true;
    }

    /**
     * 登记证照片是否为空，空返回true,不为空返回false
     *
     * @return
     */
    private boolean isRegisterLPicEmpty(){
        for (int i = 0; i < pictureItemsDLRegister.size(); i++) {
            if(!TextUtils.isEmpty(pictureItemsDLRegister.get(i).getPicPath())){
                return false;
            }
        }
        if(!TextUtils.isEmpty(registerMorePI.getPicPath())){
            return false;
        }
        if(!TextUtils.isEmpty(registerMorePI2.getPicPath())){
            return false;
        }
        return true;
    }



    @OnClick({R.id.tv_identification,R.id.iv_dri_lic_front,R.id.iv_dri_lic_back,R.id.iv_dri_lic_vice_front,R.id.iv_dri_lic_vice_back,
            R.id.sdv_register_1,R.id.sdv_register_3,R.id.sdv_register_more,R.id.sdv_register_more2,R.id.tv_register_region,R.id.tv_plate_region,
            R.id.tv_region_simple_name,R.id.tv_compulsory_insurance_address})
    void onClickListener(View view){
        switch (view.getId()){
            case R.id.tv_identification:
                //行驶证识别
                //判断是否有行驶证正本正面图片
                if(TextUtils.isEmpty(pictureItemsDL.get(0).getPicPath())){
                    MyToast.showShort("请先拍摄行驶证正本正面");
                }else {
                    Intent intent = new Intent(context, DrivingLicenceIdentifyActivity.class);
                    intent.putExtra("path", pathDLFront);
                    intent.putExtra("isShowVIN", !taskHasVIN());//是否显示VIN
                    startActivityForResult(intent, REQUEST_CODE_CAR_LICENSE);
                }
                break;
            case R.id.iv_dri_lic_front:
                curPicPos = 0;
                takePic(pictureItemsDL,PHOTO_REQUEST_DL,PHOTO_BIG_PHOTO_DL);
                //提示
//                String picPath = pictureItemsDL.get(curPicPos).getPicPath();
//                if(TextUtils.isEmpty(picPath)){
//                    MyToast.showShort("请横持设备拍摄行驶证正本正面");
//                }
                break;
            case R.id.iv_dri_lic_back:
                //行驶证正本背面
                curPicPos = 1;
                takePic(pictureItemsDL,PHOTO_REQUEST_DL,PHOTO_BIG_PHOTO_DL);
                break;
            case R.id.iv_dri_lic_vice_front:
                //行驶证副本正面
                curPicPos = 2;
                takePic(pictureItemsDL,PHOTO_REQUEST_DL,PHOTO_BIG_PHOTO_DL);
                break;
            case R.id.iv_dri_lic_vice_back:
                //行驶证副本背面
                curPicPos = 3;
                takePic(pictureItemsDL,PHOTO_REQUEST_DL,PHOTO_BIG_PHOTO_DL);
                break;
            case R.id.sdv_register_1:
                //登记证1-2页照片
                curPicPos = 0;
                takePic(pictureItemsDLRegister,PHOTO_REQUEST_DLREG,PHOTO_BIG_PHOTO_DLREG);
                break;
            case R.id.sdv_register_3:
                //登记证3-4页照片
                curPicPos = 1;
                takePic(pictureItemsDLRegister,PHOTO_REQUEST_DLREG,PHOTO_BIG_PHOTO_DLREG);
                break;
            case R.id.sdv_register_more:
                //更多登记证照片1
                curPicPos = 0;
                takeSingleCamera(PHOTO_RL_MORE_SINGLE,PHOTO_RL_MORE_BIG,registerMorePI);
                break;
            case R.id.sdv_register_more2:
                //更多登记证照片2
                curPicPos = 1;
                takeSingleCamera(PHOTO_RL_MORE_SINGLE2,PHOTO_RL_MORE_BIG2,registerMorePI2);
                break;
            case R.id.tv_register_region:
                //初登地区
                if(!PadSysApp.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }
                Intent intent = new Intent(self.getActivity(),ProvinceCitySelectActivity.class);
                startActivityForResult(intent,REQUEST_PROVINCE_CITY_CODE);
                break;
            case R.id.tv_plate_region://上牌地区
                //上牌地区
/*                if(!PadSysApp.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }*/
                intent = new Intent(self.getActivity(),ProvinceCitySelectActivity.class);
                startActivityForResult(intent,REQUEST_PROVINCE_CITY_CODE_PLATE);
                break;
            case R.id.tv_compulsory_insurance_address://交强险所在地
                //交强险所在地
                intent = new Intent(self.getActivity(),ProvinceCitySelectActivity.class);
                startActivityForResult(intent,REQUEST_PROVINCE_CITY_CODE_INSURANCE_ADDRESS);
                break;
            case R.id.tv_region_simple_name:
                //车辆号牌首字母
                ArrayList<String> listData = LicencePlateAddress.getInstance().getSimpleName();
                final String[] sArray = listData.toArray(new String[listData.size()]);
                int checkedId = 0;
                String simpleName = tvRegionSimpleName.getText().toString().trim();
                if(!TextUtils.isEmpty(simpleName)){
                    for(int i=0;i<sArray.length;i++){
                        if(sArray[i].equals(simpleName)){
                            checkedId = i;
                            break;
                        }
                    }
                }

                final MyUniversalDialog dialog = new MyUniversalDialog(context);
                View layoutView = dialog.getLayoutView(R.layout.region_simple_name);
                final TagFlowLayout tflRegionSimpleName = (TagFlowLayout) layoutView.findViewById(R.id.tflRegionSimpleName);
                setRegionSimpleNameTag(sArray,tflRegionSimpleName);
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
                        //根据车牌号码查询上牌地区
                        String shortName = tvRegionSimpleName.getText().toString();
                        String plateNo = etPlateNo.getText().toString();
                        if(StringUtils.isEmpty(plateNo)) {//etPlateNo值为空会报查询参数错误
                            plateNo = "A";
                        }
                        provinceCityUniquePresenter.getProvinceCityUnique(shortName + plateNo);

                        return false;
                    }
                });

                break;

        }
    }

    /**
     * 切换 TagFlowLayout 里 item 多选 或 单选 的状态
     *
     * @param position      当前点击的 item 角标
     * @param control       控制项的角标，checked--->true：其他变为不可选状态: false:其他变为可选状态
     * @param tagV          当前Item
     * @param tagFlowLayout 当前tagFlowLayout
     */
    public void changeTagViewEnabled(int position, int control, TagView tagV, TagFlowLayout tagFlowLayout) {
        if (position == control && tagV.isChecked()) {

            for (int i = 0; i < tagFlowLayout.getChildCount(); i++) {
                if (i != control) {
                    TagView tag = (TagView) tagFlowLayout.getChildAt(i);
                    tag.isEnabled = false;
                    tag.setChecked(false);

                    //设置item背景为灰色不可选状态
                    TextView tv = (TextView) tag.getTagView();
                    tv.setBackgroundResource(R.drawable.not_optional_bg1);

                    if (tagFlowLayout.getSelectedList().contains(i)) {
                        tagFlowLayout.getSelectedList().remove(i);
                    }
                }
            }
        } else if (position == control && !tagV.isChecked()) {
            for (int i = 0; i < tagFlowLayout.getChildCount(); i++) {
                if (i != control) {
                    TagView tag = (TagView) tagFlowLayout.getChildAt(i);
                    tag.isEnabled = true;

                    //设置item背景为初始化的状态选择器
                    TextView tv = (TextView) tag.getTagView();
                    Drawable drawable = context.getResources().getDrawable(R.drawable.tag_selector);
                    tv.setBackgroundDrawable(drawable);
                }
            }
        }
    }

    /**
     * 设置tagFlowLayout中标签的状态及相应的背景，跳过选中项标签
     * @param tagFlowLayout
     * @param isEnable
     */
    private void setTagViewState(TagFlowLayout tagFlowLayout,boolean isEnable){
        if(tagFlowLayout==null)return;
        int count = tagFlowLayout.getAdapter().getCount();
        if(isEnable){
            tagFlowLayout.setEnabled(true);
            for (int i = 0; i < count; i++) {
                setTagViewEnabled(true,i,tagFlowLayout);
            }
        }else{
            tagFlowLayout.setEnabled(false);
            Set<Integer> selectedList = tagFlowLayout.getSelectedList();
            for (int i = 0; i < count; i++) {
                if(!selectedList.contains(i)) {
                    setTagViewEnabled(false, i, tagFlowLayout);
                }
            }
        }
    }

    /**
     *  设置tabflowlayout某个子项可点/不可点
     */
    private void setTagViewEnabled(boolean canClick,int tagPosition,TagFlowLayout tagFlowLayout) {
        if (!canClick) {

            TagView tagV = (TagView) tagFlowLayout.getChildAt(tagPosition);
            tagV.isEnabled = false;
            tagV.setChecked(false);

            //设置item背景为灰色不可选状态
            TextView tv = (TextView) tagV.getTagView();
            String name = tv.getText().toString();
            tv.setBackgroundResource(R.drawable.tag_disable_bg);

            if (tagFlowLayout.getSelectedList().contains(tagPosition)) {
                tagFlowLayout.getSelectedList().remove(tagPosition);
            }
        } else {
            TagView tag = (TagView) tagFlowLayout.getChildAt(tagPosition);
            tag.isEnabled = true;

            //设置item背景为初始化的状态选择器
            TextView tv = (TextView) tag.getTagView();
            Drawable drawable = context.getResources().getDrawable(R.drawable.tag_selector);
            tv.setBackgroundDrawable(drawable);

        }
    }

    /**
     * 设置tagView的背景色
     *
     * @author zealjiang
     * @time 2017/1/3 16:42
     */
    private void setTagViewBg(TagView tagV,boolean isEnable){
        if(tagV==null){
            return;
        }
        if(isEnable){
            tagV.isEnabled = true;
            //设置item背景为初始化的状态选择器
            TextView tv = (TextView) tagV.getTagView();
            Drawable drawable = context.getResources().getDrawable(R.drawable.tag_selector);
            tv.setBackgroundDrawable(drawable);
        }else{
            tagV.isEnabled = false;
            tagV.setChecked(false);
            //设置item背景为灰色不可选状态
            TextView tv = (TextView) tagV.getTagView();
            tv.setBackgroundResource(R.drawable.tag_disable_bg);
        }
    }

    /**
     * 向其它信息发送数据
     *
     * @author zealjiang
     * @time 2016/12/15 18:03
     */
    public void sendMessage(){
        if(procedureModel.getDrivingLicenseProperty()==2){
            eventProcedureModel.setDrivingNoneIsSelect(true);
        }else{
            eventProcedureModel.setDrivingNoneIsSelect(false);
        }

        if(procedureModel.getRegistLicenseProperty()==2){
            eventProcedureModel.setRegisterNoneIsSelected(true);
        }else{
            eventProcedureModel.setRegisterNoneIsSelected(false);
        }
        EventBus.getDefault().post(eventProcedureModel);
    }


    private void setTagSelect(final  String[] vals,final TagFlowLayout tagFlowLayout,int ... poses){
        setTag(vals,tagFlowLayout);
        displacementAdapter.setSelectedList(poses);
    }

    private void setTagListener(final TagFlowLayout tagFlowLayout){

        TagFlowLayout.OnTagClickListener onTagClickListener =  new TagFlowLayout.OnTagClickListener(){

            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {
                //MyToast.showShort(((TextView)view.getTagView()).getText().toString()+"  "+view.isChecked());
                if("纯电动".equals(((TextView)view.getTagView()).getText().toString())&&!view.isChecked()){//取消选中纯电动
                    //将排量置空
                    etDisplacement.setText("");
                    TagView tagView = (TagView) tflPailiang.getChildAt(0);
                    setTagViewBg(tagView,true);
                }
                return false;
            }
        };
        tagFlowLayout.setOnTagClickListener(onTagClickListener);

        tagOnSelectListener = new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if(tagFlowLayout == tflCardType){//证件类型
                    if(selectPosSet.size()>0) {
                        int selected = selectPosSet.iterator().next();
                        if ("车主证件未见".equals(tflCardTypeLists[selected])) {
                            //证件类型选择“车主证件未见”，车主证件号清空并不可选
                            HashSet<Integer> set = null;
                            etOwnerId.setText("");
                            etOwnerId.setEnabled(false);
                            tflCheZhuZhengJian.getAdapter().setSelectedList(set);
                            setTagViewState(tflCheZhuZhengJian,false);
                            tvOwnerId.setTextColor(getResources().getColor(R.color.global_gray_7));
                        }else{
                            if(configCardId) {
                                etOwnerId.setEnabled(true);
                                setTagViewState(tflCheZhuZhengJian,true);
                                tvOwnerId.setTextColor(getResources().getColor(R.color.global_gray_6));
                            }
                        }
                    }else{
                        if(configCardId) {
                            etOwnerId.setEnabled(true);
                            setTagViewState(tflCheZhuZhengJian,true);
                            tvOwnerId.setTextColor(getResources().getColor(R.color.global_gray_6));
                        }
                    }


                }else if(tagFlowLayout == tflFuelType){//燃料种类
                    if(selectPosSet.size()>0) {
                        int selected = selectPosSet.iterator().next();
                        if ("纯电动".equals(tflFuelTypeLists[selected])) {
                            //燃料类型选择“纯电动”，则排量处自动填写为“0”且不可修改，T变为不可选的状态；
                            etDisplacement.setText("0.0");
                            etDisplacement.setEnabled(false);
                            HashSet<Integer> set = null;
                            tflPailiang.getAdapter().setSelectedList(set);
                            tflPailiang.setEnabled(false);
                            TagView tagView = (TagView) tflPailiang.getChildAt(0);
                            setTagViewBg(tagView,false);
                        } else {
                            //燃料类型选择“纯电动”，则排量处自动填写为“0”且不可修改，T变为不可选的状态；
                            etDisplacement.setEnabled(true);
                            tflPailiang.setEnabled(true);
                            if(TextUtils.isEmpty(etDisplacement.getText().toString())){
                                return;
                            }
                            if(Float.valueOf(etDisplacement.getText().toString())==0){
                                etDisplacement.setText("");
                            }

                            TagView tagView = (TagView) tflPailiang.getChildAt(0);
                            setTagViewBg(tagView,true);
                        }
                    }else{
                        //一个也没选中
                        //燃料类型选择“纯电动”，则排量处自动填写为“0”且不可修改，T变为不可选的状态；
                        etDisplacement.setEnabled(true);
                        tflPailiang.setEnabled(true);
                    }
                }else if(tagFlowLayout == tflYuanchefapiao){//原车发票  有无工商章
                    if(selectPosSet.size()>0) {
                        int selected = selectPosSet.iterator().next();
                        if ("未见".equals(tflYuanchefapiaoLists[selected])) {
                            //选未见，前三项无论是否有数据，都变为不可编辑状态；
                            etBillMoney.setText("");
                            tvBillDate.setText("");
                            etBillMoney.setEnabled(false);
                            tvBillDate.setEnabled(false);
                            setDisableTv(tvOriginalBill,false);
                            //无工商章不可点击
                            //原车发票 未见
                            TagView tagView = (TagView) tflYuanchefapiao.getChildAt(1);
                            changeTagViewEnabled(1, 1, tagView, tflYuanchefapiao);
                        }
                    }else{
                        //原车发票 未见  过户次数大于0时，原车发票置为不可输入（选择）状态，并清空已输入（选择）信息；
                        TagView tagView = (TagView) tflYuanchefapiao.getChildAt(1);
                        changeTagViewEnabled(1, 1, tagView, tflYuanchefapiao);
                        etBillMoney.setEnabled(true);
                        tvBillDate.setEnabled(true);
                        setDisableTv(tvOriginalBill,true);
                    }
                }else if(tagFlowLayout == tflJinkouGuochan){//进口国产  此项去掉了

                }else if(tagFlowLayout == tflChepaihaoma){//车牌号码
                        //车牌号码
                        carPlateNum();
                }else if(tagFlowLayout == tflCarColor){//车身颜色
                    if(selectPosSet.size()>0) {
                        int selected = selectPosSet.iterator().next();
                        if ("其他".equals(tflCarColorLists[selected])) {
                            //显示填写其他说明
                            etCarColor.setVisibility(View.VISIBLE);
                        }else{
                            etCarColor.setVisibility(View.GONE);
                            etCarColor.setText("");
                        }
                    }else{
                        etCarColor.setVisibility(View.GONE);
                        etCarColor.setText("");
                    }
                } else if (tagFlowLayout == tflJiaoqiangxianbaodan) {//交强险保单
                    //当交强险保单选择未见时，交强险到期日置为不可选择状态，并清空已选择日期
                    //当交强险保单选择未见时，交强险所在地置为不可选择状态，并清空已选择日期；
                    if (selectPosSet.size() > 0) {
                        int selected = selectPosSet.iterator().next();
                        if ("未见".equals(tflJiaoqiangxianbaodanLists[selected])) {
                            tvComInsuranceExDate.setText("");
                            tvComInsuranceExDate.setEnabled(false);

                            tvComInsuranceAddress.setText("");
                            tvComInsuranceAddress.setEnabled(false);

                            //交强险到期日
                            tvInsuranceExpDate.setTextColor(getResources().getColor(R.color.global_gray_7));
                            //交强险所在地
                            tvInsuranceAddress.setTextColor(getResources().getColor(R.color.global_gray_7));
                        } else {
                            tvComInsuranceExDate.setEnabled(true);
                            tvComInsuranceAddress.setEnabled(true);
                            //交强险到期日
                            tvInsuranceExpDate.setTextColor(getResources().getColor(R.color.global_gray_6));
                            //交强险所在地
                            tvInsuranceAddress.setTextColor(getResources().getColor(R.color.global_gray_6));
                        }
                    } else {
                        tvComInsuranceExDate.setEnabled(true);
                        tvComInsuranceAddress.setEnabled(true);
                        //交强险到期日
                        tvInsuranceExpDate.setTextColor(getResources().getColor(R.color.global_gray_6));
                        //交强险所在地
                        tvInsuranceAddress.setTextColor(getResources().getColor(R.color.global_gray_6));
                    }
                } else if (tagFlowLayout == tflQitapiaozheng) {//其他票证
                    setOtherBill(selectPosSet);
                }
            }
        };

        tagFlowLayout.setOnSelectListener(tagOnSelectListener);
    }

    /**
     * 设置其他票证
     */
    private void setOtherBill(Set<Integer> selectPosSet){
        //当选择购置税完税证明（征税）时，不可同时选择购置税完税证明（免税），反之相同；
        int tagZhengShuiPos = 0;

        //得到当前的所有显示选项
        int count = tflQitapiaozheng.getAdapter().getCount();
        HashMap<Integer, String> billMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String tag = (String) tflQitapiaozheng.getAdapter().getItem(i);
            billMap.put(i, tag);
            if ("购置税完税证明（征税）".equals(tag)) {
                tagZhengShuiPos = i;
            }
        }

        if (selectPosSet.size() > 0) {
            if(selectPosSet.contains(tagZhengShuiPos)){
                setTagViewEnabled(false, tagZhengShuiPos + 1, tflQitapiaozheng);
            }else if(selectPosSet.contains(tagZhengShuiPos+1)){
                setTagViewEnabled(false, tagZhengShuiPos, tflQitapiaozheng);
            }else{
                if (tagZhengShuiPos != 0) {
                    setTagViewEnabled(true, tagZhengShuiPos, tflQitapiaozheng);
                    setTagViewEnabled(true, tagZhengShuiPos + 1, tflQitapiaozheng);
                }
            }
        } else {
            if (tagZhengShuiPos != 0) {
                setTagViewEnabled(true, tagZhengShuiPos, tflQitapiaozheng);
                setTagViewEnabled(true, tagZhengShuiPos + 1, tflQitapiaozheng);
            }
        }
    }

    /**
     * 增加或删除tagFlowLayout中的标签
     * @author zealjiang
     * @time 2017/2/24 15:39
     * @param tagFlowLayout 当前tagFlowLayout
     * @param tagListString  当前显示的标签名称数组
     * @param isRemove  删除为true,增加为false
     * @param changeTag 要删除或增加的标签的名称
     */
    private void changeTagFlowLayout(TagFlowLayout tagFlowLayout,ArrayList<String> tagListString,boolean isRemove,String changeTag){
        //获取选中的标签名称
        Set<Integer>  selectedset =  tagFlowLayout.getSelectedList();
        ArrayList<String> selectedName = new ArrayList<>();
        for(int value :selectedset){
            selectedName.add(tagListString.get(value));
        }

        if(isRemove) {
            tagListString.remove(changeTag);
        }else{
            tagListString.add(changeTag);
        }

        //重置选中的标签
        selectedset.clear();
        for (int i = 0; i < selectedName.size(); i++) {
            for (int j = 0; j < tagListString.size(); j++) {
                if(tagListString.get(j).equals(selectedName.get(i))){
                    selectedset.add(j);
                }
            }

        }
        tagFlowLayout.getAdapter().setSelectedList(selectedset);

        tagFlowLayout.getAdapter().notifyDataChanged();
    }

    public void setTag(ArrayList<String> list,final TagFlowLayout tagFlowLayout){
        displacementAdapter = new MyTagStringAdapter(list,tagFlowLayout,getActivity());
        tagFlowLayout.setAdapter(displacementAdapter);
        setTagListener(tagFlowLayout);
    }

    public void setTag(final  String[] vals,final TagFlowLayout tagFlowLayout){
        displacementAdapter = new MyTagStringAdapter(vals,tagFlowLayout,getActivity());
        tagFlowLayout.setAdapter(displacementAdapter);
        setTagListener(tagFlowLayout);
    }
    public void setRegionSimpleNameTag(final  String[] vals,final TagFlowLayout tagFlowLayout){
        MyRegionSimpleNameTagStringAdapter simpleNameAdapter = new MyRegionSimpleNameTagStringAdapter(vals,tagFlowLayout,getActivity());
        tagFlowLayout.setAdapter(simpleNameAdapter);
    }

    /**
     * 领取的任务中是含有VIN,含有返回true,没有返回false
     *
     * @author zealjiang
     * @time 2016/11/24 10:21
     */
    private boolean taskHasVIN(){
        if(((DetectMainActivity)getActivity()).getTaskItem()==null){
            return false;
        }
        String vin = ((DetectMainActivity)getActivity()).getTaskItem().getVinCode();
        if(TextUtils.isEmpty(vin)){
            return false;
        }else{
            return true;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK)
            return;
        if(requestCode==REQUEST_CODE_CAR_LICENSE){ //此暂无 此界面去掉了行驶证识别
            DrivingLicenceModel drivingLicenceModel = (DrivingLicenceModel)data.getSerializableExtra("DrivingLicenceModel");
            MyToast.showShort("成功返回行驶证识别信息");
            //车牌号码
            if(!TextUtils.isEmpty(drivingLicenceModel.getPlateNo())&&drivingLicenceModel.getPlateNo().length()>2){

                Set<Integer> set = tflChepaihaoma.getSelectedList();
                if(set.size()>0){
                    int hasPlateNo = set.iterator().next();
                    if(hasPlateNo==0){
                        return;
                    }
                }

                ArrayList<String> listData = LicencePlateAddress.getInstance().getSimpleName();
                String shortName = drivingLicenceModel.getPlateNo().substring(0,1);
                if(!TextUtils.isEmpty(shortName)){
                    if(listData.contains(shortName)){
                        tvRegionSimpleName.setText(drivingLicenceModel.getPlateNo().substring(0,1));
                    }
                }
                etPlateNo.setText(drivingLicenceModel.getPlateNo().substring(1));
            }
            etCarType.setText(null2Length0(drivingLicenceModel.getModel()));
            if(!taskHasVIN()){
//                etVIN.setText(null2Length0(drivingLicenceModel.getVIN()));
                //维保
                showVINDialog(0, getUser().getUserId()+"",drivingLicenceModel.getVIN());
            }
            etEngineId.setText(null2Length0(drivingLicenceModel.getEngineNo()));
        }else if(requestCode==REQUEST_PROVINCE_CITY_CODE){//初登地区
            LocalProvinceCityModel localProvinceCityModel = (LocalProvinceCityModel)data.getSerializableExtra("LocalProvinceCityModel");
            if(localProvinceCityModel !=null){
                submitModel.setRegisterprovID(localProvinceCityModel.getProvinceId());
                submitModel.setProvName(localProvinceCityModel.getProvinceName());
                submitModel.setRegisterCityID(localProvinceCityModel.getCityId());
                submitModel.setCityName(localProvinceCityModel.getCityName());
            }
            if(localProvinceCityModel.getProvinceName().equals(localProvinceCityModel.getCityName())){
                tvRegisterRegion.setText(localProvinceCityModel.getProvinceName());
            }else{
                tvRegisterRegion.setText(localProvinceCityModel.getProvinceName()+localProvinceCityModel.getCityName());
            }
        }else if(requestCode==REQUEST_PROVINCE_CITY_CODE_PLATE){//上牌地区
            LocalProvinceCityModel localProvinceCityModel = (LocalProvinceCityModel)data.getSerializableExtra("LocalProvinceCityModel");
            if(localProvinceCityModel !=null){
                submitModel.setOnCardProvID(localProvinceCityModel.getProvinceId());
                submitModel.setOnCardProvName(localProvinceCityModel.getProvinceName());
                submitModel.setOnCardCityID(localProvinceCityModel.getCityId());
                submitModel.setOnCardCityName(localProvinceCityModel.getCityName());
            }
            if(localProvinceCityModel.getProvinceName().equals(localProvinceCityModel.getCityName())){
                tvPlateRegion.setText(localProvinceCityModel.getProvinceName());
            }else{
                tvPlateRegion.setText(localProvinceCityModel.getProvinceName()+localProvinceCityModel.getCityName());
            }
        }else if(requestCode==REQUEST_PROVINCE_CITY_CODE_INSURANCE_ADDRESS){//交强险所在地
            LocalProvinceCityModel localProvinceCityModel = (LocalProvinceCityModel)data.getSerializableExtra("LocalProvinceCityModel");
            if(localProvinceCityModel !=null){
                submitModel.setInsuranceProvID(localProvinceCityModel.getProvinceId());
                submitModel.setInsuranceProvName(localProvinceCityModel.getProvinceName());
                submitModel.setInsuranceCityID(localProvinceCityModel.getCityId());
                submitModel.setInsuranceCityName(localProvinceCityModel.getCityName());
            }
            if(localProvinceCityModel.getProvinceName().equals(localProvinceCityModel.getCityName())){
                tvComInsuranceAddress.setText(localProvinceCityModel.getProvinceName());
            }else{
                tvComInsuranceAddress.setText(localProvinceCityModel.getProvinceName()+localProvinceCityModel.getCityName());
            }
        }else if(requestCode==REQUEST_CODE_CAR_TYPE_SELECT){//车型选择  废弃 不再使用
            LocalCarConfigModel localCarConfigModel = (LocalCarConfigModel)data.getSerializableExtra("LocalCarConfigModel");

            //如果重新选择了品牌车系，需要重置车型ID并保存品牌、车系
            EventBus.getDefault().post(localCarConfigModel);

            //保存车辆铭牌
            SubmitModel submitModel = ((DetectMainActivity)this.getActivity()).getSubmitModel();
            submitModel.setNameplate(localCarConfigModel.getNameplate());

            ((DetectMainActivity)ProcedureCarFragment.this.getActivity()).remove(1);
            //跳到车型选择Fragment
            ((DetectMainActivity)ProcedureCarFragment.this.getActivity()).skipToFragment(1);

        }else if(requestCode==PHOTO_REQUEST_DL){//行驶证照片连续拍照
            if(null != data){
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_MULTI);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if(picList!=null && picList.size()>0){
                    if(captureType==Constants.CAPTURE_TYPE_SINGLE){
                        if(TextUtils.isEmpty(picList.get(0).getPicPath())){
                            return;
                        }
                        pictureItemsDL.get(curPicPos).setPicPath(picList.get(0).getPicPath());
                    }else{
                        pictureItemsDL.clear();
                        pictureItemsDL.addAll(picList);
                    }
                    //刷新显示照片
                    refreshDLPic();
                }
            }
        }else if(requestCode==PHOTO_BIG_PHOTO_DL){//行驶证单张
            if(data!=null){
                boolean recapture = data.getBooleanExtra("recapture", false);
                if(recapture){
                    curPicPos = data.getIntExtra("recapturePosition",curPicPos);
                    userCamera(curPicPos,Constants.CAPTURE_TYPE_SINGLE,pictureItemsDL,PHOTO_REQUEST_DL);
                }
            }
        }else if(requestCode==PHOTO_REQUEST_DLREG){//登记证照片连续拍照
            if(null != data){
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_MULTI);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if(picList!=null && picList.size()>0){
                    if(captureType==Constants.CAPTURE_TYPE_SINGLE){
                        if(TextUtils.isEmpty(picList.get(0).getPicPath())){
                            return;
                        }
                        if(curPicPos<2) {
                            pictureItemsDLRegister.get(curPicPos).setPicPath(picList.get(0).getPicPath());
                        }
                    }else{
                        pictureItemsDLRegister.clear();
                        pictureItemsDLRegister.addAll(picList);
                    }
                    //刷新显示照片
                    refreshDLRegisterPic();

                }
            }
        }else if(requestCode==PHOTO_BIG_PHOTO_DLREG){//登记证单拍
            if(data!=null){
                boolean recapture = data.getBooleanExtra("recapture", false);
                if(recapture){
                    curPicPos = data.getIntExtra("recapturePosition",curPicPos);
                    userCamera(curPicPos,Constants.CAPTURE_TYPE_SINGLE,pictureItemsDLRegister,PHOTO_REQUEST_DLREG);
                }
            }
        }else if(requestCode==PHOTO_RL_MORE_SINGLE){//TODO  是否可以被废弃掉 登记证更多拍摄
            if(data!=null){
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE,Constants.CAPTURE_TYPE_MULTI);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if(picList!=null && picList.size()>0){
                    if(captureType==Constants.CAPTURE_TYPE_SINGLE){
                        if(TextUtils.isEmpty(picList.get(0).getPicPath())){
                            return;
                        }
                        registerMorePI.setPicPath(picList.get(0).getPicPath());
                    }
                    //刷新显示照片
                    refreshDLRegisterPic();
                }
            }
        }else if(requestCode==PHOTO_RL_MORE_SINGLE2) {
            if (data != null) {
                int captureType = data.getIntExtra(Constants.CAPTURE_TYPE, Constants.CAPTURE_TYPE_MULTI);
                ArrayList<PictureItem> picList = (ArrayList<PictureItem>) data.getSerializableExtra("picList");
                if (picList != null && picList.size() > 0) {
                    if (captureType == Constants.CAPTURE_TYPE_SINGLE) {
                        if (TextUtils.isEmpty(picList.get(0).getPicPath())) {
                            return;
                        }
                        registerMorePI2.setPicPath(picList.get(0).getPicPath());
                    }
                    //刷新显示照片
                    refreshDLRegisterPic();
                }
            }
        }else if (requestCode == PHOTO_RL_MORE_BIG) {//登记证更多查看大图
            if (data != null) {
                boolean recapture = data.getBooleanExtra("recapture", false);
                curPicPos = data.getIntExtra("recapturePosition", curPicPos);
                isDel = data.getBooleanExtra("isDel", false);
                if (recapture) {
                    userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE, pictureItemsDLRegisterMore, PHOTO_REQUEST_DLREG);
                }
            }
            //删除
            if(isDel){
                delPic(curPicPos,1);
            }
        }else if (requestCode == PHOTO_RL_MORE_BIG2) {//登记证更多查看大图
            if (data != null) {
                curPicPos = data.getIntExtra("recapturePosition", curPicPos);
                isDel2 = data.getBooleanExtra("isDel", false);
                boolean recapture = data.getBooleanExtra("recapture", false);
                if (recapture) {
                    userCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE, pictureItemsDLRegisterMore, PHOTO_REQUEST_DLREG);
                }
            }
            //删除
            if(isDel2){
                delPic(curPicPos,2);
            }
        }


    }

    //删除图片
    private void delPic(int curPicPos,int pos) {


        //删除本地图片
        String path = PadSysApp.picDirPath + pictureItemsDLRegisterMore.get(curPicPos).getPicId() + ".jpg";
        if (FileUtils.isFileExists(path)) {
            FileUtils.deleteFile(path);
        }

        if (pos == 1){
            //添加删除参数
            if(submitModel != null && submitModel.getDeletePicId() != null){
                if(!submitModel.getDeletePicId().contains("23")){
                    submitModel.getDeletePicId().add("23");
                }
            }
            registerMorePI.setPicPath("");

            if(!procedureModel.getDeletePicId().contains("23")){
                procedureModel.getDeletePicId().add("23");

                //如果存在网络图片地址，则显示网络图片
                List<TaskDetailModel.ProcedurePicListBean> procedurePicList = procedureModel.getProcedurePicList();
                if(procedurePicList!=null) {
                    for (int i = 0; i < procedurePicList.size(); i++) {
                        TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                        String picId = procedurePicBean.getPicID();
                        if (!TextUtils.isEmpty(picId)) {
                            if("23".equals(picId)) {
                                procedurePicBean.setPicPath("");
                            }
                        }
                    }
                }
            }
            if(!procedureModel.getSubmitdelPicId().contains("23")){
                procedureModel.getSubmitdelPicId().add("23");
            }

            //如果先删除了23，28到23的位置，再删除23
            if(procedureModel.getDeletePicId().contains("28")){
                if(!submitModel.getDeletePicId().contains("28")){
                    submitModel.getDeletePicId().add("28");
                }
                if(!procedureModel.getSubmitdelPicId().contains("28")){
                    procedureModel.getSubmitdelPicId().add("28");
                }

                //如果存在网络图片地址，则显示网络图片
                List<TaskDetailModel.ProcedurePicListBean> procedurePicList = procedureModel.getProcedurePicList();
                if(procedurePicList!=null) {
                    for (int i = 0; i < procedurePicList.size(); i++) {
                        TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                        String picId = procedurePicBean.getPicID();
                        if (!TextUtils.isEmpty(picId)) {
                            if("28".equals(picId)) {
                                procedurePicBean.setPicPath("");
                            }
                        }
                    }
                }
            }

        }else {

            //添加删除参数
            if(submitModel != null && submitModel.getDeletePicId() != null){
                if(!submitModel.getDeletePicId().contains("28")){
                    submitModel.getDeletePicId().add("28");
                }
            }
            registerMorePI2.setPicPath("");

            if(!procedureModel.getDeletePicId().contains("28")){
                procedureModel.getDeletePicId().add("28");
            }
            if(!procedureModel.getSubmitdelPicId().contains("28")){
                procedureModel.getSubmitdelPicId().add("28");
            }


            //如果存在网络图片地址，则显示网络图片
            List<TaskDetailModel.ProcedurePicListBean> procedurePicList = procedureModel.getProcedurePicList();
            if(procedurePicList!=null) {
                for (int i = 0; i < procedurePicList.size(); i++) {
                    TaskDetailModel.ProcedurePicListBean procedurePicBean = procedurePicList.get(i);
                    String picId = procedurePicBean.getPicID();
                    if (!TextUtils.isEmpty(picId)) {
                        if("28".equals(picId)){
                            procedurePicBean.setPicPath("");
                        }
                    }
                }
            }
        }

        refreshDLRegisterPic();
    }

    /**
     * 跳转到车系选择
     *
     * @author zealjiang
     * @time 2016/12/4 12:57
     */
    @Deprecated
    public void skipToCarTypeSelectActivity(){
        SubmitModel submitModel = ((DetectMainActivity) getActivity()).getSubmitModel();
        LocalCarConfigModel localCarConfigModel = new LocalCarConfigModel();
        localCarConfigModel.setProductDate(tvProductionDate.getText().toString());
        localCarConfigModel.setVin(etVIN.getText().toString());
        localCarConfigModel.setNameplate(submitModel.getNameplate());

        Intent intent = new Intent(this.getContext(), CarTypeSelectActivity.class);
        intent.putExtra("LocalCarConfigModel",localCarConfigModel);
        startActivityForResult(intent,REQUEST_CODE_CAR_TYPE_SELECT);
    }

    /**
     * 检查并保存数据
     *
     * @param isCheck     提交时不可为空的项是否都已经填写是否检查
     * @param isNextCheck 必填项是否检查
     */
    public  boolean checkAndsaveData(boolean isCheck,boolean isNextCheck,String hintHead){
        hintHead = StringUtils.null2Length0(hintHead);
        //第一次领取一个任务时，本地数据库没有数据，服务器也没有返回详情数据时procedureModel为Null
        if(procedureModel==null){
            procedureModel = new ProcedureModel();
        }
        if(this.getActivity()==null){
            //关闭DetectMainActivity
            AppManager.getAppManager().finishActivity(DetectMainActivity.class);
            return false;
        }
        SubmitModel submitModel = ((DetectMainActivity)this.getActivity()).getSubmitModel();
        if(submitModel==null){
            return false;
        }

        //保存图片的网络地址
        if(taskDetailModel!=null){
            procedureModel.setProcedurePicList(taskDetailModel.getProcedurePicList());
        }

        //行驶证有瑕疵
        boolean isDrivingFlow = false;
        //行驶证未见
        boolean isDrivingNone = false;
        //行驶证  行驶证照片属性（ 0 有（默认）， 1 行驶证有瑕疵， 2 行驶证未见），单选
        if(configDriving&&llDriving.getVisibility()!=View.GONE){
            //行驶证有瑕疵
            isDrivingFlow = cBoxDrivingLicenceFlaw.isChecked();
            //行驶证未见
            isDrivingNone = cBoxDrivingNone.isChecked();
            int driving = 0;
            if(isDrivingFlow){
                driving = 1;
            }
            if(isDrivingNone){
                driving = 2;
            }
            //如果行驶证可见，行驶证照片不可为空
            if(!isDrivingNone&&isCheck){
                //行驶证正本正面
                if(TextUtils.isEmpty(pictureItemsDL.get(0).getPicPath())){
                    MyToast.showShort(hintHead+"请拍摄行驶证正本正面");
                    return false;
                }

                //行驶证正本背面
                if(TextUtils.isEmpty(pictureItemsDL.get(1).getPicPath())) {
                    MyToast.showShort(hintHead+"请拍摄行驶证正本背面");
                    return false;
                }
                //行驶证副本正面
                if(TextUtils.isEmpty(pictureItemsDL.get(2).getPicPath())) {
                    MyToast.showShort(hintHead+"请拍摄行驶证副本正面");
                    return false;
                }
                //行驶证副本背面
                if(TextUtils.isEmpty(pictureItemsDL.get(3).getPicPath())) {
                    MyToast.showShort(hintHead+"请拍摄行驶证副本背面");
                    return false;
                }
            }

            submitModel.setDrivingLicenseProperty(driving);
            procedureModel.setDrivingLicenseProperty(driving);

            //判断是否上传删除的照片ID
            if(driving==2){
                //判断详情中行驶证是否可见
                if(taskDetailModel!=null){
                    int drivingLicense = taskDetailModel.getBasic().getDrivingLicenseProperty();
                    if(drivingLicense!=2){
                        ArrayList<String> deleteArray = (ArrayList)submitModel.getDeletePicId();
                        if(deleteArray==null) {
                            deleteArray = new ArrayList<String>();
                        }
                        if(!deleteArray.contains("18")){
                            deleteArray.add("18");
                        }
                        if(!deleteArray.contains("19")){
                            deleteArray.add("19");
                        }
                        if(!deleteArray.contains("20")){
                            deleteArray.add("20");
                        }
                        if(!deleteArray.contains("24")){
                            deleteArray.add("24");
                        }
                        submitModel.setDeletePicId(deleteArray);

                        //本地保存
                        ArrayList<String> deleteArrayP = (ArrayList)procedureModel.getDeletePicId();
                        if(deleteArrayP==null) {
                            deleteArrayP = new ArrayList<String>();
                        }
                        if(!deleteArrayP.contains("18")){
                            deleteArrayP.add("18");
                        }
                        if(!deleteArrayP.contains("19")){
                            deleteArrayP.add("19");
                        }
                        if(!deleteArrayP.contains("20")){
                            deleteArrayP.add("20");
                        }
                        if(!deleteArrayP.contains("24")){
                            deleteArrayP.add("24");
                        }
                        procedureModel.setDeletePicId(deleteArrayP);
                    }
                }
            }
        }else{
            submitModel.setDrivingLicenseProperty(0);
            procedureModel.setDrivingLicenseProperty(0);
        }

        //登记证有瑕疵
        boolean isRegisterFlow = false;
        //登记证未见
        boolean isRegisterNone = false;
        if(configRegister&&llRegister.getVisibility()!=View.GONE){
            //登记证   登记证照片（ 0 有（默认）， 1 登记证有瑕疵， 2 登记证未见），单选
            //登记证有瑕疵
            isRegisterFlow = cBoxRegisterFlaw.isChecked();
            //登记证未见
            isRegisterNone = cBoxRegisterNone.isChecked();
            int register = 0;
            if(isRegisterFlow){
                register = 1;
            }
            if(isRegisterNone){
                register = 2;
            }
            //如果登记证可见，登记证照片不可为空
            if(!isRegisterNone&&isCheck){
                //登记证1-2页照片
                if(TextUtils.isEmpty(pictureItemsDLRegister.get(0).getPicPath())) {
                    MyToast.showShort(hintHead+"请拍摄登记证1-2页照片");
                    return false;
                }
                //登记证3-4页照片
                if(TextUtils.isEmpty(pictureItemsDLRegister.get(1).getPicPath())) {
                    MyToast.showShort(hintHead+"请拍摄登记证3-4页照片");
                    return false;
                }

            }
            submitModel.setRegistLicenseProperty(register);
            procedureModel.setRegistLicenseProperty(register);

            //判断是否上传删除的照片ID
            if(register==2){
                //判断详情中行驶证是否可见
                if(taskDetailModel!=null){
                    int registLicense = taskDetailModel.getBasic().getRegistLicenseProperty();
                    if(registLicense!=2){
                        ArrayList<String> deleteArray = (ArrayList)submitModel.getDeletePicId();
                        if(deleteArray==null) {
                            deleteArray = new ArrayList<String>();
                        }

                        if(!deleteArray.contains("21")){
                            deleteArray.add("21");
                        }
                        if(!deleteArray.contains("22")){
                            deleteArray.add("22");
                        }
                        if(!deleteArray.contains("23")){
                            deleteArray.add("23");
                        }
                        submitModel.setDeletePicId(deleteArray);
                    }
                }
            }
        }else{
            submitModel.setRegistLicenseProperty(0);
            procedureModel.setRegistLicenseProperty(0);
        }

        //提交前检查删除图片列表里的 id 和 它对应的本地图片在不在，如果本地图片在，那得将删除图片列表里面的id移除
        if(submitModel != null && submitModel.getDeletePicId() != null){

            List<String> deletePicIdS = submitModel.getDeletePicId();

            for (int i = 0; i < deletePicIdS.size(); i++) {
                String pic = PadSysApp.picDirPath + deletePicIdS.get(i)+".jpg";
                if (FileUtils.isFileExists(pic)){
                    deletePicIdS.remove(i);
                }
            }

            List<String> pDeletePics = procedureModel.getDeletePicId();

            for (int i = 0; i < pDeletePics.size(); i++) {
                String pic = PadSysApp.picDirPath + pDeletePics.get(i)+".jpg";
                if (FileUtils.isFileExists(pic)){
                    pDeletePics.remove(i);
                }
            }
        }

        Set<Integer> set;
        //年检有效期
        if(configAnnualDate){
            String sAnnualInspection = tvAnnualInspection.getText().toString();
            if(isCheck&&!isDrivingNone){
                if(TextUtils.isEmpty(sAnnualInspection)){
                    MyToast.showShort(hintHead+"请填写年检有效期");
                    return false;
                }
            }
            submitModel.setInspection(sAnnualInspection);
            procedureModel.setInspection(sAnnualInspection);
        }else{
            submitModel.setInspection("");
            procedureModel.setInspection("");
        }

        //车架号VIN
        if(configVin){
            String vin = etVIN.getText().toString();
            if(isCheck||isNextCheck){
                if(TextUtils.isEmpty(vin)){
                    MyToast.showShort(hintHead+"请填写车架号VIN");
                    return false;
                }
            }
            boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
            if(!isValid&&isNextCheck){
                MyToast.showShort("根据VIN校验规则，可能存在填写错误，请仔细核对!");
                return false;
            }
            if(!isValid&&isCheck){
                MyToast.showShort("根据VIN校验规则，可能存在填写错误，请仔细核对!");
                return false;
            }
            submitModel.setVin(vin.toUpperCase());
            procedureModel.setVin(vin.toUpperCase());
        }else{
            submitModel.setVin("");
            procedureModel.setVin("");
        }


        //机动车所有人
        if(configOwener){
            String vehicleOwener = etVehicleOwener.getText().toString();
            if(isCheck&&!isRegisterNone){
                if(TextUtils.isEmpty(vehicleOwener)){
                    MyToast.showShort(hintHead+"请填写机动车所有人");
                    return false;
                }
            }
            submitModel.setCarOwner(vehicleOwener);
            procedureModel.setCarOwner(vehicleOwener);
        }else{
            submitModel.setCarOwner("");
            procedureModel.setCarOwner("");
        }

        //证件类型  证件类型（ 0 默认， 1 身份证， 2 军官证， 3 护照， 4 组织机构代码证， 5 车主证件未见），单选
        if(configCardType){
            cardType = 0;
            set = tflCardType.getSelectedList();
            if(set.size()>0){
                cardType = set.iterator().next()+1;
            }else{
                if(isCheck&&!isRegisterNone){
                    MyToast.showShort(hintHead+"请选择证件类型");
                    return false;
                }
            }
            submitModel.setCardType(cardType);
            procedureModel.setCardType(cardType);
        }else{
            submitModel.setCardType(0);
            procedureModel.setCardType(0);
        }


        //车主证件号
        if(configCardId){
            String owenerId = etOwnerId.getText().toString();
            if(isCheck&&!isRegisterNone&&cardType != 5){
                if(TextUtils.isEmpty(owenerId)){
                    MyToast.showShort(hintHead+"请填写车主证件号");
                    return false;
                }
            }
            submitModel.setCardNum(owenerId);
            procedureModel.setCardNum(owenerId);
        }else{
            submitModel.setCardNum("");
            procedureModel.setCardNum("");

            //与车主证件一致
            submitModel.setIsCardSame(1);
            procedureModel.setIsCardSame(1);
        }

        //与车主证件号  与车主证件号是否一致（服务器： 0 不一致， 1 一致  ,默认是一致），单选
        int IsCardSame = 1;
        set = tflCheZhuZhengJian.getSelectedList();
        if(set.size()>0){
            IsCardSame = set.iterator().next();
        }

        submitModel.setIsCardSame(IsCardSame);
        procedureModel.setIsCardSame(IsCardSame);


        //登记日期  行驶证未见和登记证未见同时选中，登记日期、品牌型号、发动机号、使用性质、燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空
        if(configRegisterDate){
            String registerDate = tvRegisterDate.getText().toString();
            if(isCheck){
                if(!(isDrivingNone&&isRegisterNone)){
                    if(TextUtils.isEmpty(registerDate)){
                        MyToast.showShort(hintHead+"请填写登记日期");
                        return false;
                    }
                }
            }
            submitModel.setRecordDate(registerDate);
            procedureModel.setRecordDate(registerDate);
        }else{
            submitModel.setRecordDate("");
            procedureModel.setRecordDate("");
        }

        //车牌号码
        if(configPlateNum){
            String regionSimpleName = tvRegionSimpleName.getText().toString();
            String plateNo = etPlateNo.getText().toString();
            plateNo = regionSimpleName+plateNo;
            if(StringUtils.isEmpty(plateNo)||plateNo.length()==1){
                submitModel.setCarLicense("");//没填车牌号，提交到服务器前要把省份也去掉
            }else {
                submitModel.setCarLicense(plateNo.toUpperCase());
                procedureModel.setCarLicense(plateNo.toUpperCase());
            }

            //有无车牌  车牌有无（ 0 未落户、 1 有车牌、 2 未悬挂、3 与实车不符 ），单选
            int hasPlateNo = 1;
            set = tflChepaihaoma.getSelectedList();
            if(set.size()==0){
                hasPlateNo = 1;
            }else{
                hasPlateNo = set.iterator().next();
                if(hasPlateNo==1){
                    hasPlateNo = 2;
                }else if(hasPlateNo==2){
                    hasPlateNo = 3;
                }
            }

            if(hasPlateNo == 0){
                plateNo = "";
                submitModel.setCarLicense(plateNo);
                procedureModel.setCarLicense(plateNo);
            }

            if(isCheck && etPlateNo.isEnabled()){
                if(hasPlateNo!=0&&(TextUtils.isEmpty(regionSimpleName)||(plateNo.trim().length()<7))){//如果没有填写车牌并且也没选择无车牌,车牌号为7位
                    MyToast.showShort(hintHead+"请正确填写车牌号码");
                    return false;
                }
            }

            //车牌有无（ 0 未落户、 1 有车牌、 2 未悬挂 、3 与实车不符）
            submitModel.setCarLicenseEx(hasPlateNo);
            procedureModel.setCarLicenseEx(hasPlateNo);

            //车牌有无（0无，1有），单选
            submitModel.setCarLicenseHave((hasPlateNo>0)?1:0);
            procedureModel.setCarLicenseHave((hasPlateNo>0)?1:0);
        }else{
            submitModel.setCarLicense("");
            procedureModel.setCarLicense("");

            //车牌有无（ 0 无车牌、 1 有车牌、 2 未悬挂 、3 与实车不符）
            submitModel.setCarLicenseEx(1);
            procedureModel.setCarLicenseEx(1);

            //车牌有无（0无，1有），单选
            submitModel.setCarLicenseHave(1);
            procedureModel.setCarLicenseHave(1);
        }

        //上牌地区
        if(configPlateRegion){
            String plateRegion = tvPlateRegion.getText().toString();
            if(isCheck&&etPlateNo.isEnabled()){//当车牌号码为不可输入状态时，上牌地区置为不可选择状态（选择【未落户】或登记证和行驶证都未见，同时选择了【未悬挂】），并清空已选择地区
                if(TextUtils.isEmpty(plateRegion)){
                    MyToast.showShort(hintHead+"请填写上牌地区");
                    return false;
                }
            }

            procedureModel.setOnCardProvID(submitModel.getOnCardProvID());
            procedureModel.setOnCardProvName(submitModel.getOnCardProvName());
            procedureModel.setOnCardCityID(submitModel.getOnCardCityID());
            procedureModel.setOnCardCityName(submitModel.getOnCardCityName());

        }else{
            submitModel.setOnCardProvID(-1);
            submitModel.setOnCardProvName("");
            submitModel.setOnCardCityID(-1);
            submitModel.setOnCardCityName("");

            procedureModel.setOnCardProvID(-1);
            procedureModel.setOnCardProvName("");
            procedureModel.setOnCardCityID(-1);
            procedureModel.setOnCardCityName("");
        }

        //过户次数 默认为-1
        int transferNum = -1;
        if(configTransferNum){
            String stransferNum = etTransferNum.getText().toString();
            if(!TextUtils.isEmpty(stransferNum)){
                transferNum = Integer.valueOf(stransferNum);
            }
            if(isCheck&&!isRegisterNone){
                if(transferNum==-1){
                    MyToast.showShort(hintHead+"请填写过户次数");
                    return false;
                }
            }
            submitModel.setTransferCount(transferNum);
            procedureModel.setTransferCount(transferNum);
        }else{
            submitModel.setTransferCount(-1);
            procedureModel.setTransferCount(-1);
        }

        //最后过户日期
        if(configLastTransferDate){
            String lastTransferDate = tvLastTransferDate.getText().toString();
            if(isCheck&&!isRegisterNone&&transferNum>0){
                if(TextUtils.isEmpty(lastTransferDate)){
                    MyToast.showShort(hintHead+"请填写最后过户日期");
                    return false;
                }
            }
            submitModel.setLastTransferDate(lastTransferDate);
            procedureModel.setLastTransferDate(lastTransferDate);
        }else{
            submitModel.setLastTransferDate("");
            procedureModel.setLastTransferDate("");
        }

        //品牌型号
        if(configCarType){
            String carType = etCarType.getText().toString();
            if(isCheck){
                if(!(isDrivingNone&&isRegisterNone)) {
                    if (TextUtils.isEmpty(carType)) {
                        MyToast.showShort(hintHead + "请填写品牌型号");
                        return false;
                    }
                }
            }
            submitModel.setRecordBrand(carType.toUpperCase());
            procedureModel.setRecordBrand(carType.toUpperCase());
        }else{
            submitModel.setRecordBrand("");
            procedureModel.setRecordBrand("");
        }

        //车身颜色   车身颜色 (1 白、 2 灰、 3 红、 4 粉、 5 黄、 6 蓝、 7 绿、 8 紫、 9 棕、 10 黑、11、双色 99 其它 ) ，单选
        if(configCarColor){
            int carColor;
            set = tflCarColor.getSelectedList();
            if(set.size()==0){//一个也没选中
                carColor= -1;
            }else{
                carColor = set.iterator().next()+1;
            }
            if (carColor == 12) {
                carColor = 99;
            }
            if (isCheck&&!isRegisterNone) {
                if (carColor == -1) {
                    MyToast.showShort(hintHead + "请选择车身颜色");
                    return false;
                }
            }

            if(isCheck&&carColor==99&&TextUtils.isEmpty(etCarColor.getText().toString())){
                MyToast.showShort(hintHead+"请填写登记证车身颜色其它说明");
                return false;
            }

            submitModel.setColor(carColor);//created by wujj on 2016/12/21
            procedureModel.setColor(carColor);//created by wujj on 2016/12/21

            if(isCheck&&carColor==99&&TextUtils.isEmpty(etCarColor.getText().toString())){
                MyToast.showShort(hintHead+"请填写登记证车身颜色其它说明");
                return false;
            }
            submitModel.setCarColorDes(etCarColor.getText().toString());
            procedureModel.setCarColorDes(etCarColor.getText().toString());
        }else{
            submitModel.setColor(-1);
            procedureModel.setColor(-1);

            submitModel.setCarColorDes("");
            procedureModel.setCarColorDes("");
        }

        //发动机号
        if(configEngineId){
            String engineId = etEngineId.getText().toString();
            if(isCheck){
                if(!(isDrivingNone&&isRegisterNone)) {
                    if (TextUtils.isEmpty(engineId)) {
                        MyToast.showShort(hintHead + "请填写发动机号");
                        return false;
                    }
                }
            }
            submitModel.setEngineNum(engineId.toUpperCase());
            procedureModel.setEngineNum(engineId.toUpperCase());
        }else{
            submitModel.setEngineNum("");
            procedureModel.setEngineNum("");
        }

        //燃料种类  1 汽油、 2 柴油、 3 混动、 4 天然气、 5 纯电动
        if(configFuelType){
            int fuelType;
            set = tflFuelType.getSelectedList();
            if(set.size()==0){//一个也没选中
                fuelType= -1;
            }else{
                fuelType = set.iterator().next()+1;
            }
            if(isCheck&&!isRegisterNone){
                if(fuelType==-1){
                    MyToast.showShort(hintHead+"请选择燃料种类");
                    return false;
                }
            }
            submitModel.setFuelType(fuelType);
            procedureModel.setFuelType(fuelType);
        }else{
            submitModel.setFuelType(-1);
            procedureModel.setFuelType(-1);
        }

        //轮胎规格
        if(configTireType){
            String tireModel = etTireModel.getText().toString();
            if(isCheck&&!isRegisterNone){
                if (TextUtils.isEmpty(tireModel)) {
                    MyToast.showShort(hintHead + "请填写轮胎规格");
                    return false;
                }
            }
            submitModel.setTyre(tireModel.toUpperCase());
            procedureModel.setTyre(tireModel.toUpperCase());
        }else{
            submitModel.setTyre("");
            procedureModel.setTyre("");
        }

        //核定载客量   核定载客（ 2-9,99 代表“ 9 人以上”），单选
        if(configLoadNum){
            int busload;
            set = tflBusload.getSelectedList();
            if(set.size()==0){//一个也没选中
                busload= -1;
            }else{
                busload = set.iterator().next();
            }
            if(busload==0){
                busload =2;
            }else if (busload==1){
                busload =4;
            }else if(busload==7){
                busload = 99;
            }else if(busload>1&&busload<=6){
                busload +=3;
            }
            if(isCheck&&!isRegisterNone){
                if(busload==-1){
                    MyToast.showShort(hintHead+"请选择核定载客数");
                    return false;
                }
            }
            submitModel.setSeating(busload);
            procedureModel.setSeating(busload);
        }else{
            submitModel.setSeating(-1);
            procedureModel.setSeating(-1);
        }

        //使用性质
        // 显示顺序 使用性质（ 1 非营运、 2 营转非、 3 出租营转非、 4 营运、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
        // 提交顺序 （ 1 营运、 2 非营运、 3 营转非、 4 出租营转非、 5 租赁、 6 警用、 7 消防、 8 救护、 9 工程抢险、 10 货运、 11 公路客运、 12 公交客运、 13 出租客运、 14 旅游客运），单选
        if(configUseNature){
            int useNature;
            set = tflShiyongxingzhi.getSelectedList();
            if(set.size()==0){//一个也没选中
                useNature= -1;
            }else{
                useNature = set.iterator().next()+1;
                if(useNature == 1){
                    useNature = 2;
                }else if(useNature == 2){
                    useNature = 3;
                }else if(useNature == 3){
                    useNature = 4;
                }else if(useNature == 4){
                    useNature = 1;
                }
            }
            if(isCheck){
                if(!(isDrivingNone&&isRegisterNone)) {
                    if (useNature == -1) {
                        MyToast.showShort(hintHead + "请选择使用性质");
                        return false;
                    }
                }
            }
            submitModel.setService(useNature);
            procedureModel.setService(useNature);
        }else{
            submitModel.setService(-1);
            procedureModel.setService(-1);
        }

        //获得方式    车辆获得方式（ 1 购买、 2 仲裁裁判、 3 继承、 4 赠与、 5 协议抵偿债务、 6 中奖、 7 资产重组、 8 资产整体买卖、 9 调拨、 10 境外自带、 11 法院调解 / 裁定 / 判决），单选
        if(configObtainWay){
            int obtainWay;
            set = tflHuodeFangshi.getSelectedList();
            if(set.size()==0){//一个也没选中
                obtainWay= -1;
            }else{
                obtainWay = set.iterator().next()+1;
            }
            if(isCheck&&!isRegisterNone){
                if(obtainWay==-1){
                    MyToast.showShort(hintHead+"请选择获得方式");
                    return false;
                }
            }
            submitModel.setCarGetWay(obtainWay);
            procedureModel.setCarGetWay(obtainWay);
        }else{
            submitModel.setCarGetWay(-1);
            procedureModel.setCarGetWay(-1);
        }

        //出厂日期
        if(configProductDate){
            String productionDate = tvProductionDate.getText().toString();
            if(isCheck){
                if(TextUtils.isEmpty(productionDate)){
                    MyToast.showShort(hintHead+"请填写出厂日期");
                    return false;
                }
            }
            submitModel.setProductionTime(productionDate);
            procedureModel.setProductionTime(productionDate);
        }else{
            submitModel.setProductionTime("");
            procedureModel.setProductionTime("");
        }

        StringBuilder sbSelected = new StringBuilder();
        //登记证附加信息  此项允许为空   登记证附加信息（ 1 正在抵押、 2 发动机号变更、 3 重打车架号、 4 登记证补领、 5 颜色变更），复选（英文逗号分隔）
        if(configRegisterAdditionInfo){
            set = tflDJZFujiaxinxi.getSelectedList();
            sbSelected.delete(0,sbSelected.length());

/*            if(isCheck&&!isRegisterNone){
                if(set.size()==0){
                    MyToast.showShort(hintHead+"请选择登记证附加信息");
                    return false;
                }
            }*/
            if(set.size()==0){//一个也没选中

            }else{
                for (int value : set) {
                    sbSelected.append((value+1)+",");
                }
                sbSelected.deleteCharAt(sbSelected.length()-1);
            }
            String registerAdditionInfoSelected = sbSelected.toString();
            submitModel.setCertificateEx(registerAdditionInfoSelected);
            procedureModel.setCertificateEx(registerAdditionInfoSelected);
            //登记证附加信息 说明
            String registerAdditionInfo = etRegisterAdditionInfo.getText().toString();
            submitModel.setCertificateExDes(registerAdditionInfo);
            procedureModel.setCertificateExDes(registerAdditionInfo);
        }else{
            submitModel.setCertificateEx("");
            procedureModel.setCertificateEx("");
            submitModel.setCertificateExDes("");
            procedureModel.setCertificateExDes("");
        }

        //初登地区
        if(configRegion&&llRegisterRegion.getVisibility()!=View.GONE){
            String registerRegion = tvRegisterRegion.getText().toString();
            if(isCheck&&!isRegisterNone){
                if(TextUtils.isEmpty(registerRegion)){
                    MyToast.showShort(hintHead+"请填写初登地区");
                    return false;
                }
            }
            procedureModel.setRegisterprovID(submitModel.getRegisterprovID());
            procedureModel.setProvName(submitModel.getProvName());
            procedureModel.setRegisterCityID(submitModel.getRegisterCityID());
            procedureModel.setCityName(submitModel.getCityName());
        }else{
            submitModel.setRegisterprovID(-1);
            submitModel.setProvName("");
            submitModel.setRegisterCityID(-1);
            submitModel.setCityName("");

            procedureModel.setRegisterprovID(-1);
            procedureModel.setProvName("");
            procedureModel.setRegisterCityID(-1);
            procedureModel.setCityName("");
        }

        //曾使用方   (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
        if(configOldUser&&llOldUser.getVisibility()!=View.GONE){
            int oldUser;
            set = tflCengshiyongfang.getSelectedList();
            if(set.size()==0){//一个也没选中
                oldUser= -1;
            }else{
                oldUser = set.iterator().next()+1;
            }
            if(isCheck&&!isRegisterNone){
                if(oldUser==-1){
                    MyToast.showShort(hintHead+"请选择曾使用方");
                    return false;
                }
            }
            submitModel.setOldUseOwner(oldUser);
            procedureModel.setOldUseOwner(oldUser);
        }else{
            submitModel.setOldUseOwner(-1);
            procedureModel.setOldUseOwner(-1);
        }

        //现使用方   现使用方 (1 仅个人记录、 2 有单位记录、 3 有出租车记录、 4 有汽车租赁公司记录、 5 有汽车销售（服务）公司记录 ) ，单选
        if(configCurUser&&llCurUser.getVisibility()!=View.GONE){
            int curUser;
            set = tflXianshiyongfang.getSelectedList();
            if(set.size()==0){//一个也没选中
                curUser= -1;
            }else{
                curUser = set.iterator().next()+1;
            }
            if(isCheck&&!isRegisterNone){
                if(curUser==-1){
                    MyToast.showShort(hintHead+"请选择现使用方");
                    return false;
                }
            }
            submitModel.setNowUseOwner(curUser);
            procedureModel.setNowUseOwner(curUser);
        }else{
            submitModel.setNowUseOwner(-1);
            procedureModel.setNowUseOwner(-1);
        }

        //备用钥匙   (（0--0；1--1；2--2及以上；-1--空） ) ，单选
        if(configExtraKey&&llExtraKey.getVisibility()!=View.GONE){
            int extraKey;
            set = tflExtraKey.getSelectedList();
            if(set.size()==0){//一个也没选中
                extraKey= -1;
            }else{
                extraKey = set.iterator().next();
            }
            if(isCheck){
                if(extraKey==-1){
                    MyToast.showShort(hintHead+"请选择钥匙");
                    return false;
                }
            }
            submitModel.setSpareKey(extraKey);
            procedureModel.setSpareKey(extraKey);
        }else{
            submitModel.setSpareKey(-1);
            procedureModel.setSpareKey(-1);
        }

        //排放标准  (国二及以下：1，国三：2，国四：3，国五：4，无法判断：5 )，单选
        if(configEmissionStandard&&llEmissionStandard.getVisibility()!=View.GONE){
            int effluentStd;
            set = tflEmissionStandard.getSelectedList();
            if(set.size()==0){//一个也没选中
                effluentStd= -1;
            }else{
                effluentStd = set.iterator().next() + 1;
            }
            if(isCheck){
                if(effluentStd==-1){
                    MyToast.showShort(hintHead+"请选择排放标准");
                    return false;
                }
            }
            submitModel.setEffluentStd(effluentStd);
            procedureModel.setEffluentStd(effluentStd);

            //排放标准参考
            procedureModel.setEffluentStdRef(tvEmissionStandard.getText().toString());
        }else{
            submitModel.setEffluentStd(-1);
            procedureModel.setEffluentStd(-1);
        }


        //交强险保单   交强险保单 (1 正常、 2 未见、 3 被保险人与车主不一致 ) ，单选
        if(configInsuranceBill&&llInsuranceBill.getVisibility()!=View.GONE){
            int insuranceBill;
            set = tflJiaoqiangxianbaodan.getSelectedList();
            if(set.size()==0){//一个也没选中
                insuranceBill= -1;
            }else{
                insuranceBill = set.iterator().next()+1;
            }
            if(isCheck){
                if(insuranceBill==-1){
                    MyToast.showShort(hintHead+"请选择交强险保单");
                    return false;
                }
            }
            if (insuranceBill == 2){
                submitModel.setInsuranceProvID(-1);
                submitModel.setInsuranceProvName("");
                submitModel.setInsuranceCityID(-1);
                submitModel.setInsuranceCityName("");

                procedureModel.setInsuranceProvID(-1);
                procedureModel.setInsuranceProvName("");
                procedureModel.setInsuranceCityID(-1);
                procedureModel.setInsuranceCityName("");
            }
            submitModel.setTrafficInsuranceHave(insuranceBill);
            procedureModel.setTrafficInsuranceHave(insuranceBill);
        }else{
            submitModel.setTrafficInsuranceHave(-1);
            procedureModel.setTrafficInsuranceHave(-1);
        }

        //交强险到期日
        if(configInsuranceExpDate&&llInsuranceExpDate.getVisibility()!=View.GONE){
            String comInsuranceExDate = tvComInsuranceExDate.getText().toString();
            if(isCheck&&submitModel.getTrafficInsuranceHave()!=2){
                if(TextUtils.isEmpty(comInsuranceExDate)){
                    MyToast.showShort(hintHead+"请填写交强险到期日");
                    return false;
                }
            }
            submitModel.setInsurance(comInsuranceExDate);
            procedureModel.setInsurance(comInsuranceExDate);
        }else{
            submitModel.setInsurance("");
            procedureModel.setInsurance("");
        }

        //交强险所在地
        if(configInsuranceAddress&&llInsuranceAddress.getVisibility()!=View.GONE){
            String insuranceAddress = tvComInsuranceAddress.getText().toString();
            if(isCheck&&submitModel.getTrafficInsuranceHave()!=2){
                if(TextUtils.isEmpty(insuranceAddress)){
                    MyToast.showShort(hintHead+"请填写交强险所在地");
                    return false;
                }
            }

            procedureModel.setInsuranceProvID(submitModel.getInsuranceProvID());
            procedureModel.setInsuranceProvName(submitModel.getInsuranceProvName());
            procedureModel.setInsuranceCityID(submitModel.getInsuranceCityID());
            procedureModel.setInsuranceCityName(submitModel.getInsuranceCityName());

        }else{
            submitModel.setInsuranceProvID(-1);
            submitModel.setInsuranceProvName("");
            submitModel.setInsuranceCityID(-1);
            submitModel.setInsuranceCityName("");

            procedureModel.setInsuranceProvID(-1);
            procedureModel.setInsuranceProvName("");
            procedureModel.setInsuranceCityID(-1);
            procedureModel.setInsuranceCityName("");
        }


        //原车发票
        if(configCarBill){
            //过户次数小于等于0时，保存原车发票信息
            if(submitModel.getTransferCount() <= 0) {
                String billMoney = etBillMoney.getText().toString();
                long fBillMoney = -1;
                if (!TextUtils.isEmpty(billMoney)) {
                    fBillMoney = Long.valueOf(billMoney);
                }

                submitModel.setCarInvoiceMoney(fBillMoney);
                procedureModel.setCarInvoiceMoney(fBillMoney);
                //开票日期
                String billDate = tvBillDate.getText().toString();
                submitModel.setCarInvoiceDate(billDate);
                procedureModel.setCarInvoiceDate(billDate);
                //工商章   原车发票 (1 无工商章、 2 未见， 3 有 ) ，单选
                int industrialStamp;
                set = tflYuanchefapiao.getSelectedList();
                if (set.size() == 0) {//一个也没选中
                    industrialStamp = 3;
                } else {
                    industrialStamp = set.iterator().next() + 1;
                }
                if (isCheck) {
                    if (industrialStamp != 2) {//原车发票未见
                        if (TextUtils.isEmpty(billMoney) || TextUtils.isEmpty(billDate) || fBillMoney == -1) {
                            MyToast.showShort(hintHead + "请填写发票金额、开票日期或原车发票选择未见");
                            return false;
                        }
                    }
                }
                submitModel.setCarInvoiceHave(industrialStamp);
                procedureModel.setCarInvoiceHave(industrialStamp);
            }else{
                submitModel.setCarInvoiceMoney(0);
                procedureModel.setCarInvoiceMoney(0);

                submitModel.setCarInvoiceDate("");
                procedureModel.setCarInvoiceDate("");

                submitModel.setCarInvoiceHave(0);
                procedureModel.setCarInvoiceHave(0);
            }
        }else{
            submitModel.setCarInvoiceMoney(-1);
            procedureModel.setCarInvoiceMoney(-1);

            submitModel.setCarInvoiceDate("");
            procedureModel.setCarInvoiceDate("");

            submitModel.setCarInvoiceHave(0);
            procedureModel.setCarInvoiceHave(0);
        }


        //其它票证  其它票证 (1 过户票、 3 进口关单、 4 购置税完税证明（征税）、 5 购置税完税证明（免税） ) ，复选（英文逗号分隔）
        if(configOtherBill){
            set = tflQitapiaozheng.getSelectedList();//可能不包含‘进口关单’'过户票'
            sbSelected.delete(0,sbSelected.length());
            if(set.size()==0){//一个也没选中
            }else{
                //得到选中的标签对应的ID
                int count = tflQitapiaozheng.getAdapter().getCount();
                for (int i = 0; i < count; i++) {
                    if(set.contains(i)){
                        String tagName = (String)tflQitapiaozheng.getAdapter().getItem(i);
                        if("过户票".equals(tagName)){
                            sbSelected.append(1+",");
                        }else if("进口关单".equals(tagName)){
                            sbSelected.append(3+",");
                        }else if("购置税完税证明（征税）".equals(tagName)){
                            sbSelected.append(4+",");
                        }else if("购置税完税证明（免税）".equals(tagName)){
                            sbSelected.append(5+",");
                        }
                    }
                }

                sbSelected.deleteCharAt(sbSelected.length()-1);
            }
            String otherBill = sbSelected.toString();
            //MyToast.showShort("其它票证 : "+otherBill);
            submitModel.setCarInvoiceOther(otherBill);
            procedureModel.setCarInvoiceOther(otherBill);
        }else{
            submitModel.setCarInvoiceOther("");
            procedureModel.setCarInvoiceOther("");
        }


        //车辆铭牌
        procedureModel.setNameplate(StringUtils.null2Length0(submitModel.getNameplate()));
        //品牌和型号 之间用逗号分隔
        procedureModel.setBrandType(StringUtils.null2Length0(submitModel.getBrandType()));

        //合格证数据是否为空
        if(carInfoModel==null){
            procedureModel.setHasCertificateData(false);
        }else{
            procedureModel.setHasCertificateData(true);
        }

        String json = new Gson().toJson(procedureModel);
        //保存到数据库
        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_PROCEDURE, taskId, getUser().getUserId(),json);

        return true;

    }

    /**
     * 行驶证未见和登记证未见同时选中，登记日期、品牌型号、发动机号、使用性质、燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；
     * @param isNone
     */
    private void setDisableByDrivingIsNone(boolean isNone){
        if(isNone){
            //登记日期
            tvRegisterDateName.setTextColor(getResources().getColor(R.color.global_gray_7));
            //品牌型号
            tvCarType.setTextColor(getResources().getColor(R.color.global_gray_7));
            //发动机号
            tvEngineId.setTextColor(getResources().getColor(R.color.global_gray_7));
            //使用性质
            tvUseNature.setTextColor(getResources().getColor(R.color.global_gray_7));
        }else{
            //登记日期
            tvRegisterDateName.setTextColor(getResources().getColor(R.color.global_gray_6));
            //品牌型号
            tvCarType.setTextColor(getResources().getColor(R.color.global_gray_6));
            //发动机号
            tvEngineId.setTextColor(getResources().getColor(R.color.global_gray_6));
            //使用性质
            tvUseNature.setTextColor(getResources().getColor(R.color.global_gray_6));
        }
    }

    /**
     * 根据登记证是否可见设置字体颜色
     * @param isNone
     */
    private void setDisableByRejesterIsNone(boolean isNone){
        //选中登记证未见（行驶证可见），燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；
        //行驶证未见和登记证未见同时选中，登记日期、品牌型号、发动机号、使用性质、燃料种类、轮胎规格、核定载客数，不可输入并清空已输入信息，提交时可以为空；
        if(isNone){
            //登记证照片
            tvRegister.setTextColor(getResources().getColor(R.color.global_gray_7));
            //机动车所有人
            tvVehicleOwener.setTextColor(getResources().getColor(R.color.global_gray_7));
            //证件类型
            tvCard.setTextColor(getResources().getColor(R.color.global_gray_7));
            //车主证件号
            tvOwnerId.setTextColor(getResources().getColor(R.color.global_gray_7));

            //过户次数
            tvTransferNum.setTextColor(getResources().getColor(R.color.global_gray_7));
            //最后过户日期
            tvLastTransferDateName.setTextColor(getResources().getColor(R.color.global_gray_7));
            if(cBoxDrivingNone.isChecked()&&cBoxRegisterNone.isChecked()){
                //登记日期
                tvRegisterDateName.setTextColor(getResources().getColor(R.color.global_gray_7));
                //品牌型号
                tvCarType.setTextColor(getResources().getColor(R.color.global_gray_7));
                //发动机号
                tvEngineId.setTextColor(getResources().getColor(R.color.global_gray_7));
                //使用性质
                tvUseNature.setTextColor(getResources().getColor(R.color.global_gray_7));
            }

            //登记证车身颜色
            tvCarColor.setTextColor(getResources().getColor(R.color.global_gray_7));

            //燃料种类
            tvFuelType.setTextColor(getResources().getColor(R.color.global_gray_7));
            //轮胎规格
            tvTireModel.setTextColor(getResources().getColor(R.color.global_gray_7));

            //获得方式
            tvObtainWay.setTextColor(getResources().getColor(R.color.global_gray_7));
            //登记证附加信息
            tvRegisterAdditionInfo.setTextColor(getResources().getColor(R.color.global_gray_7));
            //初登地区
            tvRegisterRegionName.setTextColor(getResources().getColor(R.color.global_gray_7));
            //曾使用方
            tvOldUser.setTextColor(getResources().getColor(R.color.global_gray_7));
            //现使用方
            tvCurUser.setTextColor(getResources().getColor(R.color.global_gray_7));
            //核定载客数
            tvBusLoad.setTextColor(getResources().getColor(R.color.global_gray_7));
        }else{
            tvRegister.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvVehicleOwener.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvCard.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvOwnerId.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvRegisterDateName.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvTransferNum.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvLastTransferDateName.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvCarType.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvCarColor.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvEngineId.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvFuelType.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvTireModel.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvUseNature.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvObtainWay.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvRegisterAdditionInfo.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvRegisterRegionName.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvOldUser.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvCurUser.setTextColor(getResources().getColor(R.color.global_gray_6));
            tvBusLoad.setTextColor(getResources().getColor(R.color.global_gray_6));
        }

    }

    /**
     * 根据是否可输入设置字体颜色
     * boo true 表示可输入
     */
    private void setDisableTv(TextView tv,boolean boo) {
        if (!boo) {
            tv.setTextColor(getResources().getColor(R.color.global_gray_7));
        }else{
            tv.setTextColor(getResources().getColor(R.color.global_gray_6));
        }
    }

    /**
     * 如果日期长度超过10位，截取前10位
     *
     * @return
     */
    private String dateSplit(String date){
        if(TextUtils.isEmpty(date)){
            return date;
        }else if(date.length()>10){
            return date.substring(0,10);
        }else{
            return date;
        }
    }

    /**
     * 如果日期长度超过length位，截取前length位
     *
     * @return
     */
    private String dateSplit(String date,int length){
        if(TextUtils.isEmpty(date)){
            return date;
        }else if(date.length()>length){
            return date.substring(0,length);
        }else{
            return date;
        }
    }


    /***
     *
     * @param title dialog标题
     * @param tvContent 回显TextView
     * @param maxYear 最大年
     * @param minYear 最小年
     * @param laterThanNow 是否能晚于当前时间
     */
    private void selectYearMonth(final String title,final TextView tvContent,int maxYear,int minYear,final boolean laterThanNow){
        View v = UIUtils.inflate(R.layout.layout_year_month_picker);
        final QNumberPicker npYear = (QNumberPicker) v.findViewById(R.id.npYear);
        final QNumberPicker npMonth = (QNumberPicker) v.findViewById(R.id.npMonth);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        Calendar calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH);
        npYear.setMaxValue(maxYear);
        npYear.setMinValue(minYear);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);
        String ym = tvContent.getText().toString().trim();
        String[] arr = ym.split("-");
        int selectedYear = nowYear;
        int selectedMonth = nowMonth+1;
        if(arr.length==2){
            selectedYear = Integer.valueOf(arr[0]);
            selectedMonth = Integer.valueOf(arr[1]);
        }
        npYear.setValue(selectedYear);
        npMonth.setValue(selectedMonth);
        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle("请选择"+title).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int selectYear = npYear.getValue();
                int selectMonth = npMonth.getValue();
                if(!laterThanNow){
                    if(selectYear>nowYear){
                        MyToast.showLong(title+"不能晚于当前日期");
                        return;
                    }
                    if(selectYear==nowYear && selectMonth>(nowMonth+1)){
                        MyToast.showLong(title+"不能晚于当前日期");
                        return;
                    }
                }
                tvContent.setText(selectYear+"-"+selectMonth);

                carTypeSelectCanTab(etVIN.getText().toString(),tvProductionDate.getText().toString());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 滚轮日历
     * @param textView 控件
     * @param titlePrefix 如"开票日期"
     * @param laterThanNow 能否选择今天之后的日期 true表示可以，false表示不可以
     * @author zealjiang
     * @time 2017/1/16 13:52
     */
    private void selectDate(final TextView textView,final String titlePrefix,final boolean laterThanNow){
        int year,month,day;
        String date = textView.getText().toString();
        if(TextUtils.isEmpty(date)||date.length()<8){
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH)+1;
            day = calendar.get(Calendar.DATE);
        }else {
            String[] ymd = date.split("-");
            year = Integer.valueOf(ymd[0]);
            month = Integer.valueOf(ymd[1]);
            day = Integer.valueOf(ymd[2]);
        }
        dayPickerDialog = new DayPickerDialog(self.getContext(), titlePrefix,year,month,day,laterThanNow);
        dayPickerDialog.createDialog();
        dayPickerDialog.setDayPickerOkListenter(new DayPickerDialog.DayPickerOkListenter(){

            @Override
            public void selectDate(int year, int month,int day, String date) {
                textView.setText(date);
            }
        });
    }



    /**
     * Created by 李波 on 2016/12/19.
     * 从车况检测点击其他信息界面时的 基本照片的状态判断
     *
     * @param isShowHint 没填写完是否显示提示信息，true显示，false不显示
     */
    public boolean checkBasePhoto(boolean isShowHint){
        //如果行驶证可见，行驶证照片不可为空

        if (llDriving.getVisibility()==View.VISIBLE) { //当行驶证有的时候才判断

            boolean isDrivingNone = cBoxDrivingNone.isChecked();
            if (!isDrivingNone) {

                //行驶证正本正面
                if (TextUtils.isEmpty(pictureItemsDL.get(0).getPicPath())) {
                    if(isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }

                //行驶证正本背面
                if (TextUtils.isEmpty(pictureItemsDL.get(1).getPicPath())) {
                    if(isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }
                //行驶证副本正面
                if (TextUtils.isEmpty(pictureItemsDL.get(2).getPicPath())) {
                    if(isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }
                //行驶证副本背面
                if (TextUtils.isEmpty(pictureItemsDL.get(3).getPicPath())) {
                    if(isShowHint) {
                        MyToast.showShort("请先完善手续信息行驶证照片");
                    }
                    return false;
                }
            }

        }

        if (llRegister.getVisibility() == View.VISIBLE) {  //当登记证有的时候才判断

            //如果登记证可见，登记证照片不可为空
            boolean isRegisterNone = cBoxRegisterNone.isChecked();
            if (!isRegisterNone) {//
                //登记证1-2页照片
                if (TextUtils.isEmpty(pictureItemsDLRegister.get(0).getPicPath())) {
                    if(isShowHint) {
                        MyToast.showShort("请先完善手续信息登记证照片");
                    }
                    return false;
                }
                //登记证3-4页照片
                if (TextUtils.isEmpty(pictureItemsDLRegister.get(1).getPicPath())) {
                    if(isShowHint) {
                        MyToast.showShort("请先完善手续信息登记证照片");
                    }
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public void succeed(ProvinceCityUniqueModel provinceCityUniqueModel) {
        //车牌号码查询上牌地区

        if (tvPlateRegion.isEnabled()) {
            if (provinceCityUniqueModel != null) {
                submitModel.setOnCardProvID(provinceCityUniqueModel.getProvinceId());
                submitModel.setOnCardProvName(provinceCityUniqueModel.getProvinceName());
                submitModel.setOnCardCityID(provinceCityUniqueModel.getCityId());
                submitModel.setOnCardCityName(provinceCityUniqueModel.getCityName());

                if (provinceCityUniqueModel.getProvinceName().equals(provinceCityUniqueModel.getCityName())) {
                    tvPlateRegion.setText(provinceCityUniqueModel.getProvinceName());
                } else {
                    tvPlateRegion.setText(provinceCityUniqueModel.getProvinceName() + provinceCityUniqueModel.getCityName());
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventProcedurePhoto eventProcedurePhoto) {
        if(eventProcedurePhoto.isHasRefreshed()){
            //刷新照片
            refreshDLPic();
            refreshDLRegisterPic();
        }

        if (eventProcedurePhoto.isDelRegister23()){
            delPic(0,1);
        }

        if (eventProcedurePhoto.isDelRegister28()){
            delPic(1,2);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventModel eventModel) {
        if(eventModel.getName().equals("productDate")){
            tvProductionDate.setText(eventModel.getValue());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e("ProcedureCarFragment","onPause");
        checkAndsaveData(false, false,"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("ProcedureCarFragment","onDestroy"+this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e("ProcedureCarFragment","onResume"+this);
        //刷新照片
        refreshDLPic();

        //TODO 登记证照片是不是也要加上呢
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
        }
        //LogUtil.e("ProcedureCarFragment","onCreate"+this+"   submitModel: "+submitModel);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(UIUtils.getContext(), error, Toast.LENGTH_LONG).show();
    }


    public String getPathRegisterMorePI() {
        return registerMorePI.getPicPath();
    }

    public void setPathRegisterMorePI(String path) {
        registerMorePI.setPicPath(path);
    }

    public String getPathRegisterMorePI2() {
        return registerMorePI2.getPicPath();
    }

    public void setPathRegisterMorePI2(String path) {
        registerMorePI2.setPicPath(path);
    }

    /**
     * 确认是否有VIN；VIN是否正确；
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
        btnSubmit.setText("确认并查询维保");
        myUniversalDialog.setLayoutView(view,480,260);
        boolean hasVIN = true;
        if(TextUtils.isEmpty(etVIN.getText().toString())){
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
                }
                else {
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
                 * 先校验VIN，正确，且不与修改之前的VIN相同则查询维保
                 */
                String vin = editVin.getText().toString();
                boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
                if(isValid){
                    //判断是否与上次VIN相同，相同则直接关闭对话框
                    String oldVin = etVIN.getText().toString();
                    if(vin.equals(oldVin)){
                        myUniversalDialog.cancel();
                    }else{
                        if (vinCheckedPresenter == null){
                            vinCheckedPresenter = new VinCheckedPresenter(ProcedureCarFragment.this);
                        }
                        vinCheckedPresenter.getVinCheckedResult(getUser().getUserId()+"", vin,taskId,myUniversalDialog);
                    }
                }else{
                    MyToast.showLong("请输入正确VIN");
                }
            }
        });

    }
    @Override
    public void requestVinCheckedSucceed(ResponseJson<VinCheckedModel> response,MyUniversalDialog myUniversalDialog,String vin) {
        onVinClick(vin);

        myUniversalDialog.cancel();

        VinCheckedModel memberValue = response.getMemberValue();
        int isAlertAccdient = memberValue.getIsAlertAccdient();
        String accdientAlertMsg = memberValue.getAccdientAlertMsg();
        if (isAlertAccdient == 1){
            showAlertAccident(accdientAlertMsg);
        }
    }
    private void showAlertAccident(String accdientAlertMsg) {
        if (!TextUtils.isEmpty(accdientAlertMsg)){
            final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(context);
            View view = myUniversalDialog.getLayoutView(R.layout.dialog_check_vin);
            TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            TextView tvIknow = (TextView) view.findViewById(R.id.tvIknow);
            tvMsg.setText(accdientAlertMsg);
            myUniversalDialog.setLayoutView(view,480,260);
            myUniversalDialog.show();
            tvIknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myUniversalDialog.cancel();
                }
            });
        }
    }

    @Override
    public void requestVinCheckedFailed(String message,MyUniversalDialog myUniversalDialog) {
        vinCheckedFailMsg = message;

        View view = myUniversalDialog.getView();
        TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.VISIBLE);
        if (tvMsg != null){
            tvMsg.setText(vinCheckedFailMsg);
        }
    }

    /**
     * 查询维保
     * @param isOne     是否是第一次进入，0 是，1是输入编辑
     * @param userId       用户id
     * @param vinStr        vin
     */

    public void  showVINDialog(final int isOne, final String userId, final  String vinStr) {

        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(context);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog_confrim_vin_layout);
        final EditText edit_DialogVin = (EditText) view.findViewById(R.id.edit_DialogVin);
        TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_hint_msg = (TextView) view.findViewById(R.id.tv_dialog_hint_msg);
        TextView tvCheckVinMsg = (TextView) view.findViewById(R.id.tvCheckVinMsg);
        if(isOne == 0){
            tv_dialog_title.setText("请确认车架号VIN码是否正确？");
            tv_dialog_hint_msg.setText("点击取消系统将不会自动查询维保记录，如需查询请点击车架号VIN码进行查询");
        }else if(isOne == 1){
            tv_dialog_title.setText("请输入车架号VIN码");
            tv_dialog_hint_msg.setText("");
        }

        TextView tvleft = (TextView) view.findViewById(R.id.tvleft);
        tvleft.setText("取消");
        TextView tvright = (TextView) view.findViewById(R.id.tvright);
        tvright.setText("确认并查询维保");
        edit_DialogVin.setTransformationMethod(new InputLowerToUpper());
        edit_DialogVin.setText(vinStr);
        edit_DialogVin.setSelection(edit_DialogVin.getText().length());
        if(TextUtils.isEmpty(vinStr)){
            //弹出软键盘
            KeyboardUtils.showSoftInput(context,edit_DialogVin);
        }
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
                String  vin = edit_DialogVin.getText().toString();
                if(!TextUtils.isEmpty(vin) && vin.length()==17){
                    boolean isValid = CarVINCheck.getInstance().isVINValid(vin);
                    if(isValid){
                        if (vinCheckedPresenter == null){
                            vinCheckedPresenter = new VinCheckedPresenter(ProcedureCarFragment.this);
                        }
                        vinCheckedPresenter.getVinCheckedResult(getUser().getUserId()+"", vin,taskId,myUniversalDialog);
                    }else{
                        MyToast.showLong("请输入正确VIN");
                    }
                }else{
                    MyToast.showLong("请输入正确VIN");
                }

            }
        });

    }

}
