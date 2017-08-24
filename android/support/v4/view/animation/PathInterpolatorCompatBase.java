package android.support.v4.view.animation;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;

@TargetApi(9)
@RequiresApi(9)
class PathInterpolatorCompatBase
{
  public static Interpolator create(float paramFloat1, float paramFloat2)
  {
    return new PathInterpolatorGingerbread(paramFloat1, paramFloat2);
  }

  public static Interpolator create(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return new PathInterpolatorGingerbread(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }

  public static Interpolator create(Path paramPath)
  {
    return new PathInterpolatorGingerbread(paramPath);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.animation.PathInterpolatorCompatBase
 * JD-Core Version:    0.6.0
 */