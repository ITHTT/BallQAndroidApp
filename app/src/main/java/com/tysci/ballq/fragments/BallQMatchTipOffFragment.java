package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQMatchTipOffFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
