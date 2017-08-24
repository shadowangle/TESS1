package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;

@TargetApi(14)
@RequiresApi(14)
class AccessibilityEventCompatIcs
{
  public static void appendRecord(AccessibilityEvent paramAccessibilityEvent, Object paramObject)
  {
    paramAccessibilityEvent.appendRecord((AccessibilityRecord)paramObject);
  }

  public static Object getRecord(AccessibilityEvent paramAccessibilityEvent, int paramInt)
  {
    return paramAccessibilityEvent.getRecord(paramInt);
  }

  public static int getRecordCount(AccessibilityEvent paramAccessibilityEvent)
  {
    return paramAccessibilityEvent.getRecordCount();
  }

  public static void setScrollable(AccessibilityEvent paramAccessibilityEvent, boolean paramBoolean)
  {
    paramAccessibilityEvent.setScrollable(paramBoolean);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityEventCompatIcs
 * JD-Core Version:    0.6.0
 */