package com.example.myapplication;

import android.graphics.Bitmap;

public class Post {
    private float rating;
    private String service;
    private String imagePath;
    private int id;
    private String username;

    public Post() {
        // Default constructor
    }

    public Post(float rating, String service, String imagePath) {
        this.rating = rating;
        this.service = service;
        this.imagePath = imagePath;
        this.username = username;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

