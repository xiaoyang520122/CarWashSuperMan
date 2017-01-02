package com.gb.cwsup.entity;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**产品ID*/
	private int id;
	private String createDate;
	private String sn;
	/**产品名称*/
	private String name;
	/**产品名称全称*/
	private String fullName;
	/**产品简称*/
	private String shortName;
	/**产品说明*/
	private String nameMemo;
	/**产品价格*/
	private double price;
	/**超市价格*/
	private double marketPrice;
	/**图片*/
	private String image;
	
	private String unit;
	/**百度地图城市ID**/
	private String baiduid;
	
	private boolean isGift;
	private int hits;
	private int sales;
	/**是否预约*/
	private boolean isBooking;
	/**预约时间*/
	private Date bookBeginDate;
	/**预约结束时间*/
	private Date bookEndDate;
	
	private String[] specificationValues;
	

	public String getBaiduid() {
		return baiduid;
	}

	public void setBaiduid(String baiduid) {
		this.baiduid = baiduid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getNameMemo() {
		return nameMemo;
	}

	public void setNameMemo(String nameMemo) {
		this.nameMemo = nameMemo;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isGift() {
		return isGift;
	}

	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public boolean isBooking() {
		return isBooking;
	}

	public void setBooking(boolean isBooking) {
		this.isBooking = isBooking;
	}

	public Date getBookBeginDate() {
		return bookBeginDate;
	}

	public void setBookBeginDate(Date bookBeginDate) {
		this.bookBeginDate = bookBeginDate;
	}

	public Date getBookEndDate() {
		return bookEndDate;
	}

	public void setBookEndDate(Date bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	public String[] getSpecificationValues() {
		return specificationValues;
	}

	public void setSpecificationValues(String[] specificationValues) {
		this.specificationValues = specificationValues;
	}

	
	
}
