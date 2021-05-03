package models;

import java.util.ArrayList;

public class Gameplan {
    private int gameplanId;
    private String title;
    private Team team;
    private String filePath;
    private int fileId;
    private int version;

    public Gameplan(int gameplanId, String title, Team team, int version, int fileId) {
        this.gameplanId = gameplanId;
        this.title = title;
        this.team = team;
        this.version = version;
        this.fileId = fileId;
        filePath = title + "_" + version + ".pdf";
    }



    public int getGameplanId() {
        return gameplanId;
    }

    public String getTitle() {
        return title;
    }

    public Team getTeams() {
        return team;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getVersion() {
        return version;
    }

    public int getFileId() {
        return fileId;
    }
}
