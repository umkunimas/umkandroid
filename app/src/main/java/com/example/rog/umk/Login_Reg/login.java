package com.example.rog.umk.Login_Reg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;

import java.util.HashMap;

public class login extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_URL = "https://umk-jkms.com/mobile/login.php";
    public static final String UPLOAD_USR = "email";
    public static final String UPLOAD_PWD = "password";
    private EditText email;
    private EditText password;
    private Button login;
    private TextView signUp;
    String getUsr, getPwd;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String usernamePref = "username";
    public static final String passwordPref = "password";
    public static final String userType = "type";
    public static final boolean isLogin = true;
    SharedPreferences sharedpreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        email = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        //if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);

        //} else {
            super.onBackPressed();
        //}
    }
    private void goLogin() {
        class UploadImage extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(login.this, "Signing In", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                int type = Integer.parseInt(s);
                String usrType= "";
                if (type <5) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    if (type == 1)
                        usrType = "admin";
                    else if (type==2)
                        usrType = "seller";
                    else if (type==3)
                        usrType = "koperasi";
                    else
                        usrType = "buyer";
                    editor.putString(usernamePref, getUsr);
                    editor.putString(passwordPref, getPwd);
                    editor.putString(userType, usrType);
                    editor.putBoolean("isLogin", true);
                    editor.commit();
                    System.out.println("user type: " + usrType);
                    finish();//The operation from php will be ended (finish()) and..
                    Intent intent = new Intent(login.this, MainActivity.class);// This intent will be initiated
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put(UPLOAD_USR, getUsr);
                data.put(UPLOAD_PWD, getPwd);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
    @Override
    public void onClick(View view) {
        if (view == login) {
            int gotUsername = 0;
            int gotPassword = 0;
            if (view == login) {
                getPwd = password.getText().toString();
                getUsr = email.getText().toString();
                if (getUsr.length() > 0) {
                    gotUsername = 1;

                }
                if (getPwd.length() > 0) {
                    gotPassword = 1;
                }

                /* Proceed to login */
                if (gotUsername == 1 && gotPassword == 1) {
                    goLogin();
                }

                /* print error message */
                else if (gotUsername == 0 && gotPassword == 1) {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (gotUsername == 1 && gotPassword == 0) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (gotUsername == 0 && gotPassword == 0) {
                    Toast.makeText(getApplicationContext(), "Username and Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (view == signUp) {
            Intent intent = new Intent(login.this, register.class);// This intent will be initiated
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
