package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zealjiang on 2017/10/27 11:22.
 * Email: zealjiang@126.com
 */

public class BMWMaintainModel implements Serializable {

    /**
     * ReportAddr : https://www.xxx
     *
     */

    private String ReportAddr;
    private List<BMWReCheckBean.BMWRepairBean> Outfit;

    public String getReportAddr() {
        return ReportAddr;
    }

    public void setReportAddr(String ReportAddr) {
        this.ReportAddr = ReportAddr;
    }

    public List<BMWReCheckBean.BMWRepairBean> getOutfit() {
        return Outfit;
    }

    public void setOutfit(List<BMWReCheckBean.BMWRepairBean> outfit) {
        Outfit = outfit;
    }
}
