package android.support.v4.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

public final class ContentResolverCompat
{
  private static final ContentResolverCompatImpl IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new ContentResolverCompatImplJB();
      return;
    }
    IMPL = new ContentResolverCompatImplBase();
  }

  public static Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    return IMPL.query(paramContentResolver, paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, paramCancellationSignal);
  }

  static abstract interface ContentResolverCompatImpl
  {
    public abstract Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal);
  }

  static class ContentResolverCompatImplBase
    implements ContentResolverCompat.ContentResolverCompatImpl
  {
    public Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
    {
      if (paramCancellationSignal != null)
        paramCancellationSignal.throwIfCanceled();
      return paramContentResolver.query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    }
  }

  static class ContentResolverCompatImplJB extends ContentResolverCompat.ContentResolverCompatImplBase
  {
    public Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
    {
      if (paramCancellationSignal != null);
      try
      {
        for (Object localObject = paramCancellationSignal.getCancellationSignalObject(); ; localObject = null)
        {
          Cursor localCursor = ContentResolverCompatJellybean.query(paramContentResolver, paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, localObject);
          return localCursor;
        }
      }
      catch (Exception localException)
      {
        if (ContentResolverCompatJellybean.isFrameworkOperationCanceledException(localException))
          throw new OperationCanceledException();
      }
      throw localException;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.content.ContentResolverCompat
 * JD-Core Version:    0.6.0
 */