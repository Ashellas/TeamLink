package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CalendarScreenController extends MainTemplateController {

    //Declaring FXML's that used in SceneBuilder
    @FXML
    private Label monthName;
    @FXML
    private GridPane calendar;
    @FXML
    private GridPane calendarPane;
    @FXML
    private ComboBox<Team> teamSelectionCombo;
    //---------------------Help Pane---------------------------//
    @FXML
    private GridPane helpPane;
    @FXML
    private Pane darkPane;
    @FXML
    private ImageView helpPaneIcon;

    //Declaring calendar related variables
    private int realDay;
    private int realMonth;
    private int realYear;
    private int currentMonth;
    private int currentYear;
    String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private int maxDay;
    private int firstDay;
    Date date;

    //Declaring calendar view related variables
    private ArrayList<Label> labels = new ArrayList<Label>();;
    private ArrayList<ListView<Button>> lists = new ArrayList<ListView<Button>>();

    //Declaring event view related variables
    private ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
    private ArrayList<CalendarEvent> allEvents = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<Team>();
    private Team selectedTeam;

    public void initData(UserSession userSession){
        //Writing a super statement because CalendarScreenController is extended from MainTemplateController
        super.initData(userSession);

        // Theme selection
        if(user.isStyleDark()) {
            darkIcons();
        }
        else {
            lightIcons();
        }

        darkPane.setDisable(true);
        darkPane.setVisible(false);
        helpPane.setDisable(true);
        helpPane.setVisible(false);

        //Populating team selection combo box & give a starting value to it
        teams = user.getUserTeams();
        for (Team t: teams) {
            teamSelectionCombo.getItems().add(t);
        }
        selectedTeam = user.getUserTeams().get(0);
        teamSelectionCombo.setValue(selectedTeam);

        //Creating calendar
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

        //Creating a label and a listview arraylist to modify day numbers and add event buttons later on
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

        //Setting current month's events
        events = user.getCalendarEvents(selectedTeam);
        allEvents = user.getAllEvents();
        createCalendar(firstDay, maxDay, allEvents);
        AppManager.fadeIn(calendarPane,500);
    }

    /**
     * Main function of the controller. Populates the calendar. If "All Teams" is selected, shows all of the events, if not; just shows the selected one.
     * @param firstDay first day of the month starting from sunday e.g. 3 = Wednesday
     * @param maxDay maximum day count of the month e.g. 30
     * @param events Events list which belongs to that month
     */
    public void createCalendar (int firstDay, int maxDay, ArrayList<CalendarEvent> events) {
        for (int i = 1; i <= maxDay; i++) {
            int row = (i + firstDay - 2 )/7;
            int column  =  (i + firstDay - 2)%7;
            Label l = labels.get(i + firstDay - 2);
            l.setText("   " + i + "");
            for (CalendarEvent ce : events) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ce.getEventDateTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String formattedDate = sdf.format(ce.getEventDateTime());
                if (i == calendar.get(Calendar.DAY_OF_MONTH)) {
                    ListView<Button> buttonListView = lists.get(i + firstDay - 2);
                    if (ce.getActionLink() != null) {
                        try {
                            buttonListView.getItems().add(new CalendarButton(ce.getEventTitle(), formattedDate, ce.getActionLink(), ce.getColorCode(), user));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        buttonListView.getItems().add(new CalendarButton(ce.getEventTitle(), ce.getColorCode(), user));
                    }
                }
            }
        }
    }


    //
    /**
     * Clears the Calendar.
     */
    public void clearCalendar() {
        for (Label l: labels) {
            l.setText("");
        }
        for (ListView<Button> b: lists) {
            b.getItems().clear();
        }
    }

    /**
     * Clears the calendar and creates it's previous month with events.
     * @param actionEvent Clicking "<" button
     */
    public void backButtonPushed( ActionEvent actionEvent ) throws SQLException {
        currentMonth--;
        if(currentMonth == -1) {
            currentMonth = 11;
            currentYear--;
        }


        monthName.setText(months[currentMonth] + " " + currentYear);
        GregorianCalendar gc1 = new GregorianCalendar(currentYear, currentMonth, 1);
        date = gc1.getTime();
        maxDay = gc1.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        firstDay = gc1.get(GregorianCalendar.DAY_OF_WEEK);
        clearCalendar();
        events = DatabaseManager.getCalendarEventByDate(user.getDatabaseConnection(), selectedTeam, date);
        createCalendar(firstDay, maxDay, events);

    }

    /**
     * Clears the calendar and creates it's next month with events.
     * @param actionEvent Clicking ">" button
     */
    public void nextButtonPushed( ActionEvent actionEvent ) throws SQLException {
        currentMonth++;
        if(currentMonth == 12) {
            currentMonth = 0;
            currentYear++;
        }


        monthName.setText(months[currentMonth] + " " + currentYear);
        GregorianCalendar gc1 = new GregorianCalendar(currentYear, currentMonth, 1);
        date = gc1.getTime();
        maxDay = gc1.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        firstDay = gc1.get(GregorianCalendar.DAY_OF_WEEK);
        clearCalendar();
        events = DatabaseManager.getCalendarEventByDate(user.getDatabaseConnection(), selectedTeam, date);
        createCalendar(firstDay, maxDay, events);

    }

    /**
     * Clears the calendar and creates the same month with chosen team's events.
     * @param actionEvent changing the team selection combo box's value
     */
    public void teamSelection(ActionEvent actionEvent) throws SQLException {
        selectedTeam = (Team) teamSelectionCombo.getValue();
        clearCalendar();
        events = DatabaseManager.getCalendarEventByDate(user.getDatabaseConnection(), selectedTeam, date);
        createCalendar(firstDay, maxDay, events);
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
     * Helps initialising the icons according to the chosen team
     */
    public void darkIcons() {
        helpPaneIcon.setImage((new Image("/Resources/Images/white/help_white.png")));
    }

    /**
     * Helps initialising the icons according to the chosen team
     */
    public void lightIcons() {
        helpPaneIcon.setImage((new Image("/Resources/Images/black/help_black.png")));
    }

    @Override
    public void toCalendarScreen(ActionEvent actionEvent) throws IOException {}
}
