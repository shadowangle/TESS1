package android.support.v4.animation;

import android.support.annotation.RestrictTo;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public abstract interface AnimatorListenerCompat
{
  public abstract void onAnimationCancel(ValueAnimatorCompat paramValueAnimatorCompat);

  public abstract void onAnimationEnd(ValueAnimatorCompat paramValueAnimatorCompat);

  public abstract void onAnimationRepeat(ValueAnimatorCompat paramValueAnimatorCompat);

  public abstract void onAnimationStart(ValueAnimatorCompat paramValueAnimatorCompat);
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.animation.AnimatorListenerCompat
 * JD-Core Version:    0.6.0
 */