package android.support.v4.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter
{
  private static final int DEFAULT_FLAGS = 2;
  private static final BidiFormatter DEFAULT_LTR_INSTANCE;
  private static final BidiFormatter DEFAULT_RTL_INSTANCE;
  private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
  private static final int DIR_LTR = -1;
  private static final int DIR_RTL = 1;
  private static final int DIR_UNKNOWN = 0;
  private static final String EMPTY_STRING = "";
  private static final int FLAG_STEREO_RESET = 2;
  private static final char LRE = '‪';
  private static final char LRM = '‎';
  private static final String LRM_STRING = Character.toString('‎');
  private static final char PDF = '‬';
  private static final char RLE = '‫';
  private static final char RLM = '‏';
  private static final String RLM_STRING = Character.toString('‏');
  private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
  private final int mFlags;
  private final boolean mIsRtlContext;

  static
  {
    DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
  }

  private BidiFormatter(boolean paramBoolean, int paramInt, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat)
  {
    this.mIsRtlContext = paramBoolean;
    this.mFlags = paramInt;
    this.mDefaultTextDirectionHeuristicCompat = paramTextDirectionHeuristicCompat;
  }

  private static int getEntryDir(CharSequence paramCharSequence)
  {
    return new DirectionalityEstimator(paramCharSequence, false).getEntryDir();
  }

  private static int getExitDir(CharSequence paramCharSequence)
  {
    return new DirectionalityEstimator(paramCharSequence, false).getExitDir();
  }

  public static BidiFormatter getInstance()
  {
    return new Builder().build();
  }

  public static BidiFormatter getInstance(Locale paramLocale)
  {
    return new Builder(paramLocale).build();
  }

  public static BidiFormatter getInstance(boolean paramBoolean)
  {
    return new Builder(paramBoolean).build();
  }

  private static boolean isRtlLocale(Locale paramLocale)
  {
    return TextUtilsCompat.getLayoutDirectionFromLocale(paramLocale) == 1;
  }

  private String markAfter(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat)
  {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    if ((!this.mIsRtlContext) && ((bool) || (getExitDir(paramCharSequence) == 1)))
      return LRM_STRING;
    if ((this.mIsRtlContext) && ((!bool) || (getExitDir(paramCharSequence) == -1)))
      return RLM_STRING;
    return "";
  }

  private String markBefore(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat)
  {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    if ((!this.mIsRtlContext) && ((bool) || (getEntryDir(paramCharSequence) == 1)))
      return LRM_STRING;
    if ((this.mIsRtlContext) && ((!bool) || (getEntryDir(paramCharSequence) == -1)))
      return RLM_STRING;
    return "";
  }

  public boolean getStereoReset()
  {
    return (0x2 & this.mFlags) != 0;
  }

  public boolean isRtl(CharSequence paramCharSequence)
  {
    return this.mDefaultTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
  }

  public boolean isRtl(String paramString)
  {
    return isRtl(paramString);
  }

  public boolean isRtlContext()
  {
    return this.mIsRtlContext;
  }

  public CharSequence unicodeWrap(CharSequence paramCharSequence)
  {
    return unicodeWrap(paramCharSequence, this.mDefaultTextDirectionHeuristicCompat, true);
  }

  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat)
  {
    return unicodeWrap(paramCharSequence, paramTextDirectionHeuristicCompat, true);
  }

  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean)
  {
    if (paramCharSequence == null)
      return null;
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    TextDirectionHeuristicCompat localTextDirectionHeuristicCompat2;
    if ((getStereoReset()) && (paramBoolean))
    {
      if (bool)
      {
        localTextDirectionHeuristicCompat2 = TextDirectionHeuristicsCompat.RTL;
        localSpannableStringBuilder.append(markBefore(paramCharSequence, localTextDirectionHeuristicCompat2));
      }
    }
    else
    {
      if (bool == this.mIsRtlContext)
        break label154;
      if (!bool)
        break label146;
      int i = 8235;
      label84: localSpannableStringBuilder.append(i);
      localSpannableStringBuilder.append(paramCharSequence);
      localSpannableStringBuilder.append('‬');
      label108: if (!paramBoolean)
        break label172;
      if (!bool)
        break label164;
    }
    label146: label154: label164: for (TextDirectionHeuristicCompat localTextDirectionHeuristicCompat1 = TextDirectionHeuristicsCompat.RTL; ; localTextDirectionHeuristicCompat1 = TextDirectionHeuristicsCompat.LTR)
    {
      localSpannableStringBuilder.append(markAfter(paramCharSequence, localTextDirectionHeuristicCompat1));
      return localSpannableStringBuilder;
      localTextDirectionHeuristicCompat2 = TextDirectionHeuristicsCompat.LTR;
      break;
      int j = 8234;
      break label84;
      localSpannableStringBuilder.append(paramCharSequence);
      break label108;
    }
    label172: return localSpannableStringBuilder;
  }

  public CharSequence unicodeWrap(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return unicodeWrap(paramCharSequence, this.mDefaultTextDirectionHeuristicCompat, paramBoolean);
  }

  public String unicodeWrap(String paramString)
  {
    return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, true);
  }

  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat)
  {
    return unicodeWrap(paramString, paramTextDirectionHeuristicCompat, true);
  }

  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean)
  {
    if (paramString == null)
      return null;
    return unicodeWrap(paramString, paramTextDirectionHeuristicCompat, paramBoolean).toString();
  }

  public String unicodeWrap(String paramString, boolean paramBoolean)
  {
    return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, paramBoolean);
  }

  public static final class Builder
  {
    private int mFlags;
    private boolean mIsRtlContext;
    private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

    public Builder()
    {
      initialize(BidiFormatter.access$000(Locale.getDefault()));
    }

    public Builder(Locale paramLocale)
    {
      initialize(BidiFormatter.access$000(paramLocale));
    }

    public Builder(boolean paramBoolean)
    {
      initialize(paramBoolean);
    }

    private static BidiFormatter getDefaultInstanceFromContext(boolean paramBoolean)
    {
      if (paramBoolean)
        return BidiFormatter.DEFAULT_RTL_INSTANCE;
      return BidiFormatter.DEFAULT_LTR_INSTANCE;
    }

    private void initialize(boolean paramBoolean)
    {
      this.mIsRtlContext = paramBoolean;
      this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
      this.mFlags = 2;
    }

    public BidiFormatter build()
    {
      if ((this.mFlags == 2) && (this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC))
        return getDefaultInstanceFromContext(this.mIsRtlContext);
      return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat, null);
    }

    public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat)
    {
      this.mTextDirectionHeuristicCompat = paramTextDirectionHeuristicCompat;
      return this;
    }

    public Builder stereoReset(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        this.mFlags = (0x2 | this.mFlags);
        return this;
      }
      this.mFlags = (0xFFFFFFFD & this.mFlags);
      return this;
    }
  }

  private static class DirectionalityEstimator
  {
    private static final byte[] DIR_TYPE_CACHE = new byte[1792];
    private static final int DIR_TYPE_CACHE_SIZE = 1792;
    private int charIndex;
    private final boolean isHtml;
    private char lastChar;
    private final int length;
    private final CharSequence text;

    static
    {
      for (int i = 0; i < 1792; i++)
        DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
    }

    DirectionalityEstimator(CharSequence paramCharSequence, boolean paramBoolean)
    {
      this.text = paramCharSequence;
      this.isHtml = paramBoolean;
      this.length = paramCharSequence.length();
    }

    private static byte getCachedDirectionality(char paramChar)
    {
      if (paramChar < '܀')
        return DIR_TYPE_CACHE[paramChar];
      return Character.getDirectionality(paramChar);
    }

    private byte skipEntityBackward()
    {
      int i = this.charIndex;
      do
      {
        if (this.charIndex <= 0)
          break;
        CharSequence localCharSequence = this.text;
        int j = -1 + this.charIndex;
        this.charIndex = j;
        this.lastChar = localCharSequence.charAt(j);
        if (this.lastChar == '&')
          return 12;
      }
      while (this.lastChar != ';');
      this.charIndex = i;
      this.lastChar = (char)59;
      return 13;
    }

    private byte skipEntityForward()
    {
      char c;
      do
      {
        if (this.charIndex >= this.length)
          break;
        CharSequence localCharSequence = this.text;
        int i = this.charIndex;
        this.charIndex = (i + 1);
        c = localCharSequence.charAt(i);
        this.lastChar = c;
      }
      while (c != ';');
      return 12;
    }

    private byte skipTagBackward()
    {
      int i = this.charIndex;
      label152: 
      while (true)
      {
        if (this.charIndex > 0)
        {
          CharSequence localCharSequence1 = this.text;
          int j = -1 + this.charIndex;
          this.charIndex = j;
          this.lastChar = localCharSequence1.charAt(j);
          if (this.lastChar == '<')
            return 12;
          if (this.lastChar != '>');
        }
        else
        {
          this.charIndex = i;
          this.lastChar = (char)62;
          return 13;
        }
        if ((this.lastChar != '"') && (this.lastChar != '\''))
          continue;
        int k = this.lastChar;
        while (true)
        {
          if (this.charIndex <= 0)
            break label152;
          CharSequence localCharSequence2 = this.text;
          int m = -1 + this.charIndex;
          this.charIndex = m;
          char c = localCharSequence2.charAt(m);
          this.lastChar = c;
          if (c == k)
            break;
        }
      }
    }

    private byte skipTagForward()
    {
      int i = this.charIndex;
      label136: 
      while (this.charIndex < this.length)
      {
        CharSequence localCharSequence1 = this.text;
        int j = this.charIndex;
        this.charIndex = (j + 1);
        this.lastChar = localCharSequence1.charAt(j);
        if (this.lastChar == '>')
          return 12;
        if ((this.lastChar != '"') && (this.lastChar != '\''))
          continue;
        int k = this.lastChar;
        while (true)
        {
          if (this.charIndex >= this.length)
            break label136;
          CharSequence localCharSequence2 = this.text;
          int m = this.charIndex;
          this.charIndex = (m + 1);
          char c = localCharSequence2.charAt(m);
          this.lastChar = c;
          if (c == k)
            break;
        }
      }
      this.charIndex = i;
      this.lastChar = (char)60;
      return 13;
    }

    byte dirTypeBackward()
    {
      this.lastChar = this.text.charAt(-1 + this.charIndex);
      int i;
      if (Character.isLowSurrogate(this.lastChar))
      {
        int j = Character.codePointBefore(this.text, this.charIndex);
        this.charIndex -= Character.charCount(j);
        i = Character.getDirectionality(j);
      }
      do
      {
        do
        {
          return i;
          this.charIndex = (-1 + this.charIndex);
          i = getCachedDirectionality(this.lastChar);
        }
        while (!this.isHtml);
        if (this.lastChar == '>')
          return skipTagBackward();
      }
      while (this.lastChar != ';');
      return skipEntityBackward();
    }

    byte dirTypeForward()
    {
      this.lastChar = this.text.charAt(this.charIndex);
      int i;
      if (Character.isHighSurrogate(this.lastChar))
      {
        int j = Character.codePointAt(this.text, this.charIndex);
        this.charIndex += Character.charCount(j);
        i = Character.getDirectionality(j);
      }
      do
      {
        do
        {
          return i;
          this.charIndex = (1 + this.charIndex);
          i = getCachedDirectionality(this.lastChar);
        }
        while (!this.isHtml);
        if (this.lastChar == '<')
          return skipTagForward();
      }
      while (this.lastChar != '&');
      return skipEntityForward();
    }

    int getEntryDir()
    {
      this.charIndex = 0;
      int i = 0;
      int j = 0;
      int k = 0;
      while ((this.charIndex < this.length) && (i == 0))
        switch (dirTypeForward())
        {
        case 9:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 10:
        case 11:
        case 12:
        case 13:
        default:
          i = k;
          break;
        case 14:
        case 15:
          k++;
          j = -1;
          break;
        case 16:
        case 17:
          k++;
          j = 1;
          break;
        case 18:
          k--;
          j = 0;
          break;
        case 0:
          if (k == 0)
            return -1;
          i = k;
          break;
        case 1:
        case 2:
          if (k == 0)
            return 1;
          i = k;
        }
      if (i == 0)
        return 0;
      if (j == 0)
      {
        while (true)
        {
          if (this.charIndex <= 0)
            break label261;
          switch (dirTypeBackward())
          {
          default:
            break;
          case 14:
          case 15:
            if (i == k)
              break;
            k--;
            break;
          case 16:
          case 17:
            if (i == k)
              return 1;
            k--;
            break;
          case 18:
            k++;
          }
        }
        label261: return 0;
      }
      return j;
    }

    int getExitDir()
    {
      this.charIndex = this.length;
      int i = 0;
      int j = 0;
      while (true)
      {
        int k = this.charIndex;
        int m = 0;
        if (k > 0);
        switch (dirTypeBackward())
        {
        case 9:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 10:
        case 11:
        case 12:
        case 13:
        default:
          if (i != 0)
            continue;
          i = j;
          break;
        case 0:
          if (j == 0)
          {
            m = -1;
            return m;
          }
          if (i != 0)
            continue;
          i = j;
          break;
        case 14:
        case 15:
          if (i == j)
            break label193;
          j--;
          break;
        case 1:
        case 2:
          if (j == 0)
            return 1;
          if (i != 0)
            continue;
          i = j;
          break;
        case 16:
        case 17:
          if (i == j)
            return 1;
          j--;
          break;
        case 18:
          j++;
        }
      }
      label193: return -1;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.text.BidiFormatter
 * JD-Core Version:    0.6.0
 */