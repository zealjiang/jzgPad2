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
 * @time: 2016/11/15 15:05
 * @desc:
 */
public class DefectType implements Parcelable,Serializable {

    /**
     * DefectTypeId : F01
     * DefectTypeName : 表面缺陷
     * DefectDetailList : [{"DefectId":"D001","DefectName":"轻微变形","PicDefectId":"L05_P08_A003_F01_D001","PicDefectIdList":[{"PicDefectIdPer":"L05_P08_A003_F01_D001_1"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_2"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_3"}]}]
     */

    private String DefectTypeId;
    private String DefectTypeName;
    private List<DefectDetailItem> DefectDetailList;

    public String getDefectTypeId() {
        return DefectTypeId;
    }

    public void setDefectTypeId(String DefectTypeId) {
        this.DefectTypeId = DefectTypeId;
    }

    public String getDefectTypeName() {
        return DefectTypeName;
    }

    public void setDefectTypeName(String DefectTypeName) {
        this.DefectTypeName = DefectTypeName;
    }

    public List<DefectDetailItem> getDefectDetailList() {
        return DefectDetailList;
    }

    public void setDefectDetailList(List<DefectDetailItem> defectDetailList) {
        DefectDetailList = defectDetailList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DefectTypeId);
        dest.writeString(this.DefectTypeName);
        dest.writeList(this.DefectDetailList);
    }

    public DefectType() {
    }

    protected DefectType(Parcel in) {
        this.DefectTypeId = in.readString();
        this.DefectTypeName = in.readString();
        this.DefectDetailList = new ArrayList<DefectDetailItem>();
        in.readList(this.DefectDetailList, DefectDetailItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<DefectType> CREATOR = new Parcelable.Creator<DefectType>() {
        @Override
        public DefectType createFromParcel(Parcel source) {
            return new DefectType(source);
        }

        @Override
        public DefectType[] newArray(int size) {
            return new DefectType[size];
        }
    };
}
