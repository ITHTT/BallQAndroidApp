package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.BallQGuideFragment;
import com.tysci.ballq.utils.SharedPreferencesUtil;
import com.tysci.ballq.views.CustomPointView;
import com.tysci.ballq.views.CustomSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LinDe on 2016-07-13 0013.
 * guide for BallQ
 */
public class BallQGuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener
{
    @Bind(R.id.view_pager)
    CustomSlidingViewPager viewPager;
    @Bind(R.id.point_view)
    CustomPointView pointView;
    @Bind(R.id.tv_commit)
    TextView tv_commit;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews()
    {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        tv_commit.setVisibility(View.GONE);

        final List<BaseFragment> mFragmentList = new ArrayList<>();
        BallQGuideFragment fragment;
        Bundle b;

        fragment = new BallQGuideFragment();
        b = new Bundle();
        b.putInt(BallQGuideFragment.class.getName(), 0);
        fragment.setArguments(b);
        mFragmentList.add(fragment);

        fragment = new BallQGuideFragment();
        b = new Bundle();
        b.putInt(BallQGuideFragment.class.getName(), 1);
        fragment.setArguments(b);
        mFragmentList.add(fragment);

        fragment = new BallQGuideFragment();
        b = new Bundle();
        b.putInt(BallQGuideFragment.class.getName(), 2);
        fragment.setArguments(b);
        mFragmentList.add(fragment);

        fragment = new BallQGuideFragment();
        b = new Bundle();
        b.putInt(BallQGuideFragment.class.getName(), 3);
        fragment.setArguments(b);
        mFragmentList.add(fragment);

        viewPager.setAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.addOnPageChangeListener(this);
        pointView.setPointNumber(mFragmentList.size());
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent)
    {

    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        tv_commit.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        pointView.setVisibility(position == 3 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPageSelected(int position)
    {
        pointView.setCheckPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    @OnClick(R.id.tv_commit)
    public void onCommitClick(View view)
    {
        Intent intent = new Intent(this, BallQMainActivity.class);
        startActivity(intent);
        SharedPreferencesUtil.notifyGuideSuccess(this);
        overridePendingTransition(0, 0);
        finish();
    }
}
