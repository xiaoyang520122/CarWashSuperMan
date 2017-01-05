package com.gb.cwsup.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.gb.cwsup.R;
 
public class Music extends Service {
    private MediaPlayer mp;
    private Vibrator vibrator;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mp=MediaPlayer.create(this,R.raw.new_message);
 
    }
    @SuppressWarnings("deprecation")
	@Override  
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        stopservice();
        Log.d("GTIntentService", "开始播放提示音-******************************");
        mp.start();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
        long [] pattern = {100,500,300,500};   // 停止 开启 停止 开启   
        vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1   
    } 
    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        Log.d("GTIntentService", "服务停止-******************************");
    }
 
    
//   private  Timer timer;
//   private TimerTask task;
//	private void stopservice() {
//		task = new TimerTask() {
//			@Override
//			public void run() {
//				if (!mp.isPlaying()) {
//					timer.cancel();
//					Log.d("GTIntentService", "停止播放提示音-******************************");
//					task.cancel();
////					stopService(new Intent(getApplicationContext(), Music.class) );
//				}
//			}
//		};
//		timer=new Timer();
//		timer.schedule(task, 1, 500);
//	}
    
}