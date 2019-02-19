package com.example.rog.umk.Adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.R;

import java.util.List;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<MessageAdapterList> productList;
    SharedPreferences prefs;
    public MessageAdapter(Context mCtx, List<MessageAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public MessageAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.display_message, null);
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.HistoryViewHolder holder, int position) {
        MessageAdapterList product = productList.get(position);
        Glide.with(mCtx)
                .load(product.getImg())
                .transition(withCrossFade())
                .into(holder.img);

        holder.msg.setText(product.getMsg());
        holder.date.setText(product.getDate());
        if (product.getEmail().equals(holder.email)) {
            holder.name.setText("You");
        }
        else {
            holder.name.setText(product.getName());
            holder.card.setCardBackgroundColor(WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView msg, date, name;
        ImageView img;
        String email;
        CardView card;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            email = prefs.getString("username", "none");
            card = itemView.findViewById(R.id.card);
            msg = itemView.findViewById(R.id.msg);
            img = itemView.findViewById(R.id.img);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
        }
    }
}
