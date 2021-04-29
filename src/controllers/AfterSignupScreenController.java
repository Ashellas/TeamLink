package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.*;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * @version 27.04.2021
 * Help action and error pane is missing
 * Does not connect to database
 * Team constructor problem at line 203
 */
public class AfterSignupScreenController implements InitializeData {

    @FXML
    private TextField teamCodeField;

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label lastSyncLabel;

    @FXML
    private Pane messagePane;

    @FXML
    private TableView<TeamApplication> teamApplicationTable;

    @FXML
    private TableColumn<TeamApplication, String> teamNameColumn;

    @FXML
    private TableColumn<TeamApplication, String> ageGroupColumn;

    @FXML
    private TableColumn<TeamApplication, String> cityColumn;

    @FXML
    private TableColumn<TeamApplication, String> statusColumn;

    @FXML
    private GridPane createTeamPane;

    private UserSession user;

    @Override
    public void initData(UserSession userSession) {
        user = userSession;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        if(user.getUser().getProfilePhoto() != null){
            profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());
        }
        try{
            updateApplicationTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO add checking for already made submissions
    //TODO send Notification to people who can accept the application

    /**
     * Takes the team code and makes the application to the team
     * @param event submit button pushed
     * @throws SQLException
     */
    public void submitButtonPushed(ActionEvent event) throws SQLException {
        String response = DatabaseManager.isTeamCodeProper(user, teamCodeField.getText());
        System.out.println(response);
        if(response.equals("Success")){
            user = DatabaseManager.updateApplications(user);
            updateApplicationTable();
        }
        else{
            //Display Error
        }
    }

    /**
     * Opens the team creation screen
     * @param event create team button pushed
     * @throws IOException
     */
    public void createTeamButtonPushed(ActionEvent event) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/TeamCreationScreen.fxml"),event,user);
    }

    /**
     * Opens settings screen
     * @param event settings button or the profile photo pushed
     * @throws IOException
     */
    public void settingsButtonPushed(ActionEvent event) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/SettingsScreen.fxml"),event, user);
    }

    public void synchronizeData(ActionEvent event) throws IOException {
        // TODO
    }

    public void helpButtonPushed(ActionEvent event) throws IOException {
        // TODO
    }

    /**
     * Opens the login screen
     * @param event logout button pushed
     * @throws IOException
     */
    public void logoutButtonPushed(ActionEvent event) throws IOException{
        //removes user details from the userSession and keeps the database connection
        user = new UserSession(user.getDatabaseConnection());
        AppManager.changeScene(getClass().getResource("/views/LoginScreen.fxml"), event, user);
    }

    /**
     * Shows the message
     * @param message message to show
     */
    private void displayMessage(String message, boolean isError){
        System.out.println(message);
        JFXSnackbar snackbar = new JFXSnackbar(messagePane);
        snackbar.setPrefWidth(300.0);
        if(isError){
            snackbar.getStylesheets().add("sample/errorSnackBar.css");
        }
        else{
            snackbar.getStylesheets().add("sample/messageSnackBar.css");
        }
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(message)));

    }

    /**
     * Gets team applications and displays in the table
     * @throws SQLException
     */
    private void updateApplicationTable() throws SQLException {
        ObservableList<TeamApplication> appliedTeamsList = user.getTeamApplications();

        teamNameColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("teamName"));
        ageGroupColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("ageGroup"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("city"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("applicationStatus"));
        teamApplicationTable.setItems(appliedTeamsList);
    }

    public void toSettingsScreen(ActionEvent actionEvent) {
    }

    public void toChatScreen(ActionEvent actionEvent) {
    }

    public void toLeagueScreen(ActionEvent actionEvent) {
    }

    public void toTrainingsScreen(ActionEvent actionEvent) {
    }

    public void toGameplanScreen(ActionEvent actionEvent) {
    }

    public void toCalendarScreen(ActionEvent actionEvent) {
    }

    public void toSquadScreen(ActionEvent actionEvent) {
    }

    public void toMainScreen(ActionEvent actionEvent) {
    }

    public void SynchronizeData(ActionEvent actionEvent) {
    }
}
