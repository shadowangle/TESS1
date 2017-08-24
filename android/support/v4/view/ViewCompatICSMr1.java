package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(15)
@RequiresApi(15)
class ViewCompatICSMr1
{
  public static boolean hasOnClickListeners(View paramView)
  {
    return paramView.hasOnClickListeners();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewCompatICSMr1
 * JD-Core Version:    0.6.0
 */