package com.gb.cwsup.activity.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.AdressListActivity;
import com.gb.cwsup.activity.CarListActivity;
import com.gb.cwsup.activity.MipcaActivityCapture;
import com.gb.cwsup.adapter.MySpinnerAdapter;
import com.gb.cwsup.entity.AddressBean;
import com.gb.cwsup.entity.CarBean;
import com.gb.cwsup.entity.EngineerOld;
import com.gb.cwsup.entity.Product;
import com.gb.cwsup.entity.URLs;
import com.gb.cwsup.fragment.MapListsFragment;
import com.gb.cwsup.utils.ActivityManagerUtil;
import com.gb.cwsup.utils.CallUtils;
import com.gb.cwsup.utils.JsonHttpUtils;
import com.gb.cwsup.utils.LoadingDialog;
import com.gb.cwsup.utils.ToastUtil;

public class WaitSureOrderActivity extends BaseActivity implements OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener {

	private Spinner spinner;
	private RadioButton mradio1, mradio2, mradio3;
	private RadioGroup mradioGroup;
	private TextView changaddView, caraddtv, priceTv, couponDiscountTv, amountPayableTv, couponsumTv, productpriceTv, userinfo, carinfo, engnametv;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private Dialog dialog;
	private List<NameValuePair> Nparams;
	private Thread mThread;
	private LinearLayout quanline, carlinLayout, addressLayout, enginfoLayout, engLayout;
	private LoadingDialog loddialog;
	private JSONArray jsonArray;
	private JSONObject josndata;
	private static String couponCodeString;
	private boolean spinnerfirstcheckflag = true;
	private Intent intent;
	private CarBean carBean;
	private AddressBean addressBean;
	private String TYPE = "", productID = null;
	private EngineerOld eng;
	private ImageView callengnieer;
	private boolean isfirstaddID = true;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		settitlename("返回", "下单信息", "");
		setContentView(R.layout.wait_sure_order);
		ActivityManagerUtil.getInstance().addToList(this);
		EventBus.getDefault().register(this);
		initview();
	}

	private void initview() {
		loddialog = new LoadingDialog(this);
		loddialog.setMessage("订单信息生成中……").show();
		mradio1 = (RadioButton) findViewById(R.id.radio_1);
		mradio2 = (RadioButton) findViewById(R.id.radio_2);
		mradio3 = (RadioButton) findViewById(R.id.radio_3);
		mradioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		quanline = (LinearLayout) findViewById(R.id.quanline);
		carlinLayout = (LinearLayout) findViewById(R.id.waitsureorder_car_linear);
		addressLayout = (LinearLayout) findViewById(R.id.waitsureorder_caradd_linea);
		enginfoLayout = (LinearLayout) findViewById(R.id.waitsureorder_enginfo_linea);
		engLayout = (LinearLayout) findViewById(R.id.waitsureorder_eng_linear);
		callengnieer = (ImageView) findViewById(R.id.waitsureorder_call_eng);

		callengnieer.setOnClickListener(this);
		quanline.setOnClickListener(this);
		carlinLayout.setOnClickListener(this);
		addressLayout.setOnClickListener(this);
		// engLayout.setOnClickListener(this);

		mradioGroup.setOnCheckedChangeListener(this);
		spinner = (Spinner) findViewById(R.id.waitsureorder_spinner);
		caraddtv = (TextView) findViewById(R.id.waitsureorder_caradd);
		carinfo = (TextView) findViewById(R.id.waitsureorder_car);
		productpriceTv = (TextView) findViewById(R.id.wait_product_price);
		userinfo = (TextView) findViewById(R.id.wait_username_phone);
		engnametv = (TextView) findViewById(R.id.waitsureorder_eng_name);

		priceTv = (TextView) findViewById(R.id.wait_price);
		couponDiscountTv = (TextView) findViewById(R.id.couponDiscount);
		amountPayableTv = (TextView) findViewById(R.id.amountPayable);
		couponsumTv = (TextView) findViewById(R.id.wait_coapon_sum);

		caraddtv.setText(MapListsFragment.CARADD);
		intenttype();
		setUserInfo();
		createOrderInfo();
		spinnersetadapter();
		findViewById(R.id.insuerOrder).setOnClickListener(this);
	}

	private void intenttype() {
		intent = getIntent();
		TYPE = intent.getStringExtra("type");

		if (!TextUtils.isEmpty(TYPE)) {
			// enginfoLayout.setVisibility(View.VISIBLE);
			// }else if (!TextUtils.isEmpty(TYPE)&&TYPE.equals("default")) {
			// enginfoLayout.setVisibility(View.VISIBLE);
		} else {
			return;
		}
		eng = (EngineerOld) intent.getSerializableExtra("EngineerOld");
		engnametv.setText(eng.getName());
	}

	private void setUserInfo() {
		SharedPreferences sp = getSharedPreferences("register_info", Context.MODE_PRIVATE);
		String name = sp.getString("name", "");
		String mobile = sp.getString("mobile", "");
		userinfo.setText(name + "  " + mobile);
	}

	/**
	 * 提交产品ID，计算费用，生成订单信息
	 */
	private void createOrderInfo() {
		clearmark();
	}

	private void clearmark() {
		showDilog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>(1);
				JsonHttpUtils.doPost(URLs.CLEAR_CART, params, handler, JsonHttpUtils.CLEAR_CART, WaitSureOrderActivity.this);
			}
		}).start();
	}

	private void spinnersetadapter() {
		List<String> data_list1 = new ArrayList<String>();
		List<Product> products = AppApplication.PRODUCTLIST;
		if (products != null) {
			for (int i = 0; i < products.size(); i++) {
				data_list1.add(products.get(i).getFullName());
			}
		}
		spinner.setAdapter(new MySpinnerAdapter(this, data_list1));
		spinner.setSelection(MapListsFragment.PRODUCT_POINT);
		productpriceTv.setText(AppApplication.PRODUCTLIST.get(MapListsFragment.PRODUCT_POINT).getPrice() + "");
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				productpriceTv.setText(AppApplication.PRODUCTLIST.get(arg2).getPrice() + "");
				if (spinnerfirstcheckflag) {
					spinnerfirstcheckflag = false;
					return;
				}
				productID = AppApplication.PRODUCTLIST.get(arg2).getId() + "";
				clearmark();
				// sendProductID(AppApplication.PRODUCTLIST.get(arg2).getId() +
				// "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public void onClick(View clickview) {
		switch (clickview.getId()) {
		case R.id.insuerOrder:// sure order
			submitorder();
			break;
		case R.id.start_scan_tv:// goto scan
			startscan();
			break;
		case R.id.quanline:// go to select coupon
			toCouponActivity();
			break;
		case R.id.waitsureorder_car_linear:// go to choice car
			toCarListActivity();
			break;
		case R.id.waitsureorder_caradd_linea:// go to choice address
			toAdressListActivity();
			break;
		case R.id.waitsureorder_call_eng:// call engineer
			Log.i("Call", eng.getMobile());
			CallUtils.call(this, eng.getMobile());
			break;

		default:
			break;
		}
	}

	private void toAdressListActivity() {
		intent = new Intent(this, AdressListActivity.class);
		intent.putExtra("type", "CHOICE");
		startActivity(intent);

	}

	private void toCarListActivity() {
		intent = new Intent(this, CarListActivity.class);
		intent.putExtra("type", "CHOICE");
		startActivity(intent);
	}

	/**
	 * 提交订单信息生成订单
	 */
	private void submitorder() {

		try {
			if (josndata != null) {
				loddialog.setMessage("订单提交中……").show();
				Nparams = new ArrayList<NameValuePair>(2);
				Nparams.add(new BasicNameValuePair("cartToken", josndata.getString("cartToken")));
				Nparams.add(new BasicNameValuePair("deliveryId", 25 + ""));
				Nparams.add(new BasicNameValuePair("engineerId", getIntent().getStringExtra("engineerid") ));
				Nparams.add(new BasicNameValuePair("paymentMethodId", "1"));
				Nparams.add(new BasicNameValuePair("shippingMethodId", "4"));
				Nparams.add(new BasicNameValuePair("code", couponCodeString));
				Nparams.add(new BasicNameValuePair("memberCarId", carBean.getId() + ""));
				new Thread(new Runnable() {
					@Override
					public void run() {
						JsonHttpUtils.doPost(URLs.CREAT_ORDER, Nparams, handler, JsonHttpUtils.GET_ORDER, WaitSureOrderActivity.this);
					}
				}).start();
			} else {
				new AlertDialog.Builder(this).setTitle("提示信息！").setMessage("提交订单失败，请重新提交！").setPositiveButton("确定", null).create().show();
				getcouponinfo();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void toCouponActivity() {
		if (jsonArray != null && jsonArray.length() > 0) {
			intent = new Intent();
			intent.setClass(this, CouponActivity.class);
			intent.putExtra("coupon", jsonArray.toString());
			startActivity(intent);
		} else {
			ToastUtil.showToastLong(this, "暂无优惠券信息！");
			getcouponinfo();
		}
	}

	/**
	 * + 推送产品订单产品ID到服务器
	 */
	private void sendProductID(final String productID) {
		showDilog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("baiduid", MapListsFragment.CITY_ID));
				if (productID == null) {
					params.add(new BasicNameValuePair("id", MapListsFragment.PRODUCT_ID));
				} else {
					params.add(new BasicNameValuePair("id", productID));
				}
				params.add(new BasicNameValuePair("quantity", "1"));
				JsonHttpUtils.doPost(URLs.SEND_PRODUCT_ID, params, handler, JsonHttpUtils.SEN_PRODUCT_ID, WaitSureOrderActivity.this);
			}
		}).start();
		Log.i("requst_code", "发送ID成功!");
	}

	/**
	 * 开始扫码
	 */
	private void startscan() {
		if (dialog != null) {
			dialog.dismiss();
		}
		Intent insureintent = new Intent(WaitSureOrderActivity.this, MipcaActivityCapture.class);
		insureintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(insureintent, SCANNIN_GREQUEST_CODE);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	/**
	 * 发送产品ID是否成功
	 * 
	 * @throws JSONException
	 */
	private void isSendIdOk(String JsonStr) {
		try {
			JSONObject jsobj1 = new JSONObject(JsonStr);
			JSONObject jsobj2 = jsobj1.getJSONObject("message");
			if (jsobj2.getString("type").equals("success")) {
				// ToastUtil.showToastLong(this, "产品ID保存成功！")
				Log.i("requst_code", "产品ID保存成功！");
				getOrderMoney();
			}
		} catch (JSONException e) {
			ToastUtil.showToastLong(this, "产品ID发送失败，请重新选择！");
			e.printStackTrace();
			finish();
		}
	}

	/**
	 * 请求订单价格
	 */
	private void getOrderMoney() {
		showDilog();
		new Thread() {
			@Override
			public void run() {
				super.run();
				Nparams = new ArrayList<NameValuePair>(2);
				if (!TextUtils.isEmpty(couponCodeString)) {
					Nparams.add(new BasicNameValuePair("code", couponCodeString));
				}
				Log.i("LONGING", "优惠码=" + couponCodeString);
				JsonHttpUtils.doPost(URLs.GET_ORDER_MONEY, Nparams, handler, JsonHttpUtils.GET_ORDER_MONEY, WaitSureOrderActivity.this);
			}
		}.start();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setLeftTvOnClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				WaitSureOrderActivity.this.finish();
			}
		});
	}

	private void isgetMoneysuccess(String jsonstr) {
		// loddialog.dismiss();
		try {
			JSONObject jsobj1 = new JSONObject(jsonstr);
			JSONObject jsobj2 = jsobj1.getJSONObject("message");
			if (jsobj2.getString("type").equals("success")) {
				JSONObject jsobj3 = jsobj1.getJSONObject("data");
				priceTv.setText("￥" + jsobj3.getString("price"));
				couponDiscountTv.setText("￥" + jsobj3.getString("couponDiscount"));
				amountPayableTv.setText("￥  " + jsobj3.getString("amountPayable"));
				getcouponinfo();
			}else if (jsobj2.getString("type").equals("error")) {
				showErrorDialog(jsobj2.getString("content"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showErrorDialog(String string) {
		Builder builder=new AlertDialog.Builder(this).setTitle("提示")
		.setMessage("生成订单信息失败，无法获得订单金额，请重试！")
		.setPositiveButton("确定", null);
		Dialog dialog=builder.create();
		dialog.show();
		
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				WaitSureOrderActivity.this.finish();
			}
		});
	}

	private void getcouponinfo() {
		showDilog();
		new Thread() {
			@Override
			public void run() {
				super.run();
				Log.i("requst_code", "开始请求优惠券信息");
				JsonHttpUtils.getHtmlString(URLs.GET_COUPON_LIST, null, handler, JsonHttpUtils.GET_COUPON, WaitSureOrderActivity.this);
			}
		}.start();

	}

	/**
	 * 解析订单信息
	 * 
	 * @param value
	 */
	private void jiexieCoupon(String value) {

		loddialog.dismiss();
		Log.i("LONGING", value);
		try {
			JSONObject object1 = new JSONObject(value);
			if (object1.getJSONObject("message").getString("type").equals("success")) {
				josndata = object1.getJSONObject("data");
				jsonArray = josndata.getJSONArray("couponCodes");
				couponsumTv.setText(jsonArray.length() + "张");
				getcarinfo(josndata.getJSONObject("memberCar"));
				Log.i("LONGING", jsonArray.length() + "");
			} else {
				ToastUtil.showToastLong(this, object1.getJSONObject("message").getString("content"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getcarinfo(JSONObject jsoncar) {
		if (carBean == null) {
			carBean = new CarBean();
			try {
				carBean.setId(Integer.valueOf(jsoncar.getString("id")));
				carBean.setCarno(jsoncar.getString("license_number"));
				carBean.setDefault(true);
				carBean.setColor(jsoncar.getString("color"));
				carBean.setType(jsoncar.getString("brand"));
				carBean.setTypemodels(jsoncar.getString("models"));
				setcarinfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*** 解析生成订单接口返回的数据 **/
	private void isCreatSuccess(String value) {
		loddialog.dismiss();
		Log.i("LONGING", "生成订单返回数据=" + value);
		try {
			JSONObject jO = new JSONObject(value);
			JSONObject jO2 = jO.getJSONObject("message");
			if (jO2.getString("type").equals("success")) {
				Intent intent = new Intent(this, SuccessPayOrder.class);
				int spitem = spinner.getSelectedItemPosition();
				intent.putExtra("productname", AppApplication.PRODUCTLIST.get(spitem).getName());
				intent.putExtra("ordernumber", jO2.getString("content"));
				intent.putExtra("Total", amountPayableTv.getText().toString());
				startActivity(intent);
				finish();
			} else {
				ToastUtil.showToastLong(this, "提交订单失败");
			}
		} catch (JSONException e) {
			ToastUtil.showToastLong(this, "提交订单失败");
			e.printStackTrace();
		}

	}

	/**
	 * 等待超过一分钟停止网络开箱请求
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void overtime(NameValuePair valuePair) {

		int typecode = Integer.valueOf(valuePair.getName());
		switch (typecode) {
		case JsonHttpUtils.GET_ORDER_MONEY:
			isgetMoneysuccess(valuePair.getValue());
			break;
		case JsonHttpUtils.SEN_PRODUCT_ID:
			isSendIdOk(valuePair.getValue());
			break;
		case JsonHttpUtils.COUPON_CODE:
			couponCodeString = valuePair.getValue();
			getOrderMoney();
			break;
		case JsonHttpUtils.GET_COUPON:
			jiexieCoupon(valuePair.getValue());
			break;
		case JsonHttpUtils.GET_ORDER:
			isCreatSuccess(valuePair.getValue());
			break;
		case JsonHttpUtils.CLEAR_CART:
			isclearSuccess(valuePair.getValue());
			break;
		default:
			break;
		}
	}

	private void isclearSuccess(String value) {
		try {
			JSONObject jO = new JSONObject(value);
			JSONObject jO2 = jO.getJSONObject("message");
			if (jO2.getString("type").equals("success")) {
				if (isfirstaddID) {
					sendProductID(null); // 提交产品ID
					isfirstaddID = false;
				} else {
					sendProductID(productID); // 提交产品ID
				}

			} else {
				ToastUtil.showToastLong(this, jO2.getString("content"));
			}
		} catch (JSONException e) {
			ToastUtil.showToastLong(this, "提交失败");
			e.printStackTrace();
		}

	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void eventChoiceData(Map<String, Object> data) {
		try {
			if (data.get("choiceCAR") != null) {
				carBean = (CarBean) data.get("choiceCAR");
				setcarinfo();
			} else if (data.get("choiceADDRESS") != null) {
				addressBean = (AddressBean) data.get("choiceADDRESS");
				caraddtv.setText(addressBean.getAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setcarinfo() {
		carinfo.setText(carBean.getCarno() + " " + carBean.getColor() + " " + carBean.getType() + " " + carBean.getTypemodels());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int postion) {
		// TODO Auto-generated method stub
		ToastUtil.showToastShort(this, "选中第" + postion + "个");

	}

	private void showDilog() {
		if (loddialog == null) {
			return;
		}
		if (loddialog.isShowing()) {
			return;
		}
		if (!loddialog.isShowing()) {
			loddialog.show();
		}
	}

}
