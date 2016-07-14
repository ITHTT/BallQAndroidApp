package com.tysci.ballq.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/3.
 */
public class PhotoUtil {
    /**
     * caculate the bitmap sampleSize
     * @return
     */
    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height/ (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩指定路径的图片，并得到图片对象
     * @param path bitmap source path
     * @return Bitmap {@link Bitmap}
     */
    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题
     * <br> 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
     * @param url
     * @param maxBytes 压缩后的图像最大大小 单位为byte
     * @return
     */
    public final static byte[] compressBitmap(String url, long maxBytes) {
        Bitmap bitmap=BitmapFactory.decodeFile(url);
        if(bitmap!=null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                int options = 90;
                while (baos.toByteArray().length > maxBytes) {
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
                    options -= 10;
                }
                byte[] bts = baos.toByteArray();
                baos.flush();
                baos.close();
                return bts;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
        }
        return null;
    }

    public static long getBitmapsize(Bitmap bitmap){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }
}
