package com.gb.cwsup.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.gb.cwsup.R;

/**
 * 打电话
 *
 * CallUtil.java
 *
 *  Created on: 2014-11-21
 *      Author: Rex Yu  rexyu@baoxiansoft.com
 */
public class CallUtil {
	public static final String CALL_BUTTON_CALCEL = "取消";
	public static final String CALL_BUTTON_OK = "咨询";
	public static final String CALL_MESSAGE = "客服工作时间：每日8:00~22:00";
	public static final String CALL_METHOD_CALL = "android.intent.action.CALL";
	public static final String CALL_METHOD_WAIT = "android.intent.action.DIAL";
	private static final String CALL_NUMBER = "4006121121";
	public static final String CALL_TITLE = "客服中心";

	/*
	 * SIM的状态信息： SIM_STATE_UNKNOWN 未知状态 0 SIM_STATE_ABSENT 没插卡 1
	 * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2 SIM_STATE_PUK_REQUIRED
	 * 锁定状态，需要用户的PUK码解锁 3 SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
	 * SIM_STATE_READY 就绪状态 5
	 */
	public static String checkSIMCardState(Context context, boolean makeCall) {
		String msg = null;
		switch (((TelephonyManager) context.getSystemService("phone"))
				.getSimState()) {
		case TelephonyManager.SIM_STATE_READY:
			break;
		case TelephonyManager.SIM_STATE_ABSENT:
			msg = "没有SIM卡或SIM卡不可用，请插入SIM卡后再进行操作";
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			msg = "SIM卡状态:锁定状态，需要用户的PIN码解锁";
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			msg = "SIM卡状态:锁定状态，需要用户的PUK码解";
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			msg = "SIM卡状态未知，请检查您的SIM卡后再进行操作";
			break;
		default:
			msg = "SIM卡状态未知，请检查您的SIM卡后再进行操作";
			break;
		}
		if (msg != null) {
			ToastUtil.showToastLong(context, msg);
		} else if (makeCall) {
			makeCall(context);
			msg = null;

		}
		return msg;
	}

	private static void makeCall(Context context) {
		context.startActivity(new Intent("android.intent.action.DIAL", Uri
				.parse("tel:" + context.getString(R.string.about_tel))));
	}
}
