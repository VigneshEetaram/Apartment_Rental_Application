package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApartmentEditDetails extends AppCompatActivity {
    private TextView Price,Description,Title,Place;
    ImageView edit;
    ImageSlider image;
    Button btn_save;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userID;
    int count;
    List<SlideModel> slideModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_edit_details);

        image = (ImageSlider) findViewById(R.id.img_apt_edit_details);
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

        String id = intent.getExtras().getString("Id");

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("ApartmentImages").
                document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                count = Integer.valueOf(documentSnapshot.getString("count"));
                for (int i=0; i<count;i++){
                    slideModels.add(new SlideModel(documentSnapshot.getString("image"+i)));
                }
                image.setImageList(slideModels,true);
                image.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int position) {
                        // You can listen here.
                        for (int j=0;j<count;j++){
                            if (position == j) {
                                new AlertDialog.Builder(ApartmentEditDetails.this).setMessage("Do you want to delete the iamge")
                                        .setTitle("Delete")
                                        .setCancelable(true)
                                        .setPositiveButton(android.R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        DocumentReference docRef = db.collection("ApartmentImages")
                                                                .document(id);
                                                        Map<String,Object> updates = new HashMap<>();
                                                        updates.put("image"+position, FieldValue.delete());

                                                        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(ApartmentEditDetails.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                                count = count-1;
                                                                DocumentReference docRef2 = db.collection("ApartmentImages")
                                                                        .document(id);
                                                                docRef2.update("count", String.valueOf(count)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        DocumentReference docRef3 = db.collection("Apartments")
                                                                                .document(id);
                                                                        docRef3.update("count", String.valueOf(count)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                onRestart();
                                                                            }
                                                                        });
                                                                    }
                                                                });

                                                            }
                                                        });
                                                    }
                                                }).setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                    }

                    }
                });
            }
        });




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