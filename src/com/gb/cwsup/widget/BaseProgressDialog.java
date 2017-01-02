package com.gb.cwsup.widget;

import com.gb.cwsup.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseProgressDialog
  extends Dialog
{
  private static BaseProgressDialog baseProgressDialog = null;
  
  public BaseProgressDialog(Context paramContext)
  {
    super(paramContext);
  }
  
  public BaseProgressDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }
  
  public static BaseProgressDialog createDialog(Context paramContext)
  {
    baseProgressDialog = new BaseProgressDialog(paramContext, 2131361805);
    baseProgressDialog.setContentView(R.layout.view_base_progressdialog);
    baseProgressDialog.getWindow().getAttributes().gravity = 17;
    baseProgressDialog.setCanceledOnTouchOutside(false);
    return baseProgressDialog;
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    if (baseProgressDialog == null) {
      return;
    }
    ((AnimationDrawable)((ImageView)baseProgressDialog.findViewById(R.id.bpd_iv_image)).getBackground()).start();
  }
  
  public BaseProgressDialog setMessage(String paramString)
  {
    TextView localTextView = (TextView)baseProgressDialog.findViewById(R.id.bpd_tv_msg);
    if (localTextView != null) {
      localTextView.setText(paramString);
    }
    return baseProgressDialog;
  }
  
  public BaseProgressDialog setTitile(String paramString)
  {
    return baseProgressDialog;
  }
}
