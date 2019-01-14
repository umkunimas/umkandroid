package com.example.rog.umk.Login_Reg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class registerSeller extends AppCompatActivity implements View.OnClickListener{

    private static int RESULT_LOAD_IMAGE = 1;
    private static final String SERVER_ADDRESS = "https://umk-jkms.com/mobile/";

    public ImageView ivProfilePic;
    public EditText etName;
    private Spinner spn, spn2;
    public EditText etContactNum;
    public EditText etEmail;
    public EditText etPassword;
    public EditText etConfirmPass;
    public Button btnSubmit;
    public EditText etIc;
    public EditText address, etCourse, etBusiness;
    TextView login;
    Bitmap image, front, back;
    String name;
    String contactnum;
    String email, business;
    String password, course;
    String encodedImage = "";
    String addr, division, field, ic;
    com.android.volley.RequestQueue requestQueue;
    ProgressDialog progressDialog;

    Boolean imagepresent = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_seller);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        registerViews();
        requestQueue = Volley.newRequestQueue(registerSeller.this);
    }

    public void registerViews(){

        ivProfilePic = findViewById(R.id.dp);
        etName = findViewById(R.id.name);
        etContactNum = findViewById(R.id.contact);
        etConfirmPass = findViewById(R.id.password2);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnSubmit = findViewById(R.id.signup);
        address = findViewById(R.id.address);
        etIc = findViewById(R.id.ic);
        etCourse = findViewById(R.id.kursus);
        etBusiness = findViewById(R.id.business);
        spn  = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn.setAdapter(adapter);
        spn2  = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.complaint_arrays1, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn2.setAdapter(adapter1);
        requestQueue = Volley.newRequestQueue(registerSeller.this);
        progressDialog = new ProgressDialog(registerSeller.this);

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
                galleryIntent = new Intent(registerSeller.this, login.class);
                startActivity(galleryIntent);
                break;
            case R.id.signup:
                if(imagepresent){
                    if (checkValidation()){
                        String pass1 = etConfirmPass.getText().toString();
                        String pass2 = etPassword.getText().toString();
                        Bitmap image = ((BitmapDrawable) ivProfilePic.getDrawable()).getBitmap();

                        if(!pass1.equals(pass2)) {
                            Toast.makeText(registerSeller.this, "Password confirmation not match! Please try again.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(registerSeller.this, "Password confirmation not match! Please try again.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(registerSeller.this, "Please insert your Identification Card picture", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void UserRegistration(){

        name = etName.getText().toString();
        contactnum = etContactNum.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        course = etCourse.getText().toString();
        field = spn.getSelectedItem().toString();
        division = spn2.getSelectedItem().toString();
        addr = address.getText().toString();
        ic = etIc.getText().toString();
        business = etBusiness.getText().toString();
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        System.out.println("name"+name);
        System.out.println("name"+contactnum);
        System.out.println("name"+email);
        System.out.println("name"+password);
        System.out.println("name"+course);
        System.out.println("name"+field);
        System.out.println("name"+division);
        System.out.println("name"+addr);
        System.out.println("name"+ic);
        System.out.println("name"+business);
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS + "registerSeller.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        System.out.println("server: " + ServerResponse);
                        // Showing Echo Response Message Coming From Server.
                        Toast.makeText(registerSeller.this, ServerResponse, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(registerSeller.this, MainActivity.class);
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
                        Toast.makeText(registerSeller.this, "Error", Toast.LENGTH_LONG).show();
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
                params.put("ic", ic);
                params.put("field", field);
                params.put("division", division);
                params.put("address", addr);
                params.put("course", course);
                params.put("location", business);
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(registerSeller.this);

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
