module com.example.oop {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.oop to javafx.fxml;
    opens com.example.oop.controller to javafx.fxml;
    opens com.example.oop.model to javafx.fxml;

    exports com.example.oop;
    exports com.example.oop.controller;
    exports com.example.oop.model;
}