package models;

import java.util.ArrayList;

public class Gameplan {
    private int gameplanId;
    private String title;
    private ArrayList<Team> teams;
    private String filePath;

    public Gameplan(int gameplanId, String title, ArrayList<Team> teams, String filePath) {
        this.gameplanId = gameplanId;
        this.title = title;
        this.teams = teams;
        this.filePath = filePath;
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
}
