package com.example.rog.umk;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class test extends AppCompatActivity implements View.OnClickListener {
    Button logout, scan, generate;
    SharedPreferences sharedpreferences;
    ImageView qr;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        logout = findViewById(R.id.button);
        qr = findViewById(R.id.qr);
        logout.setOnClickListener(this);
        scan = findViewById(R.id.button3);
        scan.setOnClickListener(this);
        generate = findViewById(R.id.button2);
        generate.setOnClickListener(this);

    }
    public void generateQr(){
        String text="this is qr example"; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr.setImageBitmap(bitmap);
            System.out.println("Qr code content:" );
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    @Override
    public void onClick(View v) {
        if (v == logout){
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainContext);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedpreferences = getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                    startActivity(i);
                }
            });*/
            sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Toast.makeText(getApplicationContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(test.this, MainActivity.class);// This intent will be initiated
            startActivity(intent);
        }
        if (v == scan){
            //Intent intent = new Intent (test.this, testScan.class);
            //startActivity(intent);
            launchActivity(testScan.class);
        }
        if (v==generate){
            generateQr();
        }
    }
}
