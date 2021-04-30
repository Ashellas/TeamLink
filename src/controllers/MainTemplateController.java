package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import models.AppManager;
import models.InitializeData;
import models.UserSession;

import java.io.IOException;


public class MainTemplateController implements InitializeData {

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label lastSyncLabel;

    private UserSession user;

    public void initData(UserSession user) {
        this.user = user;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        if(user.getUser().getProfilePhoto() != null){
            profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());
        }
        lastSyncLabel.setText(AppManager.getLastSyncText(user.getLastSync()));
    }

    public void toMainScreen(ActionEvent actionEvent) {
    }

    public void toSquadScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/SquadScreen.fxml"),actionEvent, user);
    }

    public void toCalendarScreen(ActionEvent actionEvent) {
    }

    public void toGameplanScreen(ActionEvent actionEvent) {
    }

    public void toTrainingsScreen(ActionEvent actionEvent) {
    }

    public void toLeagueScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/LeagueScreen.fxml"),actionEvent, user);
    }

    public void toChatScreen(ActionEvent actionEvent) {
    }

    public void toSettingsScreen(ActionEvent actionEvent) {
    }

    public void logoutButtonPushed(ActionEvent actionEvent) {
    }

    public void helpButtonPushed(ActionEvent actionEvent) {
    }

    public void SynchronizeData(ActionEvent actionEvent) {
    }
}

