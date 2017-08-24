package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@TargetApi(15)
@RequiresApi(15)
class ResourcesCompatIcsMr1
{
  public static Drawable getDrawableForDensity(Resources paramResources, int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return paramResources.getDrawableForDensity(paramInt1, paramInt2);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.res.ResourcesCompatIcsMr1
 * JD-Core Version:    0.6.0
 */