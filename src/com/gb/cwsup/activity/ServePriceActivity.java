package com.gb.cwsup.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.VoicemailContract;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.adapter.MyOrderAdapter;
import com.gb.cwsup.adapter.MyViewPagerAdapter;
import com.gb.cwsup.utils.ActivityManagerUtil;

public class ServePriceActivity extends BaseActivity implements OnClickListener {

	// 标题字样
	private TextView title1, title2;
	private ViewPager pager;
	private MyViewPagerAdapter pagerAdapter;
	private List<View> views;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回", "价目表", "···");
		setContentView(R.layout.serve_price);
		ActivityManagerUtil.getInstance().addToList(this);
		initview();
	}

	private void initview() {

		title1 = (TextView) findViewById(R.id.serveprice_title_tv1);
		title2 = (TextView) findViewById(R.id.serveprice_title_tv2);
		title1.setOnClickListener(this);
		title2.setOnClickListener(this);
		getviewdate();

		pager=(ViewPager) findViewById(R.id.serveprice_viewpagers);
		pagerAdapter = new MyViewPagerAdapter(views);
		setpagerontouch();

		pager.setAdapter(pagerAdapter);
		setLeftTvOnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ServePriceActivity.this.finish();
			}
		});
	}
	
	private void setpagerontouch() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {
				switch (postion) {
				case 0:
					changtitleTv(title1,title2,0);
					break;
				case 1:
					changtitleTv(title1,title2,1);
					break;

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}

	private void getviewdate(){
		views=new ArrayList<View>();
		TextView view=(TextView) LayoutInflater.from(this).inflate(R.layout.mytextview, null);
		view.setText(R.string.serveprice_str1);
		views.add(view);
		TextView view2=(TextView) LayoutInflater.from(this).inflate(R.layout.mytextview, null);
		view2.setText(R.string.serveprice_str2);
		views.add(view2);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.serveprice_title_tv1:
			changtitleTv(title1,title2,0);
			break;
		case R.id.serveprice_title_tv2:
			changtitleTv(title1,title2,1);
			break;

		default:
			break;
		}
	}
	/**
	 * 改变半圆背景和文字颜色
	 */
	private void changtitleTv(TextView title1,TextView title2,int flag){
		switch (flag) {
		case 0:
			pager.setCurrentItem(0);
			title2.setTextColor(Color.parseColor("#ff46b0da"));
			title1.setTextColor(Color.parseColor("#ffffffff"));
			title1.setBackgroundResource(R.drawable.corners_left_blue);
			title2.setBackgroundResource(R.drawable.corners_right_white);
			break;
		case 1:
			pager.setCurrentItem(1);
			title1.setTextColor(Color.parseColor("#ff46b0da"));
			title2.setTextColor(Color.parseColor("#ffffffff"));
			title1.setBackgroundResource(R.drawable.corners_left_white);
			title2.setBackgroundResource(R.drawable.corners_right_blue);
			break;
		}
		
	}

}
