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
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.JsonParams;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SwipeUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQTipOffAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-19 0019.
 * 爆料记录
 */
public class UserTipOffListRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener {
    private String userId;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView recyclerView;

    private SwipeUtil mSwipeUtil;

    private BallQTipOffAdapter adapter = null;
    private List<BallQTipOffEntity> dataList = null;

    private int page;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tip_off_list_record;
    }

    @Override
    protected void initViews() {
        setTitleText("爆料记录");

        refreshLayout.setOnRefreshListener(this);
        mSwipeUtil = new SwipeUtil(refreshLayout);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setOnLoadMoreListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void getIntentData(Intent intent) {
        userId = intent.getStringExtra("uid");
        if (TextUtils.isEmpty(userId) && UserInfoUtil.checkLogin(this)) {
            userId = UserInfoUtil.getUserId(this);
        }

        showLoading();
        onRefresh();
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onRefresh() {
        toLoadData(false);
    }

    @Override
    public void onLoadMore() {
        toLoadData(true);
    }

    private void toLoadData(final boolean isLoadMore) {
        final int page;
        if (isLoadMore) {
            page = this.page;
        } else {
            page = 1;
        }

        HashMap<String, String> map = null;
        if (UserInfoUtil.checkLogin(this)) {
            map = new HashMap<>();
            map.put("user", UserInfoUtil.getUserId(this));
            map.put("token", UserInfoUtil.getUserToken(this));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.HOST_URL_V5 + "user/" + userId + "/tips/?p=" + page, map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                showErrorInfo(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        onRefresh();
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                hideLoad();
                JSONObject object = JSON.parseObject(response);
                if (!JsonParams.isJsonRight(response)) {
                    ToastUtil.show(UserTipOffListRecordActivity.this, object.getString(JsonParams.MESSAGE));
                    return;
                }
                JSONArray data = object.getJSONArray("data");
                if (data == null || data.size() <= 0) {
                    if (isLoadMore) {
                        recyclerView.setLoadMoreDataComplete();
                    } else {
                        showEmptyInfo();
                    }
                    return;
                }
                if (dataList == null) {
                    dataList = new ArrayList<>();
                }
                if (!isLoadMore && dataList.size() > 0) {
                    dataList.clear();
                }
                CommonUtils.getJSONListObject(data, dataList, BallQTipOffEntity.class);
                if (adapter == null) {
                    adapter = new BallQTipOffAdapter(dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                if (data.size() < 10) {
                    recyclerView.setLoadMoreDataComplete();
                } else {
                    recyclerView.setStartLoadMore();
                }
                if (isLoadMore) {
                    UserTipOffListRecordActivity.this.page++;
                } else {
                    UserTipOffListRecordActivity.this.page = 2;
                }
            }

            @Override
            public void onFinish(Call call) {
                mSwipeUtil.onRefreshComplete();
            }
        });
    }
}
