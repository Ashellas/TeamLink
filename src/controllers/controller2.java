package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.UserSession;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class controller2 implements Initializable{



    @FXML
    private Label name;
    UserSession user;

    public void initData(UserSession user){
        this.user = user;
        name.setText(user.getFirstName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
