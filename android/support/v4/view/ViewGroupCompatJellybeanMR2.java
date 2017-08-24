package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(18)
@RequiresApi(18)
class ViewGroupCompatJellybeanMR2
{
  public static int getLayoutMode(ViewGroup paramViewGroup)
  {
    return paramViewGroup.getLayoutMode();
  }

  public static void setLayoutMode(ViewGroup paramViewGroup, int paramInt)
  {
    paramViewGroup.setLayoutMode(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewGroupCompatJellybeanMR2
 * JD-Core Version:    0.6.0
 */