package com.jcpt.jzg.padsystem.vo.detection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zealjiang on 2017/3/28 11:11.
 * Email: zealjiang@126.com
 */

public class ConfigureList implements Serializable{

    /**
     * ConfigureName : ABS
     * ConfigureValueList : [{"ConfigureValueId":"B1_F001","ConfigureValueName":"有"},{"ConfigureValueId":"B1_F002","ConfigureValueName":"无"}]
     * SelectType : 1
     */

    private String ConfigureName;
    private int SelectType;//1表示单选  2 表示多选
    private List<ConfigureValueListBean> ConfigureValueList;

    public String getConfigureName() {
        return ConfigureName;
    }

    public void setConfigureName(String ConfigureName) {
        this.ConfigureName = ConfigureName;
    }

    public int getSelectType() {
        return SelectType;
    }

    public void setSelectType(int SelectType) {
        this.SelectType = SelectType;
    }

    public List<ConfigureValueListBean> getConfigureValueList() {
        return ConfigureValueList;
    }

    public void setConfigureValueList(List<ConfigureValueListBean> ConfigureValueList) {
        this.ConfigureValueList = ConfigureValueList;
    }

    public static class ConfigureValueListBean implements Serializable{
        /**
         * ConfigureValueId : B1_F001
         * ConfigureValueName : 有
         */

        private String ConfigureValueId;
        private String ConfigureValueName;

        public String getConfigureValueId() {
            return ConfigureValueId;
        }

        public void setConfigureValueId(String ConfigureValueId) {
            this.ConfigureValueId = ConfigureValueId;
        }

        public String getConfigureValueName() {
            return ConfigureValueName;
        }

        public void setConfigureValueName(String ConfigureValueName) {
            this.ConfigureValueName = ConfigureValueName;
        }
    }
}
