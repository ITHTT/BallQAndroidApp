package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;

import java.util.Date;

import butterknife.Bind;

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

    private BallQMatchEntity matchEntity;

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
                intent.putExtra("match_info",data);
                intent.putExtra("is_home_team", true);
                startActivity(intent);
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
