package com.gb.cwsup.getui;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.server.Music;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
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
    	String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
        	startService(new Intent(getApplicationContext(), Music.class));
            String data = new String(payload);
//            sendEvent(data);
            Log.d(TAG, "receiver payload = " + data);
            sendMessage(data, 0);
        }
        Log.d(TAG, "----------------------------------------------------------------------------------------------");
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