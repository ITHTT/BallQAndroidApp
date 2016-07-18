package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BallQGoReplayEntity {

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

    public void setMatch_status(String match_status) {
        this.match_status = match_status;
    }

    public void setOdds_info(String odds_info) {
        this.odds_info = odds_info;
    }

    public void setTourname_short(String tourname_short) {
        this.tourname_short = tourname_short;
    }

    public void setOdds_type(int odds_type) {
        this.odds_type = odds_type;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public void setWin_amount(int win_amount) {
        this.win_amount = win_amount;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public void setAt_name(String at_name) {
        this.at_name = at_name;
    }

    public void setHt_name(String ht_name) {
        this.ht_name = ht_name;
    }

    public void setTourname(String tourname) {
        this.tourname = tourname;
    }

    public void setAt_score(String at_score) {
        this.at_score = at_score;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTips_count(int tips_count) {
        this.tips_count = tips_count;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setReturn_amount(int return_amount) {
        this.return_amount = return_amount;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public void setHt_score(String ht_score) {
        this.ht_score = ht_score;
    }

    public void setStake_amount(int stake_amount) {
        this.stake_amount = stake_amount;
    }

    public String getMatch_status() {
        return match_status;
    }

    public String getOdds_info() {
        return odds_info;
    }

    public String getTourname_short() {
        return tourname_short;
    }

    public int getOdds_type() {
        return odds_type;
    }

    public String getCtime() {
        return ctime;
    }

    public int getWin_amount() {
        return win_amount;
    }

    public int getStatus_id() {
        return status_id;
    }

    public String getAt_name() {
        return at_name;
    }

    public String getHt_name() {
        return ht_name;
    }

    public String getTourname() {
        return tourname;
    }

    public String getAt_score() {
        return at_score;
    }

    public String getChoice() {
        return choice;
    }

    public String getContent() {
        return content;
    }

    public int getTips_count() {
        return tips_count;
    }

    public String getMatch_time() {
        return match_time;
    }

    public int getStatus() {
        return status;
    }

    public int getEid() {
        return eid;
    }

    public int getReturn_amount() {
        return return_amount;
    }

    public int getEtype() {
        return etype;
    }

    public String getHt_score() {
        return ht_score;
    }

    public int getStake_amount() {
        return stake_amount;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }
}
