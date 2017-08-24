package android.support.v4.text;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import java.util.Locale;

@TargetApi(17)
@RequiresApi(17)
class TextUtilsCompatJellybeanMr1
{
  public static int getLayoutDirectionFromLocale(@Nullable Locale paramLocale)
  {
    return TextUtils.getLayoutDirectionFromLocale(paramLocale);
  }

  @NonNull
  public static String htmlEncode(@NonNull String paramString)
  {
    return TextUtils.htmlEncode(paramString);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.text.TextUtilsCompatJellybeanMr1
 * JD-Core Version:    0.6.0
 */