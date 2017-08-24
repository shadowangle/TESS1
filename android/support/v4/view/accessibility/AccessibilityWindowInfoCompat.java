package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;

public class AccessibilityWindowInfoCompat
{
  private static final AccessibilityWindowInfoImpl IMPL;
  public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
  public static final int TYPE_APPLICATION = 1;
  public static final int TYPE_INPUT_METHOD = 2;
  public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
  public static final int TYPE_SYSTEM = 3;
  private static final int UNDEFINED = -1;
  private Object mInfo;

  static
  {
    if (Build.VERSION.SDK_INT >= 24)
    {
      IMPL = new AccessibilityWindowInfoApi24Impl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 21)
    {
      IMPL = new AccessibilityWindowInfoApi21Impl();
      return;
    }
    IMPL = new AccessibilityWindowInfoStubImpl();
  }

  private AccessibilityWindowInfoCompat(Object paramObject)
  {
    this.mInfo = paramObject;
  }

  public static AccessibilityWindowInfoCompat obtain()
  {
    return wrapNonNullInstance(IMPL.obtain());
  }

  public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat paramAccessibilityWindowInfoCompat)
  {
    if (paramAccessibilityWindowInfoCompat == null)
      return null;
    return wrapNonNullInstance(IMPL.obtain(paramAccessibilityWindowInfoCompat.mInfo));
  }

  private static String typeToString(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "<UNKNOWN>";
    case 1:
      return "TYPE_APPLICATION";
    case 2:
      return "TYPE_INPUT_METHOD";
    case 3:
      return "TYPE_SYSTEM";
    case 4:
    }
    return "TYPE_ACCESSIBILITY_OVERLAY";
  }

  static AccessibilityWindowInfoCompat wrapNonNullInstance(Object paramObject)
  {
    if (paramObject != null)
      return new AccessibilityWindowInfoCompat(paramObject);
    return null;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    AccessibilityWindowInfoCompat localAccessibilityWindowInfoCompat;
    do
      while (true)
      {
        int i = 1;
        Class localClass1;
        Class localClass2;
        do
        {
          do
          {
            return i;
            i = 0;
          }
          while (paramObject == null);
          localClass1 = getClass();
          localClass2 = paramObject.getClass();
          i = 0;
        }
        while (localClass1 != localClass2);
        localAccessibilityWindowInfoCompat = (AccessibilityWindowInfoCompat)paramObject;
        if (this.mInfo != null)
          break;
        if (localAccessibilityWindowInfoCompat.mInfo != null)
          return false;
      }
    while (this.mInfo.equals(localAccessibilityWindowInfoCompat.mInfo));
    return false;
  }

  public AccessibilityNodeInfoCompat getAnchor()
  {
    return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getAnchor(this.mInfo));
  }

  public void getBoundsInScreen(Rect paramRect)
  {
    IMPL.getBoundsInScreen(this.mInfo, paramRect);
  }

  public AccessibilityWindowInfoCompat getChild(int paramInt)
  {
    return wrapNonNullInstance(IMPL.getChild(this.mInfo, paramInt));
  }

  public int getChildCount()
  {
    return IMPL.getChildCount(this.mInfo);
  }

  public int getId()
  {
    return IMPL.getId(this.mInfo);
  }

  public int getLayer()
  {
    return IMPL.getLayer(this.mInfo);
  }

  public AccessibilityWindowInfoCompat getParent()
  {
    return wrapNonNullInstance(IMPL.getParent(this.mInfo));
  }

  public AccessibilityNodeInfoCompat getRoot()
  {
    return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getRoot(this.mInfo));
  }

  public CharSequence getTitle()
  {
    return IMPL.getTitle(this.mInfo);
  }

  public int getType()
  {
    return IMPL.getType(this.mInfo);
  }

  public int hashCode()
  {
    if (this.mInfo == null)
      return 0;
    return this.mInfo.hashCode();
  }

  public boolean isAccessibilityFocused()
  {
    return IMPL.isAccessibilityFocused(this.mInfo);
  }

  public boolean isActive()
  {
    return IMPL.isActive(this.mInfo);
  }

  public boolean isFocused()
  {
    return IMPL.isFocused(this.mInfo);
  }

  public void recycle()
  {
    IMPL.recycle(this.mInfo);
  }

  public String toString()
  {
    boolean bool1 = true;
    StringBuilder localStringBuilder1 = new StringBuilder();
    Rect localRect = new Rect();
    getBoundsInScreen(localRect);
    localStringBuilder1.append("AccessibilityWindowInfo[");
    localStringBuilder1.append("id=").append(getId());
    localStringBuilder1.append(", type=").append(typeToString(getType()));
    localStringBuilder1.append(", layer=").append(getLayer());
    localStringBuilder1.append(", bounds=").append(localRect);
    localStringBuilder1.append(", focused=").append(isFocused());
    localStringBuilder1.append(", active=").append(isActive());
    StringBuilder localStringBuilder2 = localStringBuilder1.append(", hasParent=");
    boolean bool2;
    StringBuilder localStringBuilder3;
    if (getParent() != null)
    {
      bool2 = bool1;
      localStringBuilder2.append(bool2);
      localStringBuilder3 = localStringBuilder1.append(", hasChildren=");
      if (getChildCount() <= 0)
        break label180;
    }
    while (true)
    {
      localStringBuilder3.append(bool1);
      localStringBuilder1.append(']');
      return localStringBuilder1.toString();
      bool2 = false;
      break;
      label180: bool1 = false;
    }
  }

  private static class AccessibilityWindowInfoApi21Impl extends AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl
  {
    public void getBoundsInScreen(Object paramObject, Rect paramRect)
    {
      AccessibilityWindowInfoCompatApi21.getBoundsInScreen(paramObject, paramRect);
    }

    public Object getChild(Object paramObject, int paramInt)
    {
      return AccessibilityWindowInfoCompatApi21.getChild(paramObject, paramInt);
    }

    public int getChildCount(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.getChildCount(paramObject);
    }

    public int getId(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.getId(paramObject);
    }

    public int getLayer(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.getLayer(paramObject);
    }

    public Object getParent(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.getParent(paramObject);
    }

    public Object getRoot(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.getRoot(paramObject);
    }

    public int getType(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.getType(paramObject);
    }

    public boolean isAccessibilityFocused(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.isAccessibilityFocused(paramObject);
    }

    public boolean isActive(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.isActive(paramObject);
    }

    public boolean isFocused(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.isFocused(paramObject);
    }

    public Object obtain()
    {
      return AccessibilityWindowInfoCompatApi21.obtain();
    }

    public Object obtain(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi21.obtain(paramObject);
    }

    public void recycle(Object paramObject)
    {
      AccessibilityWindowInfoCompatApi21.recycle(paramObject);
    }
  }

  private static class AccessibilityWindowInfoApi24Impl extends AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi21Impl
  {
    public Object getAnchor(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi24.getAnchor(paramObject);
    }

    public CharSequence getTitle(Object paramObject)
    {
      return AccessibilityWindowInfoCompatApi24.getTitle(paramObject);
    }
  }

  private static abstract interface AccessibilityWindowInfoImpl
  {
    public abstract Object getAnchor(Object paramObject);

    public abstract void getBoundsInScreen(Object paramObject, Rect paramRect);

    public abstract Object getChild(Object paramObject, int paramInt);

    public abstract int getChildCount(Object paramObject);

    public abstract int getId(Object paramObject);

    public abstract int getLayer(Object paramObject);

    public abstract Object getParent(Object paramObject);

    public abstract Object getRoot(Object paramObject);

    public abstract CharSequence getTitle(Object paramObject);

    public abstract int getType(Object paramObject);

    public abstract boolean isAccessibilityFocused(Object paramObject);

    public abstract boolean isActive(Object paramObject);

    public abstract boolean isFocused(Object paramObject);

    public abstract Object obtain();

    public abstract Object obtain(Object paramObject);

    public abstract void recycle(Object paramObject);
  }

  private static class AccessibilityWindowInfoStubImpl
    implements AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
  {
    public Object getAnchor(Object paramObject)
    {
      return null;
    }

    public void getBoundsInScreen(Object paramObject, Rect paramRect)
    {
    }

    public Object getChild(Object paramObject, int paramInt)
    {
      return null;
    }

    public int getChildCount(Object paramObject)
    {
      return 0;
    }

    public int getId(Object paramObject)
    {
      return -1;
    }

    public int getLayer(Object paramObject)
    {
      return -1;
    }

    public Object getParent(Object paramObject)
    {
      return null;
    }

    public Object getRoot(Object paramObject)
    {
      return null;
    }

    public CharSequence getTitle(Object paramObject)
    {
      return null;
    }

    public int getType(Object paramObject)
    {
      return -1;
    }

    public boolean isAccessibilityFocused(Object paramObject)
    {
      return true;
    }

    public boolean isActive(Object paramObject)
    {
      return true;
    }

    public boolean isFocused(Object paramObject)
    {
      return true;
    }

    public Object obtain()
    {
      return null;
    }

    public Object obtain(Object paramObject)
    {
      return null;
    }

    public void recycle(Object paramObject)
    {
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityWindowInfoCompat
 * JD-Core Version:    0.6.0
 */