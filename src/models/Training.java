package models;

import java.util.Date;

public class Training extends CalendarEvent{
    //TODO trainingId is needed or not
    private String trainingLocationName;
    private String trainingLocationLink;
    private Team team;

    //I deleted TrainingPerformance from here


    public Training(int calendarEventId, String eventTitle, Date eventDateTime, String description, String actionLink, String colorCode, String trainingLocationName, String trainingLocationLink, Team team) {
        super(calendarEventId, eventTitle, eventDateTime, description, actionLink, colorCode);
        this.trainingLocationName = trainingLocationName;
        this.trainingLocationLink = trainingLocationLink;
        this.team = team;
    }

    public String getTrainingLocationName() {
        return trainingLocationName;
    }

    public String getTrainingLocationLink() {
        return trainingLocationLink;
    }

    public Team getTeam() {
        return team;
    }

}
