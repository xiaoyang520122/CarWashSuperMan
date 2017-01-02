package com.gb.cwsup.fragment;

import com.gb.cwsup.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ServerCenterFragment extends Fragment {
	
	private View conView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		conView=getActivity().getLayoutInflater().inflate(R.layout.fragment_serve_center, null);
		return conView;
	}

}
