package com.gb.cwsup.entity;

import java.io.Serializable;

public class LoginInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String customer_code;
	private String nice_name;
	private String user_id;

	public String getCustomer_code() {
		return this.customer_code;
	}

	public String getNice_name() {
		return this.nice_name;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setCustomer_code(String paramString) {
		this.customer_code = paramString;
	}

	public void setNice_name(String paramString) {
		this.nice_name = paramString;
	}

	public void setUser_id(String paramString) {
		this.user_id = paramString;
	}
}
