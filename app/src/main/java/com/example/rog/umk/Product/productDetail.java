package com.example.rog.umk.Product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Adapter.reviewAdapter;
import com.example.rog.umk.Adapter.reviewAdapterList;
import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Profile.profile;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static java.security.AccessController.getContext;

public class productDetail extends AppCompatActivity implements View.OnClickListener {

    TextView name, location, desc, sellerName, price, blank, tel;
    ImageView  imgV, likeRed, likeWhite, sellerImg;
    CardView likeButton;
    boolean isLike = false;
    static String email;
    Button order;
    ImageButton left, right, submitReview, call, sms, whatsapp;
    SharedPreferences prefs;
    boolean isLogin;
    //pp is profile picture of the seller
    String sn = "",id = "", location1= "", contact1= "", desc1= "", name1= "", currentImg="", priceS="", sImg = "", reviewText= "";
    ArrayList<String> img;
    Toolbar mActionBarToolbar;
    RecyclerView recyclerView;
    List<reviewAdapterList> reviewList;
    EditText writeReview;
    LinearLayout sell;
    int globI = 0;
    int cur = 0;
    String tag;
    String sellerEmail;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        prefs = PreferenceManager.getDefaultSharedPreferences(productDetail.this);
        isLogin = prefs.getBoolean("isLogin", false);
                blank = findViewById(R.id.blank);
            name = findViewById(R.id.pname);
            location = findViewById(R.id.location);
            tel = findViewById(R.id.tel);
            tel.setVisibility(GONE);
            sellerImg = findViewById(R.id.simg);
            desc = findViewById(R.id.desc);
            left = findViewById(R.id.leftButton);
            right = findViewById(R.id.rightButton);
            sellerName = findViewById(R.id.seller);

            sell = findViewById(R.id.sell);

            userType = prefs.getString("type", "none");
            likeRed = findViewById(R.id.likeRed);
            likeWhite = findViewById(R.id.likeWhite);

            order = findViewById(R.id.order);
            order.setOnClickListener(this);
            if (userType.equals("koperasi"))
                order.setVisibility(GONE);
            submitReview = findViewById(R.id.submitReview);
            if (isLogin) {
                submitReview.setOnClickListener(this);

                likeWhite.setOnClickListener(this);
            }
            else {
                submitReview.setVisibility(GONE);
            }
            writeReview = findViewById(R.id.writeReview);
            price = findViewById(R.id.price);
            imgV = findViewById(R.id.img);
            mActionBarToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mActionBarToolbar);
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            isLogin = prefs.getBoolean("isLogin", false);
            img = new ArrayList<String>();
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewList = new ArrayList<>();
            Bundle bundle = getIntent().getExtras();
            id = bundle.getString("id");
            System.out.println("Id is: " + id);
            tag = bundle.getString("tag");
            email = prefs.getString("username", "none");
            System.out.println("islogin: " + isLogin);
            if (isLogin) {
                String URL_PRODUCTS = "https://umk-jkms.com/mobile/like.php?tag=check&id=" + id + "&email=" + email;
                StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("ok")) {
                                    isLike = true;
                                    if (isLike) {
                                        likeRed.setVisibility(View.VISIBLE);
                                        likeWhite.setVisibility(GONE);
                                    } else {
                                        likeWhite.setVisibility(View.VISIBLE);
                                        likeRed.setVisibility(GONE);
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                Volley.newRequestQueue(this).add(jsonObjectRequest);
            }

            // to navigate images
            left.setOnClickListener(this);
            right.setOnClickListener(this);
            System.out.println("type is"+prefs.getString("type","none"));
            loadProducts();
            loadReview();
    }

    private void loadReview() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/review.php?id=" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.equals("[]")){
                            blank.setText("No review for the item");
                            blank.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(GONE);
                        }
                        else {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);

                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject review = array.getJSONObject(i);

                                    //adding the product to product list
                                    reviewList.add(new reviewAdapterList(
                                            review.getString("name"),
                                            review.getString("date"),
                                            review.getString("review")
                                    ));
                                }

                                //creating adapter object and setting it to recyclerview
                                reviewAdapter adapter = new reviewAdapter(productDetail.this, reviewList);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        System.out.println("Enter loadproducts");
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/productDetail.php?id=" + id + "&tag=detail";
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
                                location1 = response1.getString("loc");
                                contact1 = response1.getString("cont");
                                sn = response1.getString("sName");
                                sImg = response1.getString("img");
                                priceS = response1.getString("price");

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

    private void loadFromSite()
    {
        System.out.println("Enter loadFromSite");
        location.setText(location1);
        desc.setText(desc1);
        sellerName.setText(sn);
        price.setText("RM"+priceS);
        name.setText(name1);
        tel.setText(contact1);
        Glide.with(this)
                .load(sImg)
                .transition(withCrossFade())
                .into(sellerImg);
        getSupportActionBar().setTitle(name1);
        System.out.println(location1);
        System.out.println(desc1);
        System.out.println(sn);
        System.out.println(priceS);
        System.out.println(name1);
        loadImg();
    }

    private void loadImg(){
        System.out.println("Enter loadproImg");
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/productDetail.php?id=" + id + "&tag=d";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                img.add(response1.getString("pimg"));
                                globI++;
                            }
                            currentImg = img.get(0);
                            System.out.println("current img is: " + currentImg);
                            loadImgProd();
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
    }
    public void loadImgProd(){
        Glide.with(this)
                .load(currentImg)
                .transition(withCrossFade())
                .into(imgV);
    }
    @Override
    public void onClick(View v) {

        if (v==sell){
            System.out.println("Enter loadproducts");
            String URL_PRODUCTS = "https://umk-jkms.com/mobile/getSellerEmail.php?id=" + id;
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject response1 = array.getJSONObject(i);
                                    sellerEmail = response1.getString("email");
                                    Intent intent = new Intent (productDetail.this, profile.class);
                                    intent.putExtra("sellerEmail", sellerEmail);
                                    startActivity(intent);
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
        }
        if (v == right){
            if (cur<globI) {
                cur++;
                if (cur <globI )
                    currentImg = img.get(cur);
                else {
                    cur--;
                    currentImg = img.get(cur);

                }
            }
            else{
                currentImg = img.get(globI-1);
            }
            loadImgProd();
        }
        if (v == left){
            if (cur == 0){
                currentImg = img.get(cur);
            }
            else {
                cur--;
                currentImg = img.get(cur);
            }
            loadImgProd();
        }

        if (v == submitReview) {
            if (isLogin) {
                reviewText = writeReview.getText().toString();
                postReview();
            }
            else
            {
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
            }
        }
        if (v == order) {
            Intent intent = new Intent(this, addOrder.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        if (v == likeWhite) {
            if (isLogin) {
                String URL_PRODUCTS = "https://umk-jkms.com/mobile/like.php?tag=like&id=" + id + "&email=" + email;
                StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "Added to your like list", Toast.LENGTH_SHORT).show();
                                System.out.println("like" + response);
                                likeWhite.setVisibility(GONE);
                                likeRed.setVisibility(View.VISIBLE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                Volley.newRequestQueue(this).add(jsonObjectRequest);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void postReview() {
            class uploadReview extends AsyncTask<String, Void, String> {

                ProgressDialog loading;
                RequestHandler rh = new RequestHandler();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(productDetail.this, "Posting review", null, true, true);
                }

                @Override
                protected void onPostExecute(String s) {//This part involves the php codes
                    super.onPostExecute(s);
                    loading.dismiss();
                    if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                        Toast.makeText(getApplicationContext(), "Review added", Toast.LENGTH_LONG).show();
                        writeReview.setText("");
                        reviewList.clear();
                        loadReview();
                    } else
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(String... params) {

                    HashMap<String, String> data = new HashMap<>();

                    data.put("newRes", reviewText);
                    data.put("id" , id);
                    data.put("email", email);
                    String result = rh.sendPostRequest("https://umk-jkms.com/mobile/uploadReview.php", data);
                    return result;
                }
            }
            uploadReview ui = new uploadReview();
            ui.execute();
    }
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("here at onPause");
        finish();
    }
}

