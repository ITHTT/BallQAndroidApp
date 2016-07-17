package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffSearchActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.SlidingTabLayout;
import com.tysci.ballq.views.widgets.TitleBar;
import com.tysci.ballq.views.widgets.convenientbanner.ConvenientBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQTipOffFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.title_bar)
    protected TitleBar titleBar;
    @Bind(R.id.convenientBanner)
    protected ConvenientBanner banner;
    @Bind(R.id.tab_layout)
    protected SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    @Bind(R.id.scrollable_layout)
    protected ScrollableLayout mScrollableLayout;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_tip_off;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        titleBar.setTitleBarTitle("爆料");
        titleBar.setTitleBarLeftIcon(0, null);
        titleBar.setRightMenuIcon(R.mipmap.icon_search_mark,this);
        mScrollableLayout.setDraggableView(tabLayout);
        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {
                final float tabsTranslationY;
                if (y < maxY) {
                    tabsTranslationY = .0F;
                } else {
                    tabsTranslationY = y - maxY;
                }

                tabLayout.setTranslationY(tabsTranslationY);

                banner.setTranslationY(y / 2);
            }
        });
        // Note this bit, it's very important
        mScrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
                BaseFragment fragment= (BaseFragment) getChildFragmentManager().getFragments().get(tabLayout.getCurrentTab());
                KLog.e("执行滑动操作。。");
                if(fragment instanceof CanScrollVerticallyDelegate){
                    KLog.e("执行滑动操作。。");
                    return  ((CanScrollVerticallyDelegate) fragment).canScrollVertically(direction);
                }
                return false;
            }
        });
        mScrollableLayout.setOnFlingOverListener(new OnFlingOverListener() {
            @Override
            public void onFlingOver(int y, long duration) {
                BaseFragment fragment= (BaseFragment) getChildFragmentManager().getFragments().get(tabLayout.getCurrentTab());
                KLog.e("执行滑动操作。。");
                if(fragment instanceof OnFlingOverListener){
                    KLog.e("执行滑动操作。。");
                    ((OnFlingOverListener) fragment).onFlingOver(y,duration);
                }
            }
        });
        addFragments();


    }

    private void addFragments(){
        String[] titles={"爆料","球经","视频","我的关注"};
        List<BaseFragment> fragments=new ArrayList<>(4);
        BaseFragment fragment=new BallQTipOffListFragment();
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

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iv_titlebar_next_menu01:
                searchTipOff();
                break;
        }
    }

    private void searchTipOff(){
        Intent intent=new Intent(baseActivity, BallQTipOffSearchActivity.class);
        startActivity(intent);
    }
}
