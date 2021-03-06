package android.support.v4.view;

import android.os.Build.VERSION;

public final class ScaleGestureDetectorCompat
{
  static final ScaleGestureDetectorImpl IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 19)
    {
      IMPL = new ScaleGestureDetectorCompatKitKatImpl();
      return;
    }
    IMPL = new BaseScaleGestureDetectorImpl();
  }

  public static boolean isQuickScaleEnabled(Object paramObject)
  {
    return IMPL.isQuickScaleEnabled(paramObject);
  }

  public static void setQuickScaleEnabled(Object paramObject, boolean paramBoolean)
  {
    IMPL.setQuickScaleEnabled(paramObject, paramBoolean);
  }

  private static class BaseScaleGestureDetectorImpl
    implements ScaleGestureDetectorCompat.ScaleGestureDetectorImpl
  {
    public boolean isQuickScaleEnabled(Object paramObject)
    {
      return false;
    }

    public void setQuickScaleEnabled(Object paramObject, boolean paramBoolean)
    {
    }
  }

  private static class ScaleGestureDetectorCompatKitKatImpl
    implements ScaleGestureDetectorCompat.ScaleGestureDetectorImpl
  {
    public boolean isQuickScaleEnabled(Object paramObject)
    {
      return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(paramObject);
    }

    public void setQuickScaleEnabled(Object paramObject, boolean paramBoolean)
    {
      ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(paramObject, paramBoolean);
    }
  }

  static abstract interface ScaleGestureDetectorImpl
  {
    public abstract boolean isQuickScaleEnabled(Object paramObject);

    public abstract void setQuickScaleEnabled(Object paramObject, boolean paramBoolean);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ScaleGestureDetectorCompat
 * JD-Core Version:    0.6.0
 */