package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQMatchEntity;
import com.tysci.ballq.modles.BallQTipOffEntity;
import com.tysci.ballq.modles.BallQUserCommentEntity;
import com.tysci.ballq.modles.BallQUserRewardHeaderEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.BallQMatchStateUtil;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.MatchBettingInfoUtil;
import com.tysci.ballq.utils.SoftInputUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.adapters.BallQUserCommentAdapter;
import com.tysci.ballq.views.adapters.BallQUserRewardHeaderAdapter;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.dialogs.ShareDialog;
import com.tysci.ballq.views.interfaces.OnLongClickUserHeaderListener;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.CustomRattingBar;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/6.
 */
public class BallQTipOffDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener
        , OnLongClickUserHeaderListener {
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.editText)
    protected EditText etComment;
    @Bind(R.id.ivLike)
    protected ImageView ivLike;
    @Bind(R.id.tvGuess)
    protected TextView tvGuess;
    @Bind(R.id.btnPublish)
    protected Button btPublish;
    private ImageView ivAttention;

    private View headerView;
    private BallQTipOffEntity tipOffInfo = null;
    private int currentPages = 1;

    private List<BallQUserRewardHeaderEntity> userRewardHeaderEntityList = null;
    private BallQUserRewardHeaderAdapter userRewardHeaderAdapter = null;

    private List<BallQUserCommentEntity> userCommentEntityList;
    private BallQUserCommentAdapter userCommentAdapter = null;

    private String replyerName;
    private String replyerId;

    private String cacheCommentInfo = "";

    private LoadingProgressDialog loadingProgressDialog = null;

    private ShareDialog shareDialog = null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_tip_off_detail;
    }

    @Override
    protected void initViews() {
        setTitle("爆料详情");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
        titleBar.setRightMenuIcon(R.mipmap.icon_share_gold, this);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_ballq_tip_off_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.addHeaderView(headerView);

        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivLike.setVisibility(View.GONE);
                    tvGuess.setVisibility(View.GONE);
                    btPublish.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(cacheCommentInfo)) {
                        etComment.setText(cacheCommentInfo);
                        etComment.setSelection(cacheCommentInfo.length());
                    }
                } else {
                    etComment.setText("");
                    ivLike.setVisibility(View.VISIBLE);
                    tvGuess.setVisibility(View.VISIBLE);
                    btPublish.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    SoftInputUtil.hideSoftInput(BallQTipOffDetailActivity.this);
                    cacheCommentInfo = etComment.getText().toString();
                    replyerId = null;
                    replyerName = null;
                    etComment.clearFocus();
                    etComment.setHint("发表评论");
                    etComment.setText("");
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return swipeRefresh;
    }

    private void setRefreshing() {
        if (swipeRefresh != null) {
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
    }

    private void onRefreshCompelete() {
        if (swipeRefresh != null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }

    @Override
    protected void getIntentData(Intent intent) {
        tipOffInfo = intent.getParcelableExtra(Tag);
        if (tipOffInfo != null) {
            KLog.e("加载数据...");
            showLoading();
            getTipOffInfo(tipOffInfo.getEid(), tipOffInfo.getId());
        }
    }

    /**
     * 获取爆料信息
     *
     * @param matchId
     * @param tipId
     */
    private void getTipOffInfo(final int matchId, final int tipId) {
        String url = HttpUrls.HOST_URL_V5 + "match/" + matchId + "/tip/" + tipId + "/";
        KLog.e("Url:" + url);
        Map<String, String> params = null;
        if (UserInfoUtil.checkLogin(this)) {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                KLog.e("加载失败...");
                onRefreshCompelete();
                if (userCommentAdapter == null) {
                    showErrorInfo(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoading();
                            getTipOffInfo(matchId, tipId);
                        }
                    });
                } else {
                    recyclerView.setRefreshComplete();
                    recyclerView.setStartLoadMore();
                    ToastUtil.show(BallQTipOffDetailActivity.this, "请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    UserInfoUtil.getUserTaskMsg(BallQTipOffDetailActivity.this);

                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null) {
                        BallQTipOffEntity data = obj.getObject("data", BallQTipOffEntity.class);
                        if (data != null) {
                            hideLoad();
                            tipOffInfo = data;
                            initBallQTipOffInfo(headerView, data);
                            getTipOffBounties(tipOffInfo.getId());
                            requestTipOffComments(1, tipOffInfo.getId(), false);
                            return;
                        }
                    }
                }
                if (userCommentAdapter == null) {
                    onRefreshCompelete();
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void initBallQTipOffInfo(View view, BallQTipOffEntity data) {
        CircleImageView ivUserIcon = (CircleImageView) view.findViewById(R.id.ivUserIcon);
        ivUserIcon.setOnClickListener(this);
        ImageView isV = (ImageView) view.findViewById(R.id.isV);
        TextView tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        ImageView ivAchievement01 = (ImageView) view.findViewById(R.id.iv_user_achievement01);
        ImageView ivAchievement02 = (ImageView) view.findViewById(R.id.iv_user_achievement02);
        TextView tvCreatedTime = (TextView) view.findViewById(R.id.tv_create_time);
        TextView tvUserTipCount = (TextView) view.findViewById(R.id.tv_user_tip_count);
        TextView tvUserWinRate = (TextView) view.findViewById(R.id.tv_user_tip_win_rate);
        TextView tvUserTrend = (TextView) view.findViewById(R.id.tv_user_tip_trend);
        TextView tvUserReward = (TextView) view.findViewById(R.id.tv_user_tip_reward);

        ImageView ivHomeTeam = (ImageView) view.findViewById(R.id.iv_home_team_icon);
        TextView tvHomeTeamName = (TextView) view.findViewById(R.id.tv_home_team_name);
        TextView tvMatchTime = (TextView) view.findViewById(R.id.tv_game_time);
        TextView tvMatchDate = (TextView) view.findViewById(R.id.tv_game_date);
        ImageView ivAwayTeam = (ImageView) view.findViewById(R.id.iv_away_team_icon);
        TextView tvAwayTeamName = (TextView) view.findViewById(R.id.tv_away_team_name);
        TextView tvMatchLeague = (TextView) view.findViewById(R.id.tv_game_league_name);

        TextView tvChoice = (TextView) view.findViewById(R.id.tvChoice);
        TextView tvSam = (TextView) view.findViewById(R.id.tvSam);
        ViewGroup layoutConfidence = (ViewGroup) view.findViewById(R.id.layout_confidence_data);
        CustomRattingBar rattingBar = (CustomRattingBar) view.findViewById(R.id.rating_bar);

        ImageView ivBettingResult = (ImageView) view.findViewById(R.id.ivBetResult);
        TextView tvTipContent = (TextView) view.findViewById(R.id.tv_tip_content);
        LinearLayout layoutOtherTipInfo = (LinearLayout) view.findViewById(R.id.layout_other_tips);
        TextView tvOtherTipCount = (TextView) view.findViewById(R.id.tvOtherTipNum);
        view.findViewById(R.id.bt_rewards).setOnClickListener(this);
        view.findViewById(R.id.layout_match_info).setOnClickListener(this);
        view.findViewById(R.id.iv_home_team_icon).setOnClickListener(this);
        view.findViewById(R.id.iv_away_team_icon).setOnClickListener(this);
        view.findViewById(R.id.layout_other_tips).setOnClickListener(this);
        ivAttention= (ImageView) view.findViewById(R.id.iv_attention);
        ivAttention.setOnClickListener(this);
        ivAttention.setSelected(data.getIsf()==1);

        JCVideoPlayerStandard jcVideoPlayer= (JCVideoPlayerStandard) view.findViewById(R.id.videoplayer);
        if(data.getRichtext_type()==2){
            jcVideoPlayer.setVisibility(View.VISIBLE);
            getVideoInfo(data.getVid(),jcVideoPlayer);
            if(!TextUtils.isEmpty(data.getFirst_image())) {
                GlideImageLoader.loadImage(this, data.getFirst_image(), R.mipmap.icon_ball_wrap_default_img, jcVideoPlayer.thumbImageView);
            }else{
                jcVideoPlayer.coverImageView.setImageResource(R.mipmap.icon_ball_wrap_default_img);
            }
        }else{
            jcVideoPlayer.setVisibility(View.GONE);
        }

        GlideImageLoader.loadImage(this, data.getPt(), R.mipmap.icon_user_default, ivUserIcon);
        UserInfoUtil.setUserHeaderVMark(data.getIsv(), isV, ivUserIcon);
        UserInfoUtil.setUserAchievementInfo(this, data.getTitle1(), ivAchievement01, data.getTitle2(), ivAchievement02);
        tvUserName.setText(data.getFname());
        Date date = CommonUtils.getDateAndTimeFromGMT(data.getCtime());
        if (date != null) {
            tvCreatedTime.setText(CommonUtils.getDateAndTimeFormatString(date));
        }
        tvUserTipCount.setText(String.valueOf(data.getTipcount()));
        tvUserWinRate.setText(String.format(Locale.getDefault(), "%.2f", data.getWins() * 100) + "%");
        tvUserTrend.setText(String.format(Locale.getDefault(), "%.2f", data.getRor()) + "%");
        tvUserReward.setText(String.valueOf(data.getBtyc()));

        GlideImageLoader.loadImage(this, data.getHtlogo(), 0, ivHomeTeam);
        tvHomeTeamName.setText(data.getHtname());
        GlideImageLoader.loadImage(this, data.getAtlogo(), 0, ivAwayTeam);
        tvAwayTeamName.setText(data.getAtname());
        tvMatchLeague.setText(data.getTourname());
        String matchState = BallQMatchStateUtil.getMatchState(data.getMstatus(), data.getEtype());
        Date matchDate = CommonUtils.getDateAndTimeFromGMT(data.getMtime());
        if (matchDate != null) {
            long times = matchDate.getTime();
            if (times <= System.currentTimeMillis()) {
                if (!TextUtils.isEmpty(matchState) && matchState.equals("未开始")) {
                    tvMatchTime.setText(CommonUtils.getTimeOfDay(date));
                    tvMatchDate.setText(CommonUtils.getMM_ddString(date));
                } else {
                    final String tmpScore = data.getHtscore() + " - " + data.getAtscore();
                    tvMatchTime.setText(tmpScore);
                    tvMatchDate.setText(matchState);
                }
            } else {
                tvMatchTime.setText(CommonUtils.getTimeOfDay(date));
                tvMatchDate.setText(CommonUtils.getMM_ddString(date));
            }
        }

        String choice = MatchBettingInfoUtil.getBettingResultInfo(data.getChoice(), data.getOtype(), data.getOdata());
        if (!TextUtils.isEmpty(choice)) {
            tvChoice.setText(choice);
        } else {
            tvChoice.setText("");
        }
        setGuessBettingResult(ivBettingResult, data.getStatus());
        if (data.getConfidence() == 0) {
            layoutConfidence.setVisibility(View.GONE);
        } else {
            layoutConfidence.setVisibility(View.VISIBLE);
            rattingBar.setRattingValue(data.getConfidence() / 10);
        }

        tvTipContent.setText(Html.fromHtml(data.getCont()));
        tvSam.setText(String.valueOf(data.getSam() / 100));

        if (data.getMtcount() > 1) {
            layoutOtherTipInfo.setVisibility(View.VISIBLE);
            tvOtherTipCount.setText(String.valueOf(data.getMtcount()));
        } else {
            layoutOtherTipInfo.setVisibility(View.GONE);
        }

        ivLike.setSelected(data.getIs_like() == 1);
        if (userCommentEntityList == null) {
            userCommentEntityList = new ArrayList<>(10);
            userCommentAdapter = new BallQUserCommentAdapter(userCommentEntityList);
            userCommentAdapter.setOnLongClickUserHeaderListener(this);
            recyclerView.setAdapter(userCommentAdapter);
        }
    }

    private void setGuessBettingResult(ImageView ivResult, int status) {
        switch (status) {
            case 1:
                ivResult.setVisibility(View.VISIBLE);
                ivResult.setImageResource(R.mipmap.win_icon);
                break;
            case 2:
                ivResult.setVisibility(View.VISIBLE);
                ivResult.setImageResource(R.mipmap.lose_icon);
                break;
            case 3:
                ivResult.setVisibility(View.VISIBLE);
                ivResult.setImageResource(R.mipmap.gone_icon);
                break;
            default:
                ivResult.setVisibility(View.GONE);
                break;
        }
    }

    private void getVideoInfo(String vid, final JCVideoPlayerStandard videoPlayer){
        String url=String.format("https://player.polyv.net/videojson/%s.js", new Object[]{vid});
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
                    if(obj!=null&&!obj.isEmpty()){
                        JSONArray mp4Arrays=obj.getJSONArray("mp4");
                        if(mp4Arrays!=null&&!mp4Arrays.isEmpty()){
                            videoPlayer.setUp(mp4Arrays.getString(0),"");
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private BallQMatchEntity getMatchEntity() {
        if (tipOffInfo != null) {
            BallQMatchEntity match = new BallQMatchEntity();
            match.setEid(tipOffInfo.getEid());
            match.setEtype(tipOffInfo.getEtype());
            match.setAtid(tipOffInfo.getAtid());
            match.setAtlogo(tipOffInfo.getAtlogo());
            match.setAtname(tipOffInfo.getAtname());
            match.setAtscore(String.valueOf(tipOffInfo.getAtscore()));
            match.setHtid(tipOffInfo.getHtid());
            match.setHtlogo(tipOffInfo.getHtlogo());
            match.setHtname(tipOffInfo.getHtname());
            match.setHtscore(String.valueOf(tipOffInfo.getHtscore()));
            match.setMtime(tipOffInfo.getMtime());
            match.setChampion_id(tipOffInfo.getTournament_id());
            match.setStatus(tipOffInfo.getStatus());
            match.setTourname(tipOffInfo.getTourname());
            return match;
        }
        return null;
    }


    private void getTipOffBounties(int id) {
        String url = HttpUrls.HOST_URL_V5 + "bounties/?etype=38&eid=" + id;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 10, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        if (jsonArray != null && !jsonArray.isEmpty()) {
                            if (userRewardHeaderEntityList == null) {
                                userRewardHeaderEntityList = new ArrayList<BallQUserRewardHeaderEntity>(10);
                            }
                            if (userRewardHeaderEntityList.size() > 0) {
                                userRewardHeaderEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(jsonArray, userRewardHeaderEntityList, BallQUserRewardHeaderEntity.class);
                            if (userRewardHeaderAdapter == null) {
                                userRewardHeaderAdapter = new BallQUserRewardHeaderAdapter(BallQTipOffDetailActivity.this, userRewardHeaderEntityList);
                                GridView gridView = (GridView) headerView.findViewById(R.id.gridView);
                                gridView.setAdapter(userRewardHeaderAdapter);
                            } else {
                                userRewardHeaderAdapter.notifyDataSetChanged();
                            }
                            return;
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }


    private void requestTipOffComments(int pages, int id, final boolean isLoadMore) {
        String url = HttpUrls.HOST_URL_V5 + "comments/?etype=38&eid=" + id + "&p=" + pages;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 10, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                recyclerView.setLoadMoreDataFailed();
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!isLoadMore){
                    recyclerView.setRefreshComplete();
                }
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null) {
                        JSONArray array = obj.getJSONArray("data");
                        if (array != null && !array.isEmpty()) {
                            headerView.findViewById(R.id.layout_user_comments).setVisibility(View.VISIBLE);
                            if (!isLoadMore && !userCommentEntityList.isEmpty()) {
                                userCommentEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(array, userCommentEntityList, BallQUserCommentEntity.class);
                            userCommentAdapter.notifyDataSetChanged();
                            if (array.size() < 10) {
                                if (!userCommentEntityList.isEmpty()) {
                                    recyclerView.setLoadMoreDataComplete("没有更多数据了");
                                } else {
                                    recyclerView.setLoadMoreDataComplete();
                                }
                            } else {
                                recyclerView.setStartLoadMore();
                                if (isLoadMore) {
                                    currentPages++;
                                } else {
                                    currentPages = 2;
                                }
                            }
                            return;
                        }
                    }
                }
                if (isLoadMore) {
                    if (!userCommentEntityList.isEmpty()) {
                        recyclerView.setLoadMoreDataComplete("没有更多数据了");
                    } else {
                        recyclerView.setLoadMoreDataComplete();
                    }
                } else {
                    if (userCommentEntityList.isEmpty()) {
                        recyclerView.setLoadMoreDataComplete();
                        headerView.findViewById(R.id.layout_user_comments).setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                if (!isLoadMore) {
                    onRefreshCompelete();
                }
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
        Intent intent=null;
        switch (view.getId()) {
            case R.id.bt_rewards:
                if (tipOffInfo != null) {
                    KLog.e("打赏");
                    UserRewardActivity.userReward(this, "tip", String.valueOf(tipOffInfo.getUid()), tipOffInfo.getId(), tipOffInfo.getPt(), tipOffInfo.getIsv());
                }
                break;
            case R.id.iv_titlebar_next_menu01:
                showShareDialog();
                break;
            case R.id.iv_attention:
                userAttention();
                break;
            case R.id.layout_match_info:
            case R.id.layout_other_tips:
                intent=new Intent(this,BallQMatchDetailActivity.class);
                intent.putExtra(BallQMatchDetailActivity.class.getSimpleName(),getMatchEntity());
                startActivity(intent);
                break;
            case R.id.iv_home_team_icon:
                intent = new Intent(this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info",getMatchEntity());
                intent.putExtra("is_home_team", true);
                startActivity(intent);
                break;
            case R.id.iv_away_team_icon:
                intent = new Intent(this, BallQMatchTeamTipOffHistoryActivity.class);
                intent.putExtra("match_info",getMatchEntity());
                intent.putExtra("is_home_team", false);
                startActivity(intent);
                break;
            case R.id.ivUserIcon:
                UserInfoUtil.lookUserInfo(this,tipOffInfo.getUid());
                break;
        }
    }

    private void userAttention(){
        if(UserInfoUtil.checkLogin(this)){
            String url= HttpUrls.HOST_URL_V5+ "follow/change/";
            HashMap<String,String> params=new HashMap<>(4);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
            params.put("fid",String.valueOf(tipOffInfo.getUid()));
            if(ivAttention.isSelected()){
                params.put("change","0");
            }else{
                params.put("change","1");
            }
            HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack() {
                @Override
                public void onBefore(Request request) {

                }

                @Override
                public void onError(Call call, Exception error) {
                    ToastUtil.show(BallQTipOffDetailActivity.this, "请求失败");
                }

                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject obj = JSONObject.parseObject(response);
                        if (obj != null && !obj.isEmpty()) {
                            int status = obj.getIntValue("status");
                            ToastUtil.show(BallQTipOffDetailActivity.this, obj.getString("message"));
                            if (status == 350) {
                                ivAttention.setSelected(true);
                            } else if (status == 352) {
                                ivAttention.setSelected(false);
                            }
                        }
                    }
                }

                @Override
                public void onFinish(Call call) {

                }
            });

        }else{
            UserInfoUtil.userLogin(this);
        }
    }

    private void showShareDialog() {
        if (tipOffInfo != null && !TextUtils.isEmpty(tipOffInfo.getUrl())) {
            if (shareDialog == null) {
                shareDialog = new ShareDialog(this);
                shareDialog.setShareType("0")
                        .setShareTitle(tipOffInfo.getFname() + "在球商爆料")
                        .setShareExcerpt(getShareBriefInfo())
                        .setShareUrl(tipOffInfo.getUrl());
            }
            shareDialog.show();
        }
    }

    private String getShareBriefInfo() {
        if (tipOffInfo == null) return "";
        final StringBuilder sb = new StringBuilder();
        sb.append(tipOffInfo.getHtname());
        sb.append("VS");
        sb.append(tipOffInfo.getAtname());
        sb.append(",");
        if (TextUtils.isEmpty(tipOffInfo.getOdata())) {
            sb.append(tipOffInfo.getCont());
        } else {
            String otype = MatchBettingInfoUtil.getBettingResultInfo(tipOffInfo.getChoice(), tipOffInfo.getOtype(), tipOffInfo.getOdata());
            if (!TextUtils.isEmpty(otype)) {
                sb.append(otype.replaceAll(" ", "").replaceAll("@", "赔率"));
                sb.append(",");
            }
        }
        sb.append(tipOffInfo.getCont());
        return sb.toString();
    }

    private void showProgressDialog(String msg) {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = new LoadingProgressDialog(this);
            loadingProgressDialog.setCanceledOnTouchOutside(false);
        }
        loadingProgressDialog.setMessage(msg);
        loadingProgressDialog.show();
    }

    private void dimssProgressDialog() {
        if (loadingProgressDialog != null && loadingProgressDialog.isShowing()) {
            loadingProgressDialog.dismiss();
        }
    }


    @OnClick(R.id.btnPublish)
    protected void onClickPublishComment(View view) {
        if (!UserInfoUtil.checkLogin(this)) {
            UserInfoUtil.userLogin(this);
            return;
        }
        String commentInfo = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(commentInfo)) {
            ToastUtil.show(this, "评论内容不能为空");
            return;
        }
        SoftInputUtil.hideSoftInput(this);
        if (tipOffInfo == null) {
            return;
        }
        String url = HttpUrls.HOST_URL_V5 + "comment/add/?etype=38&eid=" + tipOffInfo.getId();
        HashMap<String, String> params = null;
        if (UserInfoUtil.checkLogin(this)) {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        } else {
            UserInfoUtil.userLogin(this);
            return;
        }
        if (!TextUtils.isEmpty(replyerId)) {
            if (!commentInfo.contains(replyerName)) {
                commentInfo = replyerName + commentInfo;
            }
            params.put("reply_id", replyerId);
        }
        params.put("cont", commentInfo);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                showProgressDialog("提交中...");
            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(BallQTipOffDetailActivity.this, "评论失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int status = obj.getIntValue("status");
                        if (status == 307) {
                            etComment.setHint("发表评论");
                            etComment.clearFocus();
                            etComment.setText("");
                            cacheCommentInfo = "";
                            setRefreshing();
                            requestTipOffComments(1, tipOffInfo.getId(), false);
                        }
                        ToastUtil.show(BallQTipOffDetailActivity.this, obj.getString("message"));
                        return;
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                dimssProgressDialog();
            }
        });
    }

    @OnClick(R.id.tvGuess)
    protected void onClickGuess(View view) {
        if (!UserInfoUtil.checkLogin(this)) {
            UserInfoUtil.userLogin(this);
            return;
        }
        if (tipOffInfo != null) {
            Date date = CommonUtils.getDateAndTimeFromGMT(tipOffInfo.getMtime());
            if (date != null) {
                if (date.getTime() <= System.currentTimeMillis()) {
                    ToastUtil.show(this, "比赛进行中/已结束,无法竞猜");
                    return;
                }
            }
            BallQMatchEntity matchEntity = getMatchEntity();
            Intent intent = new Intent(this, BallQMatchGuessBettingActivity.class);
            intent.putExtra(BallQMatchGuessBettingActivity.class.getSimpleName(), matchEntity);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ivLike)
    protected void onClickUserLike(View view) {
        if (!UserInfoUtil.checkLogin(this)) {
            UserInfoUtil.userLogin(this);
            return;
        }
        if (tipOffInfo != null) {
            String url = HttpUrls.HOST_URL_V5 + "likes/";
            HashMap<String, String> params = null;
            if (UserInfoUtil.checkLogin(this)) {
                params = new HashMap<>(5);
                params.put("user", UserInfoUtil.getUserId(this));
                params.put("token", UserInfoUtil.getUserToken(this));
            } else {
                UserInfoUtil.userLogin(this);
                return;
            }
            params.put("object_type", "tip");
            params.put("object_id", String.valueOf(tipOffInfo.getId()));
            params.put("action", ivLike.isSelected() ? "cancel" : "add");
            HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
                @Override
                public void onBefore(Request request) {
                    ivLike.setEnabled(false);
                }

                @Override
                public void onError(Call call, Exception error) {
                    ToastUtil.show(BallQTipOffDetailActivity.this, "请求失败");
                }

                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject obj = JSONObject.parseObject(response);
                        if (obj != null && !obj.isEmpty()) {
                            int status = obj.getIntValue("status");
                            if (status == 8400) {
                                ivLike.setSelected(true);
                            } else if (status == 8401) {
                                ivLike.setSelected(false);
                            }
                            ToastUtil.show(BallQTipOffDetailActivity.this, obj.getString("message"));
                            return;
                        }
                    }
                    ToastUtil.show(BallQTipOffDetailActivity.this, "点赞失败");
                }

                @Override
                public void onFinish(Call call) {
                    ivLike.setEnabled(true);
                }
            });
        }
    }

    @Override
    protected void notifyEvent(String action) {
        if (!TextUtils.isEmpty(action)) {
            if (action.equals(EventType.EVENT_WECHAT_SHARE_SUCCESS)) {
                KLog.e("获取分享成功的消息。。。");
                handleWeChatShareSuccessResponse();
            }
        }
    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    private void handleWeChatShareSuccessResponse() {
        if (shareDialog != null) {
            shareDialog.dismiss();
            if (UserInfoUtil.checkLogin(this)) {
                String url = HttpUrls.HOST_URL_V5 + "user/share_stats/";
                HashMap<String, String> params = new HashMap<>(4);
                params.put("user", UserInfoUtil.getUserId(this));
                params.put("token", UserInfoUtil.getUserToken(this));
                params.put("share_type", "0");
                params.put("share_id", String.valueOf(tipOffInfo.getId()));
                HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
                    @Override
                    public void onBefore(Request request) {

                    }

                    @Override
                    public void onError(Call call, Exception error) {
                        KLog.e("请求失败");

                    }

                    @Override
                    public void onSuccess(Call call, String response) {
                        KLog.json(response);

                    }

                    @Override
                    public void onFinish(Call call) {

                    }
                });
            }

        }
    }

    @Override
    public void onLoadMore() {
        if (recyclerView.isRefreshing()) {
            KLog.e("刷新数据中....");
            recyclerView.setRefreshingTip("刷新数据中...");
        } else {
            KLog.e("currentPage:" + currentPages);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestTipOffComments(currentPages, tipOffInfo.getId(), true);
                }
            }, 300);
        }
    }

    @Override
    public void onRefresh() {
        if (recyclerView.isLoadMoreing()) {
            onRefreshCompelete();
        } else {
            recyclerView.setRefreshing();
            getTipOffInfo(tipOffInfo.getEid(), tipOffInfo.getId());
        }
    }

    @Override
    public void onLongClickUserHead(View v, int position) {
        BallQUserCommentEntity info = userCommentEntityList.get(position);
        if (info != null) {
            etComment.requestFocus();
            SoftInputUtil.showSoftInput(this, etComment);
            replyerName = "@" + info.getFname().trim() + ":";
            replyerId = String.valueOf(info.getUid());
            etComment.setHint(replyerName);
        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        setRefreshing();
        getTipOffInfo(tipOffInfo.getEid(), tipOffInfo.getId());
    }
}
