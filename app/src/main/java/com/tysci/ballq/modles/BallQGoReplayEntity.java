package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BallQGoReplayEntity
{

    /**
     * match_status : 完场
     * odds_info : {"HCH": "1.0", "HCA": "-1.0", "MLH": "1.74", "MLA": "2.00"}
     * tourname_short : 欧冠
     * odds_type : 5
     * ctime : 2016-07-10T21:41:03.791Z
     * win_amount : 0
     * status_id : 100
     * at_name : 萨格勒布迪纳摩
     * ht_name : 瓦尔达
     * tourname : 欧洲冠军联赛
     * at_score : 2
     * choice : MLH
     * content :
     * tips_count : 0
     * match_time : 2016-07-12T18:45:00Z
     * status : 3
     * eid : 121644
     * return_amount : 1000
     * etype : 0
     * ht_score : 1
     * stake_amount : 1000
     */

    private String match_status;
    private String odds_info;
    private String tourname_short;
    private int odds_type;
    private String ctime;
    private int win_amount;
    private int status_id;
    private String at_name;
    private String ht_name;
    private String tourname;
    private String at_score;
    private String choice;
    private String content;
    private int tips_count;
    private String match_time;
    private int status;
    private int eid;
    private int return_amount;
    private int etype;
    private String ht_score;
    private int stake_amount;
    private String matchTime;

    public String getMatch_status()
    {
        return match_status;
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

    public String getCtime()
    {
        return ctime;
    }

    public int getWin_amount()
    {
        return win_amount;
    }

    public int getStatus_id()
    {
        return status_id;
    }

    public String getAt_name()
    {
        return at_name;
    }

    public String getHt_name()
    {
        return ht_name;
    }

    public String getTourname()
    {
        return tourname;
    }

    public String getAt_score()
    {
        return at_score;
    }

    public String getChoice()
    {
        return choice;
    }

    public String getContent()
    {
        return content;
    }

    public int getTips_count()
    {
        return tips_count;
    }

    public String getMatch_time()
    {
        return match_time;
    }

    public int getStatus()
    {
        return status;
    }

    public int getEid()
    {
        return eid;
    }

    public int getReturn_amount()
    {
        return return_amount;
    }

    public int getEtype()
    {
        return etype;
    }

    public String getHt_score()
    {
        return ht_score;
    }

    public int getStake_amount()
    {
        return stake_amount;
    }

    public String getMatchTime()
    {
        return matchTime;
    }

    public void setMatch_status(String match_status)
    {
        this.match_status = match_status;
    }

    public void setOdds_info(String odds_info)
    {
        this.odds_info = odds_info;
    }

    public void setTourname_short(String tourname_short)
    {
        this.tourname_short = tourname_short;
    }

    public <INT> void setOdds_type(INT odds_type)
    {
        this.odds_type = ParseUtil.makeParse(odds_type, 0);
    }

    public <INT> void setCtime(String ctime)
    {
        this.ctime = ctime;
    }

    public <INT> void setWin_amount(INT win_amount)
    {
        this.win_amount = ParseUtil.makeParse(win_amount, 0);
    }

    public <INT> void setStatus_id(INT status_id)
    {
        this.status_id = ParseUtil.makeParse(status_id, 0);
    }

    public <INT> void setAt_name(String at_name)
    {
        this.at_name = at_name;
    }

    public <INT> void setHt_name(String ht_name)
    {
        this.ht_name = ht_name;
    }

    public <INT> void setTourname(String tourname)
    {
        this.tourname = tourname;
    }

    public <INT> void setAt_score(String at_score)
    {
        this.at_score = at_score;
    }

    public <INT> void setChoice(String choice)
    {
        this.choice = choice;
    }

    public <INT> void setContent(String content)
    {
        this.content = content;
    }

    public <INT> void setTips_count(INT tips_count)
    {
        this.tips_count = ParseUtil.makeParse(tips_count, 0);
    }

    public <INT> void setMatch_time(String match_time)
    {
        this.match_time = match_time;
    }

    public <INT> void setStatus(INT status)
    {
        this.status = ParseUtil.makeParse(status, 0);
    }

    public <INT> void setEid(INT eid)
    {
        this.eid = ParseUtil.makeParse(eid, 0);
    }

    public <INT> void setReturn_amount(INT return_amount)
    {
        this.return_amount = ParseUtil.makeParse(return_amount, 0);
    }

    public <INT> void setEtype(INT etype)
    {
        this.etype = ParseUtil.makeParse(etype, 0);
    }

    public <INT> void setHt_score(String ht_score)
    {
        this.ht_score = ht_score;
    }

    public <INT> void setStake_amount(INT stake_amount)
    {
        this.stake_amount = ParseUtil.makeParse(stake_amount, 0);
    }

    public <INT> void setMatchTime(String matchTime)
    {
        this.matchTime = matchTime;
    }
}
