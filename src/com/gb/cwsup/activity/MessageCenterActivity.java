package com.gb.cwsup.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.adapter.MydefaultAdapter;
import com.gb.cwsup.utils.ActivityManagerUtil;

public class MessageCenterActivity extends BaseActivity {

	// order List
	private ListView mOrderListView;
	private MydefaultAdapter orderAdapter;
	// 标题字样

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回", "消息中心", "···");
		setContentView(R.layout.message_center);
		ActivityManagerUtil.getInstance().addToList(this);
		initview();
	}

	private void initview() {


		mOrderListView = (ListView) findViewById(R.id.messagecenter_listview);
		orderAdapter = new MydefaultAdapter(this, R.layout.message_center_item);

		mOrderListView.setAdapter(orderAdapter);
		setLeftTvOnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MessageCenterActivity.this.finish();
			}
		});
	}


}
