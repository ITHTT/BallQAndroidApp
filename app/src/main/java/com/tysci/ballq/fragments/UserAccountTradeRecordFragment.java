package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQUserAccountRecordEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserAccountRecordAdapter;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/24.
 */
public class UserAccountTradeRecordFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private List<BallQUserAccountRecordEntity> recordEntityList;
    private BallQUserAccountRecordAdapter adapter=null;
    private String loadFinishedTip="没有更多数据了...";

    @Override
    protected void onLoadMoreData() {
        requestUserAccountTradeRecordInfos(currentPages,true);

    }

    @Override
    protected void onRefreshData() {
        requestUserAccountTradeRecordInfos(1,false);
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        showLoading();
        requestUserAccountTradeRecordInfos(1, false);
    }

    private void requestUserAccountTradeRecordInfos(final int pages, final boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V5 + "user/transaction/rmb/?p=" + pages;
        HashMap<String,String> params=new HashMap<>(2);
        if (UserInfoUtil.checkLogin(baseActivity)) {
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
                    recyclerView.setRefreshComplete();
                }
                if(!isLoadMore){
                    if(adapter!=null) {
                        recyclerView.setStartLoadMore();
                    }else{
                        showErrorInfo(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLoading();
                                requestUserAccountTradeRecordInfos(pages,isLoadMore);
                            }
                        });
                    }
                }else{
                    recyclerView.setLoadMoreDataFailed();
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        JSONArray datas=obj.getJSONArray("data");
                        if(datas!=null&&!datas.isEmpty()){
                            if(!isLoadMore){
                                hideLoad();
                            }
                            if(recordEntityList==null){
                                recordEntityList=new ArrayList<BallQUserAccountRecordEntity>(10);
                            }
                            if(!isLoadMore){
                                if(!recordEntityList.isEmpty()){
                                    recordEntityList.clear();
                                }
                            }
                            CommonUtils.getJSONListObject(datas,recordEntityList,BallQUserAccountRecordEntity.class);
                            setTime(recordEntityList);
                            if(adapter==null){
                                adapter=new BallQUserAccountRecordAdapter(recordEntityList);
                                StickyHeaderDecoration decoration=new StickyHeaderDecoration(adapter);
                                recyclerView.setAdapter(adapter);
                                recyclerView.addItemDecoration(decoration);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            if(datas.size()<10){
                                recyclerView.setLoadMoreDataComplete(loadFinishedTip);
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
                if(isLoadMore){
                    recyclerView.setLoadMoreDataComplete(loadFinishedTip);
                }else if(adapter==null) {
                    showEmptyInfo();
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

    private void setTime(List<BallQUserAccountRecordEntity>datas){
        if(datas!=null&&!datas.isEmpty()){
            for(BallQUserAccountRecordEntity info:datas){
                Date date= CommonUtils.getDateAndTimeFromGMT(info.getCtime());
                if(date!=null){
                    String dateStr= CommonUtils.getMM_ddString(date);
                   // KLog.e("dateStr:"+dateStr);
                    String time= CommonUtils.getTimeOfDay(date);
                    info.setTime(time);
                    info.setDate(dateStr);
                }
            }
        }
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
}
