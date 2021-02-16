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



        return v;
    }

    private void OpenGallery() {
    }

}