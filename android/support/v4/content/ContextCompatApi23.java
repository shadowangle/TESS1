package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class ContextCompatApi23
{
  public static int getColor(Context paramContext, int paramInt)
  {
    return paramContext.getColor(paramInt);
  }

  public static ColorStateList getColorStateList(Context paramContext, int paramInt)
  {
    return paramContext.getColorStateList(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.ContextCompatApi23
 * JD-Core Version:    0.6.0
 */