package com.example.owner.ecom;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.ecom.Model.cart;
import com.example.owner.ecom.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class orderDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView msg2;
    private RecyclerView.LayoutManager manager;
    private DatabaseReference reference;
    private String userID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_Admin_order_details);
        manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        msg2 = (TextView) findViewById(R.id.Orders);
        userID = getIntent().getStringExtra("phone");
        reference = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View")
                .child(userID)
                .child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions <cart> options =
                new FirebaseRecyclerOptions.Builder<cart>()
                .setQuery(reference, cart.class)
                .build();

        FirebaseRecyclerAdapter<cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull cart model) {
                        holder.txtname.setText(holder.txtname.getText() + " : " + model.getName());
                        holder.txtquantity.setText(  "Quantity : " + model.getNumber());
                        holder.txtprice.setText(holder.txtprice.getText() + " : " + model.getPrice());

                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_view, viewGroup, false);
                        CartViewHolder cartViewHolder =  new CartViewHolder(view);
                        return cartViewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
