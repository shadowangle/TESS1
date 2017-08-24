package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;

@TargetApi(24)
@RequiresApi(24)
class AccessibilityNodeInfoCompatApi24
{
  public static Object getActionSetProgress()
  {
    return AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS;
  }

  public static int getDrawingOrder(Object paramObject)
  {
    return ((AccessibilityNodeInfo)paramObject).getDrawingOrder();
  }

  public static boolean isImportantForAccessibility(Object paramObject)
  {
    return ((AccessibilityNodeInfo)paramObject).isImportantForAccessibility();
  }

  public static void setDrawingOrder(Object paramObject, int paramInt)
  {
    ((AccessibilityNodeInfo)paramObject).setDrawingOrder(paramInt);
  }

  public static void setImportantForAccessibility(Object paramObject, boolean paramBoolean)
  {
    ((AccessibilityNodeInfo)paramObject).setImportantForAccessibility(paramBoolean);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityNodeInfoCompatApi24
 * JD-Core Version:    0.6.0
 */