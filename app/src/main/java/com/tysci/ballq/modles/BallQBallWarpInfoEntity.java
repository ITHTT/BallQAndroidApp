package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQBallWarpInfoEntity implements Parcelable{
    private String cont;
    private int uid;
    private String share;
    private int like_count;
    private int reading_count;
    private int id;
    private int is_like;
    private String pt;
    private String title;
    private int artcount;
    private int fid;
    private int boncount;
    private String fname;
    private String excerpt;
    private String title1;
    private String title2;
    private int comcount;
    private String ctime;
    private String cover;
    private int isv;
    private int isc;
    private int isf;
    private String url;

    public int getArtcount() {
        return artcount;
    }

    public void setArtcount(int artcount) {
        this.artcount = artcount;
    }

    public int getBoncount() {
        return boncount;
    }

    public void setBoncount(int boncount) {
        this.boncount = boncount;
    }

    public int getComcount() {
        return comcount;
    }

    public void setComcount(int comcount) {
        this.comcount = comcount;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
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

    public int getIsc() {
        return isc;
    }

    public void setIsc(int isc) {
        this.isc = isc;
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

    public int getReading_count() {
        return reading_count;
    }

    public void setReading_count(int reading_count) {
        this.reading_count = reading_count;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cont);
        dest.writeInt(this.uid);
        dest.writeString(this.share);
        dest.writeInt(this.like_count);
        dest.writeInt(this.reading_count);
        dest.writeInt(this.id);
        dest.writeInt(this.is_like);
        dest.writeString(this.pt);
        dest.writeString(this.title);
        dest.writeInt(this.artcount);
        dest.writeInt(this.fid);
        dest.writeInt(this.boncount);
        dest.writeString(this.fname);
        dest.writeString(this.excerpt);
        dest.writeString(this.title1);
        dest.writeString(this.title2);
        dest.writeInt(this.comcount);
        dest.writeString(this.ctime);
        dest.writeString(this.cover);
        dest.writeInt(this.isv);
        dest.writeInt(this.isc);
        dest.writeInt(this.isf);
        dest.writeString(this.url);
    }

    public BallQBallWarpInfoEntity() {
    }

    private BallQBallWarpInfoEntity(Parcel in) {
        this.cont = in.readString();
        this.uid = in.readInt();
        this.share = in.readString();
        this.like_count = in.readInt();
        this.reading_count = in.readInt();
        this.id = in.readInt();
        this.is_like = in.readInt();
        this.pt = in.readString();
        this.title = in.readString();
        this.artcount = in.readInt();
        this.fid = in.readInt();
        this.boncount = in.readInt();
        this.fname = in.readString();
        this.excerpt = in.readString();
        this.title1 = in.readString();
        this.title2 = in.readString();
        this.comcount = in.readInt();
        this.ctime = in.readString();
        this.cover = in.readString();
        this.isv = in.readInt();
        this.isc = in.readInt();
        this.isf = in.readInt();
        this.url = in.readString();
    }

    public static final Creator<BallQBallWarpInfoEntity> CREATOR = new Creator<BallQBallWarpInfoEntity>() {
        public BallQBallWarpInfoEntity createFromParcel(Parcel source) {
            return new BallQBallWarpInfoEntity(source);
        }

        public BallQBallWarpInfoEntity[] newArray(int size) {
            return new BallQBallWarpInfoEntity[size];
        }
    };
}
