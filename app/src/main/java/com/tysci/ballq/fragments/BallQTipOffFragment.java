package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;
import com.tysci.ballq.views.widgets.convenientbanner.ConvenientBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQTipOffFragment extends BaseFragment{
    @Bind(R.id.convenientBanner)
    protected ConvenientBanner banner;
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_tip_off;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        addFragments();
    }

    private void addFragments(){
        String[] titles={"爆料","球经","视频","我的关注"};
        List<BaseFragment> fragments=new ArrayList<>(4);
        BaseFragment fragment=new BallQHomeTipOffListFragment();
        fragments.add(fragment);
        fragment=new BallQHomeBallWarpListFragment();
        fragments.add(fragment);
        fragment=new BallQFindCircleNoteListFragment();
        fragments.add(fragment);
        fragment=new UserAttentionMatchListFragment();
        fragments.add(fragment);
        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getChildFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setViewPager(viewPager);
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
