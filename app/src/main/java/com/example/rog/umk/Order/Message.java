package com.example.rog.umk.Order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.MessageAdapter;
import com.example.rog.umk.Adapter.MessageAdapterList;
import com.example.rog.umk.Adapter.orderAdapterKoperasi;
import com.example.rog.umk.Adapter.orderAdapterKoperasiList;
import com.example.rog.umk.Admin.viewUnsolveDetail;
import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;

import net.skoumal.fragmentback.BackFragmentAppCompatActivity;
import net.skoumal.fragmentback.BackFragmentHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity implements View.OnClickListener{

    TextView dateTv, idTv;
    String id, id1, title, date, name, status, desc, answer;
    EditText answerEt;
    List<MessageAdapterList> MessageList;
    RecyclerView recyclerView;
    SharedPreferences prefs;
    String usrType;
    Button submit;
    String email;
    MainActivity activity;
    final private String UPLOAD_URL = "https://umk-jkms.com/mobile/answerMessage.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        activity = new MainActivity();
        dateTv = findViewById(R.id.date);
        idTv = findViewById(R.id.id);
        answerEt = findViewById(R.id.answer);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        MessageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println("id is: " + id);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        usrType = prefs.getString("type", "none");
        email = prefs.getString("username", " none");
        loadCase();
        loadMessage();
    }

    private void loadCase() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/message.php?tag=detail&id=" + id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                id1 = response1.getString("orderID");
                                date = response1.getString("date");
                                loadFromSite();
                            }
                            System.out.println("id message" + id1);
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

    private void loadFromSite(){
        dateTv.setText(date);
        idTv.setText(id1);
    }
    private void loadMessage(){
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/message.php?tag=msg&id=" + id;
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
                                MessageList.add(new MessageAdapterList(
                                        product.getString("msg"),
                                        product.getString("name"),
                                        product.getString("img"),
                                        product.getString("date"),
                                        product.getString("email")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            MessageAdapter adapter = new MessageAdapter(Message.this, MessageList);
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
    @Override
    public void onClick(View v) {

        if (v == submit){
            answer = answerEt.getText().toString();
            submitAnswer();
        }

    }
    private void submitAnswer() {
        class uploadAnswer extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Message.this, "Updating message", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    Toast.makeText(getApplicationContext(), "Your message has been sent", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Message.this, MainActivity.class);
                    startActivityForResult(intent,100);
                } else {
                    Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
                    System.out.println(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put("answer", answer);
                data.put("case" , id1);
                data.put("email", email);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        uploadAnswer ui = new uploadAnswer();
        ui.execute();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                activity.goToHomeFragment();
            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
