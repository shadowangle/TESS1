package android.support.v4.media;

import android.annotation.TargetApi;
import android.media.MediaDescription;
import android.media.MediaDescription.Builder;
import android.net.Uri;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class MediaDescriptionCompatApi23 extends MediaDescriptionCompatApi21
{
  public static Uri getMediaUri(Object paramObject)
  {
    return ((MediaDescription)paramObject).getMediaUri();
  }

  static class Builder extends MediaDescriptionCompatApi21.Builder
  {
    public static void setMediaUri(Object paramObject, Uri paramUri)
    {
      ((MediaDescription.Builder)paramObject).setMediaUri(paramUri);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaDescriptionCompatApi23
 * JD-Core Version:    0.6.0
 */