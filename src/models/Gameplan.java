package models;

import java.util.ArrayList;

public class Gameplan {
    private int gameplanId;
    private String title;
    private ArrayList<Team> teams;
    private String filePath;
    private int version;

    public Gameplan(int gameplanId, String title, Team team, int version) {
        this.gameplanId = gameplanId;
        this.title = title;
        teams = new ArrayList<>();
        teams.add(team);
        this.version = version;
        filePath = title + "_" + version + ".pdf";
    }

    public int getGameplanId() {
        return gameplanId;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getVersion() {
        return version;
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public void setTeams(ArrayList<Team> teams){
        this.teams = teams;
    }
}
