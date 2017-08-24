package android.support.v4.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

public class SwipeRefreshLayout extends ViewGroup
  implements NestedScrollingParent, NestedScrollingChild
{
  private static final int ALPHA_ANIMATION_DURATION = 300;
  private static final int ANIMATE_TO_START_DURATION = 200;
  private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
  private static final int CIRCLE_BG_LIGHT = -328966;

  @VisibleForTesting
  static final int CIRCLE_DIAMETER = 40;

  @VisibleForTesting
  static final int CIRCLE_DIAMETER_LARGE = 56;
  private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0F;
  public static final int DEFAULT = 1;
  private static final int DEFAULT_CIRCLE_TARGET = 64;
  private static final float DRAG_RATE = 0.5F;
  private static final int INVALID_POINTER = -1;
  public static final int LARGE = 0;
  private static final int[] LAYOUT_ATTRS;
  private static final String LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
  private static final int MAX_ALPHA = 255;
  private static final float MAX_PROGRESS_ANGLE = 0.8F;
  private static final int SCALE_DOWN_DURATION = 150;
  private static final int STARTING_PROGRESS_ALPHA = 76;
  private int mActivePointerId = -1;
  private Animation mAlphaMaxAnimation;
  private Animation mAlphaStartAnimation;
  private final Animation mAnimateToCorrectPosition = new Animation()
  {
    public void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      if (!SwipeRefreshLayout.this.mUsingCustomStart);
      for (int i = SwipeRefreshLayout.this.mSpinnerOffsetEnd - Math.abs(SwipeRefreshLayout.this.mOriginalOffsetTop); ; i = SwipeRefreshLayout.this.mSpinnerOffsetEnd)
      {
        int j = SwipeRefreshLayout.this.mFrom;
        int k = (int)(paramFloat * (i - SwipeRefreshLayout.this.mFrom));
        int m = SwipeRefreshLayout.this.mCircleView.getTop();
        SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(k + j - m, false);
        SwipeRefreshLayout.this.mProgress.setArrowScale(1.0F - paramFloat);
        return;
      }
    }
  };
  private final Animation mAnimateToStartPosition = new Animation()
  {
    public void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      SwipeRefreshLayout.this.moveToStart(paramFloat);
    }
  };
  private OnChildScrollUpCallback mChildScrollUpCallback;
  private int mCircleDiameter;
  CircleImageView mCircleView;
  private int mCircleViewIndex = -1;
  int mCurrentTargetOffsetTop;
  private final DecelerateInterpolator mDecelerateInterpolator;
  protected int mFrom;
  private float mInitialDownY;
  private float mInitialMotionY;
  private boolean mIsBeingDragged;
  OnRefreshListener mListener;
  private int mMediumAnimationDuration;
  private boolean mNestedScrollInProgress;
  private final NestedScrollingChildHelper mNestedScrollingChildHelper;
  private final NestedScrollingParentHelper mNestedScrollingParentHelper;
  boolean mNotify;
  protected int mOriginalOffsetTop;
  private final int[] mParentOffsetInWindow = new int[2];
  private final int[] mParentScrollConsumed = new int[2];
  MaterialProgressDrawable mProgress;
  private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener()
  {
    @SuppressLint({"NewApi"})
    public void onAnimationEnd(Animation paramAnimation)
    {
      if (SwipeRefreshLayout.this.mRefreshing)
      {
        SwipeRefreshLayout.this.mProgress.setAlpha(255);
        SwipeRefreshLayout.this.mProgress.start();
        if ((SwipeRefreshLayout.this.mNotify) && (SwipeRefreshLayout.this.mListener != null))
          SwipeRefreshLayout.this.mListener.onRefresh();
        SwipeRefreshLayout.this.mCurrentTargetOffsetTop = SwipeRefreshLayout.this.mCircleView.getTop();
        return;
      }
      SwipeRefreshLayout.this.reset();
    }

    public void onAnimationRepeat(Animation paramAnimation)
    {
    }

    public void onAnimationStart(Animation paramAnimation)
    {
    }
  };
  boolean mRefreshing = false;
  private boolean mReturningToStart;
  boolean mScale;
  private Animation mScaleAnimation;
  private Animation mScaleDownAnimation;
  private Animation mScaleDownToStartAnimation;
  int mSpinnerOffsetEnd;
  float mStartingScale;
  private View mTarget;
  private float mTotalDragDistance = -1.0F;
  private float mTotalUnconsumed;
  private int mTouchSlop;
  boolean mUsingCustomStart;

  static
  {
    LAYOUT_ATTRS = new int[] { 16842766 };
  }

  public SwipeRefreshLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public SwipeRefreshLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    this.mMediumAnimationDuration = getResources().getInteger(17694721);
    setWillNotDraw(false);
    this.mDecelerateInterpolator = new DecelerateInterpolator(2.0F);
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    this.mCircleDiameter = (int)(40.0F * localDisplayMetrics.density);
    createProgressView();
    ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    this.mSpinnerOffsetEnd = (int)(64.0F * localDisplayMetrics.density);
    this.mTotalDragDistance = this.mSpinnerOffsetEnd;
    this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    this.mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
    setNestedScrollingEnabled(true);
    int i = -this.mCircleDiameter;
    this.mCurrentTargetOffsetTop = i;
    this.mOriginalOffsetTop = i;
    moveToStart(1.0F);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, LAYOUT_ATTRS);
    setEnabled(localTypedArray.getBoolean(0, true));
    localTypedArray.recycle();
  }

  private void animateOffsetToCorrectPosition(int paramInt, Animation.AnimationListener paramAnimationListener)
  {
    this.mFrom = paramInt;
    this.mAnimateToCorrectPosition.reset();
    this.mAnimateToCorrectPosition.setDuration(200L);
    this.mAnimateToCorrectPosition.setInterpolator(this.mDecelerateInterpolator);
    if (paramAnimationListener != null)
      this.mCircleView.setAnimationListener(paramAnimationListener);
    this.mCircleView.clearAnimation();
    this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
  }

  private void animateOffsetToStartPosition(int paramInt, Animation.AnimationListener paramAnimationListener)
  {
    if (this.mScale)
    {
      startScaleDownReturnToStartAnimation(paramInt, paramAnimationListener);
      return;
    }
    this.mFrom = paramInt;
    this.mAnimateToStartPosition.reset();
    this.mAnimateToStartPosition.setDuration(200L);
    this.mAnimateToStartPosition.setInterpolator(this.mDecelerateInterpolator);
    if (paramAnimationListener != null)
      this.mCircleView.setAnimationListener(paramAnimationListener);
    this.mCircleView.clearAnimation();
    this.mCircleView.startAnimation(this.mAnimateToStartPosition);
  }

  private void createProgressView()
  {
    this.mCircleView = new CircleImageView(getContext(), -328966);
    this.mProgress = new MaterialProgressDrawable(getContext(), this);
    this.mProgress.setBackgroundColor(-328966);
    this.mCircleView.setImageDrawable(this.mProgress);
    this.mCircleView.setVisibility(8);
    addView(this.mCircleView);
  }

  private void ensureTarget()
  {
    if (this.mTarget == null);
    for (int i = 0; ; i++)
    {
      if (i < getChildCount())
      {
        View localView = getChildAt(i);
        if (localView.equals(this.mCircleView))
          continue;
        this.mTarget = localView;
      }
      return;
    }
  }

  private void finishSpinner(float paramFloat)
  {
    if (paramFloat > this.mTotalDragDistance)
    {
      setRefreshing(true, true);
      return;
    }
    this.mRefreshing = false;
    this.mProgress.setStartEndTrim(0.0F, 0.0F);
    boolean bool = this.mScale;
    5 local5 = null;
    if (!bool)
      local5 = new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          if (!SwipeRefreshLayout.this.mScale)
            SwipeRefreshLayout.this.startScaleDownAnimation(null);
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
        }
      };
    animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, local5);
    this.mProgress.showArrow(false);
  }

  private boolean isAlphaUsedForScale()
  {
    return Build.VERSION.SDK_INT < 11;
  }

  private boolean isAnimationRunning(Animation paramAnimation)
  {
    return (paramAnimation != null) && (paramAnimation.hasStarted()) && (!paramAnimation.hasEnded());
  }

  @SuppressLint({"NewApi"})
  private void moveSpinner(float paramFloat)
  {
    this.mProgress.showArrow(true);
    float f1 = Math.min(1.0F, Math.abs(paramFloat / this.mTotalDragDistance));
    float f2 = 5.0F * (float)Math.max(f1 - 0.4D, 0.0D) / 3.0F;
    float f3 = Math.abs(paramFloat);
    float f4 = this.mTotalDragDistance;
    float f5;
    float f7;
    int i;
    int j;
    if (this.mUsingCustomStart)
    {
      f5 = this.mSpinnerOffsetEnd - this.mOriginalOffsetTop;
      float f6 = Math.max(0.0F, Math.min(f3 - f4, 2.0F * f5) / f5);
      f7 = 2.0F * (float)(f6 / 4.0F - Math.pow(f6 / 4.0F, 2.0D));
      i = this.mOriginalOffsetTop;
      j = (int)(f1 * f5 + 2.0F * (f5 * f7));
      if (this.mCircleView.getVisibility() != 0)
        this.mCircleView.setVisibility(0);
      if (!this.mScale)
      {
        ViewCompat.setScaleX(this.mCircleView, 1.0F);
        ViewCompat.setScaleY(this.mCircleView, 1.0F);
      }
      if (this.mScale)
        setAnimationProgress(Math.min(1.0F, paramFloat / this.mTotalDragDistance));
      if (paramFloat >= this.mTotalDragDistance)
        break label318;
      if ((this.mProgress.getAlpha() > 76) && (!isAnimationRunning(this.mAlphaStartAnimation)))
        startProgressAlphaStartAnimation();
    }
    while (true)
    {
      this.mProgress.setStartEndTrim(0.0F, Math.min(0.8F, f2 * 0.8F));
      this.mProgress.setArrowScale(Math.min(1.0F, f2));
      this.mProgress.setProgressRotation(0.5F * (-0.25F + f2 * 0.4F + f7 * 2.0F));
      setTargetOffsetTopAndBottom(j + i - this.mCurrentTargetOffsetTop, true);
      return;
      f5 = this.mSpinnerOffsetEnd;
      break;
      label318: if ((this.mProgress.getAlpha() >= 255) || (isAnimationRunning(this.mAlphaMaxAnimation)))
        continue;
      startProgressAlphaMaxAnimation();
    }
  }

  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
      if (i != 0)
        break label33;
    label33: for (int j = 1; ; j = 0)
    {
      this.mActivePointerId = paramMotionEvent.getPointerId(j);
      return;
    }
  }

  @SuppressLint({"NewApi"})
  private void setColorViewAlpha(int paramInt)
  {
    this.mCircleView.getBackground().setAlpha(paramInt);
    this.mProgress.setAlpha(paramInt);
  }

  private void setRefreshing(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mRefreshing != paramBoolean1)
    {
      this.mNotify = paramBoolean2;
      ensureTarget();
      this.mRefreshing = paramBoolean1;
      if (this.mRefreshing)
        animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
    }
    else
    {
      return;
    }
    startScaleDownAnimation(this.mRefreshListener);
  }

  @SuppressLint({"NewApi"})
  private Animation startAlphaAnimation(int paramInt1, int paramInt2)
  {
    if ((this.mScale) && (isAlphaUsedForScale()))
      return null;
    4 local4 = new Animation(paramInt1, paramInt2)
    {
      public void applyTransformation(float paramFloat, Transformation paramTransformation)
      {
        SwipeRefreshLayout.this.mProgress.setAlpha((int)(this.val$startingAlpha + paramFloat * (this.val$endingAlpha - this.val$startingAlpha)));
      }
    };
    local4.setDuration(300L);
    this.mCircleView.setAnimationListener(null);
    this.mCircleView.clearAnimation();
    this.mCircleView.startAnimation(local4);
    return local4;
  }

  @SuppressLint({"NewApi"})
  private void startDragging(float paramFloat)
  {
    if ((paramFloat - this.mInitialDownY > this.mTouchSlop) && (!this.mIsBeingDragged))
    {
      this.mInitialMotionY = (this.mInitialDownY + this.mTouchSlop);
      this.mIsBeingDragged = true;
      this.mProgress.setAlpha(76);
    }
  }

  @SuppressLint({"NewApi"})
  private void startProgressAlphaMaxAnimation()
  {
    this.mAlphaMaxAnimation = startAlphaAnimation(this.mProgress.getAlpha(), 255);
  }

  @SuppressLint({"NewApi"})
  private void startProgressAlphaStartAnimation()
  {
    this.mAlphaStartAnimation = startAlphaAnimation(this.mProgress.getAlpha(), 76);
  }

  @SuppressLint({"NewApi"})
  private void startScaleDownReturnToStartAnimation(int paramInt, Animation.AnimationListener paramAnimationListener)
  {
    this.mFrom = paramInt;
    if (isAlphaUsedForScale());
    for (this.mStartingScale = this.mProgress.getAlpha(); ; this.mStartingScale = ViewCompat.getScaleX(this.mCircleView))
    {
      this.mScaleDownToStartAnimation = new Animation()
      {
        public void applyTransformation(float paramFloat, Transformation paramTransformation)
        {
          float f1 = SwipeRefreshLayout.this.mStartingScale;
          float f2 = -SwipeRefreshLayout.this.mStartingScale;
          SwipeRefreshLayout.this.setAnimationProgress(f1 + f2 * paramFloat);
          SwipeRefreshLayout.this.moveToStart(paramFloat);
        }
      };
      this.mScaleDownToStartAnimation.setDuration(150L);
      if (paramAnimationListener != null)
        this.mCircleView.setAnimationListener(paramAnimationListener);
      this.mCircleView.clearAnimation();
      this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
      return;
    }
  }

  @SuppressLint({"NewApi"})
  private void startScaleUpAnimation(Animation.AnimationListener paramAnimationListener)
  {
    this.mCircleView.setVisibility(0);
    if (Build.VERSION.SDK_INT >= 11)
      this.mProgress.setAlpha(255);
    this.mScaleAnimation = new Animation()
    {
      public void applyTransformation(float paramFloat, Transformation paramTransformation)
      {
        SwipeRefreshLayout.this.setAnimationProgress(paramFloat);
      }
    };
    this.mScaleAnimation.setDuration(this.mMediumAnimationDuration);
    if (paramAnimationListener != null)
      this.mCircleView.setAnimationListener(paramAnimationListener);
    this.mCircleView.clearAnimation();
    this.mCircleView.startAnimation(this.mScaleAnimation);
  }

  public boolean canChildScrollUp()
  {
    if (this.mChildScrollUpCallback != null)
      return this.mChildScrollUpCallback.canChildScrollUp(this, this.mTarget);
    if (Build.VERSION.SDK_INT < 14)
    {
      if ((this.mTarget instanceof AbsListView))
      {
        AbsListView localAbsListView = (AbsListView)this.mTarget;
        if ((localAbsListView.getChildCount() > 0) && ((localAbsListView.getFirstVisiblePosition() > 0) || (localAbsListView.getChildAt(0).getTop() < localAbsListView.getPaddingTop())))
          break label113;
        return false;
      }
      if ((ViewCompat.canScrollVertically(this.mTarget, -1)) || (this.mTarget.getScrollY() > 0))
        return true;
    }
    else
    {
      return ViewCompat.canScrollVertically(this.mTarget, -1);
    }
    return false;
    label113: return true;
  }

  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    return this.mNestedScrollingChildHelper.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }

  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2)
  {
    return this.mNestedScrollingChildHelper.dispatchNestedPreFling(paramFloat1, paramFloat2);
  }

  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
  }

  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    return this.mNestedScrollingChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
  }

  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (this.mCircleViewIndex < 0);
    do
    {
      return paramInt2;
      if (paramInt2 == paramInt1 - 1)
        return this.mCircleViewIndex;
    }
    while (paramInt2 < this.mCircleViewIndex);
    return paramInt2 + 1;
  }

  public int getNestedScrollAxes()
  {
    return this.mNestedScrollingParentHelper.getNestedScrollAxes();
  }

  public int getProgressCircleDiameter()
  {
    return this.mCircleDiameter;
  }

  public int getProgressViewEndOffset()
  {
    return this.mSpinnerOffsetEnd;
  }

  public int getProgressViewStartOffset()
  {
    return this.mOriginalOffsetTop;
  }

  public boolean hasNestedScrollingParent()
  {
    return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
  }

  public boolean isNestedScrollingEnabled()
  {
    return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
  }

  public boolean isRefreshing()
  {
    return this.mRefreshing;
  }

  void moveToStart(float paramFloat)
  {
    setTargetOffsetTopAndBottom(this.mFrom + (int)(paramFloat * (this.mOriginalOffsetTop - this.mFrom)) - this.mCircleView.getTop(), false);
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    reset();
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    ensureTarget();
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if ((this.mReturningToStart) && (i == 0))
      this.mReturningToStart = false;
    if ((!isEnabled()) || (this.mReturningToStart) || (canChildScrollUp()) || (this.mRefreshing) || (this.mNestedScrollInProgress))
      return false;
    switch (i)
    {
    case 4:
    case 5:
    default:
    case 0:
    case 2:
    case 6:
    case 1:
    case 3:
    }
    while (true)
    {
      return this.mIsBeingDragged;
      setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop(), true);
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsBeingDragged = false;
      int k = paramMotionEvent.findPointerIndex(this.mActivePointerId);
      if (k < 0)
        break;
      this.mInitialDownY = paramMotionEvent.getY(k);
      continue;
      if (this.mActivePointerId == -1)
      {
        Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
        return false;
      }
      int j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
      if (j < 0)
        break;
      startDragging(paramMotionEvent.getY(j));
      continue;
      onSecondaryPointerUp(paramMotionEvent);
      continue;
      this.mIsBeingDragged = false;
      this.mActivePointerId = -1;
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getMeasuredWidth();
    int j = getMeasuredHeight();
    if (getChildCount() == 0);
    do
    {
      return;
      if (this.mTarget != null)
        continue;
      ensureTarget();
    }
    while (this.mTarget == null);
    View localView = this.mTarget;
    int k = getPaddingLeft();
    int m = getPaddingTop();
    localView.layout(k, m, k + (i - getPaddingLeft() - getPaddingRight()), m + (j - getPaddingTop() - getPaddingBottom()));
    int n = this.mCircleView.getMeasuredWidth();
    int i1 = this.mCircleView.getMeasuredHeight();
    this.mCircleView.layout(i / 2 - n / 2, this.mCurrentTargetOffsetTop, i / 2 + n / 2, i1 + this.mCurrentTargetOffsetTop);
  }

  public void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mTarget == null)
      ensureTarget();
    if (this.mTarget == null);
    while (true)
    {
      return;
      this.mTarget.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), 1073741824));
      this.mCircleView.measure(View.MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824));
      this.mCircleViewIndex = -1;
      for (int i = 0; i < getChildCount(); i++)
      {
        if (getChildAt(i) != this.mCircleView)
          continue;
        this.mCircleViewIndex = i;
        return;
      }
    }
  }

  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    return dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }

  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2)
  {
    return dispatchNestedPreFling(paramFloat1, paramFloat2);
  }

  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    if ((paramInt2 > 0) && (this.mTotalUnconsumed > 0.0F))
    {
      if (paramInt2 <= this.mTotalUnconsumed)
        break label143;
      paramArrayOfInt[1] = (paramInt2 - (int)this.mTotalUnconsumed);
      this.mTotalUnconsumed = 0.0F;
    }
    while (true)
    {
      moveSpinner(this.mTotalUnconsumed);
      if ((this.mUsingCustomStart) && (paramInt2 > 0) && (this.mTotalUnconsumed == 0.0F) && (Math.abs(paramInt2 - paramArrayOfInt[1]) > 0))
        this.mCircleView.setVisibility(8);
      int[] arrayOfInt = this.mParentScrollConsumed;
      if (dispatchNestedPreScroll(paramInt1 - paramArrayOfInt[0], paramInt2 - paramArrayOfInt[1], arrayOfInt, null))
      {
        paramArrayOfInt[0] += arrayOfInt[0];
        paramArrayOfInt[1] += arrayOfInt[1];
      }
      return;
      label143: this.mTotalUnconsumed -= paramInt2;
      paramArrayOfInt[1] = paramInt2;
    }
  }

  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mParentOffsetInWindow);
    int i = paramInt4 + this.mParentOffsetInWindow[1];
    if ((i < 0) && (!canChildScrollUp()))
    {
      this.mTotalUnconsumed += Math.abs(i);
      moveSpinner(this.mTotalUnconsumed);
    }
  }

  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    this.mNestedScrollingParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    startNestedScroll(paramInt & 0x2);
    this.mTotalUnconsumed = 0.0F;
    this.mNestedScrollInProgress = true;
  }

  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt)
  {
    return (isEnabled()) && (!this.mReturningToStart) && (!this.mRefreshing) && ((paramInt & 0x2) != 0);
  }

  public void onStopNestedScroll(View paramView)
  {
    this.mNestedScrollingParentHelper.onStopNestedScroll(paramView);
    this.mNestedScrollInProgress = false;
    if (this.mTotalUnconsumed > 0.0F)
    {
      finishSpinner(this.mTotalUnconsumed);
      this.mTotalUnconsumed = 0.0F;
    }
    stopNestedScroll();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if ((this.mReturningToStart) && (i == 0))
      this.mReturningToStart = false;
    if ((!isEnabled()) || (this.mReturningToStart) || (canChildScrollUp()) || (this.mRefreshing) || (this.mNestedScrollInProgress))
      return false;
    switch (i)
    {
    case 3:
    case 4:
    default:
    case 0:
    case 2:
    case 5:
    case 6:
      while (true)
      {
        return true;
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        this.mIsBeingDragged = false;
        continue;
        int m = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (m < 0)
        {
          Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
          return false;
        }
        float f3 = paramMotionEvent.getY(m);
        startDragging(f3);
        if (!this.mIsBeingDragged)
          continue;
        float f4 = 0.5F * (f3 - this.mInitialMotionY);
        if (f4 <= 0.0F)
          break;
        moveSpinner(f4);
        continue;
        int k = MotionEventCompat.getActionIndex(paramMotionEvent);
        if (k < 0)
        {
          Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
          return false;
        }
        this.mActivePointerId = paramMotionEvent.getPointerId(k);
        continue;
        onSecondaryPointerUp(paramMotionEvent);
      }
    case 1:
    }
    int j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
    if (j < 0)
    {
      Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
      return false;
    }
    if (this.mIsBeingDragged)
    {
      float f1 = paramMotionEvent.getY(j);
      float f2 = this.mInitialMotionY;
      this.mIsBeingDragged = false;
      finishSpinner(0.5F * (f1 - f2));
    }
    this.mActivePointerId = -1;
    return false;
  }

  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    if (((Build.VERSION.SDK_INT < 21) && ((this.mTarget instanceof AbsListView))) || ((this.mTarget != null) && (!ViewCompat.isNestedScrollingEnabled(this.mTarget))))
      return;
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }

  void reset()
  {
    this.mCircleView.clearAnimation();
    this.mProgress.stop();
    this.mCircleView.setVisibility(8);
    setColorViewAlpha(255);
    if (this.mScale)
      setAnimationProgress(0.0F);
    while (true)
    {
      this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
      return;
      setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCurrentTargetOffsetTop, true);
    }
  }

  void setAnimationProgress(float paramFloat)
  {
    if (isAlphaUsedForScale())
    {
      setColorViewAlpha((int)(255.0F * paramFloat));
      return;
    }
    ViewCompat.setScaleX(this.mCircleView, paramFloat);
    ViewCompat.setScaleY(this.mCircleView, paramFloat);
  }

  @Deprecated
  public void setColorScheme(@ColorInt int[] paramArrayOfInt)
  {
    setColorSchemeResources(paramArrayOfInt);
  }

  public void setColorSchemeColors(@ColorInt int[] paramArrayOfInt)
  {
    ensureTarget();
    this.mProgress.setColorSchemeColors(paramArrayOfInt);
  }

  public void setColorSchemeResources(@ColorRes int[] paramArrayOfInt)
  {
    Context localContext = getContext();
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++)
      arrayOfInt[i] = ContextCompat.getColor(localContext, paramArrayOfInt[i]);
    setColorSchemeColors(arrayOfInt);
  }

  public void setDistanceToTriggerSync(int paramInt)
  {
    this.mTotalDragDistance = paramInt;
  }

  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (!paramBoolean)
      reset();
  }

  public void setNestedScrollingEnabled(boolean paramBoolean)
  {
    this.mNestedScrollingChildHelper.setNestedScrollingEnabled(paramBoolean);
  }

  public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback paramOnChildScrollUpCallback)
  {
    this.mChildScrollUpCallback = paramOnChildScrollUpCallback;
  }

  public void setOnRefreshListener(OnRefreshListener paramOnRefreshListener)
  {
    this.mListener = paramOnRefreshListener;
  }

  @Deprecated
  public void setProgressBackgroundColor(int paramInt)
  {
    setProgressBackgroundColorSchemeResource(paramInt);
  }

  public void setProgressBackgroundColorSchemeColor(@ColorInt int paramInt)
  {
    this.mCircleView.setBackgroundColor(paramInt);
    this.mProgress.setBackgroundColor(paramInt);
  }

  public void setProgressBackgroundColorSchemeResource(@ColorRes int paramInt)
  {
    setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), paramInt));
  }

  public void setProgressViewEndTarget(boolean paramBoolean, int paramInt)
  {
    this.mSpinnerOffsetEnd = paramInt;
    this.mScale = paramBoolean;
    this.mCircleView.invalidate();
  }

  public void setProgressViewOffset(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    this.mScale = paramBoolean;
    this.mOriginalOffsetTop = paramInt1;
    this.mSpinnerOffsetEnd = paramInt2;
    this.mUsingCustomStart = true;
    reset();
    this.mRefreshing = false;
  }

  public void setRefreshing(boolean paramBoolean)
  {
    if ((paramBoolean) && (this.mRefreshing != paramBoolean))
    {
      this.mRefreshing = paramBoolean;
      if (!this.mUsingCustomStart);
      for (int i = this.mSpinnerOffsetEnd + this.mOriginalOffsetTop; ; i = this.mSpinnerOffsetEnd)
      {
        setTargetOffsetTopAndBottom(i - this.mCurrentTargetOffsetTop, true);
        this.mNotify = false;
        startScaleUpAnimation(this.mRefreshListener);
        return;
      }
    }
    setRefreshing(paramBoolean, false);
  }

  public void setSize(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 1))
      return;
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    if (paramInt == 0);
    for (this.mCircleDiameter = (int)(56.0F * localDisplayMetrics.density); ; this.mCircleDiameter = (int)(40.0F * localDisplayMetrics.density))
    {
      this.mCircleView.setImageDrawable(null);
      this.mProgress.updateSizes(paramInt);
      this.mCircleView.setImageDrawable(this.mProgress);
      return;
    }
  }

  void setTargetOffsetTopAndBottom(int paramInt, boolean paramBoolean)
  {
    this.mCircleView.bringToFront();
    ViewCompat.offsetTopAndBottom(this.mCircleView, paramInt);
    this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    if ((paramBoolean) && (Build.VERSION.SDK_INT < 11))
      invalidate();
  }

  public boolean startNestedScroll(int paramInt)
  {
    return this.mNestedScrollingChildHelper.startNestedScroll(paramInt);
  }

  void startScaleDownAnimation(Animation.AnimationListener paramAnimationListener)
  {
    this.mScaleDownAnimation = new Animation()
    {
      public void applyTransformation(float paramFloat, Transformation paramTransformation)
      {
        SwipeRefreshLayout.this.setAnimationProgress(1.0F - paramFloat);
      }
    };
    this.mScaleDownAnimation.setDuration(150L);
    this.mCircleView.setAnimationListener(paramAnimationListener);
    this.mCircleView.clearAnimation();
    this.mCircleView.startAnimation(this.mScaleDownAnimation);
  }

  public void stopNestedScroll()
  {
    this.mNestedScrollingChildHelper.stopNestedScroll();
  }

  public static abstract interface OnChildScrollUpCallback
  {
    public abstract boolean canChildScrollUp(SwipeRefreshLayout paramSwipeRefreshLayout, @Nullable View paramView);
  }

  public static abstract interface OnRefreshListener
  {
    public abstract void onRefresh();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.SwipeRefreshLayout
 * JD-Core Version:    0.6.0
 */