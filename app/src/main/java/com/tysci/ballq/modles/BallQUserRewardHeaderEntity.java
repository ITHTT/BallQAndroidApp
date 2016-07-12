package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/4/29.
 * 用户打赏信息的头像信息
 */
public class BallQUserRewardHeaderEntity {
    private int isv;
    private int id;
    private String pt;
    private int uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
