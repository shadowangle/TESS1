package android.support.v4.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat
{
  private static final String DEFAULT_CLASS_NAME = "android.view.View";
  public static final int HOST_ID = -1;
  public static final int INVALID_ID = -2147483648;
  private static final Rect INVALID_PARENT_BOUNDS = new Rect(2147483647, 2147483647, -2147483648, -2147483648);
  private static final FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = new FocusStrategy.BoundsAdapter()
  {
    public void obtainBounds(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat, Rect paramRect)
    {
      paramAccessibilityNodeInfoCompat.getBoundsInParent(paramRect);
    }
  };
  private static final FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter()
  {
    public AccessibilityNodeInfoCompat get(SparseArrayCompat<AccessibilityNodeInfoCompat> paramSparseArrayCompat, int paramInt)
    {
      return (AccessibilityNodeInfoCompat)paramSparseArrayCompat.valueAt(paramInt);
    }

    public int size(SparseArrayCompat<AccessibilityNodeInfoCompat> paramSparseArrayCompat)
    {
      return paramSparseArrayCompat.size();
    }
  };
  private int mAccessibilityFocusedVirtualViewId = -2147483648;
  private final View mHost;
  private int mHoveredVirtualViewId = -2147483648;
  private int mKeyboardFocusedVirtualViewId = -2147483648;
  private final AccessibilityManager mManager;
  private MyNodeProvider mNodeProvider;
  private final int[] mTempGlobalRect = new int[2];
  private final Rect mTempParentRect = new Rect();
  private final Rect mTempScreenRect = new Rect();
  private final Rect mTempVisibleRect = new Rect();

  public ExploreByTouchHelper(View paramView)
  {
    if (paramView == null)
      throw new IllegalArgumentException("View may not be null");
    this.mHost = paramView;
    this.mManager = ((AccessibilityManager)paramView.getContext().getSystemService("accessibility"));
    paramView.setFocusable(true);
    if (ViewCompat.getImportantForAccessibility(paramView) == 0)
      ViewCompat.setImportantForAccessibility(paramView, 1);
  }

  private boolean clearAccessibilityFocus(int paramInt)
  {
    if (this.mAccessibilityFocusedVirtualViewId == paramInt)
    {
      this.mAccessibilityFocusedVirtualViewId = -2147483648;
      this.mHost.invalidate();
      sendEventForVirtualView(paramInt, 65536);
      return true;
    }
    return false;
  }

  private boolean clickKeyboardFocusedVirtualView()
  {
    return (this.mKeyboardFocusedVirtualViewId != -2147483648) && (onPerformActionForVirtualView(this.mKeyboardFocusedVirtualViewId, 16, null));
  }

  private AccessibilityEvent createEvent(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default:
      return createEventForChild(paramInt1, paramInt2);
    case -1:
    }
    return createEventForHost(paramInt2);
  }

  private AccessibilityEvent createEventForChild(int paramInt1, int paramInt2)
  {
    AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt2);
    AccessibilityRecordCompat localAccessibilityRecordCompat = AccessibilityEventCompat.asRecord(localAccessibilityEvent);
    AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = obtainAccessibilityNodeInfo(paramInt1);
    localAccessibilityRecordCompat.getText().add(localAccessibilityNodeInfoCompat.getText());
    localAccessibilityRecordCompat.setContentDescription(localAccessibilityNodeInfoCompat.getContentDescription());
    localAccessibilityRecordCompat.setScrollable(localAccessibilityNodeInfoCompat.isScrollable());
    localAccessibilityRecordCompat.setPassword(localAccessibilityNodeInfoCompat.isPassword());
    localAccessibilityRecordCompat.setEnabled(localAccessibilityNodeInfoCompat.isEnabled());
    localAccessibilityRecordCompat.setChecked(localAccessibilityNodeInfoCompat.isChecked());
    onPopulateEventForVirtualView(paramInt1, localAccessibilityEvent);
    if ((localAccessibilityEvent.getText().isEmpty()) && (localAccessibilityEvent.getContentDescription() == null))
      throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
    localAccessibilityRecordCompat.setClassName(localAccessibilityNodeInfoCompat.getClassName());
    localAccessibilityRecordCompat.setSource(this.mHost, paramInt1);
    localAccessibilityEvent.setPackageName(this.mHost.getContext().getPackageName());
    return localAccessibilityEvent;
  }

  private AccessibilityEvent createEventForHost(int paramInt)
  {
    AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt);
    ViewCompat.onInitializeAccessibilityEvent(this.mHost, localAccessibilityEvent);
    return localAccessibilityEvent;
  }

  @NonNull
  private AccessibilityNodeInfoCompat createNodeForChild(int paramInt)
  {
    AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat1 = AccessibilityNodeInfoCompat.obtain();
    localAccessibilityNodeInfoCompat1.setEnabled(true);
    localAccessibilityNodeInfoCompat1.setFocusable(true);
    localAccessibilityNodeInfoCompat1.setClassName("android.view.View");
    localAccessibilityNodeInfoCompat1.setBoundsInParent(INVALID_PARENT_BOUNDS);
    localAccessibilityNodeInfoCompat1.setBoundsInScreen(INVALID_PARENT_BOUNDS);
    localAccessibilityNodeInfoCompat1.setParent(this.mHost);
    onPopulateNodeForVirtualView(paramInt, localAccessibilityNodeInfoCompat1);
    if ((localAccessibilityNodeInfoCompat1.getText() == null) && (localAccessibilityNodeInfoCompat1.getContentDescription() == null))
      throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
    localAccessibilityNodeInfoCompat1.getBoundsInParent(this.mTempParentRect);
    if (this.mTempParentRect.equals(INVALID_PARENT_BOUNDS))
      throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
    int i = localAccessibilityNodeInfoCompat1.getActions();
    if ((i & 0x40) != 0)
      throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
    if ((i & 0x80) != 0)
      throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
    localAccessibilityNodeInfoCompat1.setPackageName(this.mHost.getContext().getPackageName());
    localAccessibilityNodeInfoCompat1.setSource(this.mHost, paramInt);
    boolean bool;
    if (this.mAccessibilityFocusedVirtualViewId == paramInt)
    {
      localAccessibilityNodeInfoCompat1.setAccessibilityFocused(true);
      localAccessibilityNodeInfoCompat1.addAction(128);
      if (this.mKeyboardFocusedVirtualViewId != paramInt)
        break label362;
      bool = true;
      label201: if (!bool)
        break label368;
      localAccessibilityNodeInfoCompat1.addAction(2);
    }
    AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat2;
    while (true)
    {
      localAccessibilityNodeInfoCompat1.setFocused(bool);
      this.mHost.getLocationOnScreen(this.mTempGlobalRect);
      localAccessibilityNodeInfoCompat1.getBoundsInScreen(this.mTempScreenRect);
      if (!this.mTempScreenRect.equals(INVALID_PARENT_BOUNDS))
        break label423;
      localAccessibilityNodeInfoCompat1.getBoundsInParent(this.mTempScreenRect);
      if (localAccessibilityNodeInfoCompat1.mParentVirtualDescendantId == -1)
        break label388;
      localAccessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain();
      for (int j = localAccessibilityNodeInfoCompat1.mParentVirtualDescendantId; j != -1; j = localAccessibilityNodeInfoCompat2.mParentVirtualDescendantId)
      {
        localAccessibilityNodeInfoCompat2.setParent(this.mHost, -1);
        localAccessibilityNodeInfoCompat2.setBoundsInParent(INVALID_PARENT_BOUNDS);
        onPopulateNodeForVirtualView(j, localAccessibilityNodeInfoCompat2);
        localAccessibilityNodeInfoCompat2.getBoundsInParent(this.mTempParentRect);
        this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
      }
      localAccessibilityNodeInfoCompat1.setAccessibilityFocused(false);
      localAccessibilityNodeInfoCompat1.addAction(64);
      break;
      label362: bool = false;
      break label201;
      label368: if (!localAccessibilityNodeInfoCompat1.isFocusable())
        continue;
      localAccessibilityNodeInfoCompat1.addAction(1);
    }
    localAccessibilityNodeInfoCompat2.recycle();
    label388: this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
    label423: if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect))
    {
      this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
      this.mTempScreenRect.intersect(this.mTempVisibleRect);
      localAccessibilityNodeInfoCompat1.setBoundsInScreen(this.mTempScreenRect);
      if (isVisibleToUser(this.mTempScreenRect))
        localAccessibilityNodeInfoCompat1.setVisibleToUser(true);
    }
    return localAccessibilityNodeInfoCompat1;
  }

  @NonNull
  private AccessibilityNodeInfoCompat createNodeForHost()
  {
    AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(this.mHost);
    ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, localAccessibilityNodeInfoCompat);
    ArrayList localArrayList = new ArrayList();
    getVisibleVirtualViews(localArrayList);
    if ((localAccessibilityNodeInfoCompat.getChildCount() > 0) && (localArrayList.size() > 0))
      throw new RuntimeException("Views cannot have both real and virtual children");
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
      localAccessibilityNodeInfoCompat.addChild(this.mHost, ((Integer)localArrayList.get(j)).intValue());
    return localAccessibilityNodeInfoCompat;
  }

  private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes()
  {
    ArrayList localArrayList = new ArrayList();
    getVisibleVirtualViews(localArrayList);
    SparseArrayCompat localSparseArrayCompat = new SparseArrayCompat();
    for (int i = 0; i < localArrayList.size(); i++)
      localSparseArrayCompat.put(i, createNodeForChild(i));
    return localSparseArrayCompat;
  }

  private void getBoundsInParent(int paramInt, Rect paramRect)
  {
    obtainAccessibilityNodeInfo(paramInt).getBoundsInParent(paramRect);
  }

  private static Rect guessPreviouslyFocusedRect(@NonNull View paramView, int paramInt, @NonNull Rect paramRect)
  {
    int i = paramView.getWidth();
    int j = paramView.getHeight();
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    case 17:
      paramRect.set(i, 0, i, j);
      return paramRect;
    case 33:
      paramRect.set(0, j, i, j);
      return paramRect;
    case 66:
      paramRect.set(-1, 0, -1, j);
      return paramRect;
    case 130:
    }
    paramRect.set(0, -1, i, -1);
    return paramRect;
  }

  private boolean isVisibleToUser(Rect paramRect)
  {
    if (paramRect != null)
    {
      if (paramRect.isEmpty())
        return false;
      if (this.mHost.getWindowVisibility() == 0)
      {
        ViewParent localViewParent = this.mHost.getParent();
        while (true)
          if ((localViewParent instanceof View))
          {
            View localView = (View)localViewParent;
            if ((ViewCompat.getAlpha(localView) <= 0.0F) || (localView.getVisibility() != 0))
              break;
            localViewParent = localView.getParent();
            continue;
          }
          else
          {
            if (localViewParent == null)
              break;
            return true;
          }
      }
    }
    return false;
  }

  private static int keyToDirection(int paramInt)
  {
    switch (paramInt)
    {
    case 20:
    default:
      return 130;
    case 21:
      return 17;
    case 19:
      return 33;
    case 22:
    }
    return 66;
  }

  private boolean moveFocus(int paramInt, @Nullable Rect paramRect)
  {
    int i = 1;
    SparseArrayCompat localSparseArrayCompat = getAllNodes();
    int k = this.mKeyboardFocusedVirtualViewId;
    if (k == -2147483648);
    for (AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat1 = null; ; localAccessibilityNodeInfoCompat1 = (AccessibilityNodeInfoCompat)localSparseArrayCompat.get(k))
      switch (paramInt)
      {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 1:
      case 2:
      case 17:
      case 33:
      case 66:
      case 130:
      }
    AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat2;
    if (ViewCompat.getLayoutDirection(this.mHost) == i)
    {
      localAccessibilityNodeInfoCompat2 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInRelativeDirection(localSparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, localAccessibilityNodeInfoCompat1, paramInt, i, false);
      if (localAccessibilityNodeInfoCompat2 != null)
        break label242;
    }
    label242: for (int m = -2147483648; ; m = localSparseArrayCompat.keyAt(localSparseArrayCompat.indexOfValue(localAccessibilityNodeInfoCompat2)))
    {
      return requestKeyboardFocusForVirtualView(m);
      int j = 0;
      break;
      Rect localRect = new Rect();
      if (this.mKeyboardFocusedVirtualViewId != -2147483648)
        getBoundsInParent(this.mKeyboardFocusedVirtualViewId, localRect);
      while (true)
      {
        localAccessibilityNodeInfoCompat2 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInAbsoluteDirection(localSparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, localAccessibilityNodeInfoCompat1, localRect, paramInt);
        break;
        if (paramRect != null)
        {
          localRect.set(paramRect);
          continue;
        }
        guessPreviouslyFocusedRect(this.mHost, paramInt, localRect);
      }
    }
  }

  private boolean performActionForChild(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    switch (paramInt2)
    {
    default:
      return onPerformActionForVirtualView(paramInt1, paramInt2, paramBundle);
    case 64:
      return requestAccessibilityFocus(paramInt1);
    case 128:
      return clearAccessibilityFocus(paramInt1);
    case 1:
      return requestKeyboardFocusForVirtualView(paramInt1);
    case 2:
    }
    return clearKeyboardFocusForVirtualView(paramInt1);
  }

  private boolean performActionForHost(int paramInt, Bundle paramBundle)
  {
    return ViewCompat.performAccessibilityAction(this.mHost, paramInt, paramBundle);
  }

  private boolean requestAccessibilityFocus(int paramInt)
  {
    if ((!this.mManager.isEnabled()) || (!AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)));
    do
      return false;
    while (this.mAccessibilityFocusedVirtualViewId == paramInt);
    if (this.mAccessibilityFocusedVirtualViewId != -2147483648)
      clearAccessibilityFocus(this.mAccessibilityFocusedVirtualViewId);
    this.mAccessibilityFocusedVirtualViewId = paramInt;
    this.mHost.invalidate();
    sendEventForVirtualView(paramInt, 32768);
    return true;
  }

  private void updateHoveredVirtualView(int paramInt)
  {
    if (this.mHoveredVirtualViewId == paramInt)
      return;
    int i = this.mHoveredVirtualViewId;
    this.mHoveredVirtualViewId = paramInt;
    sendEventForVirtualView(paramInt, 128);
    sendEventForVirtualView(i, 256);
  }

  public final boolean clearKeyboardFocusForVirtualView(int paramInt)
  {
    if (this.mKeyboardFocusedVirtualViewId != paramInt)
      return false;
    this.mKeyboardFocusedVirtualViewId = -2147483648;
    onVirtualViewKeyboardFocusChanged(paramInt, false);
    sendEventForVirtualView(paramInt, 8);
    return true;
  }

  public final boolean dispatchHoverEvent(@NonNull MotionEvent paramMotionEvent)
  {
    if ((!this.mManager.isEnabled()) || (!AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)));
    do
    {
      int i;
      do
      {
        return false;
        switch (paramMotionEvent.getAction())
        {
        case 8:
        default:
          return false;
        case 7:
        case 9:
          i = getVirtualViewAt(paramMotionEvent.getX(), paramMotionEvent.getY());
          updateHoveredVirtualView(i);
        case 10:
        }
      }
      while (i == -2147483648);
      return true;
    }
    while (this.mAccessibilityFocusedVirtualViewId == -2147483648);
    updateHoveredVirtualView(-2147483648);
    return true;
  }

  public final boolean dispatchKeyEvent(@NonNull KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getAction();
    int j = 0;
    int k;
    if (i != 1)
    {
      k = paramKeyEvent.getKeyCode();
      j = 0;
      switch (k)
      {
      default:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 66:
      case 61:
      }
    }
    boolean bool1;
    do
    {
      int m;
      do
      {
        boolean bool2;
        do
        {
          boolean bool3;
          do
          {
            return j;
            bool3 = KeyEventCompat.hasNoModifiers(paramKeyEvent);
            j = 0;
          }
          while (!bool3);
          int n = keyToDirection(k);
          int i1 = paramKeyEvent.getRepeatCount();
          int i2 = 0;
          while ((i2 < i1 + 1) && (moveFocus(n, null)))
          {
            i2++;
            j = 1;
          }
          bool2 = KeyEventCompat.hasNoModifiers(paramKeyEvent);
          j = 0;
        }
        while (!bool2);
        m = paramKeyEvent.getRepeatCount();
        j = 0;
      }
      while (m != 0);
      clickKeyboardFocusedVirtualView();
      return true;
      if (KeyEventCompat.hasNoModifiers(paramKeyEvent))
        return moveFocus(2, null);
      bool1 = KeyEventCompat.hasModifiers(paramKeyEvent, 1);
      j = 0;
    }
    while (!bool1);
    return moveFocus(1, null);
  }

  public final int getAccessibilityFocusedVirtualViewId()
  {
    return this.mAccessibilityFocusedVirtualViewId;
  }

  public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View paramView)
  {
    if (this.mNodeProvider == null)
      this.mNodeProvider = new MyNodeProvider();
    return this.mNodeProvider;
  }

  @Deprecated
  public int getFocusedVirtualView()
  {
    return getAccessibilityFocusedVirtualViewId();
  }

  public final int getKeyboardFocusedVirtualViewId()
  {
    return this.mKeyboardFocusedVirtualViewId;
  }

  protected abstract int getVirtualViewAt(float paramFloat1, float paramFloat2);

  protected abstract void getVisibleVirtualViews(List<Integer> paramList);

  public final void invalidateRoot()
  {
    invalidateVirtualView(-1, 1);
  }

  public final void invalidateVirtualView(int paramInt)
  {
    invalidateVirtualView(paramInt, 0);
  }

  public final void invalidateVirtualView(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != -2147483648) && (this.mManager.isEnabled()))
    {
      ViewParent localViewParent = this.mHost.getParent();
      if (localViewParent != null)
      {
        AccessibilityEvent localAccessibilityEvent = createEvent(paramInt1, 2048);
        AccessibilityEventCompat.setContentChangeTypes(localAccessibilityEvent, paramInt2);
        ViewParentCompat.requestSendAccessibilityEvent(localViewParent, this.mHost, localAccessibilityEvent);
      }
    }
  }

  @NonNull
  AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int paramInt)
  {
    if (paramInt == -1)
      return createNodeForHost();
    return createNodeForChild(paramInt);
  }

  public final void onFocusChanged(boolean paramBoolean, int paramInt, @Nullable Rect paramRect)
  {
    if (this.mKeyboardFocusedVirtualViewId != -2147483648)
      clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
    if (paramBoolean)
      moveFocus(paramInt, paramRect);
  }

  public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
    onPopulateEventForHost(paramAccessibilityEvent);
  }

  public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
  {
    super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
    onPopulateNodeForHost(paramAccessibilityNodeInfoCompat);
  }

  protected abstract boolean onPerformActionForVirtualView(int paramInt1, int paramInt2, Bundle paramBundle);

  protected void onPopulateEventForHost(AccessibilityEvent paramAccessibilityEvent)
  {
  }

  protected void onPopulateEventForVirtualView(int paramInt, AccessibilityEvent paramAccessibilityEvent)
  {
  }

  protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
  {
  }

  protected abstract void onPopulateNodeForVirtualView(int paramInt, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat);

  protected void onVirtualViewKeyboardFocusChanged(int paramInt, boolean paramBoolean)
  {
  }

  boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    switch (paramInt1)
    {
    default:
      return performActionForChild(paramInt1, paramInt2, paramBundle);
    case -1:
    }
    return performActionForHost(paramInt2, paramBundle);
  }

  public final boolean requestKeyboardFocusForVirtualView(int paramInt)
  {
    if ((!this.mHost.isFocused()) && (!this.mHost.requestFocus()));
    do
      return false;
    while (this.mKeyboardFocusedVirtualViewId == paramInt);
    if (this.mKeyboardFocusedVirtualViewId != -2147483648)
      clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
    this.mKeyboardFocusedVirtualViewId = paramInt;
    onVirtualViewKeyboardFocusChanged(paramInt, true);
    sendEventForVirtualView(paramInt, 8);
    return true;
  }

  public final boolean sendEventForVirtualView(int paramInt1, int paramInt2)
  {
    if ((paramInt1 == -2147483648) || (!this.mManager.isEnabled()));
    ViewParent localViewParent;
    do
    {
      return false;
      localViewParent = this.mHost.getParent();
    }
    while (localViewParent == null);
    AccessibilityEvent localAccessibilityEvent = createEvent(paramInt1, paramInt2);
    return ViewParentCompat.requestSendAccessibilityEvent(localViewParent, this.mHost, localAccessibilityEvent);
  }

  private class MyNodeProvider extends AccessibilityNodeProviderCompat
  {
    MyNodeProvider()
    {
    }

    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int paramInt)
    {
      return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(paramInt));
    }

    public AccessibilityNodeInfoCompat findFocus(int paramInt)
    {
      if (paramInt == 2);
      for (int i = ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId; i == -2147483648; i = ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId)
        return null;
      return createAccessibilityNodeInfo(i);
    }

    public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      return ExploreByTouchHelper.this.performAction(paramInt1, paramInt2, paramBundle);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.ExploreByTouchHelper
 * JD-Core Version:    0.6.0
 */