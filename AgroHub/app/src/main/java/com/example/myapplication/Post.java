package com.example.myapplication;

public class Post {
    private float rating;
    private String service;
    private int id;
    private String fullName;
    private String username;

    public Post() {
        // Default constructor
    }

    public Post(float rating, String service, String review) {
        this.rating = rating;
        this.service = service;
    }

    // All getters and setters
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String username) {
        this.fullName = fullName;
    }
}
