package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016/6/15.
 */
public class BallQMatchLeagueTableEntity {

    /**
     * ranking : 1
     * matches_total : 9
     * points_total : 22
     * team_logo : http://static-cdn.ballq.cn/teams/c51e119b-3dbd-4697-acff-c37527fcc2de.jpg
     * draw_total : 1
     * team_id : 787
     * lost_total : 8
     * team : 瓦斯科达伽马
     * win_total : 7
     * goals_total : 19
     * id : 7682
     * lose_total : 1
     */

    private int ranking;
    private int matches_total;
    private int points_total;
    private String team_logo;
    private int draw_total;
    private int team_id;
    private int lost_total;
    private String team;
    private int win_total;
    private int goals_total;
    private int id;
    private int lose_total;

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setMatches_total(int matches_total) {
        this.matches_total = matches_total;
    }

    public void setPoints_total(int points_total) {
        this.points_total = points_total;
    }

    public void setTeam_logo(String team_logo) {
        this.team_logo = team_logo;
    }

    public void setDraw_total(int draw_total) {
        this.draw_total = draw_total;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public void setLost_total(int lost_total) {
        this.lost_total = lost_total;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setWin_total(int win_total) {
        this.win_total = win_total;
    }

    public void setGoals_total(int goals_total) {
        this.goals_total = goals_total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLose_total(int lose_total) {
        this.lose_total = lose_total;
    }

    public int getRanking() {
        return ranking;
    }

    public int getMatches_total() {
        return matches_total;
    }

    public int getPoints_total() {
        return points_total;
    }

    public String getTeam_logo() {
        return team_logo;
    }

    public int getDraw_total() {
        return draw_total;
    }

    public int getTeam_id() {
        return team_id;
    }

    public int getLost_total() {
        return lost_total;
    }

    public String getTeam() {
        return team;
    }

    public int getWin_total() {
        return win_total;
    }

    public int getGoals_total() {
        return goals_total;
    }

    public int getId() {
        return id;
    }

    public int getLose_total() {
        return lose_total;
    }
}
