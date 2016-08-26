package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.BallQGoGreatWarHistoryFragment;
import com.tysci.ballq.fragments.BallQGoGreatWarReViewFragment;
import com.tysci.ballq.fragments.BallQGoPKGreatWarFragment;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQGreatWarGoActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_great_war_go;
    }

    @Override
    protected void initViews() {
        setTitle("大战球商GO");
        addFragments();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    private void addFragments(){
        String titles[]={"PK大战","大战综述","历史战绩"};
        List<BaseFragment> fragments=new ArrayList<>(3);
        BaseFragment fragment=new BallQGoPKGreatWarFragment();
        fragments.add(fragment);
        fragment=new BallQGoGreatWarReViewFragment();
        fragments.add(fragment);
        fragment=new BallQGoGreatWarHistoryFragment();
        fragments.add(fragment);
        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getSupportFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setTabPxWidth(CommonUtils.getScreenDisplayMetrics(this).widthPixels/3);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState) {

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
