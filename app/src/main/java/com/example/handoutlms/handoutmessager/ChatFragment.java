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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handoutlms.Chat;
import com.example.handoutlms.ChatMessagingPage;
import com.example.handoutlms.MessageAdapter;
import com.example.handoutlms.R;
import com.example.handoutlms.Users;
import com.example.handoutlms.UsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_email = preferences.getString("email", "not available");

        listView = v.findViewById(R.id.listview);
//        recyclerView = v.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //get the sender ID
        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println("Datasnapshot = "+snapshot);

                    Users users = snapshot.getValue(Users.class);
                    String keys = snapshot.getKey();

                    String email2 = users.getEmail();

//                    System.out.println("Key "+fullname2+" = "+keys);

                    if(got_email.equals(email2)){
                        senderKey = keys;
                    }else{
//                        arr_key.add(keys);
//                        arr_id.add(id2);
//                        arr_name.add(fullname2);
//                        arr_email.add(email2);
//                        arr_institution.add(institution2);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

//                Toast.makeText(getContext(), "userList = "+usersList, Toast.LENGTH_SHORT).show();
//                System.out.println("UserList = "+usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return v;
    }

    private void readChats() {

//        int len = usersList.size();
//        Toast.makeText(getContext(), "userList Length = "+usersList.size(), Toast.LENGTH_SHORT).show();
//        System.out.println("UserList Length = "+usersList.size());



        DatabaseReference reference = FirebaseDatabase.getInstance("https://handout-lms-default-rtdb.firebaseio.com/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //display 1 user from chat
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Users users = snapshot.getValue(Users.class);
                    keys = snapshot.getKey();

                    assert users != null;
//                    assert firebaseUser != null;
                    String id2 = users.getId();
                    String fullname2 = users.getFullname();
                    String email2 = users.getEmail();
                    String institution2 = users.institution;



                    if(keys.equals(senderKey)){
                        //do not add
                    }else {
                        for(int i = 0; i<usersList.size(); i++){
                            if(usersList.contains(keys)){
                                //add to array
                                arr_key.add(keys);
                                arr_id.add(id2);
                                arr_name.add(fullname2);
                                arr_email.add(email2);
                                arr_institution.add(institution2);
                            }else{
                                //do not add to array
                            }
                        }
                    }


                }

//                for (int i =0; i<usersList.size(); i++){
//                    for(int j = 0; j<arr_key.size(); j++){
//                        if(usersList.get(i).equals(arr_key.get(i))){
//
//                        }
//                    }
//                }

                for(int i = 0; i<usersList.size(); i++){
                    if(usersList.contains(arr_key.get(i))){
                        //add to array2
                        arr_key2.add(arr_key.get(i));
                        arr_id2.add(arr_id.get(i));
                        arr_name2.add(arr_name.get(i));
                        arr_email2.add(arr_email.get(i));
                        arr_institution2.add(arr_institution.get(i));
                    }else{
                        //do not add to array
                    }


                }

                Toast.makeText(getContext(), "userlist = "+usersList, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "passed = "+arr_name2, Toast.LENGTH_SHORT).show();

                UsersAdapter usersAdapter = new UsersAdapter(getActivity(), arr_key2, arr_id2, arr_name2, arr_email2, arr_institution2, senderKey);
                listView.setAdapter(usersAdapter);

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
}