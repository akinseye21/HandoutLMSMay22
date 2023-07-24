package com.example.handoutlms.handoutmessager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.handoutlms.models.Chat;
import com.example.handoutlms.ChatNotification.Token;
import com.example.handoutlms.R;
import com.example.handoutlms.models.Users;
import com.example.handoutlms.adapters.UsersAdapter;
import com.example.handoutlms.adapters.UsersAdapter2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<Users> mUsers;
    private List<String> usersList;

    ArrayList<String> arr_key = new ArrayList<>();
    ArrayList<String> arr_id = new ArrayList<>();
    ArrayList<String> arr_name = new ArrayList<>();
    ArrayList<String> arr_email = new ArrayList<>();
    ArrayList<String> arr_institution = new ArrayList<>();

    ArrayList<String> arr_key2 = new ArrayList<>();
    ArrayList<String> arr_id2 = new ArrayList<>();
    ArrayList<String> arr_name2 = new ArrayList<>();
    ArrayList<String> arr_email2 = new ArrayList<>();
    ArrayList<String> arr_institution2 = new ArrayList<>();

    SharedPreferences preferences;
    String got_email;
    String senderKey;
    String keys;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        listView = v.findViewById(R.id.listview);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //get the sender ID
        senderKey = firebaseUser.getUid();


        usersList = new ArrayList<>();


        DatabaseReference reference3 = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Chats");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(senderKey)){
                        if(usersList.contains(chat.getReceiver())){
                            //do not add
                        }else{
                            usersList.add(chat.getReceiver());
                        }
                    }
                    if(chat.getSender().equals(senderKey)){
                        if(usersList.contains(chat.getSender())){
                            //do not add
                        }else{
                            usersList.add(chat.getSender());
                        }
                    }



                }
                readChats();

                System.out.println("UserList = "+usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


        return v;
    }


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }



    private void readChats() {

        mUsers = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                //display 1 user from chat
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);

                    for(String id: usersList){
                        if(users.getId().equals(id) && !users.getId().equals(firebaseUser.getUid())){
                            mUsers.add(users);

//                            if(mUsers.size() != 0 ){
//                                for(Users user1 : mUsers){
//                                    if(!users.getId().equals(user1.getId())){
//                                        mUsers.add(users);
//                                    }
//                                }
//                            }else{
//                                mUsers.add(users);
//                            }
                        }
                    }
                }

                UsersAdapter2 usersAdapter2 = new UsersAdapter2(getContext(), mUsers);
                recyclerView.setAdapter(usersAdapter2);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        arr_key2.clear();
        arr_id2.clear();
        arr_name2.clear();
        arr_email2.clear();
        arr_institution2.clear();
    }

    public interface OnFragmentInteractionListener {
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