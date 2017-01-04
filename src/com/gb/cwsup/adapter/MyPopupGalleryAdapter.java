package com.gb.cwsup.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.order.WaitSureOrderActivity;
import com.gb.cwsup.entity.EngineerOld;
import com.gb.cwsup.fragment.MapListsFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPopupGalleryAdapter extends BaseAdapter implements OnClickListener {
	
	private Context context;
	private int laoutyid;
	private LayoutInflater inflater;
	private List<EngineerOld> engs;
	private long engineerid;
	private EngineerOld meng;
	
	public MyPopupGalleryAdapter(Context context,int laoutyid,List<EngineerOld> engs){
		this.context=context;
		this.laoutyid=laoutyid;
		inflater=LayoutInflater.from(context);
		this.engs=engs;
	}

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
	public View getView(int postion, View contentView, ViewGroup arg2) {
		 View view = inflater.inflate(laoutyid, null);
		MyViewHolder holder=null;
		if (contentView != null) {
			view=contentView;
			holder=(MyViewHolder)view.getTag();
		}else {
			holder=new MyViewHolder();
			contentView=inflater.inflate(laoutyid, null);
			holder.headimg=(ImageView) view.findViewById(R.id.popupwindow_item_img);
			holder.nametv=(TextView) view.findViewById(R.id.popupwindow_item_name);
			holder.timetv=(TextView) view.findViewById(R.id.popupwindow_item_time);
			holder.fromtv=(TextView) view.findViewById(R.id.popupwindow_item_from);
			holder.sumtv=(TextView) view.findViewById(R.id.popupwindow_item_orderCount);
			view.setTag(holder);
		}
		meng=engs.get(postion);
		engineerid=meng.getId();
		try {
			Glide.with(context)  
			.load(R.drawable.main_table_ico_midle)  
			.placeholder(R.drawable.main_table_ico_midle)  
			.into(holder.headimg);
		} catch (Exception e) {
			Log.e("requst_code", "**********加载不了图片*************");
			e.printStackTrace();
		}
		
		holder.nametv.setText(meng.getName());
		holder.sumtv.setText(meng.getOrderCount()+"次");
		/** 以下为设置公里数 **/
		Double latitude = Double.valueOf(MapListsFragment.mlocation.getLatitude());
		Double longitude = Double.valueOf(MapListsFragment.mlocation.getLongitude());
		Log.i("initseekbar", latitude + " " + longitude);
		double jwd = Math.sqrt(Math.pow(
				latitude - Double.valueOf(meng.getLatitude()), 2)
				+ Math.pow(longitude - Double.valueOf(meng.getLongitude()), 2));// 勾股定理1
		double con = 111;// 1个经纬度为111公里
		holder.fromtv.setText(String.format( context.getString(R.string.ed_distance), jwd * con));
		
		/** 以下为设置到达时间 **/
		String fee_time = (int)((jwd * con)/15*60)+"";
		holder.timetv.setText(fee_time+"Min");
		setonclicklistenler(view.findViewById(R.id.popupwindow_tvbutton), meng);
		return view;
	}
	
	public class MyViewHolder{
		public ImageView headimg;
		public TextView nametv,timetv,fromtv,sumtv;
	}
	
	private void setonclicklistenler(final View view,final EngineerOld eng1){
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(context, WaitSureOrderActivity.class);
				intent.putExtra("engineerid", eng1.getId());
				intent.putExtra("EngineerOld", eng1);
				intent.putExtra("type", "default");
				context.startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		
	}
	/**
	 * 一下类容为设置监听事件
	 */

}
