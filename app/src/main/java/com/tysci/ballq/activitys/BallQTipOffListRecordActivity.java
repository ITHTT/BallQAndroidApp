package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.utils.UserInfoUtil;

/**
 * Created by LinDe on 2016-07-19 0019.
 * 爆料记录
 */
public class BallQTipOffListRecordActivity extends BaseActivity {
    private String userId;

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        userId = intent.getStringExtra("uid");
        if (TextUtils.isEmpty(userId) && UserInfoUtil.checkLogin(this)) {
            userId = UserInfoUtil.getUserId(this);
        }
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
