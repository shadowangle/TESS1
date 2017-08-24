package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager
{
  private static final boolean DEBUG = false;
  static final int MSG_EXEC_PENDING_BROADCASTS = 1;
  private static final String TAG = "LocalBroadcastManager";
  private static LocalBroadcastManager mInstance;
  private static final Object mLock = new Object();
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
  private final Context mAppContext;
  private final Handler mHandler;
  private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
  private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap();

  private LocalBroadcastManager(Context paramContext)
  {
    this.mAppContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper())
    {
      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default:
          super.handleMessage(paramMessage);
          return;
        case 1:
        }
        LocalBroadcastManager.this.executePendingBroadcasts();
      }
    };
  }

  private void executePendingBroadcasts()
  {
    while (true)
    {
      int j;
      synchronized (this.mReceivers)
      {
        int i = this.mPendingBroadcasts.size();
        if (i <= 0)
          return;
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        this.mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
        this.mPendingBroadcasts.clear();
        j = 0;
        if (j >= arrayOfBroadcastRecord.length)
          continue;
        BroadcastRecord localBroadcastRecord = arrayOfBroadcastRecord[j];
        int k = 0;
        if (k < localBroadcastRecord.receivers.size())
        {
          ((ReceiverRecord)localBroadcastRecord.receivers.get(k)).receiver.onReceive(this.mAppContext, localBroadcastRecord.intent);
          k++;
        }
      }
      j++;
    }
  }

  public static LocalBroadcastManager getInstance(Context paramContext)
  {
    synchronized (mLock)
    {
      if (mInstance == null)
        mInstance = new LocalBroadcastManager(paramContext.getApplicationContext());
      LocalBroadcastManager localLocalBroadcastManager = mInstance;
      return localLocalBroadcastManager;
    }
  }

  public void registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    synchronized (this.mReceivers)
    {
      ReceiverRecord localReceiverRecord = new ReceiverRecord(paramIntentFilter, paramBroadcastReceiver);
      ArrayList localArrayList1 = (ArrayList)this.mReceivers.get(paramBroadcastReceiver);
      if (localArrayList1 == null)
      {
        localArrayList1 = new ArrayList(1);
        this.mReceivers.put(paramBroadcastReceiver, localArrayList1);
      }
      localArrayList1.add(paramIntentFilter);
      for (int i = 0; i < paramIntentFilter.countActions(); i++)
      {
        String str = paramIntentFilter.getAction(i);
        ArrayList localArrayList2 = (ArrayList)this.mActions.get(str);
        if (localArrayList2 == null)
        {
          localArrayList2 = new ArrayList(1);
          this.mActions.put(str, localArrayList2);
        }
        localArrayList2.add(localReceiverRecord);
      }
      return;
    }
  }

  public boolean sendBroadcast(Intent paramIntent)
  {
    while (true)
    {
      int n;
      synchronized (this.mReceivers)
      {
        String str1 = paramIntent.getAction();
        String str2 = paramIntent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
        Uri localUri = paramIntent.getData();
        String str3 = paramIntent.getScheme();
        Set localSet = paramIntent.getCategories();
        if ((0x8 & paramIntent.getFlags()) == 0)
          break label569;
        i = 1;
        if (i == 0)
          continue;
        Log.v("LocalBroadcastManager", "Resolving type " + str2 + " scheme " + str3 + " of intent " + paramIntent);
        ArrayList localArrayList = (ArrayList)this.mActions.get(paramIntent.getAction());
        if (localArrayList == null)
          break label525;
        if (i == 0)
          break label543;
        Log.v("LocalBroadcastManager", "Action list: " + localArrayList);
        break label543;
        if (j >= localArrayList.size())
          break label445;
        ReceiverRecord localReceiverRecord = (ReceiverRecord)localArrayList.get(j);
        if (i == 0)
          continue;
        Log.v("LocalBroadcastManager", "Matching against filter " + localReceiverRecord.filter);
        if (!localReceiverRecord.broadcasting)
          continue;
        if (i == 0)
          break label529;
        Log.v("LocalBroadcastManager", "  Filter's target already added");
        localObject3 = localObject2;
        break label552;
        n = localReceiverRecord.filter.match(str1, str2, str3, localUri, localSet, "LocalBroadcastManager");
        if (n >= 0)
        {
          if (i == 0)
            continue;
          Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(n));
          if (localObject2 != null)
            break label536;
          localObject3 = new ArrayList();
          ((ArrayList)localObject3).add(localReceiverRecord);
          localReceiverRecord.broadcasting = true;
        }
      }
      if (i != 0)
      {
        String str4;
        switch (n)
        {
        default:
          str4 = "unknown reason";
        case -3:
        case -4:
        case -2:
        case -1:
        }
        while (true)
        {
          Log.v("LocalBroadcastManager", "  Filter did not match: " + str4);
          localObject3 = localObject2;
          break;
          str4 = "action";
          continue;
          str4 = "category";
          continue;
          str4 = "data";
          continue;
          str4 = "type";
        }
        label445: if (localObject2 != null)
        {
          for (int k = 0; k < ((ArrayList)localObject2).size(); k++)
            ((ReceiverRecord)((ArrayList)localObject2).get(k)).broadcasting = false;
          this.mPendingBroadcasts.add(new BroadcastRecord(paramIntent, (ArrayList)localObject2));
          if (!this.mHandler.hasMessages(1))
            this.mHandler.sendEmptyMessage(1);
          monitorexit;
          return true;
        }
        label525: monitorexit;
        return false;
      }
      label529: Object localObject3 = localObject2;
      break label552;
      label536: localObject3 = localObject2;
      continue;
      label543: Object localObject2 = null;
      int j = 0;
      continue;
      label552: int m = j + 1;
      localObject2 = localObject3;
      j = m;
      continue;
      label569: int i = 0;
    }
  }

  public void sendBroadcastSync(Intent paramIntent)
  {
    if (sendBroadcast(paramIntent))
      executePendingBroadcasts();
  }

  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    while (true)
    {
      int j;
      synchronized (this.mReceivers)
      {
        ArrayList localArrayList1 = (ArrayList)this.mReceivers.remove(paramBroadcastReceiver);
        if (localArrayList1 != null)
          break label174;
        return;
        if (i >= localArrayList1.size())
          continue;
        IntentFilter localIntentFilter = (IntentFilter)localArrayList1.get(i);
        j = 0;
        if (j >= localIntentFilter.countActions())
          break label195;
        String str = localIntentFilter.getAction(j);
        ArrayList localArrayList2 = (ArrayList)this.mActions.get(str);
        if (localArrayList2 == null)
          break label189;
        k = 0;
        if (k >= localArrayList2.size())
          continue;
        if (((ReceiverRecord)localArrayList2.get(k)).receiver == paramBroadcastReceiver)
        {
          localArrayList2.remove(k);
          m = k - 1;
          break label180;
          if (localArrayList2.size() > 0)
            break label189;
          this.mActions.remove(str);
          break label189;
          return;
        }
      }
      int m = k;
      break label180;
      label174: int i = 0;
      continue;
      label180: int k = m + 1;
      continue;
      label189: j++;
      continue;
      label195: i++;
    }
  }

  private static class BroadcastRecord
  {
    final Intent intent;
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;

    BroadcastRecord(Intent paramIntent, ArrayList<LocalBroadcastManager.ReceiverRecord> paramArrayList)
    {
      this.intent = paramIntent;
      this.receivers = paramArrayList;
    }
  }

  private static class ReceiverRecord
  {
    boolean broadcasting;
    final IntentFilter filter;
    final BroadcastReceiver receiver;

    ReceiverRecord(IntentFilter paramIntentFilter, BroadcastReceiver paramBroadcastReceiver)
    {
      this.filter = paramIntentFilter;
      this.receiver = paramBroadcastReceiver;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Receiver{");
      localStringBuilder.append(this.receiver);
      localStringBuilder.append(" filter=");
      localStringBuilder.append(this.filter);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.LocalBroadcastManager
 * JD-Core Version:    0.6.0
 */