package models;

import java.util.Date;

public class Training extends CalendarEvent{
    //TODO trainingId is needed or not
    private String trainingLocationName;
    private String trainingLocationLink;
    private Team team;
    private boolean isRated;
    //I deleted TrainingPerformance from here


    public Training(int calendarEventId, String eventTitle, Date eventDateTime, String actionLink, String colorCode, String trainingLocationName, String trainingLocationLink, Team team, boolean isRated ) {
        super(calendarEventId, eventTitle, eventDateTime, actionLink, colorCode );
        this.trainingLocationName = trainingLocationName;
        this.trainingLocationLink = trainingLocationLink;
        this.team = team;
        this.isRated = isRated;
    }

    public Training( String eventTitle, Date eventDateTime, String trainingLocationName, String trainingLocationLink, Team team, boolean isRated )
    {
        super( eventTitle, eventDateTime );
        this.trainingLocationName = trainingLocationName;
        this.trainingLocationLink = trainingLocationLink;
        this.team = team;
        this.isRated = isRated;
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

    public boolean getIsRated()
    {
        return isRated;
    }

}
