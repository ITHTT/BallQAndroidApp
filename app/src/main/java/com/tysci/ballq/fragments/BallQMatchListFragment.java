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
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchAdapter;

import org.greenrobot.eventbus.EventBus;

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
    private String filter="";
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
                if (matchAdapter == null) {
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestDatas();
                        }
                    });
                } else {
                    ToastUtil.show(baseActivity, "请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                hideLoad();
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null) {
                        JSONObject data = obj.getJSONObject("data");
                        if (data != null) {
                            JSONArray array = data.getJSONArray("matches");
                            if (array != null && !array.isEmpty()) {
                                if (matchEntityList == null) {
                                    matchEntityList = new ArrayList<BallQMatchEntity>(10);
                                } else if (!matchEntityList.isEmpty()) {
                                    matchEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(array, matchEntityList, BallQMatchEntity.class);
                                if (matchAdapter == null) {
                                    matchAdapter = new BallQMatchAdapter(matchEntityList);
                                    matchAdapter.setTag(Tag);
                                    matchAdapter.setMatchType(0);
                                    recyclerView.setAdapter(matchAdapter);
                                } else {
                                    matchAdapter.notifyDataSetChanged();
                                }
                                recyclerView.setLoadMoreDataComplete(loadFinishedTip);
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

    /**
     * 刷选数据
     * @param filter
     */
    public void filterUpdateData(String filter,String selectDate){
        boolean isUpdate=false;
        if(!"tournament".equals(filter)) {
            if (TextUtils.isEmpty(filter)) {
                if (!TextUtils.isEmpty(this.filter)) {
                    this.filter = null;
                    isUpdate = true;
                }
            } else if (!filter.equals(this.filter)) {
                this.filter = filter;
                isUpdate = true;
            }
        }else if(!filter.equals(this.filter)){
            this.filter=null;
            isUpdate=true;
        }
        if(!this.selectDate.equals(selectDate)){
            this.selectDate=selectDate;
            isUpdate=true;
        }

        if(isUpdate) {
            HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
            recyclerView.setLoadMoreDataComplete();
            if(matchAdapter!=null){
                if(matchEntityList.size()>0){
                    matchEntityList.clear();
                    matchAdapter.notifyDataSetChanged();
                }
            }
            hideLoad();
            setRefreshing();
            requestDatas();
        }
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
        if(!TextUtils.isEmpty(action)){
            if(action.equals("attention_refresh")){
                int etype=data.getInt("etype",-1);
                int eid=data.getInt("id",-1);
                if(etype>=0){
                    if(etype==type){
                        if(matchAdapter!=null){
                            matchAdapter.cancelUserAttention(eid);
                        }
                    }
                }
            }else if(action.equals("match_league_filter")){
                int etype=data.getInt("etype",-1);
                List<String> leagueIdList=data.getStringArrayList("league_ids");
                if(etype==this.type) {
                    if (leagueIdList != null) {
                        String league_Ids = getLeagueIdsString(leagueIdList);
                        if (!TextUtils.isEmpty(league_Ids)) {
                            EventBus.getDefault().post("reset_tip_off");
                            this.filter = "tournament";
                            this.leagueIds = league_Ids;
                            HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
                            recyclerView.setLoadMoreDataComplete();
                            if (matchAdapter != null) {
                                if (matchEntityList.size() > 0) {
                                    matchEntityList.clear();
                                    matchAdapter.notifyDataSetChanged();
                                }
                            }
                            hideLoad();
                            setRefreshing();
                            requestDatas();
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取选则的赛事ID
     * @param ids
     * @return
     */
    private String getLeagueIdsString(List<String>ids){
        int size=ids.size();
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<size;i++){
            if(i==0){
                stringBuilder.append(ids.get(i));
            }else{
                stringBuilder.append(',');
                stringBuilder.append(ids.get(i));
            }
        }
        return stringBuilder.toString();
    }

}
