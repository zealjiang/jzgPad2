package com.jcpt.jzg.padsystem.vo.detection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/11/15 15:12
 * @desc:
 */
public class DetectionWrapper implements Serializable{
    private List<ConfigureList> ConfigureList;
    private int ConfigureStatus;
    private List<CheckPositionItem> CheckPositionList;  //检测方位集合  -> 李波 on 2016/12/5.
    private List<String> ProcedureList;
    private int IsEdit;
    private String PlanId;
    private String PlanName;
    private List<String> ReportStarSelect; //附加信息-检测评级描述需要隐藏的项的集合
    private List<PictureItem> PictureList;
    private List<StarScoreItem> ScoreProjectDesList;
    private int IsAuto;
    private TyreInfoListBean TyreInfoList;//BMW品牌年份型号

    public TyreInfoListBean getTyreInfoList() {
        return TyreInfoList;
    }

    public void setTyreInfoList(TyreInfoListBean tyreInfoList) {
        TyreInfoList = tyreInfoList;
    }

    public int getIsAuto() {
        return IsAuto;
    }

    public void setIsAuto(int isAuto) {
        IsAuto = isAuto;
    }

    public List<StarScoreItem> getScoreProjectDesList() {
        return ScoreProjectDesList;
    }

    public void setScoreProjectDesList(List<StarScoreItem> scoreProjectDesList) {
        ScoreProjectDesList = scoreProjectDesList;
    }

    public List<PictureItem> getPictureList() {
        return PictureList;
    }

    public void setPictureList(List<PictureItem> pictureList) {
        PictureList = pictureList;
    }

    public List<String> getReportStarSelect() {
        return ReportStarSelect;
    }

    public void setReportStarSelect(List<String> reportStarSelect) {
        ReportStarSelect = reportStarSelect;
    }


    public List<CheckPositionItem> getCheckPositionList() {
        return CheckPositionList;
    }

    public void setCheckPositionList(List<CheckPositionItem> checkPositionList) {
        CheckPositionList = checkPositionList;
    }

    public List<String> getProcedureList() {
        return ProcedureList;
    }

    public void setProcedureList(List<String> procedureList) {
        ProcedureList = procedureList;
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

    public List<ConfigureList> getConfigureList() {
        return ConfigureList;
    }

    public void setConfigureList(List<ConfigureList> configureList) {
        ConfigureList = configureList;
    }

    public int getConfigureStatus() {
        return ConfigureStatus;
    }

    public void setConfigureStatus(int configureStatus) {
        ConfigureStatus = configureStatus;
    }

    //Discription:[深度复制方法,需要对象及对象所有的对象属性都实现序列化]　
    public DetectionWrapper myclone() {
        DetectionWrapper detectionWrapper = null;
        try { // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝（独立存在内存）
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            // 将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            detectionWrapper = (DetectionWrapper) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return detectionWrapper;
    }


    public static class TyreInfoListBean implements Serializable{
        private List<TyreBrandBean> TyreBrand;
        private List<TyreWideBean> TyreWide;
        private List<TyreFlatBean> TyreFlat;
        private List<TyreDiameterBean> TyreDiameter;
        private List<TyreYearBean> TyreYear;

        public List<TyreBrandBean> getTyreBrand() {
            return TyreBrand;
        }

        public void setTyreBrand(List<TyreBrandBean> TyreBrand) {
            this.TyreBrand = TyreBrand;
        }

        public List<TyreWideBean> getTyreWide() {
            return TyreWide;
        }

        public void setTyreWide(List<TyreWideBean> TyreWide) {
            this.TyreWide = TyreWide;
        }

        public List<TyreFlatBean> getTyreFlat() {
            return TyreFlat;
        }

        public void setTyreFlat(List<TyreFlatBean> TyreFlat) {
            this.TyreFlat = TyreFlat;
        }

        public List<TyreDiameterBean> getTyreDiameter() {
            return TyreDiameter;
        }

        public void setTyreDiameter(List<TyreDiameterBean> TyreDiameter) {
            this.TyreDiameter = TyreDiameter;
        }

        public List<TyreYearBean> getTyreYear() {
            return TyreYear;
        }

        public void setTyreYear(List<TyreYearBean> TyreYear) {
            this.TyreYear = TyreYear;
        }

        public static class TyreBrandBean implements Serializable{
            /**
             * Id : 1
             * ValueName : 德国马牌
             */

            private String Id;
            private String ValueName;

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getValueName() {
                return ValueName;
            }

            public void setValueName(String ValueName) {
                this.ValueName = ValueName;
            }
        }

        public static class TyreWideBean implements Serializable {
            /**
             * Id : 1
             * ValueName : 145
             */

            private String Id;
            private String ValueName;

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getValueName() {
                return ValueName;
            }

            public void setValueName(String ValueName) {
                this.ValueName = ValueName;
            }
        }

        public static class TyreFlatBean implements Serializable{
            /**
             * Id : 1
             * ValueName : 12.5
             */

            private String Id;
            private String ValueName;

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getValueName() {
                return ValueName;
            }

            public void setValueName(String ValueName) {
                this.ValueName = ValueName;
            }
        }

        public static class TyreDiameterBean implements Serializable{
            /**
             * Id : 1
             * ValueName : 12
             */

            private String Id;
            private String ValueName;

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getValueName() {
                return ValueName;
            }

            public void setValueName(String ValueName) {
                this.ValueName = ValueName;
            }
        }

        public static class TyreYearBean implements Serializable{
            /**
             * Id : 2017
             * ValueName : 2017
             */

            private String Id;
            private String ValueName;

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getValueName() {
                return ValueName;
            }

            public void setValueName(String ValueName) {
                this.ValueName = ValueName;
            }
        }
    }
}
