package models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

import java.io.IOException;

public class CalendarButton extends Button {
    String actionLink;
    String colorCode;
    UserSession user;
    public CalendarButton(String title, String time, String actionLink, String colorCode, UserSession user) throws IOException {
        this.setText(title + " @" + time);
        this.actionLink = actionLink;
        this.colorCode = colorCode;
        this.user = user;
        this.setOnAction(event -> {
            try {
                AppManager.changeScene(getClass().getResource(actionLink), event, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.setStyle("-fx-background-color:" + colorCode);
        this.setCursor(Cursor.HAND);
    }
}
