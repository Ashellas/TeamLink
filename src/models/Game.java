package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Game extends CalendarEvent{
    public static HashMap<Game, ArrayList<TeamMember>> addedPlayers = new HashMap<Game, ArrayList<TeamMember>>();
    private int roundNumber;
    private Team homeTeam;
    private Team awayTeam;
    private String gameLocationName;
    private String gameLocationLink;
    private String result;
    private int fileId;

    public Game(int calendarEventId, String eventTitle, Date eventDateTime, String actionLink, String colorCode, int roundNumber, Team homeTeam, Team awayTeam, String gameLocationName, String gameLocationLink, String result, int fileId) {
        super(calendarEventId, eventTitle, eventDateTime, actionLink, colorCode);
        this.roundNumber = roundNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameLocationName = gameLocationName;
        this.gameLocationLink = gameLocationLink;
        this.result = result;
        this.fileId = fileId;
    }

    public int getFileId(){
        return fileId;
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

    public String getHomeTeamName(){
        return homeTeam.getTeamName();
    }

    public String getAwayTeamName(){
        return awayTeam.getTeamName();
    }

    public boolean isPlayed(){
        if( getResult() == null){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean equals(Object other){
        if( other instanceof Game){
            Game otherGame = (Game) other;
            if( this.getRoundNumber() == otherGame.getRoundNumber() &&
                    this.getHomeTeam().equals(otherGame.getHomeTeam()) &&
                    this.getAwayTeam().equals(otherGame.getAwayTeam() ) ){
                return true;
            }

            return false;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return 1;
    }
}
