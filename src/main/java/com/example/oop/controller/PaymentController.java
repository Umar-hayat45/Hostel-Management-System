package com.example.oop.controller;

import com.example.oop.Main;
import com.example.oop.model.Hostel;
import com.example.oop.model.Payment;
import com.example.oop.model.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class PaymentController {

    @FXML private TextField searchStudentId;
    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, String> colPayId;
    @FXML private TableColumn<Payment, String> colPayStuId;
    @FXML private TableColumn<Payment, String> colPayAmount;
    @FXML private TableColumn<Payment, String> colPayDate;
    @FXML private TableColumn<Payment, String> colPayMethod;
    @FXML private TableColumn<Payment, String> colPayDesc;
    @FXML private TableColumn<Payment, String> colPayStatus;
    @FXML private TableColumn<Payment, String> colPayRemaining;

    @FXML private Label totalRevenueCard;
    @FXML private Label totalPaymentsCard;

    @FXML
    public void initialize() {
        colPayId       .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPaymentId()));
        colPayStuId    .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStudentId()));
        colPayAmount   .setCellValueFactory(d -> new SimpleStringProperty("Rs." + (int) d.getValue().getAmount()));
        colPayDate     .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPaymentDate().toString()));
        colPayMethod   .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPaymentMethod()));
        colPayDesc     .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        colPayStatus   .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        colPayRemaining.setCellValueFactory(d -> {
            Student stu = Main.getHostel().findStudentById(d.getValue().getStudentId());
            if (stu == null) return new SimpleStringProperty("N/A");
            double remaining = stu.getRemainingFees();
            return new SimpleStringProperty(remaining <= 0 ? "Fully Paid" : "Rs." + (int) remaining);
        });

        // White text on basic columns
        setWhiteTextCell(colPayId);
        setWhiteTextCell(colPayStuId);
        setWhiteTextCell(colPayAmount);
        setWhiteTextCell(colPayDate);
        setWhiteTextCell(colPayMethod);
        setWhiteTextCell(colPayDesc);

        // Status column — color coded
        colPayStatus.setCellFactory(col -> new TableCell<Payment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                try {
                    Payment payment = getTableView().getItems().get(getIndex());
                    if (payment.isOverdue()) {
                        setText("Overdue");
                        setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold; -fx-background-color: #3d1515;");
                    } else if (item.equals("Paid")) {
                        setText("Paid");
                        setStyle("-fx-text-fill: #4ade80; -fx-font-weight: bold;");
                    } else {
                        setText("Pending");
                        setStyle("-fx-text-fill: #facc15; -fx-font-weight: bold;");
                    }
                } catch (Exception e) {
                    setText(item);
                    setStyle("-fx-text-fill: #ffffff;");
                }
            }
        });

        // Remaining fees column — color coded
        colPayRemaining.setCellFactory(col -> new TableCell<Payment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                setStyle(item.equals("Fully Paid")
                        ? "-fx-text-fill: #4ade80; -fx-font-weight: bold;"
                        : "-fx-text-fill: #facc15; -fx-font-weight: bold;");
            }
        });

        styleTable();
        refresh();
    }

    private void setWhiteTextCell(TableColumn<Payment, String> col) {
        col.setCellFactory(column -> new TableCell<Payment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else { setText(item); setStyle("-fx-text-fill: #ffffff;"); }
            }
        });
    }

    @FXML
    public void handleSearch() {
        String id = searchStudentId.getText().trim();
        if (id.isEmpty()) { refresh(); return; }
        paymentTable.setItems(Main.getHostel().getPaymentsByStudent(id));
    }

    @FXML
    public void showAddPaymentDialog() {
        Dialog<Payment> dialog = new Dialog<>();
        dialog.setTitle("Record Payment");
        dialog.setHeaderText(null);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        String fieldStyle = "-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #a8a8b3; -fx-background-radius: 6;";
        String labelStyle = "-fx-text-fill: #a8a8b3;";

        TextField tfPayId  = new TextField(); tfPayId.setPromptText("e.g. PAY001"); tfPayId.setStyle(fieldStyle);
        TextField tfStuId  = new TextField(); tfStuId.setPromptText("e.g. S001");   tfStuId.setStyle(fieldStyle);
        TextField tfAmount = new TextField(); tfAmount.setPromptText("e.g. 5000");  tfAmount.setStyle(fieldStyle);
        TextField tfDesc   = new TextField(); tfDesc.setPromptText("e.g. Monthly rent - June"); tfDesc.setStyle(fieldStyle);

        DatePicker dpDate  = new DatePicker(LocalDate.now());

        ComboBox<String> cbMethod = new ComboBox<>(
                FXCollections.observableArrayList("Cash", "Bank Transfer", "Online"));
        cbMethod.setValue("Cash");

        // Live fee info labels
        Label lblTotalFees     = new Label("Total Fees:    --"); lblTotalFees.setStyle(labelStyle);
        Label lblFeesPaid      = new Label("Fees Paid:     --"); lblFeesPaid.setStyle(labelStyle);
        Label lblRemainingFees = new Label("Remaining:     --");
        lblRemainingFees.setStyle("-fx-text-fill: #facc15; -fx-font-weight: bold;");

        tfStuId.textProperty().addListener((obs, oldVal, newVal) -> {
            Student stu = Main.getHostel().findStudentById(newVal.trim());
            if (stu != null) {
                lblTotalFees.setText("Total Fees:    Rs." + (int) stu.getTotalFees());
                lblFeesPaid .setText("Fees Paid:     Rs." + (int) stu.getFeesPaid());
                double rem = stu.getRemainingFees();
                lblRemainingFees.setText("Remaining:     " + (rem <= 0 ? "Fully Paid ✔" : "Rs." + (int) rem));
                lblRemainingFees.setStyle(rem <= 0
                        ? "-fx-text-fill: #4ade80; -fx-font-weight: bold;"
                        : "-fx-text-fill: #facc15; -fx-font-weight: bold;");
            } else {
                lblTotalFees    .setText("Total Fees:    --");
                lblFeesPaid     .setText("Fees Paid:     --");
                lblRemainingFees.setText("Remaining:     --");
                lblRemainingFees.setStyle("-fx-text-fill: #facc15; -fx-font-weight: bold;");
            }
        });

        Label errorLbl = new Label(); errorLbl.setStyle("-fx-text-fill: #f87171;");

        Label l0 = new Label("Payment ID:");  l0.setStyle(labelStyle);
        Label l1 = new Label("Student ID:");  l1.setStyle(labelStyle);
        Label l2 = new Label("Amount (Rs):"); l2.setStyle(labelStyle);
        Label l3 = new Label("Date:");        l3.setStyle(labelStyle);
        Label l4 = new Label("Method:");      l4.setStyle(labelStyle);
        Label l5 = new Label("Description:"); l5.setStyle(labelStyle);

        grid.addRow(0, l0, tfPayId);
        grid.addRow(1, l1, tfStuId);
        grid.addRow(2, lblTotalFees);
        grid.addRow(3, lblFeesPaid);
        grid.addRow(4, lblRemainingFees);
        grid.addRow(5, l2, tfAmount);
        grid.addRow(6, l3, dpDate);
        grid.addRow(7, l4, cbMethod);
        grid.addRow(8, l5, tfDesc);
        grid.addRow(9, errorLbl);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setStyle("-fx-background-color: #1a1a2e;");

        dialog.getDialogPane().lookupButton(saveBtn).addEventFilter(
                javafx.event.ActionEvent.ACTION, ev -> {
                    String err = validate(tfPayId.getText(), tfStuId.getText(), tfAmount.getText());
                    if (!err.isEmpty()) { errorLbl.setText(err); ev.consume(); }
                });

        dialog.setResultConverter(bt -> {
            if (bt == saveBtn) {
                try {
                    Payment p = new Payment(
                            tfPayId.getText().trim(),
                            tfStuId.getText().trim(),
                            Double.parseDouble(tfAmount.getText().trim()),
                            dpDate.getValue(),
                            cbMethod.getValue(),
                            tfDesc.getText().trim()
                    );
                    Student stu = Main.getHostel().findStudentById(tfStuId.getText().trim());
                    if (stu != null) stu.setFeesPaid(stu.getFeesPaid() + p.getAmount());
                    return p;
                } catch (NumberFormatException ex) { return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(p -> {
            Main.getHostel().addPayment(p);
            refresh();
        });
    }

    private String validate(String payId, String stuId, String amount) {
        if (payId.trim().isEmpty()) return "Payment ID is required.";
        if (stuId.trim().isEmpty()) return "Student ID is required.";
        if (Main.getHostel().findStudentById(stuId.trim()) == null) return "Student ID not found.";
        try { Double.parseDouble(amount.trim()); }
        catch (NumberFormatException e) { return "Amount must be a number."; }
        return "";
    }

    private void styleTable() {
        paymentTable.setRowFactory(tv -> {
            TableRow<Payment> row = new TableRow<>();
            row.styleProperty().bind(
                    javafx.beans.binding.Bindings.when(row.selectedProperty())
                            .then("-fx-background-color: #0f3460;")
                            .otherwise("-fx-background-color: #16213e;")
            );
            return row;
        });
        paymentTable.setStyle("-fx-background-color: #16213e; -fx-border-color: #0f3460;");
    }

    public void refresh() {
        Hostel hostel = Main.getHostel();
        paymentTable.setItems(hostel.getAllPayments());
        totalRevenueCard .setText("Rs." + (int) hostel.getTotalRevenueCollected());
        totalPaymentsCard.setText(String.valueOf(hostel.getAllPayments().size()));
    }
}