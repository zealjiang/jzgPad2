package com.jcpt.jzg.padsystem.vo;


import java.io.Serializable;

/**
 * Created by libo on 2017/11/1.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 宝马 示意图 标点坐标
 */
public class BMWCoordinateBean implements Serializable {

    int marginLeft;
    int marginTop;
    String value;        //标点数字
    String type;         //缺陷类型字母表示 如： D = 凹陷....
    String ascriptionId; //标点归属用id表示 （轮胎，车灯.....）

    public String getAscriptionId() {
        return ascriptionId;
    }

    public void setAscriptionId(String ascriptionId) {
        this.ascriptionId = ascriptionId;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



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
}
