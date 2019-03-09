package com.example.rog.umk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Splash extends AppCompatActivity {
    Context mCtx;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = getApplicationContext();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        new CheckInternetAsyncTask().execute();

    }

    public class CheckInternetAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            ConnectivityManager cm =
                    (ConnectivityManager)mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();


            if (isConnected) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    if (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0)
                        return true;

                } catch (IOException e) {
                    Log.e("TAG", "Error checking internet connection", e);
                    return false;
                }
            } else {
                Log.d("TAG", "No network available!");
                return false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d("TAG", "result" + result);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            if (result){
                System.out.println("here splash result true");
                Intent intent = new Intent(mCtx, MainActivity.class);
                editor.putBoolean("connection", true);
                editor.commit();
                startActivity(intent);
                finish();
            }
            else{
                System.out.println("here splash result false");
                Intent intent = new Intent(mCtx, MainActivity.class);
                editor.putBoolean("connection", false);
                editor.commit();
                startActivity(intent);
                finish();
                /*
                try {
                    AlertDialog alertDialog = new AlertDialog.Builder(mCtx).create();

                    alertDialog.setTitle("Info");
                    alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    alertDialog.show();
                } catch (Exception e) {
                    Log.d(SyncStateContract.Constants.TAG, "Show Dialog: " + e.getMessage());
                }*/
            }
        }
    }
}
