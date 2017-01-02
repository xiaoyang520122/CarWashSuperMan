package com.gb.cwsup.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class DialogUtil {
	public static AlertDialog getAlertDialog(Context paramContext, String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		localBuilder.setTitle("提示信息");
		localBuilder.setMessage(paramString);
		localBuilder.setPositiveButton("确定", null);
		localBuilder.setNegativeButton("取消", null);
		AlertDialog localAlertDialog = localBuilder.create();
		localAlertDialog.setCancelable(true);
		localAlertDialog.setCanceledOnTouchOutside(true);
		return localAlertDialog;
	}
	public static AlertDialog getAlertDialogOneBut(Context paramContext, String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		localBuilder.setTitle("提示信息");
		localBuilder.setMessage(paramString);
		localBuilder.setPositiveButton("确定", null);
		AlertDialog localAlertDialog = localBuilder.create();
		localAlertDialog.setCancelable(true);
		localAlertDialog.setCanceledOnTouchOutside(true);
		return localAlertDialog;
	}

	public static AlertDialog getInvestDialot(Context paramContext) {
		View localView = LayoutInflater.from(paramContext).inflate(2130903147, null);
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		localBuilder.setView(localView);
		AlertDialog localAlertDialog = localBuilder.create();
		localAlertDialog.setCanceledOnTouchOutside(false);
		return localAlertDialog;
	}

	public static ProgressDialog getProgressDialog(Context paramContext, String paramString1, String paramString2) {
		ProgressDialog localProgressDialog = new ProgressDialog(paramContext);
		localProgressDialog.setTitle(paramString1);
		localProgressDialog.setMessage(paramString2);
		localProgressDialog.setCancelable(false);
		return localProgressDialog;
	}
}
