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
}
