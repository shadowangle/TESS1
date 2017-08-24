package android.support.v4.view;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.util.Pools.SynchronizedPool;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.concurrent.ArrayBlockingQueue;

public final class AsyncLayoutInflater
{
  private static final String TAG = "AsyncLayoutInflater";
  Handler mHandler;
  private Handler.Callback mHandlerCallback = new Handler.Callback()
  {
    public boolean handleMessage(Message paramMessage)
    {
      AsyncLayoutInflater.InflateRequest localInflateRequest = (AsyncLayoutInflater.InflateRequest)paramMessage.obj;
      if (localInflateRequest.view == null)
        localInflateRequest.view = AsyncLayoutInflater.this.mInflater.inflate(localInflateRequest.resid, localInflateRequest.parent, false);
      localInflateRequest.callback.onInflateFinished(localInflateRequest.view, localInflateRequest.resid, localInflateRequest.parent);
      AsyncLayoutInflater.this.mInflateThread.releaseRequest(localInflateRequest);
      return true;
    }
  };
  InflateThread mInflateThread;
  LayoutInflater mInflater;

  public AsyncLayoutInflater(@NonNull Context paramContext)
  {
    this.mInflater = new BasicInflater(paramContext);
    this.mHandler = new Handler(this.mHandlerCallback);
    this.mInflateThread = InflateThread.getInstance();
  }

  @UiThread
  public void inflate(@LayoutRes int paramInt, @Nullable ViewGroup paramViewGroup, @NonNull OnInflateFinishedListener paramOnInflateFinishedListener)
  {
    if (paramOnInflateFinishedListener == null)
      throw new NullPointerException("callback argument may not be null!");
    InflateRequest localInflateRequest = this.mInflateThread.obtainRequest();
    localInflateRequest.inflater = this;
    localInflateRequest.resid = paramInt;
    localInflateRequest.parent = paramViewGroup;
    localInflateRequest.callback = paramOnInflateFinishedListener;
    this.mInflateThread.enqueue(localInflateRequest);
  }

  private static class BasicInflater extends LayoutInflater
  {
    private static final String[] sClassPrefixList = { "android.widget.", "android.webkit.", "android.app." };

    BasicInflater(Context paramContext)
    {
      super();
    }

    public LayoutInflater cloneInContext(Context paramContext)
    {
      return new BasicInflater(paramContext);
    }

    protected View onCreateView(String paramString, AttributeSet paramAttributeSet)
      throws ClassNotFoundException
    {
      String[] arrayOfString = sClassPrefixList;
      int i = arrayOfString.length;
      int j = 0;
      while (j < i)
      {
        String str = arrayOfString[j];
        try
        {
          View localView = createView(paramString, str, paramAttributeSet);
          if (localView != null)
            return localView;
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          j++;
        }
      }
      return super.onCreateView(paramString, paramAttributeSet);
    }
  }

  private static class InflateRequest
  {
    AsyncLayoutInflater.OnInflateFinishedListener callback;
    AsyncLayoutInflater inflater;
    ViewGroup parent;
    int resid;
    View view;
  }

  private static class InflateThread extends Thread
  {
    private static final InflateThread sInstance = new InflateThread();
    private ArrayBlockingQueue<AsyncLayoutInflater.InflateRequest> mQueue = new ArrayBlockingQueue(10);
    private Pools.SynchronizedPool<AsyncLayoutInflater.InflateRequest> mRequestPool = new Pools.SynchronizedPool(10);

    static
    {
      sInstance.start();
    }

    public static InflateThread getInstance()
    {
      return sInstance;
    }

    public void enqueue(AsyncLayoutInflater.InflateRequest paramInflateRequest)
    {
      try
      {
        this.mQueue.put(paramInflateRequest);
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
      }
      throw new RuntimeException("Failed to enqueue async inflate request", localInterruptedException);
    }

    public AsyncLayoutInflater.InflateRequest obtainRequest()
    {
      AsyncLayoutInflater.InflateRequest localInflateRequest = (AsyncLayoutInflater.InflateRequest)this.mRequestPool.acquire();
      if (localInflateRequest == null)
        localInflateRequest = new AsyncLayoutInflater.InflateRequest();
      return localInflateRequest;
    }

    public void releaseRequest(AsyncLayoutInflater.InflateRequest paramInflateRequest)
    {
      paramInflateRequest.callback = null;
      paramInflateRequest.inflater = null;
      paramInflateRequest.parent = null;
      paramInflateRequest.resid = 0;
      paramInflateRequest.view = null;
      this.mRequestPool.release(paramInflateRequest);
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 30	android/support/v4/view/AsyncLayoutInflater$InflateThread:mQueue	Ljava/util/concurrent/ArrayBlockingQueue;
      //   4: invokevirtual 90	java/util/concurrent/ArrayBlockingQueue:take	()Ljava/lang/Object;
      //   7: checkcast 60	android/support/v4/view/AsyncLayoutInflater$InflateRequest
      //   10: astore_3
      //   11: aload_3
      //   12: aload_3
      //   13: getfield 70	android/support/v4/view/AsyncLayoutInflater$InflateRequest:inflater	Landroid/support/v4/view/AsyncLayoutInflater;
      //   16: getfield 96	android/support/v4/view/AsyncLayoutInflater:mInflater	Landroid/view/LayoutInflater;
      //   19: aload_3
      //   20: getfield 78	android/support/v4/view/AsyncLayoutInflater$InflateRequest:resid	I
      //   23: aload_3
      //   24: getfield 74	android/support/v4/view/AsyncLayoutInflater$InflateRequest:parent	Landroid/view/ViewGroup;
      //   27: iconst_0
      //   28: invokevirtual 102	android/view/LayoutInflater:inflate	(ILandroid/view/ViewGroup;Z)Landroid/view/View;
      //   31: putfield 82	android/support/v4/view/AsyncLayoutInflater$InflateRequest:view	Landroid/view/View;
      //   34: aload_3
      //   35: getfield 70	android/support/v4/view/AsyncLayoutInflater$InflateRequest:inflater	Landroid/support/v4/view/AsyncLayoutInflater;
      //   38: getfield 106	android/support/v4/view/AsyncLayoutInflater:mHandler	Landroid/os/Handler;
      //   41: iconst_0
      //   42: aload_3
      //   43: invokestatic 112	android/os/Message:obtain	(Landroid/os/Handler;ILjava/lang/Object;)Landroid/os/Message;
      //   46: invokevirtual 115	android/os/Message:sendToTarget	()V
      //   49: goto -49 -> 0
      //   52: astore_1
      //   53: ldc 117
      //   55: aload_1
      //   56: invokestatic 123	android/util/Log:w	(Ljava/lang/String;Ljava/lang/Throwable;)I
      //   59: pop
      //   60: goto -60 -> 0
      //   63: astore 4
      //   65: ldc 117
      //   67: ldc 125
      //   69: aload 4
      //   71: invokestatic 128	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   74: pop
      //   75: goto -41 -> 34
      //
      // Exception table:
      //   from	to	target	type
      //   0	11	52	java/lang/InterruptedException
      //   11	34	63	java/lang/RuntimeException
    }
  }

  public static abstract interface OnInflateFinishedListener
  {
    public abstract void onInflateFinished(View paramView, int paramInt, ViewGroup paramViewGroup);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.AsyncLayoutInflater
 * JD-Core Version:    0.6.0
 */