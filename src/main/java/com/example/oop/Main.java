package com.example.oop;
import com.example.oop.model.Hostel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Hostel hostel = new Hostel("Green Valley Hostel", "123 Main Street, Lahore");
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginScreen();
    }

    public static void showLoginScreen() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/oop/view/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hostel Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showDashboard() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/oop/view/Dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hostel Management System - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static Hostel getHostel() {
        return hostel;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
