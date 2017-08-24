package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@TargetApi(19)
@RequiresApi(19)
class PrintHelperKitkat
{
  public static final int COLOR_MODE_COLOR = 2;
  public static final int COLOR_MODE_MONOCHROME = 1;
  private static final String LOG_TAG = "PrintHelperKitkat";
  private static final int MAX_PRINT_SIZE = 3500;
  public static final int ORIENTATION_LANDSCAPE = 1;
  public static final int ORIENTATION_PORTRAIT = 2;
  public static final int SCALE_MODE_FILL = 2;
  public static final int SCALE_MODE_FIT = 1;
  int mColorMode = 2;
  final Context mContext;
  BitmapFactory.Options mDecodeOptions = null;
  protected boolean mIsMinMarginsHandlingCorrect = true;
  private final Object mLock = new Object();
  int mOrientation;
  protected boolean mPrintActivityRespectsOrientation = true;
  int mScaleMode = 2;

  PrintHelperKitkat(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt)
  {
    if (paramInt != 1)
      return paramBitmap;
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    ColorMatrix localColorMatrix = new ColorMatrix();
    localColorMatrix.setSaturation(0.0F);
    localPaint.setColorFilter(new ColorMatrixColorFilter(localColorMatrix));
    localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, localPaint);
    localCanvas.setBitmap(null);
    return localBitmap;
  }

  private Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3)
  {
    Matrix localMatrix = new Matrix();
    float f1 = paramRectF.width() / paramInt1;
    float f2;
    if (paramInt3 == 2)
      f2 = Math.max(f1, paramRectF.height() / paramInt2);
    while (true)
    {
      localMatrix.postScale(f2, f2);
      localMatrix.postTranslate((paramRectF.width() - f2 * paramInt1) / 2.0F, (paramRectF.height() - f2 * paramInt2) / 2.0F);
      return localMatrix;
      f2 = Math.min(f1, paramRectF.height() / paramInt2);
    }
  }

  private static boolean isPortrait(Bitmap paramBitmap)
  {
    return paramBitmap.getWidth() <= paramBitmap.getHeight();
  }

  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions)
    throws FileNotFoundException
  {
    InputStream localInputStream = null;
    if ((paramUri == null) || (this.mContext == null))
      throw new IllegalArgumentException("bad argument to loadBitmap");
    try
    {
      localInputStream = this.mContext.getContentResolver().openInputStream(paramUri);
      Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream, null, paramOptions);
      if (localInputStream != null);
      try
      {
        localInputStream.close();
        return localBitmap;
      }
      catch (IOException localIOException2)
      {
        Log.w("PrintHelperKitkat", "close fail ", localIOException2);
        return localBitmap;
      }
    }
    finally
    {
      if (localInputStream == null);
    }
    try
    {
      localInputStream.close();
      throw localObject;
    }
    catch (IOException localIOException1)
    {
      while (true)
        Log.w("PrintHelperKitkat", "close fail ", localIOException1);
    }
  }

  // ERROR //
  private Bitmap loadConstrainedBitmap(Uri paramUri, int paramInt)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iload_2
    //   3: ifle +14 -> 17
    //   6: aload_1
    //   7: ifnull +10 -> 17
    //   10: aload_0
    //   11: getfield 54	android/support/v4/print/PrintHelperKitkat:mContext	Landroid/content/Context;
    //   14: ifnonnull +13 -> 27
    //   17: new 171	java/lang/IllegalArgumentException
    //   20: dup
    //   21: ldc 209
    //   23: invokespecial 176	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   26: athrow
    //   27: new 211	android/graphics/BitmapFactory$Options
    //   30: dup
    //   31: invokespecial 212	android/graphics/BitmapFactory$Options:<init>	()V
    //   34: astore 4
    //   36: aload 4
    //   38: iload_3
    //   39: putfield 215	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   42: aload_0
    //   43: aload_1
    //   44: aload 4
    //   46: invokespecial 217	android/support/v4/print/PrintHelperKitkat:loadBitmap	(Landroid/net/Uri;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   49: pop
    //   50: aload 4
    //   52: getfield 220	android/graphics/BitmapFactory$Options:outWidth	I
    //   55: istore 6
    //   57: aload 4
    //   59: getfield 223	android/graphics/BitmapFactory$Options:outHeight	I
    //   62: istore 7
    //   64: iload 6
    //   66: ifle +8 -> 74
    //   69: iload 7
    //   71: ifgt +5 -> 76
    //   74: aconst_null
    //   75: areturn
    //   76: iload 6
    //   78: iload 7
    //   80: invokestatic 226	java/lang/Math:max	(II)I
    //   83: istore 8
    //   85: iload 8
    //   87: iload_2
    //   88: if_icmple +16 -> 104
    //   91: iload 8
    //   93: iconst_1
    //   94: iushr
    //   95: istore 8
    //   97: iload_3
    //   98: iconst_1
    //   99: ishl
    //   100: istore_3
    //   101: goto -16 -> 85
    //   104: iload_3
    //   105: ifle -31 -> 74
    //   108: iload 6
    //   110: iload 7
    //   112: invokestatic 228	java/lang/Math:min	(II)I
    //   115: iload_3
    //   116: idiv
    //   117: ifle -43 -> 74
    //   120: aload_0
    //   121: getfield 44	android/support/v4/print/PrintHelperKitkat:mLock	Ljava/lang/Object;
    //   124: astore 9
    //   126: aload 9
    //   128: monitorenter
    //   129: aload_0
    //   130: new 211	android/graphics/BitmapFactory$Options
    //   133: dup
    //   134: invokespecial 212	android/graphics/BitmapFactory$Options:<init>	()V
    //   137: putfield 42	android/support/v4/print/PrintHelperKitkat:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
    //   140: aload_0
    //   141: getfield 42	android/support/v4/print/PrintHelperKitkat:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
    //   144: iconst_1
    //   145: putfield 231	android/graphics/BitmapFactory$Options:inMutable	Z
    //   148: aload_0
    //   149: getfield 42	android/support/v4/print/PrintHelperKitkat:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
    //   152: iload_3
    //   153: putfield 234	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   156: aload_0
    //   157: getfield 42	android/support/v4/print/PrintHelperKitkat:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
    //   160: astore 11
    //   162: aload 9
    //   164: monitorexit
    //   165: aload_0
    //   166: aload_1
    //   167: aload 11
    //   169: invokespecial 217	android/support/v4/print/PrintHelperKitkat:loadBitmap	(Landroid/net/Uri;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   172: astore 15
    //   174: aload_0
    //   175: getfield 44	android/support/v4/print/PrintHelperKitkat:mLock	Ljava/lang/Object;
    //   178: astore 16
    //   180: aload 16
    //   182: monitorenter
    //   183: aload_0
    //   184: aconst_null
    //   185: putfield 42	android/support/v4/print/PrintHelperKitkat:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
    //   188: aload 16
    //   190: monitorexit
    //   191: aload 15
    //   193: areturn
    //   194: astore 17
    //   196: aload 16
    //   198: monitorexit
    //   199: aload 17
    //   201: athrow
    //   202: astore 10
    //   204: aload 9
    //   206: monitorexit
    //   207: aload 10
    //   209: athrow
    //   210: astore 12
    //   212: aload_0
    //   213: getfield 44	android/support/v4/print/PrintHelperKitkat:mLock	Ljava/lang/Object;
    //   216: astore 13
    //   218: aload 13
    //   220: monitorenter
    //   221: aload_0
    //   222: aconst_null
    //   223: putfield 42	android/support/v4/print/PrintHelperKitkat:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
    //   226: aload 13
    //   228: monitorexit
    //   229: aload 12
    //   231: athrow
    //   232: astore 14
    //   234: aload 13
    //   236: monitorexit
    //   237: aload 14
    //   239: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   183	191	194	finally
    //   196	199	194	finally
    //   129	165	202	finally
    //   204	207	202	finally
    //   165	174	210	finally
    //   221	229	232	finally
    //   234	237	232	finally
  }

  private void writeBitmap(PrintAttributes paramPrintAttributes, int paramInt, Bitmap paramBitmap, ParcelFileDescriptor paramParcelFileDescriptor, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramWriteResultCallback)
  {
    if (this.mIsMinMarginsHandlingCorrect);
    for (PrintAttributes localPrintAttributes = paramPrintAttributes; ; localPrintAttributes = copyAttributes(paramPrintAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build())
    {
      new AsyncTask(paramCancellationSignal, localPrintAttributes, paramBitmap, paramPrintAttributes, paramInt, paramParcelFileDescriptor, paramWriteResultCallback)
      {
        // ERROR //
        protected Throwable doInBackground(Void[] paramArrayOfVoid)
        {
          // Byte code:
          //   0: aload_0
          //   1: getfield 31	android/support/v4/print/PrintHelperKitkat$2:val$cancellationSignal	Landroid/os/CancellationSignal;
          //   4: invokevirtual 63	android/os/CancellationSignal:isCanceled	()Z
          //   7: ifeq +5 -> 12
          //   10: aconst_null
          //   11: areturn
          //   12: new 65	android/print/pdf/PrintedPdfDocument
          //   15: dup
          //   16: aload_0
          //   17: getfield 29	android/support/v4/print/PrintHelperKitkat$2:this$0	Landroid/support/v4/print/PrintHelperKitkat;
          //   20: getfield 69	android/support/v4/print/PrintHelperKitkat:mContext	Landroid/content/Context;
          //   23: aload_0
          //   24: getfield 33	android/support/v4/print/PrintHelperKitkat$2:val$pdfAttributes	Landroid/print/PrintAttributes;
          //   27: invokespecial 72	android/print/pdf/PrintedPdfDocument:<init>	(Landroid/content/Context;Landroid/print/PrintAttributes;)V
          //   30: astore_3
          //   31: aload_0
          //   32: getfield 29	android/support/v4/print/PrintHelperKitkat$2:this$0	Landroid/support/v4/print/PrintHelperKitkat;
          //   35: aload_0
          //   36: getfield 35	android/support/v4/print/PrintHelperKitkat$2:val$bitmap	Landroid/graphics/Bitmap;
          //   39: aload_0
          //   40: getfield 33	android/support/v4/print/PrintHelperKitkat$2:val$pdfAttributes	Landroid/print/PrintAttributes;
          //   43: invokevirtual 78	android/print/PrintAttributes:getColorMode	()I
          //   46: invokestatic 82	android/support/v4/print/PrintHelperKitkat:access$100	(Landroid/support/v4/print/PrintHelperKitkat;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
          //   49: astore 4
          //   51: aload_0
          //   52: getfield 31	android/support/v4/print/PrintHelperKitkat$2:val$cancellationSignal	Landroid/os/CancellationSignal;
          //   55: invokevirtual 63	android/os/CancellationSignal:isCanceled	()Z
          //   58: istore 5
          //   60: iload 5
          //   62: ifne +348 -> 410
          //   65: aload_3
          //   66: iconst_1
          //   67: invokevirtual 86	android/print/pdf/PrintedPdfDocument:startPage	(I)Landroid/graphics/pdf/PdfDocument$Page;
          //   70: astore 9
          //   72: aload_0
          //   73: getfield 29	android/support/v4/print/PrintHelperKitkat$2:this$0	Landroid/support/v4/print/PrintHelperKitkat;
          //   76: getfield 90	android/support/v4/print/PrintHelperKitkat:mIsMinMarginsHandlingCorrect	Z
          //   79: ifeq +129 -> 208
          //   82: new 92	android/graphics/RectF
          //   85: dup
          //   86: aload 9
          //   88: invokevirtual 98	android/graphics/pdf/PdfDocument$Page:getInfo	()Landroid/graphics/pdf/PdfDocument$PageInfo;
          //   91: invokevirtual 104	android/graphics/pdf/PdfDocument$PageInfo:getContentRect	()Landroid/graphics/Rect;
          //   94: invokespecial 107	android/graphics/RectF:<init>	(Landroid/graphics/Rect;)V
          //   97: astore 10
          //   99: aload_0
          //   100: getfield 29	android/support/v4/print/PrintHelperKitkat$2:this$0	Landroid/support/v4/print/PrintHelperKitkat;
          //   103: aload 4
          //   105: invokevirtual 112	android/graphics/Bitmap:getWidth	()I
          //   108: aload 4
          //   110: invokevirtual 115	android/graphics/Bitmap:getHeight	()I
          //   113: aload 10
          //   115: aload_0
          //   116: getfield 39	android/support/v4/print/PrintHelperKitkat$2:val$fittingMode	I
          //   119: invokestatic 119	android/support/v4/print/PrintHelperKitkat:access$200	(Landroid/support/v4/print/PrintHelperKitkat;IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
          //   122: astore 11
          //   124: aload_0
          //   125: getfield 29	android/support/v4/print/PrintHelperKitkat$2:this$0	Landroid/support/v4/print/PrintHelperKitkat;
          //   128: getfield 90	android/support/v4/print/PrintHelperKitkat:mIsMinMarginsHandlingCorrect	Z
          //   131: ifeq +178 -> 309
          //   134: aload 9
          //   136: invokevirtual 123	android/graphics/pdf/PdfDocument$Page:getCanvas	()Landroid/graphics/Canvas;
          //   139: aload 4
          //   141: aload 11
          //   143: aconst_null
          //   144: invokevirtual 129	android/graphics/Canvas:drawBitmap	(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
          //   147: aload_3
          //   148: aload 9
          //   150: invokevirtual 133	android/print/pdf/PrintedPdfDocument:finishPage	(Landroid/graphics/pdf/PdfDocument$Page;)V
          //   153: aload_0
          //   154: getfield 31	android/support/v4/print/PrintHelperKitkat$2:val$cancellationSignal	Landroid/os/CancellationSignal;
          //   157: invokevirtual 63	android/os/CancellationSignal:isCanceled	()Z
          //   160: istore 12
          //   162: iload 12
          //   164: ifeq +175 -> 339
          //   167: aload_3
          //   168: invokevirtual 136	android/print/pdf/PrintedPdfDocument:close	()V
          //   171: aload_0
          //   172: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   175: astore 15
          //   177: aload 15
          //   179: ifnull +10 -> 189
          //   182: aload_0
          //   183: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   186: invokevirtual 139	android/os/ParcelFileDescriptor:close	()V
          //   189: aload 4
          //   191: aload_0
          //   192: getfield 35	android/support/v4/print/PrintHelperKitkat$2:val$bitmap	Landroid/graphics/Bitmap;
          //   195: if_acmpeq +215 -> 410
          //   198: aload 4
          //   200: invokevirtual 142	android/graphics/Bitmap:recycle	()V
          //   203: aconst_null
          //   204: areturn
          //   205: astore_2
          //   206: aload_2
          //   207: areturn
          //   208: new 65	android/print/pdf/PrintedPdfDocument
          //   211: dup
          //   212: aload_0
          //   213: getfield 29	android/support/v4/print/PrintHelperKitkat$2:this$0	Landroid/support/v4/print/PrintHelperKitkat;
          //   216: getfield 69	android/support/v4/print/PrintHelperKitkat:mContext	Landroid/content/Context;
          //   219: aload_0
          //   220: getfield 37	android/support/v4/print/PrintHelperKitkat$2:val$attributes	Landroid/print/PrintAttributes;
          //   223: invokespecial 72	android/print/pdf/PrintedPdfDocument:<init>	(Landroid/content/Context;Landroid/print/PrintAttributes;)V
          //   226: astore 19
          //   228: aload 19
          //   230: iconst_1
          //   231: invokevirtual 86	android/print/pdf/PrintedPdfDocument:startPage	(I)Landroid/graphics/pdf/PdfDocument$Page;
          //   234: astore 20
          //   236: new 92	android/graphics/RectF
          //   239: dup
          //   240: aload 20
          //   242: invokevirtual 98	android/graphics/pdf/PdfDocument$Page:getInfo	()Landroid/graphics/pdf/PdfDocument$PageInfo;
          //   245: invokevirtual 104	android/graphics/pdf/PdfDocument$PageInfo:getContentRect	()Landroid/graphics/Rect;
          //   248: invokespecial 107	android/graphics/RectF:<init>	(Landroid/graphics/Rect;)V
          //   251: astore 10
          //   253: aload 19
          //   255: aload 20
          //   257: invokevirtual 133	android/print/pdf/PrintedPdfDocument:finishPage	(Landroid/graphics/pdf/PdfDocument$Page;)V
          //   260: aload 19
          //   262: invokevirtual 136	android/print/pdf/PrintedPdfDocument:close	()V
          //   265: goto -166 -> 99
          //   268: astore 6
          //   270: aload_3
          //   271: invokevirtual 136	android/print/pdf/PrintedPdfDocument:close	()V
          //   274: aload_0
          //   275: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   278: astore 7
          //   280: aload 7
          //   282: ifnull +10 -> 292
          //   285: aload_0
          //   286: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   289: invokevirtual 139	android/os/ParcelFileDescriptor:close	()V
          //   292: aload 4
          //   294: aload_0
          //   295: getfield 35	android/support/v4/print/PrintHelperKitkat$2:val$bitmap	Landroid/graphics/Bitmap;
          //   298: if_acmpeq +8 -> 306
          //   301: aload 4
          //   303: invokevirtual 142	android/graphics/Bitmap:recycle	()V
          //   306: aload 6
          //   308: athrow
          //   309: aload 11
          //   311: aload 10
          //   313: getfield 146	android/graphics/RectF:left	F
          //   316: aload 10
          //   318: getfield 149	android/graphics/RectF:top	F
          //   321: invokevirtual 155	android/graphics/Matrix:postTranslate	(FF)Z
          //   324: pop
          //   325: aload 9
          //   327: invokevirtual 123	android/graphics/pdf/PdfDocument$Page:getCanvas	()Landroid/graphics/Canvas;
          //   330: aload 10
          //   332: invokevirtual 159	android/graphics/Canvas:clipRect	(Landroid/graphics/RectF;)Z
          //   335: pop
          //   336: goto -202 -> 134
          //   339: aload_3
          //   340: new 161	java/io/FileOutputStream
          //   343: dup
          //   344: aload_0
          //   345: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   348: invokevirtual 165	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
          //   351: invokespecial 168	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
          //   354: invokevirtual 172	android/print/pdf/PrintedPdfDocument:writeTo	(Ljava/io/OutputStream;)V
          //   357: aload_3
          //   358: invokevirtual 136	android/print/pdf/PrintedPdfDocument:close	()V
          //   361: aload_0
          //   362: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   365: astore 13
          //   367: aload 13
          //   369: ifnull +10 -> 379
          //   372: aload_0
          //   373: getfield 41	android/support/v4/print/PrintHelperKitkat$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
          //   376: invokevirtual 139	android/os/ParcelFileDescriptor:close	()V
          //   379: aload 4
          //   381: aload_0
          //   382: getfield 35	android/support/v4/print/PrintHelperKitkat$2:val$bitmap	Landroid/graphics/Bitmap;
          //   385: if_acmpeq +25 -> 410
          //   388: aload 4
          //   390: invokevirtual 142	android/graphics/Bitmap:recycle	()V
          //   393: aconst_null
          //   394: areturn
          //   395: astore 16
          //   397: goto -208 -> 189
          //   400: astore 14
          //   402: goto -23 -> 379
          //   405: astore 8
          //   407: goto -115 -> 292
          //   410: aconst_null
          //   411: areturn
          //
          // Exception table:
          //   from	to	target	type
          //   0	10	205	java/lang/Throwable
          //   12	60	205	java/lang/Throwable
          //   167	177	205	java/lang/Throwable
          //   182	189	205	java/lang/Throwable
          //   189	203	205	java/lang/Throwable
          //   270	280	205	java/lang/Throwable
          //   285	292	205	java/lang/Throwable
          //   292	306	205	java/lang/Throwable
          //   306	309	205	java/lang/Throwable
          //   357	367	205	java/lang/Throwable
          //   372	379	205	java/lang/Throwable
          //   379	393	205	java/lang/Throwable
          //   65	99	268	finally
          //   99	134	268	finally
          //   134	162	268	finally
          //   208	265	268	finally
          //   309	336	268	finally
          //   339	357	268	finally
          //   182	189	395	java/io/IOException
          //   372	379	400	java/io/IOException
          //   285	292	405	java/io/IOException
        }

        protected void onPostExecute(Throwable paramThrowable)
        {
          if (this.val$cancellationSignal.isCanceled())
          {
            this.val$writeResultCallback.onWriteCancelled();
            return;
          }
          if (paramThrowable == null)
          {
            PrintDocumentAdapter.WriteResultCallback localWriteResultCallback = this.val$writeResultCallback;
            PageRange[] arrayOfPageRange = new PageRange[1];
            arrayOfPageRange[0] = PageRange.ALL_PAGES;
            localWriteResultCallback.onWriteFinished(arrayOfPageRange);
            return;
          }
          Log.e("PrintHelperKitkat", "Error writing printed content", paramThrowable);
          this.val$writeResultCallback.onWriteFailed(null);
        }
      }
      .execute(new Void[0]);
      return;
    }
  }

  protected PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes)
  {
    PrintAttributes.Builder localBuilder = new PrintAttributes.Builder().setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
    if (paramPrintAttributes.getColorMode() != 0)
      localBuilder.setColorMode(paramPrintAttributes.getColorMode());
    return localBuilder;
  }

  public int getColorMode()
  {
    return this.mColorMode;
  }

  public int getOrientation()
  {
    if (this.mOrientation == 0)
      return 1;
    return this.mOrientation;
  }

  public int getScaleMode()
  {
    return this.mScaleMode;
  }

  public void printBitmap(String paramString, Bitmap paramBitmap, OnPrintFinishCallback paramOnPrintFinishCallback)
  {
    if (paramBitmap == null)
      return;
    int i = this.mScaleMode;
    PrintManager localPrintManager = (PrintManager)this.mContext.getSystemService("print");
    if (isPortrait(paramBitmap));
    for (PrintAttributes.MediaSize localMediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT; ; localMediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE)
    {
      PrintAttributes localPrintAttributes = new PrintAttributes.Builder().setMediaSize(localMediaSize).setColorMode(this.mColorMode).build();
      localPrintManager.print(paramString, new PrintDocumentAdapter(paramString, i, paramBitmap, paramOnPrintFinishCallback)
      {
        private PrintAttributes mAttributes;

        public void onFinish()
        {
          if (this.val$callback != null)
            this.val$callback.onFinish();
        }

        public void onLayout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.LayoutResultCallback paramLayoutResultCallback, Bundle paramBundle)
        {
          int i = 1;
          this.mAttributes = paramPrintAttributes2;
          PrintDocumentInfo localPrintDocumentInfo = new PrintDocumentInfo.Builder(this.val$jobName).setContentType(i).setPageCount(i).build();
          if (!paramPrintAttributes2.equals(paramPrintAttributes1));
          while (true)
          {
            paramLayoutResultCallback.onLayoutFinished(localPrintDocumentInfo, i);
            return;
            int j = 0;
          }
        }

        public void onWrite(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramWriteResultCallback)
        {
          PrintHelperKitkat.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.val$bitmap, paramParcelFileDescriptor, paramCancellationSignal, paramWriteResultCallback);
        }
      }
      , localPrintAttributes);
      return;
    }
  }

  public void printBitmap(String paramString, Uri paramUri, OnPrintFinishCallback paramOnPrintFinishCallback)
    throws FileNotFoundException
  {
    3 local3 = new PrintDocumentAdapter(paramString, paramUri, paramOnPrintFinishCallback, this.mScaleMode)
    {
      private PrintAttributes mAttributes;
      Bitmap mBitmap = null;
      AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;

      private void cancelLoad()
      {
        synchronized (PrintHelperKitkat.this.mLock)
        {
          if (PrintHelperKitkat.this.mDecodeOptions != null)
          {
            PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
            PrintHelperKitkat.this.mDecodeOptions = null;
          }
          return;
        }
      }

      public void onFinish()
      {
        super.onFinish();
        cancelLoad();
        if (this.mLoadBitmap != null)
          this.mLoadBitmap.cancel(true);
        if (this.val$callback != null)
          this.val$callback.onFinish();
        if (this.mBitmap != null)
        {
          this.mBitmap.recycle();
          this.mBitmap = null;
        }
      }

      public void onLayout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.LayoutResultCallback paramLayoutResultCallback, Bundle paramBundle)
      {
        int i = 1;
        monitorenter;
        try
        {
          this.mAttributes = paramPrintAttributes2;
          monitorexit;
          if (paramCancellationSignal.isCanceled())
          {
            paramLayoutResultCallback.onLayoutCancelled();
            return;
          }
        }
        finally
        {
          monitorexit;
        }
        if (this.mBitmap != null)
        {
          PrintDocumentInfo localPrintDocumentInfo = new PrintDocumentInfo.Builder(this.val$jobName).setContentType(i).setPageCount(i).build();
          if (!paramPrintAttributes2.equals(paramPrintAttributes1));
          while (true)
          {
            paramLayoutResultCallback.onLayoutFinished(localPrintDocumentInfo, i);
            return;
            int j = 0;
          }
        }
        this.mLoadBitmap = new AsyncTask(paramCancellationSignal, paramPrintAttributes2, paramPrintAttributes1, paramLayoutResultCallback)
        {
          protected Bitmap doInBackground(Uri[] paramArrayOfUri)
          {
            try
            {
              Bitmap localBitmap = PrintHelperKitkat.this.loadConstrainedBitmap(PrintHelperKitkat.3.this.val$imageFile, 3500);
              return localBitmap;
            }
            catch (FileNotFoundException localFileNotFoundException)
            {
            }
            return null;
          }

          protected void onCancelled(Bitmap paramBitmap)
          {
            this.val$layoutResultCallback.onLayoutCancelled();
            PrintHelperKitkat.3.this.mLoadBitmap = null;
          }

          protected void onPostExecute(Bitmap paramBitmap)
          {
            int i = 1;
            super.onPostExecute(paramBitmap);
            if ((paramBitmap != null) && ((!PrintHelperKitkat.this.mPrintActivityRespectsOrientation) || (PrintHelperKitkat.this.mOrientation == 0)))
              monitorenter;
            while (true)
            {
              try
              {
                PrintAttributes.MediaSize localMediaSize = PrintHelperKitkat.3.this.mAttributes.getMediaSize();
                monitorexit;
                if ((localMediaSize == null) || (localMediaSize.isPortrait() == PrintHelperKitkat.access$600(paramBitmap)))
                  continue;
                Matrix localMatrix = new Matrix();
                localMatrix.postRotate(90.0F);
                int k = paramBitmap.getWidth();
                int m = paramBitmap.getHeight();
                paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, k, m, localMatrix, i);
                PrintHelperKitkat.3.this.mBitmap = paramBitmap;
                if (paramBitmap == null)
                  break label195;
                PrintDocumentInfo localPrintDocumentInfo = new PrintDocumentInfo.Builder(PrintHelperKitkat.3.this.val$jobName).setContentType(i).setPageCount(i).build();
                if (!this.val$newPrintAttributes.equals(this.val$oldPrintAttributes))
                {
                  this.val$layoutResultCallback.onLayoutFinished(localPrintDocumentInfo, i);
                  PrintHelperKitkat.3.this.mLoadBitmap = null;
                  return;
                }
              }
              finally
              {
                monitorexit;
              }
              int j = 0;
              continue;
              label195: this.val$layoutResultCallback.onLayoutFailed(null);
            }
          }

          protected void onPreExecute()
          {
            this.val$cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener()
            {
              public void onCancel()
              {
                PrintHelperKitkat.3.this.cancelLoad();
                PrintHelperKitkat.3.1.this.cancel(false);
              }
            });
          }
        }
        .execute(new Uri[0]);
      }

      public void onWrite(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramWriteResultCallback)
      {
        PrintHelperKitkat.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, paramParcelFileDescriptor, paramCancellationSignal, paramWriteResultCallback);
      }
    };
    PrintManager localPrintManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder localBuilder = new PrintAttributes.Builder();
    localBuilder.setColorMode(this.mColorMode);
    if ((this.mOrientation == 1) || (this.mOrientation == 0))
      localBuilder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    while (true)
    {
      localPrintManager.print(paramString, local3, localBuilder.build());
      return;
      if (this.mOrientation != 2)
        continue;
      localBuilder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    }
  }

  public void setColorMode(int paramInt)
  {
    this.mColorMode = paramInt;
  }

  public void setOrientation(int paramInt)
  {
    this.mOrientation = paramInt;
  }

  public void setScaleMode(int paramInt)
  {
    this.mScaleMode = paramInt;
  }

  public static abstract interface OnPrintFinishCallback
  {
    public abstract void onFinish();
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.print.PrintHelperKitkat
 * JD-Core Version:    0.6.0
 */