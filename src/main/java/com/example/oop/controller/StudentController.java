package com.example.oop.controller;

import com.example.oop.Main;
import com.example.oop.model.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField courseField;
    @FXML private ComboBox<Integer> yearCombo;
    @FXML private TextField feesField;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String>  colId;
    @FXML private TableColumn<Student, String>  colName;
    @FXML private TableColumn<Student, Integer> colAge;
    @FXML private TableColumn<Student, String>  colCourse;
    @FXML private TableColumn<Student, Integer> colYear;
    @FXML private TableColumn<Student, String>  colRoom;
    @FXML private TableColumn<Student, Double>  colFees;
    @FXML private TableColumn<Student, Double>  colPaid;
    @FXML private TableColumn<Student, String>  colStatus;

    private static final String DARK_BG    = "#1a1a2e";
    private static final String DARKER_BG  = "#16213e";
    private static final String BLUE       = "#0f3460";
    private static final String TEXT_WHITE = "#ffffff";
    private static final String TEXT_MUTED = "#a8a8b3";

    @FXML
    public void initialize() {
        // Style yearCombo
        yearCombo.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        yearCombo.setValue(1);
        yearCombo.setStyle(
                "-fx-background-color: " + BLUE + ";" +
                        "-fx-text-fill: " + TEXT_WHITE + ";" +
                        "-fx-border-color: #2a4a7f;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
        yearCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
                setStyle("-fx-background-color: " + DARKER_BG + "; -fx-text-fill: " + TEXT_WHITE + "; -fx-font-size: 13px;");
            }
        });
        yearCombo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.valueOf(item));
                setStyle("-fx-text-fill: " + TEXT_WHITE + "; -fx-background-color: transparent;");
            }
        });

        // Cell value factories
        colId    .setCellValueFactory(new PropertyValueFactory<>("id"));
        colName  .setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge   .setCellValueFactory(new PropertyValueFactory<>("age"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colYear  .setCellValueFactory(new PropertyValueFactory<>("year"));
        colRoom  .setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colFees  .setCellValueFactory(new PropertyValueFactory<>("totalFees"));
        colPaid  .setCellValueFactory(new PropertyValueFactory<>("feesPaid"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // White text on all columns
        setWhiteTextCell(colId);
        setWhiteTextCell(colName);
        setWhiteTextCell(colCourse);
        setWhiteTextCell(colRoom);
        setWhiteTextCell(colStatus);
        setWhiteTextCellGeneric(colAge);
        setWhiteTextCellGeneric(colYear);
        setWhiteTextCellGeneric(colFees);
        setWhiteTextCellGeneric(colPaid);

        // Style the table rows
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setStyle("-fx-background-color: " + DARKER_BG + "; -fx-text-fill: " + TEXT_WHITE + ";");
            row.selectedProperty().addListener((obs, was, isNow) ->
                    row.setStyle(isNow
                            ? "-fx-background-color: " + BLUE + "; -fx-text-fill: " + TEXT_WHITE + ";"
                            : "-fx-background-color: " + DARKER_BG + "; -fx-text-fill: " + TEXT_WHITE + ";"));
            return row;
        });

        studentTable.setItems(Main.getHostel().getAllStudents());

        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });
    }

    private <T> void setWhiteTextCell(TableColumn<Student, String> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else { setText(item); setStyle("-fx-text-fill: #ffffff;"); }
            }
        });
    }

    private <T> void setWhiteTextCellGeneric(TableColumn<Student, T> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else { setText(item.toString()); setStyle("-fx-text-fill: #ffffff;"); }
            }
        });
    }

    private void populateFields(Student s) {
        idField.setText(s.getId());
        nameField.setText(s.getName());
        ageField.setText(String.valueOf(s.getAge()));
        emailField.setText(s.getEmail());
        phoneField.setText(s.getPhone());
        courseField.setText(s.getCourse());
        yearCombo.setValue(s.getYear());
        feesField.setText(String.valueOf(s.getTotalFees()));
    }

    @FXML
    public void handleAddStudent() {
        try {
            String id     = idField.getText().trim();
            String name   = nameField.getText().trim();
            String age    = ageField.getText().trim();
            String email  = emailField.getText().trim();
            String phone  = phoneField.getText().trim();
            String course = courseField.getText().trim();
            String fees   = feesField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || age.isEmpty() || email.isEmpty()
                    || phone.isEmpty() || course.isEmpty() || fees.isEmpty()) {
                statusLabel.setText("⚠ Please fill all fields!");
                return;
            }

            if (Main.getHostel().findStudentById(id) != null) {
                statusLabel.setText("⚠ Student ID already exists!");
                return;
            }

            Student student = new Student(id, name, Integer.parseInt(age), email,
                    phone, course, yearCombo.getValue(), Double.parseDouble(fees));
            Main.getHostel().addStudent(student);
            statusLabel.setText("✔ Student added successfully!");
            handleClear();

        } catch (NumberFormatException e) {
            statusLabel.setText("⚠ Age and Fees must be numbers!");
        }
    }
    @FXML
    public void handleUpdateStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select a student to update!");
            return;
        }
        try {
            String name   = nameField.getText().trim();
            String age    = ageField.getText().trim();
            String email  = emailField.getText().trim();
            String phone  = phoneField.getText().trim();
            String course = courseField.getText().trim();
            String fees   = feesField.getText().trim();

            if (name.isEmpty() || age.isEmpty() || email.isEmpty()
                    || phone.isEmpty() || course.isEmpty() || fees.isEmpty()) {
                statusLabel.setText("⚠ Please fill all fields!");
                return;
            }

            selected.setName(name);
            selected.setAge(Integer.parseInt(age));
            selected.setEmail(email);
            selected.setPhone(phone);
            selected.setCourse(course);
            selected.setYear(yearCombo.getValue());
            selected.setTotalFees(Double.parseDouble(fees));

            studentTable.refresh();
            statusLabel.setText("✔ Student updated successfully!");

        } catch (NumberFormatException e) {
            statusLabel.setText("⚠ Age and Fees must be numbers!");
        }
    }
    @FXML
    public void handleDeleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Please select a student to delete!");
            return;
        }
        Main.getHostel().removeStudent(selected.getId());
        statusLabel.setText("✔ Student deleted successfully!");
        handleClear();
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            statusLabel.setText("⚠ Enter a name to search!");
            return;
        }
        studentTable.setItems(Main.getHostel().searchStudentsByName(query));
        statusLabel.setText("Showing results for: " + query);
    }

    @FXML
    public void handleShowAll() {
        studentTable.setItems(Main.getHostel().getAllStudents());
        searchField.clear();
        statusLabel.setText("");
    }

    @FXML
    public void handleClear() {
        idField.clear();
        nameField.clear();
        ageField.clear();
        emailField.clear();
        phoneField.clear();
        courseField.clear();
        feesField.clear();
        yearCombo.setValue(1);
        studentTable.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }
}