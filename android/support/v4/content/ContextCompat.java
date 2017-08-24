package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.BuildCompat;
import android.util.Log;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat
{
  private static final String DIR_ANDROID = "Android";
  private static final String DIR_OBB = "obb";
  private static final String TAG = "ContextCompat";
  private static final Object sLock = new Object();
  private static TypedValue sTempValue;

  private static File buildPath(File paramFile, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    int j = 0;
    Object localObject1 = paramFile;
    if (j < i)
    {
      String str = paramArrayOfString[j];
      Object localObject2;
      if (localObject1 == null)
        localObject2 = new File(str);
      while (true)
      {
        j++;
        localObject1 = localObject2;
        break;
        if (str != null)
        {
          localObject2 = new File((File)localObject1, str);
          continue;
        }
        localObject2 = localObject1;
      }
    }
    return (File)(File)localObject1;
  }

  public static int checkSelfPermission(@NonNull Context paramContext, @NonNull String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("permission is null");
    return paramContext.checkPermission(paramString, Process.myPid(), Process.myUid());
  }

  public static Context createDeviceProtectedStorageContext(Context paramContext)
  {
    if (BuildCompat.isAtLeastN())
      return ContextCompatApi24.createDeviceProtectedStorageContext(paramContext);
    return null;
  }

  private static File createFilesDir(File paramFile)
  {
    monitorenter;
    try
    {
      if ((!paramFile.exists()) && (!paramFile.mkdirs()))
      {
        boolean bool = paramFile.exists();
        if (!bool)
          break label31;
      }
      while (true)
      {
        return paramFile;
        label31: Log.w("ContextCompat", "Unable to create files subdir " + paramFile.getPath());
        paramFile = null;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static File getCodeCacheDir(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return ContextCompatApi21.getCodeCacheDir(paramContext);
    return createFilesDir(new File(paramContext.getApplicationInfo().dataDir, "code_cache"));
  }

  @ColorInt
  public static final int getColor(Context paramContext, @ColorRes int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 23)
      return ContextCompatApi23.getColor(paramContext, paramInt);
    return paramContext.getResources().getColor(paramInt);
  }

  public static final ColorStateList getColorStateList(Context paramContext, @ColorRes int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 23)
      return ContextCompatApi23.getColorStateList(paramContext, paramInt);
    return paramContext.getResources().getColorStateList(paramInt);
  }

  public static File getDataDir(Context paramContext)
  {
    if (BuildCompat.isAtLeastN())
      return ContextCompatApi24.getDataDir(paramContext);
    String str = paramContext.getApplicationInfo().dataDir;
    if (str != null)
      return new File(str);
    return null;
  }

  public static final Drawable getDrawable(Context paramContext, @DrawableRes int paramInt)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 21)
      return ContextCompatApi21.getDrawable(paramContext, paramInt);
    if (i >= 16)
      return paramContext.getResources().getDrawable(paramInt);
    synchronized (sLock)
    {
      if (sTempValue == null)
        sTempValue = new TypedValue();
      paramContext.getResources().getValue(paramInt, sTempValue, true);
      int j = sTempValue.resourceId;
      return paramContext.getResources().getDrawable(j);
    }
  }

  public static File[] getExternalCacheDirs(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 19)
      return ContextCompatKitKat.getExternalCacheDirs(paramContext);
    File[] arrayOfFile = new File[1];
    arrayOfFile[0] = paramContext.getExternalCacheDir();
    return arrayOfFile;
  }

  public static File[] getExternalFilesDirs(Context paramContext, String paramString)
  {
    if (Build.VERSION.SDK_INT >= 19)
      return ContextCompatKitKat.getExternalFilesDirs(paramContext, paramString);
    File[] arrayOfFile = new File[1];
    arrayOfFile[0] = paramContext.getExternalFilesDir(paramString);
    return arrayOfFile;
  }

  public static final File getNoBackupFilesDir(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return ContextCompatApi21.getNoBackupFilesDir(paramContext);
    return createFilesDir(new File(paramContext.getApplicationInfo().dataDir, "no_backup"));
  }

  public static File[] getObbDirs(Context paramContext)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 19)
      return ContextCompatKitKat.getObbDirs(paramContext);
    if (i >= 11);
    File localFile1;
    String[] arrayOfString;
    for (File localFile2 = ContextCompatHoneycomb.getObbDir(paramContext); ; localFile2 = buildPath(localFile1, arrayOfString))
    {
      return new File[] { localFile2 };
      localFile1 = Environment.getExternalStorageDirectory();
      arrayOfString = new String[3];
      arrayOfString[0] = "Android";
      arrayOfString[1] = "obb";
      arrayOfString[2] = paramContext.getPackageName();
    }
  }

  public static boolean isDeviceProtectedStorage(Context paramContext)
  {
    if (BuildCompat.isAtLeastN())
      return ContextCompatApi24.isDeviceProtectedStorage(paramContext);
    return false;
  }

  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent)
  {
    return startActivities(paramContext, paramArrayOfIntent, null);
  }

  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 16)
    {
      ContextCompatJellybean.startActivities(paramContext, paramArrayOfIntent, paramBundle);
      return true;
    }
    if (i >= 11)
    {
      ContextCompatHoneycomb.startActivities(paramContext, paramArrayOfIntent);
      return true;
    }
    return false;
  }

  public static void startActivity(Context paramContext, Intent paramIntent, @Nullable Bundle paramBundle)
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      ContextCompatJellybean.startActivity(paramContext, paramIntent, paramBundle);
      return;
    }
    paramContext.startActivity(paramIntent);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.ContextCompat
 * JD-Core Version:    0.6.0
 */