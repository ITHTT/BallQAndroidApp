package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BallQGoUserRankEntity
{
    private String rank;
    private String fname;
    private String win;
    private String lose;
    private String go;
    private float profit;
    private float yield_gap;
    private int user_id;

    public String getFname()
    {
        return fname;
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public String getGo()
    {
        return go;
    }

    public void setGo(String go)
    {
        this.go = go;
    }

    public String getLose()
    {
        return lose;
    }

    public void setLose(String lose)
    {
        this.lose = lose;
    }

    public float getProfit()
    {
        return profit;
    }

    public <FLOAT> void setProfit(FLOAT profit)
    {
        this.profit = ParseUtil.makeParse(profit, 0F);
    }

    public String getRank()
    {
        return rank;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public <INT> void setUser_id(INT user_id)
    {
        this.user_id = ParseUtil.makeParse(user_id, 0);
    }

    public String getWin()
    {
        return win;
    }

    public void setWin(String win)
    {
        this.win = win;
    }

    public float getYield_gap()
    {
        return yield_gap;
    }

    public <FLOAT> void setYield_gap(FLOAT yield_gap)
    {
        this.yield_gap = ParseUtil.makeParse(yield_gap, 0);
    }
}
