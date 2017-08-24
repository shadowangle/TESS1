package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.RemoteInput.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(21)
@RequiresApi(21)
class NotificationCompatApi21
{
  public static final String CATEGORY_ALARM = "alarm";
  public static final String CATEGORY_CALL = "call";
  public static final String CATEGORY_EMAIL = "email";
  public static final String CATEGORY_ERROR = "err";
  public static final String CATEGORY_EVENT = "event";
  public static final String CATEGORY_MESSAGE = "msg";
  public static final String CATEGORY_PROGRESS = "progress";
  public static final String CATEGORY_PROMO = "promo";
  public static final String CATEGORY_RECOMMENDATION = "recommendation";
  public static final String CATEGORY_SERVICE = "service";
  public static final String CATEGORY_SOCIAL = "social";
  public static final String CATEGORY_STATUS = "status";
  public static final String CATEGORY_SYSTEM = "sys";
  public static final String CATEGORY_TRANSPORT = "transport";
  private static final String KEY_AUTHOR = "author";
  private static final String KEY_MESSAGES = "messages";
  private static final String KEY_ON_READ = "on_read";
  private static final String KEY_ON_REPLY = "on_reply";
  private static final String KEY_PARTICIPANTS = "participants";
  private static final String KEY_REMOTE_INPUT = "remote_input";
  private static final String KEY_TEXT = "text";
  private static final String KEY_TIMESTAMP = "timestamp";

  private static RemoteInput fromCompatRemoteInput(RemoteInputCompatBase.RemoteInput paramRemoteInput)
  {
    return new RemoteInput.Builder(paramRemoteInput.getResultKey()).setLabel(paramRemoteInput.getLabel()).setChoices(paramRemoteInput.getChoices()).setAllowFreeFormInput(paramRemoteInput.getAllowFreeFormInput()).addExtras(paramRemoteInput.getExtras()).build();
  }

  static Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation paramUnreadConversation)
  {
    int i = 0;
    if (paramUnreadConversation == null)
      return null;
    Bundle localBundle1 = new Bundle();
    String[] arrayOfString = paramUnreadConversation.getParticipants();
    String str = null;
    if (arrayOfString != null)
    {
      int j = paramUnreadConversation.getParticipants().length;
      str = null;
      if (j > 1)
        str = paramUnreadConversation.getParticipants()[0];
    }
    Parcelable[] arrayOfParcelable = new Parcelable[paramUnreadConversation.getMessages().length];
    while (i < arrayOfParcelable.length)
    {
      Bundle localBundle2 = new Bundle();
      localBundle2.putString("text", paramUnreadConversation.getMessages()[i]);
      localBundle2.putString("author", str);
      arrayOfParcelable[i] = localBundle2;
      i++;
    }
    localBundle1.putParcelableArray("messages", arrayOfParcelable);
    RemoteInputCompatBase.RemoteInput localRemoteInput = paramUnreadConversation.getRemoteInput();
    if (localRemoteInput != null)
      localBundle1.putParcelable("remote_input", fromCompatRemoteInput(localRemoteInput));
    localBundle1.putParcelable("on_reply", paramUnreadConversation.getReplyPendingIntent());
    localBundle1.putParcelable("on_read", paramUnreadConversation.getReadPendingIntent());
    localBundle1.putStringArray("participants", paramUnreadConversation.getParticipants());
    localBundle1.putLong("timestamp", paramUnreadConversation.getLatestTimestamp());
    return localBundle1;
  }

  public static String getCategory(Notification paramNotification)
  {
    return paramNotification.category;
  }

  static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle paramBundle, NotificationCompatBase.UnreadConversation.Factory paramFactory, RemoteInputCompatBase.RemoteInput.Factory paramFactory1)
  {
    if (paramBundle == null)
      return null;
    Parcelable[] arrayOfParcelable = paramBundle.getParcelableArray("messages");
    String[] arrayOfString3;
    int i;
    int j;
    if (arrayOfParcelable != null)
    {
      arrayOfString3 = new String[arrayOfParcelable.length];
      i = 0;
      if (i < arrayOfString3.length)
      {
        boolean bool = arrayOfParcelable[i] instanceof Bundle;
        j = 0;
        if (!bool)
          label52: if (j == 0)
            break label198;
      }
    }
    for (String[] arrayOfString1 = arrayOfString3; ; arrayOfString1 = null)
    {
      PendingIntent localPendingIntent1 = (PendingIntent)paramBundle.getParcelable("on_read");
      PendingIntent localPendingIntent2 = (PendingIntent)paramBundle.getParcelable("on_reply");
      RemoteInput localRemoteInput = (RemoteInput)paramBundle.getParcelable("remote_input");
      String[] arrayOfString2 = paramBundle.getStringArray("participants");
      if ((arrayOfString2 == null) || (arrayOfString2.length != 1))
        break;
      if (localRemoteInput != null);
      for (RemoteInputCompatBase.RemoteInput localRemoteInput1 = toCompatRemoteInput(localRemoteInput, paramFactory1); ; localRemoteInput1 = null)
      {
        return paramFactory.build(arrayOfString1, localRemoteInput1, localPendingIntent2, localPendingIntent1, arrayOfString2, paramBundle.getLong("timestamp"));
        arrayOfString3[i] = ((Bundle)arrayOfParcelable[i]).getString("text");
        String str = arrayOfString3[i];
        j = 0;
        if (str == null)
          break label52;
        i++;
        break;
      }
      j = 1;
      break label52;
      label198: break;
    }
  }

  private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(RemoteInput paramRemoteInput, RemoteInputCompatBase.RemoteInput.Factory paramFactory)
  {
    return paramFactory.build(paramRemoteInput.getResultKey(), paramRemoteInput.getLabel(), paramRemoteInput.getChoices(), paramRemoteInput.getAllowFreeFormInput(), paramRemoteInput.getExtras());
  }

  public static class Builder
    implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
  {
    private Notification.Builder b;
    private RemoteViews mBigContentView;
    private RemoteViews mContentView;
    private Bundle mExtras;
    private RemoteViews mHeadsUpContentView;

    public Builder(Context paramContext, Notification paramNotification1, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, RemoteViews paramRemoteViews1, int paramInt1, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, Bitmap paramBitmap, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt4, CharSequence paramCharSequence4, boolean paramBoolean4, String paramString1, ArrayList<String> paramArrayList, Bundle paramBundle, int paramInt5, int paramInt6, Notification paramNotification2, String paramString2, boolean paramBoolean5, String paramString3, RemoteViews paramRemoteViews2, RemoteViews paramRemoteViews3, RemoteViews paramRemoteViews4)
    {
      Notification.Builder localBuilder1 = new Notification.Builder(paramContext).setWhen(paramNotification1.when).setShowWhen(paramBoolean2).setSmallIcon(paramNotification1.icon, paramNotification1.iconLevel).setContent(paramNotification1.contentView).setTicker(paramNotification1.tickerText, paramRemoteViews1).setSound(paramNotification1.sound, paramNotification1.audioStreamType).setVibrate(paramNotification1.vibrate).setLights(paramNotification1.ledARGB, paramNotification1.ledOnMS, paramNotification1.ledOffMS);
      boolean bool1;
      boolean bool2;
      label120: boolean bool3;
      label142: Notification.Builder localBuilder4;
      if ((0x2 & paramNotification1.flags) != 0)
      {
        bool1 = true;
        Notification.Builder localBuilder2 = localBuilder1.setOngoing(bool1);
        if ((0x8 & paramNotification1.flags) == 0)
          break label358;
        bool2 = true;
        Notification.Builder localBuilder3 = localBuilder2.setOnlyAlertOnce(bool2);
        if ((0x10 & paramNotification1.flags) == 0)
          break label364;
        bool3 = true;
        localBuilder4 = localBuilder3.setAutoCancel(bool3).setDefaults(paramNotification1.defaults).setContentTitle(paramCharSequence1).setContentText(paramCharSequence2).setSubText(paramCharSequence4).setContentInfo(paramCharSequence3).setContentIntent(paramPendingIntent1).setDeleteIntent(paramNotification1.deleteIntent);
        if ((0x80 & paramNotification1.flags) == 0)
          break label370;
      }
      label358: label364: label370: for (boolean bool4 = true; ; bool4 = false)
      {
        this.b = localBuilder4.setFullScreenIntent(paramPendingIntent2, bool4).setLargeIcon(paramBitmap).setNumber(paramInt1).setUsesChronometer(paramBoolean3).setPriority(paramInt4).setProgress(paramInt2, paramInt3, paramBoolean1).setLocalOnly(paramBoolean4).setGroup(paramString2).setGroupSummary(paramBoolean5).setSortKey(paramString3).setCategory(paramString1).setColor(paramInt5).setVisibility(paramInt6).setPublicVersion(paramNotification2);
        this.mExtras = new Bundle();
        if (paramBundle != null)
          this.mExtras.putAll(paramBundle);
        Iterator localIterator = paramArrayList.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          this.b.addPerson(str);
        }
        bool1 = false;
        break;
        bool2 = false;
        break label120;
        bool3 = false;
        break label142;
      }
      this.mContentView = paramRemoteViews2;
      this.mBigContentView = paramRemoteViews3;
      this.mHeadsUpContentView = paramRemoteViews4;
    }

    public void addAction(NotificationCompatBase.Action paramAction)
    {
      NotificationCompatApi20.addAction(this.b, paramAction);
    }

    public Notification build()
    {
      this.b.setExtras(this.mExtras);
      Notification localNotification = this.b.build();
      if (this.mContentView != null)
        localNotification.contentView = this.mContentView;
      if (this.mBigContentView != null)
        localNotification.bigContentView = this.mBigContentView;
      if (this.mHeadsUpContentView != null)
        localNotification.headsUpContentView = this.mHeadsUpContentView;
      return localNotification;
    }

    public Notification.Builder getBuilder()
    {
      return this.b;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.NotificationCompatApi21
 * JD-Core Version:    0.6.0
 */