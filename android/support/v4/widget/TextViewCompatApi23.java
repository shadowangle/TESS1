package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.widget.TextView;

@TargetApi(23)
@RequiresApi(23)
class TextViewCompatApi23
{
  public static void setTextAppearance(@NonNull TextView paramTextView, @StyleRes int paramInt)
  {
    paramTextView.setTextAppearance(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.TextViewCompatApi23
 * JD-Core Version:    0.6.0
 */