package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQBallWarpAdapter;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQHomeBallWarpListFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment implements CanScrollVerticallyDelegate, OnFlingOverListener {
    private List<BallQBallWarpInfoEntity> ballQBallWarpInfoEntityList;
    private BallQBallWarpAdapter adapter;

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        recyclerView.setBackgroundResource(R.color.white);
        requestDatas(currentPages, false);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void requestDatas(final int pages,final boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5+"articles/?p=" + pages;
        Map<String,String> params=null;
        if(UserInfoUtil.checkLogin(baseActivity)){
            params=new HashMap<String,String>(2);
            params.put("user", UserInfoUtil.getUserId(baseActivity));
            params.put("token", UserInfoUtil.getUserToken(baseActivity));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                
            }

            @Override
            public void onError(Call call, Exception error) {
                if(recyclerView!=null) {
                    if (!isLoadMore) {
                        if(adapter!=null) {
                            recyclerView.setStartLoadMore();
                        }else{
                            showErrorInfo(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showLoading();
                                    requestDatas(pages,isLoadMore);
                                }
                            });
                        }
                    } else {
                        recyclerView.setLoadMoreDataFailed();
                    }
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                if(!isLoadMore){
                    onRefreshCompelete();
                    hideLoad();
                }
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONArray objArray=obj.getJSONArray("data");
                        if(objArray!=null&&!objArray.isEmpty()){
                            if(ballQBallWarpInfoEntityList==null){
                                ballQBallWarpInfoEntityList=new ArrayList<BallQBallWarpInfoEntity>(10);
                            }
                            if(!isLoadMore&&!ballQBallWarpInfoEntityList.isEmpty()){
                                ballQBallWarpInfoEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(objArray,ballQBallWarpInfoEntityList,BallQBallWarpInfoEntity.class);
                            if(adapter==null){
                                adapter=new BallQBallWarpAdapter(ballQBallWarpInfoEntityList);
                                recyclerView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            if(objArray.size()<10){
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
            public void onFinish(Call call) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                    onRefreshCompelete();
                }
            }
        });
    }

    @Override
    protected void onLoadMoreData() {
        requestDatas(currentPages,true);

    }

    @Override
    protected void onRefreshData() {
        requestDatas(1,false);
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
