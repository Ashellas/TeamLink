package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.AppManager;
import models.DatabaseManager;
import models.InitializeData;
import models.UserSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


/**
 * @version 24.04.2021
 * errorPane is missing
 * help button action is missing
 * Does not connect to database
 */
public class SignupController implements InitializeData {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox roleBox;

    @FXML
    private ComboBox sportBranchBox;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView helpIcon;

    @FXML
    private ImageView backIcon;

    @FXML
    private Pane errorPane;

    private Connection myCon;

    private ObservableList<String> roleList = FXCollections.observableArrayList("Head Coach", "Assistant Coach", "Player");

    private ObservableList<String> sportBranchList = FXCollections.observableArrayList("Football", "Basketball", "Volleyball");

    private File selectedFile;

    private UserSession user;


    @Override
    public void initData(UserSession userSession) {
        user = userSession;

        roleBox.getItems().addAll(roleList);
        sportBranchBox.getItems().addAll(sportBranchList);

        if(userSession.isStyleDark()) {
            darkThemeIcons();
        }
        else {
            lightThemeIcons();
        }
    }
    /**
     * Saves the player info into database if teamCode is working and every text field is filled
     * @param event submit button pushed
     * @throws SQLException
     */
    public void submitButtonPushed(ActionEvent event) throws SQLException, IOException {
        //If there is no error in the form, saves it into database
        if( !isThereAnError() )
        {
            if(DatabaseManager.signUpUser(user, firstNameField.getText(),
                    lastNameField.getText(), emailField.getText(), java.sql.Date.valueOf(dateOfBirthPicker.getValue()),
                    passwordField.getText(), roleBox.getValue().toString(),
                    sportBranchBox.getValue().toString(), selectedFile).getUser() != null){
                AppManager.changeScene(getClass().getResource("/views/AfterSignupScreen.fxml"), event, user);
            }
        }
    }

    /**
     * Opens the file chooser for the user to select the image
     * @param event upload image button pushed
     */
    public void uploadImageButtonPushed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture Chooser");
        //Sets up the initial directory as user folder when filechooser is opened
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        //sets the file type options
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG and JPG files", "*.png","*.jpg","*.jpeg"));

        selectedFile = fileChooser.showOpenDialog(null);

        if( selectedFile != null)
        {
            // Upload button's text is changed and the display image is changed to the selected image
            uploadImageButton.setText("Change");
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    /**
     * Changes the scene back to the login screen
     * @param event back button pushed
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/LoginScreen.fxml"), event, user);
    }

    /**
     * Shows the help information for the current scene
     * @param event help button pushed
     */
    public void helpButtonPushed(ActionEvent event) {
        // TODO
    }


    /**
     * Checks if the requirements for account creation is satisfied or not
     * Displays an error message according to the error
     * @return false if every requirement is satisfied
     */
    private boolean isThereAnError() throws SQLException {
        // Checks if any of the fields are empty
        if(firstNameField.getText().equals("") || lastNameField.getText().equals("") || emailField.getText().equals("")
                || passwordField.getText().equals("") || roleBox.getValue() == null || dateOfBirthPicker.getValue() == null ||
                confirmPasswordField.getText().equals("") || sportBranchBox.getValue() == null){
            displayError("Please fill all the fields");
            return true;
        }
        // Checks if the password and the confirmation are the same
        else if (!confirmPasswordField.getText().equals(passwordField.getText())){
            displayError("Passwords do not match");
            return true;
        }
        //Checks the password length
        else if(passwordField.getText().length() < 8 || passwordField.getText().length() > 16)
        {
            displayError("Passwords must be between 8-16 characters");
            return true;
        }
        //Checks if the name does not contain numbers or punctuations
        else if( !isAllLetters( firstNameField.getText()) || !isAllLetters( lastNameField.getText()))
        {
            displayError("Names must be all letters");
            return true;
        }
        //Checks the length of names
        else if (firstNameField.getText().length() > 20 || lastNameField.getText().length() > 20){
            displayError("Names must be smaller than 20 characters");
            return true;
        }
        //Checks if the mail contains '@'
        else if (emailField.getText().indexOf('@') == -1)
        {
            displayError("Invalid Email");
            return true;
        }

        if(DatabaseManager.isEmailTaken(user.getDatabaseConnection(), emailField.getText())){
            displayError("Email is used before");
            return true;
        }

        // If there are not errors returns false
        return false;
    }

    /**
     * Checkes if the given text consists only of letters
     * @param text the given string to check
     * @return true if the text is all letters
     */
    private boolean isAllLetters(String text) {
        for ( int i = 0; i < text.length(); i++){
            if(!Character.isAlphabetic(text.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Shows the error message
     * @param errorMessage message to show
     */
    private void displayError(String errorMessage){
        System.out.println(errorMessage);
        JFXSnackbar snackbar = new JFXSnackbar(errorPane);

        snackbar.setPrefWidth(300.0);
        snackbar.getStylesheets().add("sample/errorSnackBar.css");
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(errorMessage)));
    }


    public void darkThemeIcons() {
        backIcon.setImage(new Image("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png"));
        helpIcon.setImage(new   Image("/Resources/Images/white/help_white.png"));
    }

    public void lightThemeIcons() {
        backIcon.setImage(new Image("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png"));
        helpIcon.setImage(new   Image("/Resources/Images/black/help_black.png"));
    }
}
