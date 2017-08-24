package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

@TargetApi(17)
@RequiresApi(17)
class TextViewCompatJbMr1
{
  public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView paramTextView)
  {
    int i = 1;
    if (paramTextView.getLayoutDirection() == i);
    while (true)
    {
      Drawable[] arrayOfDrawable = paramTextView.getCompoundDrawables();
      if (i != 0)
      {
        Drawable localDrawable1 = arrayOfDrawable[2];
        Drawable localDrawable2 = arrayOfDrawable[0];
        arrayOfDrawable[0] = localDrawable1;
        arrayOfDrawable[2] = localDrawable2;
      }
      return arrayOfDrawable;
      i = 0;
    }
  }

  public static void setCompoundDrawablesRelative(@NonNull TextView paramTextView, @Nullable Drawable paramDrawable1, @Nullable Drawable paramDrawable2, @Nullable Drawable paramDrawable3, @Nullable Drawable paramDrawable4)
  {
    int i;
    if (paramTextView.getLayoutDirection() == 1)
    {
      i = 1;
      if (i == 0)
        break label43;
    }
    label43: for (Drawable localDrawable = paramDrawable3; ; localDrawable = paramDrawable1)
    {
      if (i != 0)
        paramDrawable3 = paramDrawable1;
      paramTextView.setCompoundDrawables(localDrawable, paramDrawable2, paramDrawable3, paramDrawable4);
      return;
      i = 0;
      break;
    }
  }

  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView paramTextView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i;
    if (paramTextView.getLayoutDirection() == 1)
    {
      i = 1;
      if (i == 0)
        break label43;
    }
    label43: for (int j = paramInt3; ; j = paramInt1)
    {
      if (i != 0)
        paramInt3 = paramInt1;
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(j, paramInt2, paramInt3, paramInt4);
      return;
      i = 0;
      break;
    }
  }

  public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView paramTextView, @Nullable Drawable paramDrawable1, @Nullable Drawable paramDrawable2, @Nullable Drawable paramDrawable3, @Nullable Drawable paramDrawable4)
  {
    int i;
    if (paramTextView.getLayoutDirection() == 1)
    {
      i = 1;
      if (i == 0)
        break label43;
    }
    label43: for (Drawable localDrawable = paramDrawable3; ; localDrawable = paramDrawable1)
    {
      if (i != 0)
        paramDrawable3 = paramDrawable1;
      paramTextView.setCompoundDrawablesWithIntrinsicBounds(localDrawable, paramDrawable2, paramDrawable3, paramDrawable4);
      return;
      i = 0;
      break;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.TextViewCompatJbMr1
 * JD-Core Version:    0.6.0
 */