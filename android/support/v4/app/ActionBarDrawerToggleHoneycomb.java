package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

@TargetApi(11)
@RequiresApi(11)
class ActionBarDrawerToggleHoneycomb
{
  private static final String TAG = "ActionBarDrawerToggleHoneycomb";
  private static final int[] THEME_ATTRS = { 16843531 };

  public static Drawable getThemeUpIndicator(Activity paramActivity)
  {
    TypedArray localTypedArray = paramActivity.obtainStyledAttributes(THEME_ATTRS);
    Drawable localDrawable = localTypedArray.getDrawable(0);
    localTypedArray.recycle();
    return localDrawable;
  }

  public static Object setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
  {
    if (paramObject == null);
    for (Object localObject = new SetIndicatorInfo(paramActivity); ; localObject = paramObject)
    {
      SetIndicatorInfo localSetIndicatorInfo = (SetIndicatorInfo)localObject;
      if (localSetIndicatorInfo.setHomeAsUpIndicator != null);
      try
      {
        ActionBar localActionBar = paramActivity.getActionBar();
        Method localMethod = localSetIndicatorInfo.setHomeActionContentDescription;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(paramInt);
        localMethod.invoke(localActionBar, arrayOfObject);
        if (Build.VERSION.SDK_INT <= 19)
          localActionBar.setSubtitle(localActionBar.getSubtitle());
        return localObject;
      }
      catch (Exception localException)
      {
        Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", localException);
        return localObject;
      }
    }
  }

  public static Object setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
  {
    if (paramObject == null);
    for (Object localObject = new SetIndicatorInfo(paramActivity); ; localObject = paramObject)
    {
      SetIndicatorInfo localSetIndicatorInfo = (SetIndicatorInfo)localObject;
      if (localSetIndicatorInfo.setHomeAsUpIndicator != null)
        try
        {
          ActionBar localActionBar = paramActivity.getActionBar();
          localSetIndicatorInfo.setHomeAsUpIndicator.invoke(localActionBar, new Object[] { paramDrawable });
          Method localMethod = localSetIndicatorInfo.setHomeActionContentDescription;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Integer.valueOf(paramInt);
          localMethod.invoke(localActionBar, arrayOfObject);
          return localObject;
        }
        catch (Exception localException)
        {
          Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", localException);
          return localObject;
        }
      if (localSetIndicatorInfo.upIndicatorView != null)
      {
        localSetIndicatorInfo.upIndicatorView.setImageDrawable(paramDrawable);
        return localObject;
      }
      Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
      return localObject;
    }
  }

  private static class SetIndicatorInfo
  {
    public Method setHomeActionContentDescription;
    public Method setHomeAsUpIndicator;
    public ImageView upIndicatorView;

    SetIndicatorInfo(Activity paramActivity)
    {
      while (true)
      {
        View localView2;
        View localView3;
        try
        {
          this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", new Class[] { Drawable.class });
          Class[] arrayOfClass = new Class[1];
          arrayOfClass[0] = Integer.TYPE;
          this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", arrayOfClass);
          return;
        }
        catch (NoSuchMethodException localNoSuchMethodException)
        {
          View localView1 = paramActivity.findViewById(16908332);
          if (localView1 == null)
            continue;
          ViewGroup localViewGroup = (ViewGroup)localView1.getParent();
          if (localViewGroup.getChildCount() != 2)
            continue;
          localView2 = localViewGroup.getChildAt(0);
          localView3 = localViewGroup.getChildAt(1);
          if (localView2.getId() != 16908332);
        }
        while ((localView3 instanceof ImageView))
        {
          this.upIndicatorView = ((ImageView)localView3);
          return;
          localView3 = localView2;
        }
      }
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.ActionBarDrawerToggleHoneycomb
 * JD-Core Version:    0.6.0
 */