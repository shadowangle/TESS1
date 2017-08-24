package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class ActivityManagerCompatKitKat
{
  public static boolean isLowRamDevice(ActivityManager paramActivityManager)
  {
    return paramActivityManager.isLowRamDevice();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.ActivityManagerCompatKitKat
 * JD-Core Version:    0.6.0
 */