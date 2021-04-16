package models;

public class PlayerStats extends Stats{
    private TrainingPerformanceReport trainingPerformanceReport;

    public PlayerStats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, TrainingPerformanceReport trainingPerformanceReport) {
        super(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn);
        this.trainingPerformanceReport = trainingPerformanceReport;
    }

    public TrainingPerformanceReport getTrainingPerformanceReport() {
        return trainingPerformanceReport;
    }
}
