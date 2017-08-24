package android.support.v4.text;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@TargetApi(14)
@RequiresApi(14)
class ICUCompatIcs
{
  private static final String TAG = "ICUCompatIcs";
  private static Method sAddLikelySubtagsMethod;
  private static Method sGetScriptMethod;

  static
  {
    try
    {
      Class localClass = Class.forName("libcore.icu.ICU");
      if (localClass != null)
      {
        sGetScriptMethod = localClass.getMethod("getScript", new Class[] { String.class });
        sAddLikelySubtagsMethod = localClass.getMethod("addLikelySubtags", new Class[] { String.class });
      }
      return;
    }
    catch (Exception localException)
    {
      sGetScriptMethod = null;
      sAddLikelySubtagsMethod = null;
      Log.w("ICUCompatIcs", localException);
    }
  }

  private static String addLikelySubtags(Locale paramLocale)
  {
    String str1 = paramLocale.toString();
    try
    {
      if (sAddLikelySubtagsMethod != null)
      {
        String str2 = (String)sAddLikelySubtagsMethod.invoke(null, new Object[] { str1 });
        return str2;
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Log.w("ICUCompatIcs", localIllegalAccessException);
      return str1;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      while (true)
        Log.w("ICUCompatIcs", localInvocationTargetException);
    }
  }

  private static String getScript(String paramString)
  {
    try
    {
      if (sGetScriptMethod != null)
      {
        String str = (String)sGetScriptMethod.invoke(null, new Object[] { paramString });
        return str;
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Log.w("ICUCompatIcs", localIllegalAccessException);
      return null;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      while (true)
        Log.w("ICUCompatIcs", localInvocationTargetException);
    }
  }

  public static String maximizeAndGetScript(Locale paramLocale)
  {
    String str = addLikelySubtags(paramLocale);
    if (str != null)
      return getScript(str);
    return null;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.text.ICUCompatIcs
 * JD-Core Version:    0.6.0
 */