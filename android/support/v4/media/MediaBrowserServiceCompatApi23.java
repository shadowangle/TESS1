package android.support.v4.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.browse.MediaBrowser.MediaItem;
import android.os.Parcel;
import android.service.media.MediaBrowserService.Result;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class MediaBrowserServiceCompatApi23
{
  public static Object createService(Context paramContext, ServiceCompatProxy paramServiceCompatProxy)
  {
    return new MediaBrowserServiceAdaptor(paramContext, paramServiceCompatProxy);
  }

  static class MediaBrowserServiceAdaptor extends MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor
  {
    MediaBrowserServiceAdaptor(Context paramContext, MediaBrowserServiceCompatApi23.ServiceCompatProxy paramServiceCompatProxy)
    {
      super(paramServiceCompatProxy);
    }

    public void onLoadItem(String paramString, MediaBrowserService.Result<MediaBrowser.MediaItem> paramResult)
    {
      ((MediaBrowserServiceCompatApi23.ServiceCompatProxy)this.mServiceProxy).onLoadItem(paramString, new MediaBrowserServiceCompatApi21.ResultWrapper(paramResult));
    }
  }

  public static abstract interface ServiceCompatProxy extends MediaBrowserServiceCompatApi21.ServiceCompatProxy
  {
    public abstract void onLoadItem(String paramString, MediaBrowserServiceCompatApi21.ResultWrapper<Parcel> paramResultWrapper);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaBrowserServiceCompatApi23
 * JD-Core Version:    0.6.0
 */