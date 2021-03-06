package android.support.v4.view.animation;

import android.graphics.Path;
import android.os.Build.VERSION;
import android.view.animation.Interpolator;

public final class PathInterpolatorCompat
{
  public static Interpolator create(float paramFloat1, float paramFloat2)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return PathInterpolatorCompatApi21.create(paramFloat1, paramFloat2);
    return PathInterpolatorCompatBase.create(paramFloat1, paramFloat2);
  }

  public static Interpolator create(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return PathInterpolatorCompatApi21.create(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    return PathInterpolatorCompatBase.create(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }

  public static Interpolator create(Path paramPath)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return PathInterpolatorCompatApi21.create(paramPath);
    return PathInterpolatorCompatBase.create(paramPath);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.animation.PathInterpolatorCompat
 * JD-Core Version:    0.6.0
 */