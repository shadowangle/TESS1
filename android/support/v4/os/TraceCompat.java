package android.support.v4.os;

import android.os.Build.VERSION;

public final class TraceCompat
{
  public static void beginSection(String paramString)
  {
    if (Build.VERSION.SDK_INT >= 18)
      TraceJellybeanMR2.beginSection(paramString);
  }

  public static void endSection()
  {
    if (Build.VERSION.SDK_INT >= 18)
      TraceJellybeanMR2.endSection();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.os.TraceCompat
 * JD-Core Version:    0.6.0
 */