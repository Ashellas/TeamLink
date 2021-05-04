package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import models.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ChatScreenController extends MainTemplateController implements InitializeData {

    private final int MAX_ROW_INDEX = 4;

    int currentIndex;

    @FXML
    private Label teamNameLabel;

    @FXML
    private ComboBox<Team> teamBox;

    @FXML
    private Label header;

    @FXML
    private ImageView teamLogo;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    @FXML
    private Button submitButton;

    @FXML
    private ImageView arrowIcon;

    @FXML
    private GridPane announcementsGrid;

    @FXML
    private HBox announcementsEmptyHBox;

    private ArrayList<GridPane> gridPanes;

    private int upMoves;

    public void initData(UserSession user){
        super.initData(user);

        if(user.isStyleDark()) {
                darkIcons();
        }
        else {
                lightIcons();
        }

        gridPanes = new ArrayList<GridPane>();

        teamBox.getItems().addAll(user.getUserTeams());
        teamBox.getSelectionModel().selectFirst();

        currentIndex = 0;
        upMoves = user.getAnnouncements(teamBox.getValue()).size() - 5;

        downButton.setDisable(true);

        teamNameLabel.setText(teamBox.getValue().getTeamName());

        if (teamBox.getValue().getTeamLogo() != null) {
            teamLogo.setImage(teamBox.getValue().getTeamLogo().getImage());
        }
        else {
            try {
                teamLogo.setImage(new Image(getClass().getResource("/Resources/Images/emptyTeamLogo.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        Platform.runLater(() -> {
            try {
                setUpAnnouncementsGrid();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void setUpAnnouncementsGrid() throws SQLException {

        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            announcementsGrid.getRowConstraints().add(row);
        }
        int rowIndex = 4;
        for (Announcement announcement : user.getAnnouncements(teamBox.getValue())) {
            GridPane customGrid = createCustomAnnouncementGridPane(announcement.getTitle(), announcement.getDescription());
            GridPane senderPane = createSenderInfoGrid(announcement);
            announcementsGrid.add(senderPane, 0, rowIndex);
            announcementsGrid.add(customGrid, 1, rowIndex);
            gridPanes.add(senderPane);
            gridPanes.add(customGrid);
            rowIndex--;
        }

    }

    public void updateGrid() throws SQLException {
        Announcement announcement;
        int extraAnnouncement;
        int userAnnouncement;
        int rowIndex;
        int firstUserIndex;

        for(GridPane grid : gridPanes){
            announcementsGrid.getChildren().remove(grid);
        }

        int announcementIndex = currentIndex;
        //userAnnouncement = 5 - extraAnnouncement;
        rowIndex = 4;
        //firstUserIndex = extraAnnouncement;

        for (int i = 0; i < 5; i++) {
            if (currentIndex + i < user.getAnnouncements(teamBox.getValue()).size()) {
                announcement = user.getAnnouncements(teamBox.getValue()).get(currentIndex + i);
            }
            else {
                announcement = DatabaseManager.getAnnouncementsByIndex(user.getDatabaseConnection(),teamBox.getValue(),currentIndex + i);
            }
            GridPane customGrid = createCustomAnnouncementGridPane(announcement.getTitle(), announcement.getDescription());
            GridPane senderPane = createSenderInfoGrid(announcement);
            announcementsGrid.add(senderPane, 0, rowIndex - i);
            announcementsGrid.add(customGrid, 1, rowIndex - i);
            gridPanes.add(senderPane);
            gridPanes.add(customGrid);
            rowIndex--;
        }
        /*
        while (userAnnouncement > 0) {
            announcement = user.getAnnouncements(teamBox.getValue()).get(firstUserIndex);
            GridPane customGrid = createCustomAnnouncementGridPane(announcement.getTitle(), announcement.getDescription());
            GridPane senderPane = createSenderInfoGrid(announcement);
            announcementsGrid.add(senderPane, 0, rowIndex);
            announcementsGrid.add(customGrid, 1, rowIndex);
            gridPanes.add(senderPane);
            gridPanes.add(customGrid);
            rowIndex--;
            userAnnouncement--;
            firstUserIndex++;
        }
        while (extraAnnouncement > 0) {
            announcement = DatabaseManager.getAnnouncementsByIndex(user.getDatabaseConnection(),teamBox.getValue(),extraAnnouncement + 4);
            GridPane customGrid = createCustomAnnouncementGridPane(announcement.getTitle(), announcement.getDescription());
            GridPane senderPane = createSenderInfoGrid(announcement);
            announcementsGrid.add(senderPane, 0, rowIndex);
            announcementsGrid.add(customGrid, 1, rowIndex);
            gridPanes.add(senderPane);
            gridPanes.add(customGrid);
            rowIndex--;
            extraAnnouncement--;
        }

         */

    }



    public void moveUp(ActionEvent actionEvent) throws SQLException {
        /*
        if (upMoves > 1) {
            currentIndex++;
            updateGrid();
            upMoves--;
        }
        else if (upMoves == 1) {
            currentIndex++;
            updateGrid();
            upMoves--;
            upButton.setDisable(true);
        }
        downButton.setDisable(false);

         */
        currentIndex++;
        updateGrid();
    }

    public void moveDown(ActionEvent actionEvent) throws SQLException {
        if (currentIndex > 1) {
            currentIndex--;
            upMoves++;
            updateGrid();
        }
        else if (currentIndex == 1) {
            upMoves++;
            currentIndex--;
            downButton.setDisable(true);
            updateGrid();
        }
        upButton.setDisable(false);
    }

    public void teamSelected(ActionEvent actionEvent) throws SQLException {
        setUpAnnouncementsGrid();
        upMoves = user.getAnnouncements(teamBox.getValue()).size() - 5;

    }


    public void sendAnnouncement(ActionEvent actionEvent) throws SQLException {
        Announcement announcement = new Announcement(textField.getText(), textArea.getText(), user.getUser());
        user.getAnnouncements(teamBox.getValue()).add(0,announcement);
        DatabaseManager.createNewAnnouncement(user.getDatabaseConnection(), announcement, teamBox.getValue());
        updateGrid();
    }

    private void lightIcons() {
        arrowIcon.setImage((new Image("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png")));
    }


    private void darkIcons() {
        arrowIcon.setImage((new Image("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png")));
    }

    private GridPane createCustomAnnouncementGridPane(String notTitle, String notDescription){
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        row1.setMinHeight(0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(40);
        row2.setMinHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(40);
        row3.setMinHeight(0);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(10);
        row4.setMinHeight(0);
        gridPane.getRowConstraints().addAll(row1, row2, row3, row4);
        Label title = new Label(notTitle);
        Label description = new Label(notDescription);
        title.setPrefWidth(announcementsEmptyHBox.getWidth() * 0.70);
        description.setPrefWidth(announcementsEmptyHBox.getWidth()*0.70);
        title.getStyleClass().add("title");
        description.getStyleClass().add("description");
        gridPane.add(title, 0, 1);
        gridPane.add(description, 0, 2);
        return gridPane;
    }



    private GridPane createSenderInfoGrid(Announcement announcement){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDate =  sdf.format(announcement.getTimeSent());
        ImageView senderPhoto = new ImageView();

        if(announcement.getSender().getProfilePhoto() == null){
            if(user.isStyleDark()) {
                try {
                    senderPhoto.setImage(new Image(getClass().getResource("/Resources/Images/white/big_profile_white.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    senderPhoto.setImage(new Image(getClass().getResource("/Resources/Images/white/big_profile_black.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            senderPhoto.setImage(announcement.getSender().getProfilePhoto().getImage());
        }

        senderPhoto.setFitHeight(40);
        senderPhoto.setFitWidth(40);
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        BorderPane imageContainer = new BorderPane(senderPhoto);
        row1.setPercentHeight(60);
        row1.setMinHeight(0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        row2.setMinHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(20);
        row3.setMinHeight(0);
        gridPane.getRowConstraints().addAll(row1, row2, row3);
        Label senderNameLabel = new Label(announcement.getSender().getFirstName());
        Label sentDateLabel = new Label(formattedDate);
        senderNameLabel.setPrefWidth(announcementsEmptyHBox.getWidth()*0.55);
        senderNameLabel.getStyleClass().add("little");
        sentDateLabel.setPrefWidth(announcementsEmptyHBox.getWidth()*0.55);
        sentDateLabel.getStyleClass().add("little");
        gridPane.add(imageContainer, 0,0);
        gridPane.add(senderNameLabel, 0, 1);
        gridPane.add(sentDateLabel, 0, 2);
        return gridPane;
    }

}
