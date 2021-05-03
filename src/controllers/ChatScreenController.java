package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
import models.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;


public class ChatScreenController extends MainTemplateController implements InitializeData {

    @FXML
    private Label teamNameLabel;

    @FXML
    private ComboBox teamBox;

    @FXML
    private Label header;

    @FXML
    private ImageView teamLogo;

    @FXML
    private TextField textField;

    @FXML
    private ImageView arrowIcon;

    @FXML
    private GridPane announcementsGrid;

    @FXML
    private HBox announcementsEmptyHBox;



    public void initData(UserSession user){
        super.initData(user);

        if(user.isStyleDark()) {
                darkIcons();
        }
        else {
                lightIcons();
        }

        Platform.runLater(() -> {
            setUpAnnouncementsGrid();
        });
    }

    public void setUpAnnouncementsGrid() {
        int announcementCount = 0;
        for (Announcement announcement : teamBox.getValue().getAnnouncements()) {
            GridPane customGrid = createCustomAnnouncementGridPane(announcement.getTitle(), announcement.getDescription());

            GridPane senderPane = createSenderInfoGrid(announcement);

            // TODO change order
            announcementsGrid.add(senderPane, 0, announcementCount);
            announcementsGrid.add(customGrid, 1, announcementCount);
        }
    }

    private GridPane createCustomAnnouncementGridPane(String notTitle, String notDescription){
        GridPane gridPane = new GridPane()  ;
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
        System.out.println(announcementsEmptyHBox.getWidth());
        title.setPrefWidth(announcementsEmptyHBox.getWidth()*0.70);
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

    private void lightIcons() {
        arrowIcon.setImage((new Image("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png")));
    }


    private void darkIcons() {
        arrowIcon.setImage((new Image("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png")));
    }


}
