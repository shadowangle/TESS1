package android.support.v4.os;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.UserManager;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@TargetApi(24)
@RequiresApi(24)
@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class UserManagerCompatApi24
{
  public static boolean isUserUnlocked(Context paramContext)
  {
    return ((UserManager)paramContext.getSystemService(UserManager.class)).isUserUnlocked();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.os.UserManagerCompatApi24
 * JD-Core Version:    0.6.0
 */