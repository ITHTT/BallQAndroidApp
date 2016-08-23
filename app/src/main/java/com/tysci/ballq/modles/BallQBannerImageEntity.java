package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by HTT on 2016/7/7.
 */
public class BallQBannerImageEntity
{
    private String jump_type;
    private String pic_url;
    private String jump_url;
    private int id;

    public int getId()
    {
        return id;
    }

    public <T> void setId(T id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public String getJump_type()
    {
        return jump_type;
    }

    public void setJump_type(String jump_type)
    {
        this.jump_type = jump_type;
    }

    public String getJump_url()
    {
        return jump_url;
    }

    public void setJump_url(String jump_url)
    {
        this.jump_url = jump_url;
    }

    public String getPic_url()
    {
        return pic_url;
    }

    public void setPic_url(String pic_url)
    {
        this.pic_url = pic_url;
    }
}
