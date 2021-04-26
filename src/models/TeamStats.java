package models;

public class TeamStats extends Stats{
    private int placement;
    private int points;
    private int gamesLeft;
    private TrainingPerformanceReport trainingPerformanceReport;

    public TeamStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int placement, int points, int gamesLeft, TrainingPerformanceReport trainingPerformanceReport) {
        super(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn);
        this.placement = placement;
        this.points = points; //TODO how to calculate points
        this.gamesLeft = gamesLeft;
        this.trainingPerformanceReport = trainingPerformanceReport;
    }

    //For teams in the standings
    public TeamStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int placement, int points) {
        super(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn);
        this.placement = placement;
        this.points = points;
    }

    public int getPlacement() {
        return placement;
    }

    public int getPoints() {
        return points;
    }

    public int getGamesLeft() {
        return gamesLeft;
    }

    public TrainingPerformanceReport getTrainingPerformanceReport() {
        return trainingPerformanceReport;
    }
}
