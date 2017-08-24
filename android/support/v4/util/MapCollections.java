package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class MapCollections<K, V>
{
  MapCollections<K, V>.EntrySet mEntrySet;
  MapCollections<K, V>.KeySet mKeySet;
  MapCollections<K, V>.ValuesCollection mValues;

  public static <K, V> boolean containsAllHelper(Map<K, V> paramMap, Collection<?> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      if (!paramMap.containsKey(localIterator.next()))
        return false;
    return true;
  }

  public static <T> boolean equalsSetHelper(Set<T> paramSet, Object paramObject)
  {
    int i = 1;
    int j;
    if (paramSet == paramObject)
      j = i;
    boolean bool1;
    do
    {
      return j;
      bool1 = paramObject instanceof Set;
      j = 0;
    }
    while (!bool1);
    Set localSet = (Set)paramObject;
    try
    {
      if (paramSet.size() == localSet.size())
      {
        boolean bool2 = paramSet.containsAll(localSet);
        if (!bool2);
      }
      while (true)
      {
        return i;
        i = 0;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      return false;
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    return false;
  }

  public static <K, V> boolean removeAllHelper(Map<K, V> paramMap, Collection<?> paramCollection)
  {
    int i = paramMap.size();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      paramMap.remove(localIterator.next());
    return i != paramMap.size();
  }

  public static <K, V> boolean retainAllHelper(Map<K, V> paramMap, Collection<?> paramCollection)
  {
    int i = paramMap.size();
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      if (paramCollection.contains(localIterator.next()))
        continue;
      localIterator.remove();
    }
    return i != paramMap.size();
  }

  protected abstract void colClear();

  protected abstract Object colGetEntry(int paramInt1, int paramInt2);

  protected abstract Map<K, V> colGetMap();

  protected abstract int colGetSize();

  protected abstract int colIndexOfKey(Object paramObject);

  protected abstract int colIndexOfValue(Object paramObject);

  protected abstract void colPut(K paramK, V paramV);

  protected abstract void colRemoveAt(int paramInt);

  protected abstract V colSetValue(int paramInt, V paramV);

  public Set<Map.Entry<K, V>> getEntrySet()
  {
    if (this.mEntrySet == null)
      this.mEntrySet = new EntrySet();
    return this.mEntrySet;
  }

  public Set<K> getKeySet()
  {
    if (this.mKeySet == null)
      this.mKeySet = new KeySet();
    return this.mKeySet;
  }

  public Collection<V> getValues()
  {
    if (this.mValues == null)
      this.mValues = new ValuesCollection();
    return this.mValues;
  }

  public Object[] toArrayHelper(int paramInt)
  {
    int i = colGetSize();
    Object[] arrayOfObject = new Object[i];
    for (int j = 0; j < i; j++)
      arrayOfObject[j] = colGetEntry(j, paramInt);
    return arrayOfObject;
  }

  public <T> T[] toArrayHelper(T[] paramArrayOfT, int paramInt)
  {
    int i = colGetSize();
    if (paramArrayOfT.length < i);
    for (Object localObject = (Object[])(Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i); ; localObject = paramArrayOfT)
    {
      for (int j = 0; j < i; j++)
        localObject[j] = colGetEntry(j, paramInt);
      if (localObject.length > i)
        localObject[i] = null;
      return localObject;
    }
  }

  final class ArrayIterator<T>
    implements Iterator<T>
  {
    boolean mCanRemove = false;
    int mIndex;
    final int mOffset;
    int mSize;

    ArrayIterator(int arg2)
    {
      int i;
      this.mOffset = i;
      this.mSize = MapCollections.this.colGetSize();
    }

    public boolean hasNext()
    {
      return this.mIndex < this.mSize;
    }

    public T next()
    {
      Object localObject = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
      this.mIndex = (1 + this.mIndex);
      this.mCanRemove = true;
      return localObject;
    }

    public void remove()
    {
      if (!this.mCanRemove)
        throw new IllegalStateException();
      this.mIndex = (-1 + this.mIndex);
      this.mSize = (-1 + this.mSize);
      this.mCanRemove = false;
      MapCollections.this.colRemoveAt(this.mIndex);
    }
  }

  final class EntrySet
    implements Set<Map.Entry<K, V>>
  {
    EntrySet()
    {
    }

    public boolean add(Map.Entry<K, V> paramEntry)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends Map.Entry<K, V>> paramCollection)
    {
      int i = MapCollections.this.colGetSize();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        MapCollections.this.colPut(localEntry.getKey(), localEntry.getValue());
      }
      return i != MapCollections.this.colGetSize();
    }

    public void clear()
    {
      MapCollections.this.colClear();
    }

    public boolean contains(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry));
      Map.Entry localEntry;
      int i;
      do
      {
        return false;
        localEntry = (Map.Entry)paramObject;
        i = MapCollections.this.colIndexOfKey(localEntry.getKey());
      }
      while (i < 0);
      return ContainerHelpers.equal(MapCollections.this.colGetEntry(i, 1), localEntry.getValue());
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
        if (!contains(localIterator.next()))
          return false;
      return true;
    }

    public boolean equals(Object paramObject)
    {
      return MapCollections.equalsSetHelper(this, paramObject);
    }

    public int hashCode()
    {
      int i = -1 + MapCollections.this.colGetSize();
      int j = 0;
      int k = i;
      if (k >= 0)
      {
        Object localObject1 = MapCollections.this.colGetEntry(k, 0);
        Object localObject2 = MapCollections.this.colGetEntry(k, 1);
        int m;
        if (localObject1 == null)
        {
          m = 0;
          label48: if (localObject2 != null)
            break label89;
        }
        label89: for (int n = 0; ; n = localObject2.hashCode())
        {
          int i1 = j + (n ^ m);
          int i2 = k - 1;
          j = i1;
          k = i2;
          break;
          m = localObject1.hashCode();
          break label48;
        }
      }
      return j;
    }

    public boolean isEmpty()
    {
      return MapCollections.this.colGetSize() == 0;
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new MapCollections.MapIterator(MapCollections.this);
    }

    public boolean remove(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public int size()
    {
      return MapCollections.this.colGetSize();
    }

    public Object[] toArray()
    {
      throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      throw new UnsupportedOperationException();
    }
  }

  final class KeySet
    implements Set<K>
  {
    KeySet()
    {
    }

    public boolean add(K paramK)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends K> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public void clear()
    {
      MapCollections.this.colClear();
    }

    public boolean contains(Object paramObject)
    {
      return MapCollections.this.colIndexOfKey(paramObject) >= 0;
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), paramCollection);
    }

    public boolean equals(Object paramObject)
    {
      return MapCollections.equalsSetHelper(this, paramObject);
    }

    public int hashCode()
    {
      int i = -1 + MapCollections.this.colGetSize();
      int j = 0;
      int k = i;
      if (k >= 0)
      {
        Object localObject = MapCollections.this.colGetEntry(k, 0);
        if (localObject == null);
        for (int m = 0; ; m = localObject.hashCode())
        {
          int n = m + j;
          k--;
          j = n;
          break;
        }
      }
      return j;
    }

    public boolean isEmpty()
    {
      return MapCollections.this.colGetSize() == 0;
    }

    public Iterator<K> iterator()
    {
      return new MapCollections.ArrayIterator(MapCollections.this, 0);
    }

    public boolean remove(Object paramObject)
    {
      int i = MapCollections.this.colIndexOfKey(paramObject);
      if (i >= 0)
      {
        MapCollections.this.colRemoveAt(i);
        return true;
      }
      return false;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), paramCollection);
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), paramCollection);
    }

    public int size()
    {
      return MapCollections.this.colGetSize();
    }

    public Object[] toArray()
    {
      return MapCollections.this.toArrayHelper(0);
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return MapCollections.this.toArrayHelper(paramArrayOfT, 0);
    }
  }

  final class MapIterator
    implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V>
  {
    int mEnd = -1 + MapCollections.this.colGetSize();
    boolean mEntryValid = false;
    int mIndex = -1;

    MapIterator()
    {
    }

    public final boolean equals(Object paramObject)
    {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects");
      if (!(paramObject instanceof Map.Entry));
      Map.Entry localEntry;
      do
      {
        return false;
        localEntry = (Map.Entry)paramObject;
      }
      while ((!ContainerHelpers.equal(localEntry.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0))) || (!ContainerHelpers.equal(localEntry.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1))));
      return true;
    }

    public K getKey()
    {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects");
      return MapCollections.this.colGetEntry(this.mIndex, 0);
    }

    public V getValue()
    {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects");
      return MapCollections.this.colGetEntry(this.mIndex, 1);
    }

    public boolean hasNext()
    {
      return this.mIndex < this.mEnd;
    }

    public final int hashCode()
    {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects");
      Object localObject1 = MapCollections.this.colGetEntry(this.mIndex, 0);
      Object localObject2 = MapCollections.this.colGetEntry(this.mIndex, 1);
      int i;
      int j;
      if (localObject1 == null)
      {
        i = 0;
        j = 0;
        if (localObject2 != null)
          break label69;
      }
      while (true)
      {
        return j ^ i;
        i = localObject1.hashCode();
        break;
        label69: j = localObject2.hashCode();
      }
    }

    public Map.Entry<K, V> next()
    {
      this.mIndex = (1 + this.mIndex);
      this.mEntryValid = true;
      return this;
    }

    public void remove()
    {
      if (!this.mEntryValid)
        throw new IllegalStateException();
      MapCollections.this.colRemoveAt(this.mIndex);
      this.mIndex = (-1 + this.mIndex);
      this.mEnd = (-1 + this.mEnd);
      this.mEntryValid = false;
    }

    public V setValue(V paramV)
    {
      if (!this.mEntryValid)
        throw new IllegalStateException("This container does not support retaining Map.Entry objects");
      return MapCollections.this.colSetValue(this.mIndex, paramV);
    }

    public final String toString()
    {
      return getKey() + "=" + getValue();
    }
  }

  final class ValuesCollection
    implements Collection<V>
  {
    ValuesCollection()
    {
    }

    public boolean add(V paramV)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends V> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public void clear()
    {
      MapCollections.this.colClear();
    }

    public boolean contains(Object paramObject)
    {
      return MapCollections.this.colIndexOfValue(paramObject) >= 0;
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
        if (!contains(localIterator.next()))
          return false;
      return true;
    }

    public boolean isEmpty()
    {
      return MapCollections.this.colGetSize() == 0;
    }

    public Iterator<V> iterator()
    {
      return new MapCollections.ArrayIterator(MapCollections.this, 1);
    }

    public boolean remove(Object paramObject)
    {
      int i = MapCollections.this.colIndexOfValue(paramObject);
      if (i >= 0)
      {
        MapCollections.this.colRemoveAt(i);
        return true;
      }
      return false;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      int i = 0;
      int j = MapCollections.this.colGetSize();
      for (int k = 0; k < j; k++)
      {
        if (!paramCollection.contains(MapCollections.this.colGetEntry(k, 1)))
          continue;
        MapCollections.this.colRemoveAt(k);
        int m = k - 1;
        j--;
        k = m;
        i = 1;
      }
      return i;
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      int i = 0;
      int j = MapCollections.this.colGetSize();
      for (int k = 0; k < j; k++)
      {
        if (paramCollection.contains(MapCollections.this.colGetEntry(k, 1)))
          continue;
        MapCollections.this.colRemoveAt(k);
        int m = k - 1;
        j--;
        k = m;
        i = 1;
      }
      return i;
    }

    public int size()
    {
      return MapCollections.this.colGetSize();
    }

    public Object[] toArray()
    {
      return MapCollections.this.toArrayHelper(1);
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return MapCollections.this.toArrayHelper(paramArrayOfT, 1);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.util.MapCollections
 * JD-Core Version:    0.6.0
 */