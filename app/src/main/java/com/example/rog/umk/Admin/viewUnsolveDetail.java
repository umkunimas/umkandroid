package com.example.rog.umk.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.Homepage.Homepage;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class viewUnsolveDetail extends AppCompatActivity implements View.OnClickListener {

    TextView titleTv, dateTv, idTv, nameTv, descTv;
    String id, title, date, name, status, desc, answer;
    EditText answerEt;
    Button submit;
    final private String UPLOAD_URL = "https://umk-jkms.com/mobile/answerCase.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_unsolve_detail);
        titleTv = findViewById(R.id.title);
        descTv = findViewById(R.id.desc);
        dateTv = findViewById(R.id.date);
        idTv = findViewById(R.id.id);
        nameTv = findViewById(R.id.name);
        answerEt = findViewById(R.id.answer);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        loadCase();
    }

    private void loadCase() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/viewHelpDetail.php?id=" + id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                name = response1.getString("name");
                                status = response1.getString("status");
                                title = response1.getString("title");
                                date = response1.getString("date");
                                desc = response1.getString("desc");
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
    }

    private void loadFromSite(){
        titleTv.setText(title);
        dateTv.setText(date);
        descTv.setText(desc);
        nameTv.setText(name);
        idTv.setText(id);
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
                loading = ProgressDialog.show(viewUnsolveDetail.this, "Updating answer", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(viewUnsolveDetail.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
                    System.out.println(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put("answer", answer);
                data.put("case" , id);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        uploadAnswer ui = new uploadAnswer();
        ui.execute();
    }
}
