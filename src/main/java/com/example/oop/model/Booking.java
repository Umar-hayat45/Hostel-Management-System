package com.example.oop.model;
import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private String studentId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;

    public Booking(String bookingId, String studentId, String roomNumber,
                   LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "Active";
    }

    public long getDurationInMonths() {
        long days = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        return Math.max(1, days / 30);
    }

    public boolean isActive() {
        return status.equals("Active");
    }

    // Getters
    public String getBookingId()       { return bookingId; }
    public String getStudentId()       { return studentId; }
    public String getRoomNumber()      { return roomNumber; }
    public LocalDate getCheckInDate()  { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public String getStatus()          { return status; }

    // Setters
    public void setBookingId(String bookingId)         { this.bookingId = bookingId; }
    public void setStudentId(String studentId)         { this.studentId = studentId; }
    public void setRoomNumber(String roomNumber)       { this.roomNumber = roomNumber; }
    public void setCheckInDate(LocalDate checkInDate)  { this.checkInDate = checkInDate; }
    public void setCheckOutDate(LocalDate checkOutDate){ this.checkOutDate = checkOutDate; }
    public void setStatus(String status)               { this.status = status; }

    @Override
    public String toString() {
        return "Booking " + bookingId + " | Student: " + studentId
                + " | Room: " + roomNumber + " | " + checkInDate + " to " + checkOutDate
                + " | Status: " + status;
    }
}
