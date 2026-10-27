package com.example.oop.model;
public class Student extends Person {
    private String roomNumber;
    private String course;
    private int year;
    private double totalFees;
    private double feesPaid;
    private String status; // "Active", "Inactive"

    public Student(String id, String name, int age, String email, String phone,
                   String course, int year, double totalFees) {
        super(id, name, age, email, phone);
        this.course = course;
        this.year = year;
        this.totalFees = totalFees;
        this.feesPaid = 0.0;
        this.roomNumber = "Not Assigned";
        this.status = "Active";
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public double getRemainingFees() {
        return totalFees - feesPaid;
    }

    public boolean isFullyPaid() {
        return feesPaid >= totalFees;
    }

    // Getters
    public String getRoomNumber() { return roomNumber; }
    public String getCourse()     { return course; }
    public int getYear()          { return year; }
    public double getTotalFees()  { return totalFees; }
    public double getFeesPaid()   { return feesPaid; }
    public String getStatus()     { return status; }

    // Setters
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setCourse(String course)         { this.course = course; }
    public void setYear(int year)                { this.year = year; }
    public void setTotalFees(double totalFees)   { this.totalFees = totalFees; }
    public void setFeesPaid(double feesPaid)     { this.feesPaid = feesPaid; }
    public void setStatus(String status)         { this.status = status; }

    @Override
    public String toString() {
        return super.toString() + " | Course: " + course + " | Room: " + roomNumber;
    }
}
