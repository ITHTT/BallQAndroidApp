package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/5/23.
 */
public class BallQGoMatchEntity {

    /**
     * goinfo_id : 17
     * tourname_short : 斯洛超
     * ahc_odds_type : 5
     * at_name : 兹利纳
     * ht_name : 米哈洛夫采
     * ahc_odds_id : 159151
     * tourname : 斯洛伐克超级联赛
     * go_choice : MLH
     * to_odds_info : {"OO": "1.93", "UO": "1.91", "T": "2.5"}
     * go_odds_type : 5
     * match_time : 2016-05-20T18:30:00Z
     * eid : 47906
     * ahc_odds_info : {"HCH": "-0.25", "HCA": "0.25", "MLH": "2.10", "MLA": "1.75"}
     * etype : 0
     * to_odds_type : 2
     * go_odds_info : {"HCH": "0.0", "HCA": "0.0", "MLH": "1.74", "MLA": "2.02"}
     * to_odds_id : 159437
     */

    private int goinfo_id;
    private String tourname_short;
    private int ahc_odds_type;
    private String at_name;
    private String ht_name;
    private int ahc_odds_id;
    private String tourname;
    private String go_choice;
    private String to_odds_info;
    private int go_odds_type;
    private String match_time;
    private int eid;
    private String ahc_odds_info;
    private int etype;
    private int to_odds_type;
    private String go_odds_info;
    private int to_odds_id;

    private String user_choice;

    public void setGoinfo_id(int goinfo_id) {
        this.goinfo_id = goinfo_id;
    }

    public void setTourname_short(String tourname_short) {
        this.tourname_short = tourname_short;
    }

    public void setAhc_odds_type(int ahc_odds_type) {
        this.ahc_odds_type = ahc_odds_type;
    }

    public void setAt_name(String at_name) {
        this.at_name = at_name;
    }

    public void setHt_name(String ht_name) {
        this.ht_name = ht_name;
    }

    public void setAhc_odds_id(int ahc_odds_id) {
        this.ahc_odds_id = ahc_odds_id;
    }

    public void setTourname(String tourname) {
        this.tourname = tourname;
    }

    public void setGo_choice(String go_choice) {
        this.go_choice = go_choice;
    }

    public void setTo_odds_info(String to_odds_info) {
        this.to_odds_info = to_odds_info;
    }

    public void setGo_odds_type(int go_odds_type) {
        this.go_odds_type = go_odds_type;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setAhc_odds_info(String ahc_odds_info) {
        this.ahc_odds_info = ahc_odds_info;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public void setTo_odds_type(int to_odds_type) {
        this.to_odds_type = to_odds_type;
    }

    public void setGo_odds_info(String go_odds_info) {
        this.go_odds_info = go_odds_info;
    }

    public void setTo_odds_id(int to_odds_id) {
        this.to_odds_id = to_odds_id;
    }

    public int getGoinfo_id() {
        return goinfo_id;
    }

    public String getTourname_short() {
        return tourname_short;
    }

    public int getAhc_odds_type() {
        return ahc_odds_type;
    }

    public String getAt_name() {
        return at_name;
    }

    public String getHt_name() {
        return ht_name;
    }

    public int getAhc_odds_id() {
        return ahc_odds_id;
    }

    public String getTourname() {
        return tourname;
    }

    public String getGo_choice() {
        return go_choice;
    }

    public String getTo_odds_info() {
        return to_odds_info;
    }

    public int getGo_odds_type() {
        return go_odds_type;
    }

    public String getMatch_time() {
        return match_time;
    }

    public int getEid() {
        return eid;
    }

    public String getAhc_odds_info() {
        return ahc_odds_info;
    }

    public int getEtype() {
        return etype;
    }

    public int getTo_odds_type() {
        return to_odds_type;
    }

    public String getGo_odds_info() {
        return go_odds_info;
    }

    public int getTo_odds_id() {
        return to_odds_id;
    }

    public String getUser_choice() {
        return user_choice;
    }

    public void setUser_choice(String user_choice) {
        this.user_choice = user_choice;
    }
}
