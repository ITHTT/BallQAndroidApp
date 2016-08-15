package com.tysci.ballq.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BqTipOffVideoAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;

/**
 * Created by HTT
 * on 2016/7/20.
 */
public class BallQTipOffVideoListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener, CanScrollVerticallyDelegate, OnFlingOverListener
{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    private int etype = -1;

    private BqTipOffVideoAdapter mBqTipOffVideoAdapter;
    //    private BallQTipOffVideoAdapter adapter = null;
//    private List<BallQTipOffEntity> ballQTipOffEntityList = null;
    private int currentPages = 1;


    @Override
    protected int getViewLayoutId()
    {
        return R.layout.layout_swiperefresh_recyclerview;
    }

//    private void setRefreshing()
//    {
//        swipeRefresh.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                swipeRefresh.setRefreshing(true);
//            }
//        });
//    }

    private void onRefreshCompelete()
    {
        if (swipeRefresh != null)
        {
            swipeRefresh.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if (swipeRefresh != null)
                    {
                        swipeRefresh.setRefreshing(false);
                        // recyclerView.setStartLoadMore();
                    }
                }
            }, 1000);
        }
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setOnLoadMoreListener(this);
        recyclerView.setBackgroundColor(Color.parseColor("#dcdcdc"));
        // recyclerView.setAdapter(new BallQHomeTipOffAdapter());
        // showLoading();
        requestDatas(currentPages, false);
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

    public void setEtype(int etype)
    {
        this.etype = etype;
        showLoading();
        requestDatas(1, false);
    }

    private void requestDatas(int pages, final boolean isLoadMore)
    {
        String url = HttpUrls.HOST_URL_V5 + "tips/?settled=-1&etype=" + etype + "&rtype=2&p=" + pages;
        KLog.e("url:" + url);
        HashMap<String, String> params = null;
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
                if (!isLoadMore)
                {
                    recyclerView.setRefreshComplete();
                    if (mBqTipOffVideoAdapter != null)
                    {
                        recyclerView.setStartLoadMore();
                    }
                    else
                    {
                        showErrorInfo(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                showLoading();
                                requestDatas(1, false);
                            }
                        });
                    }
                }
                else
                {
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                onResponseSuccess(response, isLoadMore);
            }

            @Override
            public void onFinish(Call call)
            {
                if (!isLoadMore)
                {
                    onRefreshCompelete();
                }
            }
        });
    }

    protected void onResponseSuccess(String response, boolean isLoadMore)
    {
        if (!isLoadMore)
        {
            recyclerView.setRefreshComplete();
        }
        if (!TextUtils.isEmpty(response))
        {
            JSONObject obj = JSONObject.parseObject(response);
            if (obj != null && !obj.isEmpty())
            {
                JSONArray objArrays = obj.getJSONArray("data");
                if (objArrays != null && !objArrays.isEmpty())
                {
                    hideLoad();
//                    if (ballQTipOffEntityList == null)
//                    {
//                        ballQTipOffEntityList = new ArrayList<>(10);
//                    }
                    if (mBqTipOffVideoAdapter == null)
                    {
                        mBqTipOffVideoAdapter = new BqTipOffVideoAdapter();
                        recyclerView.setAdapter(mBqTipOffVideoAdapter);
                    }
                    if (!isLoadMore && mBqTipOffVideoAdapter.getItemCount() > 0)
                    {
                        mBqTipOffVideoAdapter.clear();
                    }
                    mBqTipOffVideoAdapter.addDataList(objArrays, isLoadMore, BallQTipOffEntity.class);
//                    CommonUtils.getJSONListObject(objArrays, ballQTipOffEntityList, BallQTipOffEntity.class);
//                    if (adapter == null)
//                    {
//                        adapter = new BallQTipOffVideoAdapter(ballQTipOffEntityList);
//                        recyclerView.setAdapter(adapter);
//                    }
//                    else
//                    {
//                        adapter.notifyDataSetChanged();
//                    }
                    if (objArrays.size() < 10)
                    {
                        recyclerView.setLoadMoreDataComplete("没有更多数据了");
                    }
                    else
                    {
                        recyclerView.setStartLoadMore();
                        if (!isLoadMore)
                        {
                            currentPages = 2;
                        }
                        else
                        {
                            currentPages++;
                        }
                    }
                    return;
                }
            }
        }
        if (isLoadMore)
        {
            recyclerView.setLoadMoreDataComplete("没有更多数据了");
        }
        if (mBqTipOffVideoAdapter == null || mBqTipOffVideoAdapter.getItemCount() == 0)
        {
            showEmptyInfo("暂无相关数据", "点击刷新", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    requestDatas(1, false);
                }
            });
        }
    }

    @Override
    public void onLoadMore()
    {
        if (recyclerView.isRefreshing())
        {
            recyclerView.setLoadMoreDataComplete("刷新数据中...");
        }
        else
        {
            recyclerView.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    requestDatas(currentPages, true);
                }
            }, 300);
        }

    }

    @Override
    public void onRefresh()
    {
        if (recyclerView.isLoadMoreing())
        {
            recyclerView.setRefreshing();
            onRefreshCompelete();
        }
        else
        {
            requestDatas(1, false);
        }
    }

    @Override
    public boolean canScrollVertically(int direction)
    {
        return recyclerView != null && recyclerView.canScrollVertically(direction);
    }

    @Override
    public void onFlingOver(int y, long duration)
    {
        if (recyclerView != null)
        {
            recyclerView.smoothScrollBy(0, y);
        }
    }
}
