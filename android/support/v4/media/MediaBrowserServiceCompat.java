package android.support.v4.media;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class MediaBrowserServiceCompat extends Service
{
  static final boolean DEBUG = false;

  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static final String KEY_MEDIA_ITEM = "media_item";
  static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
  static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
  public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
  static final String TAG = "MBServiceCompat";
  final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
  ConnectionRecord mCurConnection;
  final ServiceHandler mHandler = new ServiceHandler();
  private MediaBrowserServiceImpl mImpl;
  MediaSessionCompat.Token mSession;

  void addSubscription(String paramString, ConnectionRecord paramConnectionRecord, IBinder paramIBinder, Bundle paramBundle)
  {
    List localList = (List)paramConnectionRecord.subscriptions.get(paramString);
    if (localList == null);
    for (Object localObject = new ArrayList(); ; localObject = localList)
    {
      Iterator localIterator = ((List)localObject).iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramIBinder == localPair.first) && (MediaBrowserCompatUtils.areSameOptions(paramBundle, (Bundle)localPair.second)))
          return;
      }
      ((List)localObject).add(new Pair(paramIBinder, paramBundle));
      paramConnectionRecord.subscriptions.put(paramString, localObject);
      performLoadChildren(paramString, paramConnectionRecord, paramBundle);
      return;
    }
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

  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
  }

  public final Bundle getBrowserRootHints()
  {
    return this.mImpl.getBrowserRootHints();
  }

  @Nullable
  public MediaSessionCompat.Token getSessionToken()
  {
    return this.mSession;
  }

  boolean isValidPackage(String paramString, int paramInt)
  {
    if (paramString == null);
    while (true)
    {
      return false;
      String[] arrayOfString = getPackageManager().getPackagesForUid(paramInt);
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
        if (arrayOfString[j].equals(paramString))
          return true;
    }
  }

  public void notifyChildrenChanged(@NonNull String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    this.mImpl.notifyChildrenChanged(paramString, null);
  }

  public void notifyChildrenChanged(@NonNull String paramString, @NonNull Bundle paramBundle)
  {
    if (paramString == null)
      throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
    if (paramBundle == null)
      throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
    this.mImpl.notifyChildrenChanged(paramString, paramBundle);
  }

  public IBinder onBind(Intent paramIntent)
  {
    return this.mImpl.onBind(paramIntent);
  }

  public void onCreate()
  {
    super.onCreate();
    if ((Build.VERSION.SDK_INT >= 24) || (BuildCompat.isAtLeastN()))
      this.mImpl = new MediaBrowserServiceImplApi24();
    while (true)
    {
      this.mImpl.onCreate();
      return;
      if (Build.VERSION.SDK_INT >= 23)
      {
        this.mImpl = new MediaBrowserServiceImplApi23();
        continue;
      }
      if (Build.VERSION.SDK_INT >= 21)
      {
        this.mImpl = new MediaBrowserServiceImplApi21();
        continue;
      }
      this.mImpl = new MediaBrowserServiceImplBase();
    }
  }

  @Nullable
  public abstract BrowserRoot onGetRoot(@NonNull String paramString, int paramInt, @Nullable Bundle paramBundle);

  public abstract void onLoadChildren(@NonNull String paramString, @NonNull Result<List<MediaBrowserCompat.MediaItem>> paramResult);

  public void onLoadChildren(@NonNull String paramString, @NonNull Result<List<MediaBrowserCompat.MediaItem>> paramResult, @NonNull Bundle paramBundle)
  {
    paramResult.setFlags(1);
    onLoadChildren(paramString, paramResult);
  }

  public void onLoadItem(String paramString, Result<MediaBrowserCompat.MediaItem> paramResult)
  {
    paramResult.setFlags(2);
    paramResult.sendResult(null);
  }

  void performLoadChildren(String paramString, ConnectionRecord paramConnectionRecord, Bundle paramBundle)
  {
    1 local1 = new Result(paramString, paramConnectionRecord, paramString, paramBundle)
    {
      void onResultSent(List<MediaBrowserCompat.MediaItem> paramList, int paramInt)
      {
        if (MediaBrowserServiceCompat.this.mConnections.get(this.val$connection.callbacks.asBinder()) != this.val$connection)
        {
          if (MediaBrowserServiceCompat.DEBUG)
            Log.d("MBServiceCompat", "Not sending onLoadChildren result for connection that has been disconnected. pkg=" + this.val$connection.pkg + " id=" + this.val$parentId);
          return;
        }
        if ((paramInt & 0x1) != 0)
          paramList = MediaBrowserServiceCompat.this.applyOptions(paramList, this.val$options);
        try
        {
          this.val$connection.callbacks.onLoadChildren(this.val$parentId, paramList, this.val$options);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("MBServiceCompat", "Calling onLoadChildren() failed for id=" + this.val$parentId + " package=" + this.val$connection.pkg);
        }
      }
    };
    this.mCurConnection = paramConnectionRecord;
    if (paramBundle == null)
      onLoadChildren(paramString, local1);
    while (true)
    {
      this.mCurConnection = null;
      if (local1.isDone())
        break;
      throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + paramConnectionRecord.pkg + " id=" + paramString);
      onLoadChildren(paramString, local1, paramBundle);
    }
  }

  void performLoadItem(String paramString, ConnectionRecord paramConnectionRecord, ResultReceiver paramResultReceiver)
  {
    2 local2 = new Result(paramString, paramResultReceiver)
    {
      void onResultSent(MediaBrowserCompat.MediaItem paramMediaItem, int paramInt)
      {
        if ((paramInt & 0x2) != 0)
        {
          this.val$receiver.send(-1, null);
          return;
        }
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("media_item", paramMediaItem);
        this.val$receiver.send(0, localBundle);
      }
    };
    this.mCurConnection = paramConnectionRecord;
    onLoadItem(paramString, local2);
    this.mCurConnection = null;
    if (!local2.isDone())
      throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + paramString);
  }

  boolean removeSubscription(String paramString, ConnectionRecord paramConnectionRecord, IBinder paramIBinder)
  {
    if (paramIBinder == null)
      return paramConnectionRecord.subscriptions.remove(paramString) != null;
    List localList = (List)paramConnectionRecord.subscriptions.get(paramString);
    if (localList != null)
    {
      Iterator localIterator = localList.iterator();
      int i = 0;
      while (localIterator.hasNext())
      {
        if (paramIBinder != ((Pair)localIterator.next()).first)
          continue;
        localIterator.remove();
        i = 1;
      }
      if (localList.size() == 0)
      {
        paramConnectionRecord.subscriptions.remove(paramString);
        return i;
      }
      return i;
    }
    return false;
  }

  public void setSessionToken(MediaSessionCompat.Token paramToken)
  {
    if (paramToken == null)
      throw new IllegalArgumentException("Session token may not be null.");
    if (this.mSession != null)
      throw new IllegalStateException("The session token has already been set.");
    this.mSession = paramToken;
    this.mImpl.setSessionToken(paramToken);
  }

  public static final class BrowserRoot
  {
    public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
    public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
    public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
    public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
    private final Bundle mExtras;
    private final String mRootId;

    public BrowserRoot(@NonNull String paramString, @Nullable Bundle paramBundle)
    {
      if (paramString == null)
        throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
      this.mRootId = paramString;
      this.mExtras = paramBundle;
    }

    public Bundle getExtras()
    {
      return this.mExtras;
    }

    public String getRootId()
    {
      return this.mRootId;
    }
  }

  private class ConnectionRecord
  {
    MediaBrowserServiceCompat.ServiceCallbacks callbacks;
    String pkg;
    MediaBrowserServiceCompat.BrowserRoot root;
    Bundle rootHints;
    HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions = new HashMap();

    ConnectionRecord()
    {
    }
  }

  static abstract interface MediaBrowserServiceImpl
  {
    public abstract Bundle getBrowserRootHints();

    public abstract void notifyChildrenChanged(String paramString, Bundle paramBundle);

    public abstract IBinder onBind(Intent paramIntent);

    public abstract void onCreate();

    public abstract void setSessionToken(MediaSessionCompat.Token paramToken);
  }

  class MediaBrowserServiceImplApi21
    implements MediaBrowserServiceCompat.MediaBrowserServiceImpl, MediaBrowserServiceCompatApi21.ServiceCompatProxy
  {
    Messenger mMessenger;
    Object mServiceObj;

    MediaBrowserServiceImplApi21()
    {
    }

    public Bundle getBrowserRootHints()
    {
      if (this.mMessenger == null);
      do
      {
        return null;
        if (MediaBrowserServiceCompat.this.mCurConnection != null)
          continue;
        throw new IllegalStateException("This should be called inside of onLoadChildren or onLoadItem methods");
      }
      while (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null);
      return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
    }

    public void notifyChildrenChanged(String paramString, Bundle paramBundle)
    {
      if (this.mMessenger == null)
      {
        MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, paramString);
        return;
      }
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable(paramString, paramBundle)
      {
        public void run()
        {
          Iterator localIterator1 = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
          while (localIterator1.hasNext())
          {
            IBinder localIBinder = (IBinder)localIterator1.next();
            MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(localIBinder);
            List localList = (List)localConnectionRecord.subscriptions.get(this.val$parentId);
            if (localList == null)
              continue;
            Iterator localIterator2 = localList.iterator();
            while (localIterator2.hasNext())
            {
              Pair localPair = (Pair)localIterator2.next();
              if (!MediaBrowserCompatUtils.hasDuplicatedItems(this.val$options, (Bundle)localPair.second))
                continue;
              MediaBrowserServiceCompat.this.performLoadChildren(this.val$parentId, localConnectionRecord, (Bundle)localPair.second);
            }
          }
        }
      });
    }

    public IBinder onBind(Intent paramIntent)
    {
      return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, paramIntent);
    }

    public void onCreate()
    {
      this.mServiceObj = MediaBrowserServiceCompatApi21.createService(MediaBrowserServiceCompat.this, this);
      MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
    }

    public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(String paramString, int paramInt, Bundle paramBundle)
    {
      Bundle localBundle;
      if ((paramBundle != null) && (paramBundle.getInt("extra_client_version", 0) != 0))
      {
        paramBundle.remove("extra_client_version");
        this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
        localBundle = new Bundle();
        localBundle.putInt("extra_service_version", 1);
        BundleCompat.putBinder(localBundle, "extra_messenger", this.mMessenger.getBinder());
      }
      while (true)
      {
        MediaBrowserServiceCompat.BrowserRoot localBrowserRoot = MediaBrowserServiceCompat.this.onGetRoot(paramString, paramInt, paramBundle);
        if (localBrowserRoot == null)
          return null;
        if (localBundle == null)
          localBundle = localBrowserRoot.getExtras();
        while (true)
        {
          return new MediaBrowserServiceCompatApi21.BrowserRoot(localBrowserRoot.getRootId(), localBundle);
          if (localBrowserRoot.getExtras() == null)
            continue;
          localBundle.putAll(localBrowserRoot.getExtras());
        }
        localBundle = null;
      }
    }

    public void onLoadChildren(String paramString, MediaBrowserServiceCompatApi21.ResultWrapper<List<Parcel>> paramResultWrapper)
    {
      2 local2 = new MediaBrowserServiceCompat.Result(paramString, paramResultWrapper)
      {
        public void detach()
        {
          this.val$resultWrapper.detach();
        }

        void onResultSent(List<MediaBrowserCompat.MediaItem> paramList, int paramInt)
        {
          Object localObject = null;
          if (paramList != null)
          {
            ArrayList localArrayList = new ArrayList();
            Iterator localIterator = paramList.iterator();
            while (localIterator.hasNext())
            {
              MediaBrowserCompat.MediaItem localMediaItem = (MediaBrowserCompat.MediaItem)localIterator.next();
              Parcel localParcel = Parcel.obtain();
              localMediaItem.writeToParcel(localParcel, 0);
              localArrayList.add(localParcel);
            }
            localObject = localArrayList;
          }
          this.val$resultWrapper.sendResult(localObject);
        }
      };
      MediaBrowserServiceCompat.this.onLoadChildren(paramString, local2);
    }

    public void setSessionToken(MediaSessionCompat.Token paramToken)
    {
      MediaBrowserServiceCompatApi21.setSessionToken(this.mServiceObj, paramToken.getToken());
    }
  }

  class MediaBrowserServiceImplApi23 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi21
    implements MediaBrowserServiceCompatApi23.ServiceCompatProxy
  {
    MediaBrowserServiceImplApi23()
    {
      super();
    }

    public void onCreate()
    {
      this.mServiceObj = MediaBrowserServiceCompatApi23.createService(MediaBrowserServiceCompat.this, this);
      MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
    }

    public void onLoadItem(String paramString, MediaBrowserServiceCompatApi21.ResultWrapper<Parcel> paramResultWrapper)
    {
      1 local1 = new MediaBrowserServiceCompat.Result(paramString, paramResultWrapper)
      {
        public void detach()
        {
          this.val$resultWrapper.detach();
        }

        void onResultSent(MediaBrowserCompat.MediaItem paramMediaItem, int paramInt)
        {
          if (paramMediaItem == null)
          {
            this.val$resultWrapper.sendResult(null);
            return;
          }
          Parcel localParcel = Parcel.obtain();
          paramMediaItem.writeToParcel(localParcel, 0);
          this.val$resultWrapper.sendResult(localParcel);
        }
      };
      MediaBrowserServiceCompat.this.onLoadItem(paramString, local1);
    }
  }

  class MediaBrowserServiceImplApi24 extends MediaBrowserServiceCompat.MediaBrowserServiceImplApi23
    implements MediaBrowserServiceCompatApi24.ServiceCompatProxy
  {
    MediaBrowserServiceImplApi24()
    {
      super();
    }

    public Bundle getBrowserRootHints()
    {
      if (MediaBrowserServiceCompat.this.mCurConnection != null)
      {
        if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null)
          return null;
        return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
      }
      return MediaBrowserServiceCompatApi24.getBrowserRootHints(this.mServiceObj);
    }

    public void notifyChildrenChanged(String paramString, Bundle paramBundle)
    {
      if (paramBundle == null)
      {
        MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, paramString);
        return;
      }
      MediaBrowserServiceCompatApi24.notifyChildrenChanged(this.mServiceObj, paramString, paramBundle);
    }

    public void onCreate()
    {
      this.mServiceObj = MediaBrowserServiceCompatApi24.createService(MediaBrowserServiceCompat.this, this);
      MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj);
    }

    public void onLoadChildren(String paramString, MediaBrowserServiceCompatApi24.ResultWrapper paramResultWrapper, Bundle paramBundle)
    {
      1 local1 = new MediaBrowserServiceCompat.Result(paramString, paramResultWrapper)
      {
        public void detach()
        {
          this.val$resultWrapper.detach();
        }

        void onResultSent(List<MediaBrowserCompat.MediaItem> paramList, int paramInt)
        {
          Object localObject = null;
          if (paramList != null)
          {
            ArrayList localArrayList = new ArrayList();
            Iterator localIterator = paramList.iterator();
            while (localIterator.hasNext())
            {
              MediaBrowserCompat.MediaItem localMediaItem = (MediaBrowserCompat.MediaItem)localIterator.next();
              Parcel localParcel = Parcel.obtain();
              localMediaItem.writeToParcel(localParcel, 0);
              localArrayList.add(localParcel);
            }
            localObject = localArrayList;
          }
          this.val$resultWrapper.sendResult(localObject, paramInt);
        }
      };
      MediaBrowserServiceCompat.this.onLoadChildren(paramString, local1, paramBundle);
    }
  }

  class MediaBrowserServiceImplBase
    implements MediaBrowserServiceCompat.MediaBrowserServiceImpl
  {
    private Messenger mMessenger;

    MediaBrowserServiceImplBase()
    {
    }

    public Bundle getBrowserRootHints()
    {
      if (MediaBrowserServiceCompat.this.mCurConnection == null)
        throw new IllegalStateException("This should be called inside of onLoadChildren or onLoadItem methods");
      if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null)
        return null;
      return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
    }

    public void notifyChildrenChanged(@NonNull String paramString, Bundle paramBundle)
    {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable(paramString, paramBundle)
      {
        public void run()
        {
          Iterator localIterator1 = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
          while (localIterator1.hasNext())
          {
            IBinder localIBinder = (IBinder)localIterator1.next();
            MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(localIBinder);
            List localList = (List)localConnectionRecord.subscriptions.get(this.val$parentId);
            if (localList == null)
              continue;
            Iterator localIterator2 = localList.iterator();
            while (localIterator2.hasNext())
            {
              Pair localPair = (Pair)localIterator2.next();
              if (!MediaBrowserCompatUtils.hasDuplicatedItems(this.val$options, (Bundle)localPair.second))
                continue;
              MediaBrowserServiceCompat.this.performLoadChildren(this.val$parentId, localConnectionRecord, (Bundle)localPair.second);
            }
          }
        }
      });
    }

    public IBinder onBind(Intent paramIntent)
    {
      if ("android.media.browse.MediaBrowserService".equals(paramIntent.getAction()))
        return this.mMessenger.getBinder();
      return null;
    }

    public void onCreate()
    {
      this.mMessenger = new Messenger(MediaBrowserServiceCompat.this.mHandler);
    }

    public void setSessionToken(MediaSessionCompat.Token paramToken)
    {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable(paramToken)
      {
        public void run()
        {
          Iterator localIterator = MediaBrowserServiceCompat.this.mConnections.values().iterator();
          while (localIterator.hasNext())
          {
            MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)localIterator.next();
            try
            {
              localConnectionRecord.callbacks.onConnect(localConnectionRecord.root.getRootId(), this.val$token, localConnectionRecord.root.getExtras());
            }
            catch (RemoteException localRemoteException)
            {
              Log.w("MBServiceCompat", "Connection for " + localConnectionRecord.pkg + " is no longer valid.");
              localIterator.remove();
            }
          }
        }
      });
    }
  }

  public static class Result<T>
  {
    private Object mDebug;
    private boolean mDetachCalled;
    private int mFlags;
    private boolean mSendResultCalled;

    Result(Object paramObject)
    {
      this.mDebug = paramObject;
    }

    public void detach()
    {
      if (this.mDetachCalled)
        throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug);
      if (this.mSendResultCalled)
        throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug);
      this.mDetachCalled = true;
    }

    boolean isDone()
    {
      return (this.mDetachCalled) || (this.mSendResultCalled);
    }

    void onResultSent(T paramT, int paramInt)
    {
    }

    public void sendResult(T paramT)
    {
      if (this.mSendResultCalled)
        throw new IllegalStateException("sendResult() called twice for: " + this.mDebug);
      this.mSendResultCalled = true;
      onResultSent(paramT, this.mFlags);
    }

    void setFlags(int paramInt)
    {
      this.mFlags = paramInt;
    }
  }

  private class ServiceBinderImpl
  {
    ServiceBinderImpl()
    {
    }

    public void addSubscription(String paramString, IBinder paramIBinder, Bundle paramBundle, MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks)
    {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks, paramString, paramIBinder, paramBundle)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(localIBinder);
          if (localConnectionRecord == null)
          {
            Log.w("MBServiceCompat", "addSubscription for callback that isn't registered id=" + this.val$id);
            return;
          }
          MediaBrowserServiceCompat.this.addSubscription(this.val$id, localConnectionRecord, this.val$token, this.val$options);
        }
      });
    }

    public void connect(String paramString, int paramInt, Bundle paramBundle, MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks)
    {
      if (!MediaBrowserServiceCompat.this.isValidPackage(paramString, paramInt))
        throw new IllegalArgumentException("Package/uid mismatch: uid=" + paramInt + " package=" + paramString);
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks, paramString, paramBundle, paramInt)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          MediaBrowserServiceCompat.this.mConnections.remove(localIBinder);
          MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = new MediaBrowserServiceCompat.ConnectionRecord(MediaBrowserServiceCompat.this);
          localConnectionRecord.pkg = this.val$pkg;
          localConnectionRecord.rootHints = this.val$rootHints;
          localConnectionRecord.callbacks = this.val$callbacks;
          localConnectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(this.val$pkg, this.val$uid, this.val$rootHints);
          if (localConnectionRecord.root == null)
            Log.i("MBServiceCompat", "No root for client " + this.val$pkg + " from service " + getClass().getName());
          while (true)
          {
            try
            {
              this.val$callbacks.onConnectFailed();
              return;
            }
            catch (RemoteException localRemoteException2)
            {
              Log.w("MBServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + this.val$pkg);
              return;
            }
            try
            {
              MediaBrowserServiceCompat.this.mConnections.put(localIBinder, localConnectionRecord);
              if (MediaBrowserServiceCompat.this.mSession == null)
                continue;
              this.val$callbacks.onConnect(localConnectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, localConnectionRecord.root.getExtras());
              return;
            }
            catch (RemoteException localRemoteException1)
            {
              Log.w("MBServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + this.val$pkg);
              MediaBrowserServiceCompat.this.mConnections.remove(localIBinder);
            }
          }
        }
      });
    }

    public void disconnect(MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks)
    {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          if ((MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(localIBinder) != null);
        }
      });
    }

    public void getMediaItem(String paramString, ResultReceiver paramResultReceiver, MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks)
    {
      if ((TextUtils.isEmpty(paramString)) || (paramResultReceiver == null))
        return;
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks, paramString, paramResultReceiver)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(localIBinder);
          if (localConnectionRecord == null)
          {
            Log.w("MBServiceCompat", "getMediaItem for callback that isn't registered id=" + this.val$mediaId);
            return;
          }
          MediaBrowserServiceCompat.this.performLoadItem(this.val$mediaId, localConnectionRecord, this.val$receiver);
        }
      });
    }

    public void registerCallbacks(MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks, Bundle paramBundle)
    {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks, paramBundle)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          MediaBrowserServiceCompat.this.mConnections.remove(localIBinder);
          MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = new MediaBrowserServiceCompat.ConnectionRecord(MediaBrowserServiceCompat.this);
          localConnectionRecord.callbacks = this.val$callbacks;
          localConnectionRecord.rootHints = this.val$rootHints;
          MediaBrowserServiceCompat.this.mConnections.put(localIBinder, localConnectionRecord);
        }
      });
    }

    public void removeSubscription(String paramString, IBinder paramIBinder, MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks)
    {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks, paramString, paramIBinder)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          MediaBrowserServiceCompat.ConnectionRecord localConnectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(localIBinder);
          if (localConnectionRecord == null)
            Log.w("MBServiceCompat", "removeSubscription for callback that isn't registered id=" + this.val$id);
          do
            return;
          while (MediaBrowserServiceCompat.this.removeSubscription(this.val$id, localConnectionRecord, this.val$token));
          Log.w("MBServiceCompat", "removeSubscription called for " + this.val$id + " which is not subscribed");
        }
      });
    }

    public void unregisterCallbacks(MediaBrowserServiceCompat.ServiceCallbacks paramServiceCallbacks)
    {
      MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable(paramServiceCallbacks)
      {
        public void run()
        {
          IBinder localIBinder = this.val$callbacks.asBinder();
          MediaBrowserServiceCompat.this.mConnections.remove(localIBinder);
        }
      });
    }
  }

  private static abstract interface ServiceCallbacks
  {
    public abstract IBinder asBinder();

    public abstract void onConnect(String paramString, MediaSessionCompat.Token paramToken, Bundle paramBundle)
      throws RemoteException;

    public abstract void onConnectFailed()
      throws RemoteException;

    public abstract void onLoadChildren(String paramString, List<MediaBrowserCompat.MediaItem> paramList, Bundle paramBundle)
      throws RemoteException;
  }

  private class ServiceCallbacksCompat
    implements MediaBrowserServiceCompat.ServiceCallbacks
  {
    final Messenger mCallbacks;

    ServiceCallbacksCompat(Messenger arg2)
    {
      Object localObject;
      this.mCallbacks = localObject;
    }

    private void sendRequest(int paramInt, Bundle paramBundle)
      throws RemoteException
    {
      Message localMessage = Message.obtain();
      localMessage.what = paramInt;
      localMessage.arg1 = 1;
      localMessage.setData(paramBundle);
      this.mCallbacks.send(localMessage);
    }

    public IBinder asBinder()
    {
      return this.mCallbacks.getBinder();
    }

    public void onConnect(String paramString, MediaSessionCompat.Token paramToken, Bundle paramBundle)
      throws RemoteException
    {
      if (paramBundle == null)
        paramBundle = new Bundle();
      paramBundle.putInt("extra_service_version", 1);
      Bundle localBundle = new Bundle();
      localBundle.putString("data_media_item_id", paramString);
      localBundle.putParcelable("data_media_session_token", paramToken);
      localBundle.putBundle("data_root_hints", paramBundle);
      sendRequest(1, localBundle);
    }

    public void onConnectFailed()
      throws RemoteException
    {
      sendRequest(2, null);
    }

    public void onLoadChildren(String paramString, List<MediaBrowserCompat.MediaItem> paramList, Bundle paramBundle)
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("data_media_item_id", paramString);
      localBundle.putBundle("data_options", paramBundle);
      if (paramList != null)
        if (!(paramList instanceof ArrayList))
          break label59;
      label59: for (ArrayList localArrayList = (ArrayList)paramList; ; localArrayList = new ArrayList(paramList))
      {
        localBundle.putParcelableArrayList("data_media_item_list", localArrayList);
        sendRequest(3, localBundle);
        return;
      }
    }
  }

  private final class ServiceHandler extends Handler
  {
    private final MediaBrowserServiceCompat.ServiceBinderImpl mServiceBinderImpl = new MediaBrowserServiceCompat.ServiceBinderImpl(MediaBrowserServiceCompat.this);

    ServiceHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      Bundle localBundle = paramMessage.getData();
      switch (paramMessage.what)
      {
      default:
        Log.w("MBServiceCompat", "Unhandled message: " + paramMessage + "\n  Service version: " + 1 + "\n  Client version: " + paramMessage.arg1);
        return;
      case 1:
        this.mServiceBinderImpl.connect(localBundle.getString("data_package_name"), localBundle.getInt("data_calling_uid"), localBundle.getBundle("data_root_hints"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo));
        return;
      case 2:
        this.mServiceBinderImpl.disconnect(new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo));
        return;
      case 3:
        this.mServiceBinderImpl.addSubscription(localBundle.getString("data_media_item_id"), BundleCompat.getBinder(localBundle, "data_callback_token"), localBundle.getBundle("data_options"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo));
        return;
      case 4:
        this.mServiceBinderImpl.removeSubscription(localBundle.getString("data_media_item_id"), BundleCompat.getBinder(localBundle, "data_callback_token"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo));
        return;
      case 5:
        this.mServiceBinderImpl.getMediaItem(localBundle.getString("data_media_item_id"), (ResultReceiver)localBundle.getParcelable("data_result_receiver"), new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo));
        return;
      case 6:
        this.mServiceBinderImpl.registerCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo), localBundle.getBundle("data_root_hints"));
        return;
      case 7:
      }
      this.mServiceBinderImpl.unregisterCallbacks(new MediaBrowserServiceCompat.ServiceCallbacksCompat(MediaBrowserServiceCompat.this, paramMessage.replyTo));
    }

    public void postOrRun(Runnable paramRunnable)
    {
      if (Thread.currentThread() == getLooper().getThread())
      {
        paramRunnable.run();
        return;
      }
      post(paramRunnable);
    }

    public boolean sendMessageAtTime(Message paramMessage, long paramLong)
    {
      Bundle localBundle = paramMessage.getData();
      localBundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
      localBundle.putInt("data_calling_uid", Binder.getCallingUid());
      return super.sendMessageAtTime(paramMessage, paramLong);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaBrowserServiceCompat
 * JD-Core Version:    0.6.0
 */