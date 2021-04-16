package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Date;

public class TeamMember {

    private int memberId;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String teamRole;
    private String email;
    private String sportBranch;

    //ImageView is used instead of Image to display photo easily
    private ImageView profilePhoto;

    public TeamMember(int memberId, String firstName, String lastName, Date birthday, String teamRole, String email, String sportBranch, Image profilePhoto) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.teamRole = teamRole;
        this.email = email;
        this.sportBranch = sportBranch;
        this.profilePhoto = new ImageView(profilePhoto);
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

    public Date getBirthday() {
        return birthday;
    }

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
}
