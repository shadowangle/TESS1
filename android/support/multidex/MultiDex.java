package android.support.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class MultiDex
{
  private static final boolean IS_VM_MULTIDEX_CAPABLE = false;
  private static final int MAX_SUPPORTED_SDK_VERSION = 20;
  private static final int MIN_SDK_VERSION = 4;
  private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
  private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
  static final String TAG = "MultiDex";
  private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
  private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
  private static final Set<String> installedApk = new HashSet();

  static
  {
    IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
  }

  private static boolean checkValidZipFiles(List<File> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
      if (!MultiDexExtractor.verifyZipFile((File)localIterator.next()))
        return false;
    return true;
  }

  private static void clearOldDexDir(Context paramContext)
    throws Exception
  {
    File localFile1 = new File(paramContext.getFilesDir(), "secondary-dexes");
    File[] arrayOfFile;
    if (localFile1.isDirectory())
    {
      Log.i("MultiDex", "Clearing old secondary dex dir (" + localFile1.getPath() + ").");
      arrayOfFile = localFile1.listFiles();
      if (arrayOfFile == null)
        Log.w("MultiDex", "Failed to list secondary dex dir content (" + localFile1.getPath() + ").");
    }
    else
    {
      return;
    }
    int i = arrayOfFile.length;
    int j = 0;
    if (j < i)
    {
      File localFile2 = arrayOfFile[j];
      Log.i("MultiDex", "Trying to delete old file " + localFile2.getPath() + " of size " + localFile2.length());
      if (!localFile2.delete())
        Log.w("MultiDex", "Failed to delete old file " + localFile2.getPath());
      while (true)
      {
        j++;
        break;
        Log.i("MultiDex", "Deleted old file " + localFile2.getPath());
      }
    }
    if (!localFile1.delete())
    {
      Log.w("MultiDex", "Failed to delete secondary dex dir " + localFile1.getPath());
      return;
    }
    Log.i("MultiDex", "Deleted old secondary dex dir " + localFile1.getPath());
  }

  private static void expandFieldArray(Object paramObject, String paramString, Object[] paramArrayOfObject)
    throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    Field localField = findField(paramObject, paramString);
    Object[] arrayOfObject1 = (Object[])(Object[])localField.get(paramObject);
    Object[] arrayOfObject2 = (Object[])(Object[])Array.newInstance(arrayOfObject1.getClass().getComponentType(), arrayOfObject1.length + paramArrayOfObject.length);
    System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, arrayOfObject1.length);
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject2, arrayOfObject1.length, paramArrayOfObject.length);
    localField.set(paramObject, arrayOfObject2);
  }

  private static Field findField(Object paramObject, String paramString)
    throws NoSuchFieldException
  {
    Class localClass = paramObject.getClass();
    while (localClass != null)
      try
      {
        Field localField = localClass.getDeclaredField(paramString);
        if (!localField.isAccessible())
          localField.setAccessible(true);
        return localField;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        localClass = localClass.getSuperclass();
      }
    throw new NoSuchFieldException("Field " + paramString + " not found in " + paramObject.getClass());
  }

  private static Method findMethod(Object paramObject, String paramString, Class<?>[] paramArrayOfClass)
    throws NoSuchMethodException
  {
    Class localClass = paramObject.getClass();
    while (localClass != null)
      try
      {
        Method localMethod = localClass.getDeclaredMethod(paramString, paramArrayOfClass);
        if (!localMethod.isAccessible())
          localMethod.setAccessible(true);
        return localMethod;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        localClass = localClass.getSuperclass();
      }
    throw new NoSuchMethodException("Method " + paramString + " with parameters " + Arrays.asList(paramArrayOfClass) + " not found in " + paramObject.getClass());
  }

  private static ApplicationInfo getApplicationInfo(Context paramContext)
    throws PackageManager.NameNotFoundException
  {
    PackageManager localPackageManager;
    String str;
    try
    {
      localPackageManager = paramContext.getPackageManager();
      str = paramContext.getPackageName();
      if ((localPackageManager == null) || (str == null))
        return null;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("MultiDex", "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", localRuntimeException);
      return null;
    }
    return localPackageManager.getApplicationInfo(str, 128);
  }

  public static void install(Context paramContext)
  {
    Log.i("MultiDex", "install");
    if (IS_VM_MULTIDEX_CAPABLE)
      Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
    ApplicationInfo localApplicationInfo;
    String str;
    while (true)
    {
      return;
      if (Build.VERSION.SDK_INT < 4)
        throw new RuntimeException("Multi dex installation failed. SDK " + Build.VERSION.SDK_INT + " is unsupported. Min SDK version is " + 4 + ".");
      try
      {
        localApplicationInfo = getApplicationInfo(paramContext);
        if (localApplicationInfo == null)
          continue;
        synchronized (installedApk)
        {
          str = localApplicationInfo.sourceDir;
          if (!installedApk.contains(str))
            break;
          return;
        }
      }
      catch (Exception localException)
      {
        Log.e("MultiDex", "Multidex installation failure", localException);
        throw new RuntimeException("Multi dex installation failed (" + localException.getMessage() + ").");
      }
    }
    installedApk.add(str);
    if (Build.VERSION.SDK_INT > 20)
      Log.w("MultiDex", "MultiDex is not guaranteed to work in SDK version " + Build.VERSION.SDK_INT + ": SDK version higher than " + 20 + " should be backed by " + "runtime with built-in multidex capabilty but it's not the " + "case here: java.vm.version=\"" + System.getProperty("java.vm.version") + "\"");
    ClassLoader localClassLoader;
    try
    {
      localClassLoader = paramContext.getClassLoader();
      if (localClassLoader == null)
      {
        Log.e("MultiDex", "Context class loader is null. Must be running in test mode. Skip patching.");
        monitorexit;
        return;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("MultiDex", "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", localRuntimeException);
      monitorexit;
      return;
    }
    try
    {
      clearOldDexDir(paramContext);
      localFile = new File(localApplicationInfo.dataDir, SECONDARY_FOLDER_NAME);
      List localList1 = MultiDexExtractor.load(paramContext, localApplicationInfo, localFile, false);
      if (checkValidZipFiles(localList1))
      {
        installSecondaryDexes(localClassLoader, localFile, localList1);
        monitorexit;
        Log.i("MultiDex", "install done");
        return;
      }
    }
    catch (Throwable localThrowable)
    {
      while (true)
      {
        File localFile;
        Log.w("MultiDex", "Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", localThrowable);
        continue;
        Log.w("MultiDex", "Files were not valid zip files.  Forcing a reload.");
        List localList2 = MultiDexExtractor.load(paramContext, localApplicationInfo, localFile, true);
        if (!checkValidZipFiles(localList2))
          break;
        installSecondaryDexes(localClassLoader, localFile, localList2);
      }
    }
    throw new RuntimeException("Zip files were not valid.");
  }

  private static void installSecondaryDexes(ClassLoader paramClassLoader, File paramFile, List<File> paramList)
    throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException
  {
    if (!paramList.isEmpty())
    {
      if (Build.VERSION.SDK_INT >= 19)
        V19.access$000(paramClassLoader, paramList, paramFile);
    }
    else
      return;
    if (Build.VERSION.SDK_INT >= 14)
    {
      V14.access$100(paramClassLoader, paramList, paramFile);
      return;
    }
    V4.access$200(paramClassLoader, paramList);
  }

  static boolean isVMMultidexCapable(String paramString)
  {
    int i = 0;
    Matcher localMatcher;
    if (paramString != null)
    {
      localMatcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(paramString);
      boolean bool = localMatcher.matches();
      i = 0;
      if (!bool);
    }
    try
    {
      int j = Integer.parseInt(localMatcher.group(1));
      int k = Integer.parseInt(localMatcher.group(2));
      if (j <= 2)
      {
        i = 0;
        if (j == 2)
        {
          i = 0;
          if (k < 1);
        }
      }
      else
      {
        i = 1;
      }
      StringBuilder localStringBuilder = new StringBuilder().append("VM with version ").append(paramString);
      if (i != 0);
      for (String str = " has multidex support"; ; str = " does not have multidex support")
      {
        Log.i("MultiDex", str);
        return i;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        i = 0;
    }
  }

  private static final class V14
  {
    private static void install(ClassLoader paramClassLoader, List<File> paramList, File paramFile)
      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException
    {
      Object localObject = MultiDex.access$300(paramClassLoader, "pathList").get(paramClassLoader);
      MultiDex.access$400(localObject, "dexElements", makeDexElements(localObject, new ArrayList(paramList), paramFile));
    }

    private static Object[] makeDexElements(Object paramObject, ArrayList<File> paramArrayList, File paramFile)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
      return (Object[])(Object[])MultiDex.access$500(paramObject, "makeDexElements", new Class[] { ArrayList.class, File.class }).invoke(paramObject, new Object[] { paramArrayList, paramFile });
    }
  }

  private static final class V19
  {
    private static void install(ClassLoader paramClassLoader, List<File> paramList, File paramFile)
      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException
    {
      Object localObject1 = MultiDex.access$300(paramClassLoader, "pathList").get(paramClassLoader);
      ArrayList localArrayList = new ArrayList();
      MultiDex.access$400(localObject1, "dexElements", makeDexElements(localObject1, new ArrayList(paramList), paramFile, localArrayList));
      Field localField;
      IOException[] arrayOfIOException1;
      if (localArrayList.size() > 0)
      {
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
          Log.w("MultiDex", "Exception in makeDexElement", (IOException)localIterator.next());
        localField = MultiDex.access$300(paramClassLoader, "dexElementsSuppressedExceptions");
        arrayOfIOException1 = (IOException[])(IOException[])localField.get(paramClassLoader);
        if (arrayOfIOException1 != null)
          break label141;
      }
      label141: IOException[] arrayOfIOException2;
      for (Object localObject2 = (IOException[])localArrayList.toArray(new IOException[localArrayList.size()]); ; localObject2 = arrayOfIOException2)
      {
        localField.set(paramClassLoader, localObject2);
        return;
        arrayOfIOException2 = new IOException[localArrayList.size() + arrayOfIOException1.length];
        localArrayList.toArray(arrayOfIOException2);
        System.arraycopy(arrayOfIOException1, 0, arrayOfIOException2, localArrayList.size(), arrayOfIOException1.length);
      }
    }

    private static Object[] makeDexElements(Object paramObject, ArrayList<File> paramArrayList, File paramFile, ArrayList<IOException> paramArrayList1)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
      return (Object[])(Object[])MultiDex.access$500(paramObject, "makeDexElements", new Class[] { ArrayList.class, File.class, ArrayList.class }).invoke(paramObject, new Object[] { paramArrayList, paramFile, paramArrayList1 });
    }
  }

  private static final class V4
  {
    private static void install(ClassLoader paramClassLoader, List<File> paramList)
      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException
    {
      int i = paramList.size();
      Field localField = MultiDex.access$300(paramClassLoader, "path");
      StringBuilder localStringBuilder = new StringBuilder((String)localField.get(paramClassLoader));
      String[] arrayOfString = new String[i];
      File[] arrayOfFile = new File[i];
      ZipFile[] arrayOfZipFile = new ZipFile[i];
      DexFile[] arrayOfDexFile = new DexFile[i];
      ListIterator localListIterator = paramList.listIterator();
      while (localListIterator.hasNext())
      {
        File localFile = (File)localListIterator.next();
        String str = localFile.getAbsolutePath();
        localStringBuilder.append(':').append(str);
        int j = localListIterator.previousIndex();
        arrayOfString[j] = str;
        arrayOfFile[j] = localFile;
        arrayOfZipFile[j] = new ZipFile(localFile);
        arrayOfDexFile[j] = DexFile.loadDex(str, str + ".dex", 0);
      }
      localField.set(paramClassLoader, localStringBuilder.toString());
      MultiDex.access$400(paramClassLoader, "mPaths", arrayOfString);
      MultiDex.access$400(paramClassLoader, "mFiles", arrayOfFile);
      MultiDex.access$400(paramClassLoader, "mZips", arrayOfZipFile);
      MultiDex.access$400(paramClassLoader, "mDexs", arrayOfDexFile);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.multidex.MultiDex
 * JD-Core Version:    0.6.0
 */