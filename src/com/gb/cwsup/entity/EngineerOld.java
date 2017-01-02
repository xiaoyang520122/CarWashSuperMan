/*
 * Copyright 2005-2013 online4s.com. All rights reserved.
 * Support: http://www.online4s.com
 * License: http://www.online4s.com/license
 */
package com.gb.cwsup.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 技师
 * 
 * @author ONLINE4S Team
 * @version 3.0
 */

public class EngineerOld implements Serializable {

	private static final long serialVersionUID = 1533130686714725835L;

	/**
	 * 性别
	 */
	public enum Gender {

		/** 男 */
		male,

		/** 女 */
		female
	}

	/** "用户名"Cookie名称 */
	public static final String USERNAME_COOKIE_NAME = "username";

	/** 会员注册项值属性个数 */
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;

	/** 会员注册项值属性名称前缀 */
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	/** 最大收藏商品数 */
	public static final int MAX_FAVORITE_COUNT = 10;
	private Long id;

	/** 用户名 */

	private String username;

	/** E-mail */

	private String email;

	/** 积分 */

	private Long point;

	/** 消费金额 */
	private BigDecimal amount;

	/** 余额 */
	private BigDecimal balance;

	/** 是否启用 */

	private boolean isEnabled;

	/** 是否锁定 */

	private boolean isLocked;

	/** 连续登录失败次数 */
	private int loginFailureCount;

	/** 锁定日期 */
	private Date lockedDate;

	/** 注册IP */
	private String registerIp;

	/** 最后登录IP */

	private String loginIp;

	/** 最后登录日期 */

	private Date loginDate;

	/** 姓名 */

	private String name;

	/** 性别 */

	private Gender gender;

	/** 出生日期 */

	private Date birth;

	/** 地址 */

	private String address;

	/** 邮编 */

	private String zipCode;

	/** 电话 */
	private String phone;

	/** 手机 */

	private String mobile;

	/** 会员注册项值0 */
	private String attributeValue0;

	/** 会员注册项值1 */
	private String attributeValue1;

	/** 会员注册项值2 */
	private String attributeValue2;

	/** 会员注册项值3 */
	private String attributeValue3;

	/** 会员注册项值4 */
	private String attributeValue4;

	/** 会员注册项值5 */
	private String attributeValue5;

	/** 会员注册项值6 */
	private String attributeValue6;

	/** 会员注册项值7 */
	private String attributeValue7;

	/** 会员注册项值8 */
	private String attributeValue8;

	/** 会员注册项值9 */
	private String attributeValue9;

	/** 地区 */

	// private Area area;

	/** 会员等级 */

	// private EngineerRank engineerRank;

	/** 工号 */

	private String no;
	/** 纬度 */

	private double latitude;
	/** 经度 */

	private double longitude;
	/** 服务次数 */

	private int serviceTimes;
	/** 从业经验 */

	private Short experience;
	/** 籍贯 */

	private String hometown;
	/** 简历 */

	private String resume;
	/** 上次在线时间 */

	private Date lastOnline;
	/** 考试成绩 */

	private Short exams;
	/** 证件类型 */
	private int idCard;
	/** 证件号码 */
	private String idCardNo;
	/** 证件照片 */
	private String idCardImg;
	/** 是否实名认证 */

	private boolean isVerified;
	/** 投诉次数 */

	private int complaintsTimes;
	/** 所属机构 */

	private String organization;
	/** 常驻区域 */

	private String permanentArea;
	/** 上门费 */

	private double feeHome;
	/** 工时费 */

	private double feeTime;

	private boolean isBusy;
	private boolean isMaster;
	private boolean isCrown;
	private double score;
	private String skillCar;

	private String headBig;
	private String headMedium;
	private String headSmall;

	private int complaintsCount;
	private int orderCount;
	private int favoriteCount;
	

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取E-mail
	 * 
	 * @return E-mail
	 */

	public String getEmail() {
		return email;
	}

	/**
	 * 设置E-mail
	 * 
	 * @param email
	 *            E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取积分
	 * 
	 * @return 积分
	 */

	public Long getPoint() {
		return point;
	}

	/**
	 * 设置积分
	 * 
	 * @param point
	 *            积分
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取消费金额
	 * 
	 * @return 消费金额
	 */

	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置消费金额
	 * 
	 * @param amount
	 *            消费金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取余额
	 * 
	 * @return 余额
	 */

	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置余额
	 * 
	 * @param balance
	 *            余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */

	public boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取是否锁定
	 * 
	 * @return 是否锁定
	 */

	public boolean getIsLocked() {
		return isLocked;
	}

	/**
	 * 设置是否锁定
	 * 
	 * @param isLocked
	 *            是否锁定
	 */
	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * 获取连续登录失败次数
	 * 
	 * @return 连续登录失败次数
	 */

	public int getLoginFailureCount() {
		return loginFailureCount;
	}

	/**
	 * 设置连续登录失败次数
	 * 
	 * @param loginFailureCount
	 *            连续登录失败次数
	 */
	public void setLoginFailureCount(int loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	/**
	 * 获取锁定日期
	 * 
	 * @return 锁定日期
	 */
	public Date getLockedDate() {
		return lockedDate;
	}

	/**
	 * 设置锁定日期
	 * 
	 * @param lockedDate
	 *            锁定日期
	 */
	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	/**
	 * 获取注册IP
	 * 
	 * @return 注册IP
	 */

	public String getRegisterIp() {
		return registerIp;
	}

	/**
	 * 设置注册IP
	 * 
	 * @param registerIp
	 *            注册IP
	 */
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	/**
	 * 获取最后登录IP
	 * 
	 * @return 最后登录IP
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * 设置最后登录IP
	 * 
	 * @param loginIp
	 *            最后登录IP
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	/**
	 * 获取最后登录日期
	 * 
	 * @return 最后登录日期
	 */
	public Date getLoginDate() {
		return loginDate;
	}

	/**
	 * 设置最后登录日期
	 * 
	 * @param loginDate
	 *            最后登录日期
	 */
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	/**
	 * 获取姓名
	 * 
	 * @return 姓名
	 */

	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 * 
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取性别
	 * 
	 * @return 性别
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * 设置性别
	 * 
	 * @param gender
	 *            性别
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * 获取出生日期
	 * 
	 * @return 出生日期
	 */
	public Date getBirth() {
		return birth;
	}

	/**
	 * 设置出生日期
	 * 
	 * @param birth
	 *            出生日期
	 */
	public void setBirth(Date birth) {
		this.birth = birth;
	}

	/**
	 * 获取地址
	 * 
	 * @return 地址
	 */

	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * 
	 * @param address
	 *            地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * 
	 * @return 邮编
	 */

	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * 
	 * @param zipCode
	 *            邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * 
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * 
	 * @param phone
	 *            电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取手机
	 * 
	 * @return 手机
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机
	 * 
	 * @param mobile
	 *            手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取会员注册项值0
	 * 
	 * @return 会员注册项值0
	 */
	public String getAttributeValue0() {
		return attributeValue0;
	}

	/**
	 * 设置会员注册项值0
	 * 
	 * @param attributeValue0
	 *            会员注册项值0
	 */
	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	/**
	 * 获取会员注册项值1
	 * 
	 * @return 会员注册项值1
	 */
	public String getAttributeValue1() {
		return attributeValue1;
	}

	/**
	 * 设置会员注册项值1
	 * 
	 * @param attributeValue1
	 *            会员注册项值1
	 */
	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	/**
	 * 获取会员注册项值2
	 * 
	 * @return 会员注册项值2
	 */

	public String getAttributeValue2() {
		return attributeValue2;
	}

	/**
	 * 设置会员注册项值2
	 * 
	 * @param attributeValue2
	 *            会员注册项值2
	 */
	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	/**
	 * 获取会员注册项值3
	 * 
	 * @return 会员注册项值3
	 */

	public String getAttributeValue3() {
		return attributeValue3;
	}

	/**
	 * 设置会员注册项值3
	 * 
	 * @param attributeValue3
	 *            会员注册项值3
	 */
	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	/**
	 * 获取会员注册项值4
	 * 
	 * @return 会员注册项值4
	 */

	public String getAttributeValue4() {
		return attributeValue4;
	}

	/**
	 * 设置会员注册项值4
	 * 
	 * @param attributeValue4
	 *            会员注册项值4
	 */
	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	/**
	 * 获取会员注册项值5
	 * 
	 * @return 会员注册项值5
	 */

	public String getAttributeValue5() {
		return attributeValue5;
	}

	/**
	 * 设置会员注册项值5
	 * 
	 * @param attributeValue5
	 *            会员注册项值5
	 */
	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	/**
	 * 获取会员注册项值6
	 * 
	 * @return 会员注册项值6
	 */

	public String getAttributeValue6() {
		return attributeValue6;
	}

	/**
	 * 设置会员注册项值6
	 * 
	 * @param attributeValue6
	 *            会员注册项值6
	 */
	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	/**
	 * 获取会员注册项值7
	 * 
	 * @return 会员注册项值7
	 */

	public String getAttributeValue7() {
		return attributeValue7;
	}

	/**
	 * 设置会员注册项值7
	 * 
	 * @param attributeValue7
	 *            会员注册项值7
	 */
	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	/**
	 * 获取会员注册项值8
	 * 
	 * @return 会员注册项值8
	 */

	public String getAttributeValue8() {
		return attributeValue8;
	}

	/**
	 * 设置会员注册项值8
	 * 
	 * @param attributeValue8
	 *            会员注册项值8
	 */
	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	/**
	 * 获取会员注册项值9
	 * 
	 * @return 会员注册项值9
	 */

	public String getAttributeValue9() {
		return attributeValue9;
	}

	/**
	 * 设置会员注册项值9
	 * 
	 * @param attributeValue9
	 *            会员注册项值9
	 */
	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	public String getNo() {
		return this.no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getServiceTimes() {
		return this.serviceTimes;
	}

	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}

	public Short getExperience() {
		return this.experience;
	}

	public void setExperience(Short experience) {
		this.experience = experience;
	}

	public String getHometown() {
		return this.hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getResume() {
		return this.resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public Date getLastOnline() {
		return this.lastOnline;
	}

	public void setLastOnline(Date lastOnline) {
		this.lastOnline = lastOnline;
	}

	public Short getExams() {
		return this.exams;
	}

	public void setExams(Short exams) {
		this.exams = exams;
	}

	public int getIdCard() {
		return this.idCard;
	}

	public void setIdCard(Short idCard) {
		this.idCard = idCard;
	}

	public String getIdCardNo() {
		return this.idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public boolean getIsVerified() {
		return this.isVerified;
	}

	public void setIsVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public int getComplaintsTimes() {
		return this.complaintsTimes;
	}

	public void setComplaintsTimes(int complaintsTimes) {
		this.complaintsTimes = complaintsTimes;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPermanentArea() {
		return this.permanentArea;
	}

	public void setPermanentArea(String permanentArea) {
		this.permanentArea = permanentArea;
	}

	public double getFeeHome() {
		return this.feeHome;
	}

	public void setFeeHome(double feeHome) {
		this.feeHome = feeHome;
	}

	public double getFeeTime() {
		return this.feeTime;
	}

	public void setFeeTime(double feeTime) {
		this.feeTime = feeTime;
	}

	public String getIdCardImg() {
		return this.idCardImg;
	}

	public void setIdCardImg(String idCardImg) {
		this.idCardImg = idCardImg;
	}

	public boolean getIsBusy() {
		return this.isBusy;
	}

	public void setIsBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean getIsMaster() {
		return this.isMaster;
	}

	public void setIsMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public boolean getIsCrown() {
		return this.isCrown;
	}

	public void setIsCrown(boolean isCrown) {
		this.isCrown = isCrown;
	}

	public double getScore() {
		return this.score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getSkillCar() {
		return this.skillCar;
	}

	public void setSkillCar(String skillCar) {
		this.skillCar = skillCar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHeadBig() {
		return headBig;
	}

	public void setHeadBig(String headBig) {
		this.headBig = headBig;
	}

	public String getHeadMedium() {
		return headMedium;
	}

	public void setHeadMedium(String headMedium) {
		this.headMedium = headMedium;
	}

	public String getHeadSmall() {
		return headSmall;
	}

	public void setHeadSmall(String headSmall) {
		this.headSmall = headSmall;
	}

	public void setIdCard(int idCard) {
		this.idCard = idCard;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public void setCrown(boolean isCrown) {
		this.isCrown = isCrown;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getComplaintsCount() {
		return complaintsCount;
	}

	public void setComplaintsCount(int complaintsCount) {
		this.complaintsCount = complaintsCount;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}


}