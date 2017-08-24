package android.support.v4.os;

import android.content.Context;

public class UserManagerCompat
{
  public static boolean isUserUnlocked(Context paramContext)
  {
    if (BuildCompat.isAtLeastN())
      return UserManagerCompatApi24.isUserUnlocked(paramContext);
    return true;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.os.UserManagerCompat
 * JD-Core Version:    0.6.0
 */