package com.gb.cwsup.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;

public class MyPropertyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回", "我的资产", "···");
		setContentView(R.layout.my_property);
		initview();
	}

	private void initview() {
		setLeftTvOnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyPropertyActivity.this.finish();
			}
		});

	}

}
