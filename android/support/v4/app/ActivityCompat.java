package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.util.List;
import java.util.Map;

public class ActivityCompat extends ContextCompat
{
  private static ActivityCompatApi21.SharedElementCallback21 createCallback(SharedElementCallback paramSharedElementCallback)
  {
    SharedElementCallback21Impl localSharedElementCallback21Impl = null;
    if (paramSharedElementCallback != null)
      localSharedElementCallback21Impl = new SharedElementCallback21Impl(paramSharedElementCallback);
    return localSharedElementCallback21Impl;
  }

  private static ActivityCompatApi23.SharedElementCallback23 createCallback23(SharedElementCallback paramSharedElementCallback)
  {
    SharedElementCallback23Impl localSharedElementCallback23Impl = null;
    if (paramSharedElementCallback != null)
      localSharedElementCallback23Impl = new SharedElementCallback23Impl(paramSharedElementCallback);
    return localSharedElementCallback23Impl;
  }

  public static void finishAffinity(Activity paramActivity)
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      ActivityCompatJB.finishAffinity(paramActivity);
      return;
    }
    paramActivity.finish();
  }

  public static void finishAfterTransition(Activity paramActivity)
  {
    if (Build.VERSION.SDK_INT >= 21)
    {
      ActivityCompatApi21.finishAfterTransition(paramActivity);
      return;
    }
    paramActivity.finish();
  }

  @Nullable
  public static Uri getReferrer(Activity paramActivity)
  {
    Uri localUri;
    if (Build.VERSION.SDK_INT >= 22)
      localUri = ActivityCompatApi22.getReferrer(paramActivity);
    Intent localIntent;
    do
    {
      return localUri;
      localIntent = paramActivity.getIntent();
      localUri = (Uri)localIntent.getParcelableExtra("android.intent.extra.REFERRER");
    }
    while (localUri != null);
    String str = localIntent.getStringExtra("android.intent.extra.REFERRER_NAME");
    if (str != null)
      return Uri.parse(str);
    return null;
  }

  public static boolean invalidateOptionsMenu(Activity paramActivity)
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      ActivityCompatHoneycomb.invalidateOptionsMenu(paramActivity);
      return true;
    }
    return false;
  }

  public static void postponeEnterTransition(Activity paramActivity)
  {
    if (Build.VERSION.SDK_INT >= 21)
      ActivityCompatApi21.postponeEnterTransition(paramActivity);
  }

  public static void requestPermissions(@NonNull Activity paramActivity, @NonNull String[] paramArrayOfString, @IntRange(from=0L) int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 23)
      ActivityCompatApi23.requestPermissions(paramActivity, paramArrayOfString, paramInt);
    do
      return;
    while (!(paramActivity instanceof OnRequestPermissionsResultCallback));
    new Handler(Looper.getMainLooper()).post(new Runnable(paramArrayOfString, paramActivity, paramInt)
    {
      public void run()
      {
        int[] arrayOfInt = new int[this.val$permissions.length];
        PackageManager localPackageManager = this.val$activity.getPackageManager();
        String str = this.val$activity.getPackageName();
        int i = this.val$permissions.length;
        for (int j = 0; j < i; j++)
          arrayOfInt[j] = localPackageManager.checkPermission(this.val$permissions[j], str);
        ((ActivityCompat.OnRequestPermissionsResultCallback)this.val$activity).onRequestPermissionsResult(this.val$requestCode, this.val$permissions, arrayOfInt);
      }
    });
  }

  public static void setEnterSharedElementCallback(Activity paramActivity, SharedElementCallback paramSharedElementCallback)
  {
    if (Build.VERSION.SDK_INT >= 23)
      ActivityCompatApi23.setEnterSharedElementCallback(paramActivity, createCallback23(paramSharedElementCallback));
    do
      return;
    while (Build.VERSION.SDK_INT < 21);
    ActivityCompatApi21.setEnterSharedElementCallback(paramActivity, createCallback(paramSharedElementCallback));
  }

  public static void setExitSharedElementCallback(Activity paramActivity, SharedElementCallback paramSharedElementCallback)
  {
    if (Build.VERSION.SDK_INT >= 23)
      ActivityCompatApi23.setExitSharedElementCallback(paramActivity, createCallback23(paramSharedElementCallback));
    do
      return;
    while (Build.VERSION.SDK_INT < 21);
    ActivityCompatApi21.setExitSharedElementCallback(paramActivity, createCallback(paramSharedElementCallback));
  }

  public static boolean shouldShowRequestPermissionRationale(@NonNull Activity paramActivity, @NonNull String paramString)
  {
    if (Build.VERSION.SDK_INT >= 23)
      return ActivityCompatApi23.shouldShowRequestPermissionRationale(paramActivity, paramString);
    return false;
  }

  public static void startActivityForResult(Activity paramActivity, Intent paramIntent, int paramInt, @Nullable Bundle paramBundle)
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      ActivityCompatJB.startActivityForResult(paramActivity, paramIntent, paramInt, paramBundle);
      return;
    }
    paramActivity.startActivityForResult(paramIntent, paramInt);
  }

  public static void startIntentSenderForResult(Activity paramActivity, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, @Nullable Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      ActivityCompatJB.startIntentSenderForResult(paramActivity, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      return;
    }
    paramActivity.startIntentSenderForResult(paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4);
  }

  public static void startPostponedEnterTransition(Activity paramActivity)
  {
    if (Build.VERSION.SDK_INT >= 21)
      ActivityCompatApi21.startPostponedEnterTransition(paramActivity);
  }

  public static abstract interface OnRequestPermissionsResultCallback
  {
    public abstract void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfInt);
  }

  private static class SharedElementCallback21Impl extends ActivityCompatApi21.SharedElementCallback21
  {
    private SharedElementCallback mCallback;

    public SharedElementCallback21Impl(SharedElementCallback paramSharedElementCallback)
    {
      this.mCallback = paramSharedElementCallback;
    }

    public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF)
    {
      return this.mCallback.onCaptureSharedElementSnapshot(paramView, paramMatrix, paramRectF);
    }

    public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable)
    {
      return this.mCallback.onCreateSnapshotView(paramContext, paramParcelable);
    }

    public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap)
    {
      this.mCallback.onMapSharedElements(paramList, paramMap);
    }

    public void onRejectSharedElements(List<View> paramList)
    {
      this.mCallback.onRejectSharedElements(paramList);
    }

    public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2)
    {
      this.mCallback.onSharedElementEnd(paramList, paramList1, paramList2);
    }

    public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2)
    {
      this.mCallback.onSharedElementStart(paramList, paramList1, paramList2);
    }
  }

  private static class SharedElementCallback23Impl extends ActivityCompatApi23.SharedElementCallback23
  {
    private SharedElementCallback mCallback;

    public SharedElementCallback23Impl(SharedElementCallback paramSharedElementCallback)
    {
      this.mCallback = paramSharedElementCallback;
    }

    public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF)
    {
      return this.mCallback.onCaptureSharedElementSnapshot(paramView, paramMatrix, paramRectF);
    }

    public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable)
    {
      return this.mCallback.onCreateSnapshotView(paramContext, paramParcelable);
    }

    public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap)
    {
      this.mCallback.onMapSharedElements(paramList, paramMap);
    }

    public void onRejectSharedElements(List<View> paramList)
    {
      this.mCallback.onRejectSharedElements(paramList);
    }

    public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2)
    {
      this.mCallback.onSharedElementEnd(paramList, paramList1, paramList2);
    }

    public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2)
    {
      this.mCallback.onSharedElementStart(paramList, paramList1, paramList2);
    }

    public void onSharedElementsArrived(List<String> paramList, List<View> paramList1, ActivityCompatApi23.OnSharedElementsReadyListenerBridge paramOnSharedElementsReadyListenerBridge)
    {
      this.mCallback.onSharedElementsArrived(paramList, paramList1, new SharedElementCallback.OnSharedElementsReadyListener(paramOnSharedElementsReadyListenerBridge)
      {
        public void onSharedElementsReady()
        {
          this.val$listener.onSharedElementsReady();
        }
      });
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.ActivityCompat
 * JD-Core Version:    0.6.0
 */