package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import models.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class SettingsScreenController extends MainTemplateController {
    private final String DARK_STYLE_SHEET = getClass().getResource("/stylesheets/DarkTheme.css").toExternalForm();
    private final String LIGHT_STYLE_SHEET = getClass().getResource("/stylesheets/LightTheme.css").toExternalForm();

    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    //-----------------Main Part--------------------------//
    @FXML
    private GridPane settingsPane;

    @FXML
    private Pane darkPane;

    private ObservableList<String> cityList = FXCollections.observableArrayList("Istanbul", "Ankara", "İzmir");

    private ObservableList<String> ageGroupList = FXCollections.observableArrayList("U18", "U16", "U14", "U12");

    private String selectedAgeGroup = "", selectedCity = "", selectedLeague = "";

    private File selectedFile;

    @FXML
    private ImageView accountPhoto;

    @FXML
    private ImageView teamPhoto;

    @FXML
    private Label teamCode;

    @FXML
    private ComboBox<Team> teamCombobox;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button editAccountButton;

    @FXML
    private Button changePhotoButton;

    @FXML
    private CheckBox darkThemeCheck;

    @FXML
    private CheckBox notificationsCheck;

    @FXML
    private ImageView copyIcon;

    //--------------------------------------------//

    //-------------------Edit Pane----------------//
    @FXML
    private GridPane editTeamPane;

    @FXML
    private TextField teamNameEditField;

    @FXML
    private TextField abbrevationEditField;

    @FXML
    private ComboBox<String> chooseCityBox;

    @FXML
    private ComboBox<Team> chooseLeagueBox;

    @FXML
    private ComboBox<String> chooseLeagueTeamBox;

    @FXML
    private ImageView logoChangeImage;

    @FXML
    private Button uploadTeamLogoButton;

    //--------------------------------------------//

    //----------------Create Pane-----------------//

    @FXML
    private GridPane createTeamPane;

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

    @Override
    public void initData(UserSession user){
        super.initData(user);

        // Theme selection
        if(user.isStyleDark()) {
            darkIcons();
            darkThemeCheck.setSelected(true);
        }
        else {
            lightIcons();
            darkThemeCheck.setSelected(false);
        }

        // Filling in account info
        userNameField.setText(user.getUser().getFirstName() + " " + user.getUser().getLastName());
        emailField.setText(user.getUser().getEmail());
        datePicker.setValue(user.getUser().getBirthday());

        // Filling team combo box
        for(Team team : user.getUserTeams()){
            teamCombobox.getItems().add(team);
        }
        teamCombobox.getSelectionModel().selectFirst();

        if (teamCombobox.getValue().getTeamLogo() != null) {
            teamPhoto.setImage(teamCombobox.getValue().getTeamLogo().getImage());
            teamCode.setText("Team code : ");
        }
        teamCode.setText("Team code : " + teamCombobox.getValue().getTeamCode());

        // Editing only
        changePhotoButton.setDisable(true);
        changePhotoButton.setVisible(false);
        if (user.getUser().getProfilePhoto() == null) {
            changePhotoButton.setText("Select Photo");
        }

        // Top panes initialise
        editTeamPane.setVisible(false);
        editTeamPane.setDisable(true);
        createTeamPane.setVisible(false);
        createTeamPane.setDisable(true);
        darkPane.setDisable(true);
        darkPane.setVisible(false);

        // Fade in
        AppManager.fadeIn(settingsPane,500);
    }

    //--------------------Main Pane----------------------------//
    public void editAccount(ActionEvent actionEvent) throws SQLException {
        if (editAccountButton.getText().equals("Edit")) {
            userNameField.setEditable(true);
            emailField.setEditable(true);
            datePicker.setEditable(true);
            changePhotoButton.setVisible(true);
            changePhotoButton.setDisable(false);
            userNameField.getStylesheets().add("/stylesheets/Active.css");
            emailField.getStylesheets().add("/stylesheets/Active.css");
            datePicker.getStylesheets().add("/stylesheets/Active.css");
            editAccountButton.getStylesheets().add("/stylesheets/Active.css");
            editAccountButton.setText("Save");
        }
        else if (validInput()) {
            userNameField.setEditable(false);
            emailField.setEditable(false);
            datePicker.setEditable(false);
            changePhotoButton.setVisible(false);
            changePhotoButton.setDisable(true);
            userNameField.getStylesheets().remove("/stylesheets/Active.css");
            emailField.getStylesheets().remove("/stylesheets/Active.css");
            datePicker.getStylesheets().remove("/stylesheets/Active.css");
            editAccountButton.getStylesheets().remove("/stylesheets/Active.css");
            user.getUser().setName(userNameField.getText());
            user.getUser().setEmail(emailField.getText());
            user.getUser().setBirthday(datePicker.getValue());
            editAccountButton.setText("Edit");
            userNameLabel.setText(user.getUser().getFirstName());

            if (selectedFile != null) {
                profileIcon.setImage(new Image(selectedFile.toURI().toString()));
            }
        }
    }

    public void changePassword(ActionEvent actionEvent) {
    }

    public void deleteAccount(ActionEvent actionEvent) {
    }

    public void copyCode(ActionEvent actionEvent) {
        content.putString(String.valueOf(teamCombobox.getValue().getTeamCode()));
        clipboard.setContent(content);
        displayMessage(messagePane,"Code copied",false);
    }

    public void deleteTeam(ActionEvent actionEvent) {}

    public void changePhoto(ActionEvent actionEvent) {
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
            accountPhoto.setImage(new Image(selectedFile.toURI().toString()));
            changePhotoButton.setText("Change Photo");
        }
    }

    public void changeTheme(ActionEvent actionEvent) throws IOException {
        if (user.isStyleDark()){
            user.setStyleSheet(LIGHT_STYLE_SHEET);
        }
        else {
            user.setStyleSheet(DARK_STYLE_SHEET);
        }
        AppManager.changeScene(getClass().getResource("/views/SettingsScreen.fxml"),actionEvent, user);

    }

    public void turnOffNotifications(ActionEvent actionEvent) {
    }

    public void teamChange() {
        if (teamCombobox.getValue().getTeamLogo() != null) {
            teamPhoto.setImage(teamCombobox.getValue().getTeamLogo().getImage());
        }
        teamCode.setText("Team code : " + teamCombobox.getValue().getTeamCode());
    }

    public void createNewTeam(ActionEvent actionEvent) throws IOException {
        createTeamPane.setDisable(false);
        createTeamPane.setVisible(true);
        darkPane.setDisable(false);
        darkPane.setVisible(true);
        chooseCityBoxCreate.getItems().addAll(cityList);
        chooseAgeGroupCreate.getItems().addAll(ageGroupList);

        chooseLeagueBoxCreate.setDisable(true);
        chooseLeagueTeamBoxCreate.setDisable(true);
    }

    public void editTeam(ActionEvent actionEvent) throws IOException, SQLException {
        teamNameEditField.setText(teamCombobox.getValue().getTeamName());
        abbrevationEditField.setText(teamCombobox.getValue().getAbbrevation());

        if (teamCombobox.getValue().getTeamLogo() != null) {
            logoChangeImage.setImage(teamCombobox.getValue().getTeamLogo().getImage());
            uploadTeamLogoButton.setText("Change Photo");
        }

        editTeamPane.setDisable(false);
        editTeamPane.setVisible(true);
        darkPane.setDisable(false);
        darkPane.setVisible(true);
    }

    @Override
    public void helpButtonPushed(ActionEvent actionEvent){
        // TODO
    }

    //-------------------------Team Edit---------------------------//

    public void saveChanges(ActionEvent actionEvent) {}

    public void closeButtonPushed(ActionEvent actionEvent) {
        editTeamPane.setDisable(true);
        editTeamPane.setVisible(false);
        darkPane.setDisable(true);
        darkPane.setVisible(false);
    }

    public void changeTeamLogo(ActionEvent actionEvent) {
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
            uploadTeamLogoButton.setText("Change Photo");
            logoChangeImage.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    //------------------Team Create------------------------//

    public void createTeamLogo(ActionEvent actionEvent) {
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
            uploadTeamLogoButtonCreate.setText("Change Photo");
            logoChangeImageCreate.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    public void createTeam(ActionEvent actionEvent) throws IOException, SQLException {
        int currentTeams = user.getUserTeams().size();
        if (validCreateInput()) {
            user = DatabaseManager.createTeam(user, teamNameCreateField.getText(), abbrevationCreateField.getText(), chooseCityBoxCreate.getValue().toString(),
                    chooseAgeGroupCreate.getValue(), chooseLeagueBoxCreate.getValue().toString(), chooseLeagueTeamBoxCreate.getValue().toString(), selectedFile);
            if (user.getUserTeams().size() > currentTeams){
                createPaneClose(actionEvent);
                displayMessage(messagePane, "Team created", false);
            }
            else {
                displayMessage(messagePane, "An Error Occured", true);
            }
        }
    }

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

    public void createPaneClose(ActionEvent actionEvent) {
        createTeamPane.setDisable(true);
        createTeamPane.setVisible(false);
        darkPane.setDisable(true);
        darkPane.setVisible(false);
        logoChangeImageCreate.setImage(new Image("/Resources/Images/emptyTeamLogo.png"));
    }

    //--------------------HELPER-------------------------------//
    public void darkIcons() {
        teamPhoto.setImage((new Image("/Resources/Images/emptyTeamLogo.png")));
        copyIcon.setImage((new Image("/Resources/Images/white/copy_white.png")));
        if (user.getUser().getProfilePhoto() == null) {
            accountPhoto.setImage((new Image("/Resources/Images/white/big_profile_white.png")));
        }

    }

    public void lightIcons() {
        teamPhoto.setImage((new Image("/Resources/Images/emptyTeamLogo.png")));
        copyIcon.setImage((new Image("/Resources/Images/black/copy_black.png")));
        if (user.getUser().getProfilePhoto() == null) {
            accountPhoto.setImage((new Image("/Resources/Images/black/big_profile_black.png")));
        }
    }

    private boolean validInput() throws SQLException {
        if (userNameField.getText().length() == 0) {
            displayMessage(messagePane, "Name cannot be empty", true);
            return false;
        }
        else if (userNameField.getText().length() == 1) {
            displayMessage(messagePane, "Name cannot be empty", true);
            return false;
        }
        else if (!isAllLetters(userNameField.getText())) {
            displayMessage(messagePane, "Names must be all letters", true);
            return false;
        }
        else if (!(userNameField.getText().contains(" ")) || userNameField.getText().charAt(0) == ' ') {
            displayMessage(messagePane, "Name must consist of two words", true);
            return false;
        }
        else if (!(emailField.getText().contains("@"))) {
            displayMessage(messagePane, "Invalid email", true);
            return false;
        }
        else if (DatabaseManager.isEmailTaken(user.getDatabaseConnection(), emailField.getText()) && !(emailField.getText().equals(user.getUser().getEmail()))){
            displayMessage(messagePane, "Email is used before", true);
            return false;
        }
        else {
            displayMessage(messagePane,"Changes are saved", false);
            return true;
        }
    }

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

    private boolean validEditInput() throws SQLException {
        // Checks if any of the fields is empty
        if(teamNameEditField.getText().equals("") || abbrevationEditField.getText().equals("")
                || chooseCityBox.getValue() == null || chooseLeagueBox.getValue() == null
                || chooseLeagueTeamBox.getValue() == null) {
            displayMessage(messagePane,"Please fill all the fields",true);
            return false;
        }
        // Checks the abbrevation length
        else if(abbrevationEditField.getText().length() > 3)
        {
            displayMessage(messagePane,"Abbrevations must be at most 3 characters",true);
            return false;
        }
        // Checks team name length
        else if (teamNameEditField.getText().length() > 30){
            displayMessage(messagePane,"Team Names must be smaller than 30 characters",true);
            return false;
        }
        return true;
    }

    /**
     * Checkes if the given text consists only of letters
     * @param text the given string to check
     * @return true if the text is all letters
     */
    private boolean isAllLetters(String text) {
        for ( int i = 0; i < text.length(); i++){
            if(!(Character.isAlphabetic(text.charAt(i)) || text.charAt(i) == ' '))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void toSettingsScreen(ActionEvent actionEvent) throws IOException {}

}
