package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zealjiang on 2017/10/30 10:50.
 * Email: zealjiang@126.com
 */

public class BMWSubmitModel implements Serializable {

    private String ReportAddr;//初检报告url
    /**
     * UserId : 3
     * TaskId : 14882
     * SpRepairList : [{"Id":3,"RepairResult":1}]
     * SpCheckItemList : [{"NameEn":"CLMR_SX","ValueStr":1}]
     */

    private int UserId;
    private int TaskId;
    private List<BMWReCheckBean.BMWRepairBean> SpRepairList;
    private List<BMWBeautyModel.PartBean> SpCheckItemList;


    public String getReportAddr() {
        return ReportAddr;
    }

    public void setReportAddr(String reportAddr) {
        ReportAddr = reportAddr;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int TaskId) {
        this.TaskId = TaskId;
    }

    public List<BMWReCheckBean.BMWRepairBean> getSpRepairList() {
        return SpRepairList;
    }

    public void setSpRepairList(List<BMWReCheckBean.BMWRepairBean> spRepairList) {
        SpRepairList = spRepairList;
    }

    public List<BMWBeautyModel.PartBean> getSpCheckItemList() {
        return SpCheckItemList;
    }

    public void setSpCheckItemList(List<BMWBeautyModel.PartBean> SpCheckItemList) {
        this.SpCheckItemList = SpCheckItemList;
    }

}
