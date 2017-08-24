package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.PopupWindow;
import java.lang.reflect.Field;

@TargetApi(21)
@RequiresApi(21)
class PopupWindowCompatApi21
{
  private static final String TAG = "PopupWindowCompatApi21";
  private static Field sOverlapAnchorField;

  static
  {
    try
    {
      sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
      sOverlapAnchorField.setAccessible(true);
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", localNoSuchFieldException);
    }
  }

  static boolean getOverlapAnchor(PopupWindow paramPopupWindow)
  {
    if (sOverlapAnchorField != null)
      try
      {
        boolean bool = ((Boolean)sOverlapAnchorField.get(paramPopupWindow)).booleanValue();
        return bool;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", localIllegalAccessException);
      }
    return false;
  }

  static void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
  {
    if (sOverlapAnchorField != null);
    try
    {
      sOverlapAnchorField.set(paramPopupWindow, Boolean.valueOf(paramBoolean));
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", localIllegalAccessException);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.PopupWindowCompatApi21
 * JD-Core Version:    0.6.0
 */