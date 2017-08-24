package android.support.v4.widget;

import android.os.Build.VERSION;
import android.view.View.OnTouchListener;

public final class PopupMenuCompat
{
  static final PopupMenuImpl IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 19)
    {
      IMPL = new KitKatPopupMenuImpl();
      return;
    }
    IMPL = new BasePopupMenuImpl();
  }

  public static View.OnTouchListener getDragToOpenListener(Object paramObject)
  {
    return IMPL.getDragToOpenListener(paramObject);
  }

  static class BasePopupMenuImpl
    implements PopupMenuCompat.PopupMenuImpl
  {
    public View.OnTouchListener getDragToOpenListener(Object paramObject)
    {
      return null;
    }
  }

  static class KitKatPopupMenuImpl extends PopupMenuCompat.BasePopupMenuImpl
  {
    public View.OnTouchListener getDragToOpenListener(Object paramObject)
    {
      return PopupMenuCompatKitKat.getDragToOpenListener(paramObject);
    }
  }

  static abstract interface PopupMenuImpl
  {
    public abstract View.OnTouchListener getDragToOpenListener(Object paramObject);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.PopupMenuCompat
 * JD-Core Version:    0.6.0
 */