package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@TargetApi(9)
@RequiresApi(9)
class DrawableCompatBase
{
  public static void inflate(Drawable paramDrawable, Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws IOException, XmlPullParserException
  {
    paramDrawable.inflate(paramResources, paramXmlPullParser, paramAttributeSet);
  }

  public static void setTint(Drawable paramDrawable, int paramInt)
  {
    if ((paramDrawable instanceof TintAwareDrawable))
      ((TintAwareDrawable)paramDrawable).setTint(paramInt);
  }

  public static void setTintList(Drawable paramDrawable, ColorStateList paramColorStateList)
  {
    if ((paramDrawable instanceof TintAwareDrawable))
      ((TintAwareDrawable)paramDrawable).setTintList(paramColorStateList);
  }

  public static void setTintMode(Drawable paramDrawable, PorterDuff.Mode paramMode)
  {
    if ((paramDrawable instanceof TintAwareDrawable))
      ((TintAwareDrawable)paramDrawable).setTintMode(paramMode);
  }

  public static Drawable wrapForTinting(Drawable paramDrawable)
  {
    if (!(paramDrawable instanceof TintAwareDrawable))
      paramDrawable = new DrawableWrapperGingerbread(paramDrawable);
    return paramDrawable;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.graphics.drawable.DrawableCompatBase
 * JD-Core Version:    0.6.0
 */