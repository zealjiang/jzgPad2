/**
 * Project Name:JingZhenGu
 * File Name:Make.java
 * Package Name:com.gc.jingzhengu.vo
 * Date:2014-6-12下午4:12:57
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 */

package com.jcpt.jzg.padsystem.vo;

import android.os.Parcel;
import android.os.Parcelable;


public class Make implements Parcelable {

    //	"GroupName":"A","MakeId":9,"MakeLogo":"http:\/\/img.jingzhengu.com\/Logo\/Make\/m_9_100.jpg","MakeName":"奥迪"
    private int MakeId;

    private String MakeName;

    private String MakeLogo;

    private String GroupName;

    private int FontColor;

    // ListView中item被点击的颜色
    private int ItemColor;

    //判断当前品牌列表中是否有全部选项
    private boolean isMakeAll;

    public int getFontColor() {
        return FontColor;
    }

    public void setFontColor(int fontColor) {
        FontColor = fontColor;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public boolean isMakeAll() {
        return isMakeAll;
    }

    public void setIsMakeAll(boolean isMakeAll) {
        this.isMakeAll = isMakeAll;
    }

    public int getItemColor() {
        return ItemColor;
    }

    public void setItemColor(int itemColor) {
        ItemColor = itemColor;
    }

    public int getMakeId() {
        return MakeId;
    }

    public void setMakeId(int makeId) {
        MakeId = makeId;
    }

    public String getMakeLogo() {
        return MakeLogo;
    }

    public void setMakeLogo(String makeLogo) {
        MakeLogo = makeLogo;
    }

    public String getMakeName() {
        return MakeName;
    }

    public void setMakeName(String makeName) {
        MakeName = makeName;
    }

    @Override
    public String toString() {
        return "Make{" +
                "FontColor=" + FontColor +
                ", MakeId=" + MakeId +
                ", MakeName='" + MakeName + '\'' +
                ", MakeLogo='" + MakeLogo + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", ItemColor=" + ItemColor +
                ", isMakeAll=" + isMakeAll +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.MakeId);
        dest.writeString(this.MakeName);
        dest.writeString(this.MakeLogo);
        dest.writeString(this.GroupName);
        dest.writeInt(this.FontColor);
        dest.writeInt(this.ItemColor);
        dest.writeByte(this.isMakeAll ? (byte) 1 : (byte) 0);
    }

    public Make() {
    }

    protected Make(Parcel in) {
        this.MakeId = in.readInt();
        this.MakeName = in.readString();
        this.MakeLogo = in.readString();
        this.GroupName = in.readString();
        this.FontColor = in.readInt();
        this.ItemColor = in.readInt();
        this.isMakeAll = in.readByte() != 0;
    }

    public static final Creator<Make> CREATOR = new Creator<Make>() {
        @Override
        public Make createFromParcel(Parcel source) {
            return new Make(source);
        }

        @Override
        public Make[] newArray(int size) {
            return new Make[size];
        }
    };
}
