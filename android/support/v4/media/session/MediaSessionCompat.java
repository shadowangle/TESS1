package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaMetadataCompat.Builder;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v4.media.VolumeProviderCompat.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat
{
  static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
  static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
  static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
  static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
  static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
  static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
  static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
  static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
  static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
  static final String EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
  public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
  public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
  private static final int MAX_BITMAP_SIZE_IN_DP = 320;
  static final String TAG = "MediaSessionCompat";
  static int sMaxBitmapSize;
  private final ArrayList<OnActiveChangeListener> mActiveListeners = new ArrayList();
  private final MediaControllerCompat mController;
  private final MediaSessionImpl mImpl;

  private MediaSessionCompat(Context paramContext, MediaSessionImpl paramMediaSessionImpl)
  {
    this.mImpl = paramMediaSessionImpl;
    if (Build.VERSION.SDK_INT >= 21)
      setCallback(new Callback()
      {
      });
    this.mController = new MediaControllerCompat(paramContext, this);
  }

  public MediaSessionCompat(Context paramContext, String paramString)
  {
    this(paramContext, paramString, null, null);
  }

  public MediaSessionCompat(Context paramContext, String paramString, ComponentName paramComponentName, PendingIntent paramPendingIntent)
  {
    if (paramContext == null)
      throw new IllegalArgumentException("context must not be null");
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("tag must not be null or empty");
    if (paramComponentName == null)
    {
      paramComponentName = MediaButtonReceiver.getMediaButtonReceiverComponent(paramContext);
      if (paramComponentName == null)
        Log.w("MediaSessionCompat", "Couldn't find a unique registered media button receiver in the given context.");
    }
    if ((paramComponentName != null) && (paramPendingIntent == null))
    {
      Intent localIntent = new Intent("android.intent.action.MEDIA_BUTTON");
      localIntent.setComponent(paramComponentName);
      paramPendingIntent = PendingIntent.getBroadcast(paramContext, 0, localIntent, 0);
    }
    if (Build.VERSION.SDK_INT >= 21)
    {
      this.mImpl = new MediaSessionImplApi21(paramContext, paramString);
      this.mImpl.setMediaButtonReceiver(paramPendingIntent);
    }
    while (true)
    {
      this.mController = new MediaControllerCompat(paramContext, this);
      if (sMaxBitmapSize == 0)
        sMaxBitmapSize = (int)TypedValue.applyDimension(1, 320.0F, paramContext.getResources().getDisplayMetrics());
      return;
      this.mImpl = new MediaSessionImplBase(paramContext, paramString, paramComponentName, paramPendingIntent);
    }
  }

  public static MediaSessionCompat fromMediaSession(Context paramContext, Object paramObject)
  {
    if ((paramContext == null) || (paramObject == null) || (Build.VERSION.SDK_INT < 21))
      return null;
    return new MediaSessionCompat(paramContext, new MediaSessionImplApi21(paramObject));
  }

  @Deprecated
  public static MediaSessionCompat obtain(Context paramContext, Object paramObject)
  {
    return fromMediaSession(paramContext, paramObject);
  }

  public void addOnActiveChangeListener(OnActiveChangeListener paramOnActiveChangeListener)
  {
    if (paramOnActiveChangeListener == null)
      throw new IllegalArgumentException("Listener may not be null");
    this.mActiveListeners.add(paramOnActiveChangeListener);
  }

  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public String getCallingPackage()
  {
    return this.mImpl.getCallingPackage();
  }

  public MediaControllerCompat getController()
  {
    return this.mController;
  }

  public Object getMediaSession()
  {
    return this.mImpl.getMediaSession();
  }

  public Object getRemoteControlClient()
  {
    return this.mImpl.getRemoteControlClient();
  }

  public Token getSessionToken()
  {
    return this.mImpl.getSessionToken();
  }

  public boolean isActive()
  {
    return this.mImpl.isActive();
  }

  public void release()
  {
    this.mImpl.release();
  }

  public void removeOnActiveChangeListener(OnActiveChangeListener paramOnActiveChangeListener)
  {
    if (paramOnActiveChangeListener == null)
      throw new IllegalArgumentException("Listener may not be null");
    this.mActiveListeners.remove(paramOnActiveChangeListener);
  }

  public void sendSessionEvent(String paramString, Bundle paramBundle)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("event cannot be null or empty");
    this.mImpl.sendSessionEvent(paramString, paramBundle);
  }

  public void setActive(boolean paramBoolean)
  {
    this.mImpl.setActive(paramBoolean);
    Iterator localIterator = this.mActiveListeners.iterator();
    while (localIterator.hasNext())
      ((OnActiveChangeListener)localIterator.next()).onActiveChanged();
  }

  public void setCallback(Callback paramCallback)
  {
    setCallback(paramCallback, null);
  }

  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    MediaSessionImpl localMediaSessionImpl = this.mImpl;
    if (paramHandler != null);
    while (true)
    {
      localMediaSessionImpl.setCallback(paramCallback, paramHandler);
      return;
      paramHandler = new Handler();
    }
  }

  public void setExtras(Bundle paramBundle)
  {
    this.mImpl.setExtras(paramBundle);
  }

  public void setFlags(int paramInt)
  {
    this.mImpl.setFlags(paramInt);
  }

  public void setMediaButtonReceiver(PendingIntent paramPendingIntent)
  {
    this.mImpl.setMediaButtonReceiver(paramPendingIntent);
  }

  public void setMetadata(MediaMetadataCompat paramMediaMetadataCompat)
  {
    this.mImpl.setMetadata(paramMediaMetadataCompat);
  }

  public void setPlaybackState(PlaybackStateCompat paramPlaybackStateCompat)
  {
    this.mImpl.setPlaybackState(paramPlaybackStateCompat);
  }

  public void setPlaybackToLocal(int paramInt)
  {
    this.mImpl.setPlaybackToLocal(paramInt);
  }

  public void setPlaybackToRemote(VolumeProviderCompat paramVolumeProviderCompat)
  {
    if (paramVolumeProviderCompat == null)
      throw new IllegalArgumentException("volumeProvider may not be null!");
    this.mImpl.setPlaybackToRemote(paramVolumeProviderCompat);
  }

  public void setQueue(List<QueueItem> paramList)
  {
    this.mImpl.setQueue(paramList);
  }

  public void setQueueTitle(CharSequence paramCharSequence)
  {
    this.mImpl.setQueueTitle(paramCharSequence);
  }

  public void setRatingType(int paramInt)
  {
    this.mImpl.setRatingType(paramInt);
  }

  public void setSessionActivity(PendingIntent paramPendingIntent)
  {
    this.mImpl.setSessionActivity(paramPendingIntent);
  }

  public static abstract class Callback
  {
    final Object mCallbackObj;
    WeakReference<MediaSessionCompat.MediaSessionImpl> mSessionImpl;

    public Callback()
    {
      if (Build.VERSION.SDK_INT >= 24)
      {
        this.mCallbackObj = MediaSessionCompatApi24.createCallback(new StubApi24());
        return;
      }
      if (Build.VERSION.SDK_INT >= 23)
      {
        this.mCallbackObj = MediaSessionCompatApi23.createCallback(new StubApi23());
        return;
      }
      if (Build.VERSION.SDK_INT >= 21)
      {
        this.mCallbackObj = MediaSessionCompatApi21.createCallback(new StubApi21());
        return;
      }
      this.mCallbackObj = null;
    }

    public void onCommand(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver)
    {
    }

    public void onCustomAction(String paramString, Bundle paramBundle)
    {
    }

    public void onFastForward()
    {
    }

    public boolean onMediaButtonEvent(Intent paramIntent)
    {
      return false;
    }

    public void onPause()
    {
    }

    public void onPlay()
    {
    }

    public void onPlayFromMediaId(String paramString, Bundle paramBundle)
    {
    }

    public void onPlayFromSearch(String paramString, Bundle paramBundle)
    {
    }

    public void onPlayFromUri(Uri paramUri, Bundle paramBundle)
    {
    }

    public void onPrepare()
    {
    }

    public void onPrepareFromMediaId(String paramString, Bundle paramBundle)
    {
    }

    public void onPrepareFromSearch(String paramString, Bundle paramBundle)
    {
    }

    public void onPrepareFromUri(Uri paramUri, Bundle paramBundle)
    {
    }

    public void onRewind()
    {
    }

    public void onSeekTo(long paramLong)
    {
    }

    public void onSetRating(RatingCompat paramRatingCompat)
    {
    }

    public void onSkipToNext()
    {
    }

    public void onSkipToPrevious()
    {
    }

    public void onSkipToQueueItem(long paramLong)
    {
    }

    public void onStop()
    {
    }

    private class StubApi21
      implements MediaSessionCompatApi21.Callback
    {
      StubApi21()
      {
      }

      public void onCommand(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver)
      {
        if (paramString.equals("android.support.v4.media.session.command.GET_EXTRA_BINDER"))
        {
          MediaSessionCompat.MediaSessionImplApi21 localMediaSessionImplApi21 = (MediaSessionCompat.MediaSessionImplApi21)MediaSessionCompat.Callback.this.mSessionImpl.get();
          if (localMediaSessionImplApi21 != null)
          {
            Bundle localBundle = new Bundle();
            BundleCompat.putBinder(localBundle, "android.support.v4.media.session.EXTRA_BINDER", localMediaSessionImplApi21.getExtraSessionBinder());
            paramResultReceiver.send(0, localBundle);
          }
          return;
        }
        MediaSessionCompat.Callback.this.onCommand(paramString, paramBundle, paramResultReceiver);
      }

      public void onCustomAction(String paramString, Bundle paramBundle)
      {
        if (paramString.equals("android.support.v4.media.session.action.PLAY_FROM_URI"))
        {
          Uri localUri2 = (Uri)paramBundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
          Bundle localBundle4 = (Bundle)paramBundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPlayFromUri(localUri2, localBundle4);
          return;
        }
        if (paramString.equals("android.support.v4.media.session.action.PREPARE"))
        {
          MediaSessionCompat.Callback.this.onPrepare();
          return;
        }
        if (paramString.equals("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID"))
        {
          String str2 = paramBundle.getString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID");
          Bundle localBundle3 = paramBundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPrepareFromMediaId(str2, localBundle3);
          return;
        }
        if (paramString.equals("android.support.v4.media.session.action.PREPARE_FROM_SEARCH"))
        {
          String str1 = paramBundle.getString("android.support.v4.media.session.action.ARGUMENT_QUERY");
          Bundle localBundle2 = paramBundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPrepareFromSearch(str1, localBundle2);
          return;
        }
        if (paramString.equals("android.support.v4.media.session.action.PREPARE_FROM_URI"))
        {
          Uri localUri1 = (Uri)paramBundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
          Bundle localBundle1 = paramBundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
          MediaSessionCompat.Callback.this.onPrepareFromUri(localUri1, localBundle1);
          return;
        }
        MediaSessionCompat.Callback.this.onCustomAction(paramString, paramBundle);
      }

      public void onFastForward()
      {
        MediaSessionCompat.Callback.this.onFastForward();
      }

      public boolean onMediaButtonEvent(Intent paramIntent)
      {
        return MediaSessionCompat.Callback.this.onMediaButtonEvent(paramIntent);
      }

      public void onPause()
      {
        MediaSessionCompat.Callback.this.onPause();
      }

      public void onPlay()
      {
        MediaSessionCompat.Callback.this.onPlay();
      }

      public void onPlayFromMediaId(String paramString, Bundle paramBundle)
      {
        MediaSessionCompat.Callback.this.onPlayFromMediaId(paramString, paramBundle);
      }

      public void onPlayFromSearch(String paramString, Bundle paramBundle)
      {
        MediaSessionCompat.Callback.this.onPlayFromSearch(paramString, paramBundle);
      }

      public void onRewind()
      {
        MediaSessionCompat.Callback.this.onRewind();
      }

      public void onSeekTo(long paramLong)
      {
        MediaSessionCompat.Callback.this.onSeekTo(paramLong);
      }

      public void onSetRating(Object paramObject)
      {
        MediaSessionCompat.Callback.this.onSetRating(RatingCompat.fromRating(paramObject));
      }

      public void onSkipToNext()
      {
        MediaSessionCompat.Callback.this.onSkipToNext();
      }

      public void onSkipToPrevious()
      {
        MediaSessionCompat.Callback.this.onSkipToPrevious();
      }

      public void onSkipToQueueItem(long paramLong)
      {
        MediaSessionCompat.Callback.this.onSkipToQueueItem(paramLong);
      }

      public void onStop()
      {
        MediaSessionCompat.Callback.this.onStop();
      }
    }

    private class StubApi23 extends MediaSessionCompat.Callback.StubApi21
      implements MediaSessionCompatApi23.Callback
    {
      StubApi23()
      {
        super();
      }

      public void onPlayFromUri(Uri paramUri, Bundle paramBundle)
      {
        MediaSessionCompat.Callback.this.onPlayFromUri(paramUri, paramBundle);
      }
    }

    private class StubApi24 extends MediaSessionCompat.Callback.StubApi23
      implements MediaSessionCompatApi24.Callback
    {
      StubApi24()
      {
        super();
      }

      public void onPrepare()
      {
        MediaSessionCompat.Callback.this.onPrepare();
      }

      public void onPrepareFromMediaId(String paramString, Bundle paramBundle)
      {
        MediaSessionCompat.Callback.this.onPrepareFromMediaId(paramString, paramBundle);
      }

      public void onPrepareFromSearch(String paramString, Bundle paramBundle)
      {
        MediaSessionCompat.Callback.this.onPrepareFromSearch(paramString, paramBundle);
      }

      public void onPrepareFromUri(Uri paramUri, Bundle paramBundle)
      {
        MediaSessionCompat.Callback.this.onPrepareFromUri(paramUri, paramBundle);
      }
    }
  }

  static abstract interface MediaSessionImpl
  {
    public abstract String getCallingPackage();

    public abstract Object getMediaSession();

    public abstract Object getRemoteControlClient();

    public abstract MediaSessionCompat.Token getSessionToken();

    public abstract boolean isActive();

    public abstract void release();

    public abstract void sendSessionEvent(String paramString, Bundle paramBundle);

    public abstract void setActive(boolean paramBoolean);

    public abstract void setCallback(MediaSessionCompat.Callback paramCallback, Handler paramHandler);

    public abstract void setExtras(Bundle paramBundle);

    public abstract void setFlags(int paramInt);

    public abstract void setMediaButtonReceiver(PendingIntent paramPendingIntent);

    public abstract void setMetadata(MediaMetadataCompat paramMediaMetadataCompat);

    public abstract void setPlaybackState(PlaybackStateCompat paramPlaybackStateCompat);

    public abstract void setPlaybackToLocal(int paramInt);

    public abstract void setPlaybackToRemote(VolumeProviderCompat paramVolumeProviderCompat);

    public abstract void setQueue(List<MediaSessionCompat.QueueItem> paramList);

    public abstract void setQueueTitle(CharSequence paramCharSequence);

    public abstract void setRatingType(int paramInt);

    public abstract void setSessionActivity(PendingIntent paramPendingIntent);
  }

  static class MediaSessionImplApi21
    implements MediaSessionCompat.MediaSessionImpl
  {
    private boolean mDestroyed = false;
    private final RemoteCallbackList<IMediaControllerCallback> mExtraControllerCallbacks = new RemoteCallbackList();
    private ExtraSession mExtraSessionBinder;
    private PlaybackStateCompat mPlaybackState;
    int mRatingType;
    private final Object mSessionObj;
    private final MediaSessionCompat.Token mToken;

    public MediaSessionImplApi21(Context paramContext, String paramString)
    {
      this.mSessionObj = MediaSessionCompatApi21.createSession(paramContext, paramString);
      this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
    }

    public MediaSessionImplApi21(Object paramObject)
    {
      this.mSessionObj = MediaSessionCompatApi21.verifySession(paramObject);
      this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
    }

    public String getCallingPackage()
    {
      if (Build.VERSION.SDK_INT < 24)
        return null;
      return MediaSessionCompatApi24.getCallingPackage(this.mSessionObj);
    }

    ExtraSession getExtraSessionBinder()
    {
      if (this.mExtraSessionBinder == null)
        this.mExtraSessionBinder = new ExtraSession();
      return this.mExtraSessionBinder;
    }

    public Object getMediaSession()
    {
      return this.mSessionObj;
    }

    public Object getRemoteControlClient()
    {
      return null;
    }

    public MediaSessionCompat.Token getSessionToken()
    {
      return this.mToken;
    }

    public boolean isActive()
    {
      return MediaSessionCompatApi21.isActive(this.mSessionObj);
    }

    public void release()
    {
      this.mDestroyed = true;
      MediaSessionCompatApi21.release(this.mSessionObj);
    }

    public void sendSessionEvent(String paramString, Bundle paramBundle)
    {
      int i;
      if (Build.VERSION.SDK_INT < 23)
        i = -1 + this.mExtraControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onEvent(paramString, paramBundle);
          label44: i--;
          continue;
          this.mExtraControllerCallbacks.finishBroadcast();
          MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, paramString, paramBundle);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label44;
        }
      }
    }

    public void setActive(boolean paramBoolean)
    {
      MediaSessionCompatApi21.setActive(this.mSessionObj, paramBoolean);
    }

    public void setCallback(MediaSessionCompat.Callback paramCallback, Handler paramHandler)
    {
      Object localObject1 = this.mSessionObj;
      if (paramCallback == null);
      for (Object localObject2 = null; ; localObject2 = paramCallback.mCallbackObj)
      {
        MediaSessionCompatApi21.setCallback(localObject1, localObject2, paramHandler);
        if (paramCallback != null)
          paramCallback.mSessionImpl = new WeakReference(this);
        return;
      }
    }

    public void setExtras(Bundle paramBundle)
    {
      MediaSessionCompatApi21.setExtras(this.mSessionObj, paramBundle);
    }

    public void setFlags(int paramInt)
    {
      MediaSessionCompatApi21.setFlags(this.mSessionObj, paramInt);
    }

    public void setMediaButtonReceiver(PendingIntent paramPendingIntent)
    {
      MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, paramPendingIntent);
    }

    public void setMetadata(MediaMetadataCompat paramMediaMetadataCompat)
    {
      Object localObject1 = this.mSessionObj;
      if (paramMediaMetadataCompat == null);
      for (Object localObject2 = null; ; localObject2 = paramMediaMetadataCompat.getMediaMetadata())
      {
        MediaSessionCompatApi21.setMetadata(localObject1, localObject2);
        return;
      }
    }

    public void setPlaybackState(PlaybackStateCompat paramPlaybackStateCompat)
    {
      int i;
      if (Build.VERSION.SDK_INT < 22)
      {
        this.mPlaybackState = paramPlaybackStateCompat;
        i = -1 + this.mExtraControllerCallbacks.beginBroadcast();
      }
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onPlaybackStateChanged(paramPlaybackStateCompat);
          label51: i--;
          continue;
          this.mExtraControllerCallbacks.finishBroadcast();
          Object localObject1 = this.mSessionObj;
          if (paramPlaybackStateCompat == null);
          for (Object localObject2 = null; ; localObject2 = paramPlaybackStateCompat.getPlaybackState())
          {
            MediaSessionCompatApi21.setPlaybackState(localObject1, localObject2);
            return;
          }
        }
        catch (RemoteException localRemoteException)
        {
          break label51;
        }
      }
    }

    public void setPlaybackToLocal(int paramInt)
    {
      MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, paramInt);
    }

    public void setPlaybackToRemote(VolumeProviderCompat paramVolumeProviderCompat)
    {
      MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, paramVolumeProviderCompat.getVolumeProvider());
    }

    public void setQueue(List<MediaSessionCompat.QueueItem> paramList)
    {
      Object localObject = null;
      if (paramList != null)
      {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
          localArrayList.add(((MediaSessionCompat.QueueItem)localIterator.next()).getQueueItem());
        localObject = localArrayList;
      }
      MediaSessionCompatApi21.setQueue(this.mSessionObj, localObject);
    }

    public void setQueueTitle(CharSequence paramCharSequence)
    {
      MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, paramCharSequence);
    }

    public void setRatingType(int paramInt)
    {
      if (Build.VERSION.SDK_INT < 22)
      {
        this.mRatingType = paramInt;
        return;
      }
      MediaSessionCompatApi22.setRatingType(this.mSessionObj, paramInt);
    }

    public void setSessionActivity(PendingIntent paramPendingIntent)
    {
      MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, paramPendingIntent);
    }

    class ExtraSession extends IMediaSession.Stub
    {
      ExtraSession()
      {
      }

      public void adjustVolume(int paramInt1, int paramInt2, String paramString)
      {
        throw new AssertionError();
      }

      public void fastForward()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public Bundle getExtras()
      {
        throw new AssertionError();
      }

      public long getFlags()
      {
        throw new AssertionError();
      }

      public PendingIntent getLaunchPendingIntent()
      {
        throw new AssertionError();
      }

      public MediaMetadataCompat getMetadata()
      {
        throw new AssertionError();
      }

      public String getPackageName()
      {
        throw new AssertionError();
      }

      public PlaybackStateCompat getPlaybackState()
      {
        return MediaSessionCompat.MediaSessionImplApi21.this.mPlaybackState;
      }

      public List<MediaSessionCompat.QueueItem> getQueue()
      {
        return null;
      }

      public CharSequence getQueueTitle()
      {
        throw new AssertionError();
      }

      public int getRatingType()
      {
        return MediaSessionCompat.MediaSessionImplApi21.this.mRatingType;
      }

      public String getTag()
      {
        throw new AssertionError();
      }

      public ParcelableVolumeInfo getVolumeAttributes()
      {
        throw new AssertionError();
      }

      public boolean isTransportControlEnabled()
      {
        throw new AssertionError();
      }

      public void next()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void pause()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void play()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void playFromMediaId(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void playFromSearch(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void playFromUri(Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void prepare()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void prepareFromMediaId(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void prepareFromSearch(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void prepareFromUri(Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void previous()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void rate(RatingCompat paramRatingCompat)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void registerCallbackListener(IMediaControllerCallback paramIMediaControllerCallback)
      {
        if (!MediaSessionCompat.MediaSessionImplApi21.this.mDestroyed)
          MediaSessionCompat.MediaSessionImplApi21.this.mExtraControllerCallbacks.register(paramIMediaControllerCallback);
      }

      public void rewind()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void seekTo(long paramLong)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void sendCommand(String paramString, Bundle paramBundle, MediaSessionCompat.ResultReceiverWrapper paramResultReceiverWrapper)
      {
        throw new AssertionError();
      }

      public void sendCustomAction(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        throw new AssertionError();
      }

      public boolean sendMediaButton(KeyEvent paramKeyEvent)
      {
        throw new AssertionError();
      }

      public void setVolumeTo(int paramInt1, int paramInt2, String paramString)
      {
        throw new AssertionError();
      }

      public void skipToQueueItem(long paramLong)
      {
        throw new AssertionError();
      }

      public void stop()
        throws RemoteException
      {
        throw new AssertionError();
      }

      public void unregisterCallbackListener(IMediaControllerCallback paramIMediaControllerCallback)
      {
        MediaSessionCompat.MediaSessionImplApi21.this.mExtraControllerCallbacks.unregister(paramIMediaControllerCallback);
      }
    }
  }

  static class MediaSessionImplBase
    implements MediaSessionCompat.MediaSessionImpl
  {
    final AudioManager mAudioManager;
    volatile MediaSessionCompat.Callback mCallback;
    private final Context mContext;
    final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks = new RemoteCallbackList();
    boolean mDestroyed = false;
    Bundle mExtras;
    int mFlags;
    private MessageHandler mHandler;
    private boolean mIsActive = false;
    private boolean mIsMbrRegistered = false;
    private boolean mIsRccRegistered = false;
    int mLocalStream;
    final Object mLock = new Object();
    private final ComponentName mMediaButtonReceiverComponentName;
    private final PendingIntent mMediaButtonReceiverIntent;
    MediaMetadataCompat mMetadata;
    final String mPackageName;
    List<MediaSessionCompat.QueueItem> mQueue;
    CharSequence mQueueTitle;
    int mRatingType;
    private final Object mRccObj;
    PendingIntent mSessionActivity;
    PlaybackStateCompat mState;
    private final MediaSessionStub mStub;
    final String mTag;
    private final MediaSessionCompat.Token mToken;
    private VolumeProviderCompat.Callback mVolumeCallback = new VolumeProviderCompat.Callback()
    {
      public void onVolumeChanged(VolumeProviderCompat paramVolumeProviderCompat)
      {
        if (MediaSessionCompat.MediaSessionImplBase.this.mVolumeProvider != paramVolumeProviderCompat)
          return;
        ParcelableVolumeInfo localParcelableVolumeInfo = new ParcelableVolumeInfo(MediaSessionCompat.MediaSessionImplBase.this.mVolumeType, MediaSessionCompat.MediaSessionImplBase.this.mLocalStream, paramVolumeProviderCompat.getVolumeControl(), paramVolumeProviderCompat.getMaxVolume(), paramVolumeProviderCompat.getCurrentVolume());
        MediaSessionCompat.MediaSessionImplBase.this.sendVolumeInfoChanged(localParcelableVolumeInfo);
      }
    };
    VolumeProviderCompat mVolumeProvider;
    int mVolumeType;

    public MediaSessionImplBase(Context paramContext, String paramString, ComponentName paramComponentName, PendingIntent paramPendingIntent)
    {
      if (paramComponentName == null)
        throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
      this.mContext = paramContext;
      this.mPackageName = paramContext.getPackageName();
      this.mAudioManager = ((AudioManager)paramContext.getSystemService("audio"));
      this.mTag = paramString;
      this.mMediaButtonReceiverComponentName = paramComponentName;
      this.mMediaButtonReceiverIntent = paramPendingIntent;
      this.mStub = new MediaSessionStub();
      this.mToken = new MediaSessionCompat.Token(this.mStub);
      this.mRatingType = 0;
      this.mVolumeType = 1;
      this.mLocalStream = 3;
      if (Build.VERSION.SDK_INT >= 14)
      {
        this.mRccObj = MediaSessionCompatApi14.createRemoteControlClient(paramPendingIntent);
        return;
      }
      this.mRccObj = null;
    }

    private void sendEvent(String paramString, Bundle paramBundle)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onEvent(paramString, paramBundle);
          label36: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label36;
        }
      }
    }

    private void sendExtras(Bundle paramBundle)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onExtrasChanged(paramBundle);
          label33: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label33;
        }
      }
    }

    private void sendMetadata(MediaMetadataCompat paramMediaMetadataCompat)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onMetadataChanged(paramMediaMetadataCompat);
          label33: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label33;
        }
      }
    }

    private void sendQueue(List<MediaSessionCompat.QueueItem> paramList)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onQueueChanged(paramList);
          label33: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label33;
        }
      }
    }

    private void sendQueueTitle(CharSequence paramCharSequence)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onQueueTitleChanged(paramCharSequence);
          label33: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label33;
        }
      }
    }

    private void sendSessionDestroyed()
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onSessionDestroyed();
          label32: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          this.mControllerCallbacks.kill();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label32;
        }
      }
    }

    private void sendState(PlaybackStateCompat paramPlaybackStateCompat)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onPlaybackStateChanged(paramPlaybackStateCompat);
          label33: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label33;
        }
      }
    }

    private boolean update()
    {
      if (this.mIsActive)
      {
        if ((!this.mIsMbrRegistered) && ((0x1 & this.mFlags) != 0))
          if (Build.VERSION.SDK_INT >= 18)
          {
            MediaSessionCompatApi18.registerMediaButtonEventReceiver(this.mContext, this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
            this.mIsMbrRegistered = true;
          }
        do
        {
          if (Build.VERSION.SDK_INT < 14)
            break label316;
          if ((this.mIsRccRegistered) || ((0x2 & this.mFlags) == 0))
            break label184;
          MediaSessionCompatApi14.registerRemoteControlClient(this.mContext, this.mRccObj);
          this.mIsRccRegistered = true;
          return true;
          ((AudioManager)this.mContext.getSystemService("audio")).registerMediaButtonEventReceiver(this.mMediaButtonReceiverComponentName);
          break;
        }
        while ((!this.mIsMbrRegistered) || ((0x1 & this.mFlags) != 0));
        if (Build.VERSION.SDK_INT >= 18)
          MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
        while (true)
        {
          this.mIsMbrRegistered = false;
          break;
          ((AudioManager)this.mContext.getSystemService("audio")).unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverComponentName);
        }
        label184: if ((this.mIsRccRegistered) && ((0x2 & this.mFlags) == 0))
        {
          MediaSessionCompatApi14.setState(this.mRccObj, 0);
          MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
          this.mIsRccRegistered = false;
          return false;
        }
      }
      else
      {
        if (this.mIsMbrRegistered)
        {
          if (Build.VERSION.SDK_INT < 18)
            break label294;
          MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
        }
        while (true)
        {
          this.mIsMbrRegistered = false;
          if (!this.mIsRccRegistered)
            break;
          MediaSessionCompatApi14.setState(this.mRccObj, 0);
          MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
          this.mIsRccRegistered = false;
          return false;
          label294: ((AudioManager)this.mContext.getSystemService("audio")).unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverComponentName);
        }
      }
      label316: return false;
    }

    void adjustVolume(int paramInt1, int paramInt2)
    {
      if (this.mVolumeType == 2)
      {
        if (this.mVolumeProvider != null)
          this.mVolumeProvider.onAdjustVolume(paramInt1);
        return;
      }
      this.mAudioManager.adjustStreamVolume(this.mLocalStream, paramInt1, paramInt2);
    }

    public String getCallingPackage()
    {
      return null;
    }

    public Object getMediaSession()
    {
      return null;
    }

    public Object getRemoteControlClient()
    {
      return this.mRccObj;
    }

    public MediaSessionCompat.Token getSessionToken()
    {
      return this.mToken;
    }

    PlaybackStateCompat getStateWithUpdatedPosition()
    {
      long l1 = -1L;
      while (true)
      {
        long l4;
        synchronized (this.mLock)
        {
          PlaybackStateCompat localPlaybackStateCompat1 = this.mState;
          if ((this.mMetadata == null) || (!this.mMetadata.containsKey("android.media.metadata.DURATION")))
            continue;
          l1 = this.mMetadata.getLong("android.media.metadata.DURATION");
          if ((localPlaybackStateCompat1 == null) || ((localPlaybackStateCompat1.getState() != 3) && (localPlaybackStateCompat1.getState() != 4) && (localPlaybackStateCompat1.getState() != 5)))
            break label209;
          long l2 = localPlaybackStateCompat1.getLastPositionUpdateTime();
          long l3 = SystemClock.elapsedRealtime();
          if (l2 <= 0L)
            break label209;
          l4 = ()(localPlaybackStateCompat1.getPlaybackSpeed() * (float)(l3 - l2)) + localPlaybackStateCompat1.getPosition();
          if ((l1 >= 0L) && (l4 > l1))
          {
            PlaybackStateCompat.Builder localBuilder = new PlaybackStateCompat.Builder(localPlaybackStateCompat1);
            localBuilder.setState(localPlaybackStateCompat1.getState(), l1, localPlaybackStateCompat1.getPlaybackSpeed(), l3);
            localPlaybackStateCompat2 = localBuilder.build();
            if (localPlaybackStateCompat2 != null)
              continue;
            localPlaybackStateCompat2 = localPlaybackStateCompat1;
            return localPlaybackStateCompat2;
          }
        }
        if (l4 < 0L)
        {
          l1 = 0L;
          continue;
        }
        l1 = l4;
        continue;
        label209: PlaybackStateCompat localPlaybackStateCompat2 = null;
      }
    }

    public boolean isActive()
    {
      return this.mIsActive;
    }

    void postToHandler(int paramInt)
    {
      postToHandler(paramInt, null);
    }

    void postToHandler(int paramInt, Object paramObject)
    {
      postToHandler(paramInt, paramObject, null);
    }

    void postToHandler(int paramInt, Object paramObject, Bundle paramBundle)
    {
      synchronized (this.mLock)
      {
        if (this.mHandler != null)
          this.mHandler.post(paramInt, paramObject, paramBundle);
        return;
      }
    }

    public void release()
    {
      this.mIsActive = false;
      this.mDestroyed = true;
      update();
      sendSessionDestroyed();
    }

    public void sendSessionEvent(String paramString, Bundle paramBundle)
    {
      sendEvent(paramString, paramBundle);
    }

    void sendVolumeInfoChanged(ParcelableVolumeInfo paramParcelableVolumeInfo)
    {
      int i = -1 + this.mControllerCallbacks.beginBroadcast();
      while (true)
      {
        IMediaControllerCallback localIMediaControllerCallback;
        if (i >= 0)
          localIMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
        try
        {
          localIMediaControllerCallback.onVolumeInfoChanged(paramParcelableVolumeInfo);
          label33: i--;
          continue;
          this.mControllerCallbacks.finishBroadcast();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          break label33;
        }
      }
    }

    public void setActive(boolean paramBoolean)
    {
      if (paramBoolean == this.mIsActive);
      do
      {
        return;
        this.mIsActive = paramBoolean;
      }
      while (!update());
      setMetadata(this.mMetadata);
      setPlaybackState(this.mState);
    }

    public void setCallback(MediaSessionCompat.Callback paramCallback, Handler paramHandler)
    {
      this.mCallback = paramCallback;
      if (paramCallback == null)
      {
        if (Build.VERSION.SDK_INT >= 18)
          MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, null);
        if (Build.VERSION.SDK_INT >= 19)
          MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, null);
      }
      while (true)
      {
        return;
        if (paramHandler == null)
          paramHandler = new Handler();
        synchronized (this.mLock)
        {
          this.mHandler = new MessageHandler(paramHandler.getLooper());
          2 local2 = new MediaSessionCompatApi19.Callback()
          {
            public void onSeekTo(long paramLong)
            {
              MediaSessionCompat.MediaSessionImplBase.this.postToHandler(18, Long.valueOf(paramLong));
            }

            public void onSetRating(Object paramObject)
            {
              MediaSessionCompat.MediaSessionImplBase.this.postToHandler(19, RatingCompat.fromRating(paramObject));
            }
          };
          if (Build.VERSION.SDK_INT >= 18)
          {
            Object localObject4 = MediaSessionCompatApi18.createPlaybackPositionUpdateListener(local2);
            MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, localObject4);
          }
          if (Build.VERSION.SDK_INT < 19)
            continue;
          Object localObject3 = MediaSessionCompatApi19.createMetadataUpdateListener(local2);
          MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, localObject3);
          return;
        }
      }
    }

    public void setExtras(Bundle paramBundle)
    {
      this.mExtras = paramBundle;
      sendExtras(paramBundle);
    }

    public void setFlags(int paramInt)
    {
      synchronized (this.mLock)
      {
        this.mFlags = paramInt;
        update();
        return;
      }
    }

    public void setMediaButtonReceiver(PendingIntent paramPendingIntent)
    {
    }

    public void setMetadata(MediaMetadataCompat paramMediaMetadataCompat)
    {
      if (paramMediaMetadataCompat != null)
        paramMediaMetadataCompat = new MediaMetadataCompat.Builder(paramMediaMetadataCompat, MediaSessionCompat.sMaxBitmapSize).build();
      label101: 
      do
      {
        synchronized (this.mLock)
        {
          this.mMetadata = paramMediaMetadataCompat;
          sendMetadata(paramMediaMetadataCompat);
          if (!this.mIsActive)
            return;
        }
        if (Build.VERSION.SDK_INT < 19)
          continue;
        Object localObject4 = this.mRccObj;
        Bundle localBundle2 = null;
        long l;
        if (paramMediaMetadataCompat == null)
        {
          if (this.mState != null)
            break label101;
          l = 0L;
        }
        while (true)
        {
          MediaSessionCompatApi19.setMetadata(localObject4, localBundle2, l);
          return;
          localBundle2 = paramMediaMetadataCompat.getBundle();
          break;
          l = this.mState.getActions();
        }
      }
      while (Build.VERSION.SDK_INT < 14);
      Object localObject3 = this.mRccObj;
      Bundle localBundle1 = null;
      if (paramMediaMetadataCompat == null);
      while (true)
      {
        MediaSessionCompatApi14.setMetadata(localObject3, localBundle1);
        return;
        localBundle1 = paramMediaMetadataCompat.getBundle();
      }
    }

    public void setPlaybackState(PlaybackStateCompat paramPlaybackStateCompat)
    {
      do
      {
        while (true)
        {
          synchronized (this.mLock)
          {
            this.mState = paramPlaybackStateCompat;
            sendState(paramPlaybackStateCompat);
            if (!this.mIsActive)
              return;
          }
          if (paramPlaybackStateCompat != null)
            break;
          if (Build.VERSION.SDK_INT < 14)
            continue;
          MediaSessionCompatApi14.setState(this.mRccObj, 0);
          MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, 0L);
          return;
        }
        if (Build.VERSION.SDK_INT >= 18)
          MediaSessionCompatApi18.setState(this.mRccObj, paramPlaybackStateCompat.getState(), paramPlaybackStateCompat.getPosition(), paramPlaybackStateCompat.getPlaybackSpeed(), paramPlaybackStateCompat.getLastPositionUpdateTime());
        while (Build.VERSION.SDK_INT >= 19)
        {
          MediaSessionCompatApi19.setTransportControlFlags(this.mRccObj, paramPlaybackStateCompat.getActions());
          return;
          if (Build.VERSION.SDK_INT < 14)
            continue;
          MediaSessionCompatApi14.setState(this.mRccObj, paramPlaybackStateCompat.getState());
        }
        if (Build.VERSION.SDK_INT < 18)
          continue;
        MediaSessionCompatApi18.setTransportControlFlags(this.mRccObj, paramPlaybackStateCompat.getActions());
        return;
      }
      while (Build.VERSION.SDK_INT < 14);
      MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, paramPlaybackStateCompat.getActions());
    }

    public void setPlaybackToLocal(int paramInt)
    {
      if (this.mVolumeProvider != null)
        this.mVolumeProvider.setCallback(null);
      this.mVolumeType = 1;
      sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
    }

    public void setPlaybackToRemote(VolumeProviderCompat paramVolumeProviderCompat)
    {
      if (paramVolumeProviderCompat == null)
        throw new IllegalArgumentException("volumeProvider may not be null");
      if (this.mVolumeProvider != null)
        this.mVolumeProvider.setCallback(null);
      this.mVolumeType = 2;
      this.mVolumeProvider = paramVolumeProviderCompat;
      sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
      paramVolumeProviderCompat.setCallback(this.mVolumeCallback);
    }

    public void setQueue(List<MediaSessionCompat.QueueItem> paramList)
    {
      this.mQueue = paramList;
      sendQueue(paramList);
    }

    public void setQueueTitle(CharSequence paramCharSequence)
    {
      this.mQueueTitle = paramCharSequence;
      sendQueueTitle(paramCharSequence);
    }

    public void setRatingType(int paramInt)
    {
      this.mRatingType = paramInt;
    }

    public void setSessionActivity(PendingIntent paramPendingIntent)
    {
      synchronized (this.mLock)
      {
        this.mSessionActivity = paramPendingIntent;
        return;
      }
    }

    void setVolumeTo(int paramInt1, int paramInt2)
    {
      if (this.mVolumeType == 2)
      {
        if (this.mVolumeProvider != null)
          this.mVolumeProvider.onSetVolumeTo(paramInt1);
        return;
      }
      this.mAudioManager.setStreamVolume(this.mLocalStream, paramInt1, paramInt2);
    }

    private static final class Command
    {
      public final String command;
      public final Bundle extras;
      public final ResultReceiver stub;

      public Command(String paramString, Bundle paramBundle, ResultReceiver paramResultReceiver)
      {
        this.command = paramString;
        this.extras = paramBundle;
        this.stub = paramResultReceiver;
      }
    }

    class MediaSessionStub extends IMediaSession.Stub
    {
      MediaSessionStub()
      {
      }

      public void adjustVolume(int paramInt1, int paramInt2, String paramString)
      {
        MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(paramInt1, paramInt2);
      }

      public void fastForward()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(16);
      }

      public Bundle getExtras()
      {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock)
        {
          Bundle localBundle = MediaSessionCompat.MediaSessionImplBase.this.mExtras;
          return localBundle;
        }
      }

      public long getFlags()
      {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock)
        {
          long l = MediaSessionCompat.MediaSessionImplBase.this.mFlags;
          return l;
        }
      }

      public PendingIntent getLaunchPendingIntent()
      {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock)
        {
          PendingIntent localPendingIntent = MediaSessionCompat.MediaSessionImplBase.this.mSessionActivity;
          return localPendingIntent;
        }
      }

      public MediaMetadataCompat getMetadata()
      {
        return MediaSessionCompat.MediaSessionImplBase.this.mMetadata;
      }

      public String getPackageName()
      {
        return MediaSessionCompat.MediaSessionImplBase.this.mPackageName;
      }

      public PlaybackStateCompat getPlaybackState()
      {
        return MediaSessionCompat.MediaSessionImplBase.this.getStateWithUpdatedPosition();
      }

      public List<MediaSessionCompat.QueueItem> getQueue()
      {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock)
        {
          List localList = MediaSessionCompat.MediaSessionImplBase.this.mQueue;
          return localList;
        }
      }

      public CharSequence getQueueTitle()
      {
        return MediaSessionCompat.MediaSessionImplBase.this.mQueueTitle;
      }

      public int getRatingType()
      {
        return MediaSessionCompat.MediaSessionImplBase.this.mRatingType;
      }

      public String getTag()
      {
        return MediaSessionCompat.MediaSessionImplBase.this.mTag;
      }

      public ParcelableVolumeInfo getVolumeAttributes()
      {
        int i = 2;
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock)
        {
          int j = MediaSessionCompat.MediaSessionImplBase.this.mVolumeType;
          int k = MediaSessionCompat.MediaSessionImplBase.this.mLocalStream;
          VolumeProviderCompat localVolumeProviderCompat = MediaSessionCompat.MediaSessionImplBase.this.mVolumeProvider;
          if (j == i)
          {
            i = localVolumeProviderCompat.getVolumeControl();
            m = localVolumeProviderCompat.getMaxVolume();
            n = localVolumeProviderCompat.getCurrentVolume();
            return new ParcelableVolumeInfo(j, k, i, m, n);
          }
          int m = MediaSessionCompat.MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(k);
          int n = MediaSessionCompat.MediaSessionImplBase.this.mAudioManager.getStreamVolume(k);
        }
      }

      public boolean isTransportControlEnabled()
      {
        return (0x2 & MediaSessionCompat.MediaSessionImplBase.this.mFlags) != 0;
      }

      public void next()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(14);
      }

      public void pause()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(12);
      }

      public void play()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(7);
      }

      public void playFromMediaId(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(8, paramString, paramBundle);
      }

      public void playFromSearch(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(9, paramString, paramBundle);
      }

      public void playFromUri(Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(10, paramUri, paramBundle);
      }

      public void prepare()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(3);
      }

      public void prepareFromMediaId(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(4, paramString, paramBundle);
      }

      public void prepareFromSearch(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(5, paramString, paramBundle);
      }

      public void prepareFromUri(Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(6, paramUri, paramBundle);
      }

      public void previous()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(15);
      }

      public void rate(RatingCompat paramRatingCompat)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(19, paramRatingCompat);
      }

      public void registerCallbackListener(IMediaControllerCallback paramIMediaControllerCallback)
      {
        if (MediaSessionCompat.MediaSessionImplBase.this.mDestroyed);
        try
        {
          paramIMediaControllerCallback.onSessionDestroyed();
          return;
          MediaSessionCompat.MediaSessionImplBase.this.mControllerCallbacks.register(paramIMediaControllerCallback);
          return;
        }
        catch (Exception localException)
        {
        }
      }

      public void rewind()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(17);
      }

      public void seekTo(long paramLong)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(18, Long.valueOf(paramLong));
      }

      public void sendCommand(String paramString, Bundle paramBundle, MediaSessionCompat.ResultReceiverWrapper paramResultReceiverWrapper)
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(1, new MediaSessionCompat.MediaSessionImplBase.Command(paramString, paramBundle, MediaSessionCompat.ResultReceiverWrapper.access$000(paramResultReceiverWrapper)));
      }

      public void sendCustomAction(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(20, paramString, paramBundle);
      }

      public boolean sendMediaButton(KeyEvent paramKeyEvent)
      {
        if ((0x1 & MediaSessionCompat.MediaSessionImplBase.this.mFlags) != 0);
        for (int i = 1; ; i = 0)
        {
          if (i != 0)
            MediaSessionCompat.MediaSessionImplBase.this.postToHandler(21, paramKeyEvent);
          return i;
        }
      }

      public void setVolumeTo(int paramInt1, int paramInt2, String paramString)
      {
        MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(paramInt1, paramInt2);
      }

      public void skipToQueueItem(long paramLong)
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(11, Long.valueOf(paramLong));
      }

      public void stop()
        throws RemoteException
      {
        MediaSessionCompat.MediaSessionImplBase.this.postToHandler(13);
      }

      public void unregisterCallbackListener(IMediaControllerCallback paramIMediaControllerCallback)
      {
        MediaSessionCompat.MediaSessionImplBase.this.mControllerCallbacks.unregister(paramIMediaControllerCallback);
      }
    }

    private class MessageHandler extends Handler
    {
      private static final int KEYCODE_MEDIA_PAUSE = 127;
      private static final int KEYCODE_MEDIA_PLAY = 126;
      private static final int MSG_ADJUST_VOLUME = 2;
      private static final int MSG_COMMAND = 1;
      private static final int MSG_CUSTOM_ACTION = 20;
      private static final int MSG_FAST_FORWARD = 16;
      private static final int MSG_MEDIA_BUTTON = 21;
      private static final int MSG_NEXT = 14;
      private static final int MSG_PAUSE = 12;
      private static final int MSG_PLAY = 7;
      private static final int MSG_PLAY_MEDIA_ID = 8;
      private static final int MSG_PLAY_SEARCH = 9;
      private static final int MSG_PLAY_URI = 10;
      private static final int MSG_PREPARE = 3;
      private static final int MSG_PREPARE_MEDIA_ID = 4;
      private static final int MSG_PREPARE_SEARCH = 5;
      private static final int MSG_PREPARE_URI = 6;
      private static final int MSG_PREVIOUS = 15;
      private static final int MSG_RATE = 19;
      private static final int MSG_REWIND = 17;
      private static final int MSG_SEEK_TO = 18;
      private static final int MSG_SET_VOLUME = 22;
      private static final int MSG_SKIP_TO_ITEM = 11;
      private static final int MSG_STOP = 13;

      public MessageHandler(Looper arg2)
      {
        super();
      }

      private void onMediaButtonEvent(KeyEvent paramKeyEvent, MediaSessionCompat.Callback paramCallback)
      {
        if ((paramKeyEvent == null) || (paramKeyEvent.getAction() != 0));
        label24: int i;
        label140: int j;
        label153: label310: label316: 
        do
        {
          return;
          long l;
          if (MediaSessionCompat.MediaSessionImplBase.this.mState == null)
          {
            l = 0L;
            switch (paramKeyEvent.getKeyCode())
            {
            default:
              return;
            case 79:
            case 85:
              if ((MediaSessionCompat.MediaSessionImplBase.this.mState == null) || (MediaSessionCompat.MediaSessionImplBase.this.mState.getState() != 3))
                break;
              i = 1;
              if ((0x204 & l) != 0L)
              {
                j = 1;
                if ((l & 0x202) == 0L)
                  break label310;
              }
            case 126:
            case 127:
            case 87:
            case 88:
            case 86:
            case 90:
            case 89:
            }
          }
          for (int k = 1; ; k = 0)
          {
            if ((i == 0) || (k == 0))
              break label316;
            paramCallback.onPause();
            return;
            l = MediaSessionCompat.MediaSessionImplBase.this.mState.getActions();
            break label24;
            if ((l & 0x4) == 0L)
              break;
            paramCallback.onPlay();
            return;
            if ((l & 0x2) == 0L)
              break;
            paramCallback.onPause();
            return;
            if ((l & 0x20) == 0L)
              break;
            paramCallback.onSkipToNext();
            return;
            if ((l & 0x10) == 0L)
              break;
            paramCallback.onSkipToPrevious();
            return;
            if ((l & 1L) == 0L)
              break;
            paramCallback.onStop();
            return;
            if ((l & 0x40) == 0L)
              break;
            paramCallback.onFastForward();
            return;
            if ((l & 0x8) == 0L)
              break;
            paramCallback.onRewind();
            return;
            i = 0;
            break label140;
            j = 0;
            break label153;
          }
        }
        while ((i != 0) || (j == 0));
        paramCallback.onPlay();
      }

      public void handleMessage(Message paramMessage)
      {
        MediaSessionCompat.Callback localCallback = MediaSessionCompat.MediaSessionImplBase.this.mCallback;
        if (localCallback == null);
        KeyEvent localKeyEvent;
        Intent localIntent;
        do
        {
          return;
          switch (paramMessage.what)
          {
          default:
            return;
          case 1:
            MediaSessionCompat.MediaSessionImplBase.Command localCommand = (MediaSessionCompat.MediaSessionImplBase.Command)paramMessage.obj;
            localCallback.onCommand(localCommand.command, localCommand.extras, localCommand.stub);
            return;
          case 21:
            localKeyEvent = (KeyEvent)paramMessage.obj;
            localIntent = new Intent("android.intent.action.MEDIA_BUTTON");
            localIntent.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent);
          case 3:
          case 4:
          case 5:
          case 6:
          case 7:
          case 8:
          case 9:
          case 10:
          case 11:
          case 12:
          case 13:
          case 14:
          case 15:
          case 16:
          case 17:
          case 18:
          case 19:
          case 20:
          case 2:
          case 22:
          }
        }
        while (localCallback.onMediaButtonEvent(localIntent));
        onMediaButtonEvent(localKeyEvent, localCallback);
        return;
        localCallback.onPrepare();
        return;
        localCallback.onPrepareFromMediaId((String)paramMessage.obj, paramMessage.getData());
        return;
        localCallback.onPrepareFromSearch((String)paramMessage.obj, paramMessage.getData());
        return;
        localCallback.onPrepareFromUri((Uri)paramMessage.obj, paramMessage.getData());
        return;
        localCallback.onPlay();
        return;
        localCallback.onPlayFromMediaId((String)paramMessage.obj, paramMessage.getData());
        return;
        localCallback.onPlayFromSearch((String)paramMessage.obj, paramMessage.getData());
        return;
        localCallback.onPlayFromUri((Uri)paramMessage.obj, paramMessage.getData());
        return;
        localCallback.onSkipToQueueItem(((Long)paramMessage.obj).longValue());
        return;
        localCallback.onPause();
        return;
        localCallback.onStop();
        return;
        localCallback.onSkipToNext();
        return;
        localCallback.onSkipToPrevious();
        return;
        localCallback.onFastForward();
        return;
        localCallback.onRewind();
        return;
        localCallback.onSeekTo(((Long)paramMessage.obj).longValue());
        return;
        localCallback.onSetRating((RatingCompat)paramMessage.obj);
        return;
        localCallback.onCustomAction((String)paramMessage.obj, paramMessage.getData());
        return;
        MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(((Integer)paramMessage.obj).intValue(), 0);
        return;
        MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(((Integer)paramMessage.obj).intValue(), 0);
      }

      public void post(int paramInt)
      {
        post(paramInt, null);
      }

      public void post(int paramInt, Object paramObject)
      {
        obtainMessage(paramInt, paramObject).sendToTarget();
      }

      public void post(int paramInt1, Object paramObject, int paramInt2)
      {
        obtainMessage(paramInt1, paramInt2, 0, paramObject).sendToTarget();
      }

      public void post(int paramInt, Object paramObject, Bundle paramBundle)
      {
        Message localMessage = obtainMessage(paramInt, paramObject);
        localMessage.setData(paramBundle);
        localMessage.sendToTarget();
      }
    }
  }

  public static abstract interface OnActiveChangeListener
  {
    public abstract void onActiveChanged();
  }

  public static final class QueueItem
    implements Parcelable
  {
    public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator()
    {
      public MediaSessionCompat.QueueItem createFromParcel(Parcel paramParcel)
      {
        return new MediaSessionCompat.QueueItem(paramParcel);
      }

      public MediaSessionCompat.QueueItem[] newArray(int paramInt)
      {
        return new MediaSessionCompat.QueueItem[paramInt];
      }
    };
    public static final int UNKNOWN_ID = -1;
    private final MediaDescriptionCompat mDescription;
    private final long mId;
    private Object mItem;

    QueueItem(Parcel paramParcel)
    {
      this.mDescription = ((MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(paramParcel));
      this.mId = paramParcel.readLong();
    }

    public QueueItem(MediaDescriptionCompat paramMediaDescriptionCompat, long paramLong)
    {
      this(null, paramMediaDescriptionCompat, paramLong);
    }

    private QueueItem(Object paramObject, MediaDescriptionCompat paramMediaDescriptionCompat, long paramLong)
    {
      if (paramMediaDescriptionCompat == null)
        throw new IllegalArgumentException("Description cannot be null.");
      if (paramLong == -1L)
        throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
      this.mDescription = paramMediaDescriptionCompat;
      this.mId = paramLong;
      this.mItem = paramObject;
    }

    public static QueueItem fromQueueItem(Object paramObject)
    {
      if ((paramObject == null) || (Build.VERSION.SDK_INT < 21))
        return null;
      return new QueueItem(paramObject, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(paramObject)), MediaSessionCompatApi21.QueueItem.getQueueId(paramObject));
    }

    public static List<QueueItem> fromQueueItemList(List<?> paramList)
    {
      ArrayList localArrayList;
      if ((paramList == null) || (Build.VERSION.SDK_INT < 21))
        localArrayList = null;
      while (true)
      {
        return localArrayList;
        localArrayList = new ArrayList();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
          localArrayList.add(fromQueueItem(localIterator.next()));
      }
    }

    @Deprecated
    public static QueueItem obtain(Object paramObject)
    {
      return fromQueueItem(paramObject);
    }

    public int describeContents()
    {
      return 0;
    }

    public MediaDescriptionCompat getDescription()
    {
      return this.mDescription;
    }

    public long getQueueId()
    {
      return this.mId;
    }

    public Object getQueueItem()
    {
      if ((this.mItem != null) || (Build.VERSION.SDK_INT < 21))
        return this.mItem;
      this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
      return this.mItem;
    }

    public String toString()
    {
      return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      this.mDescription.writeToParcel(paramParcel, paramInt);
      paramParcel.writeLong(this.mId);
    }
  }

  static final class ResultReceiverWrapper
    implements Parcelable
  {
    public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new Parcelable.Creator()
    {
      public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel paramParcel)
      {
        return new MediaSessionCompat.ResultReceiverWrapper(paramParcel);
      }

      public MediaSessionCompat.ResultReceiverWrapper[] newArray(int paramInt)
      {
        return new MediaSessionCompat.ResultReceiverWrapper[paramInt];
      }
    };
    private ResultReceiver mResultReceiver;

    ResultReceiverWrapper(Parcel paramParcel)
    {
      this.mResultReceiver = ((ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel));
    }

    public ResultReceiverWrapper(ResultReceiver paramResultReceiver)
    {
      this.mResultReceiver = paramResultReceiver;
    }

    public int describeContents()
    {
      return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      this.mResultReceiver.writeToParcel(paramParcel, paramInt);
    }
  }

  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface SessionFlags
  {
  }

  public static final class Token
    implements Parcelable
  {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator()
    {
      public MediaSessionCompat.Token createFromParcel(Parcel paramParcel)
      {
        if (Build.VERSION.SDK_INT >= 21);
        for (Object localObject = paramParcel.readParcelable(null); ; localObject = paramParcel.readStrongBinder())
          return new MediaSessionCompat.Token(localObject);
      }

      public MediaSessionCompat.Token[] newArray(int paramInt)
      {
        return new MediaSessionCompat.Token[paramInt];
      }
    };
    private final Object mInner;

    Token(Object paramObject)
    {
      this.mInner = paramObject;
    }

    public static Token fromToken(Object paramObject)
    {
      if ((paramObject == null) || (Build.VERSION.SDK_INT < 21))
        return null;
      return new Token(MediaSessionCompatApi21.verifyToken(paramObject));
    }

    public int describeContents()
    {
      return 0;
    }

    public boolean equals(Object paramObject)
    {
      int i;
      if (this == paramObject)
        i = 1;
      Token localToken;
      Object localObject;
      do
      {
        boolean bool;
        do
        {
          return i;
          bool = paramObject instanceof Token;
          i = 0;
        }
        while (!bool);
        localToken = (Token)paramObject;
        if (this.mInner == null)
        {
          if (localToken.mInner == null)
            break;
          return false;
        }
        localObject = localToken.mInner;
        i = 0;
      }
      while (localObject == null);
      return this.mInner.equals(localToken.mInner);
    }

    public Object getToken()
    {
      return this.mInner;
    }

    public int hashCode()
    {
      if (this.mInner == null)
        return 0;
      return this.mInner.hashCode();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      if (Build.VERSION.SDK_INT >= 21)
      {
        paramParcel.writeParcelable((Parcelable)this.mInner, paramInt);
        return;
      }
      paramParcel.writeStrongBinder((IBinder)this.mInner);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.session.MediaSessionCompat
 * JD-Core Version:    0.6.0
 */