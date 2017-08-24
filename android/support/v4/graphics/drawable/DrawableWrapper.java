package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public abstract interface DrawableWrapper
{
  public abstract Drawable getWrappedDrawable();

  public abstract void setWrappedDrawable(Drawable paramDrawable);
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.graphics.drawable.DrawableWrapper
 * JD-Core Version:    0.6.0
 */