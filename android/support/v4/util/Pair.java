package android.support.v4.util;

public class Pair<F, S>
{
  public final F first;
  public final S second;

  public Pair(F paramF, S paramS)
  {
    this.first = paramF;
    this.second = paramS;
  }

  public static <A, B> Pair<A, B> create(A paramA, B paramB)
  {
    return new Pair(paramA, paramB);
  }

  private static boolean objectsEqual(Object paramObject1, Object paramObject2)
  {
    return (paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Pair));
    Pair localPair;
    do
    {
      return false;
      localPair = (Pair)paramObject;
    }
    while ((!objectsEqual(localPair.first, this.first)) || (!objectsEqual(localPair.second, this.second)));
    return true;
  }

  public int hashCode()
  {
    int i;
    int j;
    if (this.first == null)
    {
      i = 0;
      Object localObject = this.second;
      j = 0;
      if (localObject != null)
        break label35;
    }
    while (true)
    {
      return i ^ j;
      i = this.first.hashCode();
      break;
      label35: j = this.second.hashCode();
    }
  }

  public String toString()
  {
    return "Pair{" + String.valueOf(this.first) + " " + String.valueOf(this.second) + "}";
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.util.Pair
 * JD-Core Version:    0.6.0
 */