package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

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
import com.tysci.ballq.views.adapters.BallQTipOffUserInfoAdapter;
import com.tysci.ballq.views.widgets.DividerDecoration;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQTipOffUserListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private String searchTag;
    private List<BallQUserRankInfoEntity> rankInfoEntityList;
    private BallQTipOffUserInfoAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_tip_off_user_list;
    }

    @Override
    protected void initViews() {
        setTitle("查找爆料人");
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
        searchTag=intent.getStringExtra("search_tag");
        if(!TextUtils.isEmpty(searchTag)){
            showLoading();
            requestSearchTag(searchTag);
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

    private void onRefreshCompelete(){
        if(swipeRefresh!=null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                        // recyclerView.setStartLoadMore();
                    }
                }
            }, 1000);
        }
    }

    private void requestSearchTag(final String tag){
        String url= HttpUrls.HOST_URL_V5+ "user/query_users/";
        Map<String,String> params=new HashMap<>(3);
        params.put("fname",tag);
        if(UserInfoUtil.checkLogin(this)){
            params.put("user",UserInfoUtil.getUserId(this));
            params.put("token",UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(adapter==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestSearchTag(tag);
                        }
                    });
                }else{
                    ToastUtil.show(BallQTipOffUserListActivity.this,"请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("status");
                        if(status==0){
                            JSONArray datas=obj.getJSONArray("data");
                            if(datas!=null&&!datas.isEmpty()){
                                hideLoad();
                                if(rankInfoEntityList==null){
                                    rankInfoEntityList=new ArrayList<BallQUserRankInfoEntity>(10);
                                }
                                if(!rankInfoEntityList.isEmpty()){
                                    rankInfoEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(datas, rankInfoEntityList, BallQUserRankInfoEntity.class);
                                if(adapter==null){
                                    adapter=new BallQTipOffUserInfoAdapter(rankInfoEntityList);
                                    recyclerView.setAdapter(adapter);
//                                    DividerDecoration decoration=new DividerDecoration(BallQTipOffUserListActivity.this);
//                                    recyclerView.addItemDecoration(decoration);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                                recyclerView.setLoadMoreDataComplete("没有更多数了");
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
        requestSearchTag(searchTag);
    }
}
