package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQCircleNoteEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQHomeCircleNoteAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQHomeCircleListFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private List<BallQCircleNoteEntity> ballQCircleNoteEntityList;
    private BallQHomeCircleNoteAdapter adapter=null;

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        showLoading();
        requestDatas(currentPages,false);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void requestDatas(final int pages, final boolean isLoadMore){
        String url= HttpUrls.HOT_CIRCLE_LIST_URL+"?pageNo="+pages+"&pageSize=10";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }
            @Override
            public void onError(Call call, Exception error) {
                if(!isLoadMore){
                    if(adapter==null){
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
                }else{
                    recyclerView.setLoadMoreDataFailed();
                }
            }
            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!isLoadMore){
                    hideLoad();
                }
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONObject dataObj=obj.getJSONObject("dataMap");
                        if(dataObj!=null&&!dataObj.isEmpty()){
                            JSONArray jsonArray=dataObj.getJSONArray("hotTopics");
                            if(jsonArray!=null&&!jsonArray.isEmpty()){
                                if(ballQCircleNoteEntityList==null){
                                    ballQCircleNoteEntityList=new ArrayList<BallQCircleNoteEntity>(10);
                                }
                                if(!isLoadMore&&!ballQCircleNoteEntityList.isEmpty()){
                                    ballQCircleNoteEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(jsonArray,ballQCircleNoteEntityList,BallQCircleNoteEntity.class);
                                if(adapter==null){
                                    adapter=new BallQHomeCircleNoteAdapter(ballQCircleNoteEntityList);
                                    recyclerView.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                                if(jsonArray.size()<10){
                                    recyclerView.setLoadMoreDataComplete("没有更多数据了");
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
                if(isLoadMore){
                    recyclerView.setLoadMoreDataComplete("没有更多数据了...");
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
        requestDatas(currentPages,false);
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
