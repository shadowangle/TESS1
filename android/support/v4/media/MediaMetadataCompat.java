package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Set;

public final class MediaMetadataCompat
  implements Parcelable
{
  public static final Parcelable.Creator<MediaMetadataCompat> CREATOR;
  static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = new ArrayMap();
  public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
  public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
  public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
  public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
  public static final String METADATA_KEY_ART = "android.media.metadata.ART";
  public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
  public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
  public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
  public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
  public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
  public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
  public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
  public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
  public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
  public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
  public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
  public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
  public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
  public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
  public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
  public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
  public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
  public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
  public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
  public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
  public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
  public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
  public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
  public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
  static final int METADATA_TYPE_BITMAP = 2;
  static final int METADATA_TYPE_LONG = 0;
  static final int METADATA_TYPE_RATING = 3;
  static final int METADATA_TYPE_TEXT = 1;
  private static final String[] PREFERRED_BITMAP_ORDER;
  private static final String[] PREFERRED_DESCRIPTION_ORDER;
  private static final String[] PREFERRED_URI_ORDER;
  private static final String TAG = "MediaMetadata";
  final Bundle mBundle;
  private MediaDescriptionCompat mDescription;
  private Object mMetadataObj;

  static
  {
    METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DATE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", Integer.valueOf(1));
    PREFERRED_DESCRIPTION_ORDER = new String[] { "android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER" };
    PREFERRED_BITMAP_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART" };
    PREFERRED_URI_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI" };
    CREATOR = new Parcelable.Creator()
    {
      public MediaMetadataCompat createFromParcel(Parcel paramParcel)
      {
        return new MediaMetadataCompat(paramParcel);
      }

      public MediaMetadataCompat[] newArray(int paramInt)
      {
        return new MediaMetadataCompat[paramInt];
      }
    };
  }

  MediaMetadataCompat(Bundle paramBundle)
  {
    this.mBundle = new Bundle(paramBundle);
  }

  MediaMetadataCompat(Parcel paramParcel)
  {
    this.mBundle = paramParcel.readBundle();
  }

  public static MediaMetadataCompat fromMediaMetadata(Object paramObject)
  {
    if ((paramObject == null) || (Build.VERSION.SDK_INT < 21))
      return null;
    Parcel localParcel = Parcel.obtain();
    MediaMetadataCompatApi21.writeToParcel(paramObject, localParcel, 0);
    localParcel.setDataPosition(0);
    MediaMetadataCompat localMediaMetadataCompat = (MediaMetadataCompat)CREATOR.createFromParcel(localParcel);
    localParcel.recycle();
    localMediaMetadataCompat.mMetadataObj = paramObject;
    return localMediaMetadataCompat;
  }

  public boolean containsKey(String paramString)
  {
    return this.mBundle.containsKey(paramString);
  }

  public int describeContents()
  {
    return 0;
  }

  public Bitmap getBitmap(String paramString)
  {
    try
    {
      Bitmap localBitmap = (Bitmap)this.mBundle.getParcelable(paramString);
      return localBitmap;
    }
    catch (Exception localException)
    {
      Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", localException);
    }
    return null;
  }

  public Bundle getBundle()
  {
    return this.mBundle;
  }

  public MediaDescriptionCompat getDescription()
  {
    if (this.mDescription != null)
      return this.mDescription;
    String str1 = getString("android.media.metadata.MEDIA_ID");
    CharSequence[] arrayOfCharSequence = new CharSequence[3];
    CharSequence localCharSequence1 = getText("android.media.metadata.DISPLAY_TITLE");
    int k;
    label63: Bitmap localBitmap;
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      arrayOfCharSequence[0] = localCharSequence1;
      arrayOfCharSequence[1] = getText("android.media.metadata.DISPLAY_SUBTITLE");
      arrayOfCharSequence[2] = getText("android.media.metadata.DISPLAY_DESCRIPTION");
      k = 0;
      if (k >= PREFERRED_BITMAP_ORDER.length)
        break label357;
      localBitmap = getBitmap(PREFERRED_BITMAP_ORDER[k]);
      if (localBitmap == null)
        break label339;
    }
    while (true)
    {
      int m = 0;
      label92: String str3;
      if (m < PREFERRED_URI_ORDER.length)
      {
        str3 = getString(PREFERRED_URI_ORDER[m]);
        if (TextUtils.isEmpty(str3));
      }
      for (Uri localUri1 = Uri.parse(str3); ; localUri1 = null)
      {
        String str2 = getString("android.media.metadata.MEDIA_URI");
        boolean bool = TextUtils.isEmpty(str2);
        Uri localUri2 = null;
        if (!bool)
          localUri2 = Uri.parse(str2);
        MediaDescriptionCompat.Builder localBuilder = new MediaDescriptionCompat.Builder();
        localBuilder.setMediaId(str1);
        localBuilder.setTitle(arrayOfCharSequence[0]);
        localBuilder.setSubtitle(arrayOfCharSequence[1]);
        localBuilder.setDescription(arrayOfCharSequence[2]);
        localBuilder.setIconBitmap(localBitmap);
        localBuilder.setIconUri(localUri1);
        localBuilder.setMediaUri(localUri2);
        if (this.mBundle.containsKey("android.media.metadata.BT_FOLDER_TYPE"))
        {
          Bundle localBundle = new Bundle();
          localBundle.putLong("android.media.extra.BT_FOLDER_TYPE", getLong("android.media.metadata.BT_FOLDER_TYPE"));
          localBuilder.setExtras(localBundle);
        }
        this.mDescription = localBuilder.build();
        return this.mDescription;
        int i = 0;
        int j = 0;
        while ((j < arrayOfCharSequence.length) && (i < PREFERRED_DESCRIPTION_ORDER.length))
        {
          CharSequence localCharSequence2 = getText(PREFERRED_DESCRIPTION_ORDER[i]);
          if (!TextUtils.isEmpty(localCharSequence2))
          {
            arrayOfCharSequence[j] = localCharSequence2;
            j++;
          }
          i++;
        }
        break;
        label339: k++;
        break label63;
        m++;
        break label92;
      }
      label357: localBitmap = null;
    }
  }

  public long getLong(String paramString)
  {
    return this.mBundle.getLong(paramString, 0L);
  }

  public Object getMediaMetadata()
  {
    if ((this.mMetadataObj != null) || (Build.VERSION.SDK_INT < 21))
      return this.mMetadataObj;
    Parcel localParcel = Parcel.obtain();
    writeToParcel(localParcel, 0);
    localParcel.setDataPosition(0);
    this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(localParcel);
    localParcel.recycle();
    return this.mMetadataObj;
  }

  public RatingCompat getRating(String paramString)
  {
    try
    {
      if (Build.VERSION.SDK_INT >= 19)
        return RatingCompat.fromRating(this.mBundle.getParcelable(paramString));
      RatingCompat localRatingCompat = (RatingCompat)this.mBundle.getParcelable(paramString);
      return localRatingCompat;
    }
    catch (Exception localException)
    {
      Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", localException);
    }
    return null;
  }

  public String getString(String paramString)
  {
    CharSequence localCharSequence = this.mBundle.getCharSequence(paramString);
    if (localCharSequence != null)
      return localCharSequence.toString();
    return null;
  }

  public CharSequence getText(String paramString)
  {
    return this.mBundle.getCharSequence(paramString);
  }

  public Set<String> keySet()
  {
    return this.mBundle.keySet();
  }

  public int size()
  {
    return this.mBundle.size();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBundle(this.mBundle);
  }

  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface BitmapKey
  {
  }

  public static final class Builder
  {
    private final Bundle mBundle;

    public Builder()
    {
      this.mBundle = new Bundle();
    }

    public Builder(MediaMetadataCompat paramMediaMetadataCompat)
    {
      this.mBundle = new Bundle(paramMediaMetadataCompat.mBundle);
    }

    @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    public Builder(MediaMetadataCompat paramMediaMetadataCompat, int paramInt)
    {
      this(paramMediaMetadataCompat);
      Iterator localIterator = this.mBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        Object localObject = this.mBundle.get(str);
        if ((localObject == null) || (!(localObject instanceof Bitmap)))
          continue;
        Bitmap localBitmap = (Bitmap)localObject;
        if ((localBitmap.getHeight() > paramInt) || (localBitmap.getWidth() > paramInt))
        {
          putBitmap(str, scaleBitmap(localBitmap, paramInt));
          continue;
        }
        if ((Build.VERSION.SDK_INT < 14) || ((!str.equals("android.media.metadata.ART")) && (!str.equals("android.media.metadata.ALBUM_ART"))))
          continue;
        putBitmap(str, localBitmap.copy(localBitmap.getConfig(), false));
      }
    }

    private Bitmap scaleBitmap(Bitmap paramBitmap, int paramInt)
    {
      float f1 = paramInt;
      float f2 = Math.min(f1 / paramBitmap.getWidth(), f1 / paramBitmap.getHeight());
      int i = (int)(f2 * paramBitmap.getHeight());
      return Bitmap.createScaledBitmap(paramBitmap, (int)(f2 * paramBitmap.getWidth()), i, true);
    }

    public MediaMetadataCompat build()
    {
      return new MediaMetadataCompat(this.mBundle);
    }

    public Builder putBitmap(String paramString, Bitmap paramBitmap)
    {
      if ((MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(paramString)).intValue() != 2))
        throw new IllegalArgumentException("The " + paramString + " key cannot be used to put a Bitmap");
      this.mBundle.putParcelable(paramString, paramBitmap);
      return this;
    }

    public Builder putLong(String paramString, long paramLong)
    {
      if ((MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(paramString)).intValue() != 0))
        throw new IllegalArgumentException("The " + paramString + " key cannot be used to put a long");
      this.mBundle.putLong(paramString, paramLong);
      return this;
    }

    public Builder putRating(String paramString, RatingCompat paramRatingCompat)
    {
      if ((MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(paramString)).intValue() != 3))
        throw new IllegalArgumentException("The " + paramString + " key cannot be used to put a Rating");
      if (Build.VERSION.SDK_INT >= 19)
      {
        this.mBundle.putParcelable(paramString, (Parcelable)paramRatingCompat.getRating());
        return this;
      }
      this.mBundle.putParcelable(paramString, paramRatingCompat);
      return this;
    }

    public Builder putString(String paramString1, String paramString2)
    {
      if ((MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(paramString1)) && (((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(paramString1)).intValue() != 1))
        throw new IllegalArgumentException("The " + paramString1 + " key cannot be used to put a String");
      this.mBundle.putCharSequence(paramString1, paramString2);
      return this;
    }

    public Builder putText(String paramString, CharSequence paramCharSequence)
    {
      if ((MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(paramString)).intValue() != 1))
        throw new IllegalArgumentException("The " + paramString + " key cannot be used to put a CharSequence");
      this.mBundle.putCharSequence(paramString, paramCharSequence);
      return this;
    }
  }

  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface LongKey
  {
  }

  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface RatingKey
  {
  }

  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface TextKey
  {
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaMetadataCompat
 * JD-Core Version:    0.6.0
 */