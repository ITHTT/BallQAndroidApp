package com.tysci.ballq.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tysci.ballq.base.BaseService;
import com.tysci.ballq.fragments.BallQTipOffFragment;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.FileUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SharedPreferencesUtil;

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

    private static long minute2Flag;
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
        final long timeMinute2 = minute2Flag;
        final long timeMinute3 = minute3Flag;

        final long timeNow = System.currentTimeMillis();

        if (timeNow - timeMinute2 > 2 * 60 * 1000L) {
            KLog.a("球商后台两分钟执行...");
            minute2Flag = timeNow;

            // 爆料、球茎 小红点提示
            tipOrArticleDotTask();
        }

        // 三分钟
        if (timeNow - timeMinute3 > 3 * 60 * 1000L) {
            KLog.a("球商后台三分钟执行...");

            minute3Flag = timeNow;

            // 启动页面的广告图
            splashTask();

            // 启动用户任务轮循服务
            Intent intent = new Intent(this, UserTaskService.class);
            startService(intent);
        }
    }

    /**
     * 爆料、球茎 小红点提示
     */
    private void tipOrArticleDotTask() {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUrls.HOST_URL);
        sb.append("/api/ares/update_tags/");

        HttpClientUtil.getHttpClientUtil().sendGetRequest(TAG, sb.toString(), 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(response);
                if (!JsonParams.isJsonRight(object)) {
                    return;
                }
                com.alibaba.fastjson.JSONObject data = object.getJSONObject("data");
                if (data == null || data.isEmpty()) {
                    return;
                }
                final long defValue = -999L;
                final long tipTag = SharedPreferencesUtil.getValue(TimeTaskPickerService.this, SharedPreferencesUtil.KEY_TIP_MSG_DOT, defValue);
                final long articleTag = SharedPreferencesUtil.getValue(TimeTaskPickerService.this, SharedPreferencesUtil.KEY_ARTICLE_MSG_DOT, defValue);
                if (tipTag != defValue && tipTag != data.getLong("tip")) {
                    // 通知爆料小红点
                    sendMessageToShowDot("tip");
                }
                if (articleTag != defValue && articleTag != data.getLong("article")) {
                    // 通知球茎小红点
                    sendMessageToShowDot("article");
                }
            }

            private void sendMessageToShowDot(String type) {
                //noinspection StringBufferReplaceableByString
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"status\":\"");
                sb.append(type);
                sb.append("\"}");
                EventObject eventObject = new EventObject();
                eventObject.getData().putString("dot", sb.toString());
                eventObject.addReceiver(BallQTipOffFragment.class);
                EventObject.postEventObject(eventObject, "dot_task");
            }

            @Override
            public void onFinish(Call call) {
            }
        });
    }

    /**
     * 启动广告图的获取
     */
    private void splashTask() {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUrls.HOST_URL_V1);
        sb.append("info/splash/");

        HttpClientUtil.getHttpClientUtil().sendGetRequest(TAG, sb.toString(), 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                error.printStackTrace();
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
