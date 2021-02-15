package com.example.rentalapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private BottomSheetBehavior bottomSheetBehavior;
    FirebaseAuth fAuth;
    String userID, searchview;
    FirebaseFirestore db;
    SearchView searchView;
    Boolean isClicked = false;
    FirestoreRecyclerOptions<Model> options;
    private GoogleMap mMap;
    MapView mapView;
    String address;
    ArrayList<String> markersArray = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        searchView = v.findViewById(R.id.search_view_home);

        mapView = (MapView) v.findViewById(R.id.mapViewHomeFragment);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        firebaseFirestore = FirebaseFirestore.getInstance();
        LinearLayout linearLayout = v.findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        recyclerView = v.findViewById(R.id.recyclerViewBottomSheet);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchview = searchView.getQuery().toString();
                onStart();
            }
        });


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for(int i = 0 ; i < markersArray.size() ; i++) {

            address= markersArray.get(i);
            mMap = googleMap;
            List<Address> addressList = null;
            if (address != null || !address.equals("")) {
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address1 = addressList.get(i);
                LatLng latLng = new LatLng(address1.getLatitude(), address1.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
            }
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView Price;
        TextView Title;
        TextView Description;
        ImageView Image;
        TextView Place;
        Button Favor,message;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Price = itemView.findViewById(R.id.txt_tenant_price);
            Image = itemView.findViewById(R.id.Img_tenant_apartment);
            Place = itemView.findViewById(R.id.txt_tenant_place);
            Title = itemView.findViewById(R.id.txt_tenant_title);
            Description=itemView.findViewById(R.id.txt_tenant_description);
            Favor = itemView.findViewById(R.id.btn_tenant_favor2);
            message = itemView.findViewById(R.id.btn_tenant_message2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(searchview == null){
        Query query = firebaseFirestore.collection("Apartments");
        options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query,Model.class)
                .build();}
        else
            {
                Query query = firebaseFirestore.collection("Apartments").whereEqualTo("place", searchview);
                options = new FirestoreRecyclerOptions.Builder<Model>()
                        .setQuery(query,Model.class)
                        .build();
            }
        adapter = new FirestoreRecyclerAdapter<Model,ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tenant_recyclerviewlist,parent,false);
                return new ItemViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Model model) {
                Picasso.get().load(model.getImage()).into(holder.Image);
                holder.Title.setText(model.getName());
                holder.Price.setText(model.getPrice());
                holder.Description.setText(model.getDescription());

                markersArray.add(model.getName()+" "+model.getPlace());

                holder.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });


                holder.Favor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("Favorites").document(userID).collection("Selected").
                                document(model.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot result = task.getResult();
                                    if(!result.exists()){
                                        holder.Favor.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                                        Map<String, Object> doc = new HashMap<>();
                                        doc.put("id", model.getId());
                                        doc.put("email", model.getEmail());
                                        doc.put("name", model.getName());
                                        doc.put("price", model.getPrice());
                                        doc.put("place", model.getPlace());
                                        doc.put("description", model.getDescription());
                                        doc.put("userid", userID);
                                        doc.put("image", model.getImage());

                                        db.collection("Favorites").document(userID).collection("Selected").
                                                document(model.getId()).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else{
                                        holder.Favor.setBackgroundResource(R.drawable.ic_baseline_favorite_shadow_24);
                                        DocumentReference eRef = db.collection("Favorites").document(userID).
                                                collection("Selected")
                                                .document(model.getId());
                                        eRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getContext(), "removed", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });


                    }
                });

                holder.Image.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),TenantApartmentDetails.class);
                        intent.putExtra("Image",model.getImage());
                        intent.putExtra("Price",model.getPrice());
                        intent.putExtra("Title",model.getName());
                        intent.putExtra("UserID",userID);
                        intent.putExtra("Description",model.getDescription());
                        startActivity(intent);
                    }
                });

            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(adapter);


        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}