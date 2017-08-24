package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Method;

@TargetApi(9)
@RequiresApi(9)
@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class NotificationCompatBase
{
  private static Method sSetLatestEventInfo;

  // ERROR //
  public static android.app.Notification add(android.app.Notification paramNotification, android.content.Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    // Byte code:
    //   0: getstatic 27	android/support/v4/app/NotificationCompatBase:sSetLatestEventInfo	Ljava/lang/reflect/Method;
    //   3: ifnonnull +37 -> 40
    //   6: ldc 29
    //   8: ldc 31
    //   10: iconst_4
    //   11: anewarray 33	java/lang/Class
    //   14: dup
    //   15: iconst_0
    //   16: ldc 35
    //   18: aastore
    //   19: dup
    //   20: iconst_1
    //   21: ldc 37
    //   23: aastore
    //   24: dup
    //   25: iconst_2
    //   26: ldc 37
    //   28: aastore
    //   29: dup
    //   30: iconst_3
    //   31: ldc 39
    //   33: aastore
    //   34: invokevirtual 43	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   37: putstatic 27	android/support/v4/app/NotificationCompatBase:sSetLatestEventInfo	Ljava/lang/reflect/Method;
    //   40: getstatic 27	android/support/v4/app/NotificationCompatBase:sSetLatestEventInfo	Ljava/lang/reflect/Method;
    //   43: aload_0
    //   44: iconst_4
    //   45: anewarray 4	java/lang/Object
    //   48: dup
    //   49: iconst_0
    //   50: aload_1
    //   51: aastore
    //   52: dup
    //   53: iconst_1
    //   54: aload_2
    //   55: aastore
    //   56: dup
    //   57: iconst_2
    //   58: aload_3
    //   59: aastore
    //   60: dup
    //   61: iconst_3
    //   62: aload 4
    //   64: aastore
    //   65: invokevirtual 49	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   68: pop
    //   69: aload_0
    //   70: aload 5
    //   72: putfield 53	android/app/Notification:fullScreenIntent	Landroid/app/PendingIntent;
    //   75: aload_0
    //   76: areturn
    //   77: astore 8
    //   79: new 55	java/lang/RuntimeException
    //   82: dup
    //   83: aload 8
    //   85: invokespecial 58	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   88: athrow
    //   89: astore 6
    //   91: new 55	java/lang/RuntimeException
    //   94: dup
    //   95: aload 6
    //   97: invokespecial 58	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   100: athrow
    //   101: astore 6
    //   103: goto -12 -> 91
    //
    // Exception table:
    //   from	to	target	type
    //   6	40	77	java/lang/NoSuchMethodException
    //   40	69	89	java/lang/reflect/InvocationTargetException
    //   40	69	101	java/lang/IllegalAccessException
  }

  public static abstract class Action
  {
    public abstract PendingIntent getActionIntent();

    public abstract boolean getAllowGeneratedReplies();

    public abstract Bundle getExtras();

    public abstract int getIcon();

    public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

    public abstract CharSequence getTitle();

    public static abstract interface Factory
    {
      public abstract NotificationCompatBase.Action build(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent, Bundle paramBundle, RemoteInputCompatBase.RemoteInput[] paramArrayOfRemoteInput, boolean paramBoolean);

      public abstract NotificationCompatBase.Action[] newArray(int paramInt);
    }
  }

  public static abstract class UnreadConversation
  {
    abstract long getLatestTimestamp();

    abstract String[] getMessages();

    abstract String getParticipant();

    abstract String[] getParticipants();

    abstract PendingIntent getReadPendingIntent();

    abstract RemoteInputCompatBase.RemoteInput getRemoteInput();

    abstract PendingIntent getReplyPendingIntent();

    public static abstract interface Factory
    {
      public abstract NotificationCompatBase.UnreadConversation build(String[] paramArrayOfString1, RemoteInputCompatBase.RemoteInput paramRemoteInput, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, String[] paramArrayOfString2, long paramLong);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.NotificationCompatBase
 * JD-Core Version:    0.6.0
 */