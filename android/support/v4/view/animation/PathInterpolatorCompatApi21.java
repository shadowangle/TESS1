package android.support.v4.view.animation;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

@TargetApi(21)
@RequiresApi(21)
class PathInterpolatorCompatApi21
{
  public static Interpolator create(float paramFloat1, float paramFloat2)
  {
    return new PathInterpolator(paramFloat1, paramFloat2);
  }

  public static Interpolator create(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return new PathInterpolator(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }

  public static Interpolator create(Path paramPath)
  {
    return new PathInterpolator(paramPath);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.animation.PathInterpolatorCompatApi21
 * JD-Core Version:    0.6.0
 */