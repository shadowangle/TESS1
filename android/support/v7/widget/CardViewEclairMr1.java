package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

class CardViewEclairMr1
  implements CardViewImpl
{
  final RectF sCornerRect = new RectF();

  private RoundRectDrawableWithShadow createBackground(Context paramContext, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return new RoundRectDrawableWithShadow(paramContext.getResources(), paramInt, paramFloat1, paramFloat2, paramFloat3);
  }

  private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate paramCardViewDelegate)
  {
    return (RoundRectDrawableWithShadow)paramCardViewDelegate.getCardBackground();
  }

  public float getElevation(CardViewDelegate paramCardViewDelegate)
  {
    return getShadowBackground(paramCardViewDelegate).getShadowSize();
  }

  public float getMaxElevation(CardViewDelegate paramCardViewDelegate)
  {
    return getShadowBackground(paramCardViewDelegate).getMaxShadowSize();
  }

  public float getMinHeight(CardViewDelegate paramCardViewDelegate)
  {
    return getShadowBackground(paramCardViewDelegate).getMinHeight();
  }

  public float getMinWidth(CardViewDelegate paramCardViewDelegate)
  {
    return getShadowBackground(paramCardViewDelegate).getMinWidth();
  }

  public float getRadius(CardViewDelegate paramCardViewDelegate)
  {
    return getShadowBackground(paramCardViewDelegate).getCornerRadius();
  }

  public void initStatic()
  {
    RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper()
    {
      public void drawRoundRect(Canvas paramCanvas, RectF paramRectF, float paramFloat, Paint paramPaint)
      {
        float f1 = paramFloat * 2.0F;
        float f2 = paramRectF.width() - f1 - 1.0F;
        float f3 = paramRectF.height();
        if (paramFloat >= 1.0F)
        {
          float f4 = paramFloat + 0.5F;
          CardViewEclairMr1.this.sCornerRect.set(-f4, -f4, f4, f4);
          int i = paramCanvas.save();
          paramCanvas.translate(f4 + paramRectF.left, f4 + paramRectF.top);
          paramCanvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0F, 90.0F, true, paramPaint);
          paramCanvas.translate(f2, 0.0F);
          paramCanvas.rotate(90.0F);
          paramCanvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0F, 90.0F, true, paramPaint);
          paramCanvas.translate(f3 - f1 - 1.0F, 0.0F);
          paramCanvas.rotate(90.0F);
          paramCanvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0F, 90.0F, true, paramPaint);
          paramCanvas.translate(f2, 0.0F);
          paramCanvas.rotate(90.0F);
          paramCanvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0F, 90.0F, true, paramPaint);
          paramCanvas.restoreToCount(i);
          paramCanvas.drawRect(f4 + paramRectF.left - 1.0F, paramRectF.top, 1.0F + (paramRectF.right - f4), f4 + paramRectF.top, paramPaint);
          paramCanvas.drawRect(f4 + paramRectF.left - 1.0F, 1.0F + (paramRectF.bottom - f4), 1.0F + (paramRectF.right - f4), paramRectF.bottom, paramPaint);
        }
        paramCanvas.drawRect(paramRectF.left, paramRectF.top + Math.max(0.0F, paramFloat - 1.0F), paramRectF.right, 1.0F + (paramRectF.bottom - paramFloat), paramPaint);
      }
    };
  }

  public void initialize(CardViewDelegate paramCardViewDelegate, Context paramContext, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    RoundRectDrawableWithShadow localRoundRectDrawableWithShadow = createBackground(paramContext, paramInt, paramFloat1, paramFloat2, paramFloat3);
    localRoundRectDrawableWithShadow.setAddPaddingForCorners(paramCardViewDelegate.getPreventCornerOverlap());
    paramCardViewDelegate.setCardBackground(localRoundRectDrawableWithShadow);
    updatePadding(paramCardViewDelegate);
  }

  public void onCompatPaddingChanged(CardViewDelegate paramCardViewDelegate)
  {
  }

  public void onPreventCornerOverlapChanged(CardViewDelegate paramCardViewDelegate)
  {
    getShadowBackground(paramCardViewDelegate).setAddPaddingForCorners(paramCardViewDelegate.getPreventCornerOverlap());
    updatePadding(paramCardViewDelegate);
  }

  public void setBackgroundColor(CardViewDelegate paramCardViewDelegate, int paramInt)
  {
    getShadowBackground(paramCardViewDelegate).setColor(paramInt);
  }

  public void setElevation(CardViewDelegate paramCardViewDelegate, float paramFloat)
  {
    getShadowBackground(paramCardViewDelegate).setShadowSize(paramFloat);
  }

  public void setMaxElevation(CardViewDelegate paramCardViewDelegate, float paramFloat)
  {
    getShadowBackground(paramCardViewDelegate).setMaxShadowSize(paramFloat);
    updatePadding(paramCardViewDelegate);
  }

  public void setRadius(CardViewDelegate paramCardViewDelegate, float paramFloat)
  {
    getShadowBackground(paramCardViewDelegate).setCornerRadius(paramFloat);
    updatePadding(paramCardViewDelegate);
  }

  public void updatePadding(CardViewDelegate paramCardViewDelegate)
  {
    Rect localRect = new Rect();
    getShadowBackground(paramCardViewDelegate).getMaxShadowAndCornerPadding(localRect);
    paramCardViewDelegate.setMinWidthHeightInternal((int)Math.ceil(getMinWidth(paramCardViewDelegate)), (int)Math.ceil(getMinHeight(paramCardViewDelegate)));
    paramCardViewDelegate.setShadowPadding(localRect.left, localRect.top, localRect.right, localRect.bottom);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v7.widget.CardViewEclairMr1
 * JD-Core Version:    0.6.0
 */