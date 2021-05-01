package models;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class AppManager {

    public static String getLastSyncText(Date lastSync){
        long differenceInMinutes = (new Date().getTime() - lastSync.getTime()) / 1000 / 60;
        if(differenceInMinutes < 2){
            return "few moments ago";
        }
        else if (differenceInMinutes < 60){
            return (int)differenceInMinutes + " mins ago";
        }
        else {
            return "more than an hour ago";
        }
    }

    public static void fadeOut(Node node, int duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration),node);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

    public static void fadeIn(Node node, int duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration),node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public static void changeScene(URL fxmlfile, ActionEvent event, UserSession userSession) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlfile);
        loader.load();
        InitializeData c2 = loader.getController();
        c2.initData(userSession);
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(p, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight());
        scene.getStylesheets().add(userSession.getStyleSheet());
        stage.setScene(scene);
        stage.show();
    }


    public static void openScene(URL fxmlfile, ActionEvent event, UserSession userSession, Team team) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlfile);
        loader.load();
        TeamEditScreenController c2 = loader.getController();
        c2.initData(userSession);
        c2.getInformation(team);
        Parent p = loader.getRoot();
        Stage teamEdit = new Stage();
        Scene scene = new Scene(p);
        teamEdit.initModality(Modality.APPLICATION_MODAL);
        teamEdit.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(userSession.getStyleSheet());
        teamEdit.setScene(scene);
        teamEdit.setWidth(1100);
        teamEdit.setHeight(700);
        teamEdit.setResizable(false);
        teamEdit.show();
    }

}