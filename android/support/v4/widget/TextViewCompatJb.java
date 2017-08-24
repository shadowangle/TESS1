package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

@TargetApi(16)
@RequiresApi(16)
class TextViewCompatJb
{
  static int getMaxLines(TextView paramTextView)
  {
    return paramTextView.getMaxLines();
  }

  static int getMinLines(TextView paramTextView)
  {
    return paramTextView.getMinLines();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.TextViewCompatJb
 * JD-Core Version:    0.6.0
 */