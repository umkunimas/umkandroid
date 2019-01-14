package com.example.rog.umk.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Product.addOrder;
import com.example.rog.umk.R;

import java.util.HashMap;
import java.util.Map;

public class askForHelp extends AppCompatActivity implements View.OnClickListener {
    Button submit;
    SharedPreferences prefs;
    EditText desc, title;
    String email = "";
    com.android.volley.RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_help);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        submit = findViewById(R.id.submit);
        desc = findViewById(R.id.desc);
        title = findViewById(R.id.title);
        submit.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(askForHelp.this);
    }

    @Override
    public void onClick(View v) {
        if (v == submit){
            if (desc.getText().toString().equals("") || title.getText().toString().equals(""))
                Toast.makeText(askForHelp.this, "Please fill in all information", Toast.LENGTH_LONG).show();
            else
                submitHelp();
            //Toast.makeText(askForHelp.this, "Press Submit", Toast.LENGTH_LONG).show();
        }
    }

    private void submitHelp() {
        String UPLOAD_URL;
        UPLOAD_URL = "https://umk-jkms.com/mobile/askForHelp.php";
        email = prefs. getString("username","none");
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if (ServerResponse.equals("success")){
                            Toast.makeText(askForHelp.this, "Your concern has been record", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent (askForHelp.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(askForHelp.this, "No internet connection", Toast.LENGTH_LONG).show();
                        System.out.println(ServerResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(askForHelp.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("desc", desc.getText().toString());
                params.put("title", title.getText().toString());
                params.put("email", email);
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(askForHelp.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
}
