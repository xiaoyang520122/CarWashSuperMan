package com.gb.cwsup.adapter;

import java.util.List;

import com.gb.cwsup.R;
import com.gb.cwsup.entity.OrderItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MydefaultAdapter extends BaseAdapter {
	
	private Context context;
	private int laoutyid;
	private LayoutInflater inflater;
	
	public MydefaultAdapter(Context context,int laoutyid){
		this.context=context;
		this.laoutyid=laoutyid;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int postion, View contentView, ViewGroup arg2) {
			contentView=inflater.inflate(laoutyid, null);
		return contentView;
	}
	
}
