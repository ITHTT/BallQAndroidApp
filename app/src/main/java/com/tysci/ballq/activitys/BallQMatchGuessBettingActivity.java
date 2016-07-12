package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQMatchGuessBettingEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQMatchGuessBettingInfoAdapter;
import com.tysci.ballq.views.dialogs.BallQMatchBettingGuessDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/16.
 */
public class BallQMatchGuessBettingActivity extends BaseActivity implements BallQMatchGuessBettingInfoAdapter.OnBettingItemListener,
        BallQMatchGuessBettingInfoAdapter.OnDeleteBettingRecordListener,BallQMatchBettingGuessDialog.OnBettingClickListener{
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
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @Bind(R.id.bt_betting)
    protected Button btBetting;

    private BallQMatchEntity matchEntity=null;
    private List<BallQMatchGuessBettingEntity> matchGuessBettingEntityList=null;
    private BallQMatchGuessBettingInfoAdapter adapter=null;

    private BallQMatchBettingGuessDialog bettingDialog=null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_match_guess_betting;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected View getLoadingTargetView() {
        return this.findViewById(R.id.layout_match_guess_content);
    }

    @Override
    protected void getIntentData(Intent intent) {
        matchEntity=intent.getParcelableExtra(Tag);
        if(matchEntity!=null){
            initMatchInfo(matchEntity);
            showLoading();
            getMatchGuessInfo(matchEntity.getEid(),matchEntity.getEtype());
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

    private void initMatchInfo(BallQMatchEntity data){
        GlideImageLoader.loadImage(this, data.getHtlogo(), R.drawable.icon_default_team_logo, ivHomeTeamIcon);
        tvHomeTeamName.setText(data.getHtname());
        GlideImageLoader.loadImage(this, data.getAtlogo(), R.drawable.icon_default_team_logo, ivAwayTeamIcon);
        tvAwayTeamName.setText(data.getAtname());
        tvGameLeagueName.setText(data.getTourname());
    }

    private void getMatchGuessInfo(int eid,int etype){
        String url= HttpUrls.HOST_URL_V5 + "match/" + eid + "/odds_info/?etype=" + etype;
        HashMap<String,String> params=new HashMap<>(2);
        if (UserInfoUtil.checkLogin(this)) {
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }

        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                showErrorInfo(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading();
                        getMatchGuessInfo(matchEntity.getEid(), matchEntity.getEtype());
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null) {
                        JSONArray objArrays = obj.getJSONArray("data");
                        if (objArrays != null && !objArrays.isEmpty()) {
                            hideLoad();
                            if (matchGuessBettingEntityList == null) {
                                matchGuessBettingEntityList = new ArrayList<BallQMatchGuessBettingEntity>(3);
                            }
                            CommonUtils.getJSONListObject(objArrays, matchGuessBettingEntityList, BallQMatchGuessBettingEntity.class);
                            if (adapter == null) {
                                adapter = new BallQMatchGuessBettingInfoAdapter(matchGuessBettingEntityList);
                                adapter.setOnBettingItemListener(BallQMatchGuessBettingActivity.this);
                                adapter.setDividerPosition(objArrays.size());
                                adapter.setOnDeleteBettingRecordListener(BallQMatchGuessBettingActivity.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            return;
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private boolean checkBettingInfo(String bettingInfo){
        if(adapter!=null){
            int position=adapter.getDividerPosition();
            if(position==-1){
                return true;
            }
            int size= matchGuessBettingEntityList.size();
            for(int i=position;i<size;i++){
                if(bettingInfo.equals(matchGuessBettingEntityList.get(i).getBettingInfo())){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBettingItem(int position, String bettingInfo, BallQMatchGuessBettingEntity info, String bettingType) {
        boolean isBetting=checkBettingInfo(bettingInfo);
        if(isBetting) {
            if (bettingDialog == null) {
                bettingDialog = new BallQMatchBettingGuessDialog(this);
                bettingDialog.setBallQData(matchEntity);
                bettingDialog.setOnBettingClickListener(this);
            }
            bettingDialog.reset();
            KLog.e("BettingType:" + bettingType);
            bettingDialog.setBettingInfo(bettingInfo);
            //bettingDialog.setBettingType(type);
            bettingDialog.setBettingInfoType(info, bettingType);
            bettingDialog.show();
        }else{
            ToastUtil.show(this, "该类型投注已被选择,请选择其他类型");
        }
    }

    @Override
    public void onDeleteBettingRecord(int position, int moneys) {
        bettingDialog.addAllBettingMoneys(moneys);
        int dividerPosition=adapter.getDividerPosition();
        if(dividerPosition==matchGuessBettingEntityList.size()){
            if(btBetting.getVisibility()!=View.GONE){
                btBetting.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBettingClick(BallQMatchGuessBettingEntity info) {
        if(btBetting.getVisibility()!= View.VISIBLE){
            btBetting.setVisibility(View.VISIBLE);
        }
        matchGuessBettingEntityList.add(info);
        adapter.notifyDataSetChanged();
    }
}
