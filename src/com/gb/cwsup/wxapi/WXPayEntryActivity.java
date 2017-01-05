package com.gb.cwsup.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {  
	  
    // 标题栏返回箭头  
    private IWXAPI api;  
  
  
    @Override  
    protected void onNewIntent(Intent intent) {  
        super.onNewIntent(intent);  
        setIntent(intent);  
        api.handleIntent(intent, this);  
    }  
  
    @Override  
    public void onReq(BaseReq req) {  
  
    }  
  
    @Override  
    public void onResp(BaseResp resp) {  
        Log.d("TAG", "onPayFinish, errCode = " + resp.errCode);  
        /** 
         * 0表示支付成功，-1表示支付终端，-2表示支付失败 
         */  
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {  
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  
            builder.setTitle("提示");  
            builder.setMessage("微信支付结果：" + String.valueOf(resp.errCode));  
            builder.show();  
        }  
    }
}