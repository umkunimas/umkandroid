package com.example.rog.umk.Product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rog.umk.Adapter.cartAdapterList;
import com.example.rog.umk.Adapter.reviewAdapterList;
import com.example.rog.umk.R;

import java.util.ArrayList;
import java.util.List;

public class cart extends AppCompatActivity {
    RecyclerView recyclerView;
    List<cartAdapterList> reviewList;
    String[] arrayid;
   String[] arrayamt;
     String[] arrayname;
     String[] arrayimg;
     String[] arrayprice;
    static int arrayCounter = 0;
    public cart(){
        System.out.println("here");
        arrayid = new String[100];
        arrayamt = new String[100];
        arrayname= new String[100];
        arrayimg = new String[100];
        arrayprice = new String[100];
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewList = new ArrayList<>();
    }
    public void getArray(){

    }
}
