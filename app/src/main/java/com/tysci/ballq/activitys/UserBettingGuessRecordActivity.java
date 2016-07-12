package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQUserGuessBettingRecordEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserBettingGuessRecordAdapter;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserBettingGuessRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;
    @Bind(R.id.tv_user_profit_rate)
    protected TextView tvUserProfitRate;
    @Bind(R.id.tv_all_count)
    protected TextView tvAllCount;
    @Bind(R.id.tv_win_count)
    protected TextView tvWinCount;
    @Bind(R.id.tv_lose_count)
    protected TextView tvLoseCount;
    @Bind(R.id.tv_go_count)
    protected TextView tvGoCount;
    @Bind(R.id.ivUserIcon)
    protected CircleImageView ivUserIcon;
    @Bind(R.id.isV)
    protected ImageView ivS;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private List<BallQUserGuessBettingRecordEntity> userGuessBettingRecordEntityList;
    private BallQUserBettingGuessRecordAdapter adapter=null;

    private String uid;
    private String bet;
    private String query;
    private String etype;
    private String tip="没有更多的数据了...";

    private int currentPages=1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_user_betting_record;
    }

    @Override
    protected void initViews() {
        setTitle("竞猜记录");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    @Override
    protected void getIntentData(Intent intent) {
        uid=intent.getStringExtra(Tag);
        if(TextUtils.isEmpty(uid)){
            uid= UserInfoUtil.getUserId(this);
        }
        showLoading();
        getUserInfo(uid);
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

    /**
     * 获取用户信息
     * @param uid
     */
    private void getUserInfo(final String uid){
        String url= HttpUrls.HOST_URL_V5 + "user/" + uid + "/profile/";
        HashMap<String,String> params=null;
        if(UserInfoUtil.checkLogin(this)){
            params=new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag,url,params,new HttpClientUtil.StringResponseCallBack(){

            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                onRefreshCompelete();
                if(adapter==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            getUserInfo(uid);
                        }
                    });
                }else{
                    ToastUtil.show(UserBettingGuessRecordActivity.this, "请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null){
                        JSONObject dataObj=obj.getJSONObject("data");
                        if(dataObj!=null){
                            requestUserBettingRecordInfos(1, uid, bet, query, etype, false);
                            GlideImageLoader.loadImage(UserBettingGuessRecordActivity.this, dataObj.getString("pt"), R.mipmap.icon_user_default, ivUserIcon);
                            UserInfoUtil.setUserHeaderVMark(dataObj.getIntValue("isv"), ivS, ivUserIcon);
                            tvUserName.setText(dataObj.getString("fname"));
                            tvUserProfitRate.setText(String.format(Locale.getDefault(), "%.2f", dataObj.getFloat("ror")) + "% 盈利");
                            tvAllCount.setText("场 "+dataObj.getString("bsc"));

                            String winCount=dataObj.getString("bwc");
                            CommonUtils.setTextViewFormatString(tvWinCount, "赢 " + winCount, winCount, Color.parseColor("#e35354"), 1f);

                            String loseCount=dataObj.getString("blc");
                            CommonUtils.setTextViewFormatString(tvLoseCount, "输 "+loseCount, loseCount, Color.parseColor("#469c4a"), 1f);

                            String goneCount=dataObj.getString("bgc");
                            CommonUtils.setTextViewFormatString(tvGoCount, "走 "+goneCount, goneCount, Color.parseColor("#d4d4d4"), 1f);
                            return;
                        }
                    }
                }
                onRefreshCompelete();
                if(adapter==null) {
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void requestUserBettingRecordInfos(final int pages,final String uid,final String bet, final String query, final String etype, final boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5 + "user/" + uid + "/bets" + (TextUtils.isEmpty(bet)?"":"/"+bet) + "/?p=" + pages + (TextUtils.isEmpty(query)?"":"&query=" + query) + (!TextUtils.isEmpty(etype)? "&etype=" + etype : "");
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (!isLoadMore) {
                    if (adapter != null) {
                        recyclerView.setStartLoadMore();
                        ToastUtil.show(UserBettingGuessRecordActivity.this,"请求失败");
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestUserBettingRecordInfos(pages,uid,bet,query,etype,isLoadMore);
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
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()&&obj.getIntValue("status")==0){
                        JSONArray arrays=obj.getJSONArray("data");
                        if(arrays!=null&&!arrays.isEmpty()){
                            hideLoad();
                            if(userGuessBettingRecordEntityList==null){
                                userGuessBettingRecordEntityList=new ArrayList<BallQUserGuessBettingRecordEntity>(10);
                            }
                            if(!isLoadMore){
                                if(!userGuessBettingRecordEntityList.isEmpty()){
                                    userGuessBettingRecordEntityList.clear();
                                }
                            }
                            CommonUtils.getJSONListObject(arrays,userGuessBettingRecordEntityList,BallQUserGuessBettingRecordEntity.class);
                            if(adapter==null){
                                adapter=new BallQUserBettingGuessRecordAdapter(userGuessBettingRecordEntityList);
                                StickyHeaderDecoration decoration=new StickyHeaderDecoration(adapter);
                                recyclerView.setAdapter(adapter);
                                recyclerView.addItemDecoration(decoration);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            if(arrays.size()<10){
                                recyclerView.setLoadMoreDataComplete(tip);
                            }else{
                                recyclerView.setStartLoadMore();
                                if(isLoadMore){
                                    currentPages++;
                                }else{
                                    currentPages=2;
                                }
                                return;
                            }
                        }
                    }
                }
                if(adapter==null){
                    showEmptyInfo();
                }else{
                    if(!isLoadMore){
                        recyclerView.setLoadMoreDataComplete(tip);
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                    onRefreshCompelete();
                }

            }
        });
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
                    requestUserBettingRecordInfos(currentPages,uid,bet,query,etype,true);
                }
            }, 300);
        }
    }

    @Override
    public void onRefresh() {
        if (recyclerView.isLoadMoreing()) {
            onRefreshCompelete();
        } else {
            recyclerView.setRefreshing();
            getUserInfo(uid);
        }
    }
}
