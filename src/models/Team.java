package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Team {

    private int teamId;
    private int databaseTeamId;
    private int leagueId;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;
    private int points;
    private int placement;
    private int matchesLeft;
    private String teamName;
    private String abbrevation;
    private int teamCode;
    private String leagueName;
    private String city;
    private String ageGroup;
    private ImageView teamLogo;
    private TeamStats teamStats;
    private ArrayList<TeamMember> teamMembers;

    public Team(int teamId, int databaseTeamId, int leagueId, String teamName, String abbrevation, int teamCode, String leagueName, String city, String ageGroup, Image teamLogo, TeamStats teamStats, ArrayList<TeamMember> teamMembers) {
        this.teamId = teamId;
        this.databaseTeamId = databaseTeamId;
        this.leagueId = leagueId;
        this.teamName = teamName;
        this.abbrevation = abbrevation;
        this.teamCode = teamCode;
        this.leagueName = leagueName;
        this.city = city;
        this.ageGroup = ageGroup;
        this.teamLogo = new ImageView(teamLogo);
        this.teamStats = teamStats;
        this.teamMembers = teamMembers;
        this.gamesWon = teamStats.getGamesWon();
        this.gamesDrawn = teamStats.getGamesDrawn();
        this.gamesLost = teamStats.getGamesLost();
        this.points = teamStats.getPoints();
        this.placement = teamStats.getPlacement();
        this.matchesLeft = teamStats.getGamesLeft();
    }

    //For teams which do not belong to user
    public Team(int teamId, String teamName, String abbrevation, TeamStats teamStats) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.abbrevation = abbrevation;
        this.teamStats = teamStats;
    }



    public int getTeamId() {
        return teamId;
    }

    public int getDatabaseTeamId() {
        return databaseTeamId;
    }

    public int getLeagueId() {
        return leagueId;
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

    public int getPoints(){
        return points;
    }

    public int getPlacement(){
        return placement;
    }

    public int getMatchesLeft(){
        return matchesLeft;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getAbbrevation() {
        return abbrevation;
    }

    public int getTeamCode() {
        return teamCode;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getCity() {
        return city;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public ImageView getTeamLogo() {
        return teamLogo;
    }

    public TeamStats getTeamStats() {
        return teamStats;
    }

    public ArrayList<TeamMember> getTeamMembers() {
        return teamMembers;
    }
}
