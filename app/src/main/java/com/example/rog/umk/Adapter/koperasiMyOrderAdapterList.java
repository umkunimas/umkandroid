package com.example.rog.umk.Adapter;

public class koperasiMyOrderAdapterList {
    private String name;
    private String amt;
    private String pImg;
    private String contact;
    private String date;
    private String id, sEmail;
    private String qty;
    String status;
    String sellerName;
    public koperasiMyOrderAdapterList(String name, String pImg, String contact, String sellerName, String id, String date, String amt, String qty, String sEmail, String status) {
        this.pImg = pImg;
        this.contact = contact;
        this.name = name;
        this.sellerName = sellerName;
        this.id = id;
        this.amt = amt;
        this.date = date;
        this.qty = qty;
        this.sEmail = sEmail;
        this.status = status;
    }

    public String getpImg(){
        return pImg;
    }
    public String getContact(){
        return contact;
    }
    public String getAmount() {
        return amt;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getId() { return id;}
    public String getQty() { return qty;}
    public String getSellerName() {return sellerName;}
    public String getsEmail(){return sEmail;}
    public String getStatus() { return status;}
}
