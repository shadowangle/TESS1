package android.support.v4.media;

import android.os.SystemClock;
import android.view.KeyEvent;

public abstract class TransportPerformer
{
  static final int AUDIOFOCUS_GAIN = 1;
  static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
  static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
  static final int AUDIOFOCUS_LOSS = -1;
  static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
  static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;

  public void onAudioFocusChange(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    default:
    case -1:
    }
    while (true)
    {
      if (i != 0)
      {
        long l = SystemClock.uptimeMillis();
        onMediaButtonDown(i, new KeyEvent(l, l, 0, i, 0));
        onMediaButtonUp(i, new KeyEvent(l, l, 1, i, 0));
      }
      return;
      i = 127;
    }
  }

  public int onGetBufferPercentage()
  {
    return 100;
  }

  public abstract long onGetCurrentPosition();

  public abstract long onGetDuration();

  public int onGetTransportControlFlags()
  {
    return 60;
  }

  public abstract boolean onIsPlaying();

  public boolean onMediaButtonDown(int paramInt, KeyEvent paramKeyEvent)
  {
    switch (paramInt)
    {
    default:
    case 126:
    case 127:
    case 86:
    case 79:
    case 85:
    }
    while (true)
    {
      return true;
      onStart();
      continue;
      onPause();
      continue;
      onStop();
      continue;
      if (onIsPlaying())
      {
        onPause();
        continue;
      }
      onStart();
    }
  }

  public boolean onMediaButtonUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return true;
  }

  public abstract void onPause();

  public abstract void onSeekTo(long paramLong);

  public abstract void onStart();

  public abstract void onStop();
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.TransportPerformer
 * JD-Core Version:    0.6.0
 */