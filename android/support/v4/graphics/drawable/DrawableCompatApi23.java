package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class DrawableCompatApi23
{
  public static int getLayoutDirection(Drawable paramDrawable)
  {
    return paramDrawable.getLayoutDirection();
  }

  public static boolean setLayoutDirection(Drawable paramDrawable, int paramInt)
  {
    return paramDrawable.setLayoutDirection(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.graphics.drawable.DrawableCompatApi23
 * JD-Core Version:    0.6.0
 */