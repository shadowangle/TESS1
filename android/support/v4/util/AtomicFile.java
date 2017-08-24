package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile
{
  private final File mBackupName;
  private final File mBaseName;

  public AtomicFile(File paramFile)
  {
    this.mBaseName = paramFile;
    this.mBackupName = new File(paramFile.getPath() + ".bak");
  }

  static boolean sync(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null);
    try
    {
      paramFileOutputStream.getFD().sync();
      return true;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public void delete()
  {
    this.mBaseName.delete();
    this.mBackupName.delete();
  }

  public void failWrite(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null)
      sync(paramFileOutputStream);
    try
    {
      paramFileOutputStream.close();
      this.mBaseName.delete();
      this.mBackupName.renameTo(this.mBaseName);
      return;
    }
    catch (IOException localIOException)
    {
      Log.w("AtomicFile", "failWrite: Got exception:", localIOException);
    }
  }

  public void finishWrite(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null)
      sync(paramFileOutputStream);
    try
    {
      paramFileOutputStream.close();
      this.mBackupName.delete();
      return;
    }
    catch (IOException localIOException)
    {
      Log.w("AtomicFile", "finishWrite: Got exception:", localIOException);
    }
  }

  public File getBaseFile()
  {
    return this.mBaseName;
  }

  public FileInputStream openRead()
    throws FileNotFoundException
  {
    if (this.mBackupName.exists())
    {
      this.mBaseName.delete();
      this.mBackupName.renameTo(this.mBaseName);
    }
    return new FileInputStream(this.mBaseName);
  }

  public byte[] readFully()
    throws IOException
  {
    int i = 0;
    FileInputStream localFileInputStream = openRead();
    while (true)
    {
      int k;
      try
      {
        Object localObject2 = new byte[localFileInputStream.available()];
        int j = localFileInputStream.read(localObject2, i, localObject2.length - i);
        if (j <= 0)
          return localObject2;
        k = j + i;
        int m = localFileInputStream.available();
        if (m > localObject2.length - k)
        {
          byte[] arrayOfByte = new byte[m + k];
          System.arraycopy(localObject2, 0, arrayOfByte, 0, k);
          localObject2 = arrayOfByte;
          i = k;
          continue;
        }
      }
      finally
      {
        localFileInputStream.close();
      }
      i = k;
    }
  }

  public FileOutputStream startWrite()
    throws IOException
  {
    if (this.mBaseName.exists())
    {
      if (this.mBackupName.exists())
        break label88;
      if (!this.mBaseName.renameTo(this.mBackupName))
        Log.w("AtomicFile", "Couldn't rename file " + this.mBaseName + " to backup file " + this.mBackupName);
    }
    try
    {
      while (true)
      {
        FileOutputStream localFileOutputStream1 = new FileOutputStream(this.mBaseName);
        return localFileOutputStream1;
        label88: this.mBaseName.delete();
      }
    }
    catch (FileNotFoundException localFileNotFoundException1)
    {
      if (!this.mBaseName.getParentFile().mkdirs())
        throw new IOException("Couldn't create directory " + this.mBaseName);
      try
      {
        FileOutputStream localFileOutputStream2 = new FileOutputStream(this.mBaseName);
        return localFileOutputStream2;
      }
      catch (FileNotFoundException localFileNotFoundException2)
      {
      }
    }
    throw new IOException("Couldn't create " + this.mBaseName);
  }
}

/* Location:           C:\apk\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jarNor.jar
 * Qualified Name:     android.support.v4.util.AtomicFile
 * JD-Core Version:    0.6.0
 */