package com.example.rog.umk.Product;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class ProductSearch extends AppCompatActivity {
    ProductAdapter productsAdapter;
    List<ProductAdapterList> productList;
    String pName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        pName = bundle.getString("search");
        //Bind RecyclerView from layout to recyclerViewProducts object
        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
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

        //Crete new EndlessScrollListener fo endless recyclerview loading
        //EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
        //@Override
        //public void onLoadMore(int page, int totalItemsCount) {
        if (!productsAdapter.loading)
            feedData();
        //}
        //};
        // //to give loading item full single row
        /*gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (productsAdapter.getItemViewType(position)) {
                    case ProductAdapter.PRODUCT_ITEM:
                        return 1;
                    case ProductAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });*/
        //add on on Scroll listener
        //recyclerViewProducts.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(productsAdapter);
        //load first page of recyclerview
        // endlessScrollListener.onLoadMore(0, 0);
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
         String URL_PRODUCTS = "https://umk-jkms.com/mobile/tag1.php?search="+pName;
        productsAdapter.showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
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
}
