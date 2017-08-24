package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener;

@TargetApi(19)
@RequiresApi(19)
class AccessibilityManagerCompatKitKat
{
  public static boolean addTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, Object paramObject)
  {
    return paramAccessibilityManager.addTouchExplorationStateChangeListener((AccessibilityManager.TouchExplorationStateChangeListener)paramObject);
  }

  public static Object newTouchExplorationStateChangeListener(TouchExplorationStateChangeListenerBridge paramTouchExplorationStateChangeListenerBridge)
  {
    return new AccessibilityManager.TouchExplorationStateChangeListener(paramTouchExplorationStateChangeListenerBridge)
    {
      public void onTouchExplorationStateChanged(boolean paramBoolean)
      {
        this.val$bridge.onTouchExplorationStateChanged(paramBoolean);
      }
    };
  }

  public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, Object paramObject)
  {
    return paramAccessibilityManager.removeTouchExplorationStateChangeListener((AccessibilityManager.TouchExplorationStateChangeListener)paramObject);
  }

  static abstract interface TouchExplorationStateChangeListenerBridge
  {
    public abstract void onTouchExplorationStateChanged(boolean paramBoolean);
  }

  public static class TouchExplorationStateChangeListenerWrapper
    implements AccessibilityManager.TouchExplorationStateChangeListener
  {
    final Object mListener;
    final AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge mListenerBridge;

    public TouchExplorationStateChangeListenerWrapper(Object paramObject, AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge paramTouchExplorationStateChangeListenerBridge)
    {
      this.mListener = paramObject;
      this.mListenerBridge = paramTouchExplorationStateChangeListenerBridge;
    }

    public boolean equals(Object paramObject)
    {
      if (this == paramObject);
      TouchExplorationStateChangeListenerWrapper localTouchExplorationStateChangeListenerWrapper;
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
        localTouchExplorationStateChangeListenerWrapper = (TouchExplorationStateChangeListenerWrapper)paramObject;
        if (this.mListener != null)
          break;
        if (localTouchExplorationStateChangeListenerWrapper.mListener != null)
          return false;
      }
      return this.mListener.equals(localTouchExplorationStateChangeListenerWrapper.mListener);
    }

    public int hashCode()
    {
      if (this.mListener == null)
        return 0;
      return this.mListener.hashCode();
    }

    public void onTouchExplorationStateChanged(boolean paramBoolean)
    {
      this.mListenerBridge.onTouchExplorationStateChanged(paramBoolean);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat
 * JD-Core Version:    0.6.0
 */