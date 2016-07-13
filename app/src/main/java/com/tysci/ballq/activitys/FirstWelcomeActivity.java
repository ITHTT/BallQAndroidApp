package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.services.TimeTaskPickerService;
import com.tysci.ballq.utils.CalendarUtil;
import com.tysci.ballq.utils.FileUtil;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;

/**
 * Created by LinDe on 2016-07-13 0013.
 * Welcome to BallQ
 */
public class FirstWelcomeActivity extends BaseActivity {
    @Bind(R.id.image_view)
    ImageView iv;
    @Bind(R.id.tv_cancel)
    TextView tv_cancel;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_first_welcome;
    }

    @Override
    protected void initViews() {
        tv_cancel.setVisibility(View.GONE);

//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.first_welcome_anim);
//        animation.setFillAfter(true);// 动画执行完成后保留在此动画的最后一帧
//        iv.startAnimation(animation);// 执行动画

        if (SharedPreferencesUtil.isNeedToGuide(this)) {
            ImageUtil.loadImage(iv, R.mipmap.welcome_pic);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FirstWelcomeActivity.this, BallQGuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
            return;
        }
        checkSplash();
    }

    private void checkSplash() {
        long show_time = 3000L;
        String start_time = null;
        String end_time = null;
        Bitmap bitmap = null;
        try {
            JSONObject object = new JSONObject(FileUtil.readStringFromFile(new File(TimeTaskPickerService.PATH_SPLASH_STRING)));
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
                    show_time = aData.optLong("show_time", 3000L);
                    start_time = aData.optString("start_time");
                    end_time = aData.optString("end_time");
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
                        StringBuilder sb = new StringBuilder();
                        sb.append(".BallQSplash");
                        if (image_url.endsWith(".png") || image_url.endsWith(".PNG")) {
                            sb.append(".png");
                        } else if (image_url.endsWith(".jpg") || image_url.endsWith(".JPG") || image_url.endsWith(".jpeg") || image_url.endsWith(".JPEG")) {
                            sb.append(".jpg");
                        } else {
                            sb.append(".gif");
                        }
//                        bitmap = FileUtils.getBitmap(Environment.getExternalStorageDirectory().getPath() + "/.BallQ", sb.toString());
                        bitmap = FileUtil.readBitmapFromFile(new File(TimeTaskPickerService.PATH_SPLASH_BITMAP, sb.toString()));
                        if (bitmap != null) {
                            iv.setImageBitmap(bitmap);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CalendarUtil start = CalendarUtil.parseStringTZ(start_time);
            CalendarUtil end = CalendarUtil.parseStringTZ(end_time);
            boolean isShow = false;
            if (bitmap != null && start != null && end != null) {
                if (start.timeMillis < System.currentTimeMillis() && System.currentTimeMillis() < end.timeMillis) {
                    if (show_time > 0) {
                        isShow = true;
                        if (show_time > 10000L) {
                            show_time = 10000L;
                        }
                        tv_cancel.setVisibility(View.VISIBLE);
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FirstWelcomeActivity.this, BallQMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }
            if (!isShow) {
                tv_cancel.setVisibility(View.GONE);
                show_time = 3000L;
                ImageUtil.loadImage(iv, R.mipmap.welcome_pic);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstWelcomeActivity.this, BallQMainActivity.class);
                startActivity(intent);
                finish();
            }
        }, show_time);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
