package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.base.BaseFragment;
import com.tysci.ballq.fragments.BallQMatchBettingScaleDataFragment;
import com.tysci.ballq.fragments.BallQMatchClashDataFragment;
import com.tysci.ballq.fragments.BallQMatchForecastDataFragment;
import com.tysci.ballq.fragments.BallQMatchLeagueTableDataFragment;
import com.tysci.ballq.fragments.BallQMatchLineupDataFragment;
import com.tysci.ballq.fragments.BallQMatchTipOffListFragment;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.views.adapters.BallQFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/7.
 */
public class BallQMatchDetailActivity extends BaseActivity {
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

    @Bind(R.id.tab_layout)
    protected TabLayout tabLayout;
    @Bind(R.id.view_pager)
    protected ViewPager viewPager;
    private String[] titles={"爆料","预测","比例","对阵","阵容","积分榜"};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_match_detail;
    }

    @Override
    protected void initViews() {
        setTitle("比赛详情");
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void addFragments(BallQMatchEntity matchEntity){
        List<BaseFragment> fragments=new ArrayList<>(6);

        Bundle data=new Bundle();
        data.putParcelable("match_data",matchEntity);

        BaseFragment baseFragment=new BallQMatchTipOffListFragment();
        baseFragment.setArguments(data);
        fragments.add(baseFragment);

        if(matchEntity.getEtype()==0) {
            baseFragment=new BallQMatchForecastDataFragment();
            baseFragment.setArguments(data);
            fragments.add(baseFragment);

            baseFragment=new BallQMatchBettingScaleDataFragment();
            baseFragment.setArguments(data);
            fragments.add(baseFragment);

            baseFragment=new BallQMatchClashDataFragment();
            baseFragment.setArguments(data);
            fragments.add(baseFragment);

            baseFragment=new BallQMatchLineupDataFragment();
            baseFragment.setArguments(data);
            fragments.add(baseFragment);

            baseFragment=new BallQMatchLeagueTableDataFragment();
            baseFragment.setArguments(data);
            fragments.add(baseFragment);
        }

        BallQFragmentPagerAdapter adapter=new BallQFragmentPagerAdapter(getSupportFragmentManager(),titles,fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void getIntentData(Intent intent) {
        BallQMatchEntity data=intent.getParcelableExtra(Tag);
        if(data!=null){
            if(!TextUtils.isEmpty(data.getTourname())) {
                initMatchInfo(data);
                addFragments(data);
            }else {
                getMatchDetailInfo(data.getEid(), data.getEtype());
            }
        }
    }

    private void getMatchDetailInfo(int matchId,int etype){
        String url= HttpUrls.HOST_URL_V5 + "match/" + matchId + "/?etype=" + etype;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()&&obj.getIntValue("status")==0){
                        BallQMatchEntity info=obj.getObject("data",BallQMatchEntity.class);
                        if(info!=null){
                            KLog.e("显示数据。。。");
                            initMatchInfo(info);
                            addFragments(info);
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
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
                Intent intent = new Intent(BallQMatchDetailActivity.this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info",data);
                intent.putExtra("is_home_team", false);
                startActivity(intent);
            }
        });

        ivHomeTeamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BallQMatchDetailActivity.this, BallQMatchTeamTipOffHistoryActivity.class);
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
