package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;

/**
 * Created by Administrator on 2016/7/19.
 */
public class BallQUserRewardRankingDetailActivity extends BaseActivity{
    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_reward_events;
    }

    @Override
    protected void initViews() {
        setTitle("10万元悬赏排行榜");

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
