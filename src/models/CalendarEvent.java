package models;

import java.util.Date;

public class CalendarEvent {
    private int calendarEventId;
    private String eventTitle;
    private Date eventDateTime;
    private String description;
    private String actionLink;
    private String colorCode;

    public CalendarEvent(int calendarEventId, String eventTitle, Date eventDateTime, String description, String actionLink, String colorCode) {
        this.calendarEventId = calendarEventId;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.description = description;
        this.actionLink = actionLink;
        this.colorCode = colorCode;
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

    public String getDescription() {
        return description;
    }

    public String getActionLink() {
        return actionLink;
    }

    public String getColorCode() {
        return colorCode;
    }
}
