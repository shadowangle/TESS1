package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.OnPlaybackPositionUpdateListener;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;

@TargetApi(18)
@RequiresApi(18)
class MediaSessionCompatApi18
{
  private static final long ACTION_SEEK_TO = 256L;
  private static final String TAG = "MediaSessionCompatApi18";
  private static boolean sIsMbrPendingIntentSupported = true;

  public static Object createPlaybackPositionUpdateListener(Callback paramCallback)
  {
    return new OnPlaybackPositionUpdateListener(paramCallback);
  }

  static int getRccTransportControlFlagsFromActions(long paramLong)
  {
    int i = MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(paramLong);
    if ((0x100 & paramLong) != 0L)
      i |= 256;
    return i;
  }

  public static void registerMediaButtonEventReceiver(Context paramContext, PendingIntent paramPendingIntent, ComponentName paramComponentName)
  {
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    if (sIsMbrPendingIntentSupported);
    try
    {
      localAudioManager.registerMediaButtonEventReceiver(paramPendingIntent);
      if (!sIsMbrPendingIntentSupported)
        localAudioManager.registerMediaButtonEventReceiver(paramComponentName);
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      while (true)
      {
        Log.w("MediaSessionCompatApi18", "Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
        sIsMbrPendingIntentSupported = false;
      }
    }
  }

  public static void setOnPlaybackPositionUpdateListener(Object paramObject1, Object paramObject2)
  {
    ((RemoteControlClient)paramObject1).setPlaybackPositionUpdateListener((RemoteControlClient.OnPlaybackPositionUpdateListener)paramObject2);
  }

  public static void setState(Object paramObject, int paramInt, long paramLong1, float paramFloat, long paramLong2)
  {
    long l1 = 0L;
    long l2 = SystemClock.elapsedRealtime();
    if ((paramInt == 3) && (paramLong1 > l1))
    {
      if (paramLong2 > l1)
      {
        l1 = l2 - paramLong2;
        if ((paramFloat > 0.0F) && (paramFloat != 1.0F))
          l1 = ()(paramFloat * (float)l1);
      }
      paramLong1 += l1;
    }
    int i = MediaSessionCompatApi14.getRccStateFromState(paramInt);
    ((RemoteControlClient)paramObject).setPlaybackState(i, paramLong1, paramFloat);
  }

  public static void setTransportControlFlags(Object paramObject, long paramLong)
  {
    ((RemoteControlClient)paramObject).setTransportControlFlags(getRccTransportControlFlagsFromActions(paramLong));
  }

  public static void unregisterMediaButtonEventReceiver(Context paramContext, PendingIntent paramPendingIntent, ComponentName paramComponentName)
  {
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    if (sIsMbrPendingIntentSupported)
    {
      localAudioManager.unregisterMediaButtonEventReceiver(paramPendingIntent);
      return;
    }
    localAudioManager.unregisterMediaButtonEventReceiver(paramComponentName);
  }

  static abstract interface Callback
  {
    public abstract void onSeekTo(long paramLong);
  }

  static class OnPlaybackPositionUpdateListener<T extends MediaSessionCompatApi18.Callback>
    implements RemoteControlClient.OnPlaybackPositionUpdateListener
  {
    protected final T mCallback;

    public OnPlaybackPositionUpdateListener(T paramT)
    {
      this.mCallback = paramT;
    }

    public void onPlaybackPositionUpdate(long paramLong)
    {
      this.mCallback.onSeekTo(paramLong);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.session.MediaSessionCompatApi18
 * JD-Core Version:    0.6.0
 */