package com.gb.cwsup.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern .compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		} 
}
