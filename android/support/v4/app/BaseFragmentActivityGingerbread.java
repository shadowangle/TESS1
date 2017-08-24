package android.support.v4.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

@TargetApi(9)
@RequiresApi(9)
abstract class BaseFragmentActivityGingerbread extends SupportActivity
{
  boolean mStartedIntentSenderFromFragment;

  static void checkForValidRequestCode(int paramInt)
  {
    if ((0xFFFF0000 & paramInt) != 0)
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
  }

  abstract View dispatchFragmentsOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet);

  protected void onCreate(Bundle paramBundle)
  {
    if ((Build.VERSION.SDK_INT < 11) && (getLayoutInflater().getFactory() == null))
      getLayoutInflater().setFactory(this);
    super.onCreate(paramBundle);
  }

  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    View localView = dispatchFragmentsOnCreateView(null, paramString, paramContext, paramAttributeSet);
    if (localView == null)
      localView = super.onCreateView(paramString, paramContext, paramAttributeSet);
    return localView;
  }

  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, @Nullable Intent paramIntent, int paramInt2, int paramInt3, int paramInt4)
    throws IntentSender.SendIntentException
  {
    if ((!this.mStartedIntentSenderFromFragment) && (paramInt1 != -1))
      checkForValidRequestCode(paramInt1);
    super.startIntentSenderForResult(paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.BaseFragmentActivityGingerbread
 * JD-Core Version:    0.6.0
 */