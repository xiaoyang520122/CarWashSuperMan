package com.gb.cwsup.web;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.baidu.platform.comapi.map.s;

/**
 * HttpUtil Class Capsule Most Functions of Http Operations
 * 
 * @author sfshine
 * 
 */
public class HttpUtil {

	private static Header[] headers = new BasicHeader[12];
	private static String TAG = "HTTPUTIL";
	private static int TIMEOUT = 10 * 1000;

	/** "令牌"Cookie名称 */
	private static final String TOKEN_COOKIE_NAME = "token";

	/** "令牌"参数名称 */
	private static final String TOKEN_PARAMETER_NAME = "token";

	/**
	 * Your header of http op
	 */
	static {
		headers[0] = new BasicHeader("Appkey", "");
		headers[1] = new BasicHeader("Udid", "");
		headers[2] = new BasicHeader("Os", "");
		headers[3] = new BasicHeader("Osversion", "");
		headers[4] = new BasicHeader("Appversion", "");
		headers[5] = new BasicHeader("Sourceid", "");
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
		headers[10] = new BasicHeader("Cookie", "");
		headers[11] = new BasicHeader("Token", "");
	}

	public static void getToken(String url) {
		get(url);
	}

	/**
	 * Op Http get request
	 * 
	 * @param url
	 * @param map
	 *            Values to request
	 * @return
	 */
	public static String get(String url) {
		return get(url, new HashMap<String, String>());
	}

	public static String get(String url, Map<String, String> map) {

		if (map != null) {
			int i = 0;
			for (Map.Entry<String, String> entry : map.entrySet()) {

				Log.i(TAG, entry.getKey() + "=>" + entry.getValue());
				if (i == 0) {
					url = url + "?" + entry.getKey() + "=" + entry.getValue();
				} else {
					url = url + "&" + entry.getKey() + "=" + entry.getValue();
				}

				i++;

			}
		}
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIMEOUT);
		HttpGet post = new HttpGet(url);
		Log.i(TAG, url);
		post.setHeaders(headers);
		String result = "{'doStatu':'false','doMsg':'请求失败'}";

		try {

			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.i(TAG, "result =>" + result);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "result error =>" + e.getMessage());

		}

		Log.e(TAG, result);
		return result;

		// String reult = post(url, null);
		// return reult;

	}

	/**
	 * Op Http post request , "404error" response if failed
	 * 
	 * @param url
	 * @param map
	 *            Values to request
	 * @return
	 */

	public static String post(String url) {
		return post(url, new HashMap<String, String>());

	}

	public static String post(String url, Map<String, String> map) {

		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIMEOUT);
		HttpPost post = new HttpPost(url);
		Log.i(TAG, url);
		post.setHeaders(headers);
		String result = "{'doStatu':'false','doMsg':'请求失败'}";
		ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put(TOKEN_PARAMETER_NAME, headers[11].getValue());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Log.i(TAG, entry.getKey() + "=>" + entry.getValue());
			BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(),
					entry.getValue());
			pairList.add(pair);
		}

		try {
			HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.i(TAG, "result =>" + result);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "result error =>" + e.getMessage());

		}

		Log.e(TAG, result);
		return result;
	}

	/**
	 * Post Bytes to Server
	 * 
	 * @param url
	 * @param bytes
	 *            of text
	 * @return
	 */
	public static String PostBytes(String url, byte[] bytes) {
		try {
			URL murl = new URL(url);
			final HttpURLConnection con = (HttpURLConnection) murl
					.openConnection();

			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);

			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "text/html");
			String cookie = headers[10].getValue();
			if (!isNull(headers[10].getValue())) {
				con.setRequestProperty("cookie", cookie);
			}

			con.setReadTimeout(TIMEOUT);
			con.setConnectTimeout(TIMEOUT);
			Log.i(TAG, url);
			DataOutputStream dsDataOutputStream = new DataOutputStream(
					con.getOutputStream());
			dsDataOutputStream.write(bytes, 0, bytes.length);

			dsDataOutputStream.close();
			if (con.getResponseCode() == HttpStatus.SC_OK) {
				InputStream isInputStream = con.getInputStream();
				int ch;
				StringBuffer buffer = new StringBuffer();
				while ((ch = isInputStream.read()) != -1) {
					buffer.append((char) ch);
				}

				Log.i(TAG, "GetDataFromServer>" + buffer.toString());

				return buffer.toString();
			} else {
				return "404error";
			}
		} catch (SocketTimeoutException e) {
			return "timeouterror";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "404error";
		}
	}

	/**
	 * set Cookie
	 * 
	 * @param response
	 */
	private static void setCookie(HttpResponse response) {
		if (response.getHeaders("Set-Cookie").length > 0) {
			String cookies = response.getHeaders("Set-Cookie")[0].getValue();
			Log.d(TAG, cookies);
			if (cookies != null && cookies.contains(";")) {
				String[] cookie = cookies.split(";");
				for (String c : cookie) {
					if (c != null && c.contains(TOKEN_COOKIE_NAME)) {
						String[] tokens = c.split("=");
						headers[11] = new BasicHeader("Cookie", tokens[1]);
					}
				}
			}

			headers[10] = new BasicHeader("Cookie", cookies);
		}
	}

	/**
	 * check net work
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Toast.makeText(context, "当前无网络连接,请稍后重试", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/***
	 * @category check if the string is null
	 * @return true if is null
	 * */
	public static boolean isNull(String string) {
		boolean t1 = "".equals(string);
		boolean t2 = string == null;
		boolean t3 = string.equals("null");
		if (t1 || t2 || t3) {
			return true;
		} else {
			return false;
		}
	}
}