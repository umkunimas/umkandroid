package com.example.rog.umk.Adapter;

public class cartAdapterList {
    private String name;
    private String img, amt, desc, price;
    private String id;
    public cartAdapterList(String name, String img, String amt, String price, String id) {
        this.name = name;
        this.amt = amt;
        this.img = img;
        this.price = price;
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public String getPrice() {
        return price;
    }
    public String getImg(){return img;}
    public String getId() {return id;}
    public String getAmt() {return amt;}
}
