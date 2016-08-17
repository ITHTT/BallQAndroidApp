package com.tysci.ballq.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LinDe on 2016-07-13 0013.
 * util for file
 */
public final class FileUtil
{
    private FileUtil()
    {
    }

    public static String readStringFromFile(File file)
    {
        String result = "";
        if (!file.exists())
        {
            return result;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream outputStream = null;
        try
        {
            fis = new FileInputStream(file);
            //new一个缓冲区
            byte[] buffer = new byte[256];
            //使用ByteArrayOutputStream类来处理输出流
            outputStream = new ByteArrayOutputStream();
            int length;
            //写入数据
            while ((length = fis.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, length);
            }
            result = new String(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static Bitmap readBitmapFromFile(File file)
    {
        Bitmap bitmap = null;
        try
        {
            if (file.exists())
            {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean writeStringToFile(File file, String write, boolean append)
    {
        boolean result = false;
        if (!file.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            try
            {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file, append);
            byte[] b = write.getBytes();
            fos.write(b);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean writeBitmapToFile(File file, Bitmap bitmap)
    {
        if (file.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        if (!file.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            try
            {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        boolean result = false;
        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(file);
            String path = file.getPath();
            if (path.endsWith(".png") || path.endsWith(".PNG"))
            {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                result = true;
            }
            else if (path.endsWith(".jpg") || path.endsWith(".JPG") || path.endsWith(".jpeg") || path.endsWith(".JPEG"))
            {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                result = true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
