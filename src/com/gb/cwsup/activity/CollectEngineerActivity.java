package com.gb.cwsup.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.gb.cwsup.activity.order.WaitSureOrderActivity;
import com.gb.cwsup.entity.EngineerOld;
import com.gb.cwsup.fragment.MapListsFragment;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.ToastUtil;

public class CollectEngineerActivity extends BaseActivity {

	private ListView addresslistView;
	private Intent intent;
	private List<EngineerOld> engs;
	private String TYPE = "";
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		settitlename("返回", "技师列表", "添加");
		setContentView(R.layout.collect_engineer);
		ActivityManagerUtil.getInstance().addToList(this);
		intiview();
	}

	private void intiview() {
		addresslistView = (ListView) findViewById(R.id.collect_engineer_listview);
		inflater = LayoutInflater.from(this);
		listviewitemonclick();
		// 设置ActionBar左边文本点击事件
		this.setLeftTvOnClick(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CollectEngineerActivity.this.finish();
			}
		});
	}

	private void listviewitemonclick() {
		addresslistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ToastUtil.showToastShort(CollectEngineerActivity.this, arg2+"");
				intent=new Intent();
				intent.setClass(CollectEngineerActivity.this, WaitSureOrderActivity.class);
				intent.putExtra("type", "yuyue");
				intent.putExtra("EngineerOld", engs.get(arg2));
				CollectEngineerActivity.this.startActivity(intent);
				CollectEngineerActivity.this.finish();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		intenttype();
	}

	private void intenttype() {
		intent = getIntent();
		TYPE = intent.getStringExtra("type");
		if (!TextUtils.isEmpty(TYPE) && TYPE.equals("choice")) {
			engs = MapListsFragment.ENGS;
			addresslistView.setAdapter(new EngAdapter());
		}

	}

	/**
	 * 实现布局文件中的 onclick方法,跳转到添加页面
	 * 
	 * @param view
	 */
	public void addnewcarmsg(View view) {
		ToastUtil.showToastShort(this, "添加新地址！");

	}

	private class EngAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return engs.size();
		}

		@Override
		public Object getItem(int arg0) {
			return engs.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View conView, ViewGroup arg2) {
			TextView engname;
			EngineerOld eng = engs.get(arg0);
			conView = inflater.inflate(R.layout.collect_engineer_item, null);
			engname = (TextView) conView.findViewById(R.id.collect_eng_name);
			engname.setText(eng.getName());
			return conView;
		}

	}
}
