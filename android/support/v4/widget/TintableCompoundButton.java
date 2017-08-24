package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.Nullable;

public abstract interface TintableCompoundButton
{
  @Nullable
  public abstract ColorStateList getSupportButtonTintList();

  @Nullable
  public abstract PorterDuff.Mode getSupportButtonTintMode();

  public abstract void setSupportButtonTintList(@Nullable ColorStateList paramColorStateList);

  public abstract void setSupportButtonTintMode(@Nullable PorterDuff.Mode paramMode);
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.TintableCompoundButton
 * JD-Core Version:    0.6.0
 */