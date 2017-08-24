package android.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface VisibleForTesting
{
  public static final int NONE = 5;
  public static final int PACKAGE_PRIVATE = 3;
  public static final int PRIVATE = 2;
  public static final int PROTECTED = 4;

  public abstract int otherwise();
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.annotation.VisibleForTesting
 * JD-Core Version:    0.6.0
 */