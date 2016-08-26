package com.tysci.ballq.app;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.pgyersdk.crash.PgyCrashManager;
import com.tysci.ballq.bigdata.BigDataUtil;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.services.TimeTaskPickerService;
import com.tysci.ballq.utils.SharedPreferencesUtil;
import com.tysci.ballq.utils.WeChatUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by HTT on 2016/5/28.
 */
public class BallQApp extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        HttpUrls.initialized(this);
        PgyCrashManager.register(this);

        // 更新版本后重新打开Guide
        try
        {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            if (info.versionCode > SharedPreferencesUtil.getVersionCode(this))
            {
                SharedPreferencesUtil.resetGuideState(this);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        // 保存版本号
        SharedPreferencesUtil.notifySaveVersion(this);

        AppConfigInfo.initAppConfigInfo(this);
//        AppExceptionHandler.getInstance().init(this);
        CustomActivityOnCrash.install(this);
        HttpClientUtil.initHttpClientUtil(this, AppConfigInfo.APP_HTTP_CACHE_PATH);
        WeChatUtil.registerWXApi(this);

        JPushInterface.setDebugMode(false);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        JPushInterface.initCrashHandler(this);

        startService(new Intent(this, TimeTaskPickerService.class));

        BigDataUtil.init(this);
    }
}
