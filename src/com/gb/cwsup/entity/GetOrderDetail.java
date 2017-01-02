package com.gb.cwsup.entity;

import java.io.Serializable;

public class GetOrderDetail
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String able_balance;
  private String amount;
  private String bonus;
  private String check_date;
  private String day_flag;
  private String interest_rate;
  private String ishas_bonus;
  private double minInvest_amount;
  private String order_id;
  private String order_pro;
  private String order_title;
  private String product_id;
  private String repay_type;
  private String status;
  private String tender_alloted;
  private String tender_min;
  private String valid_date;
  
  public String getAble_balance()
  {
    return this.able_balance;
  }
  
  public String getAmount()
  {
    return this.amount;
  }
  
  public String getBonus()
  {
    return this.bonus;
  }
  
  public String getCheck_date()
  {
    return this.check_date;
  }
  
  public String getDay_flag()
  {
    return this.day_flag;
  }
  
  public String getInterest_rate()
  {
    return this.interest_rate;
  }
  
  public String getIshas_bonus()
  {
    return this.ishas_bonus;
  }
  
  public double getMinInvest_amount()
  {
    return this.minInvest_amount;
  }
  
  public String getOrder_id()
  {
    return this.order_id;
  }
  
  public String getOrder_pro()
  {
    return this.order_pro;
  }
  
  public String getOrder_title()
  {
    return this.order_title;
  }
  
  public String getProduct_id()
  {
    return this.product_id;
  }
  
  public String getRepay_type()
  {
    return this.repay_type;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public String getTender_alloted()
  {
    return this.tender_alloted;
  }
  
  public String getTender_min()
  {
    return this.tender_min;
  }
  
  public String getValid_date()
  {
    return this.valid_date;
  }
  
  public void setAble_balance(String paramString)
  {
    this.able_balance = paramString;
  }
  
  public void setAmount(String paramString)
  {
    this.amount = paramString;
  }
  
  public void setBonus(String paramString)
  {
    this.bonus = paramString;
  }
  
  public void setCheck_date(String paramString)
  {
    this.check_date = paramString;
  }
  
  public void setDay_flag(String paramString)
  {
    this.day_flag = paramString;
  }
  
  public void setInterest_rate(String paramString)
  {
    this.interest_rate = paramString;
  }
  
  public void setIshas_bonus(String paramString)
  {
    this.ishas_bonus = paramString;
  }
  
  public void setMinInvest_amount(double paramDouble)
  {
    this.minInvest_amount = paramDouble;
  }
  
  public void setOrder_id(String paramString)
  {
    this.order_id = paramString;
  }
  
  public void setOrder_pro(String paramString)
  {
    this.order_pro = paramString;
  }
  
  public void setOrder_title(String paramString)
  {
    this.order_title = paramString;
  }
  
  public void setProduct_id(String paramString)
  {
    this.product_id = paramString;
  }
  
  public void setRepay_type(String paramString)
  {
    this.repay_type = paramString;
  }
  
  public void setStatus(String paramString)
  {
    this.status = paramString;
  }
  
  public void setTender_alloted(String paramString)
  {
    this.tender_alloted = paramString;
  }
  
  public void setTender_min(String paramString)
  {
    this.tender_min = paramString;
  }
  
  public void setValid_date(String paramString)
  {
    this.valid_date = paramString;
  }
}
