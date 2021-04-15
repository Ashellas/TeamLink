package controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.UserSession;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ads implements Initializable{

    private Executor exec;
    UserSession user;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            user = new UserSession("Atak");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
    }

    public void changeScene(ActionEvent event) throws IOException, SQLException {
        seachDB();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/sample2.fxml"));
        loader.load();
        controller2 c2 = loader.getController();
        c2.initData(user);
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
    }

    private void seachDB() {
        Task<List<String>> nameSearchTask = new Task<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return user.getTeamMemberName();
            }
        };
        nameSearchTask.setOnFailed(e -> {
            nameSearchTask.getException().printStackTrace();
            // inform user of error...
        });

        nameSearchTask.setOnSucceeded(e ->
                // Task.getValue() gives the value returned from call()...
                System.out.println("gg"));

        // run the task using a thread from the thread pool:
        exec.execute(nameSearchTask);
    }
}
