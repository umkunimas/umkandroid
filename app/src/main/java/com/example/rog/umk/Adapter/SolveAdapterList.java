package com.example.rog.umk.Adapter;

public class SolveAdapterList {
    private int id;
    private String title;
    private String date;
    private String id1;
    public SolveAdapterList(String title, String date, int id) {
        this.title = title;
        this.date = date;
        this.id = id;
        id1 = Integer.toString(id);
    }

    public String getId() {
        return id1;
    }
    public String getTitle(){
        return title;
    }
    public String getDate() {
        return date;
    }
}
