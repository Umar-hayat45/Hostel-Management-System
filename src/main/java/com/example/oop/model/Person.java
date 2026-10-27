package com.example.oop.model;

public abstract class Person {
    private String id;
    private String name;
    private int age;
    private String email;
    private String phone;

    public Person(String id, String name, int age, String email, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public abstract String getRole();

    public String getId()    { return id; }
    public String getName()  { return name; }
    public int getAge()      { return age; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setId(String id)       { this.id = id; }
    public void setName(String name)   { this.name = name; }
    public void setAge(int age)        { this.age = age; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Role: " + getRole();
    }
}
