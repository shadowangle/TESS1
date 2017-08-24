package android.support.v4.media;

import android.annotation.TargetApi;
import android.media.VolumeProvider;
import android.support.annotation.RequiresApi;

@TargetApi(21)
@RequiresApi(21)
class VolumeProviderCompatApi21
{
  public static Object createVolumeProvider(int paramInt1, int paramInt2, int paramInt3, Delegate paramDelegate)
  {
    return new VolumeProvider(paramInt1, paramInt2, paramInt3, paramDelegate)
    {
      public void onAdjustVolume(int paramInt)
      {
        this.val$delegate.onAdjustVolume(paramInt);
      }

      public void onSetVolumeTo(int paramInt)
      {
        this.val$delegate.onSetVolumeTo(paramInt);
      }
    };
  }

  public static void setCurrentVolume(Object paramObject, int paramInt)
  {
    ((VolumeProvider)paramObject).setCurrentVolume(paramInt);
  }

  public static abstract interface Delegate
  {
    public abstract void onAdjustVolume(int paramInt);

    public abstract void onSetVolumeTo(int paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.VolumeProviderCompatApi21
 * JD-Core Version:    0.6.0
 */