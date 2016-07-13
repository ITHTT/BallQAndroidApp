package com.tysci.ballq.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.base.AppSwipeRefreshLoadMoreRecyclerViewFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchLineupEntity;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQMatchLineupAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/8.
 * 阵容
 */
public class BallQMatchLineupDataFragment extends AppSwipeRefreshLoadMoreRecyclerViewFragment{
    private BallQMatchEntity matchEntity;

    private List<BallQMatchLineupEntity> matchLineupEntityList=null;
    private BallQMatchLineupAdapter adapter=null;

    @Override
    protected void onLoadMoreData() {

    }

    @Override
    protected void onRefreshData() {
        requestDatas(matchEntity.getEid());
    }

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
        Bundle data=getArguments();
        if(data!=null){
            matchEntity=data.getParcelable("match_data");
            if(matchEntity!=null){
                requestDatas(matchEntity.getEid());
            }
        }
    }

    private void requestDatas(final int matchId){
        String url= HttpUrls.HOST_URL_V3 + "stats/match/" + matchId + "/lineups/";
        KLog.e("url:" + url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack() {
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
                            requestDatas(matchId);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.e(response);
                KLog.e("加载数据成功...");
                if(!TextUtils.isEmpty(response)){
                    KLog.e("获取数据JSON...");
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&obj.getIntValue("status")==0){
                        JSONObject dataObj=obj.getJSONObject("data");
                        if(dataObj!=null&&!dataObj.isEmpty()){
                            KLog.e("dataObject...");
                            BallQMatchLineupEntity homeLineupMap=null;
                            List<BallQMatchLineupEntity> homePlayers=null;
                            List<BallQMatchLineupEntity> homeSubstitutes = null;

                            JSONObject homeTeamObj=dataObj.getJSONObject("home_team_players");
                            if(homeTeamObj!=null&&!homeTeamObj.isEmpty()){
                                KLog.e("dataHomeTeam...");
                                String homeTeamFormation=homeTeamObj.getString("formation");
                                KLog.e("homeTeamFormation:" + homeTeamFormation);
                                JSONObject homeLineupObj=homeTeamObj.getJSONObject("lineup");
                                if(!TextUtils.isEmpty(homeTeamFormation)&&homeLineupObj!=null){
                                    KLog.e("homeLineupObj...");
                                    JSONArray homeMaps=homeLineupObj.getJSONArray("map");
                                    if(homeMaps!=null&&!homeMaps.isEmpty()){
                                        KLog.e("homeMaps:" + homeMaps.size());
                                        homeLineupMap=new BallQMatchLineupEntity();
                                        List<Integer> homeMapList=new ArrayList<Integer>(homeMaps.size());
                                        CommonUtils.getJSONListObject(homeMaps, homeMapList, Integer.class);
                                        homeLineupMap.setMap(homeMapList);
                                        homeLineupMap.setLineupFormation(matchEntity.getHtname()+"阵型:"+homeTeamFormation);
                                    }

                                    JSONArray homePlayerArrays=homeLineupObj.getJSONArray("players");
                                    if(homePlayerArrays!=null&&!homePlayerArrays.isEmpty()){
                                        KLog.e("homePlayers:" + homePlayerArrays.size());
                                        homePlayers=new ArrayList<BallQMatchLineupEntity>(homePlayerArrays.size());
                                        CommonUtils.getJSONListObject(homePlayerArrays, homePlayers, BallQMatchLineupEntity.class);
                                    }
                                }
                                JSONArray substituteArrays=homeTeamObj.getJSONArray("substitute");
                                KLog.e("substituteArrays....");
                                if(substituteArrays!=null&&!substituteArrays.isEmpty()){
                                    KLog.e("homesubstibutes:" + substituteArrays.size());
                                    homeSubstitutes=new ArrayList<BallQMatchLineupEntity>(substituteArrays.size());
                                    CommonUtils.getJSONListObject(substituteArrays, homeSubstitutes, BallQMatchLineupEntity.class);
                                }
                            }

                            BallQMatchLineupEntity awayLineupMap=null;
                            List<BallQMatchLineupEntity> awayPlayers=null;
                            List<BallQMatchLineupEntity> awaySubstitutes = null;

                            JSONObject awayTeamObj=dataObj.getJSONObject("away_team_players");
                            if(homeTeamObj!=null){
                                String awayTeamFormation=awayTeamObj.getString("formation");
                                JSONObject awayLineupObj=awayTeamObj.getJSONObject("lineup");
                                if(!TextUtils.isEmpty(awayTeamFormation)&&awayLineupObj!=null){
                                    JSONArray awayMaps=awayLineupObj.getJSONArray("map");
                                    if(awayMaps!=null&&!awayMaps.isEmpty()){
                                        awayLineupMap=new BallQMatchLineupEntity();
                                        List<Integer> awayMapList=new ArrayList<Integer>(awayMaps.size());
                                        CommonUtils.getJSONListObject(awayMaps, awayMapList, Integer.class);
                                        awayLineupMap.setMap(awayMapList);
                                        awayLineupMap.setLineupFormation(matchEntity.getAtname()+"阵型:"+awayTeamFormation);
                                    }

                                    JSONArray awayPlayerArrays=awayLineupObj.getJSONArray("players");
                                    if(awayPlayerArrays!=null&&!awayPlayerArrays.isEmpty()){
                                        awayPlayers=new ArrayList<BallQMatchLineupEntity>(awayPlayerArrays.size());
                                        CommonUtils.getJSONListObject(awayPlayerArrays, awayPlayers, BallQMatchLineupEntity.class);
                                    }
                                }
                                JSONArray substituteArrays=awayTeamObj.getJSONArray("substitute");
                                if(substituteArrays!=null&&!substituteArrays.isEmpty()){
                                    awaySubstitutes=new ArrayList<BallQMatchLineupEntity>(substituteArrays.size());
                                    CommonUtils.getJSONListObject(substituteArrays, awaySubstitutes, BallQMatchLineupEntity.class);
                                }
                            }

                            if(homeLineupMap!=null||homePlayers!=null||homeSubstitutes!=null||awayLineupMap!=null
                                    ||awayPlayers!=null||awaySubstitutes!=null){
                                if(matchLineupEntityList==null){
                                    matchLineupEntityList=new ArrayList<BallQMatchLineupEntity>(10);
                                }else{
                                    if(!matchLineupEntityList.isEmpty()){
                                        matchLineupEntityList.clear();
                                    }
                                }

                                if(homeLineupMap!=null){
                                    matchLineupEntityList.add(homeLineupMap);
                                }

                                if(homePlayers!=null){
                                    BallQMatchLineupEntity data=new BallQMatchLineupEntity();
                                    data.setLineupTitle(matchEntity.getHtname()+"首发阵容");
                                    matchLineupEntityList.add(data);
                                    matchLineupEntityList.addAll(homePlayers);
                                }

                                if(homeSubstitutes!=null){
                                    BallQMatchLineupEntity data=new BallQMatchLineupEntity();
                                    data.setLineupTitle(matchEntity.getHtname()+"替补阵容");
                                    matchLineupEntityList.add(data);
                                    matchLineupEntityList.addAll(homeSubstitutes);
                                }

                                if(awayLineupMap!=null){
                                    matchLineupEntityList.add(awayLineupMap);
                                }

                                if(awayPlayers!=null){
                                    BallQMatchLineupEntity data=new BallQMatchLineupEntity();
                                    data.setLineupTitle(matchEntity.getAtname()+"首发阵容");
                                    matchLineupEntityList.add(data);
                                    matchLineupEntityList.addAll(awayPlayers);
                                }

                                if(awaySubstitutes!=null){
                                    BallQMatchLineupEntity data=new BallQMatchLineupEntity();
                                    data.setLineupTitle(matchEntity.getAtname()+"替补阵容");
                                    matchLineupEntityList.add(data);
                                    matchLineupEntityList.addAll(awaySubstitutes);
                                }

                                KLog.e("Size:" + matchLineupEntityList.size());

                                if(adapter==null){
                                    adapter=new BallQMatchLineupAdapter(matchLineupEntityList);
                                    recyclerView.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                                recyclerView.setLoadMoreDataComplete("已没有更多数据了...");
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
                onRefreshCompelete();
            }
        });
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
