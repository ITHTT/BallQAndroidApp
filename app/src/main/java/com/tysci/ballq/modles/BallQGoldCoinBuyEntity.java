package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/7/7.
 */
public class BallQGoldCoinBuyEntity
{
    private String name;
    private String exchange_type;
    private String content;
    private int foreign_currency;
    private int local_currency;
    private int id;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getExchange_type()
    {
        return exchange_type;
    }

    public void setExchange_type(String exchange_type)
    {
        this.exchange_type = exchange_type;
    }

    public int getForeign_currency()
    {
        return foreign_currency;
    }

    public <INTEGER> void setForeign_currency(INTEGER foreign_currency)
    {
        this.foreign_currency = ParseUtil.makeParse(foreign_currency, 0);
    }

    public int getId()
    {
        return id;
    }

    public <INTEGER> void setId(INTEGER id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public int getLocal_currency()
    {
        return local_currency;
    }

    public <INTEGER> void setLocal_currency(INTEGER local_currency)
    {
        this.local_currency = ParseUtil.makeParse(local_currency, 0);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
