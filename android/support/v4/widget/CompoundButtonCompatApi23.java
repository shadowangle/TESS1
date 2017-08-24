package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.widget.CompoundButton;

@TargetApi(23)
@RequiresApi(23)
class CompoundButtonCompatApi23
{
  static Drawable getButtonDrawable(CompoundButton paramCompoundButton)
  {
    return paramCompoundButton.getButtonDrawable();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.CompoundButtonCompatApi23
 * JD-Core Version:    0.6.0
 */