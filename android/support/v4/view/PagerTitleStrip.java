package android.support.v4.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;

@ViewPager.DecorView
public class PagerTitleStrip extends ViewGroup
{
  private static final int[] ATTRS = { 16842804, 16842901, 16842904, 16842927 };
  private static final PagerTitleStripImpl IMPL;
  private static final float SIDE_ALPHA = 0.6F;
  private static final String TAG = "PagerTitleStrip";
  private static final int[] TEXT_ATTRS = { 16843660 };
  private static final int TEXT_SPACING = 16;
  TextView mCurrText;
  private int mGravity;
  private int mLastKnownCurrentPage = -1;
  float mLastKnownPositionOffset = -1.0F;
  TextView mNextText;
  private int mNonPrimaryAlpha;
  private final PageListener mPageListener = new PageListener();
  ViewPager mPager;
  TextView mPrevText;
  private int mScaledTextSpacing;
  int mTextColor;
  private boolean mUpdatingPositions;
  private boolean mUpdatingText;
  private WeakReference<PagerAdapter> mWatchingAdapter;

  static
  {
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new PagerTitleStripImplIcs();
      return;
    }
    IMPL = new PagerTitleStripImplBase();
  }

  public PagerTitleStrip(Context paramContext)
  {
    this(paramContext, null);
  }

  public PagerTitleStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TextView localTextView1 = new TextView(paramContext);
    this.mPrevText = localTextView1;
    addView(localTextView1);
    TextView localTextView2 = new TextView(paramContext);
    this.mCurrText = localTextView2;
    addView(localTextView2);
    TextView localTextView3 = new TextView(paramContext);
    this.mNextText = localTextView3;
    addView(localTextView3);
    TypedArray localTypedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS);
    int i = localTypedArray1.getResourceId(0, 0);
    if (i != 0)
    {
      TextViewCompat.setTextAppearance(this.mPrevText, i);
      TextViewCompat.setTextAppearance(this.mCurrText, i);
      TextViewCompat.setTextAppearance(this.mNextText, i);
    }
    int j = localTypedArray1.getDimensionPixelSize(1, 0);
    if (j != 0)
      setTextSize(0, j);
    if (localTypedArray1.hasValue(2))
    {
      int k = localTypedArray1.getColor(2, 0);
      this.mPrevText.setTextColor(k);
      this.mCurrText.setTextColor(k);
      this.mNextText.setTextColor(k);
    }
    this.mGravity = localTypedArray1.getInteger(3, 80);
    localTypedArray1.recycle();
    this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
    setNonPrimaryAlpha(0.6F);
    this.mPrevText.setEllipsize(TextUtils.TruncateAt.END);
    this.mCurrText.setEllipsize(TextUtils.TruncateAt.END);
    this.mNextText.setEllipsize(TextUtils.TruncateAt.END);
    boolean bool = false;
    if (i != 0)
    {
      TypedArray localTypedArray2 = paramContext.obtainStyledAttributes(i, TEXT_ATTRS);
      bool = localTypedArray2.getBoolean(0, false);
      localTypedArray2.recycle();
    }
    if (bool)
    {
      setSingleLineAllCaps(this.mPrevText);
      setSingleLineAllCaps(this.mCurrText);
      setSingleLineAllCaps(this.mNextText);
    }
    while (true)
    {
      this.mScaledTextSpacing = (int)(16.0F * paramContext.getResources().getDisplayMetrics().density);
      return;
      this.mPrevText.setSingleLine();
      this.mCurrText.setSingleLine();
      this.mNextText.setSingleLine();
    }
  }

  private static void setSingleLineAllCaps(TextView paramTextView)
  {
    IMPL.setSingleLineAllCaps(paramTextView);
  }

  int getMinHeight()
  {
    Drawable localDrawable = getBackground();
    int i = 0;
    if (localDrawable != null)
      i = localDrawable.getIntrinsicHeight();
    return i;
  }

  public int getTextSpacing()
  {
    return this.mScaledTextSpacing;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewParent localViewParent = getParent();
    if (!(localViewParent instanceof ViewPager))
      throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    ViewPager localViewPager = (ViewPager)localViewParent;
    PagerAdapter localPagerAdapter1 = localViewPager.getAdapter();
    localViewPager.setInternalPageChangeListener(this.mPageListener);
    localViewPager.addOnAdapterChangeListener(this.mPageListener);
    this.mPager = localViewPager;
    if (this.mWatchingAdapter != null);
    for (PagerAdapter localPagerAdapter2 = (PagerAdapter)this.mWatchingAdapter.get(); ; localPagerAdapter2 = null)
    {
      updateAdapter(localPagerAdapter2, localPagerAdapter1);
      return;
    }
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mPager != null)
    {
      updateAdapter(this.mPager.getAdapter(), null);
      this.mPager.setInternalPageChangeListener(null);
      this.mPager.removeOnAdapterChangeListener(this.mPageListener);
      this.mPager = null;
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.mPager != null)
    {
      boolean bool = this.mLastKnownPositionOffset < 0.0F;
      float f = 0.0F;
      if (!bool)
        f = this.mLastKnownPositionOffset;
      updateTextPositions(this.mLastKnownCurrentPage, f, true);
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (View.MeasureSpec.getMode(paramInt1) != 1073741824)
      throw new IllegalStateException("Must measure with an exact width");
    int i = getPaddingTop() + getPaddingBottom();
    int j = getChildMeasureSpec(paramInt2, i, -2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = getChildMeasureSpec(paramInt1, (int)(0.2F * k), -2);
    this.mPrevText.measure(m, j);
    this.mCurrText.measure(m, j);
    this.mNextText.measure(m, j);
    if (View.MeasureSpec.getMode(paramInt2) == 1073741824);
    int n;
    for (int i1 = View.MeasureSpec.getSize(paramInt2); ; i1 = Math.max(getMinHeight(), i + n))
    {
      setMeasuredDimension(k, ViewCompat.resolveSizeAndState(i1, paramInt2, ViewCompat.getMeasuredState(this.mCurrText) << 16));
      return;
      n = this.mCurrText.getMeasuredHeight();
    }
  }

  public void requestLayout()
  {
    if (!this.mUpdatingText)
      super.requestLayout();
  }

  public void setGravity(int paramInt)
  {
    this.mGravity = paramInt;
    requestLayout();
  }

  public void setNonPrimaryAlpha(@FloatRange(from=0.0D, to=1.0D) float paramFloat)
  {
    this.mNonPrimaryAlpha = (0xFF & (int)(255.0F * paramFloat));
    int i = this.mNonPrimaryAlpha << 24 | 0xFFFFFF & this.mTextColor;
    this.mPrevText.setTextColor(i);
    this.mNextText.setTextColor(i);
  }

  public void setTextColor(@ColorInt int paramInt)
  {
    this.mTextColor = paramInt;
    this.mCurrText.setTextColor(paramInt);
    int i = this.mNonPrimaryAlpha << 24 | 0xFFFFFF & this.mTextColor;
    this.mPrevText.setTextColor(i);
    this.mNextText.setTextColor(i);
  }

  public void setTextSize(int paramInt, float paramFloat)
  {
    this.mPrevText.setTextSize(paramInt, paramFloat);
    this.mCurrText.setTextSize(paramInt, paramFloat);
    this.mNextText.setTextSize(paramInt, paramFloat);
  }

  public void setTextSpacing(int paramInt)
  {
    this.mScaledTextSpacing = paramInt;
    requestLayout();
  }

  void updateAdapter(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
  {
    if (paramPagerAdapter1 != null)
    {
      paramPagerAdapter1.unregisterDataSetObserver(this.mPageListener);
      this.mWatchingAdapter = null;
    }
    if (paramPagerAdapter2 != null)
    {
      paramPagerAdapter2.registerDataSetObserver(this.mPageListener);
      this.mWatchingAdapter = new WeakReference(paramPagerAdapter2);
    }
    if (this.mPager != null)
    {
      this.mLastKnownCurrentPage = -1;
      this.mLastKnownPositionOffset = -1.0F;
      updateText(this.mPager.getCurrentItem(), paramPagerAdapter2);
      requestLayout();
    }
  }

  void updateText(int paramInt, PagerAdapter paramPagerAdapter)
  {
    int i;
    if (paramPagerAdapter != null)
    {
      i = paramPagerAdapter.getCount();
      this.mUpdatingText = true;
      if ((paramInt < 1) || (paramPagerAdapter == null))
        break label239;
    }
    label239: for (CharSequence localCharSequence1 = paramPagerAdapter.getPageTitle(paramInt - 1); ; localCharSequence1 = null)
    {
      this.mPrevText.setText(localCharSequence1);
      TextView localTextView = this.mCurrText;
      if ((paramPagerAdapter != null) && (paramInt < i));
      for (CharSequence localCharSequence2 = paramPagerAdapter.getPageTitle(paramInt); ; localCharSequence2 = null)
      {
        localTextView.setText(localCharSequence2);
        int j = paramInt + 1;
        CharSequence localCharSequence3 = null;
        if (j < i)
        {
          localCharSequence3 = null;
          if (paramPagerAdapter != null)
            localCharSequence3 = paramPagerAdapter.getPageTitle(paramInt + 1);
        }
        this.mNextText.setText(localCharSequence3);
        int k = View.MeasureSpec.makeMeasureSpec(Math.max(0, (int)(0.8F * (getWidth() - getPaddingLeft() - getPaddingRight()))), -2147483648);
        int m = View.MeasureSpec.makeMeasureSpec(Math.max(0, getHeight() - getPaddingTop() - getPaddingBottom()), -2147483648);
        this.mPrevText.measure(k, m);
        this.mCurrText.measure(k, m);
        this.mNextText.measure(k, m);
        this.mLastKnownCurrentPage = paramInt;
        if (!this.mUpdatingPositions)
          updateTextPositions(paramInt, this.mLastKnownPositionOffset, false);
        this.mUpdatingText = false;
        return;
        i = 0;
        break;
      }
    }
  }

  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean)
  {
    int i;
    int k;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i7;
    int i8;
    int i13;
    int i14;
    int i15;
    int i19;
    int i21;
    int i22;
    int i23;
    if (paramInt != this.mLastKnownCurrentPage)
    {
      updateText(paramInt, this.mPager.getAdapter());
      this.mUpdatingPositions = true;
      i = this.mPrevText.getMeasuredWidth();
      int j = this.mCurrText.getMeasuredWidth();
      k = this.mNextText.getMeasuredWidth();
      int m = j / 2;
      n = getWidth();
      i1 = getHeight();
      i2 = getPaddingLeft();
      i3 = getPaddingRight();
      i4 = getPaddingTop();
      i5 = getPaddingBottom();
      int i6 = i3 + m;
      float f = 0.5F + paramFloat;
      if (f > 1.0F)
        f -= 1.0F;
      i7 = n - i6 - (int)(f * (n - (m + i2) - i6)) - j / 2;
      i8 = i7 + j;
      int i9 = this.mPrevText.getBaseline();
      int i10 = this.mCurrText.getBaseline();
      int i11 = this.mNextText.getBaseline();
      int i12 = Math.max(Math.max(i9, i10), i11);
      i13 = i12 - i9;
      i14 = i12 - i10;
      i15 = i12 - i11;
      int i16 = this.mPrevText.getMeasuredHeight();
      int i17 = this.mCurrText.getMeasuredHeight();
      int i18 = this.mNextText.getMeasuredHeight();
      i19 = Math.max(Math.max(i16 + i13, i17 + i14), i15 + i18);
      switch (0x70 & this.mGravity)
      {
      default:
        i21 = i13 + i4;
        i22 = i14 + i4;
        i23 = i15 + i4;
      case 16:
      case 80:
      }
    }
    while (true)
    {
      this.mCurrText.layout(i7, i22, i8, i22 + this.mCurrText.getMeasuredHeight());
      int i24 = Math.min(i2, i7 - this.mScaledTextSpacing - i);
      this.mPrevText.layout(i24, i21, i + i24, i21 + this.mPrevText.getMeasuredHeight());
      int i25 = Math.max(n - i3 - k, i8 + this.mScaledTextSpacing);
      this.mNextText.layout(i25, i23, i25 + k, i23 + this.mNextText.getMeasuredHeight());
      this.mLastKnownPositionOffset = paramFloat;
      this.mUpdatingPositions = false;
      return;
      if ((paramBoolean) || (paramFloat != this.mLastKnownPositionOffset))
        break;
      return;
      int i26 = (i1 - i4 - i5 - i19) / 2;
      i21 = i13 + i26;
      i22 = i14 + i26;
      i23 = i15 + i26;
      continue;
      int i20 = i1 - i5 - i19;
      i21 = i13 + i20;
      i22 = i14 + i20;
      i23 = i15 + i20;
    }
  }

  private class PageListener extends DataSetObserver
    implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener
  {
    private int mScrollState;

    PageListener()
    {
    }

    public void onAdapterChanged(ViewPager paramViewPager, PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
    {
      PagerTitleStrip.this.updateAdapter(paramPagerAdapter1, paramPagerAdapter2);
    }

    public void onChanged()
    {
      PagerTitleStrip.this.updateText(PagerTitleStrip.this.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
      boolean bool = PagerTitleStrip.this.mLastKnownPositionOffset < 0.0F;
      float f = 0.0F;
      if (!bool)
        f = PagerTitleStrip.this.mLastKnownPositionOffset;
      PagerTitleStrip.this.updateTextPositions(PagerTitleStrip.this.mPager.getCurrentItem(), f, true);
    }

    public void onPageScrollStateChanged(int paramInt)
    {
      this.mScrollState = paramInt;
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (paramFloat > 0.5F)
        paramInt1++;
      PagerTitleStrip.this.updateTextPositions(paramInt1, paramFloat, false);
    }

    public void onPageSelected(int paramInt)
    {
      if (this.mScrollState == 0)
      {
        PagerTitleStrip.this.updateText(PagerTitleStrip.this.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
        boolean bool = PagerTitleStrip.this.mLastKnownPositionOffset < 0.0F;
        float f = 0.0F;
        if (!bool)
          f = PagerTitleStrip.this.mLastKnownPositionOffset;
        PagerTitleStrip.this.updateTextPositions(PagerTitleStrip.this.mPager.getCurrentItem(), f, true);
      }
    }
  }

  static abstract interface PagerTitleStripImpl
  {
    public abstract void setSingleLineAllCaps(TextView paramTextView);
  }

  static class PagerTitleStripImplBase
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      paramTextView.setSingleLine();
    }
  }

  static class PagerTitleStripImplIcs
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      PagerTitleStripIcs.setSingleLineAllCaps(paramTextView);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.PagerTitleStrip
 * JD-Core Version:    0.6.0
 */