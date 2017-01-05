package com.gb.cwsup.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.GsonUtil;
import com.gb.cwsup.utils.ToastUtil;
import com.google.gson.Gson;

public class MapActivity extends BaseActivity implements OnClickListener {

	private ImageView centerico;

	/** 当前地图中点地址 **/
	public static String CARADD = "请选择地址";
	private BaiduMap mBaiduMap;
	private MapView mMapView = null;
	private Marker marker;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new LbsLocationListener();
	public static BDLocation mlocation;
	private boolean isFristLoc = true;
	private Double mCurLantitude, mCurLongitude;
	// 创建地理编码检索实例
	private GeoCoder geoCoder;
	
	private TextView address;
	double lat,lng;
	Button sureBut;

	@Override
	protected void onCreate(Bundle paramBundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(paramBundle);
		setContentView(R.layout.map);
		ActivityManagerUtil.getInstance().addToList(this);
		initview();
		initMAP();
	}

	@SuppressLint("NewApi")
	private void initview() {
		address=(TextView) findViewById(R.id.map_address);
		sureBut=(Button) findViewById(R.id.map_sure);
		sureBut.setOnClickListener(this);
		centerico = (ImageView) findViewById(R.id.map_center_ico);
		if (!getIntent().getStringExtra("icoflag").isEmpty()) {
			centerico.setVisibility(View.VISIBLE);
		}
	}

	private void initMAP() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		initListener();//监听地图拖动，获取地图中心点信息
		mMapView = (MapView) findViewById(R.id.bmapView);
		displaylocation();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
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
		mBaiduMap.setMaxAndMinZoomLevel(1, 20);

		mBaiduMap.setMyLocationEnabled(true); // 设置是否允许定位图层
		mMapView.showZoomControls(false); // 隐藏缩放按钮
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
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		if (geoCoder!=null)
			geoCoder.destroy(); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	public class LbsLocationListener implements BDLocationListener {
		private Gson gson = GsonUtil.getInstance();

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MapActivity.this.mlocation = location;
			mCurLantitude = location.getLatitude();
			mCurLongitude = location.getLongitude();
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.bulue_point);
			MyLocationConfiguration mLocCfg = new MyLocationConfiguration(com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(mLocCfg);

			Log.i("initseekbar", "定位成功！");
			// 第一次定位时，将地图位置移动到当前位置
			if (isFristLoc) {
				CARADD = location.getAddrStr();
				getanddisplayinfo();
				isFristLoc = false;
				displayNowLocation();
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

		/**
		 * 刷新服务类型数据
		 */
		public void getanddisplayinfo() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("latitude", String.valueOf(mlocation.getLatitude()));
			map.put("longitude", String.valueOf(mlocation.getLongitude()));
			map.put("radius", String.valueOf(10));// 默认10公里
		}

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
		lat = mCenterLatLng.latitude;
		lng = mCenterLatLng.longitude;
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
				ToastUtil.showToastShort(MapActivity.this, "抱歉，未能找到结果");
			}
			CARADD = result.getAddress();
			address.setText(CARADD);
//			ToastUtil.showToastShort(MapActivity.this, "位置：" + result.getAddress());
		}

		// 地理编码查询结果回调函数
		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有检测到结果
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.map_sure:
			sendlocation();
			break;

		default:
			break;
		}
		
	}

	private void sendlocation() {
		Intent intent=new Intent(this,AddAddressActivity.class);
		intent.putExtra("type", "MAP");
		intent.putExtra("CARADD", CARADD);
		intent.putExtra("lat", lat);
		intent.putExtra("lng", lng);
		intent.putExtra("areaId", mlocation.getCityCode());
		startActivity(intent);
		finish();
	}
}
