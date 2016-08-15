package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.HandlerUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQGoRankListAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/15.
 */
public class BallQGoRankListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener
{

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView mRecyclerView;

    private BallQGoRankListAdapter mAdapter;

    private int nextPage = 1;

    @Override
    protected int getViewLayoutId()
    {
        return R.layout.fragment_ballq_go_rank_list;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setOnLoadMoreListener(this);

        mAdapter = new BallQGoRankListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        requestRankInfo(false);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return contentView.findViewById(R.id.swipe_refresh);
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

    private void requestRankInfo(final boolean isLoadMore)
    {
        final int page;
        if (isLoadMore)
        {
            page = nextPage;
        }
        else
        {
            page = 1;
        }

        String url = HttpUrls.HOST_URL_V5 + "go/ranklist/?p=" + page;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
            }

            @Override
            public void onError(Call call, Exception error)
            {
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        requestRankInfo(false);
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                boolean result = JsonParams.addArrayToWrapRecyclerAdapter(response, isLoadMore, mRecyclerView, mAdapter, JSONObject.class);
                if (result)
                {
                    if (isLoadMore)
                    {
                        nextPage++;
                    }
                    else
                    {
                        nextPage = 2;
                    }
                }
                else
                {
                    if (!isLoadMore)
                    {
                        showEmptyInfo();
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {
                new HandlerUtil().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },100);
            }
        });
    }

    @Override
    public void onRefresh()
    {
        requestRankInfo(false);
    }

    @Override
    public void onLoadMore()
    {
        requestRankInfo(true);
    }
}
