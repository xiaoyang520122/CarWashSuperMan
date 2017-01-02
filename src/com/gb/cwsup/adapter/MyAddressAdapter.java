package com.gb.cwsup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyAddressAdapter extends BaseAdapter {
	
	private Context context;
	private int laoutyid;
	private LayoutInflater inflater;
	
	public MyAddressAdapter(Context context,int laoutyid){
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
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		contentView=inflater.inflate(laoutyid, null);
		return contentView;
	}
	
	public class MyViewHolder{
		
	}

}
