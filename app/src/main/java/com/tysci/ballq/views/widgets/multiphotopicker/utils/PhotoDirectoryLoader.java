package com.tysci.ballq.views.widgets.multiphotopicker.utils;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by HTT on 2016/4/12.
 */
public class PhotoDirectoryLoader extends CursorLoader {
    final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    public PhotoDirectoryLoader(Context context) {
        super(context);
        setProjection(IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=? " + ("or " + MIME_TYPE + "=?"));
        String[] selectionArgs;
        selectionArgs = new String[] { "image/jpeg", "image/png", "image/gif" };
        setSelectionArgs(selectionArgs);
    }
}
