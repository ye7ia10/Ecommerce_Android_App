package com.example.owner.ecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.owner.ecom.Model.Users;
import com.example.owner.ecom.prev.prevs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button join;
    private Button login;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        join = (Button) findViewById(R.id.main_sign);
        login = (Button) findViewById(R.id.main_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });

        progressBar = new ProgressDialog(this);
        Paper.init(this);
        String userphone = Paper.book().read(prevs.userPhoneKey);
        String userpass = Paper.book().read(prevs.userPassKey);

        //for remember me
        if (userphone != "" && userpass != ""){
            if (!TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(userpass)){

                progressBar.setTitle("logging into account..");
                progressBar.setMessage("please wait while logging the account");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();
                allowAccess(userphone, userpass);
            }
        }
    }


    /**
     * remember me
     * @param phone from Ppaer library
     * @param pass from Ppaer library
     * check if they are exist in database
     * pass it to the online user
     */
    public void allowAccess(final String phone, final String pass){
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){
                    Users userData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    Log.e("phone", userData.getName());
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(pass)){
                            Toast.makeText(MainActivity.this, "You are logged ... ", Toast.LENGTH_LONG).show();
                            progressBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            prevs.onlineUser = userData;
                            startActivity(intent);
                        } else {
                            progressBar.dismiss();
                        }
                    }

                }else {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
