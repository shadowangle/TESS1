package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup
{
  private static final int DEFAULT_FADE_COLOR = -858993460;
  private static final int DEFAULT_OVERHANG_SIZE = 32;
  static final SlidingPanelLayoutImpl IMPL;
  private static final int MIN_FLING_VELOCITY = 400;
  private static final String TAG = "SlidingPaneLayout";
  private boolean mCanSlide;
  private int mCoveredFadeColor;
  final ViewDragHelper mDragHelper;
  private boolean mFirstLayout = true;
  private float mInitialMotionX;
  private float mInitialMotionY;
  boolean mIsUnableToDrag;
  private final int mOverhangSize;
  private PanelSlideListener mPanelSlideListener;
  private int mParallaxBy;
  private float mParallaxOffset;
  final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList();
  boolean mPreservedOpenState;
  private Drawable mShadowDrawableLeft;
  private Drawable mShadowDrawableRight;
  float mSlideOffset;
  int mSlideRange;
  View mSlideableView;
  private int mSliderFadeColor = -858993460;
  private final Rect mTmpRect = new Rect();

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 17)
    {
      IMPL = new SlidingPanelLayoutImplJBMR1();
      return;
    }
    if (i >= 16)
    {
      IMPL = new SlidingPanelLayoutImplJB();
      return;
    }
    IMPL = new SlidingPanelLayoutImplBase();
  }

  public SlidingPaneLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    float f = paramContext.getResources().getDisplayMetrics().density;
    this.mOverhangSize = (int)(0.5F + 32.0F * f);
    ViewConfiguration.get(paramContext);
    setWillNotDraw(false);
    ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
    ViewCompat.setImportantForAccessibility(this, 1);
    this.mDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback());
    this.mDragHelper.setMinVelocity(f * 400.0F);
  }

  private boolean closePane(View paramView, int paramInt)
  {
    int i;
    if (!this.mFirstLayout)
    {
      boolean bool = smoothSlideTo(0.0F, paramInt);
      i = 0;
      if (!bool);
    }
    else
    {
      this.mPreservedOpenState = false;
      i = 1;
    }
    return i;
  }

  private void dimChildView(View paramView, float paramFloat, int paramInt)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((paramFloat > 0.0F) && (paramInt != 0))
    {
      int i = (int)(paramFloat * ((0xFF000000 & paramInt) >>> 24));
      if (localLayoutParams.dimPaint == null)
        localLayoutParams.dimPaint = new Paint();
      localLayoutParams.dimPaint.setColorFilter(new PorterDuffColorFilter(i << 24 | 0xFFFFFF & paramInt, PorterDuff.Mode.SRC_OVER));
      if (ViewCompat.getLayerType(paramView) != 2)
        ViewCompat.setLayerType(paramView, 2, localLayoutParams.dimPaint);
      invalidateChildRegion(paramView);
    }
    do
      return;
    while (ViewCompat.getLayerType(paramView) == 0);
    if (localLayoutParams.dimPaint != null)
      localLayoutParams.dimPaint.setColorFilter(null);
    DisableLayerRunnable localDisableLayerRunnable = new DisableLayerRunnable(paramView);
    this.mPostedRunnables.add(localDisableLayerRunnable);
    ViewCompat.postOnAnimation(this, localDisableLayerRunnable);
  }

  private boolean openPane(View paramView, int paramInt)
  {
    if ((this.mFirstLayout) || (smoothSlideTo(1.0F, paramInt)))
    {
      this.mPreservedOpenState = true;
      return true;
    }
    return false;
  }

  private void parallaxOtherViews(float paramFloat)
  {
    boolean bool = isLayoutRtlSupport();
    LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i1;
    int i;
    label41: int k;
    label50: View localView;
    if (localLayoutParams.dimWhenOffset)
      if (bool)
      {
        i1 = localLayoutParams.rightMargin;
        if (i1 > 0)
          break label89;
        i = 1;
        int j = getChildCount();
        k = 0;
        if (k >= j)
          return;
        localView = getChildAt(k);
        if (localView != this.mSlideableView)
          break label95;
      }
    label89: label95: 
    do
    {
      k++;
      break label50;
      i1 = localLayoutParams.leftMargin;
      break;
      i = 0;
      break label41;
      int m = (int)((1.0F - this.mParallaxOffset) * this.mParallaxBy);
      this.mParallaxOffset = paramFloat;
      int n = m - (int)((1.0F - paramFloat) * this.mParallaxBy);
      if (bool)
        n = -n;
      localView.offsetLeftAndRight(n);
    }
    while (i == 0);
    float f;
    if (bool)
      f = this.mParallaxOffset - 1.0F;
    while (true)
    {
      dimChildView(localView, f, this.mCoveredFadeColor);
      break;
      f = 1.0F - this.mParallaxOffset;
    }
  }

  private static boolean viewIsOpaque(View paramView)
  {
    if (paramView.isOpaque());
    Drawable localDrawable;
    do
    {
      int j = 1;
      do
      {
        int i;
        do
        {
          return j;
          i = Build.VERSION.SDK_INT;
          j = 0;
        }
        while (i >= 18);
        localDrawable = paramView.getBackground();
        j = 0;
      }
      while (localDrawable == null);
    }
    while (localDrawable.getOpacity() == -1);
    return false;
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
        if ((paramInt2 + i >= localView.getLeft()) && (paramInt2 + i < localView.getRight()) && (paramInt3 + j >= localView.getTop()) && (paramInt3 + j < localView.getBottom()))
        {
          int m = paramInt2 + i - localView.getLeft();
          int n = paramInt3 + j - localView.getTop();
          if (!canScroll(localView, true, paramInt1, m, n));
        }
      }
    }
    while (true)
    {
      return true;
      k--;
      break;
      if (paramBoolean)
        if (!isLayoutRtlSupport())
          break label170;
      while (!ViewCompat.canScrollHorizontally(paramView, paramInt1))
      {
        return false;
        label170: paramInt1 = -paramInt1;
      }
    }
  }

  @Deprecated
  public boolean canSlide()
  {
    return this.mCanSlide;
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return ((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams));
  }

  public boolean closePane()
  {
    return closePane(this.mSlideableView, 0);
  }

  public void computeScroll()
  {
    if (this.mDragHelper.continueSettling(true))
    {
      if (!this.mCanSlide)
        this.mDragHelper.abort();
    }
    else
      return;
    ViewCompat.postInvalidateOnAnimation(this);
  }

  void dispatchOnPanelClosed(View paramView)
  {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelClosed(paramView);
    sendAccessibilityEvent(32);
  }

  void dispatchOnPanelOpened(View paramView)
  {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelOpened(paramView);
    sendAccessibilityEvent(32);
  }

  void dispatchOnPanelSlide(View paramView)
  {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelSlide(paramView, this.mSlideOffset);
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    Drawable localDrawable;
    if (isLayoutRtlSupport())
    {
      localDrawable = this.mShadowDrawableRight;
      if (getChildCount() <= 1)
        break label48;
    }
    label48: for (View localView = getChildAt(1); ; localView = null)
    {
      if ((localView != null) && (localDrawable != null))
        break label53;
      return;
      localDrawable = this.mShadowDrawableLeft;
      break;
    }
    label53: int i = localView.getTop();
    int j = localView.getBottom();
    int k = localDrawable.getIntrinsicWidth();
    int n;
    int m;
    if (isLayoutRtlSupport())
    {
      n = localView.getRight();
      m = n + k;
    }
    while (true)
    {
      localDrawable.setBounds(n, i, m, j);
      localDrawable.draw(paramCanvas);
      return;
      m = localView.getLeft();
      n = m - k;
    }
  }

  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save(2);
    boolean bool;
    if ((this.mCanSlide) && (!localLayoutParams.slideable) && (this.mSlideableView != null))
    {
      paramCanvas.getClipBounds(this.mTmpRect);
      if (isLayoutRtlSupport())
      {
        this.mTmpRect.left = Math.max(this.mTmpRect.left, this.mSlideableView.getRight());
        paramCanvas.clipRect(this.mTmpRect);
      }
    }
    else
    {
      if (Build.VERSION.SDK_INT < 11)
        break label140;
      bool = super.drawChild(paramCanvas, paramView, paramLong);
    }
    while (true)
    {
      paramCanvas.restoreToCount(i);
      return bool;
      this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
      break;
      label140: if ((localLayoutParams.dimWhenOffset) && (this.mSlideOffset > 0.0F))
      {
        if (!paramView.isDrawingCacheEnabled())
          paramView.setDrawingCacheEnabled(true);
        Bitmap localBitmap = paramView.getDrawingCache();
        if (localBitmap != null)
        {
          paramCanvas.drawBitmap(localBitmap, paramView.getLeft(), paramView.getTop(), localLayoutParams.dimPaint);
          bool = false;
          continue;
        }
        Log.e("SlidingPaneLayout", "drawChild: child view " + paramView + " returned null drawing cache");
        bool = super.drawChild(paramCanvas, paramView, paramLong);
        continue;
      }
      if (paramView.isDrawingCacheEnabled())
        paramView.setDrawingCacheEnabled(false);
      bool = super.drawChild(paramCanvas, paramView, paramLong);
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
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams))
      return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    return new LayoutParams(paramLayoutParams);
  }

  @ColorInt
  public int getCoveredFadeColor()
  {
    return this.mCoveredFadeColor;
  }

  public int getParallaxDistance()
  {
    return this.mParallaxBy;
  }

  @ColorInt
  public int getSliderFadeColor()
  {
    return this.mSliderFadeColor;
  }

  void invalidateChildRegion(View paramView)
  {
    IMPL.invalidateChildRegion(this, paramView);
  }

  boolean isDimmed(View paramView)
  {
    if (paramView == null)
      return false;
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    return (this.mCanSlide) && (localLayoutParams.dimWhenOffset) && (this.mSlideOffset > 0.0F);
  }

  boolean isLayoutRtlSupport()
  {
    return ViewCompat.getLayoutDirection(this) == 1;
  }

  public boolean isOpen()
  {
    return (!this.mCanSlide) || (this.mSlideOffset == 1.0F);
  }

  public boolean isSlideable()
  {
    return this.mCanSlide;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
    int i = this.mPostedRunnables.size();
    for (int j = 0; j < i; j++)
      ((DisableLayerRunnable)this.mPostedRunnables.get(j)).run();
    this.mPostedRunnables.clear();
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if ((!this.mCanSlide) && (i == 0) && (getChildCount() > 1))
    {
      View localView = getChildAt(1);
      if (localView != null)
        if (this.mDragHelper.isViewUnder(localView, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()))
          break label100;
    }
    boolean bool1;
    label100: for (boolean bool2 = true; ; bool2 = false)
    {
      this.mPreservedOpenState = bool2;
      if ((this.mCanSlide) && ((!this.mIsUnableToDrag) || (i == 0)))
        break;
      this.mDragHelper.cancel();
      bool1 = super.onInterceptTouchEvent(paramMotionEvent);
      return bool1;
    }
    if ((i == 3) || (i == 1))
    {
      this.mDragHelper.cancel();
      return false;
    }
    int j;
    switch (i)
    {
    default:
      j = 0;
    case 0:
    case 2:
    case 1:
    }
    while (true)
    {
      if (!this.mDragHelper.shouldInterceptTouchEvent(paramMotionEvent))
      {
        bool1 = false;
        if (j == 0)
          break;
      }
      return true;
      this.mIsUnableToDrag = false;
      float f5 = paramMotionEvent.getX();
      float f6 = paramMotionEvent.getY();
      this.mInitialMotionX = f5;
      this.mInitialMotionY = f6;
      if ((this.mDragHelper.isViewUnder(this.mSlideableView, (int)f5, (int)f6)) && (isDimmed(this.mSlideableView)))
      {
        j = 1;
        continue;
        float f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        float f3 = Math.abs(f1 - this.mInitialMotionX);
        float f4 = Math.abs(f2 - this.mInitialMotionY);
        if ((f3 > this.mDragHelper.getTouchSlop()) && (f4 > f3))
        {
          this.mDragHelper.cancel();
          this.mIsUnableToDrag = true;
          return false;
        }
      }
      j = 0;
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool1 = isLayoutRtlSupport();
    int i;
    int j;
    label36: int k;
    label47: int m;
    int n;
    float f;
    if (bool1)
    {
      this.mDragHelper.setEdgeTrackingEnabled(2);
      i = paramInt3 - paramInt1;
      if (!bool1)
        break label154;
      j = getPaddingRight();
      if (!bool1)
        break label163;
      k = getPaddingLeft();
      m = getPaddingTop();
      n = getChildCount();
      if (this.mFirstLayout)
      {
        if ((!this.mCanSlide) || (!this.mPreservedOpenState))
          break label172;
        f = 1.0F;
      }
    }
    int i2;
    View localView;
    int i5;
    int i9;
    while (true)
    {
      this.mSlideOffset = f;
      int i1 = 0;
      i2 = j;
      while (true)
      {
        if (i1 >= n)
          break label462;
        localView = getChildAt(i1);
        if (localView.getVisibility() != 8)
          break;
        i5 = j;
        i9 = i2;
        i1++;
        i2 = i9;
        j = i5;
      }
      this.mDragHelper.setEdgeTrackingEnabled(1);
      break;
      label154: j = getPaddingLeft();
      break label36;
      label163: k = getPaddingRight();
      break label47;
      label172: f = 0.0F;
    }
    LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
    int i4 = localView.getMeasuredWidth();
    int i13;
    label261: boolean bool2;
    label285: int i6;
    label329: int i8;
    int i7;
    if (localLayoutParams.slideable)
    {
      int i10 = localLayoutParams.leftMargin;
      int i11 = localLayoutParams.rightMargin;
      int i12 = Math.min(i2, i - k - this.mOverhangSize) - j - (i10 + i11);
      this.mSlideRange = i12;
      if (bool1)
      {
        i13 = localLayoutParams.rightMargin;
        if (i12 + (j + i13) + i4 / 2 <= i - k)
          break label393;
        bool2 = true;
        localLayoutParams.dimWhenOffset = bool2;
        int i14 = (int)(i12 * this.mSlideOffset);
        i5 = j + (i14 + i13);
        this.mSlideOffset = (i14 / this.mSlideRange);
        i6 = 0;
        if (!bool1)
          break label445;
        i8 = i6 + (i - i5);
        i7 = i8 - i4;
      }
    }
    while (true)
    {
      localView.layout(i7, m, i8, m + localView.getMeasuredHeight());
      i9 = i2 + localView.getWidth();
      break;
      i13 = localLayoutParams.leftMargin;
      break label261;
      label393: bool2 = false;
      break label285;
      if ((this.mCanSlide) && (this.mParallaxBy != 0))
      {
        i6 = (int)((1.0F - this.mSlideOffset) * this.mParallaxBy);
        i5 = i2;
        break label329;
      }
      i5 = i2;
      i6 = 0;
      break label329;
      label445: i7 = i5 - i6;
      i8 = i7 + i4;
    }
    label462: if (this.mFirstLayout)
    {
      if (!this.mCanSlide)
        break label537;
      if (this.mParallaxBy != 0)
        parallaxOtherViews(this.mSlideOffset);
      if (((LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset)
        dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
    }
    while (true)
    {
      updateObscuredViewsVisibility(this.mSlideableView);
      this.mFirstLayout = false;
      return;
      label537: for (int i3 = 0; i3 < n; i3++)
        dimChildView(getChildAt(i3), 0.0F, this.mSliderFadeColor);
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n;
    int i1;
    if (i != 1073741824)
      if (isInEditMode())
        if (i == -2147483648)
        {
          n = k;
          i1 = j;
        }
    while (true)
    {
      int i2 = -1;
      int i3 = 0;
      label88: float f1;
      boolean bool1;
      int i4;
      int i5;
      int i6;
      int i7;
      int i8;
      label145: View localView2;
      LayoutParams localLayoutParams2;
      boolean bool3;
      int i23;
      int i24;
      float f2;
      switch (n)
      {
      default:
        f1 = 0.0F;
        bool1 = false;
        i4 = i1 - getPaddingLeft() - getPaddingRight();
        i5 = getChildCount();
        if (i5 > 2)
          Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
        this.mSlideableView = null;
        i6 = 0;
        i7 = i4;
        i8 = i3;
        if (i6 < i5)
        {
          localView2 = getChildAt(i6);
          localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
          if (localView2.getVisibility() != 8)
            break;
          localLayoutParams2.dimWhenOffset = false;
          bool3 = bool1;
          i23 = i8;
          i24 = i7;
          f2 = f1;
        }
      case 1073741824:
      case -2147483648:
      }
      while (true)
      {
        i6++;
        f1 = f2;
        i7 = i24;
        i8 = i23;
        bool1 = bool3;
        break label145;
        if (i != 0)
          break label1119;
        n = k;
        i1 = 300;
        break;
        throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
        if (k != 0)
          break label1119;
        if (isInEditMode())
        {
          if (k != 0)
            break label1119;
          m = 300;
          n = -2147483648;
          i1 = j;
          break;
        }
        throw new IllegalStateException("Height must not be UNSPECIFIED");
        i3 = m - getPaddingTop() - getPaddingBottom();
        i2 = i3;
        break label88;
        i2 = m - getPaddingTop() - getPaddingBottom();
        i3 = 0;
        break label88;
        if (localLayoutParams2.weight > 0.0F)
        {
          f1 += localLayoutParams2.weight;
          if (localLayoutParams2.width == 0);
        }
        else
        {
          int i18 = localLayoutParams2.leftMargin + localLayoutParams2.rightMargin;
          int i19;
          label401: int i20;
          if (localLayoutParams2.width == -2)
          {
            i19 = View.MeasureSpec.makeMeasureSpec(i4 - i18, -2147483648);
            if (localLayoutParams2.height != -2)
              break label571;
            i20 = View.MeasureSpec.makeMeasureSpec(i2, -2147483648);
            label421: localView2.measure(i19, i20);
            int i21 = localView2.getMeasuredWidth();
            int i22 = localView2.getMeasuredHeight();
            if ((n == -2147483648) && (i22 > i8))
              i8 = Math.min(i22, i2);
            i7 -= i21;
            if (i7 >= 0)
              break label609;
          }
          label571: label609: for (boolean bool2 = true; ; bool2 = false)
          {
            localLayoutParams2.slideable = bool2;
            bool1 |= bool2;
            if (!localLayoutParams2.slideable)
              break label1100;
            this.mSlideableView = localView2;
            bool3 = bool1;
            i23 = i8;
            i24 = i7;
            f2 = f1;
            break;
            if (localLayoutParams2.width == -1)
            {
              i19 = View.MeasureSpec.makeMeasureSpec(i4 - i18, 1073741824);
              break label401;
            }
            i19 = View.MeasureSpec.makeMeasureSpec(localLayoutParams2.width, 1073741824);
            break label401;
            if (localLayoutParams2.height == -1)
            {
              i20 = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
              break label421;
            }
            i20 = View.MeasureSpec.makeMeasureSpec(localLayoutParams2.height, 1073741824);
            break label421;
          }
          if ((bool1) || (f1 > 0.0F))
          {
            int i9 = i4 - this.mOverhangSize;
            int i10 = 0;
            if (i10 < i5)
            {
              View localView1 = getChildAt(i10);
              if (localView1.getVisibility() == 8);
              while (true)
              {
                i10++;
                break;
                LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
                if (localView1.getVisibility() == 8)
                  continue;
                int i11;
                label711: int i12;
                label719: int i17;
                if ((localLayoutParams1.width == 0) && (localLayoutParams1.weight > 0.0F))
                {
                  i11 = 1;
                  if (i11 == 0)
                    break label807;
                  i12 = 0;
                  if ((!bool1) || (localView1 == this.mSlideableView))
                    break label871;
                  if ((localLayoutParams1.width >= 0) || ((i12 <= i9) && (localLayoutParams1.weight <= 0.0F)))
                    continue;
                  if (i11 == 0)
                    break label855;
                  if (localLayoutParams1.height != -2)
                    break label817;
                  i17 = View.MeasureSpec.makeMeasureSpec(i2, -2147483648);
                }
                while (true)
                {
                  localView1.measure(View.MeasureSpec.makeMeasureSpec(i9, 1073741824), i17);
                  break;
                  i11 = 0;
                  break label711;
                  label807: i12 = localView1.getMeasuredWidth();
                  break label719;
                  label817: if (localLayoutParams1.height == -1)
                  {
                    i17 = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
                    continue;
                  }
                  i17 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.height, 1073741824);
                  continue;
                  label855: i17 = View.MeasureSpec.makeMeasureSpec(localView1.getMeasuredHeight(), 1073741824);
                }
                label871: if (localLayoutParams1.weight <= 0.0F)
                  continue;
                int i13;
                if (localLayoutParams1.width == 0)
                  if (localLayoutParams1.height == -2)
                    i13 = View.MeasureSpec.makeMeasureSpec(i2, -2147483648);
                while (true)
                {
                  if (!bool1)
                    break label1013;
                  int i15 = i4 - (localLayoutParams1.leftMargin + localLayoutParams1.rightMargin);
                  int i16 = View.MeasureSpec.makeMeasureSpec(i15, 1073741824);
                  if (i12 == i15)
                    break;
                  localView1.measure(i16, i13);
                  break;
                  if (localLayoutParams1.height == -1)
                  {
                    i13 = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
                    continue;
                  }
                  i13 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.height, 1073741824);
                  continue;
                  i13 = View.MeasureSpec.makeMeasureSpec(localView1.getMeasuredHeight(), 1073741824);
                }
                label1013: int i14 = Math.max(0, i7);
                localView1.measure(View.MeasureSpec.makeMeasureSpec(i12 + (int)(localLayoutParams1.weight * i14 / f1), 1073741824), i13);
              }
            }
          }
          setMeasuredDimension(i1, i8 + getPaddingTop() + getPaddingBottom());
          this.mCanSlide = bool1;
          if ((this.mDragHelper.getViewDragState() != 0) && (!bool1))
            this.mDragHelper.abort();
          return;
        }
        label1100: bool3 = bool1;
        i23 = i8;
        i24 = i7;
        f2 = f1;
      }
      label1119: n = k;
      i1 = j;
    }
  }

  void onPanelDragged(int paramInt)
  {
    if (this.mSlideableView == null)
    {
      this.mSlideOffset = 0.0F;
      return;
    }
    boolean bool = isLayoutRtlSupport();
    LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i = this.mSlideableView.getWidth();
    if (bool)
      paramInt = getWidth() - paramInt - i;
    int j;
    if (bool)
    {
      j = getPaddingRight();
      if (!bool)
        break label146;
    }
    label146: for (int k = localLayoutParams.rightMargin; ; k = localLayoutParams.leftMargin)
    {
      this.mSlideOffset = ((paramInt - (k + j)) / this.mSlideRange);
      if (this.mParallaxBy != 0)
        parallaxOtherViews(this.mSlideOffset);
      if (localLayoutParams.dimWhenOffset)
        dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
      dispatchOnPanelSlide(this.mSlideableView);
      return;
      j = getPaddingLeft();
      break;
    }
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if (localSavedState.isOpen)
      openPane();
    while (true)
    {
      this.mPreservedOpenState = localSavedState.isOpen;
      return;
      closePane();
    }
  }

  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if (isSlideable());
    for (boolean bool = isOpen(); ; bool = this.mPreservedOpenState)
    {
      localSavedState.isOpen = bool;
      return localSavedState;
    }
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      this.mFirstLayout = true;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if (!this.mCanSlide)
      bool = super.onTouchEvent(paramMotionEvent);
    float f1;
    float f2;
    float f3;
    float f4;
    int j;
    do
    {
      do
      {
        return bool;
        this.mDragHelper.processTouchEvent(paramMotionEvent);
        int i = paramMotionEvent.getAction();
        bool = true;
        switch (i & 0xFF)
        {
        default:
          return bool;
        case 0:
          float f5 = paramMotionEvent.getX();
          float f6 = paramMotionEvent.getY();
          this.mInitialMotionX = f5;
          this.mInitialMotionY = f6;
          return bool;
        case 1:
        }
      }
      while (!isDimmed(this.mSlideableView));
      f1 = paramMotionEvent.getX();
      f2 = paramMotionEvent.getY();
      f3 = f1 - this.mInitialMotionX;
      f4 = f2 - this.mInitialMotionY;
      j = this.mDragHelper.getTouchSlop();
    }
    while ((f3 * f3 + f4 * f4 >= j * j) || (!this.mDragHelper.isViewUnder(this.mSlideableView, (int)f1, (int)f2)));
    closePane(this.mSlideableView, 0);
    return bool;
  }

  public boolean openPane()
  {
    return openPane(this.mSlideableView, 0);
  }

  public void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    if ((!isInTouchMode()) && (!this.mCanSlide))
      if (paramView1 != this.mSlideableView)
        break label36;
    label36: for (boolean bool = true; ; bool = false)
    {
      this.mPreservedOpenState = bool;
      return;
    }
  }

  void setAllChildrenVisible()
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 4)
        continue;
      localView.setVisibility(0);
    }
  }

  public void setCoveredFadeColor(@ColorInt int paramInt)
  {
    this.mCoveredFadeColor = paramInt;
  }

  public void setPanelSlideListener(PanelSlideListener paramPanelSlideListener)
  {
    this.mPanelSlideListener = paramPanelSlideListener;
  }

  public void setParallaxDistance(int paramInt)
  {
    this.mParallaxBy = paramInt;
    requestLayout();
  }

  @Deprecated
  public void setShadowDrawable(Drawable paramDrawable)
  {
    setShadowDrawableLeft(paramDrawable);
  }

  public void setShadowDrawableLeft(Drawable paramDrawable)
  {
    this.mShadowDrawableLeft = paramDrawable;
  }

  public void setShadowDrawableRight(Drawable paramDrawable)
  {
    this.mShadowDrawableRight = paramDrawable;
  }

  @Deprecated
  public void setShadowResource(@DrawableRes int paramInt)
  {
    setShadowDrawable(getResources().getDrawable(paramInt));
  }

  public void setShadowResourceLeft(int paramInt)
  {
    setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), paramInt));
  }

  public void setShadowResourceRight(int paramInt)
  {
    setShadowDrawableRight(ContextCompat.getDrawable(getContext(), paramInt));
  }

  public void setSliderFadeColor(@ColorInt int paramInt)
  {
    this.mSliderFadeColor = paramInt;
  }

  @Deprecated
  public void smoothSlideClosed()
  {
    closePane();
  }

  @Deprecated
  public void smoothSlideOpen()
  {
    openPane();
  }

  boolean smoothSlideTo(float paramFloat, int paramInt)
  {
    if (!this.mCanSlide);
    while (true)
    {
      return false;
      boolean bool = isLayoutRtlSupport();
      LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
      int j;
      int k;
      int m;
      if (bool)
      {
        j = getPaddingRight();
        k = localLayoutParams.rightMargin;
        m = this.mSlideableView.getWidth();
      }
      for (int i = (int)(getWidth() - (k + j + paramFloat * this.mSlideRange + m)); this.mDragHelper.smoothSlideViewTo(this.mSlideableView, i, this.mSlideableView.getTop()); i = (int)(getPaddingLeft() + localLayoutParams.leftMargin + paramFloat * this.mSlideRange))
      {
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
      }
    }
  }

  void updateObscuredViewsVisibility(View paramView)
  {
    boolean bool = isLayoutRtlSupport();
    int i;
    int j;
    label29: int k;
    int m;
    int n;
    int i2;
    int i1;
    int i4;
    int i3;
    label82: int i5;
    if (bool)
    {
      i = getWidth() - getPaddingRight();
      if (!bool)
        break label121;
      j = getPaddingLeft();
      k = getPaddingTop();
      m = getHeight();
      n = getPaddingBottom();
      if ((paramView == null) || (!viewIsOpaque(paramView)))
        break label135;
      i2 = paramView.getLeft();
      i1 = paramView.getRight();
      i4 = paramView.getTop();
      i3 = paramView.getBottom();
      i5 = getChildCount();
    }
    View localView;
    for (int i6 = 0; ; i6++)
    {
      if (i6 < i5)
      {
        localView = getChildAt(i6);
        if (localView != paramView);
      }
      else
      {
        return;
        i = getPaddingLeft();
        break;
        label121: j = getWidth() - getPaddingRight();
        break label29;
        label135: i1 = 0;
        i2 = 0;
        i3 = 0;
        i4 = 0;
        break label82;
      }
      if (localView.getVisibility() != 8)
        break label166;
    }
    label166: int i7;
    label174: int i10;
    if (bool)
    {
      i7 = j;
      int i8 = Math.max(i7, localView.getLeft());
      int i9 = Math.max(k, localView.getTop());
      if (!bool)
        break label279;
      i10 = i;
      label205: int i11 = Math.min(i10, localView.getRight());
      int i12 = Math.min(m - n, localView.getBottom());
      if ((i8 < i2) || (i9 < i4) || (i11 > i1) || (i12 > i3))
        break label286;
    }
    label279: label286: for (int i13 = 4; ; i13 = 0)
    {
      localView.setVisibility(i13);
      break;
      i7 = i;
      break label174;
      i10 = j;
      break label205;
    }
  }

  class AccessibilityDelegate extends AccessibilityDelegateCompat
  {
    private final Rect mTmpRect = new Rect();

    AccessibilityDelegate()
    {
    }

    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat2)
    {
      Rect localRect = this.mTmpRect;
      paramAccessibilityNodeInfoCompat2.getBoundsInParent(localRect);
      paramAccessibilityNodeInfoCompat1.setBoundsInParent(localRect);
      paramAccessibilityNodeInfoCompat2.getBoundsInScreen(localRect);
      paramAccessibilityNodeInfoCompat1.setBoundsInScreen(localRect);
      paramAccessibilityNodeInfoCompat1.setVisibleToUser(paramAccessibilityNodeInfoCompat2.isVisibleToUser());
      paramAccessibilityNodeInfoCompat1.setPackageName(paramAccessibilityNodeInfoCompat2.getPackageName());
      paramAccessibilityNodeInfoCompat1.setClassName(paramAccessibilityNodeInfoCompat2.getClassName());
      paramAccessibilityNodeInfoCompat1.setContentDescription(paramAccessibilityNodeInfoCompat2.getContentDescription());
      paramAccessibilityNodeInfoCompat1.setEnabled(paramAccessibilityNodeInfoCompat2.isEnabled());
      paramAccessibilityNodeInfoCompat1.setClickable(paramAccessibilityNodeInfoCompat2.isClickable());
      paramAccessibilityNodeInfoCompat1.setFocusable(paramAccessibilityNodeInfoCompat2.isFocusable());
      paramAccessibilityNodeInfoCompat1.setFocused(paramAccessibilityNodeInfoCompat2.isFocused());
      paramAccessibilityNodeInfoCompat1.setAccessibilityFocused(paramAccessibilityNodeInfoCompat2.isAccessibilityFocused());
      paramAccessibilityNodeInfoCompat1.setSelected(paramAccessibilityNodeInfoCompat2.isSelected());
      paramAccessibilityNodeInfoCompat1.setLongClickable(paramAccessibilityNodeInfoCompat2.isLongClickable());
      paramAccessibilityNodeInfoCompat1.addAction(paramAccessibilityNodeInfoCompat2.getActions());
      paramAccessibilityNodeInfoCompat1.setMovementGranularities(paramAccessibilityNodeInfoCompat2.getMovementGranularities());
    }

    public boolean filter(View paramView)
    {
      return SlidingPaneLayout.this.isDimmed(paramView);
    }

    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      paramAccessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(paramAccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(paramView, localAccessibilityNodeInfoCompat);
      copyNodeInfoNoChildren(paramAccessibilityNodeInfoCompat, localAccessibilityNodeInfoCompat);
      localAccessibilityNodeInfoCompat.recycle();
      paramAccessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
      paramAccessibilityNodeInfoCompat.setSource(paramView);
      ViewParent localViewParent = ViewCompat.getParentForAccessibility(paramView);
      if ((localViewParent instanceof View))
        paramAccessibilityNodeInfoCompat.setParent((View)localViewParent);
      int i = SlidingPaneLayout.this.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = SlidingPaneLayout.this.getChildAt(j);
        if ((filter(localView)) || (localView.getVisibility() != 0))
          continue;
        ViewCompat.setImportantForAccessibility(localView, 1);
        paramAccessibilityNodeInfoCompat.addChild(localView);
      }
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      if (!filter(paramView))
        return super.onRequestSendAccessibilityEvent(paramViewGroup, paramView, paramAccessibilityEvent);
      return false;
    }
  }

  private class DisableLayerRunnable
    implements Runnable
  {
    final View mChildView;

    DisableLayerRunnable(View arg2)
    {
      Object localObject;
      this.mChildView = localObject;
    }

    public void run()
    {
      if (this.mChildView.getParent() == SlidingPaneLayout.this)
      {
        ViewCompat.setLayerType(this.mChildView, 0, null);
        SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
      }
      SlidingPaneLayout.this.mPostedRunnables.remove(this);
    }
  }

  private class DragHelperCallback extends ViewDragHelper.Callback
  {
    DragHelperCallback()
    {
    }

    public int clampViewPositionHorizontal(View paramView, int paramInt1, int paramInt2)
    {
      SlidingPaneLayout.LayoutParams localLayoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      if (SlidingPaneLayout.this.isLayoutRtlSupport())
      {
        int k = SlidingPaneLayout.this.getWidth() - (SlidingPaneLayout.this.getPaddingRight() + localLayoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth());
        int m = SlidingPaneLayout.this.mSlideRange;
        return Math.max(Math.min(paramInt1, k), k - m);
      }
      int i = SlidingPaneLayout.this.getPaddingLeft() + localLayoutParams.leftMargin;
      int j = SlidingPaneLayout.this.mSlideRange;
      return Math.min(Math.max(paramInt1, i), i + j);
    }

    public int clampViewPositionVertical(View paramView, int paramInt1, int paramInt2)
    {
      return paramView.getTop();
    }

    public int getViewHorizontalDragRange(View paramView)
    {
      return SlidingPaneLayout.this.mSlideRange;
    }

    public void onEdgeDragStarted(int paramInt1, int paramInt2)
    {
      SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, paramInt2);
    }

    public void onViewCaptured(View paramView, int paramInt)
    {
      SlidingPaneLayout.this.setAllChildrenVisible();
    }

    public void onViewDragStateChanged(int paramInt)
    {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0)
      {
        if (SlidingPaneLayout.this.mSlideOffset == 0.0F)
        {
          SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = false;
        }
      }
      else
        return;
      SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
      SlidingPaneLayout.this.mPreservedOpenState = true;
    }

    public void onViewPositionChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      SlidingPaneLayout.this.onPanelDragged(paramInt1);
      SlidingPaneLayout.this.invalidate();
    }

    public void onViewReleased(View paramView, float paramFloat1, float paramFloat2)
    {
      SlidingPaneLayout.LayoutParams localLayoutParams = (SlidingPaneLayout.LayoutParams)paramView.getLayoutParams();
      int i;
      if (SlidingPaneLayout.this.isLayoutRtlSupport())
      {
        int j = SlidingPaneLayout.this.getPaddingRight() + localLayoutParams.rightMargin;
        if ((paramFloat1 < 0.0F) || ((paramFloat1 == 0.0F) && (SlidingPaneLayout.this.mSlideOffset > 0.5F)))
          j += SlidingPaneLayout.this.mSlideRange;
        int k = SlidingPaneLayout.this.mSlideableView.getWidth();
        i = SlidingPaneLayout.this.getWidth() - j - k;
      }
      while (true)
      {
        SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(i, paramView.getTop());
        SlidingPaneLayout.this.invalidate();
        return;
        i = SlidingPaneLayout.this.getPaddingLeft() + localLayoutParams.leftMargin;
        if ((paramFloat1 <= 0.0F) && ((paramFloat1 != 0.0F) || (SlidingPaneLayout.this.mSlideOffset <= 0.5F)))
          continue;
        i += SlidingPaneLayout.this.mSlideRange;
      }
    }

    public boolean tryCaptureView(View paramView, int paramInt)
    {
      if (SlidingPaneLayout.this.mIsUnableToDrag)
        return false;
      return ((SlidingPaneLayout.LayoutParams)paramView.getLayoutParams()).slideable;
    }
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    private static final int[] ATTRS = { 16843137 };
    Paint dimPaint;
    boolean dimWhenOffset;
    boolean slideable;
    public float weight = 0.0F;

    public LayoutParams()
    {
      super(-1);
    }

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS);
      this.weight = localTypedArray.getFloat(0, 0.0F);
      localTypedArray.recycle();
    }

    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      this.weight = paramLayoutParams.weight;
    }

    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }

    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
  }

  public static abstract interface PanelSlideListener
  {
    public abstract void onPanelClosed(View paramView);

    public abstract void onPanelOpened(View paramView);

    public abstract void onPanelSlide(View paramView, float paramFloat);
  }

  static class SavedState extends AbsSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
    {
      public SlidingPaneLayout.SavedState createFromParcel(Parcel paramParcel, ClassLoader paramClassLoader)
      {
        return new SlidingPaneLayout.SavedState(paramParcel, paramClassLoader);
      }

      public SlidingPaneLayout.SavedState[] newArray(int paramInt)
      {
        return new SlidingPaneLayout.SavedState[paramInt];
      }
    });
    boolean isOpen;

    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramClassLoader);
      if (paramParcel.readInt() != 0);
      for (boolean bool = true; ; bool = false)
      {
        this.isOpen = bool;
        return;
      }
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      if (this.isOpen);
      for (int i = 1; ; i = 0)
      {
        paramParcel.writeInt(i);
        return;
      }
    }
  }

  public static class SimplePanelSlideListener
    implements SlidingPaneLayout.PanelSlideListener
  {
    public void onPanelClosed(View paramView)
    {
    }

    public void onPanelOpened(View paramView)
    {
    }

    public void onPanelSlide(View paramView, float paramFloat)
    {
    }
  }

  static abstract interface SlidingPanelLayoutImpl
  {
    public abstract void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView);
  }

  static class SlidingPanelLayoutImplBase
    implements SlidingPaneLayout.SlidingPanelLayoutImpl
  {
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      ViewCompat.postInvalidateOnAnimation(paramSlidingPaneLayout, paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    }
  }

  static class SlidingPanelLayoutImplJB extends SlidingPaneLayout.SlidingPanelLayoutImplBase
  {
    private Method mGetDisplayList;
    private Field mRecreateDisplayList;

    SlidingPanelLayoutImplJB()
    {
      try
      {
        this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[])null);
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        try
        {
          while (true)
          {
            this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
            this.mRecreateDisplayList.setAccessible(true);
            return;
            localNoSuchMethodException = localNoSuchMethodException;
            Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", localNoSuchMethodException);
          }
        }
        catch (NoSuchFieldException localNoSuchFieldException)
        {
          Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", localNoSuchFieldException);
        }
      }
    }

    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      if ((this.mGetDisplayList != null) && (this.mRecreateDisplayList != null))
        try
        {
          this.mRecreateDisplayList.setBoolean(paramView, true);
          this.mGetDisplayList.invoke(paramView, (Object[])null);
          super.invalidateChildRegion(paramSlidingPaneLayout, paramView);
          return;
        }
        catch (Exception localException)
        {
          while (true)
            Log.e("SlidingPaneLayout", "Error refreshing display list state", localException);
        }
      paramView.invalidate();
    }
  }

  static class SlidingPanelLayoutImplJBMR1 extends SlidingPaneLayout.SlidingPanelLayoutImplBase
  {
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      ViewCompat.setLayerPaint(paramView, ((SlidingPaneLayout.LayoutParams)paramView.getLayoutParams()).dimPaint);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.SlidingPaneLayout
 * JD-Core Version:    0.6.0
 */