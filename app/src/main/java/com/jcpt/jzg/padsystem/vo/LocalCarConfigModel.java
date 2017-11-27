package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * 车系选择参数
 * Created by zealjiang on 2016/11/10 14:06.
 * Email: zealjiang@126.com
 */

public class LocalCarConfigModel implements Serializable{


    private int brandId;
    private String brandName;
    private int serieId;
    private String serieName;
    private int styleId;
    private String styleName;
    private String fullName;//标题
    private String gear;//变速箱
    private String drivingMode;//驱动方式
    private String displacement;//排气量
    private String productDate;//出厂日期
    private String NowMsrp;//厂商指导价
    private String Year;//出厂年月
    private String vin;
    private String nameplate;//车辆铭牌
    private String styleFullName;//车型全称（包含品牌、车系）
    private String ActivityId;//服务器用
    private String BMWRecommendUrl;//BMW推荐配置地址
    private String CarTypeDes;//宝马推荐车型


    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public int getSerieId() {
        return serieId;
    }

    public void setSerieId(int serieId) {
        this.serieId = serieId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getDrivingMode() {
        return drivingMode;
    }

    public void setDrivingMode(String drivingMode) {
        this.drivingMode = drivingMode;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getNowMsrp() {
        return NowMsrp;
    }

    public void setNowMsrp(String nowMsrp) {
        NowMsrp = nowMsrp;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNameplate() {
        return nameplate;
    }

    public void setNameplate(String nameplate) {
        this.nameplate = nameplate;
    }

    public String getStyleFullName() {
        return styleFullName;
    }

    public void setStyleFullName(String styleFullName) {
        this.styleFullName = styleFullName;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getBMWRecommendUrl() {
        return BMWRecommendUrl;
    }

    public void setBMWRecommendUrl(String BMWRecommendUrl) {
        this.BMWRecommendUrl = BMWRecommendUrl;
    }

    public String getCarTypeDes() {
        return CarTypeDes;
    }

    public void setCarTypeDes(String carTypeDes) {
        CarTypeDes = carTypeDes;
    }
}
