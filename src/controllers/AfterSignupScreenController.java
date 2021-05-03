package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import models.*;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.File;
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
 * Controls after signup scene and all its functions
 */
public class AfterSignupScreenController extends MainTemplateController implements InitializeData {

    private ObservableList<String> cityList = FXCollections.observableArrayList("Istanbul", "Ankara", "İzmir");

    private ObservableList<String> ageGroupList = FXCollections.observableArrayList("U18", "U16", "U14", "U12");

    @FXML
    private Pane darkPane;

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

    //----------------Create Pane-----------------//

    @FXML
    private GridPane popUpCreateTeamPane;

    @FXML
    private TextField teamNameCreateField;

    @FXML
    private TextField abbrevationCreateField;

    @FXML
    private ComboBox chooseCityBoxCreate;

    @FXML
    private ComboBox chooseLeagueBoxCreate;

    @FXML
    private ComboBox<String> chooseAgeGroupCreate;

    @FXML
    private ComboBox chooseLeagueTeamBoxCreate;

    @FXML
    private ImageView logoChangeImageCreate;

    @FXML
    private Button uploadTeamLogoButtonCreate;

    private File selectedFile;

    //---------------------Help Pane---------------------------//

    @FXML
    private GridPane helpPane;

    @FXML
    private ImageView helpPaneIcon;


    @Override
    public void initData(UserSession userSession) {
        super.initData(userSession);
        if(user.isStyleDark()) {
            darkIcons();
        }
        else {
            lightIcons();
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

        popUpCreateTeamPane.setVisible(false);
        popUpCreateTeamPane.setDisable(true);
        darkPane.setDisable(true);
        darkPane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);

        chooseCityBoxCreate.getItems().addAll(cityList);
        chooseAgeGroupCreate.getItems().addAll(ageGroupList);

        lastSyncLabel.setText(AppManager.getLastSyncText(user.getLastSync()));
    }

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
        popUpCreateTeamPane.setDisable(false);
        popUpCreateTeamPane.setVisible(true);
        darkPane.setDisable(false);
        darkPane.setVisible(true);

        teamNameCreateField.setText("");
        abbrevationCreateField.setText("");
        chooseCityBoxCreate.getSelectionModel().clearSelection();
        chooseLeagueBoxCreate.getSelectionModel().clearSelection();
        chooseAgeGroupCreate.getSelectionModel().clearSelection();
        chooseLeagueTeamBoxCreate.getSelectionModel().clearSelection();

        chooseLeagueBoxCreate.setDisable(true);
        chooseLeagueTeamBoxCreate.setDisable(true);
    }

    @Override
    /**
     * Shows help information of the screen
     */
    public void helpButtonPushed(ActionEvent actionEvent){
        darkPane.setVisible(true);
        darkPane.setDisable(false);
        helpPane.setDisable(false);
        helpPane.setVisible(true);
    }

    /**
     * Closes the help pane
     * @param actionEvent close button pushed
     */
    public void helpPaneClose(ActionEvent actionEvent) {
        darkPane.setDisable(true);
        darkPane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);
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

    //------------------Team Create------------------------//

    /**
     * Opens file chooser for team logo selection and displays it
     * @param actionEvent
     */
    public void createTeamLogo(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture Chooser");
        // Sets up the initial directory as user folder when filechooser is opened
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Sets the file type options
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG and JPG files", "*.png","*.jpg","*.jpeg"));

        selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {
            // Upload button's text is changed and the display image is changed to the selected image
            uploadTeamLogoButtonCreate.setText("Change Photo");
            logoChangeImageCreate.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    /**
     * Creates team and saves it to the database
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void createTeam(ActionEvent actionEvent) throws IOException, SQLException {
        if (validCreateInput()) {
            user = DatabaseManager.createTeam(user, teamNameCreateField.getText(), abbrevationCreateField.getText(), chooseCityBoxCreate.getValue().toString(),
                    chooseAgeGroupCreate.getValue(), chooseLeagueBoxCreate.getValue().toString(), chooseLeagueTeamBoxCreate.getValue().toString(), selectedFile);

            createPaneClose(actionEvent);
            AppManager.changeScene(getClass().getResource("/views/LoginScreen.fxml"),actionEvent, user);
        }
    }

    /**
     * Initialises comboboxes and enables league seleciton combobox
     * @param event city and age combobox selection
     * @throws SQLException
     */
    public void onSelectionCreate(ActionEvent event) throws SQLException {
        //TODO think about creating league model class to get id easily
        if(chooseAgeGroupCreate.getValue() != null && chooseCityBoxCreate.getValue() != null){
            chooseLeagueBoxCreate.setDisable(false);
            chooseLeagueTeamBoxCreate.getItems().clear();
            chooseLeagueBoxCreate.getItems().clear();
            ObservableList<String> leagueList = DatabaseManager.getLeagues(user, chooseCityBoxCreate.getValue().toString(), chooseAgeGroupCreate.getValue());
            chooseLeagueBoxCreate.getSelectionModel().clearSelection();
            chooseLeagueBoxCreate .setButtonCell(new ListCell<String>() {
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
                chooseLeagueBoxCreate.getItems().addAll(leagueList);
            }
        }
    }

    /**
     * Creates team list for league teams and enables team selection combobox
     * @param actionEvent
     * @throws SQLException
     */
    public void onLeagueSelectionCreate(ActionEvent actionEvent) throws SQLException {
        if(chooseLeagueBoxCreate.getValue() != null){
            chooseLeagueTeamBoxCreate.setDisable(false);
            ObservableList<String> teamList = DatabaseManager.getLeagueTeams(user, chooseCityBoxCreate.getValue().toString(), chooseAgeGroupCreate.getValue().toString(), chooseLeagueBoxCreate.getValue().toString());
            chooseLeagueTeamBoxCreate.getSelectionModel().clearSelection();
            chooseLeagueTeamBoxCreate.setButtonCell(new ListCell<String>() {
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
                chooseLeagueTeamBoxCreate.getItems().addAll(teamList);
            }
        }
    }

    /**
     * Closes the team creation pane
     * @param actionEvent cancel or close button pushed
     */
    public void createPaneClose(ActionEvent actionEvent) {
        popUpCreateTeamPane.setDisable(true);
        popUpCreateTeamPane.setVisible(false);
        darkPane.setDisable(true);
        darkPane.setVisible(false);
        logoChangeImageCreate.setImage(new Image("/Resources/Images/emptyTeamLogo.png"));
    }

    /**
     * Checks the team creation input
     * @return true if all input are valid
     * @throws SQLException
     */
    private boolean validCreateInput() throws SQLException {
        // Checks if any of the fields is empty
        if(teamNameCreateField.getText().equals("") || abbrevationCreateField.getText().equals("")
                || chooseCityBoxCreate.getValue() == null || chooseLeagueBoxCreate.getValue() == null
                || chooseAgeGroupCreate.getValue() == null || chooseLeagueTeamBoxCreate.getValue() == null){
            displayMessage(messagePane,"Please fill all the fields",true);
            return false;
        }
        // Checks the abbrevation length
        else if(abbrevationCreateField.getText().length() > 3)
        {
            displayMessage(messagePane,"Abbrevations must be at most 3 characters",true);
            return false;
        }
        // Checks team name length
        else if (teamNameCreateField.getText().length() > 30){
            displayMessage(messagePane,"Team Names must be smaller than 30 characters",true);
            return false;
        }
        return true;
    }

    /**
     * Helps initialising the icons according to the chosen team
     */
    public void darkIcons() {
        helpPaneIcon.setImage((new Image("/Resources/Images/white/help_white.png")));
    }

    /**
     * Helps initialising the icons according to the chosen team
     */
    public void lightIcons() {
        helpPaneIcon.setImage((new Image("/Resources/Images/black/help_black.png")));
    }
}
