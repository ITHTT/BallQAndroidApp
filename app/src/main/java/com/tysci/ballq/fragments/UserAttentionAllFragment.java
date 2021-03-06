package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.modles.UserAttentionListEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.UserAttentionListAdapter;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;

/**
 * Created by HTT on 2016/5/31.
 *
 * @author LinDe edit
 */
public class UserAttentionAllFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment implements CanScrollVerticallyDelegate, OnFlingOverListener
{
    private UserAttentionListAdapter adapter = null;

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
    protected void initViews(View view, Bundle savedInstanceState)
    {
        if (UserInfoUtil.checkLogin(baseActivity))
        {
            showLoading();
            requestDatas(1, false);
        }
        else
        {
            showEmptyInfo("您尚未登录,登录后才可查看", "点击登录", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    UserInfoUtil.userLogin(baseActivity);
                }
            });
        }
    }

    @Override
    protected View getLoadingTargetView()
    {
        return swipeRefresh;
    }

    private void requestDatas(final int pages, final boolean isLoadMore)
    {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append(HttpUrls.HOST_URL);
        sb.append("/api/ares/user/subscribe/");
        sb.append("?p=").append(pages);

        HashMap<String, String> params = null;
        if (UserInfoUtil.checkLogin(baseActivity))
        {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, sb.toString(), params, new HttpClientUtil.StringResponseCallBack()
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
                    if (adapter != null)
                    {
                        recyclerView.setStartLoadMore();
                        ToastUtil.show(baseActivity, "请求失败");
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

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                if (adapter == null)
                {
                    adapter = new UserAttentionListAdapter();
                    recyclerView.setAdapter(adapter);
                }
//                mtype 1 比赛 2爆料 3球茎
                JSONObject object = JSON.parseObject(response);
                if (!JsonParams.isJsonRight(object))
                {
                    if (!isLoadMore)
                    {
                        showEmptyInfo();
                    }
                    return;
                }

                JSONArray data = object.getJSONArray("data");

                if (data == null || data.isEmpty())
                {
                    if (isLoadMore)
                    {
                        recyclerView.setLoadMoreDataComplete();
                    }
                    else
                    {
                        showEmptyInfo("暂无相关数据", "点击刷新", new View.OnClickListener()
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
                else if (data.size() < 10)
                {
                    adapter.addDataList(data, isLoadMore, UserAttentionListEntity.class);
                    recyclerView.setLoadMoreDataComplete();
                }
                else
                {
                    adapter.addDataList(data, isLoadMore, UserAttentionListEntity.class);
                    recyclerView.setStartLoadMore();
                }
                if (isLoadMore)
                {
                    currentPages++;
                }
                else
                {
                    currentPages = 2;
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
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals("attention_refresh"))
            {
                if (adapter != null)
                {
                    if (adapter.getItemCount() > 0)
                    {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                recyclerView.setLoadMoreDataComplete();
                hideLoad();
                setRefreshing();
                requestDatas(1, false);
            }
        }

    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity)
    {
        super.userLogin(userInfoEntity);
        showLoading();
        requestDatas(1, false);
    }

    @Override
    protected void userExit()
    {
        super.userExit();
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        if (!adapter.isEmpty())
        {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        showEmptyInfo("您尚未登录,登录后才可查看", "点击登录", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserInfoUtil.userLogin(baseActivity);
            }
        });
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
