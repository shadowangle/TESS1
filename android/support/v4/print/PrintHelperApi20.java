package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;

@TargetApi(20)
@RequiresApi(20)
class PrintHelperApi20 extends PrintHelperKitkat
{
  PrintHelperApi20(Context paramContext)
  {
    super(paramContext);
    this.mPrintActivityRespectsOrientation = false;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.print.PrintHelperApi20
 * JD-Core Version:    0.6.0
 */