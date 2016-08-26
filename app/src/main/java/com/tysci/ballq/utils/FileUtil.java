package com.tysci.ballq.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

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

    /**
     * 获取指定文件/文件夹的大小
     *
     * @return 字节
     */
    public static long getFileLength(File file)
    {
        if (file == null)
            return 0;

        long size = 0;
        if (file.isDirectory())
        {
            File[] list = null;
            try
            {
                list = file.listFiles();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (list != null)
            {
                for (File aFile : list)
                {
                    size += (aFile.isDirectory() ? getFileLength(aFile) : aFile.length());
                }
            }
        }
        else
        {
            try
            {
                size = file.length();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 删除文件
     *
     * @param file            指定文件/文件夹
     * @param deleteDirectory 删除文件夹
     * @return 删除成功/删除失败或删除不全
     */
    public static boolean deleteFile(File file, boolean deleteDirectory, boolean deleteChildDirectory)
    {
        if (file == null)
            return false;
        boolean deleteSuccess;

        if (file.isDirectory())
        {
            File[] list = file.listFiles();
            boolean[] deleteChild = new boolean[list.length];

            for (int i = 0, length = list.length; i < length; i++)
            {
                if (list[i].isDirectory())
                {
                    deleteChild[i] = deleteFile(list[i], deleteChildDirectory, deleteChildDirectory);
                }
                else
                {
                    deleteChild[i] = list[i].delete();
                }
            }
            deleteSuccess = true;
            for (boolean b : deleteChild)
            {
                if (!b)
                {
                    deleteSuccess = false;
                    break;
                }
            }
            if (deleteDirectory)
            {
                deleteSuccess = file.delete();
            }
        }
        else
        {
            deleteSuccess = file.delete();
        }

        return deleteSuccess;
    }

    public static String getFileLengthMsg(long fileLength)
    {
        int Hex = 1000;
        if (fileLength < Hex)
        {
            if (fileLength < 0)
                return "0B";
            return fileLength + "B";
        }
        double kb = fileLength * 1D / Hex;
        if (kb < Hex)
        {
            return String.format(Locale.getDefault(), "%.2f", kb) + "KB";
        }
        double mb = kb / Hex;
        if (mb < Hex)
        {
            return String.format(Locale.getDefault(), "%.2f", mb) + "MB";
        }
        double gb = mb / Hex;
        if (gb < Hex)
        {
            return String.format(Locale.getDefault(), "%.2f", gb) + "GB";
        }
        double tb = gb / Hex;
        return String.format(Locale.getDefault(), "%.2f", tb) + "TB";
    }

    /**
     * 打开文件 由于机型差异，打开Intent可能会报错，所以打开Intent时需要尽情异常捕获处理
     */
    @Nullable
    public static Intent openFile(String filePath)
    {

        File file = new File(filePath);
        if (!file.exists()) return null;
        if (file.isDirectory())
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "file/*");
            return intent;
        }
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        switch (end)
        {
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                return getAudioFileIntent(filePath);
            case "3gp":
            case "mp4":
                return getAudioFileIntent(filePath);
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                return getImageFileIntent(filePath);
            case "apk":
                return getApkFileIntent(filePath);
            case "ppt":
                return getPptFileIntent(filePath);
            case "xls":
                return getExcelFileIntent(filePath);
            case "doc":
                return getWordFileIntent(filePath);
            case "pdf":
                return getPdfFileIntent(filePath);
            case "chm":
                return getChmFileIntent(filePath);
            case "txt":
                return getTextFileIntent(filePath, false);
            default:
                return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    private static Intent getAllIntent(String param)
    {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    private static Intent getApkFileIntent(String param)
    {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    private static Intent getVideoFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    private static Intent getAudioFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    private static Intent getHtmlFileIntent(String param)
    {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    private static Intent getImageFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    private static Intent getPptFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    private static Intent getExcelFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    private static Intent getWordFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    private static Intent getChmFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    private static Intent getTextFileIntent(String param, boolean paramBoolean)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean)
        {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        }
        else
        {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    private static Intent getPdfFileIntent(String param)
    {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
