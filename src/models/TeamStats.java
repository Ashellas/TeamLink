package models;

public class TeamStats extends Stats{
    private int placement;
    private int points;
    private int totalRounds;
    private TrainingPerformanceReport trainingPerformanceReport;

        public TeamStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int points, int totalRounds, TrainingPerformanceReport trainingPerformanceReport) {
        super(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn);
        this.points = points; //TODO how to calculate points
        this.totalRounds = totalRounds;
        this.trainingPerformanceReport = trainingPerformanceReport;
    }

    //For teams in the standings
    public TeamStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int placement, int points) {
        super(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn);
        this.placement = placement;
        this.points = points;
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
