package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;

@TargetApi(24)
@RequiresApi(24)
class PrintHelperApi24 extends PrintHelperApi23
{
  PrintHelperApi24(Context paramContext)
  {
    super(paramContext);
    this.mIsMinMarginsHandlingCorrect = true;
    this.mPrintActivityRespectsOrientation = true;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.print.PrintHelperApi24
 * JD-Core Version:    0.6.0
 */