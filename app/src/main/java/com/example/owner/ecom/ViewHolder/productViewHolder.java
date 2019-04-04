package com.example.owner.ecom.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.ecom.Interfaces.ItemClickListner;
import com.example.owner.ecom.R;

public class productViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView product_name, product_desc, product_price;
    public ImageView product_image;
    public ItemClickListner itemClickListner;

    public productViewHolder(@NonNull View itemView) {
        super(itemView);

        product_name = (TextView) itemView.findViewById(R.id.product_name);
        product_desc = (TextView) itemView.findViewById(R.id.product_desc);
        product_image = (ImageView) itemView.findViewById(R.id.product_image);
        product_price = (TextView) itemView.findViewById(R.id.product_price);


    }

    public void setItemClickListner (ItemClickListner listner){
        this.itemClickListner = listner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }
}
