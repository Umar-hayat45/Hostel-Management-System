package com.example.oop.controller;

import com.example.oop.Main;
import com.example.oop.model.Warden;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        // Add a default warden for testing
        Warden testWarden = new Warden("W001", "Mr. Ahmed", 45, "ahmed@hostel.com",
                "03001234567", "admin", "1234", "Block A");
        Main.getHostel().addWarden(testWarden);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password!");
            errorLabel.setVisible(true);
            return;
        }

        Warden warden = Main.getHostel().authenticate(username, password);

        if (warden != null) {
            try {
                Main.showDashboard();
            } catch (Exception e) {
                errorLabel.setText("Error loading dashboard!");
                errorLabel.setVisible(true);
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password!");
            errorLabel.setVisible(true);
            passwordField.clear();
        }
    }
}