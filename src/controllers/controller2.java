package controllers;

import javafx.fxml.Initializable;
import models.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class controller2 {

    UserSession user;

    public void initData(UserSession user){
        this.user = user;
        System.out.println(user.getFirstName());
    }


}
