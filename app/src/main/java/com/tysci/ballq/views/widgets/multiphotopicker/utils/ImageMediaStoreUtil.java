package com.tysci.ballq.views.widgets.multiphotopicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.tysci.ballq.views.widgets.multiphotopicker.entity.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;

/**
 * Created by HTT on 2016/4/12.
 */
public class ImageMediaStoreUtil {
    public final static int INDEX_ALL_PHOTOS = 0;

    public static void getPhotoDirs(AppCompatActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
        }

        @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context);
        }

        @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null)  return;
            List<PhotoDirectory> directories = new ArrayList<>();
            PhotoDirectory photoDirectoryAll = new PhotoDirectory();
            photoDirectoryAll.setDirectoryName("全部");
            photoDirectoryAll.setId("ALL");

            while (data.moveToNext()) {

                int imageId  = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));

                PhotoDirectory photoDirectory = new PhotoDirectory();
                photoDirectory.setId(bucketId);
                photoDirectory.setDirectoryName(name);

                if (!directories.contains(photoDirectory)) {
                    photoDirectory.setDirectoryPath(path);
                    photoDirectory.addPhoto(imageId, path);
                    photoDirectory.setCreatedDate(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                }

                photoDirectoryAll.addPhoto(imageId, path);
            }
            if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                photoDirectoryAll.setDirectoryPath(photoDirectoryAll.getPhotoPaths().get(0));
            }
            directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }
}
