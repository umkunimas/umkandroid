package com.example.rog.umk.Adapter;

public class MessageAdapterList {
    private String msg, img, date, name, email;

    public MessageAdapterList(String msg, String name, String img, String date, String email) {
        this.msg = msg;
        this.img = img;
        this.date = date;
        this.name = name;
        this.email = email;
    }

    public String getMsg(){
        return msg;
    }
    public String getImg(){
        return img;
    }
    public String getDate(){
        return date;
    }
    public String getEmail() { return email;}
    public String getName(){
        return name;
    }
}
