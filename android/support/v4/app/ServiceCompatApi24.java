package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Service;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class ServiceCompatApi24
{
  public static void stopForeground(Service paramService, int paramInt)
  {
    paramService.stopForeground(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.ServiceCompatApi24
 * JD-Core Version:    0.6.0
 */