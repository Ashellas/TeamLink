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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


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

    private UserSession user;

    GregorianCalendar cal; //Create calendar

    ArrayList<Notification> notifications = new ArrayList<>();

    String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

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
        System.out.println(user.getStandings(user.getUserTeams().get(0)).indexOf(user.getUserTeams().get(0)));
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

        for(int i = firstTeamPlacement - 1; i < firstTeamPlacement + 3; i++){

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

            standingsGrid.add(placementLabel, 0, i + 1);
            standingsGrid.add(teamNameLabel, 1, i + 1);
            standingsGrid.add(gamesPlayedLabel, 2, i + 1);
            standingsGrid.add(gamesWonLabel, 3, i + 1);
            standingsGrid.add(gamesLostLabel, 4, i + 1);
            standingsGrid.add(pointsLabel, 5, i + 1);

        }
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
        int notificationCount = 0;
        for(Notification notification : user.getNotifications()){
            GridPane customGrid = createCustomNotificationGridPane(notification.getTitle(), notification.getDescription());
            if(!notification.getClickAction().equals(   "")){
                Button button = new Button("View");
                button.setOnAction(event -> {
                    try {
                        AppManager.changeScene(getClass().getResource(notification.getClickAction()),event, user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                notificationsGrid.add(button, 2, notificationCount);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String formattedDate =  sdf.format(notification.getTimeSent());
            GridPane senderPane = createSenderInfoGrid(notification.getSender().getProfilePhoto(), notification.getSender().getFirstName(), formattedDate);
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
        System.out.println(notificationsEmptyHBox.getWidth());
        title.setPrefWidth(notificationsEmptyHBox.getWidth()*0.70);
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
        row1.setPercentHeight(60);
        row1.setMinHeight(0);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        row2.setMinHeight(0);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(20);
        row3.setMinHeight(0);
        gridPane.getRowConstraints().addAll(row1, row2, row3);
        Label senderNameLabel = new Label(senderName);
        Label sentDateLabel = new Label(sentDate);
        senderNameLabel.setPrefWidth(notificationsEmptyHBox.getWidth()*0.55);
        senderNameLabel.getStyleClass().add("little");
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
