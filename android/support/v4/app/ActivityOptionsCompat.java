package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.view.View;

public class ActivityOptionsCompat
{
  public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
  public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

  public static ActivityOptionsCompat makeBasic()
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeBasic());
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeBasic());
    return new ActivityOptionsCompat();
  }

  public static ActivityOptionsCompat makeClipRevealAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeClipRevealAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4));
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeClipRevealAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4));
    return new ActivityOptionsCompat();
  }

  public static ActivityOptionsCompat makeCustomAnimation(Context paramContext, int paramInt1, int paramInt2)
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeCustomAnimation(paramContext, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeCustomAnimation(paramContext, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT >= 21)
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeCustomAnimation(paramContext, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT >= 16)
      return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(paramContext, paramInt1, paramInt2));
    return new ActivityOptionsCompat();
  }

  public static ActivityOptionsCompat makeScaleUpAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4));
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4));
    if (Build.VERSION.SDK_INT >= 21)
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4));
    if (Build.VERSION.SDK_INT >= 16)
      return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4));
    return new ActivityOptionsCompat();
  }

  public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity paramActivity, View paramView, String paramString)
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeSceneTransitionAnimation(paramActivity, paramView, paramString));
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeSceneTransitionAnimation(paramActivity, paramView, paramString));
    if (Build.VERSION.SDK_INT >= 21)
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(paramActivity, paramView, paramString));
    return new ActivityOptionsCompat();
  }

  public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity paramActivity, Pair<View, String>[] paramArrayOfPair)
  {
    String[] arrayOfString2;
    View[] arrayOfView1;
    if (Build.VERSION.SDK_INT >= 21)
    {
      if (paramArrayOfPair == null)
        break label144;
      View[] arrayOfView2 = new View[paramArrayOfPair.length];
      arrayOfString2 = new String[paramArrayOfPair.length];
      for (int i = 0; i < paramArrayOfPair.length; i++)
      {
        arrayOfView2[i] = ((View)paramArrayOfPair[i].first);
        arrayOfString2[i] = ((String)paramArrayOfPair[i].second);
      }
      arrayOfView1 = arrayOfView2;
    }
    for (String[] arrayOfString1 = arrayOfString2; ; arrayOfString1 = null)
    {
      if (Build.VERSION.SDK_INT >= 24)
        return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeSceneTransitionAnimation(paramActivity, arrayOfView1, arrayOfString1));
      if (Build.VERSION.SDK_INT >= 23)
        return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeSceneTransitionAnimation(paramActivity, arrayOfView1, arrayOfString1));
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(paramActivity, arrayOfView1, arrayOfString1));
      return new ActivityOptionsCompat();
      label144: arrayOfView1 = null;
    }
  }

  public static ActivityOptionsCompat makeTaskLaunchBehind()
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeTaskLaunchBehind());
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeTaskLaunchBehind());
    if (Build.VERSION.SDK_INT >= 21)
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeTaskLaunchBehind());
    return new ActivityOptionsCompat();
  }

  public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    if (Build.VERSION.SDK_INT >= 24)
      return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT >= 23)
      return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT >= 21)
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2));
    if (Build.VERSION.SDK_INT >= 16)
      return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2));
    return new ActivityOptionsCompat();
  }

  @Nullable
  public Rect getLaunchBounds()
  {
    return null;
  }

  public void requestUsageTimeReport(PendingIntent paramPendingIntent)
  {
  }

  public ActivityOptionsCompat setLaunchBounds(@Nullable Rect paramRect)
  {
    return null;
  }

  public Bundle toBundle()
  {
    return null;
  }

  public void update(ActivityOptionsCompat paramActivityOptionsCompat)
  {
  }

  @TargetApi(21)
  @RequiresApi(21)
  private static class ActivityOptionsImpl21 extends ActivityOptionsCompat
  {
    private final ActivityOptionsCompat21 mImpl;

    ActivityOptionsImpl21(ActivityOptionsCompat21 paramActivityOptionsCompat21)
    {
      this.mImpl = paramActivityOptionsCompat21;
    }

    public Bundle toBundle()
    {
      return this.mImpl.toBundle();
    }

    public void update(ActivityOptionsCompat paramActivityOptionsCompat)
    {
      if ((paramActivityOptionsCompat instanceof ActivityOptionsImpl21))
      {
        ActivityOptionsImpl21 localActivityOptionsImpl21 = (ActivityOptionsImpl21)paramActivityOptionsCompat;
        this.mImpl.update(localActivityOptionsImpl21.mImpl);
      }
    }
  }

  @TargetApi(23)
  @RequiresApi(23)
  private static class ActivityOptionsImpl23 extends ActivityOptionsCompat
  {
    private final ActivityOptionsCompat23 mImpl;

    ActivityOptionsImpl23(ActivityOptionsCompat23 paramActivityOptionsCompat23)
    {
      this.mImpl = paramActivityOptionsCompat23;
    }

    public void requestUsageTimeReport(PendingIntent paramPendingIntent)
    {
      this.mImpl.requestUsageTimeReport(paramPendingIntent);
    }

    public Bundle toBundle()
    {
      return this.mImpl.toBundle();
    }

    public void update(ActivityOptionsCompat paramActivityOptionsCompat)
    {
      if ((paramActivityOptionsCompat instanceof ActivityOptionsImpl23))
      {
        ActivityOptionsImpl23 localActivityOptionsImpl23 = (ActivityOptionsImpl23)paramActivityOptionsCompat;
        this.mImpl.update(localActivityOptionsImpl23.mImpl);
      }
    }
  }

  @TargetApi(24)
  @RequiresApi(24)
  private static class ActivityOptionsImpl24 extends ActivityOptionsCompat
  {
    private final ActivityOptionsCompat24 mImpl;

    ActivityOptionsImpl24(ActivityOptionsCompat24 paramActivityOptionsCompat24)
    {
      this.mImpl = paramActivityOptionsCompat24;
    }

    public Rect getLaunchBounds()
    {
      return this.mImpl.getLaunchBounds();
    }

    public void requestUsageTimeReport(PendingIntent paramPendingIntent)
    {
      this.mImpl.requestUsageTimeReport(paramPendingIntent);
    }

    public ActivityOptionsCompat setLaunchBounds(@Nullable Rect paramRect)
    {
      return new ActivityOptionsImpl24(this.mImpl.setLaunchBounds(paramRect));
    }

    public Bundle toBundle()
    {
      return this.mImpl.toBundle();
    }

    public void update(ActivityOptionsCompat paramActivityOptionsCompat)
    {
      if ((paramActivityOptionsCompat instanceof ActivityOptionsImpl24))
      {
        ActivityOptionsImpl24 localActivityOptionsImpl24 = (ActivityOptionsImpl24)paramActivityOptionsCompat;
        this.mImpl.update(localActivityOptionsImpl24.mImpl);
      }
    }
  }

  @TargetApi(16)
  @RequiresApi(16)
  private static class ActivityOptionsImplJB extends ActivityOptionsCompat
  {
    private final ActivityOptionsCompatJB mImpl;

    ActivityOptionsImplJB(ActivityOptionsCompatJB paramActivityOptionsCompatJB)
    {
      this.mImpl = paramActivityOptionsCompatJB;
    }

    public Bundle toBundle()
    {
      return this.mImpl.toBundle();
    }

    public void update(ActivityOptionsCompat paramActivityOptionsCompat)
    {
      if ((paramActivityOptionsCompat instanceof ActivityOptionsImplJB))
      {
        ActivityOptionsImplJB localActivityOptionsImplJB = (ActivityOptionsImplJB)paramActivityOptionsCompat;
        this.mImpl.update(localActivityOptionsImplJB.mImpl);
      }
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.ActivityOptionsCompat
 * JD-Core Version:    0.6.0
 */