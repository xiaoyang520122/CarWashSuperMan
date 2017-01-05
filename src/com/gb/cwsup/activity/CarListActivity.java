package com.gb.cwsup.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.entity.CarBean;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;

public class CarListActivity extends BaseActivity {
	
	private ListView carListView;
	private Handler mhandler;
	private LoadingDialog loadingDialog;
	private List<CarBean> cars;
	private LayoutInflater inflater;
	private Intent intent;
	private String TYPE="";
	private LinearLayout nocar;

	@Override
	protected void onCreate(Bundle paramBundle) {
		settitlename("返回", "CARLIST", "添加");
		super.onCreate(paramBundle);
		setContentView(R.layout.car_list_activity);
		EventBus.getDefault().register(this);
		initView();
		setbaseonclick();
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
				CarListActivity.this.startActivity(new Intent(CarListActivity.this, CarAddActivity.class));
			}
		});
		
	}

	private void initView() {
		inflater=LayoutInflater.from(this);
		loadingDialog=new LoadingDialog(this);
		mhandler=new Handler();
		nocar=(LinearLayout) findViewById(R.id.carlist_nomsg);
		carListView=(ListView) findViewById(R.id.carlist_ListView);
		carListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int item, long arg3) {
				CarBean car=cars.get(item);
				if (!TextUtils.isEmpty(TYPE)&&TYPE.equals("CHOICE")) {
					Map<String, Object> data=new HashMap<String, Object>(1);
					data.put("choiceCAR", car);
					EventBus.getDefault().post(data);
					finish();return;
				}
				Intent intent=new Intent(CarListActivity.this, CarAddActivity.class);
				intent.putExtra("CAR", car);
				intent.putExtra("type", "UPDATE");
				CarListActivity.this.startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		dowloadingdata();
		intent=getIntent();
		TYPE=intent.getStringExtra("type");//=CHOICE
	}

	private void dowloadingdata() {
		loadingDialog.setMessage(getString(R.string.loading_msg)).show();
		new Thread(){
			@Override
			public void run() {
				super.run();
				JsonHttpUtils.doPost(URLs.CAR_LIST, new ArrayList<NameValuePair>(1), mhandler, JsonHttpUtils.GET_CARS, CarListActivity.this);
			}
		}.start();
	}
	
	@Subscribe(threadMode=ThreadMode.MAIN)
	public void eventcars(NameValuePair value){
		int code = Integer.valueOf(value.getName());
		switch (code) {
		case JsonHttpUtils.GET_CARS:
			loadingDialog.dismiss();
			jiexiecars(value.getValue());
			break;
		case JsonHttpUtils.DELETE_CAR:
			loadingDialog.dismiss();
			isdeletesuccess(value.getValue());
			break;

		default:
			break;
		}
	}

	private void isdeletesuccess(String value) {
		try {
			JSONObject jo1=new JSONObject(value);
			JSONObject jo2=jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				dowloadingdata();
			}else {
				DialogUtil.getAlertDialog(this, getString(R.string.loading_error_msg));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void jiexiecars(String value) {
		try {
			JSONObject jo1=new JSONObject(value);
			JSONObject jo2=jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				JSONObject jo3=jo1.getJSONObject("data");
				JSONArray jArray=jo3.getJSONArray("content");
				getinfo(jArray);
			}else {
				nocar.setVisibility(View.VISIBLE);
				DialogUtil.getAlertDialog(this, getString(R.string.loading_error_msg));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			nocar.setVisibility(View.VISIBLE);
		}
	}

	private void getinfo(JSONArray jArray) {
		if (jArray.length()>0) {
			nocar.setVisibility(View.GONE);
			cars=new ArrayList<CarBean>(4);
			for (int i = 0; i < jArray.length(); i++) {
				CarBean cb= new CarBean();
				try {
					JSONObject jOb=jArray.getJSONObject(i);
					cb.setId(jOb.getInt("id"));
					cb.setCarno(jOb.getString("license_number"));
					cb.setColor(jOb.getString("color"));
					cb.setType(jOb.getString("brand"));
					cb.setTypemodels(jOb.getString("models"));
					cb.setDefault(jOb.getBoolean("isDefault"));
					cars.add(cb);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}else {
			DialogUtil.getAlertDialog(this,"没有信息！！！！");
			nocar.setVisibility(View.VISIBLE);
			return;
		}
		carListView.setAdapter(new CarListAdapter());
	}
	
	
	public class CarListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return cars.size();
		}

		@Override
		public Object getItem(int arg0) {
			return cars.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View conView, ViewGroup arg2) {
			TextView type,typemodels,carno,color,delete;
			conView=inflater.inflate(R.layout.car_list_item, null);
			type=(TextView) conView.findViewById(R.id.carlist_type);
			typemodels=(TextView) conView.findViewById(R.id.carlist_typemodles);
			carno=(TextView) conView.findViewById(R.id.carlist_carno);
			color=(TextView) conView.findViewById(R.id.carlist_color);
			delete=(TextView) conView.findViewById(R.id.carlist_delete);
			
			CarBean carBean=cars.get(arg0);
			type.setText(carBean.getType());
			typemodels.setText(carBean.getTypemodels());
			carno.setText(carBean.getCarno());
			color.setText(carBean.getColor());
			setviewonclick(delete,carBean.getId());
			return conView;
		}

		private void setviewonclick(TextView delete,final int id) {
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					new AlertDialog.Builder(CarListActivity.this).setTitle("删除提示！")
					.setMessage("确定要删除这条信息")
					.setPositiveButton("取消",null)
					.setNeutralButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							CarListActivity.this.loadingDialog.setMessage("操作中……").show();
							new Thread(){
								@Override
								public void run() {
									List<NameValuePair> params=new ArrayList<NameValuePair>(1) ;
									params.add(new BasicNameValuePair("id", id+""));
									super.run();
									JsonHttpUtils.doPost(URLs.DELETE_CAR, params, mhandler, JsonHttpUtils.DELETE_CAR, CarListActivity.this);
								}
							}.start();
						}
					}).create().show();
				}
			});
		}
		
	} 
}
