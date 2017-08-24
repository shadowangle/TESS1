package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class CancellationSignalCompatJellybean
{
  public static void cancel(Object paramObject)
  {
    ((CancellationSignal)paramObject).cancel();
  }

  public static Object create()
  {
    return new CancellationSignal();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.os.CancellationSignalCompatJellybean
 * JD-Core Version:    0.6.0
 */