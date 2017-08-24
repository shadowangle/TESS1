package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v4.os.BuildCompat;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager
  implements LayoutInflaterFactory
{
  static final Interpolator ACCELERATE_CUBIC;
  static final Interpolator ACCELERATE_QUINT;
  static final int ANIM_DUR = 220;
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  static boolean DEBUG = false;
  static final Interpolator DECELERATE_CUBIC;
  static final Interpolator DECELERATE_QUINT;
  static final boolean HONEYCOMB = false;
  static final String TAG = "FragmentManager";
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  static final String TARGET_STATE_TAG = "android:target_state";
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  static final String VIEW_STATE_TAG = "android:view_state";
  static Field sAnimationListenerField;
  ArrayList<Fragment> mActive;
  ArrayList<Fragment> mAdded;
  ArrayList<Integer> mAvailBackStackIndices;
  ArrayList<Integer> mAvailIndices;
  ArrayList<BackStackRecord> mBackStack;
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  ArrayList<BackStackRecord> mBackStackIndices;
  FragmentContainer mContainer;
  ArrayList<Fragment> mCreatedMenus;
  int mCurState = 0;
  boolean mDestroyed;
  Runnable mExecCommit = new Runnable()
  {
    public void run()
    {
      FragmentManagerImpl.this.execPendingActions();
    }
  };
  boolean mExecutingActions;
  boolean mHavePendingDeferredStart;
  FragmentHostCallback mHost;
  private CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks;
  boolean mNeedMenuInvalidate;
  String mNoTransactionsBecause;
  Fragment mParent;
  ArrayList<OpGenerator> mPendingActions;
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  SparseArray<Parcelable> mStateArray = null;
  Bundle mStateBundle = null;
  boolean mStateSaved;
  Runnable[] mTmpActions;
  ArrayList<Fragment> mTmpAddedFragments;
  ArrayList<Boolean> mTmpIsPop;
  ArrayList<BackStackRecord> mTmpRecords;

  static
  {
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 11)
      bool = true;
    HONEYCOMB = bool;
    sAnimationListenerField = null;
    DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
    DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
    ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
    ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
  }

  private void addAddedFragments(ArraySet<Fragment> paramArraySet)
  {
    if (this.mCurState < 1)
      return;
    int i = Math.min(this.mCurState, 4);
    if (this.mAdded == null);
    for (int j = 0; ; j = this.mAdded.size())
    {
      for (int k = 0; k < j; k++)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(k);
        if (localFragment.mState >= i)
          continue;
        moveToState(localFragment, i, localFragment.getNextAnim(), localFragment.getNextTransition(), false);
        if ((localFragment.mView == null) || (localFragment.mHidden) || (!localFragment.mIsNewlyAdded))
          continue;
        paramArraySet.add(localFragment);
      }
      break;
    }
  }

  private void checkStateLoss()
  {
    if (this.mStateSaved)
      throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
    if (this.mNoTransactionsBecause != null)
      throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
  }

  private void cleanupExec()
  {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }

  private void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    ArrayList localArrayList1 = new ArrayList(1);
    ArrayList localArrayList2 = new ArrayList(1);
    localArrayList1.add(paramBackStackRecord);
    localArrayList2.add(Boolean.valueOf(paramBoolean1));
    executeOps(localArrayList1, localArrayList2, 0, 1);
    if (paramBoolean2)
      FragmentTransition.startTransitions(this, localArrayList1, localArrayList2, 0, 1, true);
    if (paramBoolean3)
      moveToState(this.mCurState, true);
    if (this.mActive != null)
    {
      int i = this.mActive.size();
      int j = 0;
      if (j < i)
      {
        Fragment localFragment = (Fragment)this.mActive.get(j);
        if ((localFragment != null) && (localFragment.mView != null) && (localFragment.mIsNewlyAdded) && (paramBackStackRecord.interactsWith(localFragment.mContainerId)))
        {
          if ((Build.VERSION.SDK_INT >= 11) && (localFragment.mPostponedAlpha > 0.0F))
            localFragment.mView.setAlpha(localFragment.mPostponedAlpha);
          if (!paramBoolean3)
            break label196;
          localFragment.mPostponedAlpha = 0.0F;
        }
        while (true)
        {
          j++;
          break;
          label196: localFragment.mPostponedAlpha = -1.0F;
          localFragment.mIsNewlyAdded = false;
        }
      }
    }
  }

  private void endAnimatingAwayFragments()
  {
    if (this.mActive == null);
    for (int i = 0; ; i = this.mActive.size())
      for (int j = 0; j < i; j++)
      {
        Fragment localFragment = (Fragment)this.mActive.get(j);
        if ((localFragment == null) || (localFragment.getAnimatingAway() == null))
          continue;
        int k = localFragment.getStateAfterAnimating();
        View localView = localFragment.getAnimatingAway();
        localFragment.setAnimatingAway(null);
        Animation localAnimation = localView.getAnimation();
        if (localAnimation != null)
          localAnimation.cancel();
        moveToState(localFragment, k, 0, 0, false);
      }
  }

  private void ensureExecReady(boolean paramBoolean)
  {
    if (this.mExecutingActions)
      throw new IllegalStateException("FragmentManager is already executing transactions");
    if (Looper.myLooper() != this.mHost.getHandler().getLooper())
      throw new IllegalStateException("Must be called from main thread of fragment host");
    if (!paramBoolean)
      checkStateLoss();
    if (this.mTmpRecords == null)
    {
      this.mTmpRecords = new ArrayList();
      this.mTmpIsPop = new ArrayList();
    }
    executePostponedTransaction(null, null);
  }

  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
    {
      BackStackRecord localBackStackRecord = (BackStackRecord)paramArrayList.get(paramInt1);
      if (((Boolean)paramArrayList1.get(paramInt1)).booleanValue())
        localBackStackRecord.executePopOps();
      while (true)
      {
        paramInt1++;
        break;
        localBackStackRecord.executeOps();
      }
    }
  }

  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2)
  {
    boolean bool1 = ((BackStackRecord)paramArrayList.get(paramInt1)).mAllowOptimization;
    int i;
    int j;
    label56: BackStackRecord localBackStackRecord2;
    label102: int m;
    if (this.mTmpAddedFragments == null)
    {
      this.mTmpAddedFragments = new ArrayList();
      if (this.mAdded != null)
        this.mTmpAddedFragments.addAll(this.mAdded);
      i = paramInt1;
      j = 0;
      if (i >= paramInt2)
        break label177;
      localBackStackRecord2 = (BackStackRecord)paramArrayList.get(i);
      boolean bool2 = ((Boolean)paramArrayList1.get(i)).booleanValue();
      if (bool2)
        break label153;
      localBackStackRecord2.expandReplaceOps(this.mTmpAddedFragments);
      if (!bool2)
        break label165;
      m = -1;
      label110: localBackStackRecord2.bumpBackStackNesting(m);
      if ((j == 0) && (!localBackStackRecord2.mAddToBackStack))
        break label171;
    }
    label153: label165: label171: for (int n = 1; ; n = 0)
    {
      i++;
      j = n;
      break label56;
      this.mTmpAddedFragments.clear();
      break;
      localBackStackRecord2.trackAddedFragmentsInPop(this.mTmpAddedFragments);
      break label102;
      m = 1;
      break label110;
    }
    label177: this.mTmpAddedFragments.clear();
    if (!bool1)
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, paramInt2, false);
    executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
    int k;
    if (bool1)
    {
      ArraySet localArraySet = new ArraySet();
      addAddedFragments(localArraySet);
      k = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2, localArraySet);
      makeRemovedFragmentsInvisible(localArraySet);
    }
    while (true)
    {
      if ((k != paramInt1) && (bool1))
      {
        FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, k, true);
        moveToState(this.mCurState, true);
      }
      while (paramInt1 < paramInt2)
      {
        BackStackRecord localBackStackRecord1 = (BackStackRecord)paramArrayList.get(paramInt1);
        if ((((Boolean)paramArrayList1.get(paramInt1)).booleanValue()) && (localBackStackRecord1.mIndex >= 0))
        {
          freeBackStackIndex(localBackStackRecord1.mIndex);
          localBackStackRecord1.mIndex = -1;
        }
        paramInt1++;
      }
      if (j != 0)
        reportBackStackChanged();
      return;
      k = paramInt2;
    }
  }

  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    int i;
    int j;
    int k;
    label15: StartEnterTransitionListener localStartEnterTransitionListener;
    int i1;
    int i2;
    if (this.mPostponedTransactions == null)
    {
      i = 0;
      j = i;
      k = 0;
      if (k >= j)
        break label244;
      localStartEnterTransitionListener = (StartEnterTransitionListener)this.mPostponedTransactions.get(k);
      if ((paramArrayList == null) || (localStartEnterTransitionListener.mIsBack))
        break label117;
      int i4 = paramArrayList.indexOf(localStartEnterTransitionListener.mRecord);
      if ((i4 == -1) || (!((Boolean)paramArrayList1.get(i4)).booleanValue()))
        break label117;
      localStartEnterTransitionListener.cancelTransaction();
      i1 = j;
      i2 = k;
    }
    while (true)
    {
      k = i2 + 1;
      j = i1;
      break label15;
      i = this.mPostponedTransactions.size();
      break;
      label117: if ((localStartEnterTransitionListener.isReady()) || ((paramArrayList != null) && (localStartEnterTransitionListener.mRecord.interactsWith(paramArrayList, 0, paramArrayList.size()))))
      {
        this.mPostponedTransactions.remove(k);
        int m = k - 1;
        int n = j - 1;
        if ((paramArrayList != null) && (!localStartEnterTransitionListener.mIsBack))
        {
          int i3 = paramArrayList.indexOf(localStartEnterTransitionListener.mRecord);
          if ((i3 != -1) && (((Boolean)paramArrayList1.get(i3)).booleanValue()))
          {
            localStartEnterTransitionListener.cancelTransaction();
            i1 = n;
            i2 = m;
            continue;
          }
        }
        localStartEnterTransitionListener.completeTransaction();
        i1 = n;
        i2 = m;
        continue;
        return;
      }
      label244: i1 = j;
      i2 = k;
    }
  }

  private Fragment findFragmentUnder(Fragment paramFragment)
  {
    ViewGroup localViewGroup = paramFragment.mContainer;
    View localView = paramFragment.mView;
    Fragment localFragment;
    if ((localViewGroup == null) || (localView == null))
    {
      localFragment = null;
      return localFragment;
    }
    for (int i = -1 + this.mAdded.indexOf(paramFragment); ; i--)
    {
      if (i < 0)
        break label78;
      localFragment = (Fragment)this.mAdded.get(i);
      if ((localFragment.mContainer == localViewGroup) && (localFragment.mView != null))
        break;
    }
    label78: return null;
  }

  private void forcePostponedTransactions()
  {
    if (this.mPostponedTransactions != null)
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();
  }

  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    monitorenter;
    try
    {
      if ((this.mPendingActions == null) || (this.mPendingActions.size() == 0))
        return false;
      int i = this.mPendingActions.size();
      for (int j = 0; j < i; j++)
        ((OpGenerator)this.mPendingActions.get(j)).generateOps(paramArrayList, paramArrayList1);
      this.mPendingActions.clear();
      this.mHost.getHandler().removeCallbacks(this.mExecCommit);
      monitorexit;
      if (i > 0)
        return true;
    }
    finally
    {
      monitorexit;
    }
    return false;
  }

  static Animation makeFadeAnimation(Context paramContext, float paramFloat1, float paramFloat2)
  {
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    localAlphaAnimation.setInterpolator(DECELERATE_CUBIC);
    localAlphaAnimation.setDuration(220L);
    return localAlphaAnimation;
  }

  static Animation makeOpenCloseAnimation(Context paramContext, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    AnimationSet localAnimationSet = new AnimationSet(false);
    ScaleAnimation localScaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    localScaleAnimation.setInterpolator(DECELERATE_QUINT);
    localScaleAnimation.setDuration(220L);
    localAnimationSet.addAnimation(localScaleAnimation);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    localAlphaAnimation.setInterpolator(DECELERATE_CUBIC);
    localAlphaAnimation.setDuration(220L);
    localAnimationSet.addAnimation(localAlphaAnimation);
    return localAnimationSet;
  }

  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet)
  {
    int i = paramArraySet.size();
    int j = 0;
    if (j < i)
    {
      Fragment localFragment = (Fragment)paramArraySet.valueAt(j);
      View localView;
      if (!localFragment.mAdded)
      {
        localView = localFragment.getView();
        if (Build.VERSION.SDK_INT >= 11)
          break label60;
        localFragment.getView().setVisibility(4);
      }
      while (true)
      {
        j++;
        break;
        label60: localFragment.mPostponedAlpha = localView.getAlpha();
        localView.setAlpha(0.0F);
      }
    }
  }

  static boolean modifiesAlpha(Animation paramAnimation)
  {
    int i;
    if ((paramAnimation instanceof AlphaAnimation))
      i = 1;
    boolean bool;
    do
    {
      return i;
      bool = paramAnimation instanceof AnimationSet;
      i = 0;
    }
    while (!bool);
    List localList = ((AnimationSet)paramAnimation).getAnimations();
    for (int j = 0; ; j++)
    {
      int k = localList.size();
      i = 0;
      if (j >= k)
        break;
      if ((localList.get(j) instanceof AlphaAnimation))
        break label70;
    }
    label70: return true;
  }

  private void optimizeAndExecuteOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    int i = 0;
    if ((paramArrayList == null) || (paramArrayList.isEmpty()))
      return;
    if ((paramArrayList1 == null) || (paramArrayList.size() != paramArrayList1.size()))
      throw new IllegalStateException("Internal error with the back stack records");
    executePostponedTransaction(paramArrayList, paramArrayList1);
    int j = paramArrayList.size();
    int k = 0;
    label55: int i2;
    if (k < j)
    {
      if (((BackStackRecord)paramArrayList.get(k)).mAllowOptimization)
        break label208;
      if (i != k)
        executeOpsTogether(paramArrayList, paramArrayList1, i, k);
      int n = k + 1;
      if (((Boolean)paramArrayList1.get(k)).booleanValue())
        while ((n < j) && (((Boolean)paramArrayList1.get(n)).booleanValue()) && (!((BackStackRecord)paramArrayList.get(n)).mAllowOptimization))
          n++;
      int i1 = n;
      executeOpsTogether(paramArrayList, paramArrayList1, k, i1);
      i2 = i1 - 1;
      i = i1;
    }
    label208: for (int m = i2; ; m = k)
    {
      k = m + 1;
      break label55;
      if (i == j)
        break;
      executeOpsTogether(paramArrayList, paramArrayList1, i, j);
      return;
    }
  }

  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2)
  {
    execPendingActions();
    ensureExecReady(true);
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool)
      this.mExecutingActions = true;
    try
    {
      optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
      cleanupExec();
      doPendingDeferredStart();
      return bool;
    }
    finally
    {
      cleanupExec();
    }
    throw localObject;
  }

  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet)
  {
    int i = paramInt2 - 1;
    int j = paramInt2;
    int k = i;
    BackStackRecord localBackStackRecord;
    int m;
    label71: int i2;
    if (k >= paramInt1)
    {
      localBackStackRecord = (BackStackRecord)paramArrayList.get(k);
      boolean bool = ((Boolean)paramArrayList1.get(k)).booleanValue();
      if ((localBackStackRecord.isPostponed()) && (!localBackStackRecord.interactsWith(paramArrayList, k + 1, paramInt2)))
      {
        m = 1;
        if (m == 0)
          break label206;
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList();
        StartEnterTransitionListener localStartEnterTransitionListener = new StartEnterTransitionListener(localBackStackRecord, bool);
        this.mPostponedTransactions.add(localStartEnterTransitionListener);
        localBackStackRecord.setOnStartPostponedListener(localStartEnterTransitionListener);
        if (!bool)
          break label195;
        localBackStackRecord.executeOps();
        label134: i2 = j - 1;
        if (k != i2)
        {
          paramArrayList.remove(k);
          paramArrayList.add(i2, localBackStackRecord);
        }
        addAddedFragments(paramArraySet);
      }
    }
    label195: label206: for (int n = i2; ; n = j)
    {
      int i1 = k - 1;
      j = n;
      k = i1;
      break;
      m = 0;
      break label71;
      localBackStackRecord.executePopOps();
      break label134;
      return j;
    }
  }

  public static int reverseTransit(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 0;
    case 4097:
      return 8194;
    case 8194:
      return 4097;
    case 4099:
    }
    return 4099;
  }

  private void scheduleCommit()
  {
    int i = 1;
    monitorenter;
    label44: label73: label81: label97: label100: 
    while (true)
    {
      int j;
      try
      {
        if ((this.mPostponedTransactions != null) && (!this.mPostponedTransactions.isEmpty()))
        {
          j = i;
          if ((this.mPendingActions == null) || (this.mPendingActions.size() != i))
            break label97;
          break label81;
          this.mHost.getHandler().removeCallbacks(this.mExecCommit);
          this.mHost.getHandler().post(this.mExecCommit);
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      while (true)
      {
        if (j != 0)
          break label100;
        if (i == 0)
          break label73;
        break label44;
        j = 0;
        break;
        i = 0;
      }
    }
  }

  private void setHWLayerAnimListenerIfAlpha(View paramView, Animation paramAnimation)
  {
    if ((paramView == null) || (paramAnimation == null));
    do
      return;
    while (!shouldRunOnHWLayer(paramView, paramAnimation));
    try
    {
      if (sAnimationListenerField == null)
      {
        sAnimationListenerField = Animation.class.getDeclaredField("mListener");
        sAnimationListenerField.setAccessible(true);
      }
      localAnimationListener = (Animation.AnimationListener)sAnimationListenerField.get(paramAnimation);
      ViewCompat.setLayerType(paramView, 2, null);
      paramAnimation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(paramView, paramAnimation, localAnimationListener));
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      while (true)
      {
        Log.e("FragmentManager", "No field with the name mListener is found in Animation class", localNoSuchFieldException);
        localAnimationListener = null;
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      while (true)
      {
        Log.e("FragmentManager", "Cannot access Animation's mListener field", localIllegalAccessException);
        Animation.AnimationListener localAnimationListener = null;
      }
    }
  }

  static boolean shouldRunOnHWLayer(View paramView, Animation paramAnimation)
  {
    return (Build.VERSION.SDK_INT >= 19) && (ViewCompat.getLayerType(paramView) == 0) && (ViewCompat.hasOverlappingRendering(paramView)) && (modifiesAlpha(paramAnimation));
  }

  private void throwException(RuntimeException paramRuntimeException)
  {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter localPrintWriter = new PrintWriter(new LogWriter("FragmentManager"));
    if (this.mHost != null);
    while (true)
    {
      try
      {
        this.mHost.onDump("  ", null, localPrintWriter, new String[0]);
        throw paramRuntimeException;
      }
      catch (Exception localException2)
      {
        Log.e("FragmentManager", "Failed dumping state", localException2);
        continue;
      }
      try
      {
        dump("  ", null, localPrintWriter, new String[0]);
      }
      catch (Exception localException1)
      {
        Log.e("FragmentManager", "Failed dumping state", localException1);
      }
    }
  }

  public static int transitToStyleIndex(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    default:
      return -1;
    case 4097:
      if (paramBoolean)
        return 1;
      return 2;
    case 8194:
      if (paramBoolean)
        return 3;
      return 4;
    case 4099:
    }
    if (paramBoolean)
      return 5;
    return 6;
  }

  void addBackStackState(BackStackRecord paramBackStackRecord)
  {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList();
    this.mBackStack.add(paramBackStackRecord);
    reportBackStackChanged();
  }

  public void addFragment(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mAdded == null)
      this.mAdded = new ArrayList();
    if (DEBUG)
      Log.v("FragmentManager", "add: " + paramFragment);
    makeActive(paramFragment);
    if (!paramFragment.mDetached)
    {
      if (this.mAdded.contains(paramFragment))
        throw new IllegalStateException("Fragment already added: " + paramFragment);
      this.mAdded.add(paramFragment);
      paramFragment.mAdded = true;
      paramFragment.mRemoving = false;
      if (paramFragment.mView == null)
        paramFragment.mHiddenChanged = false;
      if ((paramFragment.mHasMenu) && (paramFragment.mMenuVisible))
        this.mNeedMenuInvalidate = true;
      if (paramBoolean)
        moveToState(paramFragment);
    }
  }

  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener)
  {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList();
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }

  public int allocBackStackIndex(BackStackRecord paramBackStackRecord)
  {
    monitorenter;
    try
    {
      if ((this.mAvailBackStackIndices == null) || (this.mAvailBackStackIndices.size() <= 0))
      {
        if (this.mBackStackIndices == null)
          this.mBackStackIndices = new ArrayList();
        int i = this.mBackStackIndices.size();
        if (DEBUG)
          Log.v("FragmentManager", "Setting back stack index " + i + " to " + paramBackStackRecord);
        this.mBackStackIndices.add(paramBackStackRecord);
        return i;
      }
      int j = ((Integer)this.mAvailBackStackIndices.remove(-1 + this.mAvailBackStackIndices.size())).intValue();
      if (DEBUG)
        Log.v("FragmentManager", "Adding back stack index " + j + " with " + paramBackStackRecord);
      this.mBackStackIndices.set(j, paramBackStackRecord);
      return j;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment)
  {
    if (this.mHost != null)
      throw new IllegalStateException("Already attached");
    this.mHost = paramFragmentHostCallback;
    this.mContainer = paramFragmentContainer;
    this.mParent = paramFragment;
  }

  public void attachFragment(Fragment paramFragment)
  {
    if (DEBUG)
      Log.v("FragmentManager", "attach: " + paramFragment);
    if (paramFragment.mDetached)
    {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded)
      {
        if (this.mAdded == null)
          this.mAdded = new ArrayList();
        if (this.mAdded.contains(paramFragment))
          throw new IllegalStateException("Fragment already added: " + paramFragment);
        if (DEBUG)
          Log.v("FragmentManager", "add from attach: " + paramFragment);
        this.mAdded.add(paramFragment);
        paramFragment.mAdded = true;
        if ((paramFragment.mHasMenu) && (paramFragment.mMenuVisible))
          this.mNeedMenuInvalidate = true;
      }
    }
  }

  public FragmentTransaction beginTransaction()
  {
    return new BackStackRecord(this);
  }

  void completeShowHideFragment(Fragment paramFragment)
  {
    boolean bool;
    if (paramFragment.mView != null)
    {
      int i = paramFragment.getNextTransition();
      if (paramFragment.mHidden)
        break label152;
      bool = true;
      Animation localAnimation = loadAnimation(paramFragment, i, bool, paramFragment.getNextTransitionStyle());
      if (localAnimation != null)
      {
        setHWLayerAnimListenerIfAlpha(paramFragment.mView, localAnimation);
        paramFragment.mView.startAnimation(localAnimation);
        setHWLayerAnimListenerIfAlpha(paramFragment.mView, localAnimation);
        localAnimation.start();
      }
      if ((!paramFragment.mHidden) || (paramFragment.isHideReplaced()))
        break label157;
    }
    label152: label157: for (int j = 8; ; j = 0)
    {
      paramFragment.mView.setVisibility(j);
      if (paramFragment.isHideReplaced())
        paramFragment.setHideReplaced(false);
      if ((paramFragment.mAdded) && (paramFragment.mHasMenu) && (paramFragment.mMenuVisible))
        this.mNeedMenuInvalidate = true;
      paramFragment.mHiddenChanged = false;
      paramFragment.onHiddenChanged(paramFragment.mHidden);
      return;
      bool = false;
      break;
    }
  }

  public void detachFragment(Fragment paramFragment)
  {
    if (DEBUG)
      Log.v("FragmentManager", "detach: " + paramFragment);
    if (!paramFragment.mDetached)
    {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded)
      {
        if (this.mAdded != null)
        {
          if (DEBUG)
            Log.v("FragmentManager", "remove from detach: " + paramFragment);
          this.mAdded.remove(paramFragment);
        }
        if ((paramFragment.mHasMenu) && (paramFragment.mMenuVisible))
          this.mNeedMenuInvalidate = true;
        paramFragment.mAdded = false;
      }
    }
  }

  public void dispatchActivityCreated()
  {
    this.mStateSaved = false;
    moveToState(2, false);
  }

  public void dispatchConfigurationChanged(Configuration paramConfiguration)
  {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(i);
        if (localFragment == null)
          continue;
        localFragment.performConfigurationChanged(paramConfiguration);
      }
  }

  public boolean dispatchContextItemSelected(MenuItem paramMenuItem)
  {
    ArrayList localArrayList = this.mAdded;
    int i = 0;
    if (localArrayList != null);
    for (int j = 0; ; j++)
    {
      int k = this.mAdded.size();
      i = 0;
      if (j < k)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(j);
        if ((localFragment == null) || (!localFragment.performContextItemSelected(paramMenuItem)))
          continue;
        i = 1;
      }
      return i;
    }
  }

  public void dispatchCreate()
  {
    this.mStateSaved = false;
    moveToState(1, false);
  }

  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    if (this.mAdded != null)
    {
      localObject1 = null;
      i = 0;
      int k = 0;
      while (k < this.mAdded.size())
      {
        Fragment localFragment2 = (Fragment)this.mAdded.get(k);
        if ((localFragment2 != null) && (localFragment2.performCreateOptionsMenu(paramMenu, paramMenuInflater)))
        {
          i = 1;
          if (localObject1 == null)
            localObject1 = new ArrayList();
          ((ArrayList)localObject1).add(localFragment2);
        }
        Object localObject2 = localObject1;
        int m = i;
        k++;
        i = m;
        localObject1 = localObject2;
      }
    }
    Object localObject1 = null;
    int i = 0;
    ArrayList localArrayList = this.mCreatedMenus;
    int j = 0;
    if (localArrayList != null)
      while (j < this.mCreatedMenus.size())
      {
        Fragment localFragment1 = (Fragment)this.mCreatedMenus.get(j);
        if ((localObject1 == null) || (!((ArrayList)localObject1).contains(localFragment1)))
          localFragment1.onDestroyOptionsMenu();
        j++;
      }
    this.mCreatedMenus = ((ArrayList)localObject1);
    return i;
  }

  public void dispatchDestroy()
  {
    this.mDestroyed = true;
    execPendingActions();
    moveToState(0, false);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
  }

  public void dispatchDestroyView()
  {
    moveToState(1, false);
  }

  public void dispatchLowMemory()
  {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(i);
        if (localFragment == null)
          continue;
        localFragment.performLowMemory();
      }
  }

  public void dispatchMultiWindowModeChanged(boolean paramBoolean)
  {
    if (this.mAdded == null);
    while (true)
    {
      return;
      for (int i = -1 + this.mAdded.size(); i >= 0; i--)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(i);
        if (localFragment == null)
          continue;
        localFragment.performMultiWindowModeChanged(paramBoolean);
      }
    }
  }

  void dispatchOnFragmentActivityCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentActivityCreated(this, paramFragment, paramBundle);
      }
    }
  }

  void dispatchOnFragmentAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentAttached(paramFragment, paramContext, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentAttached(this, paramFragment, paramContext);
      }
    }
  }

  void dispatchOnFragmentCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentCreated(paramFragment, paramBundle, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentCreated(this, paramFragment, paramBundle);
      }
    }
  }

  void dispatchOnFragmentDestroyed(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentDestroyed(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentDestroyed(this, paramFragment);
      }
    }
  }

  void dispatchOnFragmentDetached(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentDetached(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentDetached(this, paramFragment);
      }
    }
  }

  void dispatchOnFragmentPaused(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentPaused(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentPaused(this, paramFragment);
      }
    }
  }

  void dispatchOnFragmentPreAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentPreAttached(paramFragment, paramContext, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentPreAttached(this, paramFragment, paramContext);
      }
    }
  }

  void dispatchOnFragmentResumed(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentResumed(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentResumed(this, paramFragment);
      }
    }
  }

  void dispatchOnFragmentSaveInstanceState(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentSaveInstanceState(this, paramFragment, paramBundle);
      }
    }
  }

  void dispatchOnFragmentStarted(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentStarted(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentStarted(this, paramFragment);
      }
    }
  }

  void dispatchOnFragmentStopped(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentStopped(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentStopped(this, paramFragment);
      }
    }
  }

  void dispatchOnFragmentViewCreated(Fragment paramFragment, View paramView, Bundle paramBundle, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentViewCreated(this, paramFragment, paramView, paramBundle);
      }
    }
  }

  void dispatchOnFragmentViewDestroyed(Fragment paramFragment, boolean paramBoolean)
  {
    if (this.mParent != null)
    {
      FragmentManager localFragmentManager = this.mParent.getFragmentManager();
      if ((localFragmentManager instanceof FragmentManagerImpl))
        ((FragmentManagerImpl)localFragmentManager).dispatchOnFragmentViewDestroyed(paramFragment, true);
    }
    if (this.mLifecycleCallbacks == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mLifecycleCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if ((paramBoolean) && (!((Boolean)localPair.second).booleanValue()))
          continue;
        ((FragmentManager.FragmentLifecycleCallbacks)localPair.first).onFragmentViewDestroyed(this, paramFragment);
      }
    }
  }

  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem)
  {
    ArrayList localArrayList = this.mAdded;
    int i = 0;
    if (localArrayList != null);
    for (int j = 0; ; j++)
    {
      int k = this.mAdded.size();
      i = 0;
      if (j < k)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(j);
        if ((localFragment == null) || (!localFragment.performOptionsItemSelected(paramMenuItem)))
          continue;
        i = 1;
      }
      return i;
    }
  }

  public void dispatchOptionsMenuClosed(Menu paramMenu)
  {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(i);
        if (localFragment == null)
          continue;
        localFragment.performOptionsMenuClosed(paramMenu);
      }
  }

  public void dispatchPause()
  {
    moveToState(4, false);
  }

  public void dispatchPictureInPictureModeChanged(boolean paramBoolean)
  {
    if (this.mAdded == null);
    while (true)
    {
      return;
      for (int i = -1 + this.mAdded.size(); i >= 0; i--)
      {
        Fragment localFragment = (Fragment)this.mAdded.get(i);
        if (localFragment == null)
          continue;
        localFragment.performPictureInPictureModeChanged(paramBoolean);
      }
    }
  }

  public boolean dispatchPrepareOptionsMenu(Menu paramMenu)
  {
    int i;
    int j;
    if (this.mAdded != null)
    {
      i = 0;
      j = 0;
      if (j >= this.mAdded.size())
        break label63;
      Fragment localFragment = (Fragment)this.mAdded.get(j);
      if ((localFragment == null) || (!localFragment.performPrepareOptionsMenu(paramMenu)))
        break label65;
    }
    label63: label65: for (int k = 1; ; k = i)
    {
      j++;
      i = k;
      break;
      i = 0;
      return i;
    }
  }

  public void dispatchReallyStop()
  {
    moveToState(2, false);
  }

  public void dispatchResume()
  {
    this.mStateSaved = false;
    moveToState(5, false);
  }

  public void dispatchStart()
  {
    this.mStateSaved = false;
    moveToState(4, false);
  }

  public void dispatchStop()
  {
    this.mStateSaved = true;
    moveToState(3, false);
  }

  void doPendingDeferredStart()
  {
    boolean bool1;
    int i;
    Fragment localFragment;
    if (this.mHavePendingDeferredStart)
    {
      bool1 = false;
      i = 0;
      if (i < this.mActive.size())
      {
        localFragment = (Fragment)this.mActive.get(i);
        if ((localFragment == null) || (localFragment.mLoaderManager == null))
          break label79;
      }
    }
    label79: for (boolean bool2 = bool1 | localFragment.mLoaderManager.hasRunningLoaders(); ; bool2 = bool1)
    {
      i++;
      bool1 = bool2;
      break;
      if (!bool1)
      {
        this.mHavePendingDeferredStart = false;
        startPendingDeferredFragments();
      }
      return;
    }
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    int i = 0;
    String str = paramString + "    ";
    if (this.mActive != null)
    {
      int i6 = this.mActive.size();
      if (i6 > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("Active Fragments in ");
        paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
        paramPrintWriter.println(":");
        for (int i7 = 0; i7 < i6; i7++)
        {
          Fragment localFragment3 = (Fragment)this.mActive.get(i7);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(i7);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localFragment3);
          if (localFragment3 == null)
            continue;
          localFragment3.dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        }
      }
    }
    if (this.mAdded != null)
    {
      int i4 = this.mAdded.size();
      if (i4 > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Added Fragments:");
        for (int i5 = 0; i5 < i4; i5++)
        {
          Fragment localFragment2 = (Fragment)this.mAdded.get(i5);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(i5);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localFragment2.toString());
        }
      }
    }
    if (this.mCreatedMenus != null)
    {
      int i2 = this.mCreatedMenus.size();
      if (i2 > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Fragments Created Menus:");
        for (int i3 = 0; i3 < i2; i3++)
        {
          Fragment localFragment1 = (Fragment)this.mCreatedMenus.get(i3);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(i3);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localFragment1.toString());
        }
      }
    }
    if (this.mBackStack != null)
    {
      int n = this.mBackStack.size();
      if (n > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Back Stack:");
        for (int i1 = 0; i1 < n; i1++)
        {
          BackStackRecord localBackStackRecord2 = (BackStackRecord)this.mBackStack.get(i1);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(i1);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localBackStackRecord2.toString());
          localBackStackRecord2.dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        }
      }
    }
    monitorenter;
    try
    {
      if (this.mBackStackIndices != null)
      {
        int k = this.mBackStackIndices.size();
        if (k > 0)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.println("Back Stack Indices:");
          for (int m = 0; m < k; m++)
          {
            BackStackRecord localBackStackRecord1 = (BackStackRecord)this.mBackStackIndices.get(m);
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  #");
            paramPrintWriter.print(m);
            paramPrintWriter.print(": ");
            paramPrintWriter.println(localBackStackRecord1);
          }
        }
      }
      if ((this.mAvailBackStackIndices != null) && (this.mAvailBackStackIndices.size() > 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mAvailBackStackIndices: ");
        paramPrintWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
      }
      monitorexit;
      if (this.mPendingActions != null)
      {
        int j = this.mPendingActions.size();
        if (j > 0)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.println("Pending Actions:");
          while (i < j)
          {
            OpGenerator localOpGenerator = (OpGenerator)this.mPendingActions.get(i);
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  #");
            paramPrintWriter.print(i);
            paramPrintWriter.print(": ");
            paramPrintWriter.println(localOpGenerator);
            i++;
          }
        }
      }
    }
    finally
    {
      monitorexit;
    }
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("FragmentManager misc state:");
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mHost=");
    paramPrintWriter.println(this.mHost);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mContainer=");
    paramPrintWriter.println(this.mContainer);
    if (this.mParent != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mParent=");
      paramPrintWriter.println(this.mParent);
    }
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("  mCurState=");
    paramPrintWriter.print(this.mCurState);
    paramPrintWriter.print(" mStateSaved=");
    paramPrintWriter.print(this.mStateSaved);
    paramPrintWriter.print(" mDestroyed=");
    paramPrintWriter.println(this.mDestroyed);
    if (this.mNeedMenuInvalidate)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mNeedMenuInvalidate=");
      paramPrintWriter.println(this.mNeedMenuInvalidate);
    }
    if (this.mNoTransactionsBecause != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mNoTransactionsBecause=");
      paramPrintWriter.println(this.mNoTransactionsBecause);
    }
    if ((this.mAvailIndices != null) && (this.mAvailIndices.size() > 0))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mAvailIndices: ");
      paramPrintWriter.println(Arrays.toString(this.mAvailIndices.toArray()));
    }
  }

  // ERROR //
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean)
  {
    // Byte code:
    //   0: iload_2
    //   1: ifne +7 -> 8
    //   4: aload_0
    //   5: invokespecial 329	android/support/v4/app/FragmentManagerImpl:checkStateLoss	()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield 786	android/support/v4/app/FragmentManagerImpl:mDestroyed	Z
    //   14: ifne +10 -> 24
    //   17: aload_0
    //   18: getfield 314	android/support/v4/app/FragmentManagerImpl:mHost	Landroid/support/v4/app/FragmentHostCallback;
    //   21: ifnonnull +19 -> 40
    //   24: new 207	java/lang/IllegalStateException
    //   27: dup
    //   28: ldc_w 1034
    //   31: invokespecial 212	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   34: athrow
    //   35: astore_3
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_3
    //   39: athrow
    //   40: aload_0
    //   41: getfield 429	android/support/v4/app/FragmentManagerImpl:mPendingActions	Ljava/util/ArrayList;
    //   44: ifnonnull +14 -> 58
    //   47: aload_0
    //   48: new 163	java/util/ArrayList
    //   51: dup
    //   52: invokespecial 330	java/util/ArrayList:<init>	()V
    //   55: putfield 429	android/support/v4/app/FragmentManagerImpl:mPendingActions	Ljava/util/ArrayList;
    //   58: aload_0
    //   59: getfield 429	android/support/v4/app/FragmentManagerImpl:mPendingActions	Ljava/util/ArrayList;
    //   62: aload_1
    //   63: invokevirtual 241	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   66: pop
    //   67: aload_0
    //   68: invokespecial 145	android/support/v4/app/FragmentManagerImpl:scheduleCommit	()V
    //   71: aload_0
    //   72: monitorexit
    //   73: return
    //
    // Exception table:
    //   from	to	target	type
    //   10	24	35	finally
    //   24	35	35	finally
    //   36	38	35	finally
    //   40	58	35	finally
    //   58	73	35	finally
  }

  public boolean execPendingActions()
  {
    ensureExecReady(true);
    int i = 0;
    while (true)
    {
      if (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop))
        this.mExecutingActions = true;
      try
      {
        optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
        i = 1;
      }
      finally
      {
        cleanupExec();
      }
    }
    return i;
  }

  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean)
  {
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop))
      this.mExecutingActions = true;
    try
    {
      optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
      cleanupExec();
      doPendingDeferredStart();
      return;
    }
    finally
    {
      cleanupExec();
    }
    throw localObject;
  }

  public boolean executePendingTransactions()
  {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }

  public Fragment findFragmentById(int paramInt)
  {
    Fragment localFragment;
    if (this.mAdded != null)
      for (int j = -1 + this.mAdded.size(); j >= 0; j--)
      {
        localFragment = (Fragment)this.mAdded.get(j);
        if ((localFragment != null) && (localFragment.mFragmentId == paramInt))
          return localFragment;
      }
    if (this.mActive != null)
      for (int i = -1 + this.mActive.size(); ; i--)
      {
        if (i < 0)
          break label107;
        localFragment = (Fragment)this.mActive.get(i);
        if ((localFragment != null) && (localFragment.mFragmentId == paramInt))
          break;
      }
    label107: return null;
  }

  public Fragment findFragmentByTag(String paramString)
  {
    Fragment localFragment;
    if ((this.mAdded != null) && (paramString != null))
      for (int j = -1 + this.mAdded.size(); j >= 0; j--)
      {
        localFragment = (Fragment)this.mAdded.get(j);
        if ((localFragment != null) && (paramString.equals(localFragment.mTag)))
          return localFragment;
      }
    if ((this.mActive != null) && (paramString != null))
      for (int i = -1 + this.mActive.size(); ; i--)
      {
        if (i < 0)
          break label121;
        localFragment = (Fragment)this.mActive.get(i);
        if ((localFragment != null) && (paramString.equals(localFragment.mTag)))
          break;
      }
    label121: return null;
  }

  public Fragment findFragmentByWho(String paramString)
  {
    if ((this.mActive != null) && (paramString != null))
      for (int i = -1 + this.mActive.size(); i >= 0; i--)
      {
        Fragment localFragment1 = (Fragment)this.mActive.get(i);
        if (localFragment1 == null)
          continue;
        Fragment localFragment2 = localFragment1.findFragmentByWho(paramString);
        if (localFragment2 != null)
          return localFragment2;
      }
    return null;
  }

  public void freeBackStackIndex(int paramInt)
  {
    monitorenter;
    try
    {
      this.mBackStackIndices.set(paramInt, null);
      if (this.mAvailBackStackIndices == null)
        this.mAvailBackStackIndices = new ArrayList();
      if (DEBUG)
        Log.v("FragmentManager", "Freeing back stack index " + paramInt);
      this.mAvailBackStackIndices.add(Integer.valueOf(paramInt));
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt)
  {
    return (FragmentManager.BackStackEntry)this.mBackStack.get(paramInt);
  }

  public int getBackStackEntryCount()
  {
    if (this.mBackStack != null)
      return this.mBackStack.size();
    return 0;
  }

  public Fragment getFragment(Bundle paramBundle, String paramString)
  {
    int i = paramBundle.getInt(paramString, -1);
    Fragment localFragment;
    if (i == -1)
      localFragment = null;
    do
    {
      return localFragment;
      if (i >= this.mActive.size())
        throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": index " + i));
      localFragment = (Fragment)this.mActive.get(i);
    }
    while (localFragment != null);
    throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": index " + i));
    return localFragment;
  }

  public List<Fragment> getFragments()
  {
    return this.mActive;
  }

  LayoutInflaterFactory getLayoutInflaterFactory()
  {
    return this;
  }

  public void hideFragment(Fragment paramFragment)
  {
    boolean bool = true;
    if (DEBUG)
      Log.v("FragmentManager", "hide: " + paramFragment);
    if (!paramFragment.mHidden)
    {
      paramFragment.mHidden = bool;
      if (paramFragment.mHiddenChanged)
        break label59;
    }
    while (true)
    {
      paramFragment.mHiddenChanged = bool;
      return;
      label59: bool = false;
    }
  }

  public boolean isDestroyed()
  {
    return this.mDestroyed;
  }

  boolean isStateAtLeast(int paramInt)
  {
    return this.mCurState >= paramInt;
  }

  Animation loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    Animation localAnimation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, paramFragment.getNextAnim());
    if (localAnimation != null);
    do
    {
      return localAnimation;
      if (paramFragment.getNextAnim() == 0)
        break;
      localAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), paramFragment.getNextAnim());
    }
    while (localAnimation != null);
    if (paramInt1 == 0)
      return null;
    int i = transitToStyleIndex(paramInt1, paramBoolean);
    if (i < 0)
      return null;
    switch (i)
    {
    default:
      if ((paramInt2 == 0) && (this.mHost.onHasWindowAnimations()))
        paramInt2 = this.mHost.onGetWindowAnimations();
      if (paramInt2 != 0)
        break;
      return null;
    case 1:
      return makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
    case 2:
      return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
    case 3:
      return makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
    case 4:
      return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
    case 5:
      return makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
    case 6:
      return makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
    }
    return null;
  }

  void makeActive(Fragment paramFragment)
  {
    if (paramFragment.mIndex >= 0);
    while (true)
    {
      return;
      if ((this.mAvailIndices == null) || (this.mAvailIndices.size() <= 0))
      {
        if (this.mActive == null)
          this.mActive = new ArrayList();
        paramFragment.setIndex(this.mActive.size(), this.mParent);
        this.mActive.add(paramFragment);
      }
      while (DEBUG)
      {
        Log.v("FragmentManager", "Allocated fragment index " + paramFragment);
        return;
        paramFragment.setIndex(((Integer)this.mAvailIndices.remove(-1 + this.mAvailIndices.size())).intValue(), this.mParent);
        this.mActive.set(paramFragment.mIndex, paramFragment);
      }
    }
  }

  void makeInactive(Fragment paramFragment)
  {
    if (paramFragment.mIndex < 0)
      return;
    if (DEBUG)
      Log.v("FragmentManager", "Freeing fragment index " + paramFragment);
    this.mActive.set(paramFragment.mIndex, null);
    if (this.mAvailIndices == null)
      this.mAvailIndices = new ArrayList();
    this.mAvailIndices.add(Integer.valueOf(paramFragment.mIndex));
    this.mHost.inactivateFragment(paramFragment.mWho);
    paramFragment.initState();
  }

  void moveFragmentToExpectedState(Fragment paramFragment)
  {
    if (paramFragment == null)
      return;
    int i = this.mCurState;
    if (paramFragment.mRemoving)
    {
      if (paramFragment.isInBackStack())
        i = Math.min(i, 1);
    }
    else
    {
      label30: moveToState(paramFragment, i, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
      if (paramFragment.mView != null)
      {
        Fragment localFragment = findFragmentUnder(paramFragment);
        if (localFragment != null)
        {
          View localView = localFragment.mView;
          ViewGroup localViewGroup = paramFragment.mContainer;
          int j = localViewGroup.indexOfChild(localView);
          int k = localViewGroup.indexOfChild(paramFragment.mView);
          if (k < j)
          {
            localViewGroup.removeViewAt(k);
            localViewGroup.addView(paramFragment.mView, j);
          }
        }
        if ((paramFragment.mIsNewlyAdded) && (paramFragment.mContainer != null))
        {
          if (Build.VERSION.SDK_INT >= 11)
            break label221;
          paramFragment.mView.setVisibility(0);
        }
      }
    }
    while (true)
    {
      paramFragment.mPostponedAlpha = 0.0F;
      paramFragment.mIsNewlyAdded = false;
      Animation localAnimation = loadAnimation(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
      if (localAnimation != null)
      {
        setHWLayerAnimListenerIfAlpha(paramFragment.mView, localAnimation);
        paramFragment.mView.startAnimation(localAnimation);
      }
      if (!paramFragment.mHiddenChanged)
        break;
      completeShowHideFragment(paramFragment);
      return;
      i = Math.min(i, 0);
      break label30;
      label221: if (paramFragment.mPostponedAlpha <= 0.0F)
        continue;
      paramFragment.mView.setAlpha(paramFragment.mPostponedAlpha);
    }
  }

  void moveToState(int paramInt, boolean paramBoolean)
  {
    if ((this.mHost == null) && (paramInt != 0))
      throw new IllegalStateException("No activity");
    if ((!paramBoolean) && (paramInt == this.mCurState));
    do
    {
      return;
      this.mCurState = paramInt;
    }
    while (this.mActive == null);
    int m;
    boolean bool1;
    label68: Fragment localFragment2;
    if (this.mAdded != null)
    {
      int k = this.mAdded.size();
      m = 0;
      bool1 = false;
      if (m >= k)
        break label126;
      localFragment2 = (Fragment)this.mAdded.get(m);
      moveFragmentToExpectedState(localFragment2);
      if (localFragment2.mLoaderManager == null)
        break label272;
    }
    label138: label272: for (boolean bool3 = bool1 | localFragment2.mLoaderManager.hasRunningLoaders(); ; bool3 = bool1)
    {
      m++;
      bool1 = bool3;
      break label68;
      bool1 = false;
      label126: int i = this.mActive.size();
      int j = 0;
      Fragment localFragment1;
      if (j < i)
      {
        localFragment1 = (Fragment)this.mActive.get(j);
        if ((localFragment1 == null) || ((!localFragment1.mRemoving) && (!localFragment1.mDetached)) || (localFragment1.mIsNewlyAdded))
          break label266;
        moveFragmentToExpectedState(localFragment1);
        if (localFragment1.mLoaderManager == null)
          break label266;
      }
      for (boolean bool2 = bool1 | localFragment1.mLoaderManager.hasRunningLoaders(); ; bool2 = bool1)
      {
        j++;
        bool1 = bool2;
        break label138;
        if (!bool1)
          startPendingDeferredFragments();
        if ((!this.mNeedMenuInvalidate) || (this.mHost == null) || (this.mCurState != 5))
          break;
        this.mHost.onSupportInvalidateOptionsMenu();
        this.mNeedMenuInvalidate = false;
        return;
      }
    }
  }

  void moveToState(Fragment paramFragment)
  {
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }

  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    int i = 1;
    if (((!paramFragment.mAdded) || (paramFragment.mDetached)) && (paramInt1 > i))
      paramInt1 = i;
    if ((paramFragment.mRemoving) && (paramInt1 > paramFragment.mState))
      paramInt1 = paramFragment.mState;
    if ((paramFragment.mDeferStart) && (paramFragment.mState < 4) && (paramInt1 > 3))
      paramInt1 = 3;
    label156: label499: ViewGroup localViewGroup;
    if (paramFragment.mState < paramInt1)
    {
      if ((paramFragment.mFromLayout) && (!paramFragment.mInLayout))
        return;
      if (paramFragment.getAnimatingAway() != null)
      {
        paramFragment.setAnimatingAway(null);
        moveToState(paramFragment, paramFragment.getStateAfterAnimating(), 0, 0, i);
      }
      switch (paramFragment.mState)
      {
      default:
        if (paramFragment.mState != paramInt1)
        {
          Log.w("FragmentManager", "moveToState: Fragment state for " + paramFragment + " not updated inline; " + "expected state " + paramInt1 + " found " + paramFragment.mState);
          paramFragment.mState = paramInt1;
          return;
        }
      case 0:
        if (DEBUG)
          Log.v("FragmentManager", "moveto CREATED: " + paramFragment);
        if (paramFragment.mSavedFragmentState != null)
        {
          paramFragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
          paramFragment.mSavedViewState = paramFragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
          paramFragment.mTarget = getFragment(paramFragment.mSavedFragmentState, "android:target_state");
          if (paramFragment.mTarget != null)
            paramFragment.mTargetRequestCode = paramFragment.mSavedFragmentState.getInt("android:target_req_state", 0);
          paramFragment.mUserVisibleHint = paramFragment.mSavedFragmentState.getBoolean("android:user_visible_hint", i);
          if (!paramFragment.mUserVisibleHint)
          {
            paramFragment.mDeferStart = i;
            if (paramInt1 > 3)
              paramInt1 = 3;
          }
        }
        paramFragment.mHost = this.mHost;
        paramFragment.mParentFragment = this.mParent;
        if (this.mParent != null);
        for (FragmentManagerImpl localFragmentManagerImpl = this.mParent.mChildFragmentManager; ; localFragmentManagerImpl = this.mHost.getFragmentManagerImpl())
        {
          paramFragment.mFragmentManager = localFragmentManagerImpl;
          dispatchOnFragmentPreAttached(paramFragment, this.mHost.getContext(), false);
          paramFragment.mCalled = false;
          paramFragment.onAttach(this.mHost.getContext());
          if (paramFragment.mCalled)
            break;
          throw new SuperNotCalledException("Fragment " + paramFragment + " did not call through to super.onAttach()");
        }
        if (paramFragment.mParentFragment == null)
        {
          this.mHost.onAttachFragment(paramFragment);
          dispatchOnFragmentAttached(paramFragment, this.mHost.getContext(), false);
          if (paramFragment.mRetaining)
            break label1147;
          paramFragment.performCreate(paramFragment.mSavedFragmentState);
          dispatchOnFragmentCreated(paramFragment, paramFragment.mSavedFragmentState, false);
          paramFragment.mRetaining = false;
          if (!paramFragment.mFromLayout)
            break;
          paramFragment.mView = paramFragment.performCreateView(paramFragment.getLayoutInflater(paramFragment.mSavedFragmentState), null, paramFragment.mSavedFragmentState);
          if (paramFragment.mView == null)
            break label1178;
          paramFragment.mInnerView = paramFragment.mView;
          if (Build.VERSION.SDK_INT < 11)
            break label1164;
          ViewCompat.setSaveFromParentEnabled(paramFragment.mView, false);
          if (paramFragment.mHidden)
            paramFragment.mView.setVisibility(8);
          paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
          dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
        }
      case 1:
        label537: label601: if (paramInt1 > i)
        {
          if (DEBUG)
            Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + paramFragment);
          if (!paramFragment.mFromLayout)
          {
            if (paramFragment.mContainerId == 0)
              break label1791;
            if (paramFragment.mContainerId == -1)
              throwException(new IllegalArgumentException("Cannot create fragment " + paramFragment + " for a container view with no id"));
            localViewGroup = (ViewGroup)this.mContainer.onFindViewById(paramFragment.mContainerId);
            if ((localViewGroup != null) || (paramFragment.mRestored));
          }
        }
      case 2:
      case 3:
      case 4:
      }
    }
    while (true)
    {
      try
      {
        String str2 = paramFragment.getResources().getResourceName(paramFragment.mContainerId);
        str1 = str2;
        throwException(new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(paramFragment.mContainerId) + " (" + str1 + ") for fragment " + paramFragment));
        paramFragment.mContainer = localViewGroup;
        paramFragment.mView = paramFragment.performCreateView(paramFragment.getLayoutInflater(paramFragment.mSavedFragmentState), localViewGroup, paramFragment.mSavedFragmentState);
        if (paramFragment.mView == null)
          continue;
        paramFragment.mInnerView = paramFragment.mView;
        if (Build.VERSION.SDK_INT < 11)
          continue;
        ViewCompat.setSaveFromParentEnabled(paramFragment.mView, false);
        if (localViewGroup == null)
          continue;
        localViewGroup.addView(paramFragment.mView);
        if (!paramFragment.mHidden)
          continue;
        paramFragment.mView.setVisibility(8);
        paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
        dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
        if ((paramFragment.mView.getVisibility() != 0) || (paramFragment.mContainer == null))
          continue;
        paramFragment.mIsNewlyAdded = i;
        paramFragment.performActivityCreated(paramFragment.mSavedFragmentState);
        dispatchOnFragmentActivityCreated(paramFragment, paramFragment.mSavedFragmentState, false);
        if (paramFragment.mView == null)
          continue;
        paramFragment.restoreViewState(paramFragment.mSavedFragmentState);
        paramFragment.mSavedFragmentState = null;
        if (paramInt1 <= 2)
          continue;
        paramFragment.mState = 3;
        if (paramInt1 <= 3)
          continue;
        if (!DEBUG)
          continue;
        Log.v("FragmentManager", "moveto STARTED: " + paramFragment);
        paramFragment.performStart();
        dispatchOnFragmentStarted(paramFragment, false);
        if (paramInt1 <= 4)
          break label156;
        if (!DEBUG)
          continue;
        Log.v("FragmentManager", "moveto RESUMED: " + paramFragment);
        paramFragment.performResume();
        dispatchOnFragmentResumed(paramFragment, false);
        paramFragment.mSavedFragmentState = null;
        paramFragment.mSavedViewState = null;
        break label156;
        paramFragment.mParentFragment.onAttachFragment(paramFragment);
        break label499;
        label1147: paramFragment.restoreChildFragmentState(paramFragment.mSavedFragmentState);
        paramFragment.mState = i;
        break label537;
        label1164: paramFragment.mView = NoSaveStateFrameLayout.wrap(paramFragment.mView);
        break label601;
        label1178: paramFragment.mInnerView = null;
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        String str1 = "unknown";
        continue;
        paramFragment.mView = NoSaveStateFrameLayout.wrap(paramFragment.mView);
        continue;
        i = 0;
        continue;
        paramFragment.mInnerView = null;
        continue;
      }
      if (paramFragment.mState <= paramInt1)
        break label156;
      switch (paramFragment.mState)
      {
      default:
        break;
      case 1:
      case 5:
      case 4:
      case 3:
      case 2:
        label1275: 
        do
        {
          if (paramInt1 >= i)
            break;
          if ((this.mDestroyed) && (paramFragment.getAnimatingAway() != null))
          {
            View localView = paramFragment.getAnimatingAway();
            paramFragment.setAnimatingAway(null);
            localView.clearAnimation();
          }
          if (paramFragment.getAnimatingAway() == null)
            break label1680;
          paramFragment.setStateAfterAnimating(paramInt1);
          paramInt1 = i;
          break;
          if (paramInt1 < 5)
          {
            if (DEBUG)
              Log.v("FragmentManager", "movefrom RESUMED: " + paramFragment);
            paramFragment.performPause();
            dispatchOnFragmentPaused(paramFragment, false);
          }
          if (paramInt1 < 4)
          {
            if (DEBUG)
              Log.v("FragmentManager", "movefrom STARTED: " + paramFragment);
            paramFragment.performStop();
            dispatchOnFragmentStopped(paramFragment, false);
          }
          if (paramInt1 >= 3)
            continue;
          if (DEBUG)
            Log.v("FragmentManager", "movefrom STOPPED: " + paramFragment);
          paramFragment.performReallyStop();
        }
        while (paramInt1 >= 2);
        if (DEBUG)
          Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + paramFragment);
        if ((paramFragment.mView != null) && (this.mHost.onShouldSaveFragmentState(paramFragment)) && (paramFragment.mSavedViewState == null))
          saveFragmentViewState(paramFragment);
        paramFragment.performDestroyView();
        dispatchOnFragmentViewDestroyed(paramFragment, false);
        if ((paramFragment.mView != null) && (paramFragment.mContainer != null))
          if ((this.mCurState <= 0) || (this.mDestroyed) || (paramFragment.mView.getVisibility() != 0) || (paramFragment.mPostponedAlpha < 0.0F))
            break label1785;
        label1680: label1767: label1785: for (Animation localAnimation = loadAnimation(paramFragment, paramInt2, false, paramInt3); ; localAnimation = null)
        {
          paramFragment.mPostponedAlpha = 0.0F;
          if (localAnimation != null)
          {
            paramFragment.setAnimatingAway(paramFragment.mView);
            paramFragment.setStateAfterAnimating(paramInt1);
            localAnimation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(paramFragment.mView, localAnimation, paramFragment)
            {
              public void onAnimationEnd(Animation paramAnimation)
              {
                super.onAnimationEnd(paramAnimation);
                if (this.val$fragment.getAnimatingAway() != null)
                {
                  this.val$fragment.setAnimatingAway(null);
                  FragmentManagerImpl.this.moveToState(this.val$fragment, this.val$fragment.getStateAfterAnimating(), 0, 0, false);
                }
              }
            });
            paramFragment.mView.startAnimation(localAnimation);
          }
          paramFragment.mContainer.removeView(paramFragment.mView);
          paramFragment.mContainer = null;
          paramFragment.mView = null;
          paramFragment.mInnerView = null;
          break label1275;
          if (DEBUG)
            Log.v("FragmentManager", "movefrom CREATED: " + paramFragment);
          if (!paramFragment.mRetaining)
          {
            paramFragment.performDestroy();
            dispatchOnFragmentDestroyed(paramFragment, false);
          }
          while (true)
          {
            paramFragment.performDetach();
            dispatchOnFragmentDetached(paramFragment, false);
            if (paramBoolean)
              break;
            if (paramFragment.mRetaining)
              break label1767;
            makeInactive(paramFragment);
            break;
            paramFragment.mState = 0;
          }
          paramFragment.mHost = null;
          paramFragment.mParentFragment = null;
          paramFragment.mFragmentManager = null;
          break label156;
          break;
        }
        label1791: localViewGroup = null;
      }
    }
  }

  public void noteStateNotSaved()
  {
    this.mStateSaved = false;
  }

  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    if (!"fragment".equals(paramString))
      return null;
    String str1 = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
    if (str1 == null);
    for (String str2 = localTypedArray.getString(0); ; str2 = str1)
    {
      int i = localTypedArray.getResourceId(1, -1);
      String str3 = localTypedArray.getString(2);
      localTypedArray.recycle();
      if (Fragment.isSupportFragmentClass(this.mHost.getContext(), str2))
      {
        if (paramView != null);
        for (int j = paramView.getId(); (j == -1) && (i == -1) && (str3 == null); j = 0)
          throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str2);
        Fragment localFragment1;
        int k;
        label295: Fragment localFragment2;
        if (i != -1)
        {
          localFragment1 = findFragmentById(i);
          if ((localFragment1 == null) && (str3 != null))
            localFragment1 = findFragmentByTag(str3);
          if ((localFragment1 == null) && (j != -1))
            localFragment1 = findFragmentById(j);
          if (DEBUG)
            Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(i) + " fname=" + str2 + " existing=" + localFragment1);
          if (localFragment1 != null)
            break label449;
          Fragment localFragment3 = Fragment.instantiate(paramContext, str2);
          localFragment3.mFromLayout = true;
          if (i == 0)
            break label442;
          k = i;
          localFragment3.mFragmentId = k;
          localFragment3.mContainerId = j;
          localFragment3.mTag = str3;
          localFragment3.mInLayout = true;
          localFragment3.mFragmentManager = this;
          localFragment3.mHost = this.mHost;
          localFragment3.onInflate(this.mHost.getContext(), paramAttributeSet, localFragment3.mSavedFragmentState);
          addFragment(localFragment3, true);
          localFragment2 = localFragment3;
        }
        while (true)
        {
          label367: if ((this.mCurState < 1) && (localFragment2.mFromLayout))
            moveToState(localFragment2, 1, 0, 0, false);
          while (true)
          {
            if (localFragment2.mView != null)
              break label593;
            throw new IllegalStateException("Fragment " + str2 + " did not create a view.");
            localFragment1 = null;
            break;
            label442: k = j;
            break label295;
            label449: if (localFragment1.mInLayout)
              throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(i) + ", tag " + str3 + ", or parent id 0x" + Integer.toHexString(j) + " with another fragment for " + str2);
            localFragment1.mInLayout = true;
            localFragment1.mHost = this.mHost;
            if (localFragment1.mRetaining)
              break label635;
            localFragment1.onInflate(this.mHost.getContext(), paramAttributeSet, localFragment1.mSavedFragmentState);
            localFragment2 = localFragment1;
            break label367;
            moveToState(localFragment2);
          }
          label593: if (i != 0)
            localFragment2.mView.setId(i);
          if (localFragment2.mView.getTag() == null)
            localFragment2.mView.setTag(str3);
          return localFragment2.mView;
          label635: localFragment2 = localFragment1;
        }
      }
      return null;
    }
  }

  public void performPendingDeferredStart(Fragment paramFragment)
  {
    if (paramFragment.mDeferStart)
    {
      if (this.mExecutingActions)
        this.mHavePendingDeferredStart = true;
    }
    else
      return;
    paramFragment.mDeferStart = false;
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }

  public void popBackStack()
  {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }

  public void popBackStack(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0)
      throw new IllegalArgumentException("Bad id: " + paramInt1);
    enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
  }

  public void popBackStack(String paramString, int paramInt)
  {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }

  public boolean popBackStackImmediate()
  {
    checkStateLoss();
    return popBackStackImmediate(null, -1, 0);
  }

  public boolean popBackStackImmediate(int paramInt1, int paramInt2)
  {
    checkStateLoss();
    execPendingActions();
    if (paramInt1 < 0)
      throw new IllegalArgumentException("Bad id: " + paramInt1);
    return popBackStackImmediate(null, paramInt1, paramInt2);
  }

  public boolean popBackStackImmediate(String paramString, int paramInt)
  {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }

  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2)
  {
    if (this.mBackStack == null)
      return false;
    if ((paramString == null) && (paramInt1 < 0) && ((paramInt2 & 0x1) == 0))
    {
      int m = -1 + this.mBackStack.size();
      if (m >= 0)
      {
        paramArrayList.add(this.mBackStack.remove(m));
        paramArrayList1.add(Boolean.valueOf(true));
      }
    }
    else
    {
      while (true)
      {
        return true;
        int i = -1;
        if ((paramString != null) || (paramInt1 >= 0))
        {
          for (int j = -1 + this.mBackStack.size(); ; j--)
          {
            BackStackRecord localBackStackRecord2;
            if (j >= 0)
            {
              localBackStackRecord2 = (BackStackRecord)this.mBackStack.get(j);
              if ((paramString == null) || (!paramString.equals(localBackStackRecord2.getName())))
                break label195;
            }
            label195: 
            do
            {
              if (j < 0)
                break label281;
              if ((paramInt2 & 0x1) == 0)
                break;
              j--;
              while (j >= 0)
              {
                BackStackRecord localBackStackRecord1 = (BackStackRecord)this.mBackStack.get(j);
                if (((paramString == null) || (!paramString.equals(localBackStackRecord1.getName()))) && ((paramInt1 < 0) || (paramInt1 != localBackStackRecord1.mIndex)))
                  break;
                j--;
              }
            }
            while ((paramInt1 >= 0) && (paramInt1 == localBackStackRecord2.mIndex));
          }
          i = j;
        }
        if (i == -1 + this.mBackStack.size())
          break;
        for (int k = -1 + this.mBackStack.size(); k > i; k--)
        {
          paramArrayList.add(this.mBackStack.remove(k));
          paramArrayList1.add(Boolean.valueOf(true));
        }
      }
    }
    label281: return false;
  }

  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment)
  {
    if (paramFragment.mIndex < 0)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager"));
    paramBundle.putInt(paramString, paramFragment.mIndex);
  }

  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean)
  {
    if (this.mLifecycleCallbacks == null)
      this.mLifecycleCallbacks = new CopyOnWriteArrayList();
    this.mLifecycleCallbacks.add(new Pair(paramFragmentLifecycleCallbacks, Boolean.valueOf(paramBoolean)));
  }

  public void removeFragment(Fragment paramFragment)
  {
    if (DEBUG)
      Log.v("FragmentManager", "remove: " + paramFragment + " nesting=" + paramFragment.mBackStackNesting);
    if (!paramFragment.isInBackStack());
    for (int i = 1; ; i = 0)
    {
      if ((!paramFragment.mDetached) || (i != 0))
      {
        if (this.mAdded != null)
          this.mAdded.remove(paramFragment);
        if ((paramFragment.mHasMenu) && (paramFragment.mMenuVisible))
          this.mNeedMenuInvalidate = true;
        paramFragment.mAdded = false;
        paramFragment.mRemoving = true;
      }
      return;
    }
  }

  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener)
  {
    if (this.mBackStackChangeListeners != null)
      this.mBackStackChangeListeners.remove(paramOnBackStackChangedListener);
  }

  void reportBackStackChanged()
  {
    if (this.mBackStackChangeListeners != null)
      for (int i = 0; i < this.mBackStackChangeListeners.size(); i++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(i)).onBackStackChanged();
  }

  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig)
  {
    if (paramParcelable == null);
    FragmentManagerState localFragmentManagerState;
    do
    {
      return;
      localFragmentManagerState = (FragmentManagerState)paramParcelable;
    }
    while (localFragmentManagerState.mActive == null);
    List localList4;
    if (paramFragmentManagerNonConfig != null)
    {
      List localList3 = paramFragmentManagerNonConfig.getFragments();
      localList4 = paramFragmentManagerNonConfig.getChildNonConfigs();
      if (localList3 != null);
      for (int i1 = localList3.size(); ; i1 = 0)
        for (int i2 = 0; i2 < i1; i2++)
        {
          Fragment localFragment4 = (Fragment)localList3.get(i2);
          if (DEBUG)
            Log.v("FragmentManager", "restoreAllState: re-attaching retained " + localFragment4);
          FragmentState localFragmentState2 = localFragmentManagerState.mActive[localFragment4.mIndex];
          localFragmentState2.mInstance = localFragment4;
          localFragment4.mSavedViewState = null;
          localFragment4.mBackStackNesting = 0;
          localFragment4.mInLayout = false;
          localFragment4.mAdded = false;
          localFragment4.mTarget = null;
          if (localFragmentState2.mSavedFragmentState == null)
            continue;
          localFragmentState2.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
          localFragment4.mSavedViewState = localFragmentState2.mSavedFragmentState.getSparseParcelableArray("android:view_state");
          localFragment4.mSavedFragmentState = localFragmentState2.mSavedFragmentState;
        }
    }
    for (List localList1 = localList4; ; localList1 = null)
    {
      this.mActive = new ArrayList(localFragmentManagerState.mActive.length);
      if (this.mAvailIndices != null)
        this.mAvailIndices.clear();
      int i = 0;
      FragmentState localFragmentState1;
      if (i < localFragmentManagerState.mActive.length)
      {
        localFragmentState1 = localFragmentManagerState.mActive[i];
        if (localFragmentState1 != null)
          if ((localList1 == null) || (i >= localList1.size()))
            break label984;
      }
      label570: label984: for (FragmentManagerNonConfig localFragmentManagerNonConfig = (FragmentManagerNonConfig)localList1.get(i); ; localFragmentManagerNonConfig = null)
      {
        Fragment localFragment3 = localFragmentState1.instantiate(this.mHost, this.mParent, localFragmentManagerNonConfig);
        if (DEBUG)
          Log.v("FragmentManager", "restoreAllState: active #" + i + ": " + localFragment3);
        this.mActive.add(localFragment3);
        localFragmentState1.mInstance = null;
        while (true)
        {
          i++;
          break;
          this.mActive.add(null);
          if (this.mAvailIndices == null)
            this.mAvailIndices = new ArrayList();
          if (DEBUG)
            Log.v("FragmentManager", "restoreAllState: avail #" + i);
          this.mAvailIndices.add(Integer.valueOf(i));
        }
        if (paramFragmentManagerNonConfig != null)
        {
          List localList2 = paramFragmentManagerNonConfig.getFragments();
          int m;
          int n;
          label494: Fragment localFragment2;
          if (localList2 != null)
          {
            m = localList2.size();
            n = 0;
            if (n >= m)
              break label620;
            localFragment2 = (Fragment)localList2.get(n);
            if (localFragment2.mTargetIndex >= 0)
              if (localFragment2.mTargetIndex >= this.mActive.size())
                break label570;
          }
          for (localFragment2.mTarget = ((Fragment)this.mActive.get(localFragment2.mTargetIndex)); ; localFragment2.mTarget = null)
          {
            n++;
            break label494;
            m = 0;
            break;
            Log.w("FragmentManager", "Re-attaching retained fragment " + localFragment2 + " target no longer exists: " + localFragment2.mTargetIndex);
          }
        }
        label620: if (localFragmentManagerState.mAdded != null)
        {
          this.mAdded = new ArrayList(localFragmentManagerState.mAdded.length);
          for (int k = 0; k < localFragmentManagerState.mAdded.length; k++)
          {
            Fragment localFragment1 = (Fragment)this.mActive.get(localFragmentManagerState.mAdded[k]);
            if (localFragment1 == null)
              throwException(new IllegalStateException("No instantiated fragment for index #" + localFragmentManagerState.mAdded[k]));
            localFragment1.mAdded = true;
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: added #" + k + ": " + localFragment1);
            if (this.mAdded.contains(localFragment1))
              throw new IllegalStateException("Already added!");
            this.mAdded.add(localFragment1);
          }
        }
        this.mAdded = null;
        if (localFragmentManagerState.mBackStack != null)
        {
          this.mBackStack = new ArrayList(localFragmentManagerState.mBackStack.length);
          for (int j = 0; j < localFragmentManagerState.mBackStack.length; j++)
          {
            BackStackRecord localBackStackRecord = localFragmentManagerState.mBackStack[j].instantiate(this);
            if (DEBUG)
            {
              Log.v("FragmentManager", "restoreAllState: back stack #" + j + " (index " + localBackStackRecord.mIndex + "): " + localBackStackRecord);
              localBackStackRecord.dump("  ", new PrintWriter(new LogWriter("FragmentManager")), false);
            }
            this.mBackStack.add(localBackStackRecord);
            if (localBackStackRecord.mIndex < 0)
              continue;
            setBackStackIndex(localBackStackRecord.mIndex, localBackStackRecord);
          }
          break;
        }
        this.mBackStack = null;
        return;
      }
    }
  }

  FragmentManagerNonConfig retainNonConfig()
  {
    Object localObject1;
    Object localObject2;
    int i;
    label189: Object localObject5;
    int j;
    label208: Object localObject4;
    Object localObject3;
    if (this.mActive != null)
    {
      localObject1 = null;
      localObject2 = null;
      i = 0;
      if (i >= this.mActive.size())
        break label252;
      Fragment localFragment = (Fragment)this.mActive.get(i);
      if (localFragment == null)
        break label295;
      int m;
      if (localFragment.mRetainInstance)
      {
        if (localObject1 == null)
          localObject1 = new ArrayList();
        ((ArrayList)localObject1).add(localFragment);
        localFragment.mRetaining = true;
        if (localFragment.mTarget == null)
          break label189;
        m = localFragment.mTarget.mIndex;
      }
      while (true)
      {
        localFragment.mTargetIndex = m;
        if (DEBUG)
          Log.v("FragmentManager", "retainNonConfig: keeping retained " + localFragment);
        if (localFragment.mChildFragmentManager != null)
        {
          FragmentManagerNonConfig localFragmentManagerNonConfig = localFragment.mChildFragmentManager.retainNonConfig();
          if (localFragmentManagerNonConfig != null)
          {
            if (localObject2 == null)
            {
              localObject2 = new ArrayList();
              int k = 0;
              while (true)
                if (k < i)
                {
                  ((ArrayList)localObject2).add(null);
                  k++;
                  continue;
                  m = -1;
                  break;
                }
            }
            ((ArrayList)localObject2).add(localFragmentManagerNonConfig);
            localObject5 = localObject2;
            j = 1;
            if ((localObject5 == null) || (j != 0))
              break;
            localObject5.add(null);
            Object localObject7 = localObject1;
            localObject4 = localObject5;
            localObject3 = localObject7;
          }
        }
      }
    }
    while (true)
    {
      i++;
      localObject2 = localObject4;
      localObject1 = localObject3;
      break;
      localObject1 = null;
      localObject2 = null;
      label252: if ((localObject1 == null) && (localObject2 == null))
        return null;
      return new FragmentManagerNonConfig((List)localObject1, (List)localObject2);
      Object localObject6 = localObject1;
      localObject4 = localObject5;
      localObject3 = localObject6;
      continue;
      localObject5 = localObject2;
      j = 0;
      break label208;
      label295: localObject3 = localObject1;
      localObject4 = localObject2;
    }
  }

  Parcelable saveAllState()
  {
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    if (HONEYCOMB)
      this.mStateSaved = true;
    if ((this.mActive == null) || (this.mActive.size() <= 0))
      return null;
    int i = this.mActive.size();
    FragmentState[] arrayOfFragmentState = new FragmentState[i];
    int j = 0;
    int k = 0;
    label62: Fragment localFragment;
    FragmentState localFragmentState;
    label309: int i3;
    if (k < i)
    {
      localFragment = (Fragment)this.mActive.get(k);
      if (localFragment == null)
        break label743;
      if (localFragment.mIndex < 0)
        throwException(new IllegalStateException("Failure saving state: active " + localFragment + " has cleared index: " + localFragment.mIndex));
      localFragmentState = new FragmentState(localFragment);
      arrayOfFragmentState[k] = localFragmentState;
      if ((localFragment.mState > 0) && (localFragmentState.mSavedFragmentState == null))
      {
        localFragmentState.mSavedFragmentState = saveFragmentBasicState(localFragment);
        if (localFragment.mTarget != null)
        {
          if (localFragment.mTarget.mIndex < 0)
            throwException(new IllegalStateException("Failure saving state: " + localFragment + " has target not in fragment manager: " + localFragment.mTarget));
          if (localFragmentState.mSavedFragmentState == null)
            localFragmentState.mSavedFragmentState = new Bundle();
          putFragment(localFragmentState.mSavedFragmentState, "android:target_state", localFragment.mTarget);
          if (localFragment.mTargetRequestCode != 0)
            localFragmentState.mSavedFragmentState.putInt("android:target_req_state", localFragment.mTargetRequestCode);
        }
        if (!DEBUG)
          break label750;
        Log.v("FragmentManager", "Saved state of " + localFragment + ": " + localFragmentState.mSavedFragmentState);
        i3 = 1;
      }
    }
    while (true)
    {
      k++;
      j = i3;
      break label62;
      localFragmentState.mSavedFragmentState = localFragment.mSavedFragmentState;
      break label309;
      if (j == 0)
      {
        if (!DEBUG)
          break;
        Log.v("FragmentManager", "saveAllState: no fragments!");
        return null;
      }
      if (this.mAdded != null)
      {
        int i1 = this.mAdded.size();
        if (i1 > 0)
        {
          arrayOfInt = new int[i1];
          for (int i2 = 0; i2 < i1; i2++)
          {
            arrayOfInt[i2] = ((Fragment)this.mAdded.get(i2)).mIndex;
            if (arrayOfInt[i2] < 0)
              throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i2) + " has cleared index: " + arrayOfInt[i2]));
            if (!DEBUG)
              continue;
            Log.v("FragmentManager", "saveAllState: adding fragment #" + i2 + ": " + this.mAdded.get(i2));
          }
        }
      }
      int[] arrayOfInt = null;
      ArrayList localArrayList = this.mBackStack;
      BackStackState[] arrayOfBackStackState = null;
      if (localArrayList != null)
      {
        int m = this.mBackStack.size();
        arrayOfBackStackState = null;
        if (m > 0)
        {
          arrayOfBackStackState = new BackStackState[m];
          for (int n = 0; n < m; n++)
          {
            arrayOfBackStackState[n] = new BackStackState((BackStackRecord)this.mBackStack.get(n));
            if (!DEBUG)
              continue;
            Log.v("FragmentManager", "saveAllState: adding back stack #" + n + ": " + this.mBackStack.get(n));
          }
        }
      }
      FragmentManagerState localFragmentManagerState = new FragmentManagerState();
      localFragmentManagerState.mActive = arrayOfFragmentState;
      localFragmentManagerState.mAdded = arrayOfInt;
      localFragmentManagerState.mBackStack = arrayOfBackStackState;
      return localFragmentManagerState;
      label743: i3 = j;
      continue;
      label750: i3 = 1;
    }
  }

  Bundle saveFragmentBasicState(Fragment paramFragment)
  {
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle();
    paramFragment.performSaveInstanceState(this.mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, this.mStateBundle, false);
    Bundle localBundle;
    if (!this.mStateBundle.isEmpty())
    {
      localBundle = this.mStateBundle;
      this.mStateBundle = null;
    }
    while (true)
    {
      if (paramFragment.mView != null)
        saveFragmentViewState(paramFragment);
      if (paramFragment.mSavedViewState != null)
      {
        if (localBundle == null)
          localBundle = new Bundle();
        localBundle.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
      }
      if (!paramFragment.mUserVisibleHint)
      {
        if (localBundle == null)
          localBundle = new Bundle();
        localBundle.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
      }
      return localBundle;
      localBundle = null;
    }
  }

  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment)
  {
    if (paramFragment.mIndex < 0)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager"));
    int i = paramFragment.mState;
    Fragment.SavedState localSavedState = null;
    if (i > 0)
    {
      Bundle localBundle = saveFragmentBasicState(paramFragment);
      localSavedState = null;
      if (localBundle != null)
        localSavedState = new Fragment.SavedState(localBundle);
    }
    return localSavedState;
  }

  void saveFragmentViewState(Fragment paramFragment)
  {
    if (paramFragment.mInnerView == null)
      return;
    if (this.mStateArray == null)
      this.mStateArray = new SparseArray();
    while (true)
    {
      paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
      if (this.mStateArray.size() <= 0)
        break;
      paramFragment.mSavedViewState = this.mStateArray;
      this.mStateArray = null;
      return;
      this.mStateArray.clear();
    }
  }

  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord)
  {
    monitorenter;
    try
    {
      if (this.mBackStackIndices == null)
        this.mBackStackIndices = new ArrayList();
      int i = this.mBackStackIndices.size();
      if (paramInt < i)
      {
        if (DEBUG)
          Log.v("FragmentManager", "Setting back stack index " + paramInt + " to " + paramBackStackRecord);
        this.mBackStackIndices.set(paramInt, paramBackStackRecord);
      }
      while (true)
      {
        return;
        while (i < paramInt)
        {
          this.mBackStackIndices.add(null);
          if (this.mAvailBackStackIndices == null)
            this.mAvailBackStackIndices = new ArrayList();
          if (DEBUG)
            Log.v("FragmentManager", "Adding available back stack index " + i);
          this.mAvailBackStackIndices.add(Integer.valueOf(i));
          i++;
        }
        if (DEBUG)
          Log.v("FragmentManager", "Adding back stack index " + paramInt + " with " + paramBackStackRecord);
        this.mBackStackIndices.add(paramBackStackRecord);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void showFragment(Fragment paramFragment)
  {
    if (DEBUG)
      Log.v("FragmentManager", "show: " + paramFragment);
    if (paramFragment.mHidden)
    {
      paramFragment.mHidden = false;
      boolean bool1 = paramFragment.mHiddenChanged;
      boolean bool2 = false;
      if (!bool1)
        bool2 = true;
      paramFragment.mHiddenChanged = bool2;
    }
  }

  void startPendingDeferredFragments()
  {
    if (this.mActive == null);
    while (true)
    {
      return;
      for (int i = 0; i < this.mActive.size(); i++)
      {
        Fragment localFragment = (Fragment)this.mActive.get(i);
        if (localFragment == null)
          continue;
        performPendingDeferredStart(localFragment);
      }
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("FragmentManager{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" in ");
    if (this.mParent != null)
      DebugUtils.buildShortClassTag(this.mParent, localStringBuilder);
    while (true)
    {
      localStringBuilder.append("}}");
      return localStringBuilder.toString();
      DebugUtils.buildShortClassTag(this.mHost, localStringBuilder);
    }
  }

  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks)
  {
    if (this.mLifecycleCallbacks == null)
      return;
    while (true)
    {
      int j;
      synchronized (this.mLifecycleCallbacks)
      {
        int i = this.mLifecycleCallbacks.size();
        j = 0;
        if (j >= i)
          continue;
        if (((Pair)this.mLifecycleCallbacks.get(j)).first == paramFragmentLifecycleCallbacks)
        {
          this.mLifecycleCallbacks.remove(j);
          return;
        }
      }
      j++;
    }
  }

  static class AnimateOnHWLayerIfNeededListener
    implements Animation.AnimationListener
  {
    private Animation.AnimationListener mOriginalListener;
    private boolean mShouldRunOnHWLayer;
    View mView;

    public AnimateOnHWLayerIfNeededListener(View paramView, Animation paramAnimation)
    {
      if ((paramView == null) || (paramAnimation == null))
        return;
      this.mView = paramView;
    }

    public AnimateOnHWLayerIfNeededListener(View paramView, Animation paramAnimation, Animation.AnimationListener paramAnimationListener)
    {
      if ((paramView == null) || (paramAnimation == null))
        return;
      this.mOriginalListener = paramAnimationListener;
      this.mView = paramView;
      this.mShouldRunOnHWLayer = true;
    }

    @CallSuper
    public void onAnimationEnd(Animation paramAnimation)
    {
      if ((this.mView != null) && (this.mShouldRunOnHWLayer))
      {
        if ((!ViewCompat.isAttachedToWindow(this.mView)) && (!BuildCompat.isAtLeastN()))
          break label64;
        this.mView.post(new Runnable()
        {
          public void run()
          {
            ViewCompat.setLayerType(FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
          }
        });
      }
      while (true)
      {
        if (this.mOriginalListener != null)
          this.mOriginalListener.onAnimationEnd(paramAnimation);
        return;
        label64: ViewCompat.setLayerType(this.mView, 0, null);
      }
    }

    public void onAnimationRepeat(Animation paramAnimation)
    {
      if (this.mOriginalListener != null)
        this.mOriginalListener.onAnimationRepeat(paramAnimation);
    }

    @CallSuper
    public void onAnimationStart(Animation paramAnimation)
    {
      if (this.mOriginalListener != null)
        this.mOriginalListener.onAnimationStart(paramAnimation);
    }
  }

  static class FragmentTag
  {
    public static final int[] Fragment = { 16842755, 16842960, 16842961 };
    public static final int Fragment_id = 1;
    public static final int Fragment_name = 0;
    public static final int Fragment_tag = 2;
  }

  static abstract interface OpGenerator
  {
    public abstract boolean generateOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1);
  }

  private class PopBackStackState
    implements FragmentManagerImpl.OpGenerator
  {
    final int mFlags;
    final int mId;
    final String mName;

    PopBackStackState(String paramInt1, int paramInt2, int arg4)
    {
      this.mName = paramInt1;
      this.mId = paramInt2;
      int i;
      this.mFlags = i;
    }

    public boolean generateOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
    {
      return FragmentManagerImpl.this.popBackStackState(paramArrayList, paramArrayList1, this.mName, this.mId, this.mFlags);
    }
  }

  static class StartEnterTransitionListener
    implements Fragment.OnStartEnterTransitionListener
  {
    private final boolean mIsBack;
    private int mNumPostponed;
    private final BackStackRecord mRecord;

    StartEnterTransitionListener(BackStackRecord paramBackStackRecord, boolean paramBoolean)
    {
      this.mIsBack = paramBoolean;
      this.mRecord = paramBackStackRecord;
    }

    public void cancelTransaction()
    {
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
    }

    public void completeTransaction()
    {
      if (this.mNumPostponed > 0);
      for (int i = 1; ; i = 0)
      {
        FragmentManagerImpl localFragmentManagerImpl1 = this.mRecord.mManager;
        int j = localFragmentManagerImpl1.mAdded.size();
        for (int k = 0; k < j; k++)
        {
          Fragment localFragment = (Fragment)localFragmentManagerImpl1.mAdded.get(k);
          localFragment.setOnStartEnterTransitionListener(null);
          if ((i == 0) || (!localFragment.isPostponed()))
            continue;
          localFragment.startPostponedEnterTransition();
        }
      }
      FragmentManagerImpl localFragmentManagerImpl2 = this.mRecord.mManager;
      BackStackRecord localBackStackRecord = this.mRecord;
      boolean bool1 = this.mIsBack;
      boolean bool2 = false;
      if (i == 0)
        bool2 = true;
      localFragmentManagerImpl2.completeExecute(localBackStackRecord, bool1, bool2, true);
    }

    public boolean isReady()
    {
      return this.mNumPostponed == 0;
    }

    public void onStartEnterTransition()
    {
      this.mNumPostponed = (-1 + this.mNumPostponed);
      if (this.mNumPostponed != 0)
        return;
      this.mRecord.mManager.scheduleCommit();
    }

    public void startListening()
    {
      this.mNumPostponed = (1 + this.mNumPostponed);
    }
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.FragmentManagerImpl
 * JD-Core Version:    0.6.0
 */