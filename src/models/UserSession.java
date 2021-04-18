package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UserSession {
    private TeamMember user;
    private ArrayList<Team> userTeams;
    private ArrayList<Game> gamesOfTheCurrentRound;
    private ArrayList<Team> standings;//It can be designed better
    private ArrayList<Notifications> notifications;
    private ArrayList<CalendarEvent> calendarEvents;
    private ArrayList<Training> trainings;
    private Connection databaseConnection;
    private ArrayList<TeamApplication> teamApplications;
    private ArrayList<Gameplan> gameplans;
    private Date lastSync;

    public UserSession(TeamMember user, ArrayList<Team> userTeams, ArrayList<Game> gamesOfTheCurrentRound, ArrayList<Team> standings, ArrayList<Notifications> notifications, ArrayList<CalendarEvent> calendarEvents, ArrayList<Training> trainings, Connection databaseConnection, ArrayList<TeamApplication> teamApplications, ArrayList<Gameplan> gameplans, Date lastSync) {
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

    public ArrayList<Game> getGamesOfTheCurrentRound() {
        return gamesOfTheCurrentRound;
    }

    public ArrayList<Team> getStandings() {
        return standings;
    }

    public ArrayList<Notifications> getNotifications() {
        return notifications;
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public ArrayList<Training> getTrainings() {
        return trainings;
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }

    public ArrayList<TeamApplication> getTeamApplications() {
        return teamApplications;
    }

    public ArrayList<Gameplan> getGameplans() {
        return gameplans;
    }

    public Date getLastSync() {
        return lastSync;
    }
}
