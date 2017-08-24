package android.support.v4.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import java.lang.reflect.Field;

@TargetApi(9)
@RequiresApi(9)
class ViewCompatBase
{
  private static final String TAG = "ViewCompatBase";
  private static Field sMinHeightField;
  private static boolean sMinHeightFieldFetched;
  private static Field sMinWidthField;
  private static boolean sMinWidthFieldFetched;

  static ColorStateList getBackgroundTintList(View paramView)
  {
    if ((paramView instanceof TintableBackgroundView))
      return ((TintableBackgroundView)paramView).getSupportBackgroundTintList();
    return null;
  }

  static PorterDuff.Mode getBackgroundTintMode(View paramView)
  {
    if ((paramView instanceof TintableBackgroundView))
      return ((TintableBackgroundView)paramView).getSupportBackgroundTintMode();
    return null;
  }

  static Display getDisplay(View paramView)
  {
    if (isAttachedToWindow(paramView))
      return ((WindowManager)paramView.getContext().getSystemService("window")).getDefaultDisplay();
    return null;
  }

  static int getMinimumHeight(View paramView)
  {
    if (!sMinHeightFieldFetched);
    try
    {
      sMinHeightField = View.class.getDeclaredField("mMinHeight");
      sMinHeightField.setAccessible(true);
      label23: sMinHeightFieldFetched = true;
      if (sMinHeightField != null)
        try
        {
          int i = ((Integer)sMinHeightField.get(paramView)).intValue();
          return i;
        }
        catch (Exception localException)
        {
        }
      return 0;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      break label23;
    }
  }

  static int getMinimumWidth(View paramView)
  {
    if (!sMinWidthFieldFetched);
    try
    {
      sMinWidthField = View.class.getDeclaredField("mMinWidth");
      sMinWidthField.setAccessible(true);
      label23: sMinWidthFieldFetched = true;
      if (sMinWidthField != null)
        try
        {
          int i = ((Integer)sMinWidthField.get(paramView)).intValue();
          return i;
        }
        catch (Exception localException)
        {
        }
      return 0;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      break label23;
    }
  }

  static boolean isAttachedToWindow(View paramView)
  {
    return paramView.getWindowToken() != null;
  }

  static boolean isLaidOut(View paramView)
  {
    return (paramView.getWidth() > 0) && (paramView.getHeight() > 0);
  }

  static void offsetLeftAndRight(View paramView, int paramInt)
  {
    int i = paramView.getLeft();
    paramView.offsetLeftAndRight(paramInt);
    if (paramInt != 0)
    {
      ViewParent localViewParent = paramView.getParent();
      if ((localViewParent instanceof View))
      {
        int j = Math.abs(paramInt);
        ((View)localViewParent).invalidate(i - j, paramView.getTop(), j + (i + paramView.getWidth()), paramView.getBottom());
      }
    }
    else
    {
      return;
    }
    paramView.invalidate();
  }

  static void offsetTopAndBottom(View paramView, int paramInt)
  {
    int i = paramView.getTop();
    paramView.offsetTopAndBottom(paramInt);
    if (paramInt != 0)
    {
      ViewParent localViewParent = paramView.getParent();
      if ((localViewParent instanceof View))
      {
        int j = Math.abs(paramInt);
        ((View)localViewParent).invalidate(paramView.getLeft(), i - j, paramView.getRight(), j + (i + paramView.getHeight()));
      }
    }
    else
    {
      return;
    }
    paramView.invalidate();
  }

  static void setBackgroundTintList(View paramView, ColorStateList paramColorStateList)
  {
    if ((paramView instanceof TintableBackgroundView))
      ((TintableBackgroundView)paramView).setSupportBackgroundTintList(paramColorStateList);
  }

  static void setBackgroundTintMode(View paramView, PorterDuff.Mode paramMode)
  {
    if ((paramView instanceof TintableBackgroundView))
      ((TintableBackgroundView)paramView).setSupportBackgroundTintMode(paramMode);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewCompatBase
 * JD-Core Version:    0.6.0
 */