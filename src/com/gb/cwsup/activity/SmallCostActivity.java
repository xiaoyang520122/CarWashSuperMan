package com.gb.cwsup.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.gb.cwsup.BaseActivity;
import com.gb.cwsup.R;
import com.gb.cwsup.adapter.MydefaultAdapter;
import com.gb.cwsup.utils.ActivityManagerUtil;

public class SmallCostActivity extends BaseActivity {
	// order List
		private ListView mcostListView;
		private MydefaultAdapter costAdapter;

		@Override
		protected void onCreate(Bundle paramBundle) {
			super.onCreate(paramBundle);
			settitlename("返回", "打赏记录", "···");
			setContentView(R.layout.smalcost_history);
			ActivityManagerUtil.getInstance().addToList(this);
			initview();
		}

		private void initview() {
			mcostListView = (ListView) findViewById(R.id.smalcost_listView);
			costAdapter = new MydefaultAdapter(this, R.layout.smalcost_history_item);

			mcostListView.setAdapter(costAdapter);
			setLeftTvOnClick(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SmallCostActivity.this.finish();
				}
			});
		}

	}