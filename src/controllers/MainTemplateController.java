package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import models.AppManager;
import models.InitializeData;
import models.UserSession;

import java.io.IOException;


public class MainTemplateController implements InitializeData {

    @FXML
    protected Label userNameLabel;

    @FXML
    protected Label userRoleLabel;

    @FXML
    protected Label lastSyncLabel;

    @FXML
    protected ImageView homeIcon;

    @FXML
    protected ImageView squadIcon;

    @FXML
    protected ImageView calendarIcon;

    @FXML
    protected ImageView gameplanIcon;

    @FXML
    protected ImageView trainingIcon;

    @FXML
    protected ImageView leagueIcon;

    @FXML
    protected ImageView chatIcon;

    @FXML
    protected ImageView settingsIcon;

    @FXML
    protected ImageView logoutIcon;

    @FXML
    protected ImageView helpIcon;

    @FXML
    protected ImageView syncIcon;

    @FXML
    protected ImageView notificationIcon;

    @FXML
    protected ImageView profileIcon;

    @FXML
    protected Pane messagePane;

    protected UserSession user;

    public void initData(UserSession user) {
        this.user = user;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());

        if(user.isStyleDark()) {
            darkThemeIcons();
        }
        else {
            lightThemeIcons();
        }

        if(user.getUser().getProfilePhoto() != null){
            profileIcon.setImage(user.getUser().getProfilePhoto().getImage());
        }
        lastSyncLabel.setText(AppManager.getLastSyncText(user.getLastSync()));

    }

    public void toMainScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/MainScreen.fxml"),actionEvent, user);
    }

    public void toSquadScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/SquadScreen.fxml"),actionEvent, user);
    }

    public void toCalendarScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/CalendarScreen.fxml"),actionEvent, user);
    }

    public void toGameplanScreen(ActionEvent actionEvent) throws  IOException {
        AppManager.changeScene(getClass().getResource("/views/GameplansScreen.fxml"),actionEvent, user);
    }

    public void toTrainingsScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/TrainingsScreen.fxml"),actionEvent, user);
    }

    public void toLeagueScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/LeagueScreen.fxml"),actionEvent, user);
    }

    public void toChatScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/ChatScreen.fxml"),actionEvent, user);
    }

    public void toSettingsScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/SettingsScreen.fxml"),actionEvent, user);
    }

    public void logoutButtonPushed(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/LoginScreen.fxml"),actionEvent, user);
    }

    public void helpButtonPushed(ActionEvent actionEvent){
        displayMessage(messagePane,"aaaa",true);
    }

    public void SynchronizeData(ActionEvent actionEvent) {
        displayMessage(messagePane,"aaaa",false);
    }

    public final void darkThemeIcons() {
        helpIcon.setImage(new Image("/Resources/Images/white/help_white.png"));
        squadIcon.setImage(new Image("/Resources/Images/white/squad_white.png"));
        leagueIcon.setImage(new Image("/Resources/Images/white/league_white.png"));
        calendarIcon.setImage(new Image("/Resources/Images/white/calendar_white.png"));
        chatIcon.setImage(new Image("/Resources/Images/white/chat_white.png"));
        settingsIcon.setImage(new Image("/Resources/Images/white/settings_white.png"));
        gameplanIcon.setImage(new Image("/Resources/Images/white/gameplan_white.png"));
        logoutIcon.setImage(new Image("/Resources/Images/white/logout_white.png"));
        syncIcon.setImage(new Image("/Resources/Images/white/sync_white.png"));
        homeIcon.setImage(new Image("/Resources/Images/white/home_white.png"));
        trainingIcon.setImage(new Image("/Resources/Images/white/training_white.png"));
        notificationIcon.setImage(new Image("/Resources/Images/white/notification_white.png"));
        if (user.getUser().getProfilePhoto() == null) {
            profileIcon.setImage(new Image("/Resources/Images/white/profile_white.png"));
        }
    }

    public final void lightThemeIcons() {
        helpIcon.setImage(new Image("/Resources/Images/black/help_black.png"));
        squadIcon.setImage(new Image("/Resources/Images/black/squad_black.png"));
        leagueIcon.setImage(new Image("/Resources/Images/black/league_black.png"));
        calendarIcon.setImage(new Image("/Resources/Images/black/calendar_black.png"));
        chatIcon.setImage(new Image("/Resources/Images/black/chat_black.png"));
        settingsIcon.setImage(new Image("/Resources/Images/black/settings_black.png"));
        gameplanIcon.setImage(new Image("/Resources/Images/black/gameplan_black.png"));
        logoutIcon.setImage(new Image("/Resources/Images/black/logout_black.png"));
        syncIcon.setImage(new Image("/Resources/Images/black/sync_black.png"));
        homeIcon.setImage(new Image("/Resources/Images/black/home_black.png"));
        trainingIcon.setImage(new Image("/Resources/Images/black/training_black.png"));
        notificationIcon.setImage(new Image("/Resources/Images/black/notifications_black.png"));
        if (user.getUser().getProfilePhoto() == null) {
            profileIcon.setImage(new Image("/Resources/Images/black/profile_black.png"));
        }
    }

    /**
     * Shows the error message
     * @param message message to show
     */
    protected void displayMessage(Pane pane, String message, boolean error){
        System.out.println(message);
        JFXSnackbar snackbar = new JFXSnackbar(pane);
        snackbar.setPrefWidth(300.0);
        if (error) {
            snackbar.getStylesheets().add("/stylesheets/errorSnackBar.css");
        }
        else {
            snackbar.getStylesheets().add("/stylesheets/messageSnackBar.css");
        }

        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(message)));
    }
}

