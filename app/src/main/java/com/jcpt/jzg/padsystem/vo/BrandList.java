package com.jcpt.jzg.padsystem.vo;

import com.jcpt.jzg.padsystem.base.BaseObject;

import java.util.List;

/**
 * 品牌
 * Created by zealjiang on 2016/11/1 16:52.
 * Email: zealjiang@126.com
 */

public class BrandList extends BaseObject {

    /**
     * MakeId : 9
     * MakeName : 奥迪
     * MakeLogo : http://image.jingzhengu.com/Vehicle/logo/make/m9100_301.jpg
     * GroupName : A
     */

    private List<MemberValueBean> MemberValue;

    public List<MemberValueBean> getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(List<MemberValueBean> MemberValue) {
        this.MemberValue = MemberValue;
    }

    public static class MemberValueBean {
        private int MakeId;
        private String MakeName;
        private String MakeLogo;
        private String GroupName;
        private int FontColor;

        // ListView中item被点击的颜色
        private int ItemColor;

        //判断当前品牌列表中是否有全部选项
        private boolean isMakeAll;

        public int getMakeId() {
            return MakeId;
        }

        public void setMakeId(int MakeId) {
            this.MakeId = MakeId;
        }

        public String getMakeName() {
            return MakeName;
        }

        public void setMakeName(String MakeName) {
            this.MakeName = MakeName;
        }

        public String getMakeLogo() {
            return MakeLogo;
        }

        public void setMakeLogo(String MakeLogo) {
            this.MakeLogo = MakeLogo;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public int getFontColor() {
            return FontColor;
        }

        public void setFontColor(int fontColor) {
            FontColor = fontColor;
        }

        public int getItemColor() {
            return ItemColor;
        }

        public void setItemColor(int itemColor) {
            ItemColor = itemColor;
        }

        public boolean isMakeAll() {
            return isMakeAll;
        }

        public void setMakeAll(boolean makeAll) {
            isMakeAll = makeAll;
        }
    }
}
