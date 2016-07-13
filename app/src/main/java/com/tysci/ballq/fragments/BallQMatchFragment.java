package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.adapters.BallQMatchFilterDateAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by HTT on 2016/5/28.
 */
public class BallQMatchFragment extends BaseFragment implements BallQMatchFilterDateAdapter.OnSelectDateListener,
ViewPager.OnPageChangeListener{
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.rv_dates)
    protected RecyclerView rvDates;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;

    private String[] titles={"足球","篮球","关注"};

    private List<String> matchFilterDates;
    private BallQMatchFilterDateAdapter dateAdapter=null;
    private String currentSelectedDate=null;

    private BallQMatchListFragment footerBallMatchListFragment;
    private BallQMatchListFragment basketBallMatchListFragment;
    private UserAttentionMatchListFragment userAttentionMatchListFragment;

    private int currentPosition=0;
    private String filter;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_match;
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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rvDates.setLayoutManager(linearLayoutManager);
        viewPager.addOnPageChangeListener(this);
        getMatchFilterDateInfo();
        addContentFragments();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void addContentFragments(){
        tabLayout.setTabPxWidth(CommonUtils.getScreenDisplayMetrics(this.getContext()).widthPixels/3);
        List<BaseFragment> fragments=new ArrayList<>(3);
        footerBallMatchListFragment=new BallQMatchListFragment();
        footerBallMatchListFragment.setType(0);
        footerBallMatchListFragment.setSelectDate(currentSelectedDate);
        fragments.add(footerBallMatchListFragment);

        basketBallMatchListFragment=new BallQMatchListFragment();
        basketBallMatchListFragment.setType(1);
        basketBallMatchListFragment.setSelectDate(currentSelectedDate);
        fragments.add(basketBallMatchListFragment);

        userAttentionMatchListFragment=new UserAttentionMatchListFragment();
        fragments.add(userAttentionMatchListFragment);

        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getChildFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setViewPager(viewPager);
    }

    private void getMatchFilterDateInfo(){
        Date currentDate=new Date();
        currentSelectedDate= CommonUtils.getYYMMdd(currentDate);
        matchFilterDates=new ArrayList<>(15);
        long currentTime=currentDate.getTime();
        for(int i=7;i>=1;i--){
            Date date= CommonUtils.getDifferenceDaysDate(currentTime,-i);
            KLog.e(CommonUtils.getYYMMdd(date));
            matchFilterDates.add(CommonUtils.getYYMMdd(date));
        }
        //KLog.e(currentSelectedDate);
        matchFilterDates.add("今日");
        for(int i=1;i<=7;i++){
            Date date= CommonUtils.getDifferenceDaysDate(currentTime,i);
            KLog.e(CommonUtils.getYYMMdd(date));
            matchFilterDates.add(CommonUtils.getYYMMdd(date));
        }

        dateAdapter=new BallQMatchFilterDateAdapter(matchFilterDates);
        dateAdapter.setCurrentSelectedDate("今日");
        dateAdapter.setOnSelectDateListener(this);
        rvDates.setAdapter(dateAdapter);
        int position=matchFilterDates.indexOf("今日");
        if(position>=1) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvDates.getLayoutManager();
            linearLayoutManager.scrollToPositionWithOffset(position-1,0);
        }
    }

    @Override
    public void onSelectDateItem(int position, String date) {
        if(position>=1){
            LinearLayoutManager linearLayoutManager= (LinearLayoutManager) rvDates.getLayoutManager();
            linearLayoutManager.scrollToPositionWithOffset(position-1,0);
            rvDates.smoothScrollToPosition(position-1);
        }
        if(date.equals("今日")){
            date=CommonUtils.getYYMMdd(new Date());
        }
        if(!currentSelectedDate.equals(date)){
            currentSelectedDate=date;
            BallQMatchListFragment fragment=getCurrentMatchListFragment();
            if (fragment !=null){
                fragment.filterUpdateData(filter,currentSelectedDate);
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition=position;
        if(position==0){
            rvDates.setVisibility(View.VISIBLE);
            footerBallMatchListFragment.filterUpdateData(filter,currentSelectedDate);
        }else if(position==1){
            rvDates.setVisibility(View.VISIBLE);
            basketBallMatchListFragment.filterUpdateData(filter,currentSelectedDate);
        }else{
            rvDates.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private BallQMatchListFragment getCurrentMatchListFragment(){
        if(currentPosition==0){
            return footerBallMatchListFragment;
        }else if(currentPosition==1){
            return basketBallMatchListFragment;
        }
        return null;
    }
}
