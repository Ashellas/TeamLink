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
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Help action and error pane is missing
 */
public class AfterSignupScreenController extends MainTemplateController implements InitializeData {

    @FXML
    private TextField teamCodeField;

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

    @Override
    public void initData(UserSession userSession) {
        super.initData(userSession);

        if(user.isStyleDark()) {
            //darkIcons();
        }
        else {
            //lightIcons();
        }

        try{
            updateApplicationTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(!user.getUser().getTeamRole().equals("Head Coach")){
            createTeamPane.setVisible(false);
        }
        lastSyncLabel.setText(AppManager.getLastSyncText(user.getLastSync()));
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
        lastSyncLabel.setText(AppManager.getLastSyncText(user.getLastSync()));
    }

    /**
     * Opens the team creation screen
     * @param event create team button pushed
     * @throws IOException
     */
    public void createTeamButtonPushed(ActionEvent event) throws IOException {
        System.out.println(user.getUser().getFirstName());
        AppManager.changeScene(getClass().getResource("/views/TeamCreationScreen.fxml"),event,user);
    }

    @Override
    public void helpButtonPushed(ActionEvent event) {
        // TODO
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


}
