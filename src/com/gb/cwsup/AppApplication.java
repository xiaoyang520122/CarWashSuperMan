package com.gb.cwsup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.gb.cwsup.entity.PersistentCookieStore;
import com.gb.cwsup.entity.Product;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.entity.User;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.Md5Util;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class AppApplication extends Application {

	public static boolean MARKER_TYPE = true;

	private static final String TAG = "GetuiSdkDemo";
	private static DemoHandler handler;
	private List<NameValuePair> NVparames;
	private static AppApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	public static List<Product> PRODUCTLIST;
	public static String CID = "";
	public static User USER;
	public static SharedPreferences sp;

	public void setmBMapManager(BMapManager mBMapManager) {
		this.mBMapManager = mBMapManager;
	}

	public BMapManager getmBMapManager() {
		return mBMapManager;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		SDKInitializer.initialize(this);
		sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		getCID();
		Loginguser();
		EventBus.getDefault().register(this);
		setMarkerType();
		initImageLoader(getApplicationContext());
		initEngineManager(getApplicationContext());
	}
	
	private void getCID() {
		if (handler == null) {
			handler = new DemoHandler();
		}
		CID = sp.getString("clientID", "nomsg");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		// 整体摧毁的时候调用这个方法
	}

	public static AppApplication getInstance() {
		return mInstance;
	}

	/**
	 * 初始化百度地图
	 * 
	 * @param context
	 */
	public void initEngineManager(Context context) {
		// if (mBMapManager == null) {
		// mBMapManager = new BMapManager(context);
		// }
		//
		// if (!mBMapManager.init(strKey, new MyGeneralListener())) {
		// Toast.makeText( AppApplication.getInstance().getApplicationContext(),
		// "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		// }
	}

	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "topnews/Cache");// 获取到缓存的目录地址
		Log.d("cacheDir", cacheDir.getPath());
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		// .memoryCacheExtraOptions(480, 800) // max width, max
		// height，即保存的每个缓存文件的最大长宽
		// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,
		// null) // Can slow ImageLoader, use it carefully (Better don't
		// use it)设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024)) // You can pass your own memory cache
				// implementation你可以通过自己的内存缓存实现
				// .memoryCacheSize(2 * 1024 * 1024)
				// /.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
																		// 加密
				// .discCacheFileNameGenerator(new
				// HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheFileCount(100) //缓存的File数量
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
				// 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	// public static class MyGeneralListener implements MKGeneralListener {
	//
	// @Override
	// public void onGetNetworkState(int iError) {
	// if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	// Toast.makeText(
	// AppApplication.getInstance().getApplicationContext(),
	// "您的网络出错啦！", Toast.LENGTH_LONG).show();
	// } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	// Toast.makeText(
	// AppApplication.getInstance().getApplicationContext(),
	// "输入正确的检索条件！", Toast.LENGTH_LONG).show();
	// }
	// // ...
	// }
	//
	// @Override
	// public void onGetPermissionState(int iError) {
	// // 非零值表示key验证未通过
	// if (iError != 0) {
	// // 授权Key错误：
	// Toast.makeText(
	// AppApplication.getInstance().getApplicationContext(),
	// "请在 MyApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "
	// + iError, Toast.LENGTH_LONG).show();
	// AppApplication.getInstance().m_bKeyRight = false;
	// } else {
	// AppApplication.getInstance().m_bKeyRight = true;
	// // Toast.makeText(AppApplication.getInstance().getApplicationContext(),
	// // "key认证成功", Toast.LENGTH_LONG).show();
	// }
	// }
	//
	//
	// }
	private void setMarkerType() {
		boolean type = sp.getBoolean("markertype", true);
		MARKER_TYPE = type;
	}

	public PersistentCookieStore getPersistentCookieStore() {
		PersistentCookieStore cookieStore = new PersistentCookieStore(mInstance);
		return cookieStore;
	}

	public static void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public String getusername() {
		if (sp == null) {
			sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		}
		return sp.getString("name", "无信息");

	}

	public static class DemoHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case 0:
			// if (demoActivity != null) {
			// payloadData.append((String) msg.obj);
			// payloadData.append("\n");
			// if (GetuiSdkDemoActivity.tLogView != null) {
			// GetuiSdkDemoActivity.tLogView.append(msg.obj + "\n");
			// }
			// }
			// break;

			case 1:
				// if (demoActivity != null) {
				// if (GetuiSdkDemoActivity.tLogView != null) {
				// GetuiSdkDemoActivity.tView.setText((String) msg.obj);
				// }
				// }
				Editor editor = sp.edit();
				editor.putString("clientID", (String) msg.obj);
				editor.commit();
				CID = (String) msg.obj;
				break;
			}
		}
	}
	
	Handler mhandler=new Handler();
	private void Loginguser() {
		String mobile=sp.getString("mobile", "");
		if (TextUtils.isEmpty(mobile)) {
			return;
		}
		NVparames = new ArrayList<NameValuePair>(2);
		NVparames.add(new BasicNameValuePair("username", mobile));
		NVparames.add(new BasicNameValuePair("enPassword", Md5Util.MD5("123456")));
		NVparames.add(new BasicNameValuePair("cid",AppApplication.CID));
		new Thread() {
			@Override
			public void run() {
				super.run();
				JsonHttpUtils.doPost(URLs.LOGING_BY_PASS, NVparames, mhandler, 555, mInstance);
			}
		}.start();
	}
	
	@Subscribe(threadMode=ThreadMode.MAIN)
	public void eventloging(NameValuePair value){
		int code = Integer.valueOf(value.getName());
		switch (code) {
		case 555:
			islogingsuccess(value.getValue());
			break;

		default:
			break;
		}
	}

	private void islogingsuccess(String jsonstr) {
		try {
			JSONObject jo1=new JSONObject(jsonstr);
			JSONObject jo2=jo1.getJSONObject("message");
			JSONObject jo3=jo1.getJSONObject("data");
			if (jo2.getString("type").equals("success")) {
				getusermsg(jo3);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void getusermsg(JSONObject jo3) {
		USER=new User();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy年MM月dd日");
		try {
			long mi=Long.valueOf(jo3.getString("birth"));
			String birthstr=fmt.format(new Date(mi));
			
			USER.setId(jo3.getString("id"));
			USER.setBirth(birthstr);
			USER.setEmail(jo3.getString("email"));
			USER.setGender(jo3.getString("gender"));
			USER.setMobile(jo3.getString("mobile"));
			USER.setName(jo3.getString("name"));
			JSONObject jo4=jo3.getJSONObject("name");
			USER.setVip(jo4.getString("name"));
//			USER.setAreaId(jo3.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
