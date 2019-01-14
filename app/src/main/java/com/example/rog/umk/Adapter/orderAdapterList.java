package com.example.rog.umk.Adapter;

public class orderAdapterList {
    private String name;
    private String date;
    private String img, amt, buyer, price, id, orderID;
    public orderAdapterList(String name, String img, String amt, String price, String id, String date, String orderID, String buyer) {
        this.name = name;
        this.amt = amt;
        this.img = img;
        this.price = price;
        this.id = id;
        this.date = date;
        this.orderID = orderID;
        this.buyer = buyer;
    }

    public String getDate() { return date;}
    public String getName(){
        return name;
    }
    public String getPrice() {
        return price;
    }
    public String getImg(){return img;}
    public String getId(){return id;}
    public String getAmt() {return amt;}
    public String getOrderID() {return orderID;}
    public String getBuyer() {return buyer;}
}
