package com.example.myapplication;

public class ExampleFarm {
    private int mImageResource;
    String mText1;
    String mText2;
    String mText3;


    public ExampleFarm(int imageResource, String text1, String text2, String address){
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = address;

    }

    public int getmImageResource(){
        return mImageResource;
    }

    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return mText2;
    }

    public String getmAddress(){
        return  mText3;
    }

}