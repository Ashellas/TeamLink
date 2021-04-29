package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import models.*;

import java.io.File;
import java.io.FileInputStream;
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
 * @version 24.04.2021
 * errorPane is missing
 * help button action is missing
 * Does not connect to database
 */
public class TeamCreationScreenController implements InitializeData {

    @FXML
    private TextField teamNameField;

    @FXML
    private TextField abbrevationField;

    @FXML
    private ComboBox cityBox;

    @FXML
    private ComboBox ageGroupBox;

    @FXML
    private ComboBox leagueBox;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView teamLogoView;

    @FXML
    private ComboBox teamBox;

    @FXML
    private Pane errorPane;

    private File selectedFile;

    private ObservableList<String> cityList = FXCollections.observableArrayList("Istanbul", "Ankara", "İzmir");

    private ObservableList<String> ageGroupList = FXCollections.observableArrayList("U18", "U16", "U14", "U12");

    private String selectedAgeGroup = "", selectedCity = "", selectedLeague = "";

    private UserSession user;


    @Override
    public void initData(UserSession userSession) {
        user = userSession;
        cityBox.getItems().addAll(cityList);
        ageGroupBox.getItems().addAll(ageGroupList);
    }


    public void onSelection( ActionEvent event) throws SQLException {

        //TODO think about creating league model class to get id easily
        if(ageGroupBox.getValue() != null && cityBox.getValue() != null){
            leagueBox.setDisable(false);
            teamBox.getItems().clear();
            leagueBox.getItems().clear();
            ObservableList<String> leagueList = DatabaseManager.getLeagues(user, cityBox.getValue().toString(), ageGroupBox.getValue().toString());
            leagueBox.getSelectionModel().clearSelection();
            leagueBox .setButtonCell(new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty || item == null) {
                        setText("Choose League");
                    } else {
                        setText(item);
                    }
                }
            });
            if(leagueList.size() != 0){
                leagueBox.getItems().addAll(leagueList);
            }
        }

    }

    public void onLeagueSelection(ActionEvent actionEvent) throws SQLException {
        if(leagueBox.getValue() != null){
            teamBox.setDisable(false);
            ObservableList<String> teamList = DatabaseManager.getLeagueTeams(user, cityBox.getValue().toString(), ageGroupBox.getValue().toString(), leagueBox.getValue().toString());
            teamBox.getSelectionModel().clearSelection();
            teamBox .setButtonCell(new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty || item == null) {
                        setText("Choose Team");
                    } else {
                        setText(item);
                    }
                }
            });
            if(teamList.size() != 0){
                teamBox.getItems().addAll(teamList);
            }
        }
    }




    /**
     * Changes scene to the after signup screen
     * @param event back button pushed
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        // TODO if he has teams go to settings if not go to aftersign up
        AppManager.changeScene(getClass().getResource("/views/AfterSignupScreen.fxml"),event, user);
        // GameManager.changeScene(getClass().getResource("AfterSignUp.fxml"), event);
    }

    /**
     * Shows the help information for the current scene
     * @param event the press of the help button
     */
    public void helpButtonPushed(ActionEvent event) {
        // TODO
    }

    /**
     * Opens the file chooser for the user to select the image
     * @param event upload image button pushed
     */
    public void uploadImageButtonPushed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture Chooser");
        // Sets up the initial directory as user folder when filechooser is opened
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Sets the file type options
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG and JPG files", "*.png","*.jpg","*.jpeg"));

        selectedFile = fileChooser.showOpenDialog(null);

        if( selectedFile != null)
        {
            // Upload button's text is changed and the display image is changed to the selected image
            uploadImageButton.setText("Change");
            teamLogoView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    /**
     * Gets the required information and saves it to the database
     * @param event submit button pushed
     * @throws IOException
     * @throws SQLException
     */
    public void submitButtonPushed(ActionEvent event) throws IOException, SQLException {
        //If there is no error in the form, saves it into database
        if( !isThereAnError())
        {
            saveToDatabase();
            // TODO
            // GameManager.changeScene(getClass().getResource("AfterSignUp.fxml"), event);
        }
    }

    /**
     * Checks if there is an error in the given information
     * @return false if there is no error
     * @throws SQLException
     */
    private boolean isThereAnError() throws SQLException {
        // Checks if any of the fields is empty
        if(teamNameField.getText().equals("") || abbrevationField.getText().equals("")
                || cityBox.getValue() == null || ageGroupBox.getValue() == null){
            displayError("Please fill all the fields");
            return true;
        }
        // Checks the abbrevation length
        else if(abbrevationField.getText().length() > 3)
        {
            displayError("Abbrevations must be at most 3 characters");
            return true;
        }
        // Checks team name length
        else if (teamNameField.getText().length() > 30){
            displayError("Team Names must be smaller than 30 characters");
            return true;
        }
        return false;
    }

    /**
     * Shows the error message
     * @param errorMessage message to show
     */
    private void displayError(String errorMessage) {
        System.out.println(errorMessage);
        JFXSnackbar snackbar = new JFXSnackbar(errorPane);

        snackbar.setPrefWidth(300.0);
        snackbar.getStylesheets().add("sample/errorSnackBar.css");
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(errorMessage)));
    }

    /**
     * Gets all the required information, creates a team code and saves it all to the database
     * @throws SQLException
     * @throws IOException
     */
    private void saveToDatabase() throws SQLException, IOException {
        /*
        user = DatabaseManager.createTeam(user, teamNameField.getText(), abbrevationField.getText(), cityBox.getValue().toString(),
                ageGroupBox.getValue().toString(), leagueBox.getValue().toString(), selectedFile);
        int uniqueCode;
        int teamId;
        //Prepares the statement
        PreparedStatement prepStmt =myCon.prepareStatement("INSERT INTO teams( team_name, abbrevation, city, age_group, team_code, team_logo) " +
                "values(?,?,?,?,?,?)");

        uniqueCode = createUniqueRandomTeamCode();

        //fills the statemenet with relevant info
        prepStmt.setString(1, teamNameField.getText());
        prepStmt.setString(2, abbrevationField.getText());
        prepStmt.setString(3, cityBox.getValue().toString());
        prepStmt.setString(4, ageGroupBox.getValue().toString());
        prepStmt.setString(5, "" + uniqueCode);


        if(selectedFile != null){
            FileInputStream fileInputStream = new FileInputStream(selectedFile.getAbsolutePath());
            prepStmt.setBinaryStream(6,fileInputStream,fileInputStream.available());
        }
        else {
            prepStmt.setBlob(6, InputStream.nullInputStream());
        }

        // Prints out a report
        int row = prepStmt.executeUpdate();
        if(row > 0)
        {
            System.out.println("Saved into the database");
        }

        PreparedStatement preparedStmt = myCon.prepareStatement("select * from teams where team_code = ?");
        preparedStmt.setInt(1, uniqueCode);

        ResultSet resultSet = preparedStmt.executeQuery();

        // Assigns user to that team
        if(resultSet.next()){
            teamId = resultSet.getInt("team_id");
            PreparedStatement preparedStatement = myCon.prepareStatement("INSERT INTO team_and_members(team_member_id, tm_id) VALUES (?,?)");
            preparedStatement.setInt(1, user.getMemberId());
            preparedStatement.setInt(2, teamId);
            preparedStatement.executeUpdate();
        }

         */
    }

    /**
     * Creates a unique 8 digit code for the team
     * @return the created code
     * @throws SQLException
     */
    private int createUniqueRandomTeamCode() throws SQLException {
        /*

        final int BOUND = 100000000;
        ResultSet resultSet;

        int teamCode;
        String tempCode;

        do{
            teamCode =  (int)(Math.random()*BOUND);
            PreparedStatement prepStmt = myCon.prepareStatement("select * from teams where team_code = ?");
            prepStmt.setInt(1,teamCode);
            resultSet = prepStmt.executeQuery();
        }while (resultSet.next());

        return teamCode;

         */
        return 0;
    }



}
