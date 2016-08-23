package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by HTT on 2016/6/5.
 */
public class BallQTipOffEntity implements Parcelable
{
    private int fid;
    private int atid;
    private int uid;
    private String atlogo;
    private String odata;
    private String atscore;
    private String choice;
    private int rtype;
    private int reading_count;
    private int eid;
    private String htname;
    private int sam;
    private String htlogo;
    private int id;
    private int tournament_id;
    private int isc;
    private String cont;
    private int etype;
    private String fname;
    private float ror;
    private int btyc;
    private String atname;
    private int comcount;
    private String title1;
    private int status;
    private String title2;
    private int is_like;
    private int tipcount;
    private String mtime;
    private int fcount;
    private int mstatus;
    private int confidence;
    private int isv;
    private String ctime;
    private String htscore;
    private int like_count;
    private String pt;
    private String tourname;
    private String otype;
    private float wins;
    private int htid;
    private int mtcount;
    private int sorder;
    private int settled;
    private int boncount;
    private String url;
    private int richtext_type;
    private String first_image;
    private String vid;
    private int isf;

    public int getFid()
    {
        return fid;
    }

    public <T> void setFid(T fid)
    {
        this.fid=ParseUtil.makeParse(fid,0);
    }

    public int getAtid()
    {
        return atid;
    }

    public <T> void setAtid(T atid)
    {
        this.atid = ParseUtil.makeParse(atid, 0);
    }

    public int getUid()
    {
        return uid;
    }

    public <T> void setUid(T uid)
    {
        this.uid=ParseUtil.makeParse(uid,0);
    }

    public String getAtlogo()
    {
        return atlogo;
    }

    public <T> void setAtlogo(String atlogo)
    {
        this.atlogo = atlogo;
    }

    public String getOdata()
    {
        return odata;
    }

    public <T> void setOdata(String odata)
    {
        this.odata = odata;
    }

    public String getAtscore()
    {
        return atscore;
    }

    public <T> void setAtscore(String atscore)
    {
        this.atscore = atscore;
    }

    public String getChoice()
    {
        return choice;
    }

    public <T> void setChoice(String choice)
    {
        this.choice = choice;
    }

    public int getRtype()
    {
        return rtype;
    }

    public <T> void setRtype(T rtype)
    {
        this.rtype=ParseUtil.makeParse(rtype,0);
    }

    public int getReading_count()
    {
        return reading_count;
    }

    public <T> void setReading_count(T reading_count)
    {
        this.reading_count=ParseUtil.makeParse(reading_count,0);
    }

    public int getEid()
    {
        return eid;
    }

    public <T> void setEid(T eid)
    {
        this.eid = ParseUtil.makeParse(eid, 0);
    }

    public String getHtname()
    {
        return htname;
    }

    public <T> void setHtname(String htname)
    {
        this.htname = htname;
    }

    public int getSam()
    {
        return sam;
    }

    public <T> void setSam(T sam)
    {
        this.sam = ParseUtil.makeParse(sam, 0);
    }

    public String getHtlogo()
    {
        return htlogo;
    }

    public <T> void setHtlogo(String htlogo)
    {
        this.htlogo = htlogo;
    }

    public int getId()
    {
        return id;
    }

    public <T> void setId(T id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public int getTournament_id()
    {
        return tournament_id;
    }

    public <T> void setTournament_id(T tournament_id)
    {
        this.tournament_id = ParseUtil.makeParse(tournament_id, 0);
    }

    public int getIsc()
    {
        return isc;
    }

    public <T> void setIsc(T isc)
    {
        this.isc = ParseUtil.makeParse(isc, 0);
    }

    public String getCont()
    {
        return cont;
    }

    public <T> void setCont(String cont)
    {
        this.cont = cont;
    }

    public int getEtype()
    {
        return etype;
    }

    public <T> void setEtype(T etype)
    {
        this.etype = ParseUtil.makeParse(etype, 0);
    }

    public String getFname()
    {
        return fname;
    }

    public <T> void setFname(String fname)
    {
        this.fname = fname;
    }

    public float getRor()
    {
        return ror;
    }

    public <T> void setRor(T ror)
    {
        this.ror = ParseUtil.makeParse(ror, 0F);
    }

    public int getBtyc()
    {
        return btyc;
    }

    public <T> void setBtyc(T btyc)
    {
        this.btyc = ParseUtil.makeParse(btyc, 0);
    }

    public String getAtname()
    {
        return atname;
    }

    public <T> void setAtname(String atname)
    {
        this.atname = atname;
    }

    public int getComcount()
    {
        return comcount;
    }

    public <T> void setComcount(T comcount)
    {
        this.comcount = ParseUtil.makeParse(comcount, 0);
    }

    public String getTitle1()
    {
        return title1;
    }

    public <T> void setTitle1(String title1)
    {
        this.title1 = title1;
    }

    public int getStatus()
    {
        return status;
    }

    public <T> void setStatus(T status)
    {
        this.status = ParseUtil.makeParse(status, 0);
    }

    public String getTitle2()
    {
        return title2;
    }

    public <T> void setTitle2(String title2)
    {
        this.title2 = title2;
    }

    public int getIs_like()
    {
        return is_like;
    }

    public <T> void setIs_like(T is_like)
    {
        this.is_like = ParseUtil.makeParse(is_like, 0);
    }

    public int getTipcount()
    {
        return tipcount;
    }

    public <T> void setTipcount(T tipcount)
    {
        this.tipcount = ParseUtil.makeParse(tipcount, 0);
    }

    public String getMtime()
    {
        return mtime;
    }

    public <T> void setMtime(String mtime)
    {
        this.mtime = mtime;
    }

    public int getFcount()
    {
        return fcount;
    }

    public <T> void setFcount(T fcount)
    {
        this.fcount = ParseUtil.makeParse(fcount, 0);
    }

    public int getMstatus()
    {
        return mstatus;
    }

    public <T> void setMstatus(T mstatus)
    {
        this.mstatus = ParseUtil.makeParse(mstatus, 0);
    }

    public int getConfidence()
    {
        return confidence;
    }

    public <T> void setConfidence(T confidence)
    {
        this.confidence = ParseUtil.makeParse(confidence, 0);
    }

    public int getIsv()
    {
        return isv;
    }

    public <T> void setIsv(T isv)
    {
        this.isv=ParseUtil.makeParse(isv,0);
    }

    public String getCtime()
    {
        return ctime;
    }

    public <T> void setCtime(String ctime)
    {
        this.ctime = ctime;
    }

    public String getHtscore()
    {
        return htscore;
    }

    public <T> void setHtscore(String htscore)
    {
        this.htscore = htscore;
    }

    public int getLike_count()
    {
        return like_count;
    }

    public <T> void setLike_count(T like_count)
    {
        this.like_count = ParseUtil.makeParse(like_count, 0);
    }

    public String getPt()
    {
        return pt;
    }

    public <T> void setPt(String pt)
    {
        this.pt = pt;
    }

    public String getTourname()
    {
        return tourname;
    }

    public <T> void setTourname(String tourname)
    {
        this.tourname = tourname;
    }

    public String getOtype()
    {
        return otype;
    }

    public <T> void setOtype(String otype)
    {
        this.otype = otype;
    }

    public float getWins()
    {
        return wins;
    }

    public <T> void setWins(T wins)
    {
        this.wins = ParseUtil.makeParse(wins, 0F);
    }

    public int getHtid()
    {
        return htid;
    }

    public <T> void setHtid(T htid)
    {
        this.htid = ParseUtil.makeParse(htid, 0);
    }

    public int getMtcount()
    {
        return mtcount;
    }

    public <T> void setMtcount(T mtcount)
    {
        this.mtcount = ParseUtil.makeParse(mtcount, 0);
    }

    public int getSettled()
    {
        return settled;
    }

    public <T> void setSettled(T settled)
    {
        this.settled = ParseUtil.makeParse(settled, 0);
    }

    public int getSorder()
    {
        return sorder;
    }

    public <T> void setSorder(T sorder)
    {
        this.sorder = ParseUtil.makeParse(sorder, 0);
    }

    public int getBoncount()
    {
        return boncount;
    }

    public <T> void setBoncount(T boncount)
    {
        this.boncount = ParseUtil.makeParse(boncount, 0);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getRichtext_type()
    {
        return richtext_type;
    }

    public <T> void setRichtext_type(T richtext_type)
    {
        this.richtext_type = ParseUtil.makeParse(richtext_type, 0);
    }

    public String getFirst_image()
    {
        return first_image;
    }

    public void setFirst_image(String first_image)
    {
        this.first_image = first_image;
    }

    public String getVid()
    {
        return vid;
    }

    public void setVid(String vid)
    {
        this.vid = vid;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public int getIsf()
    {
        return isf;
    }

    public <T> void setIsf(T isf)
    {
        this.isf = ParseUtil.makeParse(isf, 0);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.fid);
        dest.writeInt(this.atid);
        dest.writeInt(this.uid);
        dest.writeString(this.atlogo);
        dest.writeString(this.odata);
        dest.writeString(this.atscore);
        dest.writeString(this.choice);
        dest.writeInt(this.rtype);
        dest.writeInt(this.reading_count);
        dest.writeInt(this.eid);
        dest.writeString(this.htname);
        dest.writeInt(this.sam);
        dest.writeString(this.htlogo);
        dest.writeInt(this.id);
        dest.writeInt(this.tournament_id);
        dest.writeInt(this.isc);
        dest.writeString(this.cont);
        dest.writeInt(this.etype);
        dest.writeString(this.fname);
        dest.writeFloat(this.ror);
        dest.writeInt(this.btyc);
        dest.writeString(this.atname);
        dest.writeInt(this.comcount);
        dest.writeString(this.title1);
        dest.writeInt(this.status);
        dest.writeString(this.title2);
        dest.writeInt(this.is_like);
        dest.writeInt(this.tipcount);
        dest.writeString(this.mtime);
        dest.writeInt(this.fcount);
        dest.writeInt(this.mstatus);
        dest.writeInt(this.confidence);
        dest.writeInt(this.isv);
        dest.writeString(this.ctime);
        dest.writeString(this.htscore);
        dest.writeInt(this.like_count);
        dest.writeString(this.pt);
        dest.writeString(this.tourname);
        dest.writeString(this.otype);
        dest.writeFloat(this.wins);
        dest.writeInt(this.htid);
        dest.writeInt(this.mtcount);
        dest.writeInt(this.sorder);
        dest.writeInt(this.settled);
        dest.writeInt(this.boncount);
        dest.writeString(this.url);
        dest.writeInt(this.richtext_type);
        dest.writeString(this.first_image);
        dest.writeString(this.vid);
        dest.writeInt(this.isf);
    }

    public BallQTipOffEntity()
    {
    }

    protected BallQTipOffEntity(Parcel in)
    {
        this.fid = in.readInt();
        this.atid = in.readInt();
        this.uid = in.readInt();
        this.atlogo = in.readString();
        this.odata = in.readString();
        this.atscore = in.readString();
        this.choice = in.readString();
        this.rtype = in.readInt();
        this.reading_count = in.readInt();
        this.eid = in.readInt();
        this.htname = in.readString();
        this.sam = in.readInt();
        this.htlogo = in.readString();
        this.id = in.readInt();
        this.tournament_id = in.readInt();
        this.isc = in.readInt();
        this.cont = in.readString();
        this.etype = in.readInt();
        this.fname = in.readString();
        this.ror = in.readFloat();
        this.btyc = in.readInt();
        this.atname = in.readString();
        this.comcount = in.readInt();
        this.title1 = in.readString();
        this.status = in.readInt();
        this.title2 = in.readString();
        this.is_like = in.readInt();
        this.tipcount = in.readInt();
        this.mtime = in.readString();
        this.fcount = in.readInt();
        this.mstatus = in.readInt();
        this.confidence = in.readInt();
        this.isv = in.readInt();
        this.ctime = in.readString();
        this.htscore = in.readString();
        this.like_count = in.readInt();
        this.pt = in.readString();
        this.tourname = in.readString();
        this.otype = in.readString();
        this.wins = in.readFloat();
        this.htid = in.readInt();
        this.mtcount = in.readInt();
        this.sorder = in.readInt();
        this.settled = in.readInt();
        this.boncount = in.readInt();
        this.url = in.readString();
        this.richtext_type = in.readInt();
        this.first_image = in.readString();
        this.vid = in.readString();
        this.isf = in.readInt();
    }

    public static final Creator<BallQTipOffEntity> CREATOR = new Creator<BallQTipOffEntity>()
    {
        @Override
        public BallQTipOffEntity createFromParcel(Parcel source)
        {
            return new BallQTipOffEntity(source);
        }

        @Override
        public BallQTipOffEntity[] newArray(int size)
        {
            return new BallQTipOffEntity[size];
        }
    };
}
