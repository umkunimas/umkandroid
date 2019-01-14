package com.example.rog.umk.Profile;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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

import com.example.rog.umk.Adapter.SolveAdapterList;

import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class profile extends AppCompatActivity implements View.OnClickListener {
    static String id;
    //a list to store all the products
    List<SolveAdapterList> productList;
    String URL_PRODUCTS;
    //the recyclerview
    RecyclerView recyclerView;
    SharedPreferences prefs;
    String sellerEmail, sellerEmail1;
    TextView username, email, dob, sex, address, tel,test, course, titleBox, license;
    String username1, email1, dob1, sex1, address1, tel1, img1, userType, course1;
    ImageView img;
    String titleName, result, tag, license1;
    TextView bidang, lokasi, alamat;
    String bidang1, lokasi1, alamat1;

    public boolean isLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("isLogin", false);
        userType = prefs.getString("type","none");
        Bundle bundle = getIntent().getExtras();
        sellerEmail = bundle.getString("sellerEmail");
        System.out.println("email profile" + sellerEmail);
        if (isLogin) {
            setContentView(R.layout.seller_profile);
            sellerEmail1 = prefs.getString("username", "none");
            if (sellerEmail.equals(sellerEmail1)){
                System.out.println("here");
                username = findViewById(R.id.username);
                username.setOnClickListener(this);
                email = findViewById(R.id.email);
                test = findViewById(R.id.test);
                test.setOnClickListener(this);
                sex = findViewById(R.id.sex);
                sex.setOnClickListener(this);
                address = findViewById(R.id.address);
                address.setOnClickListener(this);
                tel = findViewById(R.id.tel);
                tel.setOnClickListener(this);
                course = findViewById(R.id.course);
                course.setOnClickListener(this);
                img =findViewById(R.id.img);
                bidang = findViewById(R.id.bidang);
                lokasi = findViewById(R.id.location);
                alamat = findViewById(R.id.alamat);
                license = findViewById(R.id.dob);
                license.setOnClickListener(this);
                alamat.setOnClickListener(this);
                bidang.setOnClickListener(this);
                lokasi.setOnClickListener(this);
            }
            else {
                System.out.println("here1");
                username = findViewById(R.id.username);
                email = findViewById(R.id.email);
                license = findViewById(R.id.dob);
                test = findViewById(R.id.test);
                test.setOnClickListener(this);

                sex = findViewById(R.id.sex);
                address = findViewById(R.id.address);
                tel = findViewById(R.id.tel);
                img = findViewById(R.id.img);
                bidang = findViewById(R.id.bidang);
                lokasi = findViewById(R.id.location);
                alamat = findViewById(R.id.alamat);
                course = findViewById(R.id.course);

            }
            //this method will fetch and parse json
            //to display it in recyclerview
            loadProducts();
        }
        else{
            Intent intent = new Intent (this, login.class);
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        System.out.println("selleremail" + sellerEmail);
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/getSellerDetail.php?tag=1&seller=" + sellerEmail;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                sex1 = response1.getString("sex");
                                address1 = response1.getString("address");
                                tel1 = response1.getString("tel");
                                img1 = response1.getString("img");
                                email1 = response1.getString("email");
                                username1 = response1.getString("name");
                                bidang1 = response1.getString("field");
                                lokasi1 = response1.getString("division");
                                alamat1 = response1.getString("location");
                                course1 = response1.getString("course");
                                license1 = response1.getString("license");
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
        address.setText(address1);
        username.setText(username1);
        tel.setText(tel1);
        sex.setText(sex1);
        email.setText(email1);
        bidang.setText(bidang1);
        lokasi.setText(lokasi1);
        alamat.setText(alamat1);
        course.setText(course1);
        license.setText(license1);
        Glide.with(this)
                .load(img1)
                .transition(withCrossFade())
                .into(img);
    }

    @Override
    public void onClick(View v) {
        if (v==test){
            Intent intent = new Intent (this, profilePerniagaan.class);
            intent.putExtra("sellerEmail", sellerEmail);
            startActivity(intent);
        }
        if (v==course){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Course";
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
                                    result = userInput.getText().toString();
                                    tag = "course";
                                    goLogin();
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
        if (v==lokasi){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Division";
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
                                    result = userInput.getText().toString();
                                    tag = "division";
                                    goLogin();
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
        if (v==alamat){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Business Address";
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
                                    result = userInput.getText().toString();
                                    tag = "location";
                                    goLogin();
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
        if (v==bidang){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Field";
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
                                    result = userInput.getText().toString();
                                    tag = "field";
                                    goLogin();
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
        if (v==sex){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Sex";
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
                                    result = userInput.getText().toString();
                                    tag = "sex";
                                    goLogin();
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
        if (v==tel){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Telephone Number";
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
                                    result = userInput.getText().toString();
                                    tag = "contact";
                                    goLogin();
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
        if (v==address){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Address";
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
                                    result = userInput.getText().toString();
                                    tag = "address";
                                    goLogin();
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
        if (v==username){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Name";
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
                                    result = userInput.getText().toString();
                                    tag = "name";
                                    goLogin();
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
        if (v==license){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change License Number";
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
                                    result = userInput.getText().toString();
                                    tag = "license";
                                    goLogin();
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
        if (v==course){
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.custombox, null);
            titleName = "Change Course";
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
                                    result = userInput.getText().toString();
                                    tag = "course";
                                    goLogin();
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

    private void goLogin() {
        class UploadImage extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            String UPLOAD_URL = "https://umk-jkms.com/mobile/editProfile.php";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(profile.this, "Updating profile", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                    loadProducts();
                } else
                    Toast.makeText(getApplicationContext(), "Cannot update", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put("newRes", result);
                data.put("newTag", tag);
                data.put("email" , sellerEmail1);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
}
