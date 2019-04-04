package com.example.owner.ecom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.ecom.Model.cart;
import com.example.owner.ecom.ViewHolder.CartViewHolder;
import com.example.owner.ecom.prev.prevs;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity {

    private TextView total_price , msg1;
    private Button nextPro;
    private RecyclerView.LayoutManager manager;
    private RecyclerView recyclerView;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        total_price = (TextView) findViewById(R.id.total_pr);
        msg1 = (TextView) findViewById(R.id.msg1);


        nextPro = (Button) findViewById(R.id.next_product);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_Cart);

        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        nextPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this, FinalOrder.class);
                intent.putExtra("total price", String.valueOf(totalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOreders();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final FirebaseRecyclerOptions<cart> options = new FirebaseRecyclerOptions.Builder<cart>()
                .setQuery(databaseReference.child("User View")
                .child(prevs.onlineUser.getPhone()).child("Products"), cart.class).build();

        FirebaseRecyclerAdapter<cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final cart model) {
                holder.txtname.setText(holder.txtname.getText() + " : " + model.getName());
                holder.txtquantity.setText(  "Quantity : " + model.getNumber());
                holder.txtprice.setText(holder.txtprice.getText() + " : " + model.getPrice());

                StringBuilder x =  new StringBuilder();
                for (int i = 0; i < model.getPrice().length(); i++){
                    if (Character.isDigit(model.getPrice().charAt(i))){
                        x.append(model.getPrice().charAt(i));
                    }
                }

                int temp = (Integer.valueOf(model.getNumber())) * (Integer.valueOf(x.toString()));
                totalPrice += temp;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence [] sequences = new CharSequence[]
                                { "Edit", "Remove"};

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
                        alertDialog.setTitle("Cart Options :");
                        alertDialog.setItems(sequences, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    Intent intent = new Intent(Cart.this, productDetail.class);
                                    intent.putExtra("pid", model.getId());
                                    startActivity(intent);
                                } else if ( i == 1) {
                                    //remove item from database
                                    databaseReference.child("User View").child(prevs.onlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getId())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    databaseReference.child("Admin View").child(prevs.onlineUser.getPhone())
                                                            .child("Products")
                                                            .child(model.getId())
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        Toast.makeText(Cart.this, "Item Removed", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(Cart.this, Home.class);
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }
                        });

                        alertDialog.show();
                    }
                });
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

    public void checkOreders (){
        final  DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevs.onlineUser.getPhone());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState  = dataSnapshot.child("state").getValue().toString();
                    String username  = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){
                        total_price.setText("Dear "+ username + "\n Your order is shipped you will receive it soon.");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Your Order has been shipped , Soon you will receive it at your address");
                        nextPro.setVisibility(View.GONE);
                        Toast.makeText(Cart.this, "You Can purchase more " +
                                "products when your is shipped or verified", Toast.LENGTH_LONG).show();
                    } else if (shippingState.equals("not shipped")){
                        total_price.setText("Shipping State = not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        nextPro.setVisibility(View.GONE);
                        Toast.makeText(Cart.this, "You Can purchase more " +
                                "products when your orders are shipped or verified", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
