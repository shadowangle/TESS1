package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ListView;

@TargetApi(9)
@RequiresApi(9)
class ListViewCompatGingerbread
{
  static void scrollListBy(ListView paramListView, int paramInt)
  {
    int i = paramListView.getFirstVisiblePosition();
    if (i == -1);
    View localView;
    do
    {
      return;
      localView = paramListView.getChildAt(0);
    }
    while (localView == null);
    paramListView.setSelectionFromTop(i, localView.getTop() - paramInt);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.widget.ListViewCompatGingerbread
 * JD-Core Version:    0.6.0
 */