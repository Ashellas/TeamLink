package models;

public class TeamStats{
    private int id;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;
    private int placement;
    private int points;
    private int totalRounds;
    private TrainingPerformanceReport trainingPerformanceReport;

        public TeamStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int points, int totalRounds, TrainingPerformanceReport trainingPerformanceReport) {
        this.id = id;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDrawn = gamesDrawn;
        this.points = points; //TODO how to calculate points
        this.totalRounds = totalRounds;
        this.trainingPerformanceReport = trainingPerformanceReport;
    }

    //For teams in the standings
    public TeamStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int placement, int points) {
        this.id = id;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDrawn = gamesDrawn;        this.placement = placement;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesDrawn() {
        return gamesDrawn;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public int getPlacement() {
        return placement;
    }

    public int getPoints() {
        return points;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public TrainingPerformanceReport getTrainingPerformanceReport() {
        return trainingPerformanceReport;
    }
}
