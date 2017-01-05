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
import android.util.Log;
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
import com.gb.cwsup.entity.AddressBean;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;

public class AdressListActivity extends BaseActivity {

	private ListView addresslistView;
	private LinearLayout nomsg;
	private LoadingDialog loadingDialog;
	private Handler mHandler;
	private List<AddressBean> adds;
	private LayoutInflater inflater;
	private Intent intent;
	private String TYPE = "";
	private Handler mhandler;
	private final String[] types = new String[] { "家", "单位", "其他" };

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		settitlename("返回", "地址管理", "添加");
		setContentView(R.layout.car_adress_list);
		ActivityManagerUtil.getInstance().addToList(this);
		EventBus.getDefault().register(this);
		intiview();
	}

	private void intiview() {
		mhandler = new Handler();
		inflater = LayoutInflater.from(this);
		mHandler = new Handler();
		addresslistView = (ListView) findViewById(R.id.car_adress_listview);
		nomsg= (LinearLayout) findViewById(R.id.car_adress_nomsg);
		loadingDialog = new LoadingDialog(this);
		setonclick();

	}

	private void downloadingAdds() {
		loadingDialog.setMessage(getString(R.string.loading_msg)).show();
		new Thread() {
			@Override
			public void run() {
				super.run();
				JsonHttpUtils.doPost(URLs.ADDRESS_LIST, new ArrayList<NameValuePair>(1), mHandler, JsonHttpUtils.GET_ADDS, AdressListActivity.this);
			}
		}.start();
	}

	private void setonclick() {
		// 设置ActionBar左边文本点击事件
		this.setLeftTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AdressListActivity.this.finish();
			}
		});

		setRightTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AdressListActivity.this.startActivity(new Intent(AdressListActivity.this, AddAddressActivity.class));
			}
		});

		addresslistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				AddressBean address = adds.get(arg2);
				if (!TextUtils.isEmpty(TYPE) && TYPE.equals("CHOICE")) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("choiceADDRESS", address);
					EventBus.getDefault().post(data);
					finish();
					return;
				}
				intent = new Intent(AdressListActivity.this, AddAddressActivity.class);
				intent.putExtra("postADDRESS", address);
				intent.putExtra("type", "UPDATE");
				AdressListActivity.this.startActivity(intent);
			}
		});
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void eventadds(NameValuePair value) {
		int code = Integer.valueOf(value.getName());
		switch (code) {
		case JsonHttpUtils.GET_ADDS:
			Log.i("ORDER", "addresss**=" + value.getValue());
			jiexieadds(value.getValue());
			break;
		case JsonHttpUtils.DELETE_ADDRESS:
			isdeletesuccess(value.getValue());
			break;

		default:
			break;
		}
	}

	private void isdeletesuccess(String value) {
		loadingDialog.dismiss();
		try {
			JSONObject jo1 = new JSONObject(value);
			JSONObject jo2 = jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				downloadingAdds();
			} else {
				DialogUtil.getAlertDialog(this, getString(R.string.delete_fail)).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void jiexieadds(String value) {
		loadingDialog.dismiss();
		try {
			JSONObject jo1 = new JSONObject(value);
			JSONObject jo2 = jo1.getJSONObject("message");
			if (jo2.getString("type").equals("success")) {
				JSONObject jo3 = jo1.getJSONObject("data");
				getadds(jo3);
			} else {
				DialogUtil.getAlertDialog(this, getString(R.string.loading_error_msg)).show();
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getadds(JSONObject jo3) {
		adds = new ArrayList<AddressBean>(4);
		try {
			JSONArray jarr = jo3.getJSONArray("content");
			if (jarr.length() > 0) {
				nomsg.setVisibility(View.GONE);
				for (int i = 0; i < jarr.length(); i++) {
					JSONObject jo = jarr.getJSONObject(i);
					AddressBean add = new AddressBean();
					add.setUsername(jo.getString("consignee"));
					add.setId(jo.getString("id"));
					add.setAddress(jo.getString("address"));
					add.setPhone(jo.getString("phone"));
					add.setLatitude(jo.getString("latitude"));
					add.setLongitude(jo.getString("longitude"));
					add.setDefault(jo.getBoolean("isDefault"));
					add.setType(jo.getInt("type") - 1);
					adds.add(add);
				}
			} else {
				nomsg.setVisibility(View.VISIBLE);
				return;
			}
			addresslistView.setAdapter(new AddAdapter());
		} catch (JSONException e) {
			nomsg.setVisibility(View.VISIBLE);
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
		downloadingAdds();
		intent = getIntent();
		TYPE = intent.getStringExtra("type");// =CHOICE
	}

	private class AddAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return adds.size();
		}

		@Override
		public Object getItem(int arg0) {
			return adds.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View conView, ViewGroup arg2) {
			TextView nametv, addtv, phone, type, delete;
			AddressBean addbean = adds.get(arg0);

			conView = inflater.inflate(R.layout.car_adress_list_item, null);
			nametv = (TextView) conView.findViewById(R.id.addlist_item_name);
			addtv = (TextView) conView.findViewById(R.id.addlist_item_address);
			phone = (TextView) conView.findViewById(R.id.addlist_item_phone);
			type = (TextView) conView.findViewById(R.id.addlist_item_type);
			delete = (TextView) conView.findViewById(R.id.addlist_item_delete);
			
			String address=addbean.getAddress().split("#")[0];

			nametv.setText(addbean.getUsername());
			addtv.setText(address);
			phone.setText(addbean.getPhone());
			type.setText(types[addbean.getType()]);
			deleteonclick(delete, addbean.getId());
			return conView;
		}

		private void deleteonclick(TextView delete, final String id) {
			delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					deleteData(id);
				}
			});
		}

		private void deleteData(final String id) {
			new AlertDialog.Builder(AdressListActivity.this).setTitle("提示！")
			.setMessage("确定要删除吗？").setPositiveButton("取消", null)
			.setNeutralButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					List<NameValuePair> params = new ArrayList<NameValuePair>(1);
					params.add(new BasicNameValuePair("id", id + ""));
					deleteid(params);
				}
			}).create().show();
		}

		private void deleteid(final List<NameValuePair> params) {
			new Thread() {

				@Override
				public void run() {
					super.run();
					JsonHttpUtils.doPost(URLs.DELETE_ADDRESS, params, mhandler, JsonHttpUtils.DELETE_ADDRESS, AdressListActivity.this);
				}
			}.start();
		}
	}
}
