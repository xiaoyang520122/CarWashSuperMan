package com.gb.cwsup.entity;

import java.io.Serializable;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String doMsg;
	private String doObject;
	private boolean doStatu;

	public String getDoMsg() {
		return this.doMsg;
	}

	public String getDoObject() {
		return this.doObject;
	}

	public boolean isDoStatu() {
		return this.doStatu;
	}

	public void setDoMsg(String paramString) {
		this.doMsg = paramString;
	}

	public void setDoObject(String paramString) {
		this.doObject = paramString;
	}

	public void setDoStatu(boolean paramBoolean) {
		this.doStatu = paramBoolean;
	}
}
