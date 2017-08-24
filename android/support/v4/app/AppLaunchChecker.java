package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.SharedPreferencesCompat.EditorCompat;

public class AppLaunchChecker
{
  private static final String KEY_STARTED_FROM_LAUNCHER = "startedFromLauncher";
  private static final String SHARED_PREFS_NAME = "android.support.AppLaunchChecker";

  public static boolean hasStartedFromLauncher(Context paramContext)
  {
    return paramContext.getSharedPreferences("android.support.AppLaunchChecker", 0).getBoolean("startedFromLauncher", false);
  }

  public static void onActivityCreate(Activity paramActivity)
  {
    SharedPreferences localSharedPreferences = paramActivity.getSharedPreferences("android.support.AppLaunchChecker", 0);
    if (localSharedPreferences.getBoolean("startedFromLauncher", false));
    Intent localIntent;
    do
    {
      return;
      localIntent = paramActivity.getIntent();
    }
    while ((localIntent == null) || (!"android.intent.action.MAIN".equals(localIntent.getAction())) || ((!localIntent.hasCategory("android.intent.category.LAUNCHER")) && (!localIntent.hasCategory("android.intent.category.LEANBACK_LAUNCHER"))));
    SharedPreferencesCompat.EditorCompat.getInstance().apply(localSharedPreferences.edit().putBoolean("startedFromLauncher", true));
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.AppLaunchChecker
 * JD-Core Version:    0.6.0
 */