package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AppManager;
import models.DatabaseManager;
import models.InitializeData;
import models.UserSession;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * @version 24.02.2021
 * errorPane is missing (Also in the fxml)
 * help button and forgot password actions are missing
 * does not connect to database
 */
public class LoginScreenController implements  InitializeData{

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Pane errorPane;

    private UserSession userSession;

    @FXML
    private ImageView helpIcon;

    @FXML
    private Pane disablePane;

    @Override
    public void initData(UserSession user) {
        userSession = user;
    }


    public void forgotPasswordLinkPushed(ActionEvent event) {
        // TODO
    }

    /**
     * Checkes the login information, changes the scene to appropriate screen according to user
     * Displays error message if the user with given information is not in database
     * @param event login button pushed
     * @throws SQLException
     * @throws IOException
     */
    public void loginButtonPushed(ActionEvent event) throws SQLException, IOException {
        userSession = DatabaseManager.login(userSession, emailField.getText(), passwordField.getText());
        if(userSession.getUser() == null) {
            //displayError("No user found");
            System.out.println("failed");
        }
        else {
            if (userSession.getUserTeams().size() != 0) {
                AppManager.changeScene(getClass().getResource("/views/MainTemplate2.fxml"), event, userSession);
            } else {
                AppManager.changeScene(getClass().getResource("/views/AfterSignupScreen.fxml"), event, userSession);
            }
        }
    }

    /**
     * Changes the scene to the sign up screen
     * @param event don't have an account link pushed
     * @throws IOException
     */
    public void dontHaveAnAccountLinkPushed(ActionEvent event) throws IOException {
        AppManager.changeScene(getClass().getResource("/views/SignupScreen.fxml"), event, userSession);
    }

    /**
     * Shows the help information for the current scene
     * @param event help button pushed
     */
    public void onHelpButtonPushed(ActionEvent event) throws IOException {
        // TODO
        makeIconsDark();
        AppManager.changeScene(getClass().getResource("/views/LoginScreen.fxml"), event, userSession);




        Stage loading;
        Parent root = FXMLLoader.load(getClass().getResource("/views/LoadingScreen.fxml"));
        loading = new Stage();
        loading.initStyle(StageStyle.UNDECORATED);
        loading.initModality(Modality.APPLICATION_MODAL);
        loading.setScene(new Scene(root));
        disablePane.setOpacity(0.5);
        loading.show();

        

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

    public void makeIconsDark() {
        Image blackHelp = new   Image("/Resources/Images/black/help_black.png");
        helpIcon.setImage(blackHelp);
        System.out.println("aa");
    }


}
