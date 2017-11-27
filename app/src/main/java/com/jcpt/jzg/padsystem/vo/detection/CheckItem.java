package com.jcpt.jzg.padsystem.vo.detection;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import com.jcpt.jzg.padsystem.global.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 15:03
 * @desc:
 */
public class CheckItem implements Parcelable,Serializable {


    /**
     * CheckId : P08_A003
     * CheckName : 右A柱外侧
     * DefectTypeList : [{"DefectTypeId":"F01","DefectTypeName":"表面缺陷","DefectDetailList":[{"DefectId":"D001","DefectName":"轻微变形","PicDefectId":"L05_P08_A003_F01_D001","PicDefectIdList":[{"PicDefectIdPer":"L05_P08_A003_F01_D001_1"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_2"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_3"}]}]}]
     */

    private String CheckId;
    private String CheckName;
    private int status; // 0--未选中，1--正常，2--缺陷
    private List<DefectType> DefectTypeList;
    private int itemStatus = Constants.STATUS_NORMAL;//用来记录检测项的状态

    //检测项描述    郑有权--->2017.1.8新增参数
    private String CheckItemDesc;

    public String getCheckItemDesc() {
        return CheckItemDesc;
    }

    public void setCheckItemDesc(String checkItemDesc) {
        CheckItemDesc = checkItemDesc;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public String getCheckId() {
        return CheckId;
    }

    public void setCheckId(String CheckId) {
        this.CheckId = CheckId;
    }

    public String getCheckName() {
        return CheckName;
    }

    public void setCheckName(String CheckName) {
        this.CheckName = CheckName;
    }

    public List<DefectType> getDefectTypeList() {
        return DefectTypeList;
    }

    public void setDefectTypeList(List<DefectType> defectTypeList) {
        DefectTypeList = defectTypeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CheckId);
        dest.writeInt(this.itemStatus);
        dest.writeString(this.CheckName);
        dest.writeList(this.DefectTypeList);
    }

    public CheckItem() {
    }

    protected CheckItem(Parcel in) {
        this.CheckId = in.readString();
        this.CheckName = in.readString();
        this.itemStatus = in.readInt();
        this.DefectTypeList = new ArrayList<DefectType>();
        in.readList(this.DefectTypeList, DefectType.class.getClassLoader());
    }

    public static final Parcelable.Creator<CheckItem> CREATOR = new Parcelable.Creator<CheckItem>() {
        @Override
        public CheckItem createFromParcel(Parcel source) {
            return new CheckItem(source);
        }

        @Override
        public CheckItem[] newArray(int size) {
            return new CheckItem[size];
        }
    };
}
