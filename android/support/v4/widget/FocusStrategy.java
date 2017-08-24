package android.support.v4.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy
{
  private static boolean beamBeats(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2, @NonNull Rect paramRect3)
  {
    boolean bool = beamsOverlap(paramInt, paramRect1, paramRect2);
    if ((beamsOverlap(paramInt, paramRect1, paramRect3)) || (!bool));
    do
      return false;
    while ((isToDirectionOf(paramInt, paramRect1, paramRect3)) && (paramInt != 17) && (paramInt != 66) && (majorAxisDistance(paramInt, paramRect1, paramRect2) >= majorAxisDistanceToFarEdge(paramInt, paramRect1, paramRect3)));
    return true;
  }

  private static boolean beamsOverlap(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
    case 66:
      if ((paramRect2.bottom < paramRect1.top) || (paramRect2.top > paramRect1.bottom))
        break;
    case 33:
    case 130:
    }
    do
    {
      return true;
      return false;
    }
    while ((paramRect2.right >= paramRect1.left) && (paramRect2.left <= paramRect1.right));
    return false;
  }

  public static <L, T> T findNextFocusInAbsoluteDirection(@NonNull L paramL, @NonNull CollectionAdapter<L, T> paramCollectionAdapter, @NonNull BoundsAdapter<T> paramBoundsAdapter, @Nullable T paramT, @NonNull Rect paramRect, int paramInt)
  {
    Rect localRect1 = new Rect(paramRect);
    Object localObject1;
    Rect localRect2;
    int j;
    label103: Object localObject2;
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
      localRect1.offset(1 + paramRect.width(), 0);
      localObject1 = null;
      int i = paramCollectionAdapter.size(paramL);
      localRect2 = new Rect();
      j = 0;
      if (j >= i)
        break label225;
      localObject2 = paramCollectionAdapter.get(paramL, j);
      if (localObject2 != paramT)
        break;
      label127: localObject2 = localObject1;
    case 66:
    case 33:
    case 130:
    }
    while (true)
    {
      j++;
      localObject1 = localObject2;
      break label103;
      localRect1.offset(-(1 + paramRect.width()), 0);
      break;
      localRect1.offset(0, 1 + paramRect.height());
      break;
      localRect1.offset(0, -(1 + paramRect.height()));
      break;
      paramBoundsAdapter.obtainBounds(localObject2, localRect2);
      if (!isBetterCandidate(paramInt, paramRect, localRect2, localRect1))
        break label127;
      localRect1.set(localRect2);
    }
    label225: return localObject1;
  }

  public static <L, T> T findNextFocusInRelativeDirection(@NonNull L paramL, @NonNull CollectionAdapter<L, T> paramCollectionAdapter, @NonNull BoundsAdapter<T> paramBoundsAdapter, @Nullable T paramT, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = paramCollectionAdapter.size(paramL);
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++)
      localArrayList.add(paramCollectionAdapter.get(paramL, j));
    Collections.sort(localArrayList, new SequentialComparator(paramBoolean1, paramBoundsAdapter));
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
    case 2:
      return getNextFocusable(paramT, localArrayList, paramBoolean2);
    case 1:
    }
    return getPreviousFocusable(paramT, localArrayList, paramBoolean2);
  }

  private static <T> T getNextFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean)
  {
    int i = paramArrayList.size();
    if (paramT == null);
    for (int j = -1; ; j = paramArrayList.lastIndexOf(paramT))
    {
      int k = j + 1;
      if (k >= i)
        break;
      return paramArrayList.get(k);
    }
    if ((paramBoolean) && (i > 0))
      return paramArrayList.get(0);
    return null;
  }

  private static <T> T getPreviousFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean)
  {
    int i = paramArrayList.size();
    if (paramT == null);
    for (int j = i; ; j = paramArrayList.indexOf(paramT))
    {
      int k = j - 1;
      if (k < 0)
        break;
      return paramArrayList.get(k);
    }
    if ((paramBoolean) && (i > 0))
      return paramArrayList.get(i - 1);
    return null;
  }

  private static int getWeightedDistanceFor(int paramInt1, int paramInt2)
  {
    return paramInt1 * (paramInt1 * 13) + paramInt2 * paramInt2;
  }

  private static boolean isBetterCandidate(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2, @NonNull Rect paramRect3)
  {
    if (!isCandidate(paramRect1, paramRect2, paramInt));
    do
      return false;
    while ((isCandidate(paramRect1, paramRect3, paramInt)) && (!beamBeats(paramInt, paramRect1, paramRect2, paramRect3)) && ((beamBeats(paramInt, paramRect1, paramRect3, paramRect2)) || (getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect2), minorAxisDistance(paramInt, paramRect1, paramRect2)) >= getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect3), minorAxisDistance(paramInt, paramRect1, paramRect3)))));
    return true;
  }

  private static boolean isCandidate(@NonNull Rect paramRect1, @NonNull Rect paramRect2, int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
      if (((paramRect1.right <= paramRect2.right) && (paramRect1.left < paramRect2.right)) || (paramRect1.left <= paramRect2.left))
        break;
    case 66:
    case 33:
    case 130:
    }
    do
    {
      do
      {
        do
        {
          return true;
          return false;
        }
        while (((paramRect1.left < paramRect2.left) || (paramRect1.right <= paramRect2.left)) && (paramRect1.right < paramRect2.right));
        return false;
      }
      while (((paramRect1.bottom > paramRect2.bottom) || (paramRect1.top >= paramRect2.bottom)) && (paramRect1.top > paramRect2.top));
      return false;
    }
    while (((paramRect1.top < paramRect2.top) || (paramRect1.bottom <= paramRect2.top)) && (paramRect1.bottom < paramRect2.bottom));
    return false;
  }

  private static boolean isToDirectionOf(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    int i;
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
      int j = paramRect1.left;
      int k = paramRect2.right;
      i = 0;
      if (j < k)
        break;
    case 66:
    case 33:
    case 130:
    }
    do
    {
      do
      {
        do
        {
          i = 1;
          return i;
        }
        while (paramRect1.right <= paramRect2.left);
        return false;
      }
      while (paramRect1.top >= paramRect2.bottom);
      return false;
    }
    while (paramRect1.bottom <= paramRect2.top);
    return false;
  }

  private static int majorAxisDistance(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    return Math.max(0, majorAxisDistanceRaw(paramInt, paramRect1, paramRect2));
  }

  private static int majorAxisDistanceRaw(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
      return paramRect1.left - paramRect2.right;
    case 66:
      return paramRect2.left - paramRect1.right;
    case 33:
      return paramRect1.top - paramRect2.bottom;
    case 130:
    }
    return paramRect2.top - paramRect1.bottom;
  }

  private static int majorAxisDistanceToFarEdge(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    return Math.max(1, majorAxisDistanceToFarEdgeRaw(paramInt, paramRect1, paramRect2));
  }

  private static int majorAxisDistanceToFarEdgeRaw(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
      return paramRect1.left - paramRect2.left;
    case 66:
      return paramRect2.right - paramRect1.right;
    case 33:
      return paramRect1.top - paramRect2.top;
    case 130:
    }
    return paramRect2.bottom - paramRect1.bottom;
  }

  private static int minorAxisDistance(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
    case 66:
      return Math.abs(paramRect1.top + paramRect1.height() / 2 - (paramRect2.top + paramRect2.height() / 2));
    case 33:
    case 130:
    }
    return Math.abs(paramRect1.left + paramRect1.width() / 2 - (paramRect2.left + paramRect2.width() / 2));
  }

  public static abstract interface BoundsAdapter<T>
  {
    public abstract void obtainBounds(T paramT, Rect paramRect);
  }

  public static abstract interface CollectionAdapter<T, V>
  {
    public abstract V get(T paramT, int paramInt);

    public abstract int size(T paramT);
  }

  private static class SequentialComparator<T>
    implements Comparator<T>
  {
    private final FocusStrategy.BoundsAdapter<T> mAdapter;
    private final boolean mIsLayoutRtl;
    private final Rect mTemp1 = new Rect();
    private final Rect mTemp2 = new Rect();

    SequentialComparator(boolean paramBoolean, FocusStrategy.BoundsAdapter<T> paramBoundsAdapter)
    {
      this.mIsLayoutRtl = paramBoolean;
      this.mAdapter = paramBoundsAdapter;
    }

    public int compare(T paramT1, T paramT2)
    {
      Rect localRect1 = this.mTemp1;
      Rect localRect2 = this.mTemp2;
      this.mAdapter.obtainBounds(paramT1, localRect1);
      this.mAdapter.obtainBounds(paramT2, localRect2);
      if (localRect1.top < localRect2.top);
      while (true)
      {
        return -1;
        if (localRect1.top > localRect2.top)
          return 1;
        if (localRect1.left < localRect2.left)
          if (this.mIsLayoutRtl)
            return 1;
        if (localRect1.left > localRect2.left)
          if (!this.mIsLayoutRtl)
            return 1;
        if (localRect1.bottom < localRect2.bottom)
          continue;
        if (localRect1.bottom > localRect2.bottom)
          return 1;
        if (localRect1.right < localRect2.right)
          if (this.mIsLayoutRtl)
            return 1;
        if (localRect1.right <= localRect2.right)
          break;
        if (!this.mIsLayoutRtl)
          return 1;
      }
      return 0;
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.FocusStrategy
 * JD-Core Version:    0.6.0
 */