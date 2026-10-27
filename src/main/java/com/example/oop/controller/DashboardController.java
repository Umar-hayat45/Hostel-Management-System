package com.example.oop.controller;

import com.example.oop.Main;
import com.example.oop.model.Hostel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label totalComplaintsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private AnchorPane contentArea;
    @FXML private VBox dashboardHomeContent; // must match fx:id="dashboardHomeContent" in Dashboard.fxml

    @FXML private Button btnDashboard;
    @FXML private Button btnStudents;
    @FXML private Button btnRooms;
    @FXML private Button btnPayments;
    @FXML private Button btnComplaints;

    @FXML
    public void initialize() {
        updateStats();
    }

    public void updateStats() {
        Hostel hostel = Main.getHostel();
        totalStudentsLabel.setText(String.valueOf(hostel.getAllStudents().size()));
        totalRoomsLabel.setText(String.valueOf(hostel.getAllRooms().size()));
        totalComplaintsLabel.setText(String.valueOf(hostel.getPendingComplaints().size()));
        totalRevenueLabel.setText("Rs." + (int) hostel.getTotalRevenueCollected());
    }

    private void setActiveButton(Button active) {
        String inactive = "-fx-background-color: #0f3460; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
        String activeStyle = "-fx-background-color: #e94560; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
        btnDashboard.setStyle(inactive);
        btnStudents.setStyle(inactive);
        btnRooms.setStyle(inactive);
        btnPayments.setStyle(inactive);
        btnComplaints.setStyle(inactive);
        active.setStyle(activeStyle);
    }

    @FXML
    public void showDashboardHome() {
        setActiveButton(btnDashboard);
        updateStats();
        // FIX: restore the original home panel instead of loading Dashboard.fxml again
        contentArea.getChildren().clear();
        AnchorPane.setTopAnchor(dashboardHomeContent, 0.0);
        AnchorPane.setBottomAnchor(dashboardHomeContent, 0.0);
        AnchorPane.setLeftAnchor(dashboardHomeContent, 0.0);
        AnchorPane.setRightAnchor(dashboardHomeContent, 0.0);
        contentArea.getChildren().add(dashboardHomeContent);
    }

    @FXML
    public void showStudents() {
        setActiveButton(btnStudents);
        loadView("StudentManagement.fxml");
    }

    @FXML
    public void showRooms() {
        setActiveButton(btnRooms);
        loadView("RoomManagement.fxml");
    }

    @FXML
    public void showPayments() {
        setActiveButton(btnPayments);
        loadView("Payment.fxml");
    }

    @FXML
    public void showComplaints() {
        setActiveButton(btnComplaints);
        loadView("Complaint.fxml");
    }
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/oop/view/" + fxmlFile));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            contentArea.getChildren().add(view);
            updateStats();

        } catch (Exception e) {
            System.out.println("Could not load: " + fxmlFile);
        }
    }
    @FXML
    public void handleLogout() {
        try {
            Main.showLoginScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}