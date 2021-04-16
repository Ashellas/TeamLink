package models;

public class Game {
    private int gameId;
    private int roundNumber;
    private Team homeTeam;
    private Team awayTeam;
    private String gameLocationName;
    private String gameLocationLink;
    private String result;

    public Game(int gameId, int roundNumber, Team homeTeam, Team awayTeam, String gameLocationName, String gameLocationLink, String result) {
        this.gameId = gameId;
        this.roundNumber = roundNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameLocationName = gameLocationName;
        this.gameLocationLink = gameLocationLink;
        this.result = result;
    }

    public int getGameId() {
        return gameId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public String getGameLocationName() {
        return gameLocationName;
    }

    public String getGameLocationLink() {
        return gameLocationLink;
    }

    public String getResult() {
        return result;
    }
}
