package android.support.v4.media;

import android.annotation.TargetApi;
import android.media.browse.MediaBrowser.MediaItem;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@TargetApi(21)
@RequiresApi(21)
class ParceledListSliceAdapterApi21
{
  private static Constructor sConstructor;

  static
  {
    try
    {
      sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(new Class[] { List.class });
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      label22: break label22;
    }
  }

  static Object newInstance(List<MediaBrowser.MediaItem> paramList)
  {
    try
    {
      Object localObject = sConstructor.newInstance(new Object[] { paramList });
      return localObject;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localInvocationTargetException.printStackTrace();
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label18;
    }
    catch (InstantiationException localInstantiationException)
    {
      label18: break label18;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.ParceledListSliceAdapterApi21
 * JD-Core Version:    0.6.0
 */