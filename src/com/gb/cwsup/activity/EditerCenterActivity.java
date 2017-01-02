package com.gb.cwsup.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.MainActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.register.LoingActivity;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

public class EditerCenterActivity extends BaseActivity implements OnClickListener {
	private ToggleButton tog,openlight;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "设置中心", "");
		super.onCreate(paramBundle);
		setContentView(R.layout.editer_center);
		ActivityManagerUtil.getInstance().addToList(this);
		initview();
	}

	private void initview() {
		tog = (ToggleButton) findViewById(R.id.editer_setmarker_type);
		findViewById(R.id.exciteduser).setOnClickListener(this);
		editmakertype();

		setLeftTvOnClick(this);
	}

	private void editmakertype() {
		sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		boolean togflag=sp.getBoolean("markertype", true);
		if (togflag) {
			tog.setToggleOn(true);
		}else {
			tog.setToggleOff(true);
		}
		
		final Editor editor = sp.edit();
		
		tog.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				editor.putBoolean("markertype", on);
				editor.commit();
				AppApplication.MARKER_TYPE = on;
			}
		});

	}


	private void exitUser() {
		SharedPreferences sp = this.getSharedPreferences("register_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("mobile", "");
		editor.commit();
		MainActivity.FirstLogingFlag = true;
		startActivity(new Intent(this, LoingActivity.class));
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exciteduser: // sign out
			exitUser();
			break;
		case R.id.action_left_tv: // back
			this.finish();
			break;

		default:
			break;
		}
	}

}
