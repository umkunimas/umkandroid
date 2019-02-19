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


import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Helper.RequestHandler;

import com.example.rog.umk.MainActivity;
import com.example.rog.umk.R;


import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class koperasiMyOrderAdapter extends RecyclerView.Adapter<koperasiMyOrderAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<koperasiMyOrderAdapterList> productList;
    com.android.volley.RequestQueue requestQueue;
    String itemId;
    String result;
    String sellerEmail;
    SharedPreferences prefs;
    String email1;
    String oID, productName;
    public koperasiMyOrderAdapter(Context mCtx, List<koperasiMyOrderAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public koperasiMyOrderAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.koperasi_my_order, null);
        return new koperasiMyOrderAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull koperasiMyOrderAdapter.HistoryViewHolder holder, int position) {
        koperasiMyOrderAdapterList product = productList.get(position);
        //loading the image
        Glide.with(mCtx)
                .load(product.getpImg())
                .transition(withCrossFade())
                .into(holder.pDp);

        holder.name.setText(product.getName());
        System.out.println("koperasi adapter getName" + product.getName());
        holder.contact.setText(product.getContact());
        holder.email.setText(product.getsEmail());
        holder.orderID.setText(product.getId());
        holder.date.setText(product.getDate());
        holder.price.setText(product.getAmount());
        holder.qty.setText(product.getQty());
        holder.seller.setText(product.getSellerName());
        sellerEmail =product.getsEmail();
        if (product.getStatus().equals("1"))
            holder.complete.setVisibility(GONE);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView contact;
        TextView price;
        CardView card;
        ImageView pDp;
        //name is product name
        TextView name, titleBox, seller;
        TextView date, qty;
        Button complete, view;
        TextView email;
        String userType;
        TextView orderID;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            email1 = prefs.getString("username", "none");
            contact = itemView.findViewById(R.id.tel);
            pDp = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            seller = itemView.findViewById(R.id.seller);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
            orderID = itemView.findViewById(R.id.id);
            complete = itemView.findViewById(R.id.complete);
            complete.setOnClickListener(this);
            requestQueue = Volley.newRequestQueue(mCtx);
        }

        @Override
        public void onClick(View v) {

            if (v==complete) {

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
                                            oID = orderID.getText().toString();
                                            productName = name.getText().toString();
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
                loading = ProgressDialog.show(mCtx, "Paying", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    Toast.makeText(mCtx, "Paid", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent (mCtx, MainActivity.class);
                    mCtx.startActivity(intent);
                } else {
                    Toast.makeText(mCtx, "Cannot pay", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();
                data.put("newRes", result);
                data.put("itemID", oID);
                data.put("sellerEmail" , sellerEmail);
                //email1 is koperasi email
                data.put("email", email1);
                data.put("product", productName);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
}
