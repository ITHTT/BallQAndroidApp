package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;

/**
 * Created by HTT on 2016/5/28.
 */
public class BallQCircleFragment extends BaseFragment {
    TabLayout tabLayout;
    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_circle;
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

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {


    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}
