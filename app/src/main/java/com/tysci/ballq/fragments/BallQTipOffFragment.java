package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQTipOffFragment extends BaseFragment{
    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_tip_off;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        baseActivity.getTitleBar().setTitleBarTitle("爆料");

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
