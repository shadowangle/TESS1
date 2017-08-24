package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public final class PopupWindowCompat
{
  static final PopupWindowImpl IMPL;

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 23)
    {
      IMPL = new Api23PopupWindowImpl();
      return;
    }
    if (i >= 21)
    {
      IMPL = new Api21PopupWindowImpl();
      return;
    }
    if (i >= 19)
    {
      IMPL = new KitKatPopupWindowImpl();
      return;
    }
    IMPL = new BasePopupWindowImpl();
  }

  public static boolean getOverlapAnchor(PopupWindow paramPopupWindow)
  {
    return IMPL.getOverlapAnchor(paramPopupWindow);
  }

  public static int getWindowLayoutType(PopupWindow paramPopupWindow)
  {
    return IMPL.getWindowLayoutType(paramPopupWindow);
  }

  public static void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
  {
    IMPL.setOverlapAnchor(paramPopupWindow, paramBoolean);
  }

  public static void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt)
  {
    IMPL.setWindowLayoutType(paramPopupWindow, paramInt);
  }

  public static void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    IMPL.showAsDropDown(paramPopupWindow, paramView, paramInt1, paramInt2, paramInt3);
  }

  static class Api21PopupWindowImpl extends PopupWindowCompat.KitKatPopupWindowImpl
  {
    public boolean getOverlapAnchor(PopupWindow paramPopupWindow)
    {
      return PopupWindowCompatApi21.getOverlapAnchor(paramPopupWindow);
    }

    public void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
    {
      PopupWindowCompatApi21.setOverlapAnchor(paramPopupWindow, paramBoolean);
    }
  }

  static class Api23PopupWindowImpl extends PopupWindowCompat.Api21PopupWindowImpl
  {
    public boolean getOverlapAnchor(PopupWindow paramPopupWindow)
    {
      return PopupWindowCompatApi23.getOverlapAnchor(paramPopupWindow);
    }

    public int getWindowLayoutType(PopupWindow paramPopupWindow)
    {
      return PopupWindowCompatApi23.getWindowLayoutType(paramPopupWindow);
    }

    public void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
    {
      PopupWindowCompatApi23.setOverlapAnchor(paramPopupWindow, paramBoolean);
    }

    public void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt)
    {
      PopupWindowCompatApi23.setWindowLayoutType(paramPopupWindow, paramInt);
    }
  }

  static class BasePopupWindowImpl
    implements PopupWindowCompat.PopupWindowImpl
  {
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;

    public boolean getOverlapAnchor(PopupWindow paramPopupWindow)
    {
      return false;
    }

    public int getWindowLayoutType(PopupWindow paramPopupWindow)
    {
      if (!sGetWindowLayoutTypeMethodAttempted);
      try
      {
        sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", new Class[0]);
        sGetWindowLayoutTypeMethod.setAccessible(true);
        label27: sGetWindowLayoutTypeMethodAttempted = true;
        if (sGetWindowLayoutTypeMethod != null)
          try
          {
            int i = ((Integer)sGetWindowLayoutTypeMethod.invoke(paramPopupWindow, new Object[0])).intValue();
            return i;
          }
          catch (Exception localException1)
          {
          }
        return 0;
      }
      catch (Exception localException2)
      {
        break label27;
      }
    }

    public void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
    {
    }

    public void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt)
    {
      if (!sSetWindowLayoutTypeMethodAttempted);
      try
      {
        Class[] arrayOfClass = new Class[1];
        arrayOfClass[0] = Integer.TYPE;
        sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", arrayOfClass);
        sSetWindowLayoutTypeMethod.setAccessible(true);
        sSetWindowLayoutTypeMethodAttempted = true;
        if (sSetWindowLayoutTypeMethod == null);
      }
      catch (Exception localException2)
      {
        try
        {
          Method localMethod = sSetWindowLayoutTypeMethod;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Integer.valueOf(paramInt);
          localMethod.invoke(paramPopupWindow, arrayOfObject);
          return;
          localException2 = localException2;
        }
        catch (Exception localException1)
        {
        }
      }
    }

    public void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3)
    {
      if ((0x7 & GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection(paramView))) == 5)
        paramInt1 -= paramPopupWindow.getWidth() - paramView.getWidth();
      paramPopupWindow.showAsDropDown(paramView, paramInt1, paramInt2);
    }
  }

  static class KitKatPopupWindowImpl extends PopupWindowCompat.BasePopupWindowImpl
  {
    public void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3)
    {
      PopupWindowCompatKitKat.showAsDropDown(paramPopupWindow, paramView, paramInt1, paramInt2, paramInt3);
    }
  }

  static abstract interface PopupWindowImpl
  {
    public abstract boolean getOverlapAnchor(PopupWindow paramPopupWindow);

    public abstract int getWindowLayoutType(PopupWindow paramPopupWindow);

    public abstract void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean);

    public abstract void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt);

    public abstract void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.PopupWindowCompat
 * JD-Core Version:    0.6.0
 */