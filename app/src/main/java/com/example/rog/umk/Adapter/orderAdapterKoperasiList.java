package com.example.rog.umk.Adapter;

public class orderAdapterKoperasiList {
    private String name;
    private String date;
    private String img, amt, desc, price, id, oID, status;
    private int id1;
    public orderAdapterKoperasiList(String name, String img, String amt, String id, String oID, String status) {
        this.name = name;
        this.amt = amt;
        this.img = img;
        this.id = id;
        this.oID = oID;
        this.status = status;
    }

    public String getName(){
        return name;
    }
    public String getImg(){return img;}
    public String getID() {return id;}
    public String getAmt() {return amt;}
    public String getOrderId(){return oID;}
    public String getStatus() {return status;}
}
