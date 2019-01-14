package com.example.rog.umk.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.Order.generateQr;
import com.example.rog.umk.Order.koperasiList;
import com.example.rog.umk.Product.addOrder;
import com.example.rog.umk.Profile.profile;
import com.example.rog.umk.R;
import com.example.rog.umk.koperasiAdapterList;

import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class koperasiAdapter extends RecyclerView.Adapter<koperasiAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<koperasiAdapterList> productList;
    String itemId;
    String result;
    String sellerEmail;
    SharedPreferences prefs;
    String email1;
    public koperasiAdapter(Context mCtx, List<koperasiAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public koperasiAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.koperasi_adapter, null);
        return new koperasiAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull koperasiAdapter.HistoryViewHolder holder, int position) {
        koperasiAdapterList product = productList.get(position);
        //loading the image
        Glide.with(mCtx)
                .load(product.getpImg())
                .transition(withCrossFade())
                .into(holder.pDp);

        holder.name.setText(product.getName());
        holder.contact.setText(product.getContact());
        holder.email.setText(product.getSellerEmail());
        itemId = product.getId();

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView contact;
        TextView amount;
        CardView card;
        ImageView pDp;
        TextView name, titleBox;
        TextView date;
        Button complete, view;
        TextView email;
        String userType;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            email1 = prefs.getString("username", "none");
            userType = prefs.getString("type","none");
            contact = itemView.findViewById(R.id.contact);
            pDp = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            card = itemView.findViewById(R.id.card);
            view = itemView.findViewById(R.id.view);
            complete = itemView.findViewById(R.id.complete);
            if (!userType.equals("admin")) {
                complete.setOnClickListener(this);
                view.setVisibility(GONE);
            }
            else {
                complete.setVisibility(GONE);
                card.setOnClickListener(this);
                view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v==view){
                System.out.println("seller: " + sellerEmail);
                Intent intent = new Intent (mCtx, profile.class);
                intent.putExtra("sellerEmail", email.getText().toString());
                mCtx.startActivity(intent);
            }
            if (v==complete){
                String titleName="";

                LayoutInflater li = LayoutInflater.from(mCtx);
                View promptsView = li.inflate(R.layout.custombox, null);
                titleName = "Enter Total Price";
                titleBox = promptsView.findViewById(R.id.title);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                titleBox.setText(titleName);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.input);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        result = userInput.getText().toString();

                                        sellerEmail = email.getText().toString();
                                        goLogin();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        }
    }
    private void goLogin() {
        class UploadImage extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            String UPLOAD_URL = "https://umk-jkms.com/mobile/koperasi.php";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mCtx, "Updating profile", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    Toast.makeText(mCtx, "Paid", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mCtx, "Cannot pay", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("newRes", result);
                data.put("itemID", itemId);
                data.put("sellerEmail" , sellerEmail);
                //email1 is koperasi email
                data.put("email", email1);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
}
