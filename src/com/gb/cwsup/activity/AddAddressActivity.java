package com.gb.cwsup.activity;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.entity.AddressBean;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;
import com.zcw.togglebutton.ToggleButton;

public class AddAddressActivity extends BaseActivity implements OnClickListener {

	private EditText mobledit, addressdit,adddit,typedit;
	private ToggleButton isdefaulTog;
	private ImageView tomapimg,typeimg;
	private int type;
	private LoadingDialog logingDialog;
	private List<NameValuePair> params;
	private Handler mhandler;
	
	private String username,usermobile;
	private double lat=0.0d,lng=0.0d;
	private String TYPE="",areaId;
	private Intent intent;
	private AddressBean addressBean;
	private final String[] types=new String[]{"家","单位","其他"};

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "ADDADDRESS", "确定");
		super.onCreate(paramBundle);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.add_caraddress);
		ActivityManagerUtil.getInstance().addToList(this);
		EventBus.getDefault().register(this);
		setbaseonclick();
		initview();
	}

	private void initview() {
		logingDialog=new LoadingDialog(this);
		mhandler=new Handler();
		mobledit = (EditText) findViewById(R.id.addadd_mbile);
		addressdit = (EditText) findViewById(R.id.addadd_address);
		adddit = (EditText) findViewById(R.id.addadd_add);
		typedit = (EditText) findViewById(R.id.addadd_type);
		isdefaulTog = (ToggleButton) findViewById(R.id.addadd_togglebutton);
		tomapimg= (ImageView) findViewById(R.id.addadd_tomap);
		typeimg = (ImageView) findViewById(R.id.addadd_typeimg);
		setUserInfo();
		typeimg.setOnClickListener(this);
		tomapimg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addadd_tomap:
			intent = new Intent(this, MapActivity.class);
			intent.putExtra("icoflag", "true");
			this.startActivity(intent);
			break;

		case R.id.addadd_typeimg:
			selecttype();
			break;

		default:
			break;
		}
	}
	
	private void setUserInfo() {
		SharedPreferences sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		username = sp.getString("name", "");
		usermobile = sp.getString("mobile", "");
		mobledit.setText(usermobile);
	}

	private void selecttype() {
		new AlertDialog.Builder(this).setTitle("选择标签")
		.setItems(types, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int point) {
				typedit.setText(types[point]);
				type=point+1;
				Log.i("LONGING", "type地址类型="+type);
			}
		}).create().show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		intent=getIntent();
		TYPE=intent.getStringExtra("type");
		if (!TextUtils.isEmpty(TYPE)&&TYPE.equals("MAP")) {
			adddit.setText(intent.getStringExtra("CARADD"));
			lat=intent.getDoubleExtra("lat", 0.0);
			lng=intent.getDoubleExtra("lng", 0.0);
			areaId=intent.getStringExtra("areaId");
		}else if (!TextUtils.isEmpty(TYPE)&&TYPE.equals("UPDATE")) {
			addressBean=(AddressBean) intent.getSerializableExtra("postADDRESS");
			updateview();
		}
	}
	
	private void updateview() {
		if (addressBean!=null) {
			mobledit.setText(addressBean.getPhone());
			try {
				String [] add=addressBean.getAddress().split("#");
				addressdit.setText(add[1]);
				adddit.setText(add[0]);
			} catch (Exception e) {
				e.printStackTrace();
				addressdit.setText(addressBean.getAddress());
				adddit.setText(addressBean.getAddress());
			}
			type=addressBean.getType();
			typedit.setText(types[type]);
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	private void setbaseonclick() {
		setLeftTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AddAddressActivity.this.finish();
			}
		});
		
		setRightTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveAddInfo();
			}
		});
	}

	protected void saveAddInfo() {
		if (viewisempty()) {
			logingDialog.setMessage(getString(R.string.loading_msg)).show();
			params=new ArrayList<NameValuePair>(7);
			params.add(new BasicNameValuePair("consignee", username));
			params.add(new BasicNameValuePair("longitude", lng+""));
			params.add(new BasicNameValuePair("latitude", lat+""));
			params.add(new BasicNameValuePair("address", adddit.getText().toString()+"#"+addressdit.getText().toString()));
			params.add(new BasicNameValuePair("phone", mobledit.getText().toString()));
			params.add(new BasicNameValuePair("isDefault", isdefaulTog.isClickable()+""));
			params.add(new BasicNameValuePair("type", type+""));
			params.add(new BasicNameValuePair("areaId", areaId));
			if (!TextUtils.isEmpty(TYPE)&&TYPE.equals("UPDATE")) {
				params.add(new BasicNameValuePair("id", addressBean.getId()));
				new Thread(){
					@Override
					public void run() {
						super.run();
						JsonHttpUtils.doPost(URLs.UPDATE_ADDRESS, params, mhandler, JsonHttpUtils.UPDATE_ADDRESS, AddAddressActivity.this);
					}
				}.start();
			}else {
				new Thread(){
					@Override
					public void run() {
						super.run();
						JsonHttpUtils.doPost(URLs.SAVE_ADDRESS, params, mhandler, JsonHttpUtils.SAVE_ADD, AddAddressActivity.this);
					}
				}.start();
			}
		}
	}

	private boolean viewisempty() {
		if (TextUtils.isEmpty(mobledit.getText().toString())) {
			showmsgdilog(getString(R.string.msg_no_mobile));
			return false;
		}
		if (TextUtils.isEmpty(addressdit.getText().toString())) {
			showmsgdilog(getString(R.string.addmsg_no_address));
			return false;
		}
		if (TextUtils.isEmpty(adddit.getText().toString())) {
			showmsgdilog(getString(R.string.msg_no_add));
			return false;
		}
		if (TextUtils.isEmpty(typedit.getText().toString())) {
			showmsgdilog(getString(R.string.addmsg_no_type));
			return false;
		}
		return true;
	}
	
	private void showmsgdilog(String msg){
		new AlertDialog.Builder(this).setTitle("提示")
		.setMessage(msg)
		.setPositiveButton("确定", null).create().show();
	}
	
	@Subscribe (threadMode=ThreadMode.MAIN)
	public void eventsave(NameValuePair value){
		int code=Integer.valueOf(value.getName());
		switch (code) {
		case JsonHttpUtils.SAVE_ADD:
			issavesuccess(value.getValue());
			break;
		case JsonHttpUtils.UPDATE_ADDRESS:
			isupdatesuccess(value.getValue());
			break;

		default:
			break;
		}
	}

	private void isupdatesuccess(String value) {
		logingDialog.dismiss();
		try {
			JSONObject jo1=new JSONObject(value);
			JSONObject jo2=jo1.getJSONObject("message");
			if(jo2.getString("type").equals("success")){
				finish();
			}else {
				DialogUtil.getAlertDialog(this, getString(R.string.upda_fail)).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void issavesuccess(String value) {
		logingDialog.dismiss();
		try {
			JSONObject jo1=new JSONObject(value);
			JSONObject jo2=jo1.getJSONObject("message");
			if(jo2.getString("type").equals("success")){
				finish();
			}else {
				DialogUtil.getAlertDialog(this, getString(R.string.save_fail)).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
