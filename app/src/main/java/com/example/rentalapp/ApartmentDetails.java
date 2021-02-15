package com.example.rentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class ApartmentDetails extends AppCompatActivity {
    private TextView Price,Description,Title,Place;
    ImageView Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        Image = (ImageView) findViewById(R.id.img_apt_details);
        Price= (TextView) findViewById(R.id.txt_view_price);
        Description= (TextView) findViewById(R.id.txt_view_description);
        Title = (TextView) findViewById(R.id.txt_view_title);
        Place = (TextView) findViewById(R.id.txt_view_place);

        Intent intent = getIntent();
        Picasso.get().load(intent.getStringExtra("Image")).into(Image);
        String price = intent.getExtras().getString("Price");
        String title = intent.getExtras().getString("Title");
        String place = intent.getExtras().getString("Place");
        String description = intent.getExtras().getString("Description");
        Price.setText(price);
        Title.setText(title);
        Place.setText(place);
        Description.setText(description);
    }
}