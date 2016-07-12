package com.tysci.ballq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.activitys.BallQMatchGuessBettingActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchTipOffAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/7.
 *
 * @author LinDe edit
 */
public class BallQMatchTipOffListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener {
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private BallQMatchEntity ballQMatchEntity = null;
    private int currentPages = 1;
    private List<BallQTipOffEntity> matchTipOffList = null;
    private BallQMatchTipOffAdapter adapter = null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_match_tip_off_list;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyclerView.setOnLoadMoreListener(this);
        Bundle data = getArguments();
        if (data != null) {
            ballQMatchEntity = data.getParcelable("match_data");
            showLoading();
            requestDatas(ballQMatchEntity.getEid(), ballQMatchEntity.getEtype(), currentPages, false);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return contentView.findViewById(R.id.layout_tip_off_list_content);
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
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
        if (swipeRefresh != null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }

    private void requestDatas(int matchId, int etype, int pages, final boolean isLoadMore) {
        String url = HttpUrls.HOST_URL_V5 + "match/" + matchId + "/tips/?etype=" + etype + "&p=" + pages;
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (!isLoadMore) {
                    if (adapter != null) {
                        recyclerView.setStartLoadMore();
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestDatas(ballQMatchEntity.getEid(), ballQMatchEntity.getEtype(), currentPages, false);
                            }
                        });
                    }
                } else {
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null) {
                        JSONArray arrays = obj.getJSONArray("data");
                        if (arrays != null && !arrays.isEmpty()) {
                            hideLoad();
                            if (matchTipOffList == null) {
                                matchTipOffList = new ArrayList<BallQTipOffEntity>(10);
                            }
                            if (!isLoadMore && !matchTipOffList.isEmpty()) {
                                matchTipOffList.clear();
                            }
                            CommonUtils.getJSONListObject(arrays, matchTipOffList, BallQTipOffEntity.class);
                            if (adapter == null) {
                                adapter = new BallQMatchTipOffAdapter(matchTipOffList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            if (arrays.size() < 10) {
                                recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                            } else {
                                recyclerView.setStartLoadMore();
                                if (isLoadMore) {
                                    currentPages++;
                                } else {
                                    currentPages = 2;
                                }
                            }
                            return;
                        }
                    }
                    if (!isLoadMore) {
                        if (adapter == null) {
                            showEmptyInfo();
                        }
                    } else {
                        recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                if (!isLoadMore) {
                    if (recyclerView != null) {
                        recyclerView.setRefreshComplete();
                    }
                    onRefreshCompelete();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (recyclerView.isLoadMoreing()) {
            onRefreshCompelete();
        } else {
            recyclerView.setRefreshing();
            requestDatas(ballQMatchEntity.getEid(), ballQMatchEntity.getEtype(), 1, false);
        }
    }

    @Override
    public void onLoadMore() {
        if (recyclerView.isRefreshing()) {
            KLog.e("刷新数据中....");
            recyclerView.setRefreshingTip("刷新数据中...");
        } else {
            KLog.e("currentPage:" + currentPages);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestDatas(ballQMatchEntity.getEid(), ballQMatchEntity.getEtype(), currentPages, true);
                }
            }, 300);
        }
    }

    @OnClick(R.id.vgToGuess)
    protected void onClickGuess(View view) {
        if (UserInfoUtil.checkLogin(baseActivity)) {
            Date date = CommonUtils.getDateAndTimeFromGMT(ballQMatchEntity.getMtime());
            if (date != null && date.getTime() <= System.currentTimeMillis()) {
                ToastUtil.show(baseActivity, "比赛进行中/已结束,无法竞猜");
                return;
            }
            Intent intent = new Intent(baseActivity, BallQMatchGuessBettingActivity.class);
            intent.putExtra(BallQMatchGuessBettingActivity.class.getSimpleName(), ballQMatchEntity);
            startActivity(intent);
        } else {
            UserInfoUtil.userLogin(baseActivity);
        }
    }


    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {
        if(!TextUtils.isEmpty(action)){
            if(action.equals("betting_success")){
                HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
                hideLoad();
                setRefreshing();
                recyclerView.setRefreshing();
                requestDatas(ballQMatchEntity.getEid(),ballQMatchEntity.getEtype(),1,false);
            }
        }
    }
}