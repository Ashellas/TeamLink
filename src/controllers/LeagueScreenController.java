package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import models.Team;
import models.UserSession;


public class LeagueScreenController {
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
    private TableView<models.Team> fixtureTable;

    @FXML
    private TableView<models.Team> teamOverviewTable;

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
    private TableColumn<models.Team, Integer> placementColumn;

    @FXML
    private TableColumn<models.Team, Integer> winsColumn;

    @FXML
    private TableColumn<models.Team, Integer> drawsColumn;

    @FXML
    private TableColumn<models.Team, Integer> losesColumn;

    @FXML
    private TableColumn<models.Team, Integer> pointsColumn;

    @FXML
    private TableColumn<models.Team, Integer> matchesLeftColumn;

    private UserSession user;

    private ObservableList<models.Team> teams = FXCollections.observableArrayList();

    private ObservableList<models.Team> userTeam = FXCollections.observableArrayList();

    public void initData(UserSession user){
        this.user = user;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());

        setStandingsTable(user);

        setTeamOverviewTable(user);
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

            teams = user.getStandings(null);

            standingsTableView.setItems( teams);
        }
        else if( user.getUser().getSportBranch().equals("Basketball") )
        {
            //Sets the draw column non-visible
            drawColumnStandings.setVisible(false);

            //Creates the columns of the table
            teamsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("teamName"));
            winColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            losesColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("points"));

            teams = user.getStandings(null);
            standingsTableView.setItems( teams);
        }
    }

    public void setTeamOverviewTable( UserSession user){
        //userTeam = Assign the team

        /*

        if( is a football team){
            //Creates the columns of the table
            placementColumn.setCellValueFactory( new PropertyValueFactory<>("placement"));
            winsColumn.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            drawsColumn.setCellValueFactory( new PropertyValueFactory<>("gamesDrawn"));
            losesColumn.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumn.setCellValueFactory( new PropertyValueFactory<>("points"));
            matchesLeftColumn.setCellValueFactory( new PropertyValueFactory<>("matchesLeft"));

            teamOverviewTable.setItems( userTeam);
        }
        else if( is a basketball team){
            //Sets the draw column non-visible
            drawsColumn.setVisible(false);

            //Creates the columns of the table
            placementColumn.setCellValueFactory( new PropertyValueFactory<>("placement"));
            winsColumn.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            losesColumn.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumn.setCellValueFactory( new PropertyValueFactory<>("points"));
            matchesLeftColumn.setCellValueFactory( new PropertyValueFactory<>("matchesLeft"));

            teamOverviewTable.setItems( userTeam);
        }

         */

    }

    public void setFixtureTable( UserSession user){


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
