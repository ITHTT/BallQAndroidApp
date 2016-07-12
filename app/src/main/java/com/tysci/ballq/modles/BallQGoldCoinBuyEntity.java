package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/7/7.
 */
public class BallQGoldCoinBuyEntity {
    private String name;
    private String exchange_type;
    private String content;
    private int foreign_currency;
    private int local_currency;
    private int id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExchange_type() {
        return exchange_type;
    }

    public void setExchange_type(String exchange_type) {
        this.exchange_type = exchange_type;
    }

    public int getForeign_currency() {
        return foreign_currency;
    }

    public void setForeign_currency(int foreign_currency) {
        this.foreign_currency = foreign_currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocal_currency() {
        return local_currency;
    }

    public void setLocal_currency(int local_currency) {
        this.local_currency = local_currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
