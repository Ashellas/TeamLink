package models;

public class TrainingPerformanceReport {
    private int id;
    private double[] lastFiveTrainingAverage;
    private double lastWeekAverage;
    private double lastMonthAverage;
    private double seasonAverage;

    public TrainingPerformanceReport(int id, double[] lastFiveTrainingAverage, double lastWeekAverage, double lastMonthAverage, double seasonAverage) {
        this.id = id;
        this.lastFiveTrainingAverage = lastFiveTrainingAverage;
        this.lastWeekAverage = lastWeekAverage;
        this.lastMonthAverage = lastMonthAverage;
        this.seasonAverage = seasonAverage;
    }

    public int getId() {
        return id;
    }

    public double[] getLastFiveTrainingAverage() {
        return lastFiveTrainingAverage;
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
