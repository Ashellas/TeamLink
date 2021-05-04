package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class LeagueScreenController extends MainTemplateController
{
    //Constants
    private final String DARK_STYLE_SHEET = getClass().getResource("/stylesheets/DarkTheme.css").toExternalForm();
    private final String LIGHT_STYLE_SHEET = getClass().getResource("/stylesheets/LightTheme.css").toExternalForm();
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();
    private final int FIRST_ROUND = 1;

    //Variables
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
    private GridPane leaguePane;
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

    //---------------------Help Pane---------------------------//
    @FXML
    private GridPane helpPane;
    @FXML
    private ImageView helpPaneIcon;

    private Team teamOfCoach;
    private Game gameClicked;
    private int roundNo;
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
    private Stage loading;
    private Executor exec;

    /**
     * Sets the intial data when the scene is opened
     * @param user the related information about the user
     */
    public void initData(UserSession user)
    {
        super.initData(user);

        // The color of the icons are changed according to the theme selected
        if(user.isStyleDark())
        {
            try
            {
                darkIcons();
            } catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                lightIcons();
            } catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }

        helpPane.setDisable(true);
        helpPane.setVisible(false);

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

        //Team selection combo box, player statistics table and add player table are initialized
        setTeamSelectionComboBox();
        setPlayerStatisticsTable();
        setAddPlayerTable();

        AppManager.fadeIn(leaguePane,500);
    }

    /**
     * Sets the team selection combo box and the team overview table of the selected team
     */
    public void setTeamSelectionComboBox()
    {
        //Gets the names of the user's teams for the combo box
        for( int arrayIndex = 0; arrayIndex < user.getUserTeams().size(); arrayIndex++)
        {
            userTeamNames.add( user.getUserTeams().get(arrayIndex).getTeamName());
            userTeams.add( user.getUserTeams().get(arrayIndex));
        }

        //Adds the names to the combo box
        teamSelectionComboBox.setItems( userTeamNames);

        //Set the value in the combo box to the first team that is taken
        teamSelectionComboBox.setValue( userTeamNames.get(0));

        //Specifies the league name on top of the standings table according to the first team
        leagueNameLabel.setText( userTeams.get(0).getLeagueName());

        //Creates an observable list to hold the selected team and set its value to the first team
        userSelectedTeam.add( userTeams.get(0));


        /*  Sets the standings, team overview and fixture tables
            according to the selected team in the combo box which is
            the first team, initally.
         */
        setStandingsTable();
        setTeamOverviewTable();
        setFixtureTable();
    }

    /**
     * Sets the combo box in the add players scene of the league screen. It updates the
     * user teams that will be shown in combo box and add player table by updating the observable
     * lists that hold the values for the names of these teams and the teams themselves
     */
    public void setAddPlayersComboBox()
    {

        //Clears all of the observable lists related to the add players scene
        userTeamsAddPlayersComboBox.clear();
        userTeamNamesAddPlayersComboBox.clear();
        userSelectedTeamAtPlayersAddComboBox.clear();

        /* Adds the teams of the user except for the team selected in the fixture
           into the userTeamsAddPlayersComboBox observable list
         */
        for( int arrayIndex = 0; arrayIndex < userTeams.size(); arrayIndex++)
        {
            if( !userTeams.get( arrayIndex).equals( teamOfCoach))
            {
                userTeamsAddPlayersComboBox.add( userTeams.get( arrayIndex));
            }
        }

        /* Adds the names of the teams of the user except for the team selected in the fixture
           into the userTeamNamesAddPlayersComboBox observable list
         */
        for( int arrayIndex = 0; arrayIndex < userTeamsAddPlayersComboBox.size(); arrayIndex++)
        {
            userTeamNamesAddPlayersComboBox.add( userTeamsAddPlayersComboBox.get(arrayIndex).getTeamName());
        }

        /* Set the items, which are the names of the user's teams except for the one selected,
           inside the combo box
         */
        addPlayersComboBox.setItems( userTeamNamesAddPlayersComboBox);
    }

    /**
     * Sets the add player table at the initialization of the scene. This method should be used
     * at only the initialization of the scene as it will result in duplicated rows if it is used
     * at place other than the initialization
     */
    public void setAddPlayerTable()
    {
        //Sets the columns of the table
        addPlayerNameColumn.setCellValueFactory( new PropertyValueFactory<>("fullName"));
        addPlayerRoleColumn.setCellValueFactory( new PropertyValueFactory<>("teamRole"));

        //The items are provided by the userSelectedTeamMembersAtPlayersAddComboBox observable list
        addPlayerTable.setItems(userSelectedTeamMembersAtPlayersAddComboBox);

        //Sets the column that will hold the buttons of the add player table
        addPlayerAddColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn( "Add", (TeamMember player) -> {

            //Sets the function of the button
            onAddPlayerButtonClicked( player);

            return player;
        }));
    }

    /**
     * Sets the standing table according to the league's latest situation
     */
    public void setStandingsTable()
    {
        /* If the user's branch is football then set the standings table with the draw column
           else if the user's branch is basketball, then set the standings table without the draw column
         */
        if( user.getUser().getSportBranch().equals("Football") )
        {
            //Creates the columns of the table
            teamsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("teamName"));
            matchesPlayedColumnStandings.setCellValueFactory( new PropertyValueFactory<>("matchesPlayed"));
            winColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            drawColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesDrawn"));
            losesColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("points"));

            //Get the standings information from the user according to the selected team
            teams = user.getStandings( userSelectedTeam.get(0));

            //Sets the items with teams observable list
            standingsTableView.setItems( teams);
        }
        else if( user.getUser().getSportBranch().equals("Basketball") )
        {
            //Sets the draw column non-visible
            drawColumnStandings.setVisible(false);

            //Adjust the standings table according to its width without the draw column
            standingsTableView.setMaxWidth( standingsTableView.getMaxWidth() * 0.85);

            //Creates the columns of the table
            teamsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("teamName"));
            matchesPlayedColumnStandings.setCellValueFactory( new PropertyValueFactory<>("matchesPlayed"));
            winColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesWon"));
            losesColumnStandings.setCellValueFactory( new PropertyValueFactory<>("gamesLost"));
            pointsColumnStandings.setCellValueFactory( new PropertyValueFactory<>("points"));

            //Set the teams observational list and provide the standings table with the necessary info
            teams = user.getStandings( userSelectedTeam.get(0));
            standingsTableView.setItems( teams);
        }
    }

    /**
     * Sets the team overview table of the selected team
     */
    public void setTeamOverviewTable()
    {
        /*
            If the branch of the user is football, set the table according to the football branch
            with draws column visible, else if the branch of the user is basketball, set the table
            according to the basketball branch with draws column not visible since it is impossible
            to get a draw in a basketball match.
         */
        if( user.getUser().getSportBranch().equals("Football") )
        {

            //Set the labels according to the information received from selected team
            placementLabel.setText( String.valueOf( userSelectedTeam.get(0).getPlacement()));
            winsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesWon()));
            drawsLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesDrawn()));
            losesLabel.setText( String.valueOf( userSelectedTeam.get(0).getGamesLost()));
            pointsLabel.setText( String.valueOf( userSelectedTeam.get(0).getPoints()));
            matchesLeftLabel.setText( String.valueOf( userSelectedTeam.get(0).getMatchesLeft()));
        }
        else if( user.getUser().getSportBranch().equals("Basketball"))
        {
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
    public void setFixtureTable()
    {
        // Set the columns by calling setCellValueFactory method
        homeColumn.setCellValueFactory( new PropertyValueFactory<>("homeTeamName"));
        scoreColumn.setCellValueFactory( new PropertyValueFactory<>("result"));
        awayColumn.setCellValueFactory( new PropertyValueFactory<>( "awayTeamName"));

        /* Set the gamesOfTheRound observational list according to the taken info from the user and
           the selected team
         */
        gamesOfTheRound = user.getGamesOfTheCurrentRound( userSelectedTeam.get(0) );

        //Set the items of the fixture table with the gamesOfTheRound observable list
        fixtureTable.setItems( gamesOfTheRound);

        //Set the fixture buttons and its label that indicates which round the user views
        setFixtureButtonsAndLabel();

        //Add view buttons and its listener so user can reach to the details of the clicked match
        detailsColumn.setCellFactory( ButtonTableCell.<Game>forTableColumn("View", (Game gameClicked) -> {
            //The function of the button
            onFixtureDetailsClicked( gameClicked);

            //the game that the user clicked will be hold inside a variable for further use
            this.gameClicked = gameClicked;

            return gameClicked;
        }));
    }

    /**
     * Sets the player statistics table according clicked game if the clicked game is the game
     * of the user's team. This method should be initialized at the initialization of the scene
     * to avoid duplication of rows
     */
    public void setPlayerStatisticsTable()
    {
        //Setting the rows of the table
        playerStatisticsNameColumn.setCellValueFactory( new PropertyValueFactory<>("fullName"));
        playerStatisticsFirstColumn.setCellValueFactory( new PropertyValueFactory<>("firstColumnData"));
        playerStatisticsSecondColumn.setCellValueFactory( new PropertyValueFactory<>("secondColumnData"));
        playerStatisticsThirdColumn.setCellValueFactory( new PropertyValueFactory<>("thirdColumnData"));
        playerStatisticsForthColumn.setCellValueFactory( new PropertyValueFactory<>("forthColumnData"));
        playerStatisticsFifthColumn.setCellValueFactory( new PropertyValueFactory<>("fifthColumnData"));

        /* Sets the table with the userSelectedTeamMembers observable list which consists of players
           of the user's team
         */
        playerStatisticsTable.setItems( userSelectedTeamMembers);

        // Change the name of the headers if the branch of the user is football
        if( user.getUser().getSportBranch().equals("Football") )
        {
            playerStatisticsFirstColumn.setText("Goals");
            playerStatisticsThirdColumn.setText("Fouls");
            playerStatisticsForthColumn.setText("Passes");
            playerStatisticsFifthColumn.setText("Tackles");
        }
    }

    /**
     * Updates the player statistics table at the middle of the scene. This method should be used
     * when a new game is clicked instead of the setPlayerStatisticsTable
     * @param teamOfCoach the team that the user is a part of
     * @param gameClicked the game that is clicked by the user
     */
    public void updatePlayerStatisticsTable( Team teamOfCoach, Game gameClicked)
    {
        //Clear the userSelectedTeamMembers obervational list so we can update it with new data
        userSelectedTeamMembers.clear();

        /* Add team member object with the role "player" to userSelectedTeamMembers observable list.
           These players are part of the user's team
         */
        for( int arrayIndex = 0; arrayIndex < teamOfCoach.getTeamMembers().size(); arrayIndex++)
        {
            if( !teamOfCoach.getTeamMembers().get( arrayIndex).getTeamRole().equals( "Head Coach") &&
                    !teamOfCoach.getTeamMembers().get( arrayIndex).getTeamRole().equals( "Assistant Coach"))
            {
                userSelectedTeamMembers.add( teamOfCoach.getTeamMembers().get( arrayIndex));
            }
        }

        /* If there are added players to this game, also add them.
           We use hashmaps to easily seperate the games by making games keys and
           arrayList of added players as values
         */
        if( Game.addedPlayers.get( gameClicked) != null)
        {
            for( int arrayIndex = 0; arrayIndex < Game.addedPlayers.get( gameClicked).size(); arrayIndex++)
            {
                userSelectedTeamMembers.add( Game.addedPlayers.get( gameClicked).get(arrayIndex) );
            }
        }

        //We refresh the player statistics table according to the new data
        playerStatisticsTable.refresh();

        /* If the game is played, enable the editable columns and enter statistics buttons of the
           player statistics table; else if the game is not played, enable the remove buttons
         */
        if( !gameClicked.isPlayed() )
        {
            setEditableColumnsOfPlayerStatistics( gameClicked.isPlayed());
            setRemoveAddedPlayerButton();
        }
        else
        {
            setEditableColumnsOfPlayerStatistics(gameClicked.isPlayed());
            setStatisticEnterButtonsOfPlayerStatistics();
        }
    }

    /**
     * Sets the editable columns of the player statistics
     * @param isPlayed a boolean indicating whether the clicked game is played or not
     */
    public void setEditableColumnsOfPlayerStatistics( boolean isPlayed){

       /* These two lines are necessary codes to set editable table cells
          There are five of these two lines, each for different column
        */
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

        //Set the player statistics table editable if the clicked game is played
        playerStatisticsTable.setEditable( isPlayed);
    }

    /**
     * Sets the remove added players button and setting the header of the enter statistics column
     * as "remove". These buttons enable the user to remove the added players from the player statistics
     * table
     */
    public void setRemoveAddedPlayerButton()
    {
        //Set the header of the enter statistics column
        playerStatisticsEnterStatisticsColumn.setText( "Remove");

        //Specify the function of the buttons
        playerStatisticsEnterStatisticsColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn("Remove", (TeamMember player) -> {
            /* If the addedPlayers hashmap does not contain gamesClicked object as key, then display
               the message that a main member of the team cannot be removed when the button is clicked
               as there are no added players
             */
            if( !Game.addedPlayers.containsKey( gameClicked))
            {
                super.displayMessage( messagePane, "You cannot remove a main member of the team", true);
            }
            else
            {
                //If the player that is clicked is an added player, then remove the player
                if( Game.addedPlayers.get( gameClicked).contains( player) )
                {
                    //remove from the observational list
                    userSelectedTeamMembers.remove( player);

                    //remove from the added player arrayList that is in the hashmap
                    Game.addedPlayers.get( gameClicked).remove( player);

                    //Refresh the player statistics table
                    playerStatisticsTable.refresh();

                    //display the message that informs the user that the player has been removed
                    super.displayMessage( messagePane, "The player has been removed", false);
                }
                else if( !Game.addedPlayers.get( gameClicked).contains( player) )
                {
                    //display the message that infroms the user that the player cannot be removed
                    super.displayMessage( messagePane, "You cannot remove a main member of the team", true);
                }
            }

            return player;
        }));
    }

    /**
     * Sets the statistic enter button of the player statistic table. These buttons send the input of the
     * user related with the player's statistics to the database
     */
    public void setStatisticEnterButtonsOfPlayerStatistics()
    {
        //Set the header of the enter statistics column
        playerStatisticsEnterStatisticsColumn.setText( "Enter");

        //Set the buttons with the necessary code line
        playerStatisticsEnterStatisticsColumn.setCellFactory( ButtonTableCell.<TeamMember>forTableColumn("Enter", (TeamMember player) -> {
            //The necessary checker if statements to prevent from unwanted inputs from the user
            if( player.getFirstColumnData() == null || !player.getFirstColumnData().matches("[0-9]+") || player.getFirstColumnData().length() <= 0)
            {
                //messages are displayed according to the user's sport branch in case of an error/wrong input
                if( player.getSportBranch().equals("Basketball") )
                {
                    super.displayMessage( messagePane, "The value you entered for points column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football") )
                {
                    super.displayMessage( messagePane, "The value you entered for goals scored column of " + player.getFullName() + " is invalid", true);
                }
                else
                {
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else if( player.getSecondColumnData() == null || !player.getSecondColumnData().matches("[0-9]+") || player.getSecondColumnData().length() <= 0)
            {
                //messages are displayed according to the user's sport branch in case of an error/wrong input
                super.displayMessage( messagePane," The value you entered for assists column of " + player.getFullName() + " is invalid", true);
            }
            else if( player.getThirdColumnData() == null || !player.getThirdColumnData().matches("[0-9]+") || player.getThirdColumnData().length() <= 0)
            {
                //messages are displayed according to the user's sport branch in case of an error/wrong input
                if( player.getSportBranch().equals( "Basketball") )
                {
                    super.displayMessage( messagePane, "The value you entered for rebounds column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals( "Football") )
                {
                    super.displayMessage( messagePane, "The value you entered for fouls made column of " + player.getFullName() + " is invalid", true);
                }
                else
                {
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else if( player.getForthColumnData() == null || !player.getForthColumnData().matches("[0-9]+") || player.getForthColumnData().length() <= 0)
            {
                //messages are displayed according to the user's sport branch in case of an error/wrong input
                if( player.getSportBranch().equals("Basketball") )
                {
                    super.displayMessage( messagePane, "The value you entered for steals column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football") )
                {
                    super.displayMessage( messagePane, "The value you entered for passes made column of " + player.getFullName() + " is invalid", true);
                }
                else
                {
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else if( player.getFifthColumnData() == null || !player.getFifthColumnData().matches("[0-9]+") || player.getFifthColumnData().length() <= 0)
            {
                //messages are displayed according to the user's sport branch in case of an error/wrong input
                if( player.getSportBranch().equals("Basketball"))
                {
                    super.displayMessage( messagePane, "The value you entered for blocks column of " + player.getFullName() + " is invalid", true);
                }
                else if( player.getSportBranch().equals("Football"))
                {
                    super.displayMessage( messagePane, "The value you entered for tackles made column of " + player.getFullName() + " is invalid", true);
                }
                else
                {
                    super.displayMessage( messagePane, "The value you entered for " + player.getFullName() + " is invalid", true);
                }
            }
            else
            {
                //Send the data to the database according to the user's sport branch
                if( player.getSportBranch().equals("Basketball") )
                {
                    try
                    {
                        DatabaseManager.saveBasketballStats( user, player, new BasketballStats( player.getGameStats().getId(), player.getFirstColumnData(), player.getSecondColumnData(), player.getThirdColumnData(), player.getForthColumnData(), player.getFifthColumnData() ), gameClicked );
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else if ( player.getSportBranch().equals("Football"))
                {
                    try
                    {
                        DatabaseManager.saveFootball( user, player, new FootballStats( player.getGameStats().getId(), player.getFirstColumnData(), player.getSecondColumnData(), player.getThirdColumnData(), player.getForthColumnData(), player.getFifthColumnData() ), gameClicked );
                    }
                    catch (SQLException throwables)
                    {
                        throwables.printStackTrace();
                    }
                }

                //Display the message that the data has been successfully sent
                super.displayMessage(messagePane, "You have successfully entered " + player.getFullName() +"'s statistics.", false);

                //Set the values of the columns null again after successfully sending it
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

        /* If the user's branch is football then create columns with a draw column
           else if the user's branch is basketball then create columns without the draw column
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
        /* Change the first index od userSelectedTeam observable list according to the chosen team
           in the combo box
         */
        userSelectedTeam.set(0, userTeams.get( teamSelectionComboBox.getSelectionModel().getSelectedIndex()));

        //Change the teams observable list according to the new team selected
        teams = user.getStandings( userSelectedTeam.get(0));

        //Set the gamesOfTheRound observable list according to the new team selected
        gamesOfTheRound = user.getGamesOfTheCurrentRound( userSelectedTeam.get(0) );

        //Update the team overview table according to the new team selected
        updateTeamOverviewTable();

        //Update the standings table according to the new team selected
        standingsTableView.setItems(teams);

        //Update the fixture table, its buttons and label according to the new team selected
        fixtureTable.setItems(gamesOfTheRound);
        setFixtureButtonsAndLabel();
    }

    /**
     * Sets the fixture buttons and the label according to the round number
     * and the selected team from the team selection combo box
     */
    public void setFixtureButtonsAndLabel()
    {
        //get the round number of the games in the gamesOfTheRound observable list
        roundNo = gamesOfTheRound.get(0).getRoundNumber();

        //Set the label's text according to the round number
        roundNoLabel.setText( "Round: " + roundNo);

        //If the user reaches to the last round disable the right button by making it invisible
        if( roundNo >= userSelectedTeam.get(0).getTeamStats().getTotalRounds() )
        {
            rightButtonFixture.setVisible( false);
        }
        else
        {
            rightButtonFixture.setVisible( true);
        }

        //If the user reaches to the first round, disable the left button by making it invisible
        if( roundNo <= FIRST_ROUND)
        {
            leftButtonFixture.setVisible( false);
        }
        else
        {
            leftButtonFixture.setVisible( true);
        }
    }

    /**
     * Shows the games of the next round
     * @param event ActionEvent object
     * @throws SQLException when the data cannot be retrieved from the database
     */
    public void rightButtonFixtureClicked( ActionEvent event) throws SQLException, ParseException {
        /* Sets the gamesOfTheRound observable list to a new observable list taken from the database
           which is the games of the next round of the same team
         */
        gamesOfTheRound = DatabaseManager.getGames(user.getDatabaseConnection(), user.getStandings( userSelectedTeam.get(0) ), roundNo + 1, userSelectedTeam.get(0).getLeagueId() );

        //Update the fixture table and its buttons and label
        fixtureTable.setItems(gamesOfTheRound);
        setFixtureButtonsAndLabel();
    }

    /**
     * Shows the games of the preceding round
     * @param event ActionEvent object
     * @throws SQLException when the data cannot be retrieved from the database
     */
    public void leftButtonFixtureClicked( ActionEvent event) throws SQLException, ParseException {
        /* Sets the gamesOfTheRound observable list to a new observable list taken from the database
           which is the games of the preceding round of the same team
         */
        gamesOfTheRound = DatabaseManager.getGames(user.getDatabaseConnection(), user.getStandings( userSelectedTeam.get(0) ), roundNo - 1, userSelectedTeam.get(0).getLeagueId() );

        //Update the fixture table and its buttons and label
        fixtureTable.setItems(gamesOfTheRound);
        setFixtureButtonsAndLabel();
    }

    /**
     * The function of the button that close the match detail panes
     * @param event ActionEvent object
     */
    public void onCloseDetailsButtonClicked( ActionEvent event)
    {
        //Close the match details panes
        matchDetailsPane.setVisible(false);
        setStatisticsPane.setVisible(false);

        //Close the blackened pane
        blackenedPane.setVisible(false);

        /* Sets the matchDetailsSubPane to its original position (this pane translates across
           the x-axis when the clicked game is not the game of the user's team)
         */
        matchDetailsSubPane.setTranslateX(-30);
    }

    /**
     * Open the match details and player statistics pane under some conditions. It opens the player
     * statistics pane if the clicked game is one of the games that user's team has played or will play and
     * also if the user is a coach.
     * @param gameClicked the game that is clicked in the fixture table
     */
    public void onFixtureDetailsClicked( Game gameClicked)
    {
        //Enable the blackened pane and open matchDetailsPane
        blackenedPane.setVisible(true);
        matchDetailsPane.setVisible(true);

        //Set the labels according to the information related to game

        //name of the home team
        homeTeamLabel.setText( gameClicked.getHomeTeamName());

        //name of the away team
        awayTeamLabel.setText( gameClicked.getAwayTeamName());

        //score of the game
        scoreLabel.setText( gameClicked.getResult());

        //date of the game
        dateLabel.setText( gameClicked.getEventDateTime().toString());

        //Location of the game
        gameLocationLabel.setText( "Game Location: " + gameClicked.getGameLocationName());

        //The link of the location(it directs the user to the location's google maps link)
        gameLocationLink.setText( "Open Game Location");

        //Makes the link functional
        gameLocationLink.setOnAction( e -> {
            try {
                Desktop.getDesktop().browse(new URL(gameClicked.getGameLocationLink()).toURI());
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
            }
        });

        //Setting the image of the location
        try{
            gameLocationImage.setImage( DatabaseManager.getPhoto(user.getDatabaseConnection(), gameClicked.getFileId()));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /*
        If the user is a coach and the game clicked is one of the games of the user's team, then player
        statistics panel will be visible
         */
        if( ( user.getUser().getTeamRole().equals("Head Coach") ||
                user.getUser().getTeamRole().equals("Assistant Coach") ) &&
                ( userSelectedTeam.get(0).equals( gameClicked.getHomeTeam()) ||
                        userSelectedTeam.get(0).equals( gameClicked.getAwayTeam()) ))
        {
            //Find which team is the coach's team (home or away) and assign it to a variable for further use
            if( userSelectedTeam.get(0).equals( gameClicked.getHomeTeam()) )
            {
                teamOfCoach = gameClicked.getHomeTeam();
            }
            else
            {
                teamOfCoach = gameClicked.getAwayTeam();
            }

            //open the statistics pane
            setStatisticsPane.setVisible(true);

            //update the player statistics pane according to the information related to the game
            updatePlayerStatisticsTable( teamOfCoach, gameClicked);
        }
        else
        {
            /* Move the match details pane to the middle of the screen if the conditions are not met
               to maintain the order of the scene
             */
            matchDetailsSubPane.setTranslateX( 300);
        }
    }

    /**
     * Opens the add player panel if the game is not played
     * @param event add player button is pushed
     */
    public void onClickAddPlayerButton( ActionEvent event)
    {
        //If the game is played, then do not open the add players panel; else, open the panel
        if( gameClicked.isPlayed())
        {
            //display an error message
            super.displayMessage(messagePane, "You cannot add player to a game that is played", true);
        }
        else
        {
            //Open the add players panel
            addPlayersGridPane.setVisible(true);
            blackenedPane1.setVisible(true);

            //Set the combo box inside the add players
            setAddPlayersComboBox();

            //If the clicked game is not a key in hashmap, then add it to the hashmap as a key
            if( Game.addedPlayers.get( gameClicked) == null)
            {
                Game.addedPlayers.put( gameClicked, new ArrayList<TeamMember>());
            }
        }
    }

    /**
     * closes the add players panel
     * @param event close add players panel button pushed
     */
    public void onCloseAddPlayersButtonClicked( ActionEvent event)
    {
        addPlayersGridPane.setVisible(false);
        blackenedPane1.setVisible(false);
    }

    /**
     * The selected team's player is added to the add players table when a new team is selected
     * @param event selection from the add players panel's combo box
     */
    public void onAddPlayersComboBoxSelection( ActionEvent event)
    {
        //Do not do anything if the selected item's index is -1 to avoid errors
        if( addPlayersComboBox.getSelectionModel().getSelectedIndex() != -1 )
        {
            //Add a team object to the userSelectedTeamAtPlayersAddComboBox observable list
            userSelectedTeamAtPlayersAddComboBox.add( userTeamsAddPlayersComboBox.get( addPlayersComboBox.getSelectionModel().getSelectedIndex() ) );

            //Clear userselectedTeamMembersAtPlayersAddComboBox before adding new objects to it
            userSelectedTeamMembersAtPlayersAddComboBox.clear();

            //Add the updated version to the observarble list
            for( int arrayIndex = 0; arrayIndex < userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().size(); arrayIndex++)
            {
                //Do not add the coaches to the observable list
                if( !userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().get( arrayIndex).getTeamRole().equals("Head Coach")
                        && !userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().get( arrayIndex).getTeamRole().equals("Assistant Coach"))
                {
                    userSelectedTeamMembersAtPlayersAddComboBox.add( userSelectedTeamAtPlayersAddComboBox.get(0).getTeamMembers().get( arrayIndex));
                }
            }

            //Refresh the table according to the new data
            addPlayerTable.refresh();
        }
    }

    /**
     * Adds the player to the other team (player statistics table) if it is not added already
     * @param player the player that is clicked
     */
    public void onAddPlayerButtonClicked( TeamMember player)
    {
        /* If the userSelectedTeamMembers observable list does not contain the player
           then it adds the player to the player statistics table
         */
        if( !userSelectedTeamMembers.contains( player) )
        {
            //Add the player to the userSelectedTeamMembers observable list and addedPlayers hashmap
            userSelectedTeamMembers.add( player);
            Game.addedPlayers.get( gameClicked).add( player);

            //Update the player statistics table
            playerStatisticsTable.refresh();
        }
        else
        {
            super.displayMessage(messagePane, "You cannot add the same player", true);
        }
    }

    /**
     * This method is overriden so nothing happens when the league button is pushed
     * @param actionEvent league button pushed
     */
    @Override
    public void toLeagueScreen(ActionEvent actionEvent) {}

    /**
     * sets the icon's colors if the dark theme is selected
     * @throws URISyntaxException when the images are not found
     */
    private void darkIcons() throws URISyntaxException {
        closeDetailsButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/white/cancel_white.png").toURI().toString()));
        closeAddPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/white/cancel_white.png").toURI().toString()));
        addPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/white/squad_white.png").toURI().toString()));
        leftButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png").toURI().toString()));
        rightButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/white/outline_arrow_back_ios_white_24dp.png").toURI().toString()));
        helpPaneIcon.setImage((new Image(getClass().getResource("/Resources/Images/white/help_white.png").toURI().toString())));
    }

    /**
     * sets the icon's colors if the light theme is selected
     * @throws URISyntaxException when the images are not found
     */
    private void lightIcons() throws URISyntaxException {
        closeDetailsButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/black/cancel_black.png").toURI().toString()));
        closeAddPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/black/cancel_black.png").toURI().toString()));
        addPlayerButtonImage.setImage( new Image(getClass().getResource("/Resources/Images/black/squad_black.png").toURI().toString()));
        leftButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png").toURI().toString()));
        rightButtonFixtureImage.setImage( new Image(getClass().getResource("/Resources/Images/black/outline_arrow_back_ios_black_24dp.png").toURI().toString()));
        helpPaneIcon.setImage((new Image(getClass().getResource("/Resources/Images/black/help_black.png").toURI().toString())));
    }

    @Override
    /**
     * Shows help information of the screen
     */
    public void helpButtonPushed(ActionEvent actionEvent){
        blackenedPane.setVisible(true);
        blackenedPane.setDisable(false);
        helpPane.setDisable(false);
        helpPane.setVisible(true);
    }

    /**
     * Closes the help pane
     * @param actionEvent close button pushed
     */
    public void helpPaneClose(ActionEvent actionEvent) {
        blackenedPane.setDisable(true);
        blackenedPane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);
    }

    @Override
    public void SynchronizeData(ActionEvent event) {
        blackenedPane.setVisible(true);
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
            blackenedPane.setVisible(false);
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
        blackenedPane.setOpacity(0.5);
    }

}