package com.tysci.ballq.modles;

/**
 * Created by HTT on 2016/7/7.
 */
public class BallQAuthorAnalystsEntity {
    private String rank_type;
    private int uid;
    private String pt;
    private int r_type;
    private float wins;
    private int rank;
    private String note;
    private int earnings;
    private String fname;
    private float ror;

    public String getRank_type() {
        return rank_type;
    }

    public void setRank_type(String rank_type) {
        this.rank_type = rank_type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public int getR_type() {
        return r_type;
    }

    public void setR_type(int r_type) {
        this.r_type = r_type;
    }

    public float getWins() {
        return wins;
    }

    public void setWins(float wins) {
        this.wins = wins;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getEarnings() {
        return earnings;
    }

    public void setEarnings(int earnings) {
        this.earnings = earnings;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public float getRor() {
        return ror;
    }

    public void setRor(float ror) {
        this.ror = ror;
    }
}
