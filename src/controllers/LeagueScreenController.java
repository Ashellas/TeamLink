package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.*;
import org.controlsfx.control.spreadsheet.Grid;


public class LeagueScreenController implements InitializeData {

    //Variables
    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label lastSyncLabel;

    @FXML
    private TableView<models.Team> standingsTableView;

    @FXML
    private TableView<models.Game> fixtureTable;

    @FXML
    private TableColumn<models.Team, String> teamsColumnStandings;

    @FXML
    private TableColumn<models.Team, Integer> winColumnStandings;

    @FXML
    private TableColumn<models.Team, Integer> drawColumnStandings;

    @FXML
    private TableColumn<models.Team, Integer> losesColumnStandings;

    @FXML
    private TableColumn<models.Team, Integer> pointsColumnStandings;

    @FXML
    private Label placementLabel;

    @FXML
    private Label winsLabel;

    @FXML
    private Label drawsLabel;

    @FXML
    private Label losesLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private Label matchesLeftLabel;

    @FXML
    private Label drawsNameLabel;

    @FXML
    private Label losesNameLabel;

    @FXML
    private Label pointsNameLabel;

    @FXML
    private Label matchesLeftNameLabel;

    @FXML
    private TableColumn<models.Game, String> homeColumn;

    @FXML
    private TableColumn<models.Game, String> awayColumn;

    @FXML
    private TableColumn<models.Game, String> scoreColumn;

    @FXML
    private TableColumn<models.Game, Button> detailsColumn;

    @FXML
    private ComboBox<String> teamSelectionComboBox;

    @FXML
    private Label leagueNameLabel;

    @FXML
    private Button rightButtonFixture;

    @FXML
    private Button leftButtonFixture;

    @FXML
    private Label homeTeamLabel;

    @FXML
    private Label awayTeamLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label gameLocationLabel;

    @FXML
    private Label gameLocationLinkLabel;

    @FXML
    private GridPane matchDetailsPane;

    @FXML
    private GridPane setStatisticsPane;

    @FXML
    private Pane blackenedPane;

    private UserSession user;

    private ObservableList<models.Team> teams = FXCollections.observableArrayList();

    private ObservableList<String> userTeamNames = FXCollections.observableArrayList();

    private ObservableList<models.Team> userSelectedTeam = FXCollections.observableArrayList();

    private ObservableList<models.Team> userTeams = FXCollections.observableArrayList();

    public void initData(UserSession user){
        this.user = user;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());

        setTeamSelectionComboBox(user);
    }

    /**
     * Sets the team selection combo box and the team overview table of the selected team
     * @param user UserSession class that holds the information about the user
     */
    public void setTeamSelectionComboBox( UserSession user)
    {
        for( int arrayIndex = 0; arrayIndex < user.getUserTeams().size(); arrayIndex++){
            userTeamNames.add( user.getUserTeams().get(arrayIndex).getTeamName());
            userTeams.add( user.getUserTeams().get(arrayIndex));
        }

        teamSelectionComboBox.setItems( userTeamNames);

        teamSelectionComboBox.setValue( userTeamNames.get(0));

        leagueNameLabel.setText( userTeams.get(0).getLeagueName());

        userSelectedTeam.add( userTeams.get(0));

        setStandingsTable(user);

        setTeamOverviewTable( user);

        setFixtureTable( user);
    }

    /**
     * Sets the standing table according to the league's latest situation
     * @param user UserSession class that holds the information about the user
     */
    public void setStandingsTable( UserSession user)
    {
        if( user.getUser().getSportBranch().equals("Football") )
        {
            //Creates the columns of the table
            teamsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("teamName"));
            winColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            drawColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesDrawn"));
            losesColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("points"));

            teams = user.getStandings( userSelectedTeam.get(0));

            standingsTableView.setItems( teams);
        }
        else if( user.getUser().getSportBranch().equals("Basketball") )
        {
            //Sets the draw column non-visible
            drawColumnStandings.setVisible(false);

            standingsTableView.setMaxWidth( standingsTableView.getMaxWidth() * 0.83);

            //Creates the columns of the table
            teamsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("teamName"));
            winColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            losesColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("points"));

            teams = user.getStandings( userSelectedTeam.get(0));
            standingsTableView.setItems( teams);
        }
    }

    /**
     * Sets the team overview table of the selected team
     * @param user
     */
    public void setTeamOverviewTable( UserSession user){

        if( user.getUser().getSportBranch().equals("Football") ){

            //Set the labels according to the information received from selected team
            placementLabel.setText( String.valueOf( userSelectedTeam.get(0).getPlacement()));
            winsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesWon()));
            drawsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesDrawn()));
            losesLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesLost()));
            pointsLabel.setText( String.valueOf( userSelectedTeam.get(0).getPoints()));
            matchesLeftLabel.setText( String.valueOf( userSelectedTeam.get(0).getMatchesLeft()));
        }
        else if( user.getUser().getSportBranch().equals("Basketball")){

            /*
            Set the places of the name labels to omit the draw column
             */
            drawsNameLabel.setText( losesNameLabel.getText());
            losesNameLabel.setText( pointsNameLabel.getText());
            pointsNameLabel.setText( matchesLeftNameLabel.getText());
            matchesLeftNameLabel.setText("");

            /* Set the labels according to the information received from selected team
               by avoiding draw column
             */
            placementLabel.setText( String.valueOf( userSelectedTeam.get(0).getPlacement()));
            winsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesWon()));
            drawsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesLost()));
            losesLabel.setText( String.valueOf( userSelectedTeam.get(0).getPoints()));
            pointsLabel.setText( String.valueOf( userSelectedTeam.get(0).getMatchesLeft()));
            matchesLeftLabel.setText("");
        }
    }

    public void setFixtureTable( UserSession user){
        homeColumn.setCellValueFactory( new PropertyValueFactory<>("homeTeamName"));
        scoreColumn.setCellValueFactory( new PropertyValueFactory<>("result"));
        awayColumn.setCellValueFactory( new PropertyValueFactory<>( "awayTeamName"));

        fixtureTable.setItems( user.getGamesOfTheCurrentRound( userSelectedTeam.get(0)));
        detailsColumn.setCellFactory( ButtonTableCell.<Game>forTableColumn("View", (Game gameClicked) -> {
            onFixtureDetailsClicked( gameClicked, user);
            return gameClicked;
        }));

    }

    public void updateTeamOverviewTable(UserSession user){

        if( user.getUser().getSportBranch().equals("Football") ){

            //Set the labels according to the information received from selected team
            placementLabel.setText( String.valueOf( userSelectedTeam.get(0).getPlacement()));
            winsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesWon()));
            drawsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesDrawn()));
            losesLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesLost()));
            pointsLabel.setText( String.valueOf( userSelectedTeam.get(0).getPoints()));
            matchesLeftLabel.setText( String.valueOf( userSelectedTeam.get(0).getMatchesLeft()));
        }
        else if( user.getUser().getSportBranch().equals("Basketball")){

            /*
            Set the places of the name labels to omit the draw column
             */
            drawsNameLabel.setText( losesNameLabel.getText());
            losesNameLabel.setText( pointsNameLabel.getText());
            pointsNameLabel.setText( matchesLeftNameLabel.getText());
            matchesLeftNameLabel.setText("");

            /* Set the labels according to the information received from selected team
               by avoiding draw column
             */
            placementLabel.setText( String.valueOf( userSelectedTeam.get(0).getPlacement()));
            winsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesWon()));
            drawsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesLost()));
            losesLabel.setText( String.valueOf( userSelectedTeam.get(0).getPoints()));
            pointsLabel.setText( String.valueOf( userSelectedTeam.get(0).getMatchesLeft()));
            matchesLeftLabel.setText("");
        }
    }

    /**
     * When a new team selected from the team selection combo box, team overview table is refreshed
     * according to the new team that is selected.
     * @param event ActionEvent object
     */
    public void onSelection( ActionEvent event){
        userSelectedTeam.set(0, userTeams.get( teamSelectionComboBox.getSelectionModel().getSelectedIndex()));
        updateTeamOverviewTable( user);
        standingsTableView.refresh();
        fixtureTable.refresh();
    }

    public void rightButtonFixtureClicked( ActionEvent event){

    }

    public void leftButtonFixtureClicked( ActionEvent event){

    }

    public void onCloseDetailsButtonClicked( ActionEvent event){
        matchDetailsPane.setVisible(false);
        setStatisticsPane.setVisible(false);
        blackenedPane.setVisible(false);
    }

    public void onFixtureDetailsClicked( Game gameClicked, UserSession user){
        blackenedPane.setVisible(true);
        matchDetailsPane.setVisible(true);
        homeTeamLabel.setText( gameClicked.getHomeTeamName());
        awayTeamLabel.setText( gameClicked.getAwayTeamName());
        scoreLabel.setText( gameClicked.getResult());
        dateLabel.setText( gameClicked.getEventDateTime().toString());
        gameLocationLabel.setText( gameClicked.getGameLocationName());
        gameLocationLinkLabel.setText(gameClicked.getGameLocationLink());

        if( user.getUser().getTeamRole().equals("Head Coach") ){
            setStatisticsPane.setVisible(true);
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

    public void toLeagueScreen(ActionEvent actionEvent) {
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
}
