package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class MediaBrowserCompatUtils
{
  public static boolean areSameOptions(Bundle paramBundle1, Bundle paramBundle2)
  {
    if (paramBundle1 == paramBundle2);
    do
      while (true)
      {
        return true;
        if (paramBundle1 == null)
          if ((paramBundle2.getInt("android.media.browse.extra.PAGE", -1) != -1) || (paramBundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1))
            return false;
        if (paramBundle2 != null)
          break;
        if ((paramBundle1.getInt("android.media.browse.extra.PAGE", -1) != -1) || (paramBundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1))
          return false;
      }
    while ((paramBundle1.getInt("android.media.browse.extra.PAGE", -1) == paramBundle2.getInt("android.media.browse.extra.PAGE", -1)) && (paramBundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1) == paramBundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1)));
    return false;
  }

  public static boolean hasDuplicatedItems(Bundle paramBundle1, Bundle paramBundle2)
  {
    int i = 2147483647;
    int j;
    int k;
    label16: int m;
    label23: int n;
    label30: int i1;
    int i2;
    label47: int i3;
    if (paramBundle1 == null)
    {
      j = -1;
      if (paramBundle2 != null)
        break label89;
      k = -1;
      if (paramBundle1 != null)
        break label101;
      m = -1;
      if (paramBundle2 != null)
        break label113;
      n = -1;
      if ((j != -1) && (m != -1))
        break label125;
      i1 = 0;
      i2 = i;
      if ((k != -1) && (n != -1))
        break label147;
      i3 = 0;
      label62: if ((i1 > i3) || (i3 > i2))
        break label165;
    }
    label89: label101: label113: label125: 
    do
    {
      return true;
      j = paramBundle1.getInt("android.media.browse.extra.PAGE", -1);
      break;
      k = paramBundle2.getInt("android.media.browse.extra.PAGE", -1);
      break label16;
      m = paramBundle1.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      break label23;
      n = paramBundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
      break label30;
      int i4 = m * j;
      i2 = -1 + (m + i4);
      i1 = i4;
      break label47;
      i3 = n * k;
      i = -1 + (i3 + n);
      break label62;
    }
    while ((i1 <= i) && (i <= i2));
    label147: label165: return false;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaBrowserCompatUtils
 * JD-Core Version:    0.6.0
 */