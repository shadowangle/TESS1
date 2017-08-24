package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.Arrays;

class BundleUtil
{
  public static Bundle[] getBundleArrayFromBundle(Bundle paramBundle, String paramString)
  {
    Parcelable[] arrayOfParcelable = paramBundle.getParcelableArray(paramString);
    if (((arrayOfParcelable instanceof Bundle[])) || (arrayOfParcelable == null))
      return (Bundle[])(Bundle[])arrayOfParcelable;
    Bundle[] arrayOfBundle = (Bundle[])Arrays.copyOf(arrayOfParcelable, arrayOfParcelable.length, [Landroid.os.Bundle.class);
    paramBundle.putParcelableArray(paramString, arrayOfBundle);
    return arrayOfBundle;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.BundleUtil
 * JD-Core Version:    0.6.0
 */