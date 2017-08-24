package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

@TargetApi(9)
@RequiresApi(9)
class CompoundButtonCompatGingerbread
{
  private static final String TAG = "CompoundButtonCompatGingerbread";
  private static Field sButtonDrawableField;
  private static boolean sButtonDrawableFieldFetched;

  static Drawable getButtonDrawable(CompoundButton paramCompoundButton)
  {
    if (!sButtonDrawableFieldFetched);
    try
    {
      sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
      sButtonDrawableField.setAccessible(true);
      sButtonDrawableFieldFetched = true;
      if (sButtonDrawableField == null);
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      try
      {
        Drawable localDrawable = (Drawable)sButtonDrawableField.get(paramCompoundButton);
        return localDrawable;
        localNoSuchFieldException = localNoSuchFieldException;
        Log.i("CompoundButtonCompatGingerbread", "Failed to retrieve mButtonDrawable field", localNoSuchFieldException);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Log.i("CompoundButtonCompatGingerbread", "Failed to get button drawable via reflection", localIllegalAccessException);
        sButtonDrawableField = null;
      }
    }
    return null;
  }

  static ColorStateList getButtonTintList(CompoundButton paramCompoundButton)
  {
    if ((paramCompoundButton instanceof TintableCompoundButton))
      return ((TintableCompoundButton)paramCompoundButton).getSupportButtonTintList();
    return null;
  }

  static PorterDuff.Mode getButtonTintMode(CompoundButton paramCompoundButton)
  {
    if ((paramCompoundButton instanceof TintableCompoundButton))
      return ((TintableCompoundButton)paramCompoundButton).getSupportButtonTintMode();
    return null;
  }

  static void setButtonTintList(CompoundButton paramCompoundButton, ColorStateList paramColorStateList)
  {
    if ((paramCompoundButton instanceof TintableCompoundButton))
      ((TintableCompoundButton)paramCompoundButton).setSupportButtonTintList(paramColorStateList);
  }

  static void setButtonTintMode(CompoundButton paramCompoundButton, PorterDuff.Mode paramMode)
  {
    if ((paramCompoundButton instanceof TintableCompoundButton))
      ((TintableCompoundButton)paramCompoundButton).setSupportButtonTintMode(paramMode);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.CompoundButtonCompatGingerbread
 * JD-Core Version:    0.6.0
 */