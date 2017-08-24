package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(21)
@RequiresApi(21)
class ViewGroupCompatLollipop
{
  public static int getNestedScrollAxes(ViewGroup paramViewGroup)
  {
    return paramViewGroup.getNestedScrollAxes();
  }

  public static boolean isTransitionGroup(ViewGroup paramViewGroup)
  {
    return paramViewGroup.isTransitionGroup();
  }

  public static void setTransitionGroup(ViewGroup paramViewGroup, boolean paramBoolean)
  {
    paramViewGroup.setTransitionGroup(paramBoolean);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewGroupCompatLollipop
 * JD-Core Version:    0.6.0
 */