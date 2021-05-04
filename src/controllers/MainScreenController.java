package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
    private TableColumn<TeamApplication, Button> acceptColumn;

    @FXML
    private TableColumn<TeamApplication, Button> declineColumn;


    @FXML
    private TableView<TeamApplication> myApplicationTable;

    @FXML
    private TableColumn<TeamApplication, String> teamNameColumn;

    @FXML
    private TableColumn<TeamApplication, String> ageGroupColumn;

    @FXML
    private TableColumn<TeamApplication, String> cityColumn;

    @FXML
    private TableColumn<TeamApplication, String> statusColumn;

    @FXML
    private GridPane applicantsPane;

    @FXML
    private GridPane mainScreenPane;

    @FXML
    private Pane disablePane;

    @FXML
    private ComboBox<Team> teamBox;

    //---------------------Help Pane---------------------------//
    @FXML
    private GridPane helpPane;


    @FXML
    private ComboBox applicationtypeBox;

    @FXML
    private ImageView helpPaneIcon;

    GregorianCalendar cal; //Create calendar

    private ArrayList<Label> standingLabels;

    private ArrayList<ListView> calendarItems;

    private ArrayList<Node> noticationNodes;

    private ObservableList<String>comboBoxTexts  = FXCollections.observableArrayList("My Applications", "Team Applications");

    @FXML
    private Button rightButton;

    @FXML
    private Button leftButton;

    private int notificationPageIndex;

    ArrayList<Notification> notifications = new ArrayList<>();

    String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

    private Team selectedTeam;
    private Stage loading;
    private Executor exec;

    public void initData(UserSession user){
        super.initData(user);

        // Theme selection
        if(user.isStyleDark()) {
            darkIcons();
        }
        else {
            lightIcons();
        }

        notificationPageIndex = 0;
        standingLabels = new ArrayList<>();
        calendarItems = new ArrayList<>();
        noticationNodes = new ArrayList<>();

        try {
            if(DatabaseManager.createNotifications(user.getDatabaseConnection(), user.getUser(), notificationPageIndex + 1).size() == 0){
                rightButton.setVisible(false);
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        leftButton.setVisible(false);

        //sets up the gridpane after scene establishes
        Platform.runLater(() -> {
            try {
                setUpNotificationsGrid();
            } catch (SQLException | ParseException throwables) {
                throwables.printStackTrace();
            }
            setUpStandingsTable();
            try {
                setUpCalendarGrid();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for (Team t: user.getUserTeams()) {
            teamBox.getItems().add(t);
        }
        selectedTeam = user.getUserTeams().get(0);
        teamBox.getSelectionModel().selectFirst();

        disablePane.setVisible(false);
        disablePane.setDisable(true);
        helpPane.setVisible(false);
        helpPane.setDisable(true);
        try {
            createLoading();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });

        applicantsPane.setVisible(false);
        setUpApplicantsTable();
        for(Team team : user.getStandings(user.getUserTeams().get(0))){
            System.out.println(team.getTeamName());
        }
        if(user.getUser().getTeamRole().equals("Head Coach")){
            applicationtypeBox.getItems().addAll(comboBoxTexts);
            applicationtypeBox.getSelectionModel().selectFirst();
        }
        else{
            applicationtypeBox.setVisible(false);
        }

        AppManager.fadeIn(mainScreenPane,500);
    }

    private void setUpCalendarGrid() throws IOException {
        cal = new GregorianCalendar();
        int dayOfWeekIndex = (Calendar.DAY_OF_WEEK - 2);
        for( int i = 0; i < 4; i++){
            Label dayNameLabel = new Label(daysOfTheWeek[(dayOfWeekIndex + i) % 7]);
            dayNameLabel.getStyleClass().add("standings");
            dayNameLabel.setPrefWidth(standingsEmptyHBox.getWidth());
            calendarGrid.add(dayNameLabel, i, 0);
            ListView eventsListView = new ListView();

            eventsListView.setOrientation(Orientation.VERTICAL);
            eventsListView.setFocusTraversable(false);
            ArrayList<CalendarEvent> events = user.getCalendarEvents(selectedTeam);
            for (CalendarEvent ce : events) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ce.getEventDateTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String formattedDate = sdf.format(ce.getEventDateTime());
                if (i + 1 == calendar.get(Calendar.DAY_OF_MONTH)) {
                    if (ce.getActionLink() != null) {
                        try {
                            eventsListView.getItems().add(new CalendarButton(ce.getEventTitle(), formattedDate, ce.getActionLink(), ce.getColorCode(), user));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        eventsListView.getItems().add(new CalendarButton(ce.getEventTitle(), ce.getColorCode(), user));
                    }
                }
            }
            calendarGrid.add(eventsListView, i, 1);
            calendarItems.add(eventsListView);
        }
    }

    /**
     * Clears the calendar and creates the same month with chosen team's events.
     * @param actionEvent changing the team selection combo box's value
     */
    public void teamSelection(ActionEvent actionEvent) throws SQLException, IOException {
        selectedTeam = (Team) teamBox.getValue();
        clearCalendar();
        clearStandings();
        setUpCalendarGrid();
        setUpStandingsTable();
    }

    private void clearCalendar() {
        for(ListView node : calendarItems){
            calendarGrid.getChildren().remove(node);
        }
        calendarItems = new ArrayList<>();
    }


    private void setUpStandingsTable() {
        ObservableList<Team> selectedTeamStandings = user.getStandings(selectedTeam);
        int userTeamplacement = selectedTeamStandings.indexOf(selectedTeam) + 1;
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
        for(int i = 0; i < 5; i++){

            Label placementLabel = new Label("" + (firstTeamPlacement + i) + ".");
            placementLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            placementLabel.getStyleClass().add("standings");
            Label teamNameLabel = new Label(selectedTeamStandings.get(i + firstTeamPlacement - 1).getTeamName());
            teamNameLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.4);
            teamNameLabel.getStyleClass().add("standings");
            Label gamesPlayedLabel = new Label("" + selectedTeamStandings.get(i + firstTeamPlacement - 1).getTeamStats().getGamesPlayed());
            gamesPlayedLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            gamesPlayedLabel.getStyleClass().add("standings");
            Label gamesWonLabel = new Label("" + selectedTeamStandings.get(i + firstTeamPlacement - 1).getGamesWon());
            gamesWonLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            gamesWonLabel.getStyleClass().add("standings");
            Label gamesLostLabel = new Label("" + selectedTeamStandings.get(i + firstTeamPlacement - 1).getGamesLost());
            gamesLostLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            gamesLostLabel.getStyleClass().add("standings");
            Label pointsLabel = new Label("" + selectedTeamStandings.get(i + firstTeamPlacement - 1).getPoints());
            pointsLabel.setPrefWidth(standingsEmptyHBox.getWidth() * 0.12);
            pointsLabel.getStyleClass().add("standings");

            standingsGrid.add(placementLabel, 0, i + 1);
            standingsGrid.add(teamNameLabel, 1, i + 1);
            standingsGrid.add(gamesPlayedLabel, 2, i + 1);
            standingsGrid.add(gamesWonLabel, 3, i + 1);
            standingsGrid.add(gamesLostLabel, 4, i + 1);
            standingsGrid.add(pointsLabel, 5, i + 1);

            standingLabels.add(placementLabel );
            standingLabels.add(teamNameLabel );
            standingLabels.add(gamesPlayedLabel );
            standingLabels.add(gamesWonLabel);
            standingLabels.add(gamesLostLabel );
            standingLabels.add(pointsLabel);

        }
    }

    public void toMainScreen(ActionEvent actionEvent) {}

    private void clearStandings(){
        for(Label label : standingLabels){
            standingsGrid.getChildren().remove(label);
        }
        standingLabels = new ArrayList<>();
    }

    public void setUpNotificationsGrid() throws SQLException, ParseException {
        int notificationCount = 0;
        if(notificationPageIndex == 0) {
            for (Notification notification : user.getNotifications()) {
                GridPane customGrid = createCustomNotificationGridPane(notification.getTitle(), notification.getDescription());
                if (!notification.getClickAction().equals("")) {
                    Button button = new Button("View");
                    button.getStylesheets().add("/stylesheets/ButtonStyleSheet.css");
                    button.getStyleClass().add("viewButton");
                    button.setOnAction(event -> {

                        try {
                            AppManager.changeScene(getClass().getResource(notification.getClickAction()), event, user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    notificationsGrid.add(button, 2, notificationCount);
                }

                GridPane senderPane = createSenderInfoGrid(notification);
                notificationsGrid.add(senderPane, 0, notificationCount);
                notificationsGrid.add(customGrid, 1, notificationCount);
                noticationNodes.add(customGrid);
                noticationNodes.add(senderPane);
                notificationCount++;
            }
        }else{
            for(Notification notification : DatabaseManager.createNotifications(user.getDatabaseConnection(), user.getUser(), notificationPageIndex)){
                GridPane customGrid = createCustomNotificationGridPane(notification.getTitle(), notification.getDescription());
                if(!notification.getClickAction().equals("")){
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
                    noticationNodes.add(button);
                }

                GridPane senderPane = createSenderInfoGrid(notification);
                notificationsGrid.add(senderPane, 0, notificationCount);
                notificationsGrid.add(customGrid, 1, notificationCount);
                noticationNodes.add(customGrid);
                noticationNodes.add(senderPane);

                notificationCount++;
                }
            }
        }


    private void clearNotificationGrid(){
        for(Node node : noticationNodes){
            notificationsGrid.getChildren().remove(node);
        }
        noticationNodes = new ArrayList<>();
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
        title.setPrefWidth(notificationsEmptyHBox.getWidth() * 0.70);
        description.setPrefWidth(notificationsEmptyHBox.getWidth()*0.70);
        title.getStyleClass().add("title");
        description.getStyleClass().add("description");
        gridPane.add(title, 0, 1);
        gridPane.add(description, 0, 2);
        return gridPane;
    }

    private GridPane createSenderInfoGrid(Notification notification){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
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
        ObservableList<TeamApplication> appliedTeamsList = user.getmyTeamsApplications();
        appliedTeamColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("teamName"));
        applicantColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("applicantFullName"));
        teamRoleColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("applicantTeamRole"));
        applicantsTable.setItems(appliedTeamsList);
        acceptColumn.setCellFactory(ButtonTableCell.<TeamApplication>forTableColumn("Accept", (TeamApplication p) -> {
            try {
                DatabaseManager.acceptTeamApplication(user, p);
                for (Team team: user.getUserTeams()) {
                    if (team == p.getAppliedTeam()) {
                        team.getTeamMembers().add(p.getApplicant());
                    }
                }
                user.getTeamApplications().remove(p);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            setUpApplicantsTable();
            return p;
        }));
        declineColumn.setCellFactory(ButtonTableCell.<TeamApplication>forTableColumn("Decline", (TeamApplication p) -> {
            try {
                DatabaseManager.declineTeamApplication(user, p);
                user.getTeamApplications().remove(p);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            setUpApplicantsTable();
            return p;
        }));



        ObservableList<TeamApplication> applicationList = user.getmyApplication();

        teamNameColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("teamName"));
        ageGroupColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("ageGroup"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("city"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<TeamApplication, String>("applicationStatus"));
        myApplicationTable.setItems(applicationList);
    }

    @Override
    /**
     * Shows help information of the screen
     */
    public void helpButtonPushed(ActionEvent actionEvent){
        disablePane.setVisible(true);
        disablePane.setDisable(false);
        helpPane.setDisable(false);
        helpPane.setVisible(true);
    }

    /**
     * Closes the help pane
     * @param actionEvent close button pushed
     */
    public void helpPaneClose(ActionEvent actionEvent) {
        disablePane.setDisable(true);
        disablePane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);
    }

    /**
     * Helps initialising the icons according to the chosen team
     */
    public void darkIcons() {
        helpPaneIcon.setImage((new Image("/Resources/Images/white/help_white.png")));
    }

    /**
     * Helps initialising the icons according to the chosen team
     */
    public void lightIcons() {
        helpPaneIcon.setImage((new Image("/Resources/Images/black/help_black.png")));
    }

    public void leftButtonClicked(ActionEvent event) throws SQLException {
        clearNotificationGrid();
        notificationPageIndex--;
        if(notificationPageIndex == 0){
            leftButton.setVisible(false);
        }
        rightButton.setVisible(true);
        try {
            setUpNotificationsGrid();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void rightButtonClicked(ActionEvent event) throws SQLException, ParseException {
        clearNotificationGrid();
        notificationPageIndex++;
        if(DatabaseManager.createNotifications(user.getDatabaseConnection(), user.getUser(), notificationPageIndex + 1).size() == 0){
            rightButton.setVisible(false);
        }
        leftButton.setVisible(true);
        setUpNotificationsGrid();
    }

    public void changeApplicationType(ActionEvent event) {
        if(applicationtypeBox.getValue().toString().equals( "My Applications")){
            applicantsTable.setVisible(false);
            myApplicationTable.setVisible(true);
        }
        else{
            applicantsTable.setVisible(true);
            myApplicationTable.setVisible(false);
        }
        setUpApplicantsTable();
    }

    public void calendarView(ActionEvent event) throws IOException {
        toCalendarScreen(event);
    }

    public void standingsView(ActionEvent event) throws IOException {
        toLeagueScreen(event);
    }

    @Override
    public void SynchronizeData(ActionEvent event) {
        disablePane.setVisible(true);
        loading.show();
        Task<UserSession> userCreateTask =  new Task<UserSession>() {
            @Override
            public UserSession call() throws Exception {
                System.out.println(" Succeed at : " + new java.util.Date());
                return DatabaseManager.sync(user);
            }
        };
        userCreateTask.setOnFailed(e -> {
            userCreateTask.getException().printStackTrace();
            // inform user of error...
        });

        userCreateTask.setOnSucceeded(e -> {
            displayMessage(messagePane, "Session is synchronized", false);
            loading.close();
            disablePane.setVisible(false);

            System.out.println("gg"); });

        // Task.getValue() gives the value returned from call()...
        // run the task using a thread from the thread pool:
        exec.execute(userCreateTask);
    }

    private void createLoading() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/LoadingScreen.fxml"));
        loading = new Stage();
        loading.initStyle(StageStyle.UNDECORATED);
        loading.initModality(Modality.APPLICATION_MODAL);
        loading.setScene(new Scene(root));
        disablePane.setOpacity(0.5);
    }
}
