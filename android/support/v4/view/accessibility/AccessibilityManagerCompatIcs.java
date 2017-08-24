package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
class AccessibilityManagerCompatIcs
{
  public static boolean addAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityStateChangeListenerWrapper paramAccessibilityStateChangeListenerWrapper)
  {
    return paramAccessibilityManager.addAccessibilityStateChangeListener(paramAccessibilityStateChangeListenerWrapper);
  }

  public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager, int paramInt)
  {
    return paramAccessibilityManager.getEnabledAccessibilityServiceList(paramInt);
  }

  public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager)
  {
    return paramAccessibilityManager.getInstalledAccessibilityServiceList();
  }

  public static boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
  {
    return paramAccessibilityManager.isTouchExplorationEnabled();
  }

  public static boolean removeAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityStateChangeListenerWrapper paramAccessibilityStateChangeListenerWrapper)
  {
    return paramAccessibilityManager.removeAccessibilityStateChangeListener(paramAccessibilityStateChangeListenerWrapper);
  }

  static abstract interface AccessibilityStateChangeListenerBridge
  {
    public abstract void onAccessibilityStateChanged(boolean paramBoolean);
  }

  public static class AccessibilityStateChangeListenerWrapper
    implements AccessibilityManager.AccessibilityStateChangeListener
  {
    Object mListener;
    AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge mListenerBridge;

    public AccessibilityStateChangeListenerWrapper(Object paramObject, AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge paramAccessibilityStateChangeListenerBridge)
    {
      this.mListener = paramObject;
      this.mListenerBridge = paramAccessibilityStateChangeListenerBridge;
    }

    public boolean equals(Object paramObject)
    {
      if (this == paramObject);
      AccessibilityStateChangeListenerWrapper localAccessibilityStateChangeListenerWrapper;
      while (true)
      {
        int i = 1;
        Class localClass1;
        Class localClass2;
        do
        {
          do
          {
            return i;
            i = 0;
          }
          while (paramObject == null);
          localClass1 = getClass();
          localClass2 = paramObject.getClass();
          i = 0;
        }
        while (localClass1 != localClass2);
        localAccessibilityStateChangeListenerWrapper = (AccessibilityStateChangeListenerWrapper)paramObject;
        if (this.mListener != null)
          break;
        if (localAccessibilityStateChangeListenerWrapper.mListener != null)
          return false;
      }
      return this.mListener.equals(localAccessibilityStateChangeListenerWrapper.mListener);
    }

    public int hashCode()
    {
      if (this.mListener == null)
        return 0;
      return this.mListener.hashCode();
    }

    public void onAccessibilityStateChanged(boolean paramBoolean)
    {
      this.mListenerBridge.onAccessibilityStateChanged(paramBoolean);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityManagerCompatIcs
 * JD-Core Version:    0.6.0
 */