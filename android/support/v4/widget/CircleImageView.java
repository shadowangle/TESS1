package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

class CircleImageView extends ImageView
{
  private static final int FILL_SHADOW_COLOR = 1023410176;
  private static final int KEY_SHADOW_COLOR = 503316480;
  private static final int SHADOW_ELEVATION = 4;
  private static final float SHADOW_RADIUS = 3.5F;
  private static final float X_OFFSET = 0.0F;
  private static final float Y_OFFSET = 1.75F;
  private Animation.AnimationListener mListener;
  int mShadowRadius;

  CircleImageView(Context paramContext, int paramInt)
  {
    super(paramContext);
    float f = getContext().getResources().getDisplayMetrics().density;
    int i = (int)(1.75F * f);
    int j = (int)(0.0F * f);
    this.mShadowRadius = (int)(3.5F * f);
    ShapeDrawable localShapeDrawable;
    if (elevationSupported())
    {
      localShapeDrawable = new ShapeDrawable(new OvalShape());
      ViewCompat.setElevation(this, f * 4.0F);
    }
    while (true)
    {
      localShapeDrawable.getPaint().setColor(paramInt);
      ViewCompat.setBackground(this, localShapeDrawable);
      return;
      localShapeDrawable = new ShapeDrawable(new OvalShadow(this.mShadowRadius));
      ViewCompat.setLayerType(this, 1, localShapeDrawable.getPaint());
      localShapeDrawable.getPaint().setShadowLayer(this.mShadowRadius, j, i, 503316480);
      int k = this.mShadowRadius;
      setPadding(k, k, k, k);
    }
  }

  private boolean elevationSupported()
  {
    return Build.VERSION.SDK_INT >= 21;
  }

  public void onAnimationEnd()
  {
    super.onAnimationEnd();
    if (this.mListener != null)
      this.mListener.onAnimationEnd(getAnimation());
  }

  public void onAnimationStart()
  {
    super.onAnimationStart();
    if (this.mListener != null)
      this.mListener.onAnimationStart(getAnimation());
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (!elevationSupported())
      setMeasuredDimension(getMeasuredWidth() + 2 * this.mShadowRadius, getMeasuredHeight() + 2 * this.mShadowRadius);
  }

  public void setAnimationListener(Animation.AnimationListener paramAnimationListener)
  {
    this.mListener = paramAnimationListener;
  }

  public void setBackgroundColor(int paramInt)
  {
    if ((getBackground() instanceof ShapeDrawable))
      ((ShapeDrawable)getBackground()).getPaint().setColor(paramInt);
  }

  public void setBackgroundColorRes(int paramInt)
  {
    setBackgroundColor(ContextCompat.getColor(getContext(), paramInt));
  }

  private class OvalShadow extends OvalShape
  {
    private RadialGradient mRadialGradient;
    private Paint mShadowPaint = new Paint();

    OvalShadow(int arg2)
    {
      int i;
      CircleImageView.this.mShadowRadius = i;
      updateRadialGradient((int)rect().width());
    }

    private void updateRadialGradient(int paramInt)
    {
      float f1 = paramInt / 2;
      float f2 = paramInt / 2;
      float f3 = CircleImageView.this.mShadowRadius;
      Shader.TileMode localTileMode = Shader.TileMode.CLAMP;
      this.mRadialGradient = new RadialGradient(f1, f2, f3, new int[] { 1023410176, 0 }, null, localTileMode);
      this.mShadowPaint.setShader(this.mRadialGradient);
    }

    public void draw(Canvas paramCanvas, Paint paramPaint)
    {
      int i = CircleImageView.this.getWidth();
      int j = CircleImageView.this.getHeight();
      paramCanvas.drawCircle(i / 2, j / 2, i / 2, this.mShadowPaint);
      paramCanvas.drawCircle(i / 2, j / 2, i / 2 - CircleImageView.this.mShadowRadius, paramPaint);
    }

    protected void onResize(float paramFloat1, float paramFloat2)
    {
      super.onResize(paramFloat1, paramFloat2);
      updateRadialGradient((int)paramFloat1);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.CircleImageView
 * JD-Core Version:    0.6.0
 */