package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserRewardRankInfoAdapter;
import com.tysci.ballq.views.dialogs.BallQUserRewardRankingRulesTipDialog;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/19.
 */
public class BallQUserRewardRankingDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.tv_user_rank)
    protected TextView tvUserRank;
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;
    @Bind(R.id.tv_user_betting_count)
    protected TextView tvUserBettingCount;
    @Bind(R.id.tv_user_wins)
    protected TextView tvUserWins;
    @Bind(R.id.tv_user_extra_info)
    protected TextView tvWinTips;

    private List<BallQUserRankInfoEntity> rankInfoEntityList;
    private BallQUserRewardRankInfoAdapter adapter=null;
    private int currentPages=1;
    private BallQUserRewardRankingRulesTipDialog rulesTipDialog=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_reward_events;
    }

    @Override
    protected void initViews() {
        setTitle("10万元悬赏排行榜");
        titleBar.setRightMenuIcon(R.mipmap.question_mark_check,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);

        showLoading();
        requestUserRewardRankingInfo(1, false);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void setRefreshing(){
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete(){
        if(swipeRefresh!=null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                        // mRecyclerView.setStartLoadMore();
                    }
                }
            }, 1000);
        }
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
        switch(view.getId()){
            case R.id.iv_titlebar_next_menu01:
                showRankTipDialog();
                break;
        }
    }

    private void showRankTipDialog(){
        if(rulesTipDialog==null){
            rulesTipDialog=new BallQUserRewardRankingRulesTipDialog(this);
        }
        rulesTipDialog.show();
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    private void requestUserRewardRankingInfo(final int pages,final boolean isLoadMore){
        String url= HttpUrls.HOST_URL+"/api/ares/1000ahc/?p="+pages;
        KLog.e("url:"+url);
        Map<String,String>params=new HashMap<>(2);
        params.put("user", UserInfoUtil.getUserId(this));
        params.put("token",UserInfoUtil.getUserToken(this));
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (!isLoadMore) {
                    recyclerView.setRefreshComplete();
                    if (adapter != null && !rankInfoEntityList.isEmpty()) {
                        ToastUtil.show(BallQUserRewardRankingDetailActivity.this, "刷新失败");
                        recyclerView.setStartLoadMore();
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestUserRewardRankingInfo(pages,isLoadMore);
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
                KLog.e(response);
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("status");
                        if(status==0){
                            JSONObject dataObj=obj.getJSONObject("data");
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                JSONObject userObj=dataObj.getJSONObject("userinfo");
                                if(userObj!=null&&!userObj.isEmpty()){
                                    tvUserRank.setText(userObj.getString("rank"));
                                    tvUserName.setText(userObj.getString("fname"));
                                    tvUserBettingCount.setText(userObj.getString("tip_count"));
                                    tvUserWins.setText(String.format(Locale.getDefault(), "%.0f", userObj.getFloatValue("wins")) + "%");
                                    String item=userObj.getString("counts_to_win");
                                    String tip="您还需赢"+item+"场即可获得10万奖金";
                                    CommonUtils.setTextViewFormatString(tvWinTips,tip,item, Color.parseColor("#ff0000"),1.1f);
                                }
                                JSONArray rankArray=dataObj.getJSONArray("rankinfo");
                                if(rankArray!=null&&!rankArray.isEmpty()){
                                    hideLoad();
                                    if(rankInfoEntityList==null){
                                        rankInfoEntityList=new ArrayList<BallQUserRankInfoEntity>(10);
                                    }
                                    if(!isLoadMore&&!rankInfoEntityList.isEmpty()){
                                        rankInfoEntityList.clear();
                                    }
                                    CommonUtils.getJSONListObject(rankArray,rankInfoEntityList,BallQUserRankInfoEntity.class);

                                    if(adapter==null){
                                        adapter=new BallQUserRewardRankInfoAdapter(rankInfoEntityList);
                                        recyclerView.setAdapter(adapter);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                    if(rankArray.size()<10){
                                        recyclerView.setLoadMoreDataComplete("没有更多数据");
                                    }else{
                                        recyclerView.setStartLoadMore();
                                        if(isLoadMore){
                                            currentPages++;
                                        }else{
                                            currentPages=2;
                                        }
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
                if (isLoadMore) {
                    recyclerView.setLoadMoreDataComplete("没有更多数据");
                } else {
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    onRefreshCompelete();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        if(recyclerView.isRefreshing()){
            recyclerView.setLoadMoreDataComplete("刷新数据中...");
        }else{
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestUserRewardRankingInfo(currentPages,true);
                }
            },300);
        }
    }

    @Override
    public void onRefresh() {
        if(recyclerView.isLoadMoreing()){
            recyclerView.setRefreshing();
            onRefreshCompelete();
        }else{
            requestUserRewardRankingInfo(1,false);
        }
    }
}
