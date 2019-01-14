package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rog.umk.Admin.viewEvent;
import com.example.rog.umk.Product.Product;
import com.example.rog.umk.Product.ProductAdapterList;
import com.example.rog.umk.Product.productDetail;
import com.example.rog.umk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter {
    List<ProductAdapterList> mProducts;
    Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int PRODUCT_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;
    String tag;
    public ProductAdapter(Context mContext, String tag) {
        mProducts = new ArrayList<>();
        this.mContext = mContext;
        this.tag = tag;
        System.out.println("constructor");
    }
    //method to add products as soon as they fetched
    public void addProducts(List<ProductAdapterList> products) {
        int lastPos = mProducts.size();
        this.mProducts.addAll(products);
        notifyItemRangeInserted(lastPos, mProducts.size());
    }

    @Override
    public int getItemViewType(int position) {
        ProductAdapterList currentProduct = mProducts.get(position);
        if (currentProduct.isLoading()) {
            System.out.println("loading_item");
            return LOADING_ITEM;
        } else {
            System.out.println("product_item");
            return PRODUCT_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == PRODUCT_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_product, parent, false);
            return new ProductHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        final ProductAdapterList currentProduct = mProducts.get(position);
        System.out.println("tag in adapter" + tag);
        if (holder instanceof ProductHolder) {
            ProductHolder productHolder = (ProductHolder) holder;
            //bind products information with view
            Picasso.with(mContext).load(currentProduct.getImage()).into(productHolder.imageViewProductThumb);
            productHolder.textViewProductName.setText(currentProduct.getName());
            if (tag.equals("event"))
                productHolder.textViewProductPrice.setText(currentProduct.getPrice());
            else
                productHolder.textViewProductPrice.setText("RM"+currentProduct.getPrice());

            productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = currentProduct.getId();
                    Intent intent;
                    // user selected product now you can show details of that product
                    //Toast.makeText(mContext, "Selected "+currentProduct.getName(), Toast.LENGTH_SHORT).show();
                    System.out.println("onClick position: " + currentProduct.getName()+ "Id is: " + id);
                    if (tag.equals("event")) {
                        intent = new Intent(mContext, viewEvent.class);
                        intent.putExtra("id", id);
                        intent.putExtra("tag", "order");
                        mContext.startActivity(intent);
                    }
                    else{
                        intent = new Intent(mContext, productDetail.class);
                        intent.putExtra("id", id);
                        intent.putExtra("tag", "order");
                        mContext.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    //Holds view of product with information
    private class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProductThumb;
        TextView textViewProductName, textViewProductPrice;


        public ProductHolder(View itemView) {
            super(itemView);
            imageViewProductThumb = itemView.findViewById(R.id.imageViewProductThumb);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);

        }
    }
    //holds view of loading item
    private class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    //method to show loading
    public void showLoading() {
        ProductAdapterList product = new ProductAdapterList();
        product.setLoading(true);
        mProducts.add(product);
        LoadingItemPos = mProducts.size();
        notifyItemInserted(mProducts.size());
        System.out.println("mproduct size" + mProducts.size());
        System.out.println("showloading");
        loading = true;
    }
    //method to hide loading
    public void hideLoading() {
        ProductAdapterList product = new ProductAdapterList();
        if (LoadingItemPos <= mProducts.size()) {
            mProducts.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }
    }

}
