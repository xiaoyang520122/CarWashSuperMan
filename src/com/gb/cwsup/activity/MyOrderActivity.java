package com.gb.cwsup.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.order.OrderInformationActivity;
import com.gb.cwsup.adapter.MyOrderAdapter;
import com.gb.cwsup.entity.OrderItem;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;

public class MyOrderActivity extends BaseActivity implements OnClickListener {

	// order List
	private ListView mOrderListView;
	private TextView noMSG;
	private MyOrderAdapter orderAdapter;
	private TextView nowTV, bookTV, historyTV;
	private List<NameValuePair> params;
	private List<OrderItem> orderItems;
	private SimpleDateFormat sdf;
	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回", "我的订单", "");
		setContentView(R.layout.my_order_list);
		EventBus.getDefault().register(this);
		initview();
	}

	@SuppressLint("SimpleDateFormat")
	private void initview() {
		dialog=new LoadingDialog(this);
		sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		nowTV = (TextView) findViewById(R.id.my_order_now);
		bookTV = (TextView) findViewById(R.id.my_order_yuyue);
		historyTV = (TextView) findViewById(R.id.my_order_history);
		noMSG=(TextView) findViewById(R.id.myorder_noMSG);
		nowTV.setOnClickListener(this);
		bookTV.setOnClickListener(this);
		historyTV.setOnClickListener(this);

		mOrderListView = (ListView) findViewById(R.id.my_order_list_listview);
		listViewsetonclick();
		setLeftTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MyOrderActivity.this.finish();
			}
		});
		getdata("await_comfirm");//加载默认数据
	}
	
	private void listViewsetonclick() {
		mOrderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
				Intent intent=new Intent(MyOrderActivity.this, OrderInformationActivity.class);
				intent.putExtra("sn", orderItems.get(postion).getSn());
				MyOrderActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@SuppressLint("ResourceAsColor")
	private void recovery() {
		nowTV.setTextColor(Color.parseColor("#ff7e7d83"));
		bookTV.setTextColor(Color.parseColor("#ff7e7d83"));
		historyTV.setTextColor(Color.parseColor("#ff7e7d83"));
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View v) {
		noMSG.setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.my_order_now:
			recovery();
			getdata("await_comfirm");
			nowTV.setTextColor(Color.parseColor("#ff07B6F3"));
			break;
			
		case R.id.my_order_yuyue:
			recovery();
			getdata("await_pay");
			bookTV.setTextColor(Color.parseColor("#ff07B6F3"));
			break;
			
		case R.id.my_order_history:
			recovery();
			getdata("completed");
			historyTV.setTextColor(Color.parseColor("#ff07B6F3"));
			break;

		default:
			break;
		}
	}
	
	private void getdata(String ype){
		dialog.setMessage("数据加载中……").show();
		params=new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("pageNumber", "1"));
		params.add(new BasicNameValuePair("type", ype));
		new Thread(){
			@Override
			public void run() {
				super.run();
				JsonHttpUtils.doPost(URLs.GET_ORDER_LIST, params, null, JsonHttpUtils.GET_ORDER_LIST, MyOrderActivity.this);
			}
		}.start();
	}
	
	@Subscribe(threadMode=ThreadMode.MAIN)
	public void eventbusdata(NameValuePair value){
		int code=Integer.valueOf(value.getName());
		switch (code) {
		case JsonHttpUtils.GET_ORDER_LIST:
			dialog.dismiss();
			jiexivalue(value.getValue());
			break;

		default:
			break;
		}
	}

	private void jiexivalue(String value) {
		try {
			JSONObject jo1=new JSONObject(value);
			JSONObject jo2=jo1.getJSONObject("message");
			
			if (jo2.getString("type").equals("success")) {
				jiexiinfo(jo1.getJSONObject("data").getJSONArray("content"));
			}else {
				showdilog("加载信息失败！请重试");
				setadapter(new ArrayList<OrderItem>());
			}
		} catch (JSONException e) {
			showdilog("加载信息失败！请重试");
			setadapter(new ArrayList<OrderItem>());
			e.printStackTrace();
		}
	}
	
	private void jiexiinfo(JSONArray content) {
		
		orderItems=new ArrayList<OrderItem>();
		for (int i = 0; i < content.length(); i++) {
				JSONObject jsonengineer=new JSONObject();
				try {
					jsonengineer = content.getJSONObject(i).getJSONObject("engineer");
				} catch (JSONException e) { e.printStackTrace(); }
				try {
					JSONArray orderArray=content.getJSONObject(i).getJSONArray("orderItems");
					
					for (int j = 0; j < orderArray.length(); j++) {
						OrderItem oItem=new OrderItem();
						long lomil=Long.valueOf(orderArray.getJSONObject(j).getString("createDate"));
						oItem.setCreatdate(sdf.format(new Date(lomil)));
						try {
							oItem.setEngineerID(jsonengineer.getString("id"));
							oItem.setEngineerName(jsonengineer.getString("name"));
						} catch (Exception e) { e.printStackTrace(); }
						
						oItem.setPrice(orderArray.getJSONObject(j).getString("price"));
						oItem.setProduct(orderArray.getJSONObject(j).getString("fullName"));
						oItem.setSn(content.getJSONObject(i).getString("sn"));
						orderItems.add(oItem);
					}
				} catch (NumberFormatException e) {e.printStackTrace();
				} catch (JSONException e) { e.printStackTrace(); }
		}
		setadapter(orderItems);
	}
	
	private void setadapter(List<OrderItem> orderItems){
		if (orderItems.size()==0) {
			noMSG.setVisibility(View.VISIBLE);
		}
		orderAdapter = new MyOrderAdapter(this, R.layout.my_order_list_item,orderItems);
		mOrderListView.setAdapter(orderAdapter);
	}

	private void showdilog(String msg){
		new AlertDialog.Builder(this).setTitle("提示")
		.setMessage(msg).setPositiveButton("确定", null).create().show();
	}

}
