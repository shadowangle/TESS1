package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewConfiguration;

public final class ViewConfigurationCompat
{
  static final ViewConfigurationVersionImpl IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new IcsViewConfigurationVersionImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new HoneycombViewConfigurationVersionImpl();
      return;
    }
    IMPL = new BaseViewConfigurationVersionImpl();
  }

  @Deprecated
  public static int getScaledPagingTouchSlop(ViewConfiguration paramViewConfiguration)
  {
    return paramViewConfiguration.getScaledPagingTouchSlop();
  }

  public static boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
  {
    return IMPL.hasPermanentMenuKey(paramViewConfiguration);
  }

  static class BaseViewConfigurationVersionImpl
    implements ViewConfigurationCompat.ViewConfigurationVersionImpl
  {
    public boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
    {
      return true;
    }
  }

  static class HoneycombViewConfigurationVersionImpl extends ViewConfigurationCompat.BaseViewConfigurationVersionImpl
  {
    public boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
    {
      return false;
    }
  }

  static class IcsViewConfigurationVersionImpl extends ViewConfigurationCompat.HoneycombViewConfigurationVersionImpl
  {
    public boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
    {
      return ViewConfigurationCompatICS.hasPermanentMenuKey(paramViewConfiguration);
    }
  }

  static abstract interface ViewConfigurationVersionImpl
  {
    public abstract boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewConfigurationCompat
 * JD-Core Version:    0.6.0
 */