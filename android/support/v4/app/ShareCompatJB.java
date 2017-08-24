package android.support.v4.app;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.text.Html;

@TargetApi(16)
@RequiresApi(16)
class ShareCompatJB
{
  public static String escapeHtml(CharSequence paramCharSequence)
  {
    return Html.escapeHtml(paramCharSequence);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.ShareCompatJB
 * JD-Core Version:    0.6.0
 */