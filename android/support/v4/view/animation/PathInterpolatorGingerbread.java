package android.support.v4.view.animation;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;

@TargetApi(9)
@RequiresApi(9)
class PathInterpolatorGingerbread
  implements Interpolator
{
  private static final float PRECISION = 0.002F;
  private final float[] mX;
  private final float[] mY;

  public PathInterpolatorGingerbread(float paramFloat1, float paramFloat2)
  {
    this(createQuad(paramFloat1, paramFloat2));
  }

  public PathInterpolatorGingerbread(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this(createCubic(paramFloat1, paramFloat2, paramFloat3, paramFloat4));
  }

  public PathInterpolatorGingerbread(Path paramPath)
  {
    PathMeasure localPathMeasure = new PathMeasure(paramPath, false);
    float f = localPathMeasure.getLength();
    int i = 1 + (int)(f / 0.002F);
    this.mX = new float[i];
    this.mY = new float[i];
    float[] arrayOfFloat = new float[2];
    for (int j = 0; j < i; j++)
    {
      localPathMeasure.getPosTan(f * j / (i - 1), arrayOfFloat, null);
      this.mX[j] = arrayOfFloat[0];
      this.mY[j] = arrayOfFloat[1];
    }
  }

  private static Path createCubic(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    Path localPath = new Path();
    localPath.moveTo(0.0F, 0.0F);
    localPath.cubicTo(paramFloat1, paramFloat2, paramFloat3, paramFloat4, 1.0F, 1.0F);
    return localPath;
  }

  private static Path createQuad(float paramFloat1, float paramFloat2)
  {
    Path localPath = new Path();
    localPath.moveTo(0.0F, 0.0F);
    localPath.quadTo(paramFloat1, paramFloat2, 1.0F, 1.0F);
    return localPath;
  }

  public float getInterpolation(float paramFloat)
  {
    if (paramFloat <= 0.0F)
      return 0.0F;
    if (paramFloat >= 1.0F)
      return 1.0F;
    int i = -1 + this.mX.length;
    int j = 0;
    while (i - j > 1)
    {
      int k = (j + i) / 2;
      if (paramFloat < this.mX[k])
      {
        i = k;
        continue;
      }
      j = k;
    }
    float f1 = this.mX[i] - this.mX[j];
    if (f1 == 0.0F)
      return this.mY[j];
    float f2 = (paramFloat - this.mX[j]) / f1;
    float f3 = this.mY[j];
    return f3 + f2 * (this.mY[i] - f3);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.animation.PathInterpolatorGingerbread
 * JD-Core Version:    0.6.0
 */