package com.example.rog.umk.Adapter;

public class reviewAdapterList {
    private String name;
    private String date;
    private String review;
    public reviewAdapterList(String name, String date, String review) {
        this.name = name;
        this.date = date;
        this.review = review;
    }

    public String getReview() {
        return review;
    }
    public String getName(){
        return name;
    }
    public String getDate() {
        return date;
    }
}
