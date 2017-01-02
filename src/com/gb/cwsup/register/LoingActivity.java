package com.gb.cwsup.register;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.MainActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.getui.DemoIntentService;
import com.gb.cwsup.getui.DemoPushService;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;
import com.gb.cwsup.utils.RegexUtil;
import com.gb.cwsup.utils.ToastUtil;
import com.igexin.sdk.PushManager;

public class LoingActivity extends BaseActivity implements OnClickListener {

	/** 检测手机号时msg.what值 **/
	private final static int CHECK_MOBILE = 1;
	/** 设置验证码按钮倒计时字样时msg.what值 **/
	private final static int R_TIME = 2;
	/** 设置验证码按钮回复正常时msg.what值 **/
	private final static int R_RECOVER = 3;
	/** 当获取短信登陆验证码时msg.what值 **/
	private final static int GET_LOGING_CODE = 4;
	/** 验证登陆验证码时msg.what值 **/
	private final static int LOGING = JsonHttpUtils.LOGING_FLAG;

	private EditText mphonenumber, myanzhengcode;
	private TextView mqqcodetv, buildnewusertv;
	private String myzcode, mphnumber;
	private Button mLogingbut;
	private List<String> parames;
	/** 控制是否请求验证码的标记 **/
	private boolean sendflag = true;
	private LoadingDialog lodingdialog;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// com.getui.demo.DemoPushService 为第三方自定义推送服务
		PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
		// com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loging);
		isLoging();
		EventBus.getDefault().register(this);
		ActivityManagerUtil.getInstance().finishAllActivity();
		initview();
	}

	private void initview() {
		mphonenumber = (EditText) findViewById(R.id.loging_phonenumber);
		myanzhengcode = (EditText) findViewById(R.id.loging_yanzheng);
		mqqcodetv = (TextView) findViewById(R.id.loging_qingqiucode);
		buildnewusertv = (TextView) findViewById(R.id.loging_register_NewUser);
		mLogingbut = (Button) findViewById(R.id.Loging_button);
		mqqcodetv.setOnClickListener(this);
		buildnewusertv.setOnClickListener(this);
		mLogingbut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loging_qingqiucode:
			Verificationcode();
			break;

		case R.id.Loging_button:
			LogingUser();
			break;
		case R.id.loging_register_NewUser:
			intentToRegister();
			break;

		default:
			break;
		}
	}

	/**
	 * 跳转到注册
	 */
	private void intentToRegister() {
		Intent intent = new Intent(this, RegisterActivity.class);
		intent.putExtra("phonenum", mphnumber);
		startActivity(intent);
		finish();
	}

	/**
	 * 判断输入电话号码并获取请求码
	 */
	private void Verificationcode() {
		mphnumber = mphonenumber.getText().toString();
		if (!RegexUtil.isMobileNO(mphnumber)) {
			mphonenumber.setError("请输入正确的手机号码！");
			mphonenumber.requestFocus();
			ToastUtil.showToastShort(this, "请输入正确的手机号码！");
			return;
		}
		getcheckMbile();// 发送手机号检测
	}

	/**
	 * 判断验证码并注册
	 */
	@SuppressLint("NewApi")
	private void LogingUser() {
		myzcode = myanzhengcode.getText().toString();
		mphnumber = mphonenumber.getText().toString();

		if (myzcode.isEmpty()) {
			myanzhengcode.setError("请输入验证码！");
			myanzhengcode.requestFocus();
		} else {
			YanzhengLoginCODE();
		}

	}

	/**
	 * 验证登陆验证码
	 */
	private void YanzhengLoginCODE() {
		lodingdialog = new LoadingDialog(this);
		lodingdialog.setMessage("登陆中……！").show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("mobile", mphnumber));
				params.add(new BasicNameValuePair("captcha", myzcode));
				params.add(new BasicNameValuePair("cid", AppApplication.CID));
				JsonHttpUtils.doPost(URLs.POST_DX_LOGING, params, mhandler, LOGING, LoingActivity.this);
			}
		}).start();
	}

	/**
	 * 保存登陆手机号码到共享参数里面
	 * 
	 * @param phonenumber
	 */
	private void saveregisterdate(String name, String mobile) {
		SharedPreferences sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("name", name);
		editor.putString("mobile", mobile);
		editor.commit();
	}

	/**
	 * 发送手机号检测
	 * 
	 * @param phnumber
	 * @return
	 */
	private void getcheckMbile() {
		if (sendflag) {
			ToastUtil.showToastShort(this, "发送手机号检测");
			parames = new ArrayList<String>();
			parames.add("mobile");
			parames.add(mphnumber);
			parames.add("operatorType");
			parames.add("carowner");
			new Thread(new Runnable() {

				@Override
				public void run() {
					JsonHttpUtils.getHtmlString(URLs.GET_CHECK_MOBILE, parames, mhandler, CHECK_MOBILE, LoingActivity.this);
				}
			}).start();
		}
	}

	/**
	 * 发送手机号请求验证码
	 * 
	 * @param phnumber
	 * @return
	 */
	private void getYanzhengCODE() {
		if (sendflag) {
			sendflag = false;
			setfutrue();
			ToastUtil.showToastShort(this, "获取验证码中……");
			parames = new ArrayList<String>();
			parames.add("type");
			parames.add(URLs.PARAME_LOGING);
			parames.add("mobile");
			parames.add(mphnumber);
			new Thread(new Runnable() {

				@Override
				public void run() {
					JsonHttpUtils.getHtmlString(URLs.GET_DXYZ_CODE, parames, mhandler, GET_LOGING_CODE, LoingActivity.this);
				}
			}).start();
		}
	}

	/**
	 * Handler接受注册信息和请求验证码信息
	 */
	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case R_TIME:// 设置验证码按钮倒计时字样
				mqqcodetv.setText((String) msg.obj + "秒后重发");
				break;
			case R_RECOVER:// 设置验证码按钮回复正常
				mqqcodetv.setText("获取验证码");
				mqqcodetv.setBackgroundResource(R.drawable.corners_blue_button5);
				sendflag = true;
				break;
			default:
				break;
			}
		}

	};
	
	/**
	 * 验证登陆验证码是否正确，正确就登陆
	 * 
	 * @param logingMsg
	 */
	private void CheckLoging(String logingMsg) {
		lodingdialog.dismiss();// 关闭遮罩成dialog
		Log.i("requst_code", logingMsg);
		try {
			JSONObject Jsonobj = new JSONObject(logingMsg);
			JSONObject jsonObject = Jsonobj.getJSONObject("message");
			JSONObject object3 = Jsonobj.getJSONObject("data");
			if (jsonObject.getString("type").equals("success")) {
				saveregisterdate(object3.getString("name"), object3.getString("mobile"));
				Intent intent = new Intent(LoingActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				myanzhengcode.setError("验证码不正确！");
				myanzhengcode.requestFocus();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析传递过来的手机号验证信息并提示用户是注册还是换手机号登陆
	 */
	private void CheckMobile(String jsonstr) {
		try {
			JSONObject Jsonobj = new JSONObject(jsonstr);
			JSONObject jsonObject = Jsonobj.getJSONObject("message");
			if (jsonObject.getString("type").equals("success")) {
				Dialog alertDialog = new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("您输入的手机号码还没有注册成为我们的会员，请选择注册成为会员或者重新输入手机号进行登陆！")
						.setPositiveButton("注册", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// getYanzhengCODE();
								intentToRegister();
							}
						}).setNegativeButton("重输手机号", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).create();
				alertDialog.show();

			} else if (jsonObject.getString("type").equals("warn")) {
				getYanzhengCODE();
			} else {
				ToastUtil.showToastLong(this, jsonObject.getString("content"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置验证码倒计时发送
	 */
	private void setfutrue() {
		mqqcodetv.setBackgroundResource(R.drawable.corners_hui_4_5dp);
		Timer timer = new Timer();
		timer.schedule(new Task(timer), 1, 1000);
	}

	/**
	 * 自定义TimerTask实现定时并传递timer结束循环
	 * 
	 * @author lenovo
	 * 
	 */
	class Task extends TimerTask {
		private Timer timer;

		public Task(Timer timer) {
			this.timer = timer;
		}

		int i = 60;

		@Override
		public void run() {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = i + "";
			mhandler.sendMessage(msg);
			// 当执行到第60秒，timer停止,请求验证码按钮恢复正常
			if (--i == 0) {
				Message msg2 = new Message();
				msg2.what = 3;
				mhandler.sendMessage(msg2);
				this.timer.cancel();
			}
			if (sendflag) {
				this.timer.cancel();
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void logactivity(NameValuePair value) {
		int code = Integer.valueOf(value.getName());
		switch (code) {
		case CHECK_MOBILE:// 检测手机号
			CheckMobile(value.getValue());
			break;
		case LOGING:// 登陆按钮
			CheckLoging(value.getValue());
			break;

		default:
			break;
		}
	}
	/**
	 * 判断是否够登陆状态不在登陆状态就转到登陆界面
	 */
	@SuppressLint("NewApi")
	private boolean isLoging() {
		SharedPreferences sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		String code = sp.getString("mobile", "");
		if (!code.isEmpty()) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
}