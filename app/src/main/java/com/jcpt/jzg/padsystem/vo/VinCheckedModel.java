package com.jcpt.jzg.padsystem.vo;

/**
 * Created by wujj on 2017/9/14.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class VinCheckedModel {
    /**
     * IsAlertAccdient : 1
     * AccdientAlertMsg : 此车为大事故车
     */

    private int IsAlertAccdient;
    private String AccdientAlertMsg;

    public int getIsAlertAccdient() {
        return IsAlertAccdient;
    }

    public void setIsAlertAccdient(int IsAlertAccdient) {
        this.IsAlertAccdient = IsAlertAccdient;
    }

    public String getAccdientAlertMsg() {
        return AccdientAlertMsg;
    }

    public void setAccdientAlertMsg(String AccdientAlertMsg) {
        this.AccdientAlertMsg = AccdientAlertMsg;
    }

}
