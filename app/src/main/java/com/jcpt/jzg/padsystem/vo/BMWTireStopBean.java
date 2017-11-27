package com.jcpt.jzg.padsystem.vo;

/**
 * Created by wujj on 2017/11/2.
 * 邮箱：wujj@jingzhengu.com
 * 作用：轮胎轮毂bean
 */

public class BMWTireStopBean {
    private String LTSC_TyreLeftAnterior;
    private String LTSC_TyreLeftAfter;
    private String LTSC_TyreRightFront;
    private String LTSC_TyreRightAfter;

    //胎纹深度
    private String TyreDepthLeftFront;
    private String TyreDepthRightFront;
    private String TyreDepthLeftAfter;
    private String TyreDepthRightAfter;
    private String TyreDepthSpareTire;//备胎

    private int noSpareTire;

    public int getNoSpareTire() {
        return noSpareTire;
    }

    public void setNoSpareTire(int noSpareTire) {
        this.noSpareTire = noSpareTire;
    }

    public String getTyreDepthLeftFront() {
        return TyreDepthLeftFront;
    }

    public void setTyreDepthLeftFront(String tyreDepthLeftFront) {
        TyreDepthLeftFront = tyreDepthLeftFront;
    }

    public String getTyreDepthRightFront() {
        return TyreDepthRightFront;
    }

    public void setTyreDepthRightFront(String tyreDepthRightFront) {
        TyreDepthRightFront = tyreDepthRightFront;
    }

    public String getTyreDepthLeftAfter() {
        return TyreDepthLeftAfter;
    }

    public void setTyreDepthLeftAfter(String tyreDepthLeftAfter) {
        TyreDepthLeftAfter = tyreDepthLeftAfter;
    }

    public String getTyreDepthRightAfter() {
        return TyreDepthRightAfter;
    }

    public void setTyreDepthRightAfter(String tyreDepthRightAfter) {
        TyreDepthRightAfter = tyreDepthRightAfter;
    }

    public String getTyreDepthSpareTire() {
        return TyreDepthSpareTire;
    }

    public void setTyreDepthSpareTire(String tyreDepthSpareTire) {
        TyreDepthSpareTire = tyreDepthSpareTire;
    }

    private int LTSC_TyreYear;
    private int LTSC_TyreStandard;
    private int LTSC_TyreTread;
    private int LTSC_TyrePressure;
    private int LTSC_TyreDecorate;
    private int LTSC_TyreSeason;
    private int LTSC_TyreOldAndNew;

    public String getLTSC_TyreLeftAnterior() {
        return LTSC_TyreLeftAnterior;
    }

    public void setLTSC_TyreLeftAnterior(String LTSC_TyreLeftAnterior) {
        this.LTSC_TyreLeftAnterior = LTSC_TyreLeftAnterior;
    }

    public String getLTSC_TyreLeftAfter() {
        return LTSC_TyreLeftAfter;
    }

    public void setLTSC_TyreLeftAfter(String LTSC_TyreLeftAfter) {
        this.LTSC_TyreLeftAfter = LTSC_TyreLeftAfter;
    }

    public String getLTSC_TyreRightFront() {
        return LTSC_TyreRightFront;
    }

    public void setLTSC_TyreRightFront(String LTSC_TyreRightFront) {
        this.LTSC_TyreRightFront = LTSC_TyreRightFront;
    }

    public String getLTSC_TyreRightAfter() {
        return LTSC_TyreRightAfter;
    }

    public void setLTSC_TyreRightAfter(String LTSC_TyreRightAfter) {
        this.LTSC_TyreRightAfter = LTSC_TyreRightAfter;
    }

    public int getLTSC_TyreYear() {
        return LTSC_TyreYear;
    }

    public void setLTSC_TyreYear(int LTSC_TyreYear) {
        this.LTSC_TyreYear = LTSC_TyreYear;
    }

    public int getLTSC_TyreStandard() {
        return LTSC_TyreStandard;
    }

    public void setLTSC_TyreStandard(int LTSC_TyreStandard) {
        this.LTSC_TyreStandard = LTSC_TyreStandard;
    }

    public int getLTSC_TyreTread() {
        return LTSC_TyreTread;
    }

    public void setLTSC_TyreTread(int LTSC_TyreTread) {
        this.LTSC_TyreTread = LTSC_TyreTread;
    }

    public int getLTSC_TyrePressure() {
        return LTSC_TyrePressure;
    }

    public void setLTSC_TyrePressure(int LTSC_TyrePressure) {
        this.LTSC_TyrePressure = LTSC_TyrePressure;
    }

    public int getLTSC_TyreDecorate() {
        return LTSC_TyreDecorate;
    }

    public void setLTSC_TyreDecorate(int LTSC_TyreDecorate) {
        this.LTSC_TyreDecorate = LTSC_TyreDecorate;
    }

    public int getLTSC_TyreSeason() {
        return LTSC_TyreSeason;
    }

    public void setLTSC_TyreSeason(int LTSC_TyreSeason) {
        this.LTSC_TyreSeason = LTSC_TyreSeason;
    }

    public int getLTSC_TyreOldAndNew() {
        return LTSC_TyreOldAndNew;
    }

    public void setLTSC_TyreOldAndNew(int LTSC_TyreOldAndNew) {
        this.LTSC_TyreOldAndNew = LTSC_TyreOldAndNew;
    }

    //左前品牌型号年份选中的位置
    private int leftFrontTyreBrandPos;
    private int leftFrontTyreDiameterPos;
    private int leftFrontTyreFlatPos;
    private int leftFrontTyreWidePos;
    private int leftFrontTyreYearPos;
    //右前品牌型号年份选中的位置
    private int rightFrontTyreBrandPos;
    private int rightFrontTyreDiameterPos;
    private int rightFrontTyreFlatPos;
    private int rightFrontTyreWidePos;
    private int rightFrontTyreYearPos;
    //左后品牌型号年份选中的位置
    private int leftAfterTyreBrandPos;
    private int leftAfterTyreDiameterPos;
    private int leftAfterTyreFlatPos;
    private int leftAfterTyreWidePos;
    private int leftAfterTyreYearPos;
    //右后品牌型号年份选中的位置
    private int rightAfterTyreBrandPos;
    private int rightAfterTyreDiameterPos;
    private int rightAfterTyreFlatPos;
    private int rightAfterTyreWidePos;
    private int rightAfterTyreYearPos;

    public int getLeftFrontTyreBrandPos() {
        return leftFrontTyreBrandPos;
    }

    public void setLeftFrontTyreBrandPos(int leftFrontTyreBrandPos) {
        this.leftFrontTyreBrandPos = leftFrontTyreBrandPos;
    }

    public int getLeftFrontTyreDiameterPos() {
        return leftFrontTyreDiameterPos;
    }

    public void setLeftFrontTyreDiameterPos(int leftFrontTyreDiameterPos) {
        this.leftFrontTyreDiameterPos = leftFrontTyreDiameterPos;
    }

    public int getLeftFrontTyreFlatPos() {
        return leftFrontTyreFlatPos;
    }

    public void setLeftFrontTyreFlatPos(int leftFrontTyreFlatPos) {
        this.leftFrontTyreFlatPos = leftFrontTyreFlatPos;
    }

    public int getLeftFrontTyreWidePos() {
        return leftFrontTyreWidePos;
    }

    public void setLeftFrontTyreWidePos(int leftFrontTyreWidePos) {
        this.leftFrontTyreWidePos = leftFrontTyreWidePos;
    }

    public int getLeftFrontTyreYearPos() {
        return leftFrontTyreYearPos;
    }

    public void setLeftFrontTyreYearPos(int leftFrontTyreYearPos) {
        this.leftFrontTyreYearPos = leftFrontTyreYearPos;
    }

    public int getRightFrontTyreBrandPos() {
        return rightFrontTyreBrandPos;
    }

    public void setRightFrontTyreBrandPos(int rightFrontTyreBrandPos) {
        this.rightFrontTyreBrandPos = rightFrontTyreBrandPos;
    }

    public int getRightFrontTyreDiameterPos() {
        return rightFrontTyreDiameterPos;
    }

    public void setRightFrontTyreDiameterPos(int rightFrontTyreDiameterPos) {
        this.rightFrontTyreDiameterPos = rightFrontTyreDiameterPos;
    }

    public int getRightFrontTyreFlatPos() {
        return rightFrontTyreFlatPos;
    }

    public void setRightFrontTyreFlatPos(int rightFrontTyreFlatPos) {
        this.rightFrontTyreFlatPos = rightFrontTyreFlatPos;
    }

    public int getRightFrontTyreWidePos() {
        return rightFrontTyreWidePos;
    }

    public void setRightFrontTyreWidePos(int rightFrontTyreWidePos) {
        this.rightFrontTyreWidePos = rightFrontTyreWidePos;
    }

    public int getRightFrontTyreYearPos() {
        return rightFrontTyreYearPos;
    }

    public void setRightFrontTyreYearPos(int rightFrontTyreYearPos) {
        this.rightFrontTyreYearPos = rightFrontTyreYearPos;
    }

    public int getLeftAfterTyreBrandPos() {
        return leftAfterTyreBrandPos;
    }

    public void setLeftAfterTyreBrandPos(int leftAfterTyreBrandPos) {
        this.leftAfterTyreBrandPos = leftAfterTyreBrandPos;
    }

    public int getLeftAfterTyreDiameterPos() {
        return leftAfterTyreDiameterPos;
    }

    public void setLeftAfterTyreDiameterPos(int leftAfterTyreDiameterPos) {
        this.leftAfterTyreDiameterPos = leftAfterTyreDiameterPos;
    }

    public int getLeftAfterTyreFlatPos() {
        return leftAfterTyreFlatPos;
    }

    public void setLeftAfterTyreFlatPos(int leftAfterTyreFlatPos) {
        this.leftAfterTyreFlatPos = leftAfterTyreFlatPos;
    }

    public int getLeftAfterTyreWidePos() {
        return leftAfterTyreWidePos;
    }

    public void setLeftAfterTyreWidePos(int leftAfterTyreWidePos) {
        this.leftAfterTyreWidePos = leftAfterTyreWidePos;
    }

    public int getLeftAfterTyreYearPos() {
        return leftAfterTyreYearPos;
    }

    public void setLeftAfterTyreYearPos(int leftAfterTyreYearPos) {
        this.leftAfterTyreYearPos = leftAfterTyreYearPos;
    }

    public int getRightAfterTyreBrandPos() {
        return rightAfterTyreBrandPos;
    }

    public void setRightAfterTyreBrandPos(int rightAfterTyreBrandPos) {
        this.rightAfterTyreBrandPos = rightAfterTyreBrandPos;
    }

    public int getRightAfterTyreDiameterPos() {
        return rightAfterTyreDiameterPos;
    }

    public void setRightAfterTyreDiameterPos(int rightAfterTyreDiameterPos) {
        this.rightAfterTyreDiameterPos = rightAfterTyreDiameterPos;
    }

    public int getRightAfterTyreFlatPos() {
        return rightAfterTyreFlatPos;
    }

    public void setRightAfterTyreFlatPos(int rightAfterTyreFlatPos) {
        this.rightAfterTyreFlatPos = rightAfterTyreFlatPos;
    }

    public int getRightAfterTyreWidePos() {
        return rightAfterTyreWidePos;
    }

    public void setRightAfterTyreWidePos(int rightAfterTyreWidePos) {
        this.rightAfterTyreWidePos = rightAfterTyreWidePos;
    }

    public int getRightAfterTyreYearPos() {
        return rightAfterTyreYearPos;
    }

    public void setRightAfterTyreYearPos(int rightAfterTyreYearPos) {
        this.rightAfterTyreYearPos = rightAfterTyreYearPos;
    }

    private String tyreBrandLeftFront = "";//品牌
    private String tyreDiameterLeftFront = "";//直径
    private String tyreFlatLeftFront = "";//扁平比
    private String tyreWideLeftFront = "";//胎宽
    private String tyreYearLeftFront = "";//年份

    private String tyreBrandRightFront = "";
    private String tyreDiameterRightFront = "";
    private String tyreFlatRightFront = "";
    private String tyreWideRightFront = "";
    private String tyreYearRightFront = "";

    private String tyreBrandLeftAfter = "";
    private String tyreDiameterLeftAfter = "";
    private String tyreFlatLeftAfter = "";
    private String tyreWideLeftAfter = "";
    private String tyreYearLeftAfter = "";

    private String tyreBrandRightAfter = "";
    private String tyreDiameterRightAfter = "";
    private String tyreFlatRightAfter = "";
    private String tyreWideRightAfter = "";
    private String tyreYearRightAfter = "";

    public String getTyreBrandLeftFront() {
        return tyreBrandLeftFront;
    }

    public void setTyreBrandLeftFront(String tyreBrandLeftFront) {
        this.tyreBrandLeftFront = tyreBrandLeftFront;
    }

    public String getTyreDiameterLeftFront() {
        return tyreDiameterLeftFront;
    }

    public void setTyreDiameterLeftFront(String tyreDiameterLeftFront) {
        this.tyreDiameterLeftFront = tyreDiameterLeftFront;
    }

    public String getTyreFlatLeftFront() {
        return tyreFlatLeftFront;
    }

    public void setTyreFlatLeftFront(String tyreFlatLeftFront) {
        this.tyreFlatLeftFront = tyreFlatLeftFront;
    }

    public String getTyreWideLeftFront() {
        return tyreWideLeftFront;
    }

    public void setTyreWideLeftFront(String tyreWideLeftFront) {
        this.tyreWideLeftFront = tyreWideLeftFront;
    }

    public String getTyreYearLeftFront() {
        return tyreYearLeftFront;
    }

    public void setTyreYearLeftFront(String tyreYearLeftFront) {
        this.tyreYearLeftFront = tyreYearLeftFront;
    }

    public String getTyreBrandRightFront() {
        return tyreBrandRightFront;
    }

    public void setTyreBrandRightFront(String tyreBrandRightFront) {
        this.tyreBrandRightFront = tyreBrandRightFront;
    }

    public String getTyreDiameterRightFront() {
        return tyreDiameterRightFront;
    }

    public void setTyreDiameterRightFront(String tyreDiameterRightFront) {
        this.tyreDiameterRightFront = tyreDiameterRightFront;
    }

    public String getTyreFlatRightFront() {
        return tyreFlatRightFront;
    }

    public void setTyreFlatRightFront(String tyreFlatRightFront) {
        this.tyreFlatRightFront = tyreFlatRightFront;
    }

    public String getTyreWideRightFront() {
        return tyreWideRightFront;
    }

    public void setTyreWideRightFront(String tyreWideRightFront) {
        this.tyreWideRightFront = tyreWideRightFront;
    }

    public String getTyreYearRightFront() {
        return tyreYearRightFront;
    }

    public void setTyreYearRightFront(String tyreYearRightFront) {
        this.tyreYearRightFront = tyreYearRightFront;
    }

    public String getTyreBrandLeftAfter() {
        return tyreBrandLeftAfter;
    }

    public void setTyreBrandLeftAfter(String tyreBrandLeftAfter) {
        this.tyreBrandLeftAfter = tyreBrandLeftAfter;
    }

    public String getTyreDiameterLeftAfter() {
        return tyreDiameterLeftAfter;
    }

    public void setTyreDiameterLeftAfter(String tyreDiameterLeftAfter) {
        this.tyreDiameterLeftAfter = tyreDiameterLeftAfter;
    }

    public String getTyreFlatLeftAfter() {
        return tyreFlatLeftAfter;
    }

    public void setTyreFlatLeftAfter(String tyreFlatLeftAfter) {
        this.tyreFlatLeftAfter = tyreFlatLeftAfter;
    }

    public String getTyreWideLeftAfter() {
        return tyreWideLeftAfter;
    }

    public void setTyreWideLeftAfter(String tyreWideLeftAfter) {
        this.tyreWideLeftAfter = tyreWideLeftAfter;
    }

    public String getTyreYearLeftAfter() {
        return tyreYearLeftAfter;
    }

    public void setTyreYearLeftAfter(String tyreYearLeftAfter) {
        this.tyreYearLeftAfter = tyreYearLeftAfter;
    }

    public String getTyreBrandRightAfter() {
        return tyreBrandRightAfter;
    }

    public void setTyreBrandRightAfter(String tyreBrandRightAfter) {
        this.tyreBrandRightAfter = tyreBrandRightAfter;
    }

    public String getTyreDiameterRightAfter() {
        return tyreDiameterRightAfter;
    }

    public void setTyreDiameterRightAfter(String tyreDiameterRightAfter) {
        this.tyreDiameterRightAfter = tyreDiameterRightAfter;
    }

    public String getTyreFlatRightAfter() {
        return tyreFlatRightAfter;
    }

    public void setTyreFlatRightAfter(String tyreFlatRightAfter) {
        this.tyreFlatRightAfter = tyreFlatRightAfter;
    }

    public String getTyreWideRightAfter() {
        return tyreWideRightAfter;
    }

    public void setTyreWideRightAfter(String tyreWideRightAfter) {
        this.tyreWideRightAfter = tyreWideRightAfter;
    }

    public String getTyreYearRightAfter() {
        return tyreYearRightAfter;
    }

    public void setTyreYearRightAfter(String tyreYearRightAfter) {
        this.tyreYearRightAfter = tyreYearRightAfter;
    }
}
