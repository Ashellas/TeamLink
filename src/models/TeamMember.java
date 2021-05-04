package models;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class TeamMember {
    private int memberId;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String teamRole;
    private String email;
    private String sportBranch;
    //ImageView is used instead of Image to display photo easily
    private ImageView profilePhoto;

    private GameStats gameStats;

    private int fileId;

    public TeamMember(int memberId, String firstName, String lastName, LocalDate birthday, String teamRole, String email, String sportBranch, Image profilePhoto, int fileId) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.teamRole = teamRole;
        this.email = email;
        this.sportBranch = sportBranch;
        if(profilePhoto != null){
            this.profilePhoto = new ImageView(profilePhoto);
        }
        this.fileId = fileId;

        if( sportBranch.equals("Football")){
            gameStats = new FootballStats(memberId, null, null, null, null, null);
        }
        else if( sportBranch.equals( "Basketball")){
            gameStats = new BasketballStats( memberId, null, null, null, null, null);
        }
    }

    //for applications
    public TeamMember(int memberId, String firstName, String lastName, LocalDate birthday, String teamRole, String email) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.teamRole = teamRole;
        this.email = email;
    }


    public TeamMember(int memberId, String firstName, String lastName, Image photo) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        if(photo != null){
            profilePhoto = new ImageView(photo);
        }
    }
    //TODO edit file selection
    public TeamMember(int memberId, String firstName, String lastName) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public TeamMember(int memberId, String firstName, String lastName, GameStats gameStats){
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gameStats = gameStats;
    }

    public TeamMember(int memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getBirthdayString(){return birthday.toString();}

    public String getTeamRole() {
        return teamRole;
    }

    public String getEmail() {
        return email;
    }

    public String getSportBranch() {
        return sportBranch;
    }

    public ImageView getProfilePhoto() {
        return profilePhoto;
    }

    public String getFullName(){ return firstName + " " + lastName;}

    public void setName(String name) {
        int spaceIndex;
        spaceIndex = 0;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                spaceIndex = i;
            }
        }
        firstName = name.substring(0,spaceIndex);
        lastName = name.substring(spaceIndex + 1);
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getFirstColumnData(){
        return gameStats.getFirstStat();
    }

    public String getSecondColumnData(){
        return gameStats.getSecondStat();
    }

    public String getThirdColumnData(){
        return gameStats.getThirdStat();
    }

    public String getForthColumnData(){
        return gameStats.getForthStat();
    }

    public String getFifthColumnData(){
        return gameStats.getFifthStat();
    }


    public int getFileId() {
        return fileId;
    }

    public void setProfilePhoto(ImageView accountPhoto) {
        this.profilePhoto = accountPhoto;
    }
}
