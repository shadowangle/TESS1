package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

abstract interface CardViewDelegate
{
  public abstract Drawable getCardBackground();

  public abstract View getCardView();

  public abstract boolean getPreventCornerOverlap();

  public abstract boolean getUseCompatPadding();

  public abstract void setCardBackground(Drawable paramDrawable);

  public abstract void setMinWidthHeightInternal(int paramInt1, int paramInt2);

  public abstract void setShadowPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v7.widget.CardViewDelegate
 * JD-Core Version:    0.6.0
 */