package com.jcpt.jzg.padsystem.vo;

import com.jcpt.jzg.padsystem.base.BaseObject;

import java.util.List;

/**
 * Created by zealjiang on 2017/4/19 18:02.
 * Email: zealjiang@126.com
 */

public class CarTypeModel extends BaseObject {

    private List<MemberValueBean> MemberValue;

    public List<MemberValueBean> getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(List<MemberValueBean> MemberValue) {
        this.MemberValue = MemberValue;
    }

    public static class MemberValueBean {
        /**
         * Year : 2017
         * list : [{"StyleId":"120138","StyleName":"1.4T 双离合 先锋天窗版","StyleYear":"2017","StyleNowMsrp":"13.99万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.4T 双离合 先锋天窗版","MinYEAR":null,"MaxYEAR":null},{"StyleId":"120292","StyleName":"1.4T 双离合 炫锋版","StyleYear":"2017","StyleNowMsrp":"14.99万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.4T 双离合 炫锋版","MinYEAR":null,"MaxYEAR":null},{"StyleId":"120293","StyleName":"1.4T 双离合 领锋版","StyleYear":"2017","StyleNowMsrp":"16.99万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.4T 双离合 领锋版","MinYEAR":null,"MaxYEAR":null},{"StyleId":"120295","StyleName":"1.5L 手动 先锋版","StyleYear":"2017","StyleNowMsrp":"10.99万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.5L 手动 先锋版","MinYEAR":null,"MaxYEAR":null},{"StyleId":"120296","StyleName":"1.5L 自动 先锋天窗版","StyleYear":"2017","StyleNowMsrp":"12.49万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.5L 自动 先锋天窗版","MinYEAR":null,"MaxYEAR":null},{"StyleId":"120297","StyleName":"1.5L 手动 炫锋版","StyleYear":"2017","StyleNowMsrp":"12.49万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.5L 手动 炫锋版","MinYEAR":null,"MaxYEAR":null},{"StyleId":"120299","StyleName":"1.5L 自动 炫锋版","StyleYear":"2017","StyleNowMsrp":"13.69万元","StyleFullName":"雪佛兰 科鲁兹三厢 2017款 1.5L 自动 炫锋版","MinYEAR":null,"MaxYEAR":null}]
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
             * StyleId : 120138
             * StyleName : 1.4T 双离合 先锋天窗版
             * StyleYear : 2017
             * StyleNowMsrp : 13.99
             * StyleFullName : 雪佛兰 科鲁兹三厢 2017款 1.4T 双离合 先锋天窗版
             * MinYEAR : null
             * MaxYEAR : null
             */

            private int StyleId;
            private String StyleName;
            private String StyleYear;
            private String StyleNowMsrp;
            private String StyleFullName;
            private int MinYEAR;
            private int MaxYEAR;
            private boolean isTitle;

            public int getStyleId() {
                return StyleId;
            }

            public void setStyleId(int StyleId) {
                this.StyleId = StyleId;
            }

            public String getStyleName() {
                return StyleName;
            }

            public void setStyleName(String StyleName) {
                this.StyleName = StyleName;
            }

            public String getStyleYear() {
                return StyleYear;
            }

            public void setStyleYear(String StyleYear) {
                this.StyleYear = StyleYear;
            }

            public String getStyleNowMsrp() {
                return StyleNowMsrp;
            }

            public void setStyleNowMsrp(String StyleNowMsrp) {
                this.StyleNowMsrp = StyleNowMsrp;
            }

            public String getStyleFullName() {
                return StyleFullName;
            }

            public void setStyleFullName(String StyleFullName) {
                this.StyleFullName = StyleFullName;
            }

            public int getMinYEAR() {
                return MinYEAR;
            }

            public void setMinYEAR(int MinYEAR) {
                this.MinYEAR = MinYEAR;
            }

            public int getMaxYEAR() {
                return MaxYEAR;
            }

            public void setMaxYEAR(int MaxYEAR) {
                this.MaxYEAR = MaxYEAR;
            }

            public boolean isTitle() {
                return isTitle;
            }

            public void setIsTitle(boolean title) {
                isTitle = title;
            }
        }
    }
}
