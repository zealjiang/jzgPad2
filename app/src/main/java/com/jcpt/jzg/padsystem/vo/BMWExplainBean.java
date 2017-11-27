package com.jcpt.jzg.padsystem.vo;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wujj on 2017/11/3.
 * 邮箱：wujj@jingzhengu.com
 * 作用：补充说明bean
 */

public class BMWExplainBean {
    private String priceFront;//整备前价格
    private String priceAfter;//整备后价格
    private int ifHasRepair;//是否有维修

    List<LinkedHashMap<String,String>>SpOutRepairRecordList;

    public String getPriceFront() {
        return priceFront;
    }

    public void setPriceFront(String priceFront) {
        this.priceFront = priceFront;
    }

    public String getPriceAfter() {
        return priceAfter;
    }

    public void setPriceAfter(String priceAfter) {
        this.priceAfter = priceAfter;
    }

    public int getIfHasRepair() {
        return ifHasRepair;
    }

    public void setIfHasRepair(int ifHasRepair) {
        this.ifHasRepair = ifHasRepair;
    }

    public List<LinkedHashMap<String, String>> getSpOutRepairRecordList() {
        return SpOutRepairRecordList;
    }

    public void setSpOutRepairRecordList(List<LinkedHashMap<String, String>> spOutRepairRecordList) {
        SpOutRepairRecordList = spOutRepairRecordList;
    }
}
