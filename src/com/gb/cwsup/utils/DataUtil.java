package com.gb.cwsup.utils;

import java.text.DecimalFormat;

public class DataUtil
{
  public static final String REGEX_EMIAL = "^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}$";
  public static final String REGEX_STRING_SOME_HIDDEN = "^[0-9a-zA-Z.@]+$";
  public static final String STRING_EMAIL = "@";
  public static final String STRING_ERROR_HIDDEN = "";
  public static final String STRING_REPLACE = "*";
  
  public static String changeToHiddenSomeCenter(String paramString, int paramInt)
  {
    if ((paramString != null) && (paramString.matches("^[0-9a-zA-Z.@]+$")) && (4 > 0))
    {
      int i = -8 + paramString.length();
      int j = paramString.length() - 4;
      int k = paramString.length();
      StringBuffer localStringBuffer = new StringBuffer();
      if ((k > 8) && (i > 0)) {
        for (int i1 = 0;; i1++)
        {
          if (i1 >= i) {
            return paramString.substring(0, 4) + localStringBuffer.toString() + paramString.substring(j);
          }
          localStringBuffer.append("*");
        }
      }
      if (k > 2) {
        for (int n = 0;; n++)
        {
          if (n >= k - 2) {
            return paramString.substring(0, 1) + localStringBuffer.toString() + paramString.substring(k - 1);
          }
          localStringBuffer.append("*");
        }
      }
      if (k > 0) {
        for (int m = 0;; m++)
        {
          if (m >= k) {
            return localStringBuffer.toString();
          }
          localStringBuffer.append("*");
        }
      }
    }
    return "";
  }
  
  public static String changeToHiddenSomeCenterForEmail(String paramString, int paramInt)
  {
    if ((paramString != null) && (paramString.matches("^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}$")) && (paramInt > 0))
    {
      int i = paramString.indexOf("@");
      int j = i - paramInt;
      StringBuffer localStringBuffer = new StringBuffer();
      if (j > 0) {
        for (int m = 0;; m++)
        {
          if (m >= j) {
            return paramString.substring(0, paramInt) + localStringBuffer.toString() + paramString.substring(i);
          }
          localStringBuffer.append("*");
        }
      }
      if (i > 0) {
        for (int k = 0;; k++)
        {
          if (k >= i) {
            return paramString.replace(paramString.substring(0, i), localStringBuffer.toString());
          }
          localStringBuffer.append("*");
        }
      }
    }
    return "";
  }
  
  public static boolean checkStringIsNull(String paramString)
  {
    return (paramString == null) || ("".equals(paramString));
  }
  
  public static String formatMoney(String paramString)
  {
    if ((checkStringIsNull(paramString)) || (paramString.matches("^[0.]+$"))) {
      return "0.00";
    }
    double d = Double.parseDouble(paramString);
    return new DecimalFormat("###,##0.00").format(d).toString();
  }
  
  public static String hiddenFirst(String paramString)
  {
    return "*" + paramString.substring(1);
  }
  
  public static String hiddenIDCard(String paramString)
  {
    if (paramString.length() != 18) {
      return "*";
    }
    return paramString.substring(0, 4) + "**********" + paramString.substring(14);
  }
  
  public static String hiddenPhone(String paramString)
  {
    if ((paramString != null) && (paramString.length() == 11)) {
      return paramString.substring(0, 3) + "****" + paramString.substring(7);
    }
    return "";
  }
  
  public static String hiddenUserRealAuthInfo(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0) && (paramString.contains("(")) && (paramString.contains(")")))
    {
      String[] arrayOfString = paramString.split("\\(");
      if ((arrayOfString != null) && (arrayOfString.length == 2))
      {
        String str1 = arrayOfString[0];
        String str2 = arrayOfString[1].substring(0, -1 + arrayOfString[1].length());
        String str3 = "*" + str1.substring(1);
        String str4 = hiddenIDCard(str2);
        return str3 + "(" + str4 + ")";
      }
    }
    return "";
  }
  
  public static String removeComma(String paramString)
  {
    if (paramString.contains(",")) {
      paramString = paramString.replace(",", "");
    }
    return paramString;
  }
}
