package com.gb.cwsup;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gb.cwsup.entity.BaseBean;
import com.gb.cwsup.getui.DemoIntentService;
import com.gb.cwsup.getui.DemoPushService;
import com.gb.cwsup.interfaces.ICallBack;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.BaseTask;
import com.gb.cwsup.utils.EncrypUtil;
import com.gb.cwsup.utils.EnumUtil;
import com.gb.cwsup.utils.ToastUtil;
import com.gb.cwsup.web.NetConnectionUtil;
import com.gb.cwsup.widget.BaseProgressDialog;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 基础Activity类 BaseActivity.java Created on: 2014-11-19 Author: Rex Yu
 * rexyu@baoxiansoft.com
 */
@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity {
	public static String SHARED_PREFERENCE_FILE;
	public static String SHARED_PREFERENCE_KEY_GESTURE_NEEDED;
	public static String SHARED_PREFERENCE_KEY_GESTURE_PASSWORD;
	public static String SHARED_PREFERENCE_KEY_GUIDED;
	public static String SHARED_PREFERENCE_KEY_IS_REMEMBER_PWD_CHECKED;
	public static String SHARED_PREFERENCE_KEY_NEW_GUANGGAO;
	public static String SHARED_PREFERENCE_KEY_USER_NAME;
	public static String SHARED_PREFERENCE_KEY_USER_PWD;
	public static String SHARED_PREFERENCE_KEY_WARN_ACCOUNT;
	public static String SHARED_PREFERENCE_KEY_WARN_HOME;
	public static String SHARED_PREFERENCE_KEY_WARN_INVEST;
	public static String SHARED_PREFERENCE_KEY_WARN_MORE_NEW_BLOCK;
	public static String SHARED_PREFERENCE_KEY_WARN_ORDER_DETAIL;
	public static String SHARED_PREFRENCES_KEY_NEW_MOREGONGGAO;
	private static BaseProgressDialog baseDialog = null;
	public static int heightPixel;
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static boolean isLoginActivityShowing = false;
	public static AlertDialog warnDialog;
	public static int widthPixel = 0;
	public static double widthScale;

	private ActionBar actionBar;
	private View mActionBarView;
	private TextView tvL, tvTitle, tvR;
	private ImageView leftimg, logo, more;

	static {
		heightPixel = 0;
		widthScale = 0.0D;
	}

	public static void baseCheckInternet(Activity paramActivity, String url, Map<String, String> params, ICallBack callBack, boolean isProgress) {

		baseCheckInternet(paramActivity, url, params, callBack, isProgress, false);
	}

	public static void baseCheckInternet(Activity paramActivity, String url, Map<String, String> params, ICallBack callBack, boolean isProgress, boolean useGet) {

		if ((paramActivity == null) || (paramActivity.isFinishing())) {
			return;
		}

		if (NetConnectionUtil.checkNetState(paramActivity, true)) {
			if (isProgress) {
				baseDialog = getBaseProgressDialog(paramActivity, null, null);
				baseDialog.show();
			}
			new BaseTask(paramActivity, url, params, callBack, baseDialog).execute(useGet);// 在这里提交到异步类中注册
		} else {
			ToastUtil.showToastShort(paramActivity, "请先链接网络...");
		}
	}

	public static void checkEnum(BaseBean paramBaseBean, Context paramContext) {
		String str = "";
		if ((paramBaseBean != null) && (paramBaseBean.getDoMsg() != null)) {
			str = paramBaseBean.getDoMsg();
			if ((str.equals("100001")) && (!isLoginActivityShowing)) {
				ToastUtil.showToastShort(paramContext, EnumUtil.getEnumResult(Integer.parseInt(paramBaseBean.getDoMsg())) + ",请重新登录");
				isLoginActivityShowing = true;
				// paramContext.startActivity(new Intent(paramContext,
				// LoginActivity.class));
			}
		}
		do {
			do {
				if ((str.equals("1000005")) && (!isLoginActivityShowing)) {
					// ToastUtil.showToastShort(paramContext,
					// EnumUtil.getEnumResult(Integer.parseInt(paramBaseBean.getDoMsg())));
					isLoginActivityShowing = true;
					// paramContext.startActivity(new Intent(paramContext,
					// LoginActivity.class));
					return;
				}
			} while (paramBaseBean.isDoStatu());
			if (paramBaseBean.getDoMsg().matches("^[0-9]{2,10}$")) {
				warnDialog = createDialog(paramContext, EnumUtil.getEnumResult(Integer.parseInt(paramBaseBean.getDoMsg())));
				warnDialog.show();
				return;
			}
		} while (paramBaseBean.getDoMsg() == null);
		warnDialog = createDialog(paramContext, paramBaseBean.getDoMsg());
		warnDialog.show();
	}

	private static AlertDialog createDialog(Context paramContext, String paramString) {
		warnDialog = new AlertDialog.Builder(paramContext).setTitle("提示消息").setMessage(paramString).setPositiveButton("确定", null).setNegativeButton("取消", null).create();
		return warnDialog;
	}

	public static BaseProgressDialog getBaseProgressDialog(Activity paramActivity, String paramString1, String paramString2) {
		baseDialog = BaseProgressDialog.createDialog(paramActivity);
		if (paramString1 != null) {
			baseDialog.setTitile(paramString1);
		}
		if (paramString2 != null) {
			baseDialog.setMessage(paramString2);
		}
		return baseDialog;
	}

	private String getImei() {
		try {
			String str = ((TelephonyManager) getSystemService("phone")).getDeviceId();
			return str;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}

	public static SharedPreferences getSharedPreference(Context paramContext) {
		return paramContext.getSharedPreferences(SHARED_PREFERENCE_FILE, 4);
	}

	private void init() {
		SHARED_PREFERENCE_FILE = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_SHAREDPREFERENCE_DATA_2.1.5");
		SHARED_PREFERENCE_KEY_GUIDED = EncrypUtil.encryptShardPreferencesKey(getImei() + "IS_GUIDED");
		SHARED_PREFERENCE_KEY_GESTURE_NEEDED = EncrypUtil.encryptShardPreferencesKey(getImei() + "IS_GESTURE_NEEDED");
		SHARED_PREFERENCE_KEY_GESTURE_PASSWORD = EncrypUtil.encryptShardPreferencesKey(getImei() + "GESTURE_PASSWORD");
		SHARED_PREFERENCE_KEY_IS_REMEMBER_PWD_CHECKED = EncrypUtil.encryptShardPreferencesKey(getImei() + "IS_REMEMBER_PWD");
		SHARED_PREFERENCE_KEY_USER_NAME = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_USER_NAME");
		SHARED_PREFERENCE_KEY_USER_PWD = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_USER_PWD");
		SHARED_PREFERENCE_KEY_WARN_HOME = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_WARN_HOME");
		SHARED_PREFERENCE_KEY_WARN_INVEST = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_WARN_INVEST");
		SHARED_PREFERENCE_KEY_WARN_MORE_NEW_BLOCK = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_WARN_NEW_BLOCK");
		SHARED_PREFERENCE_KEY_WARN_ACCOUNT = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_WARN_ACCOUNT");
		SHARED_PREFERENCE_KEY_WARN_ORDER_DETAIL = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_WARN_ORDER_DETAIL");
		SHARED_PREFERENCE_KEY_NEW_GUANGGAO = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_MORE_NEW_GONGGAO");
		SHARED_PREFRENCES_KEY_NEW_MOREGONGGAO = EncrypUtil.encryptShardPreferencesKey(getImei() + "HPD_MORE_BUTTON_NEW");
		// Display localDisplay = getWindowManager().getDefaultDisplay();
		// widthPixel = localDisplay.getWidth();
		// heightPixel = localDisplay.getHeight();
		widthScale = widthPixel / 480.0D;
		System.out.println(widthPixel + ":   :" + heightPixel);
	}

	public static void requestActivity(Activity paramActivity) {
		// paramActivity.requestWindowFeature(1);//不显示ActionBar
		// paramActivity.setRequestedOrientation(1);
		ActivityManagerUtil.getInstance().addToList(paramActivity);
	}

	protected void baseCheckInternet(Activity paramActivity, String paramString1, String paramString2, String paramString3, Map<String, String> paramMap, ICallBack paramICallBack) {
		if (NetConnectionUtil.checkNetState(paramActivity, true)) {
			baseDialog = getBaseProgressDialog(paramActivity, paramString1, paramString2);
			baseDialog.show();
			// new BaseTask(paramActivity, paramString3, paramMap,
			// paramICallBack, baseDialog).execute(new Void[0]);
			return;
		}
		ToastUtil.showToastShort(paramActivity, "请先链接网络...");
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// com.getui.demo.DemoPushService 为第三方自定义推送服务
		PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
		// com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
	}

	public void CreatActionBar() {
		actionBar = this.getActionBar();
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
		actionBar.setCustomView(mActionBarView, lp);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		initavtionview();
		requestActivity(this);
		init();
	}

	protected void onPause() {
		super.onPause();
	}

	private void initavtionview() {
		tvL = (TextView) mActionBarView.findViewById(R.id.action_left_tv);
		tvTitle = (TextView) mActionBarView.findViewById(R.id.action_title_tv);
		tvR = (TextView) mActionBarView.findViewById(R.id.action_right_tv);
		leftimg = (ImageView) findViewById(R.id.action_left_img);
		logo = (ImageView) findViewById(R.id.center_logo_img);
		more = (ImageView) findViewById(R.id.more_img);
	}

	public void settitlename(String titleLeft, String titleName, String titleRight) {
		CreatActionBar();
		if (!titleLeft.equals("") && titleLeft != null) {
			tvL.setText(titleLeft);
		} else {
			tvL.setVisibility(View.INVISIBLE);
		}

		if (!titleName.equals("") && titleName != null) {
			tvTitle.setText(titleName);
			logo.setVisibility(View.GONE);
		}

		if (!titleRight.equals("") && titleRight != null) {
			tvR.setText(titleRight);
			more.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置标题栏左侧文本单击事件
	 * 
	 * @param clickListener
	 */
	public void setLeftTvOnClick(OnClickListener clickListener) {
		tvL.setOnClickListener(clickListener);
	}

	/**
	 * 设置标题栏右侧文本单击事件
	 * 
	 * @param clickListener
	 */
	public void setRightTvOnClick(OnClickListener clickListener) {
		tvR.setOnClickListener(clickListener);
	}

	/**
	 * 设置标题栏左侧箭头动画
	 * 
	 * @param clickListener
	 */
	public void startLeftimgAnimation(Animation animation) {
		leftimg.startAnimation(animation);
	}

	/**
	 * 设置标题栏左侧图像
	 */
	public void SetLeftimg(int resId) {
		leftimg.setImageResource(resId);
	}

	/**
	 * 设置标题栏左侧图像单击事件
	 */
	public void SetLeftImgOnclick(OnClickListener onClickListener) {
		leftimg.setOnClickListener(onClickListener);
	}

	/**
	 * 显示中间logo图标
	 */
	public void SetLogo() {
		logo.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.GONE);
	}

	/**
	 * 设置标题栏右侧图像
	 */
	public void SetMoreimg(int resId) {
		more.setImageResource(resId);
	}

	/**
	 * 设置标题栏右侧图像单击事件
	 */
	public void SetMoreOnclick(OnClickListener onClickListener) {
		more.setOnClickListener(onClickListener);
	}

}
