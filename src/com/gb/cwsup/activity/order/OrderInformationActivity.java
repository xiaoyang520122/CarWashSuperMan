package com.gb.cwsup.activity.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.MipcaActivityCapture;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.CallUtils;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;

public class OrderInformationActivity extends BaseActivity implements OnClickListener {

	private TextView createDatetv, productnametv, subtotaltv, engNametv, engAddtv, openboxtv,carAddresstv,carInfotv;
	private List<NameValuePair> params;
	private Handler mHandler = new Handler();
	private LoadingDialog loadingDialog;
	private String password,boxNo, boxChildNo;
	private final static int SCANNIN_GREQUEST_CODE = 200;
	private List<NameValuePair> Nparams;
	private Thread mThread;
	private Intent intent;

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "订单详情", "");
		super.onCreate(paramBundle);
		setContentView(R.layout.order_information);
		ActivityManagerUtil.getInstance().addToList(this);
		EventBus.getDefault().register(this);
		initview();
	}

	private void initview() {
		createDatetv = (TextView) findViewById(R.id.orderinfo_creattime);
		productnametv = (TextView) findViewById(R.id.orderinfo_product);
		subtotaltv = (TextView) findViewById(R.id.orderinfo_Total);
		engNametv = (TextView) findViewById(R.id.orderinfo_engineer_Name);
		engAddtv = (TextView) findViewById(R.id.orderinfo_engineer_ADD);
		carAddresstv = (TextView) findViewById(R.id.orderinfo_carAddress);
		carInfotv = (TextView) findViewById(R.id.orderinfo_carinfo);
		findViewById(R.id.orderinfo_call_eng).setOnClickListener(this);
		findViewById(R.id.orderinfo_call_serve).setOnClickListener(this);
		findViewById(R.id.orderinfo_delete).setOnClickListener(this);
		openboxtv = (TextView) findViewById(R.id.orderinfo_getPassword);
		setonclick();
		initdata();
	}

	private void setonclick() {
		setLeftTvOnClick(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_left_tv: // BACK
			finish();
			break;
		case R.id.orderinfo_call_eng: // CALL ENGINEER
			callengineer();
			break;
		case R.id.orderinfo_call_serve: // call server person
			callserve();
			break;
		case R.id.orderinfo_delete: // delete order
			deleteorder();
			break;
		case R.id.orderinfo_getPassword: // get open box password  or to scan
			openOrScan();
			break;

		default:
			break;
		}

	}

	private void openOrScan() {
		if (TextUtils.isEmpty(password) || password.equals("null")) {
			Intent insureintent = new Intent(OrderInformationActivity.this, MipcaActivityCapture.class);
			insureintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(insureintent, SCANNIN_GREQUEST_CODE);
		} else {
			DialogUtil.getAlertDialogOneBut(this, String.format(getString(R.string.open_password_for_dialog), password)).show();
		}
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		switch (requestcode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultcode == RESULT_OK) {
				startWait();
				Bundle bundle = data.getExtras();
				password=bundle.getString("result");
				openbox(password);
//				dilogmsg(String.format(getString(R.string.open_password_for_dialog), password));
			}
			break;
		default:
			break;
		}
	}

	private void startWait() {
		Intent intent = new Intent(this, WaitingOpenActivity.class);
		startActivity(intent);
	}

	/**
	 * 请求open BOX
	 * 
	 * @param box_no
	 */
	private void openbox(final String box_no) {
		Nparams = new ArrayList<NameValuePair>(2);
		Nparams.add(new BasicNameValuePair("access_token", "4029e598-adf4-4595-80e4-096c68abd2ff"));
		Nparams.add(new BasicNameValuePair("box_no", box_no));
		Nparams.add(new BasicNameValuePair("order_id", intent.getStringExtra("ordernumber")));

		mThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("requst_code", "URL信息：" + URLs.OPEN_BY_ORDER_ID);
				Log.i("requst_code", "box_no：" + box_no);
				JsonHttpUtils.doPost(URLs.OPEN_BY_ORDER_ID, Nparams, mHandler, JsonHttpUtils.OPEN_BOX, OrderInformationActivity.this);
			}
		});
		mThread.start();
	}

	/**
	 * open Box 是否成功
	 * 
	 * @param obj
	 */
	private void isopen(String obj) {
		Log.i("requst_code", "网络信息：" + obj);
		try {
			if (obj != null) {
				JSONObject object = new JSONObject((String) obj);
				if (object.getString("result_code").equals("200")) {
					DialogUtil.getAlertDialogOneBut(this,"开箱成功！").show();
					saveopenmsg(object.getJSONObject("data"));
				} else if (object.getString("result_code").equals("104")) {
					DialogUtil.getAlertDialogOneBut(this,"请求超时！").show();
				} else {
					DialogUtil.getAlertDialogOneBut(this,"开箱失败！").show();
				}
				EventBus.getDefault().post(true);// 通知等待页面关闭
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void saveopenmsg(JSONObject jsondata) {
		try {
			Nparams = new ArrayList<NameValuePair>(2);
			Nparams.add(new BasicNameValuePair("sn", intent.getStringExtra("ordernumber")));
			Nparams.add(new BasicNameValuePair("boxNo", jsondata.getString("box_no")));
			Nparams.add(new BasicNameValuePair("boxChildNo", jsondata.getString("box_child_no")));
			Nparams.add(new BasicNameValuePair("keyOwner", jsondata.getString("key_owner")));
			Nparams.add(new BasicNameValuePair("keyService", jsondata.getString("key_service")));
			Nparams.add(new BasicNameValuePair("boxChildStatus", "1"));
			new Thread() {
				@Override
				public void run() {
					super.run();
					JsonHttpUtils.doPost(URLs.UPDATE_OPEN_RESPONSE, Nparams, mHandler, JsonHttpUtils.UPDATE_OPEN_RESPONSE_MSG, OrderInformationActivity.this);
				}
			}.start();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void deleteorder() {
		loadingDialog.setMessage("加载中……").show();
		params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("sn", getIntent().getStringExtra("sn")));
		new Thread() {
			@Override
			public void run() {
				super.run();
				JsonHttpUtils.doPost(URLs.CANCEL_ORDER, params, mHandler, JsonHttpUtils.CANCEL_ORDER, OrderInformationActivity.this);
			}
		}.start();
	}

	private void callserve() {
		CallUtils.call(this, getString(R.string.serve_tel));
	}

	private void callengineer() {

		if (isLoginActivityShowing) {

		}
		// CallUtils.call(this, phone);

	}

	private void initdata() {
		loadingDialog = new LoadingDialog(OrderInformationActivity.this);
		loadingDialog.setMessage("加载中……").show();
		params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("sn", getIntent().getStringExtra("sn")));
		new Thread() {
			@Override
			public void run() {
				super.run();
				JsonHttpUtils.doPost(URLs.GET_ORDER_INFORMATION, params, mHandler, JsonHttpUtils.GET_ORDER_INFORMATION, OrderInformationActivity.this);
			}
		}.start();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void responseinfo(NameValuePair value) {
		int code = Integer.valueOf(value.getName());
		switch (code) {

		case JsonHttpUtils.GET_ORDER_INFORMATION:
			jiexiedata(value.getValue());
			break;

		case JsonHttpUtils.CANCEL_ORDER:
			jiexiecancelmsg(value.getValue());
			break;
		case JsonHttpUtils.OPEN_BOX:
			isopen(value.getValue());
			break;
		case JsonHttpUtils.UPDATE_OPEN_RESPONSE_MSG:
			isupdatesuccess(value.getValue());
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param value
	 */
		private void isupdatesuccess(String value) {
			try {
				JSONObject object1=new JSONObject(value);
				if (object1.getString("type").equals("success")) {
//					ToastUtil.showToastShort(this, "上传数据成功！");
					initdata();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}

	private void jiexiecancelmsg(String value) {
		loadingDialog.dismiss();
		try {
			JSONObject jo1 = new JSONObject(value);
			JSONObject jsmsg = jo1.getJSONObject("message");
			AlertDialog dilog = DialogUtil.getAlertDialog(this, jsmsg.getString("content"));
			if (jsmsg.getString("type").equals("success")) {
				setdismissListener(dilog);
			}
			dilog.show();
		} catch (JSONException e) {
			showerrormsg();
			e.printStackTrace();
		}
	}

	private void setdismissListener(AlertDialog dilog) {
		dilog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				OrderInformationActivity.this.finish();
			}
		});

	}

	private void jiexiedata(String value) {
		loadingDialog.dismiss();
		try {
			JSONObject jo1 = new JSONObject(value);
			JSONObject jsmsg = jo1.getJSONObject("message");
			if (jsmsg.getString("type").equals("success")) {
				JSONObject jsdata = jo1.getJSONObject("data");
				setviewdata(jsdata);
			} else {
				showerrormsg();
			}
		} catch (JSONException e) {
			showerrormsg();
			e.printStackTrace();
		}
	}

	private void showerrormsg() {
		new AlertDialog.Builder(this).setTitle("提示").setMessage("操作失败！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				OrderInformationActivity.this.finish();
			}
		}).create().show();
	}

	/**
	 * y 年 M 月 d 日 h 时 在上午或下午 (1~12) H 时 在一天中 (0~23) m 分
	 * 
	 * @param jsitem
	 */
	@SuppressLint("SimpleDateFormat")
	private void setviewdata(JSONObject jsdata) {
		try {
			JSONObject jseng;
			try {
				jseng = jsdata.getJSONObject("engineer");
				String engName = jseng.getString("name");
				engNametv.setText(String.format(engNametv.getText().toString(), engName));
				setADDonclick(jseng.getString("latitude"), jseng.getString("longitude"));
				// engAddtv.setText(jseng.getString("name"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONArray jsorderItems = jsdata.getJSONArray("orderItems");
			JSONObject jsitem = jsorderItems.getJSONObject(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			long lonmil = Long.valueOf(jsitem.getString("createDate"));
			createDatetv.setText(sdf.format(new Date(lonmil)));
			productnametv.setText(jsitem.getString("name"));
			subtotaltv.setText("￥" + jsitem.getString("subtotal"));
			boxNo  = optString(jsdata, "boxNo");
			boxChildNo = optString(jsdata,"boxChildNo");
			password = optString(jsdata,"keyOwner");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(password) || password.equals("null")) {
			openboxtv.setText(getString(R.string.scan_open));
		} else {
			openboxtv.setText(getString(R.string.get_open_password));
		}
		openboxtv.setOnClickListener(this);
	}
	
	public static String optString(JSONObject json, String key)  
	{  
	    if (json.isNull(key))  
	        return null;  
	    else  
	        return json.optString(key, null);  
	}

	private void setADDonclick(String string, String string2) {
		/** 地图显示实时位置信息 **/

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
