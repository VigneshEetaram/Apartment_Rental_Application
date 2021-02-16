package com.example.rentalapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class SecondFragment extends Fragment {

    private EditText name,price,place,description;
    private ImageView mImage;
    private Button btnSubmit;
    private String email;
    private ProgressDialog pd;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private Uri imageUri;
    private ImageView imageView;
    private String userID;
    private ArrayList<Uri> ImageUris;
    private String aname, aprice, aplace ,adescription;
    private static final int gallerypic = 1;
    private static final int PICK_IMAGES_CODE = 0;
    private int position =0;
    private StorageReference AptImagesRef;
    private String[] downloadimageurls = new String[10];
    private String productRandomKey, saveCurrentDate, saveCurrentTime, downloadImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);

        name = v.findViewById(R.id.edittxt_name);
        price = v.findViewById(R.id.edittxt_price);
        place = v.findViewById(R.id.edittxt_place);
        description  = v.findViewById(R.id.edittxt_description);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        imageView = (ImageView) v.findViewById(R.id.imgSelect);
        ImageUris = new ArrayList<>();
        AptImagesRef = FirebaseStorage.getInstance().getReference().child("Apartment Images");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        btnSubmit = (Button) v.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aname = name.getText().toString().trim();
                aprice = price.getText().toString().trim();
                aplace = place.getText().toString().trim();
                adescription = description.getText().toString().trim();

                if(ImageUris == null){
                    Toast.makeText(getContext(), "Image is mandatory.", Toast.LENGTH_SHORT).show();
                    imageView.requestFocus();
                }

                uploadImage();

            }
        });


        return v;
    }

    private void uploadImage() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        for(int i = 0; i<ImageUris.size(); i++){   final StorageReference filePath = AptImagesRef.child(ImageUris.get(i).getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask = filePath.putFile(ImageUris.get(i));
            int finalI = i;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadimageurls[finalI]= filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                downloadimageurls[finalI] = task.getResult().toString();

                                Toast.makeText(getContext(), "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                                uploadData(aname, aprice, aplace, adescription);

                            }
                        }
                    });
                }
            });}

        }

    private void uploadData(String aname, String aprice, String aplace, String adescription) {
    }

    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select images"), gallerypic);
    }

}