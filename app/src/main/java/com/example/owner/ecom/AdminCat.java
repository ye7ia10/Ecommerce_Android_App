package com.example.owner.ecom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCat extends AppCompatActivity {

    private ImageView tshirt, Fdress, sport, sweater;
    private ImageView glass, bag, hat, shoes;
    private ImageView headphone, laptop, watch, mobile;
    private Button chkOrders, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cat);

        chkOrders = (Button) findViewById(R.id.checkOrders);
        logout = (Button) findViewById(R.id.LogoutAdmin);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(AdminCat.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        chkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(AdminCat.this, AdminOrders.class);
                startActivity(intent);

            }
        });

        tshirt = (ImageView) findViewById(R.id.tshirt);
        Fdress = (ImageView) findViewById(R.id.femaldress);
        sport = (ImageView) findViewById(R.id.sports);
        sweater = (ImageView) findViewById(R.id.sweater);

        glass = (ImageView) findViewById(R.id.glasses);
        bag = (ImageView) findViewById(R.id.bag);
        hat = (ImageView) findViewById(R.id.hat);
        shoes = (ImageView) findViewById(R.id.shoes);

        headphone = (ImageView) findViewById(R.id.headphone);
        laptop = (ImageView) findViewById(R.id.laptop);
        watch = (ImageView) findViewById(R.id.watch);
        mobile = (ImageView) findViewById(R.id.mobile);

        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Tshirts");
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Sports Tshirts");
                startActivity(intent);
            }
        });

        Fdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Female Dresses");
                startActivity(intent);
            }
        });


        sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Sweaters");
                startActivity(intent);
            }
        });

        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Glasses");
                startActivity(intent);
            }
        });

        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Bags");
                startActivity(intent);
            }
        });
        hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Hats");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Shoes");
                startActivity(intent);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Headphones");
                startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Laptops");
                startActivity(intent);
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Watches");
                startActivity(intent);
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCat.this, Addproduct.class);
                intent.putExtra("Category", "Mobile");
                startActivity(intent);
            }
        });



    }
}
