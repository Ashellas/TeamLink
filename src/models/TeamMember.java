package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.time.LocalDate;
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
    private String pointsOrGoalsScored;
    private String assists;
    private String reboundsOrSavesMade;
    private String stealsOrYellowCard;
    private String blocksOrRedCard;

    public TeamMember(int memberId, String firstName, String lastName, LocalDate birthday, String teamRole, String email, String sportBranch, Image profilePhoto) {
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
        else{
            InputStream inStream = getClass().getResourceAsStream("/Resources/Images/white/profile_white.png");
            this.profilePhoto = new ImageView(new Image(inStream));
        }
    }

    public TeamMember(int memberId, String firstName, String lastName) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getPointsOrGoalsScored(){
        return pointsOrGoalsScored;
    }

    public void setPointsOrGoalsScored( String pointsOrGoalsScored){
        this.pointsOrGoalsScored = pointsOrGoalsScored;
    }

    public String getAssists(){
        return assists;
    }

    public void setAssists( String assists){
        this.assists = assists;
    }

    public String getReboundsOrSavesMade(){
        return reboundsOrSavesMade;
    }

    public void setReboundsOrSavesMade( String reboundsOrSavesMade){
        this.reboundsOrSavesMade = reboundsOrSavesMade;
    }

    public String getStealsOrYellowCard(){
        return stealsOrYellowCard;
    }

    public void setStealsOrYellowCard( String stealsOrYellowCard){
        this.stealsOrYellowCard = stealsOrYellowCard;
    }

    public String getBlocksOrRedCard(){
        return blocksOrRedCard;
    }

    public void setBlocksOrRedCard( String blocksOrRedCard){
        this.blocksOrRedCard = blocksOrRedCard;
    }
}
