package com.example.oop.controller;

import com.example.oop.Main;
import com.example.oop.model.Booking;
import com.example.oop.model.Room;
import com.example.oop.model.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class RoomController {

    @FXML private ComboBox<String> filterStatus;
    @FXML private TableView<Room>  roomTable;
    @FXML private TableColumn<Room, String> colRoomNo;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, String> colBlock;
    @FXML private TableColumn<Room, String> colFloor;
    @FXML private TableColumn<Room, String> colCapacity;
    @FXML private TableColumn<Room, String> colOccupied;
    @FXML private TableColumn<Room, String> colPrice;
    @FXML private TableColumn<Room, String> colRoomStatus;
    @FXML private TableColumn<Room, Void>   colRoomActions;

    @FXML private Label totalRoomsCard;
    @FXML private Label vacantCard;
    @FXML private Label partialCard;
    @FXML private Label fullCard;

    @FXML
    public void initialize() {
        filterStatus.setItems(FXCollections.observableArrayList("All", "Vacant", "Partially Occupied", "Full"));
        filterStatus.setValue("All");
        filterStatus.setOnAction(e -> applyFilter());

        colRoomNo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRoomNumber()));
        colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
        colBlock.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBlock()));
        colFloor.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getFloor())));
        colCapacity.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCapacity())));
        colOccupied.setCellValueFactory(d -> new SimpleStringProperty(
                (d.getValue().getCapacity() - d.getValue().getAvailableSlots()) + "/" + d.getValue().getCapacity()));
        colPrice.setCellValueFactory(d -> new SimpleStringProperty("Rs." + (int) d.getValue().getPricePerMonth()));
        colRoomStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));

        colRoomStatus.setCellFactory(col -> new TableCell<Room, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item) {
                    case "Vacant":             setStyle("-fx-text-fill: #4ade80; -fx-font-weight: bold;"); break;
                    case "Partially Occupied": setStyle("-fx-text-fill: #facc15; -fx-font-weight: bold;"); break;
                    case "Full":               setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold;"); break;
                    default:                   setStyle("-fx-text-fill: #ffffff;"); break;
                }
            }
        });

        colRoomActions.setCellFactory(col -> new TableCell<Room, Void>() {
            private final Button allocateBtn = new Button("Allocate");
            private final Button editBtn     = new Button("Edit");
            private final Button deleteBtn   = new Button("Delete");
            private final HBox   box         = new HBox(5, allocateBtn, editBtn, deleteBtn);

            {
                allocateBtn.setStyle("-fx-background-color: #4ade80; -fx-text-fill: #1a1a2e; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px; -fx-font-weight: bold;");
                editBtn.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px;");
                deleteBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px;");

                allocateBtn.setOnAction(e -> {
                    Room r = getTableView().getItems().get(getIndex());
                    showAllocateDialog(r);
                });
                editBtn.setOnAction(e -> {
                    Room r = getTableView().getItems().get(getIndex());
                    showEditDialog(r);
                });
                deleteBtn.setOnAction(e -> {
                    Room r = getTableView().getItems().get(getIndex());
                    handleDelete(r);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        roomTable.setRowFactory(tv -> {
            TableRow<Room> row = new TableRow<>();
            row.styleProperty().bind(
                    javafx.beans.binding.Bindings.when(row.selectedProperty())
                            .then("-fx-background-color: #0f3460;")
                            .otherwise("-fx-background-color: #16213e;")
            );
            return row;
        });

        roomTable.setItems(Main.getHostel().getAllRooms());
        updateCards();
    }

    private void showAllocateDialog(Room room) {
        if (!room.isAvailable()) {
            showAlert(Alert.AlertType.WARNING, "Room " + room.getRoomNumber() + " is Full!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Allocate Room: " + room.getRoomNumber());
        dialog.setHeaderText("Room: " + room.getRoomNumber()
                + "  |  Type: " + room.getType()
                + "  |  Available Slots: " + room.getAvailableSlots());

        ButtonType allocateBtn = new ButtonType("Allocate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(allocateBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        TextField tfStudentId = new TextField();
        tfStudentId.setPromptText("e.g. S001");

        TextField tfBookingId = new TextField();
        tfBookingId.setPromptText("e.g. B001");

        DatePicker dpCheckIn  = new DatePicker(LocalDate.now());
        DatePicker dpCheckOut = new DatePicker(LocalDate.now().plusMonths(6));

        Label statusMsg = new Label("");
        statusMsg.setStyle("-fx-text-fill: #e94560;");

        grid.addRow(0, new Label("Student ID:"), tfStudentId);
        grid.addRow(1, new Label("Booking ID:"), tfBookingId);
        grid.addRow(2, new Label("Check-In:"),   dpCheckIn);
        grid.addRow(3, new Label("Check-Out:"),  dpCheckOut);
        grid.add(statusMsg, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(grid);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(allocateBtn);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String studentId = tfStudentId.getText().trim();
            String bookingId = tfBookingId.getText().trim();

            if (studentId.isEmpty() || bookingId.isEmpty()) {
                statusMsg.setText("Please fill all fields!");
                event.consume();
                return;
            }
            if (Main.getHostel().findStudentById(studentId) == null) {
                statusMsg.setText("Student ID not found!");
                event.consume();
                return;
            }
            if (Main.getHostel().findBookingById(bookingId) != null) {
                statusMsg.setText("Booking ID already exists!");
                event.consume();
                return;
            }
            if (!dpCheckOut.getValue().isAfter(dpCheckIn.getValue())) {
                statusMsg.setText("Check-out must be after Check-in!");
                event.consume();
                return;
            }
            if (room.getOccupantIds().contains(studentId)) {
                statusMsg.setText("Student already in this room!");
                event.consume();
            }
        });

        dialog.setResultConverter(bt -> {
            if (bt == allocateBtn) {
                String studentId = tfStudentId.getText().trim();
                String bookingId = tfBookingId.getText().trim();

                room.addOccupant(studentId);

                Student student = Main.getHostel().findStudentById(studentId);
                if (student != null) student.setRoomNumber(room.getRoomNumber());

                Booking booking = new Booking(bookingId, studentId, room.getRoomNumber(),
                        dpCheckIn.getValue(), dpCheckOut.getValue());
                Main.getHostel().addBooking(booking);

                roomTable.setItems(Main.getHostel().getAllRooms());
                updateCards();
                showAlert(Alert.AlertType.INFORMATION,
                        "Room " + room.getRoomNumber() + " allocated to " + studentId + " successfully!");
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    public void showAddRoomDialog() {
        Dialog<Room> dialog = buildRoomDialog(null);
        dialog.showAndWait().ifPresent(r -> {
            Main.getHostel().addRoom(r);
            roomTable.setItems(Main.getHostel().getAllRooms());
            updateCards();
        });
    }

    private void showEditDialog(Room room) {
        Dialog<Room> dialog = buildRoomDialog(room);
        dialog.showAndWait().ifPresent(updated -> {
            room.setType(updated.getType());
            room.setBlock(updated.getBlock());
            room.setFloor(updated.getFloor());
            room.setCapacity(updated.getCapacity());
            room.setPricePerMonth(updated.getPricePerMonth());
            roomTable.refresh();
            updateCards();
        });
    }

    private void handleDelete(Room room) {
        if (!room.getOccupantIds().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cannot delete room " + room.getRoomNumber()
                    + " — remove occupants first.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete room " + room.getRoomNumber() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                Main.getHostel().getAllRooms().remove(room);
                roomTable.setItems(Main.getHostel().getAllRooms());
                updateCards();
            }
        });
    }

    @FXML
    private void applyFilter() {
        String selected = filterStatus.getValue();
        ObservableList<Room> all = Main.getHostel().getAllRooms();
        if (selected == null || selected.equals("All")) {
            roomTable.setItems(all);
        } else {
            roomTable.setItems(all.stream()
                    .filter(r -> r.getStatus().equals(selected))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        }
    }

    private Dialog<Room> buildRoomDialog(Room existing) {
        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Room" : "Edit Room");
        dialog.setHeaderText(null);

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tfNumber   = new TextField(existing != null ? existing.getRoomNumber() : "");
        ComboBox<String> cbType = new ComboBox<>(FXCollections.observableArrayList("Single", "Double", "Triple"));
        cbType.setValue(existing != null ? existing.getType() : "Single");
        TextField tfBlock    = new TextField(existing != null ? existing.getBlock() : "");
        TextField tfFloor    = new TextField(existing != null ? String.valueOf(existing.getFloor()) : "1");
        TextField tfCapacity = new TextField(existing != null ? String.valueOf(existing.getCapacity()) : "1");
        TextField tfPrice    = new TextField(existing != null ? String.valueOf(existing.getPricePerMonth()) : "");

        if (existing != null) tfNumber.setDisable(true);

        grid.addRow(0, new Label("Room No.:"), tfNumber);
        grid.addRow(1, new Label("Type:"),     cbType);
        grid.addRow(2, new Label("Block:"),    tfBlock);
        grid.addRow(3, new Label("Floor:"),    tfFloor);
        grid.addRow(4, new Label("Capacity:"), tfCapacity);
        grid.addRow(5, new Label("Price/Mo:"), tfPrice);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(bt -> {
            if (bt == saveBtn) {
                try {
                    return new Room(
                            tfNumber.getText().trim(),
                            cbType.getValue(),
                            Integer.parseInt(tfCapacity.getText().trim()),
                            Double.parseDouble(tfPrice.getText().trim()),
                            tfBlock.getText().trim(),
                            Integer.parseInt(tfFloor.getText().trim())
                    );
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private void updateCards() {
        ObservableList<Room> all = Main.getHostel().getAllRooms();
        totalRoomsCard.setText(String.valueOf(all.size()));
        vacantCard.setText(String.valueOf(all.stream().filter(r -> r.getStatus().equals("Vacant")).count()));
        partialCard.setText(String.valueOf(all.stream().filter(r -> r.getStatus().equals("Partially Occupied")).count()));
        fullCard.setText(String.valueOf(all.stream().filter(r -> r.getStatus().equals("Full")).count()));
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }

    public void refresh() {
        roomTable.setItems(Main.getHostel().getAllRooms());
        updateCards();
    }
}