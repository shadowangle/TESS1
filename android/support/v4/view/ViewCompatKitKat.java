package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(19)
@RequiresApi(19)
class ViewCompatKitKat
{
  public static int getAccessibilityLiveRegion(View paramView)
  {
    return paramView.getAccessibilityLiveRegion();
  }

  public static boolean isAttachedToWindow(View paramView)
  {
    return paramView.isAttachedToWindow();
  }

  public static boolean isLaidOut(View paramView)
  {
    return paramView.isLaidOut();
  }

  public static boolean isLayoutDirectionResolved(View paramView)
  {
    return paramView.isLayoutDirectionResolved();
  }

  public static void setAccessibilityLiveRegion(View paramView, int paramInt)
  {
    paramView.setAccessibilityLiveRegion(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewCompatKitKat
 * JD-Core Version:    0.6.0
 */