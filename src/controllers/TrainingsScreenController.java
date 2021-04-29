package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * Training screen with create a new training button
 */
public class TrainingsScreenController implements Initializable
{
    // buttons
    @FXML
    private Button[] edit;
    @FXML
    private Button delete;
    @FXML
    private Button stats;
    @FXML
    private Button createTraining;


    // tableView for trainings
    @FXML
    private TableView<Training> trainingTable;
    @FXML
    private TableColumn<Training, String> teamsColumn;
    @FXML
    private TableColumn<Training, String> trainingName;
    @FXML
    private TableColumn<Training, String> trainingDates;
    @FXML
    private TableColumn<Training, LocalTime> trainingTime;
    @FXML
    private TableColumn<Training, String> trainingDescription;
    @FXML
    private TableColumn<Training, String> trainingAddress;
    @FXML
    private TableColumn<Training, String> trainingAddressURL;
    @FXML
    private TableColumn<Training, Button[]> trainingEdit;


    // team creation panel
    @FXML
    private TextArea descriptionHolder;
    @FXML
    private TextArea locationHolder;
    @FXML
    private TextArea noteHolder;
    @FXML
    private TextField nameHolder;
    @FXML
    private TextField locationUrlHolder;
    @FXML
    private AnchorPane createTrainingPane;
    @FXML
    private Label createTrainingLabel;
    @FXML
    private Label trainingNameLabel;
    @FXML
    private Label trainingDescriptionLabel;
    @FXML
    private Label trainingDateLabel;
    @FXML
    private Label trainingTimeLabel;
    @FXML
    private Label teamPickerLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label noteLabel;
    @FXML
    private ComboBox teamsBox;

    private ObservableList<String> teamsList = FXCollections.observableArrayList(
            "Team A", "Team B", "Team C");

    private ObservableList<Training> data = FXCollections.observableArrayList();

    @FXML
    private Label locationUrlLabel;
    @FXML
    private DatePicker datePicker;
    //@FXML
    //private JFXTimePicker timePicker;
    @FXML
    private Button submitButton;



    // profile & menu
    @FXML
    private ImageView profilePictureImageView;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Label lastSyncLabel;

    private UserSession user;

    public void initData( UserSession user ) //
    {

        this.user = user;

        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());

        if ( user.getUser().getTeamRole().equals( "Player" ) )
        {
            createTraining.setVisible( false );
        }

        // createTrainingPane.setManaged( false );
        createTrainingPane.setVisible( false );
        createTrainingLabel.setVisible( false );
        locationUrlHolder.setVisible( false );
        locationHolder.setVisible( false );
        locationLabel.setVisible( false );
        locationUrlLabel.setVisible( false );
        trainingDateLabel.setVisible( false );
        trainingDescriptionLabel.setVisible( false );
        trainingNameLabel.setVisible( false );
        trainingTimeLabel.setVisible( false );
    }



    public void statsButtonPushed( ActionEvent event )
    {
        // ToDo open ratings
    }
    public void deleteButtonPushed( ActionEvent event )
    {
        // ToDo delete the current training object
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        createTrainingPane.setVisible( false );
        // diğer panelleri göster
    }


    public void newTraining( ActionEvent actionEvent )
    {
        teamsBox.getItems().addAll( teamsList );
        createTrainingPane.setVisible( true );
        createTrainingLabel.setVisible( true );
        locationUrlHolder.setVisible( true );
        locationHolder.setVisible( true );
        locationLabel.setVisible( true );
        locationUrlLabel.setVisible( true );
        trainingDateLabel.setVisible( true );
        trainingDescriptionLabel.setVisible( true );
        trainingNameLabel.setVisible( true );
        trainingTimeLabel.setVisible( true );
    }

    public void submitTraining( ActionEvent actionEvent )
    {
        /**
         String colorCode;
         if ( teamsBox.getValue().equals( teamsList.get( 1 ) ) )
         colorCode = "#A28DC7";
         else if ( teamsBox.getValue().equals( teamsList.get( 2 ) ) )
         colorCode = "#8DC7C7";
         else if ( teamsBox.getValue().equals( teamsList.get( 3 ) ) )
         colorCode = "#C78DBA";
         else if ( teamsBox.getValue().equals( teamsList.get( 4 ) ) )
         colorCode = "#ABAC7C";
         else
         colorCode = "#A6596C";
         */
        String colorCode = "";
        Team t1 = null; //TODO create a team to debug

        // atTime( timePicker.getValue()

        ZoneOffset zone = ZoneOffset.of( "Z" );
        Date trainingDate = Date.from( ( datePicker.getValue() ).atStartOfDay().toInstant( zone ) ) ;
        String dateOfTraining = datePicker.getValue().toString();

        Training training = new Training( 1, nameHolder.getText(), trainingDate, descriptionHolder.getText()
                , "actionLink", colorCode, locationHolder.getText(), locationUrlHolder.getText()
                , t1 );

        // remove training pane and add stats pane again
        createTrainingPane.setVisible( false );

        data.add( training );
        // add new training to table list
        teamsColumn.setCellValueFactory( new PropertyValueFactory<Training, String>( teamsBox.getValue().toString() ) );
        trainingName.setCellValueFactory( new PropertyValueFactory<Training, String>( nameHolder.getText() ) );
        trainingDates.setCellValueFactory( new PropertyValueFactory<Training, String>( dateOfTraining ) );
        // trainingTime.setCellValueFactory( new PropertyValueFactory<Training, LocalTime>( timePicker.getValue().toString() ) );
        trainingAddress.setCellValueFactory( new PropertyValueFactory<Training, String>( locationHolder.getText() ) );
        trainingAddressURL.setCellValueFactory( new PropertyValueFactory<Training, String>( locationUrlHolder.getText() ) );
        trainingDescription.setCellValueFactory( new PropertyValueFactory<Training, String>( descriptionHolder.getText() ));

        trainingTable.setItems( data );

        System.out.println( "selam");
    }
}
