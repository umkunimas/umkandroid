package com.example.rog.umk.Login_Reg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
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
import com.example.rog.umk.Helper.Validation;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener {
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_FRONT = 2;
    private static int RESULT_LOAD_BACK = 3;
    private static final String SERVER_ADDRESS = "https://umk-jkms.com/mobile/";

    public ImageView ivProfilePic;
    public EditText etName;
    public EditText etContactNum;
    public EditText etEmail;
    public EditText etPassword;
    public EditText etConfirmPass;
    public Button btnSubmit, btnUpload;
    public EditText etIc;
    TextView login;
    Bitmap image, front, back;
    String name;
    String fname;
    String faddress;
    String contactnum;
    String email;
    String password;
    String encodedImage = "";
    com.android.volley.RequestQueue requestQueue;
    ProgressDialog progressDialog;

    Boolean imagepresent = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        registerViews();
        requestQueue = Volley.newRequestQueue(register.this);
    }

    public void registerViews(){

        ivProfilePic = findViewById(R.id.dp);
        etName = findViewById(R.id.name);
        etContactNum = findViewById(R.id.contact);
        etConfirmPass = findViewById(R.id.password2);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnSubmit = findViewById(R.id.signup);
        requestQueue = Volley.newRequestQueue(register.this);
        progressDialog = new ProgressDialog(register.this);

        etName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etName);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        etContactNum.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etContactNum);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(etEmail, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etPassword);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        etConfirmPass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etConfirmPass);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        ivProfilePic.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(etName)) ret = false;
        if (!Validation.hasText(etContactNum)) ret = false;
        if (!Validation.isEmailAddress(etEmail, true)) ret = false;
        if (!Validation.hasText(etPassword)) ret = false;
        if (!Validation.hasText(etConfirmPass)) ret = false;
        return ret;
    }


    @Override
    public void onClick(View v) {
        Intent galleryIntent;
        switch(v.getId()){
            case R.id.dp:
                galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                break;
            case R.id.login:
                galleryIntent = new Intent(register.this, login.class);
                startActivity(galleryIntent);
                break;
            case R.id.signup:
                if(imagepresent){
                    if (checkValidation()){
                        String pass1 = etConfirmPass.getText().toString();
                        String pass2 = etPassword.getText().toString();
                        Bitmap image = ((BitmapDrawable) ivProfilePic.getDrawable()).getBitmap();

                        if(!pass1.equals(pass2)) {
                            Toast.makeText(register.this, "Password confirmation not match! Please try again.", Toast.LENGTH_SHORT).show();
                        }else{
                            encodedImage = convertImage(image);
                            UserRegistration();
                            //ivProfilePic.setImageResource(0);
                            //etName.setText("");
                            //etFarmName.setText("");
                            //etFarmAddress.setText("");
                            //etContactNum.setText("");
                            //etEmail.setText("");
                            //etPassword.setText("");
                            //etConfirmPass.setText("");
                        }
                    }}
                else if(!imagepresent){
                    if (checkValidation()){
                        String pass1 = etConfirmPass.getText().toString();
                        String pass2 = etPassword.getText().toString();

                        if(!pass1.equals(pass2)) {
                            Toast.makeText(register.this, "Password confirmation not match! Please try again.", Toast.LENGTH_SHORT).show();
                        }else{

                            noImage();
                            UserRegistration();
                            ivProfilePic.setImageResource(0);
                            //etName.setText("");
                            //etFarmName.setText("");
                            //etFarmAddress.setText("");
                            //etContactNum.setText("");
                            //etEmail.setText("");
                            //etPassword.setText("");
                            //etConfirmPass.setText("");

                        }
                    }}
                else{
                    Toast.makeText(register.this, "Please insert your Identification Card picture", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void UserRegistration(){

        name = etName.getText().toString();
        contactnum = etContactNum.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "SaveInfo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        System.out.println("server: " + ServerResponse);
                        // Showing Echo Response Message Coming From Server.
                        Toast.makeText(register.this, ServerResponse, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(register.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        System.out.println("server: her");
                        // Showing error message if something goes wrong.
                        Toast.makeText(register.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("image", encodedImage);
                params.put("name", name);
                params.put("contactnum", contactnum);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(register.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data !=null){
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                ivProfilePic.setImageURI(selectedImage);
                imagepresent = true;
            }
        }

    }

    private String convertImage(Bitmap image){
        String encoded;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return encoded;
    }

    //If profile picture is absent
    private void noImage(){
        encodedImage = "Default Picture";
    }
}
