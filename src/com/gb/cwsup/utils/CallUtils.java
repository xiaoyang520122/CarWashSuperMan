package com.gb.cwsup.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class CallUtils {
	
	public static void call(Context context,String phone){
		Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
		context.startActivity(intent);  
	}

}
