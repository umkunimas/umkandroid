package com.example.rog.umk.Product;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.ProductAdapter;
import com.example.rog.umk.Helper.EndlessScrollListener;
import com.example.rog.umk.Helper.Space;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class Product extends AppCompatActivity implements View.OnClickListener {
    ProductAdapter productsAdapter;
    List<ProductAdapterList> productList;
    RecyclerView recyclerView;
     String URL_PRODUCTS, URL_PRODUCTS1;
     Spinner spn;
     Button search;
     String search1;
     TextView blank, filter;
     int sTag = 0, spnNumber1=0;
     String tag;
     String indi;
     String spnNumber;
    RecyclerView recyclerViewProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString("tag");
        indi = bundle.getString("indi");
        blank = findViewById(R.id.blank);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        filter = findViewById(R.id.filter);
        spn  = (Spinner)findViewById(R.id.spn);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays1, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn.setAdapter(adapter);

        if (!indi.equals("1")) {
            if (tag.equals("Bulk")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag4.php?search=null&division=null";
                sTag = 1;
            }
            if (tag.equals("Product")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag1.php?search=null&division=null";
                sTag = 1;
            }
            if (tag.equals("Food")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag3.php?search=null&division=null";
                sTag = 1;
            }
            if (tag.equals("Service")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag2.php?search=null&division=null";
                sTag = 1;
            }
            if (tag.equals("event")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/eventList.php?search=null";
                spn.setVisibility(GONE);
                search.setVisibility(GONE);
                filter.setVisibility(GONE);
            }
        }
        else {
            URL_PRODUCTS1 = bundle.getString("url");
            spnNumber = bundle.getString("spinner");
            spnNumber1 = Integer.parseInt(spnNumber);
            spn.setSelection(spnNumber1);
            if (tag.equals("Bulk")) {
                URL_PRODUCTS = URL_PRODUCTS1;
                sTag = 1;
            }
            if (tag.equals("Product")) {
                URL_PRODUCTS = URL_PRODUCTS1;
                sTag = 1;
            }
            if (tag.equals("Food")) {
                URL_PRODUCTS = URL_PRODUCTS1;
                sTag = 1;
            }
            if (tag.equals("Service")) {
                URL_PRODUCTS = URL_PRODUCTS1;
                sTag = 1;
            }
            if (tag.equals("event")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/eventList.php?search=null";
                spn.setVisibility(GONE);
                search.setVisibility(GONE);
            }
        }
        //Bind RecyclerView from layout to recyclerViewProducts object

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        productList = new ArrayList<>();
        //Create new ProductsAdapter
        productsAdapter = new ProductAdapter(this, tag);
        System.out.println("productadapter declation");
        //Create new GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewProducts.setLayoutManager(gridLayoutManager);
        if (!productsAdapter.loading)
            feedData(URL_PRODUCTS);

        //add on on Scroll listener
        //recyclerViewProducts.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(productsAdapter);
    }
    public String rTag(){
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString("tag");
        return tag;
    }
    //Load Data from your server here
    // loading data from server will make it very large
    // that's why i created data locally
    private void feedData(String URL_PRODUCTS) {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        productsAdapter.showLoading();
        productList.clear();
        System.out.println("productlist"+productList.size());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            if (array.length()==0){
                                recyclerViewProducts.setVisibility(GONE);
                                blank.setVisibility(View.VISIBLE);
                                blank.setText("No item Found");
                            }
                            else {
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);
                                    System.out.println("price is" + product.getString("price"));
                                    //adding the product to product list
                                    productList.add(new ProductAdapterList(
                                            product.getString("name"),
                                            product.getString("img"),
                                            product.getString("price"),
                                            product.getString("location"),
                                            product.getInt("id")
                                    ));
                                }

                                //creating adapter object and setting it to recyclerview
                                //ProductAdapter adapter = new ProductAdapter(this, productList);
                                //recyclerView.setAdapter(adapter);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //hide loading
                                        productsAdapter.hideLoading();
                                        //add products to recyclerview
                                        productsAdapter.addProducts(productList);
                                        System.out.println("dine");
                                    }
                                }, 2000);
                            }
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

    @Override
    public void onClick(View v) {
        String URL_PRODUCTS="";
        if (v==search){
            productsAdapter = new ProductAdapter(this, tag);
            search1 = spn.getSelectedItem().toString();
            System.out.println("string1" + search1);
            if (tag.equals("Bulk")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag4.php?search=null&division=" + search1;
                sTag = 1;
            }
            if (tag.equals("Product")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag1.php?search=null&division=" + search1;
                sTag = 1;
                System.out.println("jere product");
            }
            if (tag.equals("Food")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag3.php?search=null&division=" + search1;
                sTag = 1;
            }
            if (tag.equals("Service")) {
                URL_PRODUCTS = "https://umk-jkms.com/mobile/tag2.php?search=null&division=" + search1;
                sTag = 1;
            }
            Intent intent = new Intent (this, Product.class);
            intent.putExtra("indi","1");
            intent.putExtra("url", URL_PRODUCTS);
            intent.putExtra("tag",tag);
            String s = Integer.toString(spn.getSelectedItemPosition());
            intent.putExtra("spinner", s);
            Product.this.finish();
            startActivity(intent);
        }
    }
}
