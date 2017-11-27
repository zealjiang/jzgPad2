package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * Created by zealjiang on 2017/11/9 17:15.
 * Email: zealjiang@126.com
 */

public class BMWOrderInfBean implements Serializable {

    /**
     * TaskId : 148406
     * OrderNo : JZG2011507802608866
     * CarLicense : 京KDJKDJ
     * Vin : LE4GF4JB6CL185124
     * RecordDate : 2017-10-13T00:00:00
     * EngineNum : MXMXMMX
     * CarTypeDes : 捷豹,捷豹XJ,13款 3.0SC 手自一体 旗舰商务版
     * OtherColor :
     * BMWRecommendUrl :
     */

    private int TaskId;
    private String OrderNo;
    private String CarLicense;
    private String Vin;
    private String RecordDate;
    private String EngineNum;
    private String CarTypeDes;
    private String OtherColor;
    private String BMWRecommendUrl;

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int TaskId) {
        this.TaskId = TaskId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getCarLicense() {
        return CarLicense;
    }

    public void setCarLicense(String CarLicense) {
        this.CarLicense = CarLicense;
    }

    public String getVin() {
        return Vin;
    }

    public void setVin(String Vin) {
        this.Vin = Vin;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String RecordDate) {
        this.RecordDate = RecordDate;
    }

    public String getEngineNum() {
        return EngineNum;
    }

    public void setEngineNum(String EngineNum) {
        this.EngineNum = EngineNum;
    }

    public String getCarTypeDes() {
        return CarTypeDes;
    }

    public void setCarTypeDes(String CarTypeDes) {
        this.CarTypeDes = CarTypeDes;
    }

    public String getOtherColor() {
        return OtherColor;
    }

    public void setOtherColor(String OtherColor) {
        this.OtherColor = OtherColor;
    }

    public String getBMWRecommendUrl() {
        return BMWRecommendUrl;
    }

    public void setBMWRecommendUrl(String BMWRecommendUrl) {
        this.BMWRecommendUrl = BMWRecommendUrl;
    }
}
