package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.tysci.ballq.R;
import com.tysci.ballq.app.CustomActivityOnCrash;
import com.tysci.ballq.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by LinDe on 2016-08-25 0025.
 * 当程序崩溃时，调用这个Activity
 */
public class BqErrorActivity extends BaseActivity
{
    @Bind(R.id.layout_error_log)
    protected LinearLayout mErrorLogGroup;
    @Bind(R.id.layout_error_info)
    protected LinearLayout mErrorInfoGroup;

    @Bind(R.id.tv_error_log)
    protected TextView mErrorLog;

    private int clickErrorLogTitleTimes;
    private long firstClickErrorLogTitleTimeMillis;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_error;
    }

    @Override
    protected void initViews()
    {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        showErrorLog(false);
        clickErrorLogTitleTimes = 0;
    }

    private void showErrorLog(boolean isShow)
    {
        if (!isShow)
        {
            mErrorLogGroup.setVisibility(View.GONE);
            mErrorInfoGroup.setVisibility(View.VISIBLE);
        }
        else
        {
            mErrorLogGroup.setVisibility(View.VISIBLE);
            mErrorInfoGroup.setVisibility(View.GONE);
        }
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        String errorMsg = intent.getStringExtra(CustomActivityOnCrash.EXTRA_STACK_TRACE);
        if (errorMsg != null)
        {
            mErrorLog.setText(errorMsg);
        }
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return true;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return false;
    }

    @OnClick(R.id.tv_error_info_title)
    protected void onErrorInfoTitleClick(View view)
    {
        if (clickErrorLogTitleTimes == 0)
            firstClickErrorLogTitleTimeMillis = System.currentTimeMillis();
        clickErrorLogTitleTimes++;

    }

    @OnLongClick(R.id.tv_error_info_title)
    protected boolean onErrorInfoTitleLongClick(View view)
    {
        if (clickErrorLogTitleTimes == 5 && System.currentTimeMillis() - firstClickErrorLogTitleTimeMillis < 5 * 1000L)
        {
            showErrorLog(true);
        }

        clickErrorLogTitleTimes = 0;
        return true;
    }

    /**
     * 重启APP
     */
    @OnClick(R.id.tv_restart)
    protected void onRestartClick(View view)
    {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
}
