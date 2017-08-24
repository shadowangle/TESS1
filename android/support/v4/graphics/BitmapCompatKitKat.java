package android.support.v4.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class BitmapCompatKitKat
{
  static int getAllocationByteCount(Bitmap paramBitmap)
  {
    return paramBitmap.getAllocationByteCount();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.graphics.BitmapCompatKitKat
 * JD-Core Version:    0.6.0
 */