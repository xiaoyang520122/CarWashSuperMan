package com.gb.cwsup.utils;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.entity.BaseBean;
import com.gb.cwsup.interfaces.ICallBack;
import com.gb.cwsup.web.HttpUtil;
import com.gb.cwsup.widget.BaseProgressDialog;

public class BaseTask extends AsyncTask<Object, Integer, String> {
	private Activity activity;
	private BaseBean baseBean;
	private BaseProgressDialog baseDialog = null;
	private ICallBack callback;
	private Map<String, String> map;
	private String url;

	public BaseTask(Activity activity, String url,Map<String, String> paramMap, ICallBack callBack,BaseProgressDialog baseDialog) {
		this.map = paramMap;
		this.url = url;
		this.callback = callBack;
		this.activity = activity;
		this.baseDialog = baseDialog;
	}

	/**
	 * 第一个参数是否Get
	 */
	@Override
	protected String doInBackground(Object... paramVarArgs) {
		if (paramVarArgs != null) {
			if (paramVarArgs[0].equals(true)) {
				return HttpUtil.get(this.url, map);
			}
		}
		return HttpUtil.post(this.url, map);
		// return WebServiceUtils.getWebServiceResult(this.method, this.map);
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if ((this.baseDialog != null) && (this.baseDialog.isShowing())
				&& (this.activity != null) && (!this.activity.isFinishing())) {
			this.baseDialog.dismiss();
		}
		if (result == null) {
			ToastUtil.showToastShort(this.activity, "请求失败，请重试...");
			this.callback.excuteCallback(null);
			return;
		}
		try {
			this.baseBean = new BaseBean();
			JSONObject json = new JSONObject(result);
			this.baseBean.setDoStatu(json.optBoolean("doStatu"));
			this.baseBean.setDoMsg(json.optString("doMsg"));
			this.baseBean.setDoObject(json.optString("doObject"));
			if (!this.baseBean.isDoStatu()) {
				BaseActivity.checkEnum(this.baseBean, this.activity);
			}
			if (this.callback != null)
				this.callback.excuteCallback(this.baseBean);
		} catch (Exception e) {
			e.printStackTrace();
			if (this.callback != null)
				this.callback.excuteCallback(null);
			ToastUtil.showToastShort(this.activity, "数据解析异常，请重试...");
		}
		Log.i("result", result);
	}
}
