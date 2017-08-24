package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.List;

public final class AccessibilityManagerCompat
{
  private static final AccessibilityManagerVersionImpl IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 19)
    {
      IMPL = new AccessibilityManagerKitKatImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new AccessibilityManagerIcsImpl();
      return;
    }
    IMPL = new AccessibilityManagerStubImpl();
  }

  public static boolean addAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
  {
    return IMPL.addAccessibilityStateChangeListener(paramAccessibilityManager, paramAccessibilityStateChangeListener);
  }

  public static boolean addTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
  {
    return IMPL.addTouchExplorationStateChangeListener(paramAccessibilityManager, paramTouchExplorationStateChangeListener);
  }

  public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager, int paramInt)
  {
    return IMPL.getEnabledAccessibilityServiceList(paramAccessibilityManager, paramInt);
  }

  public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager)
  {
    return IMPL.getInstalledAccessibilityServiceList(paramAccessibilityManager);
  }

  public static boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
  {
    return IMPL.isTouchExplorationEnabled(paramAccessibilityManager);
  }

  public static boolean removeAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
  {
    return IMPL.removeAccessibilityStateChangeListener(paramAccessibilityManager, paramAccessibilityStateChangeListener);
  }

  public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
  {
    return IMPL.removeTouchExplorationStateChangeListener(paramAccessibilityManager, paramTouchExplorationStateChangeListener);
  }

  static class AccessibilityManagerIcsImpl extends AccessibilityManagerCompat.AccessibilityManagerStubImpl
  {
    public boolean addAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
    {
      return AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(paramAccessibilityManager, newAccessibilityStateChangeListener(paramAccessibilityStateChangeListener));
    }

    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager, int paramInt)
    {
      return AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(paramAccessibilityManager, paramInt);
    }

    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager)
    {
      return AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(paramAccessibilityManager);
    }

    public boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
    {
      return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(paramAccessibilityManager);
    }

    public AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
    {
      return new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper(paramAccessibilityStateChangeListener, new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge(paramAccessibilityStateChangeListener)
      {
        public void onAccessibilityStateChanged(boolean paramBoolean)
        {
          this.val$listener.onAccessibilityStateChanged(paramBoolean);
        }
      });
    }

    public boolean removeAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
    {
      return AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(paramAccessibilityManager, newAccessibilityStateChangeListener(paramAccessibilityStateChangeListener));
    }
  }

  static class AccessibilityManagerKitKatImpl extends AccessibilityManagerCompat.AccessibilityManagerIcsImpl
  {
    public boolean addTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
    {
      return AccessibilityManagerCompatKitKat.addTouchExplorationStateChangeListener(paramAccessibilityManager, newTouchExplorationStateChangeListener(paramTouchExplorationStateChangeListener));
    }

    public AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
    {
      return new AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper(paramTouchExplorationStateChangeListener, new AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge(paramTouchExplorationStateChangeListener)
      {
        public void onTouchExplorationStateChanged(boolean paramBoolean)
        {
          this.val$listener.onTouchExplorationStateChanged(paramBoolean);
        }
      });
    }

    public boolean removeTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
    {
      return AccessibilityManagerCompatKitKat.removeTouchExplorationStateChangeListener(paramAccessibilityManager, newTouchExplorationStateChangeListener(paramTouchExplorationStateChangeListener));
    }
  }

  static class AccessibilityManagerStubImpl
    implements AccessibilityManagerCompat.AccessibilityManagerVersionImpl
  {
    public boolean addAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
    {
      return false;
    }

    public boolean addTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
    {
      return false;
    }

    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager, int paramInt)
    {
      return Collections.emptyList();
    }

    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager)
    {
      return Collections.emptyList();
    }

    public boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager)
    {
      return false;
    }

    public AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
    {
      return null;
    }

    public AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
    {
      return null;
    }

    public boolean removeAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
    {
      return false;
    }

    public boolean removeTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
    {
      return false;
    }
  }

  static abstract interface AccessibilityManagerVersionImpl
  {
    public abstract boolean addAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener);

    public abstract boolean addTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener);

    public abstract List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager, int paramInt);

    public abstract List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager);

    public abstract boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager);

    public abstract AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener);

    public abstract AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener);

    public abstract boolean removeAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListener paramAccessibilityStateChangeListener);

    public abstract boolean removeTouchExplorationStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityManagerCompat.TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener);
  }

  public static abstract interface AccessibilityStateChangeListener
  {
    public abstract void onAccessibilityStateChanged(boolean paramBoolean);
  }

  @Deprecated
  public static abstract class AccessibilityStateChangeListenerCompat
    implements AccessibilityManagerCompat.AccessibilityStateChangeListener
  {
  }

  public static abstract interface TouchExplorationStateChangeListener
  {
    public abstract void onTouchExplorationStateChanged(boolean paramBoolean);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityManagerCompat
 * JD-Core Version:    0.6.0
 */