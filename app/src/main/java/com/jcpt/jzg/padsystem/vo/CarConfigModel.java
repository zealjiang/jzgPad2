package com.jcpt.jzg.padsystem.vo;

import com.jcpt.jzg.padsystem.base.BaseObject;

import java.util.List;

/**
 * Created by zealjiang on 2016/11/2 20:25.
 * Email: zealjiang@126.com
 */

public class CarConfigModel extends BaseObject {

    /**
     * Gear : 手动
     * DrivingMode : 两驱
     * Displacement : 1.6
     */

    private List<MemberValueBean> MemberValue;

    public List<MemberValueBean> getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(List<MemberValueBean> MemberValue) {
        this.MemberValue = MemberValue;
    }

    public static class MemberValueBean {
        private String Gear;
        private String DrivingMode;
        private String Displacement;

        public String getGear() {
            return Gear;
        }

        public void setGear(String Gear) {
            this.Gear = Gear;
        }

        public String getDrivingMode() {
            return DrivingMode;
        }

        public void setDrivingMode(String DrivingMode) {
            this.DrivingMode = DrivingMode;
        }

        public String getDisplacement() {
            return Displacement;
        }

        public void setDisplacement(String Displacement) {
            this.Displacement = Displacement;
        }
    }
}
