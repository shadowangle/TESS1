package android.support.v4.app;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Method;

@TargetApi(9)
@RequiresApi(9)
class BundleCompatGingerbread
{
  private static final String TAG = "BundleCompatGingerbread";
  private static Method sGetIBinderMethod;
  private static boolean sGetIBinderMethodFetched;
  private static Method sPutIBinderMethod;
  private static boolean sPutIBinderMethodFetched;

  // ERROR //
  public static android.os.IBinder getBinder(android.os.Bundle paramBundle, String paramString)
  {
    // Byte code:
    //   0: getstatic 34	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethodFetched	Z
    //   3: ifne +33 -> 36
    //   6: ldc 36
    //   8: ldc 38
    //   10: iconst_1
    //   11: anewarray 40	java/lang/Class
    //   14: dup
    //   15: iconst_0
    //   16: ldc 42
    //   18: aastore
    //   19: invokevirtual 46	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   22: putstatic 48	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethod	Ljava/lang/reflect/Method;
    //   25: getstatic 48	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethod	Ljava/lang/reflect/Method;
    //   28: iconst_1
    //   29: invokevirtual 54	java/lang/reflect/Method:setAccessible	(Z)V
    //   32: iconst_1
    //   33: putstatic 34	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethodFetched	Z
    //   36: getstatic 48	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethod	Ljava/lang/reflect/Method;
    //   39: ifnull +55 -> 94
    //   42: getstatic 48	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethod	Ljava/lang/reflect/Method;
    //   45: aload_0
    //   46: iconst_1
    //   47: anewarray 4	java/lang/Object
    //   50: dup
    //   51: iconst_0
    //   52: aload_1
    //   53: aastore
    //   54: invokevirtual 58	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   57: checkcast 60	android/os/IBinder
    //   60: astore 4
    //   62: aload 4
    //   64: areturn
    //   65: astore 5
    //   67: ldc 12
    //   69: ldc 62
    //   71: aload 5
    //   73: invokestatic 68	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   76: pop
    //   77: goto -45 -> 32
    //   80: astore_2
    //   81: ldc 12
    //   83: ldc 70
    //   85: aload_2
    //   86: invokestatic 68	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: pop
    //   90: aconst_null
    //   91: putstatic 48	android/support/v4/app/BundleCompatGingerbread:sGetIBinderMethod	Ljava/lang/reflect/Method;
    //   94: aconst_null
    //   95: areturn
    //   96: astore_2
    //   97: goto -16 -> 81
    //   100: astore_2
    //   101: goto -20 -> 81
    //
    // Exception table:
    //   from	to	target	type
    //   6	32	65	java/lang/NoSuchMethodException
    //   42	62	80	java/lang/reflect/InvocationTargetException
    //   42	62	96	java/lang/IllegalArgumentException
    //   42	62	100	java/lang/IllegalAccessException
  }

  // ERROR //
  public static void putBinder(android.os.Bundle paramBundle, String paramString, android.os.IBinder paramIBinder)
  {
    // Byte code:
    //   0: getstatic 74	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethodFetched	Z
    //   3: ifne +38 -> 41
    //   6: ldc 36
    //   8: ldc 76
    //   10: iconst_2
    //   11: anewarray 40	java/lang/Class
    //   14: dup
    //   15: iconst_0
    //   16: ldc 42
    //   18: aastore
    //   19: dup
    //   20: iconst_1
    //   21: ldc 60
    //   23: aastore
    //   24: invokevirtual 46	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   27: putstatic 78	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethod	Ljava/lang/reflect/Method;
    //   30: getstatic 78	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethod	Ljava/lang/reflect/Method;
    //   33: iconst_1
    //   34: invokevirtual 54	java/lang/reflect/Method:setAccessible	(Z)V
    //   37: iconst_1
    //   38: putstatic 74	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethodFetched	Z
    //   41: getstatic 78	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethod	Ljava/lang/reflect/Method;
    //   44: ifnull +23 -> 67
    //   47: getstatic 78	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethod	Ljava/lang/reflect/Method;
    //   50: aload_0
    //   51: iconst_2
    //   52: anewarray 4	java/lang/Object
    //   55: dup
    //   56: iconst_0
    //   57: aload_1
    //   58: aastore
    //   59: dup
    //   60: iconst_1
    //   61: aload_2
    //   62: aastore
    //   63: invokevirtual 58	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   66: pop
    //   67: return
    //   68: astore 6
    //   70: ldc 12
    //   72: ldc 80
    //   74: aload 6
    //   76: invokestatic 68	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   79: pop
    //   80: goto -43 -> 37
    //   83: astore_3
    //   84: ldc 12
    //   86: ldc 82
    //   88: aload_3
    //   89: invokestatic 68	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   92: pop
    //   93: aconst_null
    //   94: putstatic 78	android/support/v4/app/BundleCompatGingerbread:sPutIBinderMethod	Ljava/lang/reflect/Method;
    //   97: return
    //   98: astore_3
    //   99: goto -15 -> 84
    //   102: astore_3
    //   103: goto -19 -> 84
    //
    // Exception table:
    //   from	to	target	type
    //   6	37	68	java/lang/NoSuchMethodException
    //   47	67	83	java/lang/reflect/InvocationTargetException
    //   47	67	98	java/lang/IllegalArgumentException
    //   47	67	102	java/lang/IllegalAccessException
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.BundleCompatGingerbread
 * JD-Core Version:    0.6.0
 */