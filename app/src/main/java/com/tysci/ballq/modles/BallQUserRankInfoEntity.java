package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BallQUserRankInfoEntity {
    private int frc;
    private int uid;
    private String pt;
    private float wins;
    private int isv;
    private int rank;
    private int tearn;
    private String fname;
    private float ror;
    private int tipcount;
    private int isf;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getFrc() {
        return frc;
    }

    public void setFrc(int frc) {
        this.frc = frc;
    }

    public int getIsf() {
        return isf;
    }

    public void setIsf(int isf) {
        this.isf = isf;
    }

    public int getIsv() {
        return isv;
    }

    public void setIsv(int isv) {
        this.isv = isv;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getRor() {
        return ror;
    }

    public void setRor(float ror) {
        this.ror = ror;
    }

    public int getTearn() {
        return tearn;
    }

    public void setTearn(int tearn) {
        this.tearn = tearn;
    }

    public int getTipcount() {
        return tipcount;
    }

    public void setTipcount(int tipcount) {
        this.tipcount = tipcount;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getWins() {
        return wins;
    }

    public void setWins(float wins) {
        this.wins = wins;
    }
}
