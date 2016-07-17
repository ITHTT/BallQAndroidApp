package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;

/**
 * Created by HTT on 2016/7/15.
 */
public class BallQGoRankListFragment extends BaseFragment{
    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_go_rank_list;
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
