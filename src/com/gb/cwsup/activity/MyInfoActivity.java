package com.gb.cwsup.activity;

import java.text.ParseException;
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
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.platform.comapi.map.v;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.ToastUtil;

public class MyInfoActivity extends BaseActivity implements OnClickListener {
	
	private EditText nameedit,emailedit;
	private TextView birthdaytView;
	private RadioGroup rGroup;
	private RadioButton rb1,rb2;
	private String sex="male";

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "个人信息", "保存");
		super.onCreate(paramBundle);
		setContentView(R.layout.my_info);
		EventBus.getDefault().register(this);
		initview();
	}

	private void initview() {
		setLeftTvOnClick(this);
		setRightTvOnClick(this);
		findViewById(R.id.myinfo_nameimg).setOnClickListener(this);
		findViewById(R.id.myinfo_emailimg).setOnClickListener(this);
		findViewById(R.id.myinfo_birthimg).setOnClickListener(this);
		birthdaytView=(TextView) findViewById(R.id.myinfo_birthtv);
		nameedit=(EditText) findViewById(R.id.myinfo_nametv);
		emailedit=(EditText) findViewById(R.id.myinfo_emailtv);
		rGroup=(RadioGroup) findViewById(R.id.myinfo_radiogroup);
		setradiochang();
		rb1=(RadioButton) findViewById(R.id.myinfo_radioButton1);
		rb2=(RadioButton) findViewById(R.id.myinfo_radioButton2);
	}

	private void setradiochang() {
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int point) {
				if (point==0) {
					sex="male";
				}else if (point==1) {
					sex="female";
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_left_tv:
			this.finish();
			break;
		case R.id.action_right_tv:
			savechange();
			break;
		case R.id.myinfo_nameimg:
			setnameviewedit();
			break;
		case R.id.myinfo_emailimg:
			setemailEditdit();
			break;
		case R.id.myinfo_birthimg:
			checkdate();
			break;

		default:
			break;
		}
		
	}

	private void savechange() {
		final Handler mhandler=new Handler();
		new Thread(){
			@SuppressLint("SimpleDateFormat")
			@Override
			public void run() {
				super.run();
				SimpleDateFormat sft=new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat sftt=new SimpleDateFormat("yyyy-MM-dd");
				List<NameValuePair> params=new ArrayList<NameValuePair>(4);
				params.add(new BasicNameValuePair("name", nameedit.getText().toString()));
				params.add(new BasicNameValuePair("email", emailedit.getText().toString()));
				params.add(new BasicNameValuePair("gender", sex));
				String birthString=birthdaytView.getText().toString();
				try {
					params.add(new BasicNameValuePair("birth", sftt.format(sft.parse(birthString))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				JsonHttpUtils.doPost(URLs.UPDATE_USER_INFO, params, mhandler, JsonHttpUtils.UPDATE_USER_MSG, MyInfoActivity.this);
			}
		}.start();
	}
	
	@Subscribe(threadMode=ThreadMode.MAIN)
	public void eventupdate(NameValuePair value){
		int code=Integer.valueOf(value.getName());
		if (code==JsonHttpUtils.UPDATE_USER_MSG) {
			isupdatesuccess(value.getValue());
		}
	}

	private void isupdatesuccess(String jsonstr) {
		try {
			JSONObject jo1=new JSONObject(jsonstr);
			JSONObject jo2=jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				ToastUtil.showToastLong(this, "修改成功！");
				this.finish();
			}else {
				DialogUtil.getAlertDialog(this, jo2.getString("content")).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void checkdate() {
		DatePickerDialog pickerDialog=new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int y, int m, int d) {
				Log.i("DatePickerDialog", y+""+m+""+d);
				birthdaytView.setText(y+"年"+m+"月"+d+"日");
			}
		}, 2000, 01, 01);
		pickerDialog.show();
	}

	private void setemailEditdit() {
		emailedit.setEnabled(true);
		emailedit.setFocusable(true);
		emailedit.setTextColor(Color.parseColor("#ff7e7d83"));
	}

	private void setnameviewedit() {
		nameedit.setEnabled(true);
		nameedit.setFocusable(true);
		nameedit.setBackgroundResource(R.drawable.corners_white_line4_5dp);
		rb1.setEnabled(true);
		rb2.setEnabled(true);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
