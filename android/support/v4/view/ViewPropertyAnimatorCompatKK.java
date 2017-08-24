package android.support.v4.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewPropertyAnimator;

@TargetApi(19)
@RequiresApi(19)
class ViewPropertyAnimatorCompatKK
{
  public static void setUpdateListener(View paramView, ViewPropertyAnimatorUpdateListener paramViewPropertyAnimatorUpdateListener)
  {
    1 local1 = null;
    if (paramViewPropertyAnimatorUpdateListener != null)
      local1 = new ValueAnimator.AnimatorUpdateListener(paramViewPropertyAnimatorUpdateListener, paramView)
      {
        public void onAnimationUpdate(ValueAnimator paramValueAnimator)
        {
          this.val$listener.onAnimationUpdate(this.val$view);
        }
      };
    paramView.animate().setUpdateListener(local1);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewPropertyAnimatorCompatKK
 * JD-Core Version:    0.6.0
 */