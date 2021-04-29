package models;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class AppManager {
    public static void changeScene(URL fxmlfile, ActionEvent event, UserSession userSession) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlfile);
        loader.load();
        InitializeData c2 = loader.getController();
        c2.initData(userSession);
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p, ((Node) event.getSource()).getScene().getWidth(), ((Node) event.getSource()).getScene().getHeight()));
        stage.show();
    }

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
}