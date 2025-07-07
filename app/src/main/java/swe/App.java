package swe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Connection conn = Database.getConnection();
        String message;

        if (conn != null) {
            message = "Database connection successful!";
        } else {
            message = "Database connection failed.";
        }

        Label label = new Label(message);

        Scene scene = new Scene(label, 400, 200);
        primaryStage.setTitle("Time Tracking App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}