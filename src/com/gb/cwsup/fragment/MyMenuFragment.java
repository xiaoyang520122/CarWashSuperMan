package com.gb.cwsup.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gb.cwsup.AppApplication;
import com.gb.cwsup.R;
import com.gb.cwsup.activity.AdressListActivity;
import com.gb.cwsup.activity.CarListActivity;
import com.gb.cwsup.activity.CollectEngineerActivity;
import com.gb.cwsup.activity.EditerCenterActivity;
import com.gb.cwsup.activity.MessageCenterActivity;
import com.gb.cwsup.activity.MyInfoActivity;
import com.gb.cwsup.activity.MyOrderActivity;
import com.gb.cwsup.activity.MyPropertyActivity;
import com.gb.cwsup.activity.MyRefundActivity;
import com.gb.cwsup.activity.ProblemActivity;
import com.gb.cwsup.activity.ServePriceActivity;
import com.gb.cwsup.activity.SmallCostActivity;

public class MyMenuFragment extends Fragment implements OnClickListener {

	private View mview;
	private TextView nametv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mview = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_menu, null);
		initview();
		return mview;
	}

	private void initview() {
		mview.findViewById(R.id.my_menu_manage_adress).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_manage_car).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_myorder).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_mycollect).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_refund).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_msgcenter).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_myproperty).setOnClickListener(this);
		mview.findViewById(R.id.my_menu_smallcost).setOnClickListener(this);
		mview.findViewById(R.id.serveprice).setOnClickListener(this);
		mview.findViewById(R.id.problem_menu).setOnClickListener(this);
		mview.findViewById(R.id.editer_center).setOnClickListener(this);
		mview.findViewById(R.id.mymenu_me).setOnClickListener(this);
		nametv=(TextView) mview.findViewById(R.id.mymenu_name);
		String username=String.format(nametv.getText().toString(), AppApplication.getInstance().getusername());
		nametv.setText(username);
	}

	@SuppressLint("WorldReadableFiles")
	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.my_menu_manage_car: // 地址管理
			intent.setClass(getActivity(), AdressListActivity.class);
			getActivity().startActivity(intent);
			break;
			
		case R.id.my_menu_myorder: //我的订单
			intent.setClass(getActivity(), MyOrderActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.my_menu_mycollect: //我的收藏
			intent.setClass(getActivity(), CollectEngineerActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.my_menu_refund: //我的退款
			intent.setClass(getActivity(), MyRefundActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.my_menu_msgcenter: //消息中心
			intent.setClass(getActivity(), MessageCenterActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.my_menu_myproperty: //我的资产
			intent.setClass(getActivity(), MyPropertyActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.my_menu_manage_adress: //car list
			intent.setClass(getActivity(), CarListActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.my_menu_smallcost: //打赏记录
			intent.setClass(getActivity(), SmallCostActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.serveprice: //price list
			intent.setClass(getActivity(), ServePriceActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.problem_menu: //
			intent.setClass(getActivity(), ProblemActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.editer_center: //
			intent.setClass(getActivity(), EditerCenterActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.mymenu_me: //
			getActivity().startActivity(new Intent(getActivity(), MyInfoActivity.class));
			break;
		default:
			break;
		}
	}
	
}
