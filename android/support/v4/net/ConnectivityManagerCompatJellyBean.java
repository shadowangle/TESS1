package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class ConnectivityManagerCompatJellyBean
{
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
  {
    return paramConnectivityManager.isActiveNetworkMetered();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.net.ConnectivityManagerCompatJellyBean
 * JD-Core Version:    0.6.0
 */