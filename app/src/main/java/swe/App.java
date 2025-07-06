package swe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);

        Button loginBtn = new Button("Open Login Page");
        Button adminDashboardBtn = new Button("Open Admin Dashboard");
        Button employeeDashboardBtn = new Button("Open Employee Dashboard");
        Button punchViewBtn = new Button("Open Punch View");
        Button payrollViewBtn = new Button("Open Payroll View");

        loginBtn.setOnAction(e -> loadFXML("views/login.fxml", primaryStage));
        adminDashboardBtn.setOnAction(e -> loadFXML("views/admin_dashboard.fxml", primaryStage));
        employeeDashboardBtn.setOnAction(e -> loadFXML("views/employee_dashboard.fxml", primaryStage));
        punchViewBtn.setOnAction(e -> loadFXML("views/punch_view.fxml", primaryStage));
        payrollViewBtn.setOnAction(e -> loadFXML("views/payroll_view.fxml", primaryStage));

        root.getChildren().addAll(
            loginBtn,
            adminDashboardBtn,
            employeeDashboardBtn,
            punchViewBtn,
            payrollViewBtn
        );

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Testing Pages");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadFXML(String path, Stage stage) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/" + path)));
            stage.setScene(scene);
            stage.setTitle("Loaded: " + path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}