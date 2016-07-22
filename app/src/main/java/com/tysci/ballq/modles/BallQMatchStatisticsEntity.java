package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/6/6.
 */
public class BallQMatchStatisticsEntity {
    private int homeTeamValue;
    private int awayTeamValue;
    private String statisticKey;

    public int getAwayTeamValue() {
        return awayTeamValue;
    }

    public void setAwayTeamValue(int awayTeamValue) {
        this.awayTeamValue = awayTeamValue;
    }

    public int getHomeTeamValue() {
        return homeTeamValue;
    }

    public void setHomeTeamValue(int homeTeamVale) {
        this.homeTeamValue = homeTeamVale;
    }

    public String getStatisticKey() {
        return statisticKey;
    }

    public void setStatisticKey(String statisticKey) {
        this.statisticKey = statisticKey;
    }
}
