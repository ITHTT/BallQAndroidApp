package com.tysci.ballq.modles;

import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
public class BallQMatchLineupEntity {

    /**
     * start_xi :
     * goals_in_tournamet :
     * matches_played_in_tournament :
     */

    private MatchStatusEntity status;
    /**
     * status : {"start_xi":"","goals_in_tournamet":"","matches_played_in_tournament":""}
     * name : 克莱顿 多纳尔德森
     * nationality_url : http://ballq.oss-cn-beijing.aliyuncs.com/countries/f7da183f-74b2-4a91-a54c-709a277f4b4e.png
     * pos_GMDF : F
     * shirt_number : 8
     * nationality : JAM
     * position : 10
     * id : 27091
     * substitute : 0
     */

    private String name;
    private String nationality_url;
    private String pos_GMDF;
    private int shirt_number;
    private String nationality;
    private String position;
    private String id;
    private int substitute;
    /**阵型名*/
    private String lineupFormation;
    private String lineupTitle;
    private List<Integer>map;



    public void setStatus(MatchStatusEntity status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationality_url(String nationality_url) {
        this.nationality_url = nationality_url;
    }

    public void setPos_GMDF(String pos_GMDF) {
        this.pos_GMDF = pos_GMDF;
    }

    public void setShirt_number(int shirt_number) {
        this.shirt_number = shirt_number;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubstitute(int substitute) {
        this.substitute = substitute;
    }

    public MatchStatusEntity getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getNationality_url() {
        return nationality_url;
    }

    public String getPos_GMDF() {
        return pos_GMDF;
    }

    public int getShirt_number() {
        return shirt_number;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public int getSubstitute() {
        return substitute;
    }

    public String getLineupFormation() {
        return lineupFormation;
    }

    public void setLineupFormation(String lineupFormation) {
        this.lineupFormation = lineupFormation;
    }

    public String getLineupTitle() {
        return lineupTitle;
    }

    public void setLineupTitle(String lineupTitle) {
        this.lineupTitle = lineupTitle;
    }

    public List<Integer> getMap() {
        return map;
    }

    public void setMap(List<Integer> map) {
        this.map = map;
    }

    public static class MatchStatusEntity {
        private String start_xi;
        private String goals_in_tournamet;
        private String matches_played_in_tournament;

        public void setStart_xi(String start_xi) {
            this.start_xi = start_xi;
        }

        public void setGoals_in_tournamet(String goals_in_tournamet) {
            this.goals_in_tournamet = goals_in_tournamet;
        }

        public void setMatches_played_in_tournament(String matches_played_in_tournament) {
            this.matches_played_in_tournament = matches_played_in_tournament;
        }

        public String getStart_xi() {
            return start_xi;
        }

        public String getGoals_in_tournamet() {
            return goals_in_tournamet;
        }

        public String getMatches_played_in_tournament() {
            return matches_played_in_tournament;
        }
    }
}
