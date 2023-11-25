package com.example.myapplication;

public class showPost {
    private String serviceText;
    private float ratingStar;
    private String imagePath;

    public showPost(String line1, float line2, String imagePath) {
        serviceText = line1;
        ratingStar = line2;
        this.imagePath = imagePath;
    }

    public String getServiceText() {
        return serviceText;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String setImagePath(){
        this.imagePath = imagePath;
        return null;
    }
}
