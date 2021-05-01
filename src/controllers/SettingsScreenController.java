package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AppManager;
import models.InitializeData;
import models.Team;
import models.UserSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class SettingsScreenController extends MainTemplateController {
    private final String DARK_STYLE_SHEET = getClass().getResource("/stylesheets/DarkTheme.css").toExternalForm();
    private final String LIGHT_STYLE_SHEET = getClass().getResource("/stylesheets/LightTheme.css").toExternalForm();

    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    @FXML
    private GridPane settingsPane;

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
    private ImageView copyIcon;

    @FXML
    private CheckBox notificationsCheck;

    @Override
    public void initData(UserSession user){
        super.initData(user);

            if(user.isStyleDark()) {
            darkIcons();
            darkThemeCheck.setSelected(true);
        }
        else {
            lightIcons();
            darkThemeCheck.setSelected(false);
        }

        userNameField.setText(user.getUser().getFirstName() + " " + user.getUser().getLastName());
        emailField.setText(user.getUser().getEmail());
        datePicker.setValue(user.getUser().getBirthday());

        teamCode.setText("Team code : ");

        for(Team team : user.getUserTeams()){
            teamCombobox.getItems().add(team);
        }
        teamCombobox.getSelectionModel().selectFirst();

        if (teamCombobox.getValue().getTeamLogo() != null) {
            teamPhoto.setImage(teamCombobox.getValue().getTeamLogo().getImage());
        }

        teamCode.setText("Team code : " + teamCombobox.getValue().getTeamCode());

        changePhotoButton.setDisable(true);
        changePhotoButton.setVisible(false);

        if (user.getUser().getProfilePhoto() == null) {
            changePhotoButton.setText("Select Photo");
        }

        AppManager.fadeIn(settingsPane,500);
    }

    public void editAccount(ActionEvent actionEvent) {
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
        else {
            userNameField.setEditable(false);
            emailField.setEditable(false);
            datePicker.setEditable(false);
            changePhotoButton.setVisible(false);
            changePhotoButton.setDisable(true);
            userNameField.getStylesheets().remove("/stylesheets/Active.css");
            emailField.getStylesheets().remove("/stylesheets/Active.css");
            datePicker.getStylesheets().remove("/stylesheets/Active.css");
            editAccountButton.getStylesheets().remove("/stylesheets/Active.css");
            editAccountButton.setText("Edit");
        }

    }

    public void changePassword(ActionEvent actionEvent) {
    }

    public void deleteAccount(ActionEvent actionEvent) {
    }

    public void editTeam(ActionEvent actionEvent) throws IOException {
        //AppManager.openScene(getClass().getResource("/views/TeamEditScreen.fxml"),actionEvent, user, teamCombobox.getValue());
    }

    public void deleteTeam(ActionEvent actionEvent) {
    }

    public void copyCode(ActionEvent actionEvent) {
        content.putString(String.valueOf(teamCombobox.getValue().getTeamCode()));
        clipboard.setContent(content);
        displayMessage(messagePane,"Code copied",false);
    }


    public void changePhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture Chooser");
        // Sets up the initial directory as user folder when filechooser is opened
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Sets the file type options
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG and JPG files", "*.png","*.jpg","*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if( selectedFile != null)
        {
            // Upload button's text is changed and the display image is changed to the selected image
            changePhotoButton.setText("Change Photo");
            accountPhoto.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    public void createNewTeam(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/TeamCreationScreen.fxml"));
        Stage teamCreation = new Stage();
        teamCreation.initStyle(StageStyle.UNDECORATED);
        teamCreation.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(user.getStyleSheet());
        teamCreation.setScene(scene);
        teamCreation.setWidth(1100);
        teamCreation.setHeight(700);
        teamCreation.setResizable(false);
        teamCreation.show();
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

    @Override
    public void helpButtonPushed(ActionEvent actionEvent){
        // TODO
    }

    public void darkIcons() {
        teamPhoto.setImage((new Image("/Resources/Images/emptyTeamLogo.png")));
        copyIcon.setImage((new Image("/Resources/Images/white/copy_white.png")));
        if (user.getUser().getProfilePhoto() == null) {
            accountPhoto.setImage((new Image("/Resources/Images/white/profile_white.png")));
        }

    }

    public void lightIcons() {
        teamPhoto.setImage((new Image("/Resources/Images/emptyTeamLogo.png")));
        copyIcon.setImage((new Image("/Resources/Images/black/copy_black.png")));
        if (user.getUser().getProfilePhoto() == null) {
            accountPhoto.setImage((new Image("/Resources/Images/black/profile_black.png")));
        }
    }

}
