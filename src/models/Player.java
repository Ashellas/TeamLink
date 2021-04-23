package models;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Date;

public class Player extends TeamMember{
    private String position;
    private ArrayList<Injury> injuries;
    private PlayerStats stats;
    private boolean isCaptain;

    public Player(int memberId, String firstName, String lastName, Date birthday, String teamRole, String email, String sportBranch, Image profilePhoto, String position, ArrayList<Injury> injuries, PlayerStats stats, boolean isCaptain) {
        super(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePhoto);
        this.position = position;
        this.injuries = injuries;
        this.stats = stats;
        this.isCaptain = isCaptain;
    }

    public String getPosition() {
        return position;
    }

    public ArrayList<Injury> getInjuries() {
        return injuries;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public boolean isCaptain() {
        return isCaptain;
    }
}
