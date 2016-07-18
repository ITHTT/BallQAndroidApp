package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQUserAccountRecordEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ScreenUtil;
import com.tysci.ballq.utils.SwipeUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.utils.WeChatUtil;
import com.tysci.ballq.views.UserWithdrawalsHeaderView;
import com.tysci.ballq.views.adapters.BallQUserAccountRecordAdapter;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-18 0018.
 * 账户提现
 */
public class UserWithdrawsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView recyclerView;

    private SwipeUtil mSwipeUtil;
    private BallQUserAccountRecordAdapter adapter;

    private UserWithdrawalsHeaderView headerView;

    private int nextPage;
    private ArrayList<BallQUserAccountRecordEntity> recordEntityList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_withdraws;
    }

    @Override
    protected void initViews() {
        setTitleText("球商提现");

        refreshLayout.setOnRefreshListener(this);
        mSwipeUtil = new SwipeUtil(refreshLayout);

        headerView = new UserWithdrawalsHeaderView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = ScreenUtil.getDisplayMetrics(this).widthPixels;
        headerView.setLayoutParams(lp);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addHeaderView(headerView);
        recyclerView.setOnLoadMoreListener(this);
        adapter = new BallQUserAccountRecordAdapter(recordEntityList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected View getLoadingTargetView() {
        return findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void getIntentData(Intent intent) {
        int balance = intent.getIntExtra("Balance", 0);
        boolean isBindWeChat = intent.getBooleanExtra("BindToWeChat", false);
        String wx_name = intent.getStringExtra("wx_name");
        String wx_portrait = intent.getStringExtra("wx_portrait");

        headerView.setBindFlags(balance, isBindWeChat, wx_name, wx_portrait);

        mSwipeUtil.startRefreshing();
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
        KLog.e();
    }

    @Override
    protected void notifyEvent(String action, Bundle data) {
        final String code = data.getString("code");
        final String apply = data.getString("apply");
        WX_code(code);
        if (!TextUtils.isEmpty(apply) && apply.equalsIgnoreCase("success")) {
            mSwipeUtil.startRefreshing();
            onRefresh();
        }
    }

    private void WX_code(String code) {
        if (!TextUtils.isEmpty(code)) {
            final LoadingProgressDialog dialog = new LoadingProgressDialog(this);
            WeChatUtil.getWeChatUserInfo(this, Tag, code, new HttpClientUtil.StringResponseCallBack() {

                @Override
                public void onBefore(Request request) {
                    dialog.show();
                }

                @Override
                public void onError(Call call, Exception error) {
                }

                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);

                    JSONObject object = JSON.parseObject(response);
                    if (object != null) {
                        final String openid = object.getString("openid");
                        final String nickname = object.getString("nickname");
                        final String headimgurl = object.getString("headimgurl");
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                headerView.setBindWeChat(openid, nickname, headimgurl);
                            }
                        });
                    }
                }

                @Override
                public void onFinish(Call call) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        onRequestData(false);
    }

    @Override
    public void onLoadMore() {
        onRequestData(true);
    }

    private void onRequestData(final boolean isLoadMore) {
        if (!headerView.isBindToWX()) {
            mSwipeUtil.onRefreshComplete();
            return;
        }
        int page;
        if (isLoadMore) {
            page = nextPage;
        } else {
            page = 1;
        }

        HashMap<String, String> map = new HashMap<>();
        if (UserInfoUtil.checkLogin(this)) {
            map.put("user", UserInfoUtil.getUserId(this));
            map.put("token", UserInfoUtil.getUserToken(this));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.HOST_URL_V5 + "withdraw_list/?p=" + page, map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(UserWithdrawsActivity.this, R.string.request_error);
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        JSONArray datas = obj.getJSONArray("data");
                        if (datas != null && !datas.isEmpty()) {
                            if (!isLoadMore) {
                                hideLoad();
                            }
                            if (recordEntityList == null) {
                                recordEntityList = new ArrayList<>(10);
                            }
                            if (!isLoadMore) {
                                if (!recordEntityList.isEmpty()) {
                                    recordEntityList.clear();
                                }
                            }
                            CommonUtils.getJSONListObject(datas, recordEntityList, BallQUserAccountRecordEntity.class);
                            setTime(recordEntityList);
                            if (adapter == null) {
                                adapter = new BallQUserAccountRecordAdapter(recordEntityList);
                                StickyHeaderDecoration decoration = new StickyHeaderDecoration(adapter);
                                recyclerView.setAdapter(adapter);
                                recyclerView.addItemDecoration(decoration);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            if (datas.size() < 10) {
                                recyclerView.setLoadMoreDataComplete();
                            } else {
                                recyclerView.setStartLoadMore();
                                if (isLoadMore) {
                                    nextPage++;
                                } else {
                                    nextPage = 2;
                                }
                            }
                            return;
                        }
                    }
                }
                if (isLoadMore) {
                    recyclerView.setLoadMoreDataComplete();
                } else if (adapter == null) {
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {
                mSwipeUtil.onRefreshComplete();
            }
        });
    }

    private void setTime(List<BallQUserAccountRecordEntity> datas) {
        if (datas != null && !datas.isEmpty()) {
            for (BallQUserAccountRecordEntity info : datas) {
                Date date = CommonUtils.getDateAndTimeFromGMT(info.getCtime());
                if (date != null) {
                    String dateStr = CommonUtils.getMM_ddString(date);
                    // KLog.e("dateStr:"+dateStr);
                    String time = CommonUtils.getTimeOfDay(date);
                    info.setTime(time);
                    info.setDate(dateStr);
                }
            }
        }
    }
}
