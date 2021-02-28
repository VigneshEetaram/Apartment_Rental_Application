package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TenantApartmentDetails extends AppCompatActivity {
    private TextView Price,Description,Title,Place;
    ImageSlider Image;
    Button btn_showMap,share,report;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    int count;
    List<SlideModel> slideModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_apartment_details);

        Image = (ImageSlider) findViewById(R.id.img_apt_details_tenant);
        Price= (TextView) findViewById(R.id.txt_view_price_tenant);
        Description= (TextView) findViewById(R.id.txt_view_description_tenant);
        Title = (TextView) findViewById(R.id.txt_view_title_tenant);
        Place = (TextView) findViewById(R.id.txt_view_place_tenant);
        btn_showMap = (Button) findViewById(R.id.btn_tenant_apartment_details);
        share = (Button) findViewById(R.id.btn_share);
        report = (Button) findViewById(R.id.btn_report);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String price = intent.getExtras().getString("Price");
        String title = intent.getExtras().getString("Title");
        String place = intent.getExtras().getString("Place");
        String id = intent.getExtras().getString("DocumentID");
        String description = intent.getExtras().getString("Description");
        Price.setText(price);
        Title.setText(title);
        Place.setText(place);
        Description.setText(description);


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("ApartmentImages").
                document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                count = Integer.valueOf(documentSnapshot.getString("count"));
                for (int i=0; i<count;i++){
                    slideModels.add(new SlideModel(documentSnapshot.getString("image"+i)));
                }
                Image.setImageList(slideModels,true);

            }
        });



        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TenantApartmentDetails.this);
                builder.setTitle("Submit your report");

                final EditText input1 = new EditText(TenantApartmentDetails.this);
                input1.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input1);



                builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!input1.getText().toString().equals("")){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
                            String saveCurrentDate = currentDate.format(calendar.getTime());
                            Map<String, Object> doc = new HashMap<>();
                            doc.put("title", FirebaseAuth.getInstance().getCurrentUser().getUid()+title);
                            doc.put("date",saveCurrentDate);
                            doc.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            doc.put("tenantname","");
                            doc.put("renterid","");
                            doc.put("rentername","");
                            doc.put("description", input1.getText().toString());

                            db.collection("Userreports").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(doc)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(TenantApartmentDetails.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(TenantApartmentDetails.this, "ADDRESS YOUR REPORT", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



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