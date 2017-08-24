package android.support.multidex;

import android.app.Application;
import android.content.Context;

public class MultiDexApplication extends Application
{
  protected void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    MultiDex.install(this);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.multidex.MultiDexApplication
 * JD-Core Version:    0.6.0
 */