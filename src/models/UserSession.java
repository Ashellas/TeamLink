package models;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserSession {
    private TeamMember user;
    private ArrayList<Team> userTeams;
    private HashMap<Team,ObservableList<Game>> gamesOfTheCurrentRound;
    private HashMap<Team,ObservableList<Team>> standings;//It can be designed better
    private ArrayList<Notification> notifications;
    private ArrayList<CalendarEvent> calendarEvents;
    private ObservableList<Training> trainings;
    private Connection databaseConnection;
    private ArrayList<TeamApplication> teamApplications;
    private HashMap<Team, ArrayList<Gameplan>> gameplans;
    private Date lastSync;

    public UserSession(TeamMember user, ArrayList<Team> userTeams, HashMap<Team,ObservableList<Game>>  gamesOfTheCurrentRound, HashMap<Team,ObservableList<Team>> standings, ArrayList<Notification> notifications, ArrayList<CalendarEvent> calendarEvents, ObservableList<Training> trainings, Connection databaseConnection, ArrayList<TeamApplication> teamApplications, HashMap<Team, ArrayList<Gameplan>> gameplans, Date lastSync) {
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
    }

    public UserSession() throws SQLException {
        this.databaseConnection = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
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

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public ObservableList<Training> getTrainings() {
        return trainings;
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }

    public ArrayList<TeamApplication> getTeamApplications() {
        return teamApplications;
    }

    //TODO it can be modified similar to getStandings() method
    public HashMap<Team, ArrayList<Gameplan>> getGameplans() {
        return gameplans;
    }

    public Date getLastSync() {
        return lastSync;
    }
}
