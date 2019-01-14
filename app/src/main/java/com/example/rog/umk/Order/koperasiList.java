package com.example.rog.umk.Order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.koperasiAdapter;
import com.example.rog.umk.Adapter.newsAdapter;
import com.example.rog.umk.Adapter.newsAdapterList;
import com.example.rog.umk.R;
import com.example.rog.umk.koperasiAdapterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class koperasiList extends AppCompatActivity {
    RecyclerView recyclerView;
    List<koperasiAdapterList> productList;
    String id;
    String name;
    String pName;
    Toolbar mActionBarToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi_list);
        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("List of Seller");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        pName = bundle.getString("name");
        System.out.println("name: " + pName);
        loadNews();
    }
    private void loadNews() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/koperasiList.php?name=" + pName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList.add(new koperasiAdapterList(
                                        name = product.getString("name"),
                                        product.getString("img"),
                                        product.getString("tel"),
                                        product.getString("sellerEmail"),
                                        product.getString("id")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            koperasiAdapter adapter = new koperasiAdapter(koperasiList.this, productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
