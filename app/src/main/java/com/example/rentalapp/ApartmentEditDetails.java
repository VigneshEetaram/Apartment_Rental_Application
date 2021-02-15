package com.example.rentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ApartmentEditDetails extends AppCompatActivity {
    private TextView Price,Description,Title,Place;
    ImageView Image, edit;
    Button btn_save;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_edit_details);

        Image = (ImageView) findViewById(R.id.img_apt_edit_details);
        edit = (ImageView) findViewById(R.id.img_edit);
        Price= (TextView) findViewById(R.id.txt_view_edit_price);
        Description= (TextView) findViewById(R.id.txt_view_edit_description);
        Title = (TextView) findViewById(R.id.txt_view_edit_title);
        Place = (TextView) findViewById(R.id.txt_view_edit_place);
        btn_save = (Button) findViewById(R.id.btn_apartment_edit_details);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getIntent();
        Picasso.get().load(intent.getStringExtra("Image")).into(Image);
        String id = intent.getExtras().getString("Id");
        String price = intent.getExtras().getString("Price");
        String title = intent.getExtras().getString("Title");
        String place = intent.getExtras().getString("Place");
        String description = intent.getExtras().getString("Description");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Apartments").document(id)
                        .update(
                                "title", Title.getText().toString(),
                                "price", Title.getText().toString(),
                                "place", Place.getText().toString(),
                                "description", Description.getText().toString()
                        );

                db.collection("Myads").document(userID).collection("Selected").document(id)
                        .update(
                                "title", Title.getText().toString(),
                                "price", Title.getText().toString(),
                                "place", Place.getText().toString(),
                                "description", Description.getText().toString()
                        );

                Price.setEnabled(false);
                Title.setEnabled(false);
                Place.setEnabled(false);
                Description.setEnabled(false);
                btn_save.setVisibility(View.INVISIBLE);
            }


        });


        Price.setText(price);
        Title.setText(title);
        Place.setText(place);
        Description.setText(description);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Price.setEnabled(true);
                Title.setEnabled(true);
                Place.setEnabled(true);
                Description.setEnabled(true);
                btn_save.setVisibility(View.VISIBLE);
            }
        });

    }
}