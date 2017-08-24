package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class NotificationManagerCompatApi24
{
  public static boolean areNotificationsEnabled(NotificationManager paramNotificationManager)
  {
    return paramNotificationManager.areNotificationsEnabled();
  }

  public static int getImportance(NotificationManager paramNotificationManager)
  {
    return paramNotificationManager.getImportance();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.NotificationManagerCompatApi24
 * JD-Core Version:    0.6.0
 */