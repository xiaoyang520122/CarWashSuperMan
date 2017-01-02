package com.gb.cwsup.utils;

public class EncrypUtil {
	private static String MIXED_STRING = "msx";

	public static String decryptBase64WithMD5(String paramString) {
		return EncrypBase64.decryptBASE64(paramString).substring(MIXED_STRING.length());
	}

	public static String decryptShardPreferencesValue(String paramString) {
		if (DataUtil.checkStringIsNull(paramString)) {
			return "";
		}
		Object localObject = paramString;
		try {
			if (((String) localObject).endsWith("1")) {
				localObject = ((String) localObject).substring(0, -1 + ((String) localObject).length()) + "=";
			} else {
				return decryptThirdBASE64((String) localObject);

			}
			if (((String) localObject).endsWith("2")) {
				localObject = ((String) localObject).substring(0, -1 + ((String) localObject).length()) + "==";
			} else if (((String) localObject).endsWith("3")) {
				String str = ((String) localObject).substring(0, -1 + ((String) localObject).length()) + "===";
				localObject = str;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return "";
	}

	public static String decryptThirdBASE64(String paramString) {
		return EncrypBase64.decryptBASE64(EncrypBase64.decryptBASE64(EncrypBase64.decryptBASE64(paramString)));
	}

	public static String encryptBase64WithMD5(String paramString) {
		return EncrypBase64.encryptBASE64(MIXED_STRING + Md5Util.MD5(paramString));
	}

	public static String encryptShardPreferencesKey(String paramString) {
		return encryptBase64WithMD5(paramString).trim().replace("=", "");
	}

	public static String encryptShardPreferencesValue(String paramString) {
		String str = encryptThirdBASE64(paramString).trim();
		if (str.endsWith("===")) {
			str = str.substring(0, -3 + str.length()) + 3;
		} else {
			return str;
		}
		do {
			if (str.endsWith("==")) {
				return str.substring(0, -2 + str.length()) + 2;
			}
		} while (!str.endsWith("="));
		return str.substring(0, -1 + str.length()) + 1;
	}

	public static String encryptThirdBASE64(String paramString) {
		return EncrypBase64.encryptBASE64(EncrypBase64.encryptBASE64(EncrypBase64.encryptBASE64(paramString)));
	}
}
