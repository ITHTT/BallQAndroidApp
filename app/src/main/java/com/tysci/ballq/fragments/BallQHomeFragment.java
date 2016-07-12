package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;
import com.tysci.ballq.views.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by HTT on 2016/5/28.
 */
public class BallQHomeFragment extends BaseFragment {
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String[] titles={"爆料","球经","发现"};

    @Override
    protected int getViewLayoutId() {
        return R.layout.layout_tablayout_viewpager;
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
        setTitle();
        tabLayout.setTabPxWidth(CommonUtils.getScreenDisplayMetrics(this.getContext()).widthPixels/3);
        List<BaseFragment> fragments=new ArrayList<>(3);
        fragments.add(new BallQHomeTipOffListFragment());
        fragments.add(new BallQHomeBallWarpListFragment());
        fragments.add(new BallQHomeCircleListFragment());
        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getChildFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void setTitle(){
        TitleBar titleBar=baseActivity.getTitleBar();
        if(titleBar!=null) {
            titleBar.resetTitle();
            titleBar.setTitleBarTitle("资讯");
        }

    }
}
