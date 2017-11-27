/**
 * Project Name:JingZhenGu
 * File Name:MakeList.java
 * Package Name:com.gc.jingzhengu.vo
 * Date:2014-6-12下午12:20:59
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 */

package com.jcpt.jzg.padsystem.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class MakeList implements Parcelable {
    private int status;
    private String msg;
    private String success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    private ArrayList<Make> MakeList;

    public ArrayList<Make> getMakeList() {
        return MakeList;
    }

    public void setMakeList(ArrayList<Make> makeList) {
        MakeList = makeList;
    }

    @Override
    public String toString() {
        return "MakeListNew{" +
                "MakeListNew=" + MakeList +
                '}';
    }

    public MakeList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.msg);
        dest.writeTypedList(this.MakeList);
    }

    protected MakeList(Parcel in) {
        this.status = in.readInt();
        this.msg = in.readString();
        this.MakeList = in.createTypedArrayList(Make.CREATOR);
    }

    public static final Creator<MakeList> CREATOR = new Creator<MakeList>() {
        @Override
        public MakeList createFromParcel(Parcel source) {
            return new MakeList(source);
        }

        @Override
        public MakeList[] newArray(int size) {
            return new MakeList[size];
        }

    };
}
