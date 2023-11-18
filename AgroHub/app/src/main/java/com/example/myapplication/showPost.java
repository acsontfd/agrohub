package com.example.myapplication;

public class showPost {

    String serviceText;
    float ratingStar;


    public showPost(String line1, float line2){
        serviceText = line1;
        ratingStar = line2;
    }

    public String getServiceText() {
        return serviceText;
    }

    public float getRatingStar() {
        return ratingStar;
    }
}


