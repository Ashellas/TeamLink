package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Date;

public class Notification {
    private int notificationId;
    private String title;
    private TeamMember sender;
    private TeamMember recipient;
    private String clickAction;
    private Date timeSent;
    private boolean isUnread; //TODO how can this be functional
    private ImageView senderProfilePhoto;

    public Notification(int notificationId, String title, TeamMember sender, TeamMember recipient, String clickAction, Date timeSent, boolean isUnread, Image senderProfilePhoto) {
        this.notificationId = notificationId;
        this.title = title;
        this.sender = sender;
        this.recipient = recipient;
        this.clickAction = clickAction;
        this.timeSent = timeSent;
        this.isUnread = isUnread;
        this.senderProfilePhoto = new ImageView(senderProfilePhoto);
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getTitle() {
        return title;
    }

    public TeamMember getSender() {
        return sender;
    }

    public TeamMember getRecipient() {
        return recipient;
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
