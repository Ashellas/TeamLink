package controllers;

import com.sun.scenario.effect.impl.state.GaussianRenderState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import models.Gameplan;
import models.InitializeData;
import models.UserSession;

import java.io.IOException;


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

    @FXML
    private HBox emptyHBox;


    public void initData(UserSession user){
        super.initData(user);
        setGameplansGrid();

        Platform.runLater(() -> {
            setGameplansGrid();
        });
    }


    private void setGameplansGrid(){
        Gameplan gameplan = new Gameplan(1, "Title",null,1,1);
        for (int gridCounter = 0; gridCounter < 16; gridCounter += 2) {
            int row = 1 + ((int) gridCounter/ 8) * 2;
            int column = (1 + gridCounter) % 8;
            gameplansGrid.add(createAGameplan(gameplan), column, row);
        }
    }

    private GridPane createAGameplan(Gameplan gameplan){
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().addAll("mainGrid");
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(5);
        row1.setMinHeight(0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(30);
        row2.setMinHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(30);
        row3.setMinHeight(0);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(30);
        row4.setMinHeight(0);
        RowConstraints row5 = new RowConstraints();
        row5.setPercentHeight(5);
        row5.setMinHeight(0);
        gridPane.getRowConstraints().addAll(row1, row2, row3, row4, row5);
        Label gameplanTitle = new Label(gameplan.getTitle());
        gameplanTitle.setPrefWidth(emptyHBox.getWidth());
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(emptyHBox.getWidth() * 0.4);
        Button viewButton = new Button("View");
        viewButton.setPrefWidth(emptyHBox.getWidth() * 0.4);

        GridPane.setHalignment(gameplanTitle, HPos.CENTER);
        GridPane.setHalignment(deleteButton, HPos.CENTER);
        GridPane.setHalignment(viewButton, HPos.CENTER);

        gameplanTitle.getStyleClass().add("title");
        gridPane.add(gameplanTitle, 0,1);
        gridPane.add(viewButton, 0, 2);
        gridPane.add(deleteButton, 0, 3);
        return gridPane;
    }


    public void changeAddGridVisibility(ActionEvent event) {
    }

    public void submitButtonPushed(ActionEvent event) {
    }

    public void uploadButtonpushed(ActionEvent event) {
    }
}
