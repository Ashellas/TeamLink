package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


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
    private Hyperlink gameLocationLink;

    @FXML
    private GridPane matchDetailsPane;

    @FXML
    private GridPane setStatisticsPane;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsNameColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsFirstColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsSecondColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsThirdColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsForthColumn;

    @FXML
    private TableColumn<TeamMember, String> playerStatisticsFifthColumn;

    @FXML
    private TableColumn<TeamMember, Button> playerStatisticsEnterStatisticsColumn;

    @FXML
    private Pane blackenedPane;

    @FXML
    private Pane blackenedPane1;

    @FXML
    private Pane matchDetailsSubPane;

    @FXML
    private GridPane addPlayersGridPane;

    @FXML
    private Label roundNoLabel;

    @FXML
    private TableColumn<TeamMember, String> addPlayerNameColumn;

    @FXML
    private TableColumn<TeamMember, String> addPlayerRoleColumn;

    @FXML
    private TableColumn<TeamMember, Button> addPlayerAddColumn;

    @FXML
    private ImageView closeDetailsButtonImage;

    @FXML
    private ImageView addPlayerButtonImage;

    @FXML
    private ImageView closeAddPlayerButtonImage;

    @FXML
    private ImageView leftButtonFixtureImage;

    @FXML
    private ImageView rightButtonFixtureImage;

    @FXML
    private ImageView gameLocationImage;

    @FXML
    private Pane messagePane;

    @FXML
    private Button rightButtonFixture;

    @FXML
    private Button leftButtonFixture;

    private Team teamOfCoach;

    private Game gameClicked;

    private int roundNo;

    private final int FIRST_ROUND = 1;

    private ObservableList<models.Team> teams = FXCollections.observableArrayList();

    private ObservableList<models.Team> userTeams = FXCollections.observableArrayList();

    private ObservableList<Team> userTeamsAddPlayersComboBox = FXCollections.observableArrayList();

    private ObservableList<String> userTeamNames = FXCollections.observableArrayList();

    private ObservableList<String> userTeamNamesAddPlayersComboBox = FXCollections.observableArrayList();

    private ObservableList<models.Team> userSelectedTeam = FXCollections.observableArrayList();

    private ObservableList<Team> userSelectedTeamAtPlayersAddComboBox = FXCollections.observableArrayList();

    private ObservableList<TeamMember> userSelectedTeamMembers = FXCollections.observableArrayList();

    private ObservableList<TeamMember> userSelectedTeamMembersAtPlayersAddComboBox = FXCollections.observableArrayList();

    private ObservableList<Game> gamesOfTheRound = FXCollections.observableArrayList();

    private HashMap<Game, ArrayList<TeamMember>> addedPlayers = new HashMap<Game, ArrayList<TeamMember>>();

    public void initData(UserSession user){
        super.initData(user);

        if(user.isStyleDark()) {
            try {
                darkIcons();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                lightIcons();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        setTeamSelectionComboBox();
        setPlayerStatisticsTable( );
        setAddPlayerTable();
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

        userTeamsAddPlayersComboBox.clear();
        userTeamNamesAddPlayersComboBox.clear();
        userSelectedTeamAtPlayersAddComboBox.clear();

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

            standingsTableView.setMaxWidth( standingsTableView.getMaxWidth() * 0.85);

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

        gamesOfTheRound = user.getGamesOfTheCurrentRound( userSelectedTeam.get(0) );

        fixtureTable.setItems( gamesOfTheRound);

        setFixtureButtonsAndLabel();

        //Add view buttons and its listener so user can reach to the details of the clicked match
        detailsColumn.setCellFactory( ButtonTableCell.<Game>forTableColumn("View", (Game gameClicked) -> {
            onFixtureDetailsClicked( gameClicked);
            this.gameClicked = gameClicked;
            return gameClicked;
        }));

    }

    public void setPlayerStatisticsTable(){
        playerStatisticsNameColumn.setCellValueFactory( new PropertyValueFactory<>("fullName"));
        playerStatisticsFirstColumn.setCellValueFactory( new PropertyValueFactory<>("pointsOrGoalsScored"));
        playerStatisticsSecondColumn.setCellValueFactory( new PropertyValueFactory<>("assists"));
        playerStatisticsThirdColumn.setCellValueFactory( new PropertyValueFactory<>("reboundsOrSavesMade"));
        playerStatisticsForthColumn.setCellValueFactory( new PropertyValueFactory<>("stealsOrYellowCard"));
        playerStatisticsFifthColumn.setCellValueFactory( new PropertyValueFactory<>("blocksOrRedCard"));

        playerStatisticsTable.setItems( userSelectedTeamMembers);

        if( user.getUser().getSportBranch().equals("Football") ){
            playerStatisticsFirstColumn.setText("Goals");
            playerStatisticsThirdColumn.setText("Saves");
            playerStatisticsForthColumn.setText("Yellow Card");
            playerStatisticsFifthColumn.setText("Red Card");
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

        if( addedPlayers.get( gameClicked) != null){
            for( int arrayIndex = 0; arrayIndex < addedPlayers.get( gameClicked).size(); arrayIndex++){
                userSelectedTeamMembers.add( addedPlayers.get( gameClicked).get(arrayIndex) );
            }
        }

        playerStatisticsTable.refresh();

        if( !gameClicked.isPlayed() ){
            setEditableColumnsOfPlayerStatistics( gameClicked.isPlayed());
            setRemoveAddedPlayerButton();
        }
        else{
            setEditableColumnsOfPlayerStatistics(gameClicked.isPlayed());
            setStatisticEnterButtonsOfPlayerStatistics();
        }
    }

    public void setEditableColumnsOfPlayerStatistics( boolean isPlayed){
        playerStatisticsFirstColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsFirstColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).getGameStats().setFirstStat(e.getNewValue());
        });

        playerStatisticsSecondColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsSecondColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).getGameStats().setSecondStat(e.getNewValue());
        });

        playerStatisticsThirdColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsThirdColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).getGameStats().setThirdStat(e.getNewValue());
        });

        playerStatisticsForthColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsForthColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).getGameStats().setForthStat(e.getNewValue());
        });

        playerStatisticsFifthColumn.setCellFactory( TextFieldTableCell.forTableColumn());

        playerStatisticsFifthColumn.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).getGameStats().setFifthStat(e.getNewValue());
        });

        playerStatisticsTable.setEditable( isPlayed);
    }

    public void setRemoveAddedPlayerButton(){
        playerStatisticsEnterStatisticsColumn.setText( "Remove");

        playerStatisticsEnterStatisticsColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn("Remove", (TeamMember player) -> {
            if( addedPlayers.get( gameClicked).contains( player) ){
                userSelectedTeamMembers.remove( player);
                addedPlayers.get( gameClicked).remove( player);
                playerStatisticsTable.refresh();
                super.displayMessage( messagePane, "The player has been removed", false);
            }
            else if( !addedPlayers.get( gameClicked).contains( player) ){
                super.displayMessage( messagePane, "You cannot remove a main member of the team", true);
            }
            return player;
        }));
    }

    public void setStatisticEnterButtonsOfPlayerStatistics(){
        playerStatisticsEnterStatisticsColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn("Enter", (TeamMember player) -> {

            if( player.getFirstColumnData() == null || !player.getFirstColumnData().matches("[0-9]+") || player.getFirstColumnData().length() <= 0){
                if( player.getSportBranch().equals("Basketball")){
                    super.displayMessage( messagePane, "The value you entered for points column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football")){
                    super.displayMessage( messagePane, "The value you entered for goals scored column of " + player.getFullName() + " is invalid", true);
                }
                else{
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else if( player.getSecondColumnData() == null || !player.getSecondColumnData().matches("[0-9]+") || player.getSecondColumnData().length() <= 0){
                super.displayMessage( messagePane," The value you entered for assists column of " + player.getFullName() + " is invalid", true);
            }
            else if( player.getThirdColumnData() == null || !player.getThirdColumnData().matches("[0-9]+") || player.getThirdColumnData().length() <= 0){
                if( player.getSportBranch().equals("Basketball")){
                    super.displayMessage( messagePane, "The value you entered for rebounds column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football")){
                    super.displayMessage( messagePane, "The value you entered for saves made column of " + player.getFullName() + " is invalid", true);
                }
                else{
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else if( player.getForthColumnData() == null || !player.getForthColumnData().matches("[0-9]+") || player.getForthColumnData().length() <= 0){
                if( player.getSportBranch().equals("Basketball")){
                    super.displayMessage( messagePane, "The value you entered for steals column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football")){
                    super.displayMessage( messagePane, "The value you entered for yellow card column of " + player.getFullName() + " is invalid", true);
                }
                else{
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else if( player.getFifthColumnData() == null || !player.getFifthColumnData().matches("[0-9]+") || player.getFifthColumnData().length() <= 0){
                if( player.getSportBranch().equals("Basketball")){
                    super.displayMessage( messagePane, "The value you entered for blocks column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football")){
                    super.displayMessage( messagePane, "The value you entered for red card column of " + player.getFullName() + " is invalid", true);
                }
                else{
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else{
                if( player.getSportBranch().equals("Basketball")){
                    try{
                        DatabaseManager.saveBasketballStats( user, player, new BasketballStats( player, player.getFirstColumnData(), player.getSecondColumnData(), player.getThirdColumnData(), player.getForthColumnData(), player.getFifthColumnData() ), gameClicked );
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else if ( player.getSportBranch().equals("Football")){
                    try{
                        DatabaseManager.saveFootball( user, player, new FootballStats( player, player.getFirstColumnData(), player.getSecondColumnData(), player.getThirdColumnData(), player.getForthColumnData(), player.getFifthColumnData() ), gameClicked );
                    }
                    catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                super.displayMessage(messagePane, "You have successfully entered " + player.getFullName() +"'s statistics.", false);
                player.getGameStats().setFirstStat(null);
                player.getGameStats().setSecondStat(null);
                player.getGameStats().setThirdStat(null);
                player.getGameStats().setForthStat(null);
                player.getGameStats().setFifthStat(null);
            }
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
        teams = user.getStandings( userSelectedTeam.get(0));
        gamesOfTheRound = user.getGamesOfTheCurrentRound( userSelectedTeam.get(0) );
        updateTeamOverviewTable();
        standingsTableView.setItems(teams);
        fixtureTable.setItems(gamesOfTheRound);
        setFixtureButtonsAndLabel();
    }

    public void setFixtureButtonsAndLabel(){
        roundNo = gamesOfTheRound.get(0).getRoundNumber();

        roundNoLabel.setText( "Round: " + roundNo);

        if( roundNo >= userSelectedTeam.get(0).getTeamStats().getTotalRounds() ){
            rightButtonFixture.setVisible( false);
        }
        else{
            rightButtonFixture.setVisible( true);
        }

        if( roundNo <= FIRST_ROUND)
        {
            leftButtonFixture.setVisible( false);
        }
        else{
            leftButtonFixture.setVisible( true);
        }
    }


    public void rightButtonFixtureClicked( ActionEvent event) throws SQLException {
        gamesOfTheRound = DatabaseManager.getGames(user.getDatabaseConnection(), user.getStandings( userSelectedTeam.get(0) ), roundNo + 1, userSelectedTeam.get(0).getLeagueId() );
        fixtureTable.setItems(gamesOfTheRound);
        setFixtureButtonsAndLabel();
    }

    public void leftButtonFixtureClicked( ActionEvent event) throws SQLException {
        gamesOfTheRound = DatabaseManager.getGames(user.getDatabaseConnection(), user.getStandings( userSelectedTeam.get(0) ), roundNo - 1, userSelectedTeam.get(0).getLeagueId() );
        fixtureTable.setItems(gamesOfTheRound);
        setFixtureButtonsAndLabel();
    }

    public void onCloseDetailsButtonClicked( ActionEvent event){
        matchDetailsPane.setVisible(false);
        setStatisticsPane.setVisible(false);
        blackenedPane.setVisible(false);
        matchDetailsSubPane.setTranslateX(-30);
    }

    public void onFixtureDetailsClicked( Game gameClicked){
        blackenedPane.setVisible(true);
        matchDetailsPane.setVisible(true);
        homeTeamLabel.setText( gameClicked.getHomeTeamName());
        awayTeamLabel.setText( gameClicked.getAwayTeamName());
        scoreLabel.setText( gameClicked.getResult());
        dateLabel.setText( gameClicked.getEventDateTime().toString());
        gameLocationLabel.setText( "Game Location: " + gameClicked.getGameLocationName());
        gameLocationLink.setText( "Open Game Location");
        gameLocationLink.setOnAction( e -> {
            try {
                Desktop.getDesktop().browse(new URL(gameClicked.getGameLocationLink()).toURI());
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
            }
        });

        try{
            gameLocationImage.setImage( DatabaseManager.getPhoto(user.getDatabaseConnection(), gameClicked.getFileId()));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
        else{
            matchDetailsSubPane.setTranslateX( 300);
        }
    }

    public void onClickAddPlayerButton( ActionEvent event){
        if( gameClicked.isPlayed()){
            super.displayMessage(messagePane, "You cannot add player to a game that is played", true);
        }
        else{
            addPlayersGridPane.setVisible(true);
            blackenedPane1.setVisible(true);
            setAddPlayersComboBox();
            if( addedPlayers.get( gameClicked) == null){
                addedPlayers.put( gameClicked, new ArrayList<TeamMember>());
            }
        }
    }

    public void onCloseAddPlayersButtonClicked( ActionEvent event){
        addPlayersGridPane.setVisible(false);
        blackenedPane1.setVisible(false);
    }

    public void onAddPlayersComboBoxSelection( ActionEvent event){
        if( addPlayersComboBox.getSelectionModel().getSelectedIndex() != -1 ){
            userSelectedTeamAtPlayersAddComboBox.add( userTeamsAddPlayersComboBox.get( addPlayersComboBox.getSelectionModel().getSelectedIndex() ) );
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
    }

    public void onAddPlayerButtonClicked( TeamMember player){
        if( !userSelectedTeamMembers.contains( player) ){
            userSelectedTeamMembers.add( player);
            addedPlayers.get( gameClicked).add( player);
            playerStatisticsTable.refresh();
        }
        else{
            super.displayMessage(messagePane, "You cannot add the same player", true);
        }
    }

    @Override
    public void toLeagueScreen(ActionEvent actionEvent) {
    }

    private void darkIcons() throws URISyntaxException {
        closeDetailsButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/white/cancel_white.png").toURI().toString()));
        closeAddPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/white/cancel_white.png").toURI().toString()));
        addPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/white/squad_white.png").toURI().toString()));
        leftButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png").toURI().toString()));
        rightButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png").toURI().toString()));
    }

    private void lightIcons() throws URISyntaxException {
        closeDetailsButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/black/cancel_black.png").toURI().toString()));
        closeAddPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/black/cancel_black.png").toURI().toString()));
        addPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/black/squad_black.png").toURI().toString()));
        leftButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png").toURI().toString()));
        rightButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png").toURI().toString()));
    }
}