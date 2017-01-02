package com.gb.cwsup.entity;

import java.io.Serializable;

public class OrderItem implements Serializable {

	/**
	 * 初期第一版
	 */
	private static final long serialVersionUID = 1L;
	
	private String creatdate="";
	private String product="";
	private String price="";
	private String sn="";
	private String engineerName="";
	private String engineerID="";
	public String getCreatdate() {
		return creatdate;
	}
	public void setCreatdate(String creatdate) {
		this.creatdate = creatdate;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getEngineerName() {
		return engineerName;
	}
	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}
	public String getEngineerID() {
		return engineerID;
	}
	public void setEngineerID(String engineerID) {
		this.engineerID = engineerID;
	}
	
	

}
