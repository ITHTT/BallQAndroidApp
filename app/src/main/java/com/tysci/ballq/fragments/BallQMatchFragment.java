package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQMatchLeagueSelectActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.adapters.BallQMatchFilterDateAdapter;
import com.tysci.ballq.views.widgets.PopupMenuLayout;
import com.tysci.ballq.views.widgets.SlidingTabLayout;
import com.tysci.ballq.views.widgets.TitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by HTT on 2016/5/28.
 */
public class BallQMatchFragment extends BaseFragment implements BallQMatchFilterDateAdapter.OnSelectDateListener,
        ViewPager.OnPageChangeListener, View.OnClickListener, PopupMenuLayout.OnPopupMenuShowListener
{
    @Bind(R.id.popmenu)
    protected PopupMenuLayout popupMenuLayout;
    @Bind(R.id.title_bar)
    protected TitleBar mTitleBar;
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.rv_dates)
    protected RecyclerView rvDates;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    @Bind(R.id.layout_tip_show)
    protected FrameLayout layoutTipShow;

    private String[] titles = {"足球", "篮球", "关注"};

    private List<String> matchFilterDates;
    private BallQMatchFilterDateAdapter dateAdapter = null;
    private String currentSelectedDate = null;

    private BallQMatchListFragment footerBallMatchListFragment;
    private BallQMatchListFragment basketBallMatchListFragment;
    private UserAttentionMatchListFragment userAttentionMatchListFragment;

    private int currentPosition = 0;
    private String filter = "";

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.fragment_ballq_match;
    }

    @Override
    protected boolean isCancledEventBus()
    {
        return false;
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
    protected void initViews(View view, Bundle savedInstanceState)
    {
        mTitleBar.setTitleBarTitle("竞技场");
        mTitleBar.setOnClickListener(this);
        layoutTipShow.setOnClickListener(this);
        setLeftMenuAttrs();
        setRightMenuAttrs();
        addPopMenuItems();
        popupMenuLayout.setTargetView(mTitleBar.getLeftBackGroup());
        popupMenuLayout.setOnPopupMenuShowListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rvDates.setLayoutManager(linearLayoutManager);
        viewPager.addOnPageChangeListener(this);
        getMatchFilterDateInfo();
        addContentFragments();

    }

    private void setRightMenuAttrs()
    {
//        ImageView ivRightMenu = mTitleBar.getRightMenuImageView();
//        ivRightMenu.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        ivRightMenu.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        ivRightMenu.setImageResource(R.mipmap.icon_match_filter);
//        ivRightMenu.setOnClickListener(this);

        TextView tvRightMenu = mTitleBar.getRightMenuTextView();
        int hor = CommonUtils.dip2px(baseActivity, 10);
        int ver = CommonUtils.dip2px(baseActivity, 3);
        tvRightMenu.setPadding(hor, ver, hor, ver);
        //noinspection deprecation
        tvRightMenu.setTextColor(getResources().getColor(R.color.gold));
        tvRightMenu.setText("筛选");
        tvRightMenu.setTextSize(8F);
        tvRightMenu.setBackgroundResource(R.drawable.btn_tra_gold);
        tvRightMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                matchLeagueFilter();
            }
        });
    }

    private void setLeftMenuAttrs()
    {
        ImageView ivLeftMenu = mTitleBar.getLeftBack();
        ivLeftMenu.setImageResource(R.mipmap.icon_match_filter_left);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLeftMenu.getLayoutParams();
//        layoutParams.leftMargin = CommonUtils.dip2px(baseActivity, 12);
//        ivLeftMenu.setLayoutParams(layoutParams);
    }

    private void addPopMenuItems()
    {
        int[] res = {R.drawable.match_filter_all_selector, R.drawable.match_filter_finished_selector, R.drawable.match_filter_doing_selector,
                R.drawable.match_filter_no_start_selector, R.drawable.match_filter_tip_selecctor};
        for (int i = res.length - 1; i >= 0; i--)
        {
            ImageView imageView = new ImageView(baseActivity);
            imageView.setId(res[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(res[i]);
            if (i == 0)
            {
                imageView.setSelected(true);
            }
            imageView.setOnClickListener(this);
            popupMenuLayout.addMenu(imageView);
        }
    }

    @Override
    protected View getLoadingTargetView()
    {
        return null;
    }

    private void addContentFragments()
    {
        tabLayout.setTabPxWidth(CommonUtils.getScreenDisplayMetrics(this.getContext()).widthPixels / 3);
        List<BaseFragment> fragments = new ArrayList<>(3);
        footerBallMatchListFragment = new BallQMatchListFragment();
        footerBallMatchListFragment.setType(0);
        footerBallMatchListFragment.setSelectDate(currentSelectedDate);
        fragments.add(footerBallMatchListFragment);

        basketBallMatchListFragment = new BallQMatchListFragment();
        basketBallMatchListFragment.setType(1);
        basketBallMatchListFragment.setSelectDate(currentSelectedDate);
        fragments.add(basketBallMatchListFragment);

        userAttentionMatchListFragment = new UserAttentionMatchListFragment();
        fragments.add(userAttentionMatchListFragment);

        BallQFragmentPagerAdapter adapter = new BallQFragmentPagerAdapter(getChildFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setViewPager(viewPager);
    }

    private void getMatchFilterDateInfo()
    {
        Date currentDate = new Date();
        currentSelectedDate = CommonUtils.getYYMMdd(currentDate);
        matchFilterDates = new ArrayList<>(15);
        long currentTime = currentDate.getTime();
        for (int i = 7; i >= 1; i--)
        {
            Date date = CommonUtils.getDifferenceDaysDate(currentTime, -i);
            KLog.e(CommonUtils.getYYMMdd(date));
            matchFilterDates.add(CommonUtils.getYYMMdd(date));
        }
        //KLog.e(currentSelectedDate);
        matchFilterDates.add("今日");
        for (int i = 1; i <= 7; i++)
        {
            Date date = CommonUtils.getDifferenceDaysDate(currentTime, i);
            KLog.e(CommonUtils.getYYMMdd(date));
            matchFilterDates.add(CommonUtils.getYYMMdd(date));
        }

        dateAdapter = new BallQMatchFilterDateAdapter(matchFilterDates);
        dateAdapter.setCurrentSelectedDate("今日");
        dateAdapter.setOnSelectDateListener(this);
        rvDates.setAdapter(dateAdapter);
        int position = matchFilterDates.indexOf("今日");
        if (position >= 1)
        {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvDates.getLayoutManager();
            linearLayoutManager.scrollToPositionWithOffset(position - 1, 0);
        }
    }

    @Override
    public void onSelectDateItem(int position, String date)
    {
        if (position >= 1)
        {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvDates.getLayoutManager();
            linearLayoutManager.scrollToPositionWithOffset(position - 1, 0);
            rvDates.smoothScrollToPosition(position - 1);
        }
        if (date.equals("今日"))
        {
            date = CommonUtils.getYYMMdd(new Date());
        }
        if (!currentSelectedDate.equals(date))
        {
            currentSelectedDate = date;
            BallQMatchListFragment fragment = getCurrentMatchListFragment();
            if (fragment != null)
            {
                fragment.filterUpdateData(filter, currentSelectedDate);
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        currentPosition = position;
        if (position == 0)
        {
            setMatchLeagueMenuVisibility(true);
            rvDates.setVisibility(View.VISIBLE);
            footerBallMatchListFragment.filterUpdateData(filter, currentSelectedDate);
        }
        else if (position == 1)
        {
            setMatchLeagueMenuVisibility(true);
            rvDates.setVisibility(View.VISIBLE);
            basketBallMatchListFragment.filterUpdateData(filter, currentSelectedDate);
        }
        else
        {
            setMatchLeagueMenuVisibility(false);
            rvDates.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    private BallQMatchListFragment getCurrentMatchListFragment()
    {
        if (currentPosition == 0)
        {
            return footerBallMatchListFragment;
        }
        else if (currentPosition == 1)
        {
            return basketBallMatchListFragment;
        }
        return null;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
//            case R.id.iv_titlebar_next_menu01:
//                if (popupMenuLayout.isShowing())
//                {
//                    popupMenuLayout.toggle();
//                }
//                else
//                {
//                    matchLeagueFilter();
//                }
//                break;
            case R.id.title_bar:
            case R.id.layout_tip_show:
                if (popupMenuLayout.isShowing())
                {
                    popupMenuLayout.toggle();
                }
        }
        selecteMatch(v);
    }

    public void setCurrentTab(int tab)
    {
        if (tab < 0 || tab >= tabLayout.getCurrentTab())
            return;
        tabLayout.setCurrentTab(tab);
    }

    private void selecteMatch(View view)
    {
        int id = view.getId();
        String filter = "全部";
        switch (id)
        {
            case R.drawable.match_filter_all_selector:
                filter = "";
                popupMenuLayout.toggle();
                break;
            case R.drawable.match_filter_finished_selector:
                filter = "finished";
                popupMenuLayout.toggle();
                break;
            case R.drawable.match_filter_doing_selector:
                filter = "ongoing";
                popupMenuLayout.toggle();
                break;
            case R.drawable.match_filter_no_start_selector:
                filter = "not_started";
                popupMenuLayout.toggle();
                break;
            case R.drawable.match_filter_tip_selecctor:
                filter = "has_tip";
                popupMenuLayout.toggle();
                break;
        }

        if (!filter.equals("全部"))
        {
            if (!filter.equals(this.filter))
            {
                setSelectedMenu(id);
                this.filter = filter;
                BallQMatchListFragment fragment = getCurrentMatchListFragment();
                if (fragment != null)
                {
                    fragment.filterUpdateData(filter, currentSelectedDate);
                }
            }
        }
    }

    private void setSelectedMenu(int id)
    {
        List<View> views = popupMenuLayout.getMenuItems();
        int size = views.size();
        for (int i = 0; i < size; i++)
        {
            ImageView imageView = (ImageView) views.get(i);
            if (imageView.getId() == id)
            {
                imageView.setSelected(true);
            }
            else
            {
                imageView.setSelected(false);
            }
        }
    }

    private void setMatchLeagueMenuVisibility(boolean isVi)
    {
        View right = mTitleBar.getRightMenuImageView();
        View left = mTitleBar.getLeftBack();
        if (isVi)
        {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
        }
        else
        {
            right.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
        }
    }

    private void matchLeagueFilter()
    {
        Intent intent = new Intent(baseActivity, BallQMatchLeagueSelectActivity.class);
        intent.putExtra("date", currentSelectedDate);
        intent.putExtra("etype", currentPosition);
        startActivity(intent);
    }

    @Override
    public void onMenuShow()
    {
        layoutTipShow.setVisibility(View.VISIBLE);
        layoutTipShow.requestFocus();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layoutTipShow, "alpha", 0f, 1f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public void onMenuDimiss()
    {
        layoutTipShow.clearFocus();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layoutTipShow, "alpha", 1f, 0f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
        layoutTipShow.setVisibility(View.GONE);
    }
}
