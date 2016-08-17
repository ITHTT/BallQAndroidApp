package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQCircleNoteEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.adapters.BallQFindCircleNoteAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQFindCircleNoteListFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment
{
    private List<BallQCircleNoteEntity> ballQCircleNoteEntityList;
    private BallQFindCircleNoteAdapter adapter = null;
    private int sectionId;

    @Override
    protected void initViews(View view, Bundle savedInstanceState)
    {
        showLoading();
        requestDatas(1, false);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return swipeRefresh;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        recyclerView.smoothScrollToPosition(0);
        showLoading();
        requestDatas(1, false);
    }

    private void requestDatas(final int pages, final boolean isLoadMore)
    {
        String url = HttpUrls.CIRCLE_HOST_URL_V1 + "bbs/topic/list?sortType=0&pageNo=" + pages + "&sectionId=" + sectionId + "&pageSize=10";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack()
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
                    if (adapter != null)
                    {
                        ToastUtil.show(baseActivity, "请求失败");
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

            @Override
            public void onSuccess(Call call, String response)
            {
                if (!isLoadMore)
                {
                    recyclerView.setRefreshComplete();
                }
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty())
                    {
                        JSONObject dataObj = obj.getJSONObject("dataMap");
                        if (dataObj != null && !dataObj.isEmpty())
                        {
                            JSONArray jsonArray = dataObj.getJSONArray("topics");
                            if (jsonArray != null && !jsonArray.isEmpty())
                            {
                                hideLoad();
                                if (ballQCircleNoteEntityList == null)
                                {
                                    ballQCircleNoteEntityList = new ArrayList<BallQCircleNoteEntity>(10);
                                }
                                if (!isLoadMore && !ballQCircleNoteEntityList.isEmpty())
                                {
                                    ballQCircleNoteEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(jsonArray, ballQCircleNoteEntityList, BallQCircleNoteEntity.class);
                                if (adapter == null)
                                {
                                    adapter = new BallQFindCircleNoteAdapter(ballQCircleNoteEntityList);
                                    recyclerView.setAdapter(adapter);
                                }
                                else
                                {
                                    adapter.notifyDataSetChanged();
                                }
                                if (jsonArray.size() < 10)
                                {
                                    recyclerView.setLoadMoreDataComplete("没有更多数据了");
                                }
                                else
                                {
                                    recyclerView.setStartLoadMore();
                                    if (isLoadMore)
                                    {
                                        currentPages++;
                                    }
                                    else
                                    {
                                        currentPages = 2;
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
                if (isLoadMore)
                {
                    recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                }
                else
                {
                    showEmptyInfo();
                }
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
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals("delete_circle_note"))
            {
                int id = data.getInt("sectionId", -1);
                if (id == sectionId)
                {
                    setRefreshing();
                    requestDatas(1, false);
                }
            }
        }

    }

    public void setCircleSectionId(int type)
    {
        this.sectionId = type;
    }
}
