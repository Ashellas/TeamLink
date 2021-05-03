package models;

import javafx.scene.image.ImageView;

import java.util.Date;

public class Announcement {
    private int announcementId;
    private String title;
    private String description;
    private TeamMember sender;
    private Team team;
    private String clickAction;
    private Date timeSent;
    private boolean isUnread; //TODO how can this be functional
    private ImageView senderProfilePhoto;

    public Announcement(int announcementId, String title, String description, TeamMember sender, Team team, String clickAction, Date timeSent, boolean isUnread) {
        this.announcementId = announcementId;
        this.title = title;
        this.description = description;
        this.sender = sender;
        this.team = team;
        this.clickAction = clickAction;
        this.timeSent = timeSent;
        this.isUnread = isUnread;
        if (sender.getProfilePhoto() != null) {
            this.senderProfilePhoto = new ImageView(sender.getProfilePhoto().getImage());
        }
    }

    public Announcement(String title, String description, TeamMember sender, Team team, String clickAction) {
        this.title = title;
        this.description = description;
        this.sender = sender;
        this.team = team;
        this.clickAction = clickAction;
        this.timeSent = new Date();
        this.isUnread = true;
        if (sender.getProfilePhoto() != null) {
            this.senderProfilePhoto = new ImageView(sender.getProfilePhoto().getImage());
        }
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TeamMember getSender() {
        return sender;
    }

    public Team getTeam() {
        return team;
    }

    public String getClickAction() {
        return clickAction;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public ImageView getSenderProfilePhoto() {
        return senderProfilePhoto;
    }
}

}



