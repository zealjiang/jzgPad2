/**
 * Project Name:JingZhenGu
 * File Name:Model.java
 * Package Name:com.gc.jingzhengu.vo
 * Date:2014-6-13上午11:34:30
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 *
 */

package com.jcpt.jzg.padsystem.vo;

import android.os.Parcel;
import android.os.Parcelable;


public class Model implements Parcelable
{
	/**
	 * 车系id
	 */
	private int Id;

	/**
	 * 车系名称
	 */
	private String Name;

	/**
	 * 厂商名称
	 */
	private String ManufacturerName;

	/**
	 * 图片路径
	 */
	private String modelimgpath;

	/**
	 * 字体颜色
	 */
	private int fontColor;

	/**
	 * item点击颜色
	 */
	private int itemColor;

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getItemColor() {
		return itemColor;
	}

	public void setItemColor(int itemColor) {
		this.itemColor = itemColor;
	}

	public String getManufacturerName() {
		return ManufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		ManufacturerName = manufacturerName;
	}

	public String getModelimgpath() {
		return modelimgpath;
	}

	public void setModelimgpath(String modelimgpath) {
		this.modelimgpath = modelimgpath;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	@Override
	public String toString() {
		return "Model{" +
				"fontColor=" + fontColor +
				", Id=" + Id +
				", Name='" + Name + '\'' +
				", ManufacturerName='" + ManufacturerName + '\'' +
				", modelimgpath='" + modelimgpath + '\'' +
				", itemColor=" + itemColor +
				'}';
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.Id);
		dest.writeString(this.Name);
		dest.writeString(this.ManufacturerName);
		dest.writeString(this.modelimgpath);
		dest.writeInt(this.fontColor);
		dest.writeInt(this.itemColor);
	}

	public Model() {
	}

	protected Model(Parcel in) {
		this.Id = in.readInt();
		this.Name = in.readString();
		this.ManufacturerName = in.readString();
		this.modelimgpath = in.readString();
		this.fontColor = in.readInt();
		this.itemColor = in.readInt();
	}

	public static final Creator<Model> CREATOR = new Creator<Model>() {
		public Model createFromParcel(Parcel source) {
			return new Model(source);
		}

		public Model[] newArray(int size) {
			return new Model[size];
		}
	};
}
