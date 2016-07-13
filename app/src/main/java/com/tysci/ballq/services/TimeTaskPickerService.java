package com.tysci.ballq.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tysci.ballq.base.BaseService;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.FileUtil;
import com.tysci.ballq.utils.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-13 0013.
 * 定时任务
 */
public class TimeTaskPickerService extends BaseService implements Runnable {

    private static final String TAG = TimeTaskPickerService.class.getName();

    private Timer mTimer;
    private Handler handler;
    private final TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (handler != null) {
                try {
                    handler.post(TimeTaskPickerService.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static long minute3Flag;

    public static final String PATH_SPLASH_BITMAP;
    public static final String PATH_SPLASH_STRING;

    static {
        PATH_SPLASH_BITMAP = Environment.getExternalStorageDirectory().getPath() + "/.BallQ";
        PATH_SPLASH_STRING = Environment.getExternalStorageDirectory().getPath() + "/.BallQ/.splash.txt";
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        handler = new Handler(Looper.getMainLooper());

        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public void run() {
        final long timeMinute3 = minute3Flag;
        final long timeNow = System.currentTimeMillis();

        KLog.a("球商后台正在执行...");

        // 三分钟
        if (timeNow - timeMinute3 > 3 * 60 * 1000L) {
            minute3Flag = timeNow;
            splashTask();

            Intent intent = new Intent(this, UserTaskService.class);
            startService(intent);
        }
    }

    /** 启动广告图的获取 */
    private void splashTask() {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUrls.HOST_URL_V1);
        sb.append("info/splash/");

        HttpClientUtil.getHttpClientUtil().sendGetRequest(TAG, sb.toString(), 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                KLog.e();
            }

            @Override
            public void onError(Call call, Exception error) {
                KLog.e();
            }

            @Override
            public void onSuccess(Call call, final String response) {
                KLog.json(response);

                String last = FileUtil.readStringFromFile(new File(PATH_SPLASH_STRING));
                if (last.equals(response)) {
                    return;
                }

                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (object == null) {
                    return;
                }
                if (object.optInt("status", -999) == 0 && object.optString("message").equalsIgnoreCase("ok")) {
                    JSONArray data = object.optJSONArray("data");
                    JSONObject aData;
                    JSONArray items;
                    JSONObject aItem;
                    String image_url;
                    int id;
                    for (int i = 0, length = data.length(); i < length; i++) {
                        aData = data.optJSONObject(i);
                        if (aData == null) {
                            continue;
                        }
                        items = aData.optJSONArray("items");
                        if (items == null || items.length() == 0) {
                            continue;
                        }
                        for (int j = 0, itemLength = items.length(); j < itemLength; j++) {
                            aItem = items.optJSONObject(i);
                            if (aItem == null) {
                                continue;
                            }
                            image_url = aItem.optString("image_url");
                            id = aItem.optInt("id");
                            if (TextUtils.isEmpty(image_url) || id <= 0) {
                                continue;
                            }
                            KLog.e();
                            Glide.with(TimeTaskPickerService.this)
                                    .load(HttpUrls.getImageUrl(image_url))
                                    .asBitmap()
                                    .listener(new RequestListener<String, Bitmap>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                            KLog.e();
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append(".BallQSplash");
                                            if (model.endsWith(".png") || model.endsWith(".PNG")) {
                                                sb.append(".png");
                                            } else if (model.endsWith(".jpg") || model.endsWith(".JPG") || model.endsWith(".jpeg") || model.endsWith(".JPEG")) {
                                                sb.append(".jpg");
                                            } else {
                                                sb.append(".gif");
                                            }
                                            KLog.d(resource);
                                            KLog.d(model);
                                            if (resource != null) {
                                                final File splashBitmap = new File(PATH_SPLASH_BITMAP, sb.toString());
                                                FileUtil.writeBitmapToFile(splashBitmap, resource);

                                                final File splashString = new File(PATH_SPLASH_STRING);
                                                FileUtil.writeStringToFile(splashString, response, false);
                                            }
                                            return false;
                                        }
                                    })
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
            }
        });
    }
}
