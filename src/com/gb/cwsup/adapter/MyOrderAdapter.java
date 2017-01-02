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

public class MyOrderAdapter extends BaseAdapter {
	
	private Context context;
	private int laoutyid;
	private LayoutInflater inflater;
	private List<OrderItem> orderItems;
	
	public MyOrderAdapter(Context context, int laoutyid, List<OrderItem> orderItems) {
		this.context=context;
		this.laoutyid=laoutyid;
		this.orderItems=orderItems;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return orderItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return orderItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int postion, View contentView, ViewGroup arg2) {
		MyViewHolder vHolder;
		if (contentView==null) {
			contentView=inflater.inflate(laoutyid, null);
			vHolder=new MyViewHolder();
			vHolder.productTv=(TextView) contentView.findViewById(R.id.my_orderitem_productName);
			vHolder.TimeTV=(TextView) contentView.findViewById(R.id.my_orderitem_time);
			vHolder.snTV=(TextView) contentView.findViewById(R.id.my_orderitem_SN);
			vHolder.priceTV=(TextView) contentView.findViewById(R.id.my_orderitem_porice);
			vHolder.engNameTV=(TextView) contentView.findViewById(R.id.my_orderitem_engineerName);
			contentView.setTag(vHolder);
		}else {
			vHolder=(MyViewHolder) contentView.getTag();
		}
		OrderItem item=orderItems.get(postion);
		
		vHolder.productTv.setText(item.getProduct());
		vHolder.TimeTV.setText(item.getCreatdate());
		vHolder.snTV.setText(item.getSn());
		vHolder.priceTV.setText("￥"+item.getPrice());
		vHolder.engNameTV.setText(item.getEngineerName());
		return contentView;
	}
	
	private  class MyViewHolder{
		public TextView productTv,TimeTV,snTV,priceTV,engNameTV;
	}

}
