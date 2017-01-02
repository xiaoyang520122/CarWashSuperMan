package com.gb.cwsup.adapter;

import java.util.List;

import com.gb.cwsup.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MySpinnerAdapter extends BaseAdapter {
	
	private List<String> dateList;
	private int itemlayout =R.layout.spinner_item;
	private LayoutInflater inflater;
	
	public MySpinnerAdapter(Context context,List<String> dateList){
		this.dateList=dateList;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dateList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dateList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentview, ViewGroup arg2) {
		contentview=inflater.inflate(itemlayout, null);
		TextView tView=(TextView) contentview.findViewById(R.id.spinner_item_textone);
		tView.setText(dateList.get(position));
		return contentview;
	}

}
