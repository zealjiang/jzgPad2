package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 美容
 */
public class BMWBeautyModel implements Serializable {


    private List<PartBean> SpCheckItemList;

    public List<PartBean> getPart() {
        return SpCheckItemList;
    }

    public void setPart(List<PartBean> Part) {
        this.SpCheckItemList = Part;
    }

    public static class PartBean {
        /**
         * Id : 11211
         * Name : 车身整洁
         * ValueStr  是：1 否：0
         */

        private int Id;
        private String NameEn;
        private String NameCh;
        private int ValueStr;

        public void setId(int id) {
            Id = id;
        }

        public int getId() {
            return Id;
        }

        public String getNameEn() {
            return NameEn;
        }

        public void setNameEn(String nameEn) {
            NameEn = nameEn;
        }

        public String getNameCh() {
            return NameCh;
        }

        public void setNameCh(String nameCh) {
            NameCh = nameCh;
        }

        public int getValueStr() {
            return ValueStr;
        }

        public void setValueStr(int valueStr) {
            ValueStr = valueStr;
        }
    }
}
