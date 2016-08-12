package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQBallWarpInfoEntity;
import com.tysci.ballq.modles.BallQUserCommentEntity;
import com.tysci.ballq.modles.BallQUserRewardHeaderEntity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.modles.event.EventType;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.CommonUtils;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.SoftInputUtil;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.utils.WebViewUtil;
import com.tysci.ballq.views.adapters.BallQUserCommentAdapter;
import com.tysci.ballq.views.adapters.BallQUserRewardHeaderAdapter;
import com.tysci.ballq.views.dialogs.LoadingProgressDialog;
import com.tysci.ballq.views.dialogs.ShareDialog;
import com.tysci.ballq.views.interfaces.OnLongClickUserHeaderListener;
import com.tysci.ballq.views.widgets.CircleImageView;
import com.tysci.ballq.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/6.
 */
public class BallQBallWarpDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AutoLoadMoreRecyclerView.OnLoadMoreListener
        , OnLongClickUserHeaderListener
{
    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recycler_view)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.et_comment_infos)
    protected EditText etComment;
    @Bind(R.id.ivLike)
    protected ImageView ivLike;
    @Bind(R.id.btnPublish)
    protected Button btPublish;
    private ImageView ivAttention;

    private View headerView = null;
    private BallQBallWarpInfoEntity ballWarpInfo = null;
    private int currentPages = 1;

    private List<BallQUserRewardHeaderEntity> userRewardHeaderEntityList = null;
    private BallQUserRewardHeaderAdapter userRewardHeaderAdapter = null;

    private List<BallQUserCommentEntity> userCommentEntityList = null;
    private BallQUserCommentAdapter userCommentAdapter;

    private int isLike = 0;
    private int isCollect = 0;
    private int fid = 0;

    private String replyerName;
    private String replyerId;
    private String cacheCommentInfo = "";

    private LoadingProgressDialog loadingProgressDialog = null;
    private ShareDialog shareDialog = null;


    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_ballq_ball_warp_detail;
    }

    @Override
    protected void initViews()
    {
        setTitle("球经详情");
        titleBar.setRightMenuIcon(R.mipmap.icon_share_gold, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnLoadMoreListener(this);
        recyclerView.setBackgroundResource(R.color.white);
        swipeRefresh.setOnRefreshListener(this);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_ballq_ball_warp_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.addHeaderView(headerView);
        ivLike.setOnClickListener(this);

        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    ivLike.setVisibility(View.GONE);
                    btPublish.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(cacheCommentInfo))
                    {
                        etComment.setText(cacheCommentInfo);
                        etComment.setSelection(cacheCommentInfo.length());
                    }
                }
                else
                {
                    etComment.setText("");
                    ivLike.setVisibility(View.VISIBLE);
                    btPublish.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
            {
                if (e.getAction() == MotionEvent.ACTION_DOWN)
                {
                    SoftInputUtil.hideSoftInput(BallQBallWarpDetailActivity.this);
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
            public void onTouchEvent(RecyclerView rv, MotionEvent e)
            {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
            {

            }
        });
    }

    @Override
    protected View getLoadingTargetView()
    {
        return swipeRefresh;
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        KLog.e("获取数据。。。");
        ballWarpInfo = intent.getParcelableExtra(Tag);
        if (ballWarpInfo != null)
        {
            showLoading();
            getBallQBallWarpInfo(ballWarpInfo.getId());
        }
    }

    private void setRefreshing()
    {
        if (swipeRefresh != null)
        {
            swipeRefresh.post(new Runnable()
            {
                @Override
                public void run()
                {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
    }

    private void onRefreshCompelete()
    {
        if (swipeRefresh != null)
        {
            swipeRefresh.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if (swipeRefresh != null)
                    {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }

    private void showProgressDialog(String msg)
    {
        if (loadingProgressDialog == null)
        {
            loadingProgressDialog = new LoadingProgressDialog(this);
            loadingProgressDialog.setCanceledOnTouchOutside(false);
        }
        loadingProgressDialog.setMessage(msg);
        loadingProgressDialog.show();
    }

    private void dimssProgressDialog()
    {
        if (loadingProgressDialog != null && loadingProgressDialog.isShowing())
        {
            loadingProgressDialog.dismiss();
        }
    }

    /**
     * 获取球经信息
     *
     * @param id
     */
    private void getBallQBallWarpInfo(final int id)
    {
        String url = HttpUrls.HOST_URL_V5 + "article/" + id + "/";
        Map<String, String> params = null;
        if (UserInfoUtil.checkLogin(this))
        {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                onRefreshCompelete();
                if (userCommentAdapter == null)
                {
                    showErrorInfo(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showLoading();
                            getBallQBallWarpInfo(id);
                        }
                    });
                }
                else
                {
                    recyclerView.setRefreshComplete();
                    recyclerView.setStartLoadMore();
                    ToastUtil.show(BallQBallWarpDetailActivity.this, "请求失败");
                }
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        BallQBallWarpInfoEntity data = obj.getObject("data", BallQBallWarpInfoEntity.class);
                        if (data != null)
                        {
                            hideLoad();
                            ballWarpInfo = data;
                            initBallWarpInfo(headerView, data);
                            getBallWarpBounties(data.getId());
                            requestBallWarpComments(data.getId(), 1, false);
                            return;
                        }
                    }
                }
                if (userCommentAdapter == null)
                {
                    onRefreshCompelete();
                    showEmptyInfo();
                }
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    private void initBallWarpInfo(View view, BallQBallWarpInfoEntity data)
    {
        RelativeLayout layoutUserInfo = (RelativeLayout) view.findViewById(R.id.layout_user_info);
        layoutUserInfo.setBackgroundResource(CommonUtils.getRandomImageBackgournd());

        CircleImageView ivUserHeader = (CircleImageView) view.findViewById(R.id.ivUserIcon);
        ivUserHeader.setOnClickListener(this);
        ImageView isV = (ImageView) view.findViewById(R.id.isV);
        TextView tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        ImageView ivAchievement01 = (ImageView) view.findViewById(R.id.iv_user_achievement_01);
        ImageView ivAchievement02 = (ImageView) view.findViewById(R.id.iv_user_achievement_02);
        TextView tvWriteCounts = (TextView) view.findViewById(R.id.tv_user_write_article_counts);
        TextView tvCreateDate = (TextView) view.findViewById(R.id.tv_create_date);
        ImageView ivUserCollect = (ImageView) view.findViewById(R.id.iv_user_collection);
        ivUserCollect.setOnClickListener(this);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        FrameLayout webLayout = (FrameLayout) view.findViewById(R.id.webLayout);
        view.findViewById(R.id.bt_rewards).setOnClickListener(this);
        ivAttention = (ImageView) view.findViewById(R.id.iv_attention);
        ivAttention.setSelected(data.getIsf() == 1);
        ivAttention.setOnClickListener(this);

        GlideImageLoader.loadImage(this, data.getPt(), R.mipmap.icon_user_default, ivUserHeader);
        UserInfoUtil.setUserHeaderVMark(data.getIsv(), isV, ivUserHeader);
        tvUserName.setText(data.getFname());
        UserInfoUtil.setUserAchievementInfo(this, data.getTitle1(), ivAchievement01, data.getTitle2(), ivAchievement02);
        tvWriteCounts.setText(String.valueOf(data.getArtcount()));
        Date date = CommonUtils.getDateAndTimeFromGMT(data.getCtime());
        if (date != null)
        {
            tvCreateDate.setText(CommonUtils.getDateAndTimeFormatString(date));
        }
        isCollect = data.getIsc();
        isLike = data.getIs_like();
        fid = data.getFid();
        if (data.getIsc() == 1)
        {
            ivUserCollect.setSelected(true);
        }
        else
        {
            ivUserCollect.setSelected(false);
        }
        tvTitle.setText(data.getTitle());
        WebView webView = WebViewUtil.getHtmlWebView(this, data.getCont());
        KLog.e(data.getCont());
        if (webView != null)
        {
            if (webLayout.getChildCount() == 0)
            {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                webLayout.addView(webView, layoutParams);
            }
        }
        if (data.getIs_like() == 1)
        {
            ivLike.setSelected(true);
        }
        else
        {
            ivLike.setSelected(false);
        }
        if (userCommentEntityList == null || userCommentAdapter == null)
        {
            userCommentEntityList = new ArrayList<>(10);
            userCommentAdapter = new BallQUserCommentAdapter(userCommentEntityList);
            userCommentAdapter.setOnLongClickUserHeaderListener(this);
            recyclerView.setAdapter(userCommentAdapter);
        }
    }

    /**
     * 获取打赏记录
     *
     * @param id
     */
    private void getBallWarpBounties(int id)
    {
        String url = HttpUrls.HOST_URL_V5 + "bounties/?etype=49&eid=" + id;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {

            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        if (jsonArray != null && !jsonArray.isEmpty())
                        {
                            if (userRewardHeaderEntityList == null)
                            {
                                userRewardHeaderEntityList = new ArrayList<BallQUserRewardHeaderEntity>(10);
                            }
                            if (userRewardHeaderEntityList.size() > 0)
                            {
                                userRewardHeaderEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(jsonArray, userRewardHeaderEntityList, BallQUserRewardHeaderEntity.class);
                            if (userRewardHeaderAdapter == null)
                            {
                                userRewardHeaderAdapter = new BallQUserRewardHeaderAdapter(BallQBallWarpDetailActivity.this, userRewardHeaderEntityList);
                                GridView gridView = (GridView) headerView.findViewById(R.id.gridView);
                                gridView.setAdapter(userRewardHeaderAdapter);
                            }
                            else
                            {
                                userRewardHeaderAdapter.notifyDataSetChanged();
                            }
                            return;
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {

            }
        });
    }

    /**
     * 获取评论数据
     *
     * @param id
     * @param pages
     * @param isLoadMore
     */
    private void requestBallWarpComments(int id, int pages, final boolean isLoadMore)
    {
        String url = HttpUrls.HOST_URL_V5 + "comments/?etype=49&eid=" + id + "&p=" + pages;
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 30, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                if (!isLoadMore)
                {
                    recyclerView.setRefreshComplete();
                }
                recyclerView.setLoadMoreDataFailed();
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                if (!isLoadMore)
                {
                    recyclerView.setRefreshComplete();
                }
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        JSONArray objs = obj.getJSONArray("data");
                        if (objs != null && !objs.isEmpty())
                        {
                            if (!isLoadMore && !userCommentEntityList.isEmpty())
                            {
                                userCommentEntityList.clear();
                            }
                            CommonUtils.getJSONListObject(objs, userCommentEntityList, BallQUserCommentEntity.class);
                            userCommentAdapter.notifyDataSetChanged();
                            if (objs.size() < 10)
                            {
                                recyclerView.setLoadMoreDataComplete("没有更多数据了");
                            }
                            else
                            {
                                if (isLoadMore)
                                {
                                    currentPages++;
                                }
                                else
                                {
                                    currentPages = 2;
                                }
                                recyclerView.setStartLoadMore();
                            }
                            headerView.findViewById(R.id.layout_user_comments).setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                }
                if (isLoadMore)
                {
                    if (!userCommentEntityList.isEmpty())
                    {
                        recyclerView.setLoadMoreDataComplete("没有更多数据了");
                    }
                    else
                    {
                        recyclerView.setLoadMoreDataComplete();
                    }
                }
                else
                {
                    if (userCommentEntityList.isEmpty())
                    {
                        recyclerView.setLoadMoreDataComplete();
                        headerView.findViewById(R.id.layout_user_comments).setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {
                if (!isLoadMore)
                {
                    onRefreshCompelete();
                }
            }
        });
    }

    /**
     * 用户点赞
     */
    private void userLike()
    {
        if (!UserInfoUtil.checkLogin(this))
        {
            UserInfoUtil.userLogin(this);
        }
        else
        {
            String url = HttpUrls.HOST_URL_V5 + "likes/";
            HashMap<String, String> params = new HashMap<>();
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
            params.put("object_type", "article");
            params.put("object_id", String.valueOf(ballWarpInfo.getId()));
            if (isLike == 0)
            {
                params.put("action", "add");
            }
            else
            {
                params.put("action", "cancel");
            }
            HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
            {
                @Override
                public void onBefore(Request request)
                {
                    ivLike.setEnabled(false);

                }

                @Override
                public void onError(Call call, Exception error)
                {

                }

                @Override
                public void onSuccess(Call call, String response)
                {
                    KLog.e(response);
                    if (!TextUtils.isEmpty(response))
                    {
                        JSONObject object = JSONObject.parseObject(response);
                        if (object != null)
                        {
                            //ToastUtil2.show(ArticleDetailActivity.this, object.getString("message"));
                            int status = object.getIntValue("status");
                            if (status == 8400 && isLike == 0)
                            {
                                //postRefreshEvent("user_like", 1);
                                isLike = 1;
                                ivLike.setSelected(true);
                            }
                            else if (status == 8401 && isLike == 1)
                            {
                                //postRefreshEvent("user_like", -1);
                                isLike = 0;
                                ivLike.setSelected(false);
                            }
                            ToastUtil.show(BallQBallWarpDetailActivity.this, object.getString("message"));
                        }
                    }
                }

                @Override
                public void onFinish(Call call)
                {
                    ivLike.setEnabled(true);
                }
            });
        }
    }

    private void userCollect()
    {
        if (!UserInfoUtil.checkLogin(this))
        {
            UserInfoUtil.userLogin(this);
        }
        else
        {
            String url = HttpUrls.HOST_URL_V5 + "user/favorites/add/?etype=1&eid=" + ballWarpInfo.getId();
            String delUrl = HttpUrls.HOST_URL_V5 + "user/favorites/del/?etype=1&fid=" + fid;
            HashMap<String, String> params = new HashMap<>();
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
            if (isCollect == 1 && fid >= 0)
            {
                url = delUrl;
            }
            final ImageView ivUserCollect = (ImageView) headerView.findViewById(R.id.iv_user_collection);
            HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
            {
                @Override
                public void onBefore(Request request)
                {
                    ivUserCollect.setEnabled(false);
                }

                @Override
                public void onError(Call call, Exception error)
                {

                }

                @Override
                public void onSuccess(Call call, String response)
                {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response))
                    {
                        JSONObject obj = JSONObject.parseObject(response);
                        if (obj != null && !obj.isEmpty())
                        {
                            int status = obj.getIntValue("status");
                            if (status == 0)
                            {
                                if (isCollect == 1)
                                {
                                    isCollect = 0;
                                    ivUserCollect.setSelected(false);
                                    ToastUtil.show(BallQBallWarpDetailActivity.this, "取消收藏成功");
                                }
                                else
                                {
                                    fid = obj.getIntValue("data");
                                    isCollect = 1;
                                    ivUserCollect.setSelected(true);
                                    ToastUtil.show(BallQBallWarpDetailActivity.this, "收藏成功");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFinish(Call call)
                {
                    ivUserCollect.setEnabled(true);

                }
            });
        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity)
    {
        setRefreshing();
        getBallQBallWarpInfo(ballWarpInfo.getId());
    }

    @Override
    protected boolean isCanceledEventBus()
    {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState)
    {

    }

    @Override
    protected void handleInstanceState(Bundle outState)
    {

    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivLike:
                userLike();
                break;
            case R.id.iv_user_collection:
                userCollect();
                break;
            case R.id.iv_titlebar_next_menu01:
                showShareDialog();
                break;
            case R.id.iv_attention:
                userAttention();
                break;
            case R.id.bt_rewards:
                if (ballWarpInfo != null)
                {
                    if (UserInfoUtil.checkLogin(this))
                    {
                        UserRewardActivity.userReward(this, "article", String.valueOf(ballWarpInfo.getUid()), ballWarpInfo.getId(), ballWarpInfo.getPt(), ballWarpInfo.getIsv());
                    }
                    else
                    {
                        UserInfoUtil.userLogin(this);
                    }
                }
                break;
            case R.id.ivUserIcon:
                if (ballWarpInfo != null)
                {
                    UserInfoUtil.lookUserInfo(this, ballWarpInfo.getUid());
                }
                break;
        }
    }

    private void showShareDialog()
    {
        if (ballWarpInfo != null && !TextUtils.isEmpty(ballWarpInfo.getUrl()))
        {
            if (shareDialog == null)
            {
                shareDialog = new ShareDialog(this);
                shareDialog.setShareType("0")
                        .setShareTitle(ballWarpInfo.getTitle())
                        .setShareExcerpt(ballWarpInfo.getExcerpt())
                        .setShareUrl(ballWarpInfo.getUrl());
            }
            shareDialog.show();
        }
    }

    @Override
    protected void notifyEvent(String action)
    {
        if (!TextUtils.isEmpty(action))
        {
            if (action.equals(EventType.EVENT_WECHAT_SHARE_SUCCESS))
            {
                KLog.e("获取分享成功的消息。。。");
                handleWeChatShareSuccessResponse();
            }
        }
    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }

    private void handleWeChatShareSuccessResponse()
    {
        if (shareDialog != null)
        {
            shareDialog.dismiss();
            if (UserInfoUtil.checkLogin(this))
            {
                String url = HttpUrls.HOST_URL_V5 + "user/share_stats/";
                HashMap<String, String> params = new HashMap<>(4);
                params.put("user", UserInfoUtil.getUserId(this));
                params.put("token", UserInfoUtil.getUserToken(this));
                params.put("share_type", "1");
                params.put("share_id", String.valueOf(ballWarpInfo.getId()));
                HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
                {
                    @Override
                    public void onBefore(Request request)
                    {

                    }

                    @Override
                    public void onError(Call call, Exception error)
                    {
                        KLog.e("请求失败");

                    }

                    @Override
                    public void onSuccess(Call call, String response)
                    {
                        KLog.json(response);

                    }

                    @Override
                    public void onFinish(Call call)
                    {

                    }
                });
            }

        }
    }

    @Override
    public void onLoadMore()
    {
        if (recyclerView.isRefreshing())
        {
            //KLog.e("刷新数据中....");
            recyclerView.setRefreshingTip("刷新数据中...");
        }
        else
        {
            // KLog.e("currentPage:" + currentPages);
            recyclerView.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    requestBallWarpComments(ballWarpInfo.getId(), currentPages, true);
                }
            }, 300);
        }
    }

    @Override
    public void onRefresh()
    {
        if (recyclerView.isLoadMoreing())
        {
            onRefreshCompelete();
        }
        else
        {
            recyclerView.setRefreshing();
            getBallQBallWarpInfo(ballWarpInfo.getId());
        }
    }

    @Override
    public void onLongClickUserHead(View v, int position)
    {
        BallQUserCommentEntity info = userCommentEntityList.get(position);
        if (info != null)
        {
            etComment.requestFocus();
            SoftInputUtil.showSoftInput(this, etComment);
            replyerName = "@" + info.getFname().trim() + ":";
            replyerId = String.valueOf(info.getUid());
            etComment.setHint(replyerName);
        }
    }

    @OnClick(R.id.btnPublish)
    protected void onClickPublishComment(View view)
    {
        if (!UserInfoUtil.checkLogin(this))
        {
            UserInfoUtil.userLogin(this);
            return;
        }
        String commentInfo = etComment.getText().toString().trim();
        KLog.e("comment_info:" + commentInfo);

        if (TextUtils.isEmpty(commentInfo))
        {
            ToastUtil.show(this, "评论内容不能为空");
            return;
        }
        SoftInputUtil.hideSoftInput(this);
        if (ballWarpInfo == null)
        {
            return;
        }
        String url = HttpUrls.HOST_URL_V5 + "comment/add/?etype=49&eid=" + ballWarpInfo.getId();
        HashMap<String, String> params = null;
        if (UserInfoUtil.checkLogin(this))
        {
            params = new HashMap<>(2);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
        }
        else
        {
            UserInfoUtil.userLogin(this);
            return;
        }
        if (!TextUtils.isEmpty(replyerId))
        {
            if (!commentInfo.contains(replyerName))
            {
                commentInfo = replyerName + commentInfo;
            }
            params.put("reply_id", replyerId);
        }
        params.put("cont", commentInfo);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {
                showProgressDialog("提交中...");
            }

            @Override
            public void onError(Call call, Exception error)
            {
                ToastUtil.show(BallQBallWarpDetailActivity.this, "评论失败");
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty())
                    {
                        int status = obj.getIntValue("status");
                        if (status == 307)
                        {
                            etComment.setHint("发表评论");
                            etComment.clearFocus();
                            etComment.setText("");
                            cacheCommentInfo = "";
                            setRefreshing();
                            requestBallWarpComments(ballWarpInfo.getId(), 1, false);
                        }
                        ToastUtil.show(BallQBallWarpDetailActivity.this, obj.getString("message"));
                        return;
                    }
                }
            }

            @Override
            public void onFinish(Call call)
            {
                dimssProgressDialog();
            }
        });
    }

    private void userAttention()
    {
        if (UserInfoUtil.checkLogin(this))
        {
            String url = HttpUrls.HOST_URL_V5 + "follow/change/";
            HashMap<String, String> params = new HashMap<>(4);
            params.put("user", UserInfoUtil.getUserId(this));
            params.put("token", UserInfoUtil.getUserToken(this));
            params.put("fid", String.valueOf(ballWarpInfo.getUid()));
            if (ivAttention.isSelected())
            {
                params.put("change", "0");
            }
            else
            {
                params.put("change", "1");
            }
            HttpClientUtil.getHttpClientUtil().sendPostRequest(BallQUserRankingListDetailActivity.class.getSimpleName(), url, params, new HttpClientUtil.StringResponseCallBack()
            {
                @Override
                public void onBefore(Request request)
                {

                }

                @Override
                public void onError(Call call, Exception error)
                {
                    ToastUtil.show(BallQBallWarpDetailActivity.this, "请求失败");
                }

                @Override
                public void onSuccess(Call call, String response)
                {
                    KLog.json(response);
                    if (!TextUtils.isEmpty(response))
                    {
                        JSONObject obj = JSONObject.parseObject(response);
                        if (obj != null && !obj.isEmpty())
                        {
                            int status = obj.getIntValue("status");
                            ToastUtil.show(BallQBallWarpDetailActivity.this, obj.getString("message"));
                            if (status == 350)
                            {
                                ivAttention.setSelected(true);
                            }
                            else if (status == 352)
                            {
                                ivAttention.setSelected(false);
                            }
                        }
                    }
                }

                @Override
                public void onFinish(Call call)
                {

                }
            });
        }
        else
        {
            UserInfoUtil.userLogin(this);
        }
    }
}
