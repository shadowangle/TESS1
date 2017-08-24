package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@TargetApi(17)
@RequiresApi(17)
class ConfigurationHelperJellybeanMr1
{
  static int getDensityDpi(@NonNull Resources paramResources)
  {
    return paramResources.getConfiguration().densityDpi;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.res.ConfigurationHelperJellybeanMr1
 * JD-Core Version:    0.6.0
 */