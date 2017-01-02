package com.gb.cwsup.getui;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.gb.cwsup.AppApplication;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {
	

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
    }
    
    @Override
    public void onReceiveClientId(Context context, String clientid) {
    	Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        sendMessage(clientid, 1);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
    
    private void sendMessage(String data, int what) {
    	Log.e(TAG, "onReceiveClientId -> " + "clientid = " + data);
    	Log.e("LONGING", "进入sendMessage = " +data);
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        AppApplication.sendMessage(msg);
    }
}