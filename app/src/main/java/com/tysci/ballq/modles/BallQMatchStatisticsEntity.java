package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/6/6.
 */
public class BallQMatchStatisticsEntity
{
    private int homeTeamValue;
    private int awayTeamValue;
    private String statisticKey;

    public int getAwayTeamValue()
    {
        return awayTeamValue;
    }

    public <INT> void setAwayTeamValue(INT awayTeamValue)
    {
        this.awayTeamValue = ParseUtil.makeParse(awayTeamValue, 0);
    }

    public int getHomeTeamValue()
    {
        return homeTeamValue;
    }

    public <INT> void setHomeTeamValue(INT homeTeamVale)
    {
        this.homeTeamValue = ParseUtil.makeParse(homeTeamVale, 0);
    }

    public String getStatisticKey()
    {
        return statisticKey;
    }

    public void setStatisticKey(String statisticKey)
    {
        this.statisticKey = statisticKey;
    }
}
