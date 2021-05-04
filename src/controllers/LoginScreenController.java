package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

/**
 * Controls the login screen and all its functions
 */
public class LoginScreenController implements  InitializeData{

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private GridPane errorPane;

    private UserSession userSession;

    @FXML
    private ImageView logo;

    @FXML
    private ImageView helpIcon;

    @FXML
    private Pane disablePane;

    @FXML
    private GridPane helpPane;

    @FXML
    private ImageView helpPaneIcon;

    @FXML
    private GridPane resetPasswordPane;

    @FXML
    private Button sendEmailButton;

    @FXML
    private CheckBox approval;

    @FXML
    private TextField resetEmailField;

    private Executor exec;

    private Stage loading;

    private final String MAIL_SUBJECT = "Password Change";

    private final String MAIL_FROM = "TeamLink.host@gmail.com";

    private String tempPassword;

    @Override
    public void initData(UserSession user) {
        userSession = user;

        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        try{
            createLoading();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(userSession.isStyleDark()) {
            darkThemeIcons();
        }
        else {
            lightThemeIcons();
        }
        disablePane.setVisible(false);
        disablePane.setDisable(true);
        helpPane.setVisible(false);
        helpPane.setDisable(true);
        resetPasswordPane.setVisible(false);
        resetPasswordPane.setDisable(true);
        sendEmailButton.setDisable(true);
        approval.setSelected(false);
    }

    public void forgotPasswordLinkPushed(ActionEvent event) {
        approveChange(event);
        resetPasswordPane.setVisible(true);
        resetPasswordPane.setDisable(false);
        disablePane.setVisible(true);
        disablePane.setDisable(false);
    }


    public void resetPaneClose(ActionEvent event) {
        resetPasswordPane.setVisible(false);
        resetPasswordPane.setDisable(true);
        disablePane.setVisible(false);
        disablePane.setDisable(true);
    }


    public void sendMailButtonPushed(ActionEvent event) throws SQLException {
        int min = 10000000;
        int max = 99999999;
        tempPassword = String.valueOf((int) ((Math.random() * (max - min)) + min));
        TeamMember member = DatabaseManager.getTeamMemberByEmail(resetEmailField.getText());
        if (member != null) {
            send(MAIL_FROM,"gzdbjvklvtofhphq",resetEmailField.getText(), MAIL_SUBJECT,
                    "The password for the account with this mail address has been changed to " +  tempPassword);
            DatabaseManager.passwordChange(userSession.getDatabaseConnection(), member, tempPassword);
            displayMessage(errorPane, "Password is changed",false);
            resetPaneClose(event);
        }
        else {
            displayMessage(errorPane,"No account found", true);
        }

    }

    public void approveChange(ActionEvent event) {
        if (approval.isSelected()) {
            sendEmailButton.setDisable(false);
        }
        else {
            sendEmailButton.setDisable(true);
        }
    }

    /**
     * Checkes the login information, changes the scene to appropriate screen according to user
     * Displays error message if the user with given information is not in database
     * @param event login button pushed
     * @throws SQLException
     * @throws IOException
     */
    public void loginButtonPushed(ActionEvent event) throws  IOException {
        //TODO if fields are empty do not check
        createUserSession(event);
        disablePane.setVisible(true);
        loading.show();
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
        disablePane.setVisible(true);
        disablePane.setDisable(false);
        helpPane.setVisible(true);
        helpPane.setDisable(false);
        helpIcon.setVisible(false);
    }

    public void helpPaneClose(ActionEvent event) throws IOException {
        disablePane.setVisible(false);
        disablePane.setDisable(true);
        helpPane.setVisible(false);
        helpPane.setDisable(true);
        helpIcon.setVisible(true);
    }


    /**
     * Creates user session with users information or empty
     * @param event login button pushed or app start
     */
    private void createUserSession(ActionEvent event) {

        Task<UserSession> userCreateTask =  new Task<UserSession>() {
            @Override
            public UserSession call() throws Exception {
                System.out.println(" Succeed at : " + new java.util.Date());
                userSession = DatabaseManager.login(userSession, emailField.getText(), passwordField.getText());
                return userSession;
            }
        };
        userCreateTask.setOnFailed(e -> {
            userCreateTask.getException().printStackTrace();
            // inform user of error...
        });

        userCreateTask.setOnSucceeded(e -> {
            if(userSession.getUser() == null) {
                displayMessage(errorPane, "No user found", true);
                System.out.println("failed");
                disablePane.setVisible(false);
            }
            else {
                if (userSession.getUserTeams().size() != 0) {
                    try {
                        AppManager.changeScene(getClass().getResource("/views/SquadScreen.fxml"), event, userSession);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    try {
                        AppManager.changeScene(getClass().getResource("/views/AfterSignupScreen.fxml"), event, userSession);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            loading.close();
            disablePane.setVisible(false);
            System.out.println("gg"); });

        // Task.getValue() gives the value returned from call()...
        // run the task using a thread from the thread pool:
        exec.execute(userCreateTask);
    }

    /**
     * Opens loading window on top of the scene
     * @throws IOException
     */
    private void createLoading() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/LoadingScreen.fxml"));
        loading = new Stage();
        loading.initStyle(StageStyle.UNDECORATED);
        loading.initModality(Modality.APPLICATION_MODAL);
        loading.setScene(new Scene(root));
        disablePane.setOpacity(0.5);
    }

    /**
     * Shows the error message
     * @param errorMessage message to show
     */
    private void displayMessage(Pane pane, String errorMessage, boolean error) {
        System.out.println(errorMessage);
        JFXSnackbar snackbar = new JFXSnackbar(pane);
        if (error) {
            snackbar.getStylesheets().add("/stylesheets/errorSnackBar.css");
        }
        else {
            snackbar.getStylesheets().add("/stylesheets/messageSnackBar.css");
        }

        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(errorMessage)));
    }

    /**
     * Helps initialising icons according to chosen theme
     */
    public void darkThemeIcons() {
        logo.setImage(new Image("/Resources/Images/app_logo.png"));
        helpIcon.setImage(new Image("/Resources/Images/white/help_white.png"));
        helpPaneIcon.setImage((new Image("/Resources/Images/white/help_white.png")));
    }

    /**
     * Helps initialising icons according to chosen theme
     */
    public void lightThemeIcons() {
        logo.setImage(new Image("/Resources/Images/appLogo_Light.png"));
        helpIcon.setImage(new Image("/Resources/Images/black/help_black.png"));
        helpPaneIcon.setImage(new Image("/Resources/Images/black/help_black.png"));
    }

    public void send(String from,String password,String to,String sub,String msg){
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}

    }


}
