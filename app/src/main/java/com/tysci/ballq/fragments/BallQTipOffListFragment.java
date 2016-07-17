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
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQTipOffAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQTipOffListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener,CanScrollVerticallyDelegate, OnFlingOverListener {
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;

    private int etype=-1;
    private int currentPages=1;

    private BallQTipOffAdapter adapter=null;
    private List<BallQTipOffEntity> ballQTipOffEntityList=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_ballq_home_tip_off_list;
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

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setOnLoadMoreListener(this);
        recyclerView.setBackgroundResource(R.color.white);
       // recyclerView.setAdapter(new BallQHomeTipOffAdapter());
        showLoading();
        requestDatas(currentPages,false);
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
                       // recyclerView.setStartLoadMore();
                    }
                }
            }, 1000);
        }
    }


    private void requestDatas(int pages, final boolean isLoadMore){
        String url= HttpUrls.TIP_OFF_LIST_URL+etype+"&p="+pages;
        HashMap<String,String>params=null;
        if(UserInfoUtil.checkLogin(baseActivity)){
            params=new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(!isLoadMore){
                    if(adapter!=null) {
                       recyclerView.setStartLoadMore();
                    }else{
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestDatas(1,false);
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
                hideLoad();
                onResponseSuccess(response,isLoadMore);
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    if(recyclerView!=null){
                        recyclerView.setRefreshComplete();
                    }
                    onRefreshCompelete();
                }else{
                    recyclerView.setLoadMoreDataFailed();
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
                    if(ballQTipOffEntityList==null){
                        ballQTipOffEntityList=new ArrayList<>(10);
                    }
                    if(!isLoadMore&&ballQTipOffEntityList.size()>0){
                        ballQTipOffEntityList.clear();
                    }
                    CommonUtils.getJSONListObject(objArrays,ballQTipOffEntityList,BallQTipOffEntity.class);
                    if(adapter==null){
                        adapter=new BallQTipOffAdapter(ballQTipOffEntityList);
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
        }
    }

    @Override
    public void onLoadMore() {
        if(recyclerView.isRefreshing()){
            recyclerView.setLoadMoreDataComplete("刷新数据中...");
        }else{
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestDatas(currentPages,true);
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
            requestDatas(1,false);
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return recyclerView != null && recyclerView.canScrollVertically(direction);
    }

    @Override
    public void onFlingOver(int y, long duration) {
        if (recyclerView != null) {
            recyclerView.smoothScrollBy(0, y);
        }
    }
}
