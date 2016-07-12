package com.tysci.ballq.base;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.tysci.ballq.app.AppConfigInfo;

/**
 * Created by Administrator on 2016/6/2.
 */
public class BallQGlideModule implements GlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        int diskCacheSize=500*1204*1024;
        AppConfigInfo.initAppConfigInfo(context);
        ExternalCacheDiskCacheFactory externalCacheDiskCacheFactory=new ExternalCacheDiskCacheFactory(context,AppConfigInfo.APP_IMAGE_PATH,diskCacheSize);
        builder.setDiskCache(externalCacheDiskCacheFactory);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
