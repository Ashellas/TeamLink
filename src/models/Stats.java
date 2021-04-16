package models;

public abstract class Stats {
    private int id;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesDrawn;

    public Stats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn) {
        this.id = id;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDrawn = gamesDrawn;
    }
}
