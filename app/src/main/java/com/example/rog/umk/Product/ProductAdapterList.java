package com.example.rog.umk.Product;

public class ProductAdapterList {
    private int id;
    private String desc;
    private String image;
    private String eta;
    private String location;
    private String id1;
    private boolean isLoading = false;
    public ProductAdapterList(String desc, String image, String eta, String location, int id) {
        this.desc = desc;
        this.image = image;
        this.eta = eta;
        this.location = location;
        this.id = id;
        id1 = Integer.toString(id);
    }
    public ProductAdapterList() {
    }
    public String getId() {
        return id1;
    }
    public String getPrice(){
        return eta;
    }
    public String getName() {
        return desc;
    }
    public String getLocation(){
        return location;
    }
    public String getImage() {
        return image;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
