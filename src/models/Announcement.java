package models;

import javafx.scene.image.ImageView;

import java.util.Date;

public class Announcement {
    private int announcementId;
    private String title;
    private String description;
    private TeamMember sender;
    private Date timeSent;
    private ImageView senderProfilePhoto;

    public Announcement(int announcementId, String title, String description, TeamMember sender, Date timeSent) {
        this.announcementId = announcementId;
        this.title = title;
        this.description = description;
        this.sender = sender;
        this.timeSent = timeSent;
        if (sender.getProfilePhoto() != null) {
            this.senderProfilePhoto = new ImageView(sender.getProfilePhoto().getImage());
        }
    }

    public Announcement(String title, String description, TeamMember sender) {
        this.title = title;
        this.description = description;
        this.sender = sender;
        this.timeSent = new Date();
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

    public Date getTimeSent() {
        return timeSent;
    }


    public ImageView getSenderProfilePhoto() {
        return senderProfilePhoto;
    }
}





