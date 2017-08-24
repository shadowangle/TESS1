package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityWindowInfo;

@TargetApi(24)
@RequiresApi(24)
class AccessibilityWindowInfoCompatApi24
{
  public static Object getAnchor(Object paramObject)
  {
    return ((AccessibilityWindowInfo)paramObject).getAnchor();
  }

  public static CharSequence getTitle(Object paramObject)
  {
    return ((AccessibilityWindowInfo)paramObject).getTitle();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi24
 * JD-Core Version:    0.6.0
 */