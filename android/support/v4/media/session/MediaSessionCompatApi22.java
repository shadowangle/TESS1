package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.media.session.MediaSession;
import android.support.annotation.RequiresApi;

@TargetApi(22)
@RequiresApi(22)
class MediaSessionCompatApi22
{
  public static void setRatingType(Object paramObject, int paramInt)
  {
    ((MediaSession)paramObject).setRatingType(paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.session.MediaSessionCompatApi22
 * JD-Core Version:    0.6.0
 */