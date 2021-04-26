package models;

import java.util.Date;

public class Game extends CalendarEvent{
    private int roundNumber;
    private Team homeTeam;
    private Team awayTeam;
    private String gameLocationName;
    private String gameLocationLink;
    private String result;

    public Game(int calendarEventId, String eventTitle, Date eventDateTime, String description, String actionLink, String colorCode, int roundNumber, Team homeTeam, Team awayTeam, String gameLocationName, String gameLocationLink, String result) {
        super(calendarEventId, eventTitle, eventDateTime, description, actionLink, colorCode);
        this.roundNumber = roundNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameLocationName = gameLocationName;
        this.gameLocationLink = gameLocationLink;
        this.result = result;
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
