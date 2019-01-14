package com.example.rog.umk.Adapter;

public class newsAdapterList {
    private String name;
    private String desc;
    private String pImg;
    private String siteUrl;
    public newsAdapterList(String name, String pImg, String siteUrl, String desc) {
        this.pImg = pImg;
        this.siteUrl = siteUrl;
        this.desc = desc;
        this.name = name;
    }

    public String getpImg(){
        return pImg;
    }
    public String getDesc(){
        return desc;
    }
    public String getUrl() {
        return siteUrl;
    }
    public String getName() {
        return name;
    }
}
