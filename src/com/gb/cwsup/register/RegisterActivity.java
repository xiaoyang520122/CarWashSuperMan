package com.gb.cwsup.register;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.MainActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;
import com.gb.cwsup.utils.Md5Util;
import com.gb.cwsup.utils.RegexUtil;
import com.gb.cwsup.utils.ToastUtil;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private EditText mphoneEdit, mcodeEdit, mNameEdit, memailEdit;
	private TextView mgetCodetv, registerBack;
	private Button msubmitButton;
	private boolean sendflag = true;
	private List<String> parames;
	private List<NameValuePair> NVparames;
	private String mphnumber, mCode, mname, memail;
	private LoadingDialog lodingdialog;

	/** 发送手机号请求验证码 **/
	private final static int GET_CODE = 1;
	/** 短信注册验证 **/
	private final static int REGISTER_CODE = 4;
	/** 用户普通登陆 **/
	private final static int LOGING_BYPASS = JsonHttpUtils.LOGING_BY_PASS;
//	/** 用户信息提交 **/
//	private final static int UPDATE_INFO = 6;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		initview();
	}

	private void initview() {
		mphoneEdit = (EditText) findViewById(R.id.register_phone);
		mcodeEdit = (EditText) findViewById(R.id.register_code);
		mNameEdit = (EditText) findViewById(R.id.register_username);
		memailEdit = (EditText) findViewById(R.id.register_email);
		mgetCodetv = (TextView) findViewById(R.id.register_qingqiucode);
		registerBack = (TextView) findViewById(R.id.register_back);
		msubmitButton = (Button) findViewById(R.id.register_submit);
		mphoneEdit.setText(getIntent().getStringExtra("phonenum"));
		msubmitButton.setOnClickListener(this);
		mgetCodetv.setOnClickListener(this);
		registerBack.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case GET_CODE:
					isSendSuccess((String) msg.obj);
					break;
				case 2:
					mgetCodetv.setText((String) msg.obj + "秒后重发");
					break;
				case 3:
					mgetCodetv.setText("获取验证码");
					mgetCodetv.setBackgroundResource(R.drawable.corners_blue_button5);
					sendflag = true;
					break;
				case REGISTER_CODE:
					isregistersuccess((String) msg.obj);
					break;
				case LOGING_BYPASS:
					isLogingSuccess((String) msg.obj);
					break;

				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void isLogingSuccess(String obj) {
			try {
				JSONObject object1 = new JSONObject(obj);
				JSONObject object2 = object1.getJSONObject("message");
				JSONObject object3 = object1.getJSONObject("data");
				if (object2.getString("type").equals("success")) {
					Log.i("LONGING", "Loging用户登陆成功！");
					saveregisterdate(object3.getString("name"),object3.getString("mobile"));
					RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, MainActivity.class));
					finish();
				} else {
					ToastUtil.showToastShort(RegisterActivity.this, "注册失败！");
					Log.i("LONGING", "Loging用户登陆失败！"+object2.getString("content"));
					sendflag = true;
				}
			} catch (JSONException e) {
				ToastUtil.showToastShort(RegisterActivity.this, "注册失败！");
				Log.i("LONGING", "Loging用户登陆shibai ！解析错误");
				sendflag = true;
				e.printStackTrace();
			}
		}


		/**
		 * 是否注册成功
		 * 
		 * @param obj
		 */
		private void isregistersuccess(String obj) {
			lodingdialog.dismiss();//关闭遮罩成dialog

			try {
				JSONObject object1 = new JSONObject(obj);
				JSONObject object2 = object1.getJSONObject("message");
				if (object2.getString("type").equals("success")) {
					Log.i("LONGING", "注册成功，开始系统自动登陆，密码123456");
					Loginguser();
				} else {
					Log.i("LONGING", "register注册失败："+object2.getString("content"));
					ToastUtil.showToastShort(RegisterActivity.this,object2.getString("content"));
					mphoneEdit.setError(object2.getString("content"));
					mphoneEdit.setFocusable(true);
					sendflag = true;
				}
			} catch (JSONException e) {
				ToastUtil.showToastShort(RegisterActivity.this, "注册失败！");
				Log.i("LONGING", "register注册失败，解析问题");
				sendflag = true;
				e.printStackTrace();
			}
		}

		private void Loginguser() {
			NVparames = new ArrayList<NameValuePair>(2);
			NVparames.add(new BasicNameValuePair("username", mphnumber));
			NVparames.add(new BasicNameValuePair("enPassword", Md5Util.MD5("123456")));
			NVparames.add(new BasicNameValuePair("cid",AppApplication.CID));
			new Thread() {
				@Override
				public void run() {
					super.run();
					JsonHttpUtils.doPost(URLs.LOGING_BY_PASS, NVparames, mhandler, LOGING_BYPASS, RegisterActivity.this);
				}
			}.start();
		}


		/**
		 * 检测验证码发送是否成功
		 * 
		 * @param obj
		 */
		private void isSendSuccess(String obj) {
			try {
				JSONObject object1 = new JSONObject(obj);
				JSONObject object2 = object1.getJSONObject("message");
				if (object2.getString("type").equals("success")) {
					ToastUtil.showToastShort(RegisterActivity.this, "验证码成功发送！");
				} else {
					ToastUtil.showToastShort(RegisterActivity.this, "验证发送失败！");
					Log.i("LONGING", "Send验证码请求发送成功，但是验证码发送失败");
					sendflag = true;
				}
			} catch (JSONException e) {
				ToastUtil.showToastShort(RegisterActivity.this, "验证发送失败！");
				Log.i("LONGING", "Send验证码请求发送成功，但是验证码发送失败 解析错误");
				sendflag = true;
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_qingqiucode:
			getYanzhengCODE();
			break;
		case R.id.register_submit:
			submitinfo();
			break;
		case R.id.register_back:
			startActivity(new Intent(this, LoingActivity.class));
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 发送注册信息
	 */
	private void submitinfo() {
		lodingdialog=new LoadingDialog(this);
		lodingdialog.setMessage("注册中……").show();
		
		mphnumber = mphoneEdit.getText().toString();
		mCode = mcodeEdit.getText().toString();
		mname = mNameEdit.getText().toString();
		memail = memailEdit.getText().toString();
		if (isinfoEmpty()) {
			NVparames = new ArrayList<NameValuePair>(2);
			NVparames.add(new BasicNameValuePair("mobile", mphnumber));
			NVparames.add(new BasicNameValuePair("captcha", mCode));
			NVparames.add(new BasicNameValuePair("password", "123456"));
			NVparames.add(new BasicNameValuePair("name", mname));
			NVparames.add(new BasicNameValuePair("email", memailEdit.getText().toString()));
			NVparames.add(new BasicNameValuePair("operatorType", "carowner"));
			new Thread() {
				@Override
				public void run() {
					super.run();
					JsonHttpUtils.doPost(URLs.POST_DX_REGISTER, NVparames, mhandler, REGISTER_CODE, RegisterActivity.this);
				}
			}.start();
		}
	}

	@SuppressLint("NewApi")
	private boolean isinfoEmpty() {
		if (mphnumber.isEmpty()) {
			mphoneEdit.setError("手机号码不能为空！");
			mphoneEdit.setFocusable(true);
			return false;
		}

		if (mCode.isEmpty()) {
			mcodeEdit.setError("验证码不能为空！");
			mcodeEdit.setFocusable(true);
			return false;
		}
		if (mname.isEmpty()) {
			mNameEdit.setError("用户名不能为空！");
			mNameEdit.setFocusable(true);
			return false;
		}
		if (memail.isEmpty()) {
			memailEdit.setError("邮箱不能为空！");
			memailEdit.setFocusable(true);
			return false;
		}
		return true;
	}

	/**
	 * 发送手机号请求验证码
	 * 
	 * @param phnumber
	 * @return
	 */
	private void getYanzhengCODE() {
		if (sendflag && Verificationcode()) {
			sendflag = false;
			setfutrue();
			parames = new ArrayList<String>();
			parames.add("type");
			parames.add(URLs.PARAME_REGISTER);
			parames.add("mobile");
			parames.add(mphnumber);
			new Thread(new Runnable() {
				@Override
				public void run() {
					JsonHttpUtils.getHtmlString(URLs.GET_DXYZ_CODE, parames, mhandler, GET_CODE, RegisterActivity.this);
				}
			}).start();
		}
	}

	/**
	 * 判断输入电话号码并获取请求码
	 */
	private boolean Verificationcode() {
		mphnumber = mphoneEdit.getText().toString();
		if (!RegexUtil.isMobileNO(mphnumber)) {
			mphoneEdit.setError("请输入正确的手机号码！");
			mphoneEdit.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * 设置验证码倒计时发送
	 */
	private void setfutrue() {
		mgetCodetv.setBackgroundResource(R.drawable.corners_hui_4_5dp);
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
				sendmsg();
			}
			if (sendflag) {
				this.timer.cancel();
				sendmsg();
			}
		}

		private void sendmsg() {
			Message msg2 = new Message();
			msg2.what = 3;
			mhandler.sendMessage(msg2);
			this.timer.cancel();
		}
	}
	/**
	 * 保存登陆手机号码到共享参数里面
	 * @param phonenumber
	 */
		private void saveregisterdate(String name,String mobile) {
			SharedPreferences sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("name", name);
			editor.putString("mobile", mobile);
			editor.commit();
		}
}
