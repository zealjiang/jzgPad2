package com.jcpt.jzg.padsystem.vo;

import com.jcpt.jzg.padsystem.base.BaseObject;

import java.util.List;

/**
 * 备选款型
 * Created by zealjiang on 2016/11/4 20:19.
 * Email: zealjiang@126.com
 */

public class CarTypeSelectModel extends BaseObject {

    /**
     * MemberValue : {"TotalCount":1,"FullName":"雪佛兰科鲁兹三厢","ConfigList":[{"Pos":"$1$","Name":"电动天窗"},{"Pos":"$2$","Name":"前雾灯"}],"list":[{"Year":2015,"list":[{"StyleId":112206,"Name":"雪佛兰科鲁兹三厢 1.5L 手动 SL经典版","Mark":"","PosString":"","NowMsrp":8.99,"DrivingMode":"前驱","ExhaustVolume":1.5,"Fuel":"汽油","FullName":null,"SeatNumber":"5","Transmission":"MT手动","ModelID":2608,"MakeID":49,"CarTypeName":"紧凑型车","IsT":false,"EngineType":null,"MaxPower":null,"EnginePositon":null,"OilTankVolumn":0,"LowYear":"2015","ModelName":"雪佛兰科鲁兹三厢","MakeName":"雪佛兰"},{"StyleId":112257,"Name":"雪佛兰科鲁兹三厢 1.5L 手动 SE经典版","Mark":"","PosString":"$1$,$2$","NowMsrp":9.99,"DrivingMode":"前驱","ExhaustVolume":1.5,"Fuel":"汽油","FullName":null,"SeatNumber":"5","Transmission":"MT手动","ModelID":2608,"MakeID":49,"CarTypeName":"紧凑型车","IsT":false,"EngineType":null,"MaxPower":null,"EnginePositon":null,"OilTankVolumn":0,"LowYear":"2015","ModelName":"雪佛兰科鲁兹三厢","MakeName":"雪佛兰"}]}],"CarModel":{"ModelId":2608,"ManufacturerName":"上汽通用雪佛兰","ModelName":"雪佛兰科鲁兹三厢","ModelImgPath":null},"CarMake":{"GroupName":null,"MakeId":49,"MakeLogo":null,"MakeName":"雪佛兰"}}
     */

    private MemberValueBean MemberValue;

    public MemberValueBean getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(MemberValueBean MemberValue) {
        this.MemberValue = MemberValue;
    }

    public static class MemberValueBean {
        /**
         * TotalCount : 1
         * FullName : 雪佛兰科鲁兹三厢
         * ConfigList : [{"Pos":"$1$","Name":"电动天窗"},{"Pos":"$2$","Name":"前雾灯"}]
         * list : [{"Year":2015,"list":[{"StyleId":112206,"Name":"雪佛兰科鲁兹三厢 1.5L 手动 SL经典版","Mark":"","PosString":"","NowMsrp":8.99,"DrivingMode":"前驱","ExhaustVolume":1.5,"Fuel":"汽油","FullName":null,"SeatNumber":"5","Transmission":"MT手动","ModelID":2608,"MakeID":49,"CarTypeName":"紧凑型车","IsT":false,"EngineType":null,"MaxPower":null,"EnginePositon":null,"OilTankVolumn":0,"LowYear":"2015","ModelName":"雪佛兰科鲁兹三厢","MakeName":"雪佛兰"},{"StyleId":112257,"Name":"雪佛兰科鲁兹三厢 1.5L 手动 SE经典版","Mark":"","PosString":"$1$,$2$","NowMsrp":9.99,"DrivingMode":"前驱","ExhaustVolume":1.5,"Fuel":"汽油","FullName":null,"SeatNumber":"5","Transmission":"MT手动","ModelID":2608,"MakeID":49,"CarTypeName":"紧凑型车","IsT":false,"EngineType":null,"MaxPower":null,"EnginePositon":null,"OilTankVolumn":0,"LowYear":"2015","ModelName":"雪佛兰科鲁兹三厢","MakeName":"雪佛兰"}]}]
         * CarModel : {"ModelId":2608,"ManufacturerName":"上汽通用雪佛兰","ModelName":"雪佛兰科鲁兹三厢","ModelImgPath":null}
         * CarMake : {"GroupName":null,"MakeId":49,"MakeLogo":null,"MakeName":"雪佛兰"}
         */

        private int TotalCount;
        private String FullName;
        private CarModelBean CarModel;
        private CarMakeBean CarMake;
        private List<ConfigListBean> ConfigList;
        private List<ListBeanX> list;
        private String ActivityId;

        public int getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(int TotalCount) {
            this.TotalCount = TotalCount;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String FullName) {
            this.FullName = FullName;
        }

        public CarModelBean getCarModel() {
            return CarModel;
        }

        public void setCarModel(CarModelBean CarModel) {
            this.CarModel = CarModel;
        }

        public CarMakeBean getCarMake() {
            return CarMake;
        }

        public void setCarMake(CarMakeBean CarMake) {
            this.CarMake = CarMake;
        }

        public List<ConfigListBean> getConfigList() {
            return ConfigList;
        }

        public void setConfigList(List<ConfigListBean> ConfigList) {
            this.ConfigList = ConfigList;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public String getActivityId() {
            return ActivityId;
        }

        public void setActivityId(String activityId) {
            ActivityId = activityId;
        }

        public static class CarModelBean {
            /**
             * ModelId : 2608
             * ManufacturerName : 上汽通用雪佛兰
             * ModelName : 雪佛兰科鲁兹三厢
             * ModelImgPath : null
             */

            private int ModelId;
            private String ManufacturerName;
            private String ModelName;
            private String ModelImgPath;

            public int getModelId() {
                return ModelId;
            }

            public void setModelId(int ModelId) {
                this.ModelId = ModelId;
            }

            public String getManufacturerName() {
                return ManufacturerName;
            }

            public void setManufacturerName(String ManufacturerName) {
                this.ManufacturerName = ManufacturerName;
            }

            public String getModelName() {
                return ModelName;
            }

            public void setModelName(String ModelName) {
                this.ModelName = ModelName;
            }

            public String getModelImgPath() {
                return ModelImgPath;
            }

            public void setModelImgPath(String ModelImgPath) {
                this.ModelImgPath = ModelImgPath;
            }
        }

        public static class CarMakeBean {
            /**
             * GroupName : null
             * MakeId : 49
             * MakeLogo : null
             * MakeName : 雪佛兰
             */

            private String GroupName;
            private int MakeId;
            private String MakeLogo;
            private String MakeName;

            public String getGroupName() {
                return GroupName;
            }

            public void setGroupName(String GroupName) {
                this.GroupName = GroupName;
            }

            public int getMakeId() {
                return MakeId;
            }

            public void setMakeId(int MakeId) {
                this.MakeId = MakeId;
            }

            public String getMakeLogo() {
                return MakeLogo;
            }

            public void setMakeLogo(String MakeLogo) {
                this.MakeLogo = MakeLogo;
            }

            public String getMakeName() {
                return MakeName;
            }

            public void setMakeName(String MakeName) {
                this.MakeName = MakeName;
            }
        }

        public static class ConfigListBean {
            /**
             * Pos : $1$
             * Name : 电动天窗
             */

            private String Pos;
            private String Name;

            public String getPos() {
                return Pos;
            }

            public void setPos(String Pos) {
                this.Pos = Pos;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }
        }

        public static class ListBeanX {
            /**
             * Year : 2015
             * list : [{"StyleId":112206,"Name":"雪佛兰科鲁兹三厢 1.5L 手动 SL经典版","Mark":"","PosString":"","NowMsrp":8.99,"DrivingMode":"前驱","ExhaustVolume":1.5,"Fuel":"汽油","FullName":null,"SeatNumber":"5","Transmission":"MT手动","ModelID":2608,"MakeID":49,"CarTypeName":"紧凑型车","IsT":false,"EngineType":null,"MaxPower":null,"EnginePositon":null,"OilTankVolumn":0,"LowYear":"2015","ModelName":"雪佛兰科鲁兹三厢","MakeName":"雪佛兰"},{"StyleId":112257,"Name":"雪佛兰科鲁兹三厢 1.5L 手动 SE经典版","Mark":"","PosString":"$1$,$2$","NowMsrp":9.99,"DrivingMode":"前驱","ExhaustVolume":1.5,"Fuel":"汽油","FullName":null,"SeatNumber":"5","Transmission":"MT手动","ModelID":2608,"MakeID":49,"CarTypeName":"紧凑型车","IsT":false,"EngineType":null,"MaxPower":null,"EnginePositon":null,"OilTankVolumn":0,"LowYear":"2015","ModelName":"雪佛兰科鲁兹三厢","MakeName":"雪佛兰"}]
             */

            private int Year;
            private List<ListBean> list;

            public int getYear() {
                return Year;
            }

            public void setYear(int Year) {
                this.Year = Year;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * StyleId : 112206
                 * Name : 雪佛兰科鲁兹三厢 1.5L 手动 SL经典版
                 * Mark :
                 * PosString :
                 * NowMsrp : 8.99
                 * DrivingMode : 前驱
                 * ExhaustVolume : 1.5
                 * Fuel : 汽油
                 * FullName : null
                 * SeatNumber : 5
                 * Transmission : MT手动
                 * ModelID : 2608
                 * MakeID : 49
                 * CarTypeName : 紧凑型车
                 * IsT : false
                 * EngineType : null
                 * MaxPower : null
                 * EnginePositon : null
                 * OilTankVolumn : 0.0
                 * LowYear : 2015
                 * ModelName : 雪佛兰科鲁兹三厢
                 * MakeName : 雪佛兰
                 */

                private int StyleId;
                private String Name;
                private String Mark;
                private String PosString;
                private double NowMsrp;
                private String DrivingMode;
                private double ExhaustVolume;
                private String Fuel;
                private String FullName;
                private String SeatNumber;
                private String Transmission;
                private int ModelID;
                private int MakeID;
                private String CarTypeName;
                private boolean IsT;
                private Object EngineType;
                private Object MaxPower;
                private Object EnginePositon;
                private double OilTankVolumn;
                private String LowYear;
                private String ModelName;
                private String MakeName;
                private boolean IsTitle;
                private String Year;

                public int getStyleId() {
                    return StyleId;
                }

                public void setStyleId(int StyleId) {
                    this.StyleId = StyleId;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }

                public String getMark() {
                    return Mark;
                }

                public void setMark(String Mark) {
                    this.Mark = Mark;
                }

                public String getPosString() {
                    return PosString;
                }

                public void setPosString(String PosString) {
                    this.PosString = PosString;
                }

                public double getNowMsrp() {
                    return NowMsrp;
                }

                public void setNowMsrp(double NowMsrp) {
                    this.NowMsrp = NowMsrp;
                }

                public String getDrivingMode() {
                    return DrivingMode;
                }

                public void setDrivingMode(String DrivingMode) {
                    this.DrivingMode = DrivingMode;
                }

                public double getExhaustVolume() {
                    return ExhaustVolume;
                }

                public void setExhaustVolume(double ExhaustVolume) {
                    this.ExhaustVolume = ExhaustVolume;
                }

                public String getFuel() {
                    return Fuel;
                }

                public void setFuel(String Fuel) {
                    this.Fuel = Fuel;
                }

                public String getFullName() {
                    return FullName;
                }

                public void setFullName(String FullName) {
                    this.FullName = FullName;
                }

                public String getSeatNumber() {
                    return SeatNumber;
                }

                public void setSeatNumber(String SeatNumber) {
                    this.SeatNumber = SeatNumber;
                }

                public String getTransmission() {
                    return Transmission;
                }

                public void setTransmission(String Transmission) {
                    this.Transmission = Transmission;
                }

                public int getModelID() {
                    return ModelID;
                }

                public void setModelID(int ModelID) {
                    this.ModelID = ModelID;
                }

                public int getMakeID() {
                    return MakeID;
                }

                public void setMakeID(int MakeID) {
                    this.MakeID = MakeID;
                }

                public String getCarTypeName() {
                    return CarTypeName;
                }

                public void setCarTypeName(String CarTypeName) {
                    this.CarTypeName = CarTypeName;
                }

                public boolean isIsT() {
                    return IsT;
                }

                public void setIsT(boolean IsT) {
                    this.IsT = IsT;
                }

                public Object getEngineType() {
                    return EngineType;
                }

                public void setEngineType(Object EngineType) {
                    this.EngineType = EngineType;
                }

                public Object getMaxPower() {
                    return MaxPower;
                }

                public void setMaxPower(Object MaxPower) {
                    this.MaxPower = MaxPower;
                }

                public Object getEnginePositon() {
                    return EnginePositon;
                }

                public void setEnginePositon(Object EnginePositon) {
                    this.EnginePositon = EnginePositon;
                }

                public double getOilTankVolumn() {
                    return OilTankVolumn;
                }

                public void setOilTankVolumn(double OilTankVolumn) {
                    this.OilTankVolumn = OilTankVolumn;
                }

                public String getLowYear() {
                    return LowYear;
                }

                public void setLowYear(String LowYear) {
                    this.LowYear = LowYear;
                }

                public String getModelName() {
                    return ModelName;
                }

                public void setModelName(String ModelName) {
                    this.ModelName = ModelName;
                }

                public String getMakeName() {
                    return MakeName;
                }

                public void setMakeName(String MakeName) {
                    this.MakeName = MakeName;
                }

                public boolean isTitle() {
                    return IsTitle;
                }

                public void setIsTitle(boolean title) {
                    IsTitle = title;
                }

                public String getYear() {
                    return Year;
                }

                public void setYear(String year) {
                    Year = year;
                }
            }
        }
    }
}
