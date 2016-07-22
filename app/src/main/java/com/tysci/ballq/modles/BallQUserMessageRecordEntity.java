package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/5/8.
 */
public class BallQUserMessageRecordEntity {
    private String cont;
    private String ctime;
    private String pt;
    private String isv;
    private int eid;
    private String fname;
    private int etype;
    private String uid;

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getEtype() {
        return etype;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getIsv() {
        return isv;
    }

    public void setIsv(String isv) {
        this.isv = isv;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
