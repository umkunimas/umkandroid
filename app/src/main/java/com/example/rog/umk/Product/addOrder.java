package com.example.rog.umk.Product;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static java.lang.Double.parseDouble;

public class addOrder extends AppCompatActivity implements View.OnClickListener {

    String name1, desc1, price1, img1, id, counters;
    Double prices = 0.0;
    TextView desc, name, totalPrice, quantity;
    ImageView img;
    Button minus, add, proceed;
    String email = "";
    SharedPreferences prefs;
    com.android.volley.RequestQueue requestQueue;
    int counter;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        desc = findViewById(R.id.desc);
        name = findViewById(R.id.name);
        totalPrice = findViewById(R.id.total);
        quantity = findViewById(R.id.quantity);
        counter = 1;

        counters = Integer.toString(counter);
        img = findViewById(R.id.thumbnail);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        email = prefs.getString("username", "none");
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        minus.setOnClickListener(this);
        add.setOnClickListener(this);
        proceed = findViewById(R.id.proceed);
        requestQueue = Volley.newRequestQueue(addOrder.this);
        proceed.setOnClickListener(this);

        loadProducts();
    }

    private void loadProducts() {


        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        System.out.println("Enter loadproducts");
        System.out.println("addorder id: " + id);
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/productDetail.php?id=" + id + "&tag=order";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);

                                name1 = response1.getString("productName");
                                desc1 = response1.getString("desc");
                                price1 = response1.getString("price");
                                img1 = response1.getString("pimg");

                                loadFromSite();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        System.out.println("Exit loadProduct");
    }
    private void loadFromSite(){
        desc.setText(desc1);
        name.setText(name1);
        totalPrice.setText(price1);
        quantity.setText(counters);
        loadImg();
    }

    private void loadImg(){
        Glide.with(this)
                .load(img1)
                .transition(withCrossFade())
                .into(img);
    }

    @Override
    public void onClick(View v) {
        if (v == minus){
            if (counter == 1){
                counter = 1;
                quantity.setText(counters);
                updateQuantity();

            }
            else{
                counter--;
                counters = Integer.toString(counter);
                quantity.setText(counters);
                updateQuantity();
            }
        }
        if (v == add){
            counter++;
            counters = Integer.toString(counter);
            quantity.setText(counters);
            updateQuantity();
        }
        if (v == proceed){
            postOrder();
        }

    }


    private void updateQuantity(){
        prices = parseDouble(price1);
        prices = prices*counter;
        totalPrice.setText(prices.toString());
    }
    public void postOrder(){

        String UPLOAD_URL;
        UPLOAD_URL = "https://umk-jkms.com/mobile/postOrder.php";

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if (ServerResponse.equals("success")){
                            Toast.makeText(addOrder.this, "Your order has been record", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent (addOrder.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(addOrder.this, "No internet connection", Toast.LENGTH_LONG).show();
                            System.out.println("ss: " + ServerResponse);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(addOrder.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                System.out.println("quantity: " + counters);
                System.out.println("total" + totalPrice.getText().toString());
                System.out.println("email" + email);
                System.out.println("id" + id);
                params.put("quantity", counters);
                params.put("total", totalPrice.getText().toString());
                params.put("email", email);
                params.put("id", id);
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(addOrder.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
}
