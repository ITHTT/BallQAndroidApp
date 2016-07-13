package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.UserCollectionFragment;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserCollectionRecordActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String titles[]={"爆料","球经","帖子"};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_collection;
    }

    @Override
    protected void initViews() {
        setTitle("我的收藏");
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
        List<BaseFragment> fragments=new ArrayList<>(3);
        for(int i=0;i<3;i++){
            UserCollectionFragment fragment=new UserCollectionFragment();
            fragment.setType(i);
            fragments.add(fragment);
        }
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
