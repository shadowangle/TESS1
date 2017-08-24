package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

@TargetApi(14)
@RequiresApi(14)
class MotionEventCompatICS
{
  public static int getButtonState(MotionEvent paramMotionEvent)
  {
    return paramMotionEvent.getButtonState();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.MotionEventCompatICS
 * JD-Core Version:    0.6.0
 */