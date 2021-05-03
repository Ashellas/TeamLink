package controllers;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.DatabaseManager;
import models.Team;
import models.TeamMember;
import models.UserSession;
import org.controlsfx.control.PopOver;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DemoSceneController implements Initializable {
    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label counter;

    @FXML
    private Label lastSyncLabel;

    private Executor exec;

    private Executor executor;

    @FXML
    private ImageView syncImage;

    private UserSession user;

    private TeamMember member;

    private Date timeLoaded;

    @FXML
    private ProgressBar loader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        executor = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(false);
            return t ;
        });
        System.out.println(" Tried at : " + new java.util.Date());
        createUserSession();
        rotate();
        timeLoaded = new Date();
        }

    //Indefinite rotation
    private void rotate() {
        RotateTransition rt = new RotateTransition(Duration.seconds(2), syncImage);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();

    }

    private void createUserSession() {
        Task<UserSession> userCreateTask = new Task<UserSession>() {
            @Override
            public UserSession call() throws Exception {
                user =  new UserSession(null, null);
                System.out.println(" Succeed at : " + new java.util.Date());
                return user;
            }
        };
        userCreateTask.setOnFailed(e -> {
            userCreateTask.getException().printStackTrace();
            // inform user of error...
        });

        userCreateTask.setOnSucceeded(e ->
                // Task.getValue() gives the value returned from call()...
                System.out.println("gg"));

        // run the task using a thread from the thread pool:
        exec.execute(userCreateTask);
    }

    public void toMainScreen(ActionEvent actionEvent) {
    }

    public void toSquadScreen(ActionEvent actionEvent) {
    }

    public void toCalendarScreen(ActionEvent actionEvent) {
    }

    public void login(ActionEvent actionEvent) throws SQLException {
        System.out.println(" Tried at : " + new java.util.Date());
        loginUser();
    }

    private void loginUser() {
        Task<TeamMember> userCreateTask = new Task<TeamMember>() {
            @Override
            public TeamMember call() throws Exception {
                DatabaseManager.login(user, "talay@gmail.com","Talayyucel");
                return member;
            }
        };
        // run the task using a thread from the thread pool:
        exec.execute(userCreateTask);
    }
    public void toGameplanScreen(ActionEvent actionEvent) {
    }

    public void toTrainingsScreen(ActionEvent actionEvent) {
    }

    public void toLeagueScreen(ActionEvent actionEvent) {
    }

    public void toChatScreen(ActionEvent actionEvent) {
    }

    public void toSettingsScreen(ActionEvent actionEvent) {
        
    }

    public void logoutButtonPushed(ActionEvent actionEvent) {
    }

    public void helpButtonPushed(ActionEvent actionEvent) {
    }

    public void SynchronizeData(ActionEvent actionEvent) {
    }


}
