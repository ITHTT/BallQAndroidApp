package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BallQGoBettingRecordEntity
{

    /**
     * status : 0
     * bet_status : 2
     * go_choice : OO
     * go_odds_type : 2
     * go_stake_amount : 1000
     * go_odds_info : {"OO": "1.81", "UO": "2.10", "T": "2.5"}
     * at_name : 阿尔纳斯尔
     * go_ctime : 2016-05-24T01:38:53.653Z
     * go_return_amount : 0
     * stake_amount : 1000
     * etype : 0
     * odds_info : {"OO": "1.81", "UO": "2.10", "T": "2.5"}
     * tourname_short : 亚冠
     * odds_type : 2
     * tourname : 亚洲冠军联赛
     * go_status : 0
     * choice : OO
     * match_time : 2016-05-24T15:30:00Z
     * return_amount : 0
     * ctime : 2016-05-24T06:03:01.396Z
     * win_amount : -1000
     * ht_name : 泰拉克托
     * eid : 114416
     * yield_gap : 0
     */

    private int status;
    private int bet_status;
    private String go_choice;
    private int go_odds_type;
    private int go_stake_amount;
    private String go_odds_info;
    private String at_name;
    private String go_ctime;
    private int go_return_amount;
    private int stake_amount;
    private int etype;
    private String odds_info;
    private String tourname_short;
    private int odds_type;
    private String tourname;
    private int go_status;
    private String choice;
    private String match_time;
    private int return_amount;
    private String ctime;
    private int win_amount;
    private String ht_name;
    private int eid;
    private int yield_gap;
    private String betting_time;

    public int getStatus()
    {
        return status;
    }

    public int getBet_status()
    {
        return bet_status;
    }

    public String getGo_choice()
    {
        return go_choice;
    }

    public int getGo_odds_type()
    {
        return go_odds_type;
    }

    public int getGo_stake_amount()
    {
        return go_stake_amount;
    }

    public String getGo_odds_info()
    {
        return go_odds_info;
    }

    public String getAt_name()
    {
        return at_name;
    }

    public String getGo_ctime()
    {
        return go_ctime;
    }

    public int getGo_return_amount()
    {
        return go_return_amount;
    }

    public int getStake_amount()
    {
        return stake_amount;
    }

    public int getEtype()
    {
        return etype;
    }

    public String getOdds_info()
    {
        return odds_info;
    }

    public String getTourname_short()
    {
        return tourname_short;
    }

    public int getOdds_type()
    {
        return odds_type;
    }

    public String getTourname()
    {
        return tourname;
    }

    public int getGo_status()
    {
        return go_status;
    }

    public String getChoice()
    {
        return choice;
    }

    public String getMatch_time()
    {
        return match_time;
    }

    public int getReturn_amount()
    {
        return return_amount;
    }

    public String getCtime()
    {
        return ctime;
    }

    public int getWin_amount()
    {
        return win_amount;
    }

    public String getHt_name()
    {
        return ht_name;
    }

    public int getEid()
    {
        return eid;
    }

    public int getYield_gap()
    {
        return yield_gap;
    }

    public String getBetting_time()
    {
        return betting_time;
    }

    public <INTEGER> void setStatus(INTEGER status)
    {
        this.status = ParseUtil.makeParse(status, 0);
    }

    public <INTEGER> void setBet_status(INTEGER bet_status)
    {
        this.bet_status = ParseUtil.makeParse(bet_status, 0);
    }

    public void setGo_choice(String go_choice)
    {
        this.go_choice = go_choice;
    }

    public <INTEGER> void setGo_odds_type(INTEGER go_odds_type)
    {
        this.go_odds_type = ParseUtil.makeParse(go_odds_type, 0);
    }

    public <INTEGER> void setGo_stake_amount(INTEGER go_stake_amount)
    {
        this.go_stake_amount = ParseUtil.makeParse(go_stake_amount, 0);
    }

    public void setGo_odds_info(String go_odds_info)
    {
        this.go_odds_info = go_odds_info;
    }

    public void setAt_name(String at_name)
    {
        this.at_name = at_name;
    }

    public void setGo_ctime(String go_ctime)
    {
        this.go_ctime = go_ctime;
    }

    public <INTEGER> void setGo_return_amount(INTEGER go_return_amount)
    {
        this.go_return_amount = ParseUtil.makeParse(go_return_amount, 0);
    }

    public <INTEGER> void setStake_amount(INTEGER stake_amount)
    {
        this.stake_amount = ParseUtil.makeParse(stake_amount, 0);
    }

    public <INTEGER> void setEtype(INTEGER etype)
    {
        this.etype = ParseUtil.makeParse(etype, 0);
    }

    public void setOdds_info(String odds_info)
    {
        this.odds_info = odds_info;
    }

    public void setTourname_short(String tourname_short)
    {
        this.tourname_short = tourname_short;
    }

    public <INTEGER> void setOdds_type(INTEGER odds_type)
    {
        this.odds_type = ParseUtil.makeParse(odds_type, 0);
    }

    public void setTourname(String tourname)
    {
        this.tourname = tourname;
    }

    public <INTEGER> void setGo_status(INTEGER go_status)
    {
        this.go_status = ParseUtil.makeParse(go_status, 0);
    }

    public void setChoice(String choice)
    {
        this.choice = choice;
    }

    public void setMatch_time(String match_time)
    {
        this.match_time = match_time;
    }

    public <INTEGER> void setReturn_amount(INTEGER return_amount)
    {
        this.return_amount = ParseUtil.makeParse(return_amount, 0);
    }

    public void setCtime(String ctime)
    {
        this.ctime = ctime;
    }

    public <INTEGER> void setWin_amount(INTEGER win_amount)
    {
        this.win_amount = ParseUtil.makeParse(win_amount, 0);
    }

    public void setHt_name(String ht_name)
    {
        this.ht_name = ht_name;
    }

    public <INTEGER> void setEid(INTEGER eid)
    {
        this.eid = ParseUtil.makeParse(eid, 0);
    }

    public <INTEGER> void setYield_gap(INTEGER yield_gap)
    {
        this.yield_gap = ParseUtil.makeParse(yield_gap, 0);
    }

    public void setBetting_time(String betting_time)
    {
        this.betting_time = betting_time;
    }
}
