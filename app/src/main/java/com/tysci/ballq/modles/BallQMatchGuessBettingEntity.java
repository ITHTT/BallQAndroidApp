package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/5/10.
 */
public class BallQMatchGuessBettingEntity implements Parcelable
{
    private int id;
    private int AO_cnt;
    private int HO_cnt;
    private int UO_cnt;
    private int OO_cnt;
    private int DO_cnt;
    private int MLA_cnt;
    private int MLH_cnt;
    private String odata;
    private String otype;
    private int eid;
    private int etype;
    private int dataType;//0表示竞猜数据，1表示投注数据;

    private String bettingInfo;
    private int bettingMoney;
    private String bettingType;

    public int getId()
    {
        return id;
    }

    public int getAO_cnt()
    {
        return AO_cnt;
    }

    public int getHO_cnt()
    {
        return HO_cnt;
    }

    public int getUO_cnt()
    {
        return UO_cnt;
    }

    public int getOO_cnt()
    {
        return OO_cnt;
    }

    public int getDO_cnt()
    {
        return DO_cnt;
    }

    public int getMLA_cnt()
    {
        return MLA_cnt;
    }

    public int getMLH_cnt()
    {
        return MLH_cnt;
    }

    public String getOdata()
    {
        return odata;
    }

    public String getOtype()
    {
        return otype;
    }

    public int getEid()
    {
        return eid;
    }

    public int getEtype()
    {
        return etype;
    }

    public int getDataType()
    {
        return dataType;
    }

    public String getBettingInfo()
    {
        return bettingInfo;
    }

    public int getBettingMoney()
    {
        return bettingMoney;
    }

    public String getBettingType()
    {
        return bettingType;
    }

    public static Creator<BallQMatchGuessBettingEntity> getCREATOR()
    {
        return CREATOR;
    }

    public <INT> void setId(INT id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public <INT> void setAO_cnt(INT AO_cnt)
    {
        this.AO_cnt = ParseUtil.makeParse(AO_cnt, 0);
    }

    public <INT> void setHO_cnt(INT HO_cnt)
    {
        this.HO_cnt = ParseUtil.makeParse(HO_cnt, 0);
    }

    public <INT> void setUO_cnt(INT UO_cnt)
    {
        this.UO_cnt = ParseUtil.makeParse(UO_cnt, 0);
    }

    public <INT> void setOO_cnt(INT OO_cnt)
    {
        this.OO_cnt = ParseUtil.makeParse(OO_cnt, 0);
    }

    public <INT> void setDO_cnt(INT DO_cnt)
    {
        this.DO_cnt = ParseUtil.makeParse(DO_cnt, 0);
    }

    public <INT> void setMLA_cnt(INT MLA_cnt)
    {
        this.MLA_cnt = ParseUtil.makeParse(MLA_cnt, 0);
    }

    public <INT> void setMLH_cnt(INT MLH_cnt)
    {
        this.MLH_cnt = ParseUtil.makeParse(MLH_cnt, 0);
    }

    public <INT> void setOdata(String odata)
    {
        this.odata = odata;
    }

    public <INT> void setOtype(String otype)
    {
        this.otype = otype;
    }

    public <INT> void setEid(INT eid)
    {
        this.eid = ParseUtil.makeParse(eid, 0);
    }

    public <INT> void setEtype(INT etype)
    {
        this.etype = ParseUtil.makeParse(etype, 0);
    }

    public <INT> void setDataType(INT dataType)
    {
        this.dataType = ParseUtil.makeParse(dataType, 0);
    }

    public <INT> void setBettingInfo(String bettingInfo)
    {
        this.bettingInfo = bettingInfo;
    }

    public <INT> void setBettingMoney(INT bettingMoney)
    {
        this.bettingMoney = ParseUtil.makeParse(bettingMoney, 0);
    }

    public void setBettingType(String bettingType)
    {
        this.bettingType = bettingType;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeInt(this.AO_cnt);
        dest.writeInt(this.HO_cnt);
        dest.writeInt(this.UO_cnt);
        dest.writeInt(this.OO_cnt);
        dest.writeInt(this.DO_cnt);
        dest.writeInt(this.MLA_cnt);
        dest.writeInt(this.MLH_cnt);
        dest.writeString(this.odata);
        dest.writeString(this.otype);
        dest.writeInt(this.eid);
        dest.writeInt(this.etype);
        dest.writeInt(this.dataType);
        dest.writeString(this.bettingInfo);
        dest.writeInt(this.bettingMoney);
        dest.writeString(this.bettingType);
    }

    public BallQMatchGuessBettingEntity()
    {
    }

    private BallQMatchGuessBettingEntity(Parcel in)
    {
        this.id = in.readInt();
        this.AO_cnt = in.readInt();
        this.HO_cnt = in.readInt();
        this.UO_cnt = in.readInt();
        this.OO_cnt = in.readInt();
        this.DO_cnt = in.readInt();
        this.MLA_cnt = in.readInt();
        this.MLH_cnt = in.readInt();
        this.odata = in.readString();
        this.otype = in.readString();
        this.eid = in.readInt();
        this.etype = in.readInt();
        this.dataType = in.readInt();
        this.bettingInfo = in.readString();
        this.bettingMoney = in.readInt();
        this.bettingType = in.readString();
    }

    public static final Creator<BallQMatchGuessBettingEntity> CREATOR = new Creator<BallQMatchGuessBettingEntity>()
    {
        public BallQMatchGuessBettingEntity createFromParcel(Parcel source)
        {
            return new BallQMatchGuessBettingEntity(source);
        }

        public BallQMatchGuessBettingEntity[] newArray(int size)
        {
            return new BallQMatchGuessBettingEntity[size];
        }
    };
}
