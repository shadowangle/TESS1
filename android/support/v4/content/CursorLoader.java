package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.os.CancellationSignal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor>
{
  CancellationSignal mCancellationSignal;
  Cursor mCursor;
  final Loader<Cursor>.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver(this);
  String[] mProjection;
  String mSelection;
  String[] mSelectionArgs;
  String mSortOrder;
  Uri mUri;

  public CursorLoader(Context paramContext)
  {
    super(paramContext);
  }

  public CursorLoader(Context paramContext, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    super(paramContext);
    this.mUri = paramUri;
    this.mProjection = paramArrayOfString1;
    this.mSelection = paramString1;
    this.mSelectionArgs = paramArrayOfString2;
    this.mSortOrder = paramString2;
  }

  public void cancelLoadInBackground()
  {
    super.cancelLoadInBackground();
    monitorenter;
    try
    {
      if (this.mCancellationSignal != null)
        this.mCancellationSignal.cancel();
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void deliverResult(Cursor paramCursor)
  {
    if (isReset())
      if (paramCursor != null)
        paramCursor.close();
    Cursor localCursor;
    do
    {
      return;
      localCursor = this.mCursor;
      this.mCursor = paramCursor;
      if (!isStarted())
        continue;
      super.deliverResult(paramCursor);
    }
    while ((localCursor == null) || (localCursor == paramCursor) || (localCursor.isClosed()));
    localCursor.close();
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUri=");
    paramPrintWriter.println(this.mUri);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mProjection=");
    paramPrintWriter.println(Arrays.toString(this.mProjection));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelection=");
    paramPrintWriter.println(this.mSelection);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelectionArgs=");
    paramPrintWriter.println(Arrays.toString(this.mSelectionArgs));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSortOrder=");
    paramPrintWriter.println(this.mSortOrder);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mCursor=");
    paramPrintWriter.println(this.mCursor);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mContentChanged=");
    paramPrintWriter.println(this.mContentChanged);
  }

  public String[] getProjection()
  {
    return this.mProjection;
  }

  public String getSelection()
  {
    return this.mSelection;
  }

  public String[] getSelectionArgs()
  {
    return this.mSelectionArgs;
  }

  public String getSortOrder()
  {
    return this.mSortOrder;
  }

  public Uri getUri()
  {
    return this.mUri;
  }

  // ERROR //
  public Cursor loadInBackground()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual 134	android/support/v4/content/CursorLoader:isLoadInBackgroundCanceled	()Z
    //   6: ifeq +16 -> 22
    //   9: new 136	android/support/v4/os/OperationCanceledException
    //   12: dup
    //   13: invokespecial 138	android/support/v4/os/OperationCanceledException:<init>	()V
    //   16: athrow
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    //   22: aload_0
    //   23: new 50	android/support/v4/os/CancellationSignal
    //   26: dup
    //   27: invokespecial 139	android/support/v4/os/CancellationSignal:<init>	()V
    //   30: putfield 48	android/support/v4/content/CursorLoader:mCancellationSignal	Landroid/support/v4/os/CancellationSignal;
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_0
    //   36: invokevirtual 143	android/support/v4/content/CursorLoader:getContext	()Landroid/content/Context;
    //   39: invokevirtual 149	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   42: aload_0
    //   43: getfield 34	android/support/v4/content/CursorLoader:mUri	Landroid/net/Uri;
    //   46: aload_0
    //   47: getfield 36	android/support/v4/content/CursorLoader:mProjection	[Ljava/lang/String;
    //   50: aload_0
    //   51: getfield 38	android/support/v4/content/CursorLoader:mSelection	Ljava/lang/String;
    //   54: aload_0
    //   55: getfield 40	android/support/v4/content/CursorLoader:mSelectionArgs	[Ljava/lang/String;
    //   58: aload_0
    //   59: getfield 42	android/support/v4/content/CursorLoader:mSortOrder	Ljava/lang/String;
    //   62: aload_0
    //   63: getfield 48	android/support/v4/content/CursorLoader:mCancellationSignal	Landroid/support/v4/os/CancellationSignal;
    //   66: invokestatic 155	android/support/v4/content/ContentResolverCompat:query	(Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/support/v4/os/CancellationSignal;)Landroid/database/Cursor;
    //   69: astore 4
    //   71: aload 4
    //   73: ifnull +22 -> 95
    //   76: aload 4
    //   78: invokeinterface 159 1 0
    //   83: pop
    //   84: aload 4
    //   86: aload_0
    //   87: getfield 31	android/support/v4/content/CursorLoader:mObserver	Landroid/support/v4/content/Loader$ForceLoadContentObserver;
    //   90: invokeinterface 163 2 0
    //   95: aload_0
    //   96: monitorenter
    //   97: aload_0
    //   98: aconst_null
    //   99: putfield 48	android/support/v4/content/CursorLoader:mCancellationSignal	Landroid/support/v4/os/CancellationSignal;
    //   102: aload_0
    //   103: monitorexit
    //   104: aload 4
    //   106: areturn
    //   107: astore 6
    //   109: aload 4
    //   111: invokeinterface 64 1 0
    //   116: aload 6
    //   118: athrow
    //   119: astore_2
    //   120: aload_0
    //   121: monitorenter
    //   122: aload_0
    //   123: aconst_null
    //   124: putfield 48	android/support/v4/content/CursorLoader:mCancellationSignal	Landroid/support/v4/os/CancellationSignal;
    //   127: aload_0
    //   128: monitorexit
    //   129: aload_2
    //   130: athrow
    //   131: astore 5
    //   133: aload_0
    //   134: monitorexit
    //   135: aload 5
    //   137: athrow
    //   138: astore_3
    //   139: aload_0
    //   140: monitorexit
    //   141: aload_3
    //   142: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   2	17	17	finally
    //   18	20	17	finally
    //   22	35	17	finally
    //   76	95	107	java/lang/RuntimeException
    //   35	71	119	finally
    //   76	95	119	finally
    //   109	119	119	finally
    //   97	104	131	finally
    //   133	135	131	finally
    //   122	129	138	finally
    //   139	141	138	finally
  }

  public void onCanceled(Cursor paramCursor)
  {
    if ((paramCursor != null) && (!paramCursor.isClosed()))
      paramCursor.close();
  }

  protected void onReset()
  {
    super.onReset();
    onStopLoading();
    if ((this.mCursor != null) && (!this.mCursor.isClosed()))
      this.mCursor.close();
    this.mCursor = null;
  }

  protected void onStartLoading()
  {
    if (this.mCursor != null)
      deliverResult(this.mCursor);
    if ((takeContentChanged()) || (this.mCursor == null))
      forceLoad();
  }

  protected void onStopLoading()
  {
    cancelLoad();
  }

  public void setProjection(String[] paramArrayOfString)
  {
    this.mProjection = paramArrayOfString;
  }

  public void setSelection(String paramString)
  {
    this.mSelection = paramString;
  }

  public void setSelectionArgs(String[] paramArrayOfString)
  {
    this.mSelectionArgs = paramArrayOfString;
  }

  public void setSortOrder(String paramString)
  {
    this.mSortOrder = paramString;
  }

  public void setUri(Uri paramUri)
  {
    this.mUri = paramUri;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.CursorLoader
 * JD-Core Version:    0.6.0
 */