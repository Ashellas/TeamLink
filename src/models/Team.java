package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Team {

    private int teamId;
    private int databaseTeamId;
    private int leagueId;
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
    }


    public Team(int teamId, String teamName, String city, String ageGroup, Image teamLogo) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.city = city;
        this.ageGroup = ageGroup;
        this.teamLogo = new ImageView(teamLogo);
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
