package com.jcpt.jzg.padsystem.vo.detection;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 15:11
 * @desc:
 */
public class CheckPositionItem implements Parcelable,Serializable {

    private String CheckPositionId;
    private String CheckPositionName;
    private List<ImportantItem> ImportantList;


    public String getCheckPositionId() {
        return CheckPositionId;
    }

    public void setCheckPositionId(String checkPositionId) {
        CheckPositionId = checkPositionId;
    }

    public String getCheckPositionName() {
        return CheckPositionName;
    }

    public void setCheckPositionName(String checkPositionName) {
        CheckPositionName = checkPositionName;
    }

    public List<ImportantItem> getImportantList() {
        return ImportantList;
    }

    public void setImportantList(List<ImportantItem> importantList) {
        ImportantList = importantList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CheckPositionId);
        dest.writeString(this.CheckPositionName);
        dest.writeList(this.ImportantList);
    }

    public CheckPositionItem() {
    }

    protected CheckPositionItem(Parcel in) {
        this.CheckPositionId = in.readString();
        this.CheckPositionName = in.readString();
        this.ImportantList = new ArrayList<ImportantItem>();
        in.readList(this.ImportantList, ImportantItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<CheckPositionItem> CREATOR = new Parcelable.Creator<CheckPositionItem>() {
        @Override
        public CheckPositionItem createFromParcel(Parcel source) {
            return new CheckPositionItem(source);
        }

        @Override
        public CheckPositionItem[] newArray(int size) {
            return new CheckPositionItem[size];
        }
    };
}
