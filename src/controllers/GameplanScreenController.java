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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import models.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;


public class GameplanScreenController extends MainTemplateController {

    @FXML
    private GridPane gameplansGrid;

    @FXML
    private ComboBox<String> teamBox;

    @FXML
    private Label fileNamelabel;

    @FXML
    private TextField titleField;

    @FXML
    private GridPane addGrid;

    @FXML
    private Button uploadButton;

    private ArrayList<GridPane> gameplanViewsGrids;

    @FXML
    private HBox emptyHBox;

    private File selectedFile;

    private double emptyHBoxWidth;

    public void initData(UserSession user){
        gameplanViewsGrids = new ArrayList<>();
        super.initData(user);


        Platform.runLater(() -> {
            emptyHBoxWidth = emptyHBox.getWidth();
            try {
                setGameplansGrid();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        addGrid.setVisible(false);
    }


    private void setGameplansGrid() throws URISyntaxException {
        int gridCounter = 0;
        for( Gameplan gameplan : user.getGameplans(user.getUserTeams().get(0))){
            int row = 1 + ((int) gridCounter/ 8) * 2;
            int column = (1 + gridCounter) % 8;
            GridPane gameplanViewgrid = createAGameplan(gameplan);
            gameplansGrid.add(gameplanViewgrid, column, row);
            gameplanViewsGrids.add(gameplanViewgrid);
            gridCounter += 2;
        }
    }

    private GridPane createAGameplan(Gameplan gameplan) throws URISyntaxException {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().addAll("gameplan");
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        row1.setMinHeight(0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        row2.setMinHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(20);
        row3.setMinHeight(0);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(20);
        row4.setMinHeight(0);
        RowConstraints row5 = new RowConstraints();
        row5.setPercentHeight(20);
        row5.setMinHeight(0);
        RowConstraints row6 = new RowConstraints();
        row6.setPercentHeight(10);
        row6.setMinHeight(0);
        gridPane.getRowConstraints().addAll(row1, row2, row3, row4, row5,row6);
        Label gameplanTitle = new Label(gameplan.getTitle());
        gameplanTitle.setPrefWidth(emptyHBoxWidth);

        File file = new File(gameplan.getFilePath());

        Button firstButton = new Button();
        Button uninstallButton = new Button("Uninstall");


        firstButton.getStyleClass().add("viewGameplan");
        uninstallButton.getStyleClass().add("uninstall");
        if(file.exists()){
            firstButton.setText("View");
            uninstallButton.setDisable(false);
        }
        else{
            firstButton.setText("Download");
            uninstallButton.setDisable(true);
        }
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("deleteGameplan");
        deleteButton.setPrefWidth(emptyHBoxWidth * 0.4);
        deleteButton.setOnAction(event -> {
                user.getGameplans(user.getUserTeams().get(0)).remove(gameplan);
                System.out.println();
                for(GridPane grid : gameplanViewsGrids){
                    gameplansGrid.getChildren().remove(grid);
                }
            try {
                setGameplansGrid();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        uninstallButton.setPrefWidth(emptyHBoxWidth * 0.4);
        uninstallButton.setOnAction(event -> {
            file.delete();
            firstButton.setText("Download");
            uninstallButton.setDisable(true);
        });

        firstButton.setPrefWidth(emptyHBoxWidth * 0.4);
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
                uninstallButton.setDisable(false);
            }
        });

        GridPane.setHalignment(gameplanTitle, HPos.CENTER);
        GridPane.setHalignment(deleteButton, HPos.CENTER);
        GridPane.setHalignment(firstButton, HPos.CENTER);
        GridPane.setHalignment(uninstallButton, HPos.CENTER);

        gameplanTitle.getStyleClass().add("title");
        firstButton.getStylesheets().add(getClass().getResource("/stylesheets/ButtonStyleSheet.css").toURI().toString());
        uninstallButton.getStylesheets().add(getClass().getResource("/stylesheets/ButtonStyleSheet.css").toURI().toString());
        deleteButton.getStylesheets().add(getClass().getResource("/stylesheets/ButtonStyleSheet.css").toURI().toString());



        gridPane.add(gameplanTitle, 0,1);
        gridPane.add(firstButton, 0, 2);
        gridPane.add(uninstallButton,0, 3);
        gridPane.add(deleteButton, 0, 4);
        return gridPane;
    }

    //TODO set it to background
    private void downloadPDF(int fileId, String filePath) throws SQLException, IOException {
        DatabaseManager.downloadGameplan(user.getDatabaseConnection(), fileId, filePath);
    }

    public void changeAddGridVisibility(ActionEvent event) {
        titleField.setText("");
        selectedFile = null;
        addGrid.setVisible(!addGrid.isVisible());
    }

    public void submitButtonPushed(ActionEvent event) {

    }


    public void uploadButtonpushed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Picture Chooser");
        //Sets up the initial directory as user folder when filechooser is opened
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        //sets the file type options
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));

        selectedFile = fileChooser.showOpenDialog(null);

        if( selectedFile != null)
        {
            uploadButton.setText("Change PDF");
        }
        fileNamelabel.setText(selectedFile.getName());
    }
}
