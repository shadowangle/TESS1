package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class ConnectivityManagerCompatApi24
{
  public static int getRestrictBackgroundStatus(ConnectivityManager paramConnectivityManager)
  {
    return paramConnectivityManager.getRestrictBackgroundStatus();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.net.ConnectivityManagerCompatApi24
 * JD-Core Version:    0.6.0
 */