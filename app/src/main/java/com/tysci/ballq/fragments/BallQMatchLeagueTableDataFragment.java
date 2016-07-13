package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchLeagueTableEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.adapters.BallQMatchLeagueTableAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/8.
 * 积分榜
 */
public class BallQMatchLeagueTableDataFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener,RadioGroup.OnCheckedChangeListener{
    private BallQMatchEntity matchEntity;
    private int type=1;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.layout_group)
    protected RadioGroup radioGroup;
    @Bind(R.id.iv_filter)
    protected ImageView ivFilter;
    @Bind(R.id.tvWin)
    protected TextView tvWin;
    @Bind(R.id.tvDraw)
    protected TextView tvDraw;
    @Bind(R.id.tvLose)
    protected TextView tvLose;
    @Bind(R.id.tvHaveConceded)
    protected TextView tvHaveConceded;
    @Bind(R.id.tvGoalDifference)
    protected TextView tvGoalDifference;
    @Bind(R.id.tvTitle)
    protected TextView tvTitle;

    private List<BallQMatchLeagueTableEntity> matchLeagueTableEntityList=null;
    private BallQMatchLeagueTableAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_match_league_table_data;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyclerView.setOnLoadMoreListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        ivFilter.setSelected(true);
        getDatas();
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    protected void setRefreshing(){
        if(!swipeRefresh.isRefreshing()) {
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
    }

    protected void onRefreshCompelete(){
        if(swipeRefresh!=null) {
            if (swipeRefresh.isRefreshing()) {
                swipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefresh!=null) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        }
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    private void getDatas(){
        Bundle data=getArguments();
        if(data!=null){
            matchEntity= data.getParcelable("match_data");
            if(matchEntity!=null){
                tvTitle.setText(matchEntity.getTourname());
                showLoading();
                requestDatas(matchEntity.getTourid(),type);
            }
        }
    }

    private void requestDatas(final int tournamentId, final int type){
        String url= HttpUrls.HOST_URL_V3 + "stats/tournament/" + tournamentId + (type == 1 ? "/total/" : (type == 2 ? "/home/" : "/away/"));
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(adapter==null||matchLeagueTableEntityList==null||matchLeagueTableEntityList.isEmpty()){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestDatas(tournamentId,type);
                        }
                    });
                }else{
                    ToastUtil.show(baseActivity, "请求数据失败");
                }

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null){
                        JSONArray dataObjs=obj.getJSONArray("data");
                        if(dataObjs!=null&&!dataObjs.isEmpty()){
                            hideLoad();
                            if(matchLeagueTableEntityList==null){
                                matchLeagueTableEntityList=new ArrayList<BallQMatchLeagueTableEntity>();
                            }else if(!matchLeagueTableEntityList.isEmpty()){
                                matchLeagueTableEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(dataObjs, matchLeagueTableEntityList, BallQMatchLeagueTableEntity.class);
                            if(adapter==null){
                                adapter=new BallQMatchLeagueTableAdapter(matchLeagueTableEntityList);
                                recyclerView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            recyclerView.setLoadMoreDataComplete("没有更多数据了...");
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

    @Override
    public void onRefresh() {
        requestDatas(matchEntity.getTourid(),type);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.rbAll){
            if(type==1){
                return;
            }else{
                type=1;
            }
        }else if(checkedId==R.id.rbHome){
            if(type==2){
                return;
            }else{
                type=2;
            }
        }else if(checkedId==R.id.rbAway){
            if(type==3){
                return;
            }else{
                type=3;
            }
        }
        if(adapter!=null){
            if(!matchLeagueTableEntityList.isEmpty()){
                matchLeagueTableEntityList.clear();
                adapter.notifyDataSetChanged();
            }
        }
        hideLoad();
        recyclerView.setLoadMoreDataComplete();
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        setRefreshing();
        requestDatas(matchEntity.getTourid(),type);
    }

    @OnClick(R.id.iv_filter)
    protected void onFilter(View view){
        ivFilter.setSelected(!ivFilter.isSelected());
        boolean isFilter=ivFilter.isSelected();
        if(isFilter){
            tvWin.setVisibility(View.GONE);
            tvDraw.setVisibility(View.GONE);
            tvLose.setVisibility(View.GONE);
            tvHaveConceded.setVisibility(View.GONE);
            tvGoalDifference.setVisibility(View.VISIBLE);
        }else{
            tvWin.setVisibility(View.VISIBLE);
            tvDraw.setVisibility(View.VISIBLE);
            tvLose.setVisibility(View.VISIBLE);
            tvHaveConceded.setVisibility(View.VISIBLE);
            tvGoalDifference.setVisibility(View.GONE);
        }
        if(adapter!=null){
            adapter.setFilter(isFilter);
        }
    }


}
