package com.gb.cwsup.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.adapter.MyOrderAdapter;
import com.gb.cwsup.utils.ActivityManagerUtil;

public class EngineerInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回","超人详情","确定");
		setContentView(R.layout.engineer_info);
		ActivityManagerUtil.getInstance().addToList(this);
		initview();
	}

	private void initview() {

		setLeftTvOnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EngineerInfoActivity.this.finish();
			}
		});
	}
}
