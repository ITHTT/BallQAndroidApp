package com.tysci.ballq.modles;

import com.tysci.ballq.utils.ParseUtil;

/**
 * Created by Administrator on 2016/6/15.
 */
public class BallQMatchLeagueTableEntity
{

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

    public int getRanking()
    {
        return ranking;
    }

    public int getMatches_total()
    {
        return matches_total;
    }

    public int getPoints_total()
    {
        return points_total;
    }

    public String getTeam_logo()
    {
        return team_logo;
    }

    public int getDraw_total()
    {
        return draw_total;
    }

    public int getTeam_id()
    {
        return team_id;
    }

    public int getLost_total()
    {
        return lost_total;
    }

    public String getTeam()
    {
        return team;
    }

    public int getWin_total()
    {
        return win_total;
    }

    public int getGoals_total()
    {
        return goals_total;
    }

    public int getId()
    {
        return id;
    }

    public int getLose_total()
    {
        return lose_total;
    }

    public <INT> void setRanking(INT ranking)
    {
        this.ranking = ParseUtil.makeParse(ranking, 0);
    }

    public <INT> void setMatches_total(INT matches_total)
    {
        this.matches_total = ParseUtil.makeParse(matches_total, 0);
    }

    public <INT> void setPoints_total(INT points_total)
    {
        this.points_total = ParseUtil.makeParse(points_total, 0);
    }

    public <INT> void setTeam_logo(String team_logo)
    {
        this.team_logo = team_logo;
    }

    public <INT> void setDraw_total(INT draw_total)
    {
        this.draw_total = ParseUtil.makeParse(draw_total, 0);
    }

    public <INT> void setTeam_id(INT team_id)
    {
        this.team_id = ParseUtil.makeParse(team_id, 0);
    }

    public <INT> void setLost_total(INT lost_total)
    {
        this.lost_total = ParseUtil.makeParse(lost_total, 0);
    }

    public <INT> void setTeam(String team)
    {
        this.team = team;
    }

    public <INT> void setWin_total(INT win_total)
    {
        this.win_total = ParseUtil.makeParse(win_total, 0);
    }

    public <INT> void setGoals_total(INT goals_total)
    {
        this.goals_total = ParseUtil.makeParse(goals_total, 0);
    }

    public <INT> void setId(INT id)
    {
        this.id = ParseUtil.makeParse(id, 0);
    }

    public <INT> void setLose_total(INT lose_total)
    {
        this.lose_total = ParseUtil.makeParse(lose_total, 0);
    }
}
