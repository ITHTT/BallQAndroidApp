package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/5/1.
 */
public class BallQMatchEntity implements Parcelable
{

    /**
     * status : 90
     * atscore : 0
     * betcount : 0
     * tourid : 7
     * atid : 119
     * htlogo : basket_teams/42770033-6894-4d49-9bc4-979e011a5ac1.jpg
     * htid : 113
     * champion_id : -1
     * tourname : NBA
     * comcount : 0
     * htscore : 0
     * utourid : 7
     * atlogo : basket_teams/00bccb69-824c-45ac-ab36-70237abf82ee.jpg
     * htname : 亚特兰大老鹰
     * eid : 6474
     * mtime : 2016-04-30T17:00:00Z
     * etype : 1
     * atname : 波士顿凯尔特人
     * group_id : 41
     * tipcount : 0
     * isf : 0
     */

    private int status;
    private String atscore;
    private int betcount;
    private int tourid;
    private int atid;
    private String htlogo;
    private int htid;
    private int champion_id;
    private String tourname;
    private int comcount;
    private String htscore;
    private int utourid;
    private String atlogo;
    private String htname;
    private int eid;
    private String mtime;
    private int etype;
    private String atname;
    private int group_id;
    private int tipcount;
    private int isf;
    private int is_live = 0;

    public int getStatus()
    {
        return status;
    }

    public String getAtscore()
    {
        return atscore;
    }

    public int getBetcount()
    {
        return betcount;
    }

    public int getTourid()
    {
        return tourid;
    }

    public int getAtid()
    {
        return atid;
    }

    public String getHtlogo()
    {
        return htlogo;
    }

    public int getHtid()
    {
        return htid;
    }

    public int getChampion_id()
    {
        return champion_id;
    }

    public String getTourname()
    {
        return tourname;
    }

    public int getComcount()
    {
        return comcount;
    }

    public String getHtscore()
    {
        return htscore;
    }

    public int getUtourid()
    {
        return utourid;
    }

    public String getAtlogo()
    {
        return atlogo;
    }

    public String getHtname()
    {
        return htname;
    }

    public int getEid()
    {
        return eid;
    }

    public String getMtime()
    {
        return mtime;
    }

    public int getEtype()
    {
        return etype;
    }

    public String getAtname()
    {
        return atname;
    }

    public int getGroup_id()
    {
        return group_id;
    }

    public int getTipcount()
    {
        return tipcount;
    }

    public int getIsf()
    {
        return isf;
    }

    public int getIs_live()
    {
        return is_live;
    }

    public static Creator<BallQMatchEntity> getCREATOR()
    {
        return CREATOR;
    }

    public <INT> void setStatus(INT status)
    {
        this.status = ParseUtil.makeParse(status, 0);
    }

    public <INT> void setAtscore(String atscore)
    {
        this.atscore = atscore;
    }

    public <INT> void setBetcount(INT betcount)
    {
        this.betcount = ParseUtil.makeParse(betcount, 0);
    }

    public <INT> void setTourid(INT tourid)
    {
        this.tourid = ParseUtil.makeParse(tourid, 0);
    }

    public <INT> void setAtid(INT atid)
    {
        this.atid = ParseUtil.makeParse(atid, 0);
    }

    public <INT> void setHtlogo(String htlogo)
    {
        this.htlogo = htlogo;
    }

    public <INT> void setHtid(INT htid)
    {
        this.htid = ParseUtil.makeParse(htid, 0);
    }

    public <INT> void setChampion_id(INT champion_id)
    {
        this.champion_id = ParseUtil.makeParse(champion_id, 0);
    }

    public <INT> void setTourname(String tourname)
    {
        this.tourname = tourname;
    }

    public <INT> void setComcount(INT comcount)
    {
        this.comcount = ParseUtil.makeParse(comcount, 0);
    }

    public <INT> void setHtscore(String htscore)
    {
        this.htscore = htscore;
    }

    public <INT> void setUtourid(INT utourid)
    {
        this.utourid = ParseUtil.makeParse(utourid, 0);
    }

    public <INT> void setAtlogo(String atlogo)
    {
        this.atlogo = atlogo;
    }

    public <INT> void setHtname(String htname)
    {
        this.htname = htname;
    }

    public <INT> void setEid(INT eid)
    {
        this.eid = ParseUtil.makeParse(eid, 0);
    }

    public <INT> void setMtime(String mtime)
    {
        this.mtime = mtime;
    }

    public <INT> void setEtype(INT etype)
    {
        this.etype = ParseUtil.makeParse(etype, 0);
    }

    public <INT> void setAtname(String atname)
    {
        this.atname = atname;
    }

    public <INT> void setGroup_id(INT group_id)
    {
        this.group_id = ParseUtil.makeParse(group_id, 0);
    }

    public <INT> void setTipcount(INT tipcount)
    {
        this.tipcount = ParseUtil.makeParse(tipcount, 0);
    }

    public <INT> void setIsf(INT isf)
    {
        this.isf = ParseUtil.makeParse(isf, 0);
    }

    public <INT> void setIs_live(INT is_live)
    {
        this.is_live = ParseUtil.makeParse(is_live, 0);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.status);
        dest.writeString(this.atscore);
        dest.writeInt(this.betcount);
        dest.writeInt(this.tourid);
        dest.writeInt(this.atid);
        dest.writeString(this.htlogo);
        dest.writeInt(this.htid);
        dest.writeInt(this.champion_id);
        dest.writeString(this.tourname);
        dest.writeInt(this.comcount);
        dest.writeString(this.htscore);
        dest.writeInt(this.utourid);
        dest.writeString(this.atlogo);
        dest.writeString(this.htname);
        dest.writeInt(this.eid);
        dest.writeString(this.mtime);
        dest.writeInt(this.etype);
        dest.writeString(this.atname);
        dest.writeInt(this.group_id);
        dest.writeInt(this.tipcount);
        dest.writeInt(this.isf);
        dest.writeInt(this.is_live);
    }

    public BallQMatchEntity()
    {
    }

    private BallQMatchEntity(Parcel in)
    {
        this.status = in.readInt();
        this.atscore = in.readString();
        this.betcount = in.readInt();
        this.tourid = in.readInt();
        this.atid = in.readInt();
        this.htlogo = in.readString();
        this.htid = in.readInt();
        this.champion_id = in.readInt();
        this.tourname = in.readString();
        this.comcount = in.readInt();
        this.htscore = in.readString();
        this.utourid = in.readInt();
        this.atlogo = in.readString();
        this.htname = in.readString();
        this.eid = in.readInt();
        this.mtime = in.readString();
        this.etype = in.readInt();
        this.atname = in.readString();
        this.group_id = in.readInt();
        this.tipcount = in.readInt();
        this.isf = in.readInt();
        this.is_live = in.readInt();
    }

    public static final Creator<BallQMatchEntity> CREATOR = new Creator<BallQMatchEntity>()
    {
        public BallQMatchEntity createFromParcel(Parcel source)
        {
            return new BallQMatchEntity(source);
        }

        public BallQMatchEntity[] newArray(int size)
        {
            return new BallQMatchEntity[size];
        }
    };
}
