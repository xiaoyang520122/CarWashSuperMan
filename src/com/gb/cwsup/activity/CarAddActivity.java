package com.gb.cwsup.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.entity.CarBean;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;
import com.zcw.togglebutton.ToggleButton;

public class CarAddActivity extends BaseActivity {

	private EditText no, color, type, typemobels;
	private ToggleButton isdefauls;
	private List<NameValuePair> params;
	private Handler mhandler;
	private LoadingDialog loadingDialog;

	private String TYPE = "";
	private CarBean car;

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "车辆信息", "完成");
		super.onCreate(paramBundle);
		setContentView(R.layout.car_add);
		ActivityManagerUtil.getInstance().addToList(this);
		EventBus.getDefault().register(this);
		initview();
		setbaseonclick();
	}

	private void initview() {
		loadingDialog = new LoadingDialog(this);
		mhandler = new Handler();
		no = (EditText) findViewById(R.id.addcar_no);
		color = (EditText) findViewById(R.id.addcar_color);
		type = (EditText) findViewById(R.id.addcar_type);
		typemobels = (EditText) findViewById(R.id.addcar_typemobels);
		isdefauls = (ToggleButton) findViewById(R.id.addcar_togglebutton);
	}

	private void setbaseonclick() {
		setLeftTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		setRightTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadingDialog.setMessage(CarAddActivity.this.getString(R.string.update_msg)).show();
				saveinfo();
			}
		});

	}

	private void saveinfo() {
		// no,color,type,typemobels
		if (isviewempty()) {
			params = new ArrayList<NameValuePair>(5);
			params.add(new BasicNameValuePair("license_number", no.getText().toString()));
			params.add(new BasicNameValuePair("color", color.getText().toString()));
			params.add(new BasicNameValuePair("brand", type.getText().toString()));
			params.add(new BasicNameValuePair("models", typemobels.getText().toString()));
			params.add(new BasicNameValuePair("isDefault", isdefauls.isSelected() + ""));
			params.add(new BasicNameValuePair("insurance", "无"));
			params.add(new BasicNameValuePair("type_insurance", "无"));
			params.add(new BasicNameValuePair("insurance_end_date", "无"));
			params.add(new BasicNameValuePair("address", "无"));
			params.add(new BasicNameValuePair("name", "无"));
			params.add(new BasicNameValuePair("idNumber", "无"));
			new Thread() {
				@Override
				public void run() {
					super.run();
					if (!TextUtils.isEmpty(TYPE)&&TYPE.equals("UPDATE")&&car!=null) {
						params.add(new BasicNameValuePair("id", car.getId()+""));
						JsonHttpUtils.doPost(URLs.UPDATE_CAR, params, mhandler, JsonHttpUtils.UPDATE_CAR, CarAddActivity.this);
					}else{
						JsonHttpUtils.doPost(URLs.SAVE_CAR, params, mhandler, JsonHttpUtils.SAVE_CAR, CarAddActivity.this);
					}
				}
			}.start();
		}

	}

	private boolean isviewempty() {
		// no,color,type,typemobels
		if (TextUtils.isEmpty(no.getText().toString())) {
			DialogUtil.getAlertDialog(this, getString(R.string.addcar_nono)).show();
			return false;
		}
		if (TextUtils.isEmpty(color.getText().toString())) {
			DialogUtil.getAlertDialog(this, getString(R.string.addcar_nocolor)).show();
			return false;
		}
		if (TextUtils.isEmpty(type.getText().toString())) {
			DialogUtil.getAlertDialog(this, getString(R.string.addcar_notype)).show();
			return false;
		}
		if (TextUtils.isEmpty(typemobels.getText().toString())) {
			DialogUtil.getAlertDialog(this, getString(R.string.addcar_notypemobels)).show();
			return false;
		}
		return true;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void eventsavecar(NameValuePair value) {
		int code = Integer.valueOf(value.getName());
		switch (code) {
		case JsonHttpUtils.SAVE_CAR:
			issavesuccess(value.getValue());
			break;
		case JsonHttpUtils.UPDATE_CAR:
			isupdatesuccess(value.getValue());
			break;

		default:
			break;
		}

	}

	private void isupdatesuccess(String value) {
		try {
			JSONObject jo1 = new JSONObject(value);
			JSONObject jo2 = jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				finish();
			} else {
				DialogUtil.getAlertDialog(this, "更新失败，请重试！");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void issavesuccess(String value) {
		try {
			JSONObject jo1 = new JSONObject(value);
			JSONObject jo2 = jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				finish();
			} else {
				DialogUtil.getAlertDialog(this, "保存失败，请重试！");
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

	@Override
	protected void onResume() {
		super.onResume();
		intentisupdate();
	}

	private void intentisupdate() {
		Intent intent = getIntent();
		TYPE = intent.getStringExtra("type");
		if (!TextUtils.isEmpty(TYPE)) {
			if (TYPE.equals("UPDATE")) {
				car = (CarBean) intent.getSerializableExtra("CAR");
			} else {
				return;
			}
			if (car != null) {
				no.setText(car.getCarno());
				color.setText(car.getColor());
				type.setText(car.getType());
				typemobels.setText(car.getTypemodels());
				isdefauls.setSelected(car.isDefault());
			}
		}
		}
		
}
