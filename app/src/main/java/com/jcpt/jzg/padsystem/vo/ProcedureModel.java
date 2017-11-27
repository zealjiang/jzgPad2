package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 手续信息数据对象
 * Created by zealjiang on 2016/12/4 15:33.
 * Email: zealjiang@126.com
 */

public class ProcedureModel implements Serializable{

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
    private int CarColorActual;
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
    private boolean InspectionSelected;
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
    private int CarLicenseEx;
    private String nameplate;//车辆铭牌 本地用
    private String brandType;//品牌和型号 之间用逗号分隔 本地用（服务器通过vin查询返回）

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

    //宝马新增  （ 是/合格/达标  这种值是1，否/不合格/不达标值是0）
    private boolean isCarPlateNumNone;//车牌未见选中 true，反之为false    本地用
    private boolean isRegisterDateNone;//登记日期未见选中 true，反之为false   本地用
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
    private int RescueCall;//授权经销商联系手册  是：1，否：0
    private int Environmental;//救援电话  是：1，否：0
    private int ContactHandbook;//环保凭证  是：1，否：0
    private int Mileage;//里程表显示
    private String OtherColor;//车身颜色  宝马

    private int EffluentStd;//排放标准  国二及以下：1，国三：2，国四：3，国五：4，无法判断：5
    private String EffluentStdRef;//排放标准参考值


    //手续信息图片
    private List<TaskDetailModel.ProcedurePicListBean> ProcedurePicList;
    //是否有合格证数据
    private boolean isHasCertificateData = false;// 本地用

    private List<String> DeletePicId = new ArrayList<>();//网络图片删除的图片ID数组
    private List<String> submitdelPicId = new ArrayList<>();//提交时用的删除的网络图片ID

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getDrivingLicenseProperty() {
        return DrivingLicenseProperty;
    }

    public void setDrivingLicenseProperty(int drivingLicenseProperty) {
        DrivingLicenseProperty = drivingLicenseProperty;
    }

    public String getDrivingLicenseCheckEx() {
        return DrivingLicenseCheckEx;
    }

    public void setDrivingLicenseCheckEx(String drivingLicenseCheckEx) {
        DrivingLicenseCheckEx = drivingLicenseCheckEx;
    }

    public int getRegistLicenseProperty() {
        return RegistLicenseProperty;
    }

    public void setRegistLicenseProperty(int registLicenseProperty) {
        RegistLicenseProperty = registLicenseProperty;
    }

    public int getCardType() {
        return CardType;
    }

    public void setCardType(int cardType) {
        CardType = cardType;
    }

    public String getCardNum() {
        return CardNum;
    }

    public void setCardNum(String cardNum) {
        CardNum = cardNum;
    }

    public int getIsCardSame() {
        return IsCardSame;
    }

    public void setIsCardSame(int isCardSame) {
        IsCardSame = isCardSame;
    }

    public int getCarLicenseHave() {
        return CarLicenseHave;
    }

    public void setCarLicenseHave(int carLicenseHave) {
        CarLicenseHave = carLicenseHave;
    }

    public int getSeating() {
        return Seating;
    }

    public void setSeating(int seating) {
        Seating = seating;
    }

    public int getCarGetWay() {
        return CarGetWay;
    }

    public void setCarGetWay(int carGetWay) {
        CarGetWay = carGetWay;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }

    public String getCarColorDes() {
        return CarColorDes;
    }

    public void setCarColorDes(String carColorDes) {
        CarColorDes = carColorDes;
    }

    public int getCarColorActual() {
        return CarColorActual;
    }

    public void setCarColorActual(int carColorActual) {
        CarColorActual = carColorActual;
    }

    public int getTurboChargingHave() {
        return TurboChargingHave;
    }

    public void setTurboChargingHave(int turboChargingHave) {
        TurboChargingHave = turboChargingHave;
    }

    public String getTyre() {
        return Tyre;
    }

    public void setTyre(String tyre) {
        Tyre = tyre;
    }

    public String getTyreProperty() {
        return TyreProperty;
    }

    public void setTyreProperty(String tyreProperty) {
        TyreProperty = tyreProperty;
    }

    public int getService() {
        return Service;
    }

    public void setService(int service) {
        Service = service;
    }

    public String getLastTransferDate() {
        return LastTransferDate;
    }

    public void setLastTransferDate(String lastTransferDate) {
        LastTransferDate = lastTransferDate;
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

    public void setNowUseOwner(int nowUseOwner) {
        NowUseOwner = nowUseOwner;
    }

    public int getTrafficInsuranceHave() {
        return TrafficInsuranceHave;
    }

    public void setTrafficInsuranceHave(int trafficInsuranceHave) {
        TrafficInsuranceHave = trafficInsuranceHave;
    }

    public String getInsurance() {
        return Insurance;
    }

    public void setInsurance(String insurance) {
        Insurance = insurance;
    }

    public int getCarInvoiceHave() {
        return CarInvoiceHave;
    }

    public void setCarInvoiceHave(int carInvoiceHave) {
        CarInvoiceHave = carInvoiceHave;
    }

    public long getCarInvoiceMoney() {
        return CarInvoiceMoney;
    }

    public void setCarInvoiceMoney(long carInvoiceMoney) {
        CarInvoiceMoney = carInvoiceMoney;
    }

    public String getCarInvoiceDate() {
        return CarInvoiceDate;
    }

    public void setCarInvoiceDate(String carInvoiceDate) {
        CarInvoiceDate = carInvoiceDate;
    }

    public String getCarInvoiceOther() {
        return CarInvoiceOther;
    }

    public void setCarInvoiceOther(String carInvoiceOther) {
        CarInvoiceOther = carInvoiceOther;
    }

    public String getCertificateEx() {
        return CertificateEx;
    }

    public void setCertificateEx(String certificateEx) {
        CertificateEx = certificateEx;
    }

    public String getCertificateExDes() {
        return CertificateExDes;
    }

    public void setCertificateExDes(String certificateExDes) {
        CertificateExDes = certificateExDes;
    }

    public String getInspection() {
        return Inspection;
    }

    public void setInspection(String inspection) {
        Inspection = inspection;
    }

    public String getCarOwner() {
        return CarOwner;
    }

    public void setCarOwner(String likeMan) {
        CarOwner = likeMan;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String recordDate) {
        RecordDate = recordDate;
    }

    public int getProvID() {
        return ProvID;
    }

    public void setProvID(int provID) {
        ProvID = provID;
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

    public void setCityID(int cityID) {
        CityID = cityID;
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

    public void setCarLicense(String carLicense) {
        CarLicense = carLicense;
    }

    public String getRecordBrand() {
        return RecordBrand;
    }

    public void setRecordBrand(String recordBrand) {
        RecordBrand = recordBrand;
    }

    public String getVin() {
        return Vin;
    }

    public void setVin(String vin) {
        Vin = vin;
    }

    public String getEngineNum() {
        return EngineNum;
    }

    public void setEngineNum(String engineNum) {
        EngineNum = engineNum;
    }

    public int getFuelType() {
        return FuelType;
    }

    public void setFuelType(int fuelType) {
        FuelType = fuelType;
    }

    public int getIsImport() {
        return IsImport;
    }

    public void setIsImport(int isImport) {
        IsImport = isImport;
    }

    public String getExhaust() {
        return Exhaust;
    }

    public void setExhaust(String exhaust) {
        Exhaust = exhaust;
    }

    public String getProductionTime() {
        return ProductionTime;
    }

    public void setProductionTime(String productionTime) {
        ProductionTime = productionTime;
    }

    public int getTransferCount() {
        return TransferCount;
    }

    public void setTransferCount(int transferCount) {
        TransferCount = transferCount;
    }

    public String getAssessmentDes() {
        return AssessmentDes;
    }

    public void setAssessmentDes(String assessmentDes) {
        AssessmentDes = assessmentDes;
    }

    public int getMakeID() {
        return MakeID;
    }

    public void setMakeID(int makeID) {
        MakeID = makeID;
    }

    public int getModelID() {
        return ModelID;
    }

    public void setModelID(int modelID) {
        ModelID = modelID;
    }

    public int getStyleID() {
        return StyleID;
    }

    public void setStyleID(int styleID) {
        StyleID = styleID;
    }

    public boolean isInspectionSelected() {
        return InspectionSelected;
    }

    public void setInspectionSelected(boolean inspectionSelected) {
        InspectionSelected = inspectionSelected;
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

    public List<TaskDetailModel.ProcedurePicListBean> getProcedurePicList() {
        return ProcedurePicList;
    }

    public void setProcedurePicList(List<TaskDetailModel.ProcedurePicListBean> procedurePicList) {
        ProcedurePicList = procedurePicList;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public boolean isHasCertificateData() {
        return isHasCertificateData;
    }

    public void setHasCertificateData(boolean hasCertificateData) {
        isHasCertificateData = hasCertificateData;
    }

    public List<String> getDeletePicId() {
        return DeletePicId;
    }

    public void setDeletePicId(List<String> deletePicId) {
        DeletePicId = deletePicId;
    }

    public List<String> getSubmitdelPicId() {
        return submitdelPicId;
    }

    public void setSubmitdelPicId(List<String> submitdelPicId) {
        this.submitdelPicId = submitdelPicId;
    }

    public boolean isCarPlateNumNone() {
        return isCarPlateNumNone;
    }

    public void setCarPlateNumNone(boolean carPlateNumNone) {
        isCarPlateNumNone = carPlateNumNone;
    }

    public boolean isRegisterDateNone() {
        return isRegisterDateNone;
    }

    public void setRegisterDateNone(boolean registerDateNone) {
        isRegisterDateNone = registerDateNone;
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

    public int getMileage() {
        return Mileage;
    }

    public void setMileage(int mileage) {
        Mileage = mileage;
    }

    public String getOtherColor() {
        return OtherColor;
    }

    public void setOtherColor(String otherColor) {
        OtherColor = otherColor;
    }
}
