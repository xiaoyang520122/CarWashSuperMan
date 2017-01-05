package com.gb.cwsup.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.adapter.MydefaultAdapter;

public class MyRefundActivity extends BaseActivity implements OnClickListener {

	// order List
	private ListView mOrderListView;
	private MydefaultAdapter orderAdapter;
	// 标题字样
	private TextView title1, title2;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回", "我的退款", "···");
		setContentView(R.layout.my_refund);
		initview();
	}

	private void initview() {

		title1 = (TextView) findViewById(R.id.myrefund_title_tv1);
		title2 = (TextView) findViewById(R.id.myrefund_title_tv2);
		title1.setOnClickListener(this);
		title2.setOnClickListener(this);

		mOrderListView = (ListView) findViewById(R.id.myrefund_listview);
		orderAdapter = new MydefaultAdapter(this, R.layout.my_refund_item);

		mOrderListView.setAdapter(orderAdapter);
		setLeftTvOnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyRefundActivity.this.finish();
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.myrefund_title_tv1:
			title1.setTextColor(Color.parseColor("#ffffffff"));
			title2.setTextColor(Color.parseColor("#ff46b0da"));
			title1.setBackgroundResource(R.drawable.corners_left_blue);
			title2.setBackgroundResource(R.drawable.corners_right_white);
			break;
		case R.id.myrefund_title_tv2:
			title1.setTextColor(Color.parseColor("#ff46b0da"));
			title2.setTextColor(Color.parseColor("#ffffffff"));
			title1.setBackgroundResource(R.drawable.corners_left_white);
			title2.setBackgroundResource(R.drawable.corners_right_blue);
			break;

		default:
			break;
		}

	}

}
