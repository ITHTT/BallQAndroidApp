package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQMatchListFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private int type;
    private String selectDate;
    /**过滤参数*/
    private String filter;
    /**联赛ID*/
    private String leagueIds=null;

    private List<BallQMatchEntity> matchEntityList=null;
    private BallQMatchAdapter matchAdapter=null;
    private String loadFinishedTip="没有更多数据了...";

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        showLoading();
        requestDatas();
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setLeagueIds(String leagueIds) {
        this.leagueIds = leagueIds;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {
        requestDatas();
    }

    private void requestDatas(){
        String url= HttpUrls.HOST_URL_V5 + "matches/?etype="+type+"&dt="+selectDate;
        if(!TextUtils.isEmpty(filter)){
            url=url+"&filter="+filter;
        }
        if(!TextUtils.isEmpty(leagueIds)){
            url=url+"&tournament_id="+leagueIds;
        }
        KLog.e("url:" + url);
        Map<String,String>params=null;
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
                if(matchAdapter==null){
                    showErrorInfo(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestDatas();
                        }
                    });
                }else{

                }
            }
            @Override
            public void onSuccess(Call call, String response) {
                hideLoad();
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null){
                        JSONObject data=obj.getJSONObject("data");
                        if(data!=null){
                            JSONArray array=data.getJSONArray("matches");
                            if(array!=null&&!array.isEmpty()){
                                if(matchEntityList==null){
                                    matchEntityList=new ArrayList<BallQMatchEntity>(10);
                                }else if(!matchEntityList.isEmpty()){
                                    matchEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(array, matchEntityList, BallQMatchEntity.class);
                                if(matchAdapter==null){
                                    matchAdapter=new BallQMatchAdapter(matchEntityList);
                                    recyclerView.setAdapter(matchAdapter);
                                }else{
                                    matchAdapter.notifyDataSetChanged();
                                }
                                recyclerView.setLoadMoreDataComplete(loadFinishedTip);
                                return;
                            }
                        }
                    }
                }
            }
            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
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
