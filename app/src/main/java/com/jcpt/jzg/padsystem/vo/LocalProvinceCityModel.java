package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * Created by zealjiang on 2016/11/17 17:52.
 * Email: zealjiang@126.com
 */

public class LocalProvinceCityModel implements Serializable{

    //初登地区
    private int provinceId;
    private String provinceName;
    private int cityId;
    private String cityName;


    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
