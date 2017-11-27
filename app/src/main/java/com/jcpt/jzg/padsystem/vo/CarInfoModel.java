package com.jcpt.jzg.padsystem.vo;

/**
 * Created by zealjiang on 2017/8/24 18:06.
 * Email: zealjiang@126.com
 */

public class CarInfoModel {

    /**
     * RecordBrand : 宝来牌/BORA
     * EngineNum : S10997
     * FuelType : 汽油
     * Tyre : 205/55R16
     * Seating : 5
     * ProductionTime : 2010-09-19
     */

    private String RecordBrand;
    private String EngineNum;
    private String FuelType;
    private String Tyre;
    private int Seating;
    private String ProductionTime;
    private String EffluentStd;//排放标准参考值

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

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String FuelType) {
        this.FuelType = FuelType;
    }

    public String getTyre() {
        return Tyre;
    }

    public void setTyre(String Tyre) {
        this.Tyre = Tyre;
    }

    public int getSeating() {
        return Seating;
    }

    public void setSeating(int Seating) {
        this.Seating = Seating;
    }

    public String getProductionTime() {
        return ProductionTime;
    }

    public void setProductionTime(String ProductionTime) {
        this.ProductionTime = ProductionTime;
    }

    public String getEffluentStd() {
        return EffluentStd;
    }

    public void setEffluentStd(String effluentStd) {
        EffluentStd = effluentStd;
    }
}
