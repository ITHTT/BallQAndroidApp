package com.tysci.ballq.modles;

/**
 * Created by HTT on 2016/6/18.
 */
public class BallQTrendProfitStatisticEntity {
    private int type;
    private int earn;
    private int uid;
    private int winq;
    private int loseq;
    private float to_type;
    private int allq;
    private int goq;
    private int amt;
    private String month;
    private float ahc_type;
    private int tournid;
    private String tournname;
    private int weekday;
    private int sam;

    public int getEarn() {
        return earn;
    }

    public void setEarn(int earn) {
        this.earn = earn;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getWinq() {
        return winq;
    }

    public void setWinq(int winq) {
        this.winq = winq;
    }

    public int getLoseq() {
        return loseq;
    }

    public void setLoseq(int loseq) {
        this.loseq = loseq;
    }

    public float getTo_type() {
        return to_type;
    }

    public void setTo_type(float to_type) {
        this.to_type = to_type;
    }

    public int getAllq() {
        return allq;
    }

    public void setAllq(int allq) {
        this.allq = allq;
    }

    public int getGoq() {
        return goq;
    }

    public void setGoq(int goq) {
        this.goq = goq;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getAhc_type() {
        return ahc_type;
    }

    public void setAhc_type(float ahc_type) {
        this.ahc_type = ahc_type;
    }

    public int getTournid() {
        return tournid;
    }

    public void setTournid(int tournid) {
        this.tournid = tournid;
    }

    public String getTournname() {
        return tournname;
    }

    public void setTournname(String tournname) {
        this.tournname = tournname;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSam() {
        return sam;
    }

    public void setSam(int sam) {
        this.sam = sam;
    }
}
