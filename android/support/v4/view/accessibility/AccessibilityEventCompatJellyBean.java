package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityEventCompatJellyBean
{
  public static int getAction(AccessibilityEvent paramAccessibilityEvent)
  {
    return paramAccessibilityEvent.getAction();
  }

  public static int getMovementGranularity(AccessibilityEvent paramAccessibilityEvent)
  {
    return paramAccessibilityEvent.getMovementGranularity();
  }

  public static void setAction(AccessibilityEvent paramAccessibilityEvent, int paramInt)
  {
    paramAccessibilityEvent.setAction(paramInt);
  }

  public static void setMovementGranularity(AccessibilityEvent paramAccessibilityEvent, int paramInt)
  {
    paramAccessibilityEvent.setMovementGranularity(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityEventCompatJellyBean
 * JD-Core Version:    0.6.0
 */