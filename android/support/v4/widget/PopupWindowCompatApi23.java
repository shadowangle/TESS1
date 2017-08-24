package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.PopupWindow;

@TargetApi(23)
@RequiresApi(23)
class PopupWindowCompatApi23
{
  static boolean getOverlapAnchor(PopupWindow paramPopupWindow)
  {
    return paramPopupWindow.getOverlapAnchor();
  }

  static int getWindowLayoutType(PopupWindow paramPopupWindow)
  {
    return paramPopupWindow.getWindowLayoutType();
  }

  static void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
  {
    paramPopupWindow.setOverlapAnchor(paramBoolean);
  }

  static void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt)
  {
    paramPopupWindow.setWindowLayoutType(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.PopupWindowCompatApi23
 * JD-Core Version:    0.6.0
 */