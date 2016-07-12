package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/4/29.
 * 球经中用户评论的记录
 */
public class BallQUserCommentEntity {
    private String cont;
    private int uid;
    private String pt;
    private int isV;
    private String title1;
    private String title2;
    private int like_count;
    private int reply_id;
    private String fname;
    private String reply_fname;
    private int is_like;
    private int id;
    private String ctime;

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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getIsV() {
        return isV;
    }

    public void setIsV(int isV) {
        this.isV = isV;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getReply_fname() {
        return reply_fname;
    }

    public void setReply_fname(String reply_fname) {
        this.reply_fname = reply_fname;
    }

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
