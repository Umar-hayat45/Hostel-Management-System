package com.example.oop.model;

import java.time.LocalDate;

public class Payment {
    private String paymentId;
    private String studentId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentMethod; // "Cash", "Bank Transfer", "Online"
    private String description;
    private String status; // "Paid", "Pending", "Overdue"

    public Payment(String paymentId, String studentId, double amount,
                   LocalDate paymentDate, String paymentMethod, String description) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.status = "Paid";
    }

    public boolean isOverdue() {
        return status.equals("Pending") && LocalDate.now().isAfter(paymentDate);
    }

    // Getters
    public String getPaymentId()       { return paymentId; }
    public String getStudentId()       { return studentId; }
    public double getAmount()          { return amount; }
    public LocalDate getPaymentDate()  { return paymentDate; }
    public String getPaymentMethod()   { return paymentMethod; }
    public String getDescription()     { return description; }
    public String getStatus()          { return status; }

    // Setters
    public void setPaymentId(String paymentId)         { this.paymentId = paymentId; }
    public void setStudentId(String studentId)         { this.studentId = studentId; }
    public void setAmount(double amount)               { this.amount = amount; }
    public void setPaymentDate(LocalDate paymentDate)  { this.paymentDate = paymentDate; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setDescription(String description)     { this.description = description; }
    public void setStatus(String status)               { this.status = status; }

    @Override
    public String toString() {
        return "Payment " + paymentId + " | Student: " + studentId
                + " | Rs." + amount + " | " + paymentDate + " | " + status;
    }
}

