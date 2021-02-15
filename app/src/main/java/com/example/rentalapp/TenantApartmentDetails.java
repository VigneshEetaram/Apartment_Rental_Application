package com.example.rentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

public class TenantApartmentDetails extends AppCompatActivity {
    private TextView Price,Description,Title,Place;
    ImageView Image;
    Button btn_showMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_apartment_details);

        Image = (ImageView) findViewById(R.id.img_apt_details_tenant);
        Price= (TextView) findViewById(R.id.txt_view_price_tenant);
        Description= (TextView) findViewById(R.id.txt_view_description_tenant);
        Title = (TextView) findViewById(R.id.txt_view_title_tenant);
        Place = (TextView) findViewById(R.id.txt_view_place_tenant);
        btn_showMap = (Button) findViewById(R.id.btn_tenant_apartment_details);

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

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenantApartmentDetails.this,MapsActivity.class);
                intent.putExtra("Address",title+" "+place);
                startActivity(intent);
            }
        });

    }
}