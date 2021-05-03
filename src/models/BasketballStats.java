package models;

import java.util.HashMap;

public class BasketballStats extends GameStats{
    private String points;
    private String assists;
    private String rebounds;
    private String steals;
    private String blocks;

    public BasketballStats(TeamMember player, String points, String assists, String rebounds, String steals, String blocks) {
        super(player);
        this.points = points;
        this.assists = assists;
        this.rebounds = rebounds;
        this.steals = steals;
        this.blocks = blocks;
    }


    public String getFirstStat() {
        return points;
    }

    public String getSecondStat() {
        return assists;
    }

    public String getThirdStat() {
        return rebounds;
    }

    public String getForthStat() {
        return steals;
    }

    public String getFifthStat() {
        return blocks;
    }

    @Override
    public void setFirstStat(String data) {
        points = data;
    }

    @Override
    public void setSecondStat(String data) {
        assists = data;
    }

    @Override
    public void setThirdStat(String data) {
        rebounds = data;
    }

    @Override
    public void setForthStat(String data) {
        steals = data;
    }

    @Override
    public void setFifthStat(String data) {
        blocks = data;
    }
}
