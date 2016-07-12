package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.UserAttentionFragment;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserAttentionActivity extends BaseActivity{
    @Bind(R.id.tab_layout)
    protected PagerSlidingTabStrip tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String titles[]={"关注","粉丝"};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_attention;
    }

    @Override
    protected void initViews() {
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
        List<BaseFragment> fragments=new ArrayList<>(titles.length);
        for(int i=0;i<titles.length;i++){
            View view= LayoutInflater.from(this).inflate(R.layout.layout_attention_tab_title, null);
            TextView title= (TextView) view.findViewById(R.id.tv_tab_title);
            title.setText(titles[i]);
            tabLayout.addTab(view);
            fragments.add(new UserAttentionFragment());
        }

        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(this.getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
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
