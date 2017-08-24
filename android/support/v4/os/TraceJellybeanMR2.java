package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.Trace;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class TraceJellybeanMR2
{
  public static void beginSection(String paramString)
  {
    Trace.beginSection(paramString);
  }

  public static void endSection()
  {
    Trace.endSection();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.os.TraceJellybeanMR2
 * JD-Core Version:    0.6.0
 */