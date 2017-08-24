package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

@TargetApi(12)
@RequiresApi(12)
class MotionEventCompatHoneycombMr1
{
  static float getAxisValue(MotionEvent paramMotionEvent, int paramInt)
  {
    return paramMotionEvent.getAxisValue(paramInt);
  }

  static float getAxisValue(MotionEvent paramMotionEvent, int paramInt1, int paramInt2)
  {
    return paramMotionEvent.getAxisValue(paramInt1, paramInt2);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.MotionEventCompatHoneycombMr1
 * JD-Core Version:    0.6.0
 */