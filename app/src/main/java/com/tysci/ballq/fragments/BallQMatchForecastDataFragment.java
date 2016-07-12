package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SegmentTabLayout;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/8.
 */
public class BallQMatchForecastDataFragment extends BaseFragment {
    @Bind(R.id.tab_layout_match_data)
    protected SegmentTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String[] titles={"亚盘","大小球"};

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_match_forecast_data;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(new SlidingTabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Bundle data=getArguments();
        if(data!=null){
            BallQMatchEntity matchEntity=data.getParcelable("match_data");

            if(matchEntity!=null) {
                List<BaseFragment> fragments = new ArrayList<>(2);
                MatchForecastDataFragment baseFragment = new MatchForecastDataFragment();
                baseFragment.setMatchEntity(matchEntity);
                baseFragment.setOddsType(5);
                fragments.add(baseFragment);

                baseFragment=new MatchForecastDataFragment();
                baseFragment.setMatchEntity(matchEntity);
                baseFragment.setOddsType(2);
                fragments.add(baseFragment);
                BallQFragmentPagerAdapter adapter = new BallQFragmentPagerAdapter(this.getChildFragmentManager(),fragments);
                viewPager.setAdapter(adapter);
            }
        }
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
