package com.gb.cwsup.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.entity.PersistentCookieStore;
import com.gb.cwsup.entity.URLs;

public class JsonHttpUtils {

	public final static int LOGING_FLAG = 1001; //登陆
	public final static int REQUEST_COUPON_FLAG = 1002; //订单信息（优惠券等）
	public final static int LOGING_BY_PASS = 1003;//用户密码登陆（注册成功后系统自动后天登陆）
	public final static int OPEN_BOX=1004;//开箱
	public final static int SEN_PRODUCT_ID=1005;//发送订单产品ID
	public final static int GET_ORDER_MONEY = 1006;//用户密码登陆（注册成功后系统自动后tai登陆）
	public final static int GET_COUPON = 1007;//获取订单结算信息
	public final static int GET_ORDER = 1008;//获取订单结算信息
	public final static int UPDATE_OPEN_RESPONSE_MSG = 1009;//上传开箱成功后的密码等信息
	public final static int GET_ORDER_INFORMATION = 1010;//获取订单详情
	public final static int GET_ORDER_LIST = 1011;//获取订单列表
	public final static int SAVE_ADD = 1012;//保存地址
	public final static int GET_ADDS = 1013;//地址地址列表获取
	public final static int GET_CARS = 1014;//车辆列表获取
	public final static int SAVE_CAR = 1015;//车信息保存
	public final static int DELETE_CAR = 1016;//删除车信息
	public final static int UPDATE_CAR = 1017;//跟新车信息
	public final static int UPDATE_ADDRESS = 1018;//跟新地址信息
	public final static int DELETE_ADDRESS = 1019;//删除地址信息
	public final static int CLEAR_CART = 1020;//清空购物车产品
	public final static int UPDATE_USER_MSG = 1021;//修改用户信息
	public final static int CANCEL_ORDER = 1022;//修改用户信息
	
	
	
	/**通过Eventbus传递优惠券号码！**/
	public final static int COUPON_CODE = 3001;
	/**
	 * 下载html源文件
	 * 
	 * @param path
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getHtmlString(String minterface, List<String> parametes, Handler mhandler, int typecode, Context context) {
		String result = "";
		BufferedReader in = null;
		String path = minterface;
		/** 拼接请求地址 **/
		if (parametes != null) {
			path = minterface + "?";

			for (int i = 0; i < parametes.size(); i++) {
				path = path + parametes.get(i);
				if (i % 2 != 1) {
					path = path + "=";
				} else if (i < (parametes.size() - 1)) {
					path = path + "&";
				}
			}
		}
		Log.i("requst_code", "提交地址：" + path);
		try {
			 // 定义HttpClient  
            HttpClient client = new DefaultHttpClient();  
            // 实例化HTTP方法  
            HttpGet request = new HttpGet();  
            request.setURI(new URI(path));  
            HttpResponse response;
            Log.i("requst_code", "开始添加头："+typecode);
            if (GET_COUPON==typecode) {
            	PersistentCookieStore cookieStore = new PersistentCookieStore(AppApplication.getInstance().getApplicationContext());  
            	((AbstractHttpClient) client).setCookieStore(cookieStore);  
            	 Log.i("requst_code", "请求内容头："+typecode+"=="+cookieStore.hashCode());
            	response = client.execute(request); 
			}
            
           response = client.execute(request);  
            
            in = new BufferedReader(new InputStreamReader(response.getEntity()  
                    .getContent()));  
            StringBuffer sb = new StringBuffer("");  
            String line = "";  
            String NL = System.getProperty("line.separator");  
            while ((line = in.readLine()) != null) {  
                sb.append(line + NL);  
            }  
            in.close();  
            result = sb.toString();  
			
			
		} catch (Exception e) {
			Message msg = new Message();
			msg.what = typecode;
			msg.obj = null;
			mhandler.sendMessage(msg);// 发送下载字符串成功消息
			NameValuePair valuePair=new BasicNameValuePair(typecode+"", result);
			EventBus.getDefault().post(valuePair);
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			Message msg = new Message();
			msg.what = typecode;
			msg.obj = result;
			mhandler.sendMessage(msg);// 发送下载字符串成功消息
			NameValuePair valuePair=new BasicNameValuePair(typecode+"", result);
			EventBus.getDefault().post(valuePair);
		}
		return result;
	}

	/**
	 * Post请求数据
	 */
	@SuppressLint("NewApi")
	public static String SendPost(String minterface, List<String> parametes, Handler mhandler, int typecode, Context context) {
		Message msg = new Message();
		msg.what = typecode;
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		String parms = "{";
		for (int i = 0; i < parametes.size(); i++) {
			parms = parms + "\"" + parametes.get(i) + "\"";
			if (i % 2 != 1) {
				parms = parms + ":";
			} else if (i < (parametes.size() - 1)) {
				parms = parms + ",";
			}
		}
		parms = parms + "}";
		Log.i("requst_code", "POSt提交信息：" + parms);
		try {
			URL realUrl = new URL(minterface);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(parms);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			msg.obj = null;
			mhandler.sendMessage(msg);// 发送下载字符串成功消息
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			msg.obj = result;
			mhandler.sendMessage(msg);// 发送下载字符串成功消息
		}
		return parms;
	}

	/**
	 * 读取服务器数据
	 * 
	 * @param inputStream
	 * @return
	 */
	private static String readHtmlData(InputStream inputStream) throws Exception {
		String htmlString = "";
		// 缓冲区
		byte buffer[] = new byte[1024];
		int temp = 0;
		// 内存输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((temp = inputStream.read(buffer)) != -1) {
			out.write(buffer, 0, temp);
		}
		out.close();
		inputStream.close();
		// 从内存中得到下载好的数据
		byte data[] = out.toByteArray();
		htmlString = new String(data, 0, data.length);
		return htmlString;
	}


	@SuppressLint("NewApi")
	public static void doPost(String minterface, List<NameValuePair> params, Handler mhandler, int typecode, Context context) {
		// HttpClientTool.network();
		String result = "";
		HttpPost httpPost = new HttpPost(minterface);
		// httpPost.setHeaders(HttpClientTool.getHeader());
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		String str ="";
		for (NameValuePair nv:params) {
			str+=nv.getName()+"="+nv.getValue()+":";
		}
		Log.i("JsonHttpUtils", "请求参数"+typecode+"="+str);
		
		try {
			Log.i("requst_code", "开始请求！");
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			/** 保持会话Session **/
			/** 设置Cookie **/
			// MyHttpCookies li = new MyHttpCookies(context);
			// Constants.li = new MyHttpCookies(context);
			// li.AddCookies(httpPost);
			// Constants.li.AddCookies(httpPost);
			/** 保持会话Session end **/

			HttpResponse httpResp ;
			if (typecode == LOGING_FLAG || typecode == LOGING_BY_PASS|| typecode == 555 ) { }
			else {
				PersistentCookieStore cookieStore = new PersistentCookieStore(AppApplication.getInstance().getApplicationContext());  
            	((AbstractHttpClient) httpClient).setCookieStore(cookieStore);  
            	 Log.i("requst_code", "请求内容头："+typecode+"=="+cookieStore.hashCode());
			}
			httpResp=httpClient.execute(httpPost);
			saveCookie(httpClient, typecode);
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// li.saveCookies(httpResp);
				// Constants.li.saveCookies(httpResp);
				result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
				Log.i("requst_code", "HttpPost方式请求成功，返回数据如下：");
				Log.i("requst_code", result);
				/** 执行成功之后得到 **/
				/** 成功之后把返回成功的Cookis保存APP中 **/
				// 请求成功之后，每次都设置Cookis。保证每次请求都是最新的Cookis
				// li.setuCookie(httpClient.getCookieStore());
				// Constants.li.setuCookie(httpClient.getCookieStore());
				/** 设置Cookie end **/
				// doPost(params,url);
			} else {
				Log.i("HttpPost", "HttpPost方式请求失败");
				System.out.println("0000===>" + EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
				result = "{success:false,msg:'请求失败'}";
			}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			result = "{success:false,msg:'TIME_OUT' }";
			// doPost(params,minterface);
		} catch (UnsupportedEncodingException e) {
			result = "{success:false,msg:'请求失败'}";
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			result = "{success:false,msg:'请求失败'}";
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
			httpClient.getConnectionManager().shutdown();
		}
		Log.i("JsonHttpUtils", "请求返回数据"+typecode+"="+result);
		if (!TextUtils.isEmpty(result)) { 
			Message msg = new Message();
			msg.what = typecode;
			msg.obj = result;
			if (mhandler!=null) {
				mhandler.sendMessage(msg);// 发送下载字符串成功消息
			}
			NameValuePair valuePair=new BasicNameValuePair(typecode+"", result);
			EventBus.getDefault().post(valuePair);
		}
	}

	private static void saveCookie(DefaultHttpClient httpClient, int typecode) {
		if (typecode == LOGING_FLAG || typecode == LOGING_BY_PASS|| typecode == 555 ) {
			PersistentCookieStore myCookieStore = AppApplication.getInstance().getPersistentCookieStore();  
	        List<Cookie> cookies = httpClient.getCookieStore().getCookies();  
	        for (Cookie cookie:cookies){  
	            myCookieStore.addCookie(cookie); 
	            Log.i("requst_code", "登陆成功,保存cookies"+cookies);
	        } 
		}
	}
}
