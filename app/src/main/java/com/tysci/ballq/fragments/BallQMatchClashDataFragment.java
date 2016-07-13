package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQMatchClashEntity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQMatchClashAdapter;
import com.tysci.ballq.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/8.
 * 对阵
 */
public class BallQMatchClashDataFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private BallQMatchEntity matchEntity;
    private List<BallQMatchClashEntity> matchClashEntityList;
    private BallQMatchClashAdapter adapter=null;

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        getDatas();
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    @Override
    protected boolean isCancledEventBus() {
        return false;
    }

    private void getDatas(){
        Bundle data=this.getArguments();
        if(data!=null){
            matchEntity=data.getParcelable("match_data");
            if(matchEntity!=null){
                showLoading();
                requestDatas(matchEntity.getEid(),false);
            }
        }
    }

    private void requestDatas(final int eid, final boolean isLoadMore){
        String url= HttpUrls.HOST_URL_V3 + "stats/match/" + eid + "/history/";
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag,url,30, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(adapter==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestDatas(eid,false);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null){
                        JSONObject dataObj=obj.getJSONObject("data");
                        if(dataObj!=null){
                            JSONArray allMettingArrays=dataObj.getJSONArray("all_meetings");
                            List<BallQMatchClashEntity>alls=null;
                            List<BallQMatchClashEntity>homes=null;
                            List<BallQMatchClashEntity>aways=null;
                            if(allMettingArrays!=null&&!allMettingArrays.isEmpty()){
                                alls=new ArrayList<BallQMatchClashEntity>(allMettingArrays.size());
                                CommonUtils.getJSONListObject(allMettingArrays, alls, BallQMatchClashEntity.class);
                                setMatchType("两对交锋",alls);
                            }
                            JSONObject lastObj=dataObj.getJSONObject("last_matches");
                            if(lastObj!=null){
                                JSONArray homeArrays=lastObj.getJSONArray("home_team");
                                if(homeArrays!=null&&!homeArrays.isEmpty()){
                                    homes=new ArrayList<BallQMatchClashEntity>(homeArrays.size());
                                    CommonUtils.getJSONListObject(homeArrays, homes, BallQMatchClashEntity.class);
                                    setMatchType(matchEntity.getHtname()+"近期比赛",homes);
                                }

                                JSONArray awayArrays=lastObj.getJSONArray("away_team");
                                if(awayArrays!=null&&!awayArrays.isEmpty()){
                                    aways=new ArrayList<BallQMatchClashEntity>(awayArrays.size());
                                    CommonUtils.getJSONListObject(awayArrays, aways, BallQMatchClashEntity.class);
                                    setMatchType(matchEntity.getAtname()+"近期比赛",aways);
                                }
                            }
                            if(alls!=null||homes!=null||aways!=null){
                                hideLoad();
                                if(matchClashEntityList==null){
                                    matchClashEntityList=new ArrayList<BallQMatchClashEntity>();
                                }
                                if(!matchClashEntityList.isEmpty()&&!isLoadMore){
                                    matchClashEntityList.clear();
                                }
                                if(alls!=null){
                                    matchClashEntityList.addAll(alls);
                                }
                                if(homes!=null){
                                    matchClashEntityList.addAll(homes);
                                }
                                if(aways!=null){
                                    matchClashEntityList.addAll(aways);
                                }

                                if(adapter==null){
                                    adapter=new BallQMatchClashAdapter(matchClashEntityList);
                                    StickyHeaderDecoration decoration=new StickyHeaderDecoration(adapter);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.addItemDecoration(decoration);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                                recyclerView.setLoadMoreDataComplete("没有更多数据了...");
                                return;
                            }
                        }
                    }
                }
                if(adapter==null){
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {
                if(!isLoadMore){
                    if(recyclerView!=null) {
                        recyclerView.setRefreshComplete();
                    }
                    onRefreshCompelete();
                }
            }
        });
    }

    private void setMatchType(String type,List<BallQMatchClashEntity>datas){
        int size=datas.size();
        for(int i=0;i<size;i++){
            datas.get(i).setMatchType(type);
        }
    }

    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {
        requestDatas(matchEntity.getEid(),false);

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
