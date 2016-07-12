package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.views.widgets.BallQUserAnalystView;
import com.tysci.ballq.views.widgets.convenientbanner.ConvenientBanner;

import butterknife.Bind;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQIndexPageFragment extends BaseFragment{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.convenientBanner)
    protected ConvenientBanner banner;
    @Bind(R.id.iv_fight_ballq_gou)
    protected ImageView menuFightGou;
    @Bind(R.id.iv_get_ballq_gold)
    protected ImageView menuGetGold;
    @Bind(R.id.first_analyst)
    protected BallQUserAnalystView firstAnalyst;
    @Bind(R.id.second_analyst)
    protected BallQUserAnalystView secondAnalyst;
    @Bind(R.id.third_analyst)
    protected BallQUserAnalystView thirdAnalyst;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_index;
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
