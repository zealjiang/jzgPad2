package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

/**
 * Created by zealjiang on 2016/12/4 18:04.
 * Email: zealjiang@126.com
 */

public class CarSelectModel implements Serializable{
    private int MakeID;
    private int ModelID;
    private int StyleID;
    private String carFullName;

    public int getMakeID() {
        return MakeID;
    }

    public void setMakeID(int makeID) {
        MakeID = makeID;
    }

    public int getModelID() {
        return ModelID;
    }

    public void setModelID(int modelID) {
        ModelID = modelID;
    }

    public int getStyleID() {
        return StyleID;
    }

    public void setStyleID(int styleID) {
        StyleID = styleID;
    }

    public String getCarFullName() {
        return carFullName;
    }

    public void setCarFullName(String carFullName) {
        this.carFullName = carFullName;
    }
}
