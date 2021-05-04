package models;

import java.util.ArrayList;

public class Gameplan {
    private int gameplanId;
    private String title;
    private String filePath;
    private int fileId;

    public Gameplan(int gameplanId, String title, int fileId) {
        this.gameplanId = gameplanId;
        this.title = title;
        this.fileId = fileId;
        filePath = System.getProperty("user.home") + "\\Teamlink\\" + title + ".pdf";
    }




    public int getGameplanId() {
        return gameplanId;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getFileId() {
        return fileId;
    }
}
