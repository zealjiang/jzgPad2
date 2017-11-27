package com.jcpt.jzg.padsystem.vo;

import java.io.Serializable;

public class SelfCarInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SetGroupID;
	private String StyleID;// 车型
	private String isNet;// "0"代表网络，"1"代表自建
	private String CityID;
	private String ColorName;
	private String LikeTel;
	private String Des;
	private String LikeAddr;
	private String SourceID;
	private String Color;

	private String LikeMan;
	private String SourceName;
	private String CarLicense;
	private String CreateTime;
	private String Id;
	private String userID;
	private String VIN;
	private String StyleName;
	private String ModleName;
	private String ModleId;
	private String MakeName;
	private String MakeID;
	private String AppraiseBackNum;//大于0的是退回的
	private String Status;// Status 返回值 待评估 或已确认
	private String IfOwn;// IfOwn 返回值 0：本人确认的 1.其他人确认的
	private String isShenHe;//0:未提交，1：已提交待审核
	private String AppraiseBackReason;// 退回车辆原因
	private String productType;//产品类型

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getAppraiseBackReason() {
		return AppraiseBackReason;
	}

	public void setAppraiseBackReason(String appraiseBackReason) {
		AppraiseBackReason = appraiseBackReason;
	}

	public String getIsShenHe() {
		return isShenHe;
	}

	public void setIsShenHe(String isShenHe) {
		this.isShenHe = isShenHe;
	}

	public String getIfOwn() {
		return IfOwn;
	}

	public void setIfOwn(String ifOwn) {
		IfOwn = ifOwn;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getAppraiseBackNum() {
		return AppraiseBackNum;
	}

	public void setAppraiseBackNum(String appraiseBackNum) {
		AppraiseBackNum = appraiseBackNum;
	}

	public String getStyleName() {
		return StyleName;
	}

	public void setStyleName(String styleName) {
		StyleName = styleName;
	}

	// public String getStyleId() {
	// return StyleId;
	// }
	//
	// public void setStyleId(String styleId) {
	// StyleId = styleId;
	// }

	public String getModleName() {
		return ModleName;
	}

	public void setModleName(String modleName) {
		ModleName = modleName;
	}

	public String getModleId() {
		return ModleId;
	}

	public void setModleId(String modleId) {
		ModleId = modleId;
	}

	public String getMakeID() {
		return MakeID;
	}

	public void setMakeID(String makeID) {
		MakeID = makeID;
	}

	public String getMakeName() {
		return MakeName;
	}

	public void setMakeName(String makeName) {
		MakeName = makeName;
	}

	public String getVIN() {
		return VIN;
	}

	public void setVIN(String vIN) {
		VIN = vIN;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getSetGroupID() {
		return SetGroupID;
	}

	public void setSetGroupID(String setGroupID) {
		SetGroupID = setGroupID;
	}

	public SelfCarInfo(String productType, String AppraiseBackReason, String isShenhe, String IfOwn, String status, String AppraiseBackNum, String StyleName, String ModleName, String ModleId,
					   String MakeName, String MakeId, String VIN, String userID,
					   String SetGroupID, String styleID, String isNet, String cityID,
					   String colorName, String likeTel, String des, String likeAddr,
					   String sourceID, String color, String likeMan, String sourceName,
					   String carLicense, String createTime, String id) {
		super();
		this.productType = productType;
		this.AppraiseBackReason = AppraiseBackReason;
		this.isShenHe = isShenhe;
		this.IfOwn = IfOwn;
		this.Status = status;
		this.AppraiseBackNum = AppraiseBackNum;
		this.StyleName = StyleName;
		this.ModleName = ModleName;
		this.ModleId = ModleId;
		this.MakeName = MakeName;
		this.MakeID = MakeId;
		this.VIN = VIN;
		this.userID = userID;
		this.SetGroupID = SetGroupID;
		this.StyleID = styleID;
		this.isNet = isNet;
		CityID = cityID;
		ColorName = colorName;
		LikeTel = likeTel;
		Des = des;
		LikeAddr = likeAddr;
		SourceID = sourceID;
		Color = color;
		LikeMan = likeMan;
		SourceName = sourceName;
		CarLicense = carLicense;
		CreateTime = createTime;
		Id = id;
	}

	public SelfCarInfo() {
		super();
	}

	public String getIsNet() {
		return isNet;
	}

	public void setIsNet(String isNet) {
		this.isNet = isNet;
	}

	public String getStyleID() {
		return StyleID;
	}

	public void setStyleID(String styleID) {
		StyleID = styleID;
	}

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public String getColorName() {
		return ColorName;
	}

	public void setColorName(String colorName) {
		ColorName = colorName;
	}

	public String getLikeTel() {
		return LikeTel;
	}

	public void setLikeTel(String likeTel) {
		LikeTel = likeTel;
	}

	public String getDes() {
		return Des;
	}

	public void setDes(String des) {
		Des = des;
	}

	public String getLikeAddr() {
		return LikeAddr;
	}

	public void setLikeAddr(String likeAddr) {
		LikeAddr = likeAddr;
	}

	public String getSourceID() {
		return SourceID;
	}

	public void setSourceID(String sourceID) {
		SourceID = sourceID;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getLikeMan() {
		return LikeMan;
	}

	public void setLikeMan(String likeMan) {
		LikeMan = likeMan;
	}

	public String getSourceName() {
		return SourceName;
	}

	public void setSourceName(String sourceName) {
		SourceName = sourceName;
	}

	public String getCarLicense() {
		return CarLicense;
	}

	public void setCarLicense(String carLicense) {
		CarLicense = carLicense;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	@Override
	public String toString() {
		return "SelfCarInfo [StyleID=" + StyleID + ", isNet=" + isNet
				+ ", CityID=" + CityID + ", ColorName=" + ColorName
				+ ", LikeTel=" + LikeTel + ", Des=" + Des + ", LikeAddr="
				+ LikeAddr + ", SourceID=" + SourceID + ", Color=" + Color
				+ ", LikeMan=" + LikeMan + ", SourceName=" + SourceName
				+ ", CarLicense=" + CarLicense + ", CreateTime=" + CreateTime
				+ ", Id=" + Id + "]";
	}

}
