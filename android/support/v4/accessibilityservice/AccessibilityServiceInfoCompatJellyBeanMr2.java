package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class AccessibilityServiceInfoCompatJellyBeanMr2
{
  public static int getCapabilities(AccessibilityServiceInfo paramAccessibilityServiceInfo)
  {
    return paramAccessibilityServiceInfo.getCapabilities();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.accessibilityservice.AccessibilityServiceInfoCompatJellyBeanMr2
 * JD-Core Version:    0.6.0
 */