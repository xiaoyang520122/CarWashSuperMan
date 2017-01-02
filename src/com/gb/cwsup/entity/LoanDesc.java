package com.gb.cwsup.entity;

import java.io.Serializable;

public class LoanDesc implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String order_content;

	
	public LoanDesc() {
	}

	public LoanDesc(String paramString) {
		this.order_content = paramString;
	}

	public String getOrder_content() {
		return this.order_content;
	}

	public void setOrder_content(String paramString) {
		this.order_content = paramString;
	}
}
