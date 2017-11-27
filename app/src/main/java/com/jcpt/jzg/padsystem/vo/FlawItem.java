package com.jcpt.jzg.padsystem.vo;

import java.util.List;

/**
 * Created by 郑有权 on 2016/11/11.
 */

public class FlawItem {

    /**
     * DefectTypeId : F01
     * DefectTypeName : 表面缺陷
     * DefectDetailList : [{"DefectId":"D001","DefectName":"轻微变形","PicDefectId":"L05_P08_A003_F01_D001","PicDefectIdList":[{"PicDefectIdPer":"L05_P08_A003_F01_D001_1"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_2"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_3"}]}]
     */

    private String DefectTypeId;
    private String DefectTypeName;
    /**
     * DefectId : D001
     * DefectName : 轻微变形
     * PicDefectId : L05_P08_A003_F01_D001
     * PicDefectIdList : [{"PicDefectIdPer":"L05_P08_A003_F01_D001_1"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_2"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_3"}]
     */
    private List<String> DefectIdList;

    public List<String> getDefectIdList() {
        return DefectIdList;
    }

    public void setDefectIdList(List<String> defectIdList) {
        DefectIdList = defectIdList;
    }

    private List<DefectDetailListBean> DefectDetailList;

    public String getDefectTypeId() {
        return DefectTypeId;
    }

    public void setDefectTypeId(String DefectTypeId) {
        this.DefectTypeId = DefectTypeId;
    }

    public String getDefectTypeName() {
        return DefectTypeName;
    }

    public void setDefectTypeName(String DefectTypeName) {
        this.DefectTypeName = DefectTypeName;
    }

    public List<DefectDetailListBean> getDefectDetailList() {
        return DefectDetailList;
    }

    public void setDefectDetailList(List<DefectDetailListBean> DefectDetailList) {
        this.DefectDetailList = DefectDetailList;
    }

    public static class DefectDetailListBean {
        private String DefectId;
        private String DefectName;
        private String PicDefectId;

        /**
         * PicDefectIdPer : L05_P08_A003_F01_D001_1
         */

        private List<PicDefectIdListBean> PicDefectIdList;

        public String getDefectId() {
            return DefectId;
        }

        public void setDefectId(String DefectId) {
            this.DefectId = DefectId;
        }

        public String getDefectName() {
            return DefectName;
        }

        public void setDefectName(String DefectName) {
            this.DefectName = DefectName;
        }

        public String getPicDefectId() {
            return PicDefectId;
        }

        public void setPicDefectId(String PicDefectId) {
            this.PicDefectId = PicDefectId;
        }

        public List<PicDefectIdListBean> getPicDefectIdList() {
            return PicDefectIdList;
        }

        public void setPicDefectIdList(List<PicDefectIdListBean> PicDefectIdList) {
            this.PicDefectIdList = PicDefectIdList;
        }

        public static class PicDefectIdListBean {
            private String PicDefectIdPer;

            public String getPicDefectIdPer() {
                return PicDefectIdPer;
            }

            public void setPicDefectIdPer(String PicDefectIdPer) {
                this.PicDefectIdPer = PicDefectIdPer;
            }
        }
    }


}
