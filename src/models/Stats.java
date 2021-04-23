package models;

public abstract class Stats {
    private int id;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;

    public Stats(int id, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn) {
        this.id = id;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDrawn = gamesDrawn;
    }

    public int getId(){
        return id;
    }

    public int getGamesPlayed(){
        return gamesPlayed;
    }

    public int getGamesWon(){
        return gamesWon;
    }

    public int getGamesDrawn(){
        return gamesDrawn;
    }

    public int getGamesLost(){
        return gamesLost;
    }
}
