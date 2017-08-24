package android.support.v4.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V>
  implements Map<K, V>
{
  MapCollections<K, V> mCollections;

  public ArrayMap()
  {
  }

  public ArrayMap(int paramInt)
  {
    super(paramInt);
  }

  public ArrayMap(SimpleArrayMap paramSimpleArrayMap)
  {
    super(paramSimpleArrayMap);
  }

  private MapCollections<K, V> getCollection()
  {
    if (this.mCollections == null)
      this.mCollections = new MapCollections()
      {
        protected void colClear()
        {
          ArrayMap.this.clear();
        }

        protected Object colGetEntry(int paramInt1, int paramInt2)
        {
          return ArrayMap.this.mArray[(paramInt2 + (paramInt1 << 1))];
        }

        protected Map<K, V> colGetMap()
        {
          return ArrayMap.this;
        }

        protected int colGetSize()
        {
          return ArrayMap.this.mSize;
        }

        protected int colIndexOfKey(Object paramObject)
        {
          return ArrayMap.this.indexOfKey(paramObject);
        }

        protected int colIndexOfValue(Object paramObject)
        {
          return ArrayMap.this.indexOfValue(paramObject);
        }

        protected void colPut(K paramK, V paramV)
        {
          ArrayMap.this.put(paramK, paramV);
        }

        protected void colRemoveAt(int paramInt)
        {
          ArrayMap.this.removeAt(paramInt);
        }

        protected V colSetValue(int paramInt, V paramV)
        {
          return ArrayMap.this.setValueAt(paramInt, paramV);
        }
      };
    return this.mCollections;
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    return MapCollections.containsAllHelper(this, paramCollection);
  }

  public Set<Map.Entry<K, V>> entrySet()
  {
    return getCollection().getEntrySet();
  }

  public Set<K> keySet()
  {
    return getCollection().getKeySet();
  }

  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    ensureCapacity(this.mSize + paramMap.size());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      put(localEntry.getKey(), localEntry.getValue());
    }
  }

  public boolean removeAll(Collection<?> paramCollection)
  {
    return MapCollections.removeAllHelper(this, paramCollection);
  }

  public boolean retainAll(Collection<?> paramCollection)
  {
    return MapCollections.retainAllHelper(this, paramCollection);
  }

  public Collection<V> values()
  {
    return getCollection().getValues();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.util.ArrayMap
 * JD-Core Version:    0.6.0
 */