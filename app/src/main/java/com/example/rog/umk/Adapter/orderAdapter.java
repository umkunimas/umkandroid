package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.Order.Message;
import com.example.rog.umk.Order.viewOrder;
import com.example.rog.umk.Product.productDetail;
import com.example.rog.umk.R;
import com.example.rog.umk.qrCode;

import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<orderAdapterList> productList;
    int qty;
    String type;
    String total;
    String orderId, usrType;

    public orderAdapter(Context mCtx, List<orderAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_history_adapter, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  HistoryViewHolder holder, int position) {
        orderAdapterList product = productList.get(position);
        //loading the image
        Glide.with(mCtx)
                .load(product.getImg())
                .transition(withCrossFade())
                .into(holder.imageView);

        holder.amt.setText(product.getAmt());
        qty = Integer.parseInt(product.getAmt());
        holder.price.setText(product.getPrice());
        holder.name.setText(product.getName());
        holder.date.setText(product.getDate());
        holder.id.setText(product.getId());
        holder.order.setText(product.getOrderID());
        type = product.getType();
        System.out.println("type: " + type);
        if (type.equals("member")) {
            System.out.println("Here member");
            holder.msg.setVisibility(View.VISIBLE);
            holder.cont.setVisibility(View.GONE);
        }
        else {
            System.out.println("Here guest");
            holder.tele.setText(product.getEmail());
            holder.cont.setVisibility(View.VISIBLE);
            holder.msg.setVisibility(View.GONE);
            holder.msgtext.setText("Guest Order");
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, price;
        ImageView imageView;
        TextView amt;
        TextView name, rm, qty, orderID;
        CardView card;
        TextView id;
        TextView order;
        TextView buyer;
        TextView tele, msgtext;
        SharedPreferences prefs;
        String email;
        ImageButton qr;
        LinearLayout cont;
        LinearLayout msg;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.imageView);
            amt = itemView.findViewById(R.id.quantity);
            id = itemView.findViewById(R.id.id);
            price = itemView.findViewById(R.id.price);
            buyer = itemView.findViewById(R.id.buyer);
            tele = itemView.findViewById(R.id.tel);
            name = itemView.findViewById(R.id.name);
            rm = itemView.findViewById(R.id.rm);
            cont = itemView.findViewById(R.id.cont);
            order = itemView.findViewById(R.id.order);
            msgtext = itemView.findViewById(R.id.msgtext);
            qty = itemView.findViewById(R.id.qty);
            qr = itemView.findViewById(R.id.qr);
            msg = itemView.findViewById(R.id.msg);
            msg.setOnClickListener(this);
            qr.setOnClickListener(this);
            card = itemView.findViewById(R.id.card);
            card.setOnClickListener(this);
            prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            usrType = prefs.getString("type", "none");
            email = prefs. getString("username","none");
            System.out.println("usertype" + usrType);
            if (usrType.equals("seller")) {
                qr.setVisibility(View.VISIBLE);
            }
            if (usrType.equals("koperasi")) {
                qr.setVisibility(View.VISIBLE);
            }
            if (usrType.equals("admin")){
                qr.setVisibility(GONE);
                rm.setVisibility(GONE);
                qty.setVisibility(GONE);
                price.setVisibility(GONE);
                date.setVisibility(GONE);
                amt.setVisibility(GONE);
                tele.setVisibility(GONE);
                buyer.setVisibility(GONE);
            }
            if (usrType.equals("buyer")) {
                qr.setVisibility(GONE);
                tele.setVisibility(GONE);
                buyer.setVisibility(GONE);
                msg.setVisibility(View.VISIBLE);
            }

        }

        public void onClick(View v) {
            /*System.out.println("onClick position: " + getAdapterPosition()+ "Id is: " + id.getText().toString());
            String pId = id.getText().toString();
            Intent intent = new Intent(mCtx, product.class);
            intent.putExtra("id", pId);
            mCtx.startActivity(intent);*/
            if (v == qr){
                total = price.getText().toString();
                String str=email + "&" + id.getText().toString() + "&" + total + "&" + order.getText().toString(); // Whatever you need to encode in the QR code
                Intent intent = new Intent(mCtx, qrCode.class);
                intent.putExtra("info", str);
                mCtx.startActivity(intent);
            }

            if (v == msg){
                Intent intent = new Intent(mCtx, Message.class);
                intent.putExtra("id", order.getText().toString());
                mCtx.startActivity(intent);
            }

            if (v == card){
                Intent intent = new Intent(mCtx, viewOrder.class);
                intent.putExtra("id", order.getText().toString());
                mCtx.startActivity(intent);
            }
        }
    }
}
