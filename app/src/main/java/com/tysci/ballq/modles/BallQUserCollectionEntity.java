package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/5/8.
 */
public class BallQUserCollectionEntity {
    private String ctime;
    private String pt;
    private String title;
    private int fid;
    private String ctx;
    private int isv;
    private int eid;
    private String fname;
    private int etype;
    private int aid;

    public int getAid() {
        return aid;
    }

    public String getCtime() {
        return ctime;
    }

    public String getCtx() {
        return ctx;
    }

    public int getEid() {
        return eid;
    }

    public int getEtype() {
        return etype;
    }

    public int getFid() {
        return fid;
    }

    public String getFname() {
        return fname;
    }

    public int getIsv() {
        return isv;
    }

    public String getPt() {
        return pt;
    }

    public String getTitle() {
        return title;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public void setCtx(String ctx) {
        this.ctx = ctx;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setIsv(int isv) {
        this.isv = isv;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
