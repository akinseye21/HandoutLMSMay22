package com.example.handoutlms.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handoutlms.models.Chat;
import com.example.handoutlms.ChatNotification.Client;
import com.example.handoutlms.ChatNotification.Data;
import com.example.handoutlms.ChatNotification.MyResponse;
import com.example.handoutlms.ChatNotification.Sender;
import com.example.handoutlms.ChatNotification.Token;
import com.example.handoutlms.adapters.MessageAdapter;
import com.example.handoutlms.R;
import com.example.handoutlms.models.Users;
import com.example.handoutlms.handoutmessager.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessagingPage extends AppCompatActivity {

    ImageView back;
    TextView fullname, description;
    EditText edtMessage;
    ImageView send;
//    ListView listView;

    String key, id, email, name, institution, senderKey;

    ArrayList<String> arr_message = new ArrayList<>();
    ArrayList<String> arr_sender = new ArrayList<>();
    ArrayList<String> arr_receiver = new ArrayList<>();

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;

    APIService apiService;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        Intent i = getIntent();
        key = i.getStringExtra("key");
        id = i.getStringExtra("id");
        email = i.getStringExtra("email");
        name = i.getStringExtra("name");
        institution = i.getStringExtra("institution");
//        senderKey = i.getStringExtra("senderKey");
        senderKey = firebaseUser.getUid();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        back = findViewById(R.id.back);
        fullname = findViewById(R.id.fullname);
        edtMessage = findViewById(R.id.getMessage);
        description = findViewById(R.id.description);
        send = findViewById(R.id.sendMessage);
//        listView = findViewById(R.id.myList);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;

                String msg = edtMessage.getText().toString();
                if(!msg.equals("")){
                    //send to the DB
                    sendMessage(senderKey, key, msg);
                }else{
                    Toast.makeText(getApplicationContext(), "You can not send empty message", Toast.LENGTH_LONG).show();
                }
                edtMessage.setText("");
            }
        });

        fullname.setText(name);
        description.setText(institution);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        readMessages(senderKey, key);


    }

    private void sendMessage(String sender, String receiver, String message){
        //send the message to the Firebase DB chat
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chats").push().setValue(hashMap);


        final String msg = message;
        reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                if(notify) {
                    sendNotification(receiver, users.getFullname(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, String username, String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.logo, username+": "+message, "New Message", " ");
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(ChatMessagingPage.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(String myid, String userid){
        mChat = new ArrayList<>();

        DatabaseReference reference3 = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Chats");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(senderKey) && chat.getSender().equals(key) ||
                    chat.getReceiver().equals(key) && chat.getSender().equals(senderKey)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(ChatMessagingPage.this, mChat, senderKey);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        status("offline");
    }


}