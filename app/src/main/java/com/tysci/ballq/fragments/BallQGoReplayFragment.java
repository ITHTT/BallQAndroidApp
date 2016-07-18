package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQGoReplayEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQGoReplayAdapter;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/7/15.
 */
public class BallQGoReplayFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private List<BallQGoReplayEntity> replayEntityList;
    private BallQGoReplayAdapter adapter=null;


    @Override
    protected void onLoadMoreData() {
        requestBallQGoReplayDatas(currentPages,true);
    }

    @Override
    protected void onRefreshData() {
        requestBallQGoReplayDatas(1,false);
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        recyclerView.setBackgroundResource(R.color.gray);
        showLoading();
        requestBallQGoReplayDatas(1,false);
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
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

    private void requestBallQGoReplayDatas(final int pages,final boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5+"go/replay/" + "?p="+pages;
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                if(recyclerView!=null) {
                    if (!isLoadMore) {
                        if(adapter!=null) {
                            recyclerView.setStartLoadMore();
                        }else{
                            showErrorInfo(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showLoading();
                                    requestBallQGoReplayDatas(pages,isLoadMore);
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
                KLog.json(response);
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("status");
                        if(status==0){
                            JSONArray dataArray=obj.getJSONArray("data");
                            if(dataArray!=null&&!dataArray.isEmpty()){
                                hideLoad();
                                if(replayEntityList==null) {
                                    replayEntityList = new ArrayList<BallQGoReplayEntity>(10);
                                }
                                if(!isLoadMore){
                                    if(!replayEntityList.isEmpty()){
                                        replayEntityList.clear();
                                    }
                                }
                                setBallQGoReplayDatas(dataArray,replayEntityList);
                                if(adapter==null){
                                    adapter=new BallQGoReplayAdapter(replayEntityList);
                                    recyclerView.setAdapter(adapter);
                                    StickyHeaderDecoration decoration=new StickyHeaderDecoration(adapter);
                                    recyclerView.addItemDecoration(decoration);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                                if(dataArray.size()<10){
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
                if(isLoadMore){
                    recyclerView.setLoadMoreDataComplete("没有更多数据");
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

    public void setBallQGoReplayDatas(JSONArray datas,List<BallQGoReplayEntity>replayEntityList){
        if(datas!=null){
            int size=datas.size();
            for(int i=0;i<size;i++){
                BallQGoReplayEntity info=datas.getObject(i,BallQGoReplayEntity.class);
                Date date= CommonUtils.getDateAndTimeFromGMT(info.getMatch_time());
                if(date!=null){
                    info.setMatchTime(CommonUtils.getDateAndTimeFormatString(date));
                }
                replayEntityList.add(info);
            }
        }
    }
}
