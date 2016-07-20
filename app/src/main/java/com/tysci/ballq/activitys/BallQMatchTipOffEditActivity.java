package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/16.
 */
public class BallQMatchTipOffEditActivity extends BaseActivity {
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
    @Bind(R.id.et_tip_off)
    protected EditText etTipOff;

    private BallQMatchEntity matchEntity;
    private LoadingProgressDialog loadingProgressDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_match_tip_off_eidt;
    }

    @Override
    protected void initViews() {
        setTitle("爆料");
        setTitleRightAttributes();

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void showProgressDialog(String msg){
        if(loadingProgressDialog==null){
            loadingProgressDialog=new LoadingProgressDialog(this);
            loadingProgressDialog.setCanceledOnTouchOutside(false);
        }
        loadingProgressDialog.setMessage(msg);
        loadingProgressDialog.show();
    }

    private void dimssProgressDialog(){
        if(loadingProgressDialog!=null&&loadingProgressDialog.isShowing()){
            loadingProgressDialog.dismiss();
        }
    }

    public void setTitleRightAttributes(){
        TextView btnRight=titleBar.getRightMenuTextView();
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("发布");
        btnRight.setBackgroundResource(R.drawable.bt_ok_select_bg);
        btnRight.setGravity(Gravity.CENTER);
        btnRight.setWidth(CommonUtils.dip2px(this, 60));
        btnRight.setHeight(CommonUtils.dip2px(this, 30));
        btnRight.setTextColor(this.getResources().getColor(R.color.gold));
        btnRight.setTextSize(14);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTipOff();
            }
        });
    }

    @Override
    protected void getIntentData(Intent intent) {
        matchEntity=intent.getParcelableExtra(Tag);
        initMatchInfo(matchEntity);
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
                Intent intent = new Intent(BallQMatchTipOffEditActivity.this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info",data);
                intent.putExtra("is_home_team", false);
                startActivity(intent);
            }
        });

        ivHomeTeamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BallQMatchTipOffEditActivity.this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info", data);
                intent.putExtra("is_home_team", true);
                startActivity(intent);
            }
        });
    }

    private void publishTipOff(){
        String tipOff=etTipOff.getText().toString();
        if(TextUtils.isEmpty(tipOff)){
            ToastUtil.show(this, "请输入爆料内容");
            return;
        }
        String url= HttpUrls.HOST_URL_V5 + "match/" + matchEntity.getEid() + "/add_tip/";
        Map<String,String> params=new HashMap<String,String>(5);
        params.put("etype",String.valueOf(matchEntity.getEtype()));
        params.put("eid",String.valueOf(matchEntity.getEid()));
        params.put("cont",tipOff);
        params.put("user", UserInfoUtil.getUserId(this));
        params.put("token", UserInfoUtil.getUserToken(this));
        showProgressDialog("提交中...");
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
                ToastUtil.show(BallQMatchTipOffEditActivity.this,"请求失败");

            }

            @Override
            public void onSuccess(Call call, String response) {
                dimssProgressDialog();
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int status=obj.getIntValue("status");
                    }
                }

            }

            @Override
            public void onFinish(Call call) {

            }
        });


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
}
