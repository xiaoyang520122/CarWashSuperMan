package com.gb.cwsup.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.gb.cwsup.AppApplication;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.CollectEngineerActivity;
import com.gb.cwsup.activity.EngineerInfoActivity;
import com.gb.cwsup.activity.order.WaitSureOrderActivity;
import com.gb.cwsup.adapter.MyPopupGalleryAdapter;
import com.gb.cwsup.entity.BaseBean;
import com.gb.cwsup.entity.EngineerOld;
import com.gb.cwsup.entity.Product;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.interfaces.ICallBack;
import com.gb.cwsup.utils.DialogUtil;
import com.gb.cwsup.utils.GsonUtil;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.GActivity;

public class MapListsFragment extends Fragment implements OnClickListener {

	private View fragmentview;
	/** 当前地图中点地址 **/
	public static String CARADD = "请选择地址";
	public static String CITY_ID="";
	private WindowManager wm;

	// 定义路径搜索对象
	// MKSearch mkSearch = null;
	MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private Marker marker;
	/** 产品ID **/
	public static String PRODUCT_ID;

	/** 产品位置 **/
	public static int PRODUCT_POINT;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new LbsLocationListener();
	public static BDLocation mlocation;
	private boolean isFristLoc = true;
	private Double mCurLantitude, mCurLongitude;

	/** 2016 */
	private TextView yuyuetv, onkeyyuyue;
	private PopupWindow popWnd;
	private Gallery gallery;
	private View contentView;
	private Activity activity;
	private Button locButton;
	private ImageView detail_loading;
	public static List<EngineerOld> ENGS;
	/** 新增功能 **/
	private RelativeLayout seekBarView;
	private ImageView bgimg, ontouchimg;
	private int margingleft, eventpoint;
	private float seekBarX, bglenght;
	private TextView Producttv, pricetv;
	private final int GET_PRODUCT_LIST = 2;
	private JSONArray productjsonarray;
	public final static int SET_NEWSLIST = 0;
	private Resources resources;
	private Drawable drawable;
	private Intent intent;
	// 创建地理编码检索实例
	private GeoCoder geoCoder;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fragmentview = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_maps_lists, null);
		mMapView = (MapView) fragmentview.findViewById(R.id.bmapView);
		// 获取地图控件引用
		mMapView = (MapView) fragmentview.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		initListener();
		initinfo();
		initview(fragmentview);
		initseekbar(fragmentview);
		return fragmentview;
	}

	private void initinfo() {
		displaylocation();
		// setMarker();
		mLocationClient = new LocationClient(getActivity().getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation();
		mLocationClient.start();
		mMapView.removeViewAt(1); // 去掉百度logo
		mMapView.showScaleControl(false);// 不显示地图上比例尺
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 卫星地图
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// 设置地图最大以及最小缩放级别，地图支持的最大最小级别分别为[3-20]
		mBaiduMap.setMaxAndMinZoomLevel(3, 20);

		mBaiduMap.setMyLocationEnabled(true); // 设置是否允许定位图层
		mMapView.showZoomControls(false); // 隐藏缩放按钮
		// 对 marker 添加点击相应事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				gallery.setSelection(marker.getZIndex());
				Animation myAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popshow_anim);
				contentView.startAnimation(myAnimation);
				popWnd.showAtLocation(fragmentview, Gravity.BOTTOM, 0, 0);
				return false;
			}
		});
	}

	/**
	 * 显示定位坐标
	 */
	@SuppressLint("NewApi")
	private void displaylocation() {
		// 设定默认中心点坐标
		LatLng cenpt = new LatLng(22.540716, 114.067566);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		isFristLoc = true;
		if (mlocation != null) {
			displayNowLocation();
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		if (mLocationClient != null)
			mLocationClient.stop();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		if (geoCoder != null)
			geoCoder.destroy();
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	// @Override
	// public void onRestoreInstanceState(Bundle savedInstanceState) {
	// super.onRestoreInstanceState(savedInstanceState);
	// mMapView.onRestoreInstanceState(savedInstanceState);
	// }

	private void opentrafic() {
		// 开启交通图
		mBaiduMap.setTrafficEnabled(true);
	}

	/**
	 * 设置定位参数
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 10000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	/**
	 * EventBus接收定位数据
	 * 
	 * @param location
	 */

	public class LbsLocationListener implements BDLocationListener, ICallBack {
		private Gson gson = GsonUtil.getInstance();

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			CITY_ID=location.getCityCode();
			MapListsFragment.this.mlocation = location;
			mCurLantitude = location.getLatitude();
			mCurLongitude = location.getLongitude();
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.bulue_point);
			MyLocationConfiguration mLocCfg = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(mLocCfg);

			Log.i("initseekbar", "定位成功！");
			// 第一次定位时，将地图位置移动到当前位置
			if (isFristLoc) {
				getProductlist(CITY_ID);
				CARADD = location.getAddrStr();
				getanddisplayinfo();
				isFristLoc = false;
				displayNowLocation();
			}
		}

		/**
		 * 刷新服务类型数据
		 */
		public void getanddisplayinfo() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("latitude", String.valueOf(mlocation.getLatitude()));
			map.put("longitude", String.valueOf(mlocation.getLongitude()));
			map.put("radius", String.valueOf(10));// 默认10公里
			// map.put("category", category);//
			BaseActivity.baseCheckInternet(activity, URLs.GET_ENGINEERS, map, this, true, true);

		}

		@Override
		public void excuteCallback(BaseBean baseBean) {
			if ((baseBean != null) && (baseBean.isDoStatu())) {
				try {
					ENGS = this.gson.fromJson(baseBean.getDoObject(), new TypeToken<List<EngineerOld>>() {
					}.getType());
					BuildMarker(ENGS);
					gallery.setAdapter(new MyPopupGalleryAdapter(activity, R.layout.popupwindow_item, ENGS));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 创建marker并转化为bitmap添加到地图
		 * 
		 * @param i
		 * @return
		 */
		private void BuildMarker(List<EngineerOld> englist) {
			/** 先移除所有的marker **/
			mBaiduMap.clear();
			for (int i = 0; i < englist.size(); i++) {
				EngineerOld eng = englist.get(i);
				View view = LayoutInflater.from(activity).inflate(R.layout.overlay_engineer2, null);
				double Latitude = eng.getLatitude();
				double Longitude = eng.getLongitude();
				LatLng point = new LatLng(Latitude, Longitude);// 定义Maker坐标点
				BitmapDescriptor bitmap;
				if (AppApplication.MARKER_TYPE) {
					bitmap = BitmapDescriptorFactory.fromBitmap(setViewInfo(eng, view));// 构建Marker图标
				} else {
					bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_short2);// 构建Marker图标1
				}

				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions options = new MarkerOptions().position(point) // 设置marker的位置
						.icon(bitmap) // 设置marker图标
						.zIndex(9); // 设置marker所在层级
				// .draggable(true); // 设置手势拖拽
				// 将marker添加到地图上
				marker = (Marker) mBaiduMap.addOverlay(options);
				// Overlay overlay=mBaiduMap.addOverlay(options);
				// moverlaylist.add(overlay);//加入集合方便移除
				// Marker marker = (Marker) overlay;
				marker.setZIndex(i);
			}
		}
	}

	/**
	 * 刷新到当前位置
	 */
	private void displayNowLocation() {
		LatLng geoPoint = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());
		MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLngZoom(geoPoint, 16);
		mBaiduMap.animateMapStatus(mapUpdate);
	}

	private Bitmap setViewInfo(EngineerOld eng, View view) {
		TextView engName = (TextView) view.findViewById(R.id.engineer_name);
		TextView engskillar = (TextView) view.findViewById(R.id.engineer_skillcar);
		engName.setText(eng.getName());
		/** 一下为设置公里数 **/
		Double latitude = Double.valueOf(mlocation.getLatitude());
		Double longitude = Double.valueOf(mlocation.getLongitude());
		Log.i("initseekbar", latitude + " " + longitude);
		double jwd = Math.sqrt(Math.pow(latitude - Double.valueOf(eng.getLatitude()), 2) + Math.pow(longitude - Double.valueOf(eng.getLongitude()), 2));// 勾股定理
		double con = 111;// 1个经纬度为111公里
		engskillar.setText(String.format(activity.getString(R.string.ed_distance), jwd * con));
		return getViewBitmap(view);
	}

	/**
	 * 
	 * @param view
	 */

	private void initview(View view) {
		Producttv = (TextView) view.findViewById(R.id.SB_product_name);
		pricetv = (TextView) view.findViewById(R.id.SB_product_price);
		resources = getResources();
		locButton = (Button) view.findViewById(R.id.main_location_button);
		locButton.setOnClickListener(this);
		yuyuetv = (TextView) view.findViewById(R.id.map_yuyue_order);
		onkeyyuyue = (TextView) view.findViewById(R.id.map_onkey_order);
		yuyuetv.setOnClickListener(this);
		onkeyyuyue.setOnClickListener(this);
		initgallery();
		// getActivity()
	}

	/**
	 * @param view
	 */
	private void initgallery() {
		contentView = getcontentview();
		popWnd = new PopupWindow(getActivity());
		popWnd.setContentView(contentView);
		popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		popWnd.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		popWnd.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// popWnd.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popWnd.setBackgroundDrawable(dw);
		// contentView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		contentViewsetontouch();
	}

	/** 设置seekbar只能只能停留在指定的五个位置上 bglenght,startX,startY,moveX,moveY; */
	@SuppressLint("NewApi")
	private void initseekbar(View view) {
		bgimg = (ImageView) view.findViewById(R.id.baner_img);// 背景条
		ontouchimg = (ImageView) view.findViewById(R.id.ontouchimg);
		seekBarView = (RelativeLayout) view.findViewById(R.id.SeekBarview);// 拖动滑块
		int[] location = new int[2];
		bgimg.getLocationOnScreen(location);
		bglenght = bgimg.getWidth();
		margingleft = (int) bgimg.getLeft();
		doontouchshing();
	}

	/**
	 * 注册并添加ontouch事件
	 */
	@SuppressLint("NewApi")
	private void doontouchshing() {
		if (wm == null) {
			wm = getActivity().getWindowManager();
		}
		

		seekBarView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("NewApi")
			float startx = 0.0f;
			float movex = 0.0f;
			float seekBarstartX = 0.0f;
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				@SuppressWarnings("deprecation")
				int scwidth = wm.getDefaultDisplay().getWidth();
				seekBarX = seekBarView.getX();
				bglenght = bgimg.getWidth();
				final int leftwidth = (int) (scwidth * 0.13);
				final float seekX_X = leftwidth + bglenght-seekBarView.getWidth();
				final int ban = (int) (scwidth * 0.0731);
				final int tban = (int) (scwidth * 0.1463);
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					seekBarstartX = seekBarView.getX();
					startx = event.getRawX();
					break;

				case MotionEvent.ACTION_MOVE:
					movex = event.getRawX();
					float seekBarnowX = seekBarstartX + movex - startx;
					
					if ((seekBarnowX+leftwidth) > leftwidth && (seekBarnowX+leftwidth) < seekX_X) {
						seekBarView.setX((int) seekBarnowX);
						setProductmasg((int) ((seekBarnowX + ban) / tban));
						PRODUCT_POINT=(int)(seekBarnowX + ban) / tban;
					}
					break;
					
				case MotionEvent.ACTION_UP:
					int endpoint = PRODUCT_POINT;
					seekBarView.setX(endpoint * 158);
					break;

				}
				return true;
			}
		});
	}

	/**
	 * 获取产品信息
	 */
	private void getProductlist(final String cityID) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("requst_code", "定位成功后，发送请求列表信息");
				String url = URLs.GET_PRODUCT_LIST + "&baiduid=" + cityID;
				JsonHttpUtils.getHtmlString(url, null, handler, GET_PRODUCT_LIST, getActivity());
			}
		}).start();
	}

	/**
	 * 所有服务选择按钮的单击事件
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.main_location_button:
			displayNowLocation();
			// requestLocClick();//手动触发一次定位请求,同时请求一次数据
			break;
		case R.id.map_onkey_order:
			onekeyorder();// one key order
			break;

		case R.id.map_yuyue_order:
			yuyueorder();// yuyue order
			break;
		}
	}

	private void yuyueorder() {
		intent = new Intent(getActivity(), CollectEngineerActivity.class);
		intent.putExtra("type", "choice");
		getActivity().startActivity(intent);
	}

	private void onekeyorder() {
		try {
			intent = new Intent();
			intent.setClass(getActivity(), WaitSureOrderActivity.class);
			intent.putExtra("engineerid", (ENGS.get(0).getId() + ""));
			intent.putExtra("type", "onkey");
			intent.putExtra("EngineerOld", ENGS.get(0));
			startActivity(intent);
		} catch (Exception e) {
			DialogUtil.getAlertDialog(getActivity(), "无法获取网络信息！").show();
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			try {
				switch (msg.what) {
				case SET_NEWSLIST:
					detail_loading.setVisibility(View.GONE);
					// mAdapter = new EngineerAdapter(activity, engineerList);
					// mMapView.setAdapter(mAdapter);
					break;
				case 1:
					popWnd.dismiss();
					break;
				case GET_PRODUCT_LIST:
					if (msg.obj != null) {
						Type type = new TypeToken<List<Product>>() {
						}.getType();
						Log.i("requst_code", (String) msg.obj);
						JSONObject object = new JSONObject((String) msg.obj);
						JSONObject object2 = object.getJSONObject("data");
						productjsonarray = object2.getJSONArray("content");
						Log.i("requst_code", "productjsonarray=" + productjsonarray.toString());
						AppApplication.PRODUCTLIST = GsonUtil.getInstance().fromJson(productjsonarray.toString(), type);
						Log.i("requst_code", AppApplication.PRODUCTLIST.size() + "");
							setProductmasg(PRODUCT_POINT);
						
					} else {
						showDilog();
					}

					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 设置滑干上产品信息
	 * 
	 * @throws JSONException
	 */
	private void setProductmasg(int i) {
		JSONObject object;
		try {
			if (productjsonarray.getJSONObject(i) == null)
				return;
			;
			object = productjsonarray.getJSONObject(i);
			PRODUCT_ID = object.getString("id");
			Producttv.setText(object.getString("shortName"));
			pricetv.setText(object.getString("price") + "￥");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showDilog() {
		new AlertDialog.Builder(getActivity()).setTitle("网络异常！").setMessage("无法请求网络，请检查您的网络是否畅通！").setPositiveButton("确定", null).create().show();
	}

	private View getcontentview() {
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.engineer_popupwindow, null);
		gallery = (Gallery) contentView.findViewById(R.id.engineer_Gallery);
		// gallery.setAdapter(new MyPopupGalleryAdapter(getActivity(),
		// R.layout.popupwindow_item));

		gallery.setOnItemClickListener(new OnItemClickListener() {
			Intent intent;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				intent = new Intent(getActivity(), EngineerInfoActivity.class);
				getActivity().startActivity(intent);
			}
		});
		return contentView;
	}

	/**
	 * contentView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框 和返回键监听
	 */
	private void contentViewsetontouch() {
		contentView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = contentView.findViewById(R.id.engineer_Gallery).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						setcontentviewonTouch();
					}
				}
				return true;
			}
		});
		/** 点击返回键是动画关闭 目前不起作用 */
		contentView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
					setcontentviewonTouch();
				}
				return true;
			}
		});
	}

	/**
	 * tpopupwindow中的Gally设置区域为单击关闭popupwindow的监听事件
	 */
	private void setcontentviewonTouch() {
		Animation myAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.pophidden_anim);
		contentView.startAnimation(myAnimation);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/** 将View对象装换为Bitmp对象的方法（注；View对象的最外层必须为Linearlayout包裹） **/

	private Bitmap getViewBitmap(View addViewContent) {

		addViewContent.setDrawingCacheEnabled(true);

		addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());
		addViewContent.buildDrawingCache();
		Bitmap cacheBitmap = addViewContent.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		return bitmap;
	}

	/**
	 * 地图状态改变
	 */
	private void initListener() {
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			@Override
			public void onMapStatusChangeStart(MapStatus status) {
				// updateMapState(status);
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				updateMapState(status);
			}

			@Override
			public void onMapStatusChange(MapStatus status) {
				// updateMapState(status);
			}
		});
	}

	private void updateMapState(MapStatus status) {
		LatLng mCenterLatLng = status.target;
		/** 获取经纬度 */
		double lat = mCenterLatLng.latitude;
		double lng = mCenterLatLng.longitude;
		getlocationNAME(mCenterLatLng);
		// ToastUtil.showToastShort(getActivity(), "经纬度：" + lat + "  " + lng);
	}

	private void getlocationNAME(LatLng latLng) {
		if (geoCoder == null) {
			geoCoder = GeoCoder.newInstance();
			// 设置地理编码检索监听者
			geoCoder.setOnGetGeoCodeResultListener(coderlistener);
		}
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		// 释放地理编码检索实例
		// geoCoder.destroy();
	}

	OnGetGeoCoderResultListener coderlistener = new OnGetGeoCoderResultListener() {
		// 反地理编码查询结果回调函数
		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有检测到结果
				ToastUtil.showToastShort(getActivity(), "抱歉，未能找到结果");
			}
			CARADD = result.getAddress();
			Toast toast= Toast.makeText(getActivity(), "位置：" + result.getAddress(), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
		}

		// 地理编码查询结果回调函数
		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有检测到结果
			}
		}
	};
}
