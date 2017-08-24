package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.OverScroller;

@TargetApi(14)
@RequiresApi(14)
class ScrollerCompatIcs
{
  public static float getCurrVelocity(Object paramObject)
  {
    return ((OverScroller)paramObject).getCurrVelocity();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.ScrollerCompatIcs
 * JD-Core Version:    0.6.0
 */