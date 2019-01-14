package com.example.rog.umk.Adapter;

public class orderAdapterKoperasiList {
    private String name;
    private String date;
    private String img, amt, desc, price, id;
    private int id1;
    public orderAdapterKoperasiList(String name, String img, String amt, String id) {
        this.name = name;
        this.amt = amt;
        this.img = img;
        this.id = id;

    }

    public String getName(){
        return name;
    }
    public String getImg(){return img;}
    public String getID() {return id;}
    public String getAmt() {return amt;}
}
