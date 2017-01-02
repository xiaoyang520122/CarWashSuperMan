package com.gb.cwsup.entity;

import java.io.Serializable;
import java.util.List;

public class GetMyHepan
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String balance;
  private List<GetMyHepanIncome> dueinprofit_list;
  private String fund_totle;
  private String hasearned_int;
  private String next_priint;
  private String next_priint_date;
  private String unget_intefund;
  
  public String getBalance()
  {
    return this.balance;
  }
  
  public List<GetMyHepanIncome> getDueinprofit_list()
  {
    return this.dueinprofit_list;
  }
  
  public String getFund_totle()
  {
    return this.fund_totle;
  }
  
  public String getHasearned_int()
  {
    return this.hasearned_int;
  }
  
  public String getNext_priint()
  {
    return this.next_priint;
  }
  
  public String getNext_priint_date()
  {
    return this.next_priint_date;
  }
  
  public String getUnget_intefund()
  {
    return this.unget_intefund;
  }
  
  public void setBalance(String paramString)
  {
    this.balance = paramString;
  }
  
  public void setDueinprofit_list(List<GetMyHepanIncome> paramList)
  {
    this.dueinprofit_list = paramList;
  }
  
  public void setFund_totle(String paramString)
  {
    this.fund_totle = paramString;
  }
  
  public void setHasearned_int(String paramString)
  {
    this.hasearned_int = paramString;
  }
  
  public void setNext_priint(String paramString)
  {
    this.next_priint = paramString;
  }
  
  public void setNext_priint_date(String paramString)
  {
    this.next_priint_date = paramString;
  }
  
  public void setUnget_intefund(String paramString)
  {
    this.unget_intefund = paramString;
  }
}
