package android.support.v4.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;

@TargetApi(21)
@RequiresApi(21)
class DocumentsContractApi21
{
  private static final String TAG = "DocumentFile";

  private static void closeQuietly(AutoCloseable paramAutoCloseable)
  {
    if (paramAutoCloseable != null);
    try
    {
      paramAutoCloseable.close();
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (Exception localException)
    {
    }
  }

  public static Uri createDirectory(Context paramContext, Uri paramUri, String paramString)
  {
    return createFile(paramContext, paramUri, "vnd.android.document/directory", paramString);
  }

  public static Uri createFile(Context paramContext, Uri paramUri, String paramString1, String paramString2)
  {
    return DocumentsContract.createDocument(paramContext.getContentResolver(), paramUri, paramString1, paramString2);
  }

  // ERROR //
  public static Uri[] listFiles(Context paramContext, Uri paramUri)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 41	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: astore_2
    //   5: aload_1
    //   6: aload_1
    //   7: invokestatic 53	android/provider/DocumentsContract:getDocumentId	(Landroid/net/Uri;)Ljava/lang/String;
    //   10: invokestatic 57	android/provider/DocumentsContract:buildChildDocumentsUriUsingTree	(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   13: astore_3
    //   14: new 59	java/util/ArrayList
    //   17: dup
    //   18: invokespecial 60	java/util/ArrayList:<init>	()V
    //   21: astore 4
    //   23: aload_2
    //   24: aload_3
    //   25: iconst_1
    //   26: anewarray 62	java/lang/String
    //   29: dup
    //   30: iconst_0
    //   31: ldc 64
    //   33: aastore
    //   34: aconst_null
    //   35: aconst_null
    //   36: aconst_null
    //   37: invokevirtual 70	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   40: astore 9
    //   42: aload 9
    //   44: astore 6
    //   46: aload 6
    //   48: invokeinterface 76 1 0
    //   53: ifeq +74 -> 127
    //   56: aload 4
    //   58: aload_1
    //   59: aload 6
    //   61: iconst_0
    //   62: invokeinterface 80 2 0
    //   67: invokestatic 83	android/provider/DocumentsContract:buildDocumentUriUsingTree	(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   70: invokevirtual 87	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   73: pop
    //   74: goto -28 -> 46
    //   77: astore 5
    //   79: ldc 12
    //   81: new 89	java/lang/StringBuilder
    //   84: dup
    //   85: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   88: ldc 92
    //   90: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: aload 5
    //   95: invokevirtual 99	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   98: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   101: invokestatic 109	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   104: pop
    //   105: aload 6
    //   107: invokestatic 111	android/support/v4/provider/DocumentsContractApi21:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   110: aload 4
    //   112: aload 4
    //   114: invokevirtual 115	java/util/ArrayList:size	()I
    //   117: anewarray 117	android/net/Uri
    //   120: invokevirtual 121	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   123: checkcast 123	[Landroid/net/Uri;
    //   126: areturn
    //   127: aload 6
    //   129: invokestatic 111	android/support/v4/provider/DocumentsContractApi21:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   132: goto -22 -> 110
    //   135: astore 7
    //   137: aconst_null
    //   138: astore 6
    //   140: aload 6
    //   142: invokestatic 111	android/support/v4/provider/DocumentsContractApi21:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   145: aload 7
    //   147: athrow
    //   148: astore 7
    //   150: goto -10 -> 140
    //   153: astore 5
    //   155: aconst_null
    //   156: astore 6
    //   158: goto -79 -> 79
    //
    // Exception table:
    //   from	to	target	type
    //   46	74	77	java/lang/Exception
    //   23	42	135	finally
    //   46	74	148	finally
    //   79	105	148	finally
    //   23	42	153	java/lang/Exception
  }

  public static Uri prepareTreeUri(Uri paramUri)
  {
    return DocumentsContract.buildDocumentUriUsingTree(paramUri, DocumentsContract.getTreeDocumentId(paramUri));
  }

  public static Uri renameTo(Context paramContext, Uri paramUri, String paramString)
  {
    return DocumentsContract.renameDocument(paramContext.getContentResolver(), paramUri, paramString);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.provider.DocumentsContractApi21
 * JD-Core Version:    0.6.0
 */