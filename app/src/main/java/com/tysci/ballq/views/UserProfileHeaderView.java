package com.tysci.ballq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.UserInfoEntity;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LinDe on 2016-07-26 0026.
 * 用户场次
 */
public final class UserProfileHeaderView extends LinearLayout
{

    @Bind(R.id.tv_roi)
    TextView tv_roi;// 总盈亏
    @Bind(R.id.tv_total_profit_and_loss)
    TextView tv_total_profit_and_loss;// 投资回报
    @Bind(R.id.tv_winning_probability)
    TextView tv_winning_probability;// 胜率

    @Bind(R.id.tv_all_count)
    TextView tvAllCount;// 总场次
    @Bind(R.id.tv_win_count)
    TextView tvWinCount;// 赢场
    @Bind(R.id.tv_lose_count)
    TextView tvLoseCount;// 输场
    @Bind(R.id.tv_go_count)
    TextView tvGoneCount;// 走场

    public UserProfileHeaderView(Context context)
    {
        this(context, null);
    }

    public UserProfileHeaderView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UserProfileHeaderView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_user_profile_header, this, true);

        ButterKnife.bind(this);

        setUserUnLoginData();
    }

    public void setUserUnLoginData()
    {
        setUserROI_data(0, 0, 0);
        setUserCountsData(0, 0, 0, 0);
    }

    public void setUserDataInfo(UserInfoEntity userInfo)
    {
        if (userInfo == null)
            return;

        setUserROI_data(userInfo.getRor(), userInfo.getTearn(), userInfo.getWins());
        setUserCountsData(userInfo.getBsc(), userInfo.getBwc(), userInfo.getBlc(), userInfo.getBgc());
    }

    public void setUserROI_data(float ror, float tearn, float wins)
    {
        tv_roi.setText(ror >= 0 ? "+" : "");
        tv_roi.append(String.format(Locale.getDefault(), "%.2f", ror));
        tv_roi.append("%");

        tv_total_profit_and_loss.setText(tearn >= 0 ? "+" : "");
        tv_total_profit_and_loss.append(String.format(Locale.getDefault(), "%.2f", (float) tearn / 100));

        tv_winning_probability.setText(wins >= 0 ? "+" : "");
        tv_winning_probability.append(String.format(Locale.getDefault(), "%.2f", wins * 100));
        tv_winning_probability.append("%");
    }

    public void setUserCountsData(int allCount, int winCount, int loseCount, int goneCount)
    {
        if (allCount < 0)
            allCount = 0;
        tvAllCount.setText(String.valueOf(allCount));
        tvAllCount.append("场");

        if (winCount < 0)
            winCount = 0;
        tvWinCount.setText(String.valueOf(winCount));
        tvWinCount.append("赢");

        if (loseCount < 0)
            loseCount = 0;
        tvLoseCount.setText(String.valueOf(loseCount));
        tvLoseCount.append("输");

        if (goneCount < 0)
            goneCount = 0;
        tvGoneCount.setText(String.valueOf(goneCount));
        tvGoneCount.append("走");
    }
}
