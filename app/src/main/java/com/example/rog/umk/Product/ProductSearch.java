package com.example.rog.umk.Product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.rog.umk.Helper.Space;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class ProductSearch extends AppCompatActivity implements View.OnClickListener{
    ProductAdapter productsAdapter;
    List<ProductAdapterList> productList;
    String pName;
    RecyclerView recyclerView;
    Spinner spn;
    Button search;
    String search1;
    String indi;
    TextView blank;
    String URL_PRODUCTS;
    String spnNumber;
    int spnNumber1=0;
    RecyclerView recyclerViewProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        pName = bundle.getString("search");
        indi = bundle.getString("indi");
        //Bind RecyclerView from layout to recyclerViewProducts object
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        productList = new ArrayList<>();
        //Create new ProductsAdapter
        productsAdapter = new ProductAdapter(this, "no");
        //Create new GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewProducts.setLayoutManager(gridLayoutManager);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        blank = findViewById(R.id.blank);
        spn  = (Spinner)findViewById(R.id.spn);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays1, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn.setAdapter(adapter);
        if (indi.equals("2"))
            URL_PRODUCTS = "https://umk-jkms.com/mobile/tag1.php?division=null&search="+pName;
        else {
            String div = bundle.getString("division");
            spnNumber = bundle.getString("spinner");
            spnNumber1 = Integer.parseInt(spnNumber);
            spn.setSelection(spnNumber1);
            URL_PRODUCTS = "https://umk-jkms.com/mobile/tag1.php?search="+pName+"&division=" + div;

        }
        if (!productsAdapter.loading)
            feedData();

        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(productsAdapter);

    }

    //Load Data from your server here
    // loading data from server will make it very large
    // that's why i created data locally
    private void feedData() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        productList.clear();
        productsAdapter.showLoading();
        System.out.println("url is: " + URL_PRODUCTS);
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
                            if (array.length() > 0) {
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    //adding the product to product list
                                    productList.add(new ProductAdapterList(
                                            product.getString("name"),
                                            product.getString("img"),
                                            product.getString("price"),
                                            product.getString("location"),
                                            product.getInt("id")
                                    ));
                                }
                            }
                            else{

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
        if (v==search){
            search1 = spn.getSelectedItem().toString();
            System.out.println("string1" + search1);
            Intent intent = new Intent (this, ProductSearch.class);
            intent.putExtra("indi","1");
            intent.putExtra("division", search1);
            intent.putExtra("search", pName);
            String s = Integer.toString(spn.getSelectedItemPosition());
            intent.putExtra("spinner", s);
            ProductSearch.this.finish();
            startActivity(intent);
        }
    }
}
