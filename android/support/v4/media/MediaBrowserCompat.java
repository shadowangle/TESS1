package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class MediaBrowserCompat
{
  static final boolean DEBUG = false;
  public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
  public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
  static final String TAG = "MediaBrowserCompat";
  private final MediaBrowserImpl mImpl;

  public MediaBrowserCompat(Context paramContext, ComponentName paramComponentName, ConnectionCallback paramConnectionCallback, Bundle paramBundle)
  {
    if ((Build.VERSION.SDK_INT >= 24) || (BuildCompat.isAtLeastN()))
    {
      this.mImpl = new MediaBrowserImplApi24(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
      return;
    }
    if (Build.VERSION.SDK_INT >= 23)
    {
      this.mImpl = new MediaBrowserImplApi23(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
      return;
    }
    if (Build.VERSION.SDK_INT >= 21)
    {
      this.mImpl = new MediaBrowserImplApi21(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
      return;
    }
    this.mImpl = new MediaBrowserImplBase(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
  }

  public void connect()
  {
    this.mImpl.connect();
  }

  public void disconnect()
  {
    this.mImpl.disconnect();
  }

  @Nullable
  public Bundle getExtras()
  {
    return this.mImpl.getExtras();
  }

  public void getItem(@NonNull String paramString, @NonNull ItemCallback paramItemCallback)
  {
    this.mImpl.getItem(paramString, paramItemCallback);
  }

  @NonNull
  public String getRoot()
  {
    return this.mImpl.getRoot();
  }

  @NonNull
  public ComponentName getServiceComponent()
  {
    return this.mImpl.getServiceComponent();
  }

  @NonNull
  public MediaSessionCompat.Token getSessionToken()
  {
    return this.mImpl.getSessionToken();
  }

  public boolean isConnected()
  {
    return this.mImpl.isConnected();
  }

  public void subscribe(@NonNull String paramString, @NonNull Bundle paramBundle, @NonNull SubscriptionCallback paramSubscriptionCallback)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty");
    if (paramSubscriptionCallback == null)
      throw new IllegalArgumentException("callback is null");
    if (paramBundle == null)
      throw new IllegalArgumentException("options are null");
    this.mImpl.subscribe(paramString, paramBundle, paramSubscriptionCallback);
  }

  public void subscribe(@NonNull String paramString, @NonNull SubscriptionCallback paramSubscriptionCallback)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty");
    if (paramSubscriptionCallback == null)
      throw new IllegalArgumentException("callback is null");
    this.mImpl.subscribe(paramString, null, paramSubscriptionCallback);
  }

  public void unsubscribe(@NonNull String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty");
    this.mImpl.unsubscribe(paramString, null);
  }

  public void unsubscribe(@NonNull String paramString, @NonNull SubscriptionCallback paramSubscriptionCallback)
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("parentId is empty");
    if (paramSubscriptionCallback == null)
      throw new IllegalArgumentException("callback is null");
    this.mImpl.unsubscribe(paramString, paramSubscriptionCallback);
  }

  private static class CallbackHandler extends Handler
  {
    private final WeakReference<MediaBrowserCompat.MediaBrowserServiceCallbackImpl> mCallbackImplRef;
    private WeakReference<Messenger> mCallbacksMessengerRef;

    CallbackHandler(MediaBrowserCompat.MediaBrowserServiceCallbackImpl paramMediaBrowserServiceCallbackImpl)
    {
      this.mCallbackImplRef = new WeakReference(paramMediaBrowserServiceCallbackImpl);
    }

    public void handleMessage(Message paramMessage)
    {
      if ((this.mCallbacksMessengerRef == null) || (this.mCallbacksMessengerRef.get() == null) || (this.mCallbackImplRef.get() == null))
        return;
      Bundle localBundle = paramMessage.getData();
      localBundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
      switch (paramMessage.what)
      {
      default:
        Log.w("MediaBrowserCompat", "Unhandled message: " + paramMessage + "\n  Client version: " + 1 + "\n  Service version: " + paramMessage.arg1);
        return;
      case 1:
        ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onServiceConnected((Messenger)this.mCallbacksMessengerRef.get(), localBundle.getString("data_media_item_id"), (MediaSessionCompat.Token)localBundle.getParcelable("data_media_session_token"), localBundle.getBundle("data_root_hints"));
        return;
      case 2:
        ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onConnectionFailed((Messenger)this.mCallbacksMessengerRef.get());
        return;
      case 3:
      }
      ((MediaBrowserCompat.MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onLoadChildren((Messenger)this.mCallbacksMessengerRef.get(), localBundle.getString("data_media_item_id"), localBundle.getParcelableArrayList("data_media_item_list"), localBundle.getBundle("data_options"));
    }

    void setCallbacksMessenger(Messenger paramMessenger)
    {
      this.mCallbacksMessengerRef = new WeakReference(paramMessenger);
    }
  }

  public static class ConnectionCallback
  {
    ConnectionCallbackInternal mConnectionCallbackInternal;
    final Object mConnectionCallbackObj;

    public ConnectionCallback()
    {
      if (Build.VERSION.SDK_INT >= 21)
      {
        this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback(new StubApi21());
        return;
      }
      this.mConnectionCallbackObj = null;
    }

    public void onConnected()
    {
    }

    public void onConnectionFailed()
    {
    }

    public void onConnectionSuspended()
    {
    }

    void setInternalConnectionCallback(ConnectionCallbackInternal paramConnectionCallbackInternal)
    {
      this.mConnectionCallbackInternal = paramConnectionCallbackInternal;
    }

    static abstract interface ConnectionCallbackInternal
    {
      public abstract void onConnected();

      public abstract void onConnectionFailed();

      public abstract void onConnectionSuspended();
    }

    private class StubApi21
      implements MediaBrowserCompatApi21.ConnectionCallback
    {
      StubApi21()
      {
      }

      public void onConnected()
      {
        if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null)
          MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
        MediaBrowserCompat.ConnectionCallback.this.onConnected();
      }

      public void onConnectionFailed()
      {
        if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null)
          MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
        MediaBrowserCompat.ConnectionCallback.this.onConnectionFailed();
      }

      public void onConnectionSuspended()
      {
        if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null)
          MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
        MediaBrowserCompat.ConnectionCallback.this.onConnectionSuspended();
      }
    }
  }

  public static abstract class ItemCallback
  {
    final Object mItemCallbackObj;

    public ItemCallback()
    {
      if (Build.VERSION.SDK_INT >= 23)
      {
        this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback(new StubApi23());
        return;
      }
      this.mItemCallbackObj = null;
    }

    public void onError(@NonNull String paramString)
    {
    }

    public void onItemLoaded(MediaBrowserCompat.MediaItem paramMediaItem)
    {
    }

    private class StubApi23
      implements MediaBrowserCompatApi23.ItemCallback
    {
      StubApi23()
      {
      }

      public void onError(@NonNull String paramString)
      {
        MediaBrowserCompat.ItemCallback.this.onError(paramString);
      }

      public void onItemLoaded(Parcel paramParcel)
      {
        paramParcel.setDataPosition(0);
        MediaBrowserCompat.MediaItem localMediaItem = (MediaBrowserCompat.MediaItem)MediaBrowserCompat.MediaItem.CREATOR.createFromParcel(paramParcel);
        paramParcel.recycle();
        MediaBrowserCompat.ItemCallback.this.onItemLoaded(localMediaItem);
      }
    }
  }

  private static class ItemReceiver extends ResultReceiver
  {
    private final MediaBrowserCompat.ItemCallback mCallback;
    private final String mMediaId;

    ItemReceiver(String paramString, MediaBrowserCompat.ItemCallback paramItemCallback, Handler paramHandler)
    {
      super();
      this.mMediaId = paramString;
      this.mCallback = paramItemCallback;
    }

    protected void onReceiveResult(int paramInt, Bundle paramBundle)
    {
      if (paramBundle != null)
        paramBundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
      if ((paramInt != 0) || (paramBundle == null) || (!paramBundle.containsKey("media_item")))
      {
        this.mCallback.onError(this.mMediaId);
        return;
      }
      Parcelable localParcelable = paramBundle.getParcelable("media_item");
      if ((localParcelable == null) || ((localParcelable instanceof MediaBrowserCompat.MediaItem)))
      {
        this.mCallback.onItemLoaded((MediaBrowserCompat.MediaItem)localParcelable);
        return;
      }
      this.mCallback.onError(this.mMediaId);
    }
  }

  static abstract interface MediaBrowserImpl
  {
    public abstract void connect();

    public abstract void disconnect();

    @Nullable
    public abstract Bundle getExtras();

    public abstract void getItem(@NonNull String paramString, @NonNull MediaBrowserCompat.ItemCallback paramItemCallback);

    @NonNull
    public abstract String getRoot();

    public abstract ComponentName getServiceComponent();

    @NonNull
    public abstract MediaSessionCompat.Token getSessionToken();

    public abstract boolean isConnected();

    public abstract void subscribe(@NonNull String paramString, Bundle paramBundle, @NonNull MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback);

    public abstract void unsubscribe(@NonNull String paramString, MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback);
  }

  static class MediaBrowserImplApi21
    implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl, MediaBrowserCompat.ConnectionCallback.ConnectionCallbackInternal
  {
    protected final Object mBrowserObj;
    protected Messenger mCallbacksMessenger;
    protected final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
    protected final Bundle mRootHints;
    protected MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
    private final ArrayMap<String, MediaBrowserCompat.Subscription> mSubscriptions = new ArrayMap();

    public MediaBrowserImplApi21(Context paramContext, ComponentName paramComponentName, MediaBrowserCompat.ConnectionCallback paramConnectionCallback, Bundle paramBundle)
    {
      if (Build.VERSION.SDK_INT < 25)
      {
        if (paramBundle == null)
          paramBundle = new Bundle();
        paramBundle.putInt("extra_client_version", 1);
        this.mRootHints = new Bundle(paramBundle);
        paramConnectionCallback.setInternalConnectionCallback(this);
        this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(paramContext, paramComponentName, paramConnectionCallback.mConnectionCallbackObj, this.mRootHints);
        return;
      }
      if (paramBundle == null);
      for (Bundle localBundle = null; ; localBundle = new Bundle(paramBundle))
      {
        this.mRootHints = localBundle;
        break;
      }
    }

    public void connect()
    {
      MediaBrowserCompatApi21.connect(this.mBrowserObj);
    }

    public void disconnect()
    {
      if ((this.mServiceBinderWrapper != null) && (this.mCallbacksMessenger != null));
      try
      {
        this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);
        MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
          Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
      }
    }

    @Nullable
    public Bundle getExtras()
    {
      return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
    }

    public void getItem(@NonNull String paramString, @NonNull MediaBrowserCompat.ItemCallback paramItemCallback)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("mediaId is empty");
      if (paramItemCallback == null)
        throw new IllegalArgumentException("cb is null");
      if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj))
      {
        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
        this.mHandler.post(new Runnable(paramItemCallback, paramString)
        {
          public void run()
          {
            this.val$cb.onError(this.val$mediaId);
          }
        });
        return;
      }
      if (this.mServiceBinderWrapper == null)
      {
        this.mHandler.post(new Runnable(paramItemCallback, paramString)
        {
          public void run()
          {
            this.val$cb.onError(this.val$mediaId);
          }
        });
        return;
      }
      MediaBrowserCompat.ItemReceiver localItemReceiver = new MediaBrowserCompat.ItemReceiver(paramString, paramItemCallback, this.mHandler);
      try
      {
        this.mServiceBinderWrapper.getMediaItem(paramString, localItemReceiver, this.mCallbacksMessenger);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.i("MediaBrowserCompat", "Remote error getting media item: " + paramString);
        this.mHandler.post(new Runnable(paramItemCallback, paramString)
        {
          public void run()
          {
            this.val$cb.onError(this.val$mediaId);
          }
        });
      }
    }

    @NonNull
    public String getRoot()
    {
      return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
    }

    public ComponentName getServiceComponent()
    {
      return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
    }

    @NonNull
    public MediaSessionCompat.Token getSessionToken()
    {
      return MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
    }

    public boolean isConnected()
    {
      return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
    }

    public void onConnected()
    {
      Bundle localBundle = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
      if (localBundle == null);
      IBinder localIBinder;
      do
      {
        return;
        localIBinder = BundleCompat.getBinder(localBundle, "extra_messenger");
      }
      while (localIBinder == null);
      this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(localIBinder, this.mRootHints);
      this.mCallbacksMessenger = new Messenger(this.mHandler);
      this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
      try
      {
        this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
      }
    }

    public void onConnectionFailed()
    {
    }

    public void onConnectionFailed(Messenger paramMessenger)
    {
    }

    public void onConnectionSuspended()
    {
      this.mServiceBinderWrapper = null;
      this.mCallbacksMessenger = null;
      this.mHandler.setCallbacksMessenger(null);
    }

    public void onLoadChildren(Messenger paramMessenger, String paramString, List paramList, Bundle paramBundle)
    {
      if (this.mCallbacksMessenger != paramMessenger);
      MediaBrowserCompat.SubscriptionCallback localSubscriptionCallback;
      do
      {
        MediaBrowserCompat.Subscription localSubscription;
        while (true)
        {
          return;
          localSubscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(paramString);
          if (localSubscription != null)
            break;
          if (!MediaBrowserCompat.DEBUG)
            continue;
          Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + paramString);
          return;
        }
        localSubscriptionCallback = localSubscription.getCallback(paramBundle);
      }
      while (localSubscriptionCallback == null);
      if (paramBundle == null)
      {
        localSubscriptionCallback.onChildrenLoaded(paramString, paramList);
        return;
      }
      localSubscriptionCallback.onChildrenLoaded(paramString, paramList, paramBundle);
    }

    public void onServiceConnected(Messenger paramMessenger, String paramString, MediaSessionCompat.Token paramToken, Bundle paramBundle)
    {
    }

    public void subscribe(@NonNull String paramString, Bundle paramBundle, @NonNull MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      MediaBrowserCompat.Subscription localSubscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(paramString);
      if (localSubscription == null)
      {
        localSubscription = new MediaBrowserCompat.Subscription();
        this.mSubscriptions.put(paramString, localSubscription);
      }
      MediaBrowserCompat.SubscriptionCallback.access$100(paramSubscriptionCallback, localSubscription);
      localSubscription.putCallback(paramBundle, paramSubscriptionCallback);
      if (this.mServiceBinderWrapper == null)
      {
        MediaBrowserCompatApi21.subscribe(this.mBrowserObj, paramString, MediaBrowserCompat.SubscriptionCallback.access$200(paramSubscriptionCallback));
        return;
      }
      try
      {
        this.mServiceBinderWrapper.addSubscription(paramString, MediaBrowserCompat.SubscriptionCallback.access$000(paramSubscriptionCallback), paramBundle, this.mCallbacksMessenger);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.i("MediaBrowserCompat", "Remote error subscribing media item: " + paramString);
      }
    }

    public void unsubscribe(@NonNull String paramString, MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      MediaBrowserCompat.Subscription localSubscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(paramString);
      if (localSubscription == null)
        return;
      if (this.mServiceBinderWrapper == null)
        if (paramSubscriptionCallback == null)
          MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, paramString);
      while (true)
      {
        if ((localSubscription.isEmpty()) || (paramSubscriptionCallback == null))
        {
          this.mSubscriptions.remove(paramString);
          return;
          List localList3 = localSubscription.getCallbacks();
          List localList4 = localSubscription.getOptionsList();
          for (int j = -1 + localList3.size(); j >= 0; j--)
          {
            if (localList3.get(j) != paramSubscriptionCallback)
              continue;
            localList3.remove(j);
            localList4.remove(j);
          }
          if (localList3.size() != 0)
            continue;
          MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, paramString);
          continue;
          if (paramSubscriptionCallback == null)
          {
            try
            {
              this.mServiceBinderWrapper.removeSubscription(paramString, null, this.mCallbacksMessenger);
            }
            catch (RemoteException localRemoteException)
            {
              Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + paramString);
            }
            continue;
          }
        }
        else
        {
          break;
        }
        List localList1 = localSubscription.getCallbacks();
        List localList2 = localSubscription.getOptionsList();
        for (int i = -1 + localList1.size(); i >= 0; i--)
        {
          if (localList1.get(i) != paramSubscriptionCallback)
            continue;
          this.mServiceBinderWrapper.removeSubscription(paramString, MediaBrowserCompat.SubscriptionCallback.access$000(paramSubscriptionCallback), this.mCallbacksMessenger);
          localList1.remove(i);
          localList2.remove(i);
        }
      }
    }
  }

  static class MediaBrowserImplApi23 extends MediaBrowserCompat.MediaBrowserImplApi21
  {
    public MediaBrowserImplApi23(Context paramContext, ComponentName paramComponentName, MediaBrowserCompat.ConnectionCallback paramConnectionCallback, Bundle paramBundle)
    {
      super(paramComponentName, paramConnectionCallback, paramBundle);
    }

    public void getItem(@NonNull String paramString, @NonNull MediaBrowserCompat.ItemCallback paramItemCallback)
    {
      if (this.mServiceBinderWrapper == null)
      {
        MediaBrowserCompatApi23.getItem(this.mBrowserObj, paramString, paramItemCallback.mItemCallbackObj);
        return;
      }
      super.getItem(paramString, paramItemCallback);
    }
  }

  static class MediaBrowserImplApi24 extends MediaBrowserCompat.MediaBrowserImplApi23
  {
    public MediaBrowserImplApi24(Context paramContext, ComponentName paramComponentName, MediaBrowserCompat.ConnectionCallback paramConnectionCallback, Bundle paramBundle)
    {
      super(paramComponentName, paramConnectionCallback, paramBundle);
    }

    public void subscribe(@NonNull String paramString, @NonNull Bundle paramBundle, @NonNull MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      if (paramBundle == null)
      {
        MediaBrowserCompatApi21.subscribe(this.mBrowserObj, paramString, MediaBrowserCompat.SubscriptionCallback.access$200(paramSubscriptionCallback));
        return;
      }
      MediaBrowserCompatApi24.subscribe(this.mBrowserObj, paramString, paramBundle, MediaBrowserCompat.SubscriptionCallback.access$200(paramSubscriptionCallback));
    }

    public void unsubscribe(@NonNull String paramString, MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      if (paramSubscriptionCallback == null)
      {
        MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, paramString);
        return;
      }
      MediaBrowserCompatApi24.unsubscribe(this.mBrowserObj, paramString, MediaBrowserCompat.SubscriptionCallback.access$200(paramSubscriptionCallback));
    }
  }

  static class MediaBrowserImplBase
    implements MediaBrowserCompat.MediaBrowserImpl, MediaBrowserCompat.MediaBrowserServiceCallbackImpl
  {
    private static final int CONNECT_STATE_CONNECTED = 2;
    static final int CONNECT_STATE_CONNECTING = 1;
    static final int CONNECT_STATE_DISCONNECTED = 0;
    static final int CONNECT_STATE_SUSPENDED = 3;
    final MediaBrowserCompat.ConnectionCallback mCallback;
    Messenger mCallbacksMessenger;
    final Context mContext;
    private Bundle mExtras;
    final MediaBrowserCompat.CallbackHandler mHandler = new MediaBrowserCompat.CallbackHandler(this);
    private MediaSessionCompat.Token mMediaSessionToken;
    final Bundle mRootHints;
    private String mRootId;
    MediaBrowserCompat.ServiceBinderWrapper mServiceBinderWrapper;
    final ComponentName mServiceComponent;
    MediaServiceConnection mServiceConnection;
    int mState = 0;
    private final ArrayMap<String, MediaBrowserCompat.Subscription> mSubscriptions = new ArrayMap();

    public MediaBrowserImplBase(Context paramContext, ComponentName paramComponentName, MediaBrowserCompat.ConnectionCallback paramConnectionCallback, Bundle paramBundle)
    {
      if (paramContext == null)
        throw new IllegalArgumentException("context must not be null");
      if (paramComponentName == null)
        throw new IllegalArgumentException("service component must not be null");
      if (paramConnectionCallback == null)
        throw new IllegalArgumentException("connection callback must not be null");
      this.mContext = paramContext;
      this.mServiceComponent = paramComponentName;
      this.mCallback = paramConnectionCallback;
      if (paramBundle == null);
      for (Bundle localBundle = null; ; localBundle = new Bundle(paramBundle))
      {
        this.mRootHints = localBundle;
        return;
      }
    }

    private static String getStateLabel(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return "UNKNOWN/" + paramInt;
      case 0:
        return "CONNECT_STATE_DISCONNECTED";
      case 1:
        return "CONNECT_STATE_CONNECTING";
      case 2:
        return "CONNECT_STATE_CONNECTED";
      case 3:
      }
      return "CONNECT_STATE_SUSPENDED";
    }

    private boolean isCurrent(Messenger paramMessenger, String paramString)
    {
      if (this.mCallbacksMessenger != paramMessenger)
      {
        if (this.mState != 0)
          Log.i("MediaBrowserCompat", paramString + " for " + this.mServiceComponent + " with mCallbacksMessenger=" + this.mCallbacksMessenger + " this=" + this);
        return false;
      }
      return true;
    }

    public void connect()
    {
      if (this.mState != 0)
        throw new IllegalStateException("connect() called while not disconnected (state=" + getStateLabel(this.mState) + ")");
      if ((MediaBrowserCompat.DEBUG) && (this.mServiceConnection != null))
        throw new RuntimeException("mServiceConnection should be null. Instead it is " + this.mServiceConnection);
      if (this.mServiceBinderWrapper != null)
        throw new RuntimeException("mServiceBinderWrapper should be null. Instead it is " + this.mServiceBinderWrapper);
      if (this.mCallbacksMessenger != null)
        throw new RuntimeException("mCallbacksMessenger should be null. Instead it is " + this.mCallbacksMessenger);
      this.mState = 1;
      Intent localIntent = new Intent("android.media.browse.MediaBrowserService");
      localIntent.setComponent(this.mServiceComponent);
      MediaServiceConnection localMediaServiceConnection = new MediaServiceConnection();
      this.mServiceConnection = localMediaServiceConnection;
      try
      {
        boolean bool2 = this.mContext.bindService(localIntent, this.mServiceConnection, 1);
        bool1 = bool2;
        if (!bool1)
          this.mHandler.post(new Runnable(localMediaServiceConnection)
          {
            public void run()
            {
              if (this.val$thisConnection == MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection)
              {
                MediaBrowserCompat.MediaBrowserImplBase.this.forceCloseConnection();
                MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionFailed();
              }
            }
          });
        if (MediaBrowserCompat.DEBUG)
        {
          Log.d("MediaBrowserCompat", "connect...");
          dump();
        }
        return;
      }
      catch (Exception localException)
      {
        while (true)
        {
          Log.e("MediaBrowserCompat", "Failed binding to service " + this.mServiceComponent);
          boolean bool1 = false;
        }
      }
    }

    public void disconnect()
    {
      if (this.mCallbacksMessenger != null);
      try
      {
        this.mServiceBinderWrapper.disconnect(this.mCallbacksMessenger);
        forceCloseConnection();
        if (MediaBrowserCompat.DEBUG)
        {
          Log.d("MediaBrowserCompat", "disconnect...");
          dump();
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
          Log.w("MediaBrowserCompat", "RemoteException during connect for " + this.mServiceComponent);
      }
    }

    void dump()
    {
      Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
      Log.d("MediaBrowserCompat", "  mServiceComponent=" + this.mServiceComponent);
      Log.d("MediaBrowserCompat", "  mCallback=" + this.mCallback);
      Log.d("MediaBrowserCompat", "  mRootHints=" + this.mRootHints);
      Log.d("MediaBrowserCompat", "  mState=" + getStateLabel(this.mState));
      Log.d("MediaBrowserCompat", "  mServiceConnection=" + this.mServiceConnection);
      Log.d("MediaBrowserCompat", "  mServiceBinderWrapper=" + this.mServiceBinderWrapper);
      Log.d("MediaBrowserCompat", "  mCallbacksMessenger=" + this.mCallbacksMessenger);
      Log.d("MediaBrowserCompat", "  mRootId=" + this.mRootId);
      Log.d("MediaBrowserCompat", "  mMediaSessionToken=" + this.mMediaSessionToken);
    }

    void forceCloseConnection()
    {
      if (this.mServiceConnection != null)
        this.mContext.unbindService(this.mServiceConnection);
      this.mState = 0;
      this.mServiceConnection = null;
      this.mServiceBinderWrapper = null;
      this.mCallbacksMessenger = null;
      this.mHandler.setCallbacksMessenger(null);
      this.mRootId = null;
      this.mMediaSessionToken = null;
    }

    @Nullable
    public Bundle getExtras()
    {
      if (!isConnected())
        throw new IllegalStateException("getExtras() called while not connected (state=" + getStateLabel(this.mState) + ")");
      return this.mExtras;
    }

    public void getItem(@NonNull String paramString, @NonNull MediaBrowserCompat.ItemCallback paramItemCallback)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("mediaId is empty");
      if (paramItemCallback == null)
        throw new IllegalArgumentException("cb is null");
      if (this.mState != 2)
      {
        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
        this.mHandler.post(new Runnable(paramItemCallback, paramString)
        {
          public void run()
          {
            this.val$cb.onError(this.val$mediaId);
          }
        });
        return;
      }
      MediaBrowserCompat.ItemReceiver localItemReceiver = new MediaBrowserCompat.ItemReceiver(paramString, paramItemCallback, this.mHandler);
      try
      {
        this.mServiceBinderWrapper.getMediaItem(paramString, localItemReceiver, this.mCallbacksMessenger);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.i("MediaBrowserCompat", "Remote error getting media item.");
        this.mHandler.post(new Runnable(paramItemCallback, paramString)
        {
          public void run()
          {
            this.val$cb.onError(this.val$mediaId);
          }
        });
      }
    }

    @NonNull
    public String getRoot()
    {
      if (!isConnected())
        throw new IllegalStateException("getRoot() called while not connected(state=" + getStateLabel(this.mState) + ")");
      return this.mRootId;
    }

    @NonNull
    public ComponentName getServiceComponent()
    {
      if (!isConnected())
        throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")");
      return this.mServiceComponent;
    }

    @NonNull
    public MediaSessionCompat.Token getSessionToken()
    {
      if (!isConnected())
        throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")");
      return this.mMediaSessionToken;
    }

    public boolean isConnected()
    {
      return this.mState == 2;
    }

    public void onConnectionFailed(Messenger paramMessenger)
    {
      Log.e("MediaBrowserCompat", "onConnectFailed for " + this.mServiceComponent);
      if (!isCurrent(paramMessenger, "onConnectFailed"))
        return;
      if (this.mState != 1)
      {
        Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
        return;
      }
      forceCloseConnection();
      this.mCallback.onConnectionFailed();
    }

    public void onLoadChildren(Messenger paramMessenger, String paramString, List paramList, Bundle paramBundle)
    {
      if (!isCurrent(paramMessenger, "onLoadChildren"));
      MediaBrowserCompat.SubscriptionCallback localSubscriptionCallback;
      do
      {
        MediaBrowserCompat.Subscription localSubscription;
        while (true)
        {
          return;
          if (MediaBrowserCompat.DEBUG)
            Log.d("MediaBrowserCompat", "onLoadChildren for " + this.mServiceComponent + " id=" + paramString);
          localSubscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(paramString);
          if (localSubscription != null)
            break;
          if (!MediaBrowserCompat.DEBUG)
            continue;
          Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + paramString);
          return;
        }
        localSubscriptionCallback = localSubscription.getCallback(paramBundle);
      }
      while (localSubscriptionCallback == null);
      if (paramBundle == null)
      {
        localSubscriptionCallback.onChildrenLoaded(paramString, paramList);
        return;
      }
      localSubscriptionCallback.onChildrenLoaded(paramString, paramList, paramBundle);
    }

    public void onServiceConnected(Messenger paramMessenger, String paramString, MediaSessionCompat.Token paramToken, Bundle paramBundle)
    {
      if (!isCurrent(paramMessenger, "onConnect"));
      while (true)
      {
        return;
        if (this.mState != 1)
        {
          Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
          return;
        }
        this.mRootId = paramString;
        this.mMediaSessionToken = paramToken;
        this.mExtras = paramBundle;
        this.mState = 2;
        if (MediaBrowserCompat.DEBUG)
        {
          Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
          dump();
        }
        this.mCallback.onConnected();
        try
        {
          Iterator localIterator = this.mSubscriptions.entrySet().iterator();
          while (localIterator.hasNext())
          {
            Map.Entry localEntry = (Map.Entry)localIterator.next();
            String str = (String)localEntry.getKey();
            MediaBrowserCompat.Subscription localSubscription = (MediaBrowserCompat.Subscription)localEntry.getValue();
            List localList1 = localSubscription.getCallbacks();
            List localList2 = localSubscription.getOptionsList();
            for (int i = 0; i < localList1.size(); i++)
              this.mServiceBinderWrapper.addSubscription(str, MediaBrowserCompat.SubscriptionCallback.access$000((MediaBrowserCompat.SubscriptionCallback)localList1.get(i)), (Bundle)localList2.get(i), this.mCallbacksMessenger);
          }
        }
        catch (RemoteException localRemoteException)
        {
          Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
        }
      }
    }

    public void subscribe(@NonNull String paramString, Bundle paramBundle, @NonNull MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      MediaBrowserCompat.Subscription localSubscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(paramString);
      if (localSubscription == null)
      {
        localSubscription = new MediaBrowserCompat.Subscription();
        this.mSubscriptions.put(paramString, localSubscription);
      }
      localSubscription.putCallback(paramBundle, paramSubscriptionCallback);
      if (this.mState == 2);
      try
      {
        this.mServiceBinderWrapper.addSubscription(paramString, MediaBrowserCompat.SubscriptionCallback.access$000(paramSubscriptionCallback), paramBundle, this.mCallbacksMessenger);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + paramString);
      }
    }

    public void unsubscribe(@NonNull String paramString, MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      MediaBrowserCompat.Subscription localSubscription = (MediaBrowserCompat.Subscription)this.mSubscriptions.get(paramString);
      if (localSubscription == null);
      while (true)
      {
        return;
        if (paramSubscriptionCallback == null);
        try
        {
          if (this.mState == 2)
            this.mServiceBinderWrapper.removeSubscription(paramString, null, this.mCallbacksMessenger);
          while ((localSubscription.isEmpty()) || (paramSubscriptionCallback == null))
          {
            this.mSubscriptions.remove(paramString);
            return;
            List localList1 = localSubscription.getCallbacks();
            List localList2 = localSubscription.getOptionsList();
            for (int i = -1 + localList1.size(); i >= 0; i--)
            {
              if (localList1.get(i) != paramSubscriptionCallback)
                continue;
              if (this.mState == 2)
                this.mServiceBinderWrapper.removeSubscription(paramString, MediaBrowserCompat.SubscriptionCallback.access$000(paramSubscriptionCallback), this.mCallbacksMessenger);
              localList1.remove(i);
              localList2.remove(i);
            }
          }
        }
        catch (RemoteException localRemoteException)
        {
          while (true)
            Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + paramString);
        }
      }
    }

    private class MediaServiceConnection
      implements ServiceConnection
    {
      MediaServiceConnection()
      {
      }

      private void postOrRun(Runnable paramRunnable)
      {
        if (Thread.currentThread() == MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.getLooper().getThread())
        {
          paramRunnable.run();
          return;
        }
        MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.post(paramRunnable);
      }

      boolean isCurrent(String paramString)
      {
        if (MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection != this)
        {
          if (MediaBrowserCompat.MediaBrowserImplBase.this.mState != 0)
            Log.i("MediaBrowserCompat", paramString + " for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection + " this=" + this);
          return false;
        }
        return true;
      }

      public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
      {
        postOrRun(new Runnable(paramComponentName, paramIBinder)
        {
          public void run()
          {
            if (MediaBrowserCompat.DEBUG)
            {
              Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + this.val$name + " binder=" + this.val$binder);
              MediaBrowserCompat.MediaBrowserImplBase.this.dump();
            }
            if (!MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection.this.isCurrent("onServiceConnected"));
            do
            {
              return;
              MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper = new MediaBrowserCompat.ServiceBinderWrapper(this.val$binder, MediaBrowserCompat.MediaBrowserImplBase.this.mRootHints);
              MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger(MediaBrowserCompat.MediaBrowserImplBase.this.mHandler);
              MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger);
              MediaBrowserCompat.MediaBrowserImplBase.this.mState = 1;
              try
              {
                if (MediaBrowserCompat.DEBUG)
                {
                  Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                  MediaBrowserCompat.MediaBrowserImplBase.this.dump();
                }
                MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserCompat.MediaBrowserImplBase.this.mContext, MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger);
                return;
              }
              catch (RemoteException localRemoteException)
              {
                Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent);
              }
            }
            while (!MediaBrowserCompat.DEBUG);
            Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
            MediaBrowserCompat.MediaBrowserImplBase.this.dump();
          }
        });
      }

      public void onServiceDisconnected(ComponentName paramComponentName)
      {
        postOrRun(new Runnable(paramComponentName)
        {
          public void run()
          {
            if (MediaBrowserCompat.DEBUG)
            {
              Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + this.val$name + " this=" + this + " mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection);
              MediaBrowserCompat.MediaBrowserImplBase.this.dump();
            }
            if (!MediaBrowserCompat.MediaBrowserImplBase.MediaServiceConnection.this.isCurrent("onServiceDisconnected"))
              return;
            MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinderWrapper = null;
            MediaBrowserCompat.MediaBrowserImplBase.this.mCallbacksMessenger = null;
            MediaBrowserCompat.MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(null);
            MediaBrowserCompat.MediaBrowserImplBase.this.mState = 3;
            MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
          }
        });
      }
    }
  }

  static abstract interface MediaBrowserServiceCallbackImpl
  {
    public abstract void onConnectionFailed(Messenger paramMessenger);

    public abstract void onLoadChildren(Messenger paramMessenger, String paramString, List paramList, Bundle paramBundle);

    public abstract void onServiceConnected(Messenger paramMessenger, String paramString, MediaSessionCompat.Token paramToken, Bundle paramBundle);
  }

  public static class MediaItem
    implements Parcelable
  {
    public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator()
    {
      public MediaBrowserCompat.MediaItem createFromParcel(Parcel paramParcel)
      {
        return new MediaBrowserCompat.MediaItem(paramParcel);
      }

      public MediaBrowserCompat.MediaItem[] newArray(int paramInt)
      {
        return new MediaBrowserCompat.MediaItem[paramInt];
      }
    };
    public static final int FLAG_BROWSABLE = 1;
    public static final int FLAG_PLAYABLE = 2;
    private final MediaDescriptionCompat mDescription;
    private final int mFlags;

    MediaItem(Parcel paramParcel)
    {
      this.mFlags = paramParcel.readInt();
      this.mDescription = ((MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(paramParcel));
    }

    public MediaItem(@NonNull MediaDescriptionCompat paramMediaDescriptionCompat, int paramInt)
    {
      if (paramMediaDescriptionCompat == null)
        throw new IllegalArgumentException("description cannot be null");
      if (TextUtils.isEmpty(paramMediaDescriptionCompat.getMediaId()))
        throw new IllegalArgumentException("description must have a non-empty media id");
      this.mFlags = paramInt;
      this.mDescription = paramMediaDescriptionCompat;
    }

    public static MediaItem fromMediaItem(Object paramObject)
    {
      if ((paramObject == null) || (Build.VERSION.SDK_INT < 21))
        return null;
      int i = MediaBrowserCompatApi21.MediaItem.getFlags(paramObject);
      return new MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(paramObject)), i);
    }

    public static List<MediaItem> fromMediaItemList(List<?> paramList)
    {
      ArrayList localArrayList;
      if ((paramList == null) || (Build.VERSION.SDK_INT < 21))
        localArrayList = null;
      while (true)
      {
        return localArrayList;
        localArrayList = new ArrayList(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
          localArrayList.add(fromMediaItem(localIterator.next()));
      }
    }

    public int describeContents()
    {
      return 0;
    }

    @NonNull
    public MediaDescriptionCompat getDescription()
    {
      return this.mDescription;
    }

    public int getFlags()
    {
      return this.mFlags;
    }

    @NonNull
    public String getMediaId()
    {
      return this.mDescription.getMediaId();
    }

    public boolean isBrowsable()
    {
      return (0x1 & this.mFlags) != 0;
    }

    public boolean isPlayable()
    {
      return (0x2 & this.mFlags) != 0;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("MediaItem{");
      localStringBuilder.append("mFlags=").append(this.mFlags);
      localStringBuilder.append(", mDescription=").append(this.mDescription);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(this.mFlags);
      this.mDescription.writeToParcel(paramParcel, paramInt);
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface Flags
    {
    }
  }

  private static class ServiceBinderWrapper
  {
    private Messenger mMessenger;
    private Bundle mRootHints;

    public ServiceBinderWrapper(IBinder paramIBinder, Bundle paramBundle)
    {
      this.mMessenger = new Messenger(paramIBinder);
      this.mRootHints = paramBundle;
    }

    private void sendRequest(int paramInt, Bundle paramBundle, Messenger paramMessenger)
      throws RemoteException
    {
      Message localMessage = Message.obtain();
      localMessage.what = paramInt;
      localMessage.arg1 = 1;
      localMessage.setData(paramBundle);
      localMessage.replyTo = paramMessenger;
      this.mMessenger.send(localMessage);
    }

    void addSubscription(String paramString, IBinder paramIBinder, Bundle paramBundle, Messenger paramMessenger)
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("data_media_item_id", paramString);
      BundleCompat.putBinder(localBundle, "data_callback_token", paramIBinder);
      localBundle.putBundle("data_options", paramBundle);
      sendRequest(3, localBundle, paramMessenger);
    }

    void connect(Context paramContext, Messenger paramMessenger)
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("data_package_name", paramContext.getPackageName());
      localBundle.putBundle("data_root_hints", this.mRootHints);
      sendRequest(1, localBundle, paramMessenger);
    }

    void disconnect(Messenger paramMessenger)
      throws RemoteException
    {
      sendRequest(2, null, paramMessenger);
    }

    void getMediaItem(String paramString, ResultReceiver paramResultReceiver, Messenger paramMessenger)
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("data_media_item_id", paramString);
      localBundle.putParcelable("data_result_receiver", paramResultReceiver);
      sendRequest(5, localBundle, paramMessenger);
    }

    void registerCallbackMessenger(Messenger paramMessenger)
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putBundle("data_root_hints", this.mRootHints);
      sendRequest(6, localBundle, paramMessenger);
    }

    void removeSubscription(String paramString, IBinder paramIBinder, Messenger paramMessenger)
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("data_media_item_id", paramString);
      BundleCompat.putBinder(localBundle, "data_callback_token", paramIBinder);
      sendRequest(4, localBundle, paramMessenger);
    }

    void unregisterCallbackMessenger(Messenger paramMessenger)
      throws RemoteException
    {
      sendRequest(7, null, paramMessenger);
    }
  }

  private static class Subscription
  {
    private final List<MediaBrowserCompat.SubscriptionCallback> mCallbacks = new ArrayList();
    private final List<Bundle> mOptionsList = new ArrayList();

    public MediaBrowserCompat.SubscriptionCallback getCallback(Bundle paramBundle)
    {
      for (int i = 0; i < this.mOptionsList.size(); i++)
        if (MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(i), paramBundle))
          return (MediaBrowserCompat.SubscriptionCallback)this.mCallbacks.get(i);
      return null;
    }

    public List<MediaBrowserCompat.SubscriptionCallback> getCallbacks()
    {
      return this.mCallbacks;
    }

    public List<Bundle> getOptionsList()
    {
      return this.mOptionsList;
    }

    public boolean isEmpty()
    {
      return this.mCallbacks.isEmpty();
    }

    public void putCallback(Bundle paramBundle, MediaBrowserCompat.SubscriptionCallback paramSubscriptionCallback)
    {
      for (int i = 0; i < this.mOptionsList.size(); i++)
      {
        if (!MediaBrowserCompatUtils.areSameOptions((Bundle)this.mOptionsList.get(i), paramBundle))
          continue;
        this.mCallbacks.set(i, paramSubscriptionCallback);
        return;
      }
      this.mCallbacks.add(paramSubscriptionCallback);
      this.mOptionsList.add(paramBundle);
    }
  }

  public static abstract class SubscriptionCallback
  {
    private final Object mSubscriptionCallbackObj;
    WeakReference<MediaBrowserCompat.Subscription> mSubscriptionRef;
    private final IBinder mToken;

    public SubscriptionCallback()
    {
      if ((Build.VERSION.SDK_INT >= 24) || (BuildCompat.isAtLeastN()))
      {
        this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback(new StubApi24());
        this.mToken = null;
        return;
      }
      if (Build.VERSION.SDK_INT >= 21)
      {
        this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new StubApi21());
        this.mToken = new Binder();
        return;
      }
      this.mSubscriptionCallbackObj = null;
      this.mToken = new Binder();
    }

    private void setSubscription(MediaBrowserCompat.Subscription paramSubscription)
    {
      this.mSubscriptionRef = new WeakReference(paramSubscription);
    }

    public void onChildrenLoaded(@NonNull String paramString, List<MediaBrowserCompat.MediaItem> paramList)
    {
    }

    public void onChildrenLoaded(@NonNull String paramString, List<MediaBrowserCompat.MediaItem> paramList, @NonNull Bundle paramBundle)
    {
    }

    public void onError(@NonNull String paramString)
    {
    }

    public void onError(@NonNull String paramString, @NonNull Bundle paramBundle)
    {
    }

    private class StubApi21
      implements MediaBrowserCompatApi21.SubscriptionCallback
    {
      StubApi21()
      {
      }

      List<MediaBrowserCompat.MediaItem> applyOptions(List<MediaBrowserCompat.MediaItem> paramList, Bundle paramBundle)
      {
        if (paramList == null)
          paramList = null;
        int i;
        int j;
        do
        {
          return paramList;
          i = paramBundle.getInt("android.media.browse.extra.PAGE", -1);
          j = paramBundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        while ((i == -1) && (j == -1));
        int k = j * i;
        int m = k + j;
        if ((i < 0) || (j < 1) || (k >= paramList.size()))
          return Collections.EMPTY_LIST;
        if (m > paramList.size())
          m = paramList.size();
        return paramList.subList(k, m);
      }

      public void onChildrenLoaded(@NonNull String paramString, List<?> paramList)
      {
        if (MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef == null);
        for (MediaBrowserCompat.Subscription localSubscription = null; localSubscription == null; localSubscription = (MediaBrowserCompat.Subscription)MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef.get())
        {
          MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(paramString, MediaBrowserCompat.MediaItem.fromMediaItemList(paramList));
          return;
        }
        List localList1 = MediaBrowserCompat.MediaItem.fromMediaItemList(paramList);
        List localList2 = localSubscription.getCallbacks();
        List localList3 = localSubscription.getOptionsList();
        int i = 0;
        label67: Bundle localBundle;
        if (i < localList2.size())
        {
          localBundle = (Bundle)localList3.get(i);
          if (localBundle != null)
            break label114;
          MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(paramString, localList1);
        }
        while (true)
        {
          i++;
          break label67;
          break;
          label114: MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(paramString, applyOptions(localList1, localBundle), localBundle);
        }
      }

      public void onError(@NonNull String paramString)
      {
        MediaBrowserCompat.SubscriptionCallback.this.onError(paramString);
      }
    }

    private class StubApi24 extends MediaBrowserCompat.SubscriptionCallback.StubApi21
      implements MediaBrowserCompatApi24.SubscriptionCallback
    {
      StubApi24()
      {
        super();
      }

      public void onChildrenLoaded(@NonNull String paramString, List<?> paramList, @NonNull Bundle paramBundle)
      {
        MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(paramString, MediaBrowserCompat.MediaItem.fromMediaItemList(paramList), paramBundle);
      }

      public void onError(@NonNull String paramString, @NonNull Bundle paramBundle)
      {
        MediaBrowserCompat.SubscriptionCallback.this.onError(paramString, paramBundle);
      }
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaBrowserCompat
 * JD-Core Version:    0.6.0
 */