package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.tysci.ballq.R;
import com.tysci.ballq.app.CustomActivityOnCrash;
import com.tysci.ballq.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LinDe on 2016-08-25 0025.
 * 当程序崩溃时，调用这个Activity
 */
public class BqErrorActivity extends BaseActivity
{
    @Bind(R.id.text_view)
    protected TextView mTextView;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_error;
    }

    @Override
    protected void initViews()
    {
//        titleBar.getLeftBackGroup().setVisibility(View.GONE);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        mTextView.setVisibility(View.GONE);
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
            mTextView.setText(errorMsg);
        }
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
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

    @OnClick(R.id.tv_restart)
    protected void onRestartClick(View view)
    {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
}
