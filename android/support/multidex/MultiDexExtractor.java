package android.support.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

final class MultiDexExtractor
{
  private static final int BUFFER_SIZE = 16384;
  private static final String DEX_PREFIX = "classes";
  private static final String DEX_SUFFIX = ".dex";
  private static final String EXTRACTED_NAME_EXT = ".classes";
  private static final String EXTRACTED_SUFFIX = ".zip";
  private static final String KEY_CRC = "crc";
  private static final String KEY_DEX_NUMBER = "dex.number";
  private static final String KEY_TIME_STAMP = "timestamp";
  private static final int MAX_EXTRACT_ATTEMPTS = 3;
  private static final long NO_VALUE = -1L;
  private static final String PREFS_FILE = "multidex.version";
  private static final String TAG = "MultiDex";
  private static Method sApplyMethod;

  static
  {
    try
    {
      sApplyMethod = SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      sApplyMethod = null;
    }
  }

  private static void apply(SharedPreferences.Editor paramEditor)
  {
    if (sApplyMethod != null);
    try
    {
      sApplyMethod.invoke(paramEditor, new Object[0]);
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      paramEditor.commit();
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      label20: break label20;
    }
  }

  private static void closeQuietly(Closeable paramCloseable)
  {
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException localIOException)
    {
      Log.w("MultiDex", "Failed to close resource", localIOException);
    }
  }

  // ERROR //
  private static void extract(ZipFile paramZipFile, ZipEntry paramZipEntry, File paramFile, String paramString)
    throws IOException, java.io.FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 104	java/util/zip/ZipFile:getInputStream	(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
    //   5: astore 4
    //   7: aload_3
    //   8: ldc 20
    //   10: aload_2
    //   11: invokevirtual 110	java/io/File:getParentFile	()Ljava/io/File;
    //   14: invokestatic 114	java/io/File:createTempFile	(Ljava/lang/String;Ljava/lang/String;Ljava/io/File;)Ljava/io/File;
    //   17: astore 5
    //   19: ldc 41
    //   21: new 116	java/lang/StringBuilder
    //   24: dup
    //   25: invokespecial 117	java/lang/StringBuilder:<init>	()V
    //   28: ldc 119
    //   30: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: aload 5
    //   35: invokevirtual 127	java/io/File:getPath	()Ljava/lang/String;
    //   38: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   44: invokestatic 134	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   47: pop
    //   48: new 136	java/util/zip/ZipOutputStream
    //   51: dup
    //   52: new 138	java/io/BufferedOutputStream
    //   55: dup
    //   56: new 140	java/io/FileOutputStream
    //   59: dup
    //   60: aload 5
    //   62: invokespecial 143	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   65: invokespecial 146	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   68: invokespecial 147	java/util/zip/ZipOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   71: astore 7
    //   73: new 149	java/util/zip/ZipEntry
    //   76: dup
    //   77: ldc 151
    //   79: invokespecial 154	java/util/zip/ZipEntry:<init>	(Ljava/lang/String;)V
    //   82: astore 8
    //   84: aload 8
    //   86: aload_1
    //   87: invokevirtual 158	java/util/zip/ZipEntry:getTime	()J
    //   90: invokevirtual 162	java/util/zip/ZipEntry:setTime	(J)V
    //   93: aload 7
    //   95: aload 8
    //   97: invokevirtual 166	java/util/zip/ZipOutputStream:putNextEntry	(Ljava/util/zip/ZipEntry;)V
    //   100: sipush 16384
    //   103: newarray byte
    //   105: astore 12
    //   107: aload 4
    //   109: aload 12
    //   111: invokevirtual 172	java/io/InputStream:read	([B)I
    //   114: istore 13
    //   116: iload 13
    //   118: iconst_m1
    //   119: if_icmpeq +25 -> 144
    //   122: aload 7
    //   124: aload 12
    //   126: iconst_0
    //   127: iload 13
    //   129: invokevirtual 176	java/util/zip/ZipOutputStream:write	([BII)V
    //   132: aload 4
    //   134: aload 12
    //   136: invokevirtual 172	java/io/InputStream:read	([B)I
    //   139: istore 13
    //   141: goto -25 -> 116
    //   144: aload 7
    //   146: invokevirtual 179	java/util/zip/ZipOutputStream:closeEntry	()V
    //   149: aload 7
    //   151: invokevirtual 180	java/util/zip/ZipOutputStream:close	()V
    //   154: ldc 41
    //   156: new 116	java/lang/StringBuilder
    //   159: dup
    //   160: invokespecial 117	java/lang/StringBuilder:<init>	()V
    //   163: ldc 182
    //   165: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: aload_2
    //   169: invokevirtual 127	java/io/File:getPath	()Ljava/lang/String;
    //   172: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   178: invokestatic 134	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   181: pop
    //   182: aload 5
    //   184: aload_2
    //   185: invokevirtual 186	java/io/File:renameTo	(Ljava/io/File;)Z
    //   188: ifne +77 -> 265
    //   191: new 81	java/io/IOException
    //   194: dup
    //   195: new 116	java/lang/StringBuilder
    //   198: dup
    //   199: invokespecial 117	java/lang/StringBuilder:<init>	()V
    //   202: ldc 188
    //   204: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: aload 5
    //   209: invokevirtual 191	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   212: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: ldc 193
    //   217: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: aload_2
    //   221: invokevirtual 191	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   224: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   227: ldc 195
    //   229: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   232: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   235: invokespecial 196	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   238: athrow
    //   239: astore 10
    //   241: aload 4
    //   243: invokestatic 198	android/support/multidex/MultiDexExtractor:closeQuietly	(Ljava/io/Closeable;)V
    //   246: aload 5
    //   248: invokevirtual 201	java/io/File:delete	()Z
    //   251: pop
    //   252: aload 10
    //   254: athrow
    //   255: astore 9
    //   257: aload 7
    //   259: invokevirtual 180	java/util/zip/ZipOutputStream:close	()V
    //   262: aload 9
    //   264: athrow
    //   265: aload 4
    //   267: invokestatic 198	android/support/multidex/MultiDexExtractor:closeQuietly	(Ljava/io/Closeable;)V
    //   270: aload 5
    //   272: invokevirtual 201	java/io/File:delete	()Z
    //   275: pop
    //   276: return
    //
    // Exception table:
    //   from	to	target	type
    //   48	73	239	finally
    //   149	239	239	finally
    //   257	265	239	finally
    //   73	116	255	finally
    //   122	141	255	finally
    //   144	149	255	finally
  }

  private static SharedPreferences getMultiDexPreferences(Context paramContext)
  {
    if (Build.VERSION.SDK_INT < 11);
    for (int i = 0; ; i = 4)
      return paramContext.getSharedPreferences("multidex.version", i);
  }

  private static long getTimeStamp(File paramFile)
  {
    long l = paramFile.lastModified();
    if (l == -1L)
      l -= 1L;
    return l;
  }

  private static long getZipCrc(File paramFile)
    throws IOException
  {
    long l = ZipUtil.getZipCrc(paramFile);
    if (l == -1L)
      l -= 1L;
    return l;
  }

  private static boolean isModified(Context paramContext, File paramFile, long paramLong)
  {
    SharedPreferences localSharedPreferences = getMultiDexPreferences(paramContext);
    return (localSharedPreferences.getLong("timestamp", -1L) != getTimeStamp(paramFile)) || (localSharedPreferences.getLong("crc", -1L) != paramLong);
  }

  static List<File> load(Context paramContext, ApplicationInfo paramApplicationInfo, File paramFile, boolean paramBoolean)
    throws IOException
  {
    Log.i("MultiDex", "MultiDexExtractor.load(" + paramApplicationInfo.sourceDir + ", " + paramBoolean + ")");
    File localFile = new File(paramApplicationInfo.sourceDir);
    long l = getZipCrc(localFile);
    if ((!paramBoolean) && (!isModified(paramContext, localFile, l)));
    while (true)
    {
      try
      {
        List localList2 = loadExistingExtractions(paramContext, localFile, paramFile);
        localList1 = localList2;
        Log.i("MultiDex", "load found " + localList1.size() + " secondary dex files");
        return localList1;
      }
      catch (IOException localIOException)
      {
        Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", localIOException);
        localList1 = performExtractions(localFile, paramFile);
        putStoredApkInfo(paramContext, getTimeStamp(localFile), l, 1 + localList1.size());
        continue;
      }
      Log.i("MultiDex", "Detected that extraction must be performed.");
      List localList1 = performExtractions(localFile, paramFile);
      putStoredApkInfo(paramContext, getTimeStamp(localFile), l, 1 + localList1.size());
    }
  }

  private static List<File> loadExistingExtractions(Context paramContext, File paramFile1, File paramFile2)
    throws IOException
  {
    Log.i("MultiDex", "loading existing secondary dex files");
    String str = paramFile1.getName() + ".classes";
    int i = getMultiDexPreferences(paramContext).getInt("dex.number", 1);
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 2; j <= i; j++)
    {
      File localFile = new File(paramFile2, str + j + ".zip");
      if (localFile.isFile())
      {
        localArrayList.add(localFile);
        if (verifyZipFile(localFile))
          continue;
        Log.i("MultiDex", "Invalid zip file: " + localFile);
        throw new IOException("Invalid ZIP file.");
      }
      throw new IOException("Missing extracted secondary dex file '" + localFile.getPath() + "'");
    }
    return localArrayList;
  }

  private static void mkdirChecked(File paramFile)
    throws IOException
  {
    paramFile.mkdir();
    if (!paramFile.isDirectory())
    {
      File localFile = paramFile.getParentFile();
      if (localFile == null)
        Log.e("MultiDex", "Failed to create dir " + paramFile.getPath() + ". Parent file is null.");
      while (true)
      {
        throw new IOException("Failed to create cache directory " + paramFile.getPath());
        Log.e("MultiDex", "Failed to create dir " + paramFile.getPath() + ". parent file is a dir " + localFile.isDirectory() + ", a file " + localFile.isFile() + ", exists " + localFile.exists() + ", readable " + localFile.canRead() + ", writable " + localFile.canWrite());
      }
    }
  }

  private static List<File> performExtractions(File paramFile1, File paramFile2)
    throws IOException
  {
    String str1 = paramFile1.getName() + ".classes";
    prepareDexDir(paramFile2, str1);
    ArrayList localArrayList = new ArrayList();
    ZipFile localZipFile = new ZipFile(paramFile1);
    try
    {
      localObject2 = localZipFile.getEntry("classes" + 2 + ".dex");
      i = 2;
      if (localObject2 != null)
      {
        File localFile = new File(paramFile2, str1 + i + ".zip");
        localArrayList.add(localFile);
        Log.i("MultiDex", "Extraction is needed for file " + localFile);
        j = 0;
        for (k = 0; (k < 3) && (j == 0); k = n)
        {
          n = k + 1;
          extract(localZipFile, (ZipEntry)localObject2, localFile, str1);
          bool = verifyZipFile(localFile);
          StringBuilder localStringBuilder = new StringBuilder().append("Extraction ");
          if (!bool)
            break label489;
          str2 = "success";
          Log.i("MultiDex", str2 + " - length " + localFile.getAbsolutePath() + ": " + localFile.length());
          if (bool)
            break label478;
          localFile.delete();
          if (!localFile.exists())
            break label478;
          Log.w("MultiDex", "Failed to delete corrupted secondary dex '" + localFile.getPath() + "'");
          j = bool;
        }
        if (j == 0)
          throw new IOException("Could not create zip file " + localFile.getAbsolutePath() + " for secondary dex (" + i + ")");
      }
    }
    finally
    {
      while (true)
      {
        int n;
        boolean bool;
        try
        {
          localZipFile.close();
          throw localObject1;
          int m = i + 1;
          ZipEntry localZipEntry = localZipFile.getEntry("classes" + m + ".dex");
          Object localObject2 = localZipEntry;
          int i = m;
          continue;
          try
          {
            localZipFile.close();
            return localArrayList;
          }
          catch (IOException localIOException2)
          {
            Log.w("MultiDex", "Failed to close resource", localIOException2);
            return localArrayList;
          }
        }
        catch (IOException localIOException1)
        {
          Log.w("MultiDex", "Failed to close resource", localIOException1);
          continue;
        }
        label478: int j = bool;
        int k = n;
        continue;
        label489: String str2 = "failed";
      }
    }
  }

  private static void prepareDexDir(File paramFile, String paramString)
    throws IOException
  {
    mkdirChecked(paramFile.getParentFile());
    mkdirChecked(paramFile);
    File[] arrayOfFile = paramFile.listFiles(new FileFilter(paramString)
    {
      public boolean accept(File paramFile)
      {
        return !paramFile.getName().startsWith(this.val$extractedFilePrefix);
      }
    });
    if (arrayOfFile == null)
    {
      Log.w("MultiDex", "Failed to list secondary dex dir content (" + paramFile.getPath() + ").");
      return;
    }
    int i = arrayOfFile.length;
    int j = 0;
    label70: File localFile;
    if (j < i)
    {
      localFile = arrayOfFile[j];
      Log.i("MultiDex", "Trying to delete old file " + localFile.getPath() + " of size " + localFile.length());
      if (localFile.delete())
        break label170;
      Log.w("MultiDex", "Failed to delete old file " + localFile.getPath());
    }
    while (true)
    {
      j++;
      break label70;
      break;
      label170: Log.i("MultiDex", "Deleted old file " + localFile.getPath());
    }
  }

  private static void putStoredApkInfo(Context paramContext, long paramLong1, long paramLong2, int paramInt)
  {
    SharedPreferences.Editor localEditor = getMultiDexPreferences(paramContext).edit();
    localEditor.putLong("timestamp", paramLong1);
    localEditor.putLong("crc", paramLong2);
    localEditor.putInt("dex.number", paramInt);
    apply(localEditor);
  }

  static boolean verifyZipFile(File paramFile)
  {
    try
    {
      ZipFile localZipFile = new ZipFile(paramFile);
      try
      {
        localZipFile.close();
        return true;
      }
      catch (IOException localIOException1)
      {
        Log.w("MultiDex", "Failed to close zip file: " + paramFile.getAbsolutePath());
      }
      return false;
    }
    catch (ZipException localZipException)
    {
      while (true)
        Log.w("MultiDex", "File " + paramFile.getAbsolutePath() + " is not a valid zip file.", localZipException);
    }
    catch (IOException localIOException2)
    {
      while (true)
        Log.w("MultiDex", "Got an IOException trying to open zip file: " + paramFile.getAbsolutePath(), localIOException2);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.multidex.MultiDexExtractor
 * JD-Core Version:    0.6.0
 */