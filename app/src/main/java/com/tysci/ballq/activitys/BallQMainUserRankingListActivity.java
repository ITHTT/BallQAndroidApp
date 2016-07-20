package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQUserRankInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.adapters.BallQMainUserRankingAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/18.
 */
public class BallQMainUserRankingListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener {
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private Map<String, List<BallQUserRankInfoEntity>> datas = null;
    private BallQMainUserRankingAdapter adapter = null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_user_ranking_list;
    }

    @Override
    protected void initViews() {
        setTitle("排行榜");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setOnLoadMoreListener(this);
        addHeader();
        showLoading();
        requestUserRankingInfos();
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void setRefreshing() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete() {
        swipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, 1000);
    }

    private void addHeader() {
        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.layout_ballq_reward_header, null);
        imageView.setImageResource(R.mipmap.icon_reward_tip);
        recyclerView.addHeaderView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BallQMainUserRankingListActivity.this, BallQUserRewardRankingDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void getIntentData(Intent intent) {

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

    private void requestUserRankingInfos() {
        String url = HttpUrls.HOST_URL_V6 + "rank_summary/";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (adapter == null) {
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestUserRankingInfos();
                        }
                    });
                } else {
                    ToastUtil.show(BallQMainUserRankingListActivity.this, "请求失败");
                }

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int status = obj.getIntValue("status");
                        if (status == 0) {
                            JSONObject dataObj = obj.getJSONObject("data");
                            if (dataObj != null && !dataObj.isEmpty()) {
                                hideLoad();
                                if (datas == null) {
                                    datas = new HashMap<String, List<BallQUserRankInfoEntity>>(4);
                                }
                                JSONArray winsArray = dataObj.getJSONArray("wins");
                                if (winsArray != null && !winsArray.isEmpty()) {
                                    List<BallQUserRankInfoEntity> wins = new ArrayList<BallQUserRankInfoEntity>(winsArray.size());
                                    CommonUtils.getJSONListObject(winsArray, wins, BallQUserRankInfoEntity.class);
                                    datas.put("亚盘胜率榜", wins);
                                }

                                JSONArray rorArray = dataObj.getJSONArray("ror");
                                if (rorArray != null && !rorArray.isEmpty()) {
                                    List<BallQUserRankInfoEntity> rors = new ArrayList<BallQUserRankInfoEntity>(rorArray.size());
                                    CommonUtils.getJSONListObject(rorArray, rors, BallQUserRankInfoEntity.class);
                                    datas.put("盈利率榜", rors);
                                }

                                JSONArray followerArray = dataObj.getJSONArray("follower");
                                if (followerArray != null && !followerArray.isEmpty()) {
                                    List<BallQUserRankInfoEntity> followers = new ArrayList<BallQUserRankInfoEntity>(followerArray.size());
                                    CommonUtils.getJSONListObject(followerArray, followers, BallQUserRankInfoEntity.class);
                                    datas.put("人气榜", followers);
                                }

                                JSONArray tearnArray = dataObj.getJSONArray("tearn");
                                if (tearnArray != null && !tearnArray.isEmpty()) {
                                    List<BallQUserRankInfoEntity> tearns = new ArrayList<BallQUserRankInfoEntity>(tearnArray.size());
                                    CommonUtils.getJSONListObject(tearnArray, tearns, BallQUserRankInfoEntity.class);
                                    datas.put("总盈利榜", tearns);
                                }

                                if (adapter == null) {
                                    adapter = new BallQMainUserRankingAdapter(datas);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                                recyclerView.setLoadMoreDataComplete("没有更多数据");
                                return;
                            }
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();

            }
        });
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        requestUserRankingInfos();

    }
}
