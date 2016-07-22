package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQTipOffSearchActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQBannerImageEntity;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQBusinessControler;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;
import com.tysci.ballq.views.widgets.BannerNetworkImageView;
import com.tysci.ballq.views.widgets.SlidingTabLayout;
import com.tysci.ballq.views.widgets.TitleBar;
import com.tysci.ballq.views.widgets.convenientbanner.ConvenientBanner;
import com.tysci.ballq.views.widgets.convenientbanner.holder.CBViewHolderCreator;
import com.tysci.ballq.views.widgets.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;
import ru.noties.scrollable.OnScrollChangedListener;
import ru.noties.scrollable.ScrollableLayout;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQTipOffFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener
{
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

    private List<BallQBannerImageEntity> bannerList;

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.fragment_ballq_tip_off;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        titleBar.setTitleBarTitle("爆料");
        titleBar.setTitleBarLeftIcon(0, null);
        titleBar.setRightMenuIcon(R.mipmap.icon_search_mark, this);
        mScrollableLayout.setDraggableView(tabLayout);
        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY)
            {
                KLog.e(y + "  " + oldY + " " + maxY);

                final float tabsTranslationY;
                if (y < maxY)
                {
                    tabsTranslationY = .0F;
                }
                else
                {
                    tabsTranslationY = y - maxY;
                }

                tabLayout.setTranslationY(tabsTranslationY);

                banner.setTranslationY(y / 2);
            }
        });
        // Note this bit, it's very important
        mScrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate()
        {
            @Override
            public boolean canScrollVertically(int direction)
            {
                BaseFragment fragment = (BaseFragment) getChildFragmentManager().getFragments().get(tabLayout.getCurrentTab());
//                KLog.e("执行滑动操作。。");
                if (fragment instanceof CanScrollVerticallyDelegate)
                {
                    KLog.e("执行滑动操作。。");
                    return ((CanScrollVerticallyDelegate) fragment).canScrollVertically(direction);
                }
                return false;
            }
        });
        mScrollableLayout.setOnFlingOverListener(new OnFlingOverListener()
        {
            @Override
            public void onFlingOver(int y, long duration)
            {
                BaseFragment fragment = (BaseFragment) getChildFragmentManager().getFragments().get(tabLayout.getCurrentTab());
                //KLog.e("执行滑动操作。。");
                if (fragment instanceof OnFlingOverListener)
                {
                    KLog.e("执行滑动操作。。");
                    ((OnFlingOverListener) fragment).onFlingOver(y, duration);
                }
            }
        });
        addFragments();

        banner.setOnItemClickListener(this);
    }

    private void addFragments()
    {
        String[] titles = {"爆料", "球经", "视频", "我的关注"};
        List<BaseFragment> fragments = new ArrayList<>(4);
        BaseFragment fragment = new BallQTipOffListFragment();
        fragments.add(fragment);
        fragment = new BallQHomeBallWarpListFragment();
        fragments.add(fragment);
        fragment = new BallQTipOffVideoListFragment();
        fragments.add(fragment);
        fragment = new UserAttentionAllFragment();
        fragments.add(fragment);
        BallQFragmentPagerAdapter adapter = new BallQFragmentPagerAdapter(getChildFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return contentView.findViewById(R.id.convenientBanner);
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
        // 小红点通知
        String json = data.getString("dot");
        if (!TextUtils.isEmpty(json))
        {
            String status = JSON.parseObject(json).getString("status");
            if (!TextUtils.isEmpty(status))
            {
                switch (status)
                {
                    case "tip":
                        tabLayout.showDot(0);
                        break;
                    case "tip_reset":
                        tabLayout.hideMsg(0);
                        break;
                    case "article":
                        tabLayout.showDot(1);
                        break;
                    case "article_reset":
                        tabLayout.hideMsg(1);
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.iv_titlebar_next_menu01:
                searchTipOff();
                break;
        }
    }

    public void setCurrentTab(int tab)
    {
        if (tab < 0 || tab >= tabLayout.getCurrentTab())
        {
            return;
        }
        tabLayout.setCurrentTab(tab);
    }

    private void searchTipOff()
    {
        Intent intent = new Intent(baseActivity, BallQTipOffSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getBanner();
        //开始自动翻页
        banner.startTurning(3000);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        banner.stopTurning();
    }

    private void getBanner()
    {
        showLoading();
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, HttpUrls.HOST_URL + "/api/ares/banner/", 5, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request) {}

            @Override
            public void onError(Call call, Exception error)
            {
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        getBanner();
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                JSONObject object = JSON.parseObject(response);
                if (JsonParams.isJsonRight(object))
                {
                    JSONArray data = object.getJSONArray("data");
                    if (data != null && !data.isEmpty())
                    {
                        if (bannerList == null)
                        {
                            bannerList = new ArrayList<>();
                        }
                        else
                        {
                            bannerList.clear();
                        }
                        CommonUtils.getJSONListObject(data, bannerList, BallQBannerImageEntity.class);
                        banner.setPages(new CBViewHolderCreator()
                        {
                            @Override
                            public Object createHolder()
                            {
                                return new BannerNetworkImageView();
                            }
                        }, bannerList)
                                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可不设
                                .setPageIndicator(new int[]{R.drawable.guide_gray, R.drawable.guide_red});
                        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                        banner.setPointViewVisible(true);
                        banner.setManualPageable(true);
                    }
                }
                else
                {
                    bannerList.clear();
                    banner.notifyDataSetChanged();
                }
            }

            @Override
            public void onFinish(Call call)
            {
            }
        });
    }

    @Override
    public void onItemClick(int position)
    {
        if (bannerList != null)
        {
            BallQBannerImageEntity info = bannerList.get(position);
            BallQBusinessControler.businessControler(baseActivity, Integer.parseInt(info.getJump_type()), info.getJump_url());
        }
    }
}
