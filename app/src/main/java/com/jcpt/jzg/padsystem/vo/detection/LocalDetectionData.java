package com.jcpt.jzg.padsystem.vo.detection;

import com.jcpt.jzg.padsystem.vo.BmwTaskExModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来保存车况检测数据
 */
public class LocalDetectionData implements Serializable{
    private List<CheckPositionItem> CheckPositionList;  //检测方位集合  -> 李波 on 2016/12/5.
    private List<String> defectValueList;               //修改的缺陷项 ID 集合，对应submit提交时的 defectvalue
    private int IsEdit;
    private String PlanId;
    private String PlanName;

    private List<PictureItem> PictureList;              //基本照片
    private List<PictureItem> TaskCarPicAdditionalListBean; //附加照片

    List<BmwTaskExModel.DefectLegendListBean> coordinateBeenList; //宝马示意图标点数据集合

    public List<BmwTaskExModel.DefectLegendListBean> getCoordinateBeenList() {
        return coordinateBeenList;
    }

    public void setCoordinateBeenList(List<BmwTaskExModel.DefectLegendListBean>
                                              coordinateBeenList) {
        this.coordinateBeenList = coordinateBeenList;
    }


    public List<PictureItem> getPictureList() {
        return PictureList;
    }

    public void setPictureList(List<PictureItem> pictureList) {
        PictureList = pictureList;
    }

    public List<String> getDefectValueList() {
        return defectValueList;
    }

    public void setDefectValueList(List<String> defectValueList) {
        this.defectValueList = defectValueList;
    }

    public List<CheckPositionItem> getCheckPositionList() {
        return CheckPositionList;
    }

    public void setCheckPositionList(List<CheckPositionItem> checkPositionList) {
        CheckPositionList = checkPositionList;
    }

    public int getIsEdit() {
        return IsEdit;
    }

    public void setIsEdit(int isEdit) {
        IsEdit = isEdit;
    }

    public String getPlanId() {
        return PlanId;
    }

    public void setPlanId(String planId) {
        PlanId = planId;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

}
