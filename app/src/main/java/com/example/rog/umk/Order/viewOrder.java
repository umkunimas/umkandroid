package com.example.rog.umk.Order;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.rog.umk.Product.addOrder;
import com.example.rog.umk.Product.productDetail;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class viewOrder extends AppCompatActivity implements View.OnClickListener{

    String id, email;
    SharedPreferences prefs;
    String seller, product, img, amt, total, price, orderID, status, date;
    Button cancel;
    TextView seller1, product1, amt1, total1, price1, orderID1, status1, date1, buyer;
    ImageView img1;
    String buyer1;
    com.android.volley.RequestQueue requestQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_order);
        prefs = PreferenceManager.getDefaultSharedPreferences(viewOrder.this);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        email = prefs.getString("username", "none");
        seller1 = findViewById(R.id.name);
        product1 = findViewById(R.id.product);
        amt1 = findViewById(R.id.amt);
        total1 = findViewById(R.id.total);
        price1 = findViewById(R.id.price);
        buyer = findViewById(R.id.buyer);
        orderID1 = findViewById(R.id.order);
        status1 = findViewById(R.id.status);
        date1 = findViewById(R.id.date);
        img1 = findViewById(R.id.img);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(viewOrder.this);
        displayOrder();
    }

    private void displayOrder(){
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        System.out.println("Enter loadproducts");
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/viewOrder.php?id=" + id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                //String seller, product, img, amt, total, price, orderID, status, date;
                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                seller = response1.getString("seller");
                                product = response1.getString("product");
                                img = response1.getString("img");
                                amt = response1.getString("amt");
                                total = response1.getString("total");
                                price = response1.getString("price");
                                orderID = response1.getString("orderID");
                                status = response1.getString("status");
                                date = response1.getString("date");
                                buyer1 = response1.getString("name");
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
        //String seller, product, img, amt, total, price, orderID, status, date;
        Glide.with(this)
                .load(img)
                .transition(withCrossFade())
                .into(img1);

        seller1.setText(seller);
        product1.setText(product);
        amt1.setText(amt);
        total1.setText(total);
        price1.setText(price);
        orderID1.setText(orderID);
        status1.setText(status);
        date1.setText(date);
        buyer.setText(buyer1);
        if (status.equals("Not Complete"))
            cancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == cancel){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            cancelOrder();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
    }

    private void cancelOrder(){
        String UPLOAD_URL;
        UPLOAD_URL = "https://umk-jkms.com/mobile/cancelOrder.php";

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if (ServerResponse.equals("success")){
                            Toast.makeText(viewOrder.this, "Your order has been cancel", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent (viewOrder.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(viewOrder.this, "No internet connection", Toast.LENGTH_LONG).show();
                            System.out.println("ss: " + ServerResponse);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(viewOrder.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("id", id);
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(viewOrder.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("here at onPause");
        finish();
    }
}
