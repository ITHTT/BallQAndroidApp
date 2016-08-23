package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/5/31.
 */
public class BallQBallWarpInfoEntity implements Parcelable
{
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

    public void setCont(String cont)
    {
        this.cont = cont;
    }

    public <T> void setUid(T uid)
    {
        this.uid = ParseUtil.makeParse(uid, 0);
    }

    public void setShare(String share)
    {
        this.share = share;
    }

    public <T> void setLike_count(T like_count)
    {
        this.like_count = ParseUtil.makeParse(like_count, 0);
    }

    public <T> void setReading_count(T reading_count)
    {
        this.reading_count = ParseUtil.makeParse(reading_count, 0);
    }

    public <T> void setId(T id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public <T> void setIs_like(T is_like)
    {
        this.is_like = ParseUtil.makeParse(is_like, 0);
    }

    public void setPt(String pt)
    {
        this.pt = pt;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public <T> void setArtcount(T artcount)
    {
        this.artcount = ParseUtil.makeParse(artcount, 0);
    }

    public <T> void setFid(T fid)
    {
        this.fid = ParseUtil.makeParse(fid, 0);
    }

    public <T> void setBoncount(T boncount)
    {
        this.boncount = ParseUtil.makeParse(boncount, 0);
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public void setExcerpt(String excerpt)
    {
        this.excerpt = excerpt;
    }

    public void setTitle1(String title1)
    {
        this.title1 = title1;
    }

    public void setTitle2(String title2)
    {
        this.title2 = title2;
    }

    public <T> void setComcount(T comcount)
    {
        this.comcount = ParseUtil.makeParse(comcount, 0);
    }

    public void setCtime(String ctime)
    {
        this.ctime = ctime;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public <T> void setIsv(T isv)
    {
        this.isv = ParseUtil.makeParse(isv, 0);
    }

    public <T> void setIsc(T isc)
    {
        this.isc = ParseUtil.makeParse(isc, 0);
    }

    public <T> void setIsf(T isf)
    {
        this.isf = ParseUtil.makeParse(isf, 0);
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getCont()
    {
        return cont;
    }

    public int getUid()
    {
        return uid;
    }

    public String getShare()
    {
        return share;
    }

    public int getLike_count()
    {
        return like_count;
    }

    public int getReading_count()
    {
        return reading_count;
    }

    public int getId()
    {
        return id;
    }

    public int getIs_like()
    {
        return is_like;
    }

    public String getPt()
    {
        return pt;
    }

    public String getTitle()
    {
        return title;
    }

    public int getArtcount()
    {
        return artcount;
    }

    public int getFid()
    {
        return fid;
    }

    public int getBoncount()
    {
        return boncount;
    }

    public String getFname()
    {
        return fname;
    }

    public String getExcerpt()
    {
        return excerpt;
    }

    public String getTitle1()
    {
        return title1;
    }

    public String getTitle2()
    {
        return title2;
    }

    public int getComcount()
    {
        return comcount;
    }

    public String getCtime()
    {
        return ctime;
    }

    public String getCover()
    {
        return cover;
    }

    public int getIsv()
    {
        return isv;
    }

    public int getIsc()
    {
        return isc;
    }

    public int getIsf()
    {
        return isf;
    }

    public String getUrl()
    {
        return url;
    }

    public static Creator<BallQBallWarpInfoEntity> getCREATOR()
    {
        return CREATOR;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
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

    public BallQBallWarpInfoEntity()
    {
    }

    private BallQBallWarpInfoEntity(Parcel in)
    {
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

    public static final Creator<BallQBallWarpInfoEntity> CREATOR = new Creator<BallQBallWarpInfoEntity>()
    {
        public BallQBallWarpInfoEntity createFromParcel(Parcel source)
        {
            return new BallQBallWarpInfoEntity(source);
        }

        public BallQBallWarpInfoEntity[] newArray(int size)
        {
            return new BallQBallWarpInfoEntity[size];
        }
    };
}
