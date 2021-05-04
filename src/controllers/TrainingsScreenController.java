package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import models.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javafx.scene.control.Label;
import org.controlsfx.control.CheckComboBox;

/**
 * Training screen with create a new training button
 */
public class TrainingsScreenController extends MainTemplateController
{

    // training creation panel
    @FXML
    private GridPane createTrainingPane;
    @FXML
    private TextField locationHolder;
    @FXML
    private TextField nameHolder;
    @FXML
    private TextField locationUrlHolder;
    @FXML
    private ComboBox teamsBox;

    private ObservableList<Team> teamsList;

    private ObservableList<Training> data = FXCollections.observableArrayList();

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private GridPane otherPlayersPane;
    @FXML
    private CheckComboBox<TeamMember> otherPlayersBox;
    @FXML
    private ComboBox<String> amPmChoice;
    @FXML
    private ComboBox<Integer> hourChoice;
    @FXML
    private ComboBox<Integer> minuteChoice;
    @FXML
    private CheckBox addOtherCheck;

    // status of the team panel
    @FXML
    private GridPane statsPane;
    @FXML
    private LineChart<?,?> statsChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private Label weekAverageLabel;
    @FXML
    private Label monthAverageLabel;
    @FXML
    private Label overallAverage;

    // training rating screen
    @FXML
    private GridPane ratingsPane;
    @FXML
    private ListView<RateHBox> rateListView;
    @FXML
    private Button cancelRatingButton;
    @FXML
    private Button submitRatingButton;

    // additional components
    @FXML
    private Button createTraining;
    @FXML
    private  GridPane messageGridPane;
    @FXML
    private GridPane trainingGrid;
    @FXML
    private ComboBox<Team> chooseBetweenTeams;
    private ArrayList<GridPane> trainingViewHolder;

    private Hyperlink link;

    // view trainings panel
    @FXML
    private GridPane trainingsGrid;
    @FXML
    private HBox emptyHBox;
    private int trainingCounter;
    ObservableList<Training> trainingHolder = FXCollections.observableArrayList();
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    private double emptyHBoxWidth;
    private Training trainingToRate;
    //---------------------Help Pane---------------------------//
    @FXML
    private GridPane helpPane;
    @FXML
    private Pane darkPane;
    @FXML
    private ImageView helpPaneIcon;

    /**
     * This method initializes the screen
     * It gets the useful data from userSession and sets the properties of the page accordingly
     * @param userSession
     */
    @Override
    public void initData( UserSession userSession )
    {
        // calls the super class' constructor
        super.initData( userSession );

        // gets the current trainings of the user
        trainingHolder = user.getTrainings(); // holds the information of trainings

        // initializes and declares useful data
        trainingViewHolder = new ArrayList<>(); // holds viewable trainings
        ArrayList<Team> teamsOfUser = user.getUserTeams(); // gets the team of the user
        trainingCounter = 0; // useful for the next training button
        TeamStats t1; // get current team' stats

        // if there are more than 6 trainings to show
        if ( trainingHolder.size() > 6 )
        {
            nextButton.setDisable( false );
        }

        // to show players' stats and make other panes invisible
        createTrainingPane.setVisible( false );
        ratingsPane.setVisible( false );
        statsPane.setVisible( true );
        messageGridPane.setVisible( false );

        // adjust the dark & light theme
        if ( userSession.isStyleDark() )
        {
            helpPaneIcon.setImage((new Image("/Resources/Images/white/help_white.png")));
        }
        else
        {
            helpPaneIcon.setImage((new Image("/Resources/Images/black/help_black.png")));
        }

        // adjust the buttons
        if ( userSession.getUser().getTeamRole().equals( "Player" ) )
        {
            createTraining.setVisible( false ); // players cannot create trainings, hence it is not visible
        }

        // to make trainings viewable accordingly
        Platform.runLater(() -> {
            emptyHBoxWidth = emptyHBox.getWidth();
            viewTrainings( trainingHolder );
        });

        // a player can choose between teams
        for ( Team team: teamsOfUser )
        {
            chooseBetweenTeams.getItems().add( team );
        }
        chooseBetweenTeams.setValue( user.getUserTeams().get( 0 ) ); // chooses the first team manually

        t1 = chooseBetweenTeams.getValue().getTeamStats(); // gets the team selection

        // show team's stats in line chart and labels
        int[] lastFiveTraining;
        if ( t1.getTrainingPerformanceReport() != null && t1.getTrainingPerformanceReport().getLastFiveTraining() != null )
        {
            lastFiveTraining = t1.getTrainingPerformanceReport().getLastFiveTraining();
        }
        else
        {
            lastFiveTraining = new int[]{0, 0, 0, 0, 0};
        }
        // creating a new chart
        statsChart.setCreateSymbols(true);
        XYChart.Series series = new XYChart.Series();
        series.setName( "Last five training performances" );
        statsChart.setStyle( "-fx-background-color: TRANSPARENT" );

        // adding last 5 training ratings
        for ( int i = 1; i < lastFiveTraining.length + 1; i++ )
        {
            series.getData().add( new XYChart.Data("" + i, lastFiveTraining[i - 1] ) );
        }

        statsChart.getData().add( series ); // adding the data to the chart

        if ( t1.getTrainingPerformanceReport() != null )
        {
            // setting the labels' text to users' performance
            weekAverageLabel.setText( "" + t1.getTrainingPerformanceReport().getLastWeekAverage() );
            monthAverageLabel.setText( "" + t1.getTrainingPerformanceReport().getLastMonthAverage() );
            overallAverage.setText( "" + t1.getTrainingPerformanceReport().getSeasonAverage() );
        }
        else
        {
            weekAverageLabel.setText( "No report found" );
            monthAverageLabel.setText( "No report found" );
            overallAverage.setText( "No report found" );
        }

        // add items to the time picker components for training creation
        minuteChoice.getItems().addAll(00, 15, 30, 45);
        hourChoice.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 );
        amPmChoice.getItems().addAll("AM", "PM");

        // add items to the time picker components for training creation
        teamsList = FXCollections.observableArrayList( user.getUserTeams() );
        teamsBox.getItems().addAll( teamsList );

        darkPane.setDisable(true);
        darkPane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);

        AppManager.fadeIn(trainingGrid,500);
    }


    /**
     * This method opens up training creation panel
     * @param actionEvent
     */
    public void newTraining( ActionEvent actionEvent ) {
        // makes the create training button invisible
        createTraining.setVisible( false );

        // adds training creation components to the page
        otherPlayersBox = new CheckComboBox<TeamMember>();
        otherPlayersBox.setVisible( false );

        // get other players from the user
        ObservableList<TeamMember> otherPlayersList = user.getAdditionalPlayers( ( Team ) teamsBox.getValue() );

        // add the players to the box, and add the box to the pane
        otherPlayersBox.getItems().addAll( otherPlayersList );
        otherPlayersPane.add( otherPlayersBox, 0, 0 );

        // makes stats pane invisible
        statsPane.setVisible( false );
        ratingsPane.setVisible( false );
        createTrainingPane.setVisible( true );
    }

    /**
     * This method submits the training and creates a training object
     * it also makes training's properties viewable
     * @param actionEvent
     */
    public void submitTrainingPushed( ActionEvent actionEvent ) throws SQLException {

        // checks if the submission is valid
        if ( isSubmitValid() )
        {
            messageGridPane.setVisible( true );
            displayMessage( messagePane, "There is an error", true );
        }
        // if every information is correct.
        else
        {
            // creates a calendar object
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            // get values from time picker
            int hours = hourChoice.getValue();
            int minutes = minuteChoice.getValue();
            if ( amPmChoice.getValue().equals( "PM" ) )
            {
                hours = hours + 12; // if the user choose pm we add +12 hours
            }

            // create the date object
            calendar.set(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue() - 1, datePicker.getValue().getDayOfMonth(),
                    hours, minutes, 0);
            Date trainingDate = calendar.getTime();

            // creates the training object
            Training training = new Training( nameHolder.getText(), trainingDate
                    , locationHolder.getText(), locationUrlHolder.getText(), ( Team ) teamsBox.getValue(), false );

            // adds the training to the list of trainings and to the view
            trainingHolder.add( training );
            viewTrainings( trainingHolder );

            // adds new training to tableview's data
            data.add( training );

            // sends the additional players and the training to the database
            ObservableList<TeamMember> otherPlayersCheckedList = otherPlayersBox.getCheckModel().getCheckedItems();
            DatabaseManager.createNewTraining( user.getDatabaseConnection(), training, otherPlayersCheckedList );

            // remove training pane and add stats pane again
            createTrainingPane.setVisible( false );
            generateView( training );
            refreshCreation();
            statsPane.setVisible( true );

            // makes the create new training button visible again if the user is not a player
            if ( !(user.getUser().getTeamRole().equals( "Player") ) )
                createTraining.setVisible( true );

            // enables the button
            if ( trainingHolder.size() > 6 )
            {
                nextButton.setDisable( false );
            }
        }
    }

    /**
     * Checks if the coaches filled every information
     * @return true if there is an error
     */
    public boolean isSubmitValid()
    {
        return ( datePicker.getValue() == null || hourChoice.getValue() == null || minuteChoice.getValue() == null || amPmChoice.getValue() == null
                || nameHolder.getText() == null || locationHolder.getText() == null || locationUrlHolder.getText() == null
                || teamsBox.getValue() == null );
    }

    /**
     * This method refreshes the text fields and combo boxes of the training creation panel
     */
    public void refreshCreation()
    {
        messageGridPane.setVisible( false );
        // name holder
        nameHolder.setText( null );
        nameHolder.setPromptText( "Training name" );

        // location holder
        locationHolder.setText( null );
        locationHolder.setPromptText( "Location address" );

        // location URL
        locationUrlHolder.setText( null );
        locationUrlHolder.setPromptText( "Location URL" );

        // date picker
        datePicker.setValue( null );
        datePicker.setPromptText( "Select date" );

        // check box
        addOtherCheck.setDisable( true );
        addOtherCheck.setSelected( false );

        // time picker
        amPmChoice.setValue( null );
        minuteChoice.setValue( null );
        hourChoice.setValue( null );

        // team selection box
        teamsBox.setValue( null );

        // choice combo box
        otherPlayersBox.setVisible( false );
        otherPlayersBox.getItems().clear();

        // get all players from the user, add the players to the box, and add the box to the pane
        ObservableList<TeamMember> otherPlayersList = user.getAdditionalPlayers( ( Team ) teamsBox.getValue() );
        otherPlayersBox.getItems().addAll( otherPlayersList );
    }

    /**
     * This method makes stats panel and team creation button visible again
     * @param actionEvent
     */
    public void cancelButtonPushed( ActionEvent actionEvent )
    {
        // makes panes visible again
        createTrainingPane.setVisible( false );
        statsPane.setVisible( true );
        createTraining.setVisible( true );
        refreshCreation(); // to refresh the text fields
        messageGridPane.setVisible( false );
    }

    /**
     * This method return to the stats pane when clicked on the cancel rating button
     * @param actionEvent
     */
    public void CancelRatingButtonPushed(ActionEvent actionEvent)
    {
        // makes panes visible again
        ratingsPane.setVisible( false );
        statsPane.setVisible( true );
        cancelRatingButton.setVisible( false );
    }

    /**
     * This method submits the ratings and sends the information of the ratings to the database
     * @param actionEvent
     */
    public void SubmitRatingButtonPushed(ActionEvent actionEvent) throws SQLException {
        ArrayList<RateHBox> trainingRatingHolder = new ArrayList<>(); // to hold ratings

        for ( RateHBox playersRatingBox: rateListView.getItems() )
            trainingRatingHolder.add( playersRatingBox );
        // send the RatingHBox to the database
        DatabaseManager.savePlayerRatings( user.getDatabaseConnection(), trainingToRate, trainingRatingHolder );
    }

    /**
     * This method shows additional players when clicked on the check box at the training creation panel
     * @param actionEvent
     */
    public void addOtherCheckPushed(ActionEvent actionEvent) {
        otherPlayersBox.setVisible( true ); // makes choice combo box visible
    }

    /**
     * This method creates a new GridPane to view trainings
     * The pane will show trainings' properties, and locationURL as an hyperlink
     * If the user is a coach, it will have delete and rate buttons with their action events
     * @param training
     * @return the GridPane
     */
    private GridPane generateView( Training training )
    {
        // creates a new grid pane
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().addAll("mainGrid" );

        // adds new rows and adjusted the rows
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight( 3 );
        row1.setMinHeight( 0 );

        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight( 20 );
        row2.setMinHeight( 0 );

        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight( 20 );
        row3.setMinHeight( 0 );

        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight( 18 );
        row4.setMinHeight( 0 );

        RowConstraints row5 = new RowConstraints();
        row5.setPercentHeight( 18 );
        row5.setMinHeight( 0 );

        RowConstraints row6 = new RowConstraints();
        row6.setPercentHeight( 18 );
        row6.setMinHeight( 0 );

        RowConstraints row7 = new RowConstraints();
        row7.setPercentHeight( 3 );
        row7.setMinHeight( 0 );

        // adds it all to the grid
        gridPane.getRowConstraints().addAll( row1, row2, row3, row4, row5, row6, row7 );

        // creates a button holder HBox
        HBox buttonHolder = new HBox();
        buttonHolder.setStyle( "-fx-alignment: CENTER" );

        // creates labels to hold information of the training
        Label trainingName = new Label( training.getEventTitle() );
        trainingName.setPrefWidth( emptyHBox.getWidth() );

        Label trainingDateTime = new Label( training.getEventDateTime().toString() );
        trainingDateTime.setPrefWidth( emptyHBox.getWidth() );

        Label trainingLocation = new Label( training.getTrainingLocationName() );
        trainingLocation.setPrefWidth( emptyHBox.getWidth() );

        // adds hyperlink to the locationURL
        Hyperlink trainingURL = new Hyperlink( "Open Training Location" );
        trainingURL.setPrefWidth( emptyHBox.getWidth() );

        // catches some exceptions
        trainingURL.setOnAction( e -> {
            try {
                Desktop.getDesktop().browse( new URL( training.getTrainingLocationLink() ).toURI());
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
            }
        });

        // creates the buttons
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth( emptyHBox.getWidth() * 0.35 );
        Button rateButton = new Button("Rate");
        rateButton.setPrefWidth( emptyHBox.getWidth() * 0.35 );
        if ( training.getIsRated() )
        {
            // sets the texts
            rateButton.setText( "Edit" );
        }

        buttonHolder.getChildren().addAll( rateButton, deleteButton );
        buttonHolder.setSpacing( emptyHBox.getWidth() * 0.10 );

        // sets the alignments
        GridPane.setHalignment( trainingName, HPos.CENTER);
        GridPane.setHalignment( trainingDateTime, HPos.CENTER );
        GridPane.setHalignment( trainingLocation, HPos.CENTER );
        GridPane.setHalignment( trainingURL, HPos.CENTER );
        GridPane.setHalignment( buttonHolder, HPos.CENTER );

        // adds some styles
        trainingName.getStyleClass().add( "title" );
        trainingName.setStyle( "-fx-alignment: CENTER" );
        trainingDateTime.getStyleClass().add( "title" );
        trainingDateTime.setStyle( "-fx-alignment: CENTER" );
        trainingLocation.getStyleClass().add( "title" );
        trainingLocation.setStyle( "-fx-alignment: CENTER" );
        trainingURL.getStyleClass().add( "title" );
        trainingURL.setStyle( "-fx-alignment: CENTER" );

        // adds the components to the pane
        gridPane.add( trainingName, 0,1 );
        gridPane.add( trainingDateTime, 0, 2 );
        gridPane.add( trainingLocation, 0, 3 );
        gridPane.add( trainingURL, 0, 4 );

        // if the user is a coach, adds the buttons to the pane
        if ( !( user.getUser().getTeamRole().equals( "Player" ) ) )
            gridPane.add( buttonHolder, 0, 5 );

        // sets the action of hte rate button:
        rateButton.setOnAction(

                event -> {
                    // refreshes the variables and makes the rating pane visible
                    clearRatingProcess();
                    trainingToRate = training;
                    rateListView.getItems().clear();
                    cancelRatingButton.setVisible( true );
                    statsPane.setVisible( false );
                    createTrainingPane.setVisible( false );
                    ratingsPane.setVisible( true );

                    // if the training is already rated:
                    if ( training.getIsRated() )
                    {
                        // sets the texts
                        rateButton.setText( "Edit" );
                        submitRatingButton.setText( "Save changes" );

                        // creates an array to get older player ratings
                        ArrayList<RateHBox> oldPlayerRatings = new ArrayList<>();
                        try {
                            oldPlayerRatings = DatabaseManager.getPlayerRatings( user.getDatabaseConnection(), training );
                            for ( RateHBox rateValue: oldPlayerRatings )
                            {
                                rateValue.setStyle( "-fx-padding: 20" );
                                rateListView.getItems().add( rateValue );
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    else // if it is the first time rating:
                    {
                        ArrayList<TeamMember> playersToRate = new ArrayList<>();
                        for ( TeamMember member: training.getTeam().getTeamMembers() )
                        {
                            if ( member.getTeamRole().equals( "Player" ) )
                                playersToRate.add( member );
                        }
                        //playersToRate.addAll( DatabaseManager.getOtherPlayers( training) ); //???
                        for ( TeamMember member: playersToRate )
                        {
                            RateHBox box = new RateHBox( member );

                            box.setStyle( "-fx-padding: 20" );
                            rateListView.getItems().add( box );
                        }
                    }

                }
        );
        // sets the action of the delete button
        deleteButton.setOnAction(
                event -> {
                    // clears the viewing and removes the training from the list
                    clearViewTrainings();
                    trainingHolder.remove( training );

                    viewTrainings( trainingHolder );
                    if ( trainingHolder.size() <= 6 )
                    {
                        nextButton.setDisable( true );
                    }
                    // ToDo delete training from db
                }
        );
        return gridPane;
    }

    /**
     * Adds the viewable training objects to the grid pane's correct row and column
     * @param list
     */
    public void viewTrainings( ObservableList<Training> list )
    {
        for ( int i = 1; i < 7; i++ )
        {
            // calculate the row and column
            int row = ( ( i / 4 ) + 1 ) * 2 - 1;
            int column = ( 2 * i - 1 ) % 6;

            // checks if there are more trainings
            if ( list.size() >= i + trainingCounter )
            {
                Training training = list.get(i + trainingCounter - 1);
                GridPane gp = generateView( training );
                gp.getStyleClass().addAll("mainGrid");
                trainingsGrid.add( gp, column, row );
                trainingViewHolder.add( gp );
            }
            else
            {
                // if there are nı trainings to show, it will show empty panes
                GridPane grid = new GridPane();
                grid.getStyleClass().addAll("mainGrid");
                trainingsGrid.add( grid, column, row );
            }

        }
    }

    /**
     * Clears the training view holder (helpful when a training was deleted)
     */
    public void clearViewTrainings()
    {
        trainingViewHolder.clear();
    }

    /**
     * Makes the choose from other teams check enable (helpful for training creation page)
     */
    public void mainTeamBoxSelected()
    {
        addOtherCheck.setDisable( false );
    }

    /**
     * Shows the previous training
     * @param actionEvent
     */
    public void backButtonPushed( ActionEvent actionEvent )
    {
        if ( trainingCounter > 0 ) // checks if there is one
        {
            trainingCounter--;
            if ( trainingCounter == 0 ) {
                backButton.setDisable(true);
            }
            if ( trainingHolder.size() > trainingCounter + 1 && trainingHolder.size() > 6 )
            {
                nextButton.setDisable( false );
            }
        }
        clearViewTrainings();
        viewTrainings( trainingHolder );
    }

    /**
     * Shows the next training
     * @param actionEvent
     */
    public void nextButtonPushed( ActionEvent actionEvent )
    {
        trainingCounter++;
        if ( trainingHolder.size() < trainingCounter + 7 )
        {
            nextButton.setDisable( true ); // if there is nothing more to show
        }

        backButton.setDisable( false );
        clearViewTrainings(); // removes the last training
        viewTrainings( trainingHolder ); // shows the next training
    }

    @Override
    /**
     * Shows help information of the screen
     */
    public void helpButtonPushed(ActionEvent actionEvent){
        darkPane.setVisible(true);
        darkPane.setDisable(false);
        helpPane.setDisable(false);
        helpPane.setVisible(true);
    }

    /**
     * Closes the help pane
     * @param actionEvent close button pushed
     */
    public void helpPaneClose(ActionEvent actionEvent) {
        darkPane.setDisable(true);
        darkPane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);
    }

    /**
     * This method creates a new line chart when a new team is selected
     * @param actionEvent
     */
    public void chooseBetweenTeamsSelected(ActionEvent actionEvent)
    {
        TeamStats t1;
        t1 = chooseBetweenTeams.getValue().getTeamStats(); // gets the team selection

        // show team's stats in line chart and labels
        int[] lastFiveTraining;
        if ( t1.getTrainingPerformanceReport() != null && t1.getTrainingPerformanceReport().getLastFiveTraining() != null )
        {
            lastFiveTraining = t1.getTrainingPerformanceReport().getLastFiveTraining();
        }
        else
        {
            lastFiveTraining = new int[]{0, 0, 0, 0, 0};
        }
        // creating a new chart
        statsChart.setCreateSymbols( true );
        XYChart.Series series = new XYChart.Series();
        series.setName( "Last five training performances" );

        // adding last 5 training ratings
        for ( int i = 1; i < lastFiveTraining.length + 1; i++ )
        {
            series.getData().add( new XYChart.Data("" + i, lastFiveTraining[i - 1] ) );
        }

        statsChart.getData().add( series ); // adding the data to the chart

        if ( t1.getTrainingPerformanceReport() != null )
        {
            // setting the labels' text to users' performance
            weekAverageLabel.setText( "" + t1.getTrainingPerformanceReport().getLastWeekAverage() );
            monthAverageLabel.setText( "" + t1.getTrainingPerformanceReport().getLastMonthAverage() );
            overallAverage.setText( "" + t1.getTrainingPerformanceReport().getSeasonAverage() );
        }
        else
        {
            weekAverageLabel.setText( "No report found" );
            monthAverageLabel.setText( "No report found" );
            overallAverage.setText( "No report found" );
        }
    }

    /**
     * Clears the rating process
     */
    public void clearRatingProcess()
    {
        rateListView.getItems().clear();
    }


}