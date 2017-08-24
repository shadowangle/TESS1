package android.support.v7.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class CardViewJellybeanMr1 extends CardViewEclairMr1
{
  public void initStatic()
  {
    RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper()
    {
      public void drawRoundRect(Canvas paramCanvas, RectF paramRectF, float paramFloat, Paint paramPaint)
      {
        paramCanvas.drawRoundRect(paramRectF, paramFloat, paramFloat, paramPaint);
      }
    };
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v7.widget.CardViewJellybeanMr1
 * JD-Core Version:    0.6.0
 */