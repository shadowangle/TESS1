package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCanceledListener;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl extends LoaderManager
{
  static boolean DEBUG = false;
  static final String TAG = "LoaderManager";
  boolean mCreatingLoader;
  FragmentHostCallback mHost;
  final SparseArrayCompat<LoaderInfo> mInactiveLoaders = new SparseArrayCompat();
  final SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat();
  boolean mRetaining;
  boolean mRetainingStarted;
  boolean mStarted;
  final String mWho;

  LoaderManagerImpl(String paramString, FragmentHostCallback paramFragmentHostCallback, boolean paramBoolean)
  {
    this.mWho = paramString;
    this.mHost = paramFragmentHostCallback;
    this.mStarted = paramBoolean;
  }

  private LoaderInfo createAndInstallLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
  {
    try
    {
      this.mCreatingLoader = true;
      LoaderInfo localLoaderInfo = createLoader(paramInt, paramBundle, paramLoaderCallbacks);
      installLoader(localLoaderInfo);
      return localLoaderInfo;
    }
    finally
    {
      this.mCreatingLoader = false;
    }
    throw localObject;
  }

  private LoaderInfo createLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
  {
    LoaderInfo localLoaderInfo = new LoaderInfo(paramInt, paramBundle, paramLoaderCallbacks);
    localLoaderInfo.mLoader = paramLoaderCallbacks.onCreateLoader(paramInt, paramBundle);
    return localLoaderInfo;
  }

  public void destroyLoader(int paramInt)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    if (DEBUG)
      Log.v("LoaderManager", "destroyLoader in " + this + " of " + paramInt);
    int i = this.mLoaders.indexOfKey(paramInt);
    if (i >= 0)
    {
      LoaderInfo localLoaderInfo2 = (LoaderInfo)this.mLoaders.valueAt(i);
      this.mLoaders.removeAt(i);
      localLoaderInfo2.destroy();
    }
    int j = this.mInactiveLoaders.indexOfKey(paramInt);
    if (j >= 0)
    {
      LoaderInfo localLoaderInfo1 = (LoaderInfo)this.mInactiveLoaders.valueAt(j);
      this.mInactiveLoaders.removeAt(j);
      localLoaderInfo1.destroy();
    }
    if ((this.mHost != null) && (!hasRunningLoaders()))
      this.mHost.mFragmentManager.startPendingDeferredFragments();
  }

  void doDestroy()
  {
    if (!this.mRetaining)
    {
      if (DEBUG)
        Log.v("LoaderManager", "Destroying Active in " + this);
      for (int j = -1 + this.mLoaders.size(); j >= 0; j--)
        ((LoaderInfo)this.mLoaders.valueAt(j)).destroy();
      this.mLoaders.clear();
    }
    if (DEBUG)
      Log.v("LoaderManager", "Destroying Inactive in " + this);
    for (int i = -1 + this.mInactiveLoaders.size(); i >= 0; i--)
      ((LoaderInfo)this.mInactiveLoaders.valueAt(i)).destroy();
    this.mInactiveLoaders.clear();
  }

  void doReportNextStart()
  {
    for (int i = -1 + this.mLoaders.size(); i >= 0; i--)
      ((LoaderInfo)this.mLoaders.valueAt(i)).mReportNextStart = true;
  }

  void doReportStart()
  {
    for (int i = -1 + this.mLoaders.size(); i >= 0; i--)
      ((LoaderInfo)this.mLoaders.valueAt(i)).reportStart();
  }

  void doRetain()
  {
    if (DEBUG)
      Log.v("LoaderManager", "Retaining in " + this);
    if (!this.mStarted)
    {
      RuntimeException localRuntimeException = new RuntimeException("here");
      localRuntimeException.fillInStackTrace();
      Log.w("LoaderManager", "Called doRetain when not started: " + this, localRuntimeException);
    }
    while (true)
    {
      return;
      this.mRetaining = true;
      this.mStarted = false;
      for (int i = -1 + this.mLoaders.size(); i >= 0; i--)
        ((LoaderInfo)this.mLoaders.valueAt(i)).retain();
    }
  }

  void doStart()
  {
    if (DEBUG)
      Log.v("LoaderManager", "Starting in " + this);
    if (this.mStarted)
    {
      RuntimeException localRuntimeException = new RuntimeException("here");
      localRuntimeException.fillInStackTrace();
      Log.w("LoaderManager", "Called doStart when already started: " + this, localRuntimeException);
    }
    while (true)
    {
      return;
      this.mStarted = true;
      for (int i = -1 + this.mLoaders.size(); i >= 0; i--)
        ((LoaderInfo)this.mLoaders.valueAt(i)).start();
    }
  }

  void doStop()
  {
    if (DEBUG)
      Log.v("LoaderManager", "Stopping in " + this);
    if (!this.mStarted)
    {
      RuntimeException localRuntimeException = new RuntimeException("here");
      localRuntimeException.fillInStackTrace();
      Log.w("LoaderManager", "Called doStop when not started: " + this, localRuntimeException);
      return;
    }
    for (int i = -1 + this.mLoaders.size(); i >= 0; i--)
      ((LoaderInfo)this.mLoaders.valueAt(i)).stop();
    this.mStarted = false;
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    int i = 0;
    if (this.mLoaders.size() > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Active Loaders:");
      String str2 = paramString + "    ";
      for (int j = 0; j < this.mLoaders.size(); j++)
      {
        LoaderInfo localLoaderInfo2 = (LoaderInfo)this.mLoaders.valueAt(j);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(this.mLoaders.keyAt(j));
        paramPrintWriter.print(": ");
        paramPrintWriter.println(localLoaderInfo2.toString());
        localLoaderInfo2.dump(str2, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    if (this.mInactiveLoaders.size() > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Inactive Loaders:");
      String str1 = paramString + "    ";
      while (i < this.mInactiveLoaders.size())
      {
        LoaderInfo localLoaderInfo1 = (LoaderInfo)this.mInactiveLoaders.valueAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(this.mInactiveLoaders.keyAt(i));
        paramPrintWriter.print(": ");
        paramPrintWriter.println(localLoaderInfo1.toString());
        localLoaderInfo1.dump(str1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        i++;
      }
    }
  }

  void finishRetain()
  {
    if (this.mRetaining)
    {
      if (DEBUG)
        Log.v("LoaderManager", "Finished Retaining in " + this);
      this.mRetaining = false;
      for (int i = -1 + this.mLoaders.size(); i >= 0; i--)
        ((LoaderInfo)this.mLoaders.valueAt(i)).finishRetain();
    }
  }

  public <D> Loader<D> getLoader(int paramInt)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    LoaderInfo localLoaderInfo = (LoaderInfo)this.mLoaders.get(paramInt);
    if (localLoaderInfo != null)
    {
      if (localLoaderInfo.mPendingLoader != null)
        return localLoaderInfo.mPendingLoader.mLoader;
      return localLoaderInfo.mLoader;
    }
    return null;
  }

  public boolean hasRunningLoaders()
  {
    int i = this.mLoaders.size();
    int j = 0;
    int k = 0;
    if (k < i)
    {
      LoaderInfo localLoaderInfo = (LoaderInfo)this.mLoaders.valueAt(k);
      if ((localLoaderInfo.mStarted) && (!localLoaderInfo.mDeliveredData));
      for (int m = 1; ; m = 0)
      {
        int n = m | j;
        k++;
        j = n;
        break;
      }
    }
    return j;
  }

  public <D> Loader<D> initLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<D> paramLoaderCallbacks)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    LoaderInfo localLoaderInfo = (LoaderInfo)this.mLoaders.get(paramInt);
    if (DEBUG)
      Log.v("LoaderManager", "initLoader in " + this + ": args=" + paramBundle);
    if (localLoaderInfo == null)
    {
      localLoaderInfo = createAndInstallLoader(paramInt, paramBundle, paramLoaderCallbacks);
      if (DEBUG)
        Log.v("LoaderManager", "  Created new loader " + localLoaderInfo);
    }
    while (true)
    {
      if ((localLoaderInfo.mHaveData) && (this.mStarted))
        localLoaderInfo.callOnLoadFinished(localLoaderInfo.mLoader, localLoaderInfo.mData);
      return localLoaderInfo.mLoader;
      if (DEBUG)
        Log.v("LoaderManager", "  Re-using existing loader " + localLoaderInfo);
      localLoaderInfo.mCallbacks = paramLoaderCallbacks;
    }
  }

  void installLoader(LoaderInfo paramLoaderInfo)
  {
    this.mLoaders.put(paramLoaderInfo.mId, paramLoaderInfo);
    if (this.mStarted)
      paramLoaderInfo.start();
  }

  public <D> Loader<D> restartLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<D> paramLoaderCallbacks)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    LoaderInfo localLoaderInfo1 = (LoaderInfo)this.mLoaders.get(paramInt);
    if (DEBUG)
      Log.v("LoaderManager", "restartLoader in " + this + ": args=" + paramBundle);
    if (localLoaderInfo1 != null)
    {
      LoaderInfo localLoaderInfo2 = (LoaderInfo)this.mInactiveLoaders.get(paramInt);
      if (localLoaderInfo2 == null)
        break label324;
      if (!localLoaderInfo1.mHaveData)
        break label175;
      if (DEBUG)
        Log.v("LoaderManager", "  Removing last inactive loader: " + localLoaderInfo1);
      localLoaderInfo2.mDeliveredData = false;
      localLoaderInfo2.destroy();
      localLoaderInfo1.mLoader.abandon();
      this.mInactiveLoaders.put(paramInt, localLoaderInfo1);
    }
    while (true)
    {
      return createAndInstallLoader(paramInt, paramBundle, paramLoaderCallbacks).mLoader;
      label175: if (!localLoaderInfo1.cancel())
      {
        if (DEBUG)
          Log.v("LoaderManager", "  Current loader is stopped; replacing");
        this.mLoaders.put(paramInt, null);
        localLoaderInfo1.destroy();
        continue;
      }
      if (DEBUG)
        Log.v("LoaderManager", "  Current loader is running; configuring pending loader");
      if (localLoaderInfo1.mPendingLoader != null)
      {
        if (DEBUG)
          Log.v("LoaderManager", "  Removing pending loader: " + localLoaderInfo1.mPendingLoader);
        localLoaderInfo1.mPendingLoader.destroy();
        localLoaderInfo1.mPendingLoader = null;
      }
      if (DEBUG)
        Log.v("LoaderManager", "  Enqueuing as new pending loader");
      localLoaderInfo1.mPendingLoader = createLoader(paramInt, paramBundle, paramLoaderCallbacks);
      return localLoaderInfo1.mPendingLoader.mLoader;
      label324: if (DEBUG)
        Log.v("LoaderManager", "  Making last loader inactive: " + localLoaderInfo1);
      localLoaderInfo1.mLoader.abandon();
      this.mInactiveLoaders.put(paramInt, localLoaderInfo1);
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("LoaderManager{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" in ");
    DebugUtils.buildShortClassTag(this.mHost, localStringBuilder);
    localStringBuilder.append("}}");
    return localStringBuilder.toString();
  }

  void updateHostController(FragmentHostCallback paramFragmentHostCallback)
  {
    this.mHost = paramFragmentHostCallback;
  }

  final class LoaderInfo
    implements Loader.OnLoadCompleteListener<Object>, Loader.OnLoadCanceledListener<Object>
  {
    final Bundle mArgs;
    LoaderManager.LoaderCallbacks<Object> mCallbacks;
    Object mData;
    boolean mDeliveredData;
    boolean mDestroyed;
    boolean mHaveData;
    final int mId;
    boolean mListenerRegistered;
    Loader<Object> mLoader;
    LoaderInfo mPendingLoader;
    boolean mReportNextStart;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;

    public LoaderInfo(Bundle paramLoaderCallbacks, LoaderManager.LoaderCallbacks<Object> arg3)
    {
      this.mId = paramLoaderCallbacks;
      Object localObject1;
      this.mArgs = localObject1;
      Object localObject2;
      this.mCallbacks = localObject2;
    }

    void callOnLoadFinished(Loader<Object> paramLoader, Object paramObject)
    {
      String str2;
      if (this.mCallbacks != null)
      {
        if (LoaderManagerImpl.this.mHost == null)
          break label164;
        str2 = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
      }
      label164: for (String str1 = str2; ; str1 = null)
        try
        {
          if (LoaderManagerImpl.DEBUG)
            Log.v("LoaderManager", "  onLoadFinished in " + paramLoader + ": " + paramLoader.dataToString(paramObject));
          this.mCallbacks.onLoadFinished(paramLoader, paramObject);
          if (LoaderManagerImpl.this.mHost != null)
            LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = str1;
          this.mDeliveredData = true;
          return;
        }
        finally
        {
          if (LoaderManagerImpl.this.mHost != null)
            LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = str1;
        }
    }

    boolean cancel()
    {
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "  Canceling: " + this);
      if ((this.mStarted) && (this.mLoader != null) && (this.mListenerRegistered))
      {
        boolean bool = this.mLoader.cancelLoad();
        if (!bool)
          onLoadCanceled(this.mLoader);
        return bool;
      }
      return false;
    }

    void destroy()
    {
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "  Destroying: " + this);
      this.mDestroyed = true;
      boolean bool = this.mDeliveredData;
      this.mDeliveredData = false;
      String str2;
      if ((this.mCallbacks != null) && (this.mLoader != null) && (this.mHaveData) && (bool))
      {
        if (LoaderManagerImpl.DEBUG)
          Log.v("LoaderManager", "  Resetting: " + this);
        if (LoaderManagerImpl.this.mHost == null)
          break label281;
        str2 = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
      }
      label281: for (String str1 = str2; ; str1 = null)
        try
        {
          this.mCallbacks.onLoaderReset(this.mLoader);
          if (LoaderManagerImpl.this.mHost != null)
            LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = str1;
          this.mCallbacks = null;
          this.mData = null;
          this.mHaveData = false;
          if (this.mLoader != null)
          {
            if (this.mListenerRegistered)
            {
              this.mListenerRegistered = false;
              this.mLoader.unregisterListener(this);
              this.mLoader.unregisterOnLoadCanceledListener(this);
            }
            this.mLoader.reset();
          }
          if (this.mPendingLoader != null)
            this.mPendingLoader.destroy();
          return;
        }
        finally
        {
          if (LoaderManagerImpl.this.mHost != null)
            LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = str1;
        }
    }

    public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mId=");
      paramPrintWriter.print(this.mId);
      paramPrintWriter.print(" mArgs=");
      paramPrintWriter.println(this.mArgs);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCallbacks=");
      paramPrintWriter.println(this.mCallbacks);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mLoader=");
      paramPrintWriter.println(this.mLoader);
      if (this.mLoader != null)
        this.mLoader.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      if ((this.mHaveData) || (this.mDeliveredData))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mHaveData=");
        paramPrintWriter.print(this.mHaveData);
        paramPrintWriter.print("  mDeliveredData=");
        paramPrintWriter.println(this.mDeliveredData);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mData=");
        paramPrintWriter.println(this.mData);
      }
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStarted=");
      paramPrintWriter.print(this.mStarted);
      paramPrintWriter.print(" mReportNextStart=");
      paramPrintWriter.print(this.mReportNextStart);
      paramPrintWriter.print(" mDestroyed=");
      paramPrintWriter.println(this.mDestroyed);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mRetaining=");
      paramPrintWriter.print(this.mRetaining);
      paramPrintWriter.print(" mRetainingStarted=");
      paramPrintWriter.print(this.mRetainingStarted);
      paramPrintWriter.print(" mListenerRegistered=");
      paramPrintWriter.println(this.mListenerRegistered);
      if (this.mPendingLoader != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Pending Loader ");
        paramPrintWriter.print(this.mPendingLoader);
        paramPrintWriter.println(":");
        this.mPendingLoader.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }

    void finishRetain()
    {
      if (this.mRetaining)
      {
        if (LoaderManagerImpl.DEBUG)
          Log.v("LoaderManager", "  Finished Retaining: " + this);
        this.mRetaining = false;
        if ((this.mStarted != this.mRetainingStarted) && (!this.mStarted))
          stop();
      }
      if ((this.mStarted) && (this.mHaveData) && (!this.mReportNextStart))
        callOnLoadFinished(this.mLoader, this.mData);
    }

    public void onLoadCanceled(Loader<Object> paramLoader)
    {
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "onLoadCanceled: " + this);
      if (this.mDestroyed)
        if (LoaderManagerImpl.DEBUG)
          Log.v("LoaderManager", "  Ignoring load canceled -- destroyed");
      LoaderInfo localLoaderInfo;
      do
      {
        while (true)
        {
          return;
          if (LoaderManagerImpl.this.mLoaders.get(this.mId) == this)
            break;
          if (!LoaderManagerImpl.DEBUG)
            continue;
          Log.v("LoaderManager", "  Ignoring load canceled -- not active");
          return;
        }
        localLoaderInfo = this.mPendingLoader;
      }
      while (localLoaderInfo == null);
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "  Switching to pending loader: " + localLoaderInfo);
      this.mPendingLoader = null;
      LoaderManagerImpl.this.mLoaders.put(this.mId, null);
      destroy();
      LoaderManagerImpl.this.installLoader(localLoaderInfo);
    }

    public void onLoadComplete(Loader<Object> paramLoader, Object paramObject)
    {
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "onLoadComplete: " + this);
      if (this.mDestroyed)
        if (LoaderManagerImpl.DEBUG)
          Log.v("LoaderManager", "  Ignoring load complete -- destroyed");
      do
      {
        while (true)
        {
          return;
          if (LoaderManagerImpl.this.mLoaders.get(this.mId) == this)
            break;
          if (!LoaderManagerImpl.DEBUG)
            continue;
          Log.v("LoaderManager", "  Ignoring load complete -- not active");
          return;
        }
        LoaderInfo localLoaderInfo1 = this.mPendingLoader;
        if (localLoaderInfo1 != null)
        {
          if (LoaderManagerImpl.DEBUG)
            Log.v("LoaderManager", "  Switching to pending loader: " + localLoaderInfo1);
          this.mPendingLoader = null;
          LoaderManagerImpl.this.mLoaders.put(this.mId, null);
          destroy();
          LoaderManagerImpl.this.installLoader(localLoaderInfo1);
          return;
        }
        if ((this.mData != paramObject) || (!this.mHaveData))
        {
          this.mData = paramObject;
          this.mHaveData = true;
          if (this.mStarted)
            callOnLoadFinished(paramLoader, paramObject);
        }
        LoaderInfo localLoaderInfo2 = (LoaderInfo)LoaderManagerImpl.this.mInactiveLoaders.get(this.mId);
        if ((localLoaderInfo2 == null) || (localLoaderInfo2 == this))
          continue;
        localLoaderInfo2.mDeliveredData = false;
        localLoaderInfo2.destroy();
        LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
      }
      while ((LoaderManagerImpl.this.mHost == null) || (LoaderManagerImpl.this.hasRunningLoaders()));
      LoaderManagerImpl.this.mHost.mFragmentManager.startPendingDeferredFragments();
    }

    void reportStart()
    {
      if ((this.mStarted) && (this.mReportNextStart))
      {
        this.mReportNextStart = false;
        if ((this.mHaveData) && (!this.mRetaining))
          callOnLoadFinished(this.mLoader, this.mData);
      }
    }

    void retain()
    {
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "  Retaining: " + this);
      this.mRetaining = true;
      this.mRetainingStarted = this.mStarted;
      this.mStarted = false;
      this.mCallbacks = null;
    }

    void start()
    {
      if ((this.mRetaining) && (this.mRetainingStarted))
        this.mStarted = true;
      do
      {
        do
          return;
        while (this.mStarted);
        this.mStarted = true;
        if (LoaderManagerImpl.DEBUG)
          Log.v("LoaderManager", "  Starting: " + this);
        if ((this.mLoader != null) || (this.mCallbacks == null))
          continue;
        this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
      }
      while (this.mLoader == null);
      if ((this.mLoader.getClass().isMemberClass()) && (!Modifier.isStatic(this.mLoader.getClass().getModifiers())))
        throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + this.mLoader);
      if (!this.mListenerRegistered)
      {
        this.mLoader.registerListener(this.mId, this);
        this.mLoader.registerOnLoadCanceledListener(this);
        this.mListenerRegistered = true;
      }
      this.mLoader.startLoading();
    }

    void stop()
    {
      if (LoaderManagerImpl.DEBUG)
        Log.v("LoaderManager", "  Stopping: " + this);
      this.mStarted = false;
      if ((!this.mRetaining) && (this.mLoader != null) && (this.mListenerRegistered))
      {
        this.mListenerRegistered = false;
        this.mLoader.unregisterListener(this);
        this.mLoader.unregisterOnLoadCanceledListener(this);
        this.mLoader.stopLoading();
      }
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(64);
      localStringBuilder.append("LoaderInfo{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" #");
      localStringBuilder.append(this.mId);
      localStringBuilder.append(" : ");
      DebugUtils.buildShortClassTag(this.mLoader, localStringBuilder);
      localStringBuilder.append("}}");
      return localStringBuilder.toString();
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.LoaderManagerImpl
 * JD-Core Version:    0.6.0
 */