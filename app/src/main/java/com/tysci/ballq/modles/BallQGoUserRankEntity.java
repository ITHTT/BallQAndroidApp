package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BallQGoUserRankEntity {
    private String rank;
    private String fname;
    private String win;
    private String lose;
    private String go;
    private float profit;
    private float yield_gap;
    private int user_id;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getGo() {
        return go;
    }

    public void setGo(String go) {
        this.go = go;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public float getYield_gap() {
        return yield_gap;
    }

    public void setYield_gap(float yield_gap) {
        this.yield_gap = yield_gap;
    }
}
