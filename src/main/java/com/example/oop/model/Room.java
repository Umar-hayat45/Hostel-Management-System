package com.example.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomNumber;
    private String type;        // "Single", "Double", "Triple"
    private int capacity;
    private double pricePerMonth;
    private String block;
    private int floor;
    private List<String> occupantIds; // Student IDs

    public Room(String roomNumber, String type, int capacity,
                double pricePerMonth, String block, int floor) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.pricePerMonth = pricePerMonth;
        this.block = block;
        this.floor = floor;
        this.occupantIds = new ArrayList<>();
    }

    public boolean isAvailable() {
        return occupantIds.size() < capacity;
    }

    public int getAvailableSlots() {
        return capacity - occupantIds.size();
    }

    public boolean addOccupant(String studentId) {
        if (isAvailable()) {
            occupantIds.add(studentId);
            return true;
        }
        return false; // Room is full
    }

    public boolean removeOccupant(String studentId) {
        return occupantIds.remove(studentId);
    }

    public String getStatus() {
        if (occupantIds.isEmpty()) return "Vacant";
        if (isAvailable()) return "Partially Occupied";
        return "Full";
    }

    // Getters
    public String getRoomNumber()     { return roomNumber; }
    public String getType()           { return type; }
    public int getCapacity()          { return capacity; }
    public double getPricePerMonth()  { return pricePerMonth; }
    public String getBlock()          { return block; }
    public int getFloor()             { return floor; }
    public List<String> getOccupantIds() { return occupantIds; }

    // Setters
    public void setRoomNumber(String roomNumber)    { this.roomNumber = roomNumber; }
    public void setType(String type)               { this.type = type; }
    public void setCapacity(int capacity)          { this.capacity = capacity; }
    public void setPricePerMonth(double price)     { this.pricePerMonth = price; }
    public void setBlock(String block)             { this.block = block; }
    public void setFloor(int floor)               { this.floor = floor; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " | " + type + " | Block " + block
                + " | Status: " + getStatus() + " | Rs." + pricePerMonth + "/month";
    }
}
