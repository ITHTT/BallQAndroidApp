package com.tysci.ballq.activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQUserGuessBettingRecordEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.ImageUtil;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SwipeUtil;
import com.tysci.ballq.utils.TUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserBettingGuessRecordAdapter;
import com.tysci.ballq.views.widgets.BallQUserTrendStatisticLayout;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by LinDe on 2016-07-15 0015.
 * 老用户战绩
 */
public class UserOldDataActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener {
    // 亚盘胜率 总盈亏 投资回报

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    SwipeUtil mSwipeUtil;
    @Bind(R.id.recycler_view)
    AutoLoadMoreRecyclerView recycler_view;

    private List<BallQUserGuessBettingRecordEntity> dataList;
    private BallQUserBettingGuessRecordAdapter adapter;

    @Bind(R.id.tv_left)
    TextView tv_left;
    @Bind(R.id.tv_right)
    TextView tv_right;

    @Bind(R.id.ivUserIcon)
    CircleImageView ivUserIcon;
    @Bind(R.id.iv_v)
    ImageView iv_v;
    @Bind(R.id.tvUserNickName)
    TextView tvUserNickName;

    @Bind(R.id.tv_profit)
    TextView tv_profit;
    @Bind(R.id.tv_ror)
    TextView tv_ror;
    @Bind(R.id.tvRoundNum)
    TextView tvRoundNum;
    @Bind(R.id.tvWinsNum)
    TextView tvWinsNum;
    @Bind(R.id.tvLoseNum)
    TextView tvLoseNum;
    @Bind(R.id.tvGoneNum)
    TextView tvGoneNum;
    @Bind(R.id.tv_tearn)
    TextView tv_tearn;
    @Bind(R.id.tv_wins)
    TextView tv_wins;

    @Bind(R.id.layout_all_trend)
    BallQUserTrendStatisticLayout layout_all_trend;

    @Bind(R.id.layout_normal_data)
    ViewGroup layout_normal_data;
    @Bind(R.id.layout_guess_record_data)
    ViewGroup layout_guess_record_data;

    private String userId;
    private UserInfoEntity userProfile;

    private int nextPage;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_old_user;
    }

    @Override
    protected void initViews() {
        setTitleText("老用户战绩");

        mSwipeUtil = new SwipeUtil(refreshLayout);
        refreshLayout.setOnRefreshListener(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);

        dataList = new ArrayList<>();
        adapter = new BallQUserBettingGuessRecordAdapter(dataList);

        recycler_view.setOnLoadMoreListener(this);
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected View getLoadingTargetView() {
        return findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void getIntentData(Intent intent) {
        String userId = intent.getStringExtra("uid");
        if (TextUtils.isEmpty(userId)) {
            this.userId = UserInfoUtil.getUserId(this);
        } else {
            this.userId = userId;
        }
        onTabClick(tv_left);
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
        HashMap<String, String> map = new HashMap<>();
        map.put("user", UserInfoUtil.getUserId(this));
        map.put("token", UserInfoUtil.getUserToken(this));

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, HttpUrls.HOST_URL_V5 + "old/user/" + userId + "/profile/", map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(UserOldDataActivity.this, R.string.request_error);
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                JSONObject object = JSON.parseObject(response);
                if (object.getInteger("status") == 0 && object.getString("message").equalsIgnoreCase("ok")) {
                    userProfile = object.getObject("data", UserInfoEntity.class);
                    refreshUserProfile();
                }
                mSwipeUtil.onRefreshComplete();
            }

            @Override
            public void onFinish(Call call) {
            }
        });

        onRefreshData(false);

    }

    private void onRefreshData(final boolean isLoadMore) {
        final int page = isLoadMore ? nextPage : 1;

        HashMap<String, String> map = new HashMap<>();
        map.put("user", UserInfoUtil.getUserId(this));
        map.put("token", UserInfoUtil.getUserToken(this));

        StringBuilder url = new StringBuilder();
        url.append(HttpUrls.HOST_URL_V5);
        url.append("old/user/");
        url.append(userId);
        if (tv_left.isSelected()) {
            url.append("/betting_stats_summary/");
        } else if (tv_right.isSelected()) {
            url.append("/bets/?p=");
            url.append(page);
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url.toString(), map, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                JSONObject object = JSON.parseObject(response);
                if (object.getInteger("status") == 0 && object.getString("message").equalsIgnoreCase("ok")) {
                    if (tv_left.isSelected()) {
                        layout_all_trend.setVisibility(View.VISIBLE);
                        toRefreshLeftData(object);
                    } else {
                        layout_all_trend.setVisibility(View.GONE);
                    }
                    if (tv_right.isSelected()) {
                        toRefreshRightData(isLoadMore, object);
                    } else {
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        recycler_view.setLoadMoreDataComplete();
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void toRefreshRightData(boolean isLoadMore, JSONObject object) {
        JSONArray arrays = object.getJSONArray("data");
        if (arrays != null && !arrays.isEmpty()) {
            if (!isLoadMore) {
                if (!dataList.isEmpty()) {
                    dataList.clear();
                }
            }
            CommonUtils.getJSONListObject(arrays, dataList, BallQUserGuessBettingRecordEntity.class);
            if (adapter == null) {
                adapter = new BallQUserBettingGuessRecordAdapter(dataList);
                StickyHeaderDecoration decoration = new StickyHeaderDecoration(adapter);
                recycler_view.setAdapter(adapter);
                recycler_view.addItemDecoration(decoration);
            } else {
                adapter.notifyDataSetChanged();
            }
            if (arrays.size() < 10) {
                recycler_view.setLoadMoreDataComplete();
            } else {
                recycler_view.setStartLoadMore();
                if (isLoadMore) {
                    nextPage++;
                } else {
                    nextPage = 2;
                }
            }
        }
    }

    private void toRefreshLeftData(JSONObject object) {
        JSONObject data = object.getJSONObject("data");

        layout_all_trend.setAllNum(data.getString("all_count"));
        layout_all_trend.setAllWinNum(data.getString("all_win_count"));
        layout_all_trend.setAllLoseNum(data.getString("all_lose_count"));
        layout_all_trend.setAllGoneNum(data.getString("all_go_count"));
    }

    private void refreshUserProfile() {
        ImageUtil.loadImage(ivUserIcon, R.mipmap.icon_user_default, userProfile.getPt());
        UserInfoUtil.setUserHeaderVMark(userProfile.getIsv(), iv_v, ivUserIcon);
        tvUserNickName.setText(userProfile.getFname());

        tv_profit.setText(String.format(Locale.getDefault(), "%.0f", userProfile.getRor()));
        tv_profit.append("% 盈利");

        TUtil.getInstance()
                .setText(tvRoundNum, userProfile.getBsc())
                .setText(tvWinsNum, userProfile.getBwc())
                .setText(tvLoseNum, userProfile.getBlc())
                .setText(tvGoneNum, userProfile.getBgc());

        tv_ror.setText("投资回报 ");
        tv_ror.append(String.format(Locale.getDefault(), "%.2f", userProfile.getRor()));
        tv_ror.append("%");
        tv_tearn.setText("总盈亏 ");
        tv_tearn.append(String.format(Locale.getDefault(), "%.2f", userProfile.getTearn() * 1F / 100));
        tv_wins.setText("亚盘胜率 ");
        tv_wins.append(String.format(Locale.getDefault(), "%.2f", userProfile.getWins() * 1F * 100));
        tv_wins.append("%");
    }

    @SuppressWarnings("deprecation")
    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onTabClick(View view) {
        Resources res = getResources();
        int colorNormal = res.getColor(R.color.gold);
        int colorCheck = res.getColor(R.color.c_3a3a3a);

        tv_left.setTextColor(colorNormal);
        tv_right.setTextColor(colorNormal);

        tv_left.setSelected(false);
        tv_right.setSelected(false);

        switch (view.getId()) {
            case R.id.tv_left:
            default:
                tv_left.setSelected(true);
                tv_left.setTextColor(colorCheck);
                layout_normal_data.setVisibility(View.VISIBLE);
                tv_ror.setVisibility(View.VISIBLE);
                tv_profit.setVisibility(View.GONE);
                layout_guess_record_data.setVisibility(View.GONE);
                break;
            case R.id.tv_right:
                tv_right.setSelected(true);
                tv_right.setTextColor(colorCheck);
                layout_normal_data.setVisibility(View.GONE);
                tv_ror.setVisibility(View.GONE);
                tv_profit.setVisibility(View.VISIBLE);
                layout_guess_record_data.setVisibility(View.VISIBLE);
                break;
        }
        mSwipeUtil.startRefreshing();
        onRefresh();
    }

    @Override
    public void onLoadMore() {
        onRefreshData(true);
    }
}
