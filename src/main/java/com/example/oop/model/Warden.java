package com.example.oop.model;

public class Warden extends Person {
    private String username;
    private String password;
    private String assignedBlock;

    public Warden(String id, String name, int age, String email, String phone,
                  String username, String password, String assignedBlock) {
        super(id, name, age, email, phone);
        this.username = username;
        this.password = password;
        this.assignedBlock = assignedBlock;
    }

    @Override
    public String getRole() {
        return "Warden";
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Getters
    public String getUsername()     { return username; }
    public String getPassword()     { return password; }
    public String getAssignedBlock(){ return assignedBlock; }

    // Setters
    public void setUsername(String username)         { this.username = username; }
    public void setPassword(String password)         { this.password = password; }
    public void setAssignedBlock(String assignedBlock){ this.assignedBlock = assignedBlock; }

    @Override
    public String toString() {
        return super.toString() + " | Block: " + assignedBlock;
    }
}
