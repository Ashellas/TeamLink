package models;

import java.util.Date;

public class CalendarEvent {
    private int calendarEventId;
    private String eventTitle;
    private Date eventDateTime;
    private String actionLink;
    private String colorCode;

    public CalendarEvent(int calendarEventId, String eventTitle, Date eventDateTime, String actionLink, String colorCode) {
        this.calendarEventId = calendarEventId;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.actionLink = actionLink;
        this.colorCode = colorCode;
    }
    public CalendarEvent( String eventTitle, Date eventDateTime)
    {
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
    }

    public void setCalendarEventId(int calendarEventId) {
        this.calendarEventId = calendarEventId;
    }

    public int getCalendarEventId() {
        return calendarEventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public String getActionLink() {
        return actionLink;
    }

    public String getColorCode() {
        return colorCode;
    }
}

// Birthdays, National Holidays, Training, Match