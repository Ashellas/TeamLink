package controllers;

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
import java.util.ResourceBundle;

public class ads implements Initializable{

    UserSession user;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = new UserSession("Atak");
    }

    public void changeScene(ActionEvent event) throws IOException {
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
}
