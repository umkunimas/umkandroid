package com.example.rog.umk.Product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class addNewProduct extends AppCompatActivity {
    private ImageView img1, img2, img3, img4;
    private EditText name, price, desc, quantity, location;
    private Button submit;
    private Bitmap bm1, bm2, bm3, bm4;
    SharedPreferences prefs;
    String getImg1, getImg2, getImg3, getImg4, getName, getPrice, getDesc, getQty, getLoc, getImgTag;
    String email;

    public static final String UPLOAD_URL = "https://umk-jkms.com/mobile/UploadProduct.php";
    public static final String UPLOAD_KEY = "img";
    public static final String UPLOAD_KEY1 = "img1";
    public static final String UPLOAD_KEY2 = "img2";
    public static final String UPLOAD_KEY3 = "img3";
    public static final String UPLOAD_LOCATION = "location";
    public static final String UPLOAD_PRICE = "price";
    public static final String UPLOAD_NAME = "name";
    public static final String UPLOAD_QTY = "qty";
    public static final String UPLOAD_DESC = "desc";
    public static final String UPLOAD_TAG = "tag";

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_CAPTURE1 = 1;
    private static final int REQUEST_IMAGE_CAPTURE2 = 2;
    private static final int REQUEST_IMAGE_CAPTURE3 = 3;
    private static final int RESULT_LOAD_IMAGE = 4;
    private static final int RESULT_LOAD_IMAGE1 = 5;
    private static final int RESULT_LOAD_IMAGE2 = 6;
    private static final int RESULT_LOAD_IMAGE3 = 7;
    
    static boolean isInside = false;
    private Spinner spn;
    boolean imagepresent = false;
    boolean imagepresent1 = false;
    boolean imagepresent2 = false;
    boolean imagepresent3 = false;
    ArrayList<String> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        email = prefs.getString("username","none");
        boolean isLogin = prefs.getBoolean("isLogin", false);
        if (isLogin){
            img1 = findViewById(R.id.imageView);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
            img2 = findViewById(R.id.imageView1);
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent1();
                }
            });
            img3 = findViewById(R.id.imageView2);
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent2();
                }
            });
            img4 = findViewById(R.id.imageView3);
            img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent3();
                }
            });

            submit = findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upload();
                }
            });
            name = findViewById(R.id.name);
            desc = findViewById(R.id.desc);
            price = findViewById(R.id.price);
            location = findViewById(R.id.location);
            quantity = findViewById(R.id.qty);

            spn  = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays, R.layout.myspinner);
            adapter.setDropDownViewResource(R.layout.myspinnerdrop);
            spn.setAdapter(adapter);
        }
        else{
            Intent intent = new Intent (this, login.class);
            startActivity(intent);
        }

    }

    public void dispatchTakePictureIntent() {

        final CharSequence[] items={"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(addNewProduct.this);
        builder.setTitle("Choose Picture");
        builder.setIcon(R.drawable.ic_image_black_24dp);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera")){

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    System.out.println("image_one_is_capture");
                }
                else if(items[which].equals("Gallery")){

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select file"), RESULT_LOAD_IMAGE);
                    System.out.println("image_one_is_capture");
                }
            }
        });
        builder.show();
    }
    public void dispatchTakePictureIntent1() {

        final CharSequence[] items={"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(addNewProduct.this);
        builder.setTitle("Choose Picture");
        builder.setIcon(R.drawable.ic_image_black_24dp);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera")){

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE1);

                }
                else if(items[which].equals("Gallery")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select file"), RESULT_LOAD_IMAGE1);

                }
            }
        });
        builder.show();
    }
    public void dispatchTakePictureIntent2() {

        final CharSequence[] items={"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(addNewProduct.this);
        builder.setTitle("Choose Picture");
        builder.setIcon(R.drawable.ic_image_black_24dp);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera")){

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE2);

                }
                else if(items[which].equals("Gallery")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select file"), RESULT_LOAD_IMAGE2);

                }
            }
        });
        builder.show();
    }
    public void dispatchTakePictureIntent3() {

        final CharSequence[] items={"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(addNewProduct.this);
        builder.setTitle("Choose Picture");
        builder.setIcon(R.drawable.ic_image_black_24dp);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera")){

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE3);
                }
                else if(items[which].equals("Gallery")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select file"), RESULT_LOAD_IMAGE3);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                img1.setImageBitmap(imageBitmap);
                bm1 = imageBitmap;
                imagepresent = true;
                return;
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE1) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                img2.setImageBitmap(imageBitmap);
                bm2 = imageBitmap;
                imagepresent1 = true;
                return;
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE2) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                img3.setImageBitmap(imageBitmap);
                bm3 = imageBitmap;
                imagepresent2 = true;
                return;
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE3) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                img4.setImageBitmap(imageBitmap);
                bm4 = imageBitmap;
                imagepresent3 = true;
                return;
            }
            else if(requestCode == RESULT_LOAD_IMAGE){

                Uri selectedImage = data.getData();
                img1.setImageURI(selectedImage);
                imagepresent = true;
                return;
            }
            else if(requestCode == RESULT_LOAD_IMAGE1){

                Uri selectedImage = data.getData();
                img2.setImageURI(selectedImage);
                imagepresent1 = true;
                return;
            }
            else if(requestCode == RESULT_LOAD_IMAGE2){

                Uri selectedImage = data.getData();
                img3.setImageURI(selectedImage);
                imagepresent2 = true;
                return;
            }
            else if(requestCode == RESULT_LOAD_IMAGE3){

                Uri selectedImage = data.getData();
                img4.setImageURI(selectedImage);
                imagepresent3 = true;
                return;
            }
        }

    }

    public void upload() {

        if (imagepresent && imagepresent1 && imagepresent2 && imagepresent3){

            System.out.println("All four image is present");
            bm1 =  ((BitmapDrawable) img1.getDrawable()).getBitmap();
            bm2 = ((BitmapDrawable) img2.getDrawable()).getBitmap();
            bm3 = ((BitmapDrawable) img3.getDrawable()).getBitmap();
            bm4 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
            getImgTag = spn.getSelectedItem().toString();
            getLoc = location.getText().toString();
            getQty = quantity.getText().toString();

            getPrice = price.getText().toString();
            getName = name.getText().toString();
            getDesc = desc.getText().toString();
            uploadImage4();
        }

        else if (imagepresent)
        {
            System.out.println("Only 1 image is present");
            bm1 = ((BitmapDrawable) img1.getDrawable()).getBitmap();

            if (imagepresent1){
                System.out.println("Only 2 image is present");
                bm2 = ((BitmapDrawable) img2.getDrawable()).getBitmap();

                if (imagepresent2){
                    System.out.println("Only 3 image is present");
                    bm3 = ((BitmapDrawable) img3.getDrawable()).getBitmap();
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getQty = quantity.getText().toString();

                    getName = name.getText().toString();
                    getDesc = desc.getText().toString();
                    getPrice = price.getText().toString();
                    isInside = true;
                    System.out.println("Inside imagepresent2");
                    uploadImage3();

                }
                else if (imagepresent3){
                    System.out.println("Only 3 image is present");
                    bm3 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getQty = quantity.getText().toString();

                    getName = name.getText().toString();
                    getDesc = desc.getText().toString();
                    getPrice = price.getText().toString();
                    isInside = true;
                    uploadImage3();
                }
                if (!isInside) {
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getQty = quantity.getText().toString();

                    getName = name.getText().toString();
                    getDesc = desc.getText().toString();
                    getPrice = price.getText().toString();
                    isInside = true;
                    uploadImage2();
                }
            }
            else if (imagepresent2){
                System.out.println("Only 2 image is present");
                bm2 = ((BitmapDrawable) img3.getDrawable()).getBitmap();

                if (imagepresent3){
                    System.out.println("Only 3 image is present");
                    bm3 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getQty = quantity.getText().toString();

                    getName = name.getText().toString();
                    getPrice = price.getText().toString();
                    getDesc = desc.getText().toString();
                    isInside = true;
                    uploadImage3();
                }
                if (!isInside) {
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getQty = quantity.getText().toString();

                    getPrice = price.getText().toString();
                    getName = name.getText().toString();
                    getDesc = desc.getText().toString();
                    isInside = true;
                    uploadImage2();
                }
            }
            else if (imagepresent3){
                System.out.println("Only 2 image is present");
                bm2 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                getImgTag = spn.getSelectedItem().toString();
                getLoc = location.getText().toString();
                getQty = quantity.getText().toString();

                getPrice = price.getText().toString();
                getName = name.getText().toString();
                isInside = true;
                uploadImage2();
            }
            if (!isInside) {
                getImgTag = spn.getSelectedItem().toString();
                getLoc = location.getText().toString();
                getQty = quantity.getText().toString();

                getName = name.getText().toString();
                getPrice = price.getText().toString();
                getDesc = desc.getText().toString();
                System.out.println("Inside imagepresent");
                isInside = true;
                uploadImage();
            }
        }
        else if (imagepresent1)
        {
            System.out.println("Only 1 image is present");
            bm1 = ((BitmapDrawable) img2.getDrawable()).getBitmap();

            if (imagepresent2){
                System.out.println("Only 2 image is present");
                bm2 = ((BitmapDrawable) img3.getDrawable()).getBitmap();

                if (imagepresent3){
                    System.out.println("Only 3 image is present");
                    bm3 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getPrice = price.getText().toString();
                    getQty = quantity.getText().toString();

                    getName = name.getText().toString();
                    getDesc = desc.getText().toString();
                    isInside = true;
                    uploadImage3();
                }
                if (!isInside) {
                    getImgTag = spn.getSelectedItem().toString();
                    getLoc = location.getText().toString();
                    getQty = quantity.getText().toString();
 
                    getPrice = price.getText().toString();
                    getName = name.getText().toString();
                    getDesc = desc.getText().toString();
                    isInside = true;
                    uploadImage2();
                }
            }
            else if (imagepresent3){
                System.out.println("Only 2 image is present");
                bm2 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                getImgTag = spn.getSelectedItem().toString();
                getLoc = location.getText().toString();
                getQty = quantity.getText().toString();

                getPrice = price.getText().toString();
                getName = name.getText().toString();
                getDesc = desc.getText().toString();
                isInside = true;
                uploadImage2();

            }
            if (!isInside) {
                getImgTag = spn.getSelectedItem().toString();
                getLoc = location.getText().toString();
                getQty = quantity.getText().toString();

                getPrice = price.getText().toString();
                getName = name.getText().toString();
                getDesc = desc.getText().toString();
                isInside = true;
                uploadImage();
            }
        }
        else if (imagepresent2)
        {
            System.out.println("Only 1 image is present");
            bm1 = ((BitmapDrawable) img3.getDrawable()).getBitmap();

            if (imagepresent3){
                System.out.println("Only 2 image is present");
                bm2 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                getImgTag = spn.getSelectedItem().toString();
                getLoc = location.getText().toString();
                getQty = quantity.getText().toString();
                getPrice = price.getText().toString();
                getName = name.getText().toString();
                getDesc = desc.getText().toString();
                isInside = true;
                uploadImage2();
            }
            if (!isInside) {
                getImgTag = spn.getSelectedItem().toString();
                getLoc = location.getText().toString();
                getQty = quantity.getText().toString();
                getPrice = price.getText().toString();
                getName = name.getText().toString();
                getDesc = desc.getText().toString();
                isInside = true;
                uploadImage();
            }
        }
        else if (imagepresent3)
        {
            System.out.println("Only 1 image is present");
            bm1 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
            getImgTag = spn.getSelectedItem().toString();
            getLoc = location.getText().toString();
            getQty = quantity.getText().toString();
            getPrice = price.getText().toString();
            getName = name.getText().toString();
            getDesc = desc.getText().toString();
            isInside = true;
            uploadImage();
        }
        else
        {
            Toast.makeText(addNewProduct.this, "Image not found. Picture cannot be empty",Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(){

        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(addNewProduct.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                System.out.println("Error: \n" + s);
                name.setText("");
                quantity.setText("");

                spn.setSelection(0);
                desc.setText("");
                price.setText("");
                imagepresent = false;
                isInside = false;
                imagepresent1 = false;
                imagepresent2 = false;
                imagepresent3 = false;
                Intent intent = new Intent (addNewProduct.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bm1 = params[0];
                String uploadImage = getStringImage(bm1);
                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_QTY, getQty);
                data.put(UPLOAD_LOCATION, getLoc);
                data.put(UPLOAD_NAME, getName);
                data.put(UPLOAD_TAG, getImgTag);
                data.put(UPLOAD_PRICE, getPrice);
                data.put("desc", getDesc);
                data.put("email", email);
                System.out.println("Email doInBackground: " + email);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bm1);
    }
    private void uploadImage2(){

        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(addNewProduct.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                name.setText("");
                quantity.setText("");
                spn.setSelection(0);
                price.setText("");
                desc.setText("");
                imagepresent = false;
                isInside = false;
                imagepresent1 = false;
                imagepresent2 = false;
                imagepresent3 = false;
                Intent intent = new Intent (addNewProduct.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bm1 = params[0];
                Bitmap bm2 = params[1];
                String uploadImage = getStringImage(bm1);
                String uploadImage1 = getStringImage(bm2);
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_PRICE, getPrice);
                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_KEY1, uploadImage1);
                data.put(UPLOAD_QTY, getQty);
                data.put(UPLOAD_LOCATION, getLoc);
                data.put(UPLOAD_NAME, getName);
                data.put(UPLOAD_TAG, getImgTag);
                data.put("desc", getDesc);
                data.put("email", email);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bm1, bm2);
    }
    private void uploadImage3(){

        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(addNewProduct.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                name.setText("");
                quantity.setText("");

                spn.setSelection(0);
                desc.setText("");
                price.setText("");
                imagepresent = false;
                isInside = false;
                imagepresent1 = false;
                imagepresent2 = false;
                imagepresent3 = false;
                Intent intent = new Intent (addNewProduct.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bm1 = params[0];
                Bitmap bm2 = params[1];
                Bitmap bm3 = params[2];
                String uploadImage = getStringImage(bm1);
                String uploadImage1 = getStringImage(bm2);
                String uploadImage2 = getStringImage(bm3);
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_PRICE, getPrice);
                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_KEY1, uploadImage1);
                data.put(UPLOAD_KEY2, uploadImage2);
                data.put(UPLOAD_QTY, getQty);
                data.put(UPLOAD_LOCATION, getLoc);
                data.put(UPLOAD_NAME, getName);
                data.put(UPLOAD_TAG, getImgTag);
                data.put("desc", getDesc);
                data.put("email", email);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bm1, bm2, bm3);
    }
    private void uploadImage4(){

        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(addNewProduct.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                name.setText("");
                quantity.setText("");

                spn.setSelection(0);
                price.setText("");
                desc.setText("");
                imagepresent1 = false;
                imagepresent2 = false;
                imagepresent3 = false;
                imagepresent = false;
                isInside = false;
                Intent intent = new Intent (addNewProduct.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bm1 = params[0];
                Bitmap bm2 = params[1];
                Bitmap bm3 = params[2];
                Bitmap bm4 = params[3];
                String uploadImage = getStringImage(bm1);
                String uploadImage1 = getStringImage(bm2);
                String uploadImage2 = getStringImage(bm3);
                String uploadImage3 = getStringImage(bm4);
                System.out.println("Upload image: \n" + uploadImage);
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_PRICE, getPrice);
                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_KEY1, uploadImage1);
                data.put(UPLOAD_KEY2, uploadImage2);
                data.put(UPLOAD_KEY3, uploadImage3);
                data.put(UPLOAD_QTY, getQty);
                data.put(UPLOAD_LOCATION, getLoc);
                data.put(UPLOAD_NAME, getName);
                data.put(UPLOAD_TAG, getImgTag);
                data.put("desc", getDesc);
                data.put("email", email);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bm1, bm2, bm3, bm4);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
