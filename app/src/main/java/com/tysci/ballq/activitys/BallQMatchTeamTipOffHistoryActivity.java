package com.tysci.ballq.activitys;

import android.content.Intent;
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
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchTipOffAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQMatchTeamTipOffHistoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.iv_team_icon)
    protected ImageView ivTeamIcon;
    @Bind(R.id.tv_team_name)
    protected TextView tvTeamName;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private BallQMatchEntity matchEntity=null;
    private boolean isHomeTeam;
    private List<BallQTipOffEntity> tipOffEntityList;
    private BallQMatchTipOffAdapter adapter=null;
    private int currentPages=1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_match_team_tip_off_history;
    }

    @Override
    protected void initViews() {
        setTitle("球队历史爆料");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
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
                    }
                }
            }, 1000);
        }
    }

    @Override
    protected void getIntentData(Intent intent) {
        isHomeTeam=intent.getBooleanExtra("is_home_team",false);
        matchEntity=intent.getParcelableExtra("match_info");
        if(matchEntity!=null){
            if(isHomeTeam){
                tvTeamName.setText(matchEntity.getHtname());
                GlideImageLoader.loadImage(this,matchEntity.getHtlogo(),R.drawable.icon_default_team_logo,ivTeamIcon);
            }else{
                tvTeamName.setText(matchEntity.getAtname());
                GlideImageLoader.loadImage(this,matchEntity.getAtlogo(),R.drawable.icon_default_team_logo,ivTeamIcon);
            }
            requestMatchTipOff(1,false);
        }
    }

    private void requestMatchTipOff(int pages, final boolean isLoadMore){
        int id=matchEntity.getHtid();
        if(!isHomeTeam){
            id=matchEntity.getAtid();
        }
        String url= HttpUrls.HOST_URL_V5+"team/"+id+"/tips/?etype="+matchEntity.getEtype()+"&p="+pages;
        HashMap<String,String> params=null;
        if(UserInfoUtil.checkLogin(this)){
            params=new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                    if(adapter!=null) {
                        recyclerView.setStartLoadMore();
                    }else{
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestMatchTipOff(1, false);
                            }
                        });
                    }
                }else{
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                onResponseSuccess(response,isLoadMore);
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    onRefreshCompelete();
                }
            }
        });
    }

    protected void onResponseSuccess(String response,boolean isLoadMore){
        if(!TextUtils.isEmpty(response)){
            JSONObject obj=JSONObject.parseObject(response);
            if(obj!=null&&!obj.isEmpty()){
                JSONArray objArrays=obj.getJSONArray("data");
                if(objArrays!=null&&!objArrays.isEmpty()){
                    hideLoad();
                    if(tipOffEntityList==null){
                        tipOffEntityList=new ArrayList<>(10);
                    }
                    if(!isLoadMore&&tipOffEntityList.size()>0){
                        tipOffEntityList.clear();
                    }
                    CommonUtils.getJSONListObject(objArrays, tipOffEntityList, BallQTipOffEntity.class);
                    if(adapter==null){
                        adapter=new BallQMatchTipOffAdapter(tipOffEntityList);
                        recyclerView.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }

                    if(objArrays.size()<10){
                        recyclerView.setLoadMoreDataComplete("没有更多数据了");
                    }else{
                        recyclerView.setStartLoadMore();
                        if(!isLoadMore){
                            currentPages=2;
                        }else{
                            currentPages++;
                        }
                    }
                    return;
                }
            }
        }
        if(isLoadMore){
            recyclerView.setLoadMoreDataComplete("没有更多数据了");
        }else{
            showEmptyInfo();
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

    @Override
    public void onLoadMore() {
        if(recyclerView.isRefreshing()){
            recyclerView.setLoadMoreDataComplete("刷新数据中...");
        }else{
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestMatchTipOff(currentPages,true);
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
            requestMatchTipOff(1,false);
        }
    }
}
