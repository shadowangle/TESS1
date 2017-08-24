package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup
{
  private static final int CLOSE_ENOUGH = 2;
  private static final Comparator<ItemInfo> COMPARATOR;
  private static final boolean DEBUG = false;
  private static final int DEFAULT_GUTTER_SIZE = 16;
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  private static final int DRAW_ORDER_DEFAULT = 0;
  private static final int DRAW_ORDER_FORWARD = 1;
  private static final int DRAW_ORDER_REVERSE = 2;
  private static final int INVALID_POINTER = -1;
  static final int[] LAYOUT_ATTRS = { 16842931 };
  private static final int MAX_SETTLE_DURATION = 600;
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  private static final int MIN_FLING_VELOCITY = 400;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  private static final String TAG = "ViewPager";
  private static final boolean USE_CACHE;
  private static final Interpolator sInterpolator;
  private static final ViewPositionComparator sPositionComparator;
  private int mActivePointerId = -1;
  PagerAdapter mAdapter;
  private List<OnAdapterChangeListener> mAdapterChangeListeners;
  private int mBottomPageBounds;
  private boolean mCalledSuper;
  private int mChildHeightMeasureSpec;
  private int mChildWidthMeasureSpec;
  private int mCloseEnough;
  int mCurItem;
  private int mDecorChildCount;
  private int mDefaultGutterSize;
  private int mDrawingOrder;
  private ArrayList<View> mDrawingOrderedChildren;
  private final Runnable mEndScrollRunnable = new Runnable()
  {
    public void run()
    {
      ViewPager.this.setScrollState(0);
      ViewPager.this.populate();
    }
  };
  private int mExpectedAdapterCount;
  private long mFakeDragBeginTime;
  private boolean mFakeDragging;
  private boolean mFirstLayout = true;
  private float mFirstOffset = -3.402824E+038F;
  private int mFlingDistance;
  private int mGutterSize;
  private boolean mInLayout;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private OnPageChangeListener mInternalPageChangeListener;
  private boolean mIsBeingDragged;
  private boolean mIsScrollStarted;
  private boolean mIsUnableToDrag;
  private final ArrayList<ItemInfo> mItems = new ArrayList();
  private float mLastMotionX;
  private float mLastMotionY;
  private float mLastOffset = 3.4028235E+38F;
  private EdgeEffectCompat mLeftEdge;
  private Drawable mMarginDrawable;
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private boolean mNeedCalculatePageOffsets = false;
  private PagerObserver mObserver;
  private int mOffscreenPageLimit = 1;
  private OnPageChangeListener mOnPageChangeListener;
  private List<OnPageChangeListener> mOnPageChangeListeners;
  private int mPageMargin;
  private PageTransformer mPageTransformer;
  private int mPageTransformerLayerType;
  private boolean mPopulatePending;
  private Parcelable mRestoredAdapterState = null;
  private ClassLoader mRestoredClassLoader = null;
  private int mRestoredCurItem = -1;
  private EdgeEffectCompat mRightEdge;
  private int mScrollState = 0;
  private Scroller mScroller;
  private boolean mScrollingCacheEnabled;
  private Method mSetChildrenDrawingOrderEnabled;
  private final ItemInfo mTempItem = new ItemInfo();
  private final Rect mTempRect = new Rect();
  private int mTopPageBounds;
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;

  static
  {
    COMPARATOR = new Comparator()
    {
      public int compare(ViewPager.ItemInfo paramItemInfo1, ViewPager.ItemInfo paramItemInfo2)
      {
        return paramItemInfo1.position - paramItemInfo2.position;
      }
    };
    sInterpolator = new Interpolator()
    {
      public float getInterpolation(float paramFloat)
      {
        float f = paramFloat - 1.0F;
        return 1.0F + f * (f * (f * (f * f)));
      }
    };
    sPositionComparator = new ViewPositionComparator();
  }

  public ViewPager(Context paramContext)
  {
    super(paramContext);
    initViewPager();
  }

  public ViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViewPager();
  }

  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = this.mAdapter.getCount();
    int j = getClientWidth();
    float f1;
    if (j > 0)
      f1 = this.mPageMargin / j;
    int i5;
    int i16;
    float f12;
    int i17;
    ItemInfo localItemInfo6;
    int i19;
    while (true)
    {
      if (paramItemInfo2 == null)
        break label463;
      i5 = paramItemInfo2.position;
      if (i5 >= paramItemInfo1.position)
        break;
      float f11 = f1 + (paramItemInfo2.offset + paramItemInfo2.widthFactor);
      i16 = i5 + 1;
      f12 = f11;
      i17 = 0;
      if ((i16 > paramItemInfo1.position) || (i17 >= this.mItems.size()))
        break label463;
      ItemInfo localItemInfo5 = (ItemInfo)this.mItems.get(i17);
      int i18 = i17;
      localItemInfo6 = localItemInfo5;
      int i23;
      for (i19 = i18; (i16 > localItemInfo6.position) && (i19 < -1 + this.mItems.size()); i19 = i23)
      {
        i23 = i19 + 1;
        localItemInfo6 = (ItemInfo)this.mItems.get(i23);
      }
      f1 = 0.0F;
    }
    while (true)
    {
      if (i21 < localItemInfo6.position)
      {
        Object localObject2;
        float f15 = localObject2 + (f1 + this.mAdapter.getPageWidth(i21));
        i21++;
        f13 = f15;
        continue;
      }
      localItemInfo6.offset = f13;
      float f14 = f13 + (f1 + localItemInfo6.widthFactor);
      int i22 = i21 + 1;
      f12 = f14;
      i16 = i22;
      i17 = i19;
      break;
      float f7;
      int i8;
      int i9;
      ItemInfo localItemInfo4;
      int i11;
      if (i5 > paramItemInfo1.position)
      {
        int i6 = -1 + this.mItems.size();
        float f6 = paramItemInfo2.offset;
        int i7 = i5 - 1;
        f7 = f6;
        i8 = i7;
        i9 = i6;
        if ((i8 >= paramItemInfo1.position) && (i9 >= 0))
        {
          ItemInfo localItemInfo3 = (ItemInfo)this.mItems.get(i9);
          int i10 = i9;
          localItemInfo4 = localItemInfo3;
          int i15;
          for (i11 = i10; (i8 < localItemInfo4.position) && (i11 > 0); i11 = i15)
          {
            i15 = i11 - 1;
            localItemInfo4 = (ItemInfo)this.mItems.get(i15);
          }
        }
      }
      while (true)
      {
        if (i13 > localItemInfo4.position)
        {
          Object localObject1;
          float f10 = localObject1 - (f1 + this.mAdapter.getPageWidth(i13));
          i13--;
          f8 = f10;
          continue;
        }
        float f9 = f8 - (f1 + localItemInfo4.widthFactor);
        localItemInfo4.offset = f9;
        int i14 = i13 - 1;
        f7 = f9;
        i8 = i14;
        i9 = i11;
        break;
        label463: int k = this.mItems.size();
        float f2 = paramItemInfo1.offset;
        int m = -1 + paramItemInfo1.position;
        float f3;
        float f4;
        if (paramItemInfo1.position == 0)
        {
          f3 = paramItemInfo1.offset;
          this.mFirstOffset = f3;
          if (paramItemInfo1.position != i - 1)
            break label599;
          f4 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
          label529: this.mLastOffset = f4;
        }
        label599: int i4;
        for (int n = paramInt - 1; ; n = i4)
        {
          if (n < 0)
            break label656;
          ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(n);
          while (true)
            if (m > localItemInfo2.position)
            {
              f2 -= f1 + this.mAdapter.getPageWidth(m);
              m--;
              continue;
              f3 = -3.402824E+038F;
              break;
              f4 = 3.4028235E+38F;
              break label529;
            }
          f2 -= f1 + localItemInfo2.widthFactor;
          localItemInfo2.offset = f2;
          if (localItemInfo2.position == 0)
            this.mFirstOffset = f2;
          i4 = n - 1;
          m--;
        }
        label656: float f5 = f1 + (paramItemInfo1.offset + paramItemInfo1.widthFactor);
        int i1 = 1 + paramItemInfo1.position;
        int i3;
        for (int i2 = paramInt + 1; i2 < k; i2 = i3)
        {
          ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(i2);
          while (i1 < localItemInfo1.position)
          {
            f5 += f1 + this.mAdapter.getPageWidth(i1);
            i1++;
          }
          if (localItemInfo1.position == i - 1)
            this.mLastOffset = (f5 + localItemInfo1.widthFactor - 1.0F);
          localItemInfo1.offset = f5;
          f5 += f1 + localItemInfo1.widthFactor;
          i3 = i2 + 1;
          i1++;
        }
        this.mNeedCalculatePageOffsets = false;
        return;
        int i12 = i8;
        float f8 = f7;
        int i13 = i12;
      }
      int i20 = i16;
      float f13 = f12;
      int i21 = i20;
    }
  }

  private void completeScroll(boolean paramBoolean)
  {
    int i;
    int n;
    label32: int j;
    int k;
    if (this.mScrollState == 2)
    {
      i = 1;
      if (i != 0)
      {
        setScrollingCacheEnabled(false);
        if (this.mScroller.isFinished())
          break label177;
        n = 1;
        if (n != 0)
        {
          this.mScroller.abortAnimation();
          int i1 = getScrollX();
          int i2 = getScrollY();
          int i3 = this.mScroller.getCurrX();
          int i4 = this.mScroller.getCurrY();
          if ((i1 != i3) || (i2 != i4))
          {
            scrollTo(i3, i4);
            if (i3 != i1)
              pageScrolled(i3);
          }
        }
      }
      this.mPopulatePending = false;
      j = i;
      k = 0;
      label120: if (k >= this.mItems.size())
        break label183;
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(k);
      if (!localItemInfo.scrolling)
        break label210;
      localItemInfo.scrolling = false;
    }
    label177: label183: label210: for (int m = 1; ; m = j)
    {
      k++;
      j = m;
      break label120;
      i = 0;
      break;
      n = 0;
      break label32;
      if (j != 0)
      {
        if (paramBoolean)
          ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
      }
      else
        return;
      this.mEndScrollRunnable.run();
      return;
    }
  }

  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    if ((Math.abs(paramInt3) > this.mFlingDistance) && (Math.abs(paramInt2) > this.mMinimumVelocity))
    {
      if (paramInt2 > 0);
      while (true)
      {
        if (this.mItems.size() > 0)
        {
          ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
          ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
          paramInt1 = Math.max(localItemInfo1.position, Math.min(paramInt1, localItemInfo2.position));
        }
        return paramInt1;
        paramInt1++;
      }
    }
    float f;
    if (paramInt1 >= this.mCurItem)
      f = 0.4F;
    while (true)
    {
      paramInt1 += (int)(f + paramFloat);
      break;
      f = 0.6F;
    }
  }

  private void dispatchOnPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mOnPageChangeListeners != null)
    {
      int i = this.mOnPageChangeListeners.size();
      for (int j = 0; j < i; j++)
      {
        OnPageChangeListener localOnPageChangeListener = (OnPageChangeListener)this.mOnPageChangeListeners.get(j);
        if (localOnPageChangeListener == null)
          continue;
        localOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
      }
    }
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
  }

  private void dispatchOnPageSelected(int paramInt)
  {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageSelected(paramInt);
    if (this.mOnPageChangeListeners != null)
    {
      int i = this.mOnPageChangeListeners.size();
      for (int j = 0; j < i; j++)
      {
        OnPageChangeListener localOnPageChangeListener = (OnPageChangeListener)this.mOnPageChangeListeners.get(j);
        if (localOnPageChangeListener == null)
          continue;
        localOnPageChangeListener.onPageSelected(paramInt);
      }
    }
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageSelected(paramInt);
  }

  private void dispatchOnScrollStateChanged(int paramInt)
  {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrollStateChanged(paramInt);
    if (this.mOnPageChangeListeners != null)
    {
      int i = this.mOnPageChangeListeners.size();
      for (int j = 0; j < i; j++)
      {
        OnPageChangeListener localOnPageChangeListener = (OnPageChangeListener)this.mOnPageChangeListeners.get(j);
        if (localOnPageChangeListener == null)
          continue;
        localOnPageChangeListener.onPageScrollStateChanged(paramInt);
      }
    }
    if (this.mInternalPageChangeListener != null)
      this.mInternalPageChangeListener.onPageScrollStateChanged(paramInt);
  }

  private void enableLayers(boolean paramBoolean)
  {
    int i = getChildCount();
    int j = 0;
    if (j < i)
    {
      if (paramBoolean);
      for (int k = this.mPageTransformerLayerType; ; k = 0)
      {
        ViewCompat.setLayerType(getChildAt(j), k, null);
        j++;
        break;
      }
    }
  }

  private void endDrag()
  {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    if (this.mVelocityTracker != null)
    {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    }
  }

  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView)
  {
    if (paramRect == null)
      paramRect = new Rect();
    if (paramView == null)
      paramRect.set(0, 0, 0, 0);
    while (true)
    {
      return paramRect;
      paramRect.left = paramView.getLeft();
      paramRect.right = paramView.getRight();
      paramRect.top = paramView.getTop();
      paramRect.bottom = paramView.getBottom();
      ViewGroup localViewGroup;
      for (ViewParent localViewParent = paramView.getParent(); ((localViewParent instanceof ViewGroup)) && (localViewParent != this); localViewParent = localViewGroup.getParent())
      {
        localViewGroup = (ViewGroup)localViewParent;
        paramRect.left += localViewGroup.getLeft();
        paramRect.right += localViewGroup.getRight();
        paramRect.top += localViewGroup.getTop();
        paramRect.bottom += localViewGroup.getBottom();
      }
    }
  }

  private int getClientWidth()
  {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }

  private ItemInfo infoForCurrentScrollPosition()
  {
    int i = getClientWidth();
    float f1;
    float f2;
    if (i > 0)
    {
      f1 = getScrollX() / i;
      if (i <= 0)
        break label259;
      f2 = this.mPageMargin / i;
    }
    while (true)
    {
      int j = -1;
      int k = 1;
      int m = 0;
      float f3 = 0.0F;
      float f4 = 0.0F;
      Object localObject = null;
      label49: ItemInfo localItemInfo;
      if (m < this.mItems.size())
      {
        localItemInfo = (ItemInfo)this.mItems.get(m);
        if ((k != 0) || (localItemInfo.position == j + 1))
          break label252;
        localItemInfo = this.mTempItem;
        localItemInfo.offset = (f2 + (f3 + f4));
        localItemInfo.position = (j + 1);
        localItemInfo.widthFactor = this.mAdapter.getPageWidth(localItemInfo.position);
      }
      label252: for (int n = m - 1; ; n = m)
      {
        float f5 = localItemInfo.offset;
        float f6 = localItemInfo.widthFactor;
        if ((k != 0) || (f1 >= f5))
        {
          if ((f1 < f2 + (f6 + f5)) || (n == -1 + this.mItems.size()))
            localObject = localItemInfo;
        }
        else
        {
          return localObject;
          f1 = 0.0F;
          break;
        }
        int i1 = localItemInfo.position;
        float f7 = localItemInfo.widthFactor;
        int i2 = n + 1;
        f3 = f5;
        f4 = f7;
        m = i2;
        j = i1;
        localObject = localItemInfo;
        k = 0;
        break label49;
      }
      label259: f2 = 0.0F;
    }
  }

  private static boolean isDecorView(@NonNull View paramView)
  {
    return paramView.getClass().getAnnotation(DecorView.class) != null;
  }

  private boolean isGutterDrag(float paramFloat1, float paramFloat2)
  {
    return ((paramFloat1 < this.mGutterSize) && (paramFloat2 > 0.0F)) || ((paramFloat1 > getWidth() - this.mGutterSize) && (paramFloat2 < 0.0F));
  }

  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
      if (i != 0)
        break label56;
    label56: for (int j = 1; ; j = 0)
    {
      this.mLastMotionX = paramMotionEvent.getX(j);
      this.mActivePointerId = paramMotionEvent.getPointerId(j);
      if (this.mVelocityTracker != null)
        this.mVelocityTracker.clear();
      return;
    }
  }

  private boolean pageScrolled(int paramInt)
  {
    if (this.mItems.size() == 0)
    {
      if (this.mFirstLayout);
      do
      {
        return false;
        this.mCalledSuper = false;
        onPageScrolled(0, 0.0F, 0);
      }
      while (this.mCalledSuper);
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }
    ItemInfo localItemInfo = infoForCurrentScrollPosition();
    int i = getClientWidth();
    int j = this.mPageMargin;
    float f1 = this.mPageMargin / i;
    int k = localItemInfo.position;
    float f2 = (paramInt / i - localItemInfo.offset) / (f1 + localItemInfo.widthFactor);
    int m = (int)(f2 * (i + j));
    this.mCalledSuper = false;
    onPageScrolled(k, f2, m);
    if (!this.mCalledSuper)
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    return true;
  }

  private boolean performDrag(float paramFloat)
  {
    int i = 1;
    float f1 = this.mLastMotionX;
    this.mLastMotionX = paramFloat;
    float f2 = getScrollX() + (f1 - paramFloat);
    int j = getClientWidth();
    float f3 = j * this.mFirstOffset;
    float f4 = j * this.mLastOffset;
    ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
    ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
    if (localItemInfo1.position != 0)
      f3 = localItemInfo1.offset * j;
    for (int k = 0; ; k = i)
    {
      float f5;
      if (localItemInfo2.position != -1 + this.mAdapter.getCount())
      {
        f5 = localItemInfo2.offset * j;
        i = 0;
      }
      while (true)
      {
        boolean bool;
        if (f2 < f3)
        {
          bool = false;
          if (k != 0)
            bool = this.mLeftEdge.onPull(Math.abs(f3 - f2) / j);
        }
        while (true)
        {
          this.mLastMotionX += f3 - (int)f3;
          scrollTo((int)f3, getScrollY());
          pageScrolled((int)f3);
          return bool;
          if (f2 > f5)
          {
            bool = false;
            if (i != 0)
              bool = this.mRightEdge.onPull(Math.abs(f2 - f5) / j);
            f3 = f5;
            continue;
          }
          f3 = f2;
          bool = false;
        }
        f5 = f4;
      }
    }
  }

  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt2 > 0) && (!this.mItems.isEmpty()))
    {
      if (!this.mScroller.isFinished())
      {
        this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        return;
      }
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = getPaddingLeft();
      int n = getPaddingRight();
      scrollTo((int)(getScrollX() / (paramInt4 + (paramInt2 - m - n)) * (paramInt3 + (paramInt1 - j - k))), getScrollY());
      return;
    }
    ItemInfo localItemInfo = infoForPosition(this.mCurItem);
    float f;
    if (localItemInfo != null)
      f = Math.min(localItemInfo.offset, this.mLastOffset);
    while (true)
    {
      int i = (int)(f * (paramInt1 - getPaddingLeft() - getPaddingRight()));
      if (i == getScrollX())
        break;
      completeScroll(false);
      scrollTo(i, getScrollY());
      return;
      f = 0.0F;
    }
  }

  private void removeNonDecorViews()
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      if (((LayoutParams)getChildAt(i).getLayoutParams()).isDecor)
        continue;
      removeViewAt(i);
      i--;
    }
  }

  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    ViewParent localViewParent = getParent();
    if (localViewParent != null)
      localViewParent.requestDisallowInterceptTouchEvent(paramBoolean);
  }

  private boolean resetTouch()
  {
    this.mActivePointerId = -1;
    endDrag();
    return this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
  }

  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    ItemInfo localItemInfo = infoForPosition(paramInt1);
    if (localItemInfo != null);
    for (int i = (int)(getClientWidth() * Math.max(this.mFirstOffset, Math.min(localItemInfo.offset, this.mLastOffset))); ; i = 0)
    {
      if (paramBoolean1)
      {
        smoothScrollTo(i, 0, paramInt2);
        if (paramBoolean2)
          dispatchOnPageSelected(paramInt1);
        return;
      }
      if (paramBoolean2)
        dispatchOnPageSelected(paramInt1);
      completeScroll(false);
      scrollTo(i, 0);
      pageScrolled(i);
      return;
    }
  }

  private void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if (this.mScrollingCacheEnabled != paramBoolean)
      this.mScrollingCacheEnabled = paramBoolean;
  }

  private void sortChildDrawingOrder()
  {
    if (this.mDrawingOrder != 0)
    {
      if (this.mDrawingOrderedChildren == null)
        this.mDrawingOrderedChildren = new ArrayList();
      while (true)
      {
        int i = getChildCount();
        for (int j = 0; j < i; j++)
        {
          View localView = getChildAt(j);
          this.mDrawingOrderedChildren.add(localView);
        }
        this.mDrawingOrderedChildren.clear();
      }
      Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
    }
  }

  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216)
      for (int k = 0; k < getChildCount(); k++)
      {
        View localView = getChildAt(k);
        if (localView.getVisibility() != 0)
          continue;
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem))
          continue;
        localView.addFocusables(paramArrayList, paramInt1, paramInt2);
      }
    if (((j == 262144) && (i != paramArrayList.size())) || (!isFocusable()));
    do
      return;
    while ((((paramInt2 & 0x1) == 1) && (isInTouchMode()) && (!isFocusableInTouchMode())) || (paramArrayList == null));
    paramArrayList.add(this);
  }

  ItemInfo addNewItem(int paramInt1, int paramInt2)
  {
    ItemInfo localItemInfo = new ItemInfo();
    localItemInfo.position = paramInt1;
    localItemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
    localItemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
    if ((paramInt2 < 0) || (paramInt2 >= this.mItems.size()))
    {
      this.mItems.add(localItemInfo);
      return localItemInfo;
    }
    this.mItems.add(paramInt2, localItemInfo);
    return localItemInfo;
  }

  public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    if (this.mAdapterChangeListeners == null)
      this.mAdapterChangeListeners = new ArrayList();
    this.mAdapterChangeListeners.add(paramOnAdapterChangeListener);
  }

  public void addOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    if (this.mOnPageChangeListeners == null)
      this.mOnPageChangeListeners = new ArrayList();
    this.mOnPageChangeListeners.add(paramOnPageChangeListener);
  }

  public void addTouchables(ArrayList<View> paramArrayList)
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() != 0)
        continue;
      ItemInfo localItemInfo = infoForChild(localView);
      if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem))
        continue;
      localView.addTouchables(paramArrayList);
    }
  }

  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!checkLayoutParams(paramLayoutParams));
    for (ViewGroup.LayoutParams localLayoutParams = generateLayoutParams(paramLayoutParams); ; localLayoutParams = paramLayoutParams)
    {
      LayoutParams localLayoutParams1 = (LayoutParams)localLayoutParams;
      localLayoutParams1.isDecor |= isDecorView(paramView);
      if (this.mInLayout)
      {
        if ((localLayoutParams1 != null) && (localLayoutParams1.isDecor))
          throw new IllegalStateException("Cannot add pager decor view during layout");
        localLayoutParams1.needsMeasure = true;
        addViewInLayout(paramView, paramInt, localLayoutParams);
        return;
      }
      super.addView(paramView, paramInt, localLayoutParams);
      return;
    }
  }

  public boolean arrowScroll(int paramInt)
  {
    View localView1 = findFocus();
    View localView2 = null;
    View localView3;
    boolean bool;
    label86: ViewParent localViewParent1;
    if (localView1 == this)
    {
      localView3 = FocusFinder.getInstance().findNextFocus(this, localView2, paramInt);
      if ((localView3 == null) || (localView3 == localView2))
        break label324;
      if (paramInt != 17)
        break label259;
      int k = getChildRectInPagerCoordinates(this.mTempRect, localView3).left;
      int m = getChildRectInPagerCoordinates(this.mTempRect, localView2).left;
      if ((localView2 != null) && (k >= m))
      {
        bool = pageLeft();
        if (bool)
          playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
        return bool;
      }
    }
    else
    {
      if (localView1 == null)
        break label370;
      localViewParent1 = localView1.getParent();
      if (!(localViewParent1 instanceof ViewGroup))
        break label375;
      if (localViewParent1 != this);
    }
    label259: label324: label370: label375: for (int n = 1; ; n = 0)
    {
      if (n == 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(localView1.getClass().getSimpleName());
        ViewParent localViewParent2 = localView1.getParent();
        while (true)
          if ((localViewParent2 instanceof ViewGroup))
          {
            localStringBuilder.append(" => ").append(localViewParent2.getClass().getSimpleName());
            localViewParent2 = localViewParent2.getParent();
            continue;
            localViewParent1 = localViewParent1.getParent();
            break;
          }
        Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + localStringBuilder.toString());
        localView2 = null;
        break;
        bool = localView3.requestFocus();
        break label86;
        if (paramInt == 66)
        {
          int i = getChildRectInPagerCoordinates(this.mTempRect, localView3).left;
          int j = getChildRectInPagerCoordinates(this.mTempRect, localView2).left;
          if ((localView2 != null) && (i <= j))
          {
            bool = pageRight();
            break label86;
          }
          bool = localView3.requestFocus();
          break label86;
          if ((paramInt == 17) || (paramInt == 1))
          {
            bool = pageLeft();
            break label86;
          }
          if ((paramInt == 66) || (paramInt == 2))
          {
            bool = pageRight();
            break label86;
          }
        }
        bool = false;
        break label86;
      }
      localView2 = localView1;
      break;
    }
  }

  public boolean beginFakeDrag()
  {
    if (this.mIsBeingDragged)
      return false;
    this.mFakeDragging = true;
    setScrollState(1);
    this.mLastMotionX = 0.0F;
    this.mInitialMotionX = 0.0F;
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain();
    while (true)
    {
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
      this.mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      this.mFakeDragBeginTime = l;
      return true;
      this.mVelocityTracker.clear();
    }
  }

  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    int k;
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      k = -1 + localViewGroup.getChildCount();
      if (k >= 0)
      {
        View localView = localViewGroup.getChildAt(k);
        if ((paramInt2 + i < localView.getLeft()) || (paramInt2 + i >= localView.getRight()) || (paramInt3 + j < localView.getTop()) || (paramInt3 + j >= localView.getBottom()) || (!canScroll(localView, true, paramInt1, paramInt2 + i - localView.getLeft(), paramInt3 + j - localView.getTop())));
      }
    }
    do
    {
      return true;
      k--;
      break;
    }
    while ((paramBoolean) && (ViewCompat.canScrollHorizontally(paramView, -paramInt1)));
    return false;
  }

  public boolean canScrollHorizontally(int paramInt)
  {
    if (this.mAdapter == null);
    int i;
    int j;
    do
      while (true)
      {
        return false;
        i = getClientWidth();
        j = getScrollX();
        if (paramInt >= 0)
          break;
        if (j > (int)(i * this.mFirstOffset))
          return true;
      }
    while ((paramInt <= 0) || (j >= (int)(i * this.mLastOffset)));
    return true;
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return ((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams));
  }

  public void clearOnPageChangeListeners()
  {
    if (this.mOnPageChangeListeners != null)
      this.mOnPageChangeListeners.clear();
  }

  public void computeScroll()
  {
    this.mIsScrollStarted = true;
    if ((!this.mScroller.isFinished()) && (this.mScroller.computeScrollOffset()))
    {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if ((i != k) || (j != m))
      {
        scrollTo(k, m);
        if (!pageScrolled(k))
        {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        }
      }
      ViewCompat.postInvalidateOnAnimation(this);
      return;
    }
    completeScroll(true);
  }

  void dataSetChanged()
  {
    int i = this.mAdapter.getCount();
    this.mExpectedAdapterCount = i;
    int j;
    int m;
    int n;
    int i1;
    int i2;
    label61: ItemInfo localItemInfo;
    int i5;
    int i6;
    int i7;
    int i8;
    int i9;
    if ((this.mItems.size() < 1 + 2 * this.mOffscreenPageLimit) && (this.mItems.size() < i))
    {
      j = 1;
      int k = this.mCurItem;
      m = 0;
      n = k;
      i1 = j;
      i2 = 0;
      if (i2 >= this.mItems.size())
        break label307;
      localItemInfo = (ItemInfo)this.mItems.get(i2);
      i5 = this.mAdapter.getItemPosition(localItemInfo.object);
      if (i5 != -1)
        break label153;
      i6 = i2;
      i7 = m;
      i8 = n;
      i9 = i1;
    }
    while (true)
    {
      int i10 = i6 + 1;
      i1 = i9;
      n = i8;
      m = i7;
      i2 = i10;
      break label61;
      j = 0;
      break;
      label153: int i11;
      if (i5 == -2)
      {
        this.mItems.remove(i2);
        i11 = i2 - 1;
        if (m == 0)
        {
          this.mAdapter.startUpdate(this);
          m = 1;
        }
        this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
        if (this.mCurItem == localItemInfo.position)
        {
          int i12 = Math.max(0, Math.min(this.mCurItem, i - 1));
          i6 = i11;
          i7 = m;
          i8 = i12;
          i9 = 1;
          continue;
        }
      }
      else
      {
        if (localItemInfo.position != i5)
        {
          if (localItemInfo.position == this.mCurItem)
            n = i5;
          localItemInfo.position = i5;
          i6 = i2;
          i7 = m;
          i8 = n;
          i9 = 1;
          continue;
          label307: if (m != 0)
            this.mAdapter.finishUpdate(this);
          Collections.sort(this.mItems, COMPARATOR);
          if (i1 != 0)
          {
            int i3 = getChildCount();
            for (int i4 = 0; i4 < i3; i4++)
            {
              LayoutParams localLayoutParams = (LayoutParams)getChildAt(i4).getLayoutParams();
              if (localLayoutParams.isDecor)
                continue;
              localLayoutParams.widthFactor = 0.0F;
            }
            setCurrentItemInternal(n, false, true);
            requestLayout();
          }
          return;
        }
        i6 = i2;
        i7 = m;
        i8 = n;
        i9 = i1;
        continue;
      }
      i6 = i11;
      i7 = m;
      i8 = n;
      i9 = 1;
    }
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    return (super.dispatchKeyEvent(paramKeyEvent)) || (executeKeyEvent(paramKeyEvent));
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    boolean bool;
    if (paramAccessibilityEvent.getEventType() == 4096)
    {
      bool = super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
      return bool;
    }
    int i = getChildCount();
    for (int j = 0; ; j++)
    {
      bool = false;
      if (j >= i)
        break;
      View localView = getChildAt(j);
      if (localView.getVisibility() != 0)
        continue;
      ItemInfo localItemInfo = infoForChild(localView);
      if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent)))
        return true;
    }
  }

  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)(0.47123891676382D * (paramFloat - 0.5F)));
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    int i = getOverScrollMode();
    boolean bool2;
    if ((i == 0) || ((i == 1) && (this.mAdapter != null) && (this.mAdapter.getCount() > 1)))
    {
      boolean bool1 = this.mLeftEdge.isFinished();
      bool2 = false;
      if (!bool1)
      {
        int i2 = paramCanvas.save();
        int i3 = getHeight() - getPaddingTop() - getPaddingBottom();
        int i4 = getWidth();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-i3 + getPaddingTop(), this.mFirstOffset * i4);
        this.mLeftEdge.setSize(i3, i4);
        bool2 = false | this.mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(i2);
      }
      if (!this.mRightEdge.isFinished())
      {
        int j = paramCanvas.save();
        int k = getWidth();
        int m = getHeight();
        int n = getPaddingTop();
        int i1 = getPaddingBottom();
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), -(1.0F + this.mLastOffset) * k);
        this.mRightEdge.setSize(m - n - i1, k);
        bool2 |= this.mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(j);
      }
    }
    while (true)
    {
      if (bool2)
        ViewCompat.postInvalidateOnAnimation(this);
      return;
      this.mLeftEdge.finish();
      this.mRightEdge.finish();
      bool2 = false;
    }
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = this.mMarginDrawable;
    if ((localDrawable != null) && (localDrawable.isStateful()))
      localDrawable.setState(getDrawableState());
  }

  public void endFakeDrag()
  {
    if (!this.mFakeDragging)
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    if (this.mAdapter != null)
    {
      VelocityTracker localVelocityTracker = this.mVelocityTracker;
      localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
      int i = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker, this.mActivePointerId);
      this.mPopulatePending = true;
      int j = getClientWidth();
      int k = getScrollX();
      ItemInfo localItemInfo = infoForCurrentScrollPosition();
      setCurrentItemInternal(determineTargetPage(localItemInfo.position, (k / j - localItemInfo.offset) / localItemInfo.widthFactor, i, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, i);
    }
    endDrag();
    this.mFakeDragging = false;
  }

  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getAction() == 0)
      switch (paramKeyEvent.getKeyCode())
      {
      default:
      case 21:
      case 22:
      case 61:
      }
    do
    {
      do
      {
        return false;
        return arrowScroll(17);
        return arrowScroll(66);
      }
      while (Build.VERSION.SDK_INT < 11);
      if (KeyEventCompat.hasNoModifiers(paramKeyEvent))
        return arrowScroll(2);
    }
    while (!KeyEventCompat.hasModifiers(paramKeyEvent, 1));
    return arrowScroll(1);
  }

  public void fakeDragBy(float paramFloat)
  {
    if (!this.mFakeDragging)
      throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    if (this.mAdapter == null)
      return;
    this.mLastMotionX = (paramFloat + this.mLastMotionX);
    float f1 = getScrollX() - paramFloat;
    int i = getClientWidth();
    float f2 = i * this.mFirstOffset;
    float f3 = i * this.mLastOffset;
    ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
    ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(-1 + this.mItems.size());
    float f4;
    if (localItemInfo1.position != 0)
      f4 = localItemInfo1.offset * i;
    while (true)
    {
      float f5;
      if (localItemInfo2.position != -1 + this.mAdapter.getCount())
        f5 = localItemInfo2.offset * i;
      while (true)
      {
        if (f1 < f4);
        while (true)
        {
          this.mLastMotionX += f4 - (int)f4;
          scrollTo((int)f4, getScrollY());
          pageScrolled((int)f4);
          long l = SystemClock.uptimeMillis();
          MotionEvent localMotionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2, this.mLastMotionX, 0.0F, 0);
          this.mVelocityTracker.addMovement(localMotionEvent);
          localMotionEvent.recycle();
          return;
          if (f1 > f5)
          {
            f4 = f5;
            continue;
          }
          f4 = f1;
        }
        f5 = f3;
      }
      f4 = f2;
    }
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }

  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return generateDefaultLayoutParams();
  }

  public PagerAdapter getAdapter()
  {
    return this.mAdapter;
  }

  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (this.mDrawingOrder == 2)
      paramInt2 = paramInt1 - 1 - paramInt2;
    return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(paramInt2)).getLayoutParams()).childIndex;
  }

  public int getCurrentItem()
  {
    return this.mCurItem;
  }

  public int getOffscreenPageLimit()
  {
    return this.mOffscreenPageLimit;
  }

  public int getPageMargin()
  {
    return this.mPageMargin;
  }

  ItemInfo infoForAnyChild(View paramView)
  {
    while (true)
    {
      ViewParent localViewParent = paramView.getParent();
      if (localViewParent == this)
        break;
      if ((localViewParent == null) || (!(localViewParent instanceof View)))
        return null;
      paramView = (View)localViewParent;
    }
    return infoForChild(paramView);
  }

  ItemInfo infoForChild(View paramView)
  {
    for (int i = 0; i < this.mItems.size(); i++)
    {
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(i);
      if (this.mAdapter.isViewFromObject(paramView, localItemInfo.object))
        return localItemInfo;
    }
    return null;
  }

  ItemInfo infoForPosition(int paramInt)
  {
    for (int i = 0; i < this.mItems.size(); i++)
    {
      ItemInfo localItemInfo = (ItemInfo)this.mItems.get(i);
      if (localItemInfo.position == paramInt)
        return localItemInfo;
    }
    return null;
  }

  void initViewPager()
  {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context localContext = getContext();
    this.mScroller = new Scroller(localContext, sInterpolator);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    float f = localContext.getResources().getDisplayMetrics().density;
    this.mTouchSlop = localViewConfiguration.getScaledPagingTouchSlop();
    this.mMinimumVelocity = (int)(400.0F * f);
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mLeftEdge = new EdgeEffectCompat(localContext);
    this.mRightEdge = new EdgeEffectCompat(localContext);
    this.mFlingDistance = (int)(25.0F * f);
    this.mCloseEnough = (int)(2.0F * f);
    this.mDefaultGutterSize = (int)(16.0F * f);
    ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
    if (ViewCompat.getImportantForAccessibility(this) == 0)
      ViewCompat.setImportantForAccessibility(this, 1);
    ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener()
    {
      private final Rect mTempRect = new Rect();

      public WindowInsetsCompat onApplyWindowInsets(View paramView, WindowInsetsCompat paramWindowInsetsCompat)
      {
        WindowInsetsCompat localWindowInsetsCompat1 = ViewCompat.onApplyWindowInsets(paramView, paramWindowInsetsCompat);
        if (localWindowInsetsCompat1.isConsumed())
          return localWindowInsetsCompat1;
        Rect localRect = this.mTempRect;
        localRect.left = localWindowInsetsCompat1.getSystemWindowInsetLeft();
        localRect.top = localWindowInsetsCompat1.getSystemWindowInsetTop();
        localRect.right = localWindowInsetsCompat1.getSystemWindowInsetRight();
        localRect.bottom = localWindowInsetsCompat1.getSystemWindowInsetBottom();
        int i = 0;
        int j = ViewPager.this.getChildCount();
        while (i < j)
        {
          WindowInsetsCompat localWindowInsetsCompat2 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), localWindowInsetsCompat1);
          localRect.left = Math.min(localWindowInsetsCompat2.getSystemWindowInsetLeft(), localRect.left);
          localRect.top = Math.min(localWindowInsetsCompat2.getSystemWindowInsetTop(), localRect.top);
          localRect.right = Math.min(localWindowInsetsCompat2.getSystemWindowInsetRight(), localRect.right);
          localRect.bottom = Math.min(localWindowInsetsCompat2.getSystemWindowInsetBottom(), localRect.bottom);
          i++;
        }
        return localWindowInsetsCompat1.replaceSystemWindowInsets(localRect.left, localRect.top, localRect.right, localRect.bottom);
      }
    });
  }

  public boolean isFakeDragging()
  {
    return this.mFakeDragging;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }

  protected void onDetachedFromWindow()
  {
    removeCallbacks(this.mEndScrollRunnable);
    if ((this.mScroller != null) && (!this.mScroller.isFinished()))
      this.mScroller.abortAnimation();
    super.onDetachedFromWindow();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i;
    int j;
    float f1;
    ItemInfo localItemInfo;
    int k;
    int m;
    int n;
    int i1;
    float f3;
    if ((this.mPageMargin > 0) && (this.mMarginDrawable != null) && (this.mItems.size() > 0) && (this.mAdapter != null))
    {
      i = getScrollX();
      j = getWidth();
      f1 = this.mPageMargin / j;
      localItemInfo = (ItemInfo)this.mItems.get(0);
      float f2 = localItemInfo.offset;
      k = this.mItems.size();
      m = localItemInfo.position;
      n = ((ItemInfo)this.mItems.get(k - 1)).position;
      i1 = 0;
      f3 = f2;
    }
    for (int i2 = m; ; i2++)
    {
      float f5;
      if (i2 < n)
      {
        while ((i2 > localItemInfo.position) && (i1 < k))
        {
          ArrayList localArrayList = this.mItems;
          i1++;
          localItemInfo = (ItemInfo)localArrayList.get(i1);
        }
        if (i2 != localItemInfo.position)
          break label276;
        f5 = (localItemInfo.offset + localItemInfo.widthFactor) * j;
        f3 = f1 + (localItemInfo.offset + localItemInfo.widthFactor);
      }
      while (true)
      {
        if (f5 + this.mPageMargin > i)
        {
          this.mMarginDrawable.setBounds(Math.round(f5), this.mTopPageBounds, Math.round(f5 + this.mPageMargin), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        }
        if (f5 <= i + j)
          break;
        return;
        label276: float f4 = this.mAdapter.getPageWidth(i2);
        f5 = (f3 + f4) * j;
        f3 += f4 + f1;
      }
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = 0xFF & paramMotionEvent.getAction();
    if ((i == 3) || (i == 1))
      resetTouch();
    do
    {
      return false;
      if (i == 0)
        break;
      if (this.mIsBeingDragged)
        return true;
    }
    while (this.mIsUnableToDrag);
    switch (i)
    {
    default:
    case 2:
    case 0:
    case 6:
    }
    while (true)
    {
      if (this.mVelocityTracker == null)
        this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
      return this.mIsBeingDragged;
      int j = this.mActivePointerId;
      if (j == -1)
        continue;
      int k = paramMotionEvent.findPointerIndex(j);
      float f3 = paramMotionEvent.getX(k);
      float f4 = f3 - this.mLastMotionX;
      float f5 = Math.abs(f4);
      float f6 = paramMotionEvent.getY(k);
      float f7 = Math.abs(f6 - this.mInitialMotionY);
      if ((f4 != 0.0F) && (!isGutterDrag(this.mLastMotionX, f4)) && (canScroll(this, false, (int)f4, (int)f3, (int)f6)))
      {
        this.mLastMotionX = f3;
        this.mLastMotionY = f6;
        this.mIsUnableToDrag = true;
        return false;
      }
      float f8;
      if ((f5 > this.mTouchSlop) && (0.5F * f5 > f7))
      {
        this.mIsBeingDragged = true;
        requestParentDisallowInterceptTouchEvent(true);
        setScrollState(1);
        if (f4 > 0.0F)
        {
          f8 = this.mInitialMotionX + this.mTouchSlop;
          label285: this.mLastMotionX = f8;
          this.mLastMotionY = f6;
          setScrollingCacheEnabled(true);
        }
      }
      while ((this.mIsBeingDragged) && (performDrag(f3)))
      {
        ViewCompat.postInvalidateOnAnimation(this);
        break;
        f8 = this.mInitialMotionX - this.mTouchSlop;
        break label285;
        if (f7 <= this.mTouchSlop)
          continue;
        this.mIsUnableToDrag = true;
      }
      float f1 = paramMotionEvent.getX();
      this.mInitialMotionX = f1;
      this.mLastMotionX = f1;
      float f2 = paramMotionEvent.getY();
      this.mInitialMotionY = f2;
      this.mLastMotionY = f2;
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsUnableToDrag = false;
      this.mIsScrollStarted = true;
      this.mScroller.computeScrollOffset();
      if ((this.mScrollState == 2) && (Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough))
      {
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        this.mIsBeingDragged = true;
        requestParentDisallowInterceptTouchEvent(true);
        setScrollState(1);
        continue;
      }
      completeScroll(false);
      this.mIsBeingDragged = false;
      continue;
      onSecondaryPointerUp(paramMotionEvent);
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    int m = getPaddingLeft();
    int n = getPaddingTop();
    int i1 = getPaddingRight();
    int i2 = getPaddingBottom();
    int i3 = getScrollX();
    int i4 = 0;
    int i5 = 0;
    View localView2;
    int i17;
    int i18;
    int i19;
    label164: int i21;
    int i22;
    label212: int i12;
    int i10;
    int i11;
    int i9;
    if (i5 < i)
    {
      localView2 = getChildAt(i5);
      if (localView2.getVisibility() == 8)
        break label683;
      LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
      if (!localLayoutParams2.isDecor)
        break label683;
      int i13 = localLayoutParams2.gravity;
      int i14 = localLayoutParams2.gravity;
      switch (i13 & 0x7)
      {
      case 2:
      case 4:
      default:
        i17 = m;
        i18 = m;
        i19 = i1;
        switch (i14 & 0x70)
        {
        default:
          i21 = i2;
          i22 = n;
          int i23 = i18 + i3;
          localView2.layout(i23, i22, i23 + localView2.getMeasuredWidth(), i22 + localView2.getMeasuredHeight());
          int i24 = i4 + 1;
          i12 = i17;
          i10 = n;
          i11 = i19;
          i2 = i21;
          i9 = i24;
        case 48:
        case 16:
        case 80:
        }
      case 3:
      case 1:
      case 5:
      }
    }
    while (true)
    {
      i5++;
      m = i12;
      i1 = i11;
      n = i10;
      i4 = i9;
      break;
      i17 = m + localView2.getMeasuredWidth();
      i18 = m;
      i19 = i1;
      break label164;
      int i29 = Math.max((j - localView2.getMeasuredWidth()) / 2, m);
      i17 = m;
      i18 = i29;
      i19 = i1;
      break label164;
      int i15 = j - i1 - localView2.getMeasuredWidth();
      int i16 = i1 + localView2.getMeasuredWidth();
      i17 = m;
      i18 = i15;
      i19 = i16;
      break label164;
      int i27 = n + localView2.getMeasuredHeight();
      int i28 = i2;
      i22 = n;
      n = i27;
      i21 = i28;
      break label212;
      int i25 = Math.max((k - localView2.getMeasuredHeight()) / 2, n);
      int i26 = i2;
      i22 = i25;
      i21 = i26;
      break label212;
      int i20 = k - i2 - localView2.getMeasuredHeight();
      i21 = i2 + localView2.getMeasuredHeight();
      i22 = i20;
      break label212;
      int i6 = j - m - i1;
      for (int i7 = 0; i7 < i; i7++)
      {
        View localView1 = getChildAt(i7);
        if (localView1.getVisibility() == 8)
          continue;
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if (localLayoutParams1.isDecor)
          continue;
        ItemInfo localItemInfo = infoForChild(localView1);
        if (localItemInfo == null)
          continue;
        int i8 = m + (int)(i6 * localItemInfo.offset);
        if (localLayoutParams1.needsMeasure)
        {
          localLayoutParams1.needsMeasure = false;
          localView1.measure(View.MeasureSpec.makeMeasureSpec((int)(i6 * localLayoutParams1.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(k - n - i2, 1073741824));
        }
        localView1.layout(i8, n, i8 + localView1.getMeasuredWidth(), n + localView1.getMeasuredHeight());
      }
      this.mTopPageBounds = n;
      this.mBottomPageBounds = (k - i2);
      this.mDecorChildCount = i4;
      if (this.mFirstLayout)
        scrollToItem(this.mCurItem, false, 0, false);
      this.mFirstLayout = false;
      return;
      label683: i9 = i4;
      i10 = n;
      i11 = i1;
      i12 = m;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int i = getMeasuredWidth();
    this.mGutterSize = Math.min(i / 10, this.mDefaultGutterSize);
    int j = i - getPaddingLeft() - getPaddingRight();
    int k = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int m = getChildCount();
    int n = 0;
    View localView2;
    LayoutParams localLayoutParams2;
    int i5;
    int i6;
    int i7;
    label167: int i8;
    label182: label192: int i9;
    int i10;
    if (n < m)
    {
      localView2 = getChildAt(n);
      if (localView2.getVisibility() != 8)
      {
        localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if ((localLayoutParams2 != null) && (localLayoutParams2.isDecor))
        {
          int i3 = 0x7 & localLayoutParams2.gravity;
          int i4 = 0x70 & localLayoutParams2.gravity;
          i5 = -2147483648;
          i6 = -2147483648;
          if ((i4 != 48) && (i4 != 80))
            break label294;
          i7 = 1;
          if ((i3 != 3) && (i3 != 5))
            break label300;
          i8 = 1;
          if (i7 == 0)
            break label306;
          i5 = 1073741824;
          if (localLayoutParams2.width == -2)
            break label478;
          i9 = 1073741824;
          if (localLayoutParams2.width == -1)
            break label471;
          i10 = localLayoutParams2.width;
        }
      }
    }
    while (true)
    {
      if (localLayoutParams2.height != -2)
      {
        i6 = 1073741824;
        if (localLayoutParams2.height == -1);
      }
      for (int i11 = localLayoutParams2.height; ; i11 = k)
      {
        localView2.measure(View.MeasureSpec.makeMeasureSpec(i10, i9), View.MeasureSpec.makeMeasureSpec(i11, i6));
        if (i7 != 0)
          k -= localView2.getMeasuredHeight();
        while (true)
        {
          n++;
          break;
          label294: i7 = 0;
          break label167;
          label300: i8 = 0;
          break label182;
          label306: if (i8 == 0)
            break label192;
          i6 = 1073741824;
          break label192;
          if (i8 == 0)
            continue;
          j -= localView2.getMeasuredWidth();
        }
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int i1 = getChildCount();
        for (int i2 = 0; i2 < i1; i2++)
        {
          View localView1 = getChildAt(i2);
          if (localView1.getVisibility() == 8)
            continue;
          LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
          if ((localLayoutParams1 != null) && (localLayoutParams1.isDecor))
            continue;
          localView1.measure(View.MeasureSpec.makeMeasureSpec((int)(j * localLayoutParams1.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
        }
        return;
      }
      label471: i10 = j;
      continue;
      label478: i9 = i5;
      i10 = j;
    }
  }

  @CallSuper
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (this.mDecorChildCount > 0)
    {
      int m = getScrollX();
      int n = getPaddingLeft();
      int i1 = getPaddingRight();
      int i2 = getWidth();
      int i3 = getChildCount();
      int i4 = 0;
      while (i4 < i3)
      {
        View localView2 = getChildAt(i4);
        LayoutParams localLayoutParams = (LayoutParams)localView2.getLayoutParams();
        int i8;
        int i9;
        if (!localLayoutParams.isDecor)
        {
          i8 = n;
          i9 = i1;
          i4++;
          i1 = i9;
          n = i8;
          continue;
        }
        int i7;
        switch (0x7 & localLayoutParams.gravity)
        {
        case 2:
        case 4:
        default:
          i8 = n;
          int i14 = n;
          i9 = i1;
          i7 = i14;
        case 3:
        case 1:
        case 5:
        }
        while (true)
        {
          int i10 = i7 + m - localView2.getLeft();
          if (i10 == 0)
            break;
          localView2.offsetLeftAndRight(i10);
          break;
          i8 = n + localView2.getWidth();
          int i13 = i1;
          i7 = n;
          i9 = i13;
          continue;
          int i11 = Math.max((i2 - localView2.getMeasuredWidth()) / 2, n);
          int i12 = i1;
          i7 = i11;
          i8 = n;
          i9 = i12;
          continue;
          int i5 = i2 - i1 - localView2.getMeasuredWidth();
          int i6 = i1 + localView2.getMeasuredWidth();
          i7 = i5;
          i8 = n;
          i9 = i6;
        }
      }
    }
    dispatchOnPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mPageTransformer != null)
    {
      int i = getScrollX();
      int j = getChildCount();
      int k = 0;
      if (k < j)
      {
        View localView1 = getChildAt(k);
        if (((LayoutParams)localView1.getLayoutParams()).isDecor);
        while (true)
        {
          k++;
          break;
          float f = (localView1.getLeft() - i) / getClientWidth();
          this.mPageTransformer.transformPage(localView1, f);
        }
      }
    }
    this.mCalledSuper = true;
  }

  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i = -1;
    int j = getChildCount();
    int k;
    if ((paramInt & 0x2) != 0)
    {
      i = 1;
      k = 0;
    }
    while (k != j)
    {
      View localView = getChildAt(k);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem) && (localView.requestFocus(paramInt, paramRect)))
        {
          return true;
          k = j - 1;
          j = i;
          continue;
        }
      }
      k += i;
    }
    return false;
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if (this.mAdapter != null)
    {
      this.mAdapter.restoreState(localSavedState.adapterState, localSavedState.loader);
      setCurrentItemInternal(localSavedState.position, false, true);
      return;
    }
    this.mRestoredCurItem = localSavedState.position;
    this.mRestoredAdapterState = localSavedState.adapterState;
    this.mRestoredClassLoader = localSavedState.loader;
  }

  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.position = this.mCurItem;
    if (this.mAdapter != null)
      localSavedState.adapterState = this.mAdapter.saveState();
    return localSavedState;
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      recomputeScrollPosition(paramInt1, paramInt3, this.mPageMargin, this.mPageMargin);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mFakeDragging);
    while (true)
    {
      return true;
      if ((paramMotionEvent.getAction() == 0) && (paramMotionEvent.getEdgeFlags() != 0))
        return false;
      if ((this.mAdapter == null) || (this.mAdapter.getCount() == 0))
        return false;
      if (this.mVelocityTracker == null)
        this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
      int i = 0xFF & paramMotionEvent.getAction();
      boolean bool1 = false;
      switch (i)
      {
      case 4:
      default:
      case 0:
      case 2:
      case 1:
      case 3:
      case 5:
      case 6:
      }
      while (bool1)
      {
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
        this.mScroller.abortAnimation();
        this.mPopulatePending = false;
        populate();
        float f7 = paramMotionEvent.getX();
        this.mInitialMotionX = f7;
        this.mLastMotionX = f7;
        float f8 = paramMotionEvent.getY();
        this.mInitialMotionY = f8;
        this.mLastMotionY = f8;
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        bool1 = false;
        continue;
        float f4;
        float f6;
        if (!this.mIsBeingDragged)
        {
          int i1 = paramMotionEvent.findPointerIndex(this.mActivePointerId);
          if (i1 == -1)
          {
            bool1 = resetTouch();
            continue;
          }
          float f2 = paramMotionEvent.getX(i1);
          float f3 = Math.abs(f2 - this.mLastMotionX);
          f4 = paramMotionEvent.getY(i1);
          float f5 = Math.abs(f4 - this.mLastMotionY);
          if ((f3 > this.mTouchSlop) && (f3 > f5))
          {
            this.mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            if (f2 - this.mInitialMotionX <= 0.0F)
              break label396;
            f6 = this.mInitialMotionX + this.mTouchSlop;
          }
        }
        while (true)
        {
          this.mLastMotionX = f6;
          this.mLastMotionY = f4;
          setScrollState(1);
          setScrollingCacheEnabled(true);
          ViewParent localViewParent = getParent();
          if (localViewParent != null)
            localViewParent.requestDisallowInterceptTouchEvent(true);
          boolean bool4 = this.mIsBeingDragged;
          bool1 = false;
          if (!bool4)
            break;
          bool1 = false | performDrag(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId)));
          break;
          label396: f6 = this.mInitialMotionX - this.mTouchSlop;
        }
        boolean bool3 = this.mIsBeingDragged;
        bool1 = false;
        if (!bool3)
          continue;
        VelocityTracker localVelocityTracker = this.mVelocityTracker;
        localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int k = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker, this.mActivePointerId);
        this.mPopulatePending = true;
        int m = getClientWidth();
        int n = getScrollX();
        ItemInfo localItemInfo = infoForCurrentScrollPosition();
        float f1 = this.mPageMargin / m;
        setCurrentItemInternal(determineTargetPage(localItemInfo.position, (n / m - localItemInfo.offset) / (f1 + localItemInfo.widthFactor), k, (int)(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, k);
        bool1 = resetTouch();
        continue;
        boolean bool2 = this.mIsBeingDragged;
        bool1 = false;
        if (!bool2)
          continue;
        scrollToItem(this.mCurItem, true, 0, false);
        bool1 = resetTouch();
        continue;
        int j = MotionEventCompat.getActionIndex(paramMotionEvent);
        this.mLastMotionX = paramMotionEvent.getX(j);
        this.mActivePointerId = paramMotionEvent.getPointerId(j);
        bool1 = false;
        continue;
        onSecondaryPointerUp(paramMotionEvent);
        this.mLastMotionX = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId));
        bool1 = false;
      }
    }
  }

  boolean pageLeft()
  {
    if (this.mCurItem > 0)
    {
      setCurrentItem(-1 + this.mCurItem, true);
      return true;
    }
    return false;
  }

  boolean pageRight()
  {
    if ((this.mAdapter != null) && (this.mCurItem < -1 + this.mAdapter.getCount()))
    {
      setCurrentItem(1 + this.mCurItem, true);
      return true;
    }
    return false;
  }

  void populate()
  {
    populate(this.mCurItem);
  }

  void populate(int paramInt)
  {
    ItemInfo localItemInfo12;
    if (this.mCurItem != paramInt)
    {
      localItemInfo12 = infoForPosition(this.mCurItem);
      this.mCurItem = paramInt;
    }
    for (ItemInfo localItemInfo1 = localItemInfo12; ; localItemInfo1 = null)
    {
      if (this.mAdapter == null)
        sortChildDrawingOrder();
      do
      {
        return;
        if (!this.mPopulatePending)
          continue;
        sortChildDrawingOrder();
        return;
      }
      while (getWindowToken() == null);
      this.mAdapter.startUpdate(this);
      int i = this.mOffscreenPageLimit;
      int j = Math.max(0, this.mCurItem - i);
      int k = this.mAdapter.getCount();
      int m = Math.min(k - 1, i + this.mCurItem);
      if (k != this.mExpectedAdapterCount)
        try
        {
          String str2 = getResources().getResourceName(getId());
          str1 = str2;
          throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + k + " Pager id: " + str1 + " Pager class: " + getClass() + " Problematic adapter: " + this.mAdapter.getClass());
        }
        catch (Resources.NotFoundException localNotFoundException)
        {
          while (true)
            String str1 = Integer.toHexString(getId());
        }
      int n = 0;
      ItemInfo localItemInfo2;
      if (n < this.mItems.size())
      {
        localItemInfo2 = (ItemInfo)this.mItems.get(n);
        if (localItemInfo2.position >= this.mCurItem)
          if (localItemInfo2.position != this.mCurItem)
            break label1218;
      }
      while (true)
      {
        if ((localItemInfo2 == null) && (k > 0));
        for (ItemInfo localItemInfo3 = addNewItem(this.mCurItem, n); ; localItemInfo3 = localItemInfo2)
        {
          ItemInfo localItemInfo7;
          label333: int i6;
          float f1;
          label347: float f2;
          int i8;
          int i9;
          int i10;
          int i11;
          ItemInfo localItemInfo8;
          label441: float f4;
          label449: float f5;
          int i13;
          ItemInfo localItemInfo10;
          PagerAdapter localPagerAdapter;
          int i1;
          if (localItemInfo3 != null)
          {
            int i5 = n - 1;
            if (i5 >= 0)
            {
              localItemInfo7 = (ItemInfo)this.mItems.get(i5);
              i6 = getClientWidth();
              if (i6 > 0)
                break label661;
              f1 = 0.0F;
              int i7 = -1 + this.mCurItem;
              f2 = 0.0F;
              i8 = i7;
              i9 = n;
              i10 = i5;
              if (i8 >= 0)
              {
                if ((f2 < f1) || (i8 >= j))
                  break label763;
                if (localItemInfo7 != null)
                  break label683;
              }
              float f3 = localItemInfo3.widthFactor;
              i11 = i9 + 1;
              if (f3 < 2.0F)
              {
                if (i11 >= this.mItems.size())
                  break label868;
                localItemInfo8 = (ItemInfo)this.mItems.get(i11);
                if (i6 > 0)
                  break label874;
                f4 = 0.0F;
                int i12 = 1 + this.mCurItem;
                ItemInfo localItemInfo9 = localItemInfo8;
                f5 = f3;
                i13 = i12;
                localItemInfo10 = localItemInfo9;
                if (i13 < k)
                {
                  if ((f5 < f4) || (i13 <= m))
                    break label971;
                  if (localItemInfo10 != null)
                    break label890;
                }
              }
              calculatePageOffsets(localItemInfo3, i9, localItemInfo1);
            }
          }
          else
          {
            localPagerAdapter = this.mAdapter;
            i1 = this.mCurItem;
            if (localItemInfo3 == null)
              break label1092;
          }
          label661: label683: label1092: for (Object localObject = localItemInfo3.object; ; localObject = null)
          {
            localPagerAdapter.setPrimaryItem(this, i1, localObject);
            this.mAdapter.finishUpdate(this);
            int i2 = getChildCount();
            for (int i3 = 0; i3 < i2; i3++)
            {
              View localView3 = getChildAt(i3);
              LayoutParams localLayoutParams = (LayoutParams)localView3.getLayoutParams();
              localLayoutParams.childIndex = i3;
              if ((localLayoutParams.isDecor) || (localLayoutParams.widthFactor != 0.0F))
                continue;
              ItemInfo localItemInfo6 = infoForChild(localView3);
              if (localItemInfo6 == null)
                continue;
              localLayoutParams.widthFactor = localItemInfo6.widthFactor;
              localLayoutParams.position = localItemInfo6.position;
            }
            n++;
            break;
            localItemInfo7 = null;
            break label333;
            f1 = 2.0F - localItemInfo3.widthFactor + getPaddingLeft() / i6;
            break label347;
            if ((i8 == localItemInfo7.position) && (!localItemInfo7.scrolling))
            {
              this.mItems.remove(i10);
              this.mAdapter.destroyItem(this, i8, localItemInfo7.object);
              i10--;
              i9--;
              if (i10 < 0)
                break label757;
              localItemInfo7 = (ItemInfo)this.mItems.get(i10);
            }
            while (true)
            {
              i8--;
              break;
              label757: localItemInfo7 = null;
              continue;
              label763: if ((localItemInfo7 != null) && (i8 == localItemInfo7.position))
              {
                f2 += localItemInfo7.widthFactor;
                i10--;
                if (i10 >= 0)
                {
                  localItemInfo7 = (ItemInfo)this.mItems.get(i10);
                  continue;
                }
                localItemInfo7 = null;
                continue;
              }
              f2 += addNewItem(i8, i10 + 1).widthFactor;
              i9++;
              if (i10 >= 0)
              {
                localItemInfo7 = (ItemInfo)this.mItems.get(i10);
                continue;
              }
              localItemInfo7 = null;
            }
            label868: localItemInfo8 = null;
            break label441;
            label874: f4 = 2.0F + getPaddingRight() / i6;
            break label449;
            label890: if ((i13 == localItemInfo10.position) && (!localItemInfo10.scrolling))
            {
              this.mItems.remove(i11);
              this.mAdapter.destroyItem(this, i13, localItemInfo10.object);
              if (i11 >= this.mItems.size())
                break label965;
              localItemInfo10 = (ItemInfo)this.mItems.get(i11);
            }
            while (true)
            {
              i13++;
              break;
              label965: localItemInfo10 = null;
              continue;
              label971: if ((localItemInfo10 != null) && (i13 == localItemInfo10.position))
              {
                f5 += localItemInfo10.widthFactor;
                i11++;
                if (i11 < this.mItems.size())
                {
                  localItemInfo10 = (ItemInfo)this.mItems.get(i11);
                  continue;
                }
                localItemInfo10 = null;
                continue;
              }
              ItemInfo localItemInfo11 = addNewItem(i13, i11);
              i11++;
              f5 += localItemInfo11.widthFactor;
              if (i11 < this.mItems.size())
              {
                localItemInfo10 = (ItemInfo)this.mItems.get(i11);
                continue;
              }
              localItemInfo10 = null;
            }
          }
          sortChildDrawingOrder();
          if (!hasFocus())
            break;
          View localView1 = findFocus();
          if (localView1 != null);
          for (ItemInfo localItemInfo4 = infoForAnyChild(localView1); ; localItemInfo4 = null)
          {
            if ((localItemInfo4 != null) && (localItemInfo4.position == this.mCurItem))
              break label1209;
            for (int i4 = 0; ; i4++)
            {
              if (i4 >= getChildCount())
                break label1203;
              View localView2 = getChildAt(i4);
              ItemInfo localItemInfo5 = infoForChild(localView2);
              if ((localItemInfo5 != null) && (localItemInfo5.position == this.mCurItem) && (localView2.requestFocus(2)))
                break;
            }
            label1203: break;
          }
          label1209: break;
        }
        label1218: localItemInfo2 = null;
      }
    }
  }

  public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    if (this.mAdapterChangeListeners != null)
      this.mAdapterChangeListeners.remove(paramOnAdapterChangeListener);
  }

  public void removeOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    if (this.mOnPageChangeListeners != null)
      this.mOnPageChangeListeners.remove(paramOnPageChangeListener);
  }

  public void removeView(View paramView)
  {
    if (this.mInLayout)
    {
      removeViewInLayout(paramView);
      return;
    }
    super.removeView(paramView);
  }

  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    int i = 0;
    if (this.mAdapter != null)
    {
      this.mAdapter.setViewPagerObserver(null);
      this.mAdapter.startUpdate(this);
      for (int k = 0; k < this.mItems.size(); k++)
      {
        ItemInfo localItemInfo = (ItemInfo)this.mItems.get(k);
        this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
      }
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    }
    PagerAdapter localPagerAdapter = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    this.mExpectedAdapterCount = 0;
    boolean bool;
    if (this.mAdapter != null)
    {
      if (this.mObserver == null)
        this.mObserver = new PagerObserver();
      this.mAdapter.setViewPagerObserver(this.mObserver);
      this.mPopulatePending = false;
      bool = this.mFirstLayout;
      this.mFirstLayout = true;
      this.mExpectedAdapterCount = this.mAdapter.getCount();
      if (this.mRestoredCurItem < 0)
        break label297;
      this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
      setCurrentItemInternal(this.mRestoredCurItem, false, true);
      this.mRestoredCurItem = -1;
      this.mRestoredAdapterState = null;
      this.mRestoredClassLoader = null;
    }
    while ((this.mAdapterChangeListeners != null) && (!this.mAdapterChangeListeners.isEmpty()))
    {
      int j = this.mAdapterChangeListeners.size();
      while (i < j)
      {
        ((OnAdapterChangeListener)this.mAdapterChangeListeners.get(i)).onAdapterChanged(this, localPagerAdapter, paramPagerAdapter);
        i++;
      }
      label297: if (!bool)
      {
        populate();
        continue;
      }
      requestLayout();
    }
  }

  void setChildrenDrawingOrderEnabledCompat(boolean paramBoolean)
  {
    if ((Build.VERSION.SDK_INT < 7) || (this.mSetChildrenDrawingOrderEnabled == null));
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = Boolean.TYPE;
      this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", arrayOfClass);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      try
      {
        while (true)
        {
          Method localMethod = this.mSetChildrenDrawingOrderEnabled;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Boolean.valueOf(paramBoolean);
          localMethod.invoke(this, arrayOfObject);
          return;
          localNoSuchMethodException = localNoSuchMethodException;
          Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", localNoSuchMethodException);
        }
      }
      catch (Exception localException)
      {
        Log.e("ViewPager", "Error changing children drawing order", localException);
      }
    }
  }

  public void setCurrentItem(int paramInt)
  {
    this.mPopulatePending = false;
    if (!this.mFirstLayout);
    for (boolean bool = true; ; bool = false)
    {
      setCurrentItemInternal(paramInt, bool, false);
      return;
    }
  }

  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }

  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }

  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    if ((this.mAdapter == null) || (this.mAdapter.getCount() <= 0))
    {
      setScrollingCacheEnabled(false);
      return;
    }
    if ((!paramBoolean2) && (this.mCurItem == paramInt1) && (this.mItems.size() != 0))
    {
      setScrollingCacheEnabled(false);
      return;
    }
    if (paramInt1 < 0)
      paramInt1 = 0;
    while (true)
    {
      int i = this.mOffscreenPageLimit;
      if ((paramInt1 <= i + this.mCurItem) && (paramInt1 >= this.mCurItem - i))
        break;
      for (int j = 0; j < this.mItems.size(); j++)
        ((ItemInfo)this.mItems.get(j)).scrolling = true;
      if (paramInt1 < this.mAdapter.getCount())
        continue;
      paramInt1 = -1 + this.mAdapter.getCount();
    }
    int k = this.mCurItem;
    boolean bool = false;
    if (k != paramInt1)
      bool = true;
    if (this.mFirstLayout)
    {
      this.mCurItem = paramInt1;
      if (bool)
        dispatchOnPageSelected(paramInt1);
      requestLayout();
      return;
    }
    populate(paramInt1);
    scrollToItem(paramInt1, paramBoolean1, paramInt2, bool);
  }

  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    OnPageChangeListener localOnPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return localOnPageChangeListener;
  }

  public void setOffscreenPageLimit(int paramInt)
  {
    if (paramInt < 1)
    {
      Log.w("ViewPager", "Requested offscreen page limit " + paramInt + " too small; defaulting to " + 1);
      paramInt = 1;
    }
    if (paramInt != this.mOffscreenPageLimit)
    {
      this.mOffscreenPageLimit = paramInt;
      populate();
    }
  }

  @Deprecated
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }

  public void setPageMargin(int paramInt)
  {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }

  public void setPageMarginDrawable(@DrawableRes int paramInt)
  {
    setPageMarginDrawable(ContextCompat.getDrawable(getContext(), paramInt));
  }

  public void setPageMarginDrawable(Drawable paramDrawable)
  {
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState();
    if (paramDrawable == null);
    for (boolean bool = true; ; bool = false)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
    }
  }

  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer)
  {
    setPageTransformer(paramBoolean, paramPageTransformer, 2);
  }

  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer, int paramInt)
  {
    int i = 1;
    label30: int i1;
    if (Build.VERSION.SDK_INT >= 11)
    {
      if (paramPageTransformer == null)
        break label85;
      int j = i;
      if (this.mPageTransformer == null)
        break label91;
      int m = i;
      if (j == m)
        break label97;
      i1 = i;
      label41: this.mPageTransformer = paramPageTransformer;
      setChildrenDrawingOrderEnabledCompat(j);
      if (j == 0)
        break label103;
      if (paramBoolean)
        i = 2;
      this.mDrawingOrder = i;
      this.mPageTransformerLayerType = paramInt;
    }
    while (true)
    {
      if (i1 != 0)
        populate();
      return;
      label85: int k = 0;
      break;
      label91: int n = 0;
      break label30;
      label97: i1 = 0;
      break label41;
      label103: this.mDrawingOrder = 0;
    }
  }

  void setScrollState(int paramInt)
  {
    if (this.mScrollState == paramInt)
      return;
    this.mScrollState = paramInt;
    if (this.mPageTransformer != null)
      if (paramInt == 0)
        break label38;
    label38: for (boolean bool = true; ; bool = false)
    {
      enableLayers(bool);
      dispatchOnScrollStateChanged(paramInt);
      return;
    }
  }

  void smoothScrollTo(int paramInt1, int paramInt2)
  {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }

  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (getChildCount() == 0)
    {
      setScrollingCacheEnabled(false);
      return;
    }
    int i;
    int i6;
    if ((this.mScroller != null) && (!this.mScroller.isFinished()))
    {
      i = 1;
      if (i == 0)
        break label131;
      if (!this.mIsScrollStarted)
        break label119;
      i6 = this.mScroller.getCurrX();
      label54: this.mScroller.abortAnimation();
      setScrollingCacheEnabled(false);
    }
    int k;
    int m;
    int n;
    label131: for (int j = i6; ; j = getScrollX())
    {
      k = getScrollY();
      m = paramInt1 - j;
      n = paramInt2 - k;
      if ((m != 0) || (n != 0))
        break label140;
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
      i = 0;
      break;
      label119: i6 = this.mScroller.getStartX();
      break label54;
    }
    label140: setScrollingCacheEnabled(true);
    setScrollState(2);
    int i1 = getClientWidth();
    int i2 = i1 / 2;
    float f1 = Math.min(1.0F, 1.0F * Math.abs(m) / i1);
    float f2 = i2;
    float f3 = i2;
    float f4 = distanceInfluenceForSnapDuration(f1);
    int i3 = Math.abs(paramInt3);
    if (i3 > 0);
    float f5;
    float f6;
    for (int i4 = 4 * Math.round(1000.0F * Math.abs((f2 + f3 * f4) / i3)); ; i4 = (int)(100.0F * (1.0F + Math.abs(m) / (this.mPageMargin + f5 * f6))))
    {
      int i5 = Math.min(i4, 600);
      this.mIsScrollStarted = false;
      this.mScroller.startScroll(j, k, m, n, i5);
      ViewCompat.postInvalidateOnAnimation(this);
      return;
      f5 = i1;
      f6 = this.mAdapter.getPageWidth(this.mCurItem);
    }
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    return (super.verifyDrawable(paramDrawable)) || (paramDrawable == this.mMarginDrawable);
  }

  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target({java.lang.annotation.ElementType.TYPE})
  public static @interface DecorView
  {
  }

  static class ItemInfo
  {
    Object object;
    float offset;
    int position;
    boolean scrolling;
    float widthFactor;
  }

  public static class LayoutParams extends ViewGroup.LayoutParams
  {
    int childIndex;
    public int gravity;
    public boolean isDecor;
    boolean needsMeasure;
    int position;
    float widthFactor = 0.0F;

    public LayoutParams()
    {
      super(-1);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ViewPager.LAYOUT_ATTRS);
      this.gravity = localTypedArray.getInteger(0, 48);
      localTypedArray.recycle();
    }
  }

  class MyAccessibilityDelegate extends AccessibilityDelegateCompat
  {
    MyAccessibilityDelegate()
    {
    }

    private boolean canScroll()
    {
      return (ViewPager.this.mAdapter != null) && (ViewPager.this.mAdapter.getCount() > 1);
    }

    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      paramAccessibilityEvent.setClassName(ViewPager.class.getName());
      AccessibilityRecordCompat localAccessibilityRecordCompat = AccessibilityEventCompat.asRecord(paramAccessibilityEvent);
      localAccessibilityRecordCompat.setScrollable(canScroll());
      if ((paramAccessibilityEvent.getEventType() == 4096) && (ViewPager.this.mAdapter != null))
      {
        localAccessibilityRecordCompat.setItemCount(ViewPager.this.mAdapter.getCount());
        localAccessibilityRecordCompat.setFromIndex(ViewPager.this.mCurItem);
        localAccessibilityRecordCompat.setToIndex(ViewPager.this.mCurItem);
      }
    }

    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
      paramAccessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
      paramAccessibilityNodeInfoCompat.setScrollable(canScroll());
      if (ViewPager.this.canScrollHorizontally(1))
        paramAccessibilityNodeInfoCompat.addAction(4096);
      if (ViewPager.this.canScrollHorizontally(-1))
        paramAccessibilityNodeInfoCompat.addAction(8192);
    }

    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      int i;
      if (super.performAccessibilityAction(paramView, paramInt, paramBundle))
        i = 1;
      boolean bool1;
      do
      {
        boolean bool2;
        do
        {
          return i;
          switch (paramInt)
          {
          default:
            return false;
          case 4096:
            bool2 = ViewPager.this.canScrollHorizontally(1);
            i = 0;
          case 8192:
          }
        }
        while (!bool2);
        ViewPager.this.setCurrentItem(1 + ViewPager.this.mCurItem);
        return true;
        bool1 = ViewPager.this.canScrollHorizontally(-1);
        i = 0;
      }
      while (!bool1);
      ViewPager.this.setCurrentItem(-1 + ViewPager.this.mCurItem);
      return true;
    }
  }

  public static abstract interface OnAdapterChangeListener
  {
    public abstract void onAdapterChanged(@NonNull ViewPager paramViewPager, @Nullable PagerAdapter paramPagerAdapter1, @Nullable PagerAdapter paramPagerAdapter2);
  }

  public static abstract interface OnPageChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);

    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);

    public abstract void onPageSelected(int paramInt);
  }

  public static abstract interface PageTransformer
  {
    public abstract void transformPage(View paramView, float paramFloat);
  }

  private class PagerObserver extends DataSetObserver
  {
    PagerObserver()
    {
    }

    public void onChanged()
    {
      ViewPager.this.dataSetChanged();
    }

    public void onInvalidated()
    {
      ViewPager.this.dataSetChanged();
    }
  }

  public static class SavedState extends AbsSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
    {
      public ViewPager.SavedState createFromParcel(Parcel paramParcel, ClassLoader paramClassLoader)
      {
        return new ViewPager.SavedState(paramParcel, paramClassLoader);
      }

      public ViewPager.SavedState[] newArray(int paramInt)
      {
        return new ViewPager.SavedState[paramInt];
      }
    });
    Parcelable adapterState;
    ClassLoader loader;
    int position;

    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramClassLoader);
      if (paramClassLoader == null)
        paramClassLoader = getClass().getClassLoader();
      this.position = paramParcel.readInt();
      this.adapterState = paramParcel.readParcelable(paramClassLoader);
      this.loader = paramClassLoader;
    }

    public SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public String toString()
    {
      return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.position);
      paramParcel.writeParcelable(this.adapterState, paramInt);
    }
  }

  public static class SimpleOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
    }
  }

  static class ViewPositionComparator
    implements Comparator<View>
  {
    public int compare(View paramView1, View paramView2)
    {
      ViewPager.LayoutParams localLayoutParams1 = (ViewPager.LayoutParams)paramView1.getLayoutParams();
      ViewPager.LayoutParams localLayoutParams2 = (ViewPager.LayoutParams)paramView2.getLayoutParams();
      if (localLayoutParams1.isDecor != localLayoutParams2.isDecor)
      {
        if (localLayoutParams1.isDecor)
          return 1;
        return -1;
      }
      return localLayoutParams1.position - localLayoutParams2.position;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.ViewPager
 * JD-Core Version:    0.6.0
 */