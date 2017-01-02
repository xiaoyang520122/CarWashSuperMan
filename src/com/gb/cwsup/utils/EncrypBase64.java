package com.gb.cwsup.utils;

import android.util.Base64;
import java.io.UnsupportedEncodingException;

public class EncrypBase64
{
  public static String decryptBASE64(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return null;
    }
    try
    {
      byte[] arrayOfByte = paramString.getBytes("UTF-8");
      String str = new String(Base64.decode(arrayOfByte, 0, arrayOfByte.length, 0), "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
    return null;
  }
  
  public static String encryptBASE64(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return null;
    }
    try
    {
      byte[] arrayOfByte = paramString.getBytes("UTF-8");
      String str = new String(Base64.encode(arrayOfByte, 0, arrayOfByte.length, 0), "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
    return null;
  }
}
