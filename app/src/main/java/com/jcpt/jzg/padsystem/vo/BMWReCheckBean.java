package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 复检需要维修列表
 * Created by zealjiang on 2017/11/10 10:37.
 * Email: zealjiang@126.com
 */

public class BMWReCheckBean implements Serializable {

    private String CJReportUrl;//初检报告地址
    private List<BMWRepairBean> RepairList;

    public String getCJReportUrl() {
        return CJReportUrl;
    }

    public void setCJReportUrl(String CJReportUrl) {
        this.CJReportUrl = CJReportUrl;
    }

    public List<BMWRepairBean> getRepairList() {
        return RepairList;
    }

    public void setRepairList(List<BMWRepairBean> repairList) {
        RepairList = repairList;
    }

    public static class BMWRepairBean{
        /**
         * Id : 3
         * TaskId : 148883
         * RepairId : O003
         * RepairDes : 车辆事故已做记录，并严格按照宝马工作规范对事故损坏进行修复（记录在收据中维修记录,保险记录,在系统中可查）
         * Standard : OCU
         * NeedRepair : 2
         * RepairResult : 1
         */

        private int Id;
        private int TaskId;
        private String RepairId;
        private String RepairDes;
        private String Standard;
        private int NeedRepair;
        private int RepairResult;//本地和提交用  是否已维修 -1：未知  1：是  0：否

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getTaskId() {
            return TaskId;
        }

        public void setTaskId(int TaskId) {
            this.TaskId = TaskId;
        }

        public String getRepairId() {
            return RepairId;
        }

        public void setRepairId(String RepairId) {
            this.RepairId = RepairId;
        }

        public String getRepairDes() {
            return RepairDes;
        }

        public void setRepairDes(String RepairDes) {
            this.RepairDes = RepairDes;
        }

        public String getStandard() {
            return Standard;
        }

        public void setStandard(String Standard) {
            this.Standard = Standard;
        }

        public int getNeedRepair() {
            return NeedRepair;
        }

        public void setNeedRepair(int NeedRepair) {
            this.NeedRepair = NeedRepair;
        }

        public int getRepairResult() {
            return RepairResult;
        }

        public void setRepairResult(int RepairResult) {
            this.RepairResult = RepairResult;
        }
    }


}
