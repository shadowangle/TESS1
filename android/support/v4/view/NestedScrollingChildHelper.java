package android.support.v4.view;

import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper
{
  private boolean mIsNestedScrollingEnabled;
  private ViewParent mNestedScrollingParent;
  private int[] mTempNestedScrollConsumed;
  private final View mView;

  public NestedScrollingChildHelper(View paramView)
  {
    this.mView = paramView;
  }

  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    if ((isNestedScrollingEnabled()) && (this.mNestedScrollingParent != null))
      return ViewParentCompat.onNestedFling(this.mNestedScrollingParent, this.mView, paramFloat1, paramFloat2, paramBoolean);
    return false;
  }

  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2)
  {
    if ((isNestedScrollingEnabled()) && (this.mNestedScrollingParent != null))
      return ViewParentCompat.onNestedPreFling(this.mNestedScrollingParent, this.mView, paramFloat1, paramFloat2);
    return false;
  }

  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    boolean bool = isNestedScrollingEnabled();
    int i = 0;
    int j;
    if (bool)
    {
      ViewParent localViewParent = this.mNestedScrollingParent;
      i = 0;
      if (localViewParent != null)
      {
        if ((paramInt1 == 0) && (paramInt2 == 0))
          break label168;
        if (paramArrayOfInt2 == null)
          break label188;
        this.mView.getLocationInWindow(paramArrayOfInt2);
        j = paramArrayOfInt2[0];
      }
    }
    for (int k = paramArrayOfInt2[1]; ; k = 0)
    {
      if (paramArrayOfInt1 == null)
      {
        if (this.mTempNestedScrollConsumed == null)
          this.mTempNestedScrollConsumed = new int[2];
        paramArrayOfInt1 = this.mTempNestedScrollConsumed;
      }
      paramArrayOfInt1[0] = 0;
      paramArrayOfInt1[1] = 0;
      ViewParentCompat.onNestedPreScroll(this.mNestedScrollingParent, this.mView, paramInt1, paramInt2, paramArrayOfInt1);
      if (paramArrayOfInt2 != null)
      {
        this.mView.getLocationInWindow(paramArrayOfInt2);
        paramArrayOfInt2[0] -= j;
        paramArrayOfInt2[1] -= k;
      }
      if (paramArrayOfInt1[0] == 0)
      {
        int m = paramArrayOfInt1[1];
        i = 0;
        if (m == 0);
      }
      else
      {
        i = 1;
      }
      label168: 
      do
      {
        return i;
        i = 0;
      }
      while (paramArrayOfInt2 == null);
      paramArrayOfInt2[0] = 0;
      paramArrayOfInt2[1] = 0;
      return false;
      label188: j = 0;
    }
  }

  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    boolean bool = isNestedScrollingEnabled();
    int i = 0;
    int m;
    int j;
    if (bool)
    {
      ViewParent localViewParent = this.mNestedScrollingParent;
      i = 0;
      if (localViewParent != null)
      {
        if ((paramInt1 == 0) && (paramInt2 == 0) && (paramInt3 == 0) && (paramInt4 == 0))
          break label133;
        if (paramArrayOfInt == null)
          break label153;
        this.mView.getLocationInWindow(paramArrayOfInt);
        m = paramArrayOfInt[0];
        j = paramArrayOfInt[1];
      }
    }
    for (int k = m; ; k = 0)
    {
      ViewParentCompat.onNestedScroll(this.mNestedScrollingParent, this.mView, paramInt1, paramInt2, paramInt3, paramInt4);
      if (paramArrayOfInt != null)
      {
        this.mView.getLocationInWindow(paramArrayOfInt);
        paramArrayOfInt[0] -= k;
        paramArrayOfInt[1] -= j;
      }
      i = 1;
      label133: 
      do
      {
        return i;
        i = 0;
      }
      while (paramArrayOfInt == null);
      paramArrayOfInt[0] = 0;
      paramArrayOfInt[1] = 0;
      return false;
      label153: j = 0;
    }
  }

  public boolean hasNestedScrollingParent()
  {
    return this.mNestedScrollingParent != null;
  }

  public boolean isNestedScrollingEnabled()
  {
    return this.mIsNestedScrollingEnabled;
  }

  public void onDetachedFromWindow()
  {
    ViewCompat.stopNestedScroll(this.mView);
  }

  public void onStopNestedScroll(View paramView)
  {
    ViewCompat.stopNestedScroll(this.mView);
  }

  public void setNestedScrollingEnabled(boolean paramBoolean)
  {
    if (this.mIsNestedScrollingEnabled)
      ViewCompat.stopNestedScroll(this.mView);
    this.mIsNestedScrollingEnabled = paramBoolean;
  }

  public boolean startNestedScroll(int paramInt)
  {
    if (hasNestedScrollingParent())
      return true;
    if (isNestedScrollingEnabled())
    {
      ViewParent localViewParent = this.mView.getParent();
      View localView = this.mView;
      while (localViewParent != null)
      {
        if (ViewParentCompat.onStartNestedScroll(localViewParent, localView, this.mView, paramInt))
        {
          this.mNestedScrollingParent = localViewParent;
          ViewParentCompat.onNestedScrollAccepted(localViewParent, localView, this.mView, paramInt);
          return true;
        }
        if ((localViewParent instanceof View))
          localView = (View)localViewParent;
        localViewParent = localViewParent.getParent();
      }
    }
    return false;
  }

  public void stopNestedScroll()
  {
    if (this.mNestedScrollingParent != null)
    {
      ViewParentCompat.onStopNestedScroll(this.mNestedScrollingParent, this.mView);
      this.mNestedScrollingParent = null;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.NestedScrollingChildHelper
 * JD-Core Version:    0.6.0
 */