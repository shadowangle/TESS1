package android.support.v4.graphics;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;

@TargetApi(18)
@RequiresApi(18)
class BitmapCompatJellybeanMR2
{
  public static boolean hasMipMap(Bitmap paramBitmap)
  {
    return paramBitmap.hasMipMap();
  }

  public static void setHasMipMap(Bitmap paramBitmap, boolean paramBoolean)
  {
    paramBitmap.setHasMipMap(paramBoolean);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.graphics.BitmapCompatJellybeanMR2
 * JD-Core Version:    0.6.0
 */