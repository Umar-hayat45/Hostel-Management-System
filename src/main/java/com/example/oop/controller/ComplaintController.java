package com.example.oop.controller;

import com.example.oop.Main;
import com.example.oop.model.Complaint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class ComplaintController {

    @FXML private TextField complaintIdField;
    @FXML private TextField studentIdField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea resolutionArea;
    @FXML private Label statusLabel;

    @FXML private TableView<Complaint> complaintTable;
    @FXML private TableColumn<Complaint, String>    colId;
    @FXML private TableColumn<Complaint, String>    colStudentId;
    @FXML private TableColumn<Complaint, String>    colCategory;
    @FXML private TableColumn<Complaint, String>    colDescription;
    @FXML private TableColumn<Complaint, LocalDate> colDate;
    @FXML private TableColumn<Complaint, String>    colStatus;
    @FXML private TableColumn<Complaint, String>    colResolution; // CHANGE 1: New field added

    private static final String DARK_BG    = "#1a1a2e";
    private static final String DARKER_BG  = "#16213e";
    private static final String BLUE       = "#0f3460";
    private static final String TEXT_WHITE = "#ffffff";
    private static final String TEXT_MUTED = "#a8a8b3";

    @FXML
    public void initialize() {
        categoryCombo.setItems(FXCollections.observableArrayList(
                "Maintenance", "Food", "Security", "Other"));
        categoryCombo.setValue("Maintenance");

        categoryCombo.setStyle(
                "-fx-background-color: " + BLUE + ";" +
                        "-fx-text-fill: " + TEXT_WHITE + ";" +
                        "-fx-border-color: #2a4a7f;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
        categoryCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setStyle("-fx-background-color: " + DARKER_BG + "; -fx-text-fill: " + TEXT_WHITE + "; -fx-font-size: 13px;");
            }
        });
        categoryCombo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item);
                setStyle("-fx-text-fill: " + TEXT_WHITE + "; -fx-background-color: transparent;");
            }
        });

        descriptionArea.setStyle(
                "-fx-background-color: " + BLUE + ";" +
                        "-fx-text-fill: " + TEXT_WHITE + ";" +
                        "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-border-color: #2a4a7f;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-control-inner-background: " + BLUE + ";"
        );

        resolutionArea.setStyle(
                "-fx-background-color: " + BLUE + ";" +
                        "-fx-text-fill: " + TEXT_WHITE + ";" +
                        "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-border-color: #2a4a7f;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-control-inner-background: " + BLUE + ";"
        );

        colId         .setCellValueFactory(new PropertyValueFactory<>("complaintId"));
        colStudentId  .setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colCategory   .setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate       .setCellValueFactory(new PropertyValueFactory<>("dateSubmitted"));
        colStatus     .setCellValueFactory(new PropertyValueFactory<>("status"));

        setWhiteTextCell(colId);
        setWhiteTextCell(colStudentId);
        setWhiteTextCell(colCategory);
        setWhiteTextCell(colDescription);
        setWhiteTextCellGeneric(colDate);

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item) {
                    case "Pending"     -> setStyle("-fx-text-fill: #facc15; -fx-font-weight: bold;");
                    case "In Progress" -> setStyle("-fx-text-fill: #60a5fa; -fx-font-weight: bold;");
                    case "Resolved"    -> setStyle("-fx-text-fill: #4ade80; -fx-font-weight: bold;");
                    default            -> setStyle("-fx-text-fill: " + TEXT_WHITE + ";");
                }
            }
        });

        // CHANGE 2: colResolution setup added after colStatus
        colResolution.setCellValueFactory(new PropertyValueFactory<>("resolution"));
        setWhiteTextCell(colResolution);

        complaintTable.setRowFactory(tv -> {
            TableRow<Complaint> row = new TableRow<>();
            row.setStyle("-fx-background-color: " + DARKER_BG + "; -fx-text-fill: " + TEXT_WHITE + ";");
            row.selectedProperty().addListener((obs, was, isNow) ->
                    row.setStyle(isNow
                            ? "-fx-background-color: " + BLUE + "; -fx-text-fill: " + TEXT_WHITE + ";"
                            : "-fx-background-color: " + DARKER_BG + "; -fx-text-fill: " + TEXT_WHITE + ";"));
            return row;
        });

        complaintTable.setItems(Main.getHostel().getAllComplaints());

        complaintTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });
    }

    private void setWhiteTextCell(TableColumn<Complaint, String> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else { setText(item); setStyle("-fx-text-fill: #ffffff;"); }
            }
        });
    }

    private <T> void setWhiteTextCellGeneric(TableColumn<Complaint, T> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else { setText(item.toString()); setStyle("-fx-text-fill: #ffffff;"); }
            }
        });
    }

    private void populateFields(Complaint c) {
        complaintIdField.setText(c.getComplaintId());
        studentIdField.setText(c.getStudentId());
        categoryCombo.setValue(c.getCategory());
        descriptionArea.setText(c.getDescription());
        resolutionArea.setText(c.getResolution());
    }

    @FXML
    public void handleSubmitComplaint() {
        String id          = complaintIdField.getText().trim();
        String studentId   = studentIdField.getText().trim();
        String category    = categoryCombo.getValue();
        String description = descriptionArea.getText().trim();

        if (id.isEmpty() || studentId.isEmpty() || description.isEmpty()) {
            statusLabel.setText("⚠ Please fill all fields!");
            return;
        }

        if (Main.getHostel().findStudentById(studentId) == null) {
            statusLabel.setText("⚠ Student ID not found!");
            return;
        }

        Complaint complaint = new Complaint(id, studentId, category, description);
        Main.getHostel().addComplaint(complaint);
        complaintTable.setItems(Main.getHostel().getAllComplaints());
        statusLabel.setText("✔ Complaint submitted successfully!");
        handleClear();
    }

    @FXML
    public void handleMarkInProgress() {
        Complaint selected = complaintTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Please select a complaint!");
            return;
        }
        selected.markInProgress();
        complaintTable.refresh();
        statusLabel.setText("✔ Marked as In Progress!");
    }

    @FXML
    public void handleResolve() {
        Complaint selected = complaintTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Please select a complaint!");
            return;
        }
        String resolution = resolutionArea.getText().trim();
        if (resolution.isEmpty()) {
            statusLabel.setText("⚠ Please enter a resolution!");
            return;
        }
        selected.resolve(resolution);
        complaintTable.refresh();
        statusLabel.setText("✔ Complaint resolved!");
    }

    @FXML
    public void handleShowAll() {
        complaintTable.setItems(Main.getHostel().getAllComplaints());
        statusLabel.setText("");
    }

    @FXML
    public void handleShowPending() {
        complaintTable.setItems(Main.getHostel().getPendingComplaints());
        statusLabel.setText("Showing: Pending");
    }

    @FXML
    public void handleShowInProgress() {
        ObservableList<Complaint> list = Main.getHostel().getAllComplaints()
                .filtered(c -> c.getStatus().equals("In Progress"));
        complaintTable.setItems(list);
        statusLabel.setText("Showing: In Progress");
    }

    @FXML
    public void handleShowResolved() {
        ObservableList<Complaint> list = Main.getHostel().getAllComplaints()
                .filtered(c -> c.getStatus().equals("Resolved"));
        complaintTable.setItems(list);
        statusLabel.setText("Showing: Resolved");
    }

    @FXML
    public void handleClear() {
        complaintIdField.clear();
        studentIdField.clear();
        descriptionArea.clear();
        resolutionArea.clear();
        categoryCombo.setValue("Maintenance");
        complaintTable.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }
}