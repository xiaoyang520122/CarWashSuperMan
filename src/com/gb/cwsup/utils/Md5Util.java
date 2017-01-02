package com.gb.cwsup.utils;

import java.security.MessageDigest;

public class Md5Util {
	public static final String MD5(String paramString) {
		char[] arrayOfChar1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
		try {
			byte[] arrayOfByte1 = paramString.getBytes();
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(arrayOfByte1);
			byte[] arrayOfByte2 = localMessageDigest.digest();
			int i = arrayOfByte2.length;
			char[] arrayOfChar2 = new char[i * 2];
			int j = 0;
			int k = 0;
			for (;;) {
				if (j >= i) {
					return new String(arrayOfChar2).toLowerCase();
				}
				int m = arrayOfByte2[j];
				int n = k + 1;
				arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
				k = n + 1;
				arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
				j++;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}
}
