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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment.SavedState;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.MipcaActivityCapture;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.ToastUtil;

public class SuccessPayOrder extends BaseActivity implements OnClickListener {

	private TextView scanopenTv, totaltv, productNametv, orderNumbertv, creatTimetv,suretv;
	private final static int SCANNIN_GREQUEST_CODE = 200;
	private List<NameValuePair> Nparams;
	private Thread mThread;
	private Handler mHandler;
	private Intent intent;
	private boolean openflag=false;

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("", "支付成功", "完成");
		super.onCreate(paramBundle);
		setContentView(R.layout.success_pay_order);
		ActivityManagerUtil.getInstance().addToList(this);
		EventBus.getDefault().register(this);
		initview();
	}

	private void initview() {
		intent=getIntent();
		setRightTvOnClick(this);
		mHandler = new Handler();
		scanopenTv = (TextView) findViewById(R.id.scan_open);
		productNametv = (TextView) findViewById(R.id.successpay_productName);
		orderNumbertv = (TextView) findViewById(R.id.successpay_prderNumber);
		creatTimetv = (TextView) findViewById(R.id.successpay_creatTime);
		totaltv = (TextView) findViewById(R.id.success_Total);
		suretv = (TextView) findViewById(R.id.successpay_sure);
		suretv.setOnClickListener(this);
		scanopenTv.setOnClickListener(this);
		setTVinfo();
	}

	@SuppressLint("SimpleDateFormat")
	private void setTVinfo() {
		totaltv.setText(intent.getStringExtra("Total"));
		productNametv.setText(intent.getStringExtra("productname"));
		orderNumbertv.setText(intent.getStringExtra("ordernumber"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		creatTimetv.setText(sdf.format(new Date()));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scan_open:
			startscan();// 开始扫码
			break;
		case R.id.action_right_tv:
			toorderinfomation();
			break;

		case R.id.successpay_sure:
			toorderinfomation();
			break;

		default:
			break;
		}
	}
	
	private void toorderinfomation(){
		Intent intent = new Intent(this, OrderInformationActivity.class);
		intent.putExtra("sn", this.intent.getStringExtra("ordernumber"));
		startActivity(intent);
		finish();
	}

	/**
	 * 开始扫码
	 */
	private void startscan() {
		if (openflag) {
			dilogmsg("您已经开过箱了！");
			return;
		}
		Intent insureintent = new Intent(SuccessPayOrder.this, MipcaActivityCapture.class);
		insureintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(insureintent, SCANNIN_GREQUEST_CODE);
	}

	/**
	 * 接收扫描结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				startWait();
				Bundle bundle = data.getExtras();
				openbox(bundle.getString("result"));
			}
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
		Nparams.add(new BasicNameValuePair("order_id",intent.getStringExtra("ordernumber")));

		mThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("requst_code", "URL信息：" + URLs.OPEN_BY_ORDER_ID);
				Log.i("requst_code", "box_no：" + box_no);
				JsonHttpUtils.doPost(URLs.OPEN_BY_ORDER_ID, Nparams, mHandler, JsonHttpUtils.OPEN_BOX, SuccessPayOrder.this);
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
					dilogmsg("开箱成功！");
					openflag=true ;
					saveopenmsg(object.getJSONObject("data"));
				} else if (object.getString("result_code").equals("104")) {
					dilogmsg("请求超时！");
				} else {
					dilogmsg("开箱失败！");
				}
				EventBus.getDefault().post(true);// 通知等待页面关闭
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	private void saveopenmsg(JSONObject jsondata){
		try {
			Nparams = new ArrayList<NameValuePair>(2);
			Nparams.add(new BasicNameValuePair("sn",intent.getStringExtra("ordernumber")));
			Nparams.add(new BasicNameValuePair("boxNo",jsondata.getString("box_no")));
			Nparams.add(new BasicNameValuePair("boxChildNo",jsondata.getString("box_child_no")));
			Nparams.add(new BasicNameValuePair("keyOwner",jsondata.getString("key_owner")));
			Nparams.add(new BasicNameValuePair("keyService",jsondata.getString("key_service")));
			Nparams.add(new BasicNameValuePair("boxChildStatus","1"));
			new Thread(){
				@Override
				public void run() {
					super.run();
					JsonHttpUtils.doPost(URLs.UPDATE_OPEN_RESPONSE, Nparams, mHandler, JsonHttpUtils.UPDATE_OPEN_RESPONSE_MSG, SuccessPayOrder.this);
				}
			}.start();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void dilogmsg(String msg) {
		new AlertDialog.Builder(this).setTitle("提示！").setMessage(msg).setPositiveButton("OK", null).create().show();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void httpOpenMsg(NameValuePair valuePair) {
		int typecode = Integer.valueOf(valuePair.getName());
		int code = Integer.valueOf(valuePair.getName());
		if (code == JsonHttpUtils.OPEN_BOX) {
			Log.i("LONGING", valuePair.getValue());
			new AlertDialog.Builder(this).setTitle("开箱返回信息！").setMessage(valuePair.getValue())
			.setPositiveButton("OK", null).create().show();
		}
		switch (typecode) {
		case 300:
			if (mThread != null) {
				mThread.stop();
				Log.i("requst_code", "超过一分钟了！");
				ToastUtil.showToastLong(this, "超过一分钟了！");
			}
			break;
			
		case JsonHttpUtils.OPEN_BOX:
			isopen(valuePair.getValue());
			break;
		case JsonHttpUtils.UPDATE_OPEN_RESPONSE_MSG:
			isupdatesuccess(valuePair.getValue());
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
				ToastUtil.showToastShort(this, "上传数据成功！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
