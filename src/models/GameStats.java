package models;

public abstract class GameStats {
    private int overallRating;

    public GameStats(int overallRating) {
        this.overallRating = overallRating;
    }

    public int getOverallRating() {
        return overallRating;
    }
}
