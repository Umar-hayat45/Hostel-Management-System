package com.example.oop.model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;

public class Hostel {
    private String hostelName;
    private String address;
    private ObservableList<Student>   students;
    private ObservableList<Room>      rooms;
    private ObservableList<Booking>   bookings;
    private ObservableList<Payment>   payments;
    private ObservableList<Complaint> complaints;
    private ObservableList<Warden>    wardens;

    public Hostel(String hostelName, String address) {
        this.hostelName  = hostelName;
        this.address     = address;
        this.students    = FXCollections.observableArrayList();
        this.rooms       = FXCollections.observableArrayList();
        this.bookings    = FXCollections.observableArrayList();
        this.payments    = FXCollections.observableArrayList();
        this.complaints  = FXCollections.observableArrayList();
        this.wardens     = FXCollections.observableArrayList();
    }

    public void addStudent(Student s)    { students.add(s); }
    public boolean removeStudent(String studentId) {
        return students.removeIf(s -> s.getId().equals(studentId));
    }
    public Student findStudentById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
    public ObservableList<Student> searchStudentsByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public ObservableList<Student> getAllStudents() { return students; }

    public void addRoom(Room r)          { rooms.add(r); }
    public Room findRoomByNumber(String roomNumber) {
        return rooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst().orElse(null);
    }
    public ObservableList<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public ObservableList<Room> getAllRooms()       { return rooms; }

    public void addBooking(Booking b)    { bookings.add(b); }
    public Booking findBookingById(String id) {
        return bookings.stream().filter(b -> b.getBookingId().equals(id)).findFirst().orElse(null);
    }
    public ObservableList<Booking> getBookingsByStudent(String studentId) {
        return bookings.stream().filter(b -> b.getStudentId().equals(studentId))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public ObservableList<Booking> getAllBookings() { return bookings; }

    public void addPayment(Payment p)    { payments.add(p); }
    public ObservableList<Payment> getPaymentsByStudent(String studentId) {
        return payments.stream().filter(p -> p.getStudentId().equals(studentId))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public double getTotalRevenueCollected() {
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }
    public ObservableList<Payment> getAllPayments() { return payments; }

    public void addComplaint(Complaint c) { complaints.add(c); }
    public ObservableList<Complaint> getPendingComplaints() {
        return complaints.stream().filter(c -> c.getStatus().equals("Pending"))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public ObservableList<Complaint> getComplaintsByStudent(String studentId) {
        return complaints.stream().filter(c -> c.getStudentId().equals(studentId))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public ObservableList<Complaint> getAllComplaints() { return complaints; }

    public void addWarden(Warden w)       { wardens.add(w); }
    public Warden authenticate(String username, String password) {
        return wardens.stream().filter(w -> w.authenticate(username, password)).findFirst().orElse(null);
    }
    public ObservableList<Warden> getAllWardens()   { return wardens; }

    public String getHostelName()         { return hostelName; }
    public String getAddress()            { return address; }
    public void setHostelName(String n)   { this.hostelName = n; }
    public void setAddress(String a)      { this.address = a; }

    @Override
    public String toString() {
        return hostelName + " | " + address
                + " | Students: " + students.size()
                + " | Rooms: " + rooms.size();
    }
}
 