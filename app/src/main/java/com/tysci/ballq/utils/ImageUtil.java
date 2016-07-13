package com.tysci.ballq.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by LinDe on 2016-07-13 0013.
 * util for load image
 */
public class ImageUtil {

    public static <T> void loadImage(ImageView iv, T uri) {
        Glide.with(iv.getContext())
                .load(uri instanceof Integer ? uri : uri.toString())
                .asBitmap()
                .into(iv);
    }

    public static <T> void loadImage(ImageView iv, int defaultRes, T uri) {
        Glide.with(iv.getContext())
                .load(uri instanceof Integer ? uri : uri.toString())
                .asBitmap()
                .placeholder(defaultRes)
                .into(iv);
    }
}