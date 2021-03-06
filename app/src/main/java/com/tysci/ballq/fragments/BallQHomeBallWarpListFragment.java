package com.tysci.ballq.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.event.EventObject;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.SharedPreferencesUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BqBallWrapAdapter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;

/**
 * Created by HTT on 2016/5/31.
 *
 * @author LinDe edit
 */
public class BallQHomeBallWarpListFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment implements CanScrollVerticallyDelegate, OnFlingOverListener
{
    //    private List<BallQBallWarpInfoEntity> ballQBallWarpInfoEntityList;
//    private BallQBallWarpAdapter adapter;
    private BqBallWrapAdapter mBqBallWrapAdapter;

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        recyclerView.setBackgroundColor(Color.parseColor("#dcdcdc"));
        requestDatas(currentPages, false);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return swipeRefresh;
    }

    private void requestDatas(final int pages, final boolean isLoadMore)
    {
        String url = HttpUrls.HOST_URL + "/api/ares/articles/?p=" + pages;
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
                if (recyclerView != null)
                {
                    if (!isLoadMore)
                    {
                        if (mBqBallWrapAdapter != null)
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
                                    requestDatas(pages, false);
                                }
                            });
                        }
                    }
                    else
                    {
                        recyclerView.setLoadMoreDataFailed();
                    }
                }
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                if (!isLoadMore)
                {
                    onRefreshCompelete();
                    hideLoad();
                }
                if (mBqBallWrapAdapter == null)
                {
                    mBqBallWrapAdapter = new BqBallWrapAdapter();
                    recyclerView.setAdapter(mBqBallWrapAdapter);
                }
                if (!isLoadMore && mBqBallWrapAdapter.getItemCount() > 0)
                {
                    mBqBallWrapAdapter.addDataList(false);
                }
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty())
                    {
                        JSONObject data = obj.getJSONObject("data");

                        //noinspection StringBufferReplaceableByString
                        StringBuilder sb = new StringBuilder();
                        sb.append("{");
                        sb.append("\"status\":\"");
                        sb.append("article_reset");
                        sb.append("\"}");
                        EventObject eventObject = new EventObject();
                        eventObject.getData().putString("dot", sb.toString());
                        eventObject.addReceiver(BallQTipOffFragment.class);
                        EventObject.postEventObject(eventObject, "dot_task");
                        SharedPreferencesUtil.setValue(baseActivity, SharedPreferencesUtil.KEY_ARTICLE_MSG_DOT, data.getLong("tag"));

                        JSONArray objArray = data.getJSONArray("articles");
                        if (objArray != null && !objArray.isEmpty())
                        {
                            mBqBallWrapAdapter.addDataList(objArray, isLoadMore, BallQBallWarpInfoEntity.class);
                            if (objArray.size() < 10)
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
            }

            @Override
            public void onFinish(Call call)
            {
                if (!isLoadMore)
                {
                    recyclerView.setRefreshComplete();
                    onRefreshCompelete();
                }
            }
        });
    }

    @Override
    protected void onLoadMoreData()
    {
        requestDatas(currentPages, true);

    }

    @Override
    protected void onRefreshData()
    {
        requestDatas(1, false);
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
