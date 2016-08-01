package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQBannerImageEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BqIndexAdapter;
import com.tysci.ballq.views.widgets.BqIndexHeaderView;
import com.tysci.ballq.views.widgets.TitleBar;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/12.
 *
 * @author LinDe edit on 2016/8/1
 */
public class BallQIndexPageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
{
    @Bind(R.id.title_bar)
    protected TitleBar titleBar;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    //    @Bind(R.id.convenientBanner)
//    protected ConvenientBanner banner;
    //    @Bind(R.id.iv_fight_ballq_gou)
//    protected ImageView menuFightGou;
//    @Bind(R.id.iv_get_ballq_gold)
//    protected ImageView menuGetGold;
//    @Bind(R.id.event1)
//    IndexEventsView mIndexEventsView1;
//    @Bind(R.id.event2)
//    IndexEventsView mIndexEventsView2;
//    @Bind(R.id.event3)
//    IndexEventsView mIndexEventsView3;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView mRecyclerView;
    @Bind(R.id.bq_index_header_view)
    protected BqIndexHeaderView mBqIndexHeaderView;
    private BqIndexAdapter mBqIndexAdapter;

//    @Bind(R.id.first_analyst)
//    protected BallQUserAnalystView firstAnalyst;
//    @Bind(R.id.second_analyst)
//    protected BallQUserAnalystView secondAnalyst;
//    @Bind(R.id.third_analyst)
//    protected BallQUserAnalystView thirdAnalyst;

    private List<BallQBannerImageEntity> bannerImageEntityList = null;

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.fragment_ballq_index;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        titleBar.setTitleBarTitle("首页");
        titleBar.setTitleBarLeftIcon(0, null);
//        banner.setBackgroundColor(Color.parseColor("#f6f6f6"));
//        banner.setOnItemClickListener(this);
//        banner.getViewPager().setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                switch (event.getAction())
//                {
//                    case MotionEvent.ACTION_MOVE:
//                        if (swipeRefresh != null)
//                            swipeRefresh.setEnabled(false);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        if (swipeRefresh != null)
//                        {
//                            swipeRefresh.setEnabled(true);
//                        }
//                }
//                return false;
//            }
//        });
        swipeRefresh.setOnRefreshListener(this);

        GridLayoutManager lm = new GridLayoutManager(baseActivity, 4);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

//        mBqIndexHeaderView = new BqIndexHeaderView(baseActivity);
        mBqIndexHeaderView.setSwipeRefreshLayout(swipeRefresh);
//        mRecyclerView.addHeaderView(mBqIndexHeaderView);

        mBqIndexAdapter = new BqIndexAdapter(4);
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(mBqIndexAdapter);
        mRecyclerView.setAdapter(mBqIndexAdapter);
        mRecyclerView.addItemDecoration(decoration);

        showLoading();
        getHomePageInfo();

    }

    @Override
    protected View getLoadingTargetView()
    {
        return swipeRefresh;
    }

    private void setRefreshing()
    {
        swipeRefresh.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete()
    {
        swipeRefresh.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (swipeRefresh != null)
                {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, 1000);
    }

    @Override
    public void onPause()
    {
        super.onPause();
//        banner.stopTurning();
        mBqIndexHeaderView.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //开始自动翻页
//        banner.startTurning(3000);
        mBqIndexHeaderView.onResume();
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

    private void getHomePageInfo()
    {
        String url = HttpUrls.HOST_URL + "/api/ares/homepage/";
        Map<String, String> params = null;
        if (UserInfoUtil.checkLogin(baseActivity))
        {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                if (bannerImageEntityList == null)
                {
                    showErrorInfo(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showLoading();
                            getHomePageInfo();
                        }
                    });
                }
                else
                {

                }
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty() && obj.getIntValue("status") == 0)
                    {
                        hideLoad();
                        JSONObject dataObj = obj.getJSONObject("data");
                        if (dataObj != null && !dataObj.isEmpty())
                        {
                            JSONArray eventsArray = dataObj.getJSONArray("events");
                            if (eventsArray != null && !eventsArray.isEmpty())
                            {
//                                setEvents(eventsArray);
                                mBqIndexHeaderView.setEvents(eventsArray);
                            }
                            JSONArray picArray = dataObj.getJSONArray("pics");
                            if (picArray != null && !picArray.isEmpty())
                            {
//                                setBannerPictures(picArray);
                                mBqIndexHeaderView.setBannerPictures(picArray);
                            }
                            // TODO: 2016-08-01 0001
                            mBqIndexAdapter.addData(dataObj.getJSONArray("weekly_recomends"), dataObj.getJSONArray("overseas_recomends"));
//                            JSONArray recommdArray = dataObj.getJSONArray("recomend");
//                            if (recommdArray != null && !recommdArray.isEmpty())
//                            {
//                                setAuthorAnalystInfo(recommdArray);
//                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {
                onRefreshCompelete();
            }
        });
    }

    private void setEvents(JSONArray eventsArray)
    {

    }

    @Override
    public void onRefresh()
    {
        getHomePageInfo();
    }
//
//    @OnClick(R.id.tv_more_rank)
//    protected void onClickMoreUserRank(View view)
//    {
//        Intent intent = new Intent(baseActivity, BallQMainUserRankingListActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity)
    {
        super.userLogin(userInfoEntity);
        setRefreshing();
        getHomePageInfo();
    }
}
