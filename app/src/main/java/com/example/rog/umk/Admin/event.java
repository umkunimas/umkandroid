package com.example.rog.umk.Admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Product.addNewProduct;
import com.example.rog.umk.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class event extends AppCompatActivity implements View.OnClickListener{
    ImageView thumbnail;
    EditText title, location, desc, indate, intime;
    ImageButton date, time;
    Button submit;
    boolean imagepresent;
    private Bitmap bm1;
    public static final String UPLOAD_URL = "https://umk-jkms.com/mobile/event.php";
    String getLoc, getName, getDesc, getDate, getTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        thumbnail = findViewById(R.id.imageView);
        title = findViewById(R.id.name);

        date = findViewById(R.id.date);
        date.setOnClickListener(this);
        time = findViewById(R.id.time);
        time.setOnClickListener(this);

        indate = findViewById(R.id.in_date);
        intime = findViewById(R.id.in_time);
        location = findViewById(R.id.location);
        desc = findViewById(R.id.desc);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        thumbnail.setOnClickListener(this);
    }
    public void dispatchTakePictureIntent() {

        final CharSequence[] items={"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(event.this);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                thumbnail.setImageBitmap(imageBitmap);
                bm1 = imageBitmap;
                imagepresent = true;
                return;
            }
            else if(requestCode == RESULT_LOAD_IMAGE){

                Uri selectedImage = data.getData();
                thumbnail.setImageURI(selectedImage);
                imagepresent = true;
                return;
            }
        }
    }
    private void uploadImage(){

        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(event.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                System.out.println("Error: \n" + s);

                Intent intent = new Intent (event.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bm1 = params[0];
                String uploadImage = getStringImage(bm1);
                HashMap<String,String> data = new HashMap<>();

                data.put("img", uploadImage);
                data.put("location", getLoc);
                data.put("name", getName);
                data.put("desc", getDesc);
                data.put("date", getDate);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bm1);
    }
    public void upload() {

        if (imagepresent) {
            System.out.println("All four image is present");
            bm1 = ((BitmapDrawable) thumbnail.getDrawable()).getBitmap();
            getLoc = location.getText().toString();
            getDate = indate.getText().toString() + intime.getText().toString();
            System.out.println(getDate);
            getName = title.getText().toString();
            getDesc = desc.getText().toString();
            uploadImage();
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v == thumbnail)
            dispatchTakePictureIntent();
        if (v == submit){
            upload();
        }
        if (v==date){
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            indate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v==time){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            intime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
