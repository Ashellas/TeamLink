package models;

import java.util.Date;

public class Training extends CalendarEvent{
    //TODO trainingId is needed or not
    private String trainingLocationName;
    private String trainingLocationLink;
    private Team team;
    //TODO do we need Notes here?
    private String notes;
    //I deleted TrainingPerformance from here


    public Training(int calendarEventId, String eventTitle, Date eventDateTime, String description, String actionLink, String colorCode, String trainingLocationName, String trainingLocationLink, Team team, String notes) {
        super(calendarEventId, eventTitle, eventDateTime, description, actionLink, colorCode);
        this.trainingLocationName = trainingLocationName;
        this.trainingLocationLink = trainingLocationLink;
        this.team = team;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }
}
