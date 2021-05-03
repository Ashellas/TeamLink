package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CalendarScreenController extends MainTemplateController {


    @FXML
    private Label monthName;
    @FXML
    private GridPane calendar;
    @FXML
    private GridPane calendarPane;

    @FXML
    private ComboBox<Team> teamSelectionCombo;

    private int realDay;
    private int realMonth;
    private int realYear;
    private int currentMonth;
    private int currentYear;
    String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private int maxDay;
    private int firstDay;
    private ArrayList<Label> labels = new ArrayList<Label>();;
    private ArrayList<ListView<Button>> lists = new ArrayList<ListView<Button>>();
    private ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
    private ArrayList<Team> teams = new ArrayList<Team>();
    private CalendarEvent e2 = new CalendarEvent(1,"training", new Date(2021, 04, 19, 17,30),"/views/SquadScreen.fxml","lightBlue");
    private Team selectedTeam;

    public void initData(UserSession userSession){
        super.initData(userSession);
        Team allTeams = new Team(99999,"All Teams","",  "");
        teamSelectionCombo.getItems().add(allTeams);
        teams = user.getUserTeams();
        for (Team t: teams) {
            teamSelectionCombo.getItems().add(t);
        }
        selectedTeam = allTeams;
        teamSelectionCombo.setValue(selectedTeam);


        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        GregorianCalendar gc2 = new GregorianCalendar(realYear, realMonth, 1);
        maxDay = gc2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        firstDay = gc2.get(GregorianCalendar.DAY_OF_WEEK);
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;

        monthName.setText(months[currentMonth] + " " + currentYear); //Set month name
        for (int i = 0; i < 42; i++) {
            Label day = new Label("");
            day.setStyle("-fx-font-size: 14");
            labels.add(day);
            ListView<Button> buttonListView = new ListView<Button>();
            lists.add(buttonListView);
            int row = i / 7;
            int column = i % 7;
            GridPane gp = (GridPane) calendar.getChildren().get(i);
            gp.add(day, 0, 0);
            gp.add(buttonListView,0,1);
        }
        events.add(e2);
        createCalendar(firstDay, maxDay, events);
        AppManager.fadeIn(calendarPane,500);
    }

    public void createCalendar (int firstDay, int maxDay, ArrayList<CalendarEvent> events) {
        for (int i = 1; i <= maxDay; i++) {
            int row = (i + firstDay - 2 )/7;
            int column  =  (i + firstDay - 2)%7;
            Label l = labels.get(i + firstDay - 2);
            l.setText("   " + i + "");
            for (CalendarEvent ce: events) {
                if (selectedTeam.getTeamName().equals("All Teams")) { // ce.getDescription().equals(selectedTeam.getAbbrevation()) deleted
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(ce.getEventDateTime());
                    if (i == calendar.get(Calendar.DAY_OF_MONTH) && currentMonth == (Calendar.MONTH + 1)) {
                        ListView<Button> buttonListView = lists.get(i + firstDay - 2);
                        if (ce.getActionLink() != null) {
                            try {
                                buttonListView.getItems().add(new CalendarButton(ce.getEventTitle(), (ce.getEventDateTime().getHours() + "." + ce.getEventDateTime().getMinutes()), ce.getActionLink(), ce.getColorCode(), user));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            buttonListView.getItems().add(new CalendarButton(ce.getEventTitle(), ce.getColorCode(), user));
                        }
                    }
                }
            }
        }
    }

    //if (ce.getActionLink() != null) {
    //                    Calendar calendar = Calendar.getInstance();
    //                    calendar.setTime(ce.getEventDateTime());
    //                    if (i == calendar.get(Calendar.DAY_OF_MONTH) && currentMonth == (Calendar.MONTH + 1)) {
    //                        ListView<Button> buttonListView = lists.get(i + firstDay - 2);
    //                        try {
    //                            buttonListView.getItems().add(new CalendarButton(ce.getEventTitle(), (ce.getEventDateTime().getHours() + "." + ce.getEventDateTime().getMinutes()), ce.getActionLink(), ce.getColorCode(), user));
    //                        } catch (IOException e) {
    //                            e.printStackTrace();
    //                        }
    //                    }
    //                else if (ce.getEventTitle() != null) {
    //                        Calendar calendar1 = Calendar.getInstance();
    //                        calendar1.setTime(ce.getEventDateTime());
    //                        if (i == calendar.get(Calendar.DAY_OF_MONTH) && currentMonth == (Calendar.MONTH + 1)) {
    //                            ListView<Button> buttonListView = lists.get(i + firstDay - 2);
    //                            buttonListView.getItems().add(new CalendarButton(ce.getEventTitle(), ce.getColorCode(), user));
    //                        }
    //                    }
    //                }
    public void clearCalendar() {
        for (Label l: labels) {
            l.setText("");
        }
        for (ListView<Button> b: lists) {
            b.getItems().clear();
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
        firstDay = gc1.get(GregorianCalendar.DAY_OF_WEEK);
        clearCalendar();
        createCalendar(firstDay, maxDay, events);
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
        firstDay = gc1.get(GregorianCalendar.DAY_OF_WEEK);
        clearCalendar();
        createCalendar(firstDay, maxDay, events);
    }

    public void teamSelection(ActionEvent actionEvent) {
        selectedTeam = (Team) teamSelectionCombo.getValue();
        clearCalendar();
        createCalendar(firstDay, maxDay, events);
    }

}
