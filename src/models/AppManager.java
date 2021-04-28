package models;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AppManager {
    public static void changeScene(URL fxmlfile, ActionEvent event, UserSession userSession) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlfile);
        loader.load();
        InitializeData c2 = loader.getController();
        c2.initData(userSession);
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(p));
        stage.show();
    }
}