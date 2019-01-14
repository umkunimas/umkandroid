package com.example.rog.umk.Adapter;

public class likeAdapterList {
   private String img, id;
    private String name, price;
    public likeAdapterList(String img, String name, String price, String id) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getPrice() {
        price = "RM" + price;
        return price;
    }
    public String getImg(){
        return img;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

}
