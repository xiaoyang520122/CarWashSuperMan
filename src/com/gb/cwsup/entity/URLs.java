package com.gb.cwsup.entity;

import java.io.Serializable;

/**
 * 接口URL实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21-
 */
public class URLs implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final static String HOST = "http://202.170.139.223:8044/app";
	/**发送验证码**/
	public final static String GET_DXYZ_CODE = HOST + "/common/send_sms.jhtml";//发送验证码
	/**手机号检测**/
	public final static String GET_CHECK_MOBILE = HOST + "/register/check_mobile.jhtml";//手机号检测
	/**用户短信登陆验证**/
	public final static String POST_DX_LOGING = HOST + "/login/sms.jhtml";//用户短信登陆验证
	/**用户普通登陆**/
	public final static String LOGING_BY_PASS = HOST + "/login/submit.jhtml";//用户普通登陆验证
	/**用户短信注册**/
	public final static String POST_DX_REGISTER = HOST + "/register/submit_mobile.jhtml";//用户短信注册验证
	/**获取服务人员信息**/
	public final static String GET_ENGINEERS ="http://202.170.139.223:8044/wap/engineer/get_engineers.jhtml";//http://202.170.139.223:8044/wap/engineer/get_engineers.jhtml?latitude=22.557473&longitude=114.019824&radius=3
	/**获取产品信息**/
	public final static String GET_PRODUCT_LIST=HOST+"/product/list.jhtml?orderType=priceAsc";
	/**获取优惠券及订单信息**/
	public final static String GET_COUPON_LIST=HOST+"/member/order/info.jhtml";
	/**发送订单产品ID**/
	public final static String SEND_PRODUCT_ID=HOST+"/cart/add.jhtml";
	/**清空购物车**/
	public final static String CLEAR_CART=HOST+"/cart/clear.jhtml";
	/**订单金额计算**/
	public final static String GET_ORDER_MONEY=HOST+"/member/order/calculate.jhtml";
	/**生成订单**/
	public final static String CREAT_ORDER=HOST+"/member/order/create.jhtml";
	/**订单取消**/
	public final static String CANCEL_ORDER=HOST+"/member/order/cancel.jhtml";
	/**修改用户信息 用户名 性别 邮箱等**/
	public final static String UPDATE_USER_INFO=HOST+"/member/profile/update.jhtml";
	/** Box Host **/
	public final static String BOXHOST = "http://139.199.15.243";
	/**通过订单号开箱**/
	public final static String OPEN_BY_ORDER_ID = BOXHOST + "/box/service/firstopen";//发送验证码
	/**上传开password boxnodeng 等信息**/
	public final static String UPDATE_OPEN_RESPONSE = HOST + "/member/order/save_box_info.jhtml";
	/**订单列表**/
	public final static String GET_ORDER_LIST = HOST + "/member/order/list.jhtml";
	/**请求订单详情**/
	public final static String GET_ORDER_INFORMATION = HOST + "/member/order/view.jhtml";
	/**保存地址**/
	public final static String SAVE_ADDRESS = HOST + "/member/receiver/save.jhtml";
	/**地址信息列表**/
	public final static String ADDRESS_LIST = HOST + "/member/receiver/list.jhtml";
	/**更新地址信息**/
	public final static String UPDATE_ADDRESS = HOST + "/member/receiver/update.jhtml";
	/**删除地址信息**/
	public final static String DELETE_ADDRESS = HOST + "/member/receiver/delete.jhtml";
	/**车辆信息列表**/
	public final static String CAR_LIST = HOST + "/member/cars/list.jhtml";
	/**保存车辆信息**/
	public final static String SAVE_CAR = HOST + "/member/cars/save.jhtml";
	/**删除车辆信息**/
	public final static String DELETE_CAR = HOST + "/member/cars/delete.jhtml";
	/**更新车辆信息**/
	public final static String UPDATE_CAR = HOST + "/member/cars/update.jhtml";
	/**发送透传信息**/
	public final static String POST_PAYLOAD = HOST + "/common/push_for_mobile_touchuang.jhtml";
	
	
	
	
	/**http://202.170.139.223:8044/wap/engineer/get_engineers.jhtml?latitude=22.557473&longitude=114.019824&radius=3
	/**
	 * 验证码类型： 
	 * 会员登录:memberLogin, 
	 * 会员注册:memberRegister, 
	 * 后台登录:adminLogin, 
	 * 找回密码:findPassword,
	 * 重置密码 :resetPassword,
	 * 修改手机号 :modifyMobile,
	 * 其它:other
	 */
	public final static String  PARAME_REGISTER="memberRegister";//请求验证码类型-注册
	public final static String  PARAME_LOGING="memberLogin";//请求验证码类型-登陆

	// public final static String HOST =
	// "carlife.online4s.com:7071";//online4s.baoxiansoft.com:7008";//192.168.1.213
	// www.oschina.net
	// public final static String HTTP = "http://";
	// public final static String HTTPS = "https://";
	//
	// private final static String URL_SPLITTER = "/";
	// private final static String URL_UNDERLINE = "_";
	//
	// private final static String URL_SUFFIX = ".jhtml";
	//
	// public final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	//
	// public final static String API_TOKEN = URL_API_HOST +
	// "wap/login/check"+URL_SUFFIX;
	// public final static String LOGIN_HTTP = URL_API_HOST +
	// "wap/login/submit"+URL_SUFFIX;
	//
	// public final static String GET_ENGINEERS = URL_API_HOST +
	// "wap/engineer/get_engineers"+URL_SUFFIX;
	// public final static String GET_ENGINEER = URL_API_HOST +
	// "wap/engineer/get_engineer"+URL_SUFFIX;

	public final static int URL_OBJ_TYPE_OTHER = 0x000;
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_SOFTWARE = 0x002;
	public final static int URL_OBJ_TYPE_QUESTION = 0x003;
	public final static int URL_OBJ_TYPE_ZONE = 0x004;
	public final static int URL_OBJ_TYPE_BLOG = 0x005;
	public final static int URL_OBJ_TYPE_TWEET = 0x006;
	public final static int URL_OBJ_TYPE_QUESTION_TAG = 0x007;

}
