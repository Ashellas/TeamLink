package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import models.InitializeData;
import models.UserSession;

import java.util.ArrayList;
import java.util.GregorianCalendar;


public class CalendarScreenController implements InitializeData{

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label lastSyncLabel;

    @FXML
    private Label monthName;

    @FXML
    private Label teamName;

    @FXML
    private GridPane calendar;

    private UserSession user;
    private String teamNameText;
    private int realDay;
    private int realMonth;
    private int realYear;
    private int currentMonth;
    private int currentYear;
    String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private int maxDay;
    private int firstDay;
    private ArrayList<Label> labels = new ArrayList<Label>();;

    public void initData(UserSession user){
        //this.user = user;
        //userNameLabel.setText(user.getUser().getFirstName());
        //userRoleLabel.setText(user.getUser().getTeamRole());
        //profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());
        teamName.setText("teamNameText");
        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        GregorianCalendar gc2 = new GregorianCalendar(realYear, realMonth, 1);
        maxDay = gc2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        firstDay = gc2.get(GregorianCalendar.DAY_OF_WEEK);
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;
        monthName.setText(months[currentMonth] + " " + currentYear);
        for (int i = 0; i < 42; i++) {
            Label day = new Label("");
            labels.add(day);
            
            int row = i / 7;
            int column = i % 7;
            GridPane gp = (GridPane) calendar.getChildren().get(i);
            gp.add(day, 0, 0);
        }
        createCalendar(firstDay, maxDay);
    }

    public void createCalendar (int firstDay, int maxDay) {
        for (int i = 1; i <= maxDay; i++) {
            int row = (i + firstDay - 2 )/7;
            int column  =  (i + firstDay - 2)%7;
            Label l = labels.get(i + firstDay - 2);
            l.setText(i + "");
        }
    }

    public void clearCalendar() {
        for (Label l: labels) {
            l.setText("");
        }
    }

    public void backButtonPushed( ActionEvent actionEvent )
    {
        currentMonth--;
        if(currentMonth == -1) {
            currentMonth = 11;
            currentYear--;
        }
        monthName.setText(months[currentMonth] + " " + currentYear);
        GregorianCalendar gc1 = new GregorianCalendar(currentYear, currentMonth, 1);
        maxDay = gc1.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        System.out.println(maxDay);
        firstDay = gc1.get(GregorianCalendar.DAY_OF_WEEK);
        System.out.println(firstDay);
        clearCalendar();
        createCalendar(firstDay, maxDay);
    }

    public void nextButtonPushed( ActionEvent actionEvent )
    {
        currentMonth++;
        if(currentMonth == 12) {
            currentMonth = 0;
            currentYear++;
        }
        monthName.setText(months[currentMonth] + " " + currentYear);
        GregorianCalendar gc1 = new GregorianCalendar(currentYear, currentMonth, 1);
        maxDay = gc1.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        System.out.println(maxDay);
        firstDay = gc1.get(GregorianCalendar.DAY_OF_WEEK);
        System.out.println(firstDay);
        clearCalendar();
        createCalendar(firstDay, maxDay);
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
