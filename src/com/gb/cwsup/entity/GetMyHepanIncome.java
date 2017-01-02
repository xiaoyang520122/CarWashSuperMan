package com.gb.cwsup.entity;

import java.io.Serializable;

public class GetMyHepanIncome
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String date;
  private String number;
  
  public String getDate()
  {
    return this.date;
  }
  
  public String getNumber()
  {
    return this.number;
  }
  
  public void setDate(String paramString)
  {
    this.date = paramString;
  }
  
  public void setNumber(String paramString)
  {
    this.number = paramString;
  }
}
