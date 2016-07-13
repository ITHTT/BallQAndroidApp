package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.UserMessageFragment;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserMessageRecordActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String titles[]={"全部","评论","关注","打赏"};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_message;
    }

    @Override
    protected void initViews() {
        setTitle("我的消息");
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
        List<BaseFragment>fragments=new ArrayList<>(4);

        UserMessageFragment fragment=new UserMessageFragment();
        fragment.setType(0);
        fragments.add(fragment);

        fragment=new UserMessageFragment();
        fragment.setType(39);
        fragments.add(fragment);

        fragment=new UserMessageFragment();
        fragment.setType(54);
        fragments.add(fragment);

        fragment=new UserMessageFragment();
        fragment.setType(43);
        fragments.add(fragment);

        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getSupportFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setTabPxWidth(CommonUtils.getScreenDisplayMetrics(this).widthPixels/4);
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
