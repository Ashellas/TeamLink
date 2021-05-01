package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import models.AppManager;
import models.InitializeData;
import models.Notification;
import models.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class MainScreenController implements InitializeData {

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label lastSyncLabel;

    @FXML
    private HBox emptyHBox;

    @FXML
    private GridPane notificationsGrid;

    @FXML
    private GridPane standingsGrid;

    @FXML
    private GridPane calendarGrid;

    private UserSession user;

    ArrayList<Notification> notifications = new ArrayList<>();

    public void initData(UserSession user){
        this.user = user;
        //userNameLabel.setText(user.getUser().getFirstName());
        //userRoleLabel.setText(user.getUser().getTeamRole());
        //profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());
        //lastSyncLabel.setText(user.getLastSync().toString()); //TODO get timeDiff in background maybe

        for(int i = 0; i < 4; i ++){
            notifications.add(new Notification(i, "Title" + i, "Description" + i, null, null, "/views/SquadScreen.fxml",new Date(),false,null));
        }
        notifications.add(new Notification(0, "Title", "Description", null, null, "/views/LoginScreen.fxml",new Date(),false,null));

        //sets up the gridpane after scene establishes
        Platform.runLater(() -> {
            setUpNotificationsGrid();
        });
    }

    public void toMainScreen(ActionEvent actionEvent) {
    }

    public void toSquadScreen(ActionEvent actionEvent) {
    }

    public void toCalendarScreen(ActionEvent actionEvent) {
    }

    public void toGameplanScreen(ActionEvent actionEvent) {
    }

    public void toTrainingsScreen(ActionEvent actionEvent) {
    }

    public void toLeagueScreen(ActionEvent actionEvent) throws IOException {
        AppManager.changeScene(getClass().getResource("LeagueScreen.fxml"),actionEvent, user);
    }

    public void toChatScreen(ActionEvent actionEvent) {
    }

    public void toSettingsScreen(ActionEvent actionEvent) {
    }

    public void logoutButtonPushed(ActionEvent actionEvent) {
    }

    public void helpButtonPushed(ActionEvent actionEvent) {
    }

    public void SynchronizeData(ActionEvent actionEvent) {
    }


    public void setUpNotificationsGrid(){
        for(int i = 0; i < 5; i++){
            GridPane customGrid = createCustomNotificationGridPane(notifications.get(i).getTitle(), notifications.get(i).getDescription());
            Button button = new Button("View");
            InputStream inStream = getClass().getResourceAsStream("/Resources/Images/white/profile_white.png");
            ImageView imageView = new ImageView(new Image(inStream));
            final int j = i;
            button.setOnAction(event -> {
                try {
                    AppManager.changeScene(getClass().getResource(notifications.get(j).getClickAction()),event, user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String formattedDate =  sdf.format(notifications.get(i).getTimeSent());
            GridPane senderPane = createSenderInfoGrid(imageView, "Göktürk", formattedDate);
            notificationsGrid.add(senderPane, 0,i);
            notificationsGrid.add(customGrid, 1,i);
            notificationsGrid.add(button, 2, i);
        }

    }

    private GridPane createCustomNotificationGridPane(String notTitle, String notDescription){
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
        System.out.println(emptyHBox.getWidth());
        title.setPrefWidth(emptyHBox.getWidth()*0.70);
        title.getStyleClass().add("title");
        description.getStyleClass().add("description");
        gridPane.add(title, 0, 1);
        gridPane.add(description, 0, 2);
        return gridPane;
    }

    private GridPane createSenderInfoGrid(ImageView senderPhoto, String senderName, String sentDate){
        senderPhoto.setFitHeight(40);
        senderPhoto.setFitWidth(40);
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        BorderPane imageContainer = new BorderPane(senderPhoto);
        row1.setPercentHeight(50);
        row1.setMinHeight(0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(25);
        row2.setMinHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(25);
        row3.setMinHeight(0);
        gridPane.getRowConstraints().addAll(row1, row2, row3);
        Label senderNameLabel = new Label(senderName);
        Label sentDateLabel = new Label(sentDate);
        senderNameLabel.setPrefWidth(emptyHBox.getWidth()*0.55);
        senderNameLabel.getStyleClass().add("little");
        sentDateLabel.getStyleClass().add("little");
        gridPane.add(imageContainer, 0,0);
        gridPane.add(senderNameLabel, 0, 1);
        gridPane.add(sentDateLabel, 0, 2);
        return gridPane;
    }
}
