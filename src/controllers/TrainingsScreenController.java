package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import models.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    // status of user panel
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

    // rating screen
    @FXML
    private GridPane ratingsPane;
    @FXML
    private ListView<RateHBox> rateListView;
    @FXML
    private Button cancelRatingButton;
    @FXML
    private Button submitRatingButton;

    // additional
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

    // trainings showing
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

    /**
     * This method
     * @param userSession
     */
    @Override
    public void initData( UserSession userSession )
    {
        super.initData( userSession );
        trainingHolder = user.getTrainings();

        trainingViewHolder = new ArrayList<>();

        trainingCounter = 0;

        /**
        if ( trainingHolder.size() > 6 )
        {
            nextButton.setDisable( false );
        }
        */

        // first, we will show players' stats and make other panes invisible
        createTrainingPane.setVisible( false );
        ratingsPane.setVisible( false );
        statsPane.setVisible( true );
        messageGridPane.setVisible( false );

        // get current team' stats
        TeamStats t1;
        if ( userSession.isStyleDark() )
        {
            darkThemeIcons();
        }
        else
        {
            lightThemeIcons();
        }
        if ( userSession.getUser().getTeamRole().equals( "Player" ) )
        {
            createTraining.setVisible( false ); // players cannot create trainings, hence it is not visible
        }

        ArrayList<Team> teamsOfUser = user.getUserTeams();

        // a player can choose between its teams
        for ( Team team: teamsOfUser )
        {
            chooseBetweenTeams.getItems().add( team );
        }
        chooseBetweenTeams.setValue( user.getUserTeams().get( 0 ) );

        t1 = chooseBetweenTeams.getValue().getTeamStats();

        // show team's stats in line chart and labels
        /**
         int[] lastFiveTraining = t1.getTrainingPerformanceReport().getLastFiveTraining();
         // creating a new chart
         statsChart.setCreateSymbols(true);
         XYChart.Series series = new XYChart.Series();
         series.setName( "Last five training's average" );

         // adding last 5 training ratings
         for ( int i = 1; i < lastFiveTraining.length + 1; i++ )
         {
         series.getData().add( new XYChart.Data("" + i, lastFiveTraining[i - 1] ) );
         }

         statsChart.getData().add( series ); // adding the data to the chart

         // setting the labels' text to users' performance
         weekAverageLabel.setText( "" + t1.getTrainingPerformanceReport().getLastWeekAverage() );
         monthAverageLabel.setText( "" + t1.getTrainingPerformanceReport().getLastMonthAverage() );
         overallAverage.setText( "" + t1.getTrainingPerformanceReport().getSeasonAverage() );
         */

        // add items to the time picker components for training creation
        minuteChoice.getItems().addAll(00, 15, 30, 45);
        hourChoice.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        amPmChoice.getItems().addAll("AM", "PM");

        teamsList = FXCollections.observableArrayList( user.getUserTeams() );
        teamsBox.getItems().addAll( teamsList );

        viewTrainings( trainingHolder );
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

        // get all players from the database
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
     * it also makes training's properties visible on tableview
     * @param actionEvent
     */
    public void submitTrainingPushed( ActionEvent actionEvent )
    {
        if ( isSubmitValid() )
        {
            // messagePane.setVisible( true );
            displayMessage( messagePane, "There is an error", true );
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            // get values from time picker
            int hours = hourChoice.getValue();
            int minutes = minuteChoice.getValue();
            if ( amPmChoice.getValue().equals( "PM" ) )
            {
                hours = hours + 12;
            }

            // create date object
            calendar.set(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue() - 1, datePicker.getValue().getDayOfMonth(),
                    hours, minutes, 0);
            Date trainingDate = calendar.getTime();

            // creates the training object
            Training training = new Training( nameHolder.getText(), trainingDate
                    , locationHolder.getText(), locationUrlHolder.getText(), ( Team ) teamsBox.getValue(), false );

            trainingHolder.add( training );
            viewTrainings( trainingHolder );

            // adds new training to tableview's data
            data.add( training );
            link = new Hyperlink( locationUrlHolder.getText() );

            link.setOnAction( evt -> {
                // add hyperlink
                Runtime rt = Runtime.getRuntime();
                String urlAddress = locationUrlHolder.getText();
                try {
                    rt.exec("rund1132 url.dll,FileProtocolHandler " + urlAddress);
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            });

            // ToDo gönderiyo
            ObservableList<TeamMember> otherPlayersCheckedList = otherPlayersBox.getCheckModel().getCheckedItems();
            // DatabaseManager.createTraining( user.getDatabaseConnection(), training, otherPlayersCheckedList );

            // remove training pane and add stats pane again
            createTrainingPane.setVisible( false );
            generateView( training );
            refreshCreation();
            statsPane.setVisible( true );
            if ( !(user.getUser().getTeamRole().equals( "Player") ) )
                createTraining.setVisible( true );

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
     * This method refreshes the text fields
     */
    public void refreshCreation()
    {
        nameHolder.setText( null );
        nameHolder.setPromptText( "Training name" );

        locationHolder.setText( null );
        locationHolder.setPromptText( "Location address" );

        locationUrlHolder.setText( null );
        locationUrlHolder.setPromptText( "Location URL" );

        datePicker.setValue( null );
        datePicker.setPromptText( "Select date" );

        addOtherCheck.setDisable( true );
        addOtherCheck.setSelected( false );

        amPmChoice.setValue( null );
        minuteChoice.setValue( null );
        hourChoice.setValue( null );

        teamsBox.setValue( null );

        otherPlayersBox.setVisible( false );
        otherPlayersBox.getItems().clear();
        // get all players from the database
        ObservableList<TeamMember> otherPlayersList = user.getAdditionalPlayers( ( Team ) teamsBox.getValue() );

        // add the players to the box, and add the box to the pane
        otherPlayersBox.getItems().addAll( otherPlayersList );
    }

    /**
     * This method makes stats panel and team creation button visible again
     * @param actionEvent
     */
    public void cancelButtonPushed( ActionEvent actionEvent )
    {
        createTrainingPane.setVisible( false );
        statsPane.setVisible( true );
        createTraining.setVisible( true );
        refreshCreation(); // to refresh the text fields
    }

    /**
     * This method return to the stats pane when clicked on the cancel rating button
     * @param actionEvent
     */
    public void CancelRatingButtonPushed(ActionEvent actionEvent)
    {
        ratingsPane.setVisible( false );
        statsPane.setVisible( true );
        cancelRatingButton.setVisible( false );
    }

    /**
     * This method submits the ratings and sends the HashMap to the database
     * @param actionEvent
     */
    public void SubmitRatingButtonPushed(ActionEvent actionEvent)
    {
        ArrayList<RateHBox> trainingRatingHolder = new ArrayList<>();

        for ( RateHBox playersRatingBox: rateListView.getItems() )
                trainingRatingHolder.add( playersRatingBox );
        // ToDo: db'e yolla
    }

    /**
     * This method shows additional players when clicked on the check box at the training creation panel
     * @param actionEvent
     */
    public void addOtherCheckPushed(ActionEvent actionEvent) {
        otherPlayersBox.setVisible( true );
    }

    private GridPane generateView( Training training )
    {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().addAll("mainGrid" );

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
        gridPane.getRowConstraints().addAll( row1, row2, row3, row4, row5, row6, row7 );

        HBox buttonHolder = new HBox();
        buttonHolder.setStyle( "-fx-alignment: CENTER" );

        Label trainingName = new Label( training.getEventTitle() );
        trainingName.setPrefWidth( emptyHBox.getWidth() );

        Label trainingDateTime = new Label( training.getEventDateTime().toString() );
        trainingDateTime.setPrefWidth( emptyHBox.getWidth() );

        Label trainingLocation = new Label( training.getTrainingLocationName() );
        trainingLocation.setPrefWidth( emptyHBox.getWidth() );

        Hyperlink trainingURL = new Hyperlink( "Open Training Location" );
        trainingURL.setPrefWidth( emptyHBox.getWidth() );

        trainingURL.setOnAction( e -> {
            try {
                Desktop.getDesktop().browse( new URL( training.getTrainingLocationLink() ).toURI());
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth( emptyHBox.getWidth() * 0.35 );
        Button rateButton = new Button("Rate");
        rateButton.setPrefWidth( emptyHBox.getWidth() * 0.35 );

        buttonHolder.getChildren().addAll( rateButton, deleteButton );
        buttonHolder.setSpacing( emptyHBox.getWidth() * 0.10 );

        GridPane.setHalignment( trainingName, HPos.CENTER);
        GridPane.setHalignment( trainingDateTime, HPos.CENTER );
        GridPane.setHalignment( trainingLocation, HPos.CENTER );
        GridPane.setHalignment( trainingURL, HPos.CENTER );
        GridPane.setHalignment( buttonHolder, HPos.CENTER );

        trainingName.getStyleClass().add( "title" );
        trainingName.setStyle( "-fx-alignment: CENTER" );
        trainingDateTime.getStyleClass().add( "title" );
        trainingDateTime.setStyle( "-fx-alignment: CENTER" );
        trainingLocation.getStyleClass().add( "title" );
        trainingLocation.setStyle( "-fx-alignment: CENTER" );
        trainingURL.getStyleClass().add( "title" );
        trainingURL.setStyle( "-fx-alignment: CENTER" );

        gridPane.add( trainingName, 0,1 );
        gridPane.add( trainingDateTime, 0, 2 );
        gridPane.add( trainingLocation, 0, 3 );
        gridPane.add( trainingURL, 0, 4 );

        if ( !( user.getUser().getTeamRole().equals( "Player" ) ) )
            gridPane.add( buttonHolder, 0, 5 );

        rateButton.setOnAction(
                // TODO send rated players and their ratings to database

                event -> {
                    rateListView.getItems().clear();
                    cancelRatingButton.setVisible( true );
                    statsPane.setVisible( false );
                    createTrainingPane.setVisible( false );
                    ratingsPane.setVisible( true );
                    if ( training.getIsRated() )
                    {
                        rateButton.setText( "Edit" );
                        submitRatingButton.setText( "Save changes" );

                        // rateListView.getItems().add( ) );//ToDo db'den RateHBox gelecek arrayi
                    }
                    else
                    {
                        for ( int i = 0; i < 3; i++ )
                        {
                            RateHBox box = new RateHBox( ( user.getUserTeams().get( 0 ).getMembersWithRole("Player").get( i + 7 ) ) );

                            box.setStyle( "-fx-padding: 20" );
                            rateListView.getItems().add( box );
                        }
                    }
                    // ObservableList<TeamMember> ratingList = FXCollections.observableArrayList();
                    /**
                     ObservableList<TeamMember> players =
                     for (  )
                     {
                     rateHBox box = new rateHBox( ( "Player" ) );
                     rateListView.getItems().add( box );
                     }
                     */


                     System.out.println("HAHAha");
                     }
                     );
        deleteButton.setOnAction(
                event -> {

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

    public void adjustAlignments()
    {

    }
    public void viewTrainings( ObservableList<Training> list )
    {
        for ( int i = 1; i < 7; i++ )
        {
            int row = ( ( i / 4 ) + 1 ) * 2 - 1;
            int column = ( 2 * i - 1 ) % 6;

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
                GridPane grid = new GridPane();
                grid.getStyleClass().addAll("mainGrid");
                trainingsGrid.add( grid, column, row );
            }

        }
    }

    public void clearViewTrainings()
    {
        trainingViewHolder.clear();
    }

    public void mainTeamBoxSelected()
    {
        addOtherCheck.setDisable( false );
    }

    public void backButtonPushed( ActionEvent actionEvent )
    {
        if ( trainingCounter > 0 )
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


    public void nextButtonPushed( ActionEvent actionEvent )
    {
        trainingCounter++;
        if ( trainingHolder.size() < trainingCounter + 7 )
        {
            nextButton.setDisable(true);
        }

        backButton.setDisable( false );
        clearViewTrainings();
        viewTrainings( trainingHolder );
    }
}