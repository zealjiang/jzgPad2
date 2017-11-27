package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 检测数据详情接口
 * Created by zealjiang on 2016/11/23 10:08.
 * Email: zealjiang@126.com
 */

public class TaskDetailModel implements Serializable{

    /**
     * Id : 618
     * OrderNo : JZG1708201601180006
     * SourceID : 1
     * CityID : 1708
     * ProvID : 17
     * Des : 无
     * LikeMan : 张晶
     * LikeTel : 13842807435
     * LikeAddr : 大连市甘井子区
     * Vin : LUXG91S00CB023482
     * CarLicense : 辽B377AZ
     * RecordBrand : 纳智捷牌DYM6481AAA
     * EngineNum : CB0023226
     * RecordDate : 2012-08-14T00:00:00
     * RecordAddrID : 1708
     * MakeID : 155
     * ModelID : 3275
     * StyleID : 102060
     * Color : 1
     * Mileage : 40989
     * Service : 2
     * AssessmentPrace : 0
     * AssessmentDes : 前后杠喷漆，轮胎更换。无重大事故，无水淹，无火烧，市场保有量小，保值差。
     * UserID : 24
     * Status : 6
     * CreateTime : 2016-01-18T09:19:01
     * UpdateTime : 2016-01-20T11:20:30
     * StartTime : 2016-01-18T09:19:01
     * EndTime : 2016-02-02T09:19:01
     * Exhaust : 2.2
     * Seating : 5
     * CarType1 : 3
     * DrivingMode : 1
     * Transmission : 1
     * FuelType : 1
     * ProductionTime : 2012-07-11T00:00:00
     * Certificates : 1
     * ManufacturerPrice : 224000
     * SetGroupID : 1
     * ConfigureInfo :
     * ConfirmCertificates :
     * TaskType : 3
     * TaskBackNum : 0
     * TaskBackReason :
     * AppraiseBackNum : 0
     * AppraiseBackReason :
     * TransferCount : 0
     * PublishArea : 0
     * Insurance : 0001-01-01T00:00:00
     * Inspection : 0001-01-01T00:00:00
     * CarDes :
     * CreateUserId : 11
     * YXOrderNo :
     * LowestPrice : 0
     * AuctionEndTime : 0001-01-01T00:00:00
     * SaleType : 0
     * C2BBPrice : 0
     * VideoPath :
     * RegisterCityID : 0
     * RegisterProvID : 0
     * BusinessPrice : 0
     * CompositeProPrice : 0
     * SalePrice : 128000
     * SuggestSellPrice : 0
     * PerfDriveType :
     * TransmissionType :
     * EngineExhaust :
     * Fuel :
     * TaskOwnerName :
     * Tasktel :
     * CarType : 3
     * ProductType : 1
     * JZGAssessmentPrice : 0
     * JZGSalePrice : 0
     * ProgrammeId : 5
     * IsComplete : 0
     * DrivingLicenseProperty : 0
     * DrivingLicenseCheckEx :
     * RegistLicenseProperty : 0
     * CardType : 0
     * CardNum :
     * IsCardSame : 0
     * CarLicenseHave : 0
     * CarGetWay : 0
     * CarColorDes :
     * CarColorActual : 0
     * TurboChargingHave : 0
     * Tyre :
     * TyreProperty :
     * LastTransferDate :
     * NowUseOwner : 0
     * TrafficInsuranceHave : 0
     * CarInvoiceHave : 0
     * CarInvoiceMoney : 0
     * CarInvoiceDate :
     * CarInvoiceOther :
     * CertificateEx :
     * CertificateExDes :
     * MileageSame : 0
     * NameplateProperty :
     * VinProperty :
     * AppearanceCheck : 0
     * AccidentCheck : 0
     * EngineCheck : 0
     * DecorateCheck : 0
     * ChassisCheck : 0
     * ElectricalCheck : 0
     * BurningMark : 0
     * WaterMark : 0
     * MarketOwnership : 0
     * MarketAcceptance : 0
     * MarketHedgeRatio : 0
     * IsImport : 0
     */
    private BasicBean Basic;
    /**
     * CheckPositionID :
     * CheckPositionName :
     * PartsPositionList : [{"TaskID":"","PartsPositionRelationID":"","PartsPositionRelationName":"","IsImportant":"","DefectTypeList":[{"TaskID":"","PartsPositionRelationID":"","DefectResultID":"","DefectTypeID":"","DefectValueID":"","DefectValueName":"","ImgList":[{"TaskID":"","PicID":"","DefectValueID":"","DefectResultId":"","UriID":"","PicPath":""}]}]}]
     * PicList : [{"PicID":"","PicPath":"","PicName":"","CheckPositionID":""}]
     */

    private List<CheckPositionListBean> CheckPositionList;
    /**
     * PicID :
     * PicPath :
     * PicName :
     * PicType :
     */

    private List<ProcedurePicListBean> ProcedurePicList;
    private List<TaskCarPicAdditionalListBean> TaskCarPicAdditionalList;

    /**
     * PicID :
     * PicPath :
     * PicName :
     * CheckPositionID :
     */
    private List<PicListBean> PicList;

    public List<PicListBean> getPicList() {
        return PicList;
    }

    private List<String> ConfigureList;

    public void setPicList(List<PicListBean> PicList) {
        this.PicList = PicList;
    }

    public static class PicListBean implements Serializable{
        private String PicID;
        private String PicPath;
        private String PicName;
        private String CheckPositionID;

        public String getPicID() {
            return PicID;
        }

        public void setPicID(String PicID) {
            this.PicID = PicID;
        }

        public String getPicPath() {
            return PicPath;
        }

        public void setPicPath(String PicPath) {
            this.PicPath = PicPath;
        }

        public String getPicName() {
            return PicName;
        }

        public void setPicName(String PicName) {
            this.PicName = PicName;
        }

        public String getCheckPositionID() {
            return CheckPositionID;
        }

        public void setCheckPositionID(String CheckPositionID) {
            this.CheckPositionID = CheckPositionID;
        }
    }

    public BasicBean getBasic() {
        return Basic;
    }

    public void setBasic(BasicBean Basic) {
        this.Basic = Basic;
    }

    public List<CheckPositionListBean> getCheckPositionList() {
        return CheckPositionList;
    }

    public void setCheckPositionList(List<CheckPositionListBean> CheckPositionList) {
        this.CheckPositionList = CheckPositionList;
    }

    public List<ProcedurePicListBean> getProcedurePicList() {
        return ProcedurePicList;
    }

    public void setProcedurePicList(List<ProcedurePicListBean> ProcedurePicList) {
        this.ProcedurePicList = ProcedurePicList;
    }

    public List<TaskCarPicAdditionalListBean> getTaskCarPicAdditionalList() {
        return TaskCarPicAdditionalList;
    }

    public void setTaskCarPicAdditionalList(List<TaskCarPicAdditionalListBean> TaskCarPicAdditionalList) {
        this.TaskCarPicAdditionalList = TaskCarPicAdditionalList;
    }

    public List<String> getConfigureList() {
        return ConfigureList;
    }

    public void setConfigureList(List<String> configureList) {
        ConfigureList = configureList;
    }

    public static class BasicBean implements Serializable{
        private int Id;
        private String OrderNo;
        private int SourceID;
        private int CityID;
        private int ProvID;
        private int RegisterprovID;
        private String Des;
        private String CarOwner;
        private String LikeTel;
        private String LikeAddr;
        private String Vin;
        private String CarLicense;
        private String RecordBrand;
        private String EngineNum;
        private String RecordDate;
        private int RecordAddrID;
        private int MakeID;
        private int ModelID;
        private int StyleID;
        private int Color;
        private int Mileage;
        private int Service;
        private float AssessmentPrace;
        private String AssessmentDes;
        private int UserID;
        private int Status;
        private String CreateTime;
        private String UpdateTime;
        private String StartTime;
        private String EndTime;
        private String Exhaust;
        private int Seating;
        private int CarType1;
        private int DrivingMode;
        private int Transmission;
        private int FuelType;
        private String ProductionTime;
        private int Certificates;
        private double ManufacturerPrice;
        private int SetGroupID;
        private String ConfigureInfo;
        private String ConfirmCertificates;
        private int TaskType;
        private int TaskBackNum;
        private String TaskBackReason;
        private int AppraiseBackNum;
        private String AppraiseBackReason;
        private int TransferCount;
        private int PublishArea;
        private String Insurance;
        private String Inspection;
        private String CarDes;
        private int CreateUserId;
        private String YXOrderNo;
        private float LowestPrice;
        private String AuctionEndTime;
        private int SaleType;
        private float C2BBPrice;
        private String VideoPath;
        private int RegisterCityID;
        private int RegisterProvID;
        private int BusinessPrice;
        private int CompositeProPrice;
        private float SalePrice;
        private int SuggestSellPrice;
        private String PerfDriveType;
        private String TransmissionType;
        private String EngineExhaust;
        private String Fuel;
        private String TaskOwnerName;
        private String Tasktel;
        private int CarType;
        private int ProductType;
        private int JZGAssessmentPrice;
        private int JZGSalePrice;
        private int ProgrammeId;
        private int IsComplete;
        private int DrivingLicenseProperty;
        private String DrivingLicenseCheckEx;
        private int RegistLicenseProperty;
        private int CardType;
        private String CardNum;
        private int IsCardSame;
        private int CarLicenseHave;
        private int CarGetWay;
        private String CarColorDes;
        private int CarColorActual;
        private int TurboChargingHave;
        private String Tyre;
        private String TyreProperty;
        private String LastTransferDate;
        private int OldUseOwner;//曾使用方
        private int NowUseOwner;
        private int TrafficInsuranceHave;
        private int CarInvoiceHave;
        private int CarInvoiceMoney;
        private String CarInvoiceDate;
        private String CarInvoiceOther;
        private String CertificateEx;
        private String CertificateExDes;
        private int MileageSame;
        private int GaugeLamp;
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
        private int IsImport;
        private String DefectValue;
        private String ProvinceName;
        private String CityName;
        private String CarFullName;
        private String OtherColor;  //核对信息车身颜色里 其他颜色的描述  -> 李波 on 2016/12/16.
        private int CarLicenseEx;
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

        public String getOtherColor() {
            return OtherColor;
        }

        public void setOtherColor(String otherColor) {
            OtherColor = otherColor;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public int getSourceID() {
            return SourceID;
        }

        public void setSourceID(int SourceID) {
            this.SourceID = SourceID;
        }

        public int getCityID() {
            return CityID;
        }

        public void setCityID(int CityID) {
            this.CityID = CityID;
        }

        public int getProvID() {
            return ProvID;
        }

        public void setProvID(int ProvID) {
            this.ProvID = ProvID;
        }

        public String getDes() {
            return Des;
        }

        public void setDes(String Des) {
            this.Des = Des;
        }

        public String getCarOwner() {
            return CarOwner;
        }

        public void setCarOwner(String LikeMan) {
            this.CarOwner = LikeMan;
        }

        public String getLikeTel() {
            return LikeTel;
        }

        public void setLikeTel(String LikeTel) {
            this.LikeTel = LikeTel;
        }

        public String getLikeAddr() {
            return LikeAddr;
        }

        public void setLikeAddr(String LikeAddr) {
            this.LikeAddr = LikeAddr;
        }

        public String getVin() {
            return Vin;
        }

        public void setVin(String Vin) {
            this.Vin = Vin;
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

        public String getEngineNum() {
            return EngineNum;
        }

        public void setEngineNum(String EngineNum) {
            this.EngineNum = EngineNum;
        }

        public String getRecordDate() {
            return RecordDate;
        }

        public void setRecordDate(String RecordDate) {
            this.RecordDate = RecordDate;
        }

        public int getRecordAddrID() {
            return RecordAddrID;
        }

        public void setRecordAddrID(int RecordAddrID) {
            this.RecordAddrID = RecordAddrID;
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

        public int getColor() {
            return Color;
        }

        public void setColor(int Color) {
            this.Color = Color;
        }

        public int getMileage() {
            return Mileage;
        }

        public void setMileage(int Mileage) {
            this.Mileage = Mileage;
        }

        public int getService() {
            return Service;
        }

        public void setService(int Service) {
            this.Service = Service;
        }

        public float getAssessmentPrace() {
            return AssessmentPrace;
        }

        public void setAssessmentPrace(float AssessmentPrace) {
            this.AssessmentPrace = AssessmentPrace;
        }

        public String getAssessmentDes() {
            return AssessmentDes;
        }

        public void setAssessmentDes(String AssessmentDes) {
            this.AssessmentDes = AssessmentDes;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getExhaust() {
            return Exhaust;
        }

        public void setExhaust(String Exhaust) {
            this.Exhaust = Exhaust;
        }

        public int getSeating() {
            return Seating;
        }

        public void setSeating(int Seating) {
            this.Seating = Seating;
        }

        public int getCarType1() {
            return CarType1;
        }

        public void setCarType1(int CarType1) {
            this.CarType1 = CarType1;
        }

        public int getDrivingMode() {
            return DrivingMode;
        }

        public void setDrivingMode(int DrivingMode) {
            this.DrivingMode = DrivingMode;
        }

        public int getTransmission() {
            return Transmission;
        }

        public void setTransmission(int Transmission) {
            this.Transmission = Transmission;
        }

        public int getFuelType() {
            return FuelType;
        }

        public void setFuelType(int FuelType) {
            this.FuelType = FuelType;
        }

        public String getProductionTime() {
            return ProductionTime;
        }

        public void setProductionTime(String ProductionTime) {
            this.ProductionTime = ProductionTime;
        }

        public int getCertificates() {
            return Certificates;
        }

        public void setCertificates(int Certificates) {
            this.Certificates = Certificates;
        }

        public double getManufacturerPrice() {
            return ManufacturerPrice;
        }

        public void setManufacturerPrice(double ManufacturerPrice) {
            this.ManufacturerPrice = ManufacturerPrice;
        }

        public int getSetGroupID() {
            return SetGroupID;
        }

        public void setSetGroupID(int SetGroupID) {
            this.SetGroupID = SetGroupID;
        }

        public String getConfigureInfo() {
            return ConfigureInfo;
        }

        public void setConfigureInfo(String ConfigureInfo) {
            this.ConfigureInfo = ConfigureInfo;
        }

        public String getConfirmCertificates() {
            return ConfirmCertificates;
        }

        public void setConfirmCertificates(String ConfirmCertificates) {
            this.ConfirmCertificates = ConfirmCertificates;
        }

        public int getTaskType() {
            return TaskType;
        }

        public void setTaskType(int TaskType) {
            this.TaskType = TaskType;
        }

        public int getTaskBackNum() {
            return TaskBackNum;
        }

        public void setTaskBackNum(int TaskBackNum) {
            this.TaskBackNum = TaskBackNum;
        }

        public String getTaskBackReason() {
            return TaskBackReason;
        }

        public void setTaskBackReason(String TaskBackReason) {
            this.TaskBackReason = TaskBackReason;
        }

        public int getAppraiseBackNum() {
            return AppraiseBackNum;
        }

        public void setAppraiseBackNum(int AppraiseBackNum) {
            this.AppraiseBackNum = AppraiseBackNum;
        }

        public String getAppraiseBackReason() {
            return AppraiseBackReason;
        }

        public void setAppraiseBackReason(String AppraiseBackReason) {
            this.AppraiseBackReason = AppraiseBackReason;
        }

        public int getTransferCount() {
            return TransferCount;
        }

        public void setTransferCount(int TransferCount) {
            this.TransferCount = TransferCount;
        }

        public int getPublishArea() {
            return PublishArea;
        }

        public void setPublishArea(int PublishArea) {
            this.PublishArea = PublishArea;
        }

        public String getInsurance() {
            return Insurance;
        }

        public void setInsurance(String Insurance) {
            this.Insurance = Insurance;
        }

        public String getInspection() {
            return Inspection;
        }

        public void setInspection(String Inspection) {
            this.Inspection = Inspection;
        }

        public String getCarDes() {
            return CarDes;
        }

        public void setCarDes(String CarDes) {
            this.CarDes = CarDes;
        }

        public int getCreateUserId() {
            return CreateUserId;
        }

        public void setCreateUserId(int CreateUserId) {
            this.CreateUserId = CreateUserId;
        }

        public String getYXOrderNo() {
            return YXOrderNo;
        }

        public void setYXOrderNo(String YXOrderNo) {
            this.YXOrderNo = YXOrderNo;
        }

        public float getLowestPrice() {
            return LowestPrice;
        }

        public void setLowestPrice(float LowestPrice) {
            this.LowestPrice = LowestPrice;
        }

        public String getAuctionEndTime() {
            return AuctionEndTime;
        }

        public void setAuctionEndTime(String AuctionEndTime) {
            this.AuctionEndTime = AuctionEndTime;
        }

        public int getSaleType() {
            return SaleType;
        }

        public void setSaleType(int SaleType) {
            this.SaleType = SaleType;
        }

        public float getC2BBPrice() {
            return C2BBPrice;
        }

        public void setC2BBPrice(float C2BBPrice) {
            this.C2BBPrice = C2BBPrice;
        }

        public String getVideoPath() {
            return VideoPath;
        }

        public void setVideoPath(String VideoPath) {
            this.VideoPath = VideoPath;
        }

        public int getRegisterCityID() {
            return RegisterCityID;
        }

        public void setRegisterCityID(int RegisterCityID) {
            this.RegisterCityID = RegisterCityID;
        }

        public int getRegisterProvID() {
            return RegisterProvID;
        }

        public void setRegisterProvID(int RegisterProvID) {
            this.RegisterProvID = RegisterProvID;
        }

        public int getBusinessPrice() {
            return BusinessPrice;
        }

        public void setBusinessPrice(int BusinessPrice) {
            this.BusinessPrice = BusinessPrice;
        }

        public int getCompositeProPrice() {
            return CompositeProPrice;
        }

        public void setCompositeProPrice(int CompositeProPrice) {
            this.CompositeProPrice = CompositeProPrice;
        }

        public float getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(float SalePrice) {
            this.SalePrice = SalePrice;
        }

        public int getSuggestSellPrice() {
            return SuggestSellPrice;
        }

        public void setSuggestSellPrice(int SuggestSellPrice) {
            this.SuggestSellPrice = SuggestSellPrice;
        }

        public String getPerfDriveType() {
            return PerfDriveType;
        }

        public void setPerfDriveType(String PerfDriveType) {
            this.PerfDriveType = PerfDriveType;
        }

        public String getTransmissionType() {
            return TransmissionType;
        }

        public void setTransmissionType(String TransmissionType) {
            this.TransmissionType = TransmissionType;
        }

        public String getEngineExhaust() {
            return EngineExhaust;
        }

        public void setEngineExhaust(String EngineExhaust) {
            this.EngineExhaust = EngineExhaust;
        }

        public String getFuel() {
            return Fuel;
        }

        public void setFuel(String Fuel) {
            this.Fuel = Fuel;
        }

        public String getTaskOwnerName() {
            return TaskOwnerName;
        }

        public void setTaskOwnerName(String TaskOwnerName) {
            this.TaskOwnerName = TaskOwnerName;
        }

        public String getTasktel() {
            return Tasktel;
        }

        public void setTasktel(String Tasktel) {
            this.Tasktel = Tasktel;
        }

        public int getCarType() {
            return CarType;
        }

        public void setCarType(int CarType) {
            this.CarType = CarType;
        }

        public int getProductType() {
            return ProductType;
        }

        public void setProductType(int ProductType) {
            this.ProductType = ProductType;
        }

        public int getJZGAssessmentPrice() {
            return JZGAssessmentPrice;
        }

        public void setJZGAssessmentPrice(int JZGAssessmentPrice) {
            this.JZGAssessmentPrice = JZGAssessmentPrice;
        }

        public int getJZGSalePrice() {
            return JZGSalePrice;
        }

        public void setJZGSalePrice(int JZGSalePrice) {
            this.JZGSalePrice = JZGSalePrice;
        }

        public int getProgrammeId() {
            return ProgrammeId;
        }

        public void setProgrammeId(int ProgrammeId) {
            this.ProgrammeId = ProgrammeId;
        }

        public int getIsComplete() {
            return IsComplete;
        }

        public void setIsComplete(int IsComplete) {
            this.IsComplete = IsComplete;
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

        public int getCarGetWay() {
            return CarGetWay;
        }

        public void setCarGetWay(int CarGetWay) {
            this.CarGetWay = CarGetWay;
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

        public int getCarInvoiceHave() {
            return CarInvoiceHave;
        }

        public void setCarInvoiceHave(int CarInvoiceHave) {
            this.CarInvoiceHave = CarInvoiceHave;
        }

        public int getCarInvoiceMoney() {
            return CarInvoiceMoney;
        }

        public void setCarInvoiceMoney(int CarInvoiceMoney) {
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
            GaugeLamp = gaugeLamp;
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

        public int getIsImport() {
            return IsImport;
        }

        public void setIsImport(int IsImport) {
            this.IsImport = IsImport;
        }

        public String getDefectValue() {
            return DefectValue;
        }

        public void setDefectValue(String defectValue) {
            DefectValue = defectValue;
        }

        public String getProvinceName() {
            return ProvinceName;
        }

        public void setProvinceName(String provinceName) {
            ProvinceName = provinceName;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String cityName) {
            CityName = cityName;
        }

        public String getCarFullName() {
            return CarFullName;
        }

        public void setCarFullName(String carFullName) {
            CarFullName = carFullName;
        }

        public int getRegisterprovID() {
            return RegisterprovID;
        }

        public void setRegisterprovID(int registerprovID) {
            RegisterprovID = registerprovID;
        }

        public int getCarLicenseEx() {
            return CarLicenseEx;
        }

        public void setCarLicenseEx(int carLicenseEx) {
            CarLicenseEx = carLicenseEx;
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

        public String getActivityId() {
            return ActivityId;
        }

        public void setActivityId(String activityId) {
            ActivityId = activityId;
        }

        public int getEffluentStd() {
            return EffluentStd;
        }

        public void setEffluentStd(int effluentStd) {
            EffluentStd = effluentStd;
        }
    }

    public static class CheckPositionListBean implements Serializable{
        private String CheckPositionID;
        private String CheckPositionName;
        /**
         * TaskID :
         * PartsPositionRelationID :
         * PartsPositionRelationName :
         * IsImportant :
         * DefectTypeList : [{"TaskID":"","PartsPositionRelationID":"","DefectResultID":"","DefectTypeID":"","DefectValueID":"","DefectValueName":"","ImgList":[{"TaskID":"","PicID":"","DefectValueID":"","DefectResultId":"","UriID":"","PicPath":""}]}]
         */

        private List<PartsPositionListBean> PartsPositionList;


        public String getCheckPositionID() {
            return CheckPositionID;
        }

        public void setCheckPositionID(String CheckPositionID) {
            this.CheckPositionID = CheckPositionID;
        }

        public String getCheckPositionName() {
            return CheckPositionName;
        }

        public void setCheckPositionName(String CheckPositionName) {
            this.CheckPositionName = CheckPositionName;
        }

        public List<PartsPositionListBean> getPartsPositionList() {
            return PartsPositionList;
        }

        public void setPartsPositionList(List<PartsPositionListBean> PartsPositionList) {
            this.PartsPositionList = PartsPositionList;
        }



        public static class PartsPositionListBean implements Serializable{
            private String TaskID;
            private String PartsPositionRelationID;  //弃用  -> 李波 on 2016/11/30.
            private String CheckID;                  //用此检测项Id  -> 李波 on 2016/11/30.
            private String PartsPositionRelationName;
            private int IsImportant;              //1-重点，0-非重点  -> 李波 on 2016/11/30.
            private List<DefectTypeListBean> DefectTypeList; //缺陷详情列表  -> 李波 on 2016/11/30.
            private int CStatus;// 0--未选中，1--正常，2--缺陷
            /**
             * TaskID :
             * PartsPositionRelationID :
             * DefectResultID :
             * DefectTypeID :
             * DefectValueID :
             * DefectValueName :
             * ImgList : [{"TaskID":"","PicID":"","DefectValueID":"","DefectResultId":"","UriID":"","PicPath":""}]
             */
            public String getCheckID() {
                return CheckID;
            }

            public void setCheckID(String checkID) {
                CheckID = checkID;
            }
            public String getTaskID() {
                return TaskID;
            }

            public void setTaskID(String TaskID) {
                this.TaskID = TaskID;
            }

            public String getPartsPositionRelationID() {
                return PartsPositionRelationID;
            }

            public void setPartsPositionRelationID(String PartsPositionRelationID) {
                this.PartsPositionRelationID = PartsPositionRelationID;
            }

            public String getPartsPositionRelationName() {
                return PartsPositionRelationName;
            }

            public void setPartsPositionRelationName(String PartsPositionRelationName) {
                this.PartsPositionRelationName = PartsPositionRelationName;
            }

            public int getIsImportant() {
                return IsImportant;
            }

            public void setIsImportant(int IsImportant) {
                this.IsImportant = IsImportant;
            }

            public List<DefectTypeListBean> getDefectTypeList() {
                return DefectTypeList;
            }

            public void setDefectTypeList(List<DefectTypeListBean> DefectTypeList) {
                this.DefectTypeList = DefectTypeList;
            }

            public int getCStatus() {
                return CStatus;
            }

            public void setCStatus(int CStatus) {
                this.CStatus = CStatus;
            }

            public static class DefectTypeListBean implements Serializable{
                private String TaskID;
                private String PartsPositionRelationID;
                private String DefectResultID;
                private String DefectTypeID;    //缺陷类型id  -> 李波 on 2016/11/30.
                private String DefectValueID;   //缺陷详情id  -> 李波 on 2016/11/30.
                private String DefectValueName; //缺陷详情名称  -> 李波 on 2016/11/30.
                private List<ImgListBean> ImgList; //缺陷详情对应的三张图片  -> 李波 on 2016/11/30.

                /**
                 * TaskID :
                 * PicID :
                 * DefectValueID :
                 * DefectResultId :
                 * UriID :
                 * PicPath :
                 */
                public String getTaskID() {
                    return TaskID;
                }

                public void setTaskID(String TaskID) {
                    this.TaskID = TaskID;
                }

                public String getPartsPositionRelationID() {
                    return PartsPositionRelationID;
                }

                public void setPartsPositionRelationID(String PartsPositionRelationID) {
                    this.PartsPositionRelationID = PartsPositionRelationID;
                }

                public String getDefectResultID() {
                    return DefectResultID;
                }

                public void setDefectResultID(String DefectResultID) {
                    this.DefectResultID = DefectResultID;
                }

                public String getDefectTypeID() {
                    return DefectTypeID;
                }

                public void setDefectTypeID(String DefectTypeID) {
                    this.DefectTypeID = DefectTypeID;
                }

                public String getDefectValueID() {
                    return DefectValueID;
                }

                public void setDefectValueID(String DefectValueID) {
                    this.DefectValueID = DefectValueID;
                }

                public String getDefectValueName() {
                    return DefectValueName;
                }

                public void setDefectValueName(String DefectValueName) {
                    this.DefectValueName = DefectValueName;
                }

                public List<ImgListBean> getImgList() {
                    return ImgList;
                }

                public void setImgList(List<ImgListBean> ImgList) {
                    this.ImgList = ImgList;
                }

                public static class ImgListBean implements Serializable{
                    private String TaskID;
                    private String PicID;
                    private String DefectValueID;
                    private String DefectResultId;
                    private String UriID;
                    private String PicPath;

                    public String getTaskID() {
                        return TaskID;
                    }

                    public void setTaskID(String TaskID) {
                        this.TaskID = TaskID;
                    }

                    public String getPicID() {
                        return PicID;
                    }

                    public void setPicID(String PicID) {
                        this.PicID = PicID;
                    }

                    public String getDefectValueID() {
                        return DefectValueID;
                    }

                    public void setDefectValueID(String DefectValueID) {
                        this.DefectValueID = DefectValueID;
                    }

                    public String getDefectResultId() {
                        return DefectResultId;
                    }

                    public void setDefectResultId(String DefectResultId) {
                        this.DefectResultId = DefectResultId;
                    }

                    public String getUriID() {
                        return UriID;
                    }

                    public void setUriID(String UriID) {
                        this.UriID = UriID;
                    }

                    public String getPicPath() {
                        return PicPath;
                    }

                    public void setPicPath(String PicPath) {
                        this.PicPath = PicPath;
                    }
                }
            }
        }


    }

    public static class ProcedurePicListBean implements Serializable{
        private String PicID;
        private String PicPath;
        private String PicName;
        private String PicType;

        public String getPicID() {
            return PicID;
        }

        public void setPicID(String PicID) {
            this.PicID = PicID;
        }

        public String getPicPath() {
            return PicPath;
        }

        public void setPicPath(String PicPath) {
            this.PicPath = PicPath;
        }

        public String getPicName() {
            return PicName;
        }

        public void setPicName(String PicName) {
            this.PicName = PicName;
        }

        public String getPicType() {
            return PicType;
        }

        public void setPicType(String PicType) {
            this.PicType = PicType;
        }
    }

    public static class TaskCarPicAdditionalListBean implements Serializable{
        /**
         * Id : 2
         * TaskID : 27514
         * PicName : U_1
         * PicFullName : 附加照片_1
         * Path : 2017/02/20/27514/d595cd3e-72a9-4c17-a611-fcc278bec23d.jpg
         * CreateTime : 2017-02-20T20:28:03
         * OrderKey : 0
         * PicStatus : 1
         */

        private int Id;
        private int TaskID;
        private String PicName;
        private String PicFullName;
        private String Path;
        private String CreateTime;
        private int OrderKey;
        private int PicStatus;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getTaskID() {
            return TaskID;
        }

        public void setTaskID(int TaskID) {
            this.TaskID = TaskID;
        }

        public String getPicName() {
            return PicName;
        }

        public void setPicName(String PicName) {
            this.PicName = PicName;
        }

        public String getPicFullName() {
            return PicFullName;
        }

        public void setPicFullName(String PicFullName) {
            this.PicFullName = PicFullName;
        }

        public String getPath() {
            return Path;
        }

        public void setPath(String Path) {
            this.Path = Path;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public int getOrderKey() {
            return OrderKey;
        }

        public void setOrderKey(int OrderKey) {
            this.OrderKey = OrderKey;
        }

        public int getPicStatus() {
            return PicStatus;
        }

        public void setPicStatus(int PicStatus) {
            this.PicStatus = PicStatus;
        }
    }
}
