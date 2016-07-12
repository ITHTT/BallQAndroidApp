package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/5/31.
 */
public class UserInfoEntity implements Parcelable {

    /**
     * uid : 3348
     * bcount : 63
     * btyc : 0
     * rank : 1
     * btc : 0
     * ror : -5.04
     * frc : 1
     * is_old_user : 1
     * pt : users/65e0c97e-a07b-48a9-8fe9-2c1ac945d480
     * blt : 2015-05-31T16:00:00Z
     * tearn : -31452
     * fname :









     * blc : 36
     * bio :
     * title1 :
     * title2 :
     * bwc : 27
     * bsc : 63
     * acount : 0
     * bgc : 0
     * ccount : 2
     * wins : 0.49
     * isv : 0
     * show_assert : 1
     * flc : 0
     * isf : 0
     */
    private int uid;
    private int bcount;
    private int btyc;
    private int rank;
    private int btc;
    private float ror;
    private int frc;
    private int is_old_user;
    private String pt;
    private String blt;
    private int tearn;
    private String fname;
    private int blc;
    private String bio;
    private String title1;
    private String title2;
    private int bwc;
    private int bsc;
    private int acount;
    private int bgc;
    private int ccount;
    private float wins;
    private int isv;
    private int show_assert;
    private int flc;
    private int isf;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setBcount(int bcount) {
        this.bcount = bcount;
    }

    public void setBtyc(int btyc) {
        this.btyc = btyc;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setBtc(int btc) {
        this.btc = btc;
    }

    public void setRor(float ror) {
        this.ror = ror;
    }

    public void setFrc(int frc) {
        this.frc = frc;
    }

    public void setIs_old_user(int is_old_user) {
        this.is_old_user = is_old_user;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public void setBlt(String blt) {
        this.blt = blt;
    }

    public void setTearn(int tearn) {
        this.tearn = tearn;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setBlc(int blc) {
        this.blc = blc;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public void setBwc(int bwc) {
        this.bwc = bwc;
    }

    public void setBsc(int bsc) {
        this.bsc = bsc;
    }

    public void setAcount(int acount) {
        this.acount = acount;
    }

    public void setBgc(int bgc) {
        this.bgc = bgc;
    }

    public void setCcount(int ccount) {
        this.ccount = ccount;
    }

    public void setWins(float wins) {
        this.wins = wins;
    }

    public void setIsv(int isv) {
        this.isv = isv;
    }

    public void setShow_assert(int show_assert) {
        this.show_assert = show_assert;
    }

    public void setFlc(int flc) {
        this.flc = flc;
    }

    public void setIsf(int isf) {
        this.isf = isf;
    }

    public int getUid() {
        return uid;
    }

    public int getBcount() {
        return bcount;
    }

    public int getBtyc() {
        return btyc;
    }

    public int getRank() {
        return rank;
    }

    public int getBtc() {
        return btc;
    }

    public float getRor() {
        return ror;
    }

    public int getFrc() {
        return frc;
    }

    public int getIs_old_user() {
        return is_old_user;
    }

    public String getPt() {
        return pt;
    }

    public String getBlt() {
        return blt;
    }

    public int getTearn() {
        return tearn;
    }

    public String getFname() {
        return fname;
    }

    public int getBlc() {
        return blc;
    }

    public String getBio() {
        return bio;
    }

    public String getTitle1() {
        return title1;
    }

    public String getTitle2() {
        return title2;
    }

    public int getBwc() {
        return bwc;
    }

    public int getBsc() {
        return bsc;
    }

    public int getAcount() {
        return acount;
    }

    public int getBgc() {
        return bgc;
    }

    public int getCcount() {
        return ccount;
    }

    public float getWins() {
        return wins;
    }

    public int getIsv() {
        return isv;
    }

    public int getShow_assert() {
        return show_assert;
    }

    public int getFlc() {
        return flc;
    }

    public int getIsf() {
        return isf;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeInt(this.bcount);
        dest.writeInt(this.btyc);
        dest.writeInt(this.rank);
        dest.writeInt(this.btc);
        dest.writeFloat(this.ror);
        dest.writeInt(this.frc);
        dest.writeInt(this.is_old_user);
        dest.writeString(this.pt);
        dest.writeString(this.blt);
        dest.writeInt(this.tearn);
        dest.writeString(this.fname);
        dest.writeInt(this.blc);
        dest.writeString(this.bio);
        dest.writeString(this.title1);
        dest.writeString(this.title2);
        dest.writeInt(this.bwc);
        dest.writeInt(this.bsc);
        dest.writeInt(this.acount);
        dest.writeInt(this.bgc);
        dest.writeInt(this.ccount);
        dest.writeFloat(this.wins);
        dest.writeInt(this.isv);
        dest.writeInt(this.show_assert);
        dest.writeInt(this.flc);
        dest.writeInt(this.isf);
    }

    public UserInfoEntity() {
    }

    private UserInfoEntity(Parcel in) {
        this.uid = in.readInt();
        this.bcount = in.readInt();
        this.btyc = in.readInt();
        this.rank = in.readInt();
        this.btc = in.readInt();
        this.ror = in.readFloat();
        this.frc = in.readInt();
        this.is_old_user = in.readInt();
        this.pt = in.readString();
        this.blt = in.readString();
        this.tearn = in.readInt();
        this.fname = in.readString();
        this.blc = in.readInt();
        this.bio = in.readString();
        this.title1 = in.readString();
        this.title2 = in.readString();
        this.bwc = in.readInt();
        this.bsc = in.readInt();
        this.acount = in.readInt();
        this.bgc = in.readInt();
        this.ccount = in.readInt();
        this.wins = in.readFloat();
        this.isv = in.readInt();
        this.show_assert = in.readInt();
        this.flc = in.readInt();
        this.isf = in.readInt();
    }

    public static final Creator<UserInfoEntity> CREATOR = new Creator<UserInfoEntity>() {
        public UserInfoEntity createFromParcel(Parcel source) {
            return new UserInfoEntity(source);
        }

        public UserInfoEntity[] newArray(int size) {
            return new UserInfoEntity[size];
        }
    };
}
