package com.jcpt.jzg.padsystem.vo.detection;

import android.os.Parcel;
import android.os.Parcelable;

import com.jcpt.jzg.padsystem.vo.TaskDetailModel.CheckPositionListBean.PartsPositionListBean.DefectTypeListBean.ImgListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 15:03
 * @desc: 缺陷详情 Item
 */
public class DefectDetailItem implements Parcelable,Serializable {


    /**
     * DefectId : D001
     * DefectName : 轻微变形
     * PicDefectId : L05_P08_A003_F01_D001
     * PicDefectIdList : [{"PicDefectIdPer":"L05_P08_A003_F01_D001_1"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_2"},{"PicDefectIdPer":"L05_P08_A003_F01_D001_3"}]
     */

    private String DefectId;
    private String DefectName;
    private String PicDefectId;
    private String ReportId;
    private String DefectFullName;
    private List<String> PicDefectIdList;
    private List<ImgListBean> PicDefectHttpUrlList;  //数据详情返回来的图片地址集合  -> 李波 on 2016/11/30.
    private int status; // 0--未选中，1--选中

    private boolean isFix; //是否点击修改过，因为一旦点击后无论什么状态，都不允许检测项再次回到空白状态，一旦点击后置为 true 修改状态，以后再不更改。

    public boolean isFix() {
        return isFix;
    }

    public void setFix(boolean fix) {
        isFix = fix;
    }

    public List<ImgListBean> getPicDefectHttpUrlList() {
        return PicDefectHttpUrlList;
    }

    public void setPicDefectHttpUrlList(List<ImgListBean> picDefectHttpUrlList) {
        PicDefectHttpUrlList = picDefectHttpUrlList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDefectId() {
        return DefectId;
    }

    public void setDefectId(String DefectId) {
        this.DefectId = DefectId;
    }

    public String getDefectName() {
        return DefectName;
    }

    public void setDefectName(String DefectName) {
        this.DefectName = DefectName;
    }

    public String getPicDefectId() {
        return PicDefectId;
    }

    public void setPicDefectId(String PicDefectId) {
        this.PicDefectId = PicDefectId;
    }

    public List<String> getPicDefectIdList() {
        return PicDefectIdList;
    }

    public void setPicDefectIdList(List<String> PicDefectIdList) {
        this.PicDefectIdList = PicDefectIdList;
    }

    public String getReportId() {
        return ReportId;
    }

    public void setReportId(String reportId) {
        ReportId = reportId;
    }

    public String getDefectFullName() {
        return DefectFullName;
    }

    public void setDefectFullName(String defectFullName) {
        DefectFullName = defectFullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DefectId);
        dest.writeString(this.DefectName);
        dest.writeString(this.PicDefectId);
        dest.writeString(this.DefectFullName);
        dest.writeString(this.ReportId);
        dest.writeStringList(this.PicDefectIdList);
    }

    public DefectDetailItem() {
    }

    protected DefectDetailItem(Parcel in) {
        this.DefectId = in.readString();
        this.DefectName = in.readString();
        this.PicDefectId = in.readString();
        this.DefectFullName = in.readString();
        this.ReportId = in.readString();
        this.PicDefectIdList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<DefectDetailItem> CREATOR = new Parcelable.Creator<DefectDetailItem>() {
        @Override
        public DefectDetailItem createFromParcel(Parcel source) {
            return new DefectDetailItem(source);
        }

        @Override
        public DefectDetailItem[] newArray(int size) {
            return new DefectDetailItem[size];
        }
    };
}
