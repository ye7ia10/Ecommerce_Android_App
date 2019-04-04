package com.example.owner.ecom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.owner.ecom.Model.products;
import com.example.owner.ecom.prev.prevs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class productDetail extends AppCompatActivity {

    private FloatingActionButton cart;
    private TextView detailName, detailDes, detailPrice;
    private ElegantNumberButton numberButton;
    private ImageView detailImage;
    private String productID = "", state = "normal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        cart = (FloatingActionButton) findViewById(R.id.nav_btn);

        detailDes = (TextView)findViewById(R.id.product_detail_des);
        detailName = (TextView)findViewById(R.id.product_detail_name);
        detailPrice = (TextView)findViewById(R.id.product_detail_price);

        numberButton = (ElegantNumberButton) findViewById(R.id.numberbtn);

        detailImage = (ImageView) findViewById(R.id.product_detail_image);

        productID = getIntent().getStringExtra("pid");

        getProductDetails(productID);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state.equals("shipped") || state.equals("not shipped") ){
                    Toast.makeText(productDetail.this, "You Can purchase more " +
                            "products when your orders are shipped or verified", Toast.LENGTH_LONG).show();
                } else {
                    savetocart();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOreders();
    }

    private void savetocart() {
        String CurrentDate ="";
        String CurrentTime = "";
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat Date = new SimpleDateFormat("MMM dd,yyyy");
        CurrentDate = Date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = time.format(calendar.getTime());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id", productID);
        map.put("name", detailName.getText().toString());
        map.put("price", detailPrice.getText().toString());
        map.put("description", detailDes.getText().toString());
        map.put("date", CurrentDate);
        map.put("time", CurrentTime);
        map.put("discount", "");
        map.put("number", numberButton.getNumber().toString());

        databaseReference.child("User View").child(prevs.onlineUser.getPhone()).child("Products")
                .child(productID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    databaseReference.child("Admin View").child(prevs.onlineUser.getPhone()).child("Products")
                            .child(productID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(productDetail.this, "product passed to cart", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(productDetail.this, Home.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });


    }

    private void getProductDetails(final String productID) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        databaseReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    products product = dataSnapshot.getValue(products.class);
                    detailName.setText(product.getName());
                    detailPrice.setText(product.getPrice());
                    detailDes.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(detailImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void checkOreders (){
        final  DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevs.onlineUser.getPhone());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState  = dataSnapshot.child("state").getValue().toString();


                    if (shippingState.equals("shipped")){
                        state = "shipped";

                    } else if (shippingState.equals("not shipped")){
                        state = "not shipped";

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
