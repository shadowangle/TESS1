package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.View;

public final class KeyEventCompat
{
  static final KeyEventVersionImpl IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new HoneycombKeyEventVersionImpl();
      return;
    }
    IMPL = new BaseKeyEventVersionImpl();
  }

  @Deprecated
  public static boolean dispatch(KeyEvent paramKeyEvent, KeyEvent.Callback paramCallback, Object paramObject1, Object paramObject2)
  {
    return paramKeyEvent.dispatch(paramCallback, (KeyEvent.DispatcherState)paramObject1, paramObject2);
  }

  @Deprecated
  public static Object getKeyDispatcherState(View paramView)
  {
    return paramView.getKeyDispatcherState();
  }

  public static boolean hasModifiers(KeyEvent paramKeyEvent, int paramInt)
  {
    return IMPL.metaStateHasModifiers(paramKeyEvent.getMetaState(), paramInt);
  }

  public static boolean hasNoModifiers(KeyEvent paramKeyEvent)
  {
    return IMPL.metaStateHasNoModifiers(paramKeyEvent.getMetaState());
  }

  public static boolean isCtrlPressed(KeyEvent paramKeyEvent)
  {
    return IMPL.isCtrlPressed(paramKeyEvent);
  }

  @Deprecated
  public static boolean isTracking(KeyEvent paramKeyEvent)
  {
    return paramKeyEvent.isTracking();
  }

  public static boolean metaStateHasModifiers(int paramInt1, int paramInt2)
  {
    return IMPL.metaStateHasModifiers(paramInt1, paramInt2);
  }

  public static boolean metaStateHasNoModifiers(int paramInt)
  {
    return IMPL.metaStateHasNoModifiers(paramInt);
  }

  public static int normalizeMetaState(int paramInt)
  {
    return IMPL.normalizeMetaState(paramInt);
  }

  @Deprecated
  public static void startTracking(KeyEvent paramKeyEvent)
  {
    paramKeyEvent.startTracking();
  }

  static class BaseKeyEventVersionImpl
    implements KeyEventCompat.KeyEventVersionImpl
  {
    private static final int META_ALL_MASK = 247;
    private static final int META_MODIFIER_MASK = 247;

    private static int metaStateFilterDirectionalModifiers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      int i = 1;
      int j;
      int k;
      if ((paramInt2 & paramInt3) != 0)
      {
        j = i;
        k = paramInt4 | paramInt5;
        if ((paramInt2 & k) == 0)
          break label52;
      }
      while (true)
      {
        if (j == 0)
          break label67;
        if (i == 0)
          break label58;
        throw new IllegalArgumentException("bad arguments");
        j = 0;
        break;
        label52: i = 0;
      }
      label58: paramInt1 &= (k ^ 0xFFFFFFFF);
      label67: 
      do
        return paramInt1;
      while (i == 0);
      return paramInt1 & (paramInt3 ^ 0xFFFFFFFF);
    }

    public boolean isCtrlPressed(KeyEvent paramKeyEvent)
    {
      return false;
    }

    public boolean metaStateHasModifiers(int paramInt1, int paramInt2)
    {
      return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(0xF7 & normalizeMetaState(paramInt1), paramInt2, 1, 64, 128), paramInt2, 2, 16, 32) == paramInt2;
    }

    public boolean metaStateHasNoModifiers(int paramInt)
    {
      return (0xF7 & normalizeMetaState(paramInt)) == 0;
    }

    public int normalizeMetaState(int paramInt)
    {
      if ((paramInt & 0xC0) != 0);
      for (int i = paramInt | 0x1; ; i = paramInt)
      {
        if ((i & 0x30) != 0)
          i |= 2;
        return i & 0xF7;
      }
    }
  }

  static class HoneycombKeyEventVersionImpl extends KeyEventCompat.BaseKeyEventVersionImpl
  {
    public boolean isCtrlPressed(KeyEvent paramKeyEvent)
    {
      return KeyEventCompatHoneycomb.isCtrlPressed(paramKeyEvent);
    }

    public boolean metaStateHasModifiers(int paramInt1, int paramInt2)
    {
      return KeyEventCompatHoneycomb.metaStateHasModifiers(paramInt1, paramInt2);
    }

    public boolean metaStateHasNoModifiers(int paramInt)
    {
      return KeyEventCompatHoneycomb.metaStateHasNoModifiers(paramInt);
    }

    public int normalizeMetaState(int paramInt)
    {
      return KeyEventCompatHoneycomb.normalizeMetaState(paramInt);
    }
  }

  static abstract interface KeyEventVersionImpl
  {
    public abstract boolean isCtrlPressed(KeyEvent paramKeyEvent);

    public abstract boolean metaStateHasModifiers(int paramInt1, int paramInt2);

    public abstract boolean metaStateHasNoModifiers(int paramInt);

    public abstract int normalizeMetaState(int paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.KeyEventCompat
 * JD-Core Version:    0.6.0
 */