package com.example.oop.model;

import java.time.LocalDate;

public class Complaint {
    private String complaintId;
    private String studentId;
    private String category;   // "Maintenance", "Food", "Security", "Other"
    private String description;
    private LocalDate dateSubmitted;
    private String status;     // "Pending", "In Progress", "Resolved"
    private String resolution;

    public Complaint(String complaintId, String studentId,
                     String category, String description) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.category = category;
        this.description = description;
        this.dateSubmitted = LocalDate.now();
        this.status = "Pending";
        this.resolution = "";
    }

    public void resolve(String resolution) {
        this.resolution = resolution;
        this.status = "Resolved";
    }

    public void markInProgress() {
        this.status = "In Progress";
    }

    // Getters
    public String getComplaintId()      { return complaintId; }
    public String getStudentId()        { return studentId; }
    public String getCategory()         { return category; }
    public String getDescription()      { return description; }
    public LocalDate getDateSubmitted() { return dateSubmitted; }
    public String getStatus()           { return status; }
    public String getResolution()       { return resolution; }

    // Setters
    public void setComplaintId(String complaintId)   { this.complaintId = complaintId; }
    public void setStudentId(String studentId)       { this.studentId = studentId; }
    public void setCategory(String category)         { this.category = category; }
    public void setDescription(String description)   { this.description = description; }
    public void setStatus(String status)             { this.status = status; }
    public void setResolution(String resolution)     { this.resolution = resolution; }

    @Override
    public String toString() {
        return "Complaint " + complaintId + " | Student: " + studentId
                + " | " + category + " | Status: " + status + " | Date: " + dateSubmitted;
    }
}