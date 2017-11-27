package com.jcpt.jzg.padsystem.vo;

/**
 * Created by wujj on 2017/4/24.
 * 邮箱：wujj@jingzhengu.com
 * 作用：
 */

public class TopWarningValueBean {

    /**
     * SalePrice : {"MaxPeakValue":62.896597753267066,"MinPeakValue":21.892092104189196,"MaxWarningValue":52.41383146105589,"MinWarningValue":26.270510525027035}
     * AssessPrice : {"MaxPeakValue":57.225182923979766,"MinPeakValue":19.675373907576837,"MaxWarningValue":47.687652436649806,"MinWarningValue":23.610448689092205}
     */

    private SalePriceBean SalePrice;
    private AssessPriceBean AssessPrice;

    public SalePriceBean getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(SalePriceBean SalePrice) {
        this.SalePrice = SalePrice;
    }

    public AssessPriceBean getAssessPrice() {
        return AssessPrice;
    }

    public void setAssessPrice(AssessPriceBean AssessPrice) {
        this.AssessPrice = AssessPrice;
    }

    public static class SalePriceBean {
        /**
         * MaxPeakValue : 62.896597753267066
         * MinPeakValue : 21.892092104189196
         * MaxWarningValue : 52.41383146105589
         * MinWarningValue : 26.270510525027035
         */

        private double MaxPeakValue;
        private double MinPeakValue;
        private double MaxWarningValue;
        private double MinWarningValue;

        public double getMaxPeakValue() {
            return MaxPeakValue;
        }

        public void setMaxPeakValue(double MaxPeakValue) {
            this.MaxPeakValue = MaxPeakValue;
        }

        public double getMinPeakValue() {
            return MinPeakValue;
        }

        public void setMinPeakValue(double MinPeakValue) {
            this.MinPeakValue = MinPeakValue;
        }

        public double getMaxWarningValue() {
            return MaxWarningValue;
        }

        public void setMaxWarningValue(double MaxWarningValue) {
            this.MaxWarningValue = MaxWarningValue;
        }

        public double getMinWarningValue() {
            return MinWarningValue;
        }

        public void setMinWarningValue(double MinWarningValue) {
            this.MinWarningValue = MinWarningValue;
        }
    }

    public static class AssessPriceBean {
        /**
         * MaxPeakValue : 57.225182923979766
         * MinPeakValue : 19.675373907576837
         * MaxWarningValue : 47.687652436649806
         * MinWarningValue : 23.610448689092205
         */

        private double MaxPeakValue;
        private double MinPeakValue;
        private double MaxWarningValue;
        private double MinWarningValue;

        public double getMaxPeakValue() {
            return MaxPeakValue;
        }

        public void setMaxPeakValue(double MaxPeakValue) {
            this.MaxPeakValue = MaxPeakValue;
        }

        public double getMinPeakValue() {
            return MinPeakValue;
        }

        public void setMinPeakValue(double MinPeakValue) {
            this.MinPeakValue = MinPeakValue;
        }

        public double getMaxWarningValue() {
            return MaxWarningValue;
        }

        public void setMaxWarningValue(double MaxWarningValue) {
            this.MaxWarningValue = MaxWarningValue;
        }

        public double getMinWarningValue() {
            return MinWarningValue;
        }

        public void setMinWarningValue(double MinWarningValue) {
            this.MinWarningValue = MinWarningValue;
        }
    }
}
