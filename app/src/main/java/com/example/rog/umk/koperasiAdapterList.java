package com.example.rog.umk;

public class koperasiAdapterList {
    private String name;
    private String amt;
    private String pImg;
    private String contact;
    private String date;
    private String id;
    String sellerEmail;
    public koperasiAdapterList(String name, String pImg, String contact, String sellerEmail, String id) {
        this.pImg = pImg;
        this.contact = contact;
        this.name = name;
        this.sellerEmail = sellerEmail;
        this.id = id;
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
    public String getSellerEmail() {return sellerEmail;}
}