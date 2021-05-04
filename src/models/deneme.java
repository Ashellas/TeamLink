package models;

import models.CalendarEvent;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class deneme {
    public static void main(String[] args) throws SQLException, ParseException {
        Connection databaseConnection = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
        Date date = new GregorianCalendar(2021, Calendar.APRIL, 11).getTime();
        Timestamp ts=new Timestamp(date.getTime());
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("select * from calendar_events where DATE_FORMAT(event_date_time, \"%m-%Y\") = DATE_FORMAT(?, \"%m-%Y\")");
        preparedStatement.setTimestamp(1, ts);
        ResultSet calendarEventsResultSet = preparedStatement.executeQuery();

        //same for every team
        while (calendarEventsResultSet.next()){
            int eventId = calendarEventsResultSet.getInt("id");
            String title = calendarEventsResultSet.getString("title");
            Date eventDate = calendarEventsResultSet.getDate("event_date_time");
            String actionLink = calendarEventsResultSet.getString("action_link");
            calendarEvents.add(new CalendarEvent(eventId, title, eventDate, actionLink, "calendarEvent"));
        }


        preparedStatement = databaseConnection.prepareStatement("select * from league_games join league_teams lt on lt.league_team_id = league_games.away_team_id" +
                " AND DATE_FORMAT(game_date_time, \"%m-%Y\") = DATE_FORMAT(?, \"%m-%Y\") and (home_team_id = ? or away_team_id = ?)");
        preparedStatement.setTimestamp(1, ts);
        preparedStatement.setInt(2, 4);
        preparedStatement.setInt(3, 4);
        ResultSet gamesResultSet = preparedStatement.executeQuery();

        while (gamesResultSet.next()){
            int gameId = gamesResultSet.getInt("game_id");
            String title = "VS " ;
            Date eventDate = gamesResultSet.getDate("game_date_time");
            String actionLink = "/views/LeagueScreen.fxml";
            calendarEvents.add(new CalendarEvent(gameId, title, eventDate, actionLink, "game"));
        }

        preparedStatement = databaseConnection.prepareStatement("select * from trainings " +
                "where DATE_FORMAT(training_date_time, \"%m-%Y\") = DATE_FORMAT(?, \"%m-%Y\") and team_id = ?");
        preparedStatement.setTimestamp(1, ts);
        preparedStatement.setInt(2, 1);
        ResultSet trainingsResultSet = preparedStatement.executeQuery();
        while (trainingsResultSet.next()){
            int gameId = trainingsResultSet.getInt("training_id");
            String title = trainingsResultSet.getString("title");
            Date eventDate = trainingsResultSet.getDate("training_date_time");
            String actionLink = "/views/TrainingsScreen.fxml";
            calendarEvents.add(new CalendarEvent(gameId, title, eventDate, actionLink, "green"));
        }

        for (CalendarEvent ce : calendarEvents){
            System.out.println(ce.getEventTitle() + ce.getEventDateTime());
        }
    }
}
