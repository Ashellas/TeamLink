package controllers;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Controls the loading window and its animation
 */
public class LoadingScreenController implements Initializable {

    @FXML
    private ImageView ball1;

    @FXML
    private ImageView ball2;

    @FXML
    private ImageView ball3;

    @FXML
    private ImageView ball4;

    @FXML
    private ImageView ball5;

    @FXML
    private ImageView ball6;

    @FXML
    private Label loadingText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rotate(0,360,1,ball1);
        rotate(60,420,1,ball2);
        rotate(120,480,1,ball3);
        rotate(180,540,1,ball4);
        rotate(240,600,1,ball5);
        rotate(300,660,1,ball6);

    }

    /**
     * Creates rotation animation
     * @param startAngle angle that the image starts rotating
     * @param finalAngle angle that the images finished rotating
     * @param duration time for one whole rotation as miliseconds
     * @param node image that will rotate
     */
    private void rotate(int startAngle, int finalAngle, int duration, ImageView node) {
        RotateTransition rt = new RotateTransition(Duration.seconds(duration), node);
        rt.setFromAngle(startAngle);
        rt.setToAngle(finalAngle);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
    }

}
