package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import models.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainScreenController extends MainTemplateController{

    @FXML
    private HBox notificationsEmptyHBox;

    @FXML
    private HBox standingsEmptyHBox;

    @FXML
    private GridPane notificationsGrid;

    @FXML
    private GridPane standingsGrid;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private TableView<TeamApplication> applicantsTable;

    @FXML
    private TableColumn<TeamApplication, String> appliedTeamColumn;

    @FXML
    private TableColumn<TeamApplication, String> applicantColumn;

    @FXML
    private TableColumn<TeamApplication, String> teamRoleColumn;

    @FXML
    private TableColumn<TeamApplication, String> actionColumn;

    @FXML
    private GridPane applicantsPane;

    @FXML
    private Pane disablePane;

    GregorianCalendar cal; //Create calendar

    ArrayList<Notification> notifications = new ArrayList<>();

    String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

    public void initData(UserSession user){
        super.initData(user);

        //userNameLabel.setText(user.getUser().getFirstName());
        //userRoleLabel.setText(user.getUser().getTeamRole());
        //profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());
        //lastSyncLabel.setText(user.getLastSync().toString()); //TODO get timeDiff in background maybe

        /*
        for(int i = 0; i < 4; i ++){
            notifications.add(new Notification(i, "Title" + i, "Description" + i, null, null, "/views/SettingsScreen.fxml",new Date(),false,null));
        }
        notifications.add(new Notification(0, "Title", "Description", null, null, "/views/LoginScreen.fxml",new Date(),false,null));
*/
        //sets up the gridpane after scene establishes
        Platform.runLater(() -> {
            setUpNotificationsGrid();
            setUpStandingsTable();
            try {
                setUpCalendarGrid();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        disablePane.setVisible(false);
        applicantsPane.setVisible(false);
        setUpApplicantsTable();
        for(Team team : user.getStandings(user.getUserTeams().get(0))){
            System.out.println(team.getTeamName());
        }
    }

    private void setUpCalendarGrid() throws IOException {
        cal = new GregorianCalendar();
        int dayOfWeekIndex = (Calendar.DAY_OF_WEEK - 2) % 7;
        for( int i = 0; i < 4; i++){
            Label dayNameLabel = new Label(daysOfTheWeek[(dayOfWeekIndex + i) % 7]);
            dayNameLabel.getStyleClass().add("standings");
            dayNameLabel.setPrefWidth(standingsEmptyHBox.getWidth());
            calendarGrid.add(dayNameLabel, i, 0);
            ListView eventsListView = new ListView();
            
            eventsListView.setOrientation(Orientation.VERTICAL);
            eventsListView.setFocusTraversable(false);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            CalendarButton eventButton = new CalendarButton("training", sdf.format(new Date()), "/views/SquadScreen.fxml","red", user);
            eventsListView.getItems().add(eventButton);
            calendarGrid.add(eventsListView, i, 1);
        }
    }

    private void setUpStandingsTable() {
        ObservableList<Team> selectedTeamStandings = user.getStandings(user.getUserTeams().get(0));
        int userTeamplacement = selectedTeamStandings.indexOf(user.getUserTeams().get(0)) + 1;
        int teamCount = selectedTeamStandings.size();
        int firstTeamPlacement;
        if(userTeamplacement == 1 ||userTeamplacement == 2){
            firstTeamPlacement = 1;
        }
        else if(userTeamplacement == teamCount || userTeamplacement == teamCount - 1) {
            if(teamCount - 4 > 0){
                firstTeamPlacement = teamCount - 4;
            }
            else{
                firstTeamPlacement = 1;
            }
        }
        else{
            firstTeamPlacement = userTeamplacement - 2;
        }
//int i = firstTeamPlacement - 1; i < firstTeamPlacement + 3; i++
        for(int i = 2; i < 7; i++){

            Label placementLabel = new Label("" + (i + 1) + ".");
            placementLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            placementLabel.getStyleClass().add("standings");
            Label teamNameLabel = new Label(selectedTeamStandings.get(i).getTeamName());
            teamNameLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.4);
            teamNameLabel.getStyleClass().add("standings");
            Label gamesPlayedLabel = new Label("" + selectedTeamStandings.get(i).getTeamStats().getGamesPlayed());
            gamesPlayedLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            gamesPlayedLabel.getStyleClass().add("standings");
            Label gamesWonLabel = new Label("" + selectedTeamStandings.get(i).getGamesWon());
            gamesWonLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            gamesWonLabel.getStyleClass().add("standings");
            Label gamesLostLabel = new Label("" + selectedTeamStandings.get(i).getGamesLost());
            gamesLostLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            gamesLostLabel.getStyleClass().add("standings");
            Label pointsLabel = new Label("" + selectedTeamStandings.get(i).getPoints());
            pointsLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            pointsLabel.getStyleClass().add("standings");

            standingsGrid.add(placementLabel, 0, i -1);
            standingsGrid.add(teamNameLabel, 1, i - 1);
            standingsGrid.add(gamesPlayedLabel, 2, i - 1);
            standingsGrid.add(gamesWonLabel, 3, i - 1);
            standingsGrid.add(gamesLostLabel, 4, i - 1);
            standingsGrid.add(pointsLabel, 5, i - 1);

        }
    }

    public void toMainScreen(ActionEvent actionEvent) {
    }

    public void setUpNotificationsGrid()     {
        int notificationCount = 0;
        for(Notification notification : user.getNotifications()){
            GridPane customGrid = createCustomNotificationGridPane(notification.getTitle(), notification.getDescription());
            if(notification.getClickAction().equals(   "")){
                Button button = new Button("View");
                button.getStylesheets().add("/stylesheets/ButtonStyleSheet.css");
                button.getStyleClass().add("viewButton");
                button.setOnAction(event -> {
                    try {
                        AppManager.changeScene(getClass().getResource(notification.getClickAction()),event, user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                notificationsGrid.add(button, 2, notificationCount);
            }

            GridPane senderPane = createSenderInfoGrid(notification);
            notificationsGrid.add(senderPane, 0, notificationCount);
            notificationsGrid.add(customGrid, 1, notificationCount);
        }
        /*
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


         */
    }

    private GridPane createCustomNotificationGridPane(String notTitle, String notDescription){
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
        System.out.println(notificationsEmptyHBox.getWidth());
        title.setPrefWidth(notificationsEmptyHBox.getWidth()*0.70);
        title.getStyleClass().add("title");
        description.getStyleClass().add("description");
        gridPane.add(title, 0, 1);
        gridPane.add(description, 0, 2);
        return gridPane;
    }

    private GridPane createSenderInfoGrid(Notification notification){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDate =  sdf.format(notification.getTimeSent());
        ImageView senderPhoto = new ImageView();
        if(notification.getSender().getMemberId() == 1){
            if(user.isStyleDark()) {
                try {
                    senderPhoto.setImage(new Image(getClass().getResource("/Resources/Images/app_logo.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    senderPhoto.setImage(new Image(getClass().getResource("/Resources/Images/appLogo_Light.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(notification.getSender().getProfilePhoto() == null){
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
            senderPhoto.setImage(notification.getSender().getProfilePhoto().getImage());
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
        Label senderNameLabel = new Label(notification.getSender().getFirstName());
        Label sentDateLabel = new Label(formattedDate);
        senderNameLabel.setPrefWidth(notificationsEmptyHBox.getWidth()*0.55);
        senderNameLabel.getStyleClass().add("little");
        sentDateLabel.setPrefWidth(notificationsEmptyHBox.getWidth()*0.55);
        sentDateLabel.getStyleClass().add("little");
        gridPane.add(imageContainer, 0,0);
        gridPane.add(senderNameLabel, 0, 1);
        gridPane.add(sentDateLabel, 0, 2);
        return gridPane;
    }

    public void closeButtonPushed(ActionEvent event) {
        disablePane.setVisible(false);
        applicantsPane.setVisible(false);
    }

    public void viewTeamApplicationsButtonPushed(ActionEvent event) {
        disablePane.setVisible(true);
        applicantsPane.setVisible(true);
    }

    private void setUpApplicantsTable(){
        ObservableList<TeamApplication> appliedTeamsList = user.getTeamApplications();
        appliedTeamColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("teamName"));
        applicantColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("applicantFullName"));
        teamRoleColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("applicantTeamRole"));
        applicantsTable.setItems(appliedTeamsList);
    }

}
