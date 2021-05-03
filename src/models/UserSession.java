package models;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class UserSession {

    private final Preferences preferences = Preferences.userRoot();


    private TeamMember user;
    private ArrayList<Team> userTeams;
    private HashMap<Team,ObservableList<Game>> gamesOfTheCurrentRound;
    private HashMap<Team,ObservableList<Team>> standings;//It can be designed better
    private ArrayList<Notification> notifications;
    private HashMap<Team, ArrayList<CalendarEvent>> calendarEvents;
    private ObservableList<Training> trainings;
    private Connection databaseConnection;
    private ObservableList<TeamApplication> teamApplications;
    private HashMap<Team, ArrayList<Gameplan>> gameplans;
    private Date lastSync;
    private String styleSheet;

    public UserSession(TeamMember user, ArrayList<Team> userTeams, HashMap<Team,ObservableList<Game>>  gamesOfTheCurrentRound, HashMap<Team,ObservableList<Team>> standings, ArrayList<Notification> notifications, HashMap<Team, ArrayList<CalendarEvent>> calendarEvents, ObservableList<Training> trainings, Connection databaseConnection, ObservableList<TeamApplication> teamApplications, HashMap<Team, ArrayList<Gameplan>> gameplans, Date lastSync) {
        this.user = user;
        this.userTeams = userTeams;
        this.gamesOfTheCurrentRound = gamesOfTheCurrentRound;
        this.standings = standings;
        this.notifications = notifications;
        this.calendarEvents = calendarEvents;
        this.trainings = trainings;
        this.databaseConnection = databaseConnection;
        this.teamApplications = teamApplications;
        this.gameplans = gameplans;
        this.lastSync = lastSync;
        styleSheet = preferences.get("stylesheet", getClass().getResource("/stylesheets/LightTheme.css").toExternalForm());
    }

    public UserSession(Connection connection){
        this.databaseConnection = connection;
        styleSheet = preferences.get("stylesheet", getClass().getResource("/stylesheets/LightTheme.css").toExternalForm());
    }

    public TeamMember getUser() {
        return user;
    }

    public ArrayList<Team> getUserTeams() {
        return userTeams;
    }

    public ObservableList<Game> getGamesOfTheCurrentRound(Team team) {
        return gamesOfTheCurrentRound.get(team);
    }

    public ObservableList<Team> getStandings(Team team) {
        return standings.get(team);
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public ArrayList<CalendarEvent> getCalendarEvents(Team team) {
        return calendarEvents.get(team);
    }

    public ObservableList<Training> getTrainings() {
        return trainings;
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }

    public ObservableList<TeamApplication> getTeamApplications() {
        return teamApplications;
    }

    //TODO it can be modified similar to getStandings() method
    public HashMap<Team, ArrayList<Gameplan>> getGameplans() {
        return gameplans;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public String getStyleSheet() {
        return styleSheet;
    }

    public void setUser(TeamMember user) {
        this.user = user;
    }

    public void setUserTeams(ArrayList<Team> userTeams) {
        this.userTeams = userTeams;
    }

    public void setGamesOfTheCurrentRound(HashMap<Team, ObservableList<Game>> gamesOfTheCurrentRound) {
        this.gamesOfTheCurrentRound = gamesOfTheCurrentRound;
    }

    public void setStandings(HashMap<Team, ObservableList<Team>> standings) {
        this.standings = standings;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setTrainings(ObservableList<Training> trainings) {
        this.trainings = trainings;
    }

    public void setDatabaseConnection(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void setTeamApplications(ObservableList<TeamApplication> teamApplications) {
        this.teamApplications = teamApplications;
    }

    public void setGameplans(HashMap<Team, ArrayList<Gameplan>> gameplans) {
        this.gameplans = gameplans;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public void setStyleSheet(String styleSheet) {
        preferences.put("stylesheet", styleSheet);
        this.styleSheet = styleSheet;
    }

    public boolean isStyleDark() {
        if (styleSheet.equals(getClass().getResource("/stylesheets/DarkTheme.css").toExternalForm())) {
            return true;
        }
        return false;
    }
}
