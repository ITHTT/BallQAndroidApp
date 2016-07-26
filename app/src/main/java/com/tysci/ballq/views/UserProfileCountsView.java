package com.tysci.ballq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.ballq.R;

/**
 * Created by LinDe on 2016-07-26 0026.
 * 用户场次
 */
public final class UserProfileCountsView extends LinearLayout
{
    /**
     * 总场次、赢场、输场、走场
     */
    private TextView tvAllCount, tvWinCount, tvLoseCount, tvGoneCount;

    public UserProfileCountsView(Context context)
    {
        this(context, null);
    }

    public UserProfileCountsView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public UserProfileCountsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializing(context);
    }

    private void initializing(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_user_win_lose_gone, this, true);

        tvAllCount = (TextView) findViewById(R.id.tv_all_count);
        tvWinCount = (TextView) findViewById(R.id.tv_win_count);
        tvLoseCount = (TextView) findViewById(R.id.tv_lose_count);
        tvGoneCount = (TextView) findViewById(R.id.tv_go_count);

        setUserCountsData(0, 0, 0, 0);
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
