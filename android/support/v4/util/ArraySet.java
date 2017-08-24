package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E>
  implements Collection<E>, Set<E>
{
  private static final int BASE_SIZE = 4;
  private static final int CACHE_SIZE = 10;
  private static final boolean DEBUG = false;
  private static final int[] INT = new int[0];
  private static final Object[] OBJECT = new Object[0];
  private static final String TAG = "ArraySet";
  static Object[] sBaseCache;
  static int sBaseCacheSize;
  static Object[] sTwiceBaseCache;
  static int sTwiceBaseCacheSize;
  Object[] mArray;
  MapCollections<E, E> mCollections;
  int[] mHashes;
  final boolean mIdentityHashCode;
  int mSize;

  public ArraySet()
  {
    this(0, false);
  }

  public ArraySet(int paramInt)
  {
    this(paramInt, false);
  }

  public ArraySet(int paramInt, boolean paramBoolean)
  {
    this.mIdentityHashCode = paramBoolean;
    if (paramInt == 0)
    {
      this.mHashes = INT;
      this.mArray = OBJECT;
    }
    while (true)
    {
      this.mSize = 0;
      return;
      allocArrays(paramInt);
    }
  }

  public ArraySet(ArraySet<E> paramArraySet)
  {
    this();
    if (paramArraySet != null)
      addAll(paramArraySet);
  }

  public ArraySet(Collection<E> paramCollection)
  {
    this();
    if (paramCollection != null)
      addAll(paramCollection);
  }

  private void allocArrays(int paramInt)
  {
    if (paramInt == 8)
      monitorenter;
    while (true)
    {
      try
      {
        if (sTwiceBaseCache == null)
          continue;
        Object[] arrayOfObject2 = sTwiceBaseCache;
        this.mArray = arrayOfObject2;
        sTwiceBaseCache = (Object[])(Object[])arrayOfObject2[0];
        this.mHashes = ((int[])(int[])arrayOfObject2[1]);
        arrayOfObject2[1] = null;
        arrayOfObject2[0] = null;
        sTwiceBaseCacheSize = -1 + sTwiceBaseCacheSize;
        return;
        monitorexit;
        this.mHashes = new int[paramInt];
        this.mArray = new Object[paramInt];
        return;
      }
      finally
      {
        monitorexit;
      }
      if (paramInt != 4)
        continue;
      monitorenter;
      try
      {
        if (sBaseCache != null)
        {
          Object[] arrayOfObject1 = sBaseCache;
          this.mArray = arrayOfObject1;
          sBaseCache = (Object[])(Object[])arrayOfObject1[0];
          this.mHashes = ((int[])(int[])arrayOfObject1[1]);
          arrayOfObject1[1] = null;
          arrayOfObject1[0] = null;
          sBaseCacheSize = -1 + sBaseCacheSize;
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      monitorexit;
    }
  }

  private static void freeArrays(int[] paramArrayOfInt, Object[] paramArrayOfObject, int paramInt)
  {
    if (paramArrayOfInt.length == 8)
    {
      monitorenter;
      try
      {
        if (sTwiceBaseCacheSize < 10)
        {
          paramArrayOfObject[0] = sTwiceBaseCache;
          paramArrayOfObject[1] = paramArrayOfInt;
          for (int j = paramInt - 1; j >= 2; j--)
            paramArrayOfObject[j] = null;
          sTwiceBaseCache = paramArrayOfObject;
          sTwiceBaseCacheSize = 1 + sTwiceBaseCacheSize;
        }
        return;
      }
      finally
      {
        monitorexit;
      }
    }
    if (paramArrayOfInt.length == 4)
    {
      monitorenter;
      try
      {
        if (sBaseCacheSize < 10)
        {
          paramArrayOfObject[0] = sBaseCache;
          paramArrayOfObject[1] = paramArrayOfInt;
          for (int i = paramInt - 1; i >= 2; i--)
            paramArrayOfObject[i] = null;
          sBaseCache = paramArrayOfObject;
          sBaseCacheSize = 1 + sBaseCacheSize;
        }
        return;
      }
      finally
      {
        monitorexit;
      }
    }
  }

  private MapCollections<E, E> getCollection()
  {
    if (this.mCollections == null)
      this.mCollections = new MapCollections()
      {
        protected void colClear()
        {
          ArraySet.this.clear();
        }

        protected Object colGetEntry(int paramInt1, int paramInt2)
        {
          return ArraySet.this.mArray[paramInt1];
        }

        protected Map<E, E> colGetMap()
        {
          throw new UnsupportedOperationException("not a map");
        }

        protected int colGetSize()
        {
          return ArraySet.this.mSize;
        }

        protected int colIndexOfKey(Object paramObject)
        {
          return ArraySet.this.indexOf(paramObject);
        }

        protected int colIndexOfValue(Object paramObject)
        {
          return ArraySet.this.indexOf(paramObject);
        }

        protected void colPut(E paramE1, E paramE2)
        {
          ArraySet.this.add(paramE1);
        }

        protected void colRemoveAt(int paramInt)
        {
          ArraySet.this.removeAt(paramInt);
        }

        protected E colSetValue(int paramInt, E paramE)
        {
          throw new UnsupportedOperationException("not a map");
        }
      };
    return this.mCollections;
  }

  private int indexOf(Object paramObject, int paramInt)
  {
    int i = this.mSize;
    int k;
    if (i == 0)
    {
      k = -1;
      return k;
    }
    int j = ContainerHelpers.binarySearch(this.mHashes, i, paramInt);
    if ((j >= 0) && (!paramObject.equals(this.mArray[j])))
    {
      for (k = j + 1; ; k++)
      {
        if ((k >= i) || (this.mHashes[k] != paramInt))
          break label88;
        if (paramObject.equals(this.mArray[k]))
          break;
      }
      label88: for (int m = j - 1; (m >= 0) && (this.mHashes[m] == paramInt); m--)
        if (paramObject.equals(this.mArray[m]))
          return m;
      return k ^ 0xFFFFFFFF;
    }
    return j;
  }

  private int indexOfNull()
  {
    int i = this.mSize;
    int k;
    if (i == 0)
    {
      k = -1;
      return k;
    }
    int j = ContainerHelpers.binarySearch(this.mHashes, i, 0);
    if ((j >= 0) && (this.mArray[j] != null))
    {
      for (k = j + 1; ; k++)
      {
        if ((k >= i) || (this.mHashes[k] != 0))
          break label69;
        if (this.mArray[k] == null)
          break;
      }
      label69: for (int m = j - 1; (m >= 0) && (this.mHashes[m] == 0); m--)
        if (this.mArray[m] == null)
          return m;
      return k ^ 0xFFFFFFFF;
    }
    return j;
  }

  public boolean add(E paramE)
  {
    int m;
    int n;
    if (paramE == null)
    {
      m = indexOfNull();
      n = 0;
      if (m >= 0)
        return false;
    }
    else
    {
      if (this.mIdentityHashCode);
      for (int i = System.identityHashCode(paramE); ; i = paramE.hashCode())
      {
        int j = indexOf(paramE, i);
        int k = i;
        m = j;
        n = k;
        break;
      }
    }
    int i1 = m ^ 0xFFFFFFFF;
    int i2;
    if (this.mSize >= this.mHashes.length)
    {
      if (this.mSize < 8)
        break label251;
      i2 = this.mSize + (this.mSize >> 1);
    }
    while (true)
    {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(i2);
      if (this.mHashes.length > 0)
      {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, arrayOfInt.length);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, arrayOfObject.length);
      }
      freeArrays(arrayOfInt, arrayOfObject, this.mSize);
      if (i1 < this.mSize)
      {
        System.arraycopy(this.mHashes, i1, this.mHashes, i1 + 1, this.mSize - i1);
        System.arraycopy(this.mArray, i1, this.mArray, i1 + 1, this.mSize - i1);
      }
      this.mHashes[i1] = n;
      this.mArray[i1] = paramE;
      this.mSize = (1 + this.mSize);
      return true;
      label251: if (this.mSize < 4)
      {
        i2 = 4;
        continue;
      }
      i2 = 8;
    }
  }

  public void addAll(ArraySet<? extends E> paramArraySet)
  {
    int i = paramArraySet.mSize;
    ensureCapacity(i + this.mSize);
    int j = this.mSize;
    int k = 0;
    if (j == 0)
      if (i > 0)
      {
        System.arraycopy(paramArraySet.mHashes, 0, this.mHashes, 0, i);
        System.arraycopy(paramArraySet.mArray, 0, this.mArray, 0, i);
        this.mSize = i;
      }
    while (true)
    {
      return;
      while (k < i)
      {
        add(paramArraySet.valueAt(k));
        k++;
      }
    }
  }

  public boolean addAll(Collection<? extends E> paramCollection)
  {
    ensureCapacity(this.mSize + paramCollection.size());
    boolean bool = false;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      bool |= add(localIterator.next());
    return bool;
  }

  public void append(E paramE)
  {
    int i = this.mSize;
    int j;
    if (paramE == null)
      j = 0;
    while (i >= this.mHashes.length)
    {
      throw new IllegalStateException("Array is full");
      if (this.mIdentityHashCode)
      {
        j = System.identityHashCode(paramE);
        continue;
      }
      j = paramE.hashCode();
    }
    if ((i > 0) && (this.mHashes[(i - 1)] > j))
    {
      add(paramE);
      return;
    }
    this.mSize = (i + 1);
    this.mHashes[i] = j;
    this.mArray[i] = paramE;
  }

  public void clear()
  {
    if (this.mSize != 0)
    {
      freeArrays(this.mHashes, this.mArray, this.mSize);
      this.mHashes = INT;
      this.mArray = OBJECT;
      this.mSize = 0;
    }
  }

  public boolean contains(Object paramObject)
  {
    return indexOf(paramObject) >= 0;
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      if (!contains(localIterator.next()))
        return false;
    return true;
  }

  public void ensureCapacity(int paramInt)
  {
    if (this.mHashes.length < paramInt)
    {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(paramInt);
      if (this.mSize > 0)
      {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, this.mSize);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, this.mSize);
      }
      freeArrays(arrayOfInt, arrayOfObject, this.mSize);
    }
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    while (true)
    {
      return true;
      if (!(paramObject instanceof Set))
        break;
      Set localSet = (Set)paramObject;
      if (size() != localSet.size())
        return false;
      int i = 0;
      try
      {
        while (i < this.mSize)
        {
          boolean bool = localSet.contains(valueAt(i));
          if (!bool)
            return false;
          i++;
        }
      }
      catch (NullPointerException localNullPointerException)
      {
        return false;
      }
      catch (ClassCastException localClassCastException)
      {
        return false;
      }
    }
    return false;
  }

  public int hashCode()
  {
    int i = 0;
    int[] arrayOfInt = this.mHashes;
    int j = this.mSize;
    for (int k = 0; k < j; k++)
      i += arrayOfInt[k];
    return i;
  }

  public int indexOf(Object paramObject)
  {
    if (paramObject == null)
      return indexOfNull();
    if (this.mIdentityHashCode);
    for (int i = System.identityHashCode(paramObject); ; i = paramObject.hashCode())
      return indexOf(paramObject, i);
  }

  public boolean isEmpty()
  {
    return this.mSize <= 0;
  }

  public Iterator<E> iterator()
  {
    return getCollection().getKeySet().iterator();
  }

  public boolean remove(Object paramObject)
  {
    int i = indexOf(paramObject);
    if (i >= 0)
    {
      removeAt(i);
      return true;
    }
    return false;
  }

  public boolean removeAll(ArraySet<? extends E> paramArraySet)
  {
    int i = paramArraySet.mSize;
    int j = this.mSize;
    for (int k = 0; k < i; k++)
      remove(paramArraySet.valueAt(k));
    int m = this.mSize;
    int n = 0;
    if (j != m)
      n = 1;
    return n;
  }

  public boolean removeAll(Collection<?> paramCollection)
  {
    boolean bool = false;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      bool |= remove(localIterator.next());
    return bool;
  }

  public E removeAt(int paramInt)
  {
    int i = 8;
    Object localObject = this.mArray[paramInt];
    if (this.mSize <= 1)
    {
      freeArrays(this.mHashes, this.mArray, this.mSize);
      this.mHashes = INT;
      this.mArray = OBJECT;
      this.mSize = 0;
    }
    while (true)
    {
      return localObject;
      if ((this.mHashes.length <= i) || (this.mSize >= this.mHashes.length / 3))
        break;
      if (this.mSize > i)
        i = this.mSize + (this.mSize >> 1);
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(i);
      this.mSize = (-1 + this.mSize);
      if (paramInt > 0)
      {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, paramInt);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, paramInt);
      }
      if (paramInt >= this.mSize)
        continue;
      System.arraycopy(arrayOfInt, paramInt + 1, this.mHashes, paramInt, this.mSize - paramInt);
      System.arraycopy(arrayOfObject, paramInt + 1, this.mArray, paramInt, this.mSize - paramInt);
      return localObject;
    }
    this.mSize = (-1 + this.mSize);
    if (paramInt < this.mSize)
    {
      System.arraycopy(this.mHashes, paramInt + 1, this.mHashes, paramInt, this.mSize - paramInt);
      System.arraycopy(this.mArray, paramInt + 1, this.mArray, paramInt, this.mSize - paramInt);
    }
    this.mArray[this.mSize] = null;
    return localObject;
  }

  public boolean retainAll(Collection<?> paramCollection)
  {
    int i = 0;
    for (int j = -1 + this.mSize; j >= 0; j--)
    {
      if (paramCollection.contains(this.mArray[j]))
        continue;
      removeAt(j);
      i = 1;
    }
    return i;
  }

  public int size()
  {
    return this.mSize;
  }

  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[this.mSize];
    System.arraycopy(this.mArray, 0, arrayOfObject, 0, this.mSize);
    return arrayOfObject;
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    if (paramArrayOfT.length < this.mSize);
    for (Object localObject = (Object[])(Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), this.mSize); ; localObject = paramArrayOfT)
    {
      System.arraycopy(this.mArray, 0, localObject, 0, this.mSize);
      if (localObject.length > this.mSize)
        localObject[this.mSize] = null;
      return localObject;
    }
  }

  public String toString()
  {
    if (isEmpty())
      return "{}";
    StringBuilder localStringBuilder = new StringBuilder(14 * this.mSize);
    localStringBuilder.append('{');
    int i = 0;
    if (i < this.mSize)
    {
      if (i > 0)
        localStringBuilder.append(", ");
      Object localObject = valueAt(i);
      if (localObject != this)
        localStringBuilder.append(localObject);
      while (true)
      {
        i++;
        break;
        localStringBuilder.append("(this Set)");
      }
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }

  public E valueAt(int paramInt)
  {
    return this.mArray[paramInt];
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.util.ArraySet
 * JD-Core Version:    0.6.0
 */