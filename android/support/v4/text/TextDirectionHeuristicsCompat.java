package android.support.v4.text;

import java.nio.CharBuffer;
import java.util.Locale;

public final class TextDirectionHeuristicsCompat
{
  public static final TextDirectionHeuristicCompat ANYRTL_LTR;
  public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
  public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
  public static final TextDirectionHeuristicCompat LOCALE;
  public static final TextDirectionHeuristicCompat LTR = new TextDirectionHeuristicInternal(null, false);
  public static final TextDirectionHeuristicCompat RTL = new TextDirectionHeuristicInternal(null, true);
  private static final int STATE_FALSE = 1;
  private static final int STATE_TRUE = 0;
  private static final int STATE_UNKNOWN = 2;

  static
  {
    FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false);
    FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true);
    ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false);
    LOCALE = TextDirectionHeuristicLocale.INSTANCE;
  }

  static int isRtlText(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 2;
    case 0:
      return 1;
    case 1:
    case 2:
    }
    return 0;
  }

  static int isRtlTextOrFormat(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 2;
    case 0:
    case 14:
    case 15:
      return 1;
    case 1:
    case 2:
    case 16:
    case 17:
    }
    return 0;
  }

  private static class AnyStrong
    implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm
  {
    public static final AnyStrong INSTANCE_LTR;
    public static final AnyStrong INSTANCE_RTL = new AnyStrong(true);
    private final boolean mLookForRtl;

    static
    {
      INSTANCE_LTR = new AnyStrong(false);
    }

    private AnyStrong(boolean paramBoolean)
    {
      this.mLookForRtl = paramBoolean;
    }

    public int checkRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      int i = 1;
      int j = paramInt1;
      int k = 0;
      while (true)
        if (j < paramInt1 + paramInt2)
          switch (TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(paramCharSequence.charAt(j))))
          {
          default:
            j++;
            break;
          case 0:
            if (this.mLookForRtl)
              i = 0;
          case 1:
          }
      do
      {
        do
        {
          return i;
          k = i;
          break;
        }
        while (!this.mLookForRtl);
        k = i;
        break;
        if (k == 0)
          break label106;
      }
      while (this.mLookForRtl);
      return 0;
      label106: return 2;
    }
  }

  private static class FirstStrong
    implements TextDirectionHeuristicsCompat.TextDirectionAlgorithm
  {
    public static final FirstStrong INSTANCE = new FirstStrong();

    public int checkRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      int i = paramInt1;
      int j = 2;
      while ((i < paramInt1 + paramInt2) && (j == 2))
      {
        j = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(paramCharSequence.charAt(i)));
        i++;
      }
      return j;
    }
  }

  private static abstract interface TextDirectionAlgorithm
  {
    public abstract int checkRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2);
  }

  private static abstract class TextDirectionHeuristicImpl
    implements TextDirectionHeuristicCompat
  {
    private final TextDirectionHeuristicsCompat.TextDirectionAlgorithm mAlgorithm;

    public TextDirectionHeuristicImpl(TextDirectionHeuristicsCompat.TextDirectionAlgorithm paramTextDirectionAlgorithm)
    {
      this.mAlgorithm = paramTextDirectionAlgorithm;
    }

    private boolean doCheck(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      switch (this.mAlgorithm.checkRtl(paramCharSequence, paramInt1, paramInt2))
      {
      default:
        return defaultIsRtl();
      case 0:
        return true;
      case 1:
      }
      return false;
    }

    protected abstract boolean defaultIsRtl();

    public boolean isRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      if ((paramCharSequence == null) || (paramInt1 < 0) || (paramInt2 < 0) || (paramCharSequence.length() - paramInt2 < paramInt1))
        throw new IllegalArgumentException();
      if (this.mAlgorithm == null)
        return defaultIsRtl();
      return doCheck(paramCharSequence, paramInt1, paramInt2);
    }

    public boolean isRtl(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      return isRtl(CharBuffer.wrap(paramArrayOfChar), paramInt1, paramInt2);
    }
  }

  private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
  {
    private final boolean mDefaultIsRtl;

    TextDirectionHeuristicInternal(TextDirectionHeuristicsCompat.TextDirectionAlgorithm paramTextDirectionAlgorithm, boolean paramBoolean)
    {
      super();
      this.mDefaultIsRtl = paramBoolean;
    }

    protected boolean defaultIsRtl()
    {
      return this.mDefaultIsRtl;
    }
  }

  private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
  {
    public static final TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicLocale();

    public TextDirectionHeuristicLocale()
    {
      super();
    }

    protected boolean defaultIsRtl()
    {
      return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.text.TextDirectionHeuristicsCompat
 * JD-Core Version:    0.6.0
 */