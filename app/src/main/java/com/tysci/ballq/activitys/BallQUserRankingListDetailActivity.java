package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.tysci.ballq.views.adapters.BallQUserRankInfoAdapter;
import com.tysci.ballq.views.dialogs.BallQUserRankingRulesTipDialog;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/19.
 */
public class BallQUserRankingListDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener
,RadioGroup.OnCheckedChangeListener{
    @Bind(R.id.layout_search)
    protected FrameLayout layoutSearch;
    @Bind(R.id.et_search)
    protected EditText etSearch;
    @Bind(R.id.iv_search)
    protected ImageView ivSearch;
    @Bind(R.id.layout_tab_ranking)
    protected RadioGroup radioGroup;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.tvRecommendRounds)
    protected TextView tvRecommendRounds;
    @Bind(R.id.tvProfitably)
    protected TextView tvProfitably;
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;

    private String rankType;
    private int timeType=7;
    private List<BallQUserRankInfoEntity> rankInfoEntityList;
    private BallQUserRankInfoAdapter adapter;
    private int currentPages=1;
    private BallQUserRankingRulesTipDialog rulesTipDialog=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_user_rank_list;
    }

    @Override
    protected void initViews() {
        layoutSearch.setVisibility(View.GONE);
        titleBar.setRightMenuIcon(R.drawable.search_selector, this);
        titleBar.setSecondRightMenuIcon(R.drawable.question_selector, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
        radioGroup.setOnCheckedChangeListener(this);

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
        rankType=intent.getStringExtra("rank_type");
        String title=intent.getStringExtra("title");
        timeType=intent.getIntExtra("date_type",7);
        if(!TextUtils.isEmpty(title)){
            setTitle(title);
        }
        if(!TextUtils.isEmpty(rankType)) {
            if (rankType.equals("follow")){
                radioGroup.setVisibility(View.GONE);
                tvUserName.setText("头像");
                tvRecommendRounds.setText("用户名");
                tvProfitably.setText("粉丝数");
            }else{
                tvProfitably.setText(title.substring(0,title.length()-1));
                setSelectedTab(timeType);
            }
            showLoading();
            requestUserRankingDatas(1, false);
        }
    }

    private void setSelectedTab(int timeType){
        if(timeType==7){
            radioGroup.check(R.id.rb_week_ranking);
        }else if(timeType==30){
            radioGroup.check(R.id.rb_month_ranking);
        }else if(timeType==1){
            radioGroup.check(R.id.rb_all_ranking);
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
        int id=view.getId();
        switch(id){
            case R.id.iv_titlebar_next_menu01:
                toggleSearchView(view);
                break;
            case R.id.iv_titlebar_next_menu02:
                showUserRankingRuleTips();
                break;
        }
    }

    private void showUserRankingRuleTips(){
        if(rulesTipDialog==null){
            rulesTipDialog=new BallQUserRankingRulesTipDialog(this);
        }
        rulesTipDialog.show();
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    private void toggleSearchView(View view){
        ImageView ivSearchMenu= (ImageView) view;
        ivSearchMenu.setSelected(!ivSearchMenu.isSelected());
        if(ivSearchMenu.isSelected()){
            layoutSearch.setVisibility(View.VISIBLE);
        }else{
            layoutSearch.setVisibility(View.GONE);
        }
    }

    private void requestUserRankingDatas(final int pages, final boolean isLoadMore){
        String url=null;
        if(!rankType.equals("follow")) {
            url = HttpUrls.HOST_URL_V5 + "user/rank/" + rankType + "/" + timeType + "/?p=" + pages;
        }else{
            url=HttpUrls.HOST_URL_V5+ "follow/rank/?p=" + pages;
        }
        Map<String,String> params=null;
        if(UserInfoUtil.checkLogin(this)){
            params=new HashMap<String,String>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (!isLoadMore) {
                    recyclerView.setRefreshComplete();
                    if (adapter != null && !rankInfoEntityList.isEmpty()) {
                        ToastUtil.show(BallQUserRankingListDetailActivity.this, "刷新失败");
                        recyclerView.setStartLoadMore();
                    } else {
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestUserRankingDatas(pages, isLoadMore);
                            }
                        });
                    }
                } else {
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                if (!isLoadMore) {
                    recyclerView.setRefreshComplete();
                }
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        if (dataArray != null && !dataArray.isEmpty()) {
                            hideLoad();
                            if (rankInfoEntityList == null) {
                                rankInfoEntityList = new ArrayList<BallQUserRankInfoEntity>(10);
                            }
                            if (!isLoadMore) {
                                if (!rankInfoEntityList.isEmpty()) {
                                    rankInfoEntityList.clear();
                                }
                            }
                            CommonUtils.getJSONListObject(dataArray, rankInfoEntityList, BallQUserRankInfoEntity.class);
                            if (adapter == null) {
                                adapter = new BallQUserRankInfoAdapter(rankInfoEntityList);
                                adapter.setRankType(rankType);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            if (dataArray.size() < 10) {
                                recyclerView.setLoadMoreDataComplete("没有更多数据");
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
                }
                if (isLoadMore) {
                    recyclerView.setLoadMoreDataComplete("没有更多数据");
                } else {
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {
                if (!isLoadMore) {
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
                    requestUserRankingDatas(currentPages,true);
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
            requestUserRankingDatas(1, false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int type=7;
        if(checkedId==R.id.rb_week_ranking){
            type=7;
        }else if (checkedId == R.id.rb_month_ranking) {
            type=30;
        }else if(checkedId==R.id.rb_all_ranking){
            type=1;
        }
        if(timeType!=type){
            timeType=type;
            HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
            recyclerView.setLoadMoreDataComplete();
            hideLoad();
            setRefreshing();
            if(adapter!=null){
                if(!rankInfoEntityList.isEmpty()){
                    rankInfoEntityList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            requestUserRankingDatas(1,false);
        }
    }

    @OnClick(R.id.iv_search)
    protected void onClickSearch(final View view){
        String search=etSearch.getText().toString();
        if(TextUtils.isEmpty(search)){
            ToastUtil.show(this,"请输入查找内容");
            return;
        }
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        hideLoad();
        setRefreshing();
        recyclerView.setLoadMoreDataComplete();
        if(adapter!=null){
            if(!rankInfoEntityList.isEmpty()){
                rankInfoEntityList.clear();
                adapter.notifyDataSetChanged();
            }
        }
        startSearch(search);
    }

    private void startSearch(final String search){
        String url=HttpUrls.HOST_URL_V5 + "user/query_users/";
        Map<String,String> params=new HashMap<String,String>(3);;
        if(UserInfoUtil.checkLogin(this)){
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        params.put("fname",search);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                layoutSearch.setVisibility(View.GONE);
                titleBar.getRightMenuImageView().setSelected(false);
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(BallQUserRankingListDetailActivity.this,"请求失败");
                showErrorInfo(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        startSearch(search);
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        if (dataArray != null && !dataArray.isEmpty()) {
                            ToastUtil.show(BallQUserRankingListDetailActivity.this,"查找成功");
                            if (rankInfoEntityList == null) {
                                rankInfoEntityList = new ArrayList<BallQUserRankInfoEntity>(10);
                            }
                            CommonUtils.getJSONListObject(dataArray, rankInfoEntityList, BallQUserRankInfoEntity.class);
                            if (adapter == null) {
                                adapter = new BallQUserRankInfoAdapter(rankInfoEntityList);
                                adapter.setRankType(rankType);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            recyclerView.setLoadMoreDataComplete("没有更多数据");
                            return;
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
}
