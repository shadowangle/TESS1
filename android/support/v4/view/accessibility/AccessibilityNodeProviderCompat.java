package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat
{
  public static final int HOST_VIEW_ID = -1;
  private static final AccessibilityNodeProviderImpl IMPL;
  private final Object mProvider;

  static
  {
    if (Build.VERSION.SDK_INT >= 19)
    {
      IMPL = new AccessibilityNodeProviderKitKatImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new AccessibilityNodeProviderJellyBeanImpl();
      return;
    }
    IMPL = new AccessibilityNodeProviderStubImpl();
  }

  public AccessibilityNodeProviderCompat()
  {
    this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
  }

  public AccessibilityNodeProviderCompat(Object paramObject)
  {
    this.mProvider = paramObject;
  }

  @Nullable
  public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int paramInt)
  {
    return null;
  }

  @Nullable
  public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String paramString, int paramInt)
  {
    return null;
  }

  @Nullable
  public AccessibilityNodeInfoCompat findFocus(int paramInt)
  {
    return null;
  }

  public Object getProvider()
  {
    return this.mProvider;
  }

  public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    return false;
  }

  static abstract interface AccessibilityNodeProviderImpl
  {
    public abstract Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat paramAccessibilityNodeProviderCompat);
  }

  private static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl
  {
    public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat paramAccessibilityNodeProviderCompat)
    {
      return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge(paramAccessibilityNodeProviderCompat)
      {
        public Object createAccessibilityNodeInfo(int paramInt)
        {
          AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = this.val$compat.createAccessibilityNodeInfo(paramInt);
          if (localAccessibilityNodeInfoCompat == null)
            return null;
          return localAccessibilityNodeInfoCompat.getInfo();
        }

        public List<Object> findAccessibilityNodeInfosByText(String paramString, int paramInt)
        {
          List localList = this.val$compat.findAccessibilityNodeInfosByText(paramString, paramInt);
          if (localList == null)
            return null;
          ArrayList localArrayList = new ArrayList();
          int i = localList.size();
          for (int j = 0; j < i; j++)
            localArrayList.add(((AccessibilityNodeInfoCompat)localList.get(j)).getInfo());
          return localArrayList;
        }

        public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
        {
          return this.val$compat.performAction(paramInt1, paramInt2, paramBundle);
        }
      });
    }
  }

  private static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl
  {
    public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat paramAccessibilityNodeProviderCompat)
    {
      return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge(paramAccessibilityNodeProviderCompat)
      {
        public Object createAccessibilityNodeInfo(int paramInt)
        {
          AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = this.val$compat.createAccessibilityNodeInfo(paramInt);
          if (localAccessibilityNodeInfoCompat == null)
            return null;
          return localAccessibilityNodeInfoCompat.getInfo();
        }

        public List<Object> findAccessibilityNodeInfosByText(String paramString, int paramInt)
        {
          List localList = this.val$compat.findAccessibilityNodeInfosByText(paramString, paramInt);
          if (localList == null)
            return null;
          ArrayList localArrayList = new ArrayList();
          int i = localList.size();
          for (int j = 0; j < i; j++)
            localArrayList.add(((AccessibilityNodeInfoCompat)localList.get(j)).getInfo());
          return localArrayList;
        }

        public Object findFocus(int paramInt)
        {
          AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = this.val$compat.findFocus(paramInt);
          if (localAccessibilityNodeInfoCompat == null)
            return null;
          return localAccessibilityNodeInfoCompat.getInfo();
        }

        public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
        {
          return this.val$compat.performAction(paramInt1, paramInt2, paramBundle);
        }
      });
    }
  }

  static class AccessibilityNodeProviderStubImpl
    implements AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl
  {
    public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat paramAccessibilityNodeProviderCompat)
    {
      return null;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityNodeProviderCompat
 * JD-Core Version:    0.6.0
 */