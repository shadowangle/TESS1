package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.PopupWindow;

@TargetApi(19)
@RequiresApi(19)
class PopupWindowCompatKitKat
{
  public static void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    paramPopupWindow.showAsDropDown(paramView, paramInt1, paramInt2, paramInt3);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.PopupWindowCompatKitKat
 * JD-Core Version:    0.6.0
 */