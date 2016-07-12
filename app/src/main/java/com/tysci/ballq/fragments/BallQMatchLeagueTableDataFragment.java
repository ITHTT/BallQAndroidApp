package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

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
import com.tysci.ballq.views.adapters.BallQMatchLeagueTableAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/8.
 * 积分榜
 */
public class BallQMatchLeagueTableDataFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    private BallQMatchEntity matchEntity;
    private int type=1;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

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
        getDatas();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void getDatas(){
        Bundle data=getArguments();
        if(data!=null){
           matchEntity= data.getParcelable("match_data");
            if(matchEntity!=null){
                requestDatas(matchEntity.getTourid(),type);
            }
        }
    }

    private void requestDatas(int tournamentId,int type){
        String url= HttpUrls.HOST_URL_V3 + "stats/tournament/" + tournamentId + (type == 1 ? "/total/" : (type == 2 ? "/home/" : "/away/"));
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null){
                        JSONArray dataObjs=obj.getJSONArray("data");
                        if(dataObjs!=null&&!dataObjs.isEmpty()){
                            if(matchLeagueTableEntityList==null){
                                matchLeagueTableEntityList=new ArrayList<BallQMatchLeagueTableEntity>();
                            }else if(!matchLeagueTableEntityList.isEmpty()){
                                matchLeagueTableEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(dataObjs,matchLeagueTableEntityList,BallQMatchLeagueTableEntity.class);
                            if(adapter==null){
                                adapter=new BallQMatchLeagueTableAdapter(matchLeagueTableEntityList);
                                recyclerView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }


}
