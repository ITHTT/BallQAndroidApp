package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchStatisticsEntity;
import com.tysci.ballq.modles.BallQMatchTextLiveEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.adapters.BallQMatchStatisticsInfoAdapter;
import com.tysci.ballq.views.adapters.BallQMatchTextLiveAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/22.
 */
public class BallQMatchTextLiveActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.iv_home_team_icon)
    protected ImageView ivHomeTeamIcon;
    @Bind(R.id.tv_home_team_name)
    protected TextView tvHomeTeamName;
    @Bind(R.id.tv_game_time)
    protected TextView tvGameTime;
    @Bind(R.id.tv_game_date)
    protected TextView tvGameDate;
    @Bind(R.id.iv_away_team_icon)
    protected ImageView ivAwayTeamIcon;
    @Bind(R.id.tv_away_team_name)
    protected TextView tvAwayTeamName;
    @Bind(R.id.tv_game_league_name)
    protected TextView tvGameLeagueName;
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.lv_text_live)
    protected ListView lvTextLive;
    @Bind(R.id.tv_game_left_name)
    protected TextView tvGameLeftName;
    @Bind(R.id.tv_game_right_name)
    protected TextView tvRightName;
    @Bind(R.id.lv_game_statistics)
    protected ListView lvMatchStatistics;

    private BallQMatchEntity matchEntity;
    private List<BallQMatchTextLiveEntity> ballQMatchTextLiveEntityList=null;
    private BallQMatchTextLiveAdapter matchTextLiveAdapter=null;

    private List<BallQMatchStatisticsEntity>ballQMatchStatisticsEntityList;
    private BallQMatchStatisticsInfoAdapter ballQMatchStatisticsInfoAdapter;

    private boolean isRunning=false;
    private Handler handler=new Handler();
    private Runnable task=new Runnable() {
        @Override
        public void run() {
            if(swipeRefresh!=null){
                if(swipeRefresh.isRefreshing()){
                    return;
                }
                isRunning=true;
                requestTextLiveInfo(String.valueOf(matchEntity.getEid()));
                handler.postDelayed(task, 20000);
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_match_text_live;
    }

    @Override
    protected void initViews() {
        setTitleText("文字直播");
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return findViewById(R.id.layout_content);
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
                    if(swipeRefresh!=null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }

    @Override
    protected void getIntentData(Intent intent) {
        matchEntity=intent.getParcelableExtra(Tag);
        if(matchEntity!=null){
            initMatchInfo(matchEntity);
            showLoading();
            requestTextLiveInfo(String.valueOf(matchEntity.getEid()));
            handler.postDelayed(task,2000);
        }

    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onRefresh() {
        if(!isRunning) {
            requestTextLiveInfo(String.valueOf(matchEntity.getEid()));
        }else{
            onRefreshCompelete();
        }
    }

    private void initMatchInfo(final BallQMatchEntity data){
        GlideImageLoader.loadImage(this, data.getHtlogo(), R.drawable.icon_default_team_logo, ivHomeTeamIcon);
        tvHomeTeamName.setText(data.getHtname());
        GlideImageLoader.loadImage(this, data.getAtlogo(), R.drawable.icon_default_team_logo, ivAwayTeamIcon);
        tvAwayTeamName.setText(data.getAtname());
        tvGameLeagueName.setText(data.getTourname());
        Date date= CommonUtils.getDateAndTimeFromGMT(data.getMtime());
        if(date!=null) {
            if (date.getTime() <= System.currentTimeMillis()) {
                // 已开始
                String gameState = BallQMatchStateUtil.getMatchState(data.getStatus(), data.getEtype());
                if (!TextUtils.isEmpty(gameState) && gameState.equals("未开始")) {
                    tvGameTime.setText(CommonUtils.getTimeOfDay(date));
                    tvGameDate.setText(CommonUtils.getMM_ddString(date));
                } else {
                    final String tmpScore = data.getHtscore() + " - " + data.getAtscore();
                    tvGameTime.setText(tmpScore);
                    tvGameDate.setText(gameState);
                }
            } else {
                tvGameTime.setText(CommonUtils.getTimeOfDay(date));
                tvGameDate.setText(CommonUtils.getMM_ddString(date));
            }
        }
        ivAwayTeamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BallQMatchTextLiveActivity.this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info", data);
                intent.putExtra("is_home_team", false);
                startActivity(intent);
            }
        });

        ivHomeTeamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BallQMatchTextLiveActivity.this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info", data);
                intent.putExtra("is_home_team", true);
                startActivity(intent);
            }
        });
    }

    private void requestTextLiveInfo(final String matchId){
        KLog.e("match_id",matchId);
        String url= HttpUrls.HOST_URL_V5+"match_live/";
        HashMap<String,String> params=new HashMap<>(1);
        params.put("match_id",matchId);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(matchTextLiveAdapter==null&&ballQMatchStatisticsInfoAdapter==null){
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            requestTextLiveInfo(matchId);
                        }
                    });
                }else{
                    ToastUtil.show(BallQMatchTextLiveActivity.this,"请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                onResponseSuccess(response);
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }

    private void onResponseSuccess(String response) {
        if (!TextUtils.isEmpty(response)) {
            JSONObject obj = JSONObject.parseObject(response);
            if (obj != null && !obj.isEmpty()) {
                int status = obj.getIntValue("status");
                if (status == 0) {
                    JSONObject dataObj = obj.getJSONObject("data");
                    if (dataObj != null && !dataObj.isEmpty()) {
                        JSONArray eventArray = dataObj.getJSONArray("events");
                        JSONArray statisticsArray = dataObj.getJSONArray("statistics");
                        if ((eventArray != null && !eventArray.isEmpty()) || (statisticsArray != null && !statisticsArray.isEmpty())) {
                            hideLoad();
                            if (eventArray != null && !eventArray.isEmpty()) {
                                if (ballQMatchTextLiveEntityList == null) {
                                    ballQMatchTextLiveEntityList = new ArrayList<>();
                                }
                                if (!ballQMatchTextLiveEntityList.isEmpty()) {
                                    ballQMatchTextLiveEntityList.clear();
                                }
                                KLog.e("获取数据...");
                                CommonUtils.getJSONListObject(eventArray, ballQMatchTextLiveEntityList, BallQMatchTextLiveEntity.class);
                                if (matchTextLiveAdapter == null) {
                                    matchTextLiveAdapter = new BallQMatchTextLiveAdapter(ballQMatchTextLiveEntityList);
                                    lvTextLive.setAdapter(matchTextLiveAdapter);
                                } else {
                                    matchTextLiveAdapter.notifyDataSetChanged();
                                }
                            }
                            if (statisticsArray != null && !statisticsArray.isEmpty()) {
                                if (ballQMatchStatisticsEntityList == null) {
                                    ballQMatchStatisticsEntityList = new ArrayList<>(10);
                                }
                                if(!ballQMatchStatisticsEntityList.isEmpty()){
                                    ballQMatchStatisticsEntityList.clear();
                                }
                                CommonUtils.getJSONListObject(statisticsArray,ballQMatchStatisticsEntityList,BallQMatchStatisticsEntity.class);
                                if (ballQMatchStatisticsInfoAdapter == null) {
                                    ballQMatchStatisticsInfoAdapter = new BallQMatchStatisticsInfoAdapter(ballQMatchStatisticsEntityList);
                                    lvMatchStatistics.setAdapter(ballQMatchStatisticsInfoAdapter);
                                } else {
                                    ballQMatchStatisticsInfoAdapter.notifyDataSetChanged();
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
        showEmptyInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(task);
    }
}
