package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Team {

    private int teamId;
    private int databaseTeamId;
    private int leagueId;
    private String teamName;
    private String abbrevation;
    private String teamCode;
    private String leagueName;
    private String city;
    private String ageGroup;
    private ImageView teamLogo;
    private TeamStats teamStats;
    private ArrayList<TeamMember> teamMembers;

    public Team(int teamId, int databaseTeamId, int leagueId, String teamName, String abbrevation, String teamCode, String leagueName, String city, String ageGroup, Image teamLogo, TeamStats teamStats, ArrayList<TeamMember> teamMembers) {
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

    public String getTeamCode() {
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

    public int getGamesWon(){
        return teamStats.getGamesWon();
    }

    public int getGamesDrawn(){
        return teamStats.getGamesDrawn();
    }

    public int getGamesLost(){
        return teamStats.getGamesLost();
    }

    public int getPoints(){
        return teamStats.getPoints();
    }

    public int getPlacement(){
        return teamStats.getPlacement();
    }

    public int getMatchesPlayed(){
        return teamStats.getGamesPlayed();
    }

    public int getMatchesLeft(){
        return teamStats.getTotalRounds();
    }

    public ObservableList<Player> getPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        for( TeamMember member : teamMembers){
            if(member.getTeamRole().equals("Player")){
                players.add((Player) member);
            }
        }
        return FXCollections.observableArrayList(players);
    }

    public ObservableList<TeamMember> getMembersWithRole(String teamRole){
        ArrayList<TeamMember> memberList = new ArrayList<>();
        for( TeamMember member : teamMembers){
            if(member.getTeamRole().equals(teamRole)){
                memberList.add( member);
            }
        }
        return FXCollections.observableArrayList(memberList);
    }

    /**
     * Compares two teams according to their id's
     * @param otherTeam the latter team that will be compared
     * @return true if the id's of the both teams are the same
     */
    public boolean equals( Team otherTeam){
        if( this.getTeamId() == otherTeam.getTeamId() ){
            return true;
        }
        else{
            return false;
        }
    }

}
