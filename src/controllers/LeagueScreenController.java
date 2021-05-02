package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.*;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;


public class LeagueScreenController extends MainTemplateController {

    //Variables
    private final String DARK_STYLE_SHEET = getClass().getResource("/stylesheets/DarkTheme.css").toExternalForm();
    private final String LIGHT_STYLE_SHEET = getClass().getResource("/stylesheets/LightTheme.css").toExternalForm();

    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    @FXML
    private TableView<models.Team> standingsTableView;

    @FXML
    private TableView<models.Game> fixtureTable;

    @FXML
    private TableView<models.TeamMember> playerStatisticsTable;

    @FXML
    private TableView<models.TeamMember> addPlayerTable;

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
    private TableColumn<models.Team, Integer> matchesPlayedColumnStandings;

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
    private ComboBox<String> addPlayersComboBox;

    @FXML
    private Label leagueNameLabel;

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
    private TableColumn<TeamMember, String> playerStatisticsNameColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsPointsColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsAssistsColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsReboundsColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsStealsColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsBlocksColumn;

    @FXML
    private TableColumn<TeamMember, Button> playerStatisticsEnterStatisticsColumn;

    @FXML
    private Pane blackenedPane;

    @FXML
    private GridPane addPlayersGridPane;

    @FXML
    private TableColumn<TeamMember, String> addPlayerNameColumn;

    @FXML
    private TableColumn<TeamMember, String> addPlayerRoleColumn;

    @FXML
    private TableColumn<TeamMember, Button> addPlayerAddColumn;

    private Team teamOfCoach;

    private ObservableList<models.Team> teams = FXCollections.observableArrayList();

    private ObservableList<models.Team> userTeams = FXCollections.observableArrayList();

    private ObservableList<Team> userTeamsAddPlayersComboBox = FXCollections.observableArrayList();

    private ObservableList<String> userTeamNames = FXCollections.observableArrayList();

    private ObservableList<String> userTeamNamesAddPlayersComboBox = FXCollections.observableArrayList();

    private ObservableList<models.Team> userSelectedTeam = FXCollections.observableArrayList();

    private ObservableList<Team> userSelectedTeamAtPlayersAddComboBox = FXCollections.observableArrayList();

    private ObservableList<TeamMember> userSelectedTeamMembers = FXCollections.observableArrayList();

    private ObservableList<TeamMember> userSelectedTeamMembersAtPlayersAddComboBox = FXCollections.observableArrayList();


    public void initData(UserSession user){
        super.initData(user);

        setTeamSelectionComboBox();
        setPlayerStatisticsTable( );
    }

    /**
     * Sets the team selection combo box and the team overview table of the selected team
     */
    public void setTeamSelectionComboBox()
    {
        for( int arrayIndex = 0; arrayIndex < user.getUserTeams().size(); arrayIndex++){
            userTeamNames.add( user.getUserTeams().get(arrayIndex).getTeamName());
            userTeams.add( user.getUserTeams().get(arrayIndex));
        }

        teamSelectionComboBox.setItems( userTeamNames);

        teamSelectionComboBox.setValue( userTeamNames.get(0));

        leagueNameLabel.setText( userTeams.get(0).getLeagueName());

        userSelectedTeam.add( userTeams.get(0));

        setStandingsTable();

        setTeamOverviewTable();

        setFixtureTable();
    }

    public void setAddPlayersComboBox(){

        for( int arrayIndex = 0; arrayIndex < userTeams.size(); arrayIndex++){
            if( !userTeams.get( arrayIndex).equals( teamOfCoach)){
                userTeamsAddPlayersComboBox.add( userTeams.get( arrayIndex));
            }
        }

        for( int arrayIndex = 0; arrayIndex < userTeamsAddPlayersComboBox.size(); arrayIndex++){
            userTeamNamesAddPlayersComboBox.add( userTeamsAddPlayersComboBox.get(arrayIndex).getTeamName());
        }

        addPlayersComboBox.setItems( userTeamNamesAddPlayersComboBox);
    }

    public void setAddPlayerTable(){
        addPlayerNameColumn.setCellValueFactory( new PropertyValueFactory<>("fullName"));
        addPlayerRoleColumn.setCellValueFactory( new PropertyValueFactory<>("teamRole"));

        addPlayerTable.setItems(userSelectedTeamMembersAtPlayersAddComboBox);

        addPlayerAddColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn( "Add", (TeamMember player) -> {
            System.out.println( "calisiyor");
            onAddPlayerButtonClicked( player);
            return player;
        }));
    }

    /**
     * Sets the standing table according to the league's latest situation
     */
    public void setStandingsTable()
    {
        if( user.getUser().getSportBranch().equals("Football") )
        {
            //Creates the columns of the table
            teamsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("teamName"));
            matchesPlayedColumnStandings.setCellValueFactory( new PropertyValueFactory<>("matchesPlayed"));
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
            matchesPlayedColumnStandings.setCellValueFactory( new PropertyValueFactory<>("matchesPlayed"));
            winColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            losesColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("points"));

            teams = user.getStandings( userSelectedTeam.get(0));
            standingsTableView.setItems( teams);
        }
    }

    /**
     * Sets the team overview table of the selected team
     */
    public void setTeamOverviewTable(){

        /*
            If the branch of the user is football, set the table according to the football branch
            with draws column visible, else if the branch of the user is basketball, set the table
            according to the basketball branch with draws column not visible since it is impossible
            to get a draw in a basketball match.
         */
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
     * Sets the fixture table by setting the table's cells and put information inside the cells
     * according to the latest round played.
     */
    public void setFixtureTable(){
        // Set the columns by calling setCellValueFactory method
        homeColumn.setCellValueFactory( new PropertyValueFactory<>("homeTeamName"));
        scoreColumn.setCellValueFactory( new PropertyValueFactory<>("result"));
        awayColumn.setCellValueFactory( new PropertyValueFactory<>( "awayTeamName"));

        fixtureTable.setItems( user.getGamesOfTheCurrentRound( userSelectedTeam.get(0)));

        //Add view buttons and its listener so user can reach to the details of the clicked match
        detailsColumn.setCellFactory( ButtonTableCell.<Game>forTableColumn("View", (Game gameClicked) -> {
            onFixtureDetailsClicked( gameClicked);
            return gameClicked;
        }));

    }

    public void setPlayerStatisticsTable(){
        playerStatisticsNameColumn.setCellValueFactory( new PropertyValueFactory<>("fullName"));
        playerStatisticsPointsColumn.setCellValueFactory( new PropertyValueFactory<>("pointsOrGoalsScored"));
        playerStatisticsAssistsColumn.setCellValueFactory( new PropertyValueFactory<>("assists"));
        playerStatisticsReboundsColumn.setCellValueFactory( new PropertyValueFactory<>("reboundsOrSavesMade"));
        playerStatisticsStealsColumn.setCellValueFactory( new PropertyValueFactory<>("stealsOrYellowCard"));
        playerStatisticsBlocksColumn.setCellValueFactory( new PropertyValueFactory<>("blocksOrRedCard"));

        playerStatisticsTable.setItems( userSelectedTeamMembers);

        if( user.getUser().getSportBranch().equals("Football") ){
            playerStatisticsPointsColumn.setText("Goals Scored");
            playerStatisticsReboundsColumn.setText("Saves Made");
            playerStatisticsStealsColumn.setText("Yellow Card");
            playerStatisticsBlocksColumn.setText("Red Card");
        }
    }

    public void updatePlayerStatisticsTable( Team teamOfCoach, Game gameClicked){

        userSelectedTeamMembers.clear();

        for( int arrayIndex = 0; arrayIndex < teamOfCoach.getTeamMembers().size(); arrayIndex++){
            if( !teamOfCoach.getTeamMembers().get( arrayIndex).getTeamRole().equals( "Head Coach") &&
                !teamOfCoach.getTeamMembers().get( arrayIndex).getTeamRole().equals( "Assistant Coach")){
                userSelectedTeamMembers.add( teamOfCoach.getTeamMembers().get( arrayIndex));
            }
        }

        playerStatisticsTable.refresh();

        if( gameClicked.getResult() != null){
            setEditableColumnsOfPlayerStatistics();
            setStatisticEnterButtonsOfPlayerStatistics();
        }
    }

    public void setEditableColumnsOfPlayerStatistics(){
        playerStatisticsPointsColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsPointsColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPointsOrGoalsScored(e.getNewValue());
        });

        playerStatisticsAssistsColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsAssistsColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAssists(e.getNewValue());
        });

        playerStatisticsReboundsColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsReboundsColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setReboundsOrSavesMade(e.getNewValue());
        });

        playerStatisticsStealsColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsStealsColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setStealsOrYellowCard(e.getNewValue());
        });

        playerStatisticsBlocksColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsBlocksColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setBlocksOrRedCard(e.getNewValue());
        });

        playerStatisticsTable.setEditable(true);
    }

    public void setStatisticEnterButtonsOfPlayerStatistics(){
        playerStatisticsEnterStatisticsColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn("Enter", (TeamMember player) -> {
            //TODO: cellere girilmiş girdileri db'ye yollayacak kod
            System.out.println( "calisiyor");
            return player;
        }));
    }

    /**
     * Updates the team overview table when the selected team is changed via combo box
     */
    public void updateTeamOverviewTable(){

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
    public void onTeamSelection( ActionEvent event){
        userSelectedTeam.set(0, userTeams.get( teamSelectionComboBox.getSelectionModel().getSelectedIndex()));
        updateTeamOverviewTable();
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

    public void onFixtureDetailsClicked( Game gameClicked){
        blackenedPane.setVisible(true);
        matchDetailsPane.setVisible(true);
        homeTeamLabel.setText( gameClicked.getHomeTeamName());
        awayTeamLabel.setText( gameClicked.getAwayTeamName());
        scoreLabel.setText( gameClicked.getResult());
        dateLabel.setText( gameClicked.getEventDateTime().toString());
        gameLocationLabel.setText( "Game Location: " + gameClicked.getGameLocationName());
        gameLocationLinkLabel.setText( "Link: " + gameClicked.getGameLocationLink());

        if( ( user.getUser().getTeamRole().equals("Head Coach") ||
              user.getUser().getTeamRole().equals("Assistant Coach") ) &&
              ( userSelectedTeam.get(0).equals( gameClicked.getHomeTeam()) ||
                userSelectedTeam.get(0).equals( gameClicked.getAwayTeam()) ))
        {
            if( userSelectedTeam.get(0).equals( gameClicked.getHomeTeam()) ){
                teamOfCoach = gameClicked.getHomeTeam();
            }
            else{
                teamOfCoach = gameClicked.getAwayTeam();
            }
            setStatisticsPane.setVisible(true);
            updatePlayerStatisticsTable( teamOfCoach, gameClicked);
        }
    }

    public void onClickAddPlayerButton( ActionEvent event){
        addPlayersGridPane.setVisible(true);
        setAddPlayersComboBox();
        setAddPlayerTable();
    }

    public void onCloseAddPlayersButtonClicked( ActionEvent event){
        addPlayersGridPane.setVisible(false);
    }

    public void onAddPlayersComboBoxSelection( ActionEvent event){
        userSelectedTeamAtPlayersAddComboBox.clear();
        userSelectedTeamAtPlayersAddComboBox.add(0, userTeamsAddPlayersComboBox.get( addPlayersComboBox.getSelectionModel().getSelectedIndex()));
        userSelectedTeamMembersAtPlayersAddComboBox.clear();
        for( int arrayIndex = 0; arrayIndex < userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().size(); arrayIndex++){
            if( !userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().get( arrayIndex).getTeamRole().equals("Head Coach")
                && !userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().get( arrayIndex).getTeamRole().equals("Assistant Coach"))
            {
                userSelectedTeamMembersAtPlayersAddComboBox.add( userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().get( arrayIndex));
            }
        }

        addPlayerTable.refresh();
    }

    public void onAddPlayerButtonClicked( TeamMember player){
        userSelectedTeamMembers.add( player);
        playerStatisticsTable.refresh();
    }

    @Override
    public void toLeagueScreen(ActionEvent actionEvent) {
    }
}
