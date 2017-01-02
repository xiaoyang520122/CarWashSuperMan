package com.gb.cwsup;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.gb.cwsup.activity.CarListActivity;
import com.gb.cwsup.activity.EditerCenterActivity;
import com.gb.cwsup.activity.MessageCenterActivity;
import com.gb.cwsup.activity.MyOrderActivity;
import com.gb.cwsup.activity.MyPropertyActivity;
import com.gb.cwsup.activity.ServePriceActivity;
import com.gb.cwsup.fragment.MapListsFragment;
import com.gb.cwsup.fragment.MyMenuFragment;
import com.gb.cwsup.fragment.ServerCenterFragment;
import com.gb.cwsup.register.LoingActivity;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.LoadingDialog;
import com.gb.cwsup.utils.ToastUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends BaseActivity implements OnClickListener {

	private FragmentManager fm;
	// private FragmentTransaction ft;
	private Fragment mapFragment, myMenuFragment,servercenterFragment;
	private Intent intent;
	int i;
	private TextView carfrend_tv, carlife_tv, carserve_tv, carcenter_tv, carmy_tv;
	Resources resources;
	Drawable drawable;
	private SlidingMenu menu;
	private String mobile;
	private LoadingDialog lodingdialog;
	public static boolean FirstLogingFlag=true;
	private boolean menushowflag=false;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		ActivityManagerUtil.getInstance().addToList(this);
		isLoging();
		initTitle();
		initview();
	}

	private void initTitle() {
		setmoreoclicklistener();
		setLeftImgOclicklistener();
	}
	
	private void setmoreoclicklistener(){
		ImageView moreImageView=(ImageView) findViewById(R.id.main_more_img);
		moreImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopupWindow(view);
			}
		});
	}
	private void setLeftImgOclicklistener(){
		ImageView headView=(ImageView) findViewById(R.id.main_action_left_img);
		headView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (menushowflag) {
					menu.toggle();;
					menushowflag=false;
				}else {
					shoumenuu();
					menushowflag=true;
				}
			}
		});
	}

	private void initview() {
		resources = getResources();
		this.fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		carfrend_tv = (TextView) findViewById(R.id.bm_tv_frend);
		carlife_tv = (TextView) findViewById(R.id.bm_tv_life);
		carserve_tv = (TextView) findViewById(R.id.bm_tv_serve);
		carcenter_tv = (TextView) findViewById(R.id.bm_tv_center);
		carmy_tv = (TextView) findViewById(R.id.bm_tv_my);
		/** 默认显示地图Fragment **/
		if (mapFragment==null) {
			mapFragment = new MapListsFragment();
			ft.add(R.id.am_fragment, mapFragment);
		}
//		ft.addToBackStack(null);
		ft.show(mapFragment);
		ft.commit();
		viewsetonclick();
		initslidingmenu();
	}

	/**
	 * 为每一个底部导航设置单击事件
	 */
	private void viewsetonclick() {
		carfrend_tv.setOnClickListener(this);
		carlife_tv.setOnClickListener(this);
		carserve_tv.setOnClickListener(this);
		carcenter_tv.setOnClickListener(this);
		carmy_tv.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		FragmentTransaction ft = fm.beginTransaction();
		switch (view.getId()) {
		case R.id.bm_tv_frend:
			recoverIcoColor();
			drawable = resources.getDrawable(R.drawable.main_table_ico_one_h);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界
			carfrend_tv.setTextColor(0xff07B6F3);
			carfrend_tv.setCompoundDrawables(null, drawable, null, null);
			break;
		case R.id.bm_tv_life:
			recoverIcoColor();
			drawable = resources.getDrawable(R.drawable.main_table_ico_two_h);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界
			carlife_tv.setTextColor(0xff07B6F3);
			carlife_tv.setCompoundDrawables(null, drawable, null, null);
			break;

		case R.id.bm_tv_serve:
			recoverIcoColor();
			drawable = resources.getDrawable(R.drawable.main_table_ico_midle_h);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
			carserve_tv.setTextColor(0xff07B6F3);
			carserve_tv.setCompoundDrawables(null, drawable, null, null);
//			ft.replace(R.id.am_fragment, mapFragment);
			hideFragment(ft);
			if (mapFragment==null) {
				mapFragment = new MapListsFragment();
				ft.add(R.id.am_fragment, mapFragment);
			}
			ft.show(mapFragment);
			ft.commit();
			break;

		case R.id.bm_tv_center:
			recoverIcoColor();
			drawable = resources.getDrawable(R.drawable.main_table_ico_three_h);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界
			carcenter_tv.setTextColor(0xff07B6F3);
			carcenter_tv.setCompoundDrawables(null, drawable, null, null);
			hideFragment(ft);
			if (servercenterFragment==null) {
				servercenterFragment = new ServerCenterFragment();
				ft.add(R.id.am_fragment, servercenterFragment);
			}
			ft.show(servercenterFragment);
			ft.commit();
			break;

		case R.id.bm_tv_my:
			recoverIcoColor();
			drawable = resources.getDrawable(R.drawable.main_table_ico_four_h);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界
			carmy_tv.setTextColor(0xff07B6F3);
			carmy_tv.setCompoundDrawables(null, drawable, null, null);
			hideFragment(ft);
			if (myMenuFragment==null) {
				myMenuFragment = new MyMenuFragment();
				ft.add(R.id.am_fragment, myMenuFragment);
			}
			ft.show(myMenuFragment);
			ft.commit();
			break;
		case R.id.leftmenu_order_now:
			this.startActivity(new Intent(this, MyOrderActivity.class));
			break;
		case R.id.leftmenu_mycar:
			this.startActivity(new Intent(this, CarListActivity.class));
			break;
		case R.id.leftmenu_history_order:
			this.startActivity(new Intent(this, MyOrderActivity.class));
			break;
		case R.id.leftmenu_mymoney:
			this.startActivity(new Intent(this, MyPropertyActivity.class));
			break;
		case R.id.leftmenu_mymsg:
			this.startActivity(new Intent(this, MessageCenterActivity.class));
			break;
		case R.id.leftmenu_excit:
			@SuppressWarnings("deprecation")
			SharedPreferences sp=getSharedPreferences("register_info", Context.MODE_PRIVATE);
			Editor editor =sp.edit();
			editor.putString("mobile", "");
			editor.commit();
			MainActivity.FirstLogingFlag=true;
			startActivity(new Intent(this, LoingActivity.class));
			this.finish();
			break;
		case R.id.leftmenu_edit:
			intent=new Intent(this, EditerCenterActivity.class);
			startActivity(intent);
			break;
		case R.id.leftmenu_myprice:
			intent=new Intent(this, ServePriceActivity.class);
			startActivity(intent);
			break;
//		case R.id.leftmenu_my:
//			this.startActivity(new Intent(this, .class));
//			break;
		}
	}
	
	  //隐藏所有的fragment  
    private void hideFragment(FragmentTransaction transaction){ 
    	if (myMenuFragment!=null) {
    		transaction.hide(myMenuFragment);
		}
    	if (mapFragment!=null) {
    		transaction.hide(mapFragment);
		}
    	if (servercenterFragment!=null) {
    		transaction.hide(servercenterFragment);
		}
    }
    

	/** 恢复底部图标颜色 **/
	private void recoverIcoColor() {

		drawable = resources.getDrawable(R.drawable.main_table_ico_one);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		carfrend_tv.setTextColor(0xffb5b5b5);
		carfrend_tv.setCompoundDrawables(null, drawable, null, null);

		drawable = resources.getDrawable(R.drawable.main_table_ico_two);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		carlife_tv.setTextColor(0xffb5b5b5);
		carlife_tv.setCompoundDrawables(null, drawable, null, null);

		drawable = resources.getDrawable(R.drawable.main_table_ico_midle);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		carserve_tv.setTextColor(0xffb5b5b5);
		carserve_tv.setCompoundDrawables(null, drawable, null, null);

		drawable = resources.getDrawable(R.drawable.main_table_ico_three);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		carcenter_tv.setTextColor(0xffb5b5b5);
		carcenter_tv.setCompoundDrawables(null, drawable, null, null);

		drawable = resources.getDrawable(R.drawable.main_table_ico_four);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		carmy_tv.setTextColor(0xffb5b5b5);
		carmy_tv.setCompoundDrawables(null, drawable, null, null);
	}

	/**
	 * 初始化侧滑菜单并设置
	 */
	public void initslidingmenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width_menu);
		menu.setShadowDrawable(R.drawable.shadow_two);
		//
		
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset_two);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		/**
		 * SLIDING_WINDOW will include the Title/ActionBar in the content
		 * section of the SlidingMenu, while SLIDING_CONTENT does not.
		 */
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 为侧滑菜单设置布局
		menu.setMenu(R.layout.leftmenu);
		getmenuView();
	}

	private void getmenuView() {
		View view=menu.getMenu();
		if (view!=null) {
			TextView name=(TextView) view.findViewById(R.id.leftmenu_name);
			if (sp==null) {
				sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
			}
			name.setText(sp.getString("name", ""));
			view.findViewById(R.id.leftmenu_order_now).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_mycar).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_history_order).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_mymoney).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_mymsg).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_myprice).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_excit).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_edit).setOnClickListener(this);
			view.findViewById(R.id.leftmenu_my).setOnClickListener(this);
		}
	}

	/**
	 * 显示侧滑菜单的方法
	 */
	public void shoumenuu() {
		menu.showMenu();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isLoging();
	}
	
	/**1秒计时标志 是否可以退出**/
	private boolean timeflag=true;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
           if(timeflag){
        	   timeflag=false;
        	   ToastUtil.showToastShort(this, "再按一次退出!");
        	   Timer timer=new Timer();
        	   timer.schedule(new TimerTask() {
				@Override
				public void run() {
					 timeflag=true;
				}
			}, 1000);
           }else{
        	   timeflag=true;
        	   finish();
           }
            return true;
        }
        return super.onKeyDown(keyCode, event);
	}

	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(R.layout.main_more_menu, null);
		// 设置按钮的点击事件
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ToastUtil.showToastShort(MainActivity.this,  "button is pressed");
//			}
//		});

		final PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("LONGING", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		// 实例化一个ColorDrawable颜色透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(dw);
		// 设置好参数之后再show
		popupWindow.showAsDropDown(view);

	}
	/**
	 * 判断是否够登陆状态不在登陆状态就转到登陆界面
	 */
	@SuppressLint("NewApi")
	private void isLoging() {
		if (!FirstLogingFlag) 
			return;
		FirstLogingFlag=false;
		sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		String code = sp.getString("mobile", "");
		if (TextUtils.isEmpty(code)) {
			Intent intent = new Intent(this, LoingActivity.class);
			startActivity(intent);
			ActivityManagerUtil.getInstance().finishAllActivity();
		}
	}
}
