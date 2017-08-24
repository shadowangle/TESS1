package android.support.v4.animation;

import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.view.View;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public final class AnimatorCompatHelper
{
  private static final AnimatorProvider IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 12)
    {
      IMPL = new HoneycombMr1AnimatorCompatProvider();
      return;
    }
    IMPL = new GingerbreadAnimatorCompatProvider();
  }

  public static void clearInterpolator(View paramView)
  {
    IMPL.clearInterpolator(paramView);
  }

  public static ValueAnimatorCompat emptyValueAnimator()
  {
    return IMPL.emptyValueAnimator();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.animation.AnimatorCompatHelper
 * JD-Core Version:    0.6.0
 */