package com.gb.cwsup.activity.order;

import java.util.Timer;
import java.util.TimerTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.utils.ActivityManagerUtil;

public class WaitingOpenActivity extends BaseActivity {

	private ProgressBar bar;
	private TextView timetv;
	private int PROGRESS = 0;
	private Timer timer;

	@Override
	protected void onCreate(Bundle paramBundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(paramBundle);
		setContentView(R.layout.wait_open_box);
		ActivityManagerUtil.getInstance().addToList(this);
		bar = (ProgressBar) findViewById(R.id.wait_progress);
		timetv= (TextView) findViewById(R.id.useTimetv);
		EventBus.getDefault().register(this);
		initTimer();
	}

	private void initTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}, 0, 200);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showProgress();
				break;
			case 2:
				timer.cancel();
				WaitingOpenActivity.this.finish();
				break;

			default:
				break;
			}
		}

	};
	
	private void showProgress(){
		PROGRESS+=1;
		bar.setProgress(PROGRESS);
		String timeString=(int)PROGRESS/5+"";
		timetv.setText("用时：" +timeString+ "秒");
		if (PROGRESS==300) {
			timer.cancel();
			new AlertDialog.Builder(WaitingOpenActivity.this)
			.setTitle("超时提醒！")
			.setMessage("开箱时间超时！")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					EventBus.getDefault().post(300);
					WaitingOpenActivity.this.finish();
				}
			}).create().show();
		}
	}
	
	/**
	 * 开箱成功后关闭该等待页面 
	 */
	@Subscribe(threadMode=ThreadMode.MAIN)
	public void finisWait(Boolean isok){
		if (isok) {
			timer.cancel();
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    EventBus.getDefault().unregister(this);
	}
}
