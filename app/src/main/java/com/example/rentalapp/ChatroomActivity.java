package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class ChatroomActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    RelativeLayout relativeLayout;
    Toolbar toolbar;
    RecyclerView chatRecyclerView;
    MessageAdapter chatRecyclerAdapter;
    Button fab;
    EditText edtmsg;
    TextView textView,chatroomtitle;
    ListView listView;
    String tenantid,documentid,renter,chatid,chatroomname,rentername,tenantname,isuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        toolbar = findViewById(R.id.chatroom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = findViewById(R.id.fab1);
        edtmsg = findViewById(R.id.input1);
        chatRecyclerView = findViewById(R.id.list_of_messages1);
        relativeLayout = findViewById(R.id.activity_main1);
        textView = findViewById(R.id.txt_chatactivity_title);
        chatroomtitle = findViewById(R.id.toolbar_chatroom_title);
        Intent intent = getIntent();
        tenantid = intent.getExtras().getString("tenantid");
        documentid = intent.getExtras().getString("documentid");
        renter = intent.getExtras().getString("renter");
        chatid = intent.getExtras().getString("chatid");
        rentername = intent.getExtras().getString("rentername");
        tenantname = intent.getExtras().getString("tenantname");
        chatroomname = intent.getExtras().getString("chatroomname");
        isuser = intent.getExtras().getString("isuser");
        if(isuser.equals("2")){
            chatroomtitle.setText(rentername);
        }
        else if(isuser.equals("1")){
            chatroomtitle.setText(tenantname);
        }
        textView.setText(chatroomname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatroomActivity.this,TenantHomePage.class));
                finish();
            }
        });

        displayChatMessages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatMessage chat = new ChatMessage(edtmsg.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),renter,new Date().getTime());



                db.collection("Chatroom").document(chatid).collection("chats")
                        .add(chat)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
                                edtmsg.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FAILED", "Error adding document", e);
                            }
                        });
            }
        });

    }

    private void displayChatMessages() {

        Query query = FirebaseFirestore.getInstance().collection("Chatroom").document(chatid)
                .collection("chats")
                .orderBy("messageTime")
                .limit(20);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<ChatMessage> chats = snapshot.toObjects(ChatMessage.class);

                // Update UI
                chatRecyclerView.setHasFixedSize(true);
                chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                chatRecyclerAdapter = new MessageAdapter(ChatroomActivity.this,chats);
                chatRecyclerView.setAdapter(chatRecyclerAdapter);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}