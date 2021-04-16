package models;

public class BasketballStats extends GameStats{
    private int points;
    private int assists;
    private int rebounds;
    private int steals;
    private int blocks;

    public BasketballStats(int overallRating, int points, int assists, int rebounds, int steals, int blocks) {
        super(overallRating);
        this.points = points;
        this.assists = assists;
        this.rebounds = rebounds;
        this.steals = steals;
        this.blocks = blocks;
    }

    public int getPoints() {
        return points;
    }

    public int getAssists() {
        return assists;
    }

    public int getRebounds() {
        return rebounds;
    }

    public int getSteals() {
        return steals;
    }

    public int getBlocks() {
        return blocks;
    }
}
