package com.gb.cwsup.utils;

import com.google.gson.Gson;

public class GsonUtil {
	private static Gson gson = null;

	public static Gson getInstance() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}
}
