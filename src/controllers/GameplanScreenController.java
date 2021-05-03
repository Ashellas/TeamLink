package controllers;

import com.sun.scenario.effect.impl.state.GaussianRenderState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import models.InitializeData;
import models.UserSession;


public class GameplanScreenController extends MainTemplateController {

    @FXML
    private GridPane gameplansGrid;

    @FXML
    private ComboBox teamBox;

    @FXML
    private Label fileNamelabel;

    @FXML
    private TextField titleField;

    @FXML
        private GridPane addGrid;

    public void initData(UserSession user){
        super.initData(user);
    }





    public void changeAddGridVisibility(ActionEvent event) {
    }

    public void submitButtonPushed(ActionEvent event) {
    }

    public void uploadButtonpushed(ActionEvent event) {
    }
}
