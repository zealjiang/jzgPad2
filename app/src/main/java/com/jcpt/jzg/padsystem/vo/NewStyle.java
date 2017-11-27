package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * Created by JZG on 2016/7/7.
 */
public class NewStyle implements Serializable {


    /**
     * StyleId : 112206
     * Name : 雪佛兰科鲁兹三厢 1.5L 手动 SL经典版
     * Mark :
     * PosString :
     * NowMsrp : 8.99
     * DrivingMode : 前驱
     * ExhaustVolume : 1.5
     * Fuel : 汽油
     * FullName : null
     * SeatNumber : 5
     * Transmission : MT手动
     * ModelID : 2608
     * MakeID : 49
     * CarTypeName : 紧凑型车
     * IsT : false
     * EngineType : null
     * MaxPower : null
     * EnginePositon : null
     * OilTankVolumn : 0.0
     * LowYear : 2015
     * ModelName : 雪佛兰科鲁兹三厢
     * MakeName : 雪佛兰
     */

    private int StyleId;
    private String Name;
    private String Mark;
    private String PosString;
    private double NowMsrp;
    private String DrivingMode;
    private double ExhaustVolume;
    private String Fuel;
    private Object FullName;
    private String SeatNumber;
    private String Transmission;
    private int ModelID;
    private int MakeID;
    private String CarTypeName;
    private boolean IsT;
    private Object EngineType;
    private Object MaxPower;
    private Object EnginePositon;
    private double OilTankVolumn;
    private String LowYear;
    private String ModelName;
    private String MakeName;
    private String Year;//本地使用
    private boolean IsCancel;//本地使用
    private String ActivityId;//服务器使用

    public int getStyleId() {
        return StyleId;
    }

    public void setStyleId(int StyleId) {
        this.StyleId = StyleId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMark() {
        return Mark;
    }

    public void setMark(String Mark) {
        this.Mark = Mark;
    }

    public String getPosString() {
        return PosString;
    }

    public void setPosString(String PosString) {
        this.PosString = PosString;
    }

    public double getNowMsrp() {
        return NowMsrp;
    }

    public void setNowMsrp(double NowMsrp) {
        this.NowMsrp = NowMsrp;
    }

    public String getDrivingMode() {
        return DrivingMode;
    }

    public void setDrivingMode(String DrivingMode) {
        this.DrivingMode = DrivingMode;
    }

    public double getExhaustVolume() {
        return ExhaustVolume;
    }

    public void setExhaustVolume(double ExhaustVolume) {
        this.ExhaustVolume = ExhaustVolume;
    }

    public String getFuel() {
        return Fuel;
    }

    public void setFuel(String Fuel) {
        this.Fuel = Fuel;
    }

    public Object getFullName() {
        return FullName;
    }

    public void setFullName(Object FullName) {
        this.FullName = FullName;
    }

    public String getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(String SeatNumber) {
        this.SeatNumber = SeatNumber;
    }

    public String getTransmission() {
        return Transmission;
    }

    public void setTransmission(String Transmission) {
        this.Transmission = Transmission;
    }

    public int getModelID() {
        return ModelID;
    }

    public void setModelID(int ModelID) {
        this.ModelID = ModelID;
    }

    public int getMakeID() {
        return MakeID;
    }

    public void setMakeID(int MakeID) {
        this.MakeID = MakeID;
    }

    public String getCarTypeName() {
        return CarTypeName;
    }

    public void setCarTypeName(String CarTypeName) {
        this.CarTypeName = CarTypeName;
    }

    public boolean isIsT() {
        return IsT;
    }

    public void setIsT(boolean IsT) {
        this.IsT = IsT;
    }

    public Object getEngineType() {
        return EngineType;
    }

    public void setEngineType(Object EngineType) {
        this.EngineType = EngineType;
    }

    public Object getMaxPower() {
        return MaxPower;
    }

    public void setMaxPower(Object MaxPower) {
        this.MaxPower = MaxPower;
    }

    public Object getEnginePositon() {
        return EnginePositon;
    }

    public void setEnginePositon(Object EnginePositon) {
        this.EnginePositon = EnginePositon;
    }

    public double getOilTankVolumn() {
        return OilTankVolumn;
    }

    public void setOilTankVolumn(double OilTankVolumn) {
        this.OilTankVolumn = OilTankVolumn;
    }

    public String getLowYear() {
        return LowYear;
    }

    public void setLowYear(String LowYear) {
        this.LowYear = LowYear;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public String getMakeName() {
        return MakeName;
    }

    public void setMakeName(String MakeName) {
        this.MakeName = MakeName;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public boolean isCancel() {
        return IsCancel;
    }

    public void setCancel(boolean cancel) {
        IsCancel = cancel;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }
}
