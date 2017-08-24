package android.support.v4.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class FragmentActivity extends BaseFragmentActivityJB
  implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompatApi23.RequestPermissionsRequestCodeValidator
{
  static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
  static final String FRAGMENTS_TAG = "android:support:fragments";
  static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
  static final int MSG_REALLY_STOPPED = 1;
  static final int MSG_RESUME_PENDING = 2;
  static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
  static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
  private static final String TAG = "FragmentActivity";
  boolean mCreated;
  final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
  final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        super.handleMessage(paramMessage);
      case 1:
        do
          return;
        while (!FragmentActivity.this.mStopped);
        FragmentActivity.this.doReallyStop(false);
        return;
      case 2:
      }
      FragmentActivity.this.onResumeFragments();
      FragmentActivity.this.mFragments.execPendingActions();
    }
  };
  int mNextCandidateRequestIndex;
  boolean mOptionsMenuInvalidated;
  SparseArrayCompat<String> mPendingFragmentActivityResults;
  boolean mReallyStopped;
  boolean mRequestedPermissionsFromFragment;
  boolean mResumed;
  boolean mRetaining;
  boolean mStopped;

  private int allocateRequestIndex(Fragment paramFragment)
  {
    if (this.mPendingFragmentActivityResults.size() >= 65534)
      throw new IllegalStateException("Too many pending Fragment activity results.");
    while (this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0)
      this.mNextCandidateRequestIndex = ((1 + this.mNextCandidateRequestIndex) % 65534);
    int i = this.mNextCandidateRequestIndex;
    this.mPendingFragmentActivityResults.put(i, paramFragment.mWho);
    this.mNextCandidateRequestIndex = ((1 + this.mNextCandidateRequestIndex) % 65534);
    return i;
  }

  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView)
  {
    paramPrintWriter.print(paramString);
    if (paramView == null)
      paramPrintWriter.println("null");
    while (true)
    {
      return;
      paramPrintWriter.println(viewToString(paramView));
      if (!(paramView instanceof ViewGroup))
        continue;
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = localViewGroup.getChildCount();
      if (i <= 0)
        continue;
      String str = paramString + "  ";
      for (int j = 0; j < i; j++)
        dumpViewHierarchy(str, paramPrintWriter, localViewGroup.getChildAt(j));
    }
  }

  private static String viewToString(View paramView)
  {
    char c1 = 'F';
    char c2 = '.';
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append(paramView.getClass().getName());
    localStringBuilder.append('{');
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(paramView)));
    localStringBuilder.append(' ');
    switch (paramView.getVisibility())
    {
    default:
      localStringBuilder.append(c2);
    case 0:
    case 4:
    case 8:
    }
    while (true)
    {
      char c3;
      label108: char c4;
      label126: char c5;
      label143: char c6;
      label161: char c7;
      label179: char c8;
      label197: char c9;
      label215: label236: char c10;
      label253: int i;
      Resources localResources;
      if (paramView.isFocusable())
      {
        c3 = c1;
        localStringBuilder.append(c3);
        if (!paramView.isEnabled())
          break label533;
        c4 = 'E';
        localStringBuilder.append(c4);
        if (!paramView.willNotDraw())
          break label539;
        c5 = c2;
        localStringBuilder.append(c5);
        if (!paramView.isHorizontalScrollBarEnabled())
          break label546;
        c6 = 'H';
        localStringBuilder.append(c6);
        if (!paramView.isVerticalScrollBarEnabled())
          break label552;
        c7 = 'V';
        localStringBuilder.append(c7);
        if (!paramView.isClickable())
          break label558;
        c8 = 'C';
        localStringBuilder.append(c8);
        if (!paramView.isLongClickable())
          break label564;
        c9 = 'L';
        localStringBuilder.append(c9);
        localStringBuilder.append(' ');
        if (!paramView.isFocused())
          break label570;
        localStringBuilder.append(c1);
        if (!paramView.isSelected())
          break label575;
        c10 = 'S';
        localStringBuilder.append(c10);
        if (paramView.isPressed())
          c2 = 'P';
        localStringBuilder.append(c2);
        localStringBuilder.append(' ');
        localStringBuilder.append(paramView.getLeft());
        localStringBuilder.append(',');
        localStringBuilder.append(paramView.getTop());
        localStringBuilder.append('-');
        localStringBuilder.append(paramView.getRight());
        localStringBuilder.append(',');
        localStringBuilder.append(paramView.getBottom());
        i = paramView.getId();
        if (i != -1)
        {
          localStringBuilder.append(" #");
          localStringBuilder.append(Integer.toHexString(i));
          localResources = paramView.getResources();
          if ((i != 0) && (localResources != null))
            switch (0xFF000000 & i)
            {
            default:
            case 2130706432:
            case 16777216:
            }
        }
      }
      try
      {
        String str1 = localResources.getResourcePackageName(i);
        while (true)
        {
          String str2 = localResources.getResourceTypeName(i);
          String str3 = localResources.getResourceEntryName(i);
          localStringBuilder.append(" ");
          localStringBuilder.append(str1);
          localStringBuilder.append(":");
          localStringBuilder.append(str2);
          localStringBuilder.append("/");
          localStringBuilder.append(str3);
          label485: localStringBuilder.append("}");
          return localStringBuilder.toString();
          localStringBuilder.append('V');
          break;
          localStringBuilder.append('I');
          break;
          localStringBuilder.append('G');
          break;
          c3 = c2;
          break label108;
          label533: c4 = c2;
          break label126;
          label539: c5 = 'D';
          break label143;
          label546: c6 = c2;
          break label161;
          label552: c7 = c2;
          break label179;
          label558: c8 = c2;
          break label197;
          label564: c9 = c2;
          break label215;
          label570: c1 = c2;
          break label236;
          label575: c10 = c2;
          break label253;
          str1 = "app";
          continue;
          str1 = "android";
        }
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        break label485;
      }
    }
  }

  final View dispatchFragmentsOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    return this.mFragments.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
  }

  void doReallyStop(boolean paramBoolean)
  {
    if (!this.mReallyStopped)
    {
      this.mReallyStopped = true;
      this.mRetaining = paramBoolean;
      this.mHandler.removeMessages(1);
      onReallyStop();
    }
    do
      return;
    while (!paramBoolean);
    this.mFragments.doLoaderStart();
    this.mFragments.doLoaderStop(true);
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (Build.VERSION.SDK_INT >= 11);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local FragmentActivity ");
    paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
    paramPrintWriter.println(" State:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("mCreated=");
    paramPrintWriter.print(this.mCreated);
    paramPrintWriter.print("mResumed=");
    paramPrintWriter.print(this.mResumed);
    paramPrintWriter.print(" mStopped=");
    paramPrintWriter.print(this.mStopped);
    paramPrintWriter.print(" mReallyStopped=");
    paramPrintWriter.println(this.mReallyStopped);
    this.mFragments.dumpLoaders(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    this.mFragments.getSupportFragmentManager().dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    dumpViewHierarchy(paramString + "  ", paramPrintWriter, getWindow().getDecorView());
  }

  public Object getLastCustomNonConfigurationInstance()
  {
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null)
      return localNonConfigurationInstances.custom;
    return null;
  }

  public FragmentManager getSupportFragmentManager()
  {
    return this.mFragments.getSupportFragmentManager();
  }

  public LoaderManager getSupportLoaderManager()
  {
    return this.mFragments.getSupportLoaderManager();
  }

  @Deprecated
  public final MediaControllerCompat getSupportMediaController()
  {
    return MediaControllerCompat.getMediaController(this);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mFragments.noteStateNotSaved();
    int i = paramInt1 >> 16;
    if (i != 0)
    {
      int j = i - 1;
      String str = (String)this.mPendingFragmentActivityResults.get(j);
      this.mPendingFragmentActivityResults.remove(j);
      if (str == null)
      {
        Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
        return;
      }
      Fragment localFragment = this.mFragments.findFragmentByWho(str);
      if (localFragment == null)
      {
        Log.w("FragmentActivity", "Activity result no fragment exists for who: " + str);
        return;
      }
      localFragment.onActivityResult(0xFFFF & paramInt1, paramInt2, paramIntent);
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onAttachFragment(Fragment paramFragment)
  {
  }

  public void onBackPressed()
  {
    if (!this.mFragments.getSupportFragmentManager().popBackStackImmediate())
      super.onBackPressed();
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mFragments.dispatchConfigurationChanged(paramConfiguration);
  }

  protected void onCreate(@Nullable Bundle paramBundle)
  {
    this.mFragments.attachHost(null);
    super.onCreate(paramBundle);
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null)
      this.mFragments.restoreLoaderNonConfig(localNonConfigurationInstances.loaders);
    Parcelable localParcelable;
    FragmentController localFragmentController;
    if (paramBundle != null)
    {
      localParcelable = paramBundle.getParcelable("android:support:fragments");
      localFragmentController = this.mFragments;
      if (localNonConfigurationInstances == null)
        break label213;
    }
    label165: label213: for (FragmentManagerNonConfig localFragmentManagerNonConfig = localNonConfigurationInstances.fragments; ; localFragmentManagerNonConfig = null)
    {
      localFragmentController.restoreAllState(localParcelable, localFragmentManagerNonConfig);
      int[] arrayOfInt;
      String[] arrayOfString;
      if (paramBundle.containsKey("android:support:next_request_index"))
      {
        this.mNextCandidateRequestIndex = paramBundle.getInt("android:support:next_request_index");
        arrayOfInt = paramBundle.getIntArray("android:support:request_indicies");
        arrayOfString = paramBundle.getStringArray("android:support:request_fragment_who");
        if ((arrayOfInt != null) && (arrayOfString != null) && (arrayOfInt.length == arrayOfString.length))
          break label165;
        Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
      }
      while (true)
      {
        if (this.mPendingFragmentActivityResults == null)
        {
          this.mPendingFragmentActivityResults = new SparseArrayCompat();
          this.mNextCandidateRequestIndex = 0;
        }
        this.mFragments.dispatchCreate();
        return;
        this.mPendingFragmentActivityResults = new SparseArrayCompat(arrayOfInt.length);
        for (int i = 0; i < arrayOfInt.length; i++)
          this.mPendingFragmentActivityResults.put(arrayOfInt[i], arrayOfString[i]);
      }
    }
  }

  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    if (paramInt == 0)
    {
      boolean bool = super.onCreatePanelMenu(paramInt, paramMenu) | this.mFragments.dispatchCreateOptionsMenu(paramMenu, getMenuInflater());
      if (Build.VERSION.SDK_INT >= 11)
        return bool;
      return true;
    }
    return super.onCreatePanelMenu(paramInt, paramMenu);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    doReallyStop(false);
    this.mFragments.dispatchDestroy();
    this.mFragments.doLoaderDestroy();
  }

  public void onLowMemory()
  {
    super.onLowMemory();
    this.mFragments.dispatchLowMemory();
  }

  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    if (super.onMenuItemSelected(paramInt, paramMenuItem))
      return true;
    switch (paramInt)
    {
    default:
      return false;
    case 0:
      return this.mFragments.dispatchOptionsItemSelected(paramMenuItem);
    case 6:
    }
    return this.mFragments.dispatchContextItemSelected(paramMenuItem);
  }

  @CallSuper
  public void onMultiWindowModeChanged(boolean paramBoolean)
  {
    this.mFragments.dispatchMultiWindowModeChanged(paramBoolean);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    this.mFragments.noteStateNotSaved();
  }

  public void onPanelClosed(int paramInt, Menu paramMenu)
  {
    switch (paramInt)
    {
    default:
    case 0:
    }
    while (true)
    {
      super.onPanelClosed(paramInt, paramMenu);
      return;
      this.mFragments.dispatchOptionsMenuClosed(paramMenu);
    }
  }

  protected void onPause()
  {
    super.onPause();
    this.mResumed = false;
    if (this.mHandler.hasMessages(2))
    {
      this.mHandler.removeMessages(2);
      onResumeFragments();
    }
    this.mFragments.dispatchPause();
  }

  @CallSuper
  public void onPictureInPictureModeChanged(boolean paramBoolean)
  {
    this.mFragments.dispatchPictureInPictureModeChanged(paramBoolean);
  }

  protected void onPostResume()
  {
    super.onPostResume();
    this.mHandler.removeMessages(2);
    onResumeFragments();
    this.mFragments.execPendingActions();
  }

  @RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
  protected boolean onPrepareOptionsPanel(View paramView, Menu paramMenu)
  {
    return super.onPreparePanel(0, paramView, paramMenu);
  }

  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    if ((paramInt == 0) && (paramMenu != null))
    {
      if (this.mOptionsMenuInvalidated)
      {
        this.mOptionsMenuInvalidated = false;
        paramMenu.clear();
        onCreatePanelMenu(paramInt, paramMenu);
      }
      return onPrepareOptionsPanel(paramView, paramMenu) | this.mFragments.dispatchPrepareOptionsMenu(paramMenu);
    }
    return super.onPreparePanel(paramInt, paramView, paramMenu);
  }

  void onReallyStop()
  {
    this.mFragments.doLoaderStop(this.mRetaining);
    this.mFragments.dispatchReallyStop();
  }

  public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfInt)
  {
    int i = 0xFFFF & paramInt >> 16;
    String str;
    if (i != 0)
    {
      int j = i - 1;
      str = (String)this.mPendingFragmentActivityResults.get(j);
      this.mPendingFragmentActivityResults.remove(j);
      if (str == null)
        Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
    }
    else
    {
      return;
    }
    Fragment localFragment = this.mFragments.findFragmentByWho(str);
    if (localFragment == null)
    {
      Log.w("FragmentActivity", "Activity result no fragment exists for who: " + str);
      return;
    }
    localFragment.onRequestPermissionsResult(paramInt & 0xFFFF, paramArrayOfString, paramArrayOfInt);
  }

  protected void onResume()
  {
    super.onResume();
    this.mHandler.sendEmptyMessage(2);
    this.mResumed = true;
    this.mFragments.execPendingActions();
  }

  protected void onResumeFragments()
  {
    this.mFragments.dispatchResume();
  }

  public Object onRetainCustomNonConfigurationInstance()
  {
    return null;
  }

  public final Object onRetainNonConfigurationInstance()
  {
    if (this.mStopped)
      doReallyStop(true);
    Object localObject = onRetainCustomNonConfigurationInstance();
    FragmentManagerNonConfig localFragmentManagerNonConfig = this.mFragments.retainNestedNonConfig();
    SimpleArrayMap localSimpleArrayMap = this.mFragments.retainLoaderNonConfig();
    if ((localFragmentManagerNonConfig == null) && (localSimpleArrayMap == null) && (localObject == null))
      return null;
    NonConfigurationInstances localNonConfigurationInstances = new NonConfigurationInstances();
    localNonConfigurationInstances.custom = localObject;
    localNonConfigurationInstances.fragments = localFragmentManagerNonConfig;
    localNonConfigurationInstances.loaders = localSimpleArrayMap;
    return localNonConfigurationInstances;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Parcelable localParcelable = this.mFragments.saveAllState();
    if (localParcelable != null)
      paramBundle.putParcelable("android:support:fragments", localParcelable);
    if (this.mPendingFragmentActivityResults.size() > 0)
    {
      paramBundle.putInt("android:support:next_request_index", this.mNextCandidateRequestIndex);
      int[] arrayOfInt = new int[this.mPendingFragmentActivityResults.size()];
      String[] arrayOfString = new String[this.mPendingFragmentActivityResults.size()];
      for (int i = 0; i < this.mPendingFragmentActivityResults.size(); i++)
      {
        arrayOfInt[i] = this.mPendingFragmentActivityResults.keyAt(i);
        arrayOfString[i] = ((String)this.mPendingFragmentActivityResults.valueAt(i));
      }
      paramBundle.putIntArray("android:support:request_indicies", arrayOfInt);
      paramBundle.putStringArray("android:support:request_fragment_who", arrayOfString);
    }
  }

  protected void onStart()
  {
    super.onStart();
    this.mStopped = false;
    this.mReallyStopped = false;
    this.mHandler.removeMessages(1);
    if (!this.mCreated)
    {
      this.mCreated = true;
      this.mFragments.dispatchActivityCreated();
    }
    this.mFragments.noteStateNotSaved();
    this.mFragments.execPendingActions();
    this.mFragments.doLoaderStart();
    this.mFragments.dispatchStart();
    this.mFragments.reportLoaderStart();
  }

  public void onStateNotSaved()
  {
    this.mFragments.noteStateNotSaved();
  }

  protected void onStop()
  {
    super.onStop();
    this.mStopped = true;
    this.mHandler.sendEmptyMessage(1);
    this.mFragments.dispatchStop();
  }

  void requestPermissionsFromFragment(Fragment paramFragment, String[] paramArrayOfString, int paramInt)
  {
    if (paramInt == -1)
    {
      ActivityCompat.requestPermissions(this, paramArrayOfString, paramInt);
      return;
    }
    checkForValidRequestCode(paramInt);
    try
    {
      this.mRequestedPermissionsFromFragment = true;
      ActivityCompat.requestPermissions(this, paramArrayOfString, (1 + allocateRequestIndex(paramFragment) << 16) + (0xFFFF & paramInt));
      return;
    }
    finally
    {
      this.mRequestedPermissionsFromFragment = false;
    }
    throw localObject;
  }

  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback)
  {
    ActivityCompat.setEnterSharedElementCallback(this, paramSharedElementCallback);
  }

  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback)
  {
    ActivityCompat.setExitSharedElementCallback(this, paramSharedElementCallback);
  }

  @Deprecated
  public final void setSupportMediaController(MediaControllerCompat paramMediaControllerCompat)
  {
    MediaControllerCompat.setMediaController(this, paramMediaControllerCompat);
  }

  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if ((!this.mStartedActivityFromFragment) && (paramInt != -1))
      checkForValidRequestCode(paramInt);
    super.startActivityForResult(paramIntent, paramInt);
  }

  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
  {
    startActivityFromFragment(paramFragment, paramIntent, paramInt, null);
  }

  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, @Nullable Bundle paramBundle)
  {
    this.mStartedActivityFromFragment = true;
    if (paramInt == -1);
    try
    {
      ActivityCompat.startActivityForResult(this, paramIntent, -1, paramBundle);
      return;
      checkForValidRequestCode(paramInt);
      ActivityCompat.startActivityForResult(this, paramIntent, (1 + allocateRequestIndex(paramFragment) << 16) + (0xFFFF & paramInt), paramBundle);
      return;
    }
    finally
    {
      this.mStartedActivityFromFragment = false;
    }
    throw localObject;
  }

  public void startIntentSenderFromFragment(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, @Nullable Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    this.mStartedIntentSenderFromFragment = true;
    if (paramInt1 == -1);
    try
    {
      ActivityCompat.startIntentSenderForResult(this, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      return;
      checkForValidRequestCode(paramInt1);
      ActivityCompat.startIntentSenderForResult(this, paramIntentSender, (1 + allocateRequestIndex(paramFragment) << 16) + (0xFFFF & paramInt1), paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      return;
    }
    finally
    {
      this.mStartedIntentSenderFromFragment = false;
    }
    throw localObject;
  }

  public void supportFinishAfterTransition()
  {
    ActivityCompat.finishAfterTransition(this);
  }

  public void supportInvalidateOptionsMenu()
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      ActivityCompatHoneycomb.invalidateOptionsMenu(this);
      return;
    }
    this.mOptionsMenuInvalidated = true;
  }

  public void supportPostponeEnterTransition()
  {
    ActivityCompat.postponeEnterTransition(this);
  }

  public void supportStartPostponedEnterTransition()
  {
    ActivityCompat.startPostponedEnterTransition(this);
  }

  public final void validateRequestPermissionsRequestCode(int paramInt)
  {
    if ((!this.mRequestedPermissionsFromFragment) && (paramInt != -1))
      checkForValidRequestCode(paramInt);
  }

  class HostCallbacks extends FragmentHostCallback<FragmentActivity>
  {
    public HostCallbacks()
    {
      super();
    }

    public void onAttachFragment(Fragment paramFragment)
    {
      FragmentActivity.this.onAttachFragment(paramFragment);
    }

    @SuppressLint({"NewApi"})
    public void onDump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      FragmentActivity.this.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }

    @Nullable
    public View onFindViewById(int paramInt)
    {
      return FragmentActivity.this.findViewById(paramInt);
    }

    public FragmentActivity onGetHost()
    {
      return FragmentActivity.this;
    }

    public LayoutInflater onGetLayoutInflater()
    {
      return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
    }

    public int onGetWindowAnimations()
    {
      Window localWindow = FragmentActivity.this.getWindow();
      if (localWindow == null)
        return 0;
      return localWindow.getAttributes().windowAnimations;
    }

    public boolean onHasView()
    {
      Window localWindow = FragmentActivity.this.getWindow();
      return (localWindow != null) && (localWindow.peekDecorView() != null);
    }

    public boolean onHasWindowAnimations()
    {
      return FragmentActivity.this.getWindow() != null;
    }

    public void onRequestPermissionsFromFragment(@NonNull Fragment paramFragment, @NonNull String[] paramArrayOfString, int paramInt)
    {
      FragmentActivity.this.requestPermissionsFromFragment(paramFragment, paramArrayOfString, paramInt);
    }

    public boolean onShouldSaveFragmentState(Fragment paramFragment)
    {
      return !FragmentActivity.this.isFinishing();
    }

    public boolean onShouldShowRequestPermissionRationale(@NonNull String paramString)
    {
      return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, paramString);
    }

    public void onStartActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
    {
      FragmentActivity.this.startActivityFromFragment(paramFragment, paramIntent, paramInt);
    }

    public void onStartActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, @Nullable Bundle paramBundle)
    {
      FragmentActivity.this.startActivityFromFragment(paramFragment, paramIntent, paramInt, paramBundle);
    }

    public void onStartIntentSenderFromFragment(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, @Nullable Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
      throws IntentSender.SendIntentException
    {
      FragmentActivity.this.startIntentSenderFromFragment(paramFragment, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
    }

    public void onSupportInvalidateOptionsMenu()
    {
      FragmentActivity.this.supportInvalidateOptionsMenu();
    }
  }

  static final class NonConfigurationInstances
  {
    Object custom;
    FragmentManagerNonConfig fragments;
    SimpleArrayMap<String, LoaderManager> loaders;
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.FragmentActivity
 * JD-Core Version:    0.6.0
 */