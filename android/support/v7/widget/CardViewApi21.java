package android.support.v7.widget;

import android.content.Context;
import android.view.View;

class CardViewApi21
  implements CardViewImpl
{
  private RoundRectDrawable getCardBackground(CardViewDelegate paramCardViewDelegate)
  {
    return (RoundRectDrawable)paramCardViewDelegate.getCardBackground();
  }

  public float getElevation(CardViewDelegate paramCardViewDelegate)
  {
    return paramCardViewDelegate.getCardView().getElevation();
  }

  public float getMaxElevation(CardViewDelegate paramCardViewDelegate)
  {
    return getCardBackground(paramCardViewDelegate).getPadding();
  }

  public float getMinHeight(CardViewDelegate paramCardViewDelegate)
  {
    return 2.0F * getRadius(paramCardViewDelegate);
  }

  public float getMinWidth(CardViewDelegate paramCardViewDelegate)
  {
    return 2.0F * getRadius(paramCardViewDelegate);
  }

  public float getRadius(CardViewDelegate paramCardViewDelegate)
  {
    return getCardBackground(paramCardViewDelegate).getRadius();
  }

  public void initStatic()
  {
  }

  public void initialize(CardViewDelegate paramCardViewDelegate, Context paramContext, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    paramCardViewDelegate.setCardBackground(new RoundRectDrawable(paramInt, paramFloat1));
    View localView = paramCardViewDelegate.getCardView();
    localView.setClipToOutline(true);
    localView.setElevation(paramFloat2);
    setMaxElevation(paramCardViewDelegate, paramFloat3);
  }

  public void onCompatPaddingChanged(CardViewDelegate paramCardViewDelegate)
  {
    setMaxElevation(paramCardViewDelegate, getMaxElevation(paramCardViewDelegate));
  }

  public void onPreventCornerOverlapChanged(CardViewDelegate paramCardViewDelegate)
  {
    setMaxElevation(paramCardViewDelegate, getMaxElevation(paramCardViewDelegate));
  }

  public void setBackgroundColor(CardViewDelegate paramCardViewDelegate, int paramInt)
  {
    getCardBackground(paramCardViewDelegate).setColor(paramInt);
  }

  public void setElevation(CardViewDelegate paramCardViewDelegate, float paramFloat)
  {
    paramCardViewDelegate.getCardView().setElevation(paramFloat);
  }

  public void setMaxElevation(CardViewDelegate paramCardViewDelegate, float paramFloat)
  {
    getCardBackground(paramCardViewDelegate).setPadding(paramFloat, paramCardViewDelegate.getUseCompatPadding(), paramCardViewDelegate.getPreventCornerOverlap());
    updatePadding(paramCardViewDelegate);
  }

  public void setRadius(CardViewDelegate paramCardViewDelegate, float paramFloat)
  {
    getCardBackground(paramCardViewDelegate).setRadius(paramFloat);
  }

  public void updatePadding(CardViewDelegate paramCardViewDelegate)
  {
    if (!paramCardViewDelegate.getUseCompatPadding())
    {
      paramCardViewDelegate.setShadowPadding(0, 0, 0, 0);
      return;
    }
    float f1 = getMaxElevation(paramCardViewDelegate);
    float f2 = getRadius(paramCardViewDelegate);
    int i = (int)Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(f1, f2, paramCardViewDelegate.getPreventCornerOverlap()));
    int j = (int)Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(f1, f2, paramCardViewDelegate.getPreventCornerOverlap()));
    paramCardViewDelegate.setShadowPadding(i, j, i, j);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v7.widget.CardViewApi21
 * JD-Core Version:    0.6.0
 */