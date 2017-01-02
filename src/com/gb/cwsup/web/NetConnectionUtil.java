// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.gb.cwsup.web;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.gb.cwsup.utils.CallUtil;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.ToastUtil;

public class NetConnectionUtil {

	private static class ConnectThread extends Thread {
		int index = 0;

		public void run() {
			super.run();
			try {
				do {
					Thread.sleep(1000L);
					if (NetConnectionUtil.checkNetState(NetConnectionUtil.ctx,
							false)) {
						NetConnectionUtil.handler.sendEmptyMessage(101);
						return;
					}
					this.index = (1 + this.index);
				} while (this.index <= NetConnectionUtil.maxIndex);
				NetConnectionUtil.handler.sendEmptyMessage(100);
				return;
			} catch (InterruptedException localInterruptedException) {
				localInterruptedException.printStackTrace();
				NetConnectionUtil.handler.sendEmptyMessage(100);
			}
		}
	}

	public static final String CHOOSE_INTERNET = "请选择网络";
	public static final int CONNECT_FAIL = 100;
	public static final String CONNECT_INTERNET_FIRST = "请先连接网络";
	public static final int CONNECT_SUCCESS = 101;
	public static final int MAX_INDEX = 10;
	public static final String MOBILE_INTERNET = "移动网络";
	public static final String PD_CONNECTING = "正在连接网络 ，请稍后...";
	public static final String PD_CONNECTING_TITLE = "联网中";
	public static final int SIM_INDEX = 5;
	public static final int WIFI_INDEX = 10;
	private static android.app.AlertDialog.Builder builder = null;
	private static boolean cancelAble = false;
	private static Context ctx;
	private static AlertDialog dialog = null;
	private static Handler handler = new Handler() {

		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			default:
				return;

			case 101: // 'e'
				NetConnectionUtil.pd.cancel();
				ToastUtil.showToastShort(NetConnectionUtil.ctx, "网络链接成功");
				return;

			case 100: // 'd'
				NetConnectionUtil.pd.cancel();
				ToastUtil.showToastShort(NetConnectionUtil.ctx,
						"网络链接失败，请确认是否开启链接网络权限或者手动开启网络");
				NetConnectionUtil.cancelAble = true;
				NetConnectionUtil.checkNetState(NetConnectionUtil.ctx, true);
				return;
			}
		}

	};
	private static int maxIndex = 10;
	private static CharSequence method[] = { "WIFI", "移动网络" };
	private static ProgressDialog pd = null;

	public NetConnectionUtil() {
	}

	public static boolean checkNetState(Context paramContext,
			boolean paramBoolean) {
		try {
			ctx = paramContext;
			NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
					.getSystemService("connectivity")).getActiveNetworkInfo();
			if ((localNetworkInfo != null) && (localNetworkInfo.isConnected())) {
				return true;
			}
			if (paramBoolean) {
				createDialog(paramContext);
				dialog.show();
			}
		} catch (Exception localException) {
			for (;;) {
				localException.printStackTrace();
			}
		}
		return false;
	}

	private static void createDialog(final Context context) {
		pd = DialogUtil.getProgressDialog(context, "联网中", "正在连接网络 ，请稍后...");
		builder = new AlertDialog.Builder(context);
		builder.setTitle("请选择网络");
		builder.setCancelable(cancelAble);
		builder.setSingleChoiceItems(method, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						if (CallUtil.checkSIMCardState(context, false) == null) {
							NetConnectionUtil.toggleMobileData(context, true);
							NetConnectionUtil.maxIndex = 5;
						} else {
							NetConnectionUtil.toggleWiFi(context, true);
							NetConnectionUtil.maxIndex = 10;
						}
						paramAnonymousDialogInterface.dismiss();
						NetConnectionUtil.pd.show();
						new NetConnectionUtil.ConnectThread().start();
					}
				});
		dialog = builder.create();
	}

	private static boolean toggleMobileData(Context paramContext,
			boolean paramBoolean) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");
		try {
			Field localField = Class.forName(
					localConnectivityManager.getClass().getName())
					.getDeclaredField("mService");
			localField.setAccessible(true);
			Object localObject = localField.get(localConnectivityManager);
			Class localClass = Class.forName(localObject.getClass().getName());
			Class[] arrayOfClass = new Class[1];
			arrayOfClass[0] = Boolean.TYPE;
			Method localMethod = localClass.getDeclaredMethod(
					"setMobileDataEnabled", arrayOfClass);
			localMethod.setAccessible(true);
			Object[] arrayOfObject = new Object[1];
			arrayOfObject[0] = Boolean.valueOf(paramBoolean);
			localMethod.invoke(localObject, arrayOfObject);
			return true;
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		} catch (NoSuchFieldException localNoSuchFieldException) {

			localNoSuchFieldException.printStackTrace();

		} catch (SecurityException localSecurityException) {

			localSecurityException.printStackTrace();

		} catch (NoSuchMethodException localNoSuchMethodException) {

			localNoSuchMethodException.printStackTrace();

		} catch (IllegalArgumentException localIllegalArgumentException) {

			localIllegalArgumentException.printStackTrace();

		} catch (IllegalAccessException localIllegalAccessException) {

			localIllegalAccessException.printStackTrace();

		} catch (InvocationTargetException localInvocationTargetException) {
			localInvocationTargetException.printStackTrace();
		}
		return false;
	}

	private static boolean toggleWiFi(Context context, boolean flag) {
		WifiManager wifimanager = (WifiManager) context
				.getSystemService("wifi");
		wifimanager.setWifiEnabled(flag);
		return wifimanager.isWifiEnabled();
	}

}