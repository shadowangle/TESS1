package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.widget.EdgeEffect;

@TargetApi(14)
@RequiresApi(14)
class EdgeEffectCompatIcs
{
  public static boolean draw(Object paramObject, Canvas paramCanvas)
  {
    return ((EdgeEffect)paramObject).draw(paramCanvas);
  }

  public static void finish(Object paramObject)
  {
    ((EdgeEffect)paramObject).finish();
  }

  public static boolean isFinished(Object paramObject)
  {
    return ((EdgeEffect)paramObject).isFinished();
  }

  public static Object newEdgeEffect(Context paramContext)
  {
    return new EdgeEffect(paramContext);
  }

  public static boolean onAbsorb(Object paramObject, int paramInt)
  {
    ((EdgeEffect)paramObject).onAbsorb(paramInt);
    return true;
  }

  public static boolean onPull(Object paramObject, float paramFloat)
  {
    ((EdgeEffect)paramObject).onPull(paramFloat);
    return true;
  }

  public static boolean onRelease(Object paramObject)
  {
    EdgeEffect localEdgeEffect = (EdgeEffect)paramObject;
    localEdgeEffect.onRelease();
    return localEdgeEffect.isFinished();
  }

  public static void setSize(Object paramObject, int paramInt1, int paramInt2)
  {
    ((EdgeEffect)paramObject).setSize(paramInt1, paramInt2);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.EdgeEffectCompatIcs
 * JD-Core Version:    0.6.0
 */