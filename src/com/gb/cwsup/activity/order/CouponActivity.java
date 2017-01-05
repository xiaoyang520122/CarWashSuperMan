package com.gb.cwsup.activity.order;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.JsonHttpUtils;

public class CouponActivity extends BaseActivity {

	private ListView couponlistview;
//	private final static int GET_COUPON=JsonHttpUtils.REQUEST_COUPON_FLAG;
	private JSONArray jsonArray;
	private LayoutInflater inflater;
	private String CONPON_CODE="";

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "优惠券", "");
		super.onCreate(paramBundle);
		setContentView(R.layout.coupon_activity);
		ActivityManagerUtil.getInstance().addToList(this);
		couponlistview = (ListView) findViewById(R.id.coupon_listview);
		inflater=LayoutInflater.from(this);
		initDate();
		initview();
	}
	

	private void initDate() {
		Intent intent=getIntent();
		try {
			jsonArray=new JSONArray(intent.getStringExtra("coupon"));
			couponlistview.setAdapter(new MyCouponAdapter());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private void initview() {
		setLeftTvOnClick(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		couponlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					CONPON_CODE=jsonArray.getJSONObject(arg2).getString("code");
					NameValuePair valuePair=new BasicNameValuePair(JsonHttpUtils.COUPON_CODE+"", CONPON_CODE);
					EventBus.getDefault().post(valuePair);
					new Intent(CouponActivity.this, WaitSureOrderActivity.class);
					CouponActivity.this.finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public class MyCouponAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return jsonArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			try {
				return jsonArray.getJSONObject(arg0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int postion, View contentview, ViewGroup arg2) {
			contentview=inflater.inflate(R.layout.coupon_activity_item, null);
//			TextView price=(TextView) contentview.findViewById(R.id.coupon_price);
			TextView itemtitle=(TextView) contentview.findViewById(R.id.coupon_item_title);
			String pricesum;
			try {
				pricesum = jsonArray.getJSONObject(postion).getString("couponName");
				itemtitle.setText(pricesum);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return contentview;
		}
		
	}
	
}
