package com.example.owner.ecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class Addproduct extends AppCompatActivity {
    private String price, naming, desc, cate, CurrentDate, CurrentTime;
    private String Categoryname, downloadURL; // download url to make url for the image to be shown later for users
    private Button add;
    private EditText pName, pPrice, pDes;
    private ImageView pImage;
    private Uri image; //to load image from gallery
    private static final int REQUEST = 1;
    private String productKey;
    private StorageReference storageReference; // for the storage of fire base to save
    private DatabaseReference databaseReference; // for data of fire base
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);

        Categoryname = getIntent().getExtras().get("Category").toString();

        //initialize fire base tools
        storageReference = FirebaseStorage.getInstance().getReference().child("Product");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");


        Toast.makeText(this, Categoryname, Toast.LENGTH_SHORT).show();

        add = (Button) findViewById(R.id.Add_pro);
        pName = (EditText) findViewById(R.id.productText);
        pDes = (EditText) findViewById(R.id.product_des);
        pPrice = (EditText) findViewById(R.id.product_price);
        pImage = (ImageView) findViewById(R.id.select);


        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInfo();
            }
        });

        progressBar = new ProgressDialog(this);


    }

    /**
     * validate data of edit texts
     * check if they are null
     * call the save info method
     */
    public void validateInfo() {
        naming = pName.getText().toString();
        desc = pDes.getText().toString();
        price = pPrice.getText().toString();
        cate = Categoryname;
        if (image == null){
            Toast.makeText(this, "Photo is null....", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(naming)){
            Toast.makeText(this, "name is null....", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(desc)){
            Toast.makeText(this, "describe is null....", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(naming)){
            Toast.makeText(this, "price is null....", Toast.LENGTH_LONG).show();
        } else {
            saveInfo();
        }

    }

    /**
     * saving the data into the storage of fire base
     * save time and data and photo and photo path
     * combine them together to make a unique key for each product
     */
    private void saveInfo() {
        progressBar.setTitle("saving product..");
        progressBar.setMessage("please wait while saving product..");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat Date = new SimpleDateFormat("MMM dd,yyyy");
        CurrentDate = Date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = time.format(calendar.getTime());

        productKey =  CurrentDate +  CurrentTime; //date and time key

        //saving to the storage
        final StorageReference filePath = storageReference.child(image.getPathSegments() + productKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(image);

        //incas of failure
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
                progressBar.dismiss();
                String msg = e.toString();
                Toast.makeText(Addproduct.this, "ERROR : " + e, Toast.LENGTH_LONG).show();

            } // incas of success
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Addproduct.this, "photo uploaded successfully " , Toast.LENGTH_LONG).show();
                Task <Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadURL = filePath.getDownloadUrl().toString(); //make the url of the image
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadURL= task.getResult().toString(); //make the url of the image
                            progressBar.dismiss();
                            Toast.makeText(Addproduct.this, "got uploaded successfully " , Toast.LENGTH_LONG).show();
                            savetoDatabase();
                        }else {
                            progressBar.dismiss();
                            Toast.makeText(Addproduct.this, "got uploaded insuccessfully " , Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });




    }

    /**
     * saving the data int the data base of the fire base
     */

    private void savetoDatabase() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pid", productKey);
        map.put("date", CurrentDate);
        map.put("time", CurrentTime);
        map.put("description", desc);
        map.put("image", downloadURL);
        map.put("name", naming);
        map.put("price", price);
        map.put("category", Categoryname);

        databaseReference.child(productKey).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.dismiss();
                            Toast.makeText(Addproduct.this, "product uploaded successfully " , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Addproduct.this, AdminCat.class);
                            startActivity(intent);

                        }else {
                            progressBar.dismiss();
                            Toast.makeText(Addproduct.this, "got uploaded insuccessfully " , Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    /**
     * choose photo from gallery
     * and put it into image view by its uri
     */
    public void openGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (requestCode==REQUEST  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            image = data.getData();
            pImage.setImageURI(image);
        }


}
}
