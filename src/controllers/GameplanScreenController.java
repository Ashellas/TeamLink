package controllers;

import com.sun.scenario.effect.impl.state.GaussianRenderState;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import models.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


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

        addGrid.setVisible(false);
    }


    private void setGameplansGrid(){
        int gridCounter = 0;
        for( Gameplan gameplan : user.getGameplans(user.getUserTeams().get(0))){
            int row = 1 + ((int) gridCounter/ 8) * 2;
            int column = (1 + gridCounter) % 8;
            gameplansGrid.add(createAGameplan(gameplan), column, row);
            gridCounter += 2;
        }
    }

    private GridPane createAGameplan(Gameplan gameplan){
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().addAll("gameplan");
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

        File file = new File(gameplan.getFilePath());

        Button firstButton;
        if(file.exists()){
            firstButton = new Button("View");
        }
        else{
            firstButton = new Button("Download");
        }
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(emptyHBox.getWidth() * 0.4);
        deleteButton.setOnAction(event -> {

            if(file.delete()){
                user.getGameplans(user.getUserTeams().get(0)).remove(gameplan);
                deleteAllViews();
                setGameplansGrid();
            }
        });
        firstButton.setPrefWidth(emptyHBox.getWidth() * 0.4);
        firstButton.setOnAction(event -> {
            if(file.exists()){
                if (Desktop.isDesktopSupported()) {
                    new Thread(() -> {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            else{
                try {
                    downloadPDF(gameplan.getFileId(), gameplan.getFilePath());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                firstButton.setText("View");
            }
        });



        GridPane.setHalignment(gameplanTitle, HPos.CENTER);
        GridPane.setHalignment(deleteButton, HPos.CENTER);
        GridPane.setHalignment(firstButton, HPos.CENTER);

        gameplanTitle.getStyleClass().add("title");
        gridPane.add(gameplanTitle, 0,1);
        gridPane.add(firstButton, 0, 2);
        gridPane.add(deleteButton, 0, 3);
        return gridPane;
    }

    private void deleteAllViews() {
        ObservableList<Node> childrens = gameplansGrid.getChildren();

        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 3) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 5) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 7) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 1) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 3) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 5) {
                gameplansGrid.getChildren().remove(node);
            }
            else if(GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 7) {
                gameplansGrid.getChildren().remove(node);
            }
        }
    }

    private void downloadPDF(int fileId, String filePath) throws SQLException, IOException {
        DatabaseManager.downloadGameplan(user.getDatabaseConnection(), fileId, filePath);
    }

    public void changeAddGridVisibility(ActionEvent event) {
        addGrid.setVisible(!addGrid.isVisible());
    }

    public void submitButtonPushed(ActionEvent event) {
    }

    public void uploadButtonpushed(ActionEvent event) {
    }
}
