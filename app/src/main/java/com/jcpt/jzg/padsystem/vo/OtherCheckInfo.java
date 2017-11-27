package com.jcpt.jzg.padsystem.vo;

/**
 * Created by libo on 2016/12/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 其他信息 - - 核对信息数据
 */
public class OtherCheckInfo {

    //取值里有 0 ，所以初始值设置为 -1  -> 李波 on 2016/12/7.
    private int Mileage=-1;
    private int MileageSame=-1;
    private int GaugeLamp=-1;
    private int CarColorActual=-1;
    private String NameplateProperty;
    private String VinProperty;
    private String TyreProperty;
    private String DrivingLicenseCheckEx;
    private String OtherColor; //其他颜色时的描述


    public String getOtherColor() {
        return OtherColor;
    }

    public void setOtherColor(String otherColor) {
        OtherColor = otherColor;
    }

    public int getMileage() {
        return Mileage;
    }

    public void setMileage(int mileage) {
        Mileage = mileage;
    }

    public int getMileageSame() {
        return MileageSame;
    }

    public void setMileageSame(int mileageSame) {
        MileageSame = mileageSame;
    }

    public int getGaugeLamp() {
        return GaugeLamp;
    }

    public void setGaugeLamp(int gaugeLamp) {
        GaugeLamp = gaugeLamp;
    }

    public int getCarColorActual() {
        return CarColorActual;
    }

    public void setCarColorActual(int carColorActual) {
        CarColorActual = carColorActual;
    }

    public String getNameplateProperty() {
        return NameplateProperty;
    }

    public void setNameplateProperty(String nameplateProperty) {
        NameplateProperty = nameplateProperty;
    }

    public String getVinProperty() {
        return VinProperty;
    }

    public void setVinProperty(String vinProperty) {
        VinProperty = vinProperty;
    }

    public String getTyreProperty() {
        return TyreProperty;
    }

    public void setTyreProperty(String tyreProperty) {
        TyreProperty = tyreProperty;
    }

    public String getDrivingLicenseCheckEx() {
        return DrivingLicenseCheckEx;
    }

    public void setDrivingLicenseCheckEx(String drivingLicenseCheckEx) {
        DrivingLicenseCheckEx = drivingLicenseCheckEx;
    }
}
