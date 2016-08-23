package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/6/28.
 */
public class BallQMatchBettingScaleEntity
{
    private String bettingType;
    private boolean isBigSmall;
    private int ac;
    private int hr;
    private int hs;
    private int as;
    private int s;
    private int r;
    private float t;
    private int hc;
    private int ar;

    private int oc;
    private int us;
    private int ur;
    private int os;
    private int or;
    private int uc;

    private int dc;
    private int ds;
    private int dr;

    public String getBettingType()
    {
        return bettingType;
    }

    public boolean isBigSmall()
    {
        return isBigSmall;
    }

    public int getAc()
    {
        return ac;
    }

    public int getHr()
    {
        return hr;
    }

    public int getHs()
    {
        return hs;
    }

    public int getAs()
    {
        return as;
    }

    public int getS()
    {
        return s;
    }

    public int getR()
    {
        return r;
    }

    public float getT()
    {
        return t;
    }

    public int getHc()
    {
        return hc;
    }

    public int getAr()
    {
        return ar;
    }

    public int getOc()
    {
        return oc;
    }

    public int getUs()
    {
        return us;
    }

    public int getUr()
    {
        return ur;
    }

    public int getOs()
    {
        return os;
    }

    public int getOr()
    {
        return or;
    }

    public int getUc()
    {
        return uc;
    }

    public int getDc()
    {
        return dc;
    }

    public int getDs()
    {
        return ds;
    }

    public int getDr()
    {
        return dr;
    }

    public void setBettingType(String bettingType)
    {
        this.bettingType = bettingType;
    }

    public void setIsBigSmall(boolean isBigSmall)
    {
        this.isBigSmall = isBigSmall;
    }

    public <INT> void setAc(INT ac)
    {
        this.ac = ParseUtil.makeParse(ac, 0);
    }

    public <INT> void setHr(INT hr)
    {
        this.hr = ParseUtil.makeParse(hr, 0);
    }

    public <INT> void setHs(INT hs)
    {
        this.hs = ParseUtil.makeParse(hs, 0);
    }

    public <INT> void setAs(INT as)
    {
        this.as = ParseUtil.makeParse(as, 0);
    }

    public <INT> void setS(INT s)
    {
        this.s = ParseUtil.makeParse(s, 0);
    }

    public <INT> void setR(INT r)
    {
        this.r = ParseUtil.makeParse(r, 0);
    }

    public <FLOAT> void setT(FLOAT t)
    {
        this.t = ParseUtil.makeParse(t, 0f);
    }

    public <INT> void setHc(INT hc)
    {
        this.hc = ParseUtil.makeParse(hc, 0);
    }

    public <INT> void setAr(INT ar)
    {
        this.ar = ParseUtil.makeParse(ar, 0);
    }

    public <INT> void setOc(INT oc)
    {
        this.oc = ParseUtil.makeParse(oc, 0);
    }

    public <INT> void setUs(INT us)
    {
        this.us = ParseUtil.makeParse(us, 0);
    }

    public <INT> void setUr(INT ur)
    {
        this.ur = ParseUtil.makeParse(ur, 0);
    }

    public <INT> void setOs(INT os)
    {
        this.os = ParseUtil.makeParse(os, 0);
    }

    public <INT> void setOr(INT or)
    {
        this.or = ParseUtil.makeParse(or, 0);
    }

    public <INT> void setUc(INT uc)
    {
        this.uc = ParseUtil.makeParse(uc, 0);
    }

    public <INT> void setDc(INT dc)
    {
        this.dc = ParseUtil.makeParse(dc, 0);
    }

    public <INT> void setDs(INT ds)
    {
        this.ds = ParseUtil.makeParse(ds, 0);
    }

    public <INT> void setDr(INT dr)
    {
        this.dr = ParseUtil.makeParse(dr, 0);
    }
}
