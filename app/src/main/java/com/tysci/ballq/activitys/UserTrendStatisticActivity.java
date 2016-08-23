package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.UserInfoEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.networks.HttpClientUtil;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.KLog;
import com.tysci.ballq.utils.UserInfoUtil;
import com.tysci.ballq.views.widgets.BallQUserTrendStatisticLayout;
import com.tysci.ballq.views.widgets.CircleImageView;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by HTT on 2016/6/17.
 */
public class UserTrendStatisticActivity extends BaseActivity
{
    @Bind(R.id.ivUserIcon)
    protected CircleImageView ivUserIcon;
    @Bind(R.id.isV)
    protected ImageView isV;
    @Bind(R.id.tv_user_name)
    protected TextView tvUserName;
    @Bind(R.id.tv_user_win_rate)
    protected TextView tvWinRate;
    @Bind(R.id.tv_all_profit)
    protected TextView tvAllProfit;
    @Bind(R.id.tv_user_profit_rate)
    protected TextView tvProfitRate;
    @Bind(R.id.layout_all_trend)
    protected BallQUserTrendStatisticLayout layoutAllTrend;
    @Bind(R.id.layout_footerball_trend)
    protected BallQUserTrendStatisticLayout layoutFooterBallTrend;
    @Bind(R.id.layout_basketball_trend)
    protected BallQUserTrendStatisticLayout layoutBasketBallTrend;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String uid = null;
    private UserInfoEntity userInfo;

    @Override
    protected int getContentViewId()
    {
        return R.layout.activity_user_trend_statistics_info;
    }

    @Override
    protected void initViews()
    {
        setTitle("走势统计");
        layoutAllTrend.setTrendTitle("总走势");
        layoutFooterBallTrend.setTrendTitle("足球走势");
        layoutBasketBallTrend.setTrendTitle("篮球走势");

        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected View getLoadingTargetView()
    {
        return this.findViewById(R.id.layout_trend_statistic_info);
    }

    @Override
    protected void getIntentData(Intent intent)
    {
        uid = intent.getStringExtra(Tag);
        if (TextUtils.isEmpty(uid))
        {
            uid = UserInfoUtil.getUserId(this);
        }
        showLoading();
        getUserInfo(uid);
    }

    private void getUserInfo(final String uid)
    {
        String url = HttpUrls.HOST_URL_V5 + "user/" + uid + "/profile/";
        HashMap<String, String> params = null;
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
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showLoading();
                        getUserInfo(uid);
                    }
                });

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
                        JSONObject dataObj = obj.getJSONObject("data");
                        userInfo = obj.getObject("data", UserInfoEntity.class);
                        if (dataObj != null && !dataObj.isEmpty())
                        {
                            GlideImageLoader.loadImage(UserTrendStatisticActivity.this, dataObj.getString("pt"), R.mipmap.icon_user_default, ivUserIcon);
                            UserInfoUtil.setUserHeaderVMark(dataObj.getIntValue("isv"), isV, ivUserIcon);
                            tvUserName.setText(dataObj.getString("fname"));
                            tvWinRate.setText("亚盘胜率:" + String.format(Locale.getDefault(), "%.2f", dataObj.getFloat("wins") * 100) + "%");
                            float allProfit = dataObj.getFloat("tearn") / 100;
                            String allProfitInfo = "总盈亏:";
                            if (allProfit >= 0)
                            {
                                allProfitInfo = allProfitInfo + "+" + allProfit;
                            }
                            else
                            {
                                allProfitInfo = allProfitInfo + allProfit;
                            }
                            tvAllProfit.setText(allProfitInfo);
                            tvProfitRate.setText("投资回报:" + String.format(Locale.getDefault(), "%.2f", dataObj.getFloat("ror")) + "%");
                            getUserTrendStatisticInfo(uid);
                            return;
                        }
                    }
                }
                showEmptyInfo();
            }

            @Override
            public void onFinish(Call call)
            {
            }
        });
    }

    private void getUserTrendStatisticInfo(String uid)
    {
        String url = HttpUrls.HOST_URL_V5 + "user/" + uid + "/betting_stats_summary/";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack()
        {
            @Override
            public void onBefore(Request request)
            {

            }

            @Override
            public void onError(Call call, Exception error)
            {
                showErrorInfo(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response)
            {
                KLog.json(response);
                hideLoad();
                if (!TextUtils.isEmpty(response))
                {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null)
                    {
                        JSONObject dataObj = obj.getJSONObject("data");
                        if (dataObj != null)
                        {
                            layoutAllTrend.setAllNum(dataObj.getString("all_count"));
                            layoutAllTrend.setAllWinNum(dataObj.getString("all_win_count"));
                            layoutAllTrend.setAllLoseNum(dataObj.getString("all_lose_count"));
                            layoutAllTrend.setAllGoneNum(dataObj.getString("all_go_count"));

                            layoutFooterBallTrend.setAllNum(dataObj.getString("soccer_all_count"));
                            layoutFooterBallTrend.setAllWinNum(dataObj.getString("soccer_win_count"));
                            layoutFooterBallTrend.setAllLoseNum(dataObj.getString("soccer_lose_count"));
                            layoutFooterBallTrend.setAllGoneNum(dataObj.getString("soccer_go_count"));

                            layoutBasketBallTrend.setAllNum(dataObj.getString("basket_all_count"));
                            layoutBasketBallTrend.setAllWinNum(dataObj.getString("basket_win_count"));
                            layoutBasketBallTrend.setAllLoseNum(dataObj.getString("basket_lose_count"));
                            layoutBasketBallTrend.setAllGoneNum(dataObj.getString("basket_go_count"));

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

    @OnClick({R.id.layout_all_trend, R.id.layout_basketball_trend, R.id.layout_footerball_trend})
    protected void onTrendStatisticDetail(View view)
    {
        int id = view.getId();
        int type = -1;
        switch (id)
        {
            case R.id.layout_basketball_trend:
                type = 1;
                break;
            case R.id.layout_footerball_trend:
                type = 0;
                break;
        }
        BallQUserTrendStatisticLayout trendStatisticLayout = (BallQUserTrendStatisticLayout) view;
        Intent intent = new Intent(this, UserTrendStatisticDetailActivity.class);
        intent.putExtra("user_id", uid);
        intent.putExtra("etype", type);
        intent.putExtra("trend_title", trendStatisticLayout.getTrendTitle());
        intent.putExtra("trend_all", trendStatisticLayout.getAllNum());
        intent.putExtra("trend_win", trendStatisticLayout.getAllWinNum());
        intent.putExtra("trend_lose", trendStatisticLayout.getAllLoseNum());
        intent.putExtra("trend_gone", trendStatisticLayout.getAllGoneNum());
        if (userInfo != null)
        {
            intent.putExtra("user_info", userInfo);
        }
        startActivity(intent);
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

    }

    @Override
    protected void notifyEvent(String action)
    {

    }

    @Override
    protected void notifyEvent(String action, Bundle data)
    {

    }
}
