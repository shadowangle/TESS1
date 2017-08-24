package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.media.session.PlaybackState;
import android.media.session.PlaybackState.Builder;
import android.media.session.PlaybackState.CustomAction;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.util.Iterator;
import java.util.List;

@TargetApi(22)
@RequiresApi(22)
class PlaybackStateCompatApi22
{
  public static Bundle getExtras(Object paramObject)
  {
    return ((PlaybackState)paramObject).getExtras();
  }

  public static Object newInstance(int paramInt, long paramLong1, long paramLong2, float paramFloat, long paramLong3, CharSequence paramCharSequence, long paramLong4, List<Object> paramList, long paramLong5, Bundle paramBundle)
  {
    PlaybackState.Builder localBuilder = new PlaybackState.Builder();
    localBuilder.setState(paramInt, paramLong1, paramFloat, paramLong4);
    localBuilder.setBufferedPosition(paramLong2);
    localBuilder.setActions(paramLong3);
    localBuilder.setErrorMessage(paramCharSequence);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
      localBuilder.addCustomAction((PlaybackState.CustomAction)localIterator.next());
    localBuilder.setActiveQueueItemId(paramLong5);
    localBuilder.setExtras(paramBundle);
    return localBuilder.build();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.session.PlaybackStateCompatApi22
 * JD-Core Version:    0.6.0
 */