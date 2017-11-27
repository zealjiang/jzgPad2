package com.jcpt.jzg.padsystem.vo;

/**
 * 历史评估价格
 * Created by zealjiang on 2017/2/15 10:48.
 * Email: zealjiang@126.com
 */

public class HistoryPriceModel {


    /**
     * TaskId : 5321
     * UpdateTime : 2016年6月22日
     * BuyPrice : 5.80万元
     * SalePrice : 6.50万元
     * TaskSourceName : 国联资产
     */

    private int TaskId;
    private String UpdateTime;
    private String BuyPrice;
    private String SalePrice;
    private String TaskSourceName;

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int TaskId) {
        this.TaskId = TaskId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getBuyPrice() {
        return BuyPrice;
    }

    public void setBuyPrice(String BuyPrice) {
        this.BuyPrice = BuyPrice;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String SalePrice) {
        this.SalePrice = SalePrice;
    }

    public String getTaskSourceName() {
        return TaskSourceName;
    }

    public void setTaskSourceName(String TaskSourceName) {
        this.TaskSourceName = TaskSourceName;
    }
}
