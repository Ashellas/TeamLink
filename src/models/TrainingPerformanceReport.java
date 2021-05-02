package models;

public class TrainingPerformanceReport {
    private int id;
    private int[] lastFiveTraining;
    private double lastWeekAverage;
    private double lastMonthAverage;
    private double seasonAverage;

    public TrainingPerformanceReport(int id, int[] lastFiveTraining, double lastWeekAverage, double lastMonthAverage, double seasonAverage) {
        this.id = id;
        this.lastFiveTraining = lastFiveTraining;
        this.lastWeekAverage = lastWeekAverage;
        this.lastMonthAverage = lastMonthAverage;
        this.seasonAverage = seasonAverage;
    }
    public int getId() {
        return id;
    }

    public int[] getLastFiveTraining() {
        return lastFiveTraining;
    }

    public double getLastWeekAverage() {
        return lastWeekAverage;
    }

    public double getLastMonthAverage() {
        return lastMonthAverage;
    }

    public double getSeasonAverage() {
        return seasonAverage;
    }
}
