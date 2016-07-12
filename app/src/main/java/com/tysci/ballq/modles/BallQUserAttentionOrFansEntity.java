package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/7/1.
 */
public class BallQUserAttentionOrFansEntity {
    private int uid;
    private String pt;
    private float wins;
    private int isv;
    private String fname;
    private float ror;
    private int tipcount;
    private int isa;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getIsa() {
        return isa;
    }

    public void setIsa(int isa) {
        this.isa = isa;
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

    public float getRor() {
        return ror;
    }

    public void setRor(float ror) {
        this.ror = ror;
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
