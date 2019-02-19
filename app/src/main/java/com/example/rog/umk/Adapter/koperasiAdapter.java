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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Order.generateQr;
import com.example.rog.umk.Order.koperasiList;
import com.example.rog.umk.Product.addOrder;
import com.example.rog.umk.Profile.profile;
import com.example.rog.umk.R;
import com.example.rog.umk.koperasiAdapterList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class koperasiAdapter extends RecyclerView.Adapter<koperasiAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<koperasiAdapterList> productList;
    com.android.volley.RequestQueue requestQueue;
    String itemId;
    String result;
    String sellerEmail;
    SharedPreferences prefs;
    String email1;
    String tag;
    String orderID;
    public koperasiAdapter(Context mCtx, List<koperasiAdapterList> productList, String tag, String orderID) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.tag = tag;
        this.orderID = orderID;
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
        System.out.println("koperasi adapter getName" + product.getName());
        holder.contact.setText(product.getContact());
        holder.email.setText(product.getSellerEmail());
        holder.itemID.setText(product.getId());
        sellerEmail =product.getSellerEmail();

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
        TextView itemID;
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
            itemID = itemView.findViewById(R.id.id);
            view = itemView.findViewById(R.id.view);
            complete = itemView.findViewById(R.id.complete);
            requestQueue = Volley.newRequestQueue(mCtx);
            if (tag.equals("list"))
                complete.setText("Make Order");
            else if (tag.equals("order"))
                complete.setText("Complete Order");
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
            if (v==complete) {
                System.out.println("orderID at koperasiAdapter: " + orderID);
                Intent intent = new Intent(mCtx, addOrder.class);
                intent.putExtra("id", itemID.getText().toString());
                intent.putExtra("orderID", orderID);
                mCtx.startActivity(intent);
            }
        }
    }
}
