package com.jcpt.jzg.padsystem.vo;

/**
 * EventBus 发送消息对象
 * Created by zealjiang on 2016/11/22 15:29.
 * Email: zealjiang@126.com
 */

public class EventProcedureModel {

    /**
     * 行驶证有瑕疵是否选中，选中为true
     */
    private boolean drivingFlowIsSelect;
    /**
     * 行驶证有未见是否选中，选中为true
     */
    private boolean drivingNoneIsSelect;
    /**
     * 无行驶证是否选中，选中为true
     */
    private boolean drivingLicNoIsSelect;
    /**
     * 登记证有瑕疵是否选中，选中为true
     */
    private boolean registerFlowIsSelected;
    /**
     * 登记证未见是否选中，选中为true
     */
    private boolean registerNoneIsSelected;

    public boolean isDrivingFlowIsSelect() {
        return drivingFlowIsSelect;
    }

    public void setDrivingFlowIsSelect(boolean drivingFlowIsSelect) {
        this.drivingFlowIsSelect = drivingFlowIsSelect;
    }

    public boolean isRegisterNoneIsSelected() {
        return registerNoneIsSelected;
    }

    public void setRegisterNoneIsSelected(boolean registerNoneIsSelected) {
        this.registerNoneIsSelected = registerNoneIsSelected;
    }

    public boolean isRegisterFlowIsSelected() {
        return registerFlowIsSelected;
    }

    public void setRegisterFlowIsSelected(boolean registerFlowIsSelected) {
        this.registerFlowIsSelected = registerFlowIsSelected;
    }

    public boolean isDrivingNoneIsSelect() {
        return drivingNoneIsSelect;
    }

    public void setDrivingNoneIsSelect(boolean drivingNoneIsSelect) {
        this.drivingNoneIsSelect = drivingNoneIsSelect;
    }

    public boolean isDrivingLicNoIsSelect() {
        return drivingLicNoIsSelect;
    }

    public void setDrivingLicNoIsSelect(boolean drivingLicNoIsSelect) {
        this.drivingLicNoIsSelect = drivingLicNoIsSelect;
    }
}
