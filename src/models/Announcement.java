package models;

import javafx.scene.image.ImageView;

import java.util.Date;

/**
 * Model class to hold announcement
 */
public class Announcement {
    private int announcementId;
    private String title;
    private String description;
    private TeamMember sender;
    private Date timeSent;
    private ImageView senderProfilePhoto;

    /**
     * Creates announcement with all information
     * @param announcementId id
     * @param title announcement title
     * @param description description
     * @param sender user who sends the announcement
     * @param timeSent time that the announcement was sent
     */
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

    /**
     * Creates announcements with given input and gives initial value to time sent
     * @param title title
     * @param description description
     * @param sender user who sends the announcement
     */
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

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    public ImageView getSenderProfilePhoto() {
        return senderProfilePhoto;
    }
}





