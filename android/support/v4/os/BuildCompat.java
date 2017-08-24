package android.support.v4.os;

import android.os.Build.VERSION;

public class BuildCompat
{
  public static boolean isAtLeastN()
  {
    return Build.VERSION.SDK_INT >= 24;
  }

  public static boolean isAtLeastNMR1()
  {
    return Build.VERSION.SDK_INT >= 25;
  }

  public static boolean isAtLeastO()
  {
    return (!"REL".equals(Build.VERSION.CODENAME)) && (("O".equals(Build.VERSION.CODENAME)) || (Build.VERSION.CODENAME.startsWith("OMR")));
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.os.BuildCompat
 * JD-Core Version:    0.6.0
 */