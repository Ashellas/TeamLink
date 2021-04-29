import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.stage.StageStyle;

import java.awt.*;

import javafx.stage.StageStyle;
import models.InitializeData;
import models.UserSession;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;


public class Main extends Application {
//aasd
    @Override
    public void start(Stage primaryStage) throws Exception{
        Connection databaseConnection = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
        UserSession userSession = new UserSession(databaseConnection);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/TeamCreationScreen.fxml"));
        loader.load();
        InitializeData c2 = loader.getController();
        c2.initData(userSession);
        Parent p = loader.getRoot();
        primaryStage.setScene(new Scene(p));
        primaryStage.setTitle("TeamLink");
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(1100);
        primaryStage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        System.out.println(screenBounds);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
