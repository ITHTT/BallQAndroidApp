package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by HTT on 2016/7/7.
 */
public class BallQAuthorAnalystsEntity
{
    private String rank_type;
    private int uid;
    private String pt;
    private int r_type;
    private float wins;
    private int rank;
    private String note;
    private int earnings;
    private String fname;
    private float ror;
    private int fcount;
    private int isf;
    private int tips_count;
    private int v_status;

    public String getRank_type()
    {
        return rank_type;
    }

    public int getUid()
    {
        return uid;
    }

    public String getPt()
    {
        return pt;
    }

    public int getR_type()
    {
        return r_type;
    }

    public float getWins()
    {
        return wins;
    }

    public int getRank()
    {
        return rank;
    }

    public String getNote()
    {
        return note;
    }

    public int getEarnings()
    {
        return earnings;
    }

    public String getFname()
    {
        return fname;
    }

    public float getRor()
    {
        return ror;
    }

    public int getFcount()
    {
        return fcount;
    }

    public int getIsf()
    {
        return isf;
    }

    public int getTips_count()
    {
        return tips_count;
    }

    public int getV_status()
    {
        return v_status;
    }

    public void setRank_type(String rank_type)
    {
        this.rank_type = rank_type;
    }

    public <T> void setUid(T uid)
    {
        this.uid = ParseUtil.makeParse(uid, 0);
    }

    public void setPt(String pt)
    {
        this.pt = pt;
    }

    public <T> void setR_type(T r_type)
    {
        this.r_type = ParseUtil.makeParse(r_type, 0);
    }

    public void setWins(float wins)
    {
        this.wins = wins;
    }

    public <T> void setRank(T rank)
    {
        this.rank = ParseUtil.makeParse(rank, 0);
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public <T> void setEarnings(T earnings)
    {
        this.earnings = ParseUtil.makeParse(earnings, 0);
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public void setRor(float ror)
    {
        this.ror = ror;
    }

    public <T> void setFcount(T fcount)
    {
        this.fcount = ParseUtil.makeParse(fcount, 0);
    }

    public <T> void setIsf(T isf)
    {
        this.isf = ParseUtil.makeParse(isf, 0);
    }

    public <T> void setTips_count(T tips_count)
    {
        this.tips_count = ParseUtil.makeParse(tips_count, 0);
    }

    public <T> void setV_status(T v_status)
    {
        this.v_status = ParseUtil.makeParse(v_status, 0);
    }
}
