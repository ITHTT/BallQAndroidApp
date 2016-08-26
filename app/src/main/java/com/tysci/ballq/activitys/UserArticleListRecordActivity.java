package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SwipeUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BqBallWrapAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-19 0019.
 * 爆料记录
 */
public class UserArticleListRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener
{
    private String userId;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView mRecyclerView;

    private SwipeUtil mSwipeUtil;

    private BqBallWrapAdapter mAdapter = null;
//    private List<BallQBallWarpInfoEntity> dataList = null;

    private int page;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_tip_off_list_record;
    }

    @Override
    protected void initViews()
    {
        setTitleText("球经记录");

        refreshLayout.setOnRefreshListener(this);
        mSwipeUtil = new SwipeUtil(refreshLayout);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.setOnLoadMoreListener(this);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        userId = intent.getStringExtra("uid");
        if (TextUtils.isEmpty(userId) && UserInfoUtil.checkLogin(this))
        {
            userId = UserInfoUtil.getUserId(this);
        }

        showLoading();
        onRefresh();
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {

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
    public void onRefresh()
    {
        toLoadData(false);
    }

    @Override
    public void onLoadMore()
    {
        toLoadData(true);
    }

    private void toLoadData(final boolean isLoadMore)
    {
        final int page;
        if (isLoadMore)
        {
            page = this.page;
        }
        else
        {
            page = 1;
        }

        HashMap<String, String> map = null;
        if (UserInfoUtil.checkLogin(this))
        {
            map = new HashMap<>();
            map.put("user", UserInfoUtil.getUserId(this));
            map.put("token", UserInfoUtil.getUserToken(this));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.HOST_URL_V5 + "user/articles/?uid=" + userId + "&p=" + page, map, new HttpClientUtil.StringResponseCallBack()
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
                        showLoading();
                        onRefresh();
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                JSONObject object = JSON.parseObject(response);
                if (!JsonParams.isJsonRight(response))
                {
                    ToastUtil.show(UserArticleListRecordActivity.this, object.getString(JsonParams.MESSAGE));
                    return;
                }
                JSONArray data = object.getJSONArray("data");
                if (data == null || data.size() <= 0)
                {
                    if (isLoadMore)
                    {
                        mRecyclerView.setLoadMoreDataComplete();
                    }
                    else
                    {
                        showEmptyInfo();
                    }
                    return;
                }
                if (mAdapter == null)
                {
                    mAdapter = new BqBallWrapAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }
//                if (dataList == null) {
//                    dataList = new ArrayList<>();
//                }
//                if (!isLoadMore && dataList.size() > 0) {
//                    dataList.clear();
//                }
                if (!isLoadMore && mAdapter.getItemCount() > 0)
                {
                    mAdapter.addDataList(false);
                }
//                CommonUtils.getJSONListObject(data, dataList, BallQBallWarpInfoEntity.class);
//                if (mAdapter == null)
//                {
//                    mAdapter = new BqBallWrapAdapter(dataList);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//                else
//                {
//                    mAdapter.notifyDataSetChanged();
//                }
                mAdapter.addDataList(data, isLoadMore, BallQBallWarpInfoEntity.class);

                if (data.size() < 10)
                {
                    mRecyclerView.setLoadMoreDataComplete();
                }
                else
                {
                    mRecyclerView.setStartLoadMore();
                }
                if (isLoadMore)
                {
                    UserArticleListRecordActivity.this.page++;
                }
                else
                {
                    UserArticleListRecordActivity.this.page = 2;
                }
            }

            @Override
            public void onFinish(Call call)
            {
                mSwipeUtil.onRefreshComplete();
            }
        });
    }
}
