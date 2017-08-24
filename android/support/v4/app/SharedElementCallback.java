package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback
{
  private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
  private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
  private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
  private static int MAX_IMAGE_SIZE = 1048576;
  private Matrix mTempMatrix;

  private static Bitmap createDrawableBitmap(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if ((i <= 0) || (j <= 0))
      return null;
    float f = Math.min(1.0F, MAX_IMAGE_SIZE / (i * j));
    if (((paramDrawable instanceof BitmapDrawable)) && (f == 1.0F))
      return ((BitmapDrawable)paramDrawable).getBitmap();
    int k = (int)(f * i);
    int m = (int)(f * j);
    Bitmap localBitmap = Bitmap.createBitmap(k, m, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Rect localRect = paramDrawable.getBounds();
    int n = localRect.left;
    int i1 = localRect.top;
    int i2 = localRect.right;
    int i3 = localRect.bottom;
    paramDrawable.setBounds(0, 0, k, m);
    paramDrawable.draw(localCanvas);
    paramDrawable.setBounds(n, i1, i2, i3);
    return localBitmap;
  }

  public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF)
  {
    Bundle localBundle2;
    Bundle localBundle1;
    if ((paramView instanceof ImageView))
    {
      ImageView localImageView = (ImageView)paramView;
      Drawable localDrawable1 = localImageView.getDrawable();
      Drawable localDrawable2 = localImageView.getBackground();
      if ((localDrawable1 != null) && (localDrawable2 == null))
      {
        Bitmap localBitmap2 = createDrawableBitmap(localDrawable1);
        if (localBitmap2 != null)
        {
          localBundle2 = new Bundle();
          localBundle2.putParcelable("sharedElement:snapshot:bitmap", localBitmap2);
          localBundle2.putString("sharedElement:snapshot:imageScaleType", localImageView.getScaleType().toString());
          if (localImageView.getScaleType() != ImageView.ScaleType.MATRIX)
            break label295;
          Matrix localMatrix = localImageView.getImageMatrix();
          float[] arrayOfFloat = new float[9];
          localMatrix.getValues(arrayOfFloat);
          localBundle2.putFloatArray("sharedElement:snapshot:imageMatrix", arrayOfFloat);
          localBundle1 = localBundle2;
        }
      }
    }
    int i;
    int j;
    do
    {
      do
      {
        return localBundle1;
        i = Math.round(paramRectF.width());
        j = Math.round(paramRectF.height());
        localBundle1 = null;
      }
      while (i <= 0);
      localBundle1 = null;
    }
    while (j <= 0);
    float f = Math.min(1.0F, MAX_IMAGE_SIZE / (i * j));
    int k = (int)(f * i);
    int m = (int)(f * j);
    if (this.mTempMatrix == null)
      this.mTempMatrix = new Matrix();
    this.mTempMatrix.set(paramMatrix);
    this.mTempMatrix.postTranslate(-paramRectF.left, -paramRectF.top);
    this.mTempMatrix.postScale(f, f);
    Bitmap localBitmap1 = Bitmap.createBitmap(k, m, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap1);
    localCanvas.concat(this.mTempMatrix);
    paramView.draw(localCanvas);
    return localBitmap1;
    label295: return localBundle2;
  }

  public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable)
  {
    ImageView localImageView2;
    ImageView localImageView1;
    if ((paramParcelable instanceof Bundle))
    {
      Bundle localBundle = (Bundle)paramParcelable;
      Bitmap localBitmap2 = (Bitmap)localBundle.getParcelable("sharedElement:snapshot:bitmap");
      if (localBitmap2 == null)
        return null;
      localImageView2 = new ImageView(paramContext);
      localImageView2.setImageBitmap(localBitmap2);
      localImageView2.setScaleType(ImageView.ScaleType.valueOf(localBundle.getString("sharedElement:snapshot:imageScaleType")));
      if (localImageView2.getScaleType() != ImageView.ScaleType.MATRIX)
        break label148;
      float[] arrayOfFloat = localBundle.getFloatArray("sharedElement:snapshot:imageMatrix");
      Matrix localMatrix = new Matrix();
      localMatrix.setValues(arrayOfFloat);
      localImageView2.setImageMatrix(localMatrix);
      localImageView1 = localImageView2;
    }
    while (true)
    {
      return localImageView1;
      if ((paramParcelable instanceof Bitmap))
      {
        Bitmap localBitmap1 = (Bitmap)paramParcelable;
        localImageView1 = new ImageView(paramContext);
        localImageView1.setImageBitmap(localBitmap1);
        continue;
      }
      localImageView1 = null;
      continue;
      label148: localImageView1 = localImageView2;
    }
  }

  public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap)
  {
  }

  public void onRejectSharedElements(List<View> paramList)
  {
  }

  public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2)
  {
  }

  public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2)
  {
  }

  public void onSharedElementsArrived(List<String> paramList, List<View> paramList1, OnSharedElementsReadyListener paramOnSharedElementsReadyListener)
  {
    paramOnSharedElementsReadyListener.onSharedElementsReady();
  }

  public static abstract interface OnSharedElementsReadyListener
  {
    public abstract void onSharedElementsReady();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.app.SharedElementCallback
 * JD-Core Version:    0.6.0
 */