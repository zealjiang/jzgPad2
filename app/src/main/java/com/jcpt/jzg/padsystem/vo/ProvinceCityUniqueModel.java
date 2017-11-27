package com.jcpt.jzg.padsystem.vo;

/**
 * 省或市
 * Created by zealjiang on 2016/11/17 15:18.
 * Email: zealjiang@126.com
 */

public class ProvinceCityUniqueModel{


    /**
     * ProvinceId : 8
     * ProvinceName : 海南
     * CityId : 802
     * CityName : 琼海
     */

    private int ProvinceId;
    private String ProvinceName;
    private int CityId;
    private String CityName;

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int ProvinceId) {
        this.ProvinceId = ProvinceId;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int CityId) {
        this.CityId = CityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }
}
