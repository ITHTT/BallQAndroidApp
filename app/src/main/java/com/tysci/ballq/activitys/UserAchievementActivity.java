package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.interfaces.ITabCheck;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ScreenUtil;
import com.tysci.ballq.utils.SwipeUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.UserAchievementHeaderView;
import com.tysci.ballq.views.adapters.UserAchievementAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/20.
 *
 * @author Edit by Linde
 */
public class UserAchievementActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ITabCheck {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView recycler_view;

    SwipeUtil mSwipeUtil;

    UserAchievementHeaderView headerView;

    private UserAchievementAdapter adapter;
    private List<String> dataList;

    private String userId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_user_achievement;
    }

    @Override
    protected void initViews() {
        refreshLayout.setOnRefreshListener(this);
        mSwipeUtil = new SwipeUtil(refreshLayout);

        GridLayoutManager llm = new GridLayoutManager(this, 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);


        headerView = new UserAchievementHeaderView(this);
        headerView.setOnTabChangeListener(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = ScreenUtil.getDisplayMetrics(this).widthPixels;
        headerView.setLayoutParams(lp);
        recycler_view.addHeaderView(headerView);

        dataList = new ArrayList<>();
        adapter = new UserAchievementAdapter(dataList);
        recycler_view.setAdapter(adapter);

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        userId = intent.getStringExtra("uid");
        if (TextUtils.isEmpty(userId)) {
            userId = UserInfoUtil.getUserId(this);
        }

        headerView.startFirstCheck();
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
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, HttpUrls.HOST_URL_V5 + "user/" + userId + "/achievement/", 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(UserAchievementActivity.this,R.string.request_error);
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
            }

            @Override
            public void onFinish(Call call) {
            }
        });
    }

    @Override
    public void onLeftCheck() {
    }

    @Override
    public void onCenterCheck() {
    }

    @Override
    public void onRightCheck() {
    }
}
