package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 提交数据对象
 * Created by zealjiang on 2016/11/24 20:08.
 * Email: zealjiang@126.com
 */

public class SubmitModel implements Serializable{


    /**
     * Id : 0
     * DrivingLicenseProperty : 1
     * DrivingLicenseCheckEx : aaa
     * RegistLicenseProperty : 1
     * CardType : 1
     * CardNum : aaa
     * IsCardSame : 1
     * CarLicenseHave : 1
     * Seating : 1
     * CarGetWay : 1
     * Color : 1
     * CarColorDes : aaa
     * CarColorActual : 1
     * TurboChargingHave : 1
     * Tyre : aaa
     * TyreProperty : aaa
     * Service : 1
     * LastTransferDate : 2016-10-15
     * NowUseOwner : 1
     * TrafficInsuranceHave : 1
     * Insurance : 2016-10-15
     * CarInvoiceHave : 1
     * CarInvoiceMoney : 1
     * CarInvoiceDate : 2016-10-15
     * CarInvoiceOther : aaa
     * CertificateEx : aaa
     * CertificateExDes : aaa
     * Inspection : 2016-10-15
     * Mileage : 1
     * MileageSame : 1
     * NameplateProperty : aaa
     * VinProperty : aaa
     * AppearanceCheck : 1
     * AccidentCheck : 1
     * EngineCheck : 1
     * DecorateCheck : 1
     * ChassisCheck : 1
     * ElectricalCheck : 1
     * BurningMark : 1
     * WaterMark : 1
     * MarketOwnership : 1
     * MarketAcceptance : 1
     * MarketHedgeRatio : 1
     * LikeMan : aaa
     * RecordDate : 2016-10-15
     * ProvID : 1
     * CityID : 1
     * CarLicense : aaa
     * RecordBrand : aaa
     * Vin : aaa
     * EngineNum : aaa
     * FuelType : 1
     * IsImport : 1
     * Exhaust : aaa
     * ProductionTime : 2016-10-15
     * TransferCount : 1
     * AssessmentDes : aaa
     * MakeID : 1
     * ModelID : 1
     * StyleID : 1
     * DefectValue : ["L01_P08_A003_F01_D001","L02_P09_A003_F01_D001","L03_P09_A003_F01_D001"]
     * DeletePicId : ["L01_P08_A003_F01_D001","L02_P09_A003_F01_D001","L03_P09_A003_F01_D001"]
     */

    private int Id;
    private int DrivingLicenseProperty;
    private String DrivingLicenseCheckEx;
    private int RegistLicenseProperty;
    private int CardType;
    private String CardNum;
    private int IsCardSame;
    private int CarLicenseHave;
    private int Seating;
    private int CarGetWay;
    private int Color;
    private String CarColorDes;

    //取值里有 0 ，所以初始值设置为 -1  -> 李波 on 2016/12/7.
    private int Mileage=-1;
    private int MileageSame=-1;
    private int GaugeLamp=-1;
    private int CarColorActual=-1;

    private int TurboChargingHave;
    private String Tyre;
    private String TyreProperty;
    private int Service;
    private String LastTransferDate;
    private int OldUseOwner;//曾使用方
    private int NowUseOwner;
    private int TrafficInsuranceHave;
    private String Insurance;
    private int CarInvoiceHave;
    private long CarInvoiceMoney;
    private String CarInvoiceDate;
    private String CarInvoiceOther;
    private String CertificateEx;
    private String CertificateExDes;
    private String Inspection;
    private String NameplateProperty;
    private String VinProperty;
    private int AppearanceCheck;
    private int AccidentCheck;
    private int EngineCheck;
    private int DecorateCheck;
    private int ChassisCheck;
    private int ElectricalCheck;
    private int BurningMark;
    private int WaterMark;
    private int MarketOwnership;
    private int MarketAcceptance;
    private int MarketHedgeRatio;
    private String CarOwner;
    private String RecordDate;
    private int ProvID;
    private String ProvName;
    private int CityID;
    private String CityName;
    private int RegisterprovID;
    private int RegisterCityID;

    private String CarLicense;
    private String RecordBrand;
    private String Vin;
    private String EngineNum;
    private int FuelType;
    private int IsImport;
    private String Exhaust;
    private String ProductionTime;
    private int TransferCount;
    private String AssessmentDes;
    private int MakeID;
    private int ModelID;
    private int StyleID;
    private String ManufacturerPrice;//厂商指导价
    private int HasPicZip;//判断文字提交后是否需要提交zip包 0表示没有zip包, 1表示有zip包

    private List<String> DefectValue = new ArrayList<>();
    private List<String> DeletePicId = new ArrayList<>();//网络图片删除的图片ID数组

    private float AssessmentPrace;
    private float SalePrice;
    private String OtherColor;
    private int CarLicenseEx;
    private String nameplate;//车辆铭牌 本地用
    private boolean InspectionIsConfirmLocal;//年检有效期 本地用

    //上牌地区
    private int OnCardProvID;
    private int OnCardCityID;
    private String OnCardProvName;
    private String OnCardCityName;

    //交强险所在地
    private int InsuranceProvID;
    private int InsuranceCityID;
    private String InsuranceProvName;
    private String InsuranceCityName;

    //备用钥匙
    private int SpareKey;
    private String ActivityId;//服务器用
    private int EffluentStd;//排放标准  国二及以下：1，国三：2，国四：3，国五：4，无法判断：5
    private String EffluentStdRef;//排放标准参考值

    //宝马新增  （ 是/合格/达标  这种值是1，否/不合格/不达标值是0）
    private String CarKeyDes;//钥匙   例：1,0
    private String PlateCount;//车牌数量    例：2,1
    private int RepairRecord;//维保记录可查   是：1，否：0
    private int AccidentRecord;//DMS无大事故记录  是：1，否：0
    private int AccidentRepair;//事故已记录已维修   是：1，否：0
    private int SettleRecord;//无未结算事故维修记录   是：1，否：0
    private int IsRecall;//技术召回 是：1，否：0
    private int NextRepairDays;//下次保养天数
    private int NextRepairMileage;//下次保养距离
    private int PetrolAlarm;//燃油表无报警  是：1，否：0
    private int StdInspection;//年检标  是：1，否：0
    private int CoyInsurance;//交强险标  是：1，否：0
    private int UserManual;//用户手册  是：1，否：0
    private int Bordbuch;//随车说明书  是：1，否：0
    private int ThreeGuarantees;//三包凭证  是：1，否：0
    private int GuaranteeHandbook;//保修服务手册  是：1，否：0
    private int RescueCall;//救援电话   是：1，否：0
    private int Environmental;//环保凭证  是：1，否：0
    private int ContactHandbook;//授权经销商联系手册  是：1，否：0
    private int UserId;
    //本地需要用的参数，服务器不需要，是否点击的是继续检测
    private boolean isContinue;

    private int SpSourceId;//1:宝马

    public int getSpSourceId() {
        return SpSourceId;
    }

    public void setSpSourceId(int spSourceId) {
        SpSourceId = spSourceId;
    }

    //宝马扩展信息 包含外观示意图的标点数据  -> 李波 on 2017/11/13.
    private BmwTaskExModel bmwTaskExModel = new BmwTaskExModel();
    
    public BmwTaskExModel getBmwTaskExModel() {
        return bmwTaskExModel;
    }

    public void setBmwTaskExModel(BmwTaskExModel bmwTaskExModel) {
        this.bmwTaskExModel = bmwTaskExModel;
    }

   


    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    //查看配置
    private List<String> ConfigureValueList = new ArrayList<>();
    //查看配置 本地使用
    private LinkedHashMap<String,String> ConfigureSelectedMap = new LinkedHashMap <>();

    //统计时间 统计手续信息、车型选择、车况检测、车辆照片、其他信息的时间
    // TaskTimeRegionList 数据集合      TimeType  操作类型    BeginTime   开始时间   EndTime   结束时间
    private List<LinkedHashMap<String,String>> TaskTimeRegionList = new ArrayList<>();

    private String brandType;//品牌和型号 之间用逗号分隔 本地用（服务器通过vin查询返回）

    public float getAssessmentPrace() {
        return AssessmentPrace;
    }

    public void setAssessmentPrace(float assessmentPrace) {
        AssessmentPrace = assessmentPrace;
    }

    public float getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(float salePrice) {
        SalePrice = salePrice;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getDrivingLicenseProperty() {
        return DrivingLicenseProperty;
    }

    public void setDrivingLicenseProperty(int DrivingLicenseProperty) {
        this.DrivingLicenseProperty = DrivingLicenseProperty;
    }

    public String getDrivingLicenseCheckEx() {
        return DrivingLicenseCheckEx;
    }

    public void setDrivingLicenseCheckEx(String DrivingLicenseCheckEx) {
        this.DrivingLicenseCheckEx = DrivingLicenseCheckEx;
    }

    public int getRegistLicenseProperty() {
        return RegistLicenseProperty;
    }

    public void setRegistLicenseProperty(int RegistLicenseProperty) {
        this.RegistLicenseProperty = RegistLicenseProperty;
    }

    public int getCardType() {
        return CardType;
    }

    public void setCardType(int CardType) {
        this.CardType = CardType;
    }

    public String getCardNum() {
        return CardNum;
    }

    public void setCardNum(String CardNum) {
        this.CardNum = CardNum;
    }

    public int getIsCardSame() {
        return IsCardSame;
    }

    public void setIsCardSame(int IsCardSame) {
        this.IsCardSame = IsCardSame;
    }

    public int getCarLicenseHave() {
        return CarLicenseHave;
    }

    public void setCarLicenseHave(int CarLicenseHave) {
        this.CarLicenseHave = CarLicenseHave;
    }

    public int getSeating() {
        return Seating;
    }

    public void setSeating(int Seating) {
        this.Seating = Seating;
    }

    public int getCarGetWay() {
        return CarGetWay;
    }

    public void setCarGetWay(int CarGetWay) {
        this.CarGetWay = CarGetWay;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int Color) {
        this.Color = Color;
    }

    public String getCarColorDes() {
        return CarColorDes;
    }

    public void setCarColorDes(String CarColorDes) {
        this.CarColorDes = CarColorDes;
    }

    public int getCarColorActual() {
        return CarColorActual;
    }

    public void setCarColorActual(int CarColorActual) {
        this.CarColorActual = CarColorActual;
    }

    public int getTurboChargingHave() {
        return TurboChargingHave;
    }

    public void setTurboChargingHave(int TurboChargingHave) {
        this.TurboChargingHave = TurboChargingHave;
    }

    public String getTyre() {
        return Tyre;
    }

    public void setTyre(String Tyre) {
        this.Tyre = Tyre;
    }

    public String getTyreProperty() {
        return TyreProperty;
    }

    public void setTyreProperty(String TyreProperty) {
        this.TyreProperty = TyreProperty;
    }

    public int getService() {
        return Service;
    }

    public void setService(int Service) {
        this.Service = Service;
    }

    public String getLastTransferDate() {
        return LastTransferDate;
    }

    public void setLastTransferDate(String LastTransferDate) {
        this.LastTransferDate = LastTransferDate;
    }

    public int getOldUseOwner() {
        return OldUseOwner;
    }

    public void setOldUseOwner(int oldUseOwner) {
        OldUseOwner = oldUseOwner;
    }

    public int getNowUseOwner() {
        return NowUseOwner;
    }

    public void setNowUseOwner(int NowUseOwner) {
        this.NowUseOwner = NowUseOwner;
    }

    public int getTrafficInsuranceHave() {
        return TrafficInsuranceHave;
    }

    public void setTrafficInsuranceHave(int TrafficInsuranceHave) {
        this.TrafficInsuranceHave = TrafficInsuranceHave;
    }

    public String getInsurance() {
        return Insurance;
    }

    public void setInsurance(String Insurance) {
        this.Insurance = Insurance;
    }

    public int getCarInvoiceHave() {
        return CarInvoiceHave;
    }

    public void setCarInvoiceHave(int CarInvoiceHave) {
        this.CarInvoiceHave = CarInvoiceHave;
    }

    public long getCarInvoiceMoney() {
        return CarInvoiceMoney;
    }

    public void setCarInvoiceMoney(long CarInvoiceMoney) {
        this.CarInvoiceMoney = CarInvoiceMoney;
    }

    public String getCarInvoiceDate() {
        return CarInvoiceDate;
    }

    public void setCarInvoiceDate(String CarInvoiceDate) {
        this.CarInvoiceDate = CarInvoiceDate;
    }

    public String getCarInvoiceOther() {
        return CarInvoiceOther;
    }

    public void setCarInvoiceOther(String CarInvoiceOther) {
        this.CarInvoiceOther = CarInvoiceOther;
    }

    public String getCertificateEx() {
        return CertificateEx;
    }

    public void setCertificateEx(String CertificateEx) {
        this.CertificateEx = CertificateEx;
    }

    public String getCertificateExDes() {
        return CertificateExDes;
    }

    public void setCertificateExDes(String CertificateExDes) {
        this.CertificateExDes = CertificateExDes;
    }

    public String getInspection() {
        return Inspection;
    }

    public void setInspection(String Inspection) {
        this.Inspection = Inspection;
    }

    public int getMileage() {
        return Mileage;
    }

    public void setMileage(int Mileage) {
        this.Mileage = Mileage;
    }

    public int getMileageSame() {
        return MileageSame;
    }

    public void setMileageSame(int MileageSame) {
        this.MileageSame = MileageSame;
    }

    public int getGaugeLamp() {
        return GaugeLamp;
    }

    public void setGaugeLamp(int gaugeLamp) {
        this.GaugeLamp = gaugeLamp;
    }

    public String getNameplateProperty() {
        return NameplateProperty;
    }

    public void setNameplateProperty(String NameplateProperty) {
        this.NameplateProperty = NameplateProperty;
    }

    public String getVinProperty() {
        return VinProperty;
    }

    public void setVinProperty(String VinProperty) {
        this.VinProperty = VinProperty;
    }

    public int getAppearanceCheck() {
        return AppearanceCheck;
    }

    public void setAppearanceCheck(int AppearanceCheck) {
        this.AppearanceCheck = AppearanceCheck;
    }

    public int getAccidentCheck() {
        return AccidentCheck;
    }

    public void setAccidentCheck(int AccidentCheck) {
        this.AccidentCheck = AccidentCheck;
    }

    public int getEngineCheck() {
        return EngineCheck;
    }

    public void setEngineCheck(int EngineCheck) {
        this.EngineCheck = EngineCheck;
    }

    public int getDecorateCheck() {
        return DecorateCheck;
    }

    public void setDecorateCheck(int DecorateCheck) {
        this.DecorateCheck = DecorateCheck;
    }

    public int getChassisCheck() {
        return ChassisCheck;
    }

    public void setChassisCheck(int ChassisCheck) {
        this.ChassisCheck = ChassisCheck;
    }

    public int getElectricalCheck() {
        return ElectricalCheck;
    }

    public void setElectricalCheck(int ElectricalCheck) {
        this.ElectricalCheck = ElectricalCheck;
    }

    public int getBurningMark() {
        return BurningMark;
    }

    public void setBurningMark(int BurningMark) {
        this.BurningMark = BurningMark;
    }

    public int getWaterMark() {
        return WaterMark;
    }

    public void setWaterMark(int WaterMark) {
        this.WaterMark = WaterMark;
    }

    public int getMarketOwnership() {
        return MarketOwnership;
    }

    public void setMarketOwnership(int MarketOwnership) {
        this.MarketOwnership = MarketOwnership;
    }

    public int getMarketAcceptance() {
        return MarketAcceptance;
    }

    public void setMarketAcceptance(int MarketAcceptance) {
        this.MarketAcceptance = MarketAcceptance;
    }

    public int getMarketHedgeRatio() {
        return MarketHedgeRatio;
    }

    public void setMarketHedgeRatio(int MarketHedgeRatio) {
        this.MarketHedgeRatio = MarketHedgeRatio;
    }

    public String getCarOwner() {
        return CarOwner;
    }

    public void setCarOwner(String LikeMan) {
        this.CarOwner = LikeMan;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String RecordDate) {
        this.RecordDate = RecordDate;
    }

    public int getProvID() {
        return ProvID;
    }

    public void setProvID(int ProvID) {
        this.ProvID = ProvID;
    }

    public String getProvName() {
        return ProvName;
    }

    public void setProvName(String provName) {
        ProvName = provName;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int CityID) {
        this.CityID = CityID;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCarLicense() {
        return CarLicense;
    }

    public void setCarLicense(String CarLicense) {
        this.CarLicense = CarLicense;
    }

    public String getRecordBrand() {
        return RecordBrand;
    }

    public void setRecordBrand(String RecordBrand) {
        this.RecordBrand = RecordBrand;
    }

    public String getVin() {
        return Vin;
    }

    public void setVin(String Vin) {
        this.Vin = Vin;
    }

    public String getEngineNum() {
        return EngineNum;
    }

    public void setEngineNum(String EngineNum) {
        this.EngineNum = EngineNum;
    }

    public int getFuelType() {
        return FuelType;
    }

    public void setFuelType(int FuelType) {
        this.FuelType = FuelType;
    }

    public int getIsImport() {
        return IsImport;
    }

    public void setIsImport(int IsImport) {
        this.IsImport = IsImport;
    }

    public String getExhaust() {
        return Exhaust;
    }

    public void setExhaust(String Exhaust) {
        this.Exhaust = Exhaust;
    }

    public String getProductionTime() {
        return ProductionTime;
    }

    public void setProductionTime(String ProductionTime) {
        this.ProductionTime = ProductionTime;
    }

    public int getTransferCount() {
        return TransferCount;
    }

    public void setTransferCount(int TransferCount) {
        this.TransferCount = TransferCount;
    }

    public String getAssessmentDes() {
        return AssessmentDes;
    }

    public void setAssessmentDes(String AssessmentDes) {
        this.AssessmentDes = AssessmentDes;
    }

    public int getMakeID() {
        return MakeID;
    }

    public void setMakeID(int MakeID) {
        this.MakeID = MakeID;
    }

    public int getModelID() {
        return ModelID;
    }

    public void setModelID(int ModelID) {
        this.ModelID = ModelID;
    }

    public int getStyleID() {
        return StyleID;
    }

    public void setStyleID(int StyleID) {
        this.StyleID = StyleID;
    }

    public String getManufacturerPrice() {
        return ManufacturerPrice;
    }

    public void setManufacturerPrice(String manufacturerPrice) {
        ManufacturerPrice = manufacturerPrice;
    }

    public List<String> getDefectValue() {
        return DefectValue;
    }

    public void setDefectValue(List<String> DefectValue) {
        this.DefectValue = DefectValue;
    }

    public List<String> getDeletePicId() {
        return DeletePicId;
    }

    public void setDeletePicId(List<String> deletePicId) {
        DeletePicId = deletePicId;
    }

    public int getHasPicZip() {
        return HasPicZip;
    }

    public void setHasPicZip(int hasPicZip) {
        HasPicZip = hasPicZip;
    }

    public String getOtherColor() {
        return OtherColor;
    }

    public void setOtherColor(String otherColor) {
        OtherColor = otherColor;
    }

    public int getCarLicenseEx() {
        return CarLicenseEx;
    }

    public void setCarLicenseEx(int carLicenseEx) {
        CarLicenseEx = carLicenseEx;
    }

    public int getRegisterprovID() {
        return RegisterprovID;
    }

    public void setRegisterprovID(int registerprovID) {
        RegisterprovID = registerprovID;
    }

    public int getRegisterCityID() {
        return RegisterCityID;
    }

    public void setRegisterCityID(int registerCityID) {
        RegisterCityID = registerCityID;
    }

    public String getNameplate() {
        return nameplate;
    }

    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    public int getOnCardProvID() {
        return OnCardProvID;
    }

    public void setOnCardProvID(int onCardProvID) {
        OnCardProvID = onCardProvID;
    }

    public int getOnCardCityID() {
        return OnCardCityID;
    }

    public void setOnCardCityID(int onCardCityID) {
        OnCardCityID = onCardCityID;
    }

    public String getOnCardProvName() {
        return OnCardProvName;
    }

    public void setOnCardProvName(String onCardProvName) {
        OnCardProvName = onCardProvName;
    }

    public String getOnCardCityName() {
        return OnCardCityName;
    }

    public void setOnCardCityName(String onCardCityName) {
        OnCardCityName = onCardCityName;
    }

    public int getInsuranceProvID() {
        return InsuranceProvID;
    }

    public void setInsuranceProvID(int insuranceProvID) {
        InsuranceProvID = insuranceProvID;
    }

    public int getInsuranceCityID() {
        return InsuranceCityID;
    }

    public void setInsuranceCityID(int insuranceCityID) {
        InsuranceCityID = insuranceCityID;
    }

    public String getInsuranceProvName() {
        return InsuranceProvName;
    }

    public void setInsuranceProvName(String insuranceProvName) {
        InsuranceProvName = insuranceProvName;
    }

    public String getInsuranceCityName() {
        return InsuranceCityName;
    }

    public void setInsuranceCityName(String insuranceCityName) {
        InsuranceCityName = insuranceCityName;
    }

    public int getSpareKey() {
        return SpareKey;
    }

    public void setSpareKey(int spareKey) {
        SpareKey = spareKey;
    }

    public int getEffluentStd() {
        return EffluentStd;
    }

    public void setEffluentStd(int effluentStd) {
        EffluentStd = effluentStd;
    }

    public String getEffluentStdRef() {
        return EffluentStdRef;
    }

    public void setEffluentStdRef(String effluentStdRef) {
        EffluentStdRef = effluentStdRef;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public boolean isInspectionIsConfirmLocal() {
        return InspectionIsConfirmLocal;
    }

    public void setInspectionIsConfirmLocal(boolean inspectionIsConfirmLocal) {
        InspectionIsConfirmLocal = inspectionIsConfirmLocal;
    }

    public List<String> getConfigureValueList() {
        return ConfigureValueList;
    }

    public void setConfigureValueList(List<String> configureValueList) {
        ConfigureValueList = configureValueList;
    }

    public LinkedHashMap<String, String> getConfigureSelectedMap() {
        return ConfigureSelectedMap;
    }

    public void setConfigureSelectedMap(LinkedHashMap<String, String> configureSelectedMap) {
        ConfigureSelectedMap = configureSelectedMap;
    }

    public List<LinkedHashMap<String,String>> getTaskTimeRegionList() {
        return TaskTimeRegionList;
    }

    public void setTaskTimeRegionList(List<LinkedHashMap<String,String>> taskTimeRegionList) {
        TaskTimeRegionList = taskTimeRegionList;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getCarKeyDes() {
        return CarKeyDes;
    }

    public void setCarKeyDes(String carKeyDes) {
        CarKeyDes = carKeyDes;
    }

    public String getPlateCount() {
        return PlateCount;
    }

    public void setPlateCount(String plateCount) {
        PlateCount = plateCount;
    }

    public int getRepairRecord() {
        return RepairRecord;
    }

    public void setRepairRecord(int repairRecord) {
        RepairRecord = repairRecord;
    }

    public int getAccidentRecord() {
        return AccidentRecord;
    }

    public void setAccidentRecord(int accidentRecord) {
        AccidentRecord = accidentRecord;
    }

    public int getAccidentRepair() {
        return AccidentRepair;
    }

    public void setAccidentRepair(int accidentRepair) {
        AccidentRepair = accidentRepair;
    }

    public int getSettleRecord() {
        return SettleRecord;
    }

    public void setSettleRecord(int settleRecord) {
        SettleRecord = settleRecord;
    }

    public int getIsRecall() {
        return IsRecall;
    }

    public void setIsRecall(int isRecall) {
        IsRecall = isRecall;
    }

    public int getNextRepairDays() {
        return NextRepairDays;
    }

    public void setNextRepairDays(int nextRepairDays) {
        NextRepairDays = nextRepairDays;
    }

    public int getNextRepairMileage() {
        return NextRepairMileage;
    }

    public void setNextRepairMileage(int nextRepairMileage) {
        NextRepairMileage = nextRepairMileage;
    }

    public int getPetrolAlarm() {
        return PetrolAlarm;
    }

    public void setPetrolAlarm(int petrolAlarm) {
        PetrolAlarm = petrolAlarm;
    }

    public int getStdInspection() {
        return StdInspection;
    }

    public void setStdInspection(int stdInspection) {
        StdInspection = stdInspection;
    }

    public int getCoyInsurance() {
        return CoyInsurance;
    }

    public void setCoyInsurance(int coyInsurance) {
        CoyInsurance = coyInsurance;
    }

    public int getUserManual() {
        return UserManual;
    }

    public void setUserManual(int userManual) {
        UserManual = userManual;
    }

    public int getBordbuch() {
        return Bordbuch;
    }

    public void setBordbuch(int bordbuch) {
        Bordbuch = bordbuch;
    }

    public int getThreeGuarantees() {
        return ThreeGuarantees;
    }

    public void setThreeGuarantees(int threeGuarantees) {
        ThreeGuarantees = threeGuarantees;
    }

    public int getGuaranteeHandbook() {
        return GuaranteeHandbook;
    }

    public void setGuaranteeHandbook(int guaranteeHandbook) {
        GuaranteeHandbook = guaranteeHandbook;
    }

    public int getRescueCall() {
        return RescueCall;
    }

    public void setRescueCall(int rescueCall) {
        RescueCall = rescueCall;
    }

    public int getEnvironmental() {
        return Environmental;
    }

    public void setEnvironmental(int environmental) {
        Environmental = environmental;
    }

    public int getContactHandbook() {
        return ContactHandbook;
    }

    public void setContactHandbook(int contactHandbook) {
        ContactHandbook = contactHandbook;
    }

    //宝马其他信息
    private List<SpCheckItemGroupListBean> SpCheckItemGroupList = new ArrayList<>();

    public List<SpCheckItemGroupListBean> getSpCheckItemGroupList() {
        return SpCheckItemGroupList;
    }

    public void setSpCheckItemGroupList(List<SpCheckItemGroupListBean> SpCheckItemGroupList) {
        this.SpCheckItemGroupList = SpCheckItemGroupList;
    }

    public static class SpCheckItemGroupListBean {
        /**
         * GroupId : 1
         * SpCheckItemList : [{"NameEn":"LTSC_TyreLeftAnterior","ValueStr":"1","IsQualified":"0"},{"NameEn":"LTSC_TyreLeftAfter","ValueStr":"0","IsQualified":"1"}]
         */

        private int GroupId;
        private List<SpCheckItemListBean> SpCheckItemList = new ArrayList<>();

        public int getGroupId() {
            return GroupId;
        }

        public void setGroupId(int GroupId) {
            this.GroupId = GroupId;
        }

        public List<SpCheckItemListBean> getSpCheckItemList() {
            return SpCheckItemList;
        }

        public void setSpCheckItemList(List<SpCheckItemListBean> SpCheckItemList) {
            this.SpCheckItemList = SpCheckItemList;
        }

        public static class SpCheckItemListBean {
            /**
             * NameEn : LTSC_TyreLeftAnterior
             * ValueStr : 1
             * IsQualified : 0
             */

            private String NameEn;
            private String ValueStr;
            private String IsQualified;

            public String getNameEn() {
                return NameEn;
            }

            public void setNameEn(String NameEn) {
                this.NameEn = NameEn;
            }

            public String getValueStr() {
                return ValueStr;
            }

            public void setValueStr(String ValueStr) {
                this.ValueStr = ValueStr;
            }

            public String getIsQualified() {
                return IsQualified;
            }

            public void setIsQualified(String IsQualified) {
                this.IsQualified = IsQualified;
            }
        }
    }

    //补充说明-维修记录
    private List<LinkedHashMap<String,String>> SpOutRepairRecordList = new ArrayList<>();

    public List<LinkedHashMap<String, String>> getSpOutRepairRecordList() {
        return SpOutRepairRecordList;
    }

    public void setSpOutRepairRecordList(List<LinkedHashMap<String, String>> spOutRepairRecordList) {
        SpOutRepairRecordList = spOutRepairRecordList;
    }

}
