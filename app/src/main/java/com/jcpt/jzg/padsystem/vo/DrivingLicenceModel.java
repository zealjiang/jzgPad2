package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * Created by zealjiang on 2016/11/16 11:23.
 * Email: zealjiang@126.com
 */

public class DrivingLicenceModel implements Serializable{

    /**
     * PlateNo : *01B100
     * VehicleType : 小型轿车
     * Owner : 瞌口
     * Address : 用啊甲1口用
     * Model : 曲曲61·010·11·111·
     * VIN : F01C11
     * EngineNo : 11111
     * RegisterDate :
     * IssueDate : 2011-11-10
     * UseCharacter : 非营运
     */

    private String PlateNo;
    private String VehicleType;
    private String Owner;
    private String Address;
    private String Model;
    private String VIN;
    private String EngineNo;
    private String RegisterDate;
    private String IssueDate;
    private String UseCharacter;

    public String getPlateNo() {
        return PlateNo;
    }

    public void setPlateNo(String PlateNo) {
        this.PlateNo = PlateNo;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String VehicleType) {
        this.VehicleType = VehicleType;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String Owner) {
        this.Owner = Owner;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getEngineNo() {
        return EngineNo;
    }

    public void setEngineNo(String EngineNo) {
        this.EngineNo = EngineNo;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String RegisterDate) {
        this.RegisterDate = RegisterDate;
    }

    public String getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(String IssueDate) {
        this.IssueDate = IssueDate;
    }

    public String getUseCharacter() {
        return UseCharacter;
    }

    public void setUseCharacter(String UseCharacter) {
        this.UseCharacter = UseCharacter;
    }
}
