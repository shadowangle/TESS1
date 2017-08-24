package android.support.v4.app;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@TargetApi(21)
@RequiresApi(21)
class FragmentTransitionCompat21
{
  public static void addTarget(Object paramObject, View paramView)
  {
    if (paramObject != null)
      ((Transition)paramObject).addTarget(paramView);
  }

  public static void addTargets(Object paramObject, ArrayList<View> paramArrayList)
  {
    int i = 0;
    Transition localTransition = (Transition)paramObject;
    if (localTransition == null);
    while (true)
    {
      return;
      if ((localTransition instanceof TransitionSet))
      {
        TransitionSet localTransitionSet = (TransitionSet)localTransition;
        int m = localTransitionSet.getTransitionCount();
        while (i < m)
        {
          addTargets(localTransitionSet.getTransitionAt(i), paramArrayList);
          i++;
        }
        continue;
      }
      if ((hasSimpleTarget(localTransition)) || (!isNullOrEmpty(localTransition.getTargets())))
        continue;
      int j = paramArrayList.size();
      for (int k = 0; k < j; k++)
        localTransition.addTarget((View)paramArrayList.get(k));
    }
  }

  public static void beginDelayedTransition(ViewGroup paramViewGroup, Object paramObject)
  {
    TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)paramObject);
  }

  private static void bfsAddViewChildren(List<View> paramList, View paramView)
  {
    int i = paramList.size();
    if (containedBeforeIndex(paramList, paramView, i));
    while (true)
    {
      return;
      paramList.add(paramView);
      for (int j = i; j < paramList.size(); j++)
      {
        View localView1 = (View)paramList.get(j);
        if (!(localView1 instanceof ViewGroup))
          continue;
        ViewGroup localViewGroup = (ViewGroup)localView1;
        int k = localViewGroup.getChildCount();
        for (int m = 0; m < k; m++)
        {
          View localView2 = localViewGroup.getChildAt(m);
          if (containedBeforeIndex(paramList, localView2, i))
            continue;
          paramList.add(localView2);
        }
      }
    }
  }

  public static void captureTransitioningViews(ArrayList<View> paramArrayList, View paramView)
  {
    ViewGroup localViewGroup;
    if (paramView.getVisibility() == 0)
    {
      if (!(paramView instanceof ViewGroup))
        break label65;
      localViewGroup = (ViewGroup)paramView;
      if (!localViewGroup.isTransitionGroup())
        break label33;
      paramArrayList.add(localViewGroup);
    }
    while (true)
    {
      return;
      label33: int i = localViewGroup.getChildCount();
      for (int j = 0; j < i; j++)
        captureTransitioningViews(paramArrayList, localViewGroup.getChildAt(j));
    }
    label65: paramArrayList.add(paramView);
  }

  public static Object cloneTransition(Object paramObject)
  {
    Transition localTransition = null;
    if (paramObject != null)
      localTransition = ((Transition)paramObject).clone();
    return localTransition;
  }

  private static boolean containedBeforeIndex(List<View> paramList, View paramView, int paramInt)
  {
    for (int i = 0; ; i++)
    {
      int j = 0;
      if (i < paramInt)
      {
        if (paramList.get(i) != paramView)
          continue;
        j = 1;
      }
      return j;
    }
  }

  private static String findKeyForValue(Map<String, String> paramMap, String paramString)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramString.equals(localEntry.getValue()))
        return (String)localEntry.getKey();
    }
    return null;
  }

  public static void findNamedViews(Map<String, View> paramMap, View paramView)
  {
    if (paramView.getVisibility() == 0)
    {
      String str = paramView.getTransitionName();
      if (str != null)
        paramMap.put(str, paramView);
      if ((paramView instanceof ViewGroup))
      {
        ViewGroup localViewGroup = (ViewGroup)paramView;
        int i = localViewGroup.getChildCount();
        for (int j = 0; j < i; j++)
          findNamedViews(paramMap, localViewGroup.getChildAt(j));
      }
    }
  }

  public static void getBoundsOnScreen(View paramView, Rect paramRect)
  {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    paramRect.set(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
  }

  private static boolean hasSimpleTarget(Transition paramTransition)
  {
    return (!isNullOrEmpty(paramTransition.getTargetIds())) || (!isNullOrEmpty(paramTransition.getTargetNames())) || (!isNullOrEmpty(paramTransition.getTargetTypes()));
  }

  private static boolean isNullOrEmpty(List paramList)
  {
    return (paramList == null) || (paramList.isEmpty());
  }

  public static Object mergeTransitionsInSequence(Object paramObject1, Object paramObject2, Object paramObject3)
  {
    Transition localTransition1 = (Transition)paramObject1;
    Transition localTransition2 = (Transition)paramObject2;
    Transition localTransition3 = (Transition)paramObject3;
    Object localObject;
    if ((localTransition1 != null) && (localTransition2 != null))
      localObject = new TransitionSet().addTransition(localTransition1).addTransition(localTransition2).setOrdering(1);
    while (localTransition3 != null)
    {
      TransitionSet localTransitionSet = new TransitionSet();
      if (localObject != null)
        localTransitionSet.addTransition((Transition)localObject);
      localTransitionSet.addTransition(localTransition3);
      return localTransitionSet;
      if (localTransition1 != null)
      {
        localObject = localTransition1;
        continue;
      }
      localObject = null;
      if (localTransition2 == null)
        continue;
      localObject = localTransition2;
    }
    return localObject;
  }

  public static Object mergeTransitionsTogether(Object paramObject1, Object paramObject2, Object paramObject3)
  {
    TransitionSet localTransitionSet = new TransitionSet();
    if (paramObject1 != null)
      localTransitionSet.addTransition((Transition)paramObject1);
    if (paramObject2 != null)
      localTransitionSet.addTransition((Transition)paramObject2);
    if (paramObject3 != null)
      localTransitionSet.addTransition((Transition)paramObject3);
    return localTransitionSet;
  }

  public static ArrayList<String> prepareSetNameOverridesOptimized(ArrayList<View> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      View localView = (View)paramArrayList.get(j);
      localArrayList.add(localView.getTransitionName());
      localView.setTransitionName(null);
    }
    return localArrayList;
  }

  public static void removeTarget(Object paramObject, View paramView)
  {
    if (paramObject != null)
      ((Transition)paramObject).removeTarget(paramView);
  }

  public static void replaceTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2)
  {
    int i = 0;
    Transition localTransition = (Transition)paramObject;
    if ((localTransition instanceof TransitionSet))
    {
      TransitionSet localTransitionSet = (TransitionSet)localTransition;
      int n = localTransitionSet.getTransitionCount();
      while (i < n)
      {
        replaceTargets(localTransitionSet.getTransitionAt(i), paramArrayList1, paramArrayList2);
        i++;
      }
    }
    if (!hasSimpleTarget(localTransition))
    {
      List localList = localTransition.getTargets();
      if ((localList != null) && (localList.size() == paramArrayList1.size()) && (localList.containsAll(paramArrayList1)))
      {
        if (paramArrayList2 == null);
        for (int j = 0; ; j = paramArrayList2.size())
          for (int k = 0; k < j; k++)
            localTransition.addTarget((View)paramArrayList2.get(k));
        for (int m = -1 + paramArrayList1.size(); m >= 0; m--)
          localTransition.removeTarget((View)paramArrayList1.get(m));
      }
    }
  }

  public static void scheduleHideFragmentView(Object paramObject, View paramView, ArrayList<View> paramArrayList)
  {
    ((Transition)paramObject).addListener(new Transition.TransitionListener(paramView, paramArrayList)
    {
      public void onTransitionCancel(Transition paramTransition)
      {
      }

      public void onTransitionEnd(Transition paramTransition)
      {
        paramTransition.removeListener(this);
        this.val$fragmentView.setVisibility(8);
        int i = this.val$exitingViews.size();
        for (int j = 0; j < i; j++)
          ((View)this.val$exitingViews.get(j)).setVisibility(0);
      }

      public void onTransitionPause(Transition paramTransition)
      {
      }

      public void onTransitionResume(Transition paramTransition)
      {
      }

      public void onTransitionStart(Transition paramTransition)
      {
      }
    });
  }

  public static void scheduleNameReset(ViewGroup paramViewGroup, ArrayList<View> paramArrayList, Map<String, String> paramMap)
  {
    OneShotPreDrawListener.add(paramViewGroup, new Runnable(paramArrayList, paramMap)
    {
      public void run()
      {
        int i = this.val$sharedElementsIn.size();
        for (int j = 0; j < i; j++)
        {
          View localView = (View)this.val$sharedElementsIn.get(j);
          String str = localView.getTransitionName();
          localView.setTransitionName((String)this.val$nameOverrides.get(str));
        }
      }
    });
  }

  public static void scheduleRemoveTargets(Object paramObject1, Object paramObject2, ArrayList<View> paramArrayList1, Object paramObject3, ArrayList<View> paramArrayList2, Object paramObject4, ArrayList<View> paramArrayList3)
  {
    ((Transition)paramObject1).addListener(new Transition.TransitionListener(paramObject2, paramArrayList1, paramObject3, paramArrayList2, paramObject4, paramArrayList3)
    {
      public void onTransitionCancel(Transition paramTransition)
      {
      }

      public void onTransitionEnd(Transition paramTransition)
      {
      }

      public void onTransitionPause(Transition paramTransition)
      {
      }

      public void onTransitionResume(Transition paramTransition)
      {
      }

      public void onTransitionStart(Transition paramTransition)
      {
        if (this.val$enterTransition != null)
          FragmentTransitionCompat21.replaceTargets(this.val$enterTransition, this.val$enteringViews, null);
        if (this.val$exitTransition != null)
          FragmentTransitionCompat21.replaceTargets(this.val$exitTransition, this.val$exitingViews, null);
        if (this.val$sharedElementTransition != null)
          FragmentTransitionCompat21.replaceTargets(this.val$sharedElementTransition, this.val$sharedElementsIn, null);
      }
    });
  }

  public static void setEpicenter(Object paramObject, Rect paramRect)
  {
    if (paramObject != null)
      ((Transition)paramObject).setEpicenterCallback(new Transition.EpicenterCallback(paramRect)
      {
        public Rect onGetEpicenter(Transition paramTransition)
        {
          if ((this.val$epicenter == null) || (this.val$epicenter.isEmpty()))
            return null;
          return this.val$epicenter;
        }
      });
  }

  public static void setEpicenter(Object paramObject, View paramView)
  {
    if (paramView != null)
    {
      Transition localTransition = (Transition)paramObject;
      Rect localRect = new Rect();
      getBoundsOnScreen(paramView, localRect);
      localTransition.setEpicenterCallback(new Transition.EpicenterCallback(localRect)
      {
        public Rect onGetEpicenter(Transition paramTransition)
        {
          return this.val$epicenter;
        }
      });
    }
  }

  public static void setNameOverridesOptimized(View paramView, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, ArrayList<String> paramArrayList, Map<String, String> paramMap)
  {
    int i = paramArrayList2.size();
    ArrayList localArrayList = new ArrayList();
    int j = 0;
    if (j < i)
    {
      View localView = (View)paramArrayList1.get(j);
      String str1 = localView.getTransitionName();
      localArrayList.add(str1);
      if (str1 == null);
      label127: 
      while (true)
      {
        j++;
        break;
        localView.setTransitionName(null);
        String str2 = (String)paramMap.get(str1);
        for (int k = 0; ; k++)
        {
          if (k >= i)
            break label127;
          if (!str2.equals(paramArrayList.get(k)))
            continue;
          ((View)paramArrayList2.get(k)).setTransitionName(str1);
          break;
        }
      }
    }
    OneShotPreDrawListener.add(paramView, new Runnable(i, paramArrayList2, paramArrayList, paramArrayList1, localArrayList)
    {
      public void run()
      {
        for (int i = 0; i < this.val$numSharedElements; i++)
        {
          ((View)this.val$sharedElementsIn.get(i)).setTransitionName((String)this.val$inNames.get(i));
          ((View)this.val$sharedElementsOut.get(i)).setTransitionName((String)this.val$outNames.get(i));
        }
      }
    });
  }

  public static void setNameOverridesUnoptimized(View paramView, ArrayList<View> paramArrayList, Map<String, String> paramMap)
  {
    OneShotPreDrawListener.add(paramView, new Runnable(paramArrayList, paramMap)
    {
      public void run()
      {
        int i = this.val$sharedElementsIn.size();
        for (int j = 0; j < i; j++)
        {
          View localView = (View)this.val$sharedElementsIn.get(j);
          String str = localView.getTransitionName();
          if (str == null)
            continue;
          localView.setTransitionName(FragmentTransitionCompat21.access$000(this.val$nameOverrides, str));
        }
      }
    });
  }

  public static void setSharedElementTargets(Object paramObject, View paramView, ArrayList<View> paramArrayList)
  {
    TransitionSet localTransitionSet = (TransitionSet)paramObject;
    List localList = localTransitionSet.getTargets();
    localList.clear();
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
      bfsAddViewChildren(localList, (View)paramArrayList.get(j));
    localList.add(paramView);
    paramArrayList.add(paramView);
    addTargets(localTransitionSet, paramArrayList);
  }

  public static void swapSharedElementTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2)
  {
    TransitionSet localTransitionSet = (TransitionSet)paramObject;
    if (localTransitionSet != null)
    {
      localTransitionSet.getTargets().clear();
      localTransitionSet.getTargets().addAll(paramArrayList2);
      replaceTargets(localTransitionSet, paramArrayList1, paramArrayList2);
    }
  }

  public static Object wrapTransitionInSet(Object paramObject)
  {
    if (paramObject == null)
      return null;
    TransitionSet localTransitionSet = new TransitionSet();
    localTransitionSet.addTransition((Transition)paramObject);
    return localTransitionSet;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.FragmentTransitionCompat21
 * JD-Core Version:    0.6.0
 */