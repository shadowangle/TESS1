package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.cardview.R.color;
import android.support.v7.cardview.R.style;
import android.support.v7.cardview.R.styleable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class CardView extends FrameLayout
{
  private static final int[] COLOR_BACKGROUND_ATTR = { 16842801 };
  private static final CardViewImpl IMPL;
  private final CardViewDelegate mCardViewDelegate = new CardViewDelegate()
  {
    private Drawable mCardBackground;

    public Drawable getCardBackground()
    {
      return this.mCardBackground;
    }

    public View getCardView()
    {
      return CardView.this;
    }

    public boolean getPreventCornerOverlap()
    {
      return CardView.this.getPreventCornerOverlap();
    }

    public boolean getUseCompatPadding()
    {
      return CardView.this.getUseCompatPadding();
    }

    public void setCardBackground(Drawable paramDrawable)
    {
      this.mCardBackground = paramDrawable;
      CardView.this.setBackgroundDrawable(paramDrawable);
    }

    public void setMinWidthHeightInternal(int paramInt1, int paramInt2)
    {
      if (paramInt1 > CardView.this.mUserSetMinWidth)
        CardView.this.setMinimumWidth(paramInt1);
      if (paramInt2 > CardView.this.mUserSetMinHeight)
        CardView.this.setMinimumHeight(paramInt2);
    }

    public void setShadowPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      CardView.this.mShadowBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      CardView.this.setPadding(paramInt1 + CardView.this.mContentPadding.left, paramInt2 + CardView.this.mContentPadding.top, paramInt3 + CardView.this.mContentPadding.right, paramInt4 + CardView.this.mContentPadding.bottom);
    }
  };
  private boolean mCompatPadding;
  private final Rect mContentPadding = new Rect();
  private boolean mPreventCornerOverlap;
  private final Rect mShadowBounds = new Rect();
  private int mUserSetMinHeight;
  private int mUserSetMinWidth;

  static
  {
    if (Build.VERSION.SDK_INT >= 21)
      IMPL = new CardViewApi21();
    while (true)
    {
      IMPL.initStatic();
      return;
      if (Build.VERSION.SDK_INT >= 17)
      {
        IMPL = new CardViewJellybeanMr1();
        continue;
      }
      IMPL = new CardViewEclairMr1();
    }
  }

  public CardView(Context paramContext)
  {
    super(paramContext);
    initialize(paramContext, null, 0);
  }

  public CardView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initialize(paramContext, paramAttributeSet, 0);
  }

  public CardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initialize(paramContext, paramAttributeSet, paramInt);
  }

  private void initialize(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    TypedArray localTypedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CardView, paramInt, R.style.CardView);
    int j;
    if (localTypedArray1.hasValue(R.styleable.CardView_cardBackgroundColor))
      j = localTypedArray1.getColor(R.styleable.CardView_cardBackgroundColor, 0);
    while (true)
    {
      float f1 = localTypedArray1.getDimension(R.styleable.CardView_cardCornerRadius, 0.0F);
      float f2 = localTypedArray1.getDimension(R.styleable.CardView_cardElevation, 0.0F);
      float f3 = localTypedArray1.getDimension(R.styleable.CardView_cardMaxElevation, 0.0F);
      this.mCompatPadding = localTypedArray1.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
      this.mPreventCornerOverlap = localTypedArray1.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
      int k = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
      this.mContentPadding.left = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft, k);
      this.mContentPadding.top = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop, k);
      this.mContentPadding.right = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight, k);
      this.mContentPadding.bottom = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom, k);
      if (f2 > f3)
        f3 = f2;
      this.mUserSetMinWidth = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
      this.mUserSetMinHeight = localTypedArray1.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
      localTypedArray1.recycle();
      IMPL.initialize(this.mCardViewDelegate, paramContext, j, f1, f2, f3);
      return;
      TypedArray localTypedArray2 = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
      int i = localTypedArray2.getColor(0, 0);
      localTypedArray2.recycle();
      float[] arrayOfFloat = new float[3];
      Color.colorToHSV(i, arrayOfFloat);
      if (arrayOfFloat[2] > 0.5F)
      {
        j = getResources().getColor(R.color.cardview_light_background);
        continue;
      }
      j = getResources().getColor(R.color.cardview_dark_background);
    }
  }

  public float getCardElevation()
  {
    return IMPL.getElevation(this.mCardViewDelegate);
  }

  public int getContentPaddingBottom()
  {
    return this.mContentPadding.bottom;
  }

  public int getContentPaddingLeft()
  {
    return this.mContentPadding.left;
  }

  public int getContentPaddingRight()
  {
    return this.mContentPadding.right;
  }

  public int getContentPaddingTop()
  {
    return this.mContentPadding.top;
  }

  public float getMaxCardElevation()
  {
    return IMPL.getMaxElevation(this.mCardViewDelegate);
  }

  public boolean getPreventCornerOverlap()
  {
    return this.mPreventCornerOverlap;
  }

  public float getRadius()
  {
    return IMPL.getRadius(this.mCardViewDelegate);
  }

  public boolean getUseCompatPadding()
  {
    return this.mCompatPadding;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (!(IMPL instanceof CardViewApi21))
    {
      int i = View.MeasureSpec.getMode(paramInt1);
      int j;
      switch (i)
      {
      default:
        j = View.MeasureSpec.getMode(paramInt2);
        switch (j)
        {
        default:
        case 1073741824:
        case -2147483648:
        }
      case 1073741824:
      case -2147483648:
      }
      while (true)
      {
        super.onMeasure(paramInt1, paramInt2);
        return;
        paramInt1 = View.MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil(IMPL.getMinWidth(this.mCardViewDelegate)), View.MeasureSpec.getSize(paramInt1)), i);
        break;
        paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil(IMPL.getMinHeight(this.mCardViewDelegate)), View.MeasureSpec.getSize(paramInt2)), j);
      }
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public void setCardBackgroundColor(int paramInt)
  {
    IMPL.setBackgroundColor(this.mCardViewDelegate, paramInt);
  }

  public void setCardElevation(float paramFloat)
  {
    IMPL.setElevation(this.mCardViewDelegate, paramFloat);
  }

  public void setContentPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mContentPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    IMPL.updatePadding(this.mCardViewDelegate);
  }

  public void setMaxCardElevation(float paramFloat)
  {
    IMPL.setMaxElevation(this.mCardViewDelegate, paramFloat);
  }

  public void setMinimumHeight(int paramInt)
  {
    this.mUserSetMinHeight = paramInt;
    super.setMinimumHeight(paramInt);
  }

  public void setMinimumWidth(int paramInt)
  {
    this.mUserSetMinWidth = paramInt;
    super.setMinimumWidth(paramInt);
  }

  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
  }

  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
  }

  public void setPreventCornerOverlap(boolean paramBoolean)
  {
    if (paramBoolean != this.mPreventCornerOverlap)
    {
      this.mPreventCornerOverlap = paramBoolean;
      IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
    }
  }

  public void setRadius(float paramFloat)
  {
    IMPL.setRadius(this.mCardViewDelegate, paramFloat);
  }

  public void setUseCompatPadding(boolean paramBoolean)
  {
    if (this.mCompatPadding != paramBoolean)
    {
      this.mCompatPadding = paramBoolean;
      IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v7.widget.CardView
 * JD-Core Version:    0.6.0
 */