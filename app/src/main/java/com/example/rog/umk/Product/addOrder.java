package com.example.rog.umk.Product;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView desc, name, totalPrice, quantity, titleBox;
    ImageView img;
    Button minus, add, proceed;
    String email = "", usrType, titleName;
    SharedPreferences prefs;
    com.android.volley.RequestQueue requestQueue;
    int counter;
    String orderID;
    private SQLiteDatabase db;
    private Cursor cursor;
    boolean isLogin;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("isLogin", false);
        desc = findViewById(R.id.desc);
        name = findViewById(R.id.name);
        totalPrice = findViewById(R.id.total);
        quantity = findViewById(R.id.quantity);
        counter = 1;
        usrType = prefs.getString("type","none");
        counters = Integer.toString(counter);
        img = findViewById(R.id.thumbnail);
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        email = prefs.getString("username", "none");
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        minus.setOnClickListener(this);
        add.setOnClickListener(this);
        proceed = findViewById(R.id.proceed);
        requestQueue = Volley.newRequestQueue(addOrder.this);
        proceed.setOnClickListener(this);
        if (usrType.equals("koperasi")){
            orderID = bundle.getString("orderID");
        }
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
        if (v == proceed) {
            if (isLogin) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                if (usrType.equals("koperasi")) {
                                    System.out.println("here koperasi");
                                    postOrderKoperasi();
                                }
                                else
                                    postOrder();
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
            else{
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.custombox, null);
                titleName = "Enter Your Contact Number";
                titleBox = promptsView.findViewById(R.id.title);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                titleBox.setText(titleName);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.input);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        email = userInput.getText().toString();
                                        postOrder();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        }

    }

    private void postOrderKoperasi() {
        String UPLOAD_URL;
        UPLOAD_URL = "https://umk-jkms.com/mobile/postOrder.php?type=koperasi";
        System.out.println("at koeprasi");
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
                            System.out.println("koperasi: " + ServerResponse);
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
                System.out.println("orderId is" + orderID);
                params.put("quantity", counters);
                params.put("total", totalPrice.getText().toString());
                params.put("email", email);
                params.put("id", id);
                params.put("orderID", orderID);
                if (!isLogin)
                    params.put("tag", "guest");
                else
                    params.put("tag", "member");
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(addOrder.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    private void updateQuantity(){
        prices = parseDouble(price1);
        prices = prices*counter;
        totalPrice.setText(prices.toString());
    }
    public void postOrder(){

        String UPLOAD_URL;
        UPLOAD_URL = "https://umk-jkms.com/mobile/postOrder.php?type=other";
        System.out.println("at other");
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
                            System.out.println("other: " + ServerResponse);
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

                params.put("quantity", counters);
                params.put("total", totalPrice.getText().toString());
                params.put("email", email);
                params.put("id", id);
                if (!isLogin)
                    params.put("tag", "guest");
                else
                    params.put("tag", "member");
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(addOrder.this);

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
