package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;
import java.util.List;
/**
 * Created by libo on 2017/11/13.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 宝马扩展信息 包含示意图标点数据
 */
public class BmwTaskExModel implements Serializable{


    /**
     * IsOutRepair : 0
     * BToBPrice : 20
     * OCUBToBPrice : 30
     * RBBToBPrice : 23
     * RBOCUBToBPrice : 28
     * RBPrintscreenPath :
     * RBMaintainPath :
     * DefectLegendList : [{"Value":"1","Type":"D","AscriptionId":"O125"},{"Value":"2",
     * "Type":"W","AscriptionId":"O125"},{"Value":"3","Type":"G","AscriptionId":"O129"}]
     */

    private String IsOutRepair;            //是否有BMW/MINI授权经销商外的维修	默认值-1
    private String BToBPrice;              //整备前价格（B to B）评估师提供价格	默认值0
    private String OCUBToBPrice;           //按OCU标准整备后价格（B to B）评估师提供价格	默认值0
    private String RBBToBPrice;            //RedBook整备前价格（B to B）	默认值0
    private String RBOCUBToBPrice;         //RedBook按OCU标准整备后价格（B to B）	默认值0
    private String RBPrintscreenPath;      //RedBook价格截图
    private String RBMaintainPath;         //RMS提供的维保查询链接
    private List<DefectLegendListBean> DefectLegendList;  //外观示意图标点数据

    public String getIsOutRepair() {
        return IsOutRepair;
    }

    public void setIsOutRepair(String IsOutRepair) {
        this.IsOutRepair = IsOutRepair;
    }

    public String getBToBPrice() {
        return BToBPrice;
    }

    public void setBToBPrice(String BToBPrice) {
        this.BToBPrice = BToBPrice;
    }

    public String getOCUBToBPrice() {
        return OCUBToBPrice;
    }

    public void setOCUBToBPrice(String OCUBToBPrice) {
        this.OCUBToBPrice = OCUBToBPrice;
    }

    public String getRBBToBPrice() {
        return RBBToBPrice;
    }

    public void setRBBToBPrice(String RBBToBPrice) {
        this.RBBToBPrice = RBBToBPrice;
    }

    public String getRBOCUBToBPrice() {
        return RBOCUBToBPrice;
    }

    public void setRBOCUBToBPrice(String RBOCUBToBPrice) {
        this.RBOCUBToBPrice = RBOCUBToBPrice;
    }

    public String getRBPrintscreenPath() {
        return RBPrintscreenPath;
    }

    public void setRBPrintscreenPath(String RBPrintscreenPath) {
        this.RBPrintscreenPath = RBPrintscreenPath;
    }

    public String getRBMaintainPath() {
        return RBMaintainPath;
    }

    public void setRBMaintainPath(String RBMaintainPath) {
        this.RBMaintainPath = RBMaintainPath;
    }

    public List<DefectLegendListBean> getDefectLegendList() {
        return DefectLegendList;
    }

    public void setDefectLegendList(List<DefectLegendListBean> DefectLegendList) {
        this.DefectLegendList = DefectLegendList;
    }

    /**
     * Created by 李波 on 2017/11/13.
     * 外观示意图标点数据
     */
    public static class DefectLegendListBean implements Serializable{
        /**
         * Value : 1
         * Type : D
         * AscriptionId : O125
         */

        int marginLeft;
        int marginTop;
        private String Value;
        private String Type;
        private String AscriptionId;

        public int getMarginLeft() {
            return marginLeft;
        }

        public void setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
        }

        public int getMarginTop() {
            return marginTop;
        }

        public void setMarginTop(int marginTop) {
            this.marginTop = marginTop;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getAscriptionId() {
            return AscriptionId;
        }

        public void setAscriptionId(String AscriptionId) {
            this.AscriptionId = AscriptionId;
        }
    }
}
