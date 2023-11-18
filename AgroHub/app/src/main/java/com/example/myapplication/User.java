package com.example.myapplication;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String userType;
    private String full_name;
    private String phoneNumber;

    public User() {
        // Default constructor
    }

    // Constructor to initialize User object
    public User(String name, String email, String password, String userType,
                String fullName, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.full_name = fullName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters for User attributes

    // Get and set ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Get and set Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Get and set Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Get and set Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Get and set UserType
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // Get and set Full Name
    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_Name) {
        this.full_name = full_Name;
    }

    // Get and set Phone Number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
