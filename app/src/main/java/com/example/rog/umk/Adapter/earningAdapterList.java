package com.example.rog.umk.Adapter;

public class earningAdapterList {
    private String date;
    private String activity, price;
    public earningAdapterList(String date, String activity, String price) {
        this.date = date;
        this.activity = activity;
        this.price = price;

    }

    public String getPrice() {
        price = "RM" + price;
        return price;
    }
    public String getDate(){
        return date;
    }
    public String getActivity() {
        return activity;
    }

}
