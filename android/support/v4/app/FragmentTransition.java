package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArrayMap<Ljava.lang.String;Ljava.lang.String;>;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;

class FragmentTransition
{
  private static final int[] INVERSE_OPS = { 0, 3, 0, 1, 5, 4, 7, 6 };

  private static void addSharedElementsWithMatchingNames(ArrayList<View> paramArrayList, ArrayMap<String, View> paramArrayMap, Collection<String> paramCollection)
  {
    for (int i = -1 + paramArrayMap.size(); i >= 0; i--)
    {
      View localView = (View)paramArrayMap.valueAt(i);
      if (!paramCollection.contains(ViewCompat.getTransitionName(localView)))
        continue;
      paramArrayList.add(localView);
    }
  }

  private static void addToFirstInLastOut(BackStackRecord paramBackStackRecord, BackStackRecord.Op paramOp, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean1, boolean paramBoolean2)
  {
    Fragment localFragment = paramOp.fragment;
    int i = localFragment.mContainerId;
    if (i == 0)
      return;
    int j;
    label33: int k;
    int n;
    int m;
    int i1;
    if (paramBoolean1)
    {
      j = INVERSE_OPS[paramOp.cmd];
      switch (j)
      {
      default:
        k = 0;
        n = 0;
        m = 0;
        i1 = 0;
      case 5:
      case 1:
      case 7:
      case 4:
      case 3:
      case 6:
      case 2:
      }
    }
    while (true)
    {
      FragmentContainerTransition localFragmentContainerTransition1 = (FragmentContainerTransition)paramSparseArray.get(i);
      FragmentContainerTransition localFragmentContainerTransition2;
      if (k != 0)
      {
        localFragmentContainerTransition2 = ensureContainer(localFragmentContainerTransition1, paramSparseArray, i);
        localFragmentContainerTransition2.lastIn = localFragment;
        localFragmentContainerTransition2.lastInIsPop = paramBoolean1;
        localFragmentContainerTransition2.lastInTransaction = paramBackStackRecord;
      }
      while (true)
      {
        if ((!paramBoolean2) && (m != 0))
        {
          if ((localFragmentContainerTransition2 != null) && (localFragmentContainerTransition2.firstOut == localFragment))
            localFragmentContainerTransition2.firstOut = null;
          FragmentManagerImpl localFragmentManagerImpl = paramBackStackRecord.mManager;
          if ((localFragment.mState < 1) && (localFragmentManagerImpl.mCurState >= 1) && (!paramBackStackRecord.mAllowOptimization))
          {
            localFragmentManagerImpl.makeActive(localFragment);
            localFragmentManagerImpl.moveToState(localFragment, 1, 0, 0, false);
          }
        }
        FragmentContainerTransition localFragmentContainerTransition3;
        if ((i1 != 0) && ((localFragmentContainerTransition2 == null) || (localFragmentContainerTransition2.firstOut == null)))
        {
          localFragmentContainerTransition3 = ensureContainer(localFragmentContainerTransition2, paramSparseArray, i);
          localFragmentContainerTransition3.firstOut = localFragment;
          localFragmentContainerTransition3.firstOutIsPop = paramBoolean1;
          localFragmentContainerTransition3.firstOutTransaction = paramBackStackRecord;
        }
        while ((!paramBoolean2) && (n != 0) && (localFragmentContainerTransition3 != null) && (localFragmentContainerTransition3.lastIn == localFragment))
        {
          localFragmentContainerTransition3.lastIn = null;
          return;
          j = paramOp.cmd;
          break label33;
          boolean bool2;
          if (paramBoolean2)
            if ((localFragment.mHiddenChanged) && (!localFragment.mHidden) && (localFragment.mAdded))
              bool2 = true;
          while (true)
          {
            k = bool2;
            m = 1;
            n = 0;
            i1 = 0;
            break;
            bool2 = false;
            continue;
            bool2 = localFragment.mHidden;
          }
          boolean bool1;
          if (paramBoolean2)
            bool1 = localFragment.mIsNewlyAdded;
          while (true)
          {
            k = bool1;
            m = 1;
            n = 0;
            i1 = 0;
            break;
            if ((!localFragment.mAdded) && (!localFragment.mHidden))
            {
              bool1 = true;
              continue;
            }
            bool1 = false;
          }
          int i3;
          if (paramBoolean2)
            if ((localFragment.mHiddenChanged) && (localFragment.mAdded) && (localFragment.mHidden))
              i3 = 1;
          while (true)
          {
            n = 1;
            i1 = i3;
            k = 0;
            m = 0;
            break;
            i3 = 0;
            continue;
            if ((localFragment.mAdded) && (!localFragment.mHidden))
            {
              i3 = 1;
              continue;
            }
            i3 = 0;
          }
          int i2;
          if (paramBoolean2)
            if ((!localFragment.mAdded) && (localFragment.mView != null) && (localFragment.mView.getVisibility() == 0) && (localFragment.mPostponedAlpha >= 0.0F))
              i2 = 1;
          while (true)
          {
            n = 1;
            i1 = i2;
            k = 0;
            m = 0;
            break;
            i2 = 0;
            continue;
            if ((localFragment.mAdded) && (!localFragment.mHidden))
            {
              i2 = 1;
              continue;
            }
            i2 = 0;
          }
          localFragmentContainerTransition3 = localFragmentContainerTransition2;
        }
        localFragmentContainerTransition2 = localFragmentContainerTransition1;
      }
      k = 0;
      m = 0;
      n = 0;
      i1 = 0;
    }
  }

  public static void calculateFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean)
  {
    int i = paramBackStackRecord.mOps.size();
    for (int j = 0; j < i; j++)
      addToFirstInLastOut(paramBackStackRecord, (BackStackRecord.Op)paramBackStackRecord.mOps.get(j), paramSparseArray, false, paramBoolean);
  }

  private static ArrayMap<String, String> calculateNameOverrides(int paramInt1, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt2, int paramInt3)
  {
    ArrayMap localArrayMap = new ArrayMap();
    int i = paramInt3 - 1;
    if (i >= paramInt2)
    {
      BackStackRecord localBackStackRecord = (BackStackRecord)paramArrayList.get(i);
      if (!localBackStackRecord.interactsWith(paramInt1));
      boolean bool;
      do
      {
        i--;
        break;
        bool = ((Boolean)paramArrayList1.get(i)).booleanValue();
      }
      while (localBackStackRecord.mSharedElementSourceNames == null);
      int j = localBackStackRecord.mSharedElementSourceNames.size();
      ArrayList localArrayList2;
      Object localObject;
      label106: int k;
      label109: String str1;
      String str2;
      if (bool)
      {
        ArrayList localArrayList3 = localBackStackRecord.mSharedElementSourceNames;
        ArrayList localArrayList4 = localBackStackRecord.mSharedElementTargetNames;
        localArrayList2 = localArrayList3;
        localObject = localArrayList4;
        k = 0;
        if (k < j)
        {
          str1 = (String)((ArrayList)localObject).get(k);
          str2 = (String)localArrayList2.get(k);
          String str3 = (String)localArrayMap.remove(str2);
          if (str3 == null)
            break label194;
          localArrayMap.put(str1, str3);
        }
      }
      while (true)
      {
        k++;
        break label109;
        break;
        ArrayList localArrayList1 = localBackStackRecord.mSharedElementSourceNames;
        localArrayList2 = localBackStackRecord.mSharedElementTargetNames;
        localObject = localArrayList1;
        break label106;
        label194: localArrayMap.put(str1, str2);
      }
    }
    return (ArrayMap<String, String>)localArrayMap;
  }

  public static void calculatePopFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean)
  {
    if (!paramBackStackRecord.mManager.mContainer.onHasView());
    while (true)
    {
      return;
      for (int i = -1 + paramBackStackRecord.mOps.size(); i >= 0; i--)
        addToFirstInLastOut(paramBackStackRecord, (BackStackRecord.Op)paramBackStackRecord.mOps.get(i), paramSparseArray, true, paramBoolean);
    }
  }

  private static void callSharedElementStartEnd(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean1, ArrayMap<String, View> paramArrayMap, boolean paramBoolean2)
  {
    int i = 0;
    SharedElementCallback localSharedElementCallback;
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    int j;
    if (paramBoolean1)
    {
      localSharedElementCallback = paramFragment2.getEnterTransitionCallback();
      if (localSharedElementCallback == null)
        break label116;
      localArrayList1 = new ArrayList();
      localArrayList2 = new ArrayList();
      if (paramArrayMap != null)
        break label89;
      j = 0;
    }
    while (true)
    {
      if (i >= j)
        break label101;
      localArrayList2.add(paramArrayMap.keyAt(i));
      localArrayList1.add(paramArrayMap.valueAt(i));
      i++;
      continue;
      localSharedElementCallback = paramFragment1.getEnterTransitionCallback();
      break;
      label89: j = paramArrayMap.size();
      i = 0;
    }
    label101: if (paramBoolean2)
    {
      localSharedElementCallback.onSharedElementStart(localArrayList2, localArrayList1, null);
      label116: return;
    }
    localSharedElementCallback.onSharedElementEnd(localArrayList2, localArrayList1, null);
  }

  private static ArrayMap<String, View> captureInSharedElements(ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition)
  {
    Fragment localFragment = paramFragmentContainerTransition.lastIn;
    View localView1 = localFragment.getView();
    if ((paramArrayMap.isEmpty()) || (paramObject == null) || (localView1 == null))
    {
      paramArrayMap.clear();
      return null;
    }
    ArrayMap localArrayMap = new ArrayMap();
    FragmentTransitionCompat21.findNamedViews(localArrayMap, localView1);
    BackStackRecord localBackStackRecord = paramFragmentContainerTransition.lastInTransaction;
    SharedElementCallback localSharedElementCallback;
    ArrayList localArrayList;
    int i;
    label106: String str1;
    View localView2;
    if (paramFragmentContainerTransition.lastInIsPop)
    {
      localSharedElementCallback = localFragment.getExitTransitionCallback();
      localArrayList = localBackStackRecord.mSharedElementSourceNames;
      localArrayMap.retainAll(localArrayList);
      if (localSharedElementCallback == null)
        break label223;
      localSharedElementCallback.onMapSharedElements(localArrayList, localArrayMap);
      i = -1 + localArrayList.size();
      if (i < 0)
        break label232;
      str1 = (String)localArrayList.get(i);
      localView2 = (View)localArrayMap.get(str1);
      if (localView2 != null)
        break label182;
      String str3 = findKeyForValue(paramArrayMap, str1);
      if (str3 != null)
        paramArrayMap.remove(str3);
    }
    while (true)
    {
      i--;
      break label106;
      localSharedElementCallback = localFragment.getEnterTransitionCallback();
      localArrayList = localBackStackRecord.mSharedElementTargetNames;
      break;
      label182: if (str1.equals(ViewCompat.getTransitionName(localView2)))
        continue;
      String str2 = findKeyForValue(paramArrayMap, str1);
      if (str2 == null)
        continue;
      paramArrayMap.put(str2, ViewCompat.getTransitionName(localView2));
    }
    label223: retainValues(paramArrayMap, localArrayMap);
    return localArrayMap;
    label232: return localArrayMap;
  }

  private static ArrayMap<String, View> captureOutSharedElements(ArrayMap<String, String> paramArrayMap, Object paramObject, FragmentContainerTransition paramFragmentContainerTransition)
  {
    if ((paramArrayMap.isEmpty()) || (paramObject == null))
    {
      paramArrayMap.clear();
      return null;
    }
    Fragment localFragment = paramFragmentContainerTransition.firstOut;
    ArrayMap localArrayMap = new ArrayMap();
    FragmentTransitionCompat21.findNamedViews(localArrayMap, localFragment.getView());
    BackStackRecord localBackStackRecord = paramFragmentContainerTransition.firstOutTransaction;
    SharedElementCallback localSharedElementCallback;
    ArrayList localArrayList;
    int i;
    label97: String str1;
    View localView;
    if (paramFragmentContainerTransition.firstOutIsPop)
    {
      localSharedElementCallback = localFragment.getEnterTransitionCallback();
      localArrayList = localBackStackRecord.mSharedElementTargetNames;
      localArrayMap.retainAll(localArrayList);
      if (localSharedElementCallback == null)
        break label199;
      localSharedElementCallback.onMapSharedElements(localArrayList, localArrayMap);
      i = -1 + localArrayList.size();
      if (i < 0)
        break label212;
      str1 = (String)localArrayList.get(i);
      localView = (View)localArrayMap.get(str1);
      if (localView != null)
        break label160;
      paramArrayMap.remove(str1);
    }
    while (true)
    {
      i--;
      break label97;
      localSharedElementCallback = localFragment.getExitTransitionCallback();
      localArrayList = localBackStackRecord.mSharedElementSourceNames;
      break;
      label160: if (str1.equals(ViewCompat.getTransitionName(localView)))
        continue;
      String str2 = (String)paramArrayMap.remove(str1);
      paramArrayMap.put(ViewCompat.getTransitionName(localView), str2);
    }
    label199: paramArrayMap.retainAll(localArrayMap.keySet());
    return localArrayMap;
    label212: return localArrayMap;
  }

  private static ArrayList<View> configureEnteringExitingViews(Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList, View paramView)
  {
    ArrayList localArrayList = null;
    if (paramObject != null)
    {
      localArrayList = new ArrayList();
      FragmentTransitionCompat21.captureTransitioningViews(localArrayList, paramFragment.getView());
      if (paramArrayList != null)
        localArrayList.removeAll(paramArrayList);
      if (!localArrayList.isEmpty())
      {
        localArrayList.add(paramView);
        FragmentTransitionCompat21.addTargets(paramObject, localArrayList);
      }
    }
    return localArrayList;
  }

  private static Object configureSharedElementsOptimized(ViewGroup paramViewGroup, View paramView, ArrayMap<String, String> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Object paramObject1, Object paramObject2)
  {
    Fragment localFragment1 = paramFragmentContainerTransition.lastIn;
    Fragment localFragment2 = paramFragmentContainerTransition.firstOut;
    if (localFragment1 != null)
      localFragment1.getView().setVisibility(0);
    if ((localFragment1 == null) || (localFragment2 == null));
    label278: label282: 
    while (true)
    {
      return null;
      boolean bool = paramFragmentContainerTransition.lastInIsPop;
      Object localObject1;
      ArrayMap localArrayMap1;
      ArrayMap localArrayMap2;
      Object localObject2;
      if (paramArrayMap.isEmpty())
      {
        localObject1 = null;
        localArrayMap1 = captureOutSharedElements(paramArrayMap, localObject1, paramFragmentContainerTransition);
        localArrayMap2 = captureInSharedElements(paramArrayMap, localObject1, paramFragmentContainerTransition);
        if (!paramArrayMap.isEmpty())
          break label240;
        if (localArrayMap1 != null)
          localArrayMap1.clear();
        if (localArrayMap2 == null)
          break label278;
        localArrayMap2.clear();
        localObject2 = null;
      }
      while (true)
      {
        label102: if ((paramObject1 == null) && (paramObject2 == null) && (localObject2 == null))
          break label282;
        callSharedElementStartEnd(localFragment1, localFragment2, bool, localArrayMap1, true);
        Rect localRect;
        View localView;
        if (localObject2 != null)
        {
          paramArrayList2.add(paramView);
          FragmentTransitionCompat21.setSharedElementTargets(localObject2, paramView, paramArrayList1);
          setOutEpicenter(localObject2, paramObject2, localArrayMap1, paramFragmentContainerTransition.firstOutIsPop, paramFragmentContainerTransition.firstOutTransaction);
          localRect = new Rect();
          localView = getInEpicenterView(localArrayMap2, paramFragmentContainerTransition, paramObject1, bool);
          if (localView != null)
            FragmentTransitionCompat21.setEpicenter(paramObject1, localRect);
        }
        while (true)
        {
          OneShotPreDrawListener.add(paramViewGroup, new Runnable(localFragment1, localFragment2, bool, localArrayMap2, localView, localRect)
          {
            public void run()
            {
              FragmentTransition.access$200(this.val$inFragment, this.val$outFragment, this.val$inIsPop, this.val$inSharedElements, false);
              if (this.val$epicenterView != null)
                FragmentTransitionCompat21.getBoundsOnScreen(this.val$epicenterView, this.val$epicenter);
            }
          });
          return localObject2;
          localObject1 = getSharedElementTransition(localFragment1, localFragment2, bool);
          break;
          label240: addSharedElementsWithMatchingNames(paramArrayList1, localArrayMap1, paramArrayMap.keySet());
          addSharedElementsWithMatchingNames(paramArrayList2, localArrayMap2, paramArrayMap.values());
          localObject2 = localObject1;
          break label102;
          localView = null;
          localRect = null;
        }
        localObject2 = null;
      }
    }
  }

  private static Object configureSharedElementsUnoptimized(ViewGroup paramViewGroup, View paramView, ArrayMap<String, String> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Object paramObject1, Object paramObject2)
  {
    Fragment localFragment1 = paramFragmentContainerTransition.lastIn;
    Fragment localFragment2 = paramFragmentContainerTransition.firstOut;
    if ((localFragment1 == null) || (localFragment2 == null))
      return null;
    boolean bool = paramFragmentContainerTransition.lastInIsPop;
    Object localObject1;
    ArrayMap localArrayMap;
    if (paramArrayMap.isEmpty())
    {
      localObject1 = null;
      localArrayMap = captureOutSharedElements(paramArrayMap, localObject1, paramFragmentContainerTransition);
      if (!paramArrayMap.isEmpty())
        break label90;
    }
    for (Object localObject2 = null; ; localObject2 = localObject1)
    {
      if ((paramObject1 != null) || (paramObject2 != null) || (localObject2 != null))
        break label108;
      return null;
      localObject1 = getSharedElementTransition(localFragment1, localFragment2, bool);
      break;
      label90: paramArrayList1.addAll(localArrayMap.values());
    }
    label108: callSharedElementStartEnd(localFragment1, localFragment2, bool, localArrayMap, true);
    Rect localRect;
    if (localObject2 != null)
    {
      localRect = new Rect();
      FragmentTransitionCompat21.setSharedElementTargets(localObject2, paramView, paramArrayList1);
      setOutEpicenter(localObject2, paramObject2, localArrayMap, paramFragmentContainerTransition.firstOutIsPop, paramFragmentContainerTransition.firstOutTransaction);
      if (paramObject1 != null)
        FragmentTransitionCompat21.setEpicenter(paramObject1, localRect);
    }
    while (true)
    {
      OneShotPreDrawListener.add(paramViewGroup, new Runnable(paramArrayMap, localObject2, paramFragmentContainerTransition, paramArrayList2, paramView, localFragment1, localFragment2, bool, paramArrayList1, paramObject1, localRect)
      {
        public void run()
        {
          ArrayMap localArrayMap = FragmentTransition.access$300(this.val$nameOverrides, this.val$finalSharedElementTransition, this.val$fragments);
          if (localArrayMap != null)
          {
            this.val$sharedElementsIn.addAll(localArrayMap.values());
            this.val$sharedElementsIn.add(this.val$nonExistentView);
          }
          FragmentTransition.access$200(this.val$inFragment, this.val$outFragment, this.val$inIsPop, localArrayMap, false);
          if (this.val$finalSharedElementTransition != null)
          {
            FragmentTransitionCompat21.swapSharedElementTargets(this.val$finalSharedElementTransition, this.val$sharedElementsOut, this.val$sharedElementsIn);
            View localView = FragmentTransition.access$400(localArrayMap, this.val$fragments, this.val$enterTransition, this.val$inIsPop);
            if (localView != null)
              FragmentTransitionCompat21.getBoundsOnScreen(localView, this.val$inEpicenter);
          }
        }
      });
      return localObject2;
      localRect = null;
    }
  }

  private static void configureTransitionsOptimized(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap)
  {
    boolean bool1 = paramFragmentManagerImpl.mContainer.onHasView();
    ViewGroup localViewGroup = null;
    if (bool1)
      localViewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    if (localViewGroup == null);
    Fragment localFragment2;
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    Object localObject1;
    Object localObject2;
    Object localObject3;
    ArrayList localArrayList3;
    ArrayList localArrayList4;
    Object localObject4;
    do
    {
      Fragment localFragment1;
      boolean bool2;
      do
      {
        return;
        localFragment1 = paramFragmentContainerTransition.lastIn;
        localFragment2 = paramFragmentContainerTransition.firstOut;
        bool2 = paramFragmentContainerTransition.lastInIsPop;
        boolean bool3 = paramFragmentContainerTransition.firstOutIsPop;
        localArrayList1 = new ArrayList();
        localArrayList2 = new ArrayList();
        localObject1 = getEnterTransition(localFragment1, bool2);
        localObject2 = getExitTransition(localFragment2, bool3);
        localObject3 = configureSharedElementsOptimized(localViewGroup, paramView, paramArrayMap, paramFragmentContainerTransition, localArrayList2, localArrayList1, localObject1, localObject2);
      }
      while ((localObject1 == null) && (localObject3 == null) && (localObject2 == null));
      localArrayList3 = configureEnteringExitingViews(localObject2, localFragment2, localArrayList2, paramView);
      localArrayList4 = configureEnteringExitingViews(localObject1, localFragment1, localArrayList1, paramView);
      setViewVisibility(localArrayList4, 4);
      localObject4 = mergeTransitions(localObject1, localObject2, localObject3, localFragment1, bool2);
    }
    while (localObject4 == null);
    replaceHide(localObject2, localFragment2, localArrayList3);
    ArrayList localArrayList5 = FragmentTransitionCompat21.prepareSetNameOverridesOptimized(localArrayList1);
    FragmentTransitionCompat21.scheduleRemoveTargets(localObject4, localObject1, localArrayList4, localObject2, localArrayList3, localObject3, localArrayList1);
    FragmentTransitionCompat21.beginDelayedTransition(localViewGroup, localObject4);
    FragmentTransitionCompat21.setNameOverridesOptimized(localViewGroup, localArrayList2, localArrayList1, localArrayList5, paramArrayMap);
    setViewVisibility(localArrayList4, 0);
    FragmentTransitionCompat21.swapSharedElementTargets(localObject3, localArrayList2, localArrayList1);
  }

  private static void configureTransitionsUnoptimized(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap)
  {
    boolean bool1 = paramFragmentManagerImpl.mContainer.onHasView();
    ViewGroup localViewGroup = null;
    if (bool1)
      localViewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    if (localViewGroup == null);
    Fragment localFragment1;
    Fragment localFragment2;
    Object localObject1;
    Object localObject2;
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    Object localObject3;
    do
    {
      return;
      localFragment1 = paramFragmentContainerTransition.lastIn;
      localFragment2 = paramFragmentContainerTransition.firstOut;
      boolean bool2 = paramFragmentContainerTransition.lastInIsPop;
      boolean bool3 = paramFragmentContainerTransition.firstOutIsPop;
      localObject1 = getEnterTransition(localFragment1, bool2);
      localObject2 = getExitTransition(localFragment2, bool3);
      localArrayList1 = new ArrayList();
      localArrayList2 = new ArrayList();
      localObject3 = configureSharedElementsUnoptimized(localViewGroup, paramView, paramArrayMap, paramFragmentContainerTransition, localArrayList1, localArrayList2, localObject1, localObject2);
    }
    while ((localObject1 == null) && (localObject3 == null) && (localObject2 == null));
    ArrayList localArrayList3 = configureEnteringExitingViews(localObject2, localFragment2, localArrayList1, paramView);
    if ((localArrayList3 == null) || (localArrayList3.isEmpty()));
    for (Object localObject4 = null; ; localObject4 = localObject2)
    {
      FragmentTransitionCompat21.addTarget(localObject1, paramView);
      Object localObject5 = mergeTransitions(localObject1, localObject4, localObject3, localFragment1, paramFragmentContainerTransition.lastInIsPop);
      if (localObject5 == null)
        break;
      ArrayList localArrayList4 = new ArrayList();
      FragmentTransitionCompat21.scheduleRemoveTargets(localObject5, localObject1, localArrayList4, localObject4, localArrayList3, localObject3, localArrayList2);
      scheduleTargetChange(localViewGroup, localFragment1, paramView, localArrayList2, localObject1, localArrayList4, localObject4, localArrayList3);
      FragmentTransitionCompat21.setNameOverridesUnoptimized(localViewGroup, localArrayList2, paramArrayMap);
      FragmentTransitionCompat21.beginDelayedTransition(localViewGroup, localObject5);
      FragmentTransitionCompat21.scheduleNameReset(localViewGroup, localArrayList2, paramArrayMap);
      return;
    }
  }

  private static FragmentContainerTransition ensureContainer(FragmentContainerTransition paramFragmentContainerTransition, SparseArray<FragmentContainerTransition> paramSparseArray, int paramInt)
  {
    if (paramFragmentContainerTransition == null)
    {
      paramFragmentContainerTransition = new FragmentContainerTransition();
      paramSparseArray.put(paramInt, paramFragmentContainerTransition);
    }
    return paramFragmentContainerTransition;
  }

  private static String findKeyForValue(ArrayMap<String, String> paramArrayMap, String paramString)
  {
    int i = paramArrayMap.size();
    for (int j = 0; j < i; j++)
      if (paramString.equals(paramArrayMap.valueAt(j)))
        return (String)paramArrayMap.keyAt(j);
    return null;
  }

  private static Object getEnterTransition(Fragment paramFragment, boolean paramBoolean)
  {
    if (paramFragment == null)
      return null;
    if (paramBoolean);
    for (Object localObject = paramFragment.getReenterTransition(); ; localObject = paramFragment.getEnterTransition())
      return FragmentTransitionCompat21.cloneTransition(localObject);
  }

  private static Object getExitTransition(Fragment paramFragment, boolean paramBoolean)
  {
    if (paramFragment == null)
      return null;
    if (paramBoolean);
    for (Object localObject = paramFragment.getReturnTransition(); ; localObject = paramFragment.getExitTransition())
      return FragmentTransitionCompat21.cloneTransition(localObject);
  }

  private static View getInEpicenterView(ArrayMap<String, View> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, Object paramObject, boolean paramBoolean)
  {
    BackStackRecord localBackStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if ((paramObject != null) && (localBackStackRecord.mSharedElementSourceNames != null) && (!localBackStackRecord.mSharedElementSourceNames.isEmpty()))
    {
      if (paramBoolean);
      for (String str = (String)localBackStackRecord.mSharedElementSourceNames.get(0); ; str = (String)localBackStackRecord.mSharedElementTargetNames.get(0))
        return (View)paramArrayMap.get(str);
    }
    return null;
  }

  private static Object getSharedElementTransition(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean)
  {
    if ((paramFragment1 == null) || (paramFragment2 == null))
      return null;
    if (paramBoolean);
    for (Object localObject = paramFragment2.getSharedElementReturnTransition(); ; localObject = paramFragment1.getSharedElementEnterTransition())
      return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(localObject));
  }

  private static Object mergeTransitions(Object paramObject1, Object paramObject2, Object paramObject3, Fragment paramFragment, boolean paramBoolean)
  {
    boolean bool = true;
    if ((paramObject1 != null) && (paramObject2 != null) && (paramFragment != null))
      if (!paramBoolean)
        break label38;
    label38: for (bool = paramFragment.getAllowReturnTransitionOverlap(); bool; bool = paramFragment.getAllowEnterTransitionOverlap())
      return FragmentTransitionCompat21.mergeTransitionsTogether(paramObject2, paramObject1, paramObject3);
    return FragmentTransitionCompat21.mergeTransitionsInSequence(paramObject2, paramObject1, paramObject3);
  }

  private static void replaceHide(Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList)
  {
    if ((paramFragment != null) && (paramObject != null) && (paramFragment.mAdded) && (paramFragment.mHidden) && (paramFragment.mHiddenChanged))
    {
      paramFragment.setHideReplaced(true);
      FragmentTransitionCompat21.scheduleHideFragmentView(paramObject, paramFragment.getView(), paramArrayList);
      OneShotPreDrawListener.add(paramFragment.mContainer, new Runnable(paramArrayList)
      {
        public void run()
        {
          FragmentTransition.access$000(this.val$exitingViews, 4);
        }
      });
    }
  }

  private static void retainValues(ArrayMap<String, String> paramArrayMap, ArrayMap<String, View> paramArrayMap1)
  {
    for (int i = -1 + paramArrayMap.size(); i >= 0; i--)
    {
      if (paramArrayMap1.containsKey((String)paramArrayMap.valueAt(i)))
        continue;
      paramArrayMap.removeAt(i);
    }
  }

  private static void scheduleTargetChange(ViewGroup paramViewGroup, Fragment paramFragment, View paramView, ArrayList<View> paramArrayList1, Object paramObject1, ArrayList<View> paramArrayList2, Object paramObject2, ArrayList<View> paramArrayList3)
  {
    OneShotPreDrawListener.add(paramViewGroup, new Runnable(paramObject1, paramView, paramFragment, paramArrayList1, paramArrayList2, paramArrayList3, paramObject2)
    {
      public void run()
      {
        if (this.val$enterTransition != null)
        {
          FragmentTransitionCompat21.removeTarget(this.val$enterTransition, this.val$nonExistentView);
          ArrayList localArrayList2 = FragmentTransition.access$100(this.val$enterTransition, this.val$inFragment, this.val$sharedElementsIn, this.val$nonExistentView);
          this.val$enteringViews.addAll(localArrayList2);
        }
        if (this.val$exitingViews != null)
        {
          if (this.val$exitTransition != null)
          {
            ArrayList localArrayList1 = new ArrayList();
            localArrayList1.add(this.val$nonExistentView);
            FragmentTransitionCompat21.replaceTargets(this.val$exitTransition, this.val$exitingViews, localArrayList1);
          }
          this.val$exitingViews.clear();
          this.val$exitingViews.add(this.val$nonExistentView);
        }
      }
    });
  }

  private static void setOutEpicenter(Object paramObject1, Object paramObject2, ArrayMap<String, View> paramArrayMap, boolean paramBoolean, BackStackRecord paramBackStackRecord)
  {
    if ((paramBackStackRecord.mSharedElementSourceNames != null) && (!paramBackStackRecord.mSharedElementSourceNames.isEmpty()))
      if (!paramBoolean)
        break label65;
    label65: for (String str = (String)paramBackStackRecord.mSharedElementTargetNames.get(0); ; str = (String)paramBackStackRecord.mSharedElementSourceNames.get(0))
    {
      View localView = (View)paramArrayMap.get(str);
      FragmentTransitionCompat21.setEpicenter(paramObject1, localView);
      if (paramObject2 != null)
        FragmentTransitionCompat21.setEpicenter(paramObject2, localView);
      return;
    }
  }

  private static void setViewVisibility(ArrayList<View> paramArrayList, int paramInt)
  {
    if (paramArrayList == null);
    while (true)
    {
      return;
      for (int i = -1 + paramArrayList.size(); i >= 0; i--)
        ((View)paramArrayList.get(i)).setVisibility(paramInt);
    }
  }

  static void startTransitions(FragmentManagerImpl paramFragmentManagerImpl, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramFragmentManagerImpl.mCurState < 1) || (Build.VERSION.SDK_INT < 21));
    SparseArray localSparseArray;
    do
    {
      return;
      localSparseArray = new SparseArray();
      int i = paramInt1;
      if (i >= paramInt2)
        continue;
      BackStackRecord localBackStackRecord = (BackStackRecord)paramArrayList.get(i);
      if (((Boolean)paramArrayList1.get(i)).booleanValue())
        calculatePopFragments(localBackStackRecord, localSparseArray, paramBoolean);
      while (true)
      {
        i++;
        break;
        calculateFragments(localBackStackRecord, localSparseArray, paramBoolean);
      }
    }
    while (localSparseArray.size() == 0);
    View localView = new View(paramFragmentManagerImpl.mHost.getContext());
    int j = localSparseArray.size();
    int k = 0;
    label123: int m;
    ArrayMap localArrayMap;
    FragmentContainerTransition localFragmentContainerTransition;
    if (k < j)
    {
      m = localSparseArray.keyAt(k);
      localArrayMap = calculateNameOverrides(m, paramArrayList, paramArrayList1, paramInt1, paramInt2);
      localFragmentContainerTransition = (FragmentContainerTransition)localSparseArray.valueAt(k);
      if (!paramBoolean)
        break label186;
      configureTransitionsOptimized(paramFragmentManagerImpl, m, localFragmentContainerTransition, localView, localArrayMap);
    }
    while (true)
    {
      k++;
      break label123;
      break;
      label186: configureTransitionsUnoptimized(paramFragmentManagerImpl, m, localFragmentContainerTransition, localView, localArrayMap);
    }
  }

  static class FragmentContainerTransition
  {
    public Fragment firstOut;
    public boolean firstOutIsPop;
    public BackStackRecord firstOutTransaction;
    public Fragment lastIn;
    public boolean lastInIsPop;
    public BackStackRecord lastInTransaction;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.FragmentTransition
 * JD-Core Version:    0.6.0
 */