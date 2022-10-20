package com.example.handoutlms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatMessagingPage extends AppCompatActivity {

    ImageView back;
    TextView fullname;
    EditText edtMessage;
    ImageView send;
//    ListView listView;

    String key, id, email, name, institution, senderKey;

    ArrayList<String> arr_message = new ArrayList<>();
    ArrayList<String> arr_sender = new ArrayList<>();
    ArrayList<String> arr_receiver = new ArrayList<>();

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging_page);

        Intent i = getIntent();
        key = i.getStringExtra("key");
        id = i.getStringExtra("id");
        email = i.getStringExtra("email");
        name = i.getStringExtra("name");
        institution = i.getStringExtra("institution");
        senderKey = i.getStringExtra("senderKey");


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        back = findViewById(R.id.back);
        fullname = findViewById(R.id.fullname);
        edtMessage = findViewById(R.id.getMessage);
        send = findViewById(R.id.sendMessage);
//        listView = findViewById(R.id.myList);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        readMessages(senderKey, key);


        //immediately load the messages into the listview
//        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Chats");
//        reference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
////                    System.out.println("Datasnapshot = "+snapshot);
//
//                    Chat chat = snapshot.getValue(Chat.class);
//
//                    assert chat != null;
//                    String message = chat.getMessage();
//                    String receiver = chat.getReceiver();
//                    String sender = chat.getSender();
//
//                    if(receiver.equals(key)){
//                        arr_message.add(message);
//                        arr_sender.add(sender);
//                        arr_receiver.add(receiver);
//                    }
//
////                    System.out.println("Message = "+message+" Sender = "+sender+" Receiver = "+receiver);
//
//                }
//
//                ChatAdapter chatAdapter = new ChatAdapter(getApplicationContext(), arr_message, arr_sender, arr_receiver, key);
////                listView.setAdapter(chatAdapter);
////                chatAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        //clear array
//        arr_sender.clear();
//        arr_message.clear();
//        arr_receiver.clear();

    }

    private void sendMessage(String sender, String receiver, String message){
        //send the message to the Firebase DB chat
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chats").push().setValue(hashMap);



        //immediately load the messages into the listview
//        DatabaseReference reference2 = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Chats");
//        reference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    Chat chat = snapshot.getValue(Chat.class);
//
//                    assert chat != null;
//                    String message = chat.getMessage();
//                    String receiver = chat.getReceiver();
//                    String sender = chat.getSender();
//
//                    if(receiver.equals(key)){
//                        arr_message.add(message);
//                        arr_sender.add(sender);
//                        arr_receiver.add(receiver);
//                    }
//
//                }
//
//                ChatAdapter chatAdapter = new ChatAdapter(getApplicationContext(), arr_message, arr_sender, arr_receiver, key);
////                listView.setAdapter(chatAdapter);
////                chatAdapter.notifyDataSetChanged();
//
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        //clear array
//        arr_sender.clear();
//        arr_message.clear();
//        arr_receiver.clear();


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
}