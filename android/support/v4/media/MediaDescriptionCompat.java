package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;

public final class MediaDescriptionCompat
  implements Parcelable
{
  public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
  public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
  public static final long BT_FOLDER_TYPE_GENRES = 4L;
  public static final long BT_FOLDER_TYPE_MIXED = 0L;
  public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
  public static final long BT_FOLDER_TYPE_TITLES = 1L;
  public static final long BT_FOLDER_TYPE_YEARS = 6L;
  public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new Parcelable.Creator()
  {
    public MediaDescriptionCompat createFromParcel(Parcel paramParcel)
    {
      if (Build.VERSION.SDK_INT < 21)
        return new MediaDescriptionCompat(paramParcel);
      return MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(paramParcel));
    }

    public MediaDescriptionCompat[] newArray(int paramInt)
    {
      return new MediaDescriptionCompat[paramInt];
    }
  };

  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static final String DESCRIPTION_KEY_MEDIA_URI = "android.support.v4.media.description.MEDIA_URI";

  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  public static final String DESCRIPTION_KEY_NULL_BUNDLE_FLAG = "android.support.v4.media.description.NULL_BUNDLE_FLAG";
  public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";
  private final CharSequence mDescription;
  private Object mDescriptionObj;
  private final Bundle mExtras;
  private final Bitmap mIcon;
  private final Uri mIconUri;
  private final String mMediaId;
  private final Uri mMediaUri;
  private final CharSequence mSubtitle;
  private final CharSequence mTitle;

  MediaDescriptionCompat(Parcel paramParcel)
  {
    this.mMediaId = paramParcel.readString();
    this.mTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    this.mSubtitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    this.mDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    this.mIcon = ((Bitmap)paramParcel.readParcelable(null));
    this.mIconUri = ((Uri)paramParcel.readParcelable(null));
    this.mExtras = paramParcel.readBundle();
    this.mMediaUri = ((Uri)paramParcel.readParcelable(null));
  }

  MediaDescriptionCompat(String paramString, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, Bitmap paramBitmap, Uri paramUri1, Bundle paramBundle, Uri paramUri2)
  {
    this.mMediaId = paramString;
    this.mTitle = paramCharSequence1;
    this.mSubtitle = paramCharSequence2;
    this.mDescription = paramCharSequence3;
    this.mIcon = paramBitmap;
    this.mIconUri = paramUri1;
    this.mExtras = paramBundle;
    this.mMediaUri = paramUri2;
  }

  public static MediaDescriptionCompat fromMediaDescription(Object paramObject)
  {
    if (paramObject != null)
    {
      if (Build.VERSION.SDK_INT < 21)
        return null;
      Builder localBuilder = new Builder();
      localBuilder.setMediaId(MediaDescriptionCompatApi21.getMediaId(paramObject));
      localBuilder.setTitle(MediaDescriptionCompatApi21.getTitle(paramObject));
      localBuilder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(paramObject));
      localBuilder.setDescription(MediaDescriptionCompatApi21.getDescription(paramObject));
      localBuilder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(paramObject));
      localBuilder.setIconUri(MediaDescriptionCompatApi21.getIconUri(paramObject));
      Bundle localBundle1 = MediaDescriptionCompatApi21.getExtras(paramObject);
      Uri localUri;
      Bundle localBundle2;
      if (localBundle1 == null)
      {
        localUri = null;
        if (localUri == null)
          break label211;
        if (!localBundle1.containsKey("android.support.v4.media.description.NULL_BUNDLE_FLAG"))
          break label170;
        int i = localBundle1.size();
        localBundle2 = null;
        if (i != 2)
          break label170;
      }
      while (true)
      {
        label121: localBuilder.setExtras(localBundle2);
        if (localUri != null)
          localBuilder.setMediaUri(localUri);
        while (true)
        {
          MediaDescriptionCompat localMediaDescriptionCompat = localBuilder.build();
          localMediaDescriptionCompat.mDescriptionObj = paramObject;
          return localMediaDescriptionCompat;
          localUri = (Uri)localBundle1.getParcelable("android.support.v4.media.description.MEDIA_URI");
          break;
          label170: localBundle1.remove("android.support.v4.media.description.MEDIA_URI");
          localBundle1.remove("android.support.v4.media.description.NULL_BUNDLE_FLAG");
          localBundle2 = localBundle1;
          break label121;
          if (Build.VERSION.SDK_INT < 23)
            continue;
          localBuilder.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(paramObject));
        }
        label211: localBundle2 = localBundle1;
      }
    }
    return null;
  }

  public int describeContents()
  {
    return 0;
  }

  @Nullable
  public CharSequence getDescription()
  {
    return this.mDescription;
  }

  @Nullable
  public Bundle getExtras()
  {
    return this.mExtras;
  }

  @Nullable
  public Bitmap getIconBitmap()
  {
    return this.mIcon;
  }

  @Nullable
  public Uri getIconUri()
  {
    return this.mIconUri;
  }

  public Object getMediaDescription()
  {
    if ((this.mDescriptionObj != null) || (Build.VERSION.SDK_INT < 21))
      return this.mDescriptionObj;
    Object localObject = MediaDescriptionCompatApi21.Builder.newInstance();
    MediaDescriptionCompatApi21.Builder.setMediaId(localObject, this.mMediaId);
    MediaDescriptionCompatApi21.Builder.setTitle(localObject, this.mTitle);
    MediaDescriptionCompatApi21.Builder.setSubtitle(localObject, this.mSubtitle);
    MediaDescriptionCompatApi21.Builder.setDescription(localObject, this.mDescription);
    MediaDescriptionCompatApi21.Builder.setIconBitmap(localObject, this.mIcon);
    MediaDescriptionCompatApi21.Builder.setIconUri(localObject, this.mIconUri);
    Bundle localBundle = this.mExtras;
    if ((Build.VERSION.SDK_INT < 23) && (this.mMediaUri != null))
    {
      if (localBundle == null)
      {
        localBundle = new Bundle();
        localBundle.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
      }
      localBundle.putParcelable("android.support.v4.media.description.MEDIA_URI", this.mMediaUri);
    }
    MediaDescriptionCompatApi21.Builder.setExtras(localObject, localBundle);
    if (Build.VERSION.SDK_INT >= 23)
      MediaDescriptionCompatApi23.Builder.setMediaUri(localObject, this.mMediaUri);
    this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(localObject);
    return this.mDescriptionObj;
  }

  @Nullable
  public String getMediaId()
  {
    return this.mMediaId;
  }

  @Nullable
  public Uri getMediaUri()
  {
    return this.mMediaUri;
  }

  @Nullable
  public CharSequence getSubtitle()
  {
    return this.mSubtitle;
  }

  @Nullable
  public CharSequence getTitle()
  {
    return this.mTitle;
  }

  public String toString()
  {
    return this.mTitle + ", " + this.mSubtitle + ", " + this.mDescription;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (Build.VERSION.SDK_INT < 21)
    {
      paramParcel.writeString(this.mMediaId);
      TextUtils.writeToParcel(this.mTitle, paramParcel, paramInt);
      TextUtils.writeToParcel(this.mSubtitle, paramParcel, paramInt);
      TextUtils.writeToParcel(this.mDescription, paramParcel, paramInt);
      paramParcel.writeParcelable(this.mIcon, paramInt);
      paramParcel.writeParcelable(this.mIconUri, paramInt);
      paramParcel.writeBundle(this.mExtras);
      paramParcel.writeParcelable(this.mMediaUri, paramInt);
      return;
    }
    MediaDescriptionCompatApi21.writeToParcel(getMediaDescription(), paramParcel, paramInt);
  }

  public static final class Builder
  {
    private CharSequence mDescription;
    private Bundle mExtras;
    private Bitmap mIcon;
    private Uri mIconUri;
    private String mMediaId;
    private Uri mMediaUri;
    private CharSequence mSubtitle;
    private CharSequence mTitle;

    public MediaDescriptionCompat build()
    {
      return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
    }

    public Builder setDescription(@Nullable CharSequence paramCharSequence)
    {
      this.mDescription = paramCharSequence;
      return this;
    }

    public Builder setExtras(@Nullable Bundle paramBundle)
    {
      this.mExtras = paramBundle;
      return this;
    }

    public Builder setIconBitmap(@Nullable Bitmap paramBitmap)
    {
      this.mIcon = paramBitmap;
      return this;
    }

    public Builder setIconUri(@Nullable Uri paramUri)
    {
      this.mIconUri = paramUri;
      return this;
    }

    public Builder setMediaId(@Nullable String paramString)
    {
      this.mMediaId = paramString;
      return this;
    }

    public Builder setMediaUri(@Nullable Uri paramUri)
    {
      this.mMediaUri = paramUri;
      return this;
    }

    public Builder setSubtitle(@Nullable CharSequence paramCharSequence)
    {
      this.mSubtitle = paramCharSequence;
      return this;
    }

    public Builder setTitle(@Nullable CharSequence paramCharSequence)
    {
      this.mTitle = paramCharSequence;
      return this;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.media.MediaDescriptionCompat
 * JD-Core Version:    0.6.0
 */