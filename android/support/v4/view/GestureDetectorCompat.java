package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat
{
  private final GestureDetectorCompatImpl mImpl;

  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener)
  {
    this(paramContext, paramOnGestureListener, null);
  }

  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
  {
    if (Build.VERSION.SDK_INT > 17)
    {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
      return;
    }
    this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
  }

  public boolean isLongpressEnabled()
  {
    return this.mImpl.isLongpressEnabled();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }

  public void setIsLongpressEnabled(boolean paramBoolean)
  {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }

  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
  {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }

  static abstract interface GestureDetectorCompatImpl
  {
    public abstract boolean isLongpressEnabled();

    public abstract boolean onTouchEvent(MotionEvent paramMotionEvent);

    public abstract void setIsLongpressEnabled(boolean paramBoolean);

    public abstract void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener);
  }

  static class GestureDetectorCompatImplBase
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private static final int DOUBLE_TAP_TIMEOUT = 0;
    private static final int LONGPRESS_TIMEOUT = 0;
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private boolean mAlwaysInBiggerTapRegion;
    private boolean mAlwaysInTapRegion;
    MotionEvent mCurrentDownEvent;
    boolean mDeferConfirmSingleTap;
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mInLongPress;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    final GestureDetector.OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    boolean mStillDown;
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;

    static
    {
      DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    }

    public GestureDetectorCompatImplBase(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      if (paramHandler != null);
      for (this.mHandler = new GestureHandler(paramHandler); ; this.mHandler = new GestureHandler())
      {
        this.mListener = paramOnGestureListener;
        if ((paramOnGestureListener instanceof GestureDetector.OnDoubleTapListener))
          setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)paramOnGestureListener);
        init(paramContext);
        return;
      }
    }

    private void cancel()
    {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false;
    }

    private void cancelTaps()
    {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false;
    }

    private void init(Context paramContext)
    {
      if (paramContext == null)
        throw new IllegalArgumentException("Context must not be null");
      if (this.mListener == null)
        throw new IllegalArgumentException("OnGestureListener must not be null");
      this.mIsLongpressEnabled = true;
      ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
      int i = localViewConfiguration.getScaledTouchSlop();
      int j = localViewConfiguration.getScaledDoubleTapSlop();
      this.mMinimumFlingVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
      this.mMaximumFlingVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
      this.mTouchSlopSquare = (i * i);
      this.mDoubleTapSlopSquare = (j * j);
    }

    private boolean isConsideredDoubleTap(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, MotionEvent paramMotionEvent3)
    {
      if (!this.mAlwaysInBiggerTapRegion);
      int i;
      int j;
      do
      {
        do
          return false;
        while (paramMotionEvent3.getEventTime() - paramMotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT);
        i = (int)paramMotionEvent1.getX() - (int)paramMotionEvent3.getX();
        j = (int)paramMotionEvent1.getY() - (int)paramMotionEvent3.getY();
      }
      while (i * i + j * j >= this.mDoubleTapSlopSquare);
      return true;
    }

    void dispatchLongPress()
    {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }

    public boolean isLongpressEnabled()
    {
      return this.mIsLongpressEnabled;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getAction();
      if (this.mVelocityTracker == null)
        this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
      int j;
      int k;
      label49: int m;
      int n;
      float f1;
      float f2;
      if ((i & 0xFF) == 6)
      {
        j = 1;
        if (j == 0)
          break label89;
        k = MotionEventCompat.getActionIndex(paramMotionEvent);
        m = paramMotionEvent.getPointerCount();
        n = 0;
        f1 = 0.0F;
        f2 = 0.0F;
        label64: if (n >= m)
          break label120;
        if (k != n)
          break label95;
      }
      while (true)
      {
        n++;
        break label64;
        j = 0;
        break;
        label89: k = -1;
        break label49;
        label95: f1 += paramMotionEvent.getX(n);
        f2 += paramMotionEvent.getY(n);
      }
      label120: if (j != 0);
      float f3;
      float f4;
      boolean bool3;
      for (int i1 = m - 1; ; i1 = m)
      {
        f3 = f1 / i1;
        f4 = f2 / i1;
        switch (i & 0xFF)
        {
        default:
          bool3 = false;
          return bool3;
        case 5:
        case 6:
        case 0:
        case 2:
        case 1:
        case 3:
        case 4:
        }
      }
      this.mLastFocusX = f3;
      this.mDownFocusX = f3;
      this.mLastFocusY = f4;
      this.mDownFocusY = f4;
      cancelTaps();
      return false;
      this.mLastFocusX = f3;
      this.mDownFocusX = f3;
      this.mLastFocusY = f4;
      this.mDownFocusY = f4;
      this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
      int i6 = MotionEventCompat.getActionIndex(paramMotionEvent);
      int i7 = paramMotionEvent.getPointerId(i6);
      float f9 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, i7);
      float f10 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, i7);
      int i8 = 0;
      boolean bool4;
      if (i8 < m)
      {
        if (i8 == i6);
        int i9;
        do
        {
          i8++;
          break;
          i9 = paramMotionEvent.getPointerId(i8);
        }
        while (f9 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, i9) + f10 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, i9) >= 0.0F);
        this.mVelocityTracker.clear();
        return false;
        if (this.mDoubleTapListener == null)
          break label1159;
        boolean bool5 = this.mHandler.hasMessages(3);
        if (bool5)
          this.mHandler.removeMessages(3);
        if ((this.mCurrentDownEvent != null) && (this.mPreviousUpEvent != null) && (bool5) && (isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, paramMotionEvent)))
        {
          this.mIsDoubleTapping = true;
          bool4 = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
        }
      }
      while (true)
      {
        this.mLastFocusX = f3;
        this.mDownFocusX = f3;
        this.mLastFocusY = f4;
        this.mDownFocusY = f4;
        if (this.mCurrentDownEvent != null)
          this.mCurrentDownEvent.recycle();
        this.mCurrentDownEvent = MotionEvent.obtain(paramMotionEvent);
        this.mAlwaysInTapRegion = true;
        this.mAlwaysInBiggerTapRegion = true;
        this.mStillDown = true;
        this.mInLongPress = false;
        this.mDeferConfirmSingleTap = false;
        if (this.mIsLongpressEnabled)
        {
          this.mHandler.removeMessages(2);
          this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT + LONGPRESS_TIMEOUT);
        }
        this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
        return bool4 | this.mListener.onDown(paramMotionEvent);
        this.mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
        bool4 = false;
        continue;
        float f7;
        float f8;
        int i5;
        if (!this.mInLongPress)
        {
          f7 = this.mLastFocusX - f3;
          f8 = this.mLastFocusY - f4;
          if (this.mIsDoubleTapping)
            return false | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
          if (this.mAlwaysInTapRegion)
          {
            int i3 = (int)(f3 - this.mDownFocusX);
            int i4 = (int)(f4 - this.mDownFocusY);
            i5 = i3 * i3 + i4 * i4;
            if (i5 <= this.mTouchSlopSquare)
              break label1153;
            bool3 = this.mListener.onScroll(this.mCurrentDownEvent, paramMotionEvent, f7, f8);
            this.mLastFocusX = f3;
            this.mLastFocusY = f4;
            this.mAlwaysInTapRegion = false;
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
          }
        }
        while (i5 > this.mTouchSlopSquare)
        {
          this.mAlwaysInBiggerTapRegion = false;
          return bool3;
          if ((Math.abs(f7) >= 1.0F) || (Math.abs(f8) >= 1.0F))
          {
            boolean bool2 = this.mListener.onScroll(this.mCurrentDownEvent, paramMotionEvent, f7, f8);
            this.mLastFocusX = f3;
            this.mLastFocusY = f4;
            return bool2;
            this.mStillDown = false;
            MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
            boolean bool1;
            if (this.mIsDoubleTapping)
              bool1 = false | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
            while (true)
            {
              if (this.mPreviousUpEvent != null)
                this.mPreviousUpEvent.recycle();
              this.mPreviousUpEvent = localMotionEvent;
              if (this.mVelocityTracker != null)
              {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
              }
              this.mIsDoubleTapping = false;
              this.mDeferConfirmSingleTap = false;
              this.mHandler.removeMessages(1);
              this.mHandler.removeMessages(2);
              return bool1;
              if (this.mInLongPress)
              {
                this.mHandler.removeMessages(3);
                this.mInLongPress = false;
                bool1 = false;
                continue;
              }
              if (this.mAlwaysInTapRegion)
              {
                bool1 = this.mListener.onSingleTapUp(paramMotionEvent);
                if ((!this.mDeferConfirmSingleTap) || (this.mDoubleTapListener == null))
                  continue;
                this.mDoubleTapListener.onSingleTapConfirmed(paramMotionEvent);
                continue;
              }
              VelocityTracker localVelocityTracker = this.mVelocityTracker;
              int i2 = paramMotionEvent.getPointerId(0);
              localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
              float f5 = VelocityTrackerCompat.getYVelocity(localVelocityTracker, i2);
              float f6 = VelocityTrackerCompat.getXVelocity(localVelocityTracker, i2);
              if ((Math.abs(f5) > this.mMinimumFlingVelocity) || (Math.abs(f6) > this.mMinimumFlingVelocity))
              {
                bool1 = this.mListener.onFling(this.mCurrentDownEvent, paramMotionEvent, f6, f5);
                continue;
                cancel();
                return false;
              }
              bool1 = false;
            }
          }
          return false;
          label1153: bool3 = false;
        }
        label1159: bool4 = false;
      }
    }

    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      this.mIsLongpressEnabled = paramBoolean;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      this.mDoubleTapListener = paramOnDoubleTapListener;
    }

    private class GestureHandler extends Handler
    {
      GestureHandler()
      {
      }

      GestureHandler(Handler arg2)
      {
        super();
      }

      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default:
          throw new RuntimeException("Unknown message " + paramMessage);
        case 1:
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
        case 2:
        case 3:
        }
        do
        {
          return;
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
          return;
        }
        while (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener == null);
        if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown)
        {
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
          return;
        }
        GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
      }
    }
  }

  static class GestureDetectorCompatImplJellybeanMr2
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private final GestureDetector mDetector;

    public GestureDetectorCompatImplJellybeanMr2(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      this.mDetector = new GestureDetector(paramContext, paramOnGestureListener, paramHandler);
    }

    public boolean isLongpressEnabled()
    {
      return this.mDetector.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      return this.mDetector.onTouchEvent(paramMotionEvent);
    }

    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      this.mDetector.setIsLongpressEnabled(paramBoolean);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      this.mDetector.setOnDoubleTapListener(paramOnDoubleTapListener);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.GestureDetectorCompat
 * JD-Core Version:    0.6.0
 */